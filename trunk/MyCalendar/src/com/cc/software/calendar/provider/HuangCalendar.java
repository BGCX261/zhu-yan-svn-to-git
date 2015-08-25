package com.cc.software.calendar.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class HuangCalendar {

    public static final String TABLE_NAME = "calendar";
    public static final String AUTHOURITIES = "hut.cc.software.calendar";
    public static final Uri CALENDAR_URI = Uri.parse("content://" + AUTHOURITIES
            + "/" + TABLE_NAME);

    public static String CALENDAR_QUERY_PROJECTION[] = new String[] {
            HuangCalendarColumns._ID, HuangCalendarColumns.TRADITION_CALENDAR,
            HuangCalendarColumns.GANZHI, HuangCalendarColumns.FITTING,
            HuangCalendarColumns.FORBID, HuangCalendarColumns.JISHENYIQU,
            HuangCalendarColumns.XIONGSHENYIJI,
            HuangCalendarColumns.TAISHENZHANFANG, HuangCalendarColumns.WUHANG,
            HuangCalendarColumns.CHONG, HuangCalendarColumns.PENGZUBAIJI,
            HuangCalendarColumns.CALENDAR };

    public static class HuangCalendarColumns implements BaseColumns {
        public static final String TRADITION_CALENDAR = "tracalendar"; //ũ��
        public static final String GANZHI = "ganzhi"; //��֧
        public static final String FITTING = "fitting"; // ��
        public static final String FORBID = "forbid"; //��
        public static final String JISHENYIQU = "jishenyiqu"; //��������
        public static final String XIONGSHENYIJI = "xiongshenyiji"; //�����˼�
        public static final String TAISHENZHANFANG = "taishenzhanfang"; //̥��ռ��
        public static final String WUHANG = "wuhang";// ����
        public static final String CHONG = "chong"; //��
        public static final String PENGZUBAIJI = "pengzubaiji"; // ����ټ�
        public static final String CALENDAR = "calendar"; //

        public static final int COLUME_INDEX_ID = 0;
        public static final int COLUME_INDEX_TRADITION_CALENDAR = 1;
        public static final int COLUME_INDEX_GANZHI = 2;
        public static final int COLUME_INDEX_FITTING = 3;
        public static final int COLUME_INDEX_FORBID = 4;
        public static final int COLUME_INDEX_JISHENYIQU = 5;
        public static final int COLUME_INDEX_XIONGSHENYIJI = 6;
        public static final int COLUME_INDEX_TAISHENZHANFANG = 7;
        public static final int COLUME_INDEX_WUHANG = 8;
        public static final int COLUME_INDEX_CHONG = 9;
        public static final int COLUME_INDEX_PENGZUBAIJI = 10;
        public static final int COLUME_INDEX_CALENDAR = 11;
    }

}
