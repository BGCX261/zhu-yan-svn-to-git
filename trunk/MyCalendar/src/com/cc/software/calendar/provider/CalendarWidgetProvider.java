package com.cc.software.calendar.provider;

import hut.cc.software.calendar.R;

import java.util.Calendar;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.cc.software.calendar.activity.MainActivity;
import com.cc.software.calendar.comp.Constants;
import com.cc.software.calendar.service.DayService;
import com.cc.software.calendar.util.CalendarManager;
import com.cc.software.calendar.util.CalendarUtil;
import com.cc.software.calendar.util.WidgetDateUtil;
import com.cc.software.calendar.view.CalendarMonthView;

/**
 * Simple widget to show analog clock.
 */
public class CalendarWidgetProvider extends AppWidgetProvider {
    static final String TAG = "AnalogAppWidgetProvider";

    public static final String LAST_MONTH_ACTION = "com.cc.software.calendar.LAST_MONTH_ACTION";
    public static final String NEXT_MONTH_ACTION = "com.cc.software.calendar.NEXT_MONTH_ACTION";
    public static final String REFRESH_ACTION = "com.cc.software.calendar.REFRESH_ACTION";
    public static final String DAY_ACTION = "com.cc.software.calendar.DAY_ACTION";
    public static final String MONTH_ACTION = "com.cc.software.calendar.MONTH_ACTION";
    public static final String DAY_CHANGE_ACTION = "com.cc.software.calendar.DAY_CHANGE_ACTION";

    private static int Cal_Id[] = { R.id.cal01, R.id.cal02, R.id.cal03, R.id.cal04, R.id.cal05, R.id.cal06, R.id.cal07,
                    R.id.cal08, R.id.cal09, R.id.cal10, R.id.cal11, R.id.cal12, R.id.cal13, R.id.cal14, R.id.cal15,
                    R.id.cal16, R.id.cal17, R.id.cal18, R.id.cal19, R.id.cal20, R.id.cal21, R.id.cal22, R.id.cal23,
                    R.id.cal24, R.id.cal25, R.id.cal26, R.id.cal27, R.id.cal28, R.id.cal29, R.id.cal30, R.id.cal31,
                    R.id.cal32, R.id.cal33, R.id.cal34, R.id.cal35, R.id.cal36, R.id.cal37, R.id.cal38, R.id.cal39,
                    R.id.cal40, R.id.cal41, R.id.cal42, };

    private static int NongLi_Id[] = { R.id.nongli01, R.id.nongli02, R.id.nongli03, R.id.nongli04, R.id.nongli05,
                    R.id.nongli06, R.id.nongli07, R.id.nongli08, R.id.nongli09, R.id.nongli10, R.id.nongli11,
                    R.id.nongli12, R.id.nongli13, R.id.nongli14, R.id.nongli15, R.id.nongli16, R.id.nongli17,
                    R.id.nongli18, R.id.nongli19, R.id.nongli20, R.id.nongli21, R.id.nongli22, R.id.nongli23,
                    R.id.nongli24, R.id.nongli25, R.id.nongli26, R.id.nongli27, R.id.nongli28, R.id.nongli29,
                    R.id.nongli30, R.id.nongli31, R.id.nongli32, R.id.nongli33, R.id.nongli34, R.id.nongli35,
                    R.id.nongli36, R.id.nongli37, R.id.nongli38, R.id.nongli39, R.id.nongli40, R.id.nongli41,
                    R.id.nongli42, };

