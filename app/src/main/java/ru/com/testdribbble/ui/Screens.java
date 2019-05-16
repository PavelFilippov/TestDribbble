package ru.com.testdribbble.ui;

import android.content.Context;
import android.content.Intent;

import ru.com.testdribbble.ui.login.LoginActivity_;
import ru.com.testdribbble.ui.main.MainActivity_;
import ru.terrakok.cicerone.android.support.SupportAppScreen;

public class Screens {

//-----------------------------Login Screen---------------------------------

    public static final class LoginScreen extends SupportAppScreen {
        @Override
        public Intent getActivityIntent(Context context) {
            return new Intent(context, LoginActivity_.class);
        }
    }

//-----------------------------Main Screen---------------------------------

    public static final class MainScreen extends SupportAppScreen {
        @Override
        public Intent getActivityIntent(Context context) {
            return new Intent(context, MainActivity_.class);
        }
    }

}
