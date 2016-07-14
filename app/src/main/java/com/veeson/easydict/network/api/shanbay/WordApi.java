package com.veeson.easydict.network.api.shanbay;

import com.veeson.easydict.model.shanbay.Word;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Wilson on 2016/7/1.
 */
public interface WordApi {
    @GET("search/")
    Observable<Word> getWord(@Query("access_token") String access_token, @Query("word") String word);
}
