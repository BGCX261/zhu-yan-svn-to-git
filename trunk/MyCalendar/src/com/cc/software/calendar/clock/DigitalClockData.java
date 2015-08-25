package com.cc.software.calendar.clock;

public class DigitalClockData {
    
    private boolean isAM;
    private int month;
    private int dayOfMonth;
    private int week;

    private int hour0;

    public int getHour0() {
        return hour0;
    }

    public void setHour0(int hour0) {
        this.hour0 = hour0;
    }

    public int getHour1() {
        return hour1;
    }

    public void setHour1(int hour1) {
        this.hour1 = hour1;
    }

    public int getMin0() {
        return min0;
    }

    public void setMin0(int min0) {
        this.min0 = min0;
    }

    public int getMin1() {
        return min1;
    }

    public void setMin1(int min1) {
        this.min1 = min1;
    }

    private int hour1;
    private int min0;
    private int min1;

    public boolean isAM() {
        return isAM;
    }

    public void setAM(boolean isAM) {
        this.isAM = isAM;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

}
