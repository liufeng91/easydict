package com.veeson.easydict.pinyin;


import com.veeson.easydict.model.YoudaoWord;

import java.util.Comparator;

/**
 * @author xiaanming
 */
public class PinyinComparator implements Comparator<YoudaoWord> {

    public int compare(YoudaoWord o1, YoudaoWord o2) {
        if (o1.sortLetters.equals("@")
                || o2.sortLetters.equals("#")) {
            return -1;
        } else if (o1.sortLetters.equals("#")
                || o2.sortLetters.equals("@")) {
            return 1;
        } else {
            return o1.sortLetters.compareTo(o2.sortLetters);
        }
    }

}
