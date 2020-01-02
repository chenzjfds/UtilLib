package com.webank.ai.util;

import android.content.Context;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.download.DownloadListener;
import com.tencent.bugly.beta.download.DownloadTask;
import com.tencent.bugly.beta.upgrade.UpgradeStateListener;
import com.tencent.bugly.crashreport.CrashReport;
import com.webank.ai.http.BuildConfig;

import java.util.Timer;

/**
 * creaed by  zhijunchen on 2019/5/8
 */
public class BugLyUtil {
    public static final String TAG = "HelmetApp";

    public static void init(Context mContext, String id) {
        Beta.autoCheckUpgrade = false;
        Bugly.init(mContext, id, BuildConfig.DEBUG);//todo  不应该使用bugly升级方案，需替换，只需要日志上报系统

    }

    public static void init(Context mContext, String id, CrashReport.UserStrategy strategy) {
        Beta.autoCheckUpgrade = false;
        //Bugly.init(mContext, BUGLY_ID, BuildConfig.DEBUG);//todo  不应该使用bugly升级方案，需替换，只需要日志上报系统
        Bugly.init(mContext, id, BuildConfig.DEBUG, strategy);
        //Bugly.init(mContext, BUGLY_ID,false, strategy);
    }

    /**
     * @param mContext
     * @return
     */
    public static boolean updateApp(Context mContext) {
        LogUtil.i(TAG, "updateApp");
        Beta.registerDownloadListener(new DownloadListener() {
            @Override
            public void onReceive(DownloadTask downloadTask) {
                LogUtil.i(TAG, "onReceive downloadTask");
                updateBtn(downloadTask);
            }

            @Override
            public void onCompleted(DownloadTask downloadTask) {
                LogUtil.i(TAG, "onCompleted");
                updateBtn(downloadTask);
            }

            @Override
            public void onFailed(DownloadTask downloadTask, int i, String s) {
                LogUtil.i(TAG, "onFailed");
                updateBtn(downloadTask);
            }
        });

        Beta.upgradeStateListener = new UpgradeStateListener() {
            @Override
            public void onUpgradeSuccess(boolean isManual) {
                LogUtil.i(TAG, "onUpgradeSuccess");
                // Toast.makeText(mContext, "UPGRADE_SUCCESS", Toast.LENGTH_SHORT).show();
                download();
            }

            @Override
            public void onUpgradeFailed(boolean isManual) {
                LogUtil.i(TAG, "onUpgradeFailed");
                //   Toast.makeText(mContext, "UPGRADE_FAILED", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUpgrading(boolean isManual) {
                LogUtil.i(TAG, "onUpgrading isManual=" + isManual);
                download();
                //   Toast.makeText(mContext, "UPGRADE_CHECKING", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDownloadCompleted(boolean b) {
                LogUtil.i(TAG, "onDownloadCompleted");
                download();
            }

            @Override
            public void onUpgradeNoVersion(boolean isManual) {
                LogUtil.i(TAG, "onUpgradeNoVersion");
                //  Toast.makeText(mContext, "UPGRADE_NO_VERSION", Toast.LENGTH_SHORT).show();
                download();
            }
        };

        Beta.checkUpgrade(true, true);//不显示弹窗
        DownloadTask task = Beta.getStrategyTask();
       /* if (task != null) {
            task.download();
        }*/
        //Beta.checkUpgrade();
        //  DownloadTask task = Beta.startDownload();
        LogUtil.i(TAG, "DownloadTask444=" + task);
        updateBtn(task);

        return task != null;
    }

    private static DownloadTask download() {
        DownloadTask task = Beta.startDownload();
        LogUtil.i(TAG, "DownloadTask555=" + task);
        return task;
    }

    public static void updateBtn(DownloadTask task) {

        /*根据下载任务状态设置按钮*/
        if (task == null) {
            return;
        }
        switch (task.getStatus()) {
            case DownloadTask.INIT:
            case DownloadTask.DELETED:
            case DownloadTask.FAILED: {
                LogUtil.i(TAG, "开始下载");
            }
            break;
            case DownloadTask.COMPLETE: {
                LogUtil.i(TAG, "下载完成");
                // Beta.checkUpgrade(false, true);
                Beta.startDownload();
            }
            break;
            case DownloadTask.DOWNLOADING: {
                LogUtil.i(TAG, "下载中");

            }
            break;
            case DownloadTask.PAUSED: {
                LogUtil.i(TAG, "暂停下载了");
            }
            break;
        }
    }

}
