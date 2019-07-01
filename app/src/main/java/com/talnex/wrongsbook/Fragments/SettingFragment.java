package com.talnex.wrongsbook.Fragments;


import android.os.Bundle;
import android.support.v14.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;

import com.talnex.wrongsbook.R;


public class SettingFragment extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new PrefenceFragment()).commit();
    }

    public static class PrefenceFragment extends PreferenceFragment{

        @Override
        public void onCreatePreferences(Bundle bundle, String s) {
            addPreferencesFromResource(R.xml.preference_setting);
        }
    }
}
