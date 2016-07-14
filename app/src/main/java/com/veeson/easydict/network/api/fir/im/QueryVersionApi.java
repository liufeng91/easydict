package com.veeson.easydict.network.api.fir.im;

import com.veeson.easydict.model.fir.im.QueryVersion;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Wilson on 2016/7/6.
 */
public interface QueryVersionApi {
    // http://api.fir.im/apps/latest/xxx?api_token=xxx #使用 `id` 请求
    // http://api.fir.im/apps/latest/577218d9e75e2d73dc000007?api_token=d9860778d39487cba68f067b848d4c5c
    @GET("577218d9e75e2d73dc000007")
    Observable<QueryVersion> getQueryVersion(@Query("api_token") String api_token);
}
