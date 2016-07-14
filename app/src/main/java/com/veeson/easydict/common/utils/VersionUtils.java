package com.veeson.easydict.common.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.veeson.easydict.R;
import com.veeson.easydict.model.fir.im.QueryVersion;
import com.veeson.easydict.network.Network;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Wilson on 2016/7/6.
 */
public class VersionUtils {
    public static void queryVersion(final Context context, Subscription subscription, final View view) {
        subscription = Network.getQueryVersionApi()
                .getQueryVersion("d9860778d39487cba68f067b848d4c5c")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<QueryVersion>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(context, R.string.bad_network, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(QueryVersion queryVersion) {
                        if (!queryVersion.versionShort.equals(VersionUtils.getVersionName(context))) {
                            showNewVersion(context, queryVersion);
                        } else {
                            Toast.makeText(context, "当前已是最新版本", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private static void showNewVersion(final Context context, final QueryVersion queryVersion) {
        new MaterialDialog.Builder(context)
                .title("检测到新版本V" + queryVersion.versionShort)
                .content("作者出新版本了，点击可直接下载。")
                .positiveText("好的")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        Uri uri = Uri.parse(queryVersion.direct_install_url); // missing 'http://' will cause crashed
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        context.startActivity(intent);
                        dialog.dismiss();
                    }
                })
                .show();

    }

    public static String getVersionName(Context context) {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        assert packInfo != null;
        return packInfo.versionName;
    }
}
