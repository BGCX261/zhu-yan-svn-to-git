package com.cc.software.calendar.util;

import hut.cc.software.calendar.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;

import com.cc.software.calendar.bean.CalendarInfo;
import com.cc.software.calendar.provider.HuangCalendar;
import com.cc.software.calendar.provider.HuangCalendar.HuangCalendarColumns;

public class CalendarManager {

    private static final String CALENDAR_DATA_DIR = "multi_fuction_calendar";

    private static final String FILE_NAME = "calendar.db";

    private static final String TABLE_NAME = "calendar";

    public static final boolean isLeapYear(int year) {
        boolean isLeap = false;
        if (year % 4 == 0)
            isLeap = true;
        if (year % 100 == 0)
            isLeap = false;
        if (year % 400 == 0)
            isLeap = true;
        return isLeap;
    }

    public static final int getMonthDays(int year, int month) {
        while (month <= 0) {
            month += 12;
        }
        if (month > 12) {
            month %= 12;
        }
        int day = 0;
        if (month == 2) {
            if (isLeapYear(year)) {
                day = 29;
            } else {
                day = 28;
            }
        } else if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
            day = 31;
        } else {
            day = 30;
        }
        return day;
    }

    public static final int getDayOfWeek(Context context, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /*    public static final int getChineseLastDay(Context context) {
            int lastDay = 29;
            Cursor cursor = context.getContentResolver().query(
                    HuangCalendar.CALENDAR_URI,
                    HuangCalendar.ALARM_QUERY_PROJECTION, "tracalendar like ", null, null);
        }*/

    public static final CalendarInfo getCalendarInfoByDate(Context context, int year, int month, int day) {
        SQLiteDatabase database = getCalendarDatabase(context);
        String where = HuangCalendarColumns.CALENDAR + " = ?";
        String selection = DateUtil.getDateSimpleString(year, month, day);
        Cursor cursor = database.query(TABLE_NAME, HuangCalendar.CALENDAR_QUERY_PROJECTION, where,
                        new String[] { selection }, null, null, null);
        if (cursor == null) {
            return null;
        }
        cursor.moveToFirst();
        CalendarInfo info = null;
        if (cursor.moveToFirst()) {
            info = new CalendarInfo();
            info.setChong(cursor.getString(HuangCalendarColumns.COLUME_INDEX_CHONG));
            info.setFitting(cursor.getString(HuangCalendarColumns.COLUME_INDEX_FITTING));
            info.setForbid(cursor.getString(HuangCalendarColumns.COLUME_INDEX_FORBID));
            info.setGanZhi(cursor.getString(HuangCalendarColumns.COLUME_INDEX_GANZHI));
            info.setJiShenYiQu(cursor.getString(HuangCalendarColumns.COLUME_INDEX_JISHENYIQU));
            info.setPengZuBaiJi(cursor.getString(HuangCalendarColumns.COLUME_INDEX_PENGZUBAIJI));
            info.setTaiShenZhanFang(cursor.getString(HuangCalendarColumns.COLUME_INDEX_TAISHENZHANFANG));
            info.setTraCalendar(cursor.getString(HuangCalendarColumns.COLUME_INDEX_TRADITION_CALENDAR));
            info.setWuHang(cursor.getString(HuangCalendarColumns.COLUME_INDEX_WUHANG));
            info.setXiongShenYiJi(cursor.getString(HuangCalendarColumns.COLUME_INDEX_XIONGSHENYIJI));
        }
        database.close();
        cursor.close();
        return info;
    }

    public static final ArrayList<String> getCalendarInfoByDateString(Context context, String dateString) {
        SQLiteDatabase database = getCalendarDatabase(context);
        String where = HuangCalendarColumns.CALENDAR + " = ?";
        Cursor cursor = database.query(TABLE_NAME, HuangCalendar.CALENDAR_QUERY_PROJECTION, where,
                        new String[] { dateString }, null, null, null);
        if (cursor == null) {
            return null;
        }
        cursor.moveToFirst();
        ArrayList<String> info = null;
        if (cursor.moveToFirst()) {
            info = new ArrayList<String>();
            info.add(cursor.getString(HuangCalendarColumns.COLUME_INDEX_TRADITION_CALENDAR));
            info.add(cursor.getString(HuangCalendarColumns.COLUME_INDEX_GANZHI));

            info.add(cursor.getString(HuangCalendarColumns.COLUME_INDEX_FITTING));
            info.add(cursor.getString(HuangCalendarColumns.COLUME_INDEX_FORBID));

            info.add(cursor.getString(HuangCalendarColumns.COLUME_INDEX_JISHENYIQU));
            info.add(cursor.getString(HuangCalendarColumns.COLUME_INDEX_XIONGSHENYIJI));
            info.add(cursor.getString(HuangCalendarColumns.COLUME_INDEX_TAISHENZHANFANG));
            info.add(cursor.getString(HuangCalendarColumns.COLUME_INDEX_WUHANG));
            info.add(cursor.getString(HuangCalendarColumns.COLUME_INDEX_CHONG));
            info.add(cursor.getString(HuangCalendarColumns.COLUME_INDEX_PENGZUBAIJI));
        }
        database.close();
        cursor.close();
        return info;
    }

    public static final File getCalendarDataFile(Context context, String fileName) {
        File dir = context.getDir(CALENDAR_DATA_DIR, Activity.MODE_PRIVATE);
        return new File(dir, fileName);
    }

    public static SQLiteDatabase getCalendarDatabase(Context mContext) {
        String string = getCalendarDataFile(mContext, FILE_NAME).getPath();// "/mnt/sdcard/calendar.db"
        return SQLiteDatabase.openOrCreateDatabase(string, null);
    }

    public static final void copyDataBaseIntoStorage(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean update = sharedPreferences.getBoolean("app_update", true);
        if (update) {
            try {
                File dir = getCalendarDataFile(context, FILE_NAME);
                InputStream is = context.getResources().openRawResource(R.raw.calendar);
                FileOutputStream fos = new FileOutputStream(dir);
                byte[] buffer = new byte[8192];
                int count = 0;

                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }

                fos.flush();
                fos.close();
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            sharedPreferences.edit().putBoolean("app_update", false).commit();
        }
    }
}
