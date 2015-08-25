package com.cc.software.calendar.util;

import java.util.Calendar;

public class DateUtil {

    //Notice: month is the fact,it already add 1 to fix us habit.

    private static int mYear, mMonth, mDay;

    public static void initCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH) + 1;
        mDay = calendar.get(Calendar.DATE);
    }

    public static String getDateString(int year, int month) {
        if (month < 10) {
            return year + "-0" + month;
        } else {
            return year + "-" + month;
        }
    }

    public static String getDateString(int year, int month, int day) {
        StringBuilder builder = new StringBuilder(year + "-");
        if (month < 10) {
            builder.append("0" + month + "-");
        } else {
            builder.append(month + "-");
        }
        if (day < 10) {
            builder.append("0" + day);
        } else {
            builder.append(day);
        }
        return builder.toString();
    }

    public static String getDateSimpleString(int year, int month, int day) {
        return year + "-" + month + "-" + day;
    }

    public static String getDateSimpleString() {
        if(mYear ==0){
            initCurrentTime();
        }
        return mYear + "-" + mMonth + "-" + mDay;
    }

    public static int getYear() {
        if (mYear == 0) {
            initCurrentTime();
        }
        return mYear;
    }

    public static int getMonth() {
        if (mMonth == 0) {
            initCurrentTime();
        }
        return mMonth;
    }

    public static int getDay() {
        if (mDay == 0) {
            initCurrentTime();
        }
        return mDay;
    }

    public static void setYear(int year) {
        mYear = year;
    }

    public static void setMonth(int month) {
        mMonth = month;
    }

    public static void setDay(int day) {
        mDay = day;
    }
}
