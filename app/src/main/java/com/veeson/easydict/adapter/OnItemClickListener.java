package com.veeson.easydict.adapter;

import android.view.View;

/**
 * Created by Wilson on 2016/6/23.
 */
public interface OnItemClickListener {
    void onItemClick(View view, int position);

    void onItemLongClick(View view, int position);
}
