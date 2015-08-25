package com.cc.software.calendar.activity;

import hut.cc.software.calendar.R;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.cc.software.calendar.provider.HuangCalendar;
import com.cc.software.calendar.provider.HuangCalendar.HuangCalendarColumns;

public class PostActivity extends Activity {

    private static final int SUCCESS = 1, FAILURE = 0, Stop = -1;
    private TextView mTextView1;
    private int year = 2013, month = 1, day = 1;
    boolean isStop = false;

    public String[] strs = new String[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post);
        mTextView1 = (TextView) findViewById(R.id.tv);
        mTextView1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                isStop = false;
                postUrl(year, month, day);
            }
        });


//        getContentResolver().delete(HuangCalendar.CALENDAR_URI, null, null);

        findViewById(R.id.stop).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                isStop = true;
            }
        });

    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (isStop == true) {
                return;
            }
            if (msg.what == Stop) {
                return;
            }

            if (msg.what == SUCCESS) {
                updateDate();
            }
            new myThread(year, month, day).start();
        };
    };

    private void updateDate() {
        if (month == 2) {
            if ((day == 28 && !isGregorianLeapYear(year)) || day == 29) {
                day = 1;
                month += 1;
                return;
            } else {
                day += 1;
            }
        }
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 //月大
                || month == 10 || month == 12) {
            if (day == 31) {
                day = 1;
                if (month == 12) {
                    month = 1;
                    year += 1;
                } else {
                    month += 1;
                }
            } else {
                day += 1;
            }
        }
        if (month == 4 || month == 6 || month == 9 || month == 11) {
            if (day == 30) {
                day = 1;
                if (month == 12) {
                    month = 1;
                    year += 1;
                } else {
                    month += 1;
                }
            } else {
                day += 1;
            }
        }
    }

    private boolean isGregorianLeapYear(int year) {
        boolean isLeap = false;
        if (year % 4 == 0)
            isLeap = true;
        if (year % 100 == 0)
            isLeap = false;
        if (year % 400 == 0)
            isLeap = true;
        return isLeap;
    }

    private class myThread extends Thread {

        private int mYear, mMonth, mDay;

        public myThread(int year, int month, int day) {
            mYear = year;
            mMonth = month;
            mDay = day;
        }

        @Override
        public void run() {
            postUrl(mYear, mMonth, mDay);
        }
    }

    private void postUrl(int year, int month, int day) {
        /*建立HTTPost对象*/
        HttpPost httpRequest = new HttpPost(
                "http://www.nongli.com/item4/index.asp");
        /*
         * NameValuePair实现请求参数的封装
        */
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("nn", String.valueOf(year)));
        params.add(new BasicNameValuePair("yy", String.valueOf(month)));
        params.add(new BasicNameValuePair("rr", String.valueOf(day)));
        try {
            /* 添加请求参数到请求对象*/
            httpRequest.setEntity(new UrlEncodedFormEntity(params, "gbk"));
            /*发送请求并等待响应*/
            HttpResponse httpResponse = new DefaultHttpClient()
                    .execute(httpRequest);
            /*若状态码为200 ok*/
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                /*读返回数据*/
                String strResult = EntityUtils.toString(
                        httpResponse.getEntity(), "gbk");
                dealString(strResult);
                mHandler.sendEmptyMessage(SUCCESS);
            }else{
            }
        } catch (Exception e) {

            mHandler.sendEmptyMessage(FAILURE);
            e.printStackTrace();
        }
    }

    private void dealString(String strResult) {
        String startStr = "<td width=\"74%\" bgcolor=\"#FFD680\">";
        for (int i = 0; i < 10; i++) {
            int start = strResult.indexOf(startStr);
            strResult = strResult.substring(start);
            int end = strResult.indexOf("</td>");
            strs[i] = strResult.substring(startStr.length(), end);
            strResult = strResult.substring(end);
        }
        ContentValues values = new ContentValues();
        values.put(HuangCalendarColumns.TRADITION_CALENDAR, strs[0]);
        values.put(HuangCalendarColumns.GANZHI, strs[1]);
        values.put(HuangCalendarColumns.FITTING, strs[2]);
        values.put(HuangCalendarColumns.FORBID, strs[3]);
        values.put(HuangCalendarColumns.JISHENYIQU, strs[4]);
        values.put(HuangCalendarColumns.XIONGSHENYIJI, strs[5]);
        values.put(HuangCalendarColumns.TAISHENZHANFANG, strs[6]);
        values.put(HuangCalendarColumns.WUHANG, strs[7]);
        values.put(HuangCalendarColumns.CHONG, strs[8]);
        values.put(HuangCalendarColumns.PENGZUBAIJI, strs[9]);
        String date = year + "-" + month + "-" + day;
        values.put(HuangCalendarColumns.CALENDAR, date);
        getContentResolver().insert(HuangCalendar.CALENDAR_URI, values);
    }
}
