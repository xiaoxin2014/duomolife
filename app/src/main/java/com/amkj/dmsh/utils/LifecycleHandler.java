package com.amkj.dmsh.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;


/**
 * Created by 程序亦非猿 on 2019/3/11.
 */
public class LifecycleHandler extends Handler implements LifecycleObserver {

    private Context mContext;

    public LifecycleHandler(final Context lifecycleOwner) {
        this.mContext = lifecycleOwner;
        addObserver();
    }

    public LifecycleHandler(final Context lifecycleOwner,final Callback callback ) {
        super(callback);
        this.mContext = lifecycleOwner;
        addObserver();
    }

    public LifecycleHandler(final Context lifecycleOwner,final Looper looper ) {
        super(looper);
        this.mContext = lifecycleOwner;
        addObserver();
    }

    public LifecycleHandler(final Context lifecycleOwner, final Looper looper, final Callback callback) {
        super(looper, callback);
        this.mContext = lifecycleOwner;
        addObserver();
    }

    private void addObserver() {
        if (mContext instanceof LifecycleOwner) {
            ((LifecycleOwner) mContext).getLifecycle().addObserver(this);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private void onDestroy() {
        removeCallbacksAndMessages(null);
        if (mContext instanceof LifecycleOwner) {
            ((LifecycleOwner) mContext).getLifecycle().removeObserver(this);
        }
    }
}
