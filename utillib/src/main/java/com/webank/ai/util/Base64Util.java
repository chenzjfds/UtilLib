package com.webank.ai.util;

import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * creaed by  zhijunchen on 2019/2/15
 */
public class Base64Util {
    public static final String TAG = "Base64Util";

    /**
     * 将base64字符解码保存文件
     *
     * @throws Exception
     */
    public static String encodeBase64FromFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return "";
        }
        int size = (int) file.length();
        byte[] bytes = new byte[size];
        FileInputStream out = null;
        try {
            out = new FileInputStream(file);
            out.read(bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        byte[] tmp = Base64.encode(bytes, Base64.NO_WRAP);
        String base64Date = new String(tmp);
        return base64Date;
    }

    /**
     * 将base64字符解码保存文件
     *
     * @throws Exception
     */
    public static String encodeBase64FromData(byte[] data) {
        byte[] tmp = Base64.encode(data, Base64.NO_WRAP);
        String base64Date = new String(tmp);
        return base64Date;
    }

    public static byte[] decodeBase64ToData(String data) {
        return Base64.decode(data, Base64.NO_WRAP);
    }

}
