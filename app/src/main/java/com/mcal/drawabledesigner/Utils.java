package com.mcal.drawabledesigner;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Xml;

import com.mcal.drawabledesigner.state.State;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class Utils {
    public static String stripExt(String name) {
        int index = name.lastIndexOf(46);
        if (index != -1) {
            return name.substring(0, index);
        }
        return name;
    }

    public static File getFile(String name, boolean create) throws IOException {
        File dir = new File(Environment.getExternalStorageDirectory(), State.projectFolder);
        File file = new File(dir, name);
        if (create) {
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
        }
        return file;
    }

    public static int dp(String dp) {
        return Integer.parseInt(dp.replace("dp", ""));
    }

    public static int color(String color) {
        return Long.decode(color).intValue();
    }

    @SuppressLint("DefaultLocale")
    public static String dpStr(int dp) {
        return String.format("%ddp", dp);
    }

    public static String colorStr(int value) {
        return String.format("#%X", value);
    }

    public static int pxToDp(Activity context, int px) {
        DisplayMetrics metrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return (int) (((float) px) / metrics.density);
    }

    public static int dpToPx(Activity context, int dp) {
        DisplayMetrics metrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return (int) (((float) dp) * metrics.density);
    }

    public static void setInnerRadius(GradientDrawable drawable, int value) throws NoSuchFieldException, IllegalAccessException {
        getField(getGradientStateClass(), "mInnerRadius").setInt(drawable.getConstantState(), value);
    }

    public static void setThickness(GradientDrawable drawable, int value) throws NoSuchFieldException, IllegalAccessException {
        getField(getGradientStateClass(), "mThickness").setInt(drawable.getConstantState(), value);
    }

    public static void setInnerRadiusRatio(GradientDrawable drawable, float value) throws NoSuchFieldException, IllegalAccessException {
        getField(getGradientStateClass(), "mInnerRadiusRatio").setFloat(drawable.getConstantState(), value);
    }

    public static void setThicknessRatio(GradientDrawable drawable, float value) throws NoSuchFieldException, IllegalAccessException {
        getField(getGradientStateClass(), "mThicknessRatio").setFloat(drawable.getConstantState(), value);
    }

    public static void setUseShapeLevel(GradientDrawable drawable, boolean value) throws NoSuchFieldException, IllegalAccessException {
        getField(getGradientStateClass(), "mUseLevelForShape").setBoolean(drawable.getConstantState(), value);
    }

    public static void setPadding(GradientDrawable drawable, Rect rect) throws NoSuchFieldException, IllegalAccessException {
        getField(getGradientStateClass(), "mPadding").set(drawable.getConstantState(), rect);
    }

    private static Class<?> getGradientStateClass() {
        for (Class<?> cls : GradientDrawable.class.getDeclaredClasses()) {
            if (cls.getSimpleName().equals("GradientState")) {
                return cls;
            }
        }
        throw new IllegalStateException("No GradientState member in current GradientDrawable implementation");
    }

    private static Field getField(Class<?> cls, String name) throws NoSuchFieldException {
        Field field = cls.getDeclaredField(name);
        field.setAccessible(true);
        return field;
    }

    public static XmlPullParser rawToXpp(Context context, int id) throws XmlPullParserException {
        XmlPullParser xpp = Xml.newPullParser();
        xpp.setFeature("http://xmlpull.org/v1/doc/features.html#process-namespaces", true);
        xpp.setFeature("http://xmlpull.org/v1/doc/features.html#process-docdecl", false);
        xpp.setInput(context.getResources().openRawResource(id), null);
        return xpp;
    }

    public static String prettyFormat(String str, int indent) throws TransformerException {
        Source source = new StreamSource(new StringReader(str));
        StreamResult result = new StreamResult(new StringWriter());
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty("indent", "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", String.valueOf(indent));
        transformer.transform(source, result);
        return result.getWriter().toString();
    }
}