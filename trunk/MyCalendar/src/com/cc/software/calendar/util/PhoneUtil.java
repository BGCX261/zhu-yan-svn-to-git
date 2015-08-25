package com.cc.software.calendar.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.PhoneLookup;
import android.text.format.DateUtils;

import com.cc.software.calendar.comp.Constants;

public class PhoneUtil {
    public final static String getContactNameByPhoneNumber(String mNumber, Context context) {
        Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(mNumber));
        Cursor cursor = context.getContentResolver().query(uri, new String[] { PhoneLookup.DISPLAY_NAME }, null, null,
                        null);
        if (cursor == null) {
            return null;
        }
        if (cursor.moveToFirst()) {
            cursor.moveToPosition(0);
            return cursor.getString(0);
        }
        return null;
    }

    public final static CharSequence formatTime(long time, int formatType, Context context) {
        switch (formatType) {
        case Constants.DATE_FORMAT_1:
            return DateUtils.getRelativeTimeSpanString(time, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS,
                            DateUtils.FORMAT_ABBREV_RELATIVE);
        case Constants.DATE_FORMAT_2:
            return DateUtils.formatDateTime(context, time, DateUtils.FORMAT_24HOUR | DateUtils.FORMAT_SHOW_DATE
                            | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_NUMERIC_DATE);
        case Constants.DATE_FORMAT_3:
            return DateUtils.formatDateTime(context, time, DateUtils.FORMAT_24HOUR | DateUtils.FORMAT_SHOW_DATE
                            | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_ABBREV_ALL);
        case Constants.DATE_FORMAT_4:
            return DateUtils.formatDateTime(context, time, DateUtils.FORMAT_12HOUR | DateUtils.FORMAT_SHOW_DATE
                            | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_NUMERIC_DATE);
        case Constants.DATE_FORMAT_5:
            return DateUtils.formatDateTime(context, time, DateUtils.FORMAT_12HOUR | DateUtils.FORMAT_SHOW_DATE
                            | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_ABBREV_ALL);
        }
        return null;
    }
}
