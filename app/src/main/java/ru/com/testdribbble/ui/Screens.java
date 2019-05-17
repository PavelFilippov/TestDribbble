package ru.com.testdribbble.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import ru.com.testdribbble.ui.login.LoginActivity_;
import ru.com.testdribbble.ui.main.MainActivity_;
import ru.com.testdribbble.ui.main.shots.ShotsFragment_;
import ru.terrakok.cicerone.android.support.SupportAppScreen;

public class Screens {

//-----------------------------Login Screen---------------------------------

    public static final class LoginScreen extends SupportAppScreen {
        @Override
        public Intent getActivityIntent(Context context) {
            return new Intent(context, LoginActivity_.class);
        }
    }

//-----------------------------Main Container---------------------------------

    public static final class MainScreen extends SupportAppScreen {
        @Override
        public Intent getActivityIntent(Context context) {
            return new Intent(context, MainActivity_.class);
        }
    }

//-----------------------------Shots screen---------------------------------

    public static final class ShotsScreen extends SupportAppScreen {
        private Context context;

        @Override
        public Intent getActivityIntent(Context context) {
            this.context = context;
            return super.getActivityIntent(context);
        }

        @Override
        public Fragment getFragment() {
            return Fragment.instantiate(context, ShotsFragment_.class.getName());
        }
    }

}
