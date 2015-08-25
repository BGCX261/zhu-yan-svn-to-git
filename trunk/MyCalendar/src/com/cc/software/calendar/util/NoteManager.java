package com.cc.software.calendar.util;

import hut.cc.software.calendar.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.cc.software.calendar.activity.NotePadActivity;
import com.cc.software.calendar.bean.NoteInfo;
import com.cc.software.calendar.comp.Constants;
import com.cc.software.calendar.provider.Note;
import com.cc.software.calendar.provider.Note.NoteColumns;

public class NoteManager {

    private static final int KILO_BYTE = 1024;
    private static final int MILLION_BYTE = 1024 * 1024;

    public static final int TYPE_NOTE = 1, TYPE_IMAGE = 2, TYPE_RECORD = 3, TYPE_VEDIO = 4;

    public static final void initNoteDb(Context context) {
        String folders[] = context.getResources().getStringArray(R.array.folder_name);
        int length = folders.length;
        for (int i = 0; i < length; i++) {
            ContentValues values = new ContentValues();
            values.put(NoteColumns._ID, i + 1);
            values.put(NoteColumns.TITLE, folders[i]);
            values.put(NoteColumns.DESCRIPTION, "");
            values.put(NoteColumns.PATH, "");
            values.put(NoteColumns.TYPE, 0);
            values.put(NoteColumns.CREATE_DATE, System.currentTimeMillis());
            values.put(NoteColumns.PATENT, 0);
            context.getContentResolver().insert(Note.NOTE_URI, values);
        }
    }

    public static final boolean addNote(Context context, String title, String content, String path, int type,
                    String date) {
        ContentValues values = new ContentValues();
        values.put(NoteColumns.TITLE, title);
        values.put(NoteColumns.DESCRIPTION, content);
        values.put(NoteColumns.PATH, path);
        values.put(NoteColumns.TYPE, type);
        values.put(NoteColumns.PATENT, type);
        values.put(NoteColumns.CREATE_DATE, System.currentTimeMillis());
        values.put(NoteColumns.CALENDAR, date);
        context.getContentResolver().insert(Note.NOTE_URI, values);
        return true;
    }

    public static final boolean deleteNote(Context context, int id) {
        context.getContentResolver().delete(Note.NOTE_URI, NoteColumns._ID + "=" + id, null);
        return false;
    }

    public static final boolean updateNote(Context context, int id, String title, String content) {
        ContentValues values = new ContentValues();
        values.put(NoteColumns.TITLE, title);
        values.put(NoteColumns.DESCRIPTION, content);
        context.getContentResolver().update(Note.NOTE_URI, values, NoteColumns._ID + "=" + id, null);
        return false;
    }

    public static final Cursor getNoteByParent(Context context, int parent) {
        return context.getContentResolver().query(Note.NOTE_URI, Note.NOTE_QUERY_PROJECTION, "parent = " + parent,
                        null, "create_date");
    }

    public static final int getNotePicIdByType(int type) {
        switch (type) {
        case TYPE_IMAGE:
            return R.drawable.icon_file_jpg;
        case TYPE_NOTE:
            return R.drawable.icon_file_txt;
        case TYPE_RECORD:
            return R.drawable.icon_file_mp3;
        case TYPE_VEDIO:
            return R.drawable.icon_file_video;

        default:
            break;
        }
        return -1;
    }

    public static final ArrayList<NoteInfo> getNoteInfosByDate(Context context, String date) {
        ArrayList<NoteInfo> noteInfos;
        Cursor cursor = context.getContentResolver().query(Note.NOTE_URI, Note.NOTE_QUERY_PROJECTION,
                        NoteColumns.CALENDAR + "=?", new String[] { date }, null);
        if (cursor == null || cursor.getCount() == 0) {
            return null;
        } else {
            noteInfos = new ArrayList<NoteInfo>();
            while (cursor.moveToNext()) {
                NoteInfo info = new NoteInfo();
                info.setTitle(cursor.getString(NoteColumns.COLUME_INDEX_TITLE));
                info.setType(cursor.getInt(NoteColumns.COLUME_INDEX_TYPE));
                info.setContent(cursor.getString(NoteColumns.COLUME_INDEX_CONTENT));
                info.setId(cursor.getInt(NoteColumns.COLUME_INDEX_ID));
                info.setPath(cursor.getString(NoteColumns.COLUME_INDEX_PATH));
                noteInfos.add(info);
            }
        }
        return noteInfos;
    }

