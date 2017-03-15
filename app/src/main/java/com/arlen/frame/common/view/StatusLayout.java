package com.arlen.frame.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.arlen.frame.R;
import com.arlen.frame.common.utils.ImageUtil;


public class StatusLayout extends StatusLayoutAbstract {

    public StatusLayout(Context context, AttributeSet attrs) throws IllegalAccessException {
        super(context, attrs);
    }

    public StatusLayout(Context context) throws IllegalAccessException {
        super(context);
    }

    protected OnClickListener onReloadListener;

    public void setOnReloadListener(OnClickListener onReloadListener) {
        this.onReloadListener = onReloadListener;
    }

    @Override
    protected View createLoadingView() {
        View loadingView = inflate(getContext(), R.layout.base_status_layout, null);
        ImageView imageView = (ImageView) loadingView.findViewById(R.id.tips_icon);
        ImageUtil.loadGif(loadingView.getContext(), R.mipmap.loading, imageView);
        TextView textView = (TextView) loadingView.findViewById(R.id.tips_text);
        textView.setText(getContext().getString(R.string.loading));
        loadingView.findViewById(R.id.tips_btn).setVisibility(View.GONE);
        return loadingView;
    }

    @Override
    protected View createEmptyView() {
        View emptyView = inflate(getContext(), R.layout.base_status_layout, null);
        ImageView imageView = (ImageView) emptyView.findViewById(R.id.tips_icon);
        imageView.setImageResource(R.mipmap.state_not_open);
        TextView textView = (TextView) emptyView.findViewById(R.id.tips_text);
        textView.setText(getContext().getString(R.string.empty_text));
        Button button = (Button) emptyView.findViewById(R.id.tips_btn);
        button.setText(getContext().getString(R.string.reload_text));
        button.setOnClickListener(onReloadListener);
        return emptyView;
    }

    @Override
    protected View createOtherErrorView(String err) {
        View serverOtherView = inflate(getContext(), R.layout.base_status_layout, null);
        ImageView imageView = (ImageView) serverOtherView.findViewById(R.id.tips_icon);
        imageView.setImageResource(R.mipmap.state_err_net);
        TextView textView = (TextView) serverOtherView.findViewById(R.id.tips_text);
        textView.setText(err);
        Button button = (Button) serverOtherView.findViewById(R.id.tips_btn);
        button.setText(getContext().getString(R.string.reload_text));
        button.setOnClickListener(onReloadListener);
        return serverOtherView;
    }

    @Override
    protected View createServerErrorView() {
        View serverErrorView = inflate(getContext(), R.layout.base_status_layout, null);
        ImageView imageView = (ImageView) serverErrorView.findViewById(R.id.tips_icon);
        imageView.setImageResource(R.mipmap.state_err_net);
        TextView textView = (TextView) serverErrorView.findViewById(R.id.tips_text);
        textView.setText(getContext().getString(R.string.http_time_out));
        Button button = (Button) serverErrorView.findViewById(R.id.tips_btn);
        button.setText(getContext().getString(R.string.reload_text));
        button.setOnClickListener(onReloadListener);
        return serverErrorView;
    }

    @Override
    protected View createNetworkErrorView() {
        View networkErrorView = inflate(getContext(), R.layout.base_status_layout, null);
        ImageView imageView = (ImageView) networkErrorView.findViewById(R.id.tips_icon);
        imageView.setImageResource(R.mipmap.state_err_net);
        TextView textView = (TextView) networkErrorView.findViewById(R.id.tips_text);
        textView.setText(getContext().getString(R.string.http_time_out));
        Button button = (Button) networkErrorView.findViewById(R.id.tips_btn);
        button.setText(getContext().getString(R.string.reload_text));
        button.setOnClickListener(onReloadListener);
        return networkErrorView;
    }

    @Override
    protected View createNoNetworkView() {
        View view = inflate(getContext(), R.layout.base_status_layout, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.tips_icon);
        imageView.setImageResource(R.mipmap.state_no_net);
        TextView textView = (TextView) view.findViewById(R.id.tips_text);
        textView.setText(getContext().getString(R.string.no_network));
        Button button = (Button) view.findViewById(R.id.tips_btn);
        button.setText(getContext().getString(R.string.reload_text));
        button.setOnClickListener(onReloadListener);
        return view;
    }

    public void setTipsIcon(int layer, int iconRes) {
        ImageView tips_icon = (ImageView) getViewByLayer(layer).findViewById(R.id.tips_icon);
        tips_icon.setImageResource(iconRes);
    }

    public void setTipsText(int layer, String hint) {
        TextView tips_text = (TextView) getViewByLayer(layer).findViewById(R.id.tips_text);
        tips_text.setText(hint);
    }

}
