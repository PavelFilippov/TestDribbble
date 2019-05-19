package ru.com.testdribbble.mvp.login;

import ru.com.testdribbble.mvp.BasePresenter;
import ru.com.testdribbble.mvp.BaseView;

public interface LoginActivityContract {

    interface LoginActivityView extends BaseView {

    }

    interface LoginActivityPresenter extends BasePresenter {

        void setView(LoginActivityView view);

        void getToken(String code);

        void goToNextScreen();

    }
}
