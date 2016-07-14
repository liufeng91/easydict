package com.veeson.easydict.model.fir.im;

/**
 * Created by Wilson on 2016/7/6.
 */
public class QueryVersion {

    /**
     * name : EasyDict
     * version : 1
     * changelog : First release
     * updated_at : 1467095268
     * versionShort : 1.0
     * build : 1
     * installUrl : http://download.fir.im/v2/app/install/577218d9e75e2d73dc000007?download_token=d003c1a9f894526d7a487c42a3c5b4b7
     * install_url : http://download.fir.im/v2/app/install/577218d9e75e2d73dc000007?download_token=d003c1a9f894526d7a487c42a3c5b4b7
     * direct_install_url : http://download.fir.im/v2/app/install/577218d9e75e2d73dc000007?download_token=d003c1a9f894526d7a487c42a3c5b4b7
     * update_url : http://fir.im/easydict
     * binary : {"fsize":2806320}
     */

    public String name;
    public String version;
    public String changelog;
    public long updated_at;
    public String versionShort;
    public String build;
    public String installUrl;
    public String install_url;
    public String direct_install_url;
    public String update_url;
    /**
     * fsize : 2806320
     */

    public BinaryEntity binary;

    public static class BinaryEntity {
        public long fsize;
    }
}
