package com.cc.software.calendar.observer;

import hut.cc.software.calendar.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Audio;
import android.telephony.SmsMessage;

import com.cc.software.calendar.activity.SMSContentActivity;

public class SMSReceiver extends BroadcastReceiver {
    public static SmsMessage smsMessage[];
    private Notification mNotification;
    public static NotificationManager mNotificationManager;
    public final static int NOTIFICATION_ID = 0x0001;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Object messages[] = (Object[]) bundle.get("pdus");
        smsMessage = new SmsMessage[messages.length];
        for (int n = 0; n < messages.length; n++) {
            smsMessage[n] = SmsMessage.createFromPdu((byte[]) messages[n]);
        }
        mNotification = new Notification(R.drawable.icon, "This is a notification.", System.currentTimeMillis());
        mNotification.sound = Uri.withAppendedPath(Audio.Media.INTERNAL_CONTENT_URI, "3");
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent mIntent = new Intent(context, SMSContentActivity.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent mContentIntent = PendingIntent.getActivity(context, 0, mIntent, 0);
        String title = smsMessage[0].getDisplayOriginatingAddress();
        mNotification.setLatestEventInfo(context, title, smsMessage[0].getMessageBody(), mContentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mNotification);
    }
}
