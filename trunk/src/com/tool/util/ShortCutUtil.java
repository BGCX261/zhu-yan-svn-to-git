
package com.tool.util;

import com.view.main.MainActivity;
import com.view.wheelview.R;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;

public class ShortCutUtil {

    public static final void addShortCut(Context context) {
        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,
                context.getResources().getString(R.string.app_name));
        shortcut.putExtra("duplicate", false);
        Parcelable icon = Intent.ShortcutIconResource.fromContext(context,
                R.drawable.ic_launcher);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        intent.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setClass(context, MainActivity.class);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
        context.sendBroadcast(shortcut);
    }

    /** 
     * 删除程序的快捷方式 
     */
    public static void delShortcut(Activity activity) {
        Intent shortcut = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
        //快捷方式的名称 
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,
                activity.getResources().getString(R.string.app_name));
        Intent intent = new Intent();
        //intent.setClass(activity, MainActivity.class);
        ComponentName componentName = new ComponentName("com.view.wheelview",
                "com.view.main.MainActivity");
        intent.setComponent(componentName);
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
        activity.sendBroadcast(shortcut);
        Log.d("w.w", "delShortcut");
    }
}
