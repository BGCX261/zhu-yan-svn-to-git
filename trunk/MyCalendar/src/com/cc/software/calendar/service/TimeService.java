package com.cc.software.calendar.service;

import java.util.Calendar;

import com.cc.software.calendar.clock.DigitalAppWidgetProvider;
import com.cc.software.calendar.comp.Constants;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class TimeService extends Service {

    public static boolean isStop = false;
    
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        if(!isStop){
            long now = System.currentTimeMillis();
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(System.currentTimeMillis());

            int minute = c.get(Calendar.MINUTE);
            c.set(Calendar.MINUTE, minute+1);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            PendingIntent pintent = PendingIntent.getService(this, 0, intent, 0);

            AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            long unit = c.getTimeInMillis() - System.currentTimeMillis();//���һ��
            alarm.set(AlarmManager.RTC_WAKEUP, now + unit, pintent);        
            
            Intent changeIntent =new Intent(this,DigitalAppWidgetProvider.class);
            changeIntent.setAction(DigitalAppWidgetProvider.CHANGE_ACTION);
            sendBroadcast(changeIntent);   
        }
        
    }
}