    public static RemoteViews getRemoteViews(Context context) {
        return new RemoteViews(context.getPackageName(), R.layout.calendar);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d(Constants.LOG_TAG, intent.getAction());
        String action = intent.getAction();
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.calendar);
        if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(action)) {
            changeRemoteViews(context, views);
            Intent in = new Intent(context, CalendarWidgetProvider.class);
            in.setAction(LAST_MONTH_ACTION);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, in, 0);
            views.setOnClickPendingIntent(R.id.last_month, pendingIntent);

            in.setAction(NEXT_MONTH_ACTION);
            pendingIntent = PendingIntent.getBroadcast(context, 0, in, 0);
            views.setOnClickPendingIntent(R.id.next_month, pendingIntent);

            in.setAction(REFRESH_ACTION);
            pendingIntent = PendingIntent.getBroadcast(context, 0, in, 0);
            views.setOnClickPendingIntent(R.id.refresh, pendingIntent);

            in = new Intent(context, MainActivity.class);
            in.setAction(DAY_ACTION);
            pendingIntent = PendingIntent.getActivity(context, 0, in, 0);
            views.setOnClickPendingIntent(R.id.day, pendingIntent);

            in = new Intent(context, MainActivity.class);
            in.setAction(MONTH_ACTION);
            pendingIntent = PendingIntent.getActivity(context, 0, in, 0);
            views.setOnClickPendingIntent(R.id.date, pendingIntent);
            
            context.startService(new Intent(context,DayService.class));
        } else if (NEXT_MONTH_ACTION.equals(action)) {
            updateMonth(true);
            changeRemoteViews(context, views);
        } else if (LAST_MONTH_ACTION.equals(action)) {
            updateMonth(false);
            changeRemoteViews(context, views);
        } else if (REFRESH_ACTION.equals(action)) {
            WidgetDateUtil.resetCalendar();
            changeRemoteViews(context, views);
        } else if (AppWidgetManager.ACTION_APPWIDGET_DELETED.equals(action)) {
            WidgetDateUtil.resetCalendar();
            //context.stopService(new Intent(context,DayService.class));
        } else if (DAY_CHANGE_ACTION.equals(action)) {
            WidgetDateUtil.resetCalendar();
            changeRemoteViews(context, views);
        } else if ("android.intent.action.TIME_SET".equals(action) || Intent.ACTION_TIMEZONE_CHANGED.equals(action)) {
            context.startService(new Intent(context, DayService.class));
            Log.d(Constants.LOG_TAG, "startService");
        }
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        int[] appids = manager.getAppWidgetIds(new ComponentName(context, CalendarWidgetProvider.class));
        manager.updateAppWidget(appids, views);
    }

    public static void changeRemoteViews(Context context, RemoteViews remoteViews) {
        CalendarUtil instance = CalendarUtil.getInstance();
        int year = WidgetDateUtil.getYear();
        int month = WidgetDateUtil.getMonth();
        int weekday = CalendarManager.getDayOfWeek(context, year, month, 1);
        int monthDays = CalendarManager.getMonthDays(year, month);
        int mDay = 1;
        int count = 0;
        remoteViews.setTextViewText(R.id.date, WidgetDateUtil.getDateString(year, month));
        for (int i = 1; i <= 6; i++) {
            for (int j = 1; j <= 7; j++) {
                if (i == 1 && weekday > j) { //if the first day is not sunday, show draw but not write content.
                    remoteViews.setTextViewText(Cal_Id[count], "");
                    remoteViews.setTextViewText(NongLi_Id[count], "");
                } else if (monthDays < mDay) {
                    if (i == 6 && j == 1) { //if five line cun show current month,six line not need to draw.
                        remoteViews.setViewVisibility(R.id.widget_last_row, View.GONE);
                        return;
                    } else {
                        remoteViews.setViewVisibility(R.id.widget_last_row, View.VISIBLE);
                        remoteViews.setTextViewText(Cal_Id[count], "");
                        remoteViews.setTextViewText(NongLi_Id[count], "");
                    }
                } else {
                    instance.setDate(year, WidgetDateUtil.getMonth(), mDay);
                    int chineseDate = instance.getChineseDate();
                    int chineseMonth = instance.getChineseMonth();
                    int gregorianHoliday = CalendarUtil.getGregorianCalendarHoliday(year, month, mDay);
                    int tradionHoliday = CalendarUtil.getTraditionHoliday(chineseMonth, chineseDate);
                    if (gregorianHoliday != -1 && tradionHoliday != -1) {
                        remoteViews.setTextViewText(Cal_Id[count], context.getString(gregorianHoliday));
                        remoteViews.setTextViewText(NongLi_Id[count], context.getString(tradionHoliday));
                        remoteViews.setTextColor(Cal_Id[count], Color.GREEN);
                        remoteViews.setTextColor(NongLi_Id[count], Color.GREEN);
                    } else if (gregorianHoliday != -1) {
                        remoteViews.setTextViewText(Cal_Id[count], mDay + "");
                        remoteViews.setTextViewText(NongLi_Id[count], context.getString(gregorianHoliday));
                        remoteViews.setTextColor(Cal_Id[count], Color.GREEN);
                        remoteViews.setTextColor(NongLi_Id[count], Color.GREEN);
                    } else if (tradionHoliday != -1) {
                        remoteViews.setTextViewText(Cal_Id[count], mDay + "");
                        remoteViews.setTextViewText(NongLi_Id[count], context.getString(tradionHoliday));
                        remoteViews.setTextColor(Cal_Id[count], Color.GREEN);
                        remoteViews.setTextColor(NongLi_Id[count], Color.GREEN);
                    } else {
                        remoteViews.setTextColor(Cal_Id[count], Color.WHITE);
                        remoteViews.setTextColor(NongLi_Id[count], Color.WHITE);
                        remoteViews.setTextViewText(Cal_Id[count], mDay + "");
                        if (chineseDate == 1) {
                            remoteViews.setTextViewText(NongLi_Id[count],
                                            context.getString(CalendarMonthView.MONTH_OF_ALMANAC[chineseMonth - 1]));
                        } else {
                            remoteViews.setTextViewText(NongLi_Id[count],
                                            context.getString(CalendarMonthView.daysOfAlmanac[chineseDate - 1]));
                        }
                        if (isToday(year, month, mDay)) {
                            remoteViews.setTextColor(Cal_Id[count], Color.RED);
                            remoteViews.setTextColor(NongLi_Id[count], Color.RED);
                        }
                    }

                    mDay++;
                }
                count++;
            }
        }
    }

    private void updateMonth(boolean next) {
        int month = -1;
        int year = WidgetDateUtil.getYear();
        if (!next) {
            if (WidgetDateUtil.getMonth() != 1) {
                month = WidgetDateUtil.getMonth() - 1;
            } else {
                year -= 1;
                month = 12;
            }
        } else {
            if (WidgetDateUtil.getMonth() < 12) {
                month = WidgetDateUtil.getMonth() + 1;
            } else {
                year += 1;
                month = 1;
            }
        }
        if (month != -1) {
            WidgetDateUtil.setMonth(month);
            WidgetDateUtil.setYear(year);
        }
    }

    private static boolean isToday(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentDay = calendar.get(Calendar.DATE);
        if (year == currentYear && month == currentMonth && day == currentDay) {
            return true;
        }
        return false;
    }
}
