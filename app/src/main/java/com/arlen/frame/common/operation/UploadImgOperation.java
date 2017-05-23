package com.arlen.frame.common.operation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import com.arlen.frame.common.model.BaseResult;
import com.arlen.frame.common.net.BasePresenterSubscriber;
import com.arlen.frame.common.net.HttpProvider;
import com.arlen.frame.common.utils.DensityUtils;
import com.arlen.frame.common.utils.FileSizeUtil;
import com.arlen.frame.common.utils.ToastUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Arlen on 2017/1/20 17:05.
 */
public class UploadImgOperation<T> {

    private Activity mActivity;
    private HandleResult<T> mHandleResult;
    private boolean loading;

    public UploadImgOperation(Activity context) {
        mActivity = context;
    }

    public UploadImgOperation(HandleResult<T> handleResult) {
        this.mHandleResult = handleResult;
    }

    public UploadImgOperation(Activity context, HandleResult<T> handleResult) {
        mActivity = context;
        this.mHandleResult = handleResult;
    }


    public void uploadImage(final Uri uri) {
        if (uri == null)
            return;
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                loading = true;
                File file = new File(FileSizeUtil.getImageAbsolutePath(mActivity, uri));
                BitmapFactory.Options options = FileSizeUtil.getBitmapOptions(file.getPath());
                int screenMax = Math.max(DensityUtils.getWindowWidth(mActivity),
                        DensityUtils.getWindowHeight(mActivity));
                int imgMax = Math.max(options.outWidth, options.outHeight);
                int inSimpleSize = 1;
                if (screenMax <= imgMax) {
                    inSimpleSize = Math.max(screenMax, imgMax) / Math.min(screenMax, imgMax);
                }
                return FileSizeUtil.compressBitmap(mActivity,
                        file.getAbsolutePath(),
                        Bitmap.CompressFormat.JPEG,
                        options.outWidth / inSimpleSize,
                        options.outHeight / inSimpleSize,
                        false);
            }

            @Override
            protected void onPostExecute(final String fileName) {
                final File imageBytes = new File(fileName);
                Map<String, RequestBody> map = new HashMap<>();
                if (imageBytes != null) {
                    RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), imageBytes);
                    map.put("uploadedFile\"; filename=\"" + imageBytes.getName() + "", requestBody);
                }
                setObservable(HttpProvider.getInstance().create(ICommonService.class).uploadImage(map),
                        new BasePresenterSubscriber<BaseResult<String>>() {

                            @Override
                            public void onNext(BaseResult<String> result) {
                                super.onNext(result);
                                if (result.isSuccess()) {
                                    if (mHandleResult != null) {
                                        mHandleResult.onSuccess((T) result.data);
                                    }
                                }
                            }

                            @Override
                            public void onCompleted() {
                                super.onCompleted();
                                loading = false;
                            }

                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                mHandleResult.onFail(e);
                                loading = false;
                            }
                        });
            }
        }.execute();
    }

    public void saveImage(final Context context, String imgUrl, final String path) {
        if (loading)
            return;
        loading = true;
        HttpProvider.getInstance().create(ICommonService.class).downloadPicFromNet(imgUrl)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                        loading = false;
                        if (mHandleResult != null) {
                            mHandleResult.onSuccess(null);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mHandleResult == null) {
                            ToastUtils.toastShort("保存失败!");
                        }
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        if (responseBody != null) {
                            if (writeResponseBodyToDisk(context, path, responseBody)) {
                                if (mHandleResult == null) {
                                    ToastUtils.toastShort("保存成功");
                                }
                            } else {
                                if (mHandleResult == null) {
                                    ToastUtils.toastShort("内存卡空间不足!");
                                }
                            }
                        } else {
                            if (mHandleResult == null) {
                                ToastUtils.toastShort("保存失败");
                            }
                        }
                    }
                });
    }

    private boolean writeResponseBodyToDisk(Context context, String path, ResponseBody body) {
        try {
            File futureStudioIconFile = new File(path);
            if (futureStudioIconFile.exists()) {
                futureStudioIconFile.delete();
            }
            InputStream inputStream = null;
            FileOutputStream outputStream = null;
            try {
                byte[] fileReader = new byte[4096];
                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);
                while (fileSizeDownloaded < fileSize) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                }
                outputStream.flush();
                try {
                    MediaStore.Images.Media.insertImage(context.getContentResolver(),
                            futureStudioIconFile.getAbsolutePath(), futureStudioIconFile.getName(), null);//插入图库
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));//发送广播通知图库更新
                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    public Subscription setObservable(Observable observable, Subscription callback) {
        return observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new BasePresenterSubscriber());
    }

    public boolean isLoading() {
        return loading;
    }
}
