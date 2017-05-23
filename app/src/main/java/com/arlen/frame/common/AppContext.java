package com.arlen.frame.common;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.arlen.frame.BuildConfig;
import com.arlen.frame.common.utils.ActivityManager;
import com.arlen.frame.common.thirdsdk.jpush.JPushOperator;
import com.arlen.frame.common.thirdsdk.tinker.FetchPatchHandler;
import com.arlen.frame.common.utils.OsUtils;
import com.baidu.mapapi.SDKInitializer;
import com.tencent.tinker.loader.app.ApplicationLike;
import com.tinkerpatch.sdk.TinkerPatch;
import com.tinkerpatch.sdk.loader.TinkerPatchApplicationLike;
import com.umeng.analytics.MobclickAgent;

import cn.sharesdk.framework.ShareSDK;

/**
 * Created by Arlen on 2016/12/21 15:13.
 */
public class AppContext extends Application {

    private static Application mAppContext;
    private ApplicationLike mTinkerApplicationLike;

    public AppContext(){

    }

    @Override
    public void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //you must install multiDex whatever tinker is installed!
        MultiDex.install(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (OsUtils.shouldInit(this)){
            mAppContext = this;
            //极光推送
            JPushOperator.init(mAppContext);
            //百度地图
            SDKInitializer.initialize(mAppContext.getApplicationContext());
            //分享
            ShareSDK.initSDK(mAppContext);
            // 友盟功能初始化
            MobclickAgent.openActivityDurationTrack(false); // umeng 禁用默认的页面统计
            MobclickAgent.setDebugMode(true);

            // 我们可以从这里获得Tinker加载过程的信息
            if (BuildConfig.TINKER_ENABLE) {
                mTinkerApplicationLike = TinkerPatchApplicationLike.getTinkerPatchApplicationLike();

                // 初始化TinkerPatch SDK
                TinkerPatch.init(mTinkerApplicationLike)
                        .reflectPatchLibrary()
                        .setPatchRollbackOnScreenOff(true)
                        .setPatchRestartOnSrceenOff(true);

                // 获取当前的补丁版本
                Log.d("tag", "current patch version is " + TinkerPatch.with().getPatchVersion());

                // 每隔3个小时去访问后台时候有更新,通过handler实现轮训的效果
                new FetchPatchHandler().fetchPatchWithInterval(3);
            }
        }
    }

    /**
     * @return 全局的上下文context
     * @Title: getAppContext
     * @Description: 获取全局的上下文context
     */
    public static Context getAppContext() {
        if (mAppContext == null) {
            throw new IllegalStateException("Application is not created.");
        }
        return mAppContext;
    }

    /**
     * 退出App
     */
    public void exitApp() {
        ActivityManager.getInstance().clearAllActivity();
    }

}
