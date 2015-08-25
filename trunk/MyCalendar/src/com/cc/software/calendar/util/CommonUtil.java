package com.cc.software.calendar.util;

import hut.cc.software.calendar.R;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

import com.cc.software.calendar.comp.Constants;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

public class CommonUtil {

    public static final Bitmap getBitmap(String path) {
        FileInputStream ins = null;
        Bitmap bitmap = null;
        try {
            ins = new FileInputStream(new File(path));
            if (ins != null) {
                bitmap = BitmapFactory.decodeStream(ins);
                ins.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static final String getRealPathFromURI(Context context) {
        Cursor cursor = ((Activity) context).managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null,
                        null, "_id desc");
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        if (cursor.moveToFirst()) {
            return cursor.getString(column_index);
        }
        return null;
    }

    public static final String getRealPathFromVedioURI(Context context) {
        Cursor cursor = ((Activity) context).managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null,
                        null, "_id desc");
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
        if (cursor.moveToFirst()) {
            return cursor.getString(column_index);
        }
        return null;
    }

    public static void showToastMessage(int msgId, Context context) {
        Toast.makeText(context, msgId, Toast.LENGTH_SHORT).show();
    }

    public static void showToastMessage(String msg, Context context) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static BitmapDrawable getBackGround(Context context) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.body_bg);
        BitmapDrawable logo = new BitmapDrawable(bitmap);
        logo.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
        logo.setDither(true);
        return logo;
    }

    public static boolean isNetAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null) {
            return info.isAvailable();
        } else {
            CommonUtil.showToastMessage(R.string.net_cannot_work, context);
        }
        return false;
    }

    public static final void shareImage(Context c, String url) {
        Intent send = new Intent(Intent.ACTION_SEND);
        send.setType("image/jpeg");
        send.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + url));
        send.putExtra(Intent.EXTRA_TEXT, c.getString(R.string.share_from_application));
        send.putExtra(Intent.EXTRA_SUBJECT, c.getString(R.string.image_share));
        try {
            c.startActivity(Intent.createChooser(send, c.getString(R.string.image_share)));
        } catch (android.content.ActivityNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public static String MD5Encode(byte[] bytes) {
        StringBuffer hexString = new StringBuffer();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(bytes);
            byte[] digest = md.digest();
            String text;
            for (int i = 0; i < digest.length; i++) {
                text = Integer.toHexString(0xFF & digest[i]);
                if (text.length() < 2) {
                    text = "0" + text;
                }
                hexString.append(text);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hexString.toString();
    }

    public static String MD5Encode(String text) {
        return MD5Encode(text.getBytes());
    }

    public static void log_d(String tag, String message) {
        if (Constants.SHOW_LOG)
            Log.d(tag, message);
    }
}
