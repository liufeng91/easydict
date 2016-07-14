package com.veeson.easydict;

import android.os.Environment;

/**
 * Created by Wilson on 2016/6/11.
 */
public class AppConstants {
    public static final String APP_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/EasyDict";
//    public static final String APP_IMAGE_DIR = APP_DIR + "/image";
    public static final String APP_VOICE_DIR = APP_DIR + "/voice";

    public static final String DAILY_SENTENCE_DATE = "DAILY_SENTENCE_DATE";
    public static final String DAILY_SENTENCE_CONTENT = "DAILY_SENTENCE_CONTENT";
    public static final String DAILY_SENTENCE_NOTE = "DAILY_SENTENCE_NOTE";

    public static final String APP_KEY = "f7564a58dd35913aa9e6";
//    public static final String APP_SECRET = "61970bf0b64ec3a0771872ec462f43f0f6edbe3c";
    public static final String SUFFIX_DENIED = "access_denied";
    public static final String URL_AUTH = "https://api.shanbay.com/oauth2/authorize/?client_id=%s&response_type=token&redirect_uri=%s";
    public static final String URL_CALLBACK = "https://api.shanbay.com/oauth2/auth/success/";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String USER_INFO = "user_info";
    public static final String LOGIN_SIGN = "login_sign";
    public static final String LOGIN_SHANBAY = "login_shanbay";

    public static final String LOGIN_WARN = "login_warn";
}
