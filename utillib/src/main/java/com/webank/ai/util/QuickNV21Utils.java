package com.webank.ai.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.renderscript.Type;

/**
 * creaed by  zhijunchen on 2019/8/30
 */
public class QuickNV21Utils {
    private static final String TAG = "QuickNV21Utils";

    private RenderScript rs;
    private ScriptIntrinsicYuvToRGB yuvToRgbIntrinsic;
    private Type.Builder yuvType, rgbaType;
    private Allocation in, out;

    public QuickNV21Utils(Context context) {
        LogUtil.e(TAG, "NV21Utils初始化1");
        rs = RenderScript.create(context);
        yuvToRgbIntrinsic = ScriptIntrinsicYuvToRGB.create(rs, Element.U8_4(rs));
        LogUtil.e(TAG, "NV21Utils初始化2");

        LogUtil.e(TAG, "yuvType init1");
        yuvType = new Type.Builder(rs, Element.U8(rs));
        in = Allocation.createTyped(rs, yuvType.create(), Allocation.USAGE_SCRIPT);
        rgbaType = new Type.Builder(rs, Element.RGBA_8888(rs));
        out = Allocation.createTyped(rs, rgbaType.create(), Allocation.USAGE_SCRIPT);
        LogUtil.e(TAG, "yuvType init2");
    }


    public synchronized Bitmap nv21ToBitmap(byte[] nv21, int width, int height) {
        if (rs == null) {
            LogUtil.e(TAG, "NV21Utils尚未初始化");
        }
        LogUtil.e(TAG, "nv21ToBitmap 1");
        if (yuvType == null) {
            LogUtil.e(TAG, "yuvType init1");
            yuvType = new Type.Builder(rs, Element.U8(rs)).setX(nv21.length);
            in = Allocation.createTyped(rs, yuvType.create(), Allocation.USAGE_SCRIPT);
            rgbaType = new Type.Builder(rs, Element.RGBA_8888(rs)).setX(width).setY(height);
            out = Allocation.createTyped(rs, rgbaType.create(), Allocation.USAGE_SCRIPT);
            LogUtil.e(TAG, "yuvType init2");
        }
        in.copyFrom(nv21);
        yuvToRgbIntrinsic.setInput(in);
        yuvToRgbIntrinsic.forEach(out);
        Bitmap bmpout = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        out.copyTo(bmpout);
        LogUtil.e(TAG, "nv21ToBitmap 2  ");
        return bmpout;
    }

    /**
     * NV21裁剪 by lake 算法效率 11ms
     *
     * @param src    源数据
     * @param width  源宽
     * @param height 源高
     * @param left   顶点坐标
     * @param top    顶点坐标
     * @param clip_w 裁剪后的宽
     * @param clip_h 裁剪后的高
     * @return 裁剪后的数据
     */
    public byte[] cropNV21(byte[] src, int width, int height, int left, int top, int clip_w, int clip_h) {
        if (left > width || top > height) {
            return null;
        }
        //取偶
        int x = left / 2 * 2, y = top / 2 * 2;
        int w = clip_w / 2 * 2, h = clip_h / 2 * 2;
        int y_unit = w * h;
        int src_unit = width * height;
        int uv = y_unit >> 1;
        byte[] nData = new byte[y_unit + uv];


        for (int i = y, len_i = y + h; i < len_i; i++) {
            for (int j = x, len_j = x + w; j < len_j; j++) {
                nData[(i - y) * w + j - x] = src[i * width + j];
                nData[y_unit + ((i - y) / 2) * w + j - x] = src[src_unit + i / 2 * width + j];
            }
        }

        return nData;
    }

}
