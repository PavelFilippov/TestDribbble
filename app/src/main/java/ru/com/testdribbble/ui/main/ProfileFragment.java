package ru.com.testdribbble.ui.main;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import ru.com.testdribbble.R;
import ru.com.testdribbble.TheApplication;
import ru.com.testdribbble.core.data.model.Shot;
import ru.com.testdribbble.core.data.model.User;
import ru.com.testdribbble.mvp.main.profile.ProfileFragmentContract;
import ru.com.testdribbble.mvp.main.profile.ProfileFragmentPresenter;
import ru.com.testdribbble.mvp.main.shots.ShotsFragmentContract;
import ru.com.testdribbble.mvp.main.shots.ShotsFragmentPresenter;
import ru.com.testdribbble.ui.BaseMainFragment;
import ru.com.testdribbble.ui.adapters.ShotsAdapter;
import ru.com.testdribbble.ui.common.BackButtonListener;
import ru.com.testdribbble.ui.common.RouterProvider;
import ru.com.testdribbble.ui.main.shots.IOnViewInItemClick;
import ru.com.testdribbble.views.ProfileImageProgressBar;
import ru.terrakok.cicerone.Router;

@EFragment(R.layout.fragment_profile)
public class ProfileFragment extends BaseMainFragment implements
        ProfileFragmentContract.ProfileFragmentView,
        BackButtonListener {

    private static final String TAG = "ProfileFragment";

//Set screen views

    //Set header views

    @ViewById
    TextView txtHeader;

    @ViewById
    AppCompatImageView imgLeftButton;

    //Set main views

    @ViewById
    View llProfile;

    @ViewById
    ProgressBar progressBar;

    @ViewById
    AppCompatImageView imgNoImage;

    @ViewById
    SimpleDraweeView sdvUserPhoto;

    @ViewById
    TextView txtUserName;

    @ViewById
    TextView txtFollowers;

//Set app navigation injections and it's management

    @Inject
    Router router;

//Set Bundles and Beans

    @Bean(ProfileFragmentPresenter.class)
    ProfileFragmentContract.ProfileFragmentPresenter presenter;

    @App
    TheApplication application;

//Set Local variables

    private long mLastClickTime = 0;

//Main methods

    @Override
    public void onCreate(Bundle savedInstanceState) {
        application.getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    public void afterViews() {
        presenter.setView(this);
        if ((getParentFragment()) != null) {
            presenter.setRouter(((RouterProvider) getParentFragment()).getRouter());
        } else {
            presenter.setRouter(router);
        }

        imgLeftButton.setImageDrawable(getResources().getDrawable(R.drawable.back));
        txtHeader.setText(getString(R.string.profile));

        presenter.loadProfileInfo();
    }

    @Override
    public void showLoading() {
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }, 300);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
        llProfile.setVisibility(View.VISIBLE);
    }

    @Override
    public void setProfileInfo(User user) {

        txtUserName.setText(user.getName() != null ? user.getName() : "");
        txtFollowers.setText(String.format(getString(R.string._followers), user.getFollowersCount()));

        if (TextUtils.isEmpty(user.getAvatarUrl())) {
            imgNoImage.setVisibility(View.VISIBLE);
        } else {
            sdvUserPhoto.setVisibility(View.VISIBLE);
            sdvUserPhoto.getHierarchy().setProgressBarImage(new ProfileImageProgressBar());
            sdvUserPhoto.setImageURI(Uri.parse(user.getAvatarUrl()));
        }

    }

    @Override
    public void showError(Throwable throwable) {
        getMainActivity().showError(throwable);
    }

    @Override
    public boolean onBackPressed() {
        onBackClick();
        return true;
    }

//Manage all views clicks

    @Click(R.id.imgLeftButton)
    void onBackClick() {
        if (isDoubleClick(mLastClickTime)) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        presenter.onBackPressed();
    }
}
