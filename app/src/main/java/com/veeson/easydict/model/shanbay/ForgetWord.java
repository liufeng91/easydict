package com.veeson.easydict.model.shanbay;

/**
 * Created by Wilson on 2016/7/1.
 */
public class ForgetWord {

    /**
     * msg : SUCCESS
     * status_code : 0
     */

    public String msg;
    public int status_code;
    public DataEntity data;
    public static class DataEntity{
        public long id;
    }

    @Override
    public String toString() {
        return "msg: " + msg + " status_code: " + status_code + " id: " + data.id;
    }
}
