package com.veeson.easydict.common.capsulation;

import android.content.Context;
import android.widget.Toast;

import com.veeson.easydict.AppConstants;
import com.veeson.easydict.network.Network;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 下载管理类
 * Created by Wilson on 2016/6/11.
 */
public class DownloadManager {

    /**
     * 下载每日一句英文发音文件
     *
     * @param date
     * @return
     */
    public static void downloadDailyVoice(final Context context, final String date) {
        Network.getDailyVoiceApi().downloadDailyVoice("http://news.iciba.com/admin/tts/" + date + "-day.mp3").enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(context, "正在获取发音...", Toast.LENGTH_SHORT).show();
                FileOutputStream fos = null;
                InputStream is = null;
                try {
                    String file_path = AppConstants.APP_VOICE_DIR;
                    File dir = new File(file_path);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    File file = new File(dir, date + ".mp3");
                    fos = new FileOutputStream(file);
                    is = response.body().byteStream();
                    byte[] buffer = new byte[1024];
                    int len1 = 0;
                    while ((len1 = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, len1);
                    }
                    PlayAudioManager.playAudio(context, AppConstants.APP_VOICE_DIR + "/" + date + ".mp3");
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "获取发音失败,请检查网络设置", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
