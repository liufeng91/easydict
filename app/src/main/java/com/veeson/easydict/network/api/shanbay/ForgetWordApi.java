package com.veeson.easydict.network.api.shanbay;

import com.veeson.easydict.model.shanbay.ForgetWord;

import retrofit2.http.PUT;
import retrofit2.http.Query;
import rx.Observable;


/**
 * Created by Wilson on 2016/7/1.
 */
public interface ForgetWordApi {
    @PUT("learning/")
    Observable<ForgetWord> getForgetWord(@Query("access_token") String access_token, @Query("id") int id, @Query("forget") int forget);
}
