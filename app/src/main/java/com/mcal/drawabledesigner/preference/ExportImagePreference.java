package com.mcal.drawabledesigner.preference;

import android.content.Context;
import android.util.AttributeSet;

import com.mcal.drawabledesigner.event.GetEvent;

import org.greenrobot.eventbus.EventBus;

public class ExportImagePreference extends FileAskPreference {
    public ExportImagePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void save(String filename) {
        EventBus.getDefault().post(new GetEvent(new ExportImagePreference1(filename)));
    }
}