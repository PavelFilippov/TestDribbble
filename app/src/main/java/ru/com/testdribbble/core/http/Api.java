package ru.com.testdribbble.core.http;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import ru.com.testdribbble.core.data.model.Token;

public interface Api {

    @POST("token")
    Observable<Token> getToken(@Query("client_id") String clientId,
                               @Query("client_secret") String clientSecret,
                               @Query("code") String code);
}
