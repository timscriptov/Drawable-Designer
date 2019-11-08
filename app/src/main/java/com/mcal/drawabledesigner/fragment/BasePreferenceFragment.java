package com.mcal.drawabledesigner.fragment;

import android.content.SharedPreferences;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceGroup;

abstract class BasePreferenceFragment extends PreferenceFragmentCompat {
    void refreshPreferenceScreen() {
        setPreferenceScreen(null);
        addPreferencesFromResource(com.mcal.drawabledesigner.R.xml.shape_drawable);
    }

    void setDefaultString(String key, String value) {
        if (!getPrefs().contains(key)) {
            getPrefs().edit().putString(key, value).apply();
        }
    }

    void setDefaultInt(String key, int value) {
        if (!getPrefs().contains(key)) {
            getPrefs().edit().putInt(key, value).apply();
        }
    }

    void setDefaultBoolean(String key, boolean value) {
        if (!getPrefs().contains(key)) {
            getPrefs().edit().putBoolean(key, value).apply();
        }
    }

    void showPreference(String key, boolean show) {
        Preference preference = findPreference(key);
        if (preference instanceof PreferenceGroup) {
            showPreferenceGroup((PreferenceGroup) preference, show);
        } else {
            preference.setVisible(show);
        }
    }

    private void showPreferenceGroup(PreferenceGroup group, boolean show) {
        group.setVisible(show);
        for (int i = 0; i < group.getPreferenceCount(); i++) {
            group.getPreference(i).setVisible(show);
        }
    }

    SharedPreferences getPrefs() {
        return getPreferenceScreen().getSharedPreferences();
    }

	Preference findPreference(String key) {
        return getPreferenceScreen().findPreference(key);
    }
}
