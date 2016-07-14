package com.veeson.easydict.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;

import com.veeson.easydict.R;

import rx.Subscription;

/**
 * Created by Wilson on 2016/6/13.
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected Subscription subscription;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unsubscribe();
    }

    protected void unsubscribe() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    protected void setToolbar() {
        Toolbar toolbar = getToolbar();
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_arrows);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            // make toggle drawable center-vertical, you can make each view alignment whatever you want
            Toolbar.LayoutParams lp = (Toolbar.LayoutParams) toolbar.getChildAt(i).getLayoutParams();
            lp.gravity = Gravity.CENTER_VERTICAL;
        }
    }

    protected abstract Toolbar getToolbar();
}
