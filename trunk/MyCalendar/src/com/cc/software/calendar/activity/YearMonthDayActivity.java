package com.cc.software.calendar.activity;

import hut.cc.software.calendar.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.cc.software.calendar.comp.Constants;
import com.cc.software.calendar.util.CalendarManager;
import com.cc.software.calendar.util.DateUtil;
import com.cc.software.calendar.wheelView.NumericWheelAdapter;
import com.cc.software.calendar.wheelView.OnWheelChangedListener;
import com.cc.software.calendar.wheelView.OnWheelScrollListener;
import com.cc.software.calendar.wheelView.WheelView;

public class YearMonthDayActivity extends Activity implements OnClickListener, OnWheelChangedListener,
                OnWheelScrollListener {

    private WheelView monthWheelView, yearWheelView, dayWheelView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picker_date_ymd);
        yearWheelView = (WheelView) findViewById(R.id.year_wheel);
        yearWheelView.setAdapter(new NumericWheelAdapter(1900, 2100));
        yearWheelView.setCyclic(true);
        //yearWheelView.setLabel("年");
        yearWheelView.addScrollingListener(this);

        monthWheelView = (WheelView) findViewById(R.id.month_wheel);
        monthWheelView.setAdapter(new NumericWheelAdapter(1, 12));
        monthWheelView.setCyclic(true);
        //monthWheelView.setLabel("月");
        monthWheelView.addScrollingListener(this);

        int year = DateUtil.getYear();
        int month = DateUtil.getMonth();
        int monthDays = CalendarManager.getMonthDays(year, month);
        dayWheelView = (WheelView) findViewById(R.id.day_wheel);
        dayWheelView.setAdapter(new NumericWheelAdapter(1, monthDays));
        dayWheelView.setCyclic(true);
        //dayWheelView.setLabel("日");

        int day = DateUtil.getDay();
        yearWheelView.setCurrentItem(year - 1900);
        monthWheelView.setCurrentItem(month - 1);
        dayWheelView.setCurrentItem(day - 1);

        findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.button1:
            int year = Integer.parseInt(yearWheelView.getAdapter().getItem(yearWheelView.getCurrentItem()));
            int month = Integer.parseInt(monthWheelView.getAdapter().getItem(monthWheelView.getCurrentItem()));
            int day = Integer.parseInt(dayWheelView.getAdapter().getItem(dayWheelView.getCurrentItem()));
            Intent data = new Intent();
            data.putExtra("year", year);
            data.putExtra("month", month);
            data.putExtra("day", day);
            setResult(Constants.RESUSTCODE_YEAR_MONTH, data);
            finish();
            break;
        case R.id.button2:
            finish();
            break;

        default:
            break;
        }
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
    }

    @Override
    public void onScrollingStarted(WheelView wheel) {

    }

    @Override
    public void onScrollingFinished(WheelView wheel) {
        String str = wheel.getAdapter().getItem(wheel.getCurrentItem());
        int yearOrMonth = Integer.parseInt(str);
        int monthDays = -1;
        if (yearOrMonth > 12) {
            String monthStr = monthWheelView.getAdapter().getItem(monthWheelView.getCurrentItem());
            int month = Integer.parseInt(monthStr);
            monthDays = CalendarManager.getMonthDays(yearOrMonth, month);
        } else {
            String yearStr = yearWheelView.getAdapter().getItem(monthWheelView.getCurrentItem());
            int year = Integer.parseInt(yearStr);
            monthDays = CalendarManager.getMonthDays(year, yearOrMonth);
        }
        int day = Integer.parseInt(dayWheelView.getAdapter().getItem(dayWheelView.getCurrentItem()));
        dayWheelView.setAdapter(new NumericWheelAdapter(1, monthDays));
        dayWheelView.setCurrentItem(day - 1);
    }
}
