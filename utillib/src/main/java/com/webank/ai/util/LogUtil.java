package com.webank.ai.util;

import android.util.Log;


import com.webank.ai.http.BuildConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * <pre>
 *     author : v_wizardchen
 *     e-mail : v_wizardchen@webank.com
 *     time   : 2018/06/14
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class LogUtil {
    public static final String TAG = "webank_ai_coffee";
    public static final String LOG_TAG_ERROR = "Error-";
    public static final String LOG_TAG_WARM = "Warm-";
    public static final String LOG_XUNFEI = "VoiceModuleXF";
    public static final String WeBotCameraModule = "WeBotCameraModule";
    public static final String LOG_XUNFEI2 = "VoiceModuleXF2";
    public static final String LOG_MSG_MODULE = "WeBotMsgModule";
    public static final String LOG_TAG_ACTION = "WeBotActionModule";
    public static final String LOG_TAG_INIT = "InitPresenter";
    public static final String LOG_TAG_ROUTER = "WeBotRouterModule";
    public static final String LOG_TAG_DATARESPONSITY = "DataResponsity";
    public static final String LOG_TAG_WEBOT_MODULE = "WeBotModule";
    public static final String LOG_TAG_WEBOT_REQUEST = "WeBotRequest";
    public static final String WeBotFaceModule = "WeBotCameraModule";
    public static final String CoffeeActionExecutor = "CoffeeActionExecutor";
    public static final String SpeakActionExecutor = "SpeakActionExecutor";
    public static final String CameraMoule = "CameraMoule";
    public static final String WaitActionExecutor = "WaitActionExecutor";
    public static final String WeBotVoiceModule = "WeBotVoiceModule";
    public static final String InitPresenter = "InitPresenter";
    public static final String RemotePictureModule = "RemotePictureModule";
    public static final String CollectDocActionPresenter = "CollectDocActionPresenter";
    public static final String CollectFaceActionPresenter = "CollectFaceActionPresenter";
    public static final String CollectSpaceActionPresenter = "CollectSpaceActionPresenter";
    public static final String CtrlDevActionPresenter = "CtrlDevActionPresenter";
    public static final String AppUpdatePresenter = "AppUpdatePresenter";
    public static final String AnswerProcessor = "AnswerProcessor";
    public static final String WeBotPresenter = "WeBotPresenter";
    public static final String ExtraCmdPresenter = "ExtraCmdPresenter";
    private static WeBotLogFilesUtil logFielsUtil = new WeBotLogFilesUtil(UtilConstants.LOG_DIR, "Log_" + TAG + "_" + LogUtil.getCurrentTime2S() + ".txt");

    public static boolean close = false;
    static HashMap<String, ArrayList<String>> logCache = new HashMap<>();
    private static boolean isCache = false;
    private static boolean SAVE_FILE = true;
    private static LogChangeCallback logChangeCallback;

    public static void setLogCallback(LogChangeCallback callback) {
        logChangeCallback = callback;
    }


    public static ArrayList<String> getTagLogCache(String tag) {
        return logCache.get(tag);
    }

    public static void pauseLogCache() {
        isCache = false;
    }

    public static void startLogCache() {
        isCache = true;
    }

    public static void clearLogCache() {
        for (ArrayList list : logCache.values()) {
            list.clear();
        }
    }

    private static void putCacheLog(String tag, String log) {
        if (!isCache) {
            return;
        }
        if (!logCache.containsKey(tag)) {
            ArrayList list = new ArrayList();
            list.add(log);
            logCache.put(tag, list);
        } else {
            ArrayList list = logCache.get(tag);
            if (list.size() >= 1000) {
                Log.w(TAG, "日志过长，清空日志=" + tag);
                list.clear();
            }
            list.add(log);
        }
        if (logChangeCallback != null) {
            logChangeCallback.onLogChanged(tag);
        }
    }

    public static void i(String tag, String info) {
        if (close) {
            return;
        }
        if (BuildConfig.DEBUG) {
            Log.i(tag, info);
        }
        StringBuffer str = new StringBuffer(getCurrentTime() + ": " + "Verbose-");
        str.append(tag);
        str.append(" ：");
        str.append(info);
        String log = str.toString();
        if (SAVE_FILE) {
            fileLog(log);
        }
        putCacheLog(tag, log);

    }

    public static void w(String tag, String info) {
        if (close) {
            return;
        }
        if (BuildConfig.DEBUG) {
            Log.w(tag, info);
        }
        StringBuffer str = new StringBuffer(getCurrentTime() + ": " + LOG_TAG_WARM);
        str.append(tag);
        str.append(" ：");
        str.append(info);
        String log = str.toString();
        if (SAVE_FILE) {
            fileLog(log);
        }
        putCacheLog(tag, log);
        putCacheLog(LOG_TAG_WARM, log);
    }

    public static void i(String info) {
        if (close) {
            return;
        }
        i(TAG, info);
    }

    public static void logFile(String tag, String info) {
        if (close) {
            return;
        }
        if (BuildConfig.DEBUG) {
            Log.e(tag, info);
        }

        StringBuffer str = new StringBuffer(getCurrentTime() + ": " + LOG_TAG_ERROR);
        str.append(tag);
        str.append(" ：");
        str.append(info);
        String log = str.toString();
        if (SAVE_FILE) {
            fileLog(log);
        }
        putCacheLog(tag, log);
        putCacheLog(LOG_TAG_ERROR, log);
    }

    public static void e(String tag, String info) {
        if (close) {
            return;
        }
        if (BuildConfig.DEBUG) {
            Log.e(tag, info);
        }

        StringBuffer str = new StringBuffer(getCurrentTime() + ": " + LOG_TAG_ERROR);
        str.append(tag);
        str.append(" ：");
        str.append(info);
        String log = str.toString();
        // fileLog(log);
        putCacheLog(tag, log);
        putCacheLog(LOG_TAG_ERROR, log);
    }

    public static void e(String info) {
        if (close) {
            return;
        }
        e(TAG, info);
    }

    private static void fileLog(String content) {
        if (logFielsUtil != null) {
            logFielsUtil.write(content);
        }
    }

    public static String SPLIT = "\\n ";

    public static void weTestLog() {
        //  LogReportUtil.getInstance().sendLogImmediately(SPLIT+"testkey="+"testvalue"+SPLIT+"testkey2="+"testvalue2");
    }

    public static void initWeLog() {
        //  LogReportUtil.getInstance().init(WeBotApplication.instance, BuildConfig.reportUrl,WE_LOG_KEY2,WE_LOG_KEY);
    }

    private static String getCurrentTime() {
        Date curDate = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
        return formatter.format(curDate);
    }

    public static String getCurrentTime2S() {
        Date curDate = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        return formatter.format(curDate);
    }

    public static String getCurrentTime2D() {
        Date curDate = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(curDate);
    }

    public static class WeBotLogFilesUtil {
        public FileOutputStream fileOutputStream;
        private File logFile;
        private String line = System.getProperty("line.separator");
        ExecutorService singlePool;

        public WeBotLogFilesUtil(String dir, String fileName) {
            this.initLogFile(dir, fileName);
        }

        private void initLogFile(final String dir, final String fileName) {
            singlePool = Executors.newSingleThreadExecutor();
            singlePool.submit(new Runnable() {
                @Override
                public void run() {
                    try {

                        if (logFile == null) {
                            logFile = new File(dir, fileName);
                            if (!logFile.getParentFile().exists()) {
                                logFile.getParentFile().mkdirs();
                            }

                            if (!logFile.exists()) {
                                logFile.createNewFile();
                            }

                            deleteOldFile();
                        }

                        if (fileOutputStream == null) {
                            fileOutputStream = new FileOutputStream(logFile, true);
                        }
                    } catch (Exception var3) {
                        var3.printStackTrace();
                    }
                }
            });
        }

        private void deleteOldFile() {
            if (this.logFile != null) {
                File[] files = this.logFile.getParentFile().listFiles();
                if (files.length > 7) {
                    for (int i = 0; i < files.length; ++i) {
                        long l = files[i].lastModified();
                        if (l < System.currentTimeMillis() - 259200000) {
                            Log.d("WeBotLogFilesUtil", "deleteOldFile: ");
                            files[i].delete();
                        }
                    }
                }

            }
        }

        public void write(final String content) {
            singlePool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (fileOutputStream != null) {
                            fileOutputStream.write((content + line).getBytes());
                        }
                    } catch (Exception var3) {
                        var3.printStackTrace();
                    }
                }
            });
        }

        public File getLogFile() {
            return logFile;
        }

        public void release() {
            if (this.fileOutputStream != null) {
                try {
                    this.fileOutputStream.close();
                } catch (IOException var2) {
                    var2.printStackTrace();
                }
            }

            if (this.logFile != null) {
                this.logFile = null;
            }

            if (this.fileOutputStream != null) {
                System.gc();
            }
        }
    }

    public interface LogChangeCallback {
        void onLogChanged(String key);
    }
}
