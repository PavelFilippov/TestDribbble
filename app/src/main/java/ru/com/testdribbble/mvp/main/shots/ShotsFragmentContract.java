package ru.com.testdribbble.mvp.main.shots;

import java.util.List;

import ru.com.testdribbble.core.data.model.Shot;
import ru.com.testdribbble.core.data.model.User;
import ru.com.testdribbble.mvp.BasePresenter;

public interface ShotsFragmentContract {

    interface ShotsFragmentView {

        void showLoading();

        void hideLoading();

        void setShots(int page, List<Shot> shots, boolean isLoadedFromDb);

        void setUserInfo(User user);

        void removeScrollListener();

        void showDataBaseError(Throwable throwable);

        void showServerError(Throwable throwable);

    }

    interface ShotsFragmentPresenter extends BasePresenter {

        void setView(ShotsFragmentView view);

        void loadUserInfo();

        void loadShots(boolean loadFromDb, int page);

        void refresh();

        void goToProfileScreen();

        void logOut();

    }
}
