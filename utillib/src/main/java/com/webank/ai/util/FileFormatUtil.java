package com.webank.ai.util;

import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * <pre>
 *     author : v_wizardchen
 *     e-mail : v_wizardchen@webank.com
 *     time   : 2018/07/11
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class FileFormatUtil {

    public static final String FILE_TYPE_MAP = "地图";
    public static final String FILE_TYPE_FACE = "人脸";
    public static final String FILE_TYPE_VIDEO = "视频";
    public static final String FILE_TYPE_AUDIO = "音频";
    public static final String FILE_TYPE_CONFIG_FILE = "配置文件";
    public static final String FILE_TYPE_UI_IMAGE = "图片";



    public static boolean isHttpSource(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        if (url.startsWith("http")) {
            return true;
        }
        return false;
    }

    public static String File2Str(File file) {
        InputStream in = null;
        String result = null;
        try {
            in = new FileInputStream(file);
            int length = in.available();
            byte[] buffer = new byte[length];
            in.read(buffer);

            result = new String(buffer, "utf8");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static String File2Str(String file) {
        InputStream in = null;
        String result = null;
        try {
            in = new FileInputStream(file);
            int length = in.available();
            byte[] buffer = new byte[length];
            in.read(buffer);

            result = new String(buffer, "utf8");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 删除单个文件
     *
     * @param filePath 被删除文件的文件名
     * @return 文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }

    public static boolean deleteDirectory(String filePath) {
        boolean flag = false;
        // 如果filePath不以文件分隔符结尾，自动添加文件分隔符
        if (!filePath.endsWith(File.separator)) {
            filePath = filePath + File.separator;
        }
        File dirFile = new File(filePath);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        File[] files = dirFile.listFiles();
        // 遍历删除文件夹下的所有文件(包括子目录)
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                // 删除子文件
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            } else {
                // 删除子目录
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag)
            return false;
        // 删除当前空目录
        return dirFile.delete();
    }

    public static void createFle(String path) {
        File file = new File(path);
        if (!file.exists()) {
            if (!file.mkdir()) {
            }
        }
    }

    public static final boolean isExist(String path) {
        File file = new File(path);
        return file.exists();
    }

    public static boolean deleteDirectory(File dir) {
        if (!dir.exists()) return false;
        if (dir.isDirectory()) {
            String[] childrens = dir.list();
            // 递归删除目录中的子目录下
            for (String child : childrens) {
                boolean success = deleteDirectory(new File(dir, child));
                if (!success) return false;
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    public static void str2File(String path, String content) {
        File file = new File(path);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file));
            out.write(content);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static final void copyFile(String sourcefile, String targetFile) {
        File sFile = new File(sourcefile);
        File tFile = new File(targetFile);
        if (tFile.exists()) {
            return;
        }
        try {
            InputStream input = new FileInputStream(sFile);
            OutputStream output = new FileOutputStream(tFile);
            byte buffer[] = new byte[4 * 1024];
            int count = 0;
            while ((count = input.read(buffer)) != -1) {
                output.write(buffer, 0, count);
            }
            // 清除缓存
            output.flush();
            output.close();
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean facePhotoTypeCheck(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return false;
        }
        return fileName.endsWith("jpg")
                || fileName.endsWith("png")
                || fileName.endsWith("PNG")
                || fileName.endsWith("jpeg")
                || fileName.endsWith("JPG");
    }


}