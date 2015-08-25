package com.cc.software.calendar.clock;

import java.util.Calendar;

import android.text.format.DateFormat;

public class DigitalClockService {

    private static final DigitalClockService digitalClockService = new DigitalClockService();
    private final static String M12 = "hh:mm:ss";
    private DigitalClockData mDigitalClockData = new DigitalClockData();
    private Calendar mCalendar;

    private DigitalClockService() {
        mCalendar = Calendar.getInstance();
    }

    public static DigitalClockService getInstance() {
        return digitalClockService;
    }

    public DigitalClockData getDigitalClockData() {
        updateTime(mDigitalClockData);
        return mDigitalClockData;
    }

    // private void setDateFormat() {
    // mFormat = Alarms.get24HourMode(getContext()) ? M24 : M12;
    // mAmPm.setShowAmPm(mFormat == M12);
    // }

    public void updateTime(DigitalClockData digitalClockData) {
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        CharSequence newTime = DateFormat.format(M12, mCalendar);
        // System.out.println("newTime---->" + newTime);
        boolean isAm = mCalendar.get(Calendar.AM_PM) == 0;
        int month = mCalendar.get(Calendar.MONTH) + 1;
        int dayOfMonth = mCalendar.get(Calendar.DAY_OF_MONTH);
        int dayOfWeek = mCalendar.get(Calendar.DAY_OF_WEEK);

        digitalClockData.setAM(isAm);
        digitalClockData.setMonth(month);
        digitalClockData.setDayOfMonth(dayOfMonth);
        digitalClockData.setWeek(dayOfWeek);
        timeUpdate(digitalClockData, newTime);
    }

    public void timeUpdate(DigitalClockData digitalClockData, CharSequence timeCharSequence) {
        String timeStr = (String) timeCharSequence;
        int hour0 = Integer.parseInt(timeStr.substring(0, 1)); // 1 -- 30
        int hour1 = Integer.parseInt(timeStr.substring(1, 2));
        int min0 = Integer.parseInt(timeStr.substring(3, 4));
        int min1 = Integer.parseInt(timeStr.substring(4, 5));
        digitalClockData.setHour0(hour0);
        digitalClockData.setHour1(hour1);
        digitalClockData.setMin0(min0);
        digitalClockData.setMin1(min1);
        // char c =(char)hour0;
    }
}
