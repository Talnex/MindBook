package com.talnex.wrongsbook.Fragments;


import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.talnex.wrongsbook.R;


public class SettingFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_setting);
    }

}
