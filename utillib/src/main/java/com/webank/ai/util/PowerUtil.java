package com.webank.ai.util;

import android.content.Context;
import android.os.PowerManager;

import static android.content.Context.POWER_SERVICE;

/**
 * creaed by  zhijunchen on 2019/10/9
 */
public class PowerUtil {
    private static PowerManager pManager;
    private static PowerManager.WakeLock mWakeLock;
    private static final String TAG = "PowerUtil";

    public static void keepScreenOn(Context mContext) {
        pManager = ((PowerManager) mContext.getSystemService(POWER_SERVICE));
        mWakeLock = pManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, TAG);
        mWakeLock.acquire();
    }

    public static void offScreen() {
        if (null != mWakeLock) {
            mWakeLock.release();
        }
    }
}
