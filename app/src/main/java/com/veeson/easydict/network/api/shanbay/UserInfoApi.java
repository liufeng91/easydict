package com.veeson.easydict.network.api.shanbay;

import com.veeson.easydict.model.shanbay.UserInfo;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Wilson on 2016/6/30.
 */
public interface UserInfoApi {
    @GET("account/")
    Observable<UserInfo> getUserInfo(@Query("access_token") String access_token);
}
