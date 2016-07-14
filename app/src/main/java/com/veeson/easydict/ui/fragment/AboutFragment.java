package com.veeson.easydict.ui.fragment;

import android.os.Bundle;
import android.preference.Preference;

import com.veeson.easydict.R;
import com.veeson.easydict.common.capsulation.EasyDictUtils;
import com.veeson.easydict.common.utils.VersionUtils;

/**
 * Created by Wilson on 2016/7/6.
 */
public class AboutFragment extends BasePreferenceFragment implements Preference.OnPreferenceClickListener {
    private Preference versionUpdate;
    private Preference aboutAuthor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.about);
        versionUpdate = findPreference("version_update");
        aboutAuthor = findPreference("about_author");
        versionUpdate.setSummary("version-" + VersionUtils.getVersionName(getActivity()));
        aboutAuthor.setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (aboutAuthor == preference) {
            EasyDictUtils.sendEmailToAuthor(getActivity());
        }
        return false;
    }
}
