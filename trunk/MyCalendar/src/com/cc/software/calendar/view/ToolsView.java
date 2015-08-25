package com.cc.software.calendar.view;

import hut.cc.software.calendar.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.cc.software.calendar.activity.BackUpActivity;
import com.cc.software.calendar.activity.SMSContentActivity;
import com.cc.software.calendar.activity.WeiboInfoActivity;
import com.cc.software.calendar.clock.AlarmClock;

public class ToolsView extends LinearLayout implements OnClickListener {

    private Context mContext;

    public ToolsView(Context context) {
        this(context, null);
    }

    public ToolsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        setOrientation(LinearLayout.VERTICAL);
        LayoutInflater.from(mContext).inflate(R.layout.entertainment_view, this);
        findViewById(R.id.sina_blog).setOnClickListener(this);
        findViewById(R.id.alarm_clock).setOnClickListener(this);
        findViewById(R.id.tool_sms).setOnClickListener(this);
        findViewById(R.id.tool_back_up).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.sina_blog:
            Intent intent = new Intent(mContext, WeiboInfoActivity.class);
            mContext.startActivity(intent);
            
            ((Activity)mContext).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            break;
        case R.id.alarm_clock:
            Intent alarmIntent = new Intent(mContext, AlarmClock.class);
            mContext.startActivity(alarmIntent);
            ((Activity)mContext).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            break;
        case R.id.tool_sms:
            Intent smsIntent = new Intent(mContext, SMSContentActivity.class);
            mContext.startActivity(smsIntent);
            ((Activity)mContext).overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
            break;
        case R.id.tool_back_up:
            Intent backUpIntent = new Intent(mContext, BackUpActivity.class);
            mContext.startActivity(backUpIntent);
            ((Activity)mContext).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            break;
        default:
            break;
        }
    }

}
