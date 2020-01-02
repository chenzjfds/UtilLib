package com.webank.ai.util;

import android.text.TextUtils;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * <pre>
 *     author : v_wizardchen
 *     e-mail : v_wizardchen@webank.com
 *     time   : 2018/08/09
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class EncryptUtil {
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    public static String generateNonce() {
        String string = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 32; i++) {
            int num = random.nextInt(62);
            builder.append(string.charAt(num));
        }
        return builder.toString();
    }

    public static byte[] base64ToBytes(String string) {
        string = string.replaceAll(" ", "+");
        return Base64.decode(string, Base64.DEFAULT);
    }

    /**
     * 使用 HMAC-SHA1 签名方法对data进行签名
     *
     * @param data 被签名的字符串
     * @param key  密钥
     * @return 加密后的字符串
     */
    public static String genHMAC(String data, String key) {
        try {
            //根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
            SecretKeySpec signinKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);
            //生成一个指定 Mac 算法 的 Mac 对象
            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            //用给定密钥初始化 Mac 对象
            mac.init(signinKey);
            //完成 Mac 操作
            byte[] rawHmac = mac.doFinal(data.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : rawHmac) {
                sb.append(byteToHexString(b));
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            System.err.println(e.getMessage());
        } catch (InvalidKeyException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    private static String byteToHexString(byte ib) {
        char[] Digit = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
        };
        char[] ob = new char[2];
        ob[0] = Digit[(ib >>> 4) & 0X0f];
        ob[1] = Digit[ib & 0X0F];
        String s = new String(ob);
        return s;
    }


    /**
     * 将字符串中的中文进行编码
     * :/
     *
     * @param s
     * @return 返回字符串中汉字编码后的字符串
     */
    public static String cnToEncode(String s) {
        char[] ch = s.toCharArray();
        String result = "";
        for (int i = 0; i < ch.length; i++) {
            char temp = ch[i];

            if (check(temp)) {
                try {
                    String encode = URLEncoder.encode(String.valueOf(temp), "utf-8");
                    result = result + encode;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
                result = result + temp;
            }
        }
        return result.replace(" ", "%20");
    }

    private static boolean check(char src) {
        char a = '/';
        char b = ':';
        char c = ' ';

        if (src == a || src == b || src == c) {
            return false;
        }
        return true;

    }
    /*public static String cnToEncode(String s) {
        char[] ch = s.toCharArray();
        String result = "";
        for (int i = 0; i < ch.length; i++) {
            char temp = ch[i];

            if (isChinese(temp)||Character.isSpaceChar(temp)) {
                try {
                    String encode = URLEncoder.encode(String.valueOf(temp), "utf-8");
                    result = result + encode;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
                result = result + temp;
            }
        }
        return result;
    }*/

    /**
     * 判断字符是否为汉字
     *
     * @param c
     * @return
     */
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }

    public static String identityNoEncrypt(String identityNo) {
        if (TextUtils.isEmpty(identityNo)) {
            return "";
        }
        if (identityNo.length() < 10) {
            return identityNo;
        }
        StringBuilder strBuilder = new StringBuilder(identityNo);
        strBuilder.replace(6, identityNo.length() - 4, "******");
        String result = strBuilder.toString();
        return result;
    }
}
