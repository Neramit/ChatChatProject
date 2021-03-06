package com.example.chatchatapplication.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import com.example.chatchatapplication.R;

import java.util.Objects;

public class Setting extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    // Shared preferrence
    SharedPreferences sp;
    SharedPreferences.Editor mEdit1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        mEdit1 = sp.edit();
        String theme = sp.getString("Theme", "Green");
        switch (theme) {
            case "Blue":
                setTheme(R.style.Blue_NoActionBar);
                break;
            case "Pink":
                setTheme(R.style.Pink_NoActionBar);
                break;
            case "Orange":
                setTheme(R.style.Orange_NoActionBar);
                break;
            default:
                setTheme(R.style.AppTheme_NoActionBar);
                break;
        }
        addPreferencesFromResource(R.xml.preferences);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
        switch (key) {
            case "pref_key_passcode_enable": {
//            Preference connectionPref = findPreference(key);
                // Set summary to be the user-description for the selected value
                Boolean m = sharedPreferences.getBoolean(key, true);
                if (m) {
                    Intent intent = new Intent(this, SetPasscode_1.class);
                    intent.putExtra("isChangePasscode", false);
                    startActivity(intent);
                } else {
                    mEdit1.putInt("Passcode", 0);
                    mEdit1.apply();
                }
                break;
            }
            case "pref_key_theme": {
                String m = sharedPreferences.getString(key, "Green");
                mEdit1.putString("Theme", m);
                mEdit1.commit();

                startActivity(new Intent(this, MainActivity.class));
                startActivity(new Intent(this, Setting.class));
                finishAffinity();
                break;
            }
            case "pref_key_notification_enable": {
                Boolean m = sharedPreferences.getBoolean(key, true);
                if (m)
                    mEdit1.putString("notification_enable", "enable");
                else
                    mEdit1.putString("notification_enable", "disable");
                mEdit1.commit();
                break;
            }
            case "pref_key_vibration": {
                Boolean m = sharedPreferences.getBoolean(key, true);
                if (m)
                    mEdit1.putString("vibrate_enable", "enable");
                else
                    mEdit1.putString("vibrate_enable", "disable");
                mEdit1.commit();
                break;
            }
            case "pref_key_sound": {
                Boolean m = sharedPreferences.getBoolean(key, true);
                if (m)
                    mEdit1.putString("sound_enable", "enable");
                else
                    mEdit1.putString("sound_enable", "disable");
                mEdit1.commit();
                break;
            }
            case "pref_key_music": {
                String m = sharedPreferences.getString(key, null);
                if (Objects.equals(m, "2")) {
                    mEdit1.putInt("music", 2);
                } else {
                    mEdit1.putInt("music", 1);
                }
                mEdit1.commit();
                break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener((SharedPreferences.OnSharedPreferenceChangeListener) this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener((SharedPreferences.OnSharedPreferenceChangeListener) this);
    }
}
