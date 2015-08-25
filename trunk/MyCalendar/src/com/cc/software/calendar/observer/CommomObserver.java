package com.cc.software.calendar.observer;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;

public class CommomObserver extends ContentObserver {
    public static int CALLLOG_CONTENT_CHANGE = 0;

    private Handler handler;

    public CommomObserver(Context context, Handler handler) {
        super(handler);
        this.handler = handler;
    }

    public void onChange(boolean selfChange) {
        handler.sendEmptyMessage(CommomObserver.CALLLOG_CONTENT_CHANGE);
    }

}