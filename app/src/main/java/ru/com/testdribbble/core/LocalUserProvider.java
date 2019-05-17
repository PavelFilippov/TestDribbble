package ru.com.testdribbble.core;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.JsonSyntaxException;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.sharedpreferences.Pref;

import lombok.Getter;
import lombok.Setter;
import ru.com.testdribbble.Preferences_;

@Getter
@Setter
@EBean(scope = EBean.Scope.Singleton)
public class LocalUserProvider {

    private static final String TAG = "LocalUserProvider";

    @Pref
    Preferences_ preferences;

    public String getToken() {
        String s = preferences.token().get();
        if (TextUtils.isEmpty(s)) return null;
        try {
            return s;
        } catch (JsonSyntaxException e) {
            Log.e(TAG, "getToken: ", e);
            return null;
        }
    }

    public void removeToken() {
        preferences.edit().token().put(null).apply();
    }

}
