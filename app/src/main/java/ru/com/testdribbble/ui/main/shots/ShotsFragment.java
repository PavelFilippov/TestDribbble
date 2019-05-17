package ru.com.testdribbble.ui.main.shots;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ProgressBar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import ru.com.testdribbble.R;
import ru.com.testdribbble.TheApplication;
import ru.com.testdribbble.core.data.model.Shot;
import ru.com.testdribbble.mvp.main.ShotsFragmentContract;
import ru.com.testdribbble.mvp.main.ShotsFragmentPresenter;
import ru.com.testdribbble.ui.BaseMainFragment;
import ru.com.testdribbble.ui.adapters.ShotsAdapter;
import ru.com.testdribbble.ui.common.BackButtonListener;
import ru.com.testdribbble.ui.common.RouterProvider;
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
    AppCompatImageView imgLogOut;

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

//Set Local variables

    private boolean isLoadingData;
    private ShotsAdapter adapter;

    private int page = 1;

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

        setAdapter();
    }

    @Override
    public void showLoading() {
        isLoadingData = true;
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            if (isLoadingData) {
                if (adapter.isEmpty()) {
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
    public void showError(Throwable throwable) {
        getMainActivity().showError(throwable);
    }

    @Override
    public void onAuthorNameClick() {

    }

    @Override
    public boolean onBackPressed() {
        presenter.onBackPressed();
        return true;
    }

//Manage all views clicks

//Internal methods

    private void setAdapter() {
        ablHeader.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ablHeader.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int ablHeaderHeight = ablHeader.getHeight();

                adapter = new ShotsAdapter(
                        getMainActivity(),
                        (getMainActivity().getScreenHeight() - (ablHeaderHeight * 3 / 2)) / 2,
                        "",
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

                adapter.setRecyclerTouchListener((model, position) -> {
                    if (isDoubleClick(mLastClickTime)) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();

                });

                loadShots();
            }
        });
    }

    private void loadShots() {
        presenter.loadShots(page);
    }
}
