package ru.com.testdribbble.ui.login;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import javax.inject.Inject;

import ru.com.testdribbble.R;
import ru.com.testdribbble.TheApplication;
import ru.com.testdribbble.core.LocalUserProvider;
import ru.com.testdribbble.core.utils.Constants;
import ru.com.testdribbble.core.utils.EncodeUtils;
import ru.com.testdribbble.mvp.login.LoginActivityContract;
import ru.com.testdribbble.mvp.login.LoginActivityPresenter;
import ru.com.testdribbble.ui.BaseActivity;
import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Router;
import ru.terrakok.cicerone.android.support.SupportAppNavigator;

@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity implements LoginActivityContract.LoginActivityView {

    private static final String TAG = "LoginActivity";

    //Set screen views

    @ViewById
    ProgressBar progressBar;

    @ViewById
    WebView webView;

//Set app navigation injections

    @Inject
    Router router;

    @Inject
    NavigatorHolder navigatorHolder;

    private Navigator navigator = new SupportAppNavigator(this, -1);

//Set Bundles and Beans

    @Bean(LoginActivityPresenter.class)
    LoginActivityContract.LoginActivityPresenter presenter;

    @App
    TheApplication application;

    @Bean
    LocalUserProvider userProvider;

//Set Local variables

    private MaterialDialog loginDialog;
    private long mLastClickTime = 0;

//Main methods

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application.getAppComponent().inject(this);
    }

    @AfterViews
    public void afterViews() {
        presenter.setRouter(router);
        presenter.setView(this);
        String token = userProvider.getToken();
        if(!TextUtils.isEmpty(token)) {
            presenter.goToNextScreen();
        } else {
            showLoginDialog();
            setLoginScreen();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigatorHolder.setNavigator(navigator);
    }

    @Override
    protected void onPause() {
        navigatorHolder.removeNavigator();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        presenter.onBackPressed();
    }

//Internal methods

    private void showLoginDialog() {
        loginDialog = new MaterialDialog.Builder(this)
                .customView(R.layout.dialog_question_yes_no, false)
                .build();
        TextView txtQuestion = (TextView) loginDialog.findViewById(R.id.txtQuestion);
        MaterialButton btnSignIn = (MaterialButton) loginDialog.findViewById(R.id.btnNo);
        MaterialButton btnSignUp = (MaterialButton) loginDialog.findViewById(R.id.btnYes);

        txtQuestion.setText(getString(R.string.if_you_dont_have_account_on_dribbble_please_sign_up_there));
        btnSignIn.setText(getString(R.string.sign_in));
        btnSignUp.setText(getString(R.string.sign_up));

        loginDialog.show();

        btnSignIn.setOnClickListener(v -> loginDialog.dismiss());
        btnSignUp.setOnClickListener(v -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            loginDialog.dismiss();
            this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.DRIBBBLE_SIGN_UP_URL)));
        });
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setLoginScreen() {
        webView.setWebViewClient(new MyWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        try {
            webView.loadUrl(Constants.DRIBBBLE_SIGN_IN_URL +
                    EncodeUtils.decrypt(Base64.decode(Constants.CLIENT_ID.getBytes("UTF-16LE"), Base64.DEFAULT)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showLoading() {
        if(webView != null) webView.setVisibility(View.GONE);
        if(progressBar != null) progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        if(webView != null) webView.setVisibility(View.VISIBLE);
        if(progressBar != null) progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showServerError(Throwable throwable) {
        showServerError(throwable);
    }

    private class MyWebViewClient extends WebViewClient {

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            if(hasCode(request.getUrl().toString())) {
                presenter.getToken(request.getUrl().toString().replace(Constants.REDIRECT_URL, ""));
            }
            return !hasCode(request.getUrl().toString());
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            if(hasCode(url)) {
                presenter.getToken(url.replace(Constants.REDIRECT_URL, ""));
            }
            return !hasCode(url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if(progressBar != null) progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if(progressBar != null) progressBar.setVisibility(View.GONE);
        }

        private boolean hasCode(String url) {
            return url.startsWith(Constants.REDIRECT_URL);
        }
    }

}
