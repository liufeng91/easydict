package com.veeson.easydict.network.api.shanbay;

import com.veeson.easydict.model.shanbay.AddWord;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Wilson on 2016/7/1.
 */
public interface AddWordApi {
    @FormUrlEncoded
    @POST("learning/")
    Observable<AddWord> getAddWord(@Field("access_token") String access_token, @Field("id") int id);
}
