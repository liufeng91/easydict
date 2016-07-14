package com.veeson.easydict.common.capsulation;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Locale;

/**
 * 音频播放管理类
 * Created by Wilson on 2016/6/12.
 */
public class PlayAudioManager {
    private static MediaPlayer mediaPlayer;
    private static TextToSpeech tts;

    public static void playAudio(final Context context, final String url) throws Exception {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, Uri.parse(url));
        }
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                killMediaPlayer();
            }
        });
        mediaPlayer.start();
    }

    private static void killMediaPlayer() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 播放英文发音
     *
     * @param word
     * @param voiceType
     */
    public static void playENPronVoice(final Context context, final String word, final String voiceType) {
        ((AppCompatActivity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, "正在获取发音...", Toast.LENGTH_SHORT).show();
                try {
                    PlayAudioManager.playAudio(context, "http://dict.youdao.com/dictvoice?audio=" + word + "&type=" + voiceType);
                } catch (Exception e) {
                    Toast.makeText(context, "获取发音失败,请检查网络设置", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 播放中文语音
     *
     * @param context
     * @param chinese
     */
    public static void playCNPronVoice(final Context context, final String chinese) {
        tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                        @Override
                        public void onStart(String utteranceId) {

                        }

                        @Override
                        public void onDone(String utteranceId) {
                            killTTS();
                        }

                        @Override
                        public void onError(String utteranceId) {
                            Toast.makeText(context, "发音失败", Toast.LENGTH_SHORT).show();
                            killTTS();
                        }
                    });
                    int result = tts.setLanguage(Locale.SIMPLIFIED_CHINESE);
                    if (result == TextToSpeech.LANG_MISSING_DATA ||
                            result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("error", "This Language is not supported");
                    } else {
                        ConvertTextToSpeech(context, chinese);
                    }
                } else
                    Log.e("error", "Initilization Failed!");
            }
        });
    }

    private static void killTTS() {
        if (tts != null) {

            tts.stop();
            tts.shutdown();
        }
    }

    private static void ConvertTextToSpeech(Context context, String s) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ttsGreater21(context, s);
        } else {
            ttsUnder20(s);
        }
    }

    @SuppressWarnings("deprecation")
    private static void ttsUnder20(String text) {
        HashMap<String, String> map = new HashMap<>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, map);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void ttsGreater21(Context context, String text) {
        String utteranceId = context.hashCode() + "";
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
    }
}
