package com.webank.ai.util;

import android.os.Environment;
import android.util.Base64;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;


/**
 * creaed by  zhijunchen on 2019/4/26
 */
public class RSAUtils {
    //构建Cipher实例时所传入的的字符串，默认为"RSA/NONE/PKCS1Padding"
    private static String sTransform = "RSA/NONE/PKCS1Padding";

    //进行Base64转码时的flag设置，默认为Base64.DEFAULT
    private static int sBase64Mode = Base64.DEFAULT;

    //初始化方法，设置参数
    public static void init(String transform, int base64Mode) {
        sTransform = transform;
        sBase64Mode = base64Mode;
    }

    //产生密钥对
    public static KeyPair generateRSAKeyPair(int keyLength) {
        KeyPair keyPair = null;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            //设置密钥长度
            keyPairGenerator.initialize(keyLength);
            //产生密钥对
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return keyPair;
    }

    /**
     * 加密或解密数据的通用的方法,srcData:待处理的数据；key:公钥或者私钥，mode指
     * 加密还是解密，值为Cipher.ENCRYPT_MODE或者Cipher.DECRYPT_MODE
     */
    public static byte[] processDAta(byte[] srcData, Key key, int mode) {
        //用来保存处理的结果
        byte[] resultBytes = null;
        //构建Cipher对象，需要传入一个字符串，格式必须为"algorithm/mode/padding"或者"algorithm/",意为"算法/加密模式/填充方式"
        try {
            Cipher cipher = Cipher.getInstance("RSA/NONE/PKCS1Padding");
            //初始化Cipher,mode指定是加密还是解密，key为公钥或密钥
            cipher.init(mode, key);
            //处理数据
            resultBytes = cipher.doFinal(srcData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultBytes;
    }

    //使用公钥加密数据，结果用Base64转码
    public static String encryptDataByPublicKey(byte[] srcData, PublicKey publicKey) {
        byte[] resultBytes = processDAta(srcData, publicKey, Cipher.ENCRYPT_MODE);
        return Base64.encodeToString(resultBytes, sBase64Mode);
    }

    //使用公钥加密数据，结果用Base64转码
    public static String encryptDataByPrivateKey(byte[] srcData, PublicKey publicKey) {
        byte[] resultBytes = processDAta(srcData, publicKey, Cipher.ENCRYPT_MODE);
        return Base64.encodeToString(resultBytes, sBase64Mode);
    }

    public static final String RSA = "RSA";// 非对称加密密钥算法
    public static final String ECB_PKCS1_PADDING = "RSA/ECB/PKCS1Padding";//加密填充方式
    public static final int DEFAULT_KEY_SIZE = 2048;//秘钥默认长度
    public static final byte[] DEFAULT_SPLIT = "#PART#".getBytes();    // 当要加密的内容超过bufferSize，则采用partSplit进行分块加密
    public static final int DEFAULT_BUFFERSIZE = (DEFAULT_KEY_SIZE / 8) - 11;// 当前秘钥支持加密的最大字节数

    /**
     * 用公钥对字符串进行加密
     *
     * @param data 原文
     */
    public static byte[] encryptByPublicKey(byte[] data, byte[] publicKey) throws Exception {
        // 得到公钥
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
        KeyFactory kf = KeyFactory.getInstance(RSA);
        PublicKey keyPublic = kf.generatePublic(keySpec);
        // 加密数据
        Cipher cp = Cipher.getInstance(ECB_PKCS1_PADDING);
        cp.init(Cipher.ENCRYPT_MODE, keyPublic);
        return cp.doFinal(data);
    }

    /**
     * 私钥加密
     *
     * @param data       待加密数据
     * @param privateKey 密钥
     * @return byte[] 加密数据
     */
    public static byte[] encryptByPrivateKey(byte[] data, byte[] privateKey) throws Exception {
        // 得到私钥
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);
        KeyFactory kf = KeyFactory.getInstance(RSA);
        PrivateKey keyPrivate = kf.generatePrivate(keySpec);
        // 数据加密
        Cipher cipher = Cipher.getInstance(ECB_PKCS1_PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, keyPrivate);
        return cipher.doFinal(data);
    }

    /**
     * 私钥加密
     *
     * @param data           待加密数据
     * @param privateKeyData 密钥
     * @return byte[] 加密数据
     */
    public static byte[] signByPrivateKey(byte[] data, byte[] privateKeyData) throws Exception {
        // 得到私钥
        //读取储存的私钥字节数组
        //包装私钥字节数组为一个KeySpec
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyData);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        //通过KeyFactory生成私钥
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        Signature signature = Signature.getInstance("MD5withRSA");//签名的算法
        //通过私钥初始化Signature，签名时用
        signature.initSign(privateKey);
        //指定需要进行签名的内容
        signature.update(data);
        //签名
        byte[] result = signature.sign();
        return result;
    }

    public static boolean testVerifySign(byte[] data,byte[] sign, byte[] publicKeyCode) throws Exception {
        Signature signature = Signature.getInstance("MD5withRSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyCode);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        //以验签的方式初始化Signature
        signature.initVerify(publicKey);
        //指定需要验证的签名
        signature.update(data);
        //进行验签，返回验签结果
        boolean result = signature.verify(sign);
        return result;
    }

    //使用私钥解密，结果用Base64转码
    public static byte[] decryptDataByPrivate(String encryptedData, PrivateKey privateKey) {
        byte[] bytes = Base64.decode(encryptedData, sBase64Mode);
        return processDAta(bytes, privateKey, Cipher.DECRYPT_MODE);
    }

    //使用私钥解密，返回解码数据
    public static String decryptToStrByPrivate(String encryptedData, PrivateKey privateKey) {
        return new String(decryptDataByPrivate(encryptedData, privateKey));
    }

    /**
     * 公钥解密
     *
     * @param data      待解密数据
     * @param publicKey 密钥
     * @return byte[] 解密数据
     */
    public static byte[] decryptByPublicKey(byte[] data, byte[] publicKey) throws Exception {
        // 得到公钥
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
        KeyFactory kf = KeyFactory.getInstance(RSA);
        PublicKey keyPublic = kf.generatePublic(keySpec);
        // 数据解密
        Cipher cipher = Cipher.getInstance(ECB_PKCS1_PADDING);
        cipher.init(Cipher.DECRYPT_MODE, keyPublic);
        return cipher.doFinal(data);
    }
}
