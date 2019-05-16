package ru.com.testdribbble.core.dagger.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.com.testdribbble.core.LocalCiceroneHolder;

@Module
public class LocalNavigationModule {

    @Provides
    @Singleton
    LocalCiceroneHolder provideLocalNavigationHolder() {
        return new LocalCiceroneHolder();
    }
}