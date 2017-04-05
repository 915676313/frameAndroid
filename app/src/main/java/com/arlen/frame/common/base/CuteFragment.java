package com.arlen.frame.common.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.arlen.frame.common.net.HttpProvider;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.internal.util.SubscriptionList;
import rx.schedulers.Schedulers;

/**
 * 直接在Fragment中联网获取数据
 * Created by Arlen on 2017/1/11.
 */
public class CuteFragment extends BaseFragment {

    private SubscriptionList mSubscriptionList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSubscriptionList = new SubscriptionList();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mSubscriptionList != null && mSubscriptionList.hasSubscriptions()) {
            mSubscriptionList.unsubscribe();
        }
    }

    /**
     * 轮询 每隔五秒执行一次 订阅消息在子线程中轮询 所以在onErro时不能更新View 需要用Handler通知更新
     * 也可以自己设置轮询条件
     *
     * @param observable
     * @param subscriber
     * @return
     */
    public void setObservable(Observable observable, Subscriber subscriber) {
        //取消了订阅, 同时也取消了http请求, RxJavaCallAdapterFactory帮我们给subscriber添加了call.cancel()
        Subscription subscribe = observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .repeatWhen(new Func1<Observable<? extends Void>, Observable<? extends Void>>() {
                    @Override
                    public Observable<? extends Void> call(Observable<? extends Void> observable) {
                        return observable.delay(5, TimeUnit.SECONDS);
                    }
                }).subscribe(subscriber);

        if (mSubscriptionList != null) {
            mSubscriptionList.add(subscribe);
        }
    }

    public void setObservableNoRepeat(Observable observable, Subscriber subscriber) {
        //取消了订阅, 同时也取消了http请求, RxJavaCallAdapterFactory帮我们给subscriber添加了call.cancel()
        Subscription subscribe = observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(subscriber);

        if (mSubscriptionList != null) {
            mSubscriptionList.add(subscribe);
        }
    }

    public <V> V createService(final Class<V> service) {
        return HttpProvider.getInstance().create(service);
    }

}
