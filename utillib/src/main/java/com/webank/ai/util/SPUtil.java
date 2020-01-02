package com.webank.ai.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;



/**
 * <pre>
 *     author : v_wizardchen
 *     e-mail : v_wizardchen@webank.com
 *     time   : 2018/06/20
 *     desc   :
 *     version: 1.0
 * </pre>
 */


/**
 * Created by Administrator on 2016/5/3.
 */
public class SPUtil {
    public static final String RECODE_DURATION = "RECODE_DURATION";
    public static final String RECODE_SPEED_INDEX = "RECODE_SPEED_INDEX";
    public static final String RECODE_QUALITY = "RECODE_QUALITY";
    public static final int RECODE_SPEED_MIN = 100;
    public static final int RECODE_SPEED_MAX = 500;
    public static final int RECODE_DURATION_MAX = 8000;
    public static final int RECODE_DURATION_MIN = 2000;
    public static final int RECODE_DURATION_DEFAULT = 6000;
    public static final String KEY_FIRTST_RUNNING = "first_running";
    private static SharedPreferences mSharedPreferences;
    private Context mContext;

    private SPUtil(Context mContext) {
        this.mContext = mContext;
    }

    private static SPUtil instance;

    public static synchronized SPUtil getInstance(Context context) {
        if (instance == null) {
            instance = new SPUtil(context.getApplicationContext());
        }
        return instance;
    }



    private  synchronized SharedPreferences getPreferneces() {
        if (mSharedPreferences == null) {
            // mSharedPreferences = App.mContext.getSharedPreferences(
            // PREFERENCE_NAME, Context.MODE_PRIVATE);
            mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        }
        return mSharedPreferences;
    }

    /**
     * 打印所有
     */
    public  void print() {
        System.out.println(getPreferneces().getAll());
    }

    /**
     * 清空保存在默认SharePreference下的所有数据
     */
    public  void clear() {
        getPreferneces().edit().clear().commit();
    }

    /**
     * 保存字符串
     *
     * @return
     */
    public  void putString(String key, String value) {
        getPreferneces().edit().putString(key, value).commit();
    }

    /**
     * 读取字符串
     *
     * @param key
     * @return
     */
    public  String getString(String key) {
        return getPreferneces().getString(key, null);

    }

    /**
     * 读取字符串
     *
     * @param key
     * @return
     */
    public  String getString(String key, String value) {
        return getPreferneces().getString(key, value);

    }

    /**
     * 保存整型值
     *
     * @return
     */
    public  void putInt(String key, int value) {
        getPreferneces().edit().putInt(key, value).commit();
    }

    /**
     * 读取整型值
     *
     * @param key
     * @return
     */
    public  int getInt(String key) {
        return getPreferneces().getInt(key, 0);
    }

    public  int getInt(String key, int value) {
        return getPreferneces().getInt(key, value);
    }

    public  float getFloat(String key, float value) {
        return getPreferneces().getFloat(key, value);
    }

    public  void putFloat(String key, float value) {
        getPreferneces().edit().putFloat(key, value);
    }

    /**
     * 保存布尔值
     *
     * @return
     */
    public  void putBoolean(String key, Boolean value) {
        getPreferneces().edit().putBoolean(key, value).commit();
    }

    public  void putLong(String key, long value) {
        getPreferneces().edit().putLong(key, value).commit();
    }

    public  long getLong(String key) {
        return getPreferneces().getLong(key, 0);
    }

    /**
     * t 读取布尔值
     *
     * @param key
     * @return
     */
    public  boolean getBoolean(String key, boolean defValue) {
        return getPreferneces().getBoolean(key, defValue);

    }

    /**
     * 移除字段
     *
     * @return
     */
    public  void removeString(String key) {
        getPreferneces().edit().remove(key).commit();
    }
}