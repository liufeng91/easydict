package com.veeson.easydict.model.shanbay;

import java.util.List;

/**
 * Created by Wilson on 2016/7/1.
 */
public class Sentence {
    public String status_code;
    public String msg;
    public List<DataEntity> data;

    public static class DataEntity {
        public String id;
        public UserEntity user;
        public String unlikes;
        public String likes;
        public String translation;
        public String annotation;
        public String version;

        public static class UserEntity {
            public String username;
            public String nickname;
            public String id;
            public String avatar;
        }
    }
}
