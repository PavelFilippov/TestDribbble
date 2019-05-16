package ru.com.testdribbble;

import android.support.multidex.MultiDexApplication;

import org.androidannotations.annotations.EApplication;

import ru.com.testdribbble.core.dagger.AppComponent;
import ru.com.testdribbble.core.dagger.DaggerAppComponent;

@EApplication
public class TheApplication extends MultiDexApplication {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public AppComponent getAppComponent() {
        if (appComponent == null) {
            appComponent = DaggerAppComponent.builder().build();
        }
        return appComponent;
    }

}
