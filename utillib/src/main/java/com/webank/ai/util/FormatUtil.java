package com.webank.ai.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * creaed by  zhijunchen on 2019/4/26
 */
public class FormatUtil {
    public static String DATE_TO_STRING_DETAIAL_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /** 年-月-日 显示格式 */
    public static String DATE_TO_STRING_SHORT_PATTERN = "yyyy-MM-dd";
    public static String formatTimt(String format){
        SimpleDateFormat dateFormat=new SimpleDateFormat(format);
        Date currentTime = new Date();
        return dateFormat.format(currentTime);
    }
}
