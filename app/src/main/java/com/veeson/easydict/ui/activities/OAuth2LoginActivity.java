package com.veeson.easydict.ui.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.veeson.easydict.AppConstants;
import com.veeson.easydict.R;
import com.veeson.easydict.common.utils.PreferencesUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OAuth2LoginActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.web_view)
    WebView webView;

    private static final String TAG = "OAuth2LoginActivity";
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oauth2_login);
        ButterKnife.bind(this);
        mContext = this;

        initView();
    }

    private void initView() {
        setToolbar();
        LoginWebViewClient client = new LoginWebViewClient();
        webView.setWebViewClient(client);
//        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        String url = String.format(AppConstants.URL_AUTH, AppConstants.APP_KEY, AppConstants.URL_CALLBACK);
        webView.loadUrl(url);
    }

    @Override
    protected Toolbar getToolbar() {
        return toolbar;
    }

    private class LoginWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (url.endsWith(AppConstants.SUFFIX_DENIED)) {
                Toast.makeText(mContext, "登陆取消", Toast.LENGTH_SHORT).show();
                finish();
            } else if (url.startsWith(AppConstants.URL_CALLBACK)) {
                String args = url.split("#")[1];
                if (args.startsWith("access_token")) {
                    String access_token = args.split("&")[0].split("=")[1];
                    PreferencesUtils.putString(mContext, AppConstants.ACCESS_TOKEN, access_token);
                    PreferencesUtils.putBoolean(mContext, AppConstants.LOGIN_SIGN, true);
                    PreferencesUtils.putBoolean(mContext, AppConstants.LOGIN_SHANBAY, true); // 表示已经授权登录了扇贝账号
                    Toast.makeText(mContext, "登陆成功", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                } else {
                    Toast.makeText(mContext, "未授权", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        }
    }
}
