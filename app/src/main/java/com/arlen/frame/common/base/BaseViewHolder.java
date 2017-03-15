package com.arlen.frame.common.base;

/**
 * Created by Arlen on 2016/12/29 17:23.
 */

import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arlen.frame.common.utils.ImageUtil;


/**
 * Created by Arlen on 2016/7/18 15:40.
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> sparseArray = new SparseArray();
    private int viewType;

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public <T extends View> T findViewById(int viewId) {
        View view = this.sparseArray.get(viewId);
        if(view == null) {
            view = this.itemView.findViewById(viewId);
            this.sparseArray.put(viewId, view);
        }
        return (T) view;
    }

    public BaseViewHolder setText(int viewId, CharSequence value) {
        TextView view = findViewById(viewId);
        view.setText(value);
        return this;
    }

    public BaseViewHolder setText(int viewId, CharSequence value, int colorRes) {
        TextView view = findViewById(viewId);
        view.setText(value);
        view.setTextColor(view.getResources().getColor(colorRes));
        return this;
    }

    public BaseViewHolder setColorText(int viewId, SpannableStringBuilder value) {
        TextView view = findViewById(viewId);
        view.setText(value);
        return this;
    }

    public BaseViewHolder setTextColor(int viewId, int colorRes) {
        TextView view = findViewById(viewId);
        view.setTextColor(view.getResources().getColor(colorRes));
        return this;
    }

    public void setTextStrike(int viewId) {
        TextView view = findViewById(viewId);
        view.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); // 设置中划线并加清晰
    }


    public BaseViewHolder setAlpha(int viewId, float value) {
        View view = findViewById(viewId);
        view.setAlpha(value);
        return this;
    }

    public BaseViewHolder setBackground(int viewId, int resId) {
        View view = findViewById(viewId);
        view.setBackgroundResource(resId);
        return this;
    }

    public BaseViewHolder setImageView(int viewId, String uri) {
        ImageView view = findViewById(viewId);
        if (!TextUtils.isEmpty(uri)) {
            ImageUtil.load(view.getContext(), uri, view, 0);
        }
        return this;
    }

    public BaseViewHolder setImageRoundView(int viewId, int res) {
        ImageView view = findViewById(viewId);
        ImageUtil.showRoundImageByRes(view.getContext(), view, res, 0);
        return this;
    }

    public BaseViewHolder setRountImageView(int viewId, String uri) {
        ImageView view = findViewById(viewId);
        if (!TextUtils.isEmpty(uri)) {
            ImageUtil.showRoundImageByUrl(view.getContext(), view, uri, 0);
        }
        return this;
    }

    public BaseViewHolder setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = this.findViewById(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    public BaseViewHolder setOnTouchListener(int viewId, View.OnTouchListener listener) {
        View view = this.findViewById(viewId);
        view.setOnTouchListener(listener);
        return this;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
