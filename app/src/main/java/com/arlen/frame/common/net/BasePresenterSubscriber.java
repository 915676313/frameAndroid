package com.arlen.frame.common.net;

import com.arlen.frame.R;
import com.arlen.frame.common.AppContext;
import com.arlen.frame.common.base.IBaseView;
import com.arlen.frame.common.model.BaseResult;
import com.arlen.frame.common.utils.NetUtils;
import com.arlen.frame.common.utils.ToastUtils;
import com.google.gson.JsonSyntaxException;

import java.lang.ref.WeakReference;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import retrofit2.adapter.rxjava.HttpException;

/**
 * 普通联网刷新界面的Subscriber
 * <p/>
 * Created by Arlen on 2016/6/17.
 */
public class BasePresenterSubscriber<T extends BaseResult> extends BaseSubscriberAbstract<T> {

    private boolean isContent;
    private WeakReference<IBaseView> baseView;

    public BasePresenterSubscriber() {
    }

    public BasePresenterSubscriber(IBaseView baseView) {
        this.baseView = new WeakReference<>(baseView);
    }

    public BasePresenterSubscriber(IBaseView baseView, boolean isContent) {
        this.baseView = new WeakReference<>(baseView);
        this.isContent = isContent;
    }

    @Override
    public void onStart() {//加载数据前
        if (baseView != null) {
            baseView.get().showLoadingView(isContent);
        }
    }

    @Override
    public void handleError(Throwable e) {
        if (isContent) {//retrofit网络未连接报的是UnknownHostException
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
            if (baseView != null) {
                baseView.get().showDataView();
            }
        } else {
            if (baseView != null) {
                baseView.get().showErrorView();
            }
        }
    }

    @Override
    public void handleSuccess(T result) {
        if(isContent) {
            baseView.get().showDataView();
        }else{
            if(result.data instanceof List){
                if(result.data != null && ((List) result.data).size()>0){
                    baseView.get().showDataView();
                }else{
                    baseView.get().showEmptyView();
                }
            }else{
                baseView.get().showDataView();
            }
        }
    }

    @Override
    public void handleFail(T result) {
        if (isContent) {
            ToastUtils.toastShort(result.msg);
            if (baseView != null) {
                baseView.get().showDataView();
            }
        } else {
            if (baseView != null) {
                baseView.get().showEmptyView();
            }
        }
    }

    @Override
    public void onFinish() {
    }
}
