package com.cc.software.calendar.bean;

public class TraditonInfo {
    private String traditionAboutYear;
    private String traditionDate;
    private String dayInWeek;

    public static TraditonInfo getTraditonInfo(String traditionString,String ganzhi){
        TraditonInfo info = new TraditonInfo();

        return info;
    }

    public String getTraditionAboutYear() {
        return traditionAboutYear;
    }

    public void setTraditionAboutYear(String traditionAboutYear) {
        this.traditionAboutYear = traditionAboutYear;
    }

    public String getTraditionDate() {
        return traditionDate;
    }

    public void setTraditionDate(String traditionDate) {
        this.traditionDate = traditionDate;
    }

    public String getDayInWeek() {
        return dayInWeek;
    }

    public void setDayInWeek(String dayInWeek) {
        this.dayInWeek = dayInWeek;
    }
}
