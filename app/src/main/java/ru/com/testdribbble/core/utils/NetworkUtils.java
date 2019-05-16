package ru.com.testdribbble.core.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EBean;

import ru.com.testdribbble.TheApplication;

@EBean
public class NetworkUtils {

    @App
    TheApplication application;


    public boolean hasNetworkConnection() {
        ConnectivityManager cm = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
