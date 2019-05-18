package ru.com.testdribbble.mvp.main.profile;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import ru.com.testdribbble.core.data.DataProvider;
import ru.terrakok.cicerone.Router;

@EBean
public class ProfileFragmentPresenter implements ProfileFragmentContract.ProfileFragmentPresenter {

    private static final String TAG = "ProfileFragmentPresenter";

    private ProfileFragmentContract.ProfileFragmentView view;
    private Router router;

    @Bean
    DataProvider dataProvider;

    @Override
    public void setView(ProfileFragmentContract.ProfileFragmentView view) {
        this.view = view;
    }

    @Override
    public void loadProfileInfo() {
        view.showLoading();
        dataProvider.getUser(user -> {
            view.hideLoading();
            view.setProfileInfo(user);
        }, throwable -> {
            view.hideLoading();
            view.showServerError(throwable);
        });
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
