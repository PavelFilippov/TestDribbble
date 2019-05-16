package ru.com.testdribbble.core.http;

import android.text.TextUtils;

import com.google.gson.Gson;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

import lombok.Getter;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.com.testdribbble.BuildConfig;
import ru.com.testdribbble.Preferences_;
import ru.com.testdribbble.TheApplication;
import ru.com.testdribbble.core.configuration.GsonConfiguredFactory;
import ru.com.testdribbble.core.data.response.BaseRESTModel;
import ru.com.testdribbble.core.exception.NetworkException;
import ru.com.testdribbble.core.exception.PlatformHttpException;
import ru.com.testdribbble.core.utils.Constants;

@EBean(scope = EBean.Scope.Singleton)
public class NetworkModule {

    private static final String AUTHORIZATION = "Authorization";
    @Getter
    private Api tokenApi;
    @Getter
    private Api mainApi;
    @Pref
    Preferences_ preferences;
    @App
    TheApplication application;

    private Cache cache;

    @AfterInject
    public void afterInject() {
        Retrofit.Builder tokenBuilder = new Retrofit.Builder()
                .baseUrl(Constants.TOKEN_URL)
                .addConverterFactory(GsonConverterFactory.create(GsonConfiguredFactory.getGson()))
                .validateEagerly(true)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(provideHttpClient());
        Retrofit tokenBuild = tokenBuilder.build();
        tokenApi = tokenBuild.create(Api.class);

        Retrofit.Builder mainBuilder = new Retrofit.Builder()
                .baseUrl(Constants.API_URL)
                .addConverterFactory(GsonConverterFactory.create(GsonConfiguredFactory.getGson()))
                .validateEagerly(true)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(provideHttpClient());
        Retrofit mainBuild = mainBuilder.build();
        mainApi = mainBuild.create(Api.class);
    }


    private OkHttpClient provideHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY
                : HttpLoggingInterceptor.Level.BASIC);


        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .cache(buildCache())
                .addInterceptor(chain -> {
                    String authHeader = MessageFormat.format("Bearer {0}", preferences.token().get());
                    Request authorization = chain.request().newBuilder()
                            .addHeader(AUTHORIZATION, authHeader)
                            .build();
                    Response response = null;
                    try {
                        response = chain.proceed(authorization);
                    } catch (IOException e) {
                        try {
                            response = chain.proceed(authorization);
                        } catch (IOException e1) {
                            throw new NetworkException(authorization.toString(), e);
                        }
                    }
                    CacheControl cacheControl = new CacheControl.Builder()
                            .maxAge(2, TimeUnit.MINUTES)
                            .build();


                    Response build = response.newBuilder()
                            .header("Cache-Control", cacheControl.toString())
                            .build();

                    if (!build.isSuccessful()) {
                        ResponseBody body = build.body();
                        if (body != null) {
                            String string = body.string();
                            if (!TextUtils.isEmpty(string)) {
                                BaseRESTModel restModel = new Gson().fromJson(string, BaseRESTModel.class);
                            } else {
                                BaseRESTModel restModel = new BaseRESTModel();
                                restModel.setMessage(response.message());
                                throw new PlatformHttpException(restModel);
                            }
                        }
                        return build;
                    } else {
                        return build;
                    }
                })
                .followRedirects(false)
                .build();
    }

    private Cache buildCache() {
        if (cache == null) {
            cache = new Cache(new File(application.getCacheDir(), "http-cache"), 10 * 1024 * 1024);
        }
        return cache;
    }
}
