package com.veeson.easydict.model;

import java.util.List;

/**
 * Created by Wilson on 2016/6/17.
 */
public class GoogleTranslation {
    public List<SentencesEntity> sentences;
    public String src;
    public String confidence;
    public Ld_resultEntity ld_result;

    public static class SentencesEntity {
        public String trans;
        public String orig;
        public String backend;
    }

    public static class Ld_resultEntity {
        public List<String> srclangs;
        public List<String> srclangs_confidences;
        public List<String> extended_srclangs;
    }
}
