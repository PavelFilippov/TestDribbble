package ru.com.testdribbble;

import android.support.multidex.MultiDexApplication;

import com.facebook.drawee.backends.pipeline.Fresco;

import org.androidannotations.annotations.EApplication;

import lombok.Getter;
import lombok.Setter;
import ru.com.testdribbble.core.dagger.AppComponent;
import ru.com.testdribbble.core.dagger.DaggerAppComponent;

@EApplication
public class TheApplication extends MultiDexApplication {

    public static TheApplication INSTANCE;

    private AppComponent appComponent;

    @Getter
    @Setter
    private boolean hasNetworkConnection = true;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        Fresco.initialize(this);
    }

    public AppComponent getAppComponent() {
        if (appComponent == null) {
            appComponent = DaggerAppComponent.builder().build();
        }
        return appComponent;
    }

}
