package com.amkj.dmsh.mine;

import android.content.Context;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.utils.CountDownTimer;

import androidx.annotation.NonNull;

import static com.amkj.dmsh.constant.ConstantMethod.getIntegralFormat;

/**
 * Created by xiaoxin on 2020/5/28
 * Version:v4.6.1
 * ClassDescription :短信验证码倒计时工具类
 */
public class SmsCodeHelper {

    public static void startCountDownTimer(Context context, @NonNull TextView textView) {
        CountDownTimer countDownTimer = (CountDownTimer) textView.getTag(R.id.id_tag);
        if (countDownTimer == null) {
            countDownTimer = new CountDownTimer(context) {
                @Override
                public void onTick(long millisUntilFinished) {
                    textView.setEnabled(false);
                    textView.setText(getIntegralFormat(context, R.string.wait_msg, (int) (millisUntilFinished / 1000)));
                }

                @Override
                public void onFinish() {
                    textView.setEnabled(true);
                    textView.setText(R.string.send_sms);
                }
            };
        }

        countDownTimer.setMillisInFuture(60 * 1000);
        countDownTimer.start();
    }
}
