package ru.com.testdribbble.mvp.main;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import ru.com.testdribbble.core.data.DataProvider;
import ru.terrakok.cicerone.Router;

@EBean
public class ShotsFragmentPresenter implements ShotsFragmentContract.ShotsFragmentPresenter {

    private static final String TAG = "ShotsFragmentPresenter";
    private static final int PER_PAGE = 5;

    private ShotsFragmentContract.ShotsFragmentView view;
    private Router router;

    @Bean
    DataProvider dataProvider;

    @Override
    public void setView(ShotsFragmentContract.ShotsFragmentView view) {
        this.view = view;
    }

    @Override
    public void loadShots(int page) {
        view.showLoading();
        dataProvider.getShots(page, PER_PAGE, shots -> {
            view.hideLoading();
            view.setShots(shots);
        }, throwable -> {
            view.hideLoading();
            view.showError(throwable);
        });
    }

    @Override
    public void setRouter(Router router) {
        this.router = router;
    }

    @Override
    public void onBackPressed() {
        router.exit();
    }
}
