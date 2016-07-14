package com.veeson.easydict.network;

import com.veeson.easydict.model.shanbay.AddWord;
import com.veeson.easydict.model.shanbay.ForgetWord;
import com.veeson.easydict.network.api.BaiduTranslationApi;
import com.veeson.easydict.network.api.DailySentenceApi;
import com.veeson.easydict.network.api.DailyVoiceApi;
import com.veeson.easydict.network.api.GoogleTranslationApi;
import com.veeson.easydict.network.api.YoudaoTranslationApi;
import com.veeson.easydict.network.api.YoudaoWordApi;
import com.veeson.easydict.network.api.fir.im.QueryVersionApi;
import com.veeson.easydict.network.api.shanbay.AddWordApi;
import com.veeson.easydict.network.api.shanbay.ForgetWordApi;
import com.veeson.easydict.network.api.shanbay.SentenceApi;
import com.veeson.easydict.network.api.shanbay.UserInfoApi;
import com.veeson.easydict.network.api.shanbay.WordApi;

import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Wilson on 2016/6/10.
 */
public class Network {
    private static DailySentenceApi dailySentenceApi;
    private static DailyVoiceApi dailyVoiceApi;
    private static YoudaoWordApi youdaoWordApi;
    private static BaiduTranslationApi baiduTranslationApi;
    private static YoudaoTranslationApi youdaoTranslationApi;
    private static GoogleTranslationApi googleTranslationApi;
    private static UserInfoApi userInfoApi;
    private static WordApi wordApi;
    private static AddWordApi addWordApi;
    private static ForgetWordApi forgetWordApi;
    private static SentenceApi sentenceApi;
    private static QueryVersionApi queryVersionApi;
    private static OkHttpClient okHttpClient = new OkHttpClient();
    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();

    public static DailySentenceApi getDailySentenceApi() {
        if (dailySentenceApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("http://open.iciba.com/")
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            dailySentenceApi = retrofit.create(DailySentenceApi.class);
        }
        return dailySentenceApi;
    }

    public static YoudaoWordApi getYoudaoWordApi() {
        if (youdaoWordApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("http://fanyi.youdao.com/")
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            youdaoWordApi = retrofit.create(YoudaoWordApi.class);
        }
        return youdaoWordApi;
    }

    public static GoogleTranslationApi getGoogleTranslationApi(){
        if (googleTranslationApi == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("https://translate.google.cn/translate_a/")
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            googleTranslationApi = retrofit.create(GoogleTranslationApi.class);
        }
        return googleTranslationApi;
    }

    public static YoudaoTranslationApi getYoudaoTranslationApi(){
        if (youdaoTranslationApi == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("http://fanyi.youdao.com/")
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            youdaoTranslationApi = retrofit.create(YoudaoTranslationApi.class);
        }
        return youdaoTranslationApi;
    }

    public static BaiduTranslationApi getBaiduTranslationApi() {
        if (baiduTranslationApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("http://api.fanyi.baidu.com/api/trans/vip/")
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            baiduTranslationApi = retrofit.create(BaiduTranslationApi.class);
        }
        return baiduTranslationApi;
    }

    public static UserInfoApi getUserInfoApi() {
        if (userInfoApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("https://api.shanbay.com/")
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            userInfoApi = retrofit.create(UserInfoApi.class);
        }
        return userInfoApi;
    }


    public static DailyVoiceApi getDailyVoiceApi() {
        if (dailyVoiceApi == null) {
            Retrofit retrofit =
                    new Retrofit.Builder()
                            .baseUrl("http://news.iciba.com/admin/tts/") // REMEMBER TO END with /
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
            dailyVoiceApi = retrofit.create(DailyVoiceApi.class);
        }
        return dailyVoiceApi;
    }

    public static WordApi getWordApi() {
        if (wordApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("https://api.shanbay.com/bdc/")
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            wordApi = retrofit.create(WordApi.class);
        }
        return wordApi;
    }

    public static AddWordApi getAddWordApi() {
        if (addWordApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("https://api.shanbay.com/bdc/")
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            addWordApi = retrofit.create(AddWordApi.class);
        }
        return addWordApi;
    }

    public static ForgetWordApi getForgetWordApi() {
        if (forgetWordApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("https://api.shanbay.com/bdc/")
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            forgetWordApi = retrofit.create(ForgetWordApi.class);
        }
        return forgetWordApi;
    }

    public static SentenceApi getSentenceApi() {
        if (sentenceApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("https://api.shanbay.com/bdc/")
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            sentenceApi = retrofit.create(SentenceApi.class);
        }
        return sentenceApi;
    }

    public static QueryVersionApi getQueryVersionApi() {
        if (queryVersionApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("http://api.fir.im/apps/latest/")
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            queryVersionApi = retrofit.create(QueryVersionApi.class);
        }
        return queryVersionApi;
    }
}
