package com.veeson.easydict.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Wilson on 2016/6/10.
 */
public class DailySentence implements Serializable {
    public String sid;
    public String tts;
    public String content;
    public String note;
    public String love;
    public String translation;
    public String picture;
    public String picture2;
    public String caption;
    public String dateline;
    public String s_pv;
    public String sp_pv;
    public List<TagsEntity> tags;
    public String fenxiang_img;

    public static class TagsEntity {
        public String id;
        public String name;
    }
}
