package ru.com.testdribbble.core.data;

import android.text.TextUtils;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import lombok.Getter;
import ru.com.testdribbble.Preferences_;
import ru.com.testdribbble.TheApplication;
import ru.com.testdribbble.core.LocalUserProvider;
import ru.com.testdribbble.core.data.model.Shot;
import ru.com.testdribbble.core.data.model.Token;
import ru.com.testdribbble.core.data.model.User;
import ru.com.testdribbble.core.exception.NoNetworkException;
import ru.com.testdribbble.core.http.NetworkModule;
import ru.com.testdribbble.core.utils.NetworkUtils;
import ru.com.testdribbble.core.utils.RxUtils;

@EBean(scope = EBean.Scope.Singleton)
public class DataProvider {

    @Getter
    @Bean
    NetworkModule networkModule;
    @Pref
    Preferences_ pref;
    @Bean
    NetworkUtils networkUtils;
    @App
    TheApplication application;
    @Bean
    LocalUserProvider userProvider;

    public Disposable getToken(String clientId, String clientSecret, String code, Consumer<Token> onComplete, Consumer<Throwable> onError) {
        if (!hasNetwork()) return createNoNetworkSubscription(onComplete, onError);
        return networkModule.getTokenApi().getToken(clientId, clientSecret, code)
                .doOnNext(tokenResponse -> {
                    if (!TextUtils.isEmpty(tokenResponse.getAccessToken())) {
                        pref.edit().token().put(tokenResponse.getAccessToken()).apply();
                    }
                }).compose(RxUtils.applySchedulers())
                .subscribe(onComplete, onError);
    }

    public Disposable getUser(Consumer<User> onComplete, Consumer<Throwable> onError) {
        if (!hasNetwork()) return createNoNetworkSubscription(onComplete, onError);
        return networkModule.getMainApi().getUser()
                .compose(RxUtils.applySchedulers())
                .subscribe(onComplete, onError);
    }

    public Disposable getShots(int page, int perPage, Consumer<List<Shot>> onComplete, Consumer<Throwable> onError) {
        if (!hasNetwork()) return createNoNetworkSubscription(onComplete, onError);
        return networkModule.getMainApi().getShots(page, perPage)
                .compose(RxUtils.applySchedulers())
                .subscribe(onComplete, onError);
    }

    private boolean hasNetwork() {
        return networkUtils.hasNetworkConnection();
    }

    private <T> Disposable createNoNetworkSubscription(Consumer<T> onComplete, Consumer<Throwable> onError) {
        return RxUtils.makeObservable((Callable<T>) () -> {
            throw new NoNetworkException();
        }).subscribe(onComplete, onError);
    }
}
