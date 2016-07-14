package com.veeson.easydict.model;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * 存储翻译历史记录的实体类
 * Created by Wilson on 2016/6/22.
 */
public class TranslationRecord implements Comparable<TranslationRecord>, Serializable {
    public String original;
    public String result;
    public String date;
    public String fromTo;

    @Override
    public int compareTo(@NonNull TranslationRecord o) {
        return date.compareTo(o.date);
    }

    @Override
    public String toString() {
        return original + " " + result + " " + date + " " + fromTo;
    }
}
