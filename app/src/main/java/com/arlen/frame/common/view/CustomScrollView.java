package com.arlen.frame.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by Arlen on 2017/2/22 18:03.
 */
public class CustomScrollView extends ScrollView{

    public interface OnScrollListener{
        void onScroll(int y);
    }

    private OnScrollListener mOnScrollListener;

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnScrollListener(OnScrollListener onScrollListener){
        this.mOnScrollListener = onScrollListener;
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        mOnScrollListener.onScroll(t);
    }
}
