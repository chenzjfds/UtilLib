package com.webank.ai.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * creaed by  zhijunchen on 2019/2/26
 */
public class TelUtil {
    /**
     * 拨打电话（直接拨打电话）
     *
     * @param phone 电话号码
     */
    public static void callPhton(Context mContext, String phone) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phone);
        intent.setData(data);
        mContext.startActivity(intent);

    }

    /**g'ra
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    public static void callPhoneNeedSure(Context mContext, String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        mContext.startActivity(intent);
    }

}
