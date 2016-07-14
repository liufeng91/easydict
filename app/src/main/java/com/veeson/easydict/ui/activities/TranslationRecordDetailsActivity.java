package com.veeson.easydict.ui.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.veeson.easydict.R;
import com.veeson.easydict.model.TranslationRecord;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TranslationRecordDetailsActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_trans_original)
    TextView tvTransOriginal;
    @BindView(R.id.tv_trans_result)
    TextView tvTransResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translation_record_details);
        ButterKnife.bind(this);

        setToolbar();

        tvTransOriginal.setMovementMethod(new ScrollingMovementMethod()); // 设置TextView可滚动
        tvTransResult.setMovementMethod(new ScrollingMovementMethod());
        tvTransOriginal.setTextIsSelectable(true); // 设置文本可选
        tvTransResult.setTextIsSelectable(true);
        TranslationRecord translationRecord = (TranslationRecord) getIntent().getSerializableExtra("trans_record_data");
        tvTransOriginal.setText(translationRecord.original);
        tvTransResult.setText(translationRecord.result);
    }

    @Override
    protected Toolbar getToolbar() {
        return toolbar;
    }
}
