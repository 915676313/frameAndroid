package com.arlen.frame.common.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arlen.frame.R;
import com.arlen.frame.common.view.StatusLayout;

import butterknife.ButterKnife;

/**
 * Created by Arlen on 2017/1/11.
 */
public class BaseFragment extends Fragment {

    private StatusLayout mStatusLayout;
    private View mBaseView;
    private View mContentView;

    public void start(Context context, Class<? extends BaseActivity> activity) {
        context.startActivity(new Intent(context, activity));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mBaseView == null) {
            throw new NullPointerException("BaseFragment视图为空, setContentView(...)要在onCreateView(...)中调用");
        }
        return mBaseView;
    }

    public void setContentView(@LayoutRes int contentLayout) {
        mBaseView = View.inflate(getContext(), R.layout.base_view_activity, null);
        mStatusLayout = (StatusLayout) mBaseView.findViewById(R.id.sl_base);
        mContentView = View.inflate(getContext(), contentLayout, null);
        addContentView(mContentView);
    }

    public void addContentView(View contentView) {
        mStatusLayout.addContentView(contentView);
    }

    public View getBaseView() {
        return mBaseView;
    }

    public StatusLayout getStatusView() {
        return mStatusLayout;
    }

    public View getContentView() {
        return mContentView;
    }

}
