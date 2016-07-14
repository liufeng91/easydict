package com.veeson.easydict.network.api;

import com.veeson.easydict.model.DailySentence;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Wilson on 2016/6/10.
 */
public interface DailySentenceApi {
    @GET("dsapi/")
    Observable<DailySentence> getDailySentence(@Query("date") String query);
}
