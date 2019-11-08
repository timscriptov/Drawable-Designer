package com.mcal.drawabledesigner.xml;

import android.util.Xml;

import com.mcal.drawabledesigner.Utils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

public abstract class BaseNode {
    protected final String ANDROID = "android";
    protected final String ANDROID_NS = "http://schemas.android.com/apk/res/android";
    private XmlSerializer serializer;

    public BaseNode(XmlPullParser xpp) {
        parse(xpp);
    }

    public final void assertTag(XmlPullParser xpp, String name) throws XmlPullParserException {
        if (xpp.getEventType() != 2 && !xpp.getName().equals(name)) {
            throw new RuntimeException(String.format("not on %s tag", name));
        }
    }

    public final AttributeSetHelper getAttributes(XmlPullParser xpp) {
        return new AttributeSetHelper(Xml.asAttributeSet(xpp));
    }

    private void parse(XmlPullParser xpp) {
    }

    public void write(XmlSerializer serializer) {
        this.serializer = serializer;
    }

    public final void write(String name, Object value) throws IOException {
        serializer.attribute("http://schemas.android.com/apk/res/android", name, String.valueOf(value));
    }

    public final void writeInt(String name, int value) throws IOException {
        if (value != -1) {
            serializer.attribute("http://schemas.android.com/apk/res/android", name, String.valueOf(value));
        }
    }

    public final void writeDp(String name, int value) throws IOException {
        if (value != -1) {
            serializer.attribute("http://schemas.android.com/apk/res/android", name, Utils.dpStr(value));
        }
    }

    public final void writeColor(String name, int value) throws IOException {
        serializer.attribute("http://schemas.android.com/apk/res/android", name, Utils.colorStr(value));
    }
}