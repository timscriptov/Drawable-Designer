package com.mcal.drawabledesigner.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;

import com.mcal.drawabledesigner.R;
import com.mcal.drawabledesigner.Utils;
import com.mcal.drawabledesigner.event.GetEvent;
import com.mcal.drawabledesigner.fragment.ShapeDrawablePreferenceFragment;
import com.mcal.drawabledesigner.state.State;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class ShapeDrawableActivity extends BaseActivity {
    private boolean firstRun = true;
    private int previewHeightPx;
    private int previewWidthPx;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shape_drawable);
        getSupportFragmentManager().beginTransaction().replace(R.id.settings, new ShapeDrawablePreferenceFragment(), "shape_drawable").commit();
        initPreview();
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void onEvent(GetEvent<Bitmap> getEvent) {
        View view = findView();
        view.buildDrawingCache(true);
        getEvent.receiveCallback.receive(view.getDrawingCache(true));
        view.destroyDrawingCache();
    }

    private void initPreview() {
        final View preview = findView();
        preview.setDrawingCacheBackgroundColor(0);
        preview.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            ((ShapeDrawablePreferenceFragment) getSupportFragmentManager().findFragmentByTag("shape_drawable")).onPreviewChanged(pxToDp(preview.getWidth()), pxToDp(preview.getHeight()));
            previewWidthPx = preview.getWidth();
            previewHeightPx = preview.getHeight();
            if (firstRun) {
                firstRun = false;
                updatePreview(true);
            }
        });
    }

    private int pxToDp(int px) {
        return Utils.pxToDp(this, px);
    }

    private int dpToPx(int dp) {
        return Utils.dpToPx(this, dp);
    }

    public void updatePreview(boolean recreate) {
        Button preview = (Button) findView();
        if (recreate) {
            try {
                preview.setBackgroundDrawable(createGradientDrawable());
            } catch (IllegalAccessException x) {
                x.printStackTrace();
            } catch (NoSuchFieldException x2) {
                x2.printStackTrace();
            }
        }
        preview.setWidth(dpToPx(State.shape.previewWidth));
        preview.setHeight(dpToPx(State.shape.previewHeight));
        Drawable background = preview.getBackground();
        int i = (State.shape.useShapeLevel || State.shape.useGradientLevel) ? State.shape.previewLevel : 10000;
        background.setLevel(i);
        preview.requestLayout();
    }

    @SuppressLint("NewApi")
    private Drawable createGradientDrawable() throws NoSuchFieldException, IllegalAccessException {
        float f;
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(getShapeType());
        Utils.setUseShapeLevel(drawable, State.shape.useShapeLevel);
        switch (State.shape.shapeType) {
            case rectangle:
                float[] radiipx = new float[8];
                for (int i = 0; i < radiipx.length; i++) {
                    radiipx[i] = dpToPx((int) State.shape.radii[i]);
                }
                drawable.setCornerRadii(radiipx);
                break;
            case ring:
                if (State.shape.useRadiusRatio) {
                    Utils.setInnerRadius(drawable, -1);
                    Utils.setInnerRadiusRatio(drawable, (float) State.shape.innerRadiusRatio);
                } else {
                    Utils.setInnerRadius(drawable, dpToPx(State.shape.innerRadius));
                }
                if (!State.shape.useThicknessRatio) {
                    Utils.setThickness(drawable, dpToPx(State.shape.ringThickness));
                    break;
                }
                Utils.setThickness(drawable, -1);
                Utils.setThicknessRatio(drawable, (float) State.shape.ringThicknessRatio);
                break;
        }
        if (State.shape.useGradient) {
            int[] colors;
            Orientation[] orientations = new Orientation[]{Orientation.LEFT_RIGHT, Orientation.BL_TR, Orientation.BOTTOM_TOP, Orientation.BR_TL, Orientation.RIGHT_LEFT, Orientation.TR_BL, Orientation.TOP_BOTTOM, Orientation.TL_BR};
            int[] colors3 = new int[]{State.shape.startColor, State.shape.centerColor, State.shape.endColor};
            int[] colors2 = new int[]{State.shape.startColor, State.shape.endColor};
            if (State.shape.useCenterColor) {
                colors = colors3;
            } else {
                colors = colors2;
            }
            float x = this.previewWidthPx == 0 ? 0.0f : ((float) dpToPx(State.shape.centerX)) / ((float) this.previewWidthPx);
            float y = this.previewHeightPx == 0 ? 0.0f : ((float) dpToPx(State.shape.centerY)) / ((float) this.previewHeightPx);
            drawable.setGradientType(getGradientType());
            drawable.setUseLevel(State.shape.useGradientLevel);
            drawable.setColors(colors);
            drawable.setOrientation(orientations[State.shape.angle / 45]);
            drawable.setGradientCenter(x, y);
            drawable.setGradientRadius((float) dpToPx(State.shape.gradientRadius));
        } else {
            drawable.setColor(State.shape.color);
        }
        int dpToPx = State.shape.useStroke ? dpToPx(State.shape.strokeWidth) : 0;
        int i2 = State.shape.strokeColor;
        if (State.shape.solidStroke) {
            f = 0.0f;
        } else {
            f = dpToPx(State.shape.dashWidth);
        }
        drawable.setStroke(dpToPx, i2, f, (float) dpToPx(State.shape.dashGap));
        drawable.setSize(State.shape.useShapeSize ? dpToPx(State.shape.shapeWidth) : -1, State.shape.useShapeSize ? dpToPx(State.shape.shapeHeight) : -1);
        Utils.setPadding(drawable, State.shape.usePadding ? new Rect(dpToPx(State.shape.paddingLeft), dpToPx(State.shape.paddingTop), dpToPx(State.shape.paddingRight), dpToPx(State.shape.paddingBottom)) : null);
        return drawable;
    }

    private int getGradientType() {
        switch (State.shape.gradientType) {
            case linear:
                return 0;
            case sweep:
                return 2;
            default:
                return 1;
        }
    }

    private int getShapeType() {
        switch (State.shape.shapeType) {
            case ring:
                return 3;
            case oval:
                return 1;
            case line:
                return 2;
            default:
                return 0;
        }
    }

    private void showMessage(String title, String message) {
        new Builder(this).setTitle(title).setMessage(message).setPositiveButton("Close", (dialogInterface, i) -> dialogInterface.dismiss()).create().show();
    }

    private <T extends View> T findView() {
	 return findViewById(R.id.preview);
	 }
}
