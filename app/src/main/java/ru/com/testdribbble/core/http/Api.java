package ru.com.testdribbble.core.http;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import ru.com.testdribbble.core.data.model.Shot;
import ru.com.testdribbble.core.data.model.Token;

public interface Api {

    @POST("token")
    Observable<Token> getToken(@Query("client_id") String clientId,
                               @Query("client_secret") String clientSecret,
                               @Query("code") String code);

    @GET("user/shots")
    Observable<List<Shot>> getShots(@Query("page") int page,
                                    @Query("per_page") int per_page);
}
