package com.view.tab;

import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import com.view.wheelview.R;

public class TabRelativeLayout extends RelativeLayout implements Callback {
    private ViewGroup mParent;
    private final ArrayList<View> children;
    private final int TAB_OVERLAY;
    private int mTabParentCenter;
    private int newTabWidth;
    public final static int tab_animation_duration = 150;
    public final static int HScroll_duration = 250;
    private int TabWidth = 0;
    private Handler mHandler;
    private final static int TAB_SHOW_ANIMATION = 1, CHECK_TAB_VISIBLE = 2;
    private final static int TAB_ANIMATION_DELAYED_CHECK_TIME = 50;

    public void setCurrentTabParentMaxWidth(int width) {
        mTabParentCenter = width / 2;
    }

    public TabRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHandler = new Handler(this);
        TAB_OVERLAY = 0;
        children = new ArrayList<View>(12);
        newTabWidth = getResources().getDrawable(R.drawable.tab_strip_new_tab_btn_default).getIntrinsicWidth();
        TabWidth = (int) getResources().getDimension(R.dimen.tab_width);
    }

    @Override
    public void addView(View child, int index, android.view.ViewGroup.LayoutParams params) {
        if (children.size() == 0 || children.size() == 1) {
            children.add(child);
        } else {
            int currentTabIndex = 0;
            if (currentTabIndex < 0) {
                children.add(child);
            } else {
                children.add(currentTabIndex + 1, child);
            }
        }
        super.addView(child, index, params);
    }

    @Override
    public void removeView(View view) {
        children.remove(view);
        super.removeView(view);
    }

    @Override
    public void removeViewAt(int index) {
        throw new UnsupportedOperationException("not supposed to call this");
    }

    @Override
    public void removeAllViews() {
        super.removeAllViews();
        children.clear();
    }

    @Override
    public void removeViews(int start, int count) {
        throw new UnsupportedOperationException("not supposed to call this");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int offset = 0;
        if (children.size() != getChildCount()) {
            throw new RuntimeException("Error, children is not well managed locally, got different number of children");
        }
        int length = children.size();
        for (int i = 0; i < length; i++) {
            View v = children.get(i);
            if (i == 0) {
                v.findViewById(R.id.shadow_left).setVisibility(View.GONE);
                ((RelativeLayout.LayoutParams) v.getLayoutParams()).leftMargin = offset;
                offset = TabWidth;
                if (v.isSelected()) {
                    v.findViewById(R.id.right_shadow).setVisibility(View.VISIBLE);
                } else {
                    v.findViewById(R.id.right_shadow).setVisibility(View.GONE);
                }
            } else {
                if (v.isSelected()) {
                    v.findViewById(R.id.right_shadow).setVisibility(View.VISIBLE);
                    v.findViewById(R.id.shadow_left).setVisibility(View.VISIBLE);
                    offset -= v.findViewById(R.id.right_shadow).getMeasuredWidth();
                } else {
                    v.findViewById(R.id.right_shadow).setVisibility(View.GONE);
                    v.findViewById(R.id.shadow_left).setVisibility(View.GONE);
                }
                ((RelativeLayout.LayoutParams) v.getLayoutParams()).leftMargin = offset;
                offset = Math.max(0, offset + v.getMeasuredWidth() - TAB_OVERLAY);
                if (v.isSelected()) {
                    offset -= v.findViewById(R.id.right_shadow).getMeasuredWidth();
                }
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public final void bringTabViewToFitOtherStrip(View tab) {
        //((HScrollView) mParent).smartSmoothScrollTo(mParent.getScrollX(), mParent.getScrollY(), 0);
    }

    public final void bringTabToViewPort(final View tab, View tabstrip, boolean shouldAnimate) {
        int leftMargin = ((RelativeLayout.LayoutParams) tab.getLayoutParams()).leftMargin;
        int mWidth = getWidth() - newTabWidth;
        if (tab.getVisibility() != View.VISIBLE && shouldAnimate) {
            if (getWidth() <= mWidth) {
                if (tabstrip.getVisibility() == View.VISIBLE) {
                    tab.startAnimation(AnimationHelper.getTabShowAnimation(tab, tab_animation_duration));
                    checkAnimationEnd(tab, tab_animation_duration + TAB_ANIMATION_DELAYED_CHECK_TIME);
                } else {
                    tab.setVisibility(View.VISIBLE);
                }
            } else {
                if (tabstrip.getVisibility() == View.VISIBLE) {
                    postDelayed(showTab(tab), HScroll_duration);
                } else {
                    tab.setVisibility(View.VISIBLE);
                }
            }
        }
        if (tabstrip.getVisibility() == View.VISIBLE) {
            scrollRangeIntoViewCenter(tab);
        } else {
            scrollRangeIntoViewPortIfNecessary(leftMargin, tab.getWidth(), 0);
        }

        bringChildToFront(tab);
    }

    private final void scrollRangeIntoViewPortIfNecessary(int rangeStart, int length, int HScroll_duration) {
        if (mParent == null) {
            mParent = (ViewGroup) getParent();
        }
        final int scrollX = mParent.getScrollX();
        if (scrollX > rangeStart) {
            //((HScrollView) mParent).smartSmoothScrollTo(rangeStart, mParent.getScrollY(), HScroll_duration);
        } else if (rangeStart + length > scrollX + mParent.getWidth()) {
            //((HScrollView) mParent).smartSmoothScrollTo((rangeStart + length) - mParent.getWidth(),
            //                mParent.getScrollY(), HScroll_duration);
        } else {
            //when tab is layout in middle
            //((HScrollView) mParent).smartSmoothScrollTo(rangeStart, mParent.getScrollY(), HScroll_duration);
        }
    }

    public final void scrollRangeIntoViewCenter(final View tab) {
        if (tab == null) {
            return;
        }
        if (mParent == null) {
            mParent = (ViewGroup) getParent();
        }
        final int length = tab.getWidth();
        int rangeStart = ((RelativeLayout.LayoutParams) tab.getLayoutParams()).leftMargin;
        if (getWidth() == 0) {
            tab.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
                public boolean onPreDraw() {
                    srcollToCenterIfNecessary(((RelativeLayout.LayoutParams) tab.getLayoutParams()).leftMargin, length);
                    tab.getViewTreeObserver().removeOnPreDrawListener(this);
                    return true;
                }
            });
        } else {
            srcollToCenterIfNecessary(rangeStart, length);
        }
        bringChildToFront(tab);
    }

    private void srcollToCenterIfNecessary(int rangeStart, int length) {
        if ((rangeStart + length / 2) < mTabParentCenter) {
            //((HScrollView) mParent).smartSmoothScrollTo(0, mParent.getScrollY(), HScroll_duration);
        } else {
            //            ((HScrollView) mParent).smartSmoothScrollTo(rangeStart + length / 2 - mTabParentCenter,
            //                            mParent.getScrollY(), HScroll_duration);
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
        case TAB_SHOW_ANIMATION:
            View tab = (View) msg.obj;
            //在4.0的机子上有時候tab动画的OnAnimationEnd方法没有被调到，导致tab没有显示出来，新加了一个tab的检测Message
            checkAnimationEnd(tab, tab_animation_duration + TAB_ANIMATION_DELAYED_CHECK_TIME);
            Animation animation = AnimationHelper.getTabShowAnimation(tab, tab_animation_duration);
            tab.startAnimation(animation);
            invalidate();
            break;
        case CHECK_TAB_VISIBLE:
            View mTab = (View) msg.obj;
            if (mTab.getVisibility() != View.VISIBLE) {
                mTab.setVisibility(View.VISIBLE);
            }
            break;
        default:
            break;
        }
        return false;
    }

    private Runnable showTab(final View tab) {
        Runnable show_tab = new Runnable() {

            @Override
            public void run() {
                mHandler.removeMessages(TAB_SHOW_ANIMATION);
                Message msg = Message.obtain();
                msg.obj = tab;
                msg.what = TAB_SHOW_ANIMATION;
                mHandler.sendMessage(msg);
            }
        };
        return show_tab;
    }

    private void checkAnimationEnd(View tab, int delayed) {
        Message check_msg = Message.obtain();
        check_msg.obj = tab;
        check_msg.what = CHECK_TAB_VISIBLE;
        mHandler.sendMessageDelayed(check_msg, delayed);
    }
}
