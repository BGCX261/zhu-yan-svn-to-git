package com.cc.software.calendar.clock;

import android.content.Context;
import android.os.PowerManager;

class AlarmAlertWakeLock {

    private static PowerManager.WakeLock sWakeLock;

    static void acquire(Context context) {
        Log.v("Acquiring wake lock");
        if (sWakeLock != null) {
            sWakeLock.release();
        }

        PowerManager pm =
                (PowerManager) context.getSystemService(Context.POWER_SERVICE);

        sWakeLock = pm.newWakeLock(
                PowerManager.FULL_WAKE_LOCK |
                PowerManager.ACQUIRE_CAUSES_WAKEUP |
                PowerManager.ON_AFTER_RELEASE, Log.LOGTAG);
        sWakeLock.acquire();
    }

    static void release() {
        Log.v("Releasing wake lock");
        if (sWakeLock != null) {
            sWakeLock.release();
            sWakeLock = null;
        }
    }
}
