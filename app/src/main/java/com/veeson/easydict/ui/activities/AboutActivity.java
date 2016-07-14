package com.veeson.easydict.ui.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.veeson.easydict.R;
import com.veeson.easydict.ui.fragment.AboutFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        setToolbar();
        getFragmentManager().beginTransaction().replace(R.id.frame_layout, new AboutFragment()).commit();
    }

    @Override
    protected Toolbar getToolbar() {
        return toolbar;
    }
}
