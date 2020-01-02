package com.webank.ai.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;

import java.io.ByteArrayOutputStream;

/**
 * creaed by  zhijunchen on 2019/2/28
 */
public class ImageConvertUtil {
    public static final String TAG = "ImageConvertUtil";

    /**
     * yuv转bitmap
     *
     * @param data
     * @param width
     * @param height
     * @param quality
     * @param rect
     * @return
     */
    public static Bitmap yuv2Bitmap(byte[] data, int width, int height, int quality, Rect rect) {
        LogUtil.i(TAG, "yuv2Bitmap1");
        if (rect.left < 0) {
            rect.left = 0;
        }
        if (rect.right > width) {
            rect.right = width;
        }
        if (rect.top < 0) {
            rect.top = 0;
        }
        if (rect.bottom > height) {
            rect.bottom = height;
        }
        long time = System.currentTimeMillis();

        YuvImage image = new YuvImage(data, ImageFormat.NV21, width, height, null);

        ByteArrayOutputStream os = new ByteArrayOutputStream(data.length);
        if (!image.compressToJpeg(rect, quality, os)) {
            return null;
        }
        byte[] tmp = os.toByteArray();
        //  Bitmap bitmap=BitmapFactory.decodeByteArray(tmp,0,tmp.length);
        Bitmap bitmap = BitmapFactory.decodeByteArray(tmp, 0, tmp.length);
        LogUtil.i(TAG, "yuv2Bitmap2");
        return bitmap;
    }

    /**
     * yuv转bitmap格式二进制
     *
     * @param data
     * @param width
     * @param height
     * @param quality
     * @param rect
     * @return
     */
    public static byte[] yuv2JpgData(byte[] data, int width, int height, int quality, Rect rect) {
        LogUtil.i(TAG, "yuv2jpgData1");
        if (rect.left < 0) {
            rect.left = 0;
        }
        if (rect.right > width) {
            rect.right = width;
        }
        if (rect.top < 0) {
            rect.top = 0;
        }
        if (rect.bottom > height) {
            rect.bottom = height;
        }

        YuvImage image = new YuvImage(data, ImageFormat.NV21, width, height, null);

        ByteArrayOutputStream os = new ByteArrayOutputStream(data.length);
        if (!image.compressToJpeg(rect, quality, os)) {
            return null;
        }
        byte[] tmp = os.toByteArray();
        return tmp;

    }

    public static byte[] compressBitmap(Bitmap bitmap,int quality) {
        ByteArrayOutputStream os = new ByteArrayOutputStream(bitmap.getByteCount());
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, os);
        LogUtil.i(TAG, "yuv2jpgData2");
        byte[] data2 = os.toByteArray();
        return data2;
    }
}
