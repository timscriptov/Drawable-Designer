package com.mcal.drawabledesigner.preference;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

import com.mcal.drawabledesigner.R;

public class SeekBarPreference extends Preference {
    private int currentPersistValue;
    private int defaultValue;
    private int displayResolution;
    private String format;
    private int increment;
    private TextView label;
    private int max;
    private int min;
    private OnChangeListener onChangeListener;

    public SeekBarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWidgetLayoutResource(R.layout.seekbar);
        min = attrs.getAttributeIntValue(null, "min", 0);
        max = attrs.getAttributeIntValue(null, "max", 100);
        increment = attrs.getAttributeIntValue(null, "increment", 1);
        defaultValue = attrs.getAttributeIntValue(null, "defaultValue", 50);
        displayResolution = attrs.getAttributeIntValue(null, "displayResolution", max);
        format = attrs.getAttributeValue(null, "format");
        if (format == null || format.equals("")) {
            format = "%d";
        }
    }

    public Object onGetDefaultValue(TypedArray a, int index) {
        return defaultValue;
    }

    public void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        super.onSetInitialValue(restorePersistedValue, defaultValue);
        if (restorePersistedValue) {
            currentPersistValue = getPersistedInt(this.defaultValue);
            return;
        }
        this.currentPersistValue = (Integer) defaultValue;
        if (shouldPersist()) {
            persistInt(currentPersistValue);
        }
    }

    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        @SuppressLint("ResourceType") LinearLayout widgetFrame = holder.itemView.findViewById(16908312);
        SeekBar seekBar = widgetFrame.findViewById(R.id.seekBar);
        label = widgetFrame.findViewById(R.id.textView);
        initWidgetFrame(widgetFrame);
        setLabel(getDisplayValue(currentPersistValue), currentPersistValue);
        initSeekBar(seekBar, currentPersistValue);
    }

    private void initWidgetFrame(ViewGroup widgetFrame) {
        widgetFrame.setLayoutParams(new LayoutParams(0, -2, 1.0f));
    }

    private void initSeekBar(SeekBar seekBar, int persistValue) {
        seekBar.setMax(max - min);
        seekBar.setProgress(getProgressValue(persistValue));
        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    onChange(progress);
                }
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                persistInt(getPersistValue(seekBar.getProgress()));
            }
        });
    }

    private void onChange(int progress) {
        int persistValue = getPersistValue(progress);
        currentPersistValue = persistValue;
        setLabel(getDisplayValue(persistValue), persistValue);
        if (onChangeListener != null) {
            onChangeListener.onChange(persistValue);
        }
    }

    private void setLabel(int displayValue, int persistValue) {
        label.setText(String.format(format, displayValue, persistValue));
    }

    private int getProgressValue(int persistValue) {
        return persistValue - min;
    }

    private int getPersistValue(int progress) {
        return Math.round((float) ((progress + min) / increment)) * increment;
    }

    private int getDisplayValue(int persistValue) {
        return (int) ((((float) persistValue) / ((float) max)) * ((float) displayResolution));
    }

    public void setOnChangeListener(OnChangeListener onChangeListener) {
        this.onChangeListener = onChangeListener;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setMaxAndResolution(int max) {
        this.max = max;
        this.displayResolution = max;
    }

    public void setDisplayResolution(int displayResolution) {
        this.displayResolution = displayResolution;
    }

    public void setValue(int value) {
        this.currentPersistValue = value;
    }

    public interface OnChangeListener {
        void onChange(int i);
    }
}