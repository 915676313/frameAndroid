package com.arlen.frame.common.net;


import com.arlen.frame.BuildConfig;
import com.arlen.frame.common.model.BaseResult;
import com.arlen.frame.common.utils.Logger;

import rx.Subscriber;

/**
 * Created by Arlen on 2017/1/11.
 */
public abstract class BaseSubscriberAbstract<T extends BaseResult> extends Subscriber<T> {

    @Override
    public void onCompleted() {
        onFinish();
    }

    @Override
    public void onError(Throwable e) {
        Logger.info("onError e = " + e);
        handleError(e);
        if (BuildConfig.DEBUG) {
            e.printStackTrace();
        }
        onFinish();
    }

    /**
     * 刷新时
     * 1.如果请求成功就可以改变布局
     * 2.否者, 不能改变布局,只进行toast提示
     */

    @Override
    public void onNext(T result) {
        Logger.info("onNext result = " + result);
        if (result != null && result.isSuccess()) {
            handleSuccess(result);
        } else {
            handleFail(result);
        }
    }

    public abstract void handleError(Throwable e);

    public abstract void handleSuccess(T result);

    public abstract void handleFail(T result);

    public abstract void onFinish();
}