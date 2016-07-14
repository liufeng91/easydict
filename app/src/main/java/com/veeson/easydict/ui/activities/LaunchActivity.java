package com.veeson.easydict.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.veeson.easydict.AppConstants;
import com.veeson.easydict.R;
import com.veeson.easydict.common.utils.NetWorkUtils;
import com.veeson.easydict.common.utils.PreferencesUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LaunchActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.tv_no_account)
    TextView tvNoAccount;
    @BindView(R.id.tv_login_account)
    TextView tvLoginAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        ButterKnife.bind(this);
        if (PreferencesUtils.getBoolean(this, AppConstants.LOGIN_SHANBAY) || PreferencesUtils.getBoolean(this, AppConstants.LOGIN_WARN)){
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            //activity切换的淡入淡出效果
//            overridePendingTransition(0, 0);
            finish();
        }
        setListener();
    }

    private void setListener() {
        tvNoAccount.setOnClickListener(this);
        tvLoginAccount.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_no_account:
                PreferencesUtils.putBoolean(LaunchActivity.this, AppConstants.LOGIN_WARN, true);
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                //activity切换的淡入淡出效果
//                overridePendingTransition(0, 0);
                finish();
                break;
            case R.id.tv_login_account:
                if (NetWorkUtils.isOnline(this)) {
                    startActivityForResult(new Intent(this, OAuth2LoginActivity.class), 3);
                } else {
                    Toast.makeText(LaunchActivity.this, R.string.bad_network, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 3:
                if (resultCode == RESULT_OK) {
                    startActivity(new Intent(this, MainActivity.class));
                }
                break;
        }
    }

    @Override
    protected Toolbar getToolbar() {
        return null;
    }
}
