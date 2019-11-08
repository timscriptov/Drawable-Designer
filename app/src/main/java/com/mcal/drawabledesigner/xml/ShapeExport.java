package com.mcal.drawabledesigner.xml;

import android.annotation.SuppressLint;

import com.mcal.drawabledesigner.Utils;
import com.mcal.drawabledesigner.state.Shape;
import com.mcal.drawabledesigner.state.Shape.GradientType;
import com.mcal.drawabledesigner.state.Shape.ShapeType;

import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;

public class ShapeExport {
    private static final String ANDROID_NS = "http://schemas.android.com/apk/res/android";
    public final String DRAWABLE_DESIGNER_NS = "http://schemas.tiraisoft.com/app/DrawableDesigner";
    public final String DRAWABLE_DESIGNER_PREFIX = "dd";
    private Shape shape;

    public ShapeExport(Shape shape) {
        this.shape = shape;
    }

    public void exportXml(OutputStream outputStream) throws IOException {
        Throwable th;
        OutputStreamWriter osw = null;
        try {
            StringWriter writer = new StringWriter();
            buildXml(writer);
            OutputStreamWriter osw2 = new OutputStreamWriter(outputStream);
            try {
                osw2.write(Utils.prettyFormat(writer.toString(), 4));
                osw2.close();
            } catch (Throwable th2) {
                th = th2;
                osw = osw2;
                osw.close();
                throw th;
            }
        } catch (Throwable th3) {
            if (osw != null) {
                osw.close();
            }
        }
    }

    private void buildXml(Writer writer) throws XmlPullParserException, IOException {
        XmlSerializer s = XmlPullParserFactory.newInstance().newSerializer();
        s.setOutput(writer);
        s.startDocument("utf-8", null);
        s.setPrefix("android", ANDROID_NS);
        buildShape(s);
        s.endDocument();
        s.flush();
    }

    private void buildShape(XmlSerializer s) throws IOException {
        s.startTag(null, "shape");
        s.attribute(ANDROID_NS, "shape", shape.shapeType.toString().toLowerCase());
        if (shape.shapeType == ShapeType.ring) {
            if (shape.useRadiusRatio) {
                s.attribute(ANDROID_NS, "innerRadiusRatio", String.valueOf(shape.innerRadiusRatio));
            } else {
                s.attribute(ANDROID_NS, "innerRadius", dp(shape.innerRadius));
            }
            if (shape.useThicknessRatio) {
                s.attribute(ANDROID_NS, "thicknessRatio", String.valueOf(shape.ringThicknessRatio));
            } else {
                s.attribute(ANDROID_NS, "thickness", dp(shape.ringThickness));
            }
            s.attribute(ANDROID_NS, "useLevel", String.valueOf(shape.useShapeLevel));
        }
        buildCorners(s);
        buildGradient(s);
        buildPadding(s);
        buildShapeSize(s);
        buildSolidColor(s);
        buildStroke(s);
        s.endTag(null, "shape");
    }

    private void buildPreview(XmlSerializer s) throws IOException {
        s.setPrefix("dd", "http://schemas.tiraisoft.com/app/DrawableDesigner");
        s.startTag("http://schemas.tiraisoft.com/app/DrawableDesigner", "preview");
        s.attribute("http://schemas.tiraisoft.com/app/DrawableDesigner", "width", dp(shape.previewWidth));
        s.attribute("http://schemas.tiraisoft.com/app/DrawableDesigner", "height", dp(shape.previewHeight));
        s.endTag("http://schemas.tiraisoft.com/app/DrawableDesigner", "preview");
    }

    private void buildCorners(XmlSerializer s) throws IOException {
        if (shape.shapeType == ShapeType.rectangle) {
            s.startTag(null, "corners");
            s.attribute(ANDROID_NS, "topLeftRadius", dp((int) shape.radii[0]));
            s.attribute(ANDROID_NS, "topRightRadius", dp((int) shape.radii[2]));
            s.attribute(ANDROID_NS, "bottomRightRadius", dp((int) shape.radii[4]));
            s.attribute(ANDROID_NS, "bottomLeftRadius", dp((int) shape.radii[6]));
            s.endTag(null, "corners");
        }
    }

    @SuppressLint("DefaultLocale")
    private void buildGradient(XmlSerializer s) throws IOException {
        if (shape.useGradient) {
            s.startTag(null, "gradient");
            s.attribute(ANDROID_NS, "type", shape.gradientType.toString().toLowerCase());
            if (shape.gradientType == GradientType.linear) {
                s.attribute(ANDROID_NS, "angle", String.valueOf(shape.angle));
            } else {
                s.attribute(ANDROID_NS, "centerX", String.format("%f", ((float) shape.centerX) / ((float) shape.previewWidth)));
                s.attribute(ANDROID_NS, "centerY", String.format("%f", ((float) shape.centerY) / ((float) shape.previewHeight)));
                if (shape.gradientType == GradientType.radial) {
                    s.attribute(ANDROID_NS, "gradientRadius", dp(shape.gradientRadius));
                }
            }
            s.attribute(ANDROID_NS, "startColor", color(shape.startColor));
            if (shape.useCenterColor) {
                s.attribute(ANDROID_NS, "centerColor", color(shape.centerColor));
            }
            s.attribute(ANDROID_NS, "endColor", color(shape.endColor));
            s.attribute(ANDROID_NS, "useLevel", String.valueOf(shape.useGradientLevel));
            s.endTag(null, "gradient");
        }
    }

    private void buildPadding(XmlSerializer s) throws IOException {
        if (shape.usePadding) {
            s.startTag(null, "padding");
            s.attribute(ANDROID_NS, "left", dp(shape.paddingLeft));
            s.attribute(ANDROID_NS, "top", dp(shape.paddingTop));
            s.attribute(ANDROID_NS, "right", dp(shape.paddingRight));
            s.attribute(ANDROID_NS, "bottom", dp(shape.paddingBottom));
            s.endTag(null, "padding");
        }
    }

    private void buildShapeSize(XmlSerializer s) throws IOException {
        if (shape.useShapeSize) {
            s.startTag(null, "size");
            s.attribute(ANDROID_NS, "width", dp(shape.shapeWidth));
            s.attribute(ANDROID_NS, "height", dp(shape.shapeHeight));
            s.endTag(null, "size");
        }
    }

    private void buildSolidColor(XmlSerializer s) throws IOException {
        if (!shape.useGradient) {
            s.startTag(null, "solid");
            s.attribute(ANDROID_NS, "color", color(shape.color));
            s.endTag(null, "solid");
        }
    }

    private void buildStroke(XmlSerializer s) throws IOException {
        if (shape.useStroke) {
            s.startTag(null, "stroke");
            s.attribute(ANDROID_NS, "width", dp(shape.strokeWidth));
            s.attribute(ANDROID_NS, "color", color(shape.strokeColor));
            if (!shape.solidStroke) {
                s.attribute(ANDROID_NS, "dashGap", dp(shape.dashGap));
                s.attribute(ANDROID_NS, "dashWidth", dp(shape.dashWidth));
            }
            s.endTag(null, "stroke");
        }
    }

    private String color(int value) {
        return Utils.colorStr(value);
    }

    private String dp(int value) {
        return Utils.dpStr(value);
    }
}