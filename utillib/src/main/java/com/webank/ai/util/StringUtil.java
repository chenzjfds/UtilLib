package com.webank.ai.util;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * creaed by  zhijunchen on 2019/5/10
 */
public class StringUtil {
    public static String format(String data) {
        // 只允许字母和数字
        // String   regEx  =  "[^a-zA-Z0-9]";
        // 清除掉所有特殊字符
        if(TextUtils.isEmpty(data)){
            return "";
        }
        String regEx = "[。？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(data);
        return m.replaceAll("").trim();
    }

    public static String getFileName(String path) {
        return path.substring(path.lastIndexOf("/")+1);
    }
    public static  String getName(String name){
       return name.substring(0,name.lastIndexOf("."));
    }
}
