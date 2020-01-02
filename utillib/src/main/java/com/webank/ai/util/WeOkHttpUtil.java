package com.webank.ai.util;


import com.webank.mbank.a.g;
import com.webank.mbank.okhttp3.Call;
import com.webank.mbank.okhttp3.Callback;
import com.webank.mbank.okhttp3.MediaType;
import com.webank.mbank.okhttp3.MultipartBody;
import com.webank.mbank.okhttp3.OkHttpClient;
import com.webank.mbank.okhttp3.Request;
import com.webank.mbank.okhttp3.RequestBody;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

/**
 * creaed by  zhijunchen on 2019/3/21
 */
public class WeOkHttpUtil {
    public static final long DEFAULT_READ_TIMEOUT_MILLIS = 100;
    public static final long DEFAULT_WRITE_TIMEOUT_MILLIS = 100;
    public static final long DEFAULT_CONNECT_TIMEOUT_MILLIS = 100;
    private static final long HTTP_RESPONSE_DISK_CACHE_MAX_SIZE = 10 * 1024 * 1024;
    public static final String TAG = "HELEMET_OKHTTP";
    // HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//包含header、body数据
//loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    private static OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
            .readTimeout(DEFAULT_READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
            .writeTimeout(DEFAULT_WRITE_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
            .connectTimeout(DEFAULT_CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
            //FaceBook 网络调试器，可在Chrome调试网络请求，查看SharePreferences,数据库等
            // .addNetworkInterceptor(new StethoInterceptor())
            //http数据log，日志中打印出HTTP请求&响应数据
            //.addInterceptor(loggingInterceptor)
            .build();



    public static String uploadBytes(String mUrl, String index, Callback callback, byte[] mfileData, NameValuePair... params) {
        try {
            //补全请求地址
            MultipartBody.Builder builder = new MultipartBody.Builder();
            //设置类型
            builder.setType(MediaType.parse("multipart/form-data"));
            //追加参数
            for (NameValuePair item : params) {
                builder.addFormDataPart(item.key, item.value);
            }
            builder.addFormDataPart("files", "image_" + index, createProgressRequestBody(MediaType.parse("application/octet-stream"), mfileData));

            //创建RequestBody
            RequestBody body = builder.build();
            //创建Request
            final Request request = new Request.Builder().url(mUrl).post(body).addHeader("Connection", "keep-alive").
                    build();
            //   LogUtil.i(TAG, "time=" + (System.currentTimeMillis()));
        /*    final Call call = mOkHttpClient.newBuilder().
                    connectTimeout(100, TimeUnit.MILLISECONDS).
                    readTimeout(100, TimeUnit.MILLISECONDS).
                    writeTimeout(100, TimeUnit.MILLISECONDS).build().newCall(request);
*/
       //     mOkHttpClient.new
            final Call call = mOkHttpClient.newCall(request);
            try {
                call.enqueue(callback);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private static  int  mSendCounts=0;
    public static String uploadBytesList(String mUrl, Callback callback, List<byte[]> mfileData, NameValuePair... params) {
        try {
            //补全请求地址
            MultipartBody.Builder builder = new MultipartBody.Builder();
            //设置类型
            builder.setType(MediaType.parse("multipart/form-data"));
            //追加参数
            for (NameValuePair item : params) {
                builder.addFormDataPart(item.key, item.value);
            }
            for (int i = 0; i < mfileData.size(); i++) {
                mSendCounts++;
                byte[] data = mfileData.get(i);
                builder.addFormDataPart("files", "image_" + mSendCounts, createProgressRequestBody(MediaType.parse("application/octet-stream"), data));
            }

            //创建RequestBody
            RequestBody body = builder.build();
            //创建Request
            final Request request = new Request.Builder().url(mUrl).post(body).addHeader("Connection", "keep-alive").
                    build();
            //   LogUtil.i(TAG, "time=" + (System.currentTimeMillis()));
            final Call call = mOkHttpClient.newBuilder().
                    connectTimeout(1000, TimeUnit.MILLISECONDS).
                    readTimeout(1000, TimeUnit.MILLISECONDS).
                    writeTimeout(1000, TimeUnit.MILLISECONDS).build().newCall(request);
          //  mOkHttpClient.
//            final Call call = mOkHttpClient.newCall(request);
            try {
                call.enqueue(callback);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static <T> RequestBody createProgressRequestBody(final MediaType contentType, byte[] mfileData) {
        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return contentType;
            }

            @Override
            public long contentLength() {
                return mfileData.length;
            }

            @Override
            public void writeTo(g g) {

            }
/*
            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                Source source;
                try {
                    source = Okio.source(new ByteArrayInputStream(mfileData));
                    Buffer buf = new Buffer();
                    long remaining = contentLength();
                    long current = 0;
                    for (long readCount; (readCount = source.read(buf, 2048)) != -1; ) {
                        sink.write(buf, readCount);
                        current += readCount;
                        //callback  进度通知
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }*/

        };
    }

    public static class NameValuePair {
        public String key;
        public String value;

        public NameValuePair(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }

}

