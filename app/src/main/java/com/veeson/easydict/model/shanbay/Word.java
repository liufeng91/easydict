package com.veeson.easydict.model.shanbay;

import java.util.List;

/**
 * Created by Wilson on 2016/7/1.
 */
public class Word {


    /**
     * msg : SUCCESS
     * status_code : 0
     * data : {"pronunciations":{"uk":"wɜːd","us":"wɜːrd"},"en_definitions":{"v":["put into words or an expression"],"n":["a unit of language that native speakers can identify","a brief statement","information about recent and important events"]},"content_id":312,"audio_addresses":{"uk":["https://words-audio.oss.aliyuncs.com/uk%2Fw%2Fwo%2Fword.mp3","http://words-audio.cdn.shanbay.com/uk/w/wo/word.mp3"],"us":["https://words-audio.oss.aliyuncs.com/us%2Fw%2Fwo%2Fword.mp3","http://words-audio.cdn.shanbay.com/us/w/wo/word.mp3"]},"en_definition":{"pos":"v","defn":"put into words or an expression"},"uk_audio":"http://media.shanbay.com/audio/uk/word.mp3","has_audio":true,"conent_id":312,"pronunciation":"wɜːrd","audio_name":"word","content":"word","pron":"wɜːrd","num_sense":1,"object_id":312,"content_type":"vocabulary","definition":" n. 词, 单词, 消息, 诺言\nvt. 用词语表达","sense_id":0,"audio":"http://media.shanbay.com/audio/us/word.mp3","id":312,"cn_definition":{"pos":"","defn":"n. 词, 单词, 消息, 诺言\nvt. 用词语表达"},"us_audio":"http://media.shanbay.com/audio/us/word.mp3"}
     */

    public String msg;
    public int status_code;
    /**
     * pronunciations : {"uk":"wɜːd","us":"wɜːrd"}
     * en_definitions : {"v":["put into words or an expression"],"n":["a unit of language that native speakers can identify","a brief statement","information about recent and important events"]}
     * content_id : 312
     * audio_addresses : {"uk":["https://words-audio.oss.aliyuncs.com/uk%2Fw%2Fwo%2Fword.mp3","http://words-audio.cdn.shanbay.com/uk/w/wo/word.mp3"],"us":["https://words-audio.oss.aliyuncs.com/us%2Fw%2Fwo%2Fword.mp3","http://words-audio.cdn.shanbay.com/us/w/wo/word.mp3"]}
     * en_definition : {"pos":"v","defn":"put into words or an expression"}
     * uk_audio : http://media.shanbay.com/audio/uk/word.mp3
     * has_audio : true
     * conent_id : 312
     * pronunciation : wɜːrd
     * audio_name : word
     * content : word
     * pron : wɜːrd
     * num_sense : 1
     * object_id : 312
     * content_type : vocabulary
     * definition :  n. 词, 单词, 消息, 诺言
     * vt. 用词语表达
     * sense_id : 0
     * audio : http://media.shanbay.com/audio/us/word.mp3
     * id : 312
     * cn_definition : {"pos":"","defn":"n. 词, 单词, 消息, 诺言\nvt. 用词语表达"}
     * us_audio : http://media.shanbay.com/audio/us/word.mp3
     */

    public DataEntity data;

    public static class DataEntity {
        /**
         * uk : wɜːd
         * us : wɜːrd
         */

        public PronunciationsEntity pronunciations;
        public EnDefinitionsEntity en_definitions;
        public int content_id;
        public AudioAddressesEntity audio_addresses;
        /**
         * pos : v
         * defn : put into words or an expression
         */

        public EnDefinitionEntity en_definition;
        public String uk_audio;
        public boolean has_audio;
        public int conent_id;
        public String pronunciation;
        public String audio_name;
        public String content;
        public String pron;
        public int num_sense;
        public int object_id;
        public String content_type;
        public String definition;
        public int sense_id;
        public String audio;
        public int id;
        /**
         * pos :
         * defn : n. 词, 单词, 消息, 诺言
         * vt. 用词语表达
         */

        public CnDefinitionEntity cn_definition;
        public String us_audio;

        public static class PronunciationsEntity {
            public String uk;
            public String us;
        }

        public static class EnDefinitionsEntity {
            public List<String> v;
            public List<String> n;
        }

        public static class AudioAddressesEntity {
            public List<String> uk;
            public List<String> us;
        }

        public static class EnDefinitionEntity {
            public String pos;
            public String defn;
        }

        public static class CnDefinitionEntity {
            public String pos;
            public String defn;
        }
    }
}
