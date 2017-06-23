package com.arlen.frame.common.operation;

import android.os.Environment;
import android.text.TextUtils;

import com.arlen.frame.common.utils.Logger;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProgressHelper {

    private Call<ResponseBody> call;
    private String fileName;
    private ProgressBean progressBean = new ProgressBean();
    private ProgressHandler mProgressHandler;

    public void retrofitDownload(String url) {
        if (TextUtils.isEmpty(url))
            return;
        final String[] strUrl = new String[2];
        if (url.contains("//") && url.split("//").length > 1) {
            strUrl[0] = url.substring(0, url.lastIndexOf("//") + 2);
            strUrl[1] = url.substring(url.lastIndexOf("//") + 2, url.length());
            fileName = strUrl[1];
            //监听下载进度
            Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(strUrl[0]);
            OkHttpClient.Builder builder = addProgress(null);

            ICommonService retrofit = retrofitBuilder
                    .client(builder.build())
                    .build().create(ICommonService.class);

            call = retrofit.retrofitDownload(strUrl[1]);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response != null && response.body() != null) {
                        try {
                            InputStream is = response.body().byteStream();
                            File file = new File(Environment.getExternalStorageDirectory(), fileName);
                            FileOutputStream fos = new FileOutputStream(file);
                            BufferedInputStream bis = new BufferedInputStream(is);
                            byte[] buffer = new byte[1024];
                            int len;
                            while ((len = bis.read(buffer)) != -1) {
                                fos.write(buffer, 0, len);
                                fos.flush();
                            }
                            fos.close();
                            bis.close();
                            is.close();
                            mProgressHandler.downComplete();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        mProgressHandler.downFailure();
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    mProgressHandler.downFailure();
                }
            });
        }
    }

    public File getApkFile() {
        File file = new File(Environment.getExternalStorageDirectory(), fileName);
        return file.exists()?file:null;
    }

    public void cancelDown() {
        if (call != null && !call.isCanceled()) {
            call.cancel();
        }
    }

    public OkHttpClient.Builder addProgress(OkHttpClient.Builder builder) {

        if (builder == null) {
            builder = new OkHttpClient.Builder();
        }

        final ProgressListener progressListener = new ProgressListener() {
            //该方法在子线程中运行
            @Override
            public void onProgress(long progress, long total, boolean done) {
                Logger.d("progress:", String.format("%d%% done\n", (100 * progress) / total));
                if (mProgressHandler == null) {
                    return;
                }
                progressBean.setBytesRead(progress);
                progressBean.setContentLength(total);
                progressBean.setDone(done);
                mProgressHandler.sendMessage(progressBean);
            }
        };

        //添加拦截器，自定义ResponseBody，添加下载进度
        builder.networkInterceptors().add(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                okhttp3.Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder().body(
                        new ProgressResponseBody(originalResponse.body(), progressListener))
                        .build();

            }
        });
        return builder;
    }

    public void setProgressHandler(ProgressHandler progressHandler) {
        mProgressHandler = progressHandler;
    }
}