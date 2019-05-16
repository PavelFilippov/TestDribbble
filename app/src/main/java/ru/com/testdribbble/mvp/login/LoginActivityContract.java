package ru.com.testdribbble.mvp.login;

import ru.com.testdribbble.mvp.BasePresenter;

public interface LoginActivityContract {

    interface LoginActivityView {

        void showLoading();

        void hideLoading();

        void showErrorOnGetToken(Throwable throwable);

    }

    interface LoginActivityPresenter extends BasePresenter {

        void setView(LoginActivityView view);

        void getToken(String code);

        void goToNextScreen();

    }
}
