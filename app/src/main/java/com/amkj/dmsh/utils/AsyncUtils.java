package com.amkj.dmsh.utils;

import com.amkj.dmsh.base.BaseActivity;
import com.dhh.rxlife2.RxLife;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * RxJava异步任务工具类
 * Created by xiaoxin on 2017/12/21 0021
 */


public abstract class AsyncUtils<T> {

    private final BaseActivity mActivity;

    protected AsyncUtils(BaseActivity activity) {
        mActivity = activity;
    }

    public void excueTask() {
        Observable<T> observable = Observable.create(e -> {
            //执行一些其他操作
            T data = runOnIO();
            //执行完毕，触发回调，通知观察者
            e.onNext(data);

        });

        Observer<T> observer = new Observer<T>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            //观察者接收到通知,进行相关操作
            public void onNext(T data) {
//                System.out.println("我接收到数据了");
                runOnUI(data);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };

        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).compose(RxLife.with(mActivity).bindToLifecycle()).subscribe(observer);

    }

    public abstract T runOnIO();

    public abstract void runOnUI(T t);
}
