package com.veeson.easydict.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.veeson.easydict.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BlankActivity extends BaseActivity {
    @BindView(R.id.tv_expand_result)
    TextView tvExpandResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank);
        ButterKnife.bind(this);
        tvExpandResult.setMovementMethod(new ScrollingMovementMethod()); // 设置TextView可滚动
        tvExpandResult.setTextIsSelectable(true); // 设置文本可选
        String transResult = getIntent().getStringExtra("trans_result");
        tvExpandResult.setText(transResult);
    }

    @Override
    protected Toolbar getToolbar() {
        return null;
    }
}
