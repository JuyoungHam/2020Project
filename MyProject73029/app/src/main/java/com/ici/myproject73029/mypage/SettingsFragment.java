package com.ici.myproject73029.mypage;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.ici.myproject73029.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }
}