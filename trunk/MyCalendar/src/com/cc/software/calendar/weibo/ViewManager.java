package com.cc.software.calendar.weibo;

import java.util.Stack;

import com.cc.software.calendar.util.CommonUtil;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class ViewManager {

    private static Stack<View> mViewStack = null;
    private static ViewManager mViewManager = null;
    private static LinearLayout.LayoutParams mLayoutParams = null;
    private View mCurrentShow = null;
    private LinearLayout root = null;
    private Context mContext = null;

    private ViewManager() {
        mViewStack = new Stack<View>();
        mLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    public View getRootView() {
        if (mContext == null)
            return null;

        if (root == null) {
            root = new LinearLayout(mContext);
        }
        return root;
    }

    public void destoryRootView() {
        if (root != null) {
            root = null;
        }
    }

    public static ViewManager getInstance() {
        if (mViewManager == null)
            mViewManager = new ViewManager();
        return mViewManager;
    }

    public boolean initialize(Context context, View initChild) {
        if (root == null) {
            mContext = context;
            if (getRootView() == null)
                return false;
        }

        if (initChild == null)
            return false;
        root.addView(initChild, mLayoutParams);
        return true;
    }

    public void Push(View gone, View display) {
        if (display == null)
            return;
        mCurrentShow = display;
        if (gone == null)
            return;
        if (mViewStack == null || mViewStack.push(gone) == null)
            return;
        root.removeView(gone);
        root.addView(display, mLayoutParams);
        display.setBackgroundDrawable(CommonUtil.getBackGround(mContext));
        return;
    }

    public boolean Pop() {
        if (mViewStack == null || mViewStack.isEmpty())
            return false;

        View willShow = mViewStack.pop();

        if (willShow != null && mCurrentShow != null) {
            root.removeView(mCurrentShow);
            if (willShow instanceof LoginView) {
                return false;
            }
            mCurrentShow = willShow;
            root.addView(mCurrentShow, mLayoutParams);
            return true;
        }

        return false;
    }
}
