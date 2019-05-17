package ru.com.testdribbble.mvp.main.shots;

import java.util.List;

import ru.com.testdribbble.core.data.model.Shot;
import ru.com.testdribbble.core.data.model.User;
import ru.com.testdribbble.mvp.BasePresenter;

public interface ShotsFragmentContract {

    interface ShotsFragmentView {

        void showLoading();

        void hideLoading();

        void setShots(List<Shot> shots);

        void setUserInfo(User user);

        void showError(Throwable throwable);

    }

    interface ShotsFragmentPresenter extends BasePresenter {

        void setView(ShotsFragmentView view);

        void loadUserInfo();

        void loadShots(int page);

        void goToProfileScreen();

        void logOut();

    }
}
