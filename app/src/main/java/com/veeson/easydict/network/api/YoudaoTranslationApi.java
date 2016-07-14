package com.veeson.easydict.network.api;

import com.veeson.easydict.model.YoudaoTranslation;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Wilson on 2016/6/17.
 */
public interface YoudaoTranslationApi {
    @GET("openapi.do")
    Observable<YoudaoTranslation> getYoudaoTranslation(@Query("keyfrom") String keyFrom, @Query("key") String key, @Query("type") String type, @Query("doctype") String docType, @Query("version") String version, @Query("only") String only, @Query("q") String q);
}
