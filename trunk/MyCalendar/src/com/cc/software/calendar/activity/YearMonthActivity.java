package com.cc.software.calendar.activity;

import hut.cc.software.calendar.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.cc.software.calendar.comp.Constants;
import com.cc.software.calendar.util.DateUtil;
import com.cc.software.calendar.wheelView.NumericWheelAdapter;
import com.cc.software.calendar.wheelView.WheelView;

public class YearMonthActivity extends Activity implements OnClickListener {

    private WheelView yearWheelView, monthWheelView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picker_date_ym);
        yearWheelView = (WheelView) findViewById(R.id.year_wheel);
        yearWheelView.setAdapter(new NumericWheelAdapter(1900, 2100));
        //yearWheelView.setLabel("年");
        yearWheelView.setCyclic(true);

        monthWheelView = (WheelView) findViewById(R.id.month_wheel);
        monthWheelView.setAdapter(new NumericWheelAdapter(1, 12));
        monthWheelView.setCyclic(true);
        //monthWheelView.setLabel("月");

        int year = DateUtil.getYear();
        int month = DateUtil.getMonth();
        yearWheelView.setCurrentItem(year - 1900);
        monthWheelView.setCurrentItem(month - 1);

        findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.button1:
            int year = Integer.parseInt(yearWheelView.getAdapter().getItem(yearWheelView.getCurrentItem()));
            int month = Integer.parseInt(monthWheelView.getAdapter().getItem(monthWheelView.getCurrentItem()));
            Intent data = new Intent();
            data.putExtra("year", year);
            data.putExtra("month", month);
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
}
