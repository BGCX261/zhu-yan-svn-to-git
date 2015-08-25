package com.cc.software.calendar.service;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.cc.software.calendar.provider.CalendarWidgetProvider;

public class DayService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        long now = System.currentTimeMillis();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());

        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day+1);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);


        PendingIntent pintent = PendingIntent.getService(this, 0, intent, 0);

        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        long unit = c.getTimeInMillis() - System.currentTimeMillis();//���һ��
        alarm.set(AlarmManager.RTC_WAKEUP, now + unit, pintent);

        Intent changeIntent = new Intent(this, CalendarWidgetProvider.class);
        changeIntent.setAction(CalendarWidgetProvider.DAY_CHANGE_ACTION);
        sendBroadcast(changeIntent);

    }
}
