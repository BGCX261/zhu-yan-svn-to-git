package com.cc.software.calendar.helper;

import java.lang.ref.SoftReference;

import android.graphics.Bitmap;

public class BitmapHolder {
    public final static int NEEDED = 0;
    public final static int LOADING = 1;
    public final static int LOADED = 2;
    public int state;
    public SoftReference<Bitmap> bitmapRef;
}
