
package com.view.crop;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class CropImageView extends ImageViewTouchBase {

    public CropImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CropImageView(Context context) {
        super(context);
    }

    HighLightView mHighLightView = null;
    float mLastX, mLastY;
    int mMotionEdge = HighLightView.GROW_NONE;
    private boolean mShouldScale = true;

    public void setShouldScale(boolean shouldScale) {
        mShouldScale = shouldScale;
    }

    public void setHightLightView(HighLightView highLightView) {
        mHighLightView = highLightView;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mBitmapDisplayed.getBitmap() != null && mHighLightView != null) {
            centerBasedOnHighlightView(mHighLightView);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mHighLightView != null) {
            mHighLightView.onDraw(canvas);
        }
    }

    @Override
    protected void postTranslate(float dx, float dy) {
        super.postTranslate(dx, dy);
        if (mHighLightView != null) {
            mHighLightView.mMatrix.postTranslate(dx, dy);
            mHighLightView.invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event == null || mHighLightView == null) {
            return false;
        }
        if (mHighLightView.getHidden() == true) {
            return super.onTouchEvent(event);
        }
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                int edge = mHighLightView.getEdgeType(event.getX(), event.getY());
                if (edge != HighLightView.GROW_NONE) {
                    mMotionEdge = edge;
                    mLastX = event.getX();
                    mLastY = event.getY();
                    mHighLightView
                            .setMode((edge == HighLightView.MOVE) ? HighLightView.ModifyMode.Move
                                    : HighLightView.ModifyMode.Grow);
                    break;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mMotionEdge != HighLightView.GROW_NONE) {
                    float x = event.getX();
                    float y = event.getY();
                    Log.d("w.w", "mMotionEdge=" + mMotionEdge);
                    mHighLightView.handleMotion(mMotionEdge, x - mLastX, y - mLastY);
                    mLastX = x;
                    mLastY = y;
                    if (mShouldScale) {
                        ensureVisible(mHighLightView);
                        if (getScale() == 1f) {
                            center(true, true);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mMotionEdge != HighLightView.GROW_NONE && mShouldScale) {
                    centerBasedOnHighlightView(mHighLightView);
                    mHighLightView.setMode(HighLightView.ModifyMode.None);
                    center(true, true);
                }
                mMotionEdge = HighLightView.GROW_NONE;
                break;
            default:
                break;
        }
        return true;
    }

    private void centerBasedOnHighlightView(HighLightView hv) {
        RectF drawRect = hv.mDrawRect;

        float width = drawRect.width();
        float height = drawRect.height();

        float thisWidth = getWidth();
        float thisHeight = getHeight();

        float z1 = thisWidth / width * .6F;
        float z2 = thisHeight / height * .6F;

        float zoom = Math.min(z1, z2);
        zoom = zoom * this.getScale();
        zoom = Math.max(1F, zoom);

        if ((Math.abs(zoom - getScale()) / zoom) > .1) {
            float[] coordinates = new float[] {
                    hv.mCropRect.centerX(), hv.mCropRect.centerY()
            };
            getImageMatrix().mapPoints(coordinates);
            zoomTo(zoom, coordinates[0], coordinates[1], 300F); // CR: 300.0f.
        }
        ensureVisible(hv);
    }

    private void ensureVisible(HighLightView hv) {
        RectF r = hv.mDrawRect;

        int panDeltaX1 = (int) Math.max(0, getLeft() - r.left);
        int panDeltaX2 = (int) Math.min(0, getRight() - r.right);

        int panDeltaY1 = (int) Math.max(0, getTop() - r.top);
        int panDeltaY2 = (int) Math.min(0, getBottom() - r.bottom);

        int panDeltaX = panDeltaX1 != 0 ? panDeltaX1 : panDeltaX2;
        int panDeltaY = panDeltaY1 != 0 ? panDeltaY1 : panDeltaY2;

        if (panDeltaX != 0 || panDeltaY != 0) {
            panBy(panDeltaX, panDeltaY);
        }
    }

    public void setHidden(boolean hidden) {
        if (mHighLightView != null) {
            mHighLightView.setHidden(hidden);
        }
    }
    
    public void setMaintainAspectRatio(boolean maintainAspectRatio) {
        if (mHighLightView != null) {
            mHighLightView.setMaintainAspectRatio(maintainAspectRatio);
        }
    }
}
