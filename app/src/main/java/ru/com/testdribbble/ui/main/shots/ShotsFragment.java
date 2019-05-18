package ru.com.testdribbble.ui.main.shots;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import javax.inject.Inject;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import ru.com.testdribbble.R;
import ru.com.testdribbble.TheApplication;
import ru.com.testdribbble.core.LocalUserProvider;
import ru.com.testdribbble.core.data.model.Shot;
import ru.com.testdribbble.core.data.model.User;
import ru.com.testdribbble.core.utils.Constants;
import ru.com.testdribbble.mvp.main.shots.ShotsFragmentContract;
import ru.com.testdribbble.mvp.main.shots.ShotsFragmentPresenter;
import ru.com.testdribbble.ui.BaseMainFragment;
import ru.com.testdribbble.ui.adapters.ShotsAdapter;
import ru.com.testdribbble.ui.common.BackButtonListener;
import ru.com.testdribbble.ui.common.RouterProvider;
import ru.terrakok.cicerone.Router;

@EFragment(R.layout.fragment_shots)
public class ShotsFragment extends BaseMainFragment implements
        ShotsFragmentContract.ShotsFragmentView,
        BackButtonListener,
        IOnViewInItemClick {

    private static final String TAG = "ShotsFragment";

//Set screen views

    //Set header views

    @ViewById
    AppBarLayout ablHeader;

    @ViewById
    TextView txtHeader;

    @ViewById
    AppCompatImageView imgLeftButton;

    //Set main views

    @ViewById
    SwipeRefreshLayout srlRefresh;

    @ViewById
    ProgressBar progressBar;

    @ViewById
    RecyclerView rvShots;

    @ViewById
    TextView txtNoShots;

    @ViewById
    SmoothProgressBar progressBottom;

//Set app navigation injections and it's management

    @Inject
    Router router;

//Set Bundles and Beans

    @Bean(ShotsFragmentPresenter.class)
    ShotsFragmentContract.ShotsFragmentPresenter presenter;

    @App
    TheApplication application;

    @Bean
    LocalUserProvider userProvider;

//Set Local variables

    User user;

    private boolean isLoadingData;
    private ShotsAdapter adapter;
    int ablHeaderHeight;
    int lastVisibleElement;

    private int page = -1;

    private MaterialDialog logoutDialog;
    private MaterialDialog noShotsDialog;

    private long mLastClickTime = 0;

    private IOnViewInItemClick onViewInItemClick;

//Main methods

    @Override
    public void onCreate(Bundle savedInstanceState) {
        application.getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    public void afterViews() {
        presenter.setView(this);
        if ((getParentFragment()) != null) {
            presenter.setRouter(((RouterProvider) getParentFragment()).getRouter());
        } else {
            presenter.setRouter(router);
        }

        onViewInItemClick = this;

        imgLeftButton.setImageDrawable(getResources().getDrawable(R.drawable.log_out));
        txtHeader.setText(getString(R.string.app_name));

        if(ablHeaderHeight > 0) {
            setAdapterViews(ablHeaderHeight);
            setRefresh();
        } else {
            setListItemHeight();
        }
    }

    @Override
    public void showLoading() {
        isLoadingData = true;
        if (txtNoShots != null) txtNoShots.setVisibility(View.GONE);
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            if (isLoadingData) {
                if (adapter == null || adapter.isEmpty()) {
                    if (progressBar != null) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (progressBottom != null) {
                        progressBottom.setVisibility(View.VISIBLE);
                    }
                }
            }
        }, 300);
    }

    @Override
    public void hideLoading() {
        isLoadingData = false;
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
            rvShots.setVisibility(View.VISIBLE);
        }
        if (progressBottom != null) {
            progressBottom.setVisibility(View.GONE);
            rvShots.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setShots(int page, List<Shot> shots, boolean isLoadedFromDb) {
        this.page = page;
        if (isLoadedFromDb) {
            removeScrollListener();
            adapter.addData(shots);
        } else if (shots.isEmpty() && adapter.getItemCount() != 0) {
            removeScrollListener();
        } else {
            if (shots.isEmpty() && adapter.getItemCount() == 0) {
                removeScrollListener();
                if (txtNoShots != null) txtNoShots.setVisibility(View.VISIBLE);
                showNoShotsDialog();
            } else {
                adapter.addData(shots);
                this.page++;
            }
        }
    }

    @Override
    public void setUserInfo(User user) {
        this.user = user;
        setListViews();
    }

    @Override
    public void showServerError(Throwable throwable) {
        getMainActivity().showServerError(throwable);
    }

    @Override
    public void showDataBaseError(Throwable throwable) {
        getMainActivity().showDbError(throwable);
    }

    @Override
    public void onAuthorNameClick(int itemPosition) {
        lastVisibleElement = itemPosition;
        presenter.goToProfileScreen();
    }

    @Override
    public void removeScrollListener() {
        rvShots.clearOnScrollListeners();
    }

    @Override
    public boolean onBackPressed() {
        presenter.onBackPressed();
        return true;
    }

    //Manage all views clicks

    @Click(R.id.imgLeftButton)
    void onLogoutClick() {
        if (isDoubleClick(mLastClickTime)) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        showLogOutDialog();
    }

//Internal methods

    private void setListItemHeight() {
        ablHeader.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ablHeader.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                ablHeaderHeight = ablHeader.getHeight();
                presenter.loadUserInfo();
            }
        });
    }

    private void setListViews() {
        setAdapterViews(ablHeaderHeight);
        setRefresh();
        loadShots(true);
    }

    private void setAdapterViews(int height) {
        if (adapter == null) {
            adapter = new ShotsAdapter(
                    getMainActivity(),
                    (getMainActivity().getScreenHeight() - (height * 3 / 2)) / 2,
                    user != null ? user.getName() : "",
                    onViewInItemClick);
        }

        rvShots.setLayoutManager(new LinearLayoutManager(getMainActivity()));
        rvShots.setAdapter(adapter);

        if (adapter != null && rvShots.getLayoutManager() != null) {
            rvShots.getLayoutManager().scrollToPosition(lastVisibleElement);
        }

        setScrollListener();

        adapter.setRecyclerTouchListener((model, position) -> {
            if (isDoubleClick(mLastClickTime)) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();

        });
    }

    private void setScrollListener() {
        rvShots.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!isLoadingData) {
                    loadShots(false);
                }
            }
        });
    }

    private void setRefresh() {
        srlRefresh.setColorSchemeResources(R.color.colorRoseLight);

        srlRefresh.setOnRefreshListener(() -> {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (srlRefresh != null) {
                        srlRefresh.setRefreshing(false);
                        page = 1;
                        adapter.clear();
                        presenter.refresh();
                        setScrollListener();
                        loadShots(false);
                    }
                }
            }, 500);
        });
    }

    private void showLogOutDialog() {
        logoutDialog = new MaterialDialog.Builder(getMainActivity())
                .customView(R.layout.dialog_question_yes_no, false)
                .build();
        TextView txtQuestion = (TextView) logoutDialog.findViewById(R.id.txtQuestion);
        MaterialButton btnNo = (MaterialButton) logoutDialog.findViewById(R.id.btnNo);
        MaterialButton btnYes = (MaterialButton) logoutDialog.findViewById(R.id.btnYes);

        txtQuestion.setText(getString(R.string.are_you_sure_you_want_to_log_out));
        btnNo.setText(getString(R.string.no));
        btnYes.setText(getString(R.string.yes));

        logoutDialog.show();

        btnNo.setOnClickListener(v -> logoutDialog.dismiss());
        btnYes.setOnClickListener(v -> {
            logoutDialog.dismiss();
            presenter.logOut();
        });
    }

    private void showNoShotsDialog() {
        noShotsDialog = new MaterialDialog.Builder(getMainActivity())
                .customView(R.layout.dialog_question_yes_no, false)
                .build();
        TextView txtQuestion = (TextView) noShotsDialog.findViewById(R.id.txtQuestion);
        MaterialButton btnCancel = (MaterialButton) noShotsDialog.findViewById(R.id.btnNo);
        MaterialButton btnUpload = (MaterialButton) noShotsDialog.findViewById(R.id.btnYes);

        txtQuestion.setText(getString(R.string.you_have_no_nonanimated_shots_on_your_account));
        btnCancel.setText(getString(R.string.cancel));
        btnUpload.setText(getString(R.string.upload));

        noShotsDialog.show();

        btnCancel.setOnClickListener(v -> noShotsDialog.dismiss());
        btnUpload.setOnClickListener(v -> {
            noShotsDialog.dismiss();
            this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.DRIBBBLE_ADD_SHOTS_URL)));
        });
    }

    private void loadShots(boolean loadFromDb) {
        presenter.loadShots(loadFromDb, page);
    }

}
