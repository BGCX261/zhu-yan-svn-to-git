package com.cc.software.calendar.activity;

import hut.cc.software.calendar.R;
import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;

import com.cc.software.calendar.comp.Constants;
import com.cc.software.calendar.observer.NoteContentObserver;
import com.cc.software.calendar.provider.CalendarWidgetProvider;
import com.cc.software.calendar.provider.Note;
import com.cc.software.calendar.util.CalendarManager;
import com.cc.software.calendar.util.CommonUtil;
import com.cc.software.calendar.util.DialogUtil;
import com.cc.software.calendar.view.CalendarDayView;
import com.cc.software.calendar.view.CalendarMonthView;
import com.cc.software.calendar.view.NoteMediaView;
import com.cc.software.calendar.view.NoteView;
import com.cc.software.calendar.view.ToolsView;
import com.cc.software.calendar.view.ViewContainer;
import com.cc.software.calendar.view.ViewContainer.OnScrollToScreen;

public class MainActivity extends Activity implements OnClickListener, OnScrollToScreen {

    private CalendarMonthView mMonthView;
    private ViewGroup mWrapper;
    private CalendarDayView mDayView;
    private NoteView mNoteView;
    private ToolsView mEntertainmentView;
    private View btnMonthView, btnDayView, btnNoteView, btnMore;
    private ViewGroup.LayoutParams params;
    private NoteMediaView mMediaView;
    private Handler mHandler;
    private NoteContentObserver mObserver;
    private ViewContainer mViewContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        long date = System.currentTimeMillis();
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_view);
        mViewContainer = (ViewContainer) findViewById(R.id.container);
        CalendarManager.copyDataBaseIntoStorage(this);
        params = new ViewGroup.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,
                        android.view.ViewGroup.LayoutParams.FILL_PARENT);
        //((LinearLayout) findViewById(R.id.dayview)).addView(getCalendarDayView(), params);
        mViewContainer.addView(getCalendarDayView(), params);
        CommonUtil.log_d(Constants.LOG_TAG, "getCalendarDayView time =" + (System.currentTimeMillis() - date));
        //((LinearLayout) findViewById(R.id.monthview)).addView(getCalendarMonthView(), params);
        mViewContainer.addView(getCalendarMonthView(), params);
        CommonUtil.log_d(Constants.LOG_TAG, "getCalendarMonthView time =" + (System.currentTimeMillis() - date));
        //((LinearLayout) findViewById(R.id.noteview)).addView(getNoteView(), params);
        //((LinearLayout) findViewById(R.id.toolview)).addView(geteEntertainmentView(), params);
        mViewContainer.addView(getNoteView(), params);
        mViewContainer.addView(geteToolsView(),params);
        CommonUtil.log_d(Constants.LOG_TAG, "geteEntertainmentView time =" + (System.currentTimeMillis() - date));
        btnDayView = findViewById(R.id.day_view);
        btnMonthView = findViewById(R.id.month_view);
        btnNoteView = findViewById(R.id.note_tv);
        btnMore = findViewById(R.id.more_tv);

        mViewContainer.setOnScrollToScreen(this);
        btnDayView.setOnClickListener(this);
        btnMonthView.setOnClickListener(this);
        btnNoteView.setOnClickListener(this);
        btnMore.setOnClickListener(this);

        mWrapper = (ViewGroup) findViewById(R.id.wrapper);
        mWrapper.setBackgroundDrawable(CommonUtil.getBackGround(this));
        CommonUtil.log_d(Constants.LOG_TAG, "setBackgroundDrawable time =" + (System.currentTimeMillis() - date));
        registerNoteContentObserver();
        Intent intent = getIntent();
        if (intent != null) {
            String action = intent.getAction();
            if (CalendarWidgetProvider.DAY_ACTION.equals(action)) {
                mViewContainer.setToScreen(0);
            } else if (CalendarWidgetProvider.MONTH_ACTION.equals(action)) {
                mViewContainer.setToScreen(1);
            }
        }
    }

    private void registerNoteContentObserver() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                getNoteView().notifyNoteChanged();
                getCalendarDayView().onNoteDataChanged();
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

    private ToolsView geteToolsView() {
        if (mEntertainmentView == null) {
            mEntertainmentView = new ToolsView(this);
        }
        return mEntertainmentView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.day_view:
            mViewContainer.setToScreen(0);
            break;
        case R.id.month_view:
            mViewContainer.setToScreen(1);
            break;
        case R.id.note_tv:
            mViewContainer.setToScreen(2);
            break;
        case R.id.more_tv:
            mViewContainer.setToScreen(3);
            break;
        default:
            break;
        }
    }

    public void onNoteViewClick() {
        mViewContainer.setToScreen(2);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
        case KeyEvent.KEYCODE_BACK:
            if (mNoteView != null && mNoteView.onKeyDown(keyCode, event)) {
                return true;
            } else {
                DialogUtil.createConfirmExitDialog(this, confirmExitListener).show();
            }
            break;
        default:
            break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private DialogInterface.OnClickListener confirmExitListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
            case Dialog.BUTTON1:
                ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).cancelAll();
                Process.killProcess(Process.myPid());
                break;
            case Dialog.BUTTON2:
                break;
            default:
                break;
            }
            dialog.dismiss();
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        int currentIndex = mViewContainer.getCurScreen();
        if (resultCode == Constants.RESUSTCODE_YEAR_MONTH) {
            mMonthView.onActivityResult(requestCode, resultCode, data);
            mDayView.onActivityResult(requestCode, resultCode, data);
        } else {
            if (currentIndex == 1) {
                mMonthView.onActivityResult(requestCode, resultCode, data);
            } else if (currentIndex == 2) {
                mNoteView.onActivityResult(requestCode, resultCode, data);
            } else if (currentIndex == 0) {
                mDayView.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    public void popUpMediaView(Bitmap bitmap) {
        mMediaView.popUpNoteMediaView(bitmap);
    }

    @Override
    public void doAction(int duration, boolean isCurrentScreen) {
        int currentIndex = mViewContainer.getCurScreen();
        focusMenuTitleByIndex(currentIndex);
    }

    private void focusMenuTitleByIndex(int index) {
        btnDayView.setSelected(index == 0);
        btnMonthView.setSelected(index == 1);
        btnNoteView.setSelected(index == 2);
        btnMore.setSelected(index == 3);
    }
}
