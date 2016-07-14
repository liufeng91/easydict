package com.veeson.easydict.network.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by Wilson on 2016/6/11.
 */
public interface DailyVoiceApi {
    @GET
    Call<ResponseBody> downloadDailyVoice(@Url String fileUrl);
}
