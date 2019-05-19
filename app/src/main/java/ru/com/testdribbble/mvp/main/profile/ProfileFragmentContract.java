package ru.com.testdribbble.mvp.main.profile;

import ru.com.testdribbble.core.data.model.User;
import ru.com.testdribbble.mvp.BasePresenter;
import ru.com.testdribbble.mvp.BaseView;

public interface ProfileFragmentContract {

    interface ProfileFragmentView extends BaseView {

        void setProfileInfo(User user);

    }

    interface ProfileFragmentPresenter extends BasePresenter {

        void setView(ProfileFragmentView view);

        void loadProfileInfo();

    }

}
