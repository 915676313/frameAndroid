package com.arlen.frame.common.base;

/**
 * Created by Arlen on 2016/12/29 17:26.
 *
 * 使用:
 *
 * 1.实现getNormalLayout(int viewType)和onBindNormalHolder
 * 2.如果有多样式重写getNormalItemType(int normalPos)
 *
 */

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arlen.frame.R;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecycleAdapter<T> extends BaseRecycleAdapterAbstract {

    public boolean mShowFooter;
    private boolean mIsEnd;
    private List<T> mDataList;

    private BaseRecycleAdapter.OnItemClickListener<T> listener;
    private BaseRecycleAdapter.OnItemLongClickListener<T> onItemLongClickListener;

    public BaseRecycleAdapter(Context context) {
        super(context);
        init();
    }

    public BaseRecycleAdapter(Context context, boolean showFooter) {
        super(context);
        this.mShowFooter = showFooter;
        init();
    }

    private void init() {
        this.mDataList = new ArrayList();
        //设置Footer隐藏, 调用BaseRecycleAdapter(Context context, boolean showFooter)
        if (mShowFooter) {
            setFooter(new RvItemInterface() {
                @Override
                public int getLayout() {
                    return R.layout.base_foot_loading;
                }

                @Override
                public void onBind(BaseViewHolder holder) {
                    TextView footer_text = holder.findViewById(R.id.footer_text);
                    ProgressBar footer_img = holder.findViewById(R.id.footer_progressBar);
                    if (mDataList.size() == 0) {
                        footer_text.setVisibility(View.GONE);
                        footer_img.setVisibility(View.GONE);
                    } else {
                        if (mIsEnd) {
                            footer_text.setVisibility(View.VISIBLE);
                            footer_img.setVisibility(View.GONE);
                        } else {
                            footer_text.setVisibility(View.GONE);
                            footer_img.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public int getListSize() {
        return mDataList.size();
    }

    @Override
    public int getNormalItemType(int normalPos) {
        return super.getNormalItemType(normalPos);
    }

    @Override
    public void onBindNormal(final BaseViewHolder holder, final int normalPos) {
        holder.itemView.setTag(Integer.valueOf(normalPos));
        final T item = this.mDataList.get(normalPos);

        if (listener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    listener.onItemClick(holder.itemView, normalPos, item);
                }
            });
        }

        if (onItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                public boolean onLongClick(View v) {
                    onItemLongClickListener.onItemLongClick(holder.itemView, normalPos, item);
                    return true;
                }
            });
        }

        onBindNormalHolder(holder, item, normalPos);
    }

    //父类getNormalLayout(int viewType), 没显示
    public abstract void onBindNormalHolder(BaseViewHolder holder, T item, int position);

    public T getItem(int position) {
        return position >= this.mDataList.size() ? null : this.mDataList.get(position);
    }

    public void setIsEnd(boolean isEnd) {
        if (this.mIsEnd == isEnd) {
            return;
        }
        this.mIsEnd = isEnd;
        notifyDataSetChanged();
    }

    public boolean isEnd() {
        return mIsEnd;
    }

    public void add(T elem) {
        this.mDataList.add(elem);
        this.notifyDataSetChanged();
    }

    public void addAll(List<T> elem) {
        this.mDataList.addAll(elem);
        this.notifyDataSetChanged();
    }

    public List<T> getItemList() {
        return mDataList;
    }

    public void set(List<T> list) {
        this.mDataList = list;
        notifyDataSetChanged();
    }

    public void set(T oldElem, T newElem) {
        this.set(this.mDataList.indexOf(oldElem), newElem);
    }

    public void set(int index, T elem) {
        this.mDataList.set(index, elem);
        this.notifyDataSetChanged();
    }

    public void remove(T elem) {
        this.mDataList.remove(elem);
        this.notifyDataSetChanged();
    }

    public void remove(int index) {
        this.mDataList.remove(index);
        this.notifyDataSetChanged();
    }

    public boolean contains(T elem) {
        return this.mDataList.contains(elem);
    }

    public void clear() {
        this.mDataList.clear();
        this.notifyDataSetChanged();
    }

    @Override
    public BaseRecycleAdapter setFooter(RvItemInterface i) {
        super.setFooter(i);
        return this;
    }

    @Override
    public BaseRecycleAdapter setHeader(RvItemInterface i) {
        super.setHeader(i);
        return this;
    }

    public OnItemClickListener geDataListener() {
        return listener;
    }

    public BaseRecycleAdapter setOnItemClickListener(BaseRecycleAdapter.OnItemClickListener<T> listener) {
        this.listener = listener;
        return this;
    }

    public BaseRecycleAdapter setOnItemLongClickListener(BaseRecycleAdapter.OnItemLongClickListener<T> onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
        return this;
    }

    public interface OnItemLongClickListener<T> {
        void onItemLongClick(View view, int position, T item);
    }

    public interface OnItemClickListener<T> {
        void onItemClick(View view, int position, T item);
    }
}