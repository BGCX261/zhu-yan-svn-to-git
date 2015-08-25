package com.cc.software.calendar.activity;

import hut.cc.software.calendar.R;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TextView;

import com.cc.software.calendar.util.CalendarManager;
import com.cc.software.calendar.util.CommonUtil;

public class CalendarInfoActivity extends Activity {

    private TableLayout mRootLayout;
    private LayoutParams mParams;
    private TextView mEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.huang_calendar);
        mRootLayout = (TableLayout) findViewById(R.id.root);
        
        findViewById(R.id.mainpane).setBackgroundDrawable(CommonUtil.getBackGround(this));
        mParams = new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT);
        initContent();
    }

    private void initContent() {
        mEmpty = (TextView) findViewById(R.id.empty);
        String dateString = getIntent().getStringExtra("date");
        ((TextView) findViewById(R.id.current_date_info)).setText(dateString
                + " " + getString(R.string.huang_calendar_info));
        ArrayList<String> info = CalendarManager.getCalendarInfoByDateString(
                this, dateString);
        String calentTitleArray[] = getResources().getStringArray(
                R.array.calendar_title);

        if (info != null) {
            mEmpty.setVisibility(View.GONE);
            for (int i = 0, count = calentTitleArray.length; i < count; i++) {
                View item = LayoutInflater.from(this).inflate(
                        R.layout.huang_calendar_item, null);
                ((TextView) item.findViewById(R.id.tv_title))
                        .setText(calentTitleArray[i]);
                ((TextView) item.findViewById(R.id.tv_content)).setText(info
                        .get(i));
                mRootLayout.addView(item, mParams);

            }
        } else {
            mEmpty.setVisibility(View.VISIBLE);
        }
    }
}
