package com.veeson.easydict.common.capsulation;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

/**
 * Created by Wilson on 2016/7/6.
 */
public class EasyDictUtils {

    /**
     * 发送邮件
     */
    public static void sendEmailToAuthor(Context context) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"weishengliu@foxmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "EasyDict意见反馈");
        i.putExtra(Intent.EXTRA_TEXT, "");
        try {
            context.startActivity(Intent.createChooser(i, "选择发送邮件的应用"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "没有找到邮件客户端", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 发送短信
     * @param context
     */
    public static void sendSMS(Context context, String msg){
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse("sms:"));
        sendIntent.putExtra("sms_body", msg);
        context.startActivity(sendIntent);
    }
}