    public static final String getNoteCountStringByParent(Context context, int parent) {
        Cursor cursor = context.getContentResolver().query(Note.NOTE_URI, Note.NOTE_QUERY_PROJECTION,
                        "parent = " + parent, null, " create_date DESC");
        if (cursor == null) {
            return context.getString(R.string.file_count) + 0;
        }
        cursor.close();
        return context.getString(R.string.file_count) + cursor.getCount();
    }

    public static final String getNoteFileSize(Context context, String path) {
        File file = new File(path);
        long filesize = 0;
        if (file != null) {
            filesize = file.length();
        }
        String unit = null;
        if (filesize >= MILLION_BYTE) {
            unit = (int) Math.ceil(filesize / MILLION_BYTE) + context.getString(R.string.million_byte);
        } else if (filesize >= KILO_BYTE) {
            unit = (int) Math.ceil(filesize / KILO_BYTE) + context.getString(R.string.kilo_byte);
        } else {
            unit = filesize + context.getString(R.string.byte_unit);
        }
        return context.getString(R.string.file_size) + unit;
    }

    public static final String getLastEditTimeString(Context context, long time) {
        SimpleDateFormat format = new SimpleDateFormat();
        Date date = new Date(time);
        return context.getString(R.string.last_edit) + format.format(date);
    }

    public static final void viewTextNote(Context context, NoteInfo info) {
        Intent intent = new Intent();
        intent.setClass(context, NotePadActivity.class);
        intent.putExtra("id", info.getId());
        intent.putExtra("title", info.getTitle());
        intent.putExtra("content", info.getContent());
        context.startActivity(intent);
    }

    public static final void viewImageNote(Context context, NoteInfo info) {
        Intent imageIntent = new Intent();
        imageIntent.setAction("android.intent.action.VIEW");
        imageIntent.setDataAndType(Uri.fromFile(new File(info.getPath())), "image/*");
        context.startActivity(imageIntent);
    }

    public static final void viewVedioNote(Context context, NoteInfo info) {
        Intent videoIntent = new Intent();
        videoIntent.setAction("android.intent.action.VIEW");
        videoIntent.setDataAndType(Uri.fromFile(new File(info.getPath())), "video/*");
        context.startActivity(videoIntent);
    }

    public static final void viewAudioNote(Context context, NoteInfo info) {
        Intent audioIntent = new Intent();
        audioIntent.setAction("android.intent.action.VIEW");
        audioIntent.setDataAndType(Uri.fromFile(new File(info.getPath())), "audio/amr");
        context.startActivity(audioIntent);
    }

    //two types:TAKE_PHOTO and VEDIO 
    public static final void addMediaNote(final Context mContext, final int type) {
        String filePath = null;
        String title = null;
        if (type == Constants.VEDIO) {
            filePath = CommonUtil.getRealPathFromVedioURI(mContext);
        } else if (type == Constants.TAKE_PHOTO) {
            filePath = CommonUtil.getRealPathFromURI(mContext);
        }
        if (TextUtils.isEmpty(filePath)) {
            return;
        }
        StringTokenizer tokenizer = new StringTokenizer(filePath, "/");
        while (tokenizer.hasMoreElements()) {
            title = (String) tokenizer.nextToken();
        }
        NoteManager.addNote(mContext, title, "", filePath, type, DateUtil.getDateSimpleString());
        final String path = filePath;
        final String fileName = title;
        if (filePath != null) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    FileOutputStream out = null;
                    FileInputStream ins = null;
                    try {
                        ins = new FileInputStream(new File(path));
                        out = new FileOutputStream(new File(FileUtil.getFilePathByType(mContext, "media" + fileName,
                                        type)));
                        byte buffer[] = new byte[8904];
                        while (ins.read(buffer) != -1) {
                            out.write(buffer);
                            out.flush();
                        }
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute();

        }
    }
}
