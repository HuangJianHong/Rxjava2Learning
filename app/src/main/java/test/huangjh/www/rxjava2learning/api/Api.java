package test.huangjh.www.rxjava2learning.api;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Query;
import test.huangjh.www.rxjava2learning.entity.LoginRequest;
import test.huangjh.www.rxjava2learning.entity.LoginResponse;
import test.huangjh.www.rxjava2learning.entity.RegisterRequest;
import test.huangjh.www.rxjava2learning.entity.RegisterResponse;
import test.huangjh.www.rxjava2learning.entity.UserBaseInfoRequest;
import test.huangjh.www.rxjava2learning.entity.UserBaseInfoResponse;
import test.huangjh.www.rxjava2learning.entity.UserExtraInfoRequest;
import test.huangjh.www.rxjava2learning.entity.UserExtraInfoResponse;

/**
 * Created by  Hjh on 2018/2/27.
 * desc：
 */

public interface Api {

    @GET
    Observable<LoginResponse> login(@Body LoginRequest request);

    @GET
    Observable<RegisterResponse> register(@Body RegisterRequest request);


    @GET
    Observable<UserBaseInfoResponse> getUserBaseInfo(@Body UserBaseInfoRequest request);

    @GET
    Observable<UserExtraInfoResponse> getUserExtraInfo(@Body UserExtraInfoRequest request);

    @GET("v2/movie/top250")
    Observable<Response<ResponseBody>> getTop250(@Query("start") int start, @Query("count") int count);

}
