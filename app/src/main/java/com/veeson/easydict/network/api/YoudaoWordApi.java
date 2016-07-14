package com.veeson.easydict.network.api;

import com.veeson.easydict.model.YoudaoWord;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Wilson on 2016/6/13.
 */
public interface YoudaoWordApi {
    @GET("openapi.do")
    Observable<YoudaoWord> getYoudaoWord(@Query("keyfrom") String keyFrom, @Query("key") String key, @Query("type") String type, @Query("doctype") String docType, @Query("version") String version, @Query("q") String word);
}
