package com.veeson.easydict.model;

import java.util.List;

/**
 * Created by Wilson on 2016/6/16.
 */
public class BaiduTranslation {
    public String from;
    public String to;
    public List<TransResultEntity> trans_result;
    public static class TransResultEntity{
        public String src;
        public String dst;
    }
}
