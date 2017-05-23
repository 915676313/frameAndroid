package com.arlen.frame.common.operation;

/**
 * Created by Arlen on 2017/1/20 17:24.
 */
public abstract class HandleResult<T> {
    public abstract void onSuccess(T t);

    public void onFail(Throwable e) {

    }
}
