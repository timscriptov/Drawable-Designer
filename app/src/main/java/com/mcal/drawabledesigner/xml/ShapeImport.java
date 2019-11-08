package com.mcal.drawabledesigner.xml;

import android.util.Xml;

import com.mcal.drawabledesigner.state.Shape;
import com.mcal.drawabledesigner.state.Shape.GradientType;
import com.mcal.drawabledesigner.state.Shape.ShapeType;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

public class ShapeImport {
    public static void importXml(InputStream inputStream, Shape shape) throws XmlPullParserException, IOException {
        XmlPullParser xpp = Xml.newPullParser();
        xpp.setInput(inputStream, null);
        while (xpp.getEventType() != 1) {
            if (xpp.getEventType() == 2) {
                parseShape(xpp, shape);
                parseCorners(xpp, shape);
                parseGradient(xpp, shape);
                parseSize(xpp, shape);
                parsePadding(xpp, shape);
                parseSolid(xpp, shape);
                parseStroke(xpp, shape);
                parsePreview(xpp, shape);
            }
            xpp.next();
        }
    }

    private static void parsePreview(XmlPullParser xpp, Shape shape) throws XmlPullParserException {
        if (isTag(xpp, "preview")) {
            AttributeSetHelper attrs = getAttributes(xpp);
            shape.previewWidth = attrs.getDp("width", shape.previewWidth);
            shape.previewHeight = attrs.getDp("height", shape.previewHeight);
        }
    }

    private static void parseShape(XmlPullParser xpp, Shape shape) throws XmlPullParserException {
        if (isTag(xpp, "shape")) {
            AttributeSetHelper attrs = getAttributes(xpp);
            shape.shapeType = ShapeType.valueOf(attrs.getString("shape", shape.shapeType.toString()));
            shape.innerRadius = attrs.getDp("innerRadius", shape.innerRadius);
            shape.innerRadiusRatio = attrs.getInt("innerRadiusRatio", shape.innerRadiusRatio);
            shape.ringThickness = attrs.getDp("thickness", shape.ringThickness);
            shape.ringThicknessRatio = attrs.getInt("thicknessRatio", shape.ringThicknessRatio);
            shape.useShapeLevel = attrs.getBoolean(shape.useShapeLevel);
        }
    }

    private static void parseCorners(XmlPullParser xpp, Shape shape) throws XmlPullParserException {
        if (isTag(xpp, "corners")) {
            AttributeSetHelper attrs = getAttributes(xpp);
            float[] fArr = shape.radii;
            float dp = attrs.getDp("topLeftRadius", attrs.getDp("radius", 0));
            shape.radii[1] = dp;
            fArr[0] = dp;
            fArr = shape.radii;
            float dp2 = attrs.getDp("topRightRadius", attrs.getDp("radius", 0));
            shape.radii[3] = dp2;
            fArr[2] = dp2;
            fArr = shape.radii;
            dp2 = attrs.getDp("bottomLeftRadius", attrs.getDp("radius", 0));
            shape.radii[5] = dp2;
            fArr[4] = dp2;
            fArr = shape.radii;
            dp2 = attrs.getDp("bottomRightRadius", attrs.getDp("radius", 0));
            shape.radii[7] = dp2;
            fArr[6] = dp2;
        }
    }

    private static void parseGradient(XmlPullParser xpp, Shape shape) throws XmlPullParserException {
        if (isTag(xpp, "gradient")) {
            AttributeSetHelper attrs = getAttributes(xpp);
            shape.gradientType = GradientType.valueOf(attrs.getString("type", shape.gradientType.toString()));
            shape.angle = attrs.getInt("angle", shape.angle);
            shape.centerX = ((int) attrs.getFloat("centerX", ((float) shape.centerX) / ((float) shape.previewWidth))) * shape.previewWidth;
            shape.centerY = ((int) attrs.getFloat("centerY", ((float) shape.centerY) / ((float) shape.previewHeight))) * shape.previewHeight;
            shape.gradientRadius = attrs.getDp("gradientRadius", shape.gradientRadius);
            shape.startColor = attrs.getColor("startColor", shape.startColor);
            shape.centerColor = attrs.getColor("centerColor", shape.centerColor);
            shape.endColor = attrs.getColor("endColor", shape.endColor);
            shape.useGradientLevel = attrs.getBoolean(shape.useGradientLevel);
        }
    }

    private static void parsePadding(XmlPullParser xpp, Shape shape) throws XmlPullParserException {
        if (isTag(xpp, "padding")) {
            AttributeSetHelper attrs = getAttributes(xpp);
            shape.paddingLeft = attrs.getDp("left", shape.paddingLeft);
            shape.paddingRight = attrs.getDp("right", shape.paddingRight);
            shape.paddingTop = attrs.getDp("top", shape.paddingTop);
            shape.paddingBottom = attrs.getDp("bottom", shape.paddingBottom);
        }
    }

    private static void parseSize(XmlPullParser xpp, Shape shape) throws XmlPullParserException {
        if (isTag(xpp, "size")) {
            AttributeSetHelper attrs = getAttributes(xpp);
            shape.shapeWidth = attrs.getDp("width", shape.shapeWidth);
            shape.shapeHeight = attrs.getDp("height", shape.shapeHeight);
        }
    }

    private static void parseSolid(XmlPullParser xpp, Shape shape) throws XmlPullParserException {
        if (isTag(xpp, "solid")) {
            shape.color = getAttributes(xpp).getColor("color", shape.color);
        }
    }

    private static void parseStroke(XmlPullParser xpp, Shape shape) throws XmlPullParserException {
        if (isTag(xpp, "stroke")) {
            AttributeSetHelper attrs = getAttributes(xpp);
            shape.strokeWidth = attrs.getDp("width", shape.strokeWidth);
            shape.strokeColor = attrs.getColor("color", shape.strokeColor);
            shape.dashGap = attrs.getDp("dashGap", shape.dashGap);
            shape.dashWidth = attrs.getDp("dashWidth", shape.dashWidth);
        }
    }

    private static AttributeSetHelper getAttributes(XmlPullParser xpp) {
        return new AttributeSetHelper(Xml.asAttributeSet(xpp));
    }

    private static boolean isTag(XmlPullParser xpp, String name) throws XmlPullParserException {
        return xpp.getEventType() == 2 && xpp.getName().equals(name);
    }
}