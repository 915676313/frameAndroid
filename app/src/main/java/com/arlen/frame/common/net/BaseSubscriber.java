package com.arlen.frame.common.net;

import com.arlen.frame.R;
import com.arlen.frame.common.model.BaseResult;
import com.arlen.frame.common.utils.NetUtils;
import com.arlen.frame.common.utils.ToastUtils;
import com.arlen.frame.common.view.StatusLayout;
import com.arlen.frame.common.activity.AppContext;
import com.google.gson.JsonSyntaxException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.adapter.rxjava.HttpException;

/**
 * 普通联网刷新界面的Subscriber
 * <p/>
 * Created by Arlen on 2016/6/17.
 */
public class BaseSubscriber<T extends BaseResult> extends BaseSubscriberAbstract<T> {

    /**
     * 没有loading
     */
    public static final int STYLE_NO = 0;
    /**
     * 有loading
     */
    public static final int STYLE_LOADING = 1;
    /**
     * loading覆盖在内容上
     */
    public static final int STYLE_CONTENT_LOADING = 2;

    private StatusLayout statusLayout;
    private int style = STYLE_NO;

    public BaseSubscriber(StatusLayout statusLayout) {
        this.statusLayout = statusLayout;
    }

    public BaseSubscriber(StatusLayout statusLayout, boolean isContent) {
        this.statusLayout = statusLayout;
        style = isContent?STYLE_CONTENT_LOADING:STYLE_LOADING;
    }

    public boolean isContent() {//是否是内容
        return statusLayout.getLayer() == StatusLayout.LAYER_CONTENT
                || statusLayout.getLayer() == StatusLayout.LAYER_CONTENT_LOADING;
    }

    @Override
    public void onStart() {//加载数据前
        if (style == STYLE_LOADING) {
            statusLayout.setViewLayer(StatusLayout.LAYER_LOADING);
        } else if (style == STYLE_CONTENT_LOADING) {
            statusLayout.setViewLayer(StatusLayout.LAYER_CONTENT_LOADING);
        }
    }

    @Override
    public void handleError(Throwable e) {
        if (isContent()) {//retrofit网络未连接报的是UnknownHostException
            if (e instanceof ConnectException || !NetUtils.isConnected(AppContext.getAppContext())) {
                ToastUtils.toastShort(AppContext.getAppContext().getString(R.string.connect_time_out));
            } else if (e instanceof SocketTimeoutException) {
                ToastUtils.toastShort(AppContext.getAppContext().getString(R.string.socket_time_out));
            } else if (e instanceof HttpException) {
                ToastUtils.toastShort(AppContext.getAppContext().getString(R.string.http_time_out));
            } else if (e instanceof UnknownHostException) {
                ToastUtils.toastShort(AppContext.getAppContext().getString(R.string.unknown_time_out));
            } else if (e instanceof JsonSyntaxException) {
                ToastUtils.toastShort(AppContext.getAppContext().getString(R.string.json_time_out));
            } else {
                ToastUtils.toastShort(AppContext.getAppContext().getString(R.string.custom_time_out) + e.getMessage());
            }
            statusLayout.setViewLayer(StatusLayout.LAYER_CONTENT);
        } else {
            if (e instanceof ConnectException || !NetUtils.isConnected(AppContext.getAppContext())) {//网络连接异常
                statusLayout.setViewLayer(StatusLayout.LAYER_NO_NETWORK);
            } else if (e instanceof SocketTimeoutException) {
                statusLayout.setViewLayer(StatusLayout.LAYER_NETWORK_ERROR);
            } else if (e instanceof HttpException || e instanceof UnknownHostException) {
                statusLayout.setViewLayer(StatusLayout.LAYER_NETWORK_ERROR);
            } else if (e instanceof JsonSyntaxException) {
                statusLayout.setViewLayer(StatusLayout.LAYER_OTHER_NETWORK);
            } else {
                statusLayout.setViewLayer(StatusLayout.LAYER_OTHER_NETWORK);
            }
        }
    }

    @Override
    public void handleSuccess(T result) {
        statusLayout.setViewLayer(StatusLayout.LAYER_CONTENT);
    }

    @Override
    public void handleFail(T result) {
        if (isContent()) {
            ToastUtils.toastShort(result.msg);
            statusLayout.setViewLayer(StatusLayout.LAYER_CONTENT);
        } else {
            statusLayout.setViewLayer(StatusLayout.LAYER_OTHER_NETWORK);
        }
    }

    @Override
    public void onFinish() {
    }
}
