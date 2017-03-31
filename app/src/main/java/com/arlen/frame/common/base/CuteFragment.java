package com.arlen.frame.common.base;

import com.arlen.frame.common.net.HttpProvider;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 直接在Fragment中联网获取数据
 * Created by Arlen on 2017/1/11.
 */
public class CuteFragment extends BaseFragment {

    private Subscription mSubscription;

    @Override
    public void onPause() {
        super.onPause();
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden) {
            if (mSubscription != null) {
                mSubscription.unsubscribe();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * 轮询 每隔五秒执行一次 订阅消息在子线程中轮询 所以在onErro时不能更新View 需要用Handler通知更新
     * 也可以自己设置轮询条件
     * @param observable
     * @param subscriber
     * @return
     */
    public Subscription setObservable(Observable observable, Subscriber subscriber) {
        //取消了订阅, 同时也取消了http请求, RxJavaCallAdapterFactory帮我们给subscriber添加了call.cancel()
//        if (mSubscription != null) {
//            mSubscription.unsubscribe();
//        }
        mSubscription = observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .repeatWhen(new Func1<Observable<? extends Void>, Observable<? extends Void>>() {
                    @Override
                    public Observable<? extends Void> call(Observable<? extends Void> observable) {
                        return observable.delay(5, TimeUnit.SECONDS);
                    }
                })
                .subscribe(subscriber);
        return mSubscription;
    }

    public Subscription setObservableNoRepeat(Observable observable, Subscriber subscriber) {
        //取消了订阅, 同时也取消了http请求, RxJavaCallAdapterFactory帮我们给subscriber添加了call.cancel()
//        if (mSubscription != null) {
//            mSubscription.unsubscribe();
//        }
        mSubscription = observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(subscriber);
        return mSubscription;
    }

    public <V> V createService(final Class<V> service) {
        return HttpProvider.getInstance().create(service);
    }

}
