package ru.com.testdribbble.mvp.login;

import org.androidannotations.annotations.EBean;

import ru.terrakok.cicerone.Router;

@EBean
public class LoginActivityPresenter implements LoginActivityContract.LoginActivityPresenter {

    private static final String TAG = "LoginActivityPresenter";

    private Router router;
    private LoginActivityContract.LoginActivityView view;

    @Override
    public void setView(LoginActivityContract.LoginActivityView view) {
        this.view = view;
    }

    @Override
    public void getToken(String code) {

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
