package com.veeson.easydict.ui.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.veeson.easydict.AppConstants;
import com.veeson.easydict.R;
import com.veeson.easydict.common.capsulation.EasyDictUtils;
import com.veeson.easydict.common.utils.NetWorkUtils;
import com.veeson.easydict.common.utils.PreferencesUtils;
import com.veeson.easydict.common.utils.StringUtils;
import com.veeson.easydict.common.utils.VersionUtils;
import com.veeson.easydict.model.shanbay.UserInfo;
import com.veeson.easydict.network.Network;
import com.veeson.easydict.ui.activities.AboutActivity;
import com.veeson.easydict.ui.activities.AccountActivity;
import com.veeson.easydict.ui.activities.CollectionWordActivity;
import com.veeson.easydict.ui.activities.OAuth2LoginActivity;
import com.veeson.easydict.ui.activities.TranslationRecordActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.tv_record)
    TextView tvRecord;
    @BindView(R.id.tv_notebook)
    TextView tvNotebook;
    @BindView(R.id.cv_shanbay)
    ImageView cvShanbay;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.ly_update)
    LinearLayout tvUpdate;
    @BindView(R.id.ly_about)
    LinearLayout tvAbout;
    @BindView(R.id.ly_like)
    LinearLayout tvLike;
    @BindView(R.id.ly_share_software)
    LinearLayout tvShareSoftware;
    @BindView(R.id.ly_email)
    LinearLayout tvEmail;

    private static final String TAG = "MeFragment";

    Observer<UserInfo> userInfoObserver = new Observer<UserInfo>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, e.toString());
        }

        @Override
        public void onNext(UserInfo userInfo) {
            Glide.with(getActivity()).load(userInfo.avatar).into(cvShanbay);
            tvUserName.setText(userInfo.username);
            Gson gson = new Gson();
            PreferencesUtils.putString(getActivity(), AppConstants.USER_INFO, gson.toJson(userInfo));
        }
    };

    public MeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        ButterKnife.bind(this, view);
        if (PreferencesUtils.getBoolean(getActivity(), AppConstants.LOGIN_SIGN)) {
            PreferencesUtils.putBoolean(getActivity(), AppConstants.LOGIN_SIGN, false);
            getUserInfo();
        }
        String gsonString = PreferencesUtils.getString(getActivity(), AppConstants.USER_INFO);
        if (gsonString != null) {
            Gson gson = new Gson();
            UserInfo userInfo = gson.fromJson(gsonString, UserInfo.class);
            Glide.with(getActivity()).load(userInfo.avatar).into(cvShanbay);
            tvUserName.setText(userInfo.username);
        }
        setListener();
        return view;
    }

    private void getUserInfo() {
        subscription = Network.getUserInfoApi()
                .getUserInfo(PreferencesUtils.getString(getActivity(), AppConstants.ACCESS_TOKEN))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userInfoObserver);
    }

    private void setListener() {
        tvNotebook.setOnClickListener(this);
        tvRecord.setOnClickListener(this);
        cvShanbay.setOnClickListener(this);
        tvUpdate.setOnClickListener(this);
        tvAbout.setOnClickListener(this);
        tvLike.setOnClickListener(this);
        tvShareSoftware.setOnClickListener(this);
        tvEmail.setOnClickListener(this);
    }

    private void showShareOption() {
        new MaterialDialog.Builder(getActivity())
                .title("分享给好友")
                .items(R.array.share_option)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        switch (which) {
                            case 0:
                                StringUtils.copyToClipboard(getActivity(), getResources().getString(R.string.share_msg));
                                break;
                            case 1:
                                EasyDictUtils.sendSMS(getActivity(), getResources().getString(R.string.share_msg));
                                break;
                            case 2:
                                StringUtils.shareToApps(getActivity(), getResources().getString(R.string.share_msg));
                                break;
                        }
                    }
                })
                .show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cv_shanbay:
                if (!PreferencesUtils.getBoolean(getActivity(), AppConstants.LOGIN_SHANBAY)) {
                    if (NetWorkUtils.isOnline(getActivity())) {
                        getActivity().startActivityForResult(new Intent(getActivity(), OAuth2LoginActivity.class), 2);
                    } else {
                        Toast.makeText(getActivity(), R.string.bad_network, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Intent intent = new Intent(getActivity(), AccountActivity.class);
                    intent.putExtra("user_info", PreferencesUtils.getString(getActivity(), AppConstants.USER_INFO));
                    startActivity(intent);
                }
                break;
            case R.id.tv_notebook:
                startActivity(new Intent(getActivity(), CollectionWordActivity.class));
                break;
            case R.id.tv_record:
                startActivity(new Intent(getActivity(), TranslationRecordActivity.class));
                break;
            case R.id.ly_update:
                VersionUtils.queryVersion(getActivity(), subscription, getView());
                break;
            case R.id.ly_about:
                startActivity(new Intent(getActivity(), AboutActivity.class));
                break;
            case R.id.ly_like:
                break;
            case R.id.ly_share_software:
                showShareOption();
                break;
            case R.id.ly_email:
                EasyDictUtils.sendEmailToAuthor(getActivity());
                break;
        }
    }
}
