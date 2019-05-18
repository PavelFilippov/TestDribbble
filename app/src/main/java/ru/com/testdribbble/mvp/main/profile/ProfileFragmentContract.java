package ru.com.testdribbble.mvp.main.profile;

import ru.com.testdribbble.core.data.model.User;
import ru.com.testdribbble.mvp.BasePresenter;

public interface ProfileFragmentContract {

    interface ProfileFragmentView {

        void showLoading();

        void hideLoading();

        void setProfileInfo(User user);

        void showServerError(Throwable throwable);

    }

    interface ProfileFragmentPresenter extends BasePresenter {

        void setView(ProfileFragmentView view);

        void loadProfileInfo();

    }

}
