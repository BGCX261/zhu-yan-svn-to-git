package com.cc.software.calendar.bean;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;

import com.cc.software.calendar.comp.Constants;
import com.cc.software.calendar.util.PhoneUtil;

public class SmsInfo {

    public static final String SMS_PROJECTION[] = { "thread_id", "body", "address", "date" };
    public static final int COLUMN_INDEX_THREAD_ID = 0, COLUMN_INDEX_BODY = 1, COLUMN_INDEX_ADDRESS = 2,
                    COLUMN_INDEX_DATE = 3;

    private String thread_id;
    private String date;
    private String body;
    private String address;
    private String contact_name;

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getThread_id() {
        return thread_id;
    }

    public void setThread_id(String thread_id) {
        this.thread_id = thread_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static String[] getSmsProjection() {
        return SMS_PROJECTION;
    }

    public static int getColumnIndexThreadId() {
        return COLUMN_INDEX_THREAD_ID;
    }

    public static int getColumnIndexBody() {
        return COLUMN_INDEX_BODY;
    }

    public static int getColumnIndexAddress() {
        return COLUMN_INDEX_ADDRESS;
    }

    public static int getColumnIndexDate() {
        return COLUMN_INDEX_DATE;
    }

    public static final ArrayList<SmsInfo> getSmsInfos(Context context, Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        ArrayList<SmsInfo> smsInfos = new ArrayList<SmsInfo>();
        while (cursor.moveToNext()) {
            SmsInfo info = new SmsInfo();
            String address = cursor.getString(COLUMN_INDEX_ADDRESS);
            info.setAddress(address);
            String name = PhoneUtil.getContactNameByPhoneNumber(address, context);
            if (name == null)
                name = address;
            info.setContact_name(name);
            info.setBody(cursor.getString(COLUMN_INDEX_BODY));
            info.setDate(PhoneUtil.formatTime(Long.valueOf(cursor.getString(COLUMN_INDEX_DATE)),
                            Constants.DATE_FORMAT_2, context).toString());
            info.setThread_id(cursor.getString(COLUMN_INDEX_THREAD_ID));
            smsInfos.add(info);
        }
        return smsInfos;
    }

    public static final SmsInfo getSmsInfoByCursorAdapter(Context context, Cursor cursor) {
        SmsInfo info = new SmsInfo();
        String address = cursor.getString(COLUMN_INDEX_ADDRESS);
        info.setAddress(address);
        String name = PhoneUtil.getContactNameByPhoneNumber(address, context);
        if (name == null)
            name = address;
        info.setContact_name(name);
        info.setBody(cursor.getString(COLUMN_INDEX_BODY));
        info.setDate(PhoneUtil.formatTime(Long.valueOf(cursor.getString(COLUMN_INDEX_DATE)), Constants.DATE_FORMAT_2,
                        context).toString());
        info.setThread_id(cursor.getString(COLUMN_INDEX_THREAD_ID));
        return info;
    }
}
