package com.cc.software.calendar.view;import android.app.Activity;import android.content.Context;import android.util.AttributeSet;import android.view.MotionEvent;import android.view.VelocityTracker;import android.view.View;import android.view.ViewConfiguration;import android.view.ViewGroup;import android.view.inputmethod.InputMethodManager;import android.widget.Scroller;public class ViewContainer extends ViewGroup {    public static boolean startTouch = true;    public boolean isScrollable = true;    public boolean isScrollable() {        return isScrollable;    }    public void setScrollable(boolean isScrollable) {        this.isScrollable = isScrollable;    }    private Scroller mScroller;    /*     * 速度追踪器，主要是为了通过当前滑动速度判断当前滑动是否为fling     */    private VelocityTracker mVelocityTracker;    /*     * 记录当前屏幕下标，取值范围是：0 到 getChildCount()-1     */    private int mCurScreen;    private int mDefaultScreen = 0;    private Context mContext;    /**     * 初始化后默认显示的那个屏幕号     * @param defaultScreen     */    public void setDefaultScreen(int defaultScreen) {        this.mDefaultScreen = defaultScreen;    }    /*     * Touch状态值 0：静止 1：滑动     */    private static final int TOUCH_STATE_REST = 0;    private static final int TOUCH_STATE_SCROLLING = 1;    /*     * 记录当前touch事件状态--滑动（TOUCH_STATE_SCROLLING）、静止（TOUCH_STATE_REST 默认）     */    private int mTouchState = TOUCH_STATE_REST;    private static final int SNAP_VELOCITY = 600;    /*     * 记录touch事件中被认为是滑动事件前的最大可滑动距离     */    private int mTouchSlop;    /*     * 记录滑动时上次手指所处的位置     */    private float mLastMotionX;    private float mLastMotionY;    private OnScrollToScreen onScrollToScreen = null;    private OnScrollScreenChangedListener mOnScrollScreenChangedListener = null;    public ViewContainer(Context context, AttributeSet attrs) {        this(context, attrs, 0);    }    public ViewContainer(Context context, AttributeSet attrs, int defStyle) {        super(context, attrs, defStyle);        mScroller = new Scroller(context);        mCurScreen = mDefaultScreen;        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();        mContext = context;    }    @Override    protected void onLayout(boolean changed, int l, int t, int r, int b) {        int childLeft = 0;        final int childCount = getChildCount();        for (int i = 0; i < childCount; i++) {            final View childView = getChildAt(i);            if (childView.getVisibility() != View.GONE) {                final int childWidth = childView.getMeasuredWidth();                childView.layout(childLeft, 0, childLeft + childWidth, childView.getMeasuredHeight());                childLeft += childWidth;            }        }    }    @Override    protected void onScrollChanged(int l, int t, int oldl, int oldt) {        super.onScrollChanged(l, t, oldl, oldt);        if (mOnScrollScreenChangedListener != null) {            mOnScrollScreenChangedListener.onScrollChanged();        }    }    @Override    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {        super.onMeasure(widthMeasureSpec, heightMeasureSpec);        final int width = MeasureSpec.getSize(widthMeasureSpec);        final int count = getChildCount();        for (int i = 0; i < count; i++) {            getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);        }        scrollTo(mCurScreen * width, 0);        doScrollAction();    }    /**     * 方法名称：snapToDestination 方法描述：根据当前位置滑动到相应界面     *     * @param whichScreen     */    public void snapToDestination() {        final int screenWidth = getWidth();        final int destScreen = (getScrollX() + screenWidth / 2) / screenWidth;        snapToScreen(destScreen);    }    /**     * 方法名称：snapToScreen 方法描述：滑动到到第whichScreen（从0开始）个界面，有过渡效果     *     * @param whichScreen     */    public void snapToScreen(int whichScreen) {        // get the valid layout page        whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));        boolean isCurrentScreen = whichScreen == mCurScreen ? true : false;        if (getScrollX() != (whichScreen * getWidth())) {            final int delta = whichScreen * getWidth() - getScrollX();            final int duration = Math.abs(delta) * 2;            mScroller.startScroll(getScrollX(), 0, delta, 0, duration);            mCurScreen = whichScreen;            doScrollAction(duration, isCurrentScreen);            invalidate(); // Redraw the layout        }    }    /**     * 方法名称：setToScreen 方法描述：指定并跳转到第whichScreen（从0开始）个界面     *     * @param whichScreen     */    public void setToScreen(int whichScreen) {        whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));        mCurScreen = whichScreen;        scrollTo(whichScreen * getWidth(), 0);        doScrollAction();    }    public int getCurScreen() {        return mCurScreen;    }    @Override    public void computeScroll() {        if (mScroller.computeScrollOffset()) {            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());            postInvalidate();        }    }    @Override    public boolean onTouchEvent(MotionEvent event) {        if (mVelocityTracker == null) {            mVelocityTracker = VelocityTracker.obtain();        }        mVelocityTracker.addMovement(event);        final int action = event.getAction();        final float x = event.getX();        switch (action) {        case MotionEvent.ACTION_DOWN:            if (!mScroller.isFinished()) {                mScroller.abortAnimation();            }            mLastMotionX = x;            break;        case MotionEvent.ACTION_MOVE:            if (!isScrollable) {                return false;            }            if (imm == null)                imm = (InputMethodManager) mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);            if (imm.isActive()) {                imm.hideSoftInputFromWindow(getWindowToken(), 0);            }            int deltaX = (int) (mLastMotionX - x);            mLastMotionX = x;            if (deltaX > 0) {                // moving left                if (getScrollX() >= getWidth() * (getChildCount() - 1)) {                    // do nothing so won't scroll over the right edge                } else {                    int actualMovedValue = Math.min(deltaX, (getWidth() * (getChildCount() - 1)) - getScrollX());                    scrollBy(actualMovedValue, 0);                    this.doContentScrollAction(actualMovedValue);                }            } else {                // moving right                if (getScrollX() == 0) {                    // do nothing so won't scroll over the left edge                } else {                    int actualMovedValue = Math.min(-deltaX, getScrollX());                    scrollBy(-actualMovedValue, 0);                    this.doContentScrollAction(-actualMovedValue);                }            }            break;        case MotionEvent.ACTION_UP:        case MotionEvent.ACTION_CANCEL:            final VelocityTracker velocityTracker = mVelocityTracker;            velocityTracker.computeCurrentVelocity(1000);            int velocityX = (int) velocityTracker.getXVelocity();            if (velocityX > SNAP_VELOCITY && mCurScreen > 0) {                // Fling enough to move left                snapToScreen(mCurScreen - 1);            } else if (velocityX < -SNAP_VELOCITY && mCurScreen < getChildCount() - 1) {                // Fling enough to move right                snapToScreen(mCurScreen + 1);            } else {                snapToDestination();            }            if (mVelocityTracker != null) {                mVelocityTracker.recycle();                mVelocityTracker = null;            }            mTouchState = TOUCH_STATE_REST;            break;        /*case MotionEvent.ACTION_CANCEL:            mTouchState = TOUCH_STATE_REST;            break;        */        }        return true;    }    InputMethodManager imm;    @Override    public boolean onInterceptTouchEvent(MotionEvent ev) {        final int action = ev.getAction();        if ((action == MotionEvent.ACTION_MOVE) && (mTouchState != TOUCH_STATE_REST)) {            return true;        }        final float x = ev.getX();        final float y = ev.getY();        switch (action) {        case MotionEvent.ACTION_DOWN:            mLastMotionX = x;            mLastMotionY = y;            mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST : TOUCH_STATE_SCROLLING;            break;        case MotionEvent.ACTION_MOVE:            if (!isScrollable) {                return false;            }            if (imm == null)                imm = (InputMethodManager) mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);            if (imm.isActive()) {                imm.hideSoftInputFromWindow(getWindowToken(), 0);            }            final int xDiff = (int) Math.abs(mLastMotionX - x);            if (xDiff > mTouchSlop) {                if (Math.abs(mLastMotionY - y) / Math.abs(mLastMotionX - x) < 1)                    mTouchState = TOUCH_STATE_SCROLLING;            }            break;        case MotionEvent.ACTION_CANCEL:        case MotionEvent.ACTION_UP:            mTouchState = TOUCH_STATE_REST;            break;        }        return mTouchState != TOUCH_STATE_REST;    }    /**     * 方法名称：doScrollAction 方法描述：当滑动切换界面时执行相应操作     *     * @param index     */    private void doScrollAction() {        this.doScrollAction(0, mCurScreen == 0 ? true : false);    }    private void doScrollAction(int duration, boolean isCurrentScreen) {        if (onScrollToScreen != null) {            onScrollToScreen.doAction(duration, isCurrentScreen);        }    }    /**     * 方法名称：setOnScrollToScreen 方法描述：设置内部接口的实现类实例     *     * @param index     */    public void setOnScrollToScreen(OnScrollToScreen paramOnScrollToScreen) {        onScrollToScreen = paramOnScrollToScreen;    }    public void setOnScrollScreenChangedListener(OnScrollScreenChangedListener onScrollScreenChangedListener) {        mOnScrollScreenChangedListener = onScrollScreenChangedListener;    }    public void removeOnScrollScreenChangedListener() {        mOnScrollScreenChangedListener = null;    }    /**    * 接口名称：OnScrollToScreen 接口描述：当滑动到某个界面时可以调用该接口下的*doAction()方法执行某些操作     *     * @author wader     */    public abstract interface OnScrollToScreen {        public void doAction(int duration, boolean isCurrentScreen);    }    public abstract interface OnScrollScreenChangedListener {        public void onScrollChanged();    }    OnContentScrollListener mOnContentScrollListener;    private void doContentScrollAction(int deltaX) {        if (mOnContentScrollListener != null) {            mOnContentScrollListener.onContentScroll(-(deltaX / getChildCount()));        }    }    public void setOnContentScrollListener(OnContentScrollListener onContentScrollListener) {        mOnContentScrollListener = onContentScrollListener;    }    public abstract interface OnContentScrollListener {        public void onContentScroll(int deltaX);    }}