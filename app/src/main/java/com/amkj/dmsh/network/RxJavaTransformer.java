package com.amkj.dmsh.network;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/11/30
 * version 3.1.9
 * class description:rxjava 快速切换线程
 */
public class RxJavaTransformer {
    /**
     * 返回rxjava 线程切换Transformer
     * @param <T>
     * @return
     */
    public static <T>FlowableTransformer<T, T> getSchedulerFlowableTransformer(){
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(Flowable<T> upstream) {
                return upstream.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io());
            }
        };
    }
    /**
     * 返回rxjava 线程切换Transformer
     * @param <T>
     * @return
     */
    public static <T>ObservableTransformer<T, T> getSchedulerObservableTransformer(){
        return new ObservableTransformer<T,T>(){
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io());
            }
        };
    }
}
