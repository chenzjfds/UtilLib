package com.webank.ai.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * <pre>
 *     author : v_wizardchen
 *     e-mail : v_wizardchen@webank.com
 *     time   : 2018/06/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class ImageUtil {
    private static final String TAG = "ImageUtil";

    public static Bitmap getImageFile(String path) {
        try {
            FileInputStream fis = new FileInputStream(path);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param name
     * @param data
     * @throws IOException
     */
    public static boolean saveData2File(String name, byte[] data) {
        File file = new File(name);
        if (!file.exists()) {
            LogUtil.i(TAG, "文件不存在，创建");
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            LogUtil.i(TAG, "文件已存在");
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.write(data);
            out.flush();
            LogUtil.i(TAG, "写入文件");
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            LogUtil.i(TAG, "文件不存在");
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (out != null) {
                    out.close();
                    LogUtil.i(TAG, "关闭文件流");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Bitmap getAssetFile(Context context, String name) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(context
                    .getAssets().open(name));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    /*
     * 获取位图的RGB数据
     */
    public static byte[] getRGBByBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int size = width * height;

        int pixels[] = new int[size];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        byte[] data = convertColorToByte(pixels);

        return data;
    }

    /*
     * 获取位图的YUV数据
     * todo 可能会出现内存溢出，需要做图片压缩
     */
    public static byte[] getYUVByBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int size = width * height;

        int pixels[] = new int[size];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        byte[] data = rgb2YCbCr420(pixels, width, height);
        return data;
    }

    /*
     * 像素数组转化为RGB数组
     */
    public static byte[] convertColorToByte(int color[]) {
        if (color == null) {
            return null;
        }

        byte[] data = new byte[color.length * 3];
        for (int i = 0; i < color.length; i++) {
            data[i * 3] = (byte) (color[i] >> 16 & 0xff);
            data[i * 3 + 1] = (byte) (color[i] >> 8 & 0xff);
            data[i * 3 + 2] = (byte) (color[i] & 0xff);
        }

        return data;

    }

    public static byte[] rgb2YCbCr420(int[] pixels, int width, int height) {
        int len = width * height;
        // yuv格式数组大小，y亮度占len长度，u,v各占len/4长度。
        byte[] yuv = new byte[len * 3 / 2];
        int y, u, v;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                // 屏蔽ARGB的透明度值
                int rgb = pixels[i * width + j] & 0x00FFFFFF;
                // 像素的颜色顺序为bgr，移位运算。
                int r = rgb & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = (rgb >> 16) & 0xFF;
                // 套用公式
                y = ((66 * r + 129 * g + 25 * b + 128) >> 8) + 16;
                u = ((-38 * r - 74 * g + 112 * b + 128) >> 8) + 128;
                v = ((112 * r - 94 * g - 18 * b + 128) >> 8) + 128;
                // rgb2yuv
                // y = (int) (0.299 * r + 0.587 * g + 0.114 * b);
                // u = (int) (-0.147 * r - 0.289 * g + 0.437 * b);
                // v = (int) (0.615 * r - 0.515 * g - 0.1 * b);
                // RGB转换YCbCr
                // y = (int) (0.299 * r + 0.587 * g + 0.114 * b);
                // u = (int) (-0.1687 * r - 0.3313 * g + 0.5 * b + 128);
                // if (u > 255)
                // u = 255;
                // v = (int) (0.5 * r - 0.4187 * g - 0.0813 * b + 128);
                // if (v > 255)
                // v = 255;
                // 调整
                y = y < 16 ? 16 : (y > 255 ? 255 : y);
                u = u < 0 ? 0 : (u > 255 ? 255 : u);
                v = v < 0 ? 0 : (v > 255 ? 255 : v);
                // 赋值
                yuv[i * width + j] = (byte) y;
                try {
                    yuv[len + (i >> 1) * width + (j & ~1) + 0] = (byte) u;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    yuv[len + +(i >> 1) * width + (j & ~1) + 1] = (byte) v;
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        return yuv;
    }

    public static Bitmap mirrorConvert(Bitmap bitmap) {
        Matrix m = new Matrix();
        m.setScale(-1, 1);//水平翻转
        //生成的翻转后的bitmap
        Bitmap reversePic = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
        return reversePic;
    }


    public static Bitmap changeBitmapSize(Bitmap src, int newWidth, int newHeight) {
        int width = src.getWidth();
        int height = src.getHeight();
        LogUtil.i("width", "width:" + width);
        LogUtil.i("height", "height:" + height);
        //计算压缩的比率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        //获取想要缩放的matrix
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        //获取新的bitmap
        Bitmap bitmap = Bitmap.createBitmap(src, 0, 0, width, height, matrix, true);
        LogUtil.i("newWidth", "newWidth" + bitmap.getWidth());
        LogUtil.i("newHeight", "newHeight" + bitmap.getHeight());
        return bitmap;


    }

    private static String generateFileName() {
        return UUID.randomUUID().toString();
    }

    public static void saveBitmap(Bitmap mBitmap, File file) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void saveJPG(byte[] data, File file) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 释放帧动画占用内存
     *
     * @param animationDrawables
     */
    public static void tryRecycleAnimationDrawable(AnimationDrawable animationDrawables) {
        if (animationDrawables != null) {
            animationDrawables.stop();
            for (int i = 0; i < animationDrawables.getNumberOfFrames(); i++) {
                Drawable frame = animationDrawables.getFrame(i);
                if (frame instanceof BitmapDrawable) {
                    ((BitmapDrawable) frame).getBitmap().recycle();
                }
                frame.setCallback(null);
            }
            animationDrawables.setCallback(null);

        }
    }

    public static Bitmap byte2Bitmap(byte[] data) {
        if (data == null) {
            return null;
        }
        return BitmapFactory.decodeByteArray(data, 0, data.length);

    }

    public static byte[] compressBitmap(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    public static byte[] compressBitmap(Bitmap bm, int quality) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        return baos.toByteArray();
    }

    public static Bitmap jpgData2Bitmap(byte[] data) {
        if (data == null) {
            return null;
        }

        return null;

    }

    public interface CreatePhotoCallback {
        void onSuccess(String path);

        void onFault();
    }
}
