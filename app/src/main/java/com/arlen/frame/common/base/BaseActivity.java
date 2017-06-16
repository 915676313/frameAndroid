package com.arlen.frame.common.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.arlen.frame.R;
import com.arlen.frame.common.view.NavBarHeader;
import com.arlen.frame.common.view.StatusLayout;

import butterknife.ButterKnife;

/**
 * Created by Arlen on 2017/1/10.
 */
public abstract class BaseActivity extends AppCompatActivity implements IEmptyTypeView {

    private NavBarHeader mNavBarHeader;
    private StatusLayout mStatusLayout;
    private View mContentView;
    private View mBaseView;

    public static void start(Context context, Class<? extends BaseActivity> activity) {
        context.startActivity(new Intent(context, activity));
    }

    public Activity getContext() {//方便内部内使用
        return this;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);//默认带有EditText的界面不弹出键盘
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        ButterKnife.bind(this);
    }

    public void setCommonContentView(@LayoutRes int contentLayout) {
        super.setContentView(contentLayout);
        ButterKnife.bind(this);
    }

    public void setNoHeaderContentView(@LayoutRes int contentLayout) {
        mBaseView = View.inflate(this, R.layout.base_view_activity, null);
        mStatusLayout = (StatusLayout) mBaseView.findViewById(R.id.sl_base);
        mContentView = View.inflate(this, contentLayout, null);
        addContentView(mContentView);
        setContentView(mBaseView);
    }

    public void setBaseAndContentView(@LayoutRes int contentLayout) {
        mBaseView = View.inflate(this, R.layout.base_activity, null);
        mStatusLayout = (StatusLayout) mBaseView.findViewById(R.id.sl_base);
        mNavBarHeader = (NavBarHeader) mBaseView.findViewById(R.id.nav_bar_header);
        mContentView = View.inflate(this, contentLayout, null);
        mNavBarHeader.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        addContentView(mContentView);
        setContentView(mBaseView);
    }

    public void addContentView(View contentView) {
        mStatusLayout.addContentView(contentView);
    }

    public View getBaseView() {
        return mBaseView;
    }

    public NavBarHeader getTitleBar() {
        return mNavBarHeader;
    }

    public StatusLayout getStatusView() {
        return mStatusLayout;
    }

    public View getContentView() {
        return mContentView;
    }

    /**
     * 多种隐藏软件盘方法的其中一种
     *
     * @param token
     */
    public void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();

            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，
     * 来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    public void setEmptyType(EmptyType emptyType){
        if(mStatusLayout != null){
            mStatusLayout.setTipsText(StatusLayout.LAYER_EMPTY,emptyType.getContentRes());
            mStatusLayout.setTipsIcon(StatusLayout.LAYER_EMPTY,emptyType.getImgResId());
        }
    }

    @Override
    public EmptyType getEmptyType() {
        return EmptyType.NONE;
    }
}
