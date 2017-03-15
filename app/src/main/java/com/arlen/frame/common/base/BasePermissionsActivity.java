package com.arlen.frame.common.base;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zhoujian on 2017/3/5.
 */

public class BasePermissionsActivity extends Activity
{


    public static final String TAG = "BasePermissionsActivity";

    public static  int REQUEST_CODE = 0;

    public void requestPermission(String[] permissions,int requestCode)
    {

        this.REQUEST_CODE = requestCode;

        //检查权限是否授权
        if(checkPermissions(permissions))
        {
            permissinSucceed(REQUEST_CODE);
        }
        else
        {
            List<String> needPermissions = getPermissions(permissions);
            ActivityCompat.requestPermissions(this, needPermissions.toArray(new String[needPermissions.size()]), REQUEST_CODE);
        }
    }

    private List<String> getPermissions(String[] permissions)
    {
        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) !=
                    PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                permissionList.add(permission);
            }
        }
        return permissionList;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (requestCode == REQUEST_CODE) {
            if (verificationPermissions(grantResults)) {
                permissinSucceed(REQUEST_CODE);
            } else {
                permissionFailing(REQUEST_CODE);
                showFaiingDialog();
            }
        }
    }

    private boolean verificationPermissions(int[] results)
    {

        for (int result : results) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;

    }

    /**
     * 检测所有的权限是否都已授权
     *
     * @param permissions
     * @return
     */
    private boolean checkPermissions(String[] permissions)
    {
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M)
        {
            return true;
        }

        for(String permission:permissions)
        {

            if(ContextCompat.checkSelfPermission(BasePermissionsActivity.this,permission)!= PackageManager.PERMISSION_GRANTED)
            {
                return false;
            }
        }
        return true;
    }


    private void showFaiingDialog()
    {
        new AlertDialog.Builder(this)
                .setTitle("消息")
                .setMessage("当前应用无此权限，该功能暂时无法使用。如若需要，请单击确定按钮进行权限授权！")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        return;
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startSettings();
                    }
                }).show();

    }

    private void startSettings() {

        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    public void permissionFailing(int code) {

        Log.d(TAG, "获取权限失败=" + code);
    }

    public void permissinSucceed(int code) {

        Log.d(TAG, "获取权限成功=" + code);
    }
}