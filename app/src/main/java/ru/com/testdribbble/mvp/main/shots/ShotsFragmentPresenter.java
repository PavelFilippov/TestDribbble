package ru.com.testdribbble.mvp.main.shots;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.List;

import ru.com.testdribbble.TheApplication;
import ru.com.testdribbble.core.LocalUserProvider;
import ru.com.testdribbble.core.data.DataProvider;
import ru.com.testdribbble.core.data.ShotsDb;
import ru.com.testdribbble.core.data.model.Shot;
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
    ShotsDb shotsDb;

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
            view.showServerError(throwable);
        });
    }

    @Override
    public void loadShots( boolean loadFromDb, int page) {
        view.showLoading();
        if (loadFromDb) {
            if (!shotsDb.isEmpty()) {
                dataProvider.getShotsFromDb(shots -> {
                    view.hideLoading();
                    view.setShots(page, shots, true);
                }, throwable -> {
                    view.showDataBaseError(throwable);
                    loadShotsFromNet(page);
                });
            } else {
                loadShotsFromNet(1);
            }
        } else {
            if(page != -1) {
                loadShotsFromNet(page);
            } else {
                view.removeScrollListener();
                view.hideLoading();
            }

        }
    }

    @Override
    public void refresh() {
        dataProvider.clearShotsInDb(throwable -> {
            view.showDataBaseError(throwable);
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

    private void loadShotsFromNet(int page) {
        dataProvider.getShots(page, PER_PAGE, shots -> {
            view.hideLoading();
            List<Shot> selectedShots = new ArrayList<>(shots);
            for (Shot shot : shots) {
                if (shot.isAnimated()) {
                    selectedShots.remove(shot);
                }
            }
            dataProvider.updateShotsInDb(shots, throwable -> {
                view.showDataBaseError(throwable);
            });
            view.setShots(page, selectedShots, false);
        }, throwable -> {
            view.hideLoading();
            view.showServerError(throwable);
        });
    }
}
