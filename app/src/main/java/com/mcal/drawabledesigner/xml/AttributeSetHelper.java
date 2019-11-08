package com.mcal.drawabledesigner.xml;

import android.util.AttributeSet;

import com.mcal.drawabledesigner.Utils;

public class AttributeSetHelper {
    private AttributeSet attrs;

    AttributeSetHelper(AttributeSet attrs) {
        this.attrs = attrs;
    }

    public boolean has(String name) {
        return getAttributeIndex(name) != -1;
    }

    String getString(String name, String defaultValue) {
        int index = getAttributeIndex(name);
        return index == -1 ? defaultValue : attrs.getAttributeValue(index);
    }

    int getInt(String name, int defaultValue) {
        int index = getAttributeIndex(name);
        return index == -1 ? defaultValue : attrs.getAttributeIntValue(index, -1);
    }

    float getFloat(String name, float defaultValue) {
        int index = getAttributeIndex(name);
        return index == -1 ? defaultValue : attrs.getAttributeFloatValue(index, -1.0f);
    }

    int getDp(String name, int defaultValue) {
        int index = getAttributeIndex(name);
        return index == -1 ? defaultValue : Utils.dp(attrs.getAttributeValue(index));
    }

    int getColor(String name, int defaultValue) {
        int index = getAttributeIndex(name);
        return index == -1 ? defaultValue : Utils.color(attrs.getAttributeValue(index));
    }

    boolean getBoolean(boolean defaultValue) {
        int index = getAttributeIndex("useLevel");
        return index == -1 ? defaultValue : attrs.getAttributeBooleanValue(index, defaultValue);
    }

    /*public <T extends Enum> T getEnum(Class<T> enumType, String name, T defaultValue) {
        int index = getAttributeIndex(name);
        if (index == -1) return defaultValue;
        else return Enum.valueOf(enumType, attrs.getAttributeValue(index));
    }*/

    private int getAttributeIndex(String name) {
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            if (this.attrs.getAttributeName(i).equals(name)) {
                return i;
            }
        }
        return -1;
    }
}