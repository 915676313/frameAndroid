package com.arlen.frame.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;


/**
 * 界面层级布局管理器，定义了6个层：{@link #LAYER_LOADING}, {@link #LAYER_CONTENT},
 * {@link #LAYER_EMPTY}, {@link #LAYER_SERVER_ERROR},
 * {@link #LAYER_NETWORK_ERROR}, {@link #LAYER_NO_NETWORK}
 */
public abstract class StatusLayoutAbstract extends FrameLayout {

    public static final int LAYER_LOADING = 1;
    public static final int LAYER_CONTENT = 2;
    public static final int LAYER_CONTENT_LOADING = 3; //loading在内容上面
    public static final int LAYER_EMPTY = 4;
    public static final int LAYER_SERVER_ERROR = 5;
    public static final int LAYER_NETWORK_ERROR = 6;
    public static final int LAYER_NO_NETWORK = 7;
    public static final int LAYER_OTHER_NETWORK = 100;

    private int layer;

    protected View loadingView;
    protected View contentView;
    protected View emptyView;
    protected View serverErrorView;
    protected View networkErrorView;
    protected View noNetworkView;
    protected View otherErrView;

    public StatusLayoutAbstract(Context context) throws IllegalAccessException {
        super(context);
    }

    public StatusLayoutAbstract(Context context, AttributeSet attrs) throws IllegalAccessException {
        super(context, attrs);
    }

//    @Override
//    protected void onFinishInflate() {
//        super.onFinishInflate();
//        try {
//            init();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void init() throws IllegalAccessException {
//        if (getChildCount() == 0) {
//            throw new NullPointerException("ContentLayout子元素不能为空");
//        } else if (getChildCount() > 1) {
//            throw new IllegalAccessException("ContentLayout子元素只能为一个");
//        }
//        contentView = getChildAt(0);
//    }

    public void addContentView(View v) {
        if (getChildCount() > 0) {
            return;
        }
        contentView = v;
        super.addView(v);
    }

    public int getLayer() {
        return layer;
    }

    /**
     * 指定显示某层视图，并隐藏其他层
     */
    public void setViewLayer(int layer) {
        if (this.layer == layer) {
            return;
        }
        this.layer = layer;
        View view = getViewByLayer(layer);
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setVisibility(GONE);
        }
        view.setVisibility(View.VISIBLE);

        if (layer == LAYER_CONTENT_LOADING) {
            if (loadingView == null) {
                loadingView = getLoadingView();
            }
            loadingView.setVisibility(VISIBLE);
        }
    }

    public void showLoadingView() {
        setViewLayer(LAYER_LOADING);
    }

    public void showContentView() {
        setViewLayer(LAYER_CONTENT);
    }

    public View getViewByLayer(int layer) {
        View view = null;
        switch (layer) {
            case LAYER_LOADING:
                view = getLoadingView();
                break;

            case LAYER_CONTENT:
            case LAYER_CONTENT_LOADING:
                view = getContentView();
                break;

            case LAYER_EMPTY:
                view = getEmptyView();
                break;

            case LAYER_SERVER_ERROR:
                view = getServerErrorView();
                break;

            case LAYER_NETWORK_ERROR:
                view = getNetworkErrorView();
                break;

            case LAYER_NO_NETWORK:
                view = getNoNetworkView();
                break;

            case LAYER_OTHER_NETWORK:
                view = getOtherErrorView("未知错误");
                break;
        }
        return view;
    }

    //LoadingView
    public View getLoadingView() {
        if (loadingView == null) {
            loadingView = createLoadingView();
            addView(loadingView);
        }
        return loadingView;
    }

    protected abstract View createLoadingView();

    public void setLoadingView(View loadingView) {
        this.loadingView = loadingView;
    }

    //ContentView
    public View getContentView() {
        return contentView;
    }

    public void setContentView(View contentView) {
        this.contentView = contentView;
    }

    //emptyView
    public View getEmptyView() {
        if (emptyView == null) {
            emptyView = createEmptyView();
            addView(emptyView);
        }
        return emptyView;
    }

    protected abstract View createEmptyView();

    public void seEmptyView(View emptyView) {
        this.emptyView = emptyView;
    }

    //serverErrorView
    public View getServerErrorView() {
        if (serverErrorView == null) {
            serverErrorView = createServerErrorView();
            addView(serverErrorView);
        }
        return serverErrorView;
    }

    protected abstract View createServerErrorView();

    public void setServerErrorView(View serverErrorView) {
        this.serverErrorView = serverErrorView;
    }

    //networkErrorView
    public View getNetworkErrorView() {
        if (networkErrorView == null) {
            networkErrorView = createNetworkErrorView();
            addView(networkErrorView);
        }
        return networkErrorView;
    }

    protected abstract View createNetworkErrorView();

    public void setNetworkErrorView(View networkErrorView) {
        this.networkErrorView = networkErrorView;
    }

    //NoNetworkView
    public View getNoNetworkView() {
        if (noNetworkView == null) {
            noNetworkView = createNoNetworkView();
            addView(noNetworkView);
        }
        return noNetworkView;
    }

    protected abstract View createNoNetworkView();


    public void setNoNetworkView(View noNetworkView) {
        this.noNetworkView = noNetworkView;
    }

    //OtherErrorView
    protected abstract View createOtherErrorView(String err);

    public View getOtherErrorView(String err) {
        if (otherErrView == null) {
            otherErrView = createOtherErrorView(err);
            addView(otherErrView);
        }
        return otherErrView;
    }

}
