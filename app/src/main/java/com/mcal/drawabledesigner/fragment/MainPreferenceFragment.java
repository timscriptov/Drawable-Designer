package com.mcal.drawabledesigner.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.Preference.OnPreferenceClickListener;

import com.mcal.drawabledesigner.R;
import com.mcal.drawabledesigner.Utils;
import com.mcal.drawabledesigner.activity.ShapeDrawableActivity;
import com.mcal.drawabledesigner.state.State;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainPreferenceFragment extends BasePreferenceFragment {
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.main);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    public void onResume() {
        super.onResume();
        fillDrawableList();
    }

    private void init() {
        findPreference("edit_drawable").setOnPreferenceClickListener(preference -> {
            String filename = ((ListPreference) findPreference("drawable_list")).getValue();
            Intent it = new Intent(getActivity(), ShapeDrawableActivity.class);
            it.putExtra("filename", filename);
            startActivity(it);
            return true;
        });
    }

    private void fillDrawableList() {
        ListPreference listPref = (ListPreference) findPreference("drawable_list");
        List<String> names = new ArrayList<>();
        List<String> values = new ArrayList<>();
        names.add("[New]");
        values.add("+new");
        setDefaultString("drawable_list", values.get(0));
        File[] files = getFiles();
        if (files != null) {
            for (File f : files) {
                String name = f.getName();
                if (name.endsWith(".json")) {
                    String entry = Utils.stripExt(name);
                    names.add(entry);
                    values.add(entry);
                }
            }
        }
        listPref.setEntries(names.toArray(new CharSequence[0]));
        listPref.setEntryValues(values.toArray(new CharSequence[0]));
    }

    private File[] getFiles() {
        return new File(Environment.getExternalStorageDirectory(), State.projectFolder).listFiles();
    }
}
