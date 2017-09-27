package com.dfa.vinatrip.base;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

/**
 * Created by duonghd on 9/19/2017.
 */

public class BasePresenter<V extends MvpView> extends MvpBasePresenter<V> implements MvpPresenter<V> {
    
    @Inject
    protected EventBus eventBus;
    
    @Inject
    public BasePresenter(EventBus eventBus) {
        this.eventBus = eventBus;
    }
    
    @Override
    @Subscribe
    public void attachView(V view) {
        super.attachView(view);
        eventBus.register(this);
    }
    
    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        eventBus.unregister(this);
    }
}