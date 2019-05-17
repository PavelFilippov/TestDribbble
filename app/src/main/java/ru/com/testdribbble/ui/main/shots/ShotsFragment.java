package ru.com.testdribbble.ui.main.shots;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Resources;
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
import android.text.TextUtils;
import android.util.DisplayMetrics;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
import ru.com.testdribbble.ui.login.LoginActivity_;
import ru.terrakok.cicerone.Router;

@EFragment(R.layout.fragment_shots)
public class ShotsFragment extends BaseMainFragment implements
        ShotsFragmentContract.ShotsFragmentView,
        BackButtonListener,
        IOnViewInItemClick{

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

    private int page;

    private MaterialDialog logoutDialog;

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

        imgLeftButton.setImageDrawable(getResources().getDrawable(R.drawable.log_out));
        txtHeader.setText(getString(R.string.app_name));

        onViewInItemClick = this;
        presenter.loadUserInfo();
    }

    @Override
    public void showLoading() {
        isLoadingData = true;
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
    public void setShots(List<Shot> shots) {
        List<Shot> selectedShots = new ArrayList<>(shots);
        for (Shot shot : shots) {
            if (shot.isAnimated()) {
                selectedShots.remove(shot);
            }
        }
        adapter.addData(selectedShots);
        page++;
    }

    @Override
    public void setUserInfo(User user) {
        this.user = user;
        setListViews();
    }

    @Override
    public void showError(Throwable throwable) {
        getMainActivity().showError(throwable);
    }

    @Override
    public void onAuthorNameClick() {
        presenter.goToProfileScreen();
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

    private void setListViews() {
        ablHeader.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ablHeader.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int ablHeaderHeight = ablHeader.getHeight();
                setAdapterViews(ablHeaderHeight);
                setRefresh();
                loadShots();
            }
        });
    }

    private void setAdapterViews(int height) {
        adapter = new ShotsAdapter(
                getMainActivity(),
                (getMainActivity().getScreenHeight() - (height* 3 / 2)) / 2,
                user != null ? user.getName() : "",
                onViewInItemClick);

        rvShots.setLayoutManager(new LinearLayoutManager(getMainActivity()));
        rvShots.setAdapter(adapter);

        rvShots.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!isLoadingData) {
                    loadShots();
                }
            }
        });

        adapter.setRecyclerTouchListener((model, position) -> {
            if (isDoubleClick(mLastClickTime)) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();

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
                        loadShots();
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

    private void loadShots() {
        presenter.loadShots(page);
    }
}
