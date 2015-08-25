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

import com.cc.software.calendar.provider.Note.NoteColumns;

public class NoteProvider extends ContentProvider {

    private DataBaseHelper mOpenhHelper;

    private static final int NOTE = 1;
    private static final int NOTE_ID = 2;
    private static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        mUriMatcher.addURI(Note.AUTHOURITIES, "note", NOTE);
        mUriMatcher.addURI(Note.AUTHOURITIES, "note/#", NOTE_ID);
    }

    private class DataBaseHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "note.db";
        private static final int DATABASE_VERSION = 10;

        public DataBaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String sql = "create table " + Note.TABLE_NAME + "(" + NoteColumns._ID + " integer primary key,"
                            + NoteColumns.TITLE + " text," + NoteColumns.DESCRIPTION + " text," + NoteColumns.PATH
                            + " text," + NoteColumns.TYPE + " int," + NoteColumns.CREATE_DATE + " long,"
                            + NoteColumns.PATENT + " int," + NoteColumns.CALENDAR + " text);";
            db.execSQL(sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + Note.TABLE_NAME);
            onCreate(db);
        }

    }

    @Override
    public boolean onCreate() {
        mOpenhHelper = new DataBaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder sq = new SQLiteQueryBuilder();
        int match = mUriMatcher.match(uri);
        switch (match) {
        case NOTE:
            sq.setTables(Note.TABLE_NAME);
            break;
        case NOTE_ID:
            sq.setTables(Note.TABLE_NAME);
            sq.appendWhere("_id=");
            sq.appendWhere(uri.getPathSegments().get(1));
            break;
        default:
            break;
        }
        SQLiteDatabase db = mOpenhHelper.getReadableDatabase();
        Cursor cursor = db.query(Note.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
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
        if (mUriMatcher.match(uri) != NOTE) {
            throw new IllegalArgumentException("Cannot insert into URL: " + uri);
        }
        SQLiteDatabase db = mOpenhHelper.getWritableDatabase();
        long id = db.insert(Note.TABLE_NAME, null, values);
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
        case NOTE:
            count = db.delete(Note.TABLE_NAME, selection, selectionArgs);
            break;
        case NOTE_ID:
            String segment = uri.getPathSegments().get(1);
            if (TextUtils.isEmpty(selection)) {
                selection = "_id = " + segment;
            } else {
                selection = "_id = " + segment + " and (" + selection + ")";
            }
            db.delete(Note.TABLE_NAME, selection, selectionArgs);
            break;
        default:
            break;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenhHelper.getWritableDatabase();
        switch (mUriMatcher.match(uri)) {
        case NOTE:
            db.update(Note.TABLE_NAME, values, selection, selectionArgs);
            break;
        case NOTE_ID:
            String segment = uri.getPathSegments().get(1);
            if (TextUtils.isEmpty(selection)) {
                selection = "_id = " + segment;
            } else {
                selection = "_id = " + segment + " and (" + selection + ")";
            }
            db.update(Note.TABLE_NAME, values, selection, selectionArgs);
            break;
        default:
            break;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return 0;
    }

}
