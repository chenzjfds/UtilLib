package com.webank.ai.util;

import android.os.Handler;
import android.os.HandlerThread;

/**
 * <pre>
 *     author : v_wizardchen
 *     e-mail : v_wizardchen@webank.com
 *     time   : 2018/10/16
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class WorkThreadUtil {
    private static HandlerThread handlerThread;
    private static Handler handler;

    static {
        handlerThread = new HandlerThread("work");
        handlerThread.setPriority(Thread.MIN_PRIORITY);
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
    }

    public static void post(Runnable runnable) {
        handler.post(runnable);
    }

    public static void postDelay(Runnable runnable, long delay) {
        handler.postDelayed(runnable, delay);
    }

    public static void removeCallbacksAndMessages() {
        handler.removeCallbacksAndMessages(null);
    }

    public static void removeCallbacks(Runnable runnable) {
        handler.removeCallbacks(runnable);
    }


}
