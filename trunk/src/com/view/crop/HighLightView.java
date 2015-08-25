
package com.view.crop;

import com.view.wheelview.R;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

public class HighLightView {

    public static final int GROW_NONE = (1 << 0);
    public static final int GROW_LEFT_EDGE = (1 << 1);
    public static final int GROW_RIGHT_EDGE = (1 << 2);
    public static final int GROW_TOP_EDGE = (1 << 3);
    public static final int GROW_BOTTOM_EDGE = (1 << 4);
    public static final int MOVE = (1 << 5);

    View mView;
    Matrix mMatrix;
    private ModifyMode mMode = ModifyMode.None;
    RectF mImageRect, mCropRect;
    RectF mDrawRect;

    private final Paint mFocusPaint = new Paint();
    private final Paint mOutlinePaint = new Paint();
    Path mPath = new Path();
    private boolean mHidden;
    private boolean mMaintainAspectRatio = false;
    private float mInitialAspectRatio;
    private Drawable mResizeDrawableH, mResizeDrawableV;

    public HighLightView(View view) {
        mView = view;
        Log.d("w.w", "GROW_NONE=" + GROW_NONE + "  GROW_LEFT_EDGE=" + GROW_LEFT_EDGE
                + "  GROW_RIGHT_EDGE=" + GROW_RIGHT_EDGE + "   GROW_BOTTOM_EDGE="
                + GROW_BOTTOM_EDGE + "   MOVE=" + MOVE);
    }

    enum ModifyMode {
        None, Move, Grow
    }

    private void init() {
        Resources resources = mView.getResources();
        mResizeDrawableH = resources.getDrawable(R.drawable.camera_crop_width);
        mResizeDrawableV = resources.getDrawable(R.drawable.camera_crop_height);

        mFocusPaint.setARGB(125, 50, 50, 50);

        mOutlinePaint.setStrokeWidth(3F);
        mOutlinePaint.setStyle(Paint.Style.STROKE);
        mOutlinePaint.setAntiAlias(true);

        mMode = ModifyMode.None;
    }

    public void setUp(Matrix matrix, Rect imgRect, RectF cropRectF, boolean maintainAspectRatio) {
        mMatrix = new Matrix(matrix);
        mImageRect = new RectF(imgRect);
        mCropRect = cropRectF;

        mMaintainAspectRatio = maintainAspectRatio;

        mInitialAspectRatio = mCropRect.width() / mCropRect.height();
        mDrawRect = computeLayout();
        init();
    }

    private RectF computeLayout() {
        RectF r = new RectF(mCropRect.left, mCropRect.top, mCropRect.right, mCropRect.bottom);
        mMatrix.mapRect(r);
        return new RectF(Math.round(r.left), Math.round(r.top), Math.round(r.right),
                Math.round(r.bottom));
    }

    void handleMotion(int edge, float dx, float dy) {

        if (edge == GROW_NONE) {
            return;
        }
        RectF r = computeLayout();
        if (edge == MOVE) {
            // Convert to image space before sending to moveBy().
            moveBy(dx * (mCropRect.width() / r.width()), dy * (mCropRect.height() / r.height()));
        } else {
            if (((GROW_LEFT_EDGE | GROW_RIGHT_EDGE) & edge) == 0) {
                dx = 0;
            }

            if (((GROW_TOP_EDGE | GROW_BOTTOM_EDGE) & edge) == 0) {
                dy = 0;
            }
            Log.d("w.w", "handleMotion dx=" + dx + "  dy=" + dy);
            // Convert to image space before sending to growBy().
            float xDelta = dx * (mCropRect.width() / r.width());
            float yDelta = dy * (mCropRect.height() / r.height());
            growBy((((edge & GROW_LEFT_EDGE) != 0) ? -1 : 1) * xDelta,
                    (((edge & GROW_TOP_EDGE) != 0) ? -1 : 1) * yDelta);
        }
    }

    void moveBy(float dx, float dy) {

        mCropRect.offset(dx, dy);

        // Put the cropping rectangle inside image rectangle.
        mCropRect.offset(Math.max(0, mImageRect.left - mCropRect.left),
                Math.max(0, mImageRect.top - mCropRect.top));

        mCropRect.offset(Math.min(0, mImageRect.right - mCropRect.right),
                Math.min(0, mImageRect.bottom - mCropRect.bottom));

        mDrawRect = computeLayout();
        mView.invalidate();
    }

    void growBy(float dx, float dy) {
        if (mMaintainAspectRatio) {
            if (dx != 0) {
                dy = dx / mInitialAspectRatio;
            } else if (dy != 0) {
                dx = dy * mInitialAspectRatio;
            }
        }

        RectF r = new RectF(mCropRect);
        if (dx > 0F && r.width() + 2 * dx > mImageRect.width()) {
            float adjustment = (mImageRect.width() - r.width()) / 2F;
            dx = adjustment;
            if (mMaintainAspectRatio) {
                dy = dx / mInitialAspectRatio;
            }
        }
        if (dy > 0F && r.height() + 2 * dy > mImageRect.height()) {
            float adjustment = (mImageRect.height() - r.height()) / 2F;
            dy = adjustment;
            if (mMaintainAspectRatio) {
                dx = dy * mInitialAspectRatio;
            }
        }
        Log.d("w.w", "dx=" + dx + "  dy=" + dy);
        r.inset(-dx, -dy);

        final float widthCap = 25F;

        if (r.width() < widthCap) {
            r.inset(-(widthCap - r.width()) / 2F, 0F);
        }

        float heightCap = mMaintainAspectRatio ? (widthCap / mInitialAspectRatio) : widthCap;
        if (r.height() < heightCap) {
            r.inset(0F, -(heightCap - r.height()) / 2F);
        }

        if (r.left < mImageRect.left) {
            r.offset(mImageRect.left - r.left, 0F);
        } else if (r.right > mImageRect.right) {
            r.offset(-(r.right - mImageRect.right), 0);
        }
        if (r.top < mImageRect.top) {
            r.offset(0F, mImageRect.top - r.top);
        } else if (r.bottom > mImageRect.bottom) {
            r.offset(0F, -(r.bottom - mImageRect.bottom));
        }

        mCropRect.set(r);
        mDrawRect = computeLayout();
        mView.invalidate();
    }

