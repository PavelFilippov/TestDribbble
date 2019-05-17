package ru.com.testdribbble.mvp.main.shots;

import android.os.Handler;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import ru.com.testdribbble.TheApplication;
import ru.com.testdribbble.core.LocalUserProvider;
import ru.com.testdribbble.core.data.DataProvider;
import ru.com.testdribbble.ui.Screens;
import ru.terrakok.cicerone.Router;

@EBean
public class ShotsFragmentPresenter implements ShotsFragmentContract.ShotsFragmentPresenter {

    private static final String TAG = "ShotsFragmentPresenter";
    private static final int PER_PAGE = 5;

    private ShotsFragmentContract.ShotsFragmentView view;
    private Router router;

    @Bean
    DataProvider dataProvider;

    @Bean
    LocalUserProvider userProvider;

    @App
    TheApplication application;

    @Override
    public void setView(ShotsFragmentContract.ShotsFragmentView view) {
        this.view = view;
    }

    @Override
    public void loadUserInfo() {
        view.showLoading();
        dataProvider.getUser(user -> {
            view.hideLoading();
            view.setUserInfo(user);
        }, throwable -> {
            view.hideLoading();
            view.showError(throwable);
        });
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
    public void goToProfileScreen() {
        router.navigateTo(new Screens.ProfileScreen());
    }

    @Override
    public void logOut() {
        userProvider.removeToken();
        application.clearApplicationData();
        router.newRootScreen(new Screens.LoginScreen());
        android.os.Process.killProcess(android.os.Process.myPid());
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
