package com.amkj.dmsh.utils.inteface;

import com.amkj.dmsh.utils.Log;

import org.xutils.common.Callback;

public class MyCacheCallBack<ResultType> implements Callback.CacheCallback<ResultType> {

    @Override
    public boolean onCache(ResultType result) {
        return false;
    }

    @Override
    public void onSuccess(ResultType result) {

    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        Log.d(this, "onError:", ex);
    }

    @Override
    public void onCancelled(CancelledException cex) {

    }

    @Override
    public void onFinished() {

    }
}
