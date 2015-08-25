package com.cc.software.calendar.bean;

import com.cc.software.calendar.provider.Note.NoteColumns;

import android.database.Cursor;

public class NoteInfo {
    private int id;
    private String title;
    private String content;
    private String path;
    private int type;
    private long date;
    private int parent;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public static final NoteInfo getNoteInfoFromAdapterCursot(Cursor cursor) {
        if (null == cursor || cursor.getCount() == 0)
            return null;
        NoteInfo info = new NoteInfo();

        info.setId(cursor.getInt(NoteColumns.COLUME_INDEX_ID));
        info.setTitle(cursor.getString(NoteColumns.COLUME_INDEX_TITLE));
        info.setContent(cursor.getString(NoteColumns.COLUME_INDEX_CONTENT));
        info.setPath(cursor.getString(NoteColumns.COLUME_INDEX_PATH));
        info.setType(cursor.getInt(NoteColumns.COLUME_INDEX_TYPE));
        info.setDate(cursor.getLong(NoteColumns.COLUME_INDEX_CREATE_DATE));
        info.setParent(cursor.getInt(NoteColumns.COLUME_INDEX_PATENT));

        return info;
    }
}
