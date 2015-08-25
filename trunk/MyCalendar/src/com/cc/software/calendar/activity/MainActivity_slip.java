package com.cc.software.calendar.activity;

import hut.cc.software.calendar.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.cc.software.calendar.observer.NoteContentObserver;
import com.cc.software.calendar.provider.Note;
import com.cc.software.calendar.util.CalendarManager;
import com.cc.software.calendar.util.CommonUtil;
import com.cc.software.calendar.util.DateUtil;
import com.cc.software.calendar.view.CalendarDayView;
import com.cc.software.calendar.view.CalendarMonthView;
import com.cc.software.calendar.view.ToolsView;
import com.cc.software.calendar.view.NoteMediaView;
import com.cc.software.calendar.view.NoteView;

public class MainActivity_slip extends Activity implements OnClickListener {

    private CalendarMonthView mMonthView;
    private ViewGroup mWrapper;
    private CalendarDayView mDayView;
    private NoteView mNoteView;
    private ToolsView mEntertainmentView;
    private LinearLayout mRoot;
    private View btnMonthView, btnDayView, btnNoteView, btnMore;
    private LayoutParams params;
    private FrameLayout.LayoutParams mFraParams;
    private NoteMediaView mMediaView;
    private View mCurrentClick, mLastClick;
    private Handler mHandler;
    private NoteContentObserver mObserver;
    private View mCurrentShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_view);
        CalendarManager.copyDataBaseIntoStorage(this);
        btnDayView = findViewById(R.id.day_view);
        btnMonthView = findViewById(R.id.month_view);
        btnNoteView = findViewById(R.id.note_tv);
        btnMore = findViewById(R.id.more_tv);

        btnDayView.setOnClickListener(this);
        btnMonthView.setOnClickListener(this);
        btnNoteView.setOnClickListener(this);
        btnMore.setOnClickListener(this);

        mRoot = (LinearLayout) findViewById(R.id.root);
        mWrapper = (ViewGroup) findViewById(R.id.wrapper);
        
        mWrapper.setBackgroundDrawable(CommonUtil.getBackGround(this));

        params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

        mFraParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT,
                        FrameLayout.LayoutParams.FILL_PARENT);

        mRoot.addView(getCalendarDayView(), params);
        mLastClick = btnDayView;
        btnDayView.setSelected(true);

        mMediaView = new NoteMediaView(this);
        mWrapper.addView(mMediaView, mFraParams);
        mMediaView.dismissNoteMediaView();
        registerNoteContentObserver();
        mCurrentShow = mDayView;
    }

    private void registerNoteContentObserver() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == NoteContentObserver.NOTE_URI_CHANGED) {
                    if (mCurrentShow == mNoteView) {
                        mNoteView.notifyNoteChanged();

                    } else if (mCurrentShow == mDayView) {
                        mDayView.onNoteDataChanged();
                    }

                }
            }
        };
        mObserver = new NoteContentObserver(mHandler);
        getContentResolver().registerContentObserver(Note.NOTE_URI, true, mObserver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getContentResolver().unregisterContentObserver(mObserver);
    }

    private CalendarMonthView getCalendarMonthView() {
        if (mMonthView == null) {
            mMonthView = new CalendarMonthView(this);
        }
        return mMonthView;
    }

    private NoteView getNoteView() {
        if (mNoteView == null) {
            mNoteView = new NoteView(this);
        }
        return mNoteView;
    }

    private CalendarDayView getCalendarDayView() {
        if (mDayView == null) {
            mDayView = new CalendarDayView(this);
        }
        return mDayView;
    }

    private ToolsView geteEntertainmentView() {
        if (mEntertainmentView == null) {
            mEntertainmentView = new ToolsView(this);
        }
        return mEntertainmentView;
    }

    @Override
    public void onClick(View v) {
        mCurrentClick = v;
        if (mCurrentClick != mLastClick) {
            mCurrentClick.setSelected(true);
            mLastClick.setSelected(false);
            mRoot.removeAllViews();
        } else {
            return;
        }
        switch (v.getId()) {
        case R.id.day_view:
            mRoot.addView(getCalendarDayView(), params);
            mDayView.updateContent(DateUtil.getYear(), DateUtil.getMonth(), DateUtil.getDay());
            mCurrentShow = mDayView;
            break;
        case R.id.month_view:
            mRoot.addView(getCalendarMonthView(), params);
            mMonthView.updateLayout(DateUtil.getYear(), DateUtil.getMonth());
            mCurrentShow = mMonthView;
            break;
        case R.id.note_tv:
            mRoot.addView(getNoteView(), params);
            mCurrentShow = mNoteView;
            break;
        case R.id.more_tv:            
            mRoot.addView(geteEntertainmentView(), params);
            mCurrentShow = mEntertainmentView;
            break;
        default:
            break;
        }
        mLastClick = v;
    }
    
    public void onNoteViewClick(){
        mCurrentClick = btnNoteView;
        mCurrentClick.setSelected(true);
        mLastClick.setSelected(false);
        mRoot.removeAllViews();
        mRoot.addView(getNoteView(), params);
        mCurrentShow = mNoteView;
        mLastClick = btnNoteView;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
        case KeyEvent.KEYCODE_BACK:
            if (mNoteView != null && mNoteView.onKeyDown(keyCode, event)) {
                return true;
            }
            break;
        default:
            break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mCurrentShow instanceof CalendarMonthView) {
            mMonthView.onActivityResult(requestCode, resultCode, data);
        } else if (mCurrentShow instanceof NoteView) {
            mNoteView.onActivityResult(requestCode, resultCode, data);
        }

    }

    public void popUpMediaView(Bitmap bitmap) {
        mMediaView.popUpNoteMediaView(bitmap);
    }
}
