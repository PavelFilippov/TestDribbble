package ru.com.testdribbble.core.dagger;

import javax.inject.Singleton;

import dagger.Component;
import ru.com.testdribbble.core.dagger.module.LocalNavigationModule;
import ru.com.testdribbble.core.dagger.module.NavigationModule;
import ru.com.testdribbble.ui.LoginActivity;
import ru.com.testdribbble.ui.MainActivity;

@Singleton
@Component(modules = {
        NavigationModule.class,
        LocalNavigationModule.class
})

public interface AppComponent {

    //----------------------------------------LoginScreen--------------------------------------------

    void inject(LoginActivity activity);

    //----------------------------------------MainScreen--------------------------------------------

    void inject(MainActivity activity);
}
