package com.arlen.frame.common.base;

import com.arlen.frame.common.net.HttpProvider;

import java.lang.ref.WeakReference;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Arlen on 2016/12/21 16:30.
 */
public abstract class BasePresenter<T> implements IBasePresenter<T> {

    public WeakReference<T> mView;
    private Subscription mSubscription;

    @Override
    public void attach(T view) {
        this.mView = new WeakReference<T>(view);
    }

    @Override
    public T getView() {
        if(mView != null) {
            if (mView.get() == null)
                throw new RuntimeException("View has been detach by detachView method. " +
                        "Ensure calling detach method inside activity destroy method");
            return mView.get();
        }
        return null;
    }

    @Override
    public void detach() {
        if (mView != null) {
            mView.clear();
            mView = null;
        }
    }

    @Override
    public void onResume() {
    }

    public Subscription setObservable(Observable observable, Subscriber subscriber) {
        //取消了订阅, 同时也取消了http请求, RxJavaCallAdapterFactory帮我们给subscriber添加了call.cancel()'
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
