package com.veeson.easydict.model.shanbay;

import java.io.Serializable;

/**
 * Created by Wilson on 2016/6/30.
 */
public class UserInfo implements Serializable {
    public String username;
    public String nickname;
    public String id;
    public String avatar;

    @Override
    public String toString() {
        return username + "   " + nickname + "   " + id + "   " + avatar;
    }
}
