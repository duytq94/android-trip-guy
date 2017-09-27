package com.dfa.vinatrip.utils;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by duonghd on 9/25/2017.
 */

public class RxHelper {
    private static final Observable.Transformer SCHEDULERS_COMPUTE_TRANSFORMER =
            (Observable.Transformer<Observable<Object>,
                    Observable<Object>>) observable -> observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
    
    private static final Observable.Transformer SCHEDULERS_IO_TRANSFORMER =
            (Observable.Transformer<Observable<Object>,
                    Observable<Object>>) observable -> observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
    
    private static final Observable.Transformer SCHEDULERS_TRANSFORMER_NEW_THREAD =
            (Observable.Transformer<Observable<Object>,
                    Observable<Object>>) observable -> observable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread());
    
    /**
     * Apply scheduler:
     * observable
     * .subscribeOn(Schedulers.computation())
     * .observeOn(AndroidSchedulers.mainThread());
     *
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> Observable.Transformer<T, T> applySchedulers() {
        return (Observable.Transformer<T, T>) SCHEDULERS_COMPUTE_TRANSFORMER;
    }
    
    /**
     * Apply scheduler io for call api
     *
     * @param <T>
     * @return
     */
    public static <T> Observable.Transformer<T, T> applyIOSchedulers() {
        return (Observable.Transformer<T, T>) SCHEDULERS_IO_TRANSFORMER;
    }
    
    /**
     * Apply scheduler with newThread:
     * observable
     * .subscribeOn(Schedulers.newThread())
     * .observeOn(AndroidSchedulers.mainThread());
     *
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> Observable.Transformer<T, T> applyNewThreadSchedulers() {
        return (Observable.Transformer<T, T>) SCHEDULERS_TRANSFORMER_NEW_THREAD;
    }
    
    /**
     * onNext with subscriber and item
     *
     * @param subscriber
     * @param item
     * @param <T>
     */
    
    /**
     * onStop with subscription
     *
     * @param subscription
     */
    public static void onStop(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
