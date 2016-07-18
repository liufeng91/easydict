package com.veeson.easydict.ui.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.veeson.easydict.AppConstants;
import com.veeson.easydict.R;
import com.veeson.easydict.common.capsulation.PlayAudioManager;
import com.veeson.easydict.common.utils.ACache;
import com.veeson.easydict.common.capsulation.DownloadManager;
import com.veeson.easydict.common.utils.FileUtils;
import com.veeson.easydict.common.utils.StringUtils;
import com.veeson.easydict.common.utils.TimeUtils;
import com.veeson.easydict.model.DailySentence;
import com.veeson.easydict.network.Network;
import com.veeson.easydict.ui.activities.BaseActivity;
import com.veeson.easydict.ui.activities.SearchWordActivity;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.daily_en)
    TextView dailyEn;
    @BindView(R.id.daily_cn)
    TextView dailyCn;
    @BindView(R.id.daily_voice)
    ImageView dailyVoice;
    @BindView(R.id.card_view)
    CardView searchWordButton;

    private ACache mCache;
    private static final String TAG = "HomeFragment";
    Observer<DailySentence> observer = new Observer<DailySentence>() {
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, e.toString());
        }

        @Override
        public void onNext(DailySentence dailySentence) {
            mCache.clear();
            mCache.put(AppConstants.DAILY_SENTENCE_CONTENT, dailySentence.content);
            mCache.put(AppConstants.DAILY_SENTENCE_NOTE, dailySentence.note);
            mCache.put(AppConstants.DAILY_SENTENCE_DATE, dailySentence.dateline);
            dailyEn.setText(dailySentence.content);
            dailyCn.setText(dailySentence.note);
        }
    };

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        mCache = ACache.get(getActivity());
        setListener();
        getDailySentenceContent();
        return view;
    }

    private void setListener() {
        dailyVoice.setOnClickListener(this);
        searchWordButton.setOnClickListener(this);
    }

    private void getDailySentenceContent() {
        String date = TimeUtils.getTime(TimeUtils.getCurrentTimeInLong(), TimeUtils.DATE_FORMAT_DATE);
        String cacheDate = mCache.getAsString(AppConstants.DAILY_SENTENCE_DATE);
        if (cacheDate != null) {
            if (cacheDate.equals(date)) {
                dailyEn.setText(mCache.getAsString(AppConstants.DAILY_SENTENCE_CONTENT));
                dailyCn.setText(mCache.getAsString(AppConstants.DAILY_SENTENCE_NOTE));
                return;
            } else {
                FileUtils.deleteFile(AppConstants.APP_VOICE_DIR + "/" + cacheDate + ".mp3");
            }
        }
        subscription = Network.getDailySentenceApi()
                .getDailySentence(date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void getDailyVoice() throws Exception {
        String date = mCache.getAsString(AppConstants.DAILY_SENTENCE_DATE);
        String dailyVoiceDir = AppConstants.APP_VOICE_DIR + "/" + date + ".mp3";
        File file = new File(dailyVoiceDir);
        if (file.exists()) {
            PlayAudioManager.playAudio(getActivity(), dailyVoiceDir);
        } else {
            DownloadManager.downloadDailyVoice(getActivity(), date);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.daily_voice:
                try {
                    getDailyVoice();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.card_view:
                Intent intent = new Intent(getActivity(), SearchWordActivity.class);
//                startActivityForResult(intent, 1);
//                如果直接调用startActivityForResult()，那么回调的就是fragment里面的onActivityResult()方法。
//                加上getActivity().则回调的是activity里面的onActivityResult()方法。
                getActivity().startActivityForResult(intent, 1);
                break;
        }
    }

}
