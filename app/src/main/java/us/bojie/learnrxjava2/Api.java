package us.bojie.learnrxjava2;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by bojiejiang on 4/17/17.
 */

public interface Api {

    @GET("user/{id}")
    Call<User> getUserInfoWithPath(@Path("id") int userId);

    @POST("login/json")
    Call<BaseResult> login(@Body UserParam param);
}
