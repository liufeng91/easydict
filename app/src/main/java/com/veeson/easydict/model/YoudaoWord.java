package com.veeson.easydict.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Wilson on 2016/6/12.
 */
public class YoudaoWord implements Serializable {
    @SerializedName("errorCode")
    public String errorCode;
    @SerializedName("web")
    public List<WebEntity> web;
    @SerializedName("translation")
    public List<String> translation;
    @SerializedName("basic")
    public BasicEntity basic;
    @SerializedName("query")
    public String query;

    public static class WebEntity implements Serializable {
        @SerializedName("value")
        public List<String> value;
        @SerializedName("key")
        public String key;

        @Override
        public String toString() {
            return " value: " + value.toString() + " key: " + key;
        }
    }

    public static class BasicEntity implements Serializable {
        @SerializedName("uk-speech")
        public String uk_speech;
        @SerializedName("us-phonetic")
        public String us_phonetic;
        @SerializedName("speech")
        public String speech;
        @SerializedName("phonetic")
        public String phonetic;
        @SerializedName("explains")
        public List<String> explains;
        @SerializedName("uk-phonetic")
        public String uk_phonetic;
        @SerializedName("us-speech")
        public String us_speech;

        @Override
        public String toString() {
            return " uk_speech: " + uk_speech + " uk_phonetic: " + uk_phonetic + " us_phonetic: " + us_phonetic + " speed: " + speech + " phonetic: " + phonetic + " uk_phonetic: " + uk_phonetic + " us_speech: " + us_speech;
        }
    }

    @Expose(serialize = false, deserialize = false)
    public String sortLetters;

    @Override
    public String toString() {
        return "query: " + query + "errorCode: " + errorCode + " web: " + web.toString() + " translation: " + translation + " basic: " + basic;
    }
}
