package com.cc.software.calendar.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.cc.software.calendar.provider.HuangCalendar.HuangCalendarColumns;

public class CalendarHuangProvider extends ContentProvider {

    private DataBaseHelper mOpenhHelper;

    private static final int CALENDAR = 1;
    private static final int CALENDAR_ID = 2;
    private static final UriMatcher mUriMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);

    static {
        mUriMatcher.addURI(HuangCalendar.AUTHOURITIES, "calendar", CALENDAR);
        mUriMatcher.addURI(HuangCalendar.AUTHOURITIES, "calendar/#", CALENDAR_ID);
    }

    private class DataBaseHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "/mnt/sdcard/calendar.db";
        private static final int DATABASE_VERSION = 10;

        public DataBaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String sql = "create table " + HuangCalendar.TABLE_NAME + "("
            + HuangCalendarColumns._ID + " integer primary key,"
            + HuangCalendarColumns.TRADITION_CALENDAR + " text,"
            + HuangCalendarColumns.GANZHI + " text,"
            + HuangCalendarColumns.FITTING + " text,"
            + HuangCalendarColumns.FORBID + " text,"
            + HuangCalendarColumns.JISHENYIQU + " text,"
            + HuangCalendarColumns.XIONGSHENYIJI + " text,"
            + HuangCalendarColumns.TAISHENZHANFANG + " text,"
            + HuangCalendarColumns.WUHANG + " text,"
            + HuangCalendarColumns.CHONG + " text,"
            + HuangCalendarColumns.PENGZUBAIJI + " text,"
            + HuangCalendarColumns.CALENDAR + " text);";
            db.execSQL(sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + HuangCalendar.TABLE_NAME);
            onCreate(db);
        }

    }

    @Override
    public boolean onCreate() {
        mOpenhHelper = new DataBaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder sq = new SQLiteQueryBuilder();
        int match = mUriMatcher.match(uri);
        switch (match) {
        case CALENDAR:
            sq.setTables(HuangCalendar.TABLE_NAME);
            break;
        case CALENDAR_ID:
            sq.setTables(HuangCalendar.TABLE_NAME);
            sq.appendWhere("_id=");
            sq.appendWhere(uri.getPathSegments().get(1));
            break;
        default:
            break;
        }
        SQLiteDatabase db = mOpenhHelper.getReadableDatabase();
        Cursor cursor = db.query(HuangCalendar.TABLE_NAME, projection,
                selection, selectionArgs, null, null, sortOrder);
        if (cursor != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (mUriMatcher.match(uri) != CALENDAR) {
            throw new IllegalArgumentException("Cannot insert into URL: " + uri);
        }
        SQLiteDatabase db = mOpenhHelper.getWritableDatabase();
        long id = db.insert(HuangCalendar.TABLE_NAME, null, values);
        if (id < 0) {
            throw new IllegalArgumentException("insert into failure: " + uri);
        }
        Uri newUri = ContentUris.withAppendedId(uri, id);
        getContext().getContentResolver().notifyChange(newUri, null);
        return newUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenhHelper.getWritableDatabase();
        int count = 0;
        switch (mUriMatcher.match(uri)) {
        case CALENDAR:
            count = db.delete(HuangCalendar.TABLE_NAME, selection,
                    selectionArgs);
            break;
        case CALENDAR_ID:
            String segment = uri.getPathSegments().get(1);
            if (TextUtils.isEmpty(selection)) {
                selection = "_id = " + segment;
            } else {
                selection = "_id = " + segment + " and (" + selection + ")";
            }
            db.delete(HuangCalendar.TABLE_NAME, selection, selectionArgs);
            break;
        default:
            break;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        SQLiteDatabase db = mOpenhHelper.getWritableDatabase();
        switch (mUriMatcher.match(uri)) {
        case CALENDAR:
            db.update(HuangCalendar.TABLE_NAME, values, selection,
                    selectionArgs);
            break;
        case CALENDAR_ID:
            String segment = uri.getPathSegments().get(1);
            if (TextUtils.isEmpty(selection)) {
                selection = "_id = " + segment;
            } else {
                selection = "_id = " + segment + " and (" + selection + ")";
            }
            db.update(HuangCalendar.TABLE_NAME, values, selection,
                    selectionArgs);
            break;
        default:
            break;
        }
        return 0;
    }

}
