
package com.common.util;

import android.content.res.Resources;
import android.util.DisplayMetrics;

public class SystemInfos {

    public static float density;
    public static int densityDpi;
    public static int widthPixels;
    public static int heightPixels;

    public final static void updateDimension(Resources resource) {
        final DisplayMetrics displayMetrics = resource.getDisplayMetrics();
        density = displayMetrics.density;
        densityDpi = displayMetrics.densityDpi;
        widthPixels = displayMetrics.widthPixels;
        heightPixels = displayMetrics.heightPixels;
    }
}
