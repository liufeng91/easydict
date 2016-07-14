package com.veeson.easydict.network.api;

import com.veeson.easydict.model.BaiduTranslation;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Wilson on 2016/6/16.
 */
public interface BaiduTranslationApi {
    // http://api.fanyi.baidu.com/api/trans/vip/translate?
    // q=apple&fromTo=en&to=zh&appid=2015063000000001&salt=1435660288&sign=f89f9594663708c1605f3d736d01d2d4
    @GET("translate")
    Observable<BaiduTranslation> getBaiduTranslation(@Query("q") String q, @Query("from") String from, @Query("to") String to, @Query("appid") String appid, @Query("salt") String salt, @Query("sign") String sign);
}
