package com.cc.software.calendar.clock;

import hut.cc.software.calendar.R;

import java.util.Calendar;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.cc.software.calendar.comp.Constants;
import com.cc.software.calendar.service.TimeService;
import com.cc.software.calendar.util.CommonUtil;

public class DigitalAppWidgetProvider extends AppWidgetProvider {

    public static final String CHANGE_ACTION = "com.cc.software.calendar.clock.CHANGE_ACTION";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        super.onReceive(context, intent);
        if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(action)) {
            RemoteViews views = getTimeView(context);
            changeRemoteViews(context, views);
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            int[] appids = manager.getAppWidgetIds(new ComponentName(context, DigitalAppWidgetProvider.class));
            manager.updateAppWidget(appids, views);
        } else if ("android.intent.action.TIME_SET".equals(action) || Intent.ACTION_TIMEZONE_CHANGED.equals(action)) {
            context.startService(new Intent(context, TimeService.class));
        } else if (CHANGE_ACTION.equals(action)) {
            RemoteViews views = getTimeView(context);
            changeRemoteViews(context, views);
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            int[] appids = manager.getAppWidgetIds(new ComponentName(context, DigitalAppWidgetProvider.class));
            manager.updateAppWidget(appids, views);
        } else if (AppWidgetManager.ACTION_APPWIDGET_DELETED.equals(action)) {
            //context.stopService(new Intent(context, TimeService.class));
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        CommonUtil.log_d(Constants.LOG_TAG, "onDeleted");
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
        Log.d(Constants.LOG_TAG, "onDisabled");
        TimeService.isStop = true;
        super.onDisabled(context);
    }

    @Override
    public void onEnabled(Context context) {
        Log.d(Constants.LOG_TAG, "onEnabled");
        TimeService.isStop = false;
        context.startService(new Intent(context, TimeService.class));
        super.onEnabled(context);
    }

    public static RemoteViews getTimeView(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.main);
        return views;
    }

    public static void changeRemoteViews(Context context, RemoteViews remoteViews) {
        DigitalClockData dcd = DigitalClockService.getInstance().getDigitalClockData();
        boolean isAm = dcd.isAM();
        int am = R.string.am;
        if (!isAm) {
            am = R.string.pm;
        }
        CharSequence amText = context.getText(am);
        remoteViews.setTextViewText(R.id.TextView_AM_PM, amText);
        int day = dcd.getDayOfMonth();
        int month = dcd.getMonth();
        String day_month = month + "/" + day;
        remoteViews.setTextViewText(R.id.TextView_Mon_Day, day_month);

        int weekRes = R.string.sun;
        switch (dcd.getWeek()) {
        case Calendar.SUNDAY:
            weekRes = R.string.sun;
            break;
        case Calendar.MONDAY:
            weekRes = R.string.mon;
            break;
        case Calendar.TUESDAY:
            weekRes = R.string.tue;
            break;
        case Calendar.WEDNESDAY:
            weekRes = R.string.wed;
            break;
        case Calendar.THURSDAY:
            weekRes = R.string.thu;
            break;
        case Calendar.FRIDAY:
            weekRes = R.string.fri;
            break;
        case Calendar.SATURDAY:
            weekRes = R.string.sat;
            break;
        default:
            break;
        }
        CharSequence weekText = context.getText(weekRes);
        remoteViews.setTextViewText(R.id.TextView_Week, weekText);
        remoteViews.setViewVisibility(R.id.TextView_Week, View.GONE);
        remoteViews.setImageViewResource(R.id.ImageView_Hour0, getTimeResId(dcd.getHour0()));
        remoteViews.setImageViewResource(R.id.ImageView_Hour1, getTimeResId(dcd.getHour1()));
        remoteViews.setImageViewResource(R.id.ImageView_Min0, getTimeResId(dcd.getMin0()));
        remoteViews.setImageViewResource(R.id.ImageView_Min1, getTimeResId(dcd.getMin1()));
    }

    public static int getTimeResId(int intTime) {
        int resId = R.drawable.number_pink0;
        switch (intTime) {
        case 0:
            resId = R.drawable.number_pink0;
            break;
        case 1:
            resId = R.drawable.number_pink1;
            break;
        case 2:
            resId = R.drawable.number_pink2;
            break;
        case 3:
            resId = R.drawable.number_pink3;
            break;
        case 4:
            resId = R.drawable.number_pink4;
            break;
        case 5:
            resId = R.drawable.number_pink5;
            break;
        case 6:
            resId = R.drawable.number_pink6;
            break;
        case 7:
            resId = R.drawable.number_pink7;
            break;
        case 8:
            resId = R.drawable.number_pink8;
            break;
        case 9:
            resId = R.drawable.number_pink9;
            break;
        default:
            break;
        }
        return resId;
    }
}
