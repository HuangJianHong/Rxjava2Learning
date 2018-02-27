package test.huangjh.www.rxjava2learning.api;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import test.huangjh.www.rxjava2learning.entity.LoginRequest;
import test.huangjh.www.rxjava2learning.entity.LoginResponse;
import test.huangjh.www.rxjava2learning.entity.RegisterRequest;
import test.huangjh.www.rxjava2learning.entity.RegisterResponse;

/**
 * Created by  Hjh on 2018/2/27.
 * desc：
 */

public interface Api {

    @GET
    Observable<LoginResponse> login(@Body LoginRequest request);

    @GET
    Observable<RegisterResponse> register(@Body RegisterRequest request);




}
