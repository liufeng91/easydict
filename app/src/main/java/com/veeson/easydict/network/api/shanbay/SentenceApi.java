package com.veeson.easydict.network.api.shanbay;

import com.veeson.easydict.model.shanbay.Sentence;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Wilson on 2016/7/1.
 */
public interface SentenceApi {
    @GET("example/")
    Observable<Sentence> getSentence(@Query("access_token") String access_token, @Query("vocabulary_id") int vocabulary_id, @Query("type") String type);
}
