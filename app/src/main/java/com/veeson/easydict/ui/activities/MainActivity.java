package com.veeson.easydict.ui.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.veeson.easydict.R;
import com.veeson.easydict.ui.fragment.HomeFragment;
import com.veeson.easydict.ui.fragment.MeFragment;
import com.veeson.easydict.ui.fragment.TranslationFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tab_menu_home)
    LinearLayout tabMenuHome;
    @BindView(R.id.tv_menu_home)
    TextView tvMenuHome;
    @BindView(R.id.tab_menu_translation)
    LinearLayout tabMenuTranslation;
    @BindView(R.id.tv_menu_translation)
    TextView tvMenuTranslation;
    @BindView(R.id.tab_menu_me)
    LinearLayout tabMenuMe;
    @BindView(R.id.tv_menu_me)
    TextView tvMenuMe;
//    private ImageView tab_menu_setting_partner;

    //    private final static String TAG = "MainActivity";
    private HomeFragment homeFragment;
    private TranslationFragment translationFragment;
    private MeFragment meFragment;
    private FragmentManager mFragmentManager;

    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        mFragmentManager = getFragmentManager();
        setListener();
        tabMenuHome.performClick();
    }

    private void setListener() {
        tabMenuHome.setOnClickListener(this);
        tabMenuTranslation.setOnClickListener(this);
        tabMenuMe.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab_menu_home:
                setTabSelection(0);
                break;
            case R.id.tab_menu_translation:
                setTabSelection(1);
                break;
            case R.id.tab_menu_me:
                setTabSelection(3);
//                tab_menu_setting_partner.setVisibility(View.INVISIBLE);
                break;
        }
    }

    /**
     * 根据传入的index参数来设置选中的tab页,0表示"首页"Fragment，1表示"翻译"Fragment,2表示"发现"Fragment,3表示"我的"Fragment.
     *
     * @param index
     */
    private void setTabSelection(int index) {
        setTextSelection();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (index) {
            case 0:
                tvMenuHome.setSelected(true);
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.fragment_container, homeFragment).commit();
                } else {
                    transaction.show(homeFragment).commit();
                }
                break;
            case 1:
                tvMenuTranslation.setSelected(true);
                if (translationFragment == null) {
                    translationFragment = TranslationFragment.newInstance("");
                    transaction.add(R.id.fragment_container, translationFragment).commit();
                } else {
                    transaction.show(translationFragment).commit();
                }
                break;
            case 3:
                tvMenuMe.setSelected(true);
                if (meFragment == null) {
                    meFragment = new MeFragment();
                    transaction.add(R.id.fragment_container, meFragment).commit();
                } else {
                    transaction.show(meFragment).commit();
                }
            default:
                break;
        }
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if (translationFragment != null) {
            transaction.hide(translationFragment);
        }
        if (meFragment != null) {
            transaction.hide(meFragment);
        }
    }

    /**
     * 重置所有文本的选中状态
     */
    private void setTextSelection() {
        tvMenuHome.setSelected(false);
        tvMenuTranslation.setSelected(false);
//        tvMenuDiscovery.setSelected(false);
        tvMenuMe.setSelected(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    setTextSelection();
                    FragmentTransaction transaction = mFragmentManager.beginTransaction();
                    // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
                    hideFragments(transaction);
                    tvMenuTranslation.setSelected(true);
                    translationFragment = TranslationFragment.newInstance(data.getStringExtra("to_trans"));
                    transaction.add(R.id.fragment_container, translationFragment).commit();
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    setTextSelection();
                    FragmentTransaction transaction = mFragmentManager.beginTransaction();
                    // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
                    hideFragments(transaction);
                    tvMenuMe.setSelected(true);
                    meFragment = new MeFragment();
                    transaction.add(R.id.fragment_container, meFragment).commit();
                }
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    protected Toolbar getToolbar() {
        return null;
    }
}
