package com.veeson.easydict.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.veeson.easydict.AppConstants;
import com.veeson.easydict.R;
import com.veeson.easydict.common.utils.PreferencesUtils;
import com.veeson.easydict.model.shanbay.UserInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AccountActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.cv_shanbay)
    ImageView cvShanbay;
    @OnClick(R.id.bt_logout)
    public void logout(View view) {
        PreferencesUtils.removeKey(this, AppConstants.ACCESS_TOKEN);
        PreferencesUtils.removeKey(this, AppConstants.USER_INFO);
        PreferencesUtils.removeKey(this, AppConstants.LOGIN_SIGN);
        PreferencesUtils.removeKey(this, AppConstants.LOGIN_SHANBAY);
        PreferencesUtils.removeKey(this, AppConstants.LOGIN_WARN);
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ButterKnife.bind(this);
        setToolbar();
        Gson gson = new Gson();
        UserInfo userInfo = gson.fromJson(getIntent().getStringExtra("user_info"), UserInfo.class);
        Glide.with(this).load(userInfo.avatar).into(cvShanbay);
        tvUsername.setText(userInfo.username);
        tvNickname.setText(userInfo.nickname);
    }

    @Override
    protected Toolbar getToolbar() {
        return toolbar;
    }
}
