package com.example.chatchatapplication.Activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.example.chatchatapplication.R;

public class Setting extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

    }
}
