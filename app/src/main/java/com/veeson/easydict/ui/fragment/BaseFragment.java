package com.veeson.easydict.ui.fragment;

import android.app.Fragment;

import rx.Subscription;

/**
 * Created by Wilson on 2016/6/10.
 */
public class BaseFragment extends Fragment {
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
