package com.mcal.drawabledesigner.preference;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

import com.mcal.drawabledesigner.R;

import net.margaritov.preference.colorpicker.ColorPickerDialog;

public class ColorPickerPreference extends Preference {
    private static final int DEFAULT_COLOR = -16777216;
    private View colorView;

    public ColorPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWidgetLayoutResource(R.layout.color_picker);
    }

    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        colorView = holder.findViewById(R.id.color);
        setStatusColor(getPersistedInt(DEFAULT_COLOR));
    }

    private void setStatusColor(int color) {
        colorView.setBackgroundColor(color);
    }

    public void onClick() {
        super.onClick();
        ColorPickerDialog dialog = new ColorPickerDialog(getContext(), getPersistedInt(DEFAULT_COLOR));
        dialog.setAlphaSliderVisible(true);
        dialog.setHexValueEnabled(true);
        dialog.setOnColorChangedListener(color -> {
            persistInt(color);
            setStatusColor(color);
        });
        dialog.show();
    }
}
