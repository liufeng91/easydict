package com.veeson.easydict.ui.fragment;

import android.preference.PreferenceFragment;

import rx.Subscription;

/**
 * Created by Wilson on 2016/7/6.
 */
public class BasePreferenceFragment extends PreferenceFragment {
    protected Subscription subscription;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unsubscribe();
    }

    protected void unsubscribe() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
