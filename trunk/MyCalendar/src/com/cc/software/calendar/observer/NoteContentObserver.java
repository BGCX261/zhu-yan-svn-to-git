package com.cc.software.calendar.observer;

import android.database.ContentObserver;
import android.os.Handler;

public class NoteContentObserver extends ContentObserver {

    private Handler mHandler;
    public static final int NOTE_URI_CHANGED = 1;

    public NoteContentObserver(Handler handler) {
        super(handler);
        mHandler = handler;
    }

    @Override
    public void onChange(boolean selfChange) {
        mHandler.sendEmptyMessage(NOTE_URI_CHANGED);
    }

}
