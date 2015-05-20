package com.uvdoha.trelolo;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by dmitry on 20.05.15.
 */
public class PreferencesActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.activity_pref);
    }
}
