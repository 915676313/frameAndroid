package com.arlen.frame.common.base;


import android.os.Bundle;
import android.support.annotation.Nullable;

import com.arlen.frame.common.net.HttpProvider;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.internal.util.SubscriptionList;
import rx.schedulers.Schedulers;


/**
 *  不适用MVP模式的Activity获取数据
 * Created by Arlen on 2017/1/11.
 */
public class CuteActivity extends BaseActivity{

    private SubscriptionList mSubscriptionList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSubscriptionList  = new SubscriptionList();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSubscriptionList != null && mSubscriptionList.hasSubscriptions()) {
            mSubscriptionList.unsubscribe();
        }
    }

    public void setObservable(Observable observable, Subscriber subscriber) {
        //取消了订阅, 同时也取消了http请求, RxJavaCallAdapterFactory帮我们给subscriber添加了call.cancel()
        Subscription subscribe = observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        if(mSubscriptionList != null){
            mSubscriptionList.add(subscribe);
        }
    }

    public <V> V createService(final Class<V> service) {
        return HttpProvider.getInstance().create(service);
    }
}
