
package com.view.refereshlist;

import com.view.wheelview.R;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PullToRefreshListView extends ListView implements OnScrollListener {

    private static final int TAP_TO_REFRESH = 1;
    private static final int PULL_TO_REFRESH = 2;
    private static final int RELEASE_TO_REFRESH = 3;
    private static final int REFRESHING = 4;

    private static final String TAG = "PullToRefreshListView";

    private OnRefreshListener mOnRefreshListener;

    private OnScrollListener mOnScrollListener;
    private LayoutInflater mInflater;

    private RelativeLayout mRefreshView;
    private TextView mRefreshViewText;
    private ImageView mRefreshViewImage;
    private ProgressBar mRefreshViewProgress;
    private TextView mRefreshViewLastUpdated;

    private int mCurrentScrollState;
    private int mRefreshState;

    private RotateAnimation mFlipAnimation;
    private RotateAnimation mReverseFlipAnimation;

    private int mRefreshViewHeight;
    private int mRefreshOriginalBottomPadding;
    private int mLastMotionY;

    public PullToRefreshListView(Context context) {
        super(context);
        init(context);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        mFlipAnimation = new RotateAnimation(0, -180,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mFlipAnimation.setInterpolator(new LinearInterpolator());
        mFlipAnimation.setDuration(250);
        mFlipAnimation.setFillAfter(true);
        mReverseFlipAnimation = new RotateAnimation(-180, 0,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mReverseFlipAnimation.setInterpolator(new LinearInterpolator());
        mReverseFlipAnimation.setDuration(250);
        mReverseFlipAnimation.setFillAfter(true);

        mInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

        mRefreshView = (RelativeLayout) mInflater.inflate(
                R.layout.pull_to_refresh_header, this, false);
        mRefreshViewText =
                (TextView) mRefreshView.findViewById(R.id.pull_to_refresh_text);
        mRefreshViewImage =
                (ImageView) mRefreshView.findViewById(R.id.pull_to_refresh_image);
        mRefreshViewProgress =
                (ProgressBar) mRefreshView.findViewById(R.id.pull_to_refresh_progress);
        mRefreshViewLastUpdated =
                (TextView) mRefreshView.findViewById(R.id.pull_to_refresh_updated_at);

        mRefreshViewImage.setMinimumHeight(50);
        mRefreshView.setOnClickListener(new OnClickRefreshListener());
        mRefreshOriginalBottomPadding = mRefreshView.getPaddingBottom();

        mRefreshState = TAP_TO_REFRESH;

        addFooterView(mRefreshView);

        super.setOnScrollListener(this);

        measureView(mRefreshView);
        mRefreshViewHeight = mRefreshView.getMeasuredHeight();
    }

    @Override
    protected void onAttachedToWindow() {
        //setSelection(1);  
        super.onAttachedToWindow();
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
    }

    @Override
    public void setOnScrollListener(OnScrollListener l) {
        mOnScrollListener = l;
    }

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        mOnRefreshListener = onRefreshListener;
    }

    public void setLastUpdated(CharSequence lastUpdated) {
        if (lastUpdated != null) {
            mRefreshViewLastUpdated.setVisibility(View.VISIBLE);
            mRefreshViewLastUpdated.setText(lastUpdated);
        } else {
            mRefreshViewLastUpdated.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:

                if (!isVerticalScrollBarEnabled()) {
                    setVerticalScrollBarEnabled(true);
                }
                if ((getFirstVisiblePosition() == 0||getLastVisiblePosition() == getAdapter().getCount() - 1)
                        && mRefreshState != REFRESHING) {
                    if ((mRefreshView.getTop() <= getMeasuredHeight() - mRefreshViewHeight)
                            && mRefreshState == RELEASE_TO_REFRESH) {
                        mRefreshState = REFRESHING;
                        prepareForRefresh();
                        onRefresh();
                    } else if (mRefreshView.getTop() > getMeasuredHeight() - mRefreshViewHeight) {
                        resetHeader();
                        if (getFooterViewsCount() > 0) {
                            setSelectionFromTop(getAdapter().getCount() - 1,
                                    (this.getMeasuredHeight()));
                        }
                    }
                }
                break;

            case MotionEvent.ACTION_DOWN:
                mLastMotionY = y;
                break;

            case MotionEvent.ACTION_MOVE:
                applyHeaderPadding(event);
                break;
        }

        return super.onTouchEvent(event);
    }

    private void applyHeaderPadding(MotionEvent ev) {
        final int historySize = ev.getHistorySize();
        int pointerCount = 1;
        try {
            Method method = MotionEvent.class.getMethod("getPointerCount");
            pointerCount = (Integer) method.invoke(ev);
        } catch (NoSuchMethodException e) {
            pointerCount = 1;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (IllegalAccessException e) {
            System.err.println("unexpected " + e);
        } catch (InvocationTargetException e) {
            System.err.println("unexpected " + e);
        }

        for (int h = 0; h < historySize; h++) {
            for (int p = 0; p < pointerCount; p++) {
                if (mRefreshState == RELEASE_TO_REFRESH) {
                    if (isVerticalFadingEdgeEnabled()) {
                        setVerticalScrollBarEnabled(false);
                    }

                    int historicalY = 0;
                    try {
                        // For Android > 2.0  
                        Method method = MotionEvent.class.getMethod(
                                "getHistoricalY", Integer.TYPE, Integer.TYPE);
                        historicalY = ((Float) method.invoke(ev, p, h)).intValue();
                    } catch (NoSuchMethodException e) {
                        // For Android < 2.0  
                        historicalY = (int) (ev.getHistoricalY(h));
                    } catch (IllegalArgumentException e) {
                        throw e;
                    } catch (IllegalAccessException e) {
                        System.err.println("unexpected " + e);
                    } catch (InvocationTargetException e) {
                        System.err.println("unexpected " + e);
                    }

                    // Calculate the padding to apply, we divide by 1.7 to  
                    // simulate a more resistant effect during pull.  
                    int topPadding = (int) (((historicalY + mLastMotionY)
                            + mRefreshViewHeight) / 1.7);

                    mRefreshView.setPadding(
                            mRefreshView.getPaddingLeft(),
                            mRefreshView.getPaddingTop(),
                            mRefreshView.getPaddingRight(),
                            topPadding);
                }
            }
        }
    }

    private void resetHeaderPadding() {
        mRefreshView.setPadding(
                mRefreshView.getPaddingLeft(),
                mRefreshView.getPaddingTop(),
                mRefreshView.getPaddingRight(),
                mRefreshOriginalBottomPadding);
    }

    private void resetHeader() {
        if (mRefreshState != TAP_TO_REFRESH) {
            mRefreshState = TAP_TO_REFRESH;

            resetHeaderPadding();

            // Set refresh view text to the pull label  
            mRefreshViewText.setText(R.string.pull_to_refresh_tap_label);
            // Replace refresh drawable with arrow drawable  
            mRefreshViewImage.setImageResource(R.drawable.ic_pulltorefresh_arrow);
            // Clear the full rotation animation  
            mRefreshViewImage.clearAnimation();
            // Hide progress bar and arrow.  
            mRefreshViewImage.setVisibility(View.GONE);
            mRefreshViewProgress.setVisibility(View.GONE);
        }
    }

    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        int childWidthSpec = ViewGroup.getChildMeasureSpec(0,
                0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
            int visibleItemCount, int totalItemCount) {
        Log.d("w.w", "firstVisibleItem=" + firstVisibleItem);
        if (mCurrentScrollState == SCROLL_STATE_TOUCH_SCROLL
                && mRefreshState != REFRESHING) {
            if (firstVisibleItem + visibleItemCount == totalItemCount || firstVisibleItem==0) {
                mRefreshViewImage.setVisibility(View.VISIBLE);
                if ((
                        mRefreshView.getTop() <= getMeasuredHeight() - mRefreshViewHeight)
                        && mRefreshState != RELEASE_TO_REFRESH) {
                    mRefreshViewText.setText(R.string.pull_to_refresh_release_label);
                    mRefreshViewImage.clearAnimation();
                    mRefreshViewImage.startAnimation(mFlipAnimation);
                    mRefreshState = RELEASE_TO_REFRESH;
                } else if (mRefreshView.getTop() > getMeasuredHeight() - 20 - mRefreshViewHeight
                        && mRefreshState != PULL_TO_REFRESH) {
                    mRefreshViewText.setText(R.string.pull_to_refresh_pull_label);
                    if (mRefreshState != TAP_TO_REFRESH) {
                        mRefreshViewImage.clearAnimation();
                        mRefreshViewImage.startAnimation(mReverseFlipAnimation);
                    }
                    mRefreshState = PULL_TO_REFRESH;
                }
            } else {
                mRefreshViewImage.setVisibility(View.GONE);
                resetHeader();
            }
        } else if (mCurrentScrollState == SCROLL_STATE_FLING
                && getLastVisiblePosition() >= getAdapter().getCount() - 1
                && mRefreshState != REFRESHING) {

            if (getFooterViewsCount() > 0) {
                setSelectionFromTop(getAdapter().getCount() - 1, (this.getMeasuredHeight()));
            }

        }

        if (mOnScrollListener != null) {
            mOnScrollListener.onScroll(view, firstVisibleItem,
                    visibleItemCount, totalItemCount);
        }

    }

    public View getLoadBarView() {
        return mRefreshView;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        mCurrentScrollState = scrollState;

        if (mOnScrollListener != null) {
            mOnScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    public void prepareForRefresh() {
        resetHeaderPadding();

        mRefreshViewImage.setVisibility(View.GONE);
        mRefreshViewImage.setImageDrawable(null);
        mRefreshViewProgress.setVisibility(View.VISIBLE);

        mRefreshViewText.setText(R.string.pull_to_refresh_refreshing_label);

        mRefreshState = REFRESHING;
    }

    public void onRefresh() {
        Log.d(TAG, "onRefresh");

        if (mOnRefreshListener != null) {
            mOnRefreshListener.onRefresh();
        }
    }

    public void onRefreshComplete(CharSequence lastUpdated) {
        setLastUpdated(lastUpdated);
        onRefreshComplete();
    }

    public void onRefreshComplete() {
        Log.d(TAG, "onRefreshComplete");

        resetHeader();

        if (mRefreshView.getBottom() > 0) {
            invalidateViews();
        }
    }

    private class OnClickRefreshListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            if (mRefreshState != REFRESHING) {
                prepareForRefresh();
                onRefresh();
            }
        }

    }

    public interface OnRefreshListener {

        public void onRefresh();
    }
}
