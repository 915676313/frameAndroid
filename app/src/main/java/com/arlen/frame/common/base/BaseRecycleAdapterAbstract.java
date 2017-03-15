package com.arlen.frame.common.base;

/**
 * Created by Arlen on 2016/12/29 17:26.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.arlen.frame.R;


public abstract class BaseRecycleAdapterAbstract extends RecyclerView.Adapter {

    public Context mContext;

    final static int T_header = -10;
    final static int T_footer = -20;
    final static int T_normal = 0;

    private RvItemInterface mRvHeaderInterface;
    private RvItemInterface mRvFooterInterface;
    private boolean mHasHeader;
    private boolean mHasFooter;

    public BaseRecycleAdapterAbstract(Context context) {
        this.mContext = context;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mHasHeader = mRvHeaderInterface != null;
        mHasFooter = mRvFooterInterface != null;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout;
        if (viewType == T_header) {
            layout = mRvHeaderInterface.getLayout();
        } else if (viewType == T_footer) {
            layout = mRvFooterInterface.getLayout();
        } else {
            layout = getNormalLayout(viewType);
        }
        return new BaseViewHolder(LayoutInflater.from(mContext).inflate(layout, parent, false));
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        BaseViewHolder holder = (BaseViewHolder) viewHolder;
        holder.setViewType(getItemViewType(position));
        int viewType = getItemViewType(position);
        if (viewType == T_header) {
            mRvHeaderInterface.onBind(holder);
        } else if (viewType == T_footer) {
            mRvFooterInterface.onBind(holder);
        } else {
            int pos = mHasHeader ? position - 1 : position;
            holder.itemView.setTag(R.id.tag1, pos);
            onBindNormal(holder, pos);
        }
    }

    @Override
    public int getItemCount() {
        int size = getListSize();
        if (mHasHeader && mHasFooter) {
            return 2 + size;
        } else if (mHasHeader || mHasFooter) {
            return 1 + size;
        } else {
            return size;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mHasHeader && position == 0) {
            return T_header;
        } else if (mHasFooter && position == (getItemCount() - 1)) {
            return T_footer;
        } else {
            return getNormalItemType(mHasHeader ? position - 1 : position);
        }
    }

    public abstract int getListSize();

    /**
     * @param normalPos 相对于normal的position
     * @return
     */
    public int getNormalItemType(int normalPos) {
        return T_normal;
    }

    public abstract int getNormalLayout(int viewType);

    /**
     * @param holder
     * @param normalPos 相对于normal的position
     */
    public abstract void onBindNormal(BaseViewHolder holder, int normalPos);

    public BaseRecycleAdapterAbstract setHeader(RvItemInterface i) {
        this.mRvHeaderInterface = i;
        return this;
    }

    public BaseRecycleAdapterAbstract setFooter(RvItemInterface i) {
        this.mRvFooterInterface = i;
        return this;
    }

    /**
     * RecycleView接口
     */
    public interface RvItemInterface {
        /**
         * 获取布局
         *
         * @return
         */
        int getLayout();

        /**
         * 绑定数据
         *
         * @param holder
         */
        void onBind(BaseViewHolder holder);
    }

}