package com.mcal.drawabledesigner.state;

public class Shape {
    public int angle = 0;
    public int centerColor = -16711936;
    public int centerX = 0;
    public int centerY = 0;
    public int color = -16777216;
    public int dashGap = 3;
    public int dashWidth = 6;
    public int endColor = -16776961;
    public String filename;
    public int gradientRadius = 100;
    public GradientType gradientType = GradientType.radial;
    public int innerRadius = 20;
    public int innerRadiusRatio = 9;
    public int paddingBottom = 5;
    public int paddingLeft = 5;
    public int paddingRight = 5;
    public int paddingTop = 5;
    public int previewHeight = 100;
    public int previewLevel = 10000;
    public int previewWidth = 100;
    public float[] radii = new float[8];
    public int ringThickness = 10;
    public int ringThicknessRatio = 3;
    public int shapeHeight = 56;
    public ShapeType shapeType = ShapeType.rectangle;
    public int shapeWidth = 132;
    public boolean solidStroke = true;
    public int startColor = -65536;
    public int strokeColor = -16777216;
    public int strokeWidth = 1;
    public boolean useCenterColor = false;
    public boolean useGradient = true;
    public boolean useGradientLevel = false;
    public boolean usePadding = false;
    public boolean useRadiusRatio = true;
    public boolean useShapeLevel = false;
    public boolean useShapeSize = false;
    public boolean useStroke = false;
    public boolean useThicknessRatio = true;

    public enum GradientType {
        linear,
        radial,
        sweep
    }

    public enum ShapeType {
        rectangle,
        oval,
        line,
        ring
    }
}