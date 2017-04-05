package com.arlen.frame.common.base;

import com.arlen.frame.common.net.HttpProvider;

import java.lang.ref.WeakReference;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.internal.util.SubscriptionList;
import rx.schedulers.Schedulers;

/**
 * Created by Arlen on 2016/12/21 16:30.
 */
public abstract class BasePresenter<T> implements IBasePresenter<T> {

    public WeakReference<T> mView;
    private SubscriptionList mSubscriptionList;

    @Override
    public void attach(T view) {
        this.mView = new WeakReference<T>(view);
        mSubscriptionList = new SubscriptionList();
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
        if(mSubscriptionList != null && mSubscriptionList.hasSubscriptions()){
            mSubscriptionList.unsubscribe();
        }
    }

    @Override
    public void onResume() {
    }

    public void setObservable(Observable observable, Subscriber subscriber) {
        Subscription subscription = observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(subscriber);
        if(mSubscriptionList != null){
            mSubscriptionList.add(subscription);
        }
    }

    public <V> V createService(final Class<V> service) {
        return HttpProvider.getInstance().create(service);
    }
}
