/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.amkj.dmsh.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Message;
import android.os.SystemClock;


/**
 * Created by xiaoxin on 2020/5/27
 * Version:v4.6.1
 * ClassDescription :定时任务工具类,基于官方CountDownTimer修改
 * 优化1.使用LifecycleHandler处理定时任务，页面销毁时自动移除未处理的message,避免内存泄漏
 * 优化2.可动态设置倒计时总时长，并处理误差
 * 优化3.倒计时结束时自动取消定时任务
 */
public abstract class CountDownTimer {

    /**
     * Millis since epoch when alarm should stop.
     */
    private long mMillisInFuture;

    public long getMillisInFuture() {
        return mMillisInFuture;
    }

    //动态设置倒计时总时长，复用定时任务时需要更新倒计时时间，避免错乱
    public void setMillisInFuture(long millisInFuture) {
        //代码执行消耗时间，所以+300处理误差
        mMillisInFuture = millisInFuture + 300;
    }

    /**
     * The interval in millis that the user receives callbacks
     */
    private long mCountdownInterval;

    private long mStopTimeInFuture;

    /**
     * boolean representing if the timer was cancelled
     */
    private boolean mCancelled = false;


    @SuppressLint("HandlerLeak")
    public CountDownTimer(Context lifecycleOwner) {
        //默认间隔时间1s
        this(lifecycleOwner, 1000);
    }

    @SuppressLint("HandlerLeak")
    private CountDownTimer(Context lifecycleOwner, long countDownInterval) {
        mCountdownInterval = countDownInterval;
        if (mHandler == null) {
            mHandler = new LifecycleHandler(lifecycleOwner) {
                @Override
                public void handleMessage(Message msg) {
                    synchronized (CountDownTimer.this) {
                        if (mCancelled) {
                            return;
                        }

                        final long millisLeft = mStopTimeInFuture - SystemClock.elapsedRealtime();

                        //倒计时结束
                        if (millisLeft <= 0) {
                            cancel();
                            onFinish();
                        } else {
                            long lastTickStart = SystemClock.elapsedRealtime();
                            onTick(millisLeft);

                            // take into account user's onTick taking time to execute
                            long lastTickDuration = SystemClock.elapsedRealtime() - lastTickStart;
                            long delay;

                            if (millisLeft < mCountdownInterval) {
                                // just delay until done
                                delay = millisLeft - lastTickDuration;

                                // special case: user's onTick took more than interval to
                                // complete, trigger onFinish without delay
                                if (delay < 0) delay = 0;
                            } else {
                                delay = mCountdownInterval - lastTickDuration;

                                // special case: user's onTick took more than interval to
                                // complete, skip to next interval
                                while (delay < 0) delay += mCountdownInterval;
                            }

                            sendMessageDelayed(obtainMessage(MSG), delay);
                        }
                    }
                }
            };
        }
    }


    /**
     * Cancel the countdown.
     */
    public synchronized final void cancel() {
        mCancelled = true;
        mHandler.removeMessages(MSG);
    }

    /**
     * Start the countdown.
     */
    public synchronized final CountDownTimer start() {
        mCancelled = false;
        if (mMillisInFuture <= 0) {
            onFinish();
            return this;
        }
        mStopTimeInFuture = SystemClock.elapsedRealtime() + mMillisInFuture;
        mHandler.sendMessage(mHandler.obtainMessage(MSG));
        return this;
    }


    /**
     * Callback fired on regular interval.
     *
     * @param millisUntilFinished The amount of time until finished.
     */
    public abstract void onTick(long millisUntilFinished);

    /**
     * Callback fired when the time is up.
     */
    public abstract void onFinish();


    private static final int MSG = 1;


    // handles counting down
    private LifecycleHandler mHandler;
}
