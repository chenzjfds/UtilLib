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
public class NV21Utils {
    public static byte[] NV21_rotate_to_90(byte[] nv21_data, int width, int height) {
        int y_size = width * height;
        int buffser_size = y_size * 3 / 2;
        byte[] nv21_rotated = new byte[buffser_size];
// Rotate the Y luma


        int i = 0;
        int startPos = (height - 1) * width;
        for (int x = 0; x < width; x++) {
            int offset = startPos;
            for (int y = height - 1; y >= 0; y--) {
                nv21_rotated[i] = nv21_data[offset + x];
                i++;
                offset -= width;
            }
        }
// Rotate the U and V color components
        i = buffser_size - 1;
        for (int x = width - 1; x > 0; x = x - 2) {
            int offset = y_size;
            for (int y = 0; y < height / 2; y++) {
                nv21_rotated[i] = nv21_data[offset + x];
                i--;
                nv21_rotated[i] = nv21_data[offset + (x - 1)];
                i--;
                offset += width;
            }
        }
        return nv21_rotated;

    }


    public static byte[] rotateYUV420Degree90(byte[] data, int imageWidth, int imageHeight) {
        byte[] yuv = new byte[imageWidth * imageHeight * 3 / 2];
        int i = 0;
        LogUtil.i(TAG, "rotateYUV420Degree90 1  ");
        for (int x = 0; x < imageWidth; x++) {
            for (int y = imageHeight - 1; y >= 0; y--) {
                yuv[i] = data[y * imageWidth + x];
                i++;
            }
        }
        i = imageWidth * imageHeight * 3 / 2 - 1;
        LogUtil.i(TAG, "rotateYUV420Degree90  2");
        for (int x = imageWidth - 1; x > 0; x = x - 2) {
            for (int y = 0; y < imageHeight / 2; y++) {
                yuv[i] = data[(imageWidth * imageHeight) + (y * imageWidth) + x];
                i--;
                yuv[i] = data[(imageWidth * imageHeight) + (y * imageWidth)
                        + (x - 1)];
                i--;
            }
        }
        LogUtil.i(TAG, "rotateYUV420Degree90  3");
        return yuv;
    }

    public static byte[] rotateYUV420Degree180(byte[] data, int imageWidth, int imageHeight) {
        byte[] yuv = new byte[imageWidth * imageHeight * 3 / 2];
        int i = 0;
        int count = 0;
        LogUtil.i(TAG, "rotateYUV420Degree180 1  ");
        for (i = imageWidth * imageHeight - 1; i >= 0; i--) {
            yuv[count] = data[i];
            count++;
        }
        LogUtil.i(TAG, "rotateYUV420Degree180 2  ");
        i = imageWidth * imageHeight * 3 / 2 - 1;
        for (i = imageWidth * imageHeight * 3 / 2 - 1; i >= imageWidth
                * imageHeight; i -= 2) {
            yuv[count++] = data[i - 1];
            yuv[count++] = data[i];
        }
        LogUtil.i(TAG, "rotateYUV420Degree180 3  ");
        return yuv;
    }

    public static byte[] rotateYUV420Degree270(byte[] data, int imageWidth,
                                               int imageHeight) {
        byte[] yuv = new byte[imageWidth * imageHeight * 3 / 2];
        int nWidth = 0, nHeight = 0;
        int wh = 0;
        int uvHeight = 0;
        if (imageWidth != nWidth || imageHeight != nHeight) {
            nWidth = imageWidth;
            nHeight = imageHeight;
            wh = imageWidth * imageHeight;
            uvHeight = imageHeight >> 1;// uvHeight = height / 2
        }

        int k = 0;
        for (int i = 0; i < imageWidth; i++) {
            int nPos = 0;
            for (int j = 0; j < imageHeight; j++) {
                yuv[k] = data[nPos + i];
                k++;
                nPos += imageWidth;
            }
        }
        for (int i = 0; i < imageWidth; i += 2) {
            int nPos = wh;
            for (int j = 0; j < uvHeight; j++) {
                yuv[k] = data[nPos + i];
                yuv[k + 1] = data[nPos + i + 1];
                k += 2;
                nPos += imageWidth;
            }
        }
        return rotateYUV420Degree180(rotateYUV420Degree90(data, imageWidth, imageHeight), imageWidth, imageHeight);
    }

    private static final String TAG = "NV21Utils";

    public static void NV21ToNV12(byte[] nv21, byte[] nv12, int width, int height) {
        if (nv21 == null || nv12 == null) return;
        LogUtil.i(TAG, "NV21ToNV12 1  nv21 lenghth=" + nv21.length + "  nv12 lenght=" + nv12.length);
        int framesize = width * height;
        int i = 0, j = 0;

        // System.arraycopy(nv21, 0, nv12, 0, framesize);

        for (i = 0; i < framesize; i++) {
            nv12[i] = nv21[i];
        }
        LogUtil.i(TAG, "NV21ToNV12 2");
        for (j = 0; j < framesize / 2; j += 2) {
            nv12[framesize + j - 1] = nv21[j + framesize];
        }
        LogUtil.i(TAG, "NV21ToNV12 3");
        for (j = 0; j < framesize / 2; j += 2) {
            nv12[framesize + j] = nv21[j + framesize - 1];
        }
        LogUtil.i(TAG, "NV21ToNV12 4");
    }

    public static void NV12ToYuv420P(byte[] nv12, byte[] yuv420p, int width, int height) {
        LogUtil.i(TAG, "NV21ToNV12 1  nv12 lenghth=" + nv12.length + "  yuv420p lenght=" + yuv420p.length);
        int ySize = width * height;
        int i, j;
//y
        LogUtil.i(TAG, "NV12ToYuv420P 1");
        for (i = 0; i < ySize; i++) {
            yuv420p[i] = nv12[i];
        }
        LogUtil.i(TAG, "NV12ToYuv420P 2");
//u
        i = 0;
        for (j = 0; j < ySize / 2; j += 2) {
            yuv420p[ySize + i] = nv12[ySize + j];
            i++;
        }
        LogUtil.i(TAG, "NV12ToYuv420P 3");
//v
        i = 0;
        for (j = 1; j < ySize / 2; j += 2) {
            yuv420p[ySize * 5 / 4 + i] = nv12[ySize + j];
            i++;
        }
        LogUtil.i(TAG, "NV12ToYuv420P 4");
    }


    private static RenderScript rs;
    private static ScriptIntrinsicYuvToRGB yuvToRgbIntrinsic;
    private static Type.Builder yuvType, rgbaType;
    private static Allocation in, out;

    public static void init(Context context) {
        LogUtil.e(TAG, "NV21Utils初始化1");
        rs = RenderScript.create(context);
        yuvToRgbIntrinsic = ScriptIntrinsicYuvToRGB.create(rs, Element.U8_4(rs));
        LogUtil.e(TAG, "NV21Utils初始化2");
    }

    public static  Bitmap nv21ToBitmap(byte[] nv21, int width, int height) {
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
    public static byte[] cropNV21(byte[] src, int width, int height, int left, int top, int clip_w, int clip_h) {
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
