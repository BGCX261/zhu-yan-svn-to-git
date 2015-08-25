package com.cc.software.calendar.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class Note {

    public static final String TABLE_NAME = "note";
    public static final String AUTHOURITIES = "hut.cc.software.note";
    public static final Uri NOTE_URI = Uri.parse("content://" + AUTHOURITIES + "/" + TABLE_NAME);

    public static String NOTE_QUERY_PROJECTION[] = new String[] { NoteColumns._ID, NoteColumns.TITLE,
                    NoteColumns.DESCRIPTION, NoteColumns.PATH, NoteColumns.TYPE, NoteColumns.CREATE_DATE,
                    NoteColumns.PATENT, NoteColumns.CALENDAR };

    public static class NoteColumns implements BaseColumns {
        public static final String TITLE = "title"; //����
        public static final String DESCRIPTION = "description"; //����
        public static final String PATH = "path"; // �洢·��
        public static final String TYPE = "type"; //0�ļ���  ���  1.�ı� 2.ͼƬ 3.¼�� 4.��Ƶ
        public static final String CREATE_DATE = "create_date"; //��������
        public static final String PATENT = "parent"; //
        public static final String CALENDAR = "calendar";

        public static final int COLUME_INDEX_ID = 0;
        public static final int COLUME_INDEX_TITLE = 1;
        public static final int COLUME_INDEX_CONTENT = 2;
        public static final int COLUME_INDEX_PATH = 3;
        public static final int COLUME_INDEX_TYPE = 4;
        public static final int COLUME_INDEX_CREATE_DATE = 5;
        public static final int COLUME_INDEX_PATENT = 6;
        public static final int COLUME_INDEX_CALENDAR = 7;
    }

}
