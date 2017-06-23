package com.arlen.frame.view.account.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arlen.frame.R;
import com.arlen.frame.common.operation.DownloadProgressHandler;
import com.arlen.frame.common.operation.ProgressHelper;
import com.arlen.frame.common.operation.UpdateService;
import com.arlen.frame.common.utils.ActivityManager;
import com.arlen.frame.common.utils.DensityUtils;
import com.arlen.frame.view.account.model.UpdateInfo;

/**
 * Created by Arlen on 2017/5/15 17:10.
 */

public class VersionPopupWindow extends Dialog implements View.OnClickListener, DialogInterface.OnKeyListener {

    private Activity mContext;
    private UpdateInfo mUpdateInfo;

    private TextView mTvVersion;
    private TextView mTvContent;
    private TextView mTvPercent;
    private Button mBtnNoVersion;
    private Button mBtnYesVersion;
    private ProgressBar mProgressBar;

    private ProgressHelper mProgressHelper;

    public VersionPopupWindow(Activity activity) {
        super(activity, R.style.myDialog);
        mContext = activity;
        mProgressHelper = new ProgressHelper();
        initView(activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int height = DensityUtils.dp2px(mContext, 355);
        int width = DensityUtils.getWindowWidth(mContext) - DensityUtils.dp2px(mContext, 50);
        getWindow().setWindowAnimations(R.style.PhotoPickDialogAnimation); // 添加动画
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.height = height; // 设置宽度
        lp.width = width;
        getWindow().setAttributes(lp);
        setCanceledOnTouchOutside(false);
        setOnKeyListener(this);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.popup_version, null);
        mTvVersion = (TextView) view.findViewById(R.id.tv_version);
        mTvContent = (TextView) view.findViewById(R.id.tv_content);
        mTvPercent = (TextView) view.findViewById(R.id.tv_percent);
        mBtnNoVersion = (Button) view.findViewById(R.id.btn_no_update);
        mBtnYesVersion = (Button) view.findViewById(R.id.btn_yes_update);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        mTvContent.setMovementMethod(ScrollingMovementMethod.getInstance());
        mBtnNoVersion.setOnClickListener(this);
        mBtnYesVersion.setOnClickListener(this);
        setContentView(view);
    }

    public void setData(final UpdateInfo updateInfo) {
        mUpdateInfo = updateInfo;
        mTvVersion.setText("版本号：v" + updateInfo.versionNo);
        mBtnNoVersion.setVisibility(updateInfo.enforce ? View.GONE : View.VISIBLE);
        mTvContent.setText(updateInfo.versionDescription);
        mProgressHelper.setProgressHandler(new DownloadProgressHandler() {
            @Override
            protected void onProgress(long bytesRead, long contentLength, boolean done) {
                mProgressBar.setMax((int) (contentLength / 1024));
                mProgressBar.setProgress((int) (bytesRead / 1024));
                mTvPercent.setText((100*bytesRead/contentLength)+"%");
            }

            @Override
            protected void downFailure() {
                mBtnYesVersion.setText("重试");
            }

            @Override
            protected void downComplete() {
                if (updateInfo.enforce) {
                    //强制更新
                    mBtnYesVersion.setText("安装");
                    if(mProgressHelper.getApkFile() != null) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(mProgressHelper.getApkFile()),
                                "application/vnd.android.package-archive");
                        mContext.startActivity(intent);
                    }
                    exit();
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_no_update:
                dismiss();
                break;
            case R.id.btn_yes_update:
                if (mUpdateInfo.enforce) {
                    if (mBtnYesVersion.getText().toString().equals("退出")) {
                        exit();
                    } else {
                        mProgressHelper.cancelDown();
                        mProgressHelper.retrofitDownload(mUpdateInfo.url);
                        mBtnNoVersion.setVisibility(View.GONE);
                        mTvContent.setVisibility(View.GONE);
                        mTvPercent.setVisibility(View.VISIBLE);
                        mProgressBar.setVisibility(View.VISIBLE);
                        mBtnYesVersion.setText("退出");
                    }
                } else {
                    dismiss();
                    Intent intent = new Intent(mContext,UpdateService.class);
                    intent.putExtra("url",mUpdateInfo.url);
                    mContext.startService(intent);
                }
                break;
        }
    }

    private void exit(){
        mProgressHelper.cancelDown();
        dismiss();
        Activity activity = ActivityManager.getInstance().currentActivity();
        if (activity != null || !activity.isFinishing()) {
            activity.finish();
        }
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0
                && mUpdateInfo.enforce){
            exit();
        }
        return false;
    }
}
