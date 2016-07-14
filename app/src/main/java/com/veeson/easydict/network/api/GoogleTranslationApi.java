package com.veeson.easydict.network.api;

import com.veeson.easydict.model.GoogleTranslation;
import com.veeson.easydict.model.YoudaoTranslation;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Wilson on 2016/6/17.
 */
public interface GoogleTranslationApi {
    @GET("single")
    Observable<GoogleTranslation> getGoogleTranslation(@Query("client") String client, @Query("sl") String sl, @Query("tl") String tl, @Query("hl") String hl, @Query("dt") String[] dt, @Query("dj") String dj, @Query("source") String source, @Query("q") String q);
}
