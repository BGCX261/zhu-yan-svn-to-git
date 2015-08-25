package com.cc.software.calendar.clock;

import hut.cc.software.calendar.R;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Simple widget to show analog clock.
 */
public class AnalogAppWidgetProvider extends BroadcastReceiver {
    static final String TAG = "AnalogAppWidgetProvider";

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        
        if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(action)) {
            RemoteViews views = new RemoteViews(context.getPackageName(),
                    R.layout.clock_analog_appwidget);
            int[] appWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
            
            AppWidgetManager gm = AppWidgetManager.getInstance(context);
            gm.updateAppWidget(appWidgetIds, views);
        }
    }
    
    
}

