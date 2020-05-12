package com.amkj.dmsh.mine;

import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.ConstantMethod;

/**
 * 倒计时Button帮助类
 * @date 3.1.5
 * @author liuguipeng
 * @see
 */
public class CountDownHelper {
    private static int remain = 0;
    private ConstantMethod constantMethod;
    private static CountDownHelper countDownHelper;

    private CountDownHelper() {
    }

    //    单例模式
    public static CountDownHelper getTimerInstance() {
        if (countDownHelper == null) {
            synchronized (CountDownHelper.class) {
                if (countDownHelper == null) {
                    countDownHelper = new CountDownHelper();
                }
            }
        }
        return countDownHelper;
    }


    /**
     * @param textView      需要显示倒计时的Button
     * @param defaultString 默认显示的字符串
     * @param max           需要进行倒计时的最大值,单位是秒
     */
    public void setSmsCountDown(final TextView textView, final String defaultString, int max) {
        // 由于CountDownTimer并不是准确计时，在onTick方法调用的时候，time会有1-10ms左右的误差，这会导致最后一秒不会调用onTick()
        // 因此，设置间隔的时候，默认减去了10ms，从而减去误差。
        // 经过以上的微调，最后一秒的显示时间会由于10ms延迟的积累，导致显示时间比1s长max*10ms的时间，其他时间的显示正常,总时间正常
        if (remain < 1) {
            remain = max;
        }
        startCountDownTimer(textView, defaultString);
    }

    /**
     * 启动倒计时
     *
     * @param textView
     * @param defaultString 默认展示
     */
    private void startCountDownTimer(@NonNull TextView textView, String defaultString) {
        if (remain > 0) {
            textView.setEnabled(false);
            textView.setText(String.format(textView.getResources().getString(R.string.wait_msg), remain));
            if(constantMethod==null){
                constantMethod = new ConstantMethod();
                constantMethod.createSchedule();
            }
            if(constantMethod.getScheduler()!=null
                    &&constantMethod.getScheduler().isShutdown()){
                constantMethod.createSchedule();
            }
            constantMethod.setRefreshTimeListener(new ConstantMethod.RefreshTimeListener() {
                @Override
                public void refreshTime() {
                    if (remain > 0) {
                        --remain;
                        if (remain > 0) {
                            textView.setText(String.format(textView.getResources().getString(R.string.wait_msg), remain));
                        } else {
                            constantMethod.stopSchedule();
                            textView.setEnabled(true);
                            textView.setText(TextUtils.isEmpty(defaultString) ? textView.getResources().getString(R.string.send_sms) : defaultString);
                        }
                    }
                }
            });
        } else {
            textView.setEnabled(true);
            textView.setText(TextUtils.isEmpty(defaultString) ? textView.getResources().getString(R.string.send_sms) : defaultString);
        }
    }

    /**
     * 获取验证码 倒计时
     *
     * @param textView
     */
    public void setSmsCountDown(final TextView textView) {
        startCountDownTimer(textView, "");
    }
}