    protected void onDraw(Canvas canvas) {
        if (mHidden) {
            return;
        }
        canvas.save();

        Rect viewDrawingRect = getViewDrawingRect();
        mPath.reset();
        mPath.addRect(mDrawRect, Path.Direction.CW);
        mOutlinePaint.setColor(Color.GREEN);

        // 画mask
        Paint paint = mFocusPaint;
        canvas.drawRect(0, 0, viewDrawingRect.right, mDrawRect.top, paint);
        canvas.drawRect(0, mDrawRect.top, mDrawRect.left, mDrawRect.bottom + 1, paint);
        canvas.drawRect(mDrawRect.right + 1, mDrawRect.top, viewDrawingRect.right,
                mDrawRect.bottom + 1, paint);
        canvas.drawRect(0, mDrawRect.bottom + 1, viewDrawingRect.right, viewDrawingRect.bottom,
                paint);
        // 画绿框
        canvas.restore();
        canvas.drawPath(mPath, mOutlinePaint);

        if (mMode == ModifyMode.Grow) {
            int left = (int) (mDrawRect.left + 1);
            int right = (int) (mDrawRect.right + 1);
            int top = (int) (mDrawRect.top + 4);
            int bottom = (int) (mDrawRect.bottom + 3);

            int widthWidth = mResizeDrawableH.getIntrinsicWidth() / 2;
            int widthHeight = mResizeDrawableH.getIntrinsicHeight() / 2;
            int heightHeight = mResizeDrawableV.getIntrinsicHeight() / 2;
            int heightWidth = mResizeDrawableV.getIntrinsicWidth() / 2;

            int xMiddle = (int) (mDrawRect.left + ((mDrawRect.right - mDrawRect.left) / 2));
            int yMiddle = (int) (mDrawRect.top + ((mDrawRect.bottom - mDrawRect.top) / 2));

            mResizeDrawableH.setBounds(left - widthWidth, yMiddle - widthHeight, left
                    + widthWidth, yMiddle
                    + widthHeight);
            mResizeDrawableH.draw(canvas);

            mResizeDrawableH.setBounds(right - widthWidth, yMiddle - widthHeight, right
                    + widthWidth, yMiddle
                    + widthHeight);
            mResizeDrawableH.draw(canvas);

            mResizeDrawableV.setBounds(xMiddle - heightWidth, top - heightHeight, xMiddle
                    + heightWidth, top
                    + heightHeight);
            mResizeDrawableV.draw(canvas);

            mResizeDrawableV.setBounds(xMiddle - heightWidth, bottom - heightHeight,
                    xMiddle + heightWidth, bottom
                            + heightHeight);
            mResizeDrawableV.draw(canvas);
        }
    }

    private Rect getViewDrawingRect() {
        Rect viewDrawingRect = new Rect();
        mView.getDrawingRect(viewDrawingRect);
        return viewDrawingRect;
    }

    public void setHidden(boolean hidden) {
        mHidden = hidden;
        mView.invalidate();
    }

    public void setMaintainAspectRatio(boolean maintainAspectRatio) {
        mMaintainAspectRatio = maintainAspectRatio;
    }

    public boolean getHidden() {
        return mHidden;
    }

    public void setMode(ModifyMode mode) {
        if (mode != mMode) {
            mMode = mode;
            mView.invalidate();
        }
    }

    public int getEdgeType(float x, float y) {
        RectF r = computeLayout();
        final float hysteresis = 20F;
        int retval = GROW_NONE;

        boolean verticalCheck = (y >= r.top - hysteresis) && (y < r.bottom + hysteresis);
        boolean horizCheck = (x >= r.left - hysteresis) && (x < r.right + hysteresis);

        if ((Math.abs(r.left - x) < hysteresis) && verticalCheck) {
            retval |= GROW_LEFT_EDGE;
        }
        if ((Math.abs(r.right - x) < hysteresis) && verticalCheck) {
            retval |= GROW_RIGHT_EDGE;
        }
        if ((Math.abs(r.top - y) < hysteresis) && horizCheck) {
            retval |= GROW_TOP_EDGE;
        }
        if ((Math.abs(r.bottom - y) < hysteresis) && horizCheck) {
            retval |= GROW_BOTTOM_EDGE;
        }

        if (retval == GROW_NONE && r.contains((int) x, (int) y)) {
            retval = MOVE;
        }
        return retval;
    }

    public void invalidate() {
        mDrawRect = computeLayout();
    }
}
