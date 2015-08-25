package com.cc.software.calendar.activity;

import hut.cc.software.calendar.R;

import com.cc.software.calendar.provider.Note;
import com.cc.software.calendar.provider.Note.NoteColumns;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;

public class TestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post);
        //        getContentResolver().delete(Note.NOTE_URI, null, null);

        String folders[] = getResources().getStringArray(R.array.folder_name);
        int length = folders.length;
        for (int i = 0; i < length; i++) {
            ContentValues values = new ContentValues();
            values.put(NoteColumns.TITLE, folders[i]);
            values.put(NoteColumns.DESCRIPTION, "");
            values.put(NoteColumns.PATH, "");
            values.put(NoteColumns.TYPE, 0);
            values.put(NoteColumns.CREATE_DATE, System.currentTimeMillis());
            values.put(NoteColumns.PATENT, 0);
            getContentResolver().insert(Note.NOTE_URI, values);
        }

        Cursor cursor = getContentResolver().query(Note.NOTE_URI,
                Note.NOTE_QUERY_PROJECTION, null, null, null);
        while (cursor.moveToNext()) {
            System.out.println(cursor.getString(NoteColumns.COLUME_INDEX_TITLE)
                    + ":" + cursor.getInt(NoteColumns.COLUME_INDEX_ID));
        }
        System.out.println(cursor.getCount());
        System.out.println("zhixing");
    }

}
