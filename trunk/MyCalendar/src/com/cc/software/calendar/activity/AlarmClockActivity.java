package com.cc.software.calendar.activity;

import hut.cc.software.calendar.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class AlarmClockActivity extends Activity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clock_main_view);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.body_bg);
        BitmapDrawable logo = new BitmapDrawable(bitmap);
        logo.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
        logo.setDither(true);
        findViewById(R.id.clock_root).setBackgroundDrawable(logo);
        findViewById(R.id.add_alarm_clock).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.add_alarm_clock:
            Intent alarmSettingIntent = new Intent(this, AlarmSettingActivity.class);
            startActivity(alarmSettingIntent);
            break;

        default:
            break;
        }
    }
}
