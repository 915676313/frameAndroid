package com.arlen.frame.common.base;

import android.os.Bundle;

import com.arlen.frame.common.view.StatusLayout;


/**
 * Created by Arlen on 2017/1/10.
 */
public abstract class BasePresenterActivity <V, T extends IBasePresenter<V>> extends BaseActivity implements IBaseView {

    public T mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = initPresenter();
        mPresenter.attach((V)this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detach();
    }

    public T getPresenter(){
        return mPresenter;
    }

    public abstract T initPresenter();

    @Override
    public void showDataView() {
        getStatusView().setViewLayer(StatusLayout.LAYER_CONTENT);
    }

    @Override
    public void showLoadingView(boolean isContent) {
        getStatusView().setViewLayer(isContent?StatusLayout.LAYER_CONTENT_LOADING:StatusLayout.LAYER_LOADING);
    }

    @Override
    public void showErrorView() {
        getStatusView().setViewLayer(StatusLayout.LAYER_NETWORK_ERROR);
    }

    @Override
    public void showEmptyView() {
        getStatusView().setViewLayer(StatusLayout.LAYER_EMPTY);
    }
}

