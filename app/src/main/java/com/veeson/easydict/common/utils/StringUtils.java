package com.veeson.easydict.common.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Wilson on 2016/6/17.
 */
public class StringUtils {

    /**
     * 将带有" "的字符串连成一起："what is you name" ---> "whatisyouname"
     * @param str
     * @return
     */
    public static String arraysToString(String str){
        StringBuilder builder = new StringBuilder();
        for(String s : str.split(" ")) {
            builder.append(s);
        }
        return builder.toString();
    }

    /**
     * 只要字符串有中文则为中文
     *
     * @param str
     * @return
     */
    public static String checkString(String str) {
        String res = "";
        if (str != null) {
            for (int i = 0; i < str.length(); i++) {
                // 只要字符串中有中文则为中文
                if (!checkChar(str.charAt(i))) {
                    res = "zh";
                    break;
                } else {
                    res = "en";
                }
            }
        }
        return res;
    }

    /**
     * 英文占1byte，非英文（可认为是中文）占2byte，根据这个特性来判断字符
     *
     * @param ch
     * @return
     */
    public static boolean checkChar(char ch) {
        if ((ch + "").getBytes().length == 1) {
            return true;//英文
        } else {
            return false;//中文
        }
    }

    /**
     * 检测文本是不是全为中文
     *
     * @param s
     * @return
     */
    public static boolean ifAllChinese(String s) {
        boolean isAllChinese = true;
        Character.UnicodeBlock ub;
        for (char c : s.toCharArray()) {
            ub = Character.UnicodeBlock.of(c);
            if (!(ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                    || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                    || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                    || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                    || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS)) {
                isAllChinese = false;
                break;
            }
        }
        return isAllChinese;
    }

    public static boolean ifHaveChinese(String s){
        boolean ifHaveChinese = false;
        Character.UnicodeBlock ub;
        for (char c : s.toCharArray()) {
            ub = Character.UnicodeBlock.of(c);
            if ((ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                    || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                    || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                    || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                    || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS)) {
                ifHaveChinese = true;
                break;
            }
            ifHaveChinese = false;
        }
        return ifHaveChinese;
    }

    /**
     * 判断是不是全为英文字母
     *
     * @param charaString
     * @return
     */
    public static boolean isEnglish(String charaString) {
        return charaString.matches("^[a-zA-Z]*");
    }

    /**
     * 复制内容到Clipboard
     *
     * @param text
     */
    public static void copyToClipboard(Context context, String text) {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
        clipboard.setPrimaryClip(clip);
        if (!TextUtils.isEmpty(clipboard.getPrimaryClip().getItemAt(0).getText())) {
            Toast.makeText(context, "复制成功", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 分享文本给其他应用程序
     *
     * @param text
     */
    public static void shareToApps(Context context, String text) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(sendIntent, "分享到"));
    }

    /**
     * 移除扇贝例句中的标签<vocab>和</vocab>
     * @param raw
     * @return
     */
    public static String removeTags(String raw){
        String reg = "<[^>]*>";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(raw);
        return matcher.replaceAll("");
    }

}
