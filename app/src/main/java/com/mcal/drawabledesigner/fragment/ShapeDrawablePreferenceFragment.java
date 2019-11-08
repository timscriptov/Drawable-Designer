package com.mcal.drawabledesigner.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;

import com.mcal.drawabledesigner.R;
import com.mcal.drawabledesigner.Utils;
import com.mcal.drawabledesigner.activity.ShapeDrawableActivity;
import com.mcal.drawabledesigner.preference.SeekBarPreference;
import com.mcal.drawabledesigner.preference.SeekBarPreference.OnChangeListener;
import com.mcal.drawabledesigner.state.Shape;
import com.mcal.drawabledesigner.state.Shape.GradientType;
import com.mcal.drawabledesigner.state.Shape.ShapeType;
import com.mcal.drawabledesigner.state.State;
import com.mcal.drawabledesigner.state.StatePersist;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ShapeDrawablePreferenceFragment extends BasePreferenceFragment implements OnSharedPreferenceChangeListener {
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof ShapeDrawableActivity)) {
            throw new IllegalStateException("Activity not an instance of ShapeDrawableActivity");
        }
    }

    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.shape_drawable);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    public void onResume() {
        super.onResume();
        getPrefs().registerOnSharedPreferenceChangeListener(this);
    }

    public void onPause() {
        super.onPause();
        getPrefs().unregisterOnSharedPreferenceChangeListener(this);
    }

    public void onPreviewChanged(int width, int height) {
        int max = Math.max(width, height);
        ((SeekBarPreference) findPreference(getString(R.string.centerX))).setMax(width);
        ((SeekBarPreference) findPreference(getString(R.string.centerY))).setMax(height);
        ((SeekBarPreference) findPreference(getString(R.string.gradientRadius))).setMaxAndResolution(max * 3);
        ((SeekBarPreference) findPreference(getString(R.string.innerRadius))).setMaxAndResolution(max);
        ((SeekBarPreference) findPreference(getString(R.string.ringThickness))).setMaxAndResolution(max);
    }

    private void loadDrawable() throws IOException {
        String filename = getActivity().getIntent().getStringExtra("filename");
        State.shape.filename = filename;
        if (filename != null) {
            if (filename.equals("+new")) {
                State.shape = new Shape();
            } else {
                InputStreamReader isr = null;
                try {
                    InputStreamReader isr2 = new InputStreamReader(new FileInputStream(Utils.getFile(filename + ".json", false)));
                    try {
                        State.shape = StatePersist.load(isr2);
                        isr2.close();
                    } catch (Throwable ignored) {
                    }
                } catch (Throwable ignored) {
                }
            }
            getPrefs().edit().clear().apply();
        }
        if (filename == null) {
            filename = "no drawable file, resuming edit";
        }
    }

    private void init() {
        try {
            loadDrawable();
        } catch (IOException x) {
            x.printStackTrace();
        }
        setDefaults();
        init(getPreferenceScreen());
    }

    private void setDefaults() {
        setDefaultString(getString(R.string.shapeType), State.shape.shapeType.toString());
        setDefaultBoolean(getString(R.string.useRadiusRatio), State.shape.useRadiusRatio);
        setDefaultInt(getString(R.string.innerRadius), State.shape.innerRadius);
        setDefaultInt(getString(R.string.innerRadiusRatio), State.shape.innerRadiusRatio);
        setDefaultBoolean(getString(R.string.useThicknessRatio), State.shape.useThicknessRatio);
        setDefaultInt(getString(R.string.ringThickness), State.shape.ringThickness);
        setDefaultInt(getString(R.string.ringThicknessRatio), State.shape.ringThicknessRatio);
        setDefaultBoolean(getString(R.string.useShapeLevel), State.shape.useShapeLevel);
        setDefaultInt(getString(R.string.topLeftCorner), (int) State.shape.radii[0]);
        setDefaultInt(getString(R.string.topRightCorner), (int) State.shape.radii[2]);
        setDefaultInt(getString(R.string.bottomRightCorner), (int) State.shape.radii[4]);
        setDefaultInt(getString(R.string.bottomLeftCorner), (int) State.shape.radii[6]);
        setDefaultInt(getString(R.string.solidColor), State.shape.color);
        setDefaultBoolean(getString(R.string.useGradient), State.shape.useGradient);
        setDefaultBoolean(getString(R.string.useGradientLevel), State.shape.useGradientLevel);
        setDefaultBoolean(getString(R.string.useCenterColor), State.shape.useCenterColor);
        setDefaultString(getString(R.string.gradientType), State.shape.gradientType.toString());
        setDefaultInt(getString(R.string.angle), State.shape.angle);
        setDefaultInt(getString(R.string.centerX), State.shape.centerX);
        setDefaultInt(getString(R.string.centerY), State.shape.centerY);
        setDefaultInt(getString(R.string.gradientRadius), State.shape.gradientRadius);
        setDefaultInt(getString(R.string.startColor), State.shape.startColor);
        setDefaultInt(getString(R.string.centerColor), State.shape.centerColor);
        setDefaultInt(getString(R.string.endColor), State.shape.endColor);
        setDefaultBoolean(getString(R.string.useStroke), State.shape.useStroke);
        setDefaultInt(getString(R.string.strokeWidth), State.shape.strokeWidth);
        setDefaultInt(getString(R.string.strokeColor), State.shape.strokeColor);
        setDefaultBoolean(getString(R.string.solidStroke), State.shape.solidStroke);
        setDefaultInt(getString(R.string.dashGap), State.shape.dashGap);
        setDefaultInt(getString(R.string.dashWidth), State.shape.dashWidth);
        setDefaultBoolean(getString(R.string.useShapeSize), State.shape.useShapeSize);
        setDefaultInt(getString(R.string.shapeWidth), State.shape.shapeWidth);
        setDefaultInt(getString(R.string.shapeHeight), State.shape.shapeHeight);
        setDefaultBoolean(getString(R.string.usePadding), State.shape.usePadding);
        setDefaultInt(getString(R.string.paddingLeft), State.shape.paddingLeft);
        setDefaultInt(getString(R.string.paddingRight), State.shape.paddingRight);
        setDefaultInt(getString(R.string.paddingTop), State.shape.paddingTop);
        setDefaultInt(getString(R.string.paddingBottom), State.shape.paddingBottom);
        setDefaultInt(getString(R.string.previewWidth), State.shape.previewWidth);
        setDefaultInt(getString(R.string.previewHeight), State.shape.previewHeight);
        setDefaultInt(getString(R.string.previewLevel), State.shape.previewLevel);
        refreshPreferenceScreen();
    }

    private void init(PreferenceGroup group) {
        SharedPreferences prefs = getPrefs();
        for (int i = 0; i < group.getPreferenceCount(); i++) {
            Preference preference = group.getPreference(i);
            if (preference instanceof PreferenceGroup) {
                init((PreferenceGroup) preference);
            } else if (preference.hasKey()) {
                String key = preference.getKey();
                if (preference instanceof SeekBarPreference) {
                    initSeekBarPreference((SeekBarPreference) preference);
                } else {
                    onSharedPreferenceChanged(prefs, key);
                }
            }
        }
    }

    private void initSeekBarPreference(final SeekBarPreference pref) {
        pref.setOnChangeListener(value -> onSeekBarChanged(pref.getKey(), value));
    }

    private void onSeekBarChanged(String key, int value) {
        if (key.equals(getString(R.string.allCorners))) {
            onSeekBarChanged(getString(R.string.topLeftCorner), value);
            onSeekBarChanged(getString(R.string.topRightCorner), value);
            onSeekBarChanged(getString(R.string.bottomRightCorner), value);
            onSeekBarChanged(getString(R.string.bottomLeftCorner), value);
        } else if (key.equals(getString(R.string.topLeftCorner))) {
            State.shape.radii[0] = value;
            State.shape.radii[1] = value;
        } else if (key.equals(getString(R.string.topRightCorner))) {
            State.shape.radii[2] = value;
            State.shape.radii[3] = value;
        } else if (key.equals(getString(R.string.bottomRightCorner))) {
            State.shape.radii[4] = value;
            State.shape.radii[5] = value;
        } else if (key.equals(getString(R.string.bottomLeftCorner))) {
            State.shape.radii[6] = value;
            State.shape.radii[7] = value;
        } else if (key.equals(getString(R.string.innerRadius))) {
            State.shape.innerRadius = value;
        } else if (key.equals(getString(R.string.innerRadiusRatio))) {
            State.shape.innerRadiusRatio = value;
        } else if (key.equals(getString(R.string.ringThickness))) {
            State.shape.ringThickness = value;
        } else if (key.equals(getString(R.string.ringThicknessRatio))) {
            State.shape.ringThicknessRatio = value;
        } else if (key.equals(getString(R.string.angle))) {
            State.shape.angle = value;
        } else if (key.equals(getString(R.string.centerX))) {
            State.shape.centerX = value;
        } else if (key.equals(getString(R.string.centerY))) {
            State.shape.centerY = value;
        } else if (key.equals(getString(R.string.gradientRadius))) {
            State.shape.gradientRadius = value;
        } else if (key.equals(getString(R.string.strokeWidth))) {
            State.shape.strokeWidth = value;
        } else if (key.equals(getString(R.string.dashWidth))) {
            State.shape.dashWidth = value;
        } else if (key.equals(getString(R.string.dashGap))) {
            State.shape.dashGap = value;
        } else if (key.equals(getString(R.string.shapeWidth))) {
            State.shape.shapeWidth = value;
        } else if (key.equals(getString(R.string.shapeHeight))) {
            State.shape.shapeHeight = value;
        } else if (key.equals(getString(R.string.paddingLeft))) {
            State.shape.paddingLeft = value;
        } else if (key.equals(getString(R.string.paddingRight))) {
            State.shape.paddingRight = value;
        } else if (key.equals(getString(R.string.paddingTop))) {
            State.shape.paddingTop = value;
        } else if (key.equals(getString(R.string.paddingBottom))) {
            State.shape.paddingBottom = value;
        } else if (key.equals(getString(R.string.previewWidth))) {
            State.shape.previewWidth = value;
        } else if (key.equals(getString(R.string.previewHeight))) {
            State.shape.previewHeight = value;
        } else if (key.equals(getString(R.string.previewLevel))) {
            State.shape.previewLevel = value;
            getHostActivity().updatePreview(false);
            return;
        }
        getHostActivity().updatePreview(true);
    }

    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        String type;
        if (key.equals(getString(R.string.shapeType))) {
            type = prefs.getString(key, State.shape.shapeType.toString());
            State.shape.shapeType = ShapeType.valueOf(type.toLowerCase());
            updateShapeDependants();
        } else if (key.equals(getString(R.string.useShapeLevel))) {
            State.shape.useShapeLevel = prefs.getBoolean(key, State.shape.useShapeLevel);
            updatePreviewDependants();
        } else if (key.equals(getString(R.string.allCorners))) {
            int value = prefs.getInt(key, 0);
            Editor edit = prefs.edit();
            edit.putInt(getString(R.string.topLeftCorner), value);
            edit.putInt(getString(R.string.topRightCorner), value);
            edit.putInt(getString(R.string.bottomRightCorner), value);
            edit.putInt(getString(R.string.bottomLeftCorner), value);
            edit.apply();
        } else if (key.equals(getString(R.string.useRadiusRatio))) {
            State.shape.useRadiusRatio = prefs.getBoolean(key, State.shape.useRadiusRatio);
            updateShapeDependants();
        } else if (key.equals(getString(R.string.useThicknessRatio))) {
            State.shape.useThicknessRatio = prefs.getBoolean(key, State.shape.useThicknessRatio);
            updateShapeDependants();
        } else if (key.equals(getString(R.string.useGradient))) {
            State.shape.useGradient = prefs.getBoolean(key, State.shape.useGradient);
            updateGradientDependants();
            updatePreviewDependants();
        } else if (key.equals(getString(R.string.useCenterColor))) {
            State.shape.useCenterColor = prefs.getBoolean(key, State.shape.useCenterColor);
            updateGradientDependants();
        } else if (key.equals(getString(R.string.useGradientLevel))) {
            State.shape.useGradientLevel = prefs.getBoolean(key, State.shape.useGradientLevel);
            updatePreviewDependants();
        } else if (key.equals(getString(R.string.solidColor))) {
            State.shape.color = prefs.getInt(key, State.shape.color);
            setColorSummary(key, State.shape.color);
        } else if (key.equals(getString(R.string.gradientType))) {
            type = prefs.getString(key, State.shape.gradientType.toString());
            State.shape.gradientType = GradientType.valueOf(type.toLowerCase());
            updateGradientDependants();
        } else if (key.equals(getString(R.string.startColor))) {
            State.shape.startColor = prefs.getInt(key, State.shape.startColor);
            setColorSummary(key, State.shape.startColor);
        } else if (key.equals(getString(R.string.centerColor))) {
            State.shape.centerColor = prefs.getInt(key, State.shape.centerColor);
            setColorSummary(key, State.shape.centerColor);
        } else if (key.equals(getString(R.string.endColor))) {
            State.shape.endColor = prefs.getInt(key, State.shape.endColor);
            setColorSummary(key, State.shape.endColor);
        } else if (key.equals(getString(R.string.useStroke))) {
            State.shape.useStroke = prefs.getBoolean(key, State.shape.useStroke);
            updateStrokeDependants();
        } else if (key.equals(getString(R.string.solidStroke))) {
            State.shape.solidStroke = prefs.getBoolean(key, State.shape.solidStroke);
            updateStrokeDependants();
        } else if (key.equals(getString(R.string.strokeColor))) {
            State.shape.strokeColor = prefs.getInt(key, State.shape.strokeColor);
            setColorSummary(key, State.shape.strokeColor);
        } else if (key.equals(getString(R.string.useShapeSize))) {
            State.shape.useShapeSize = prefs.getBoolean(key, State.shape.useShapeSize);
            updateShapeSizeDependants();
        } else if (key.equals(getString(R.string.usePadding))) {
            State.shape.usePadding = prefs.getBoolean(key, State.shape.usePadding);
            updatePadddingDependants();
        }
        getHostActivity().updatePreview(true);
    }

    private void setColorSummary(String key, int color) {
        findPreference(key).setSummary(String.format("#%X (ARGB)", color));
    }

    private void updateShapeSizeDependants() {
        showPreference(getString(R.string.shapeWidth), State.shape.useShapeSize);
        showPreference(getString(R.string.shapeHeight), State.shape.useShapeSize);
    }

    private void updatePadddingDependants() {
        showPreference(getString(R.string.paddingLeft), State.shape.usePadding);
        showPreference(getString(R.string.paddingRight), State.shape.usePadding);
        showPreference(getString(R.string.paddingTop), State.shape.usePadding);
        showPreference(getString(R.string.paddingBottom), State.shape.usePadding);
    }

    private void updatePreviewDependants() {
        String string = getString(R.string.previewLevel);
        boolean z = State.shape.useShapeLevel || (State.shape.useGradient && State.shape.useGradientLevel);
        showPreference(string, z);
    }

    private void updateStrokeDependants() {
        boolean z;
        boolean z2 = true;
        showPreference(getString(R.string.strokeColor), State.shape.useStroke);
        showPreference(getString(R.string.strokeWidth), State.shape.useStroke);
        showPreference(getString(R.string.solidStroke), State.shape.useStroke);
        String string = getString(R.string.dashGap);
        if (!State.shape.useStroke || State.shape.solidStroke) {
            z = false;
        } else {
            z = true;
        }
        showPreference(string, z);
        String string2 = getString(R.string.dashWidth);
        if (!State.shape.useStroke || State.shape.solidStroke) {
            z2 = false;
        }
        showPreference(string2, z2);
    }

    private void updateShapeDependants() {
        boolean z;
        boolean z2 = true;
        ShapeType type = ShapeType.valueOf(getPrefs().getString(getString(R.string.shapeType), State.shape.shapeType.toString()));
        showPreference(getString(R.string.rectangle), type == ShapeType.rectangle);
        String string = getString(R.string.ring);
        if (type == ShapeType.ring) {
            z = true;
        } else {
            z = false;
        }
        showPreference(string, z);
        string = getString(R.string.innerRadius);
        if (type != ShapeType.ring || State.shape.useRadiusRatio) {
            z = false;
        } else {
            z = true;
        }
        showPreference(string, z);
        string = getString(R.string.innerRadiusRatio);
        if (type == ShapeType.ring && State.shape.useRadiusRatio) {
            z = true;
        } else {
            z = false;
        }
        showPreference(string, z);
        string = getString(R.string.ringThickness);
        if (type != ShapeType.ring || State.shape.useThicknessRatio) {
            z = false;
        } else {
            z = true;
        }
        showPreference(string, z);
        String string2 = getString(R.string.ringThicknessRatio);
        if (!(type == ShapeType.ring && State.shape.useThicknessRatio)) {
            z2 = false;
        }
        showPreference(string2, z2);
    }

    private void updateGradientDependants() {
        boolean z;
        boolean z2 = true;
        boolean useGradient = getPrefs().getBoolean(getString(R.string.useGradient), false);
        boolean useCenterColor = getPrefs().getBoolean(getString(R.string.useCenterColor), false);
        GradientType type = GradientType.valueOf(getPrefs().getString(getString(R.string.gradientType), GradientType.radial.toString()));
        showPreference(getString(R.string.solidColor), !useGradient);
        showPreference(getString(R.string.gradient), useGradient);
        String string = getString(R.string.centerColor);
        if (useGradient && useCenterColor) {
            z = true;
        } else {
            z = false;
        }
        showPreference(string, z);
        string = getString(R.string.angle);
        if (useGradient && type == GradientType.linear) {
            z = true;
        } else {
            z = false;
        }
        showPreference(string, z);
        string = getString(R.string.gradientRadius);
        if (useGradient && type == GradientType.radial) {
            z = true;
        } else {
            z = false;
        }
        showPreference(string, z);
        showPreference(getString(R.string.useGradientLevel), State.shape.useGradient);
        string = getString(R.string.centerX);
        if (useGradient && (type == GradientType.radial || type == GradientType.sweep)) {
            z = true;
        } else {
            z = false;
        }
        showPreference(string, z);
        String string2 = getString(R.string.centerY);
        if (!(useGradient && (type == GradientType.radial || type == GradientType.sweep))) {
            z2 = false;
        }
        showPreference(string2, z2);
    }

    private ShapeDrawableActivity getHostActivity() {
        return (ShapeDrawableActivity) getActivity();
    }
}