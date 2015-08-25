package com.cc.software.calendar.activity;

import hut.cc.software.calendar.R;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class AlarmSettingActivity extends Activity {

    private static final String HOURS[] = new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09",
                    "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" };

    private static final String MINUTES[] = new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09",
                    "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25",
                    "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41",
                    "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57",
                    "58", "59" };

    private ListView mHourList, mMinuteList;
    int listHeight = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clock_setting);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.body_bg);
        BitmapDrawable logo = new BitmapDrawable(bitmap);
        logo.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
        logo.setDither(true);
        findViewById(R.id.alarm_setting_root).setBackgroundDrawable(logo);
        mHourList = (ListView) findViewById(R.id.hour_list);
        mMinuteList = (ListView) findViewById(R.id.minute_list);
        listHeight = getResources().getDrawable(R.drawable.time_left_bg).getIntrinsicHeight();
        mHourList.getLayoutParams().height = listHeight;
        mMinuteList.getLayoutParams().height = listHeight;

        mHourList.setAdapter(new HourAdapter());
        mMinuteList.setAdapter(new MinuteAdater());
        mHourList.setSelection(2000);
        mMinuteList.setSelection(2000);

        mHourList.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == 0) {
                    mHourList.setSelection(view.getFirstVisiblePosition());
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        
        mMinuteList.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == 0) {
                    mMinuteList.setSelection(view.getFirstVisiblePosition());
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    private class HourAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {

            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(AlarmSettingActivity.this).inflate(R.layout.clock_list_item, null);
            }
            TextView time = (TextView) convertView.findViewById(R.id.time);
            time.setHeight(listHeight / 3);
            time.setText(HOURS[position % 24]);
            return convertView;
        }
    }

    private class MinuteAdater extends BaseAdapter {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(AlarmSettingActivity.this).inflate(R.layout.clock_list_item, null);
            }

            TextView time = (TextView) convertView.findViewById(R.id.time);
            time.setHeight(listHeight / 3);
            time.setText(MINUTES[position % 60]);
            return convertView;
        }

    }
}
