package com.arlen.frame.common.base;


import com.arlen.frame.common.net.HttpProvider;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 *  不适用MVP模式的Activity获取数据
 * Created by Arlen on 2017/1/11.
 */
public class CuteActivity extends BaseActivity{

    private Subscription mSubscription;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
    }

    public Subscription setObservable(Observable observable, Subscriber subscriber) {
        //取消了订阅, 同时也取消了http请求, RxJavaCallAdapterFactory帮我们给subscriber添加了call.cancel()
//        if (mSubscription != null) {
//            mSubscription.unsubscribe();
//        }
        mSubscription = observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        return mSubscription;
    }

    public <V> V createService(final Class<V> service) {
        return HttpProvider.getInstance().create(service);
    }
}
