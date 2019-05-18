package ru.com.testdribbble.ui;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import ru.com.testdribbble.R;
import ru.com.testdribbble.core.data.events.NetworkData;
import ru.com.testdribbble.core.exception.NoNetworkException;
import ru.com.testdribbble.core.exception.PlatformHttpException;
import ru.com.testdribbble.core.http.NetworkConnectivityAware_;

public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";

    private NetworkConnectivityAware_ receiver;
    private int screenHeight;
    private int screenWidth;

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkConnectivityAware_();
        registerReceiver(receiver, filter);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
    }

    @Override
    protected void onStop() {
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onNetworkStateChanged(NetworkData networkData) {
        Crouton.cancelAllCroutons();
        if (networkData.isConnected()) {
            Crouton.makeText(this, getString(R.string.network_connection_established), new Style.Builder().setBackgroundColorValue(getResources().getColor(R.color.colorDeepSkyBlue)).build()).show();
        } else {
            Crouton.makeText(this, getString(R.string.network_connection_lost), new Style.Builder().setBackgroundColorValue(getResources().getColor(R.color.colorRoseLight)).build()).show();
        }
    }

    public void showServerError(Throwable throwable) {
        if (throwable instanceof PlatformHttpException) {
            onServerErrorError();
        } else if (throwable instanceof NoNetworkException) {
            EventBus.getDefault().post(new NetworkData(false));
        } else {
            onServerErrorError();
        }
    }

    public void showDbError(Throwable throwable) {
        Crouton.makeText(this, getString(R.string.database_error), new Style.Builder()
                .setBackgroundColorValue(getResources().getColor(R.color.colorRoseLight))
                .build())
                .show();
    }

    private void onServerErrorError() {
        Crouton.makeText(this, getString(R.string.server_error), new Style.Builder()
                .setBackgroundColorValue(getResources().getColor(R.color.colorRoseLight))
                .build())
                .show();
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public int getScreenWidth() {
        return screenWidth;
    }
}
