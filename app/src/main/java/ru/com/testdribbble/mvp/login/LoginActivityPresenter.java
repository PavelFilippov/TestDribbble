package ru.com.testdribbble.mvp.login;

import android.text.TextUtils;
import android.util.Base64;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import ru.com.testdribbble.core.data.DataProvider;
import ru.com.testdribbble.core.utils.Constants;
import ru.com.testdribbble.core.utils.EncodeUtils;
import ru.com.testdribbble.ui.Screens;
import ru.terrakok.cicerone.Router;

@EBean
public class LoginActivityPresenter implements LoginActivityContract.LoginActivityPresenter {

    private static final String TAG = "LoginActivityPresenter";

    private Router router;
    private LoginActivityContract.LoginActivityView view;

    @Bean
    DataProvider dataProvider;

    @Override
    public void setView(LoginActivityContract.LoginActivityView view) {
        this.view = view;
    }

    @Override
    public void getToken(String code) {
        view.showLoading();
        String clientId = "";
        String clientSecret = "";
        try {
            clientId = EncodeUtils.decrypt(Base64.decode(Constants.CLIENT_ID.getBytes("UTF-16LE"), Base64.DEFAULT));
            clientSecret = EncodeUtils.decrypt(Base64.decode(Constants.CLIENT_SECRET.getBytes("UTF-16LE"), Base64.DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
        }
        dataProvider.getToken(clientId, clientSecret, code, token -> {
            if (!TextUtils.isEmpty(token.getAccessToken())) goToNextScreen();
        }, throwable -> {
            view.hideLoading();
            view.showServerError(throwable);
        });
    }

    @Override
    public void goToNextScreen() {
        router.newRootScreen(new Screens.MainScreen());
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
