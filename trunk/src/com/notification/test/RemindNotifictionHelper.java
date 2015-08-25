
package com.notification.test;

import com.view.main.MainActivity;
import com.view.wheelview.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class RemindNotifictionHelper {

    private RemindNotifictionHelper() {

    }

    private Notification mNotification = null;
    public static NotificationManager mNotificationManager = null;

    private static final int NOTIFICATION_STATE = 10238;
    private Intent mIntent = null;
    private PendingIntent mPendingIntent = null;

    private static RemindNotifictionHelper mHelper;
    private Context mContext;
    private boolean isInit = false;

    public static RemindNotifictionHelper getInstance() {
        if (mHelper == null) {
            mHelper = new RemindNotifictionHelper();
        }
        return mHelper;
    }

    public void init(Context context) {
        if (!isInit) {
            mContext = context;
            mNotificationManager = (NotificationManager) mContext
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            mIntent = new Intent(context, MainActivity.class);
            //mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mNotification = new Notification();
            mNotification.icon = R.drawable.ic_launcher;
            mNotification.defaults = Notification.DEFAULT_LIGHTS;
            mNotification.flags |= Notification.FLAG_AUTO_CANCEL;
        }
    }

    @SuppressWarnings("deprecation")
    public void showNotifiction(String info) {
        //cancelNotifiction();
        //mIntent.setAction(PcUrlManager.ACTION_DESKTOP_PC_URL);
        //mIntent.setAction(ShortcutUtil.ACTION_DESKTOP_LINK);
        //mIntent.setData(Uri.parse(info));
        mPendingIntent = PendingIntent.getActivity(mContext, 0, mIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        //int count = PcUrlManager.getPcUnReadUrlCount(mContext);
        mNotification.setLatestEventInfo(mContext, "收到" + 0 + "条来自电脑的网页", "点击查看详情",
                mPendingIntent);
        mNotificationManager.notify(NOTIFICATION_STATE, mNotification);
        Log.d("w.w", "showNotifiction=" + info);
    }

    public void cancelNotifiction() {
        Log.d("w.w", "cancelNotifiction");
        if (mNotificationManager != null) {
            mNotificationManager.cancelAll();
            //mNotificationManager.cancel(NOTIFICATION_STATE);
        }
    }

}
