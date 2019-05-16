package ru.com.testdribbble.core;

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

}
