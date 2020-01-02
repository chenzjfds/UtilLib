package com.webank.ai.util;

import com.webank.mbank.wejson.WeJson;

/**
 * <pre>
 *     author : v_wizardchen
 *     e-mail : v_wizardchen@webank.com
 *     time   : 2018/08/21
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class JsonUtil {
    private static WeJson weJson = new WeJson();

    public static WeJson getWeJson() {
        return weJson;
    }

    public static <T> String toJson(T t) {
        return weJson.toJson(t);
    }

    public static <T> T fromJson(String json, Class<T> t) {
        return weJson.fromJson(json, t);
    }
}
