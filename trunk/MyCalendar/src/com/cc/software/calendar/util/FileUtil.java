package com.cc.software.calendar.util;

import java.io.File;

import com.cc.software.calendar.comp.Constants;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;

public class FileUtil {

    //public static final int TYPE_TXT_NOTE = 1, TYPE_IMAGE = 2, TYPE_RECORD = 3, TYPE_VEDIO = 4;

    private static final String FOLDER = "multi_fuction_calendar";
    private static final String RECORD_NOTE = "record";
    private static final String TXT_NOTE = "txt";
    private static final String VEDIO_NOTE = "vedio";
    private static final String IMAGE_NOTE = "image";

    public static final File getFileDir(Context context, int type) {
        File dir = null;
        String relativePath = null;
        if (type == Constants.TAKE_PHOTO) {
            relativePath = IMAGE_NOTE;
        } else if (type == Constants.RECORD) {
            relativePath = RECORD_NOTE;
        } else if (type == Constants.VEDIO) {
            relativePath = VEDIO_NOTE;
        } else {
            relativePath = TXT_NOTE;
        }
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            dir = new File("/sdcard/multi_fuction_calendar" + File.separator + relativePath);
        } else {
            dir = new File(context.getDir(FOLDER, Activity.MODE_PRIVATE).getPath() + File.separator + relativePath);
        }
        if (!dir.exists()) {
            dir.mkdirs();
        }

        return dir;
    }

    public static final String getFilePathByType(Context context, String fileName, int type) {
        return getFileDir(context, type).getAbsolutePath() + File.separator + fileName;
    }
}
