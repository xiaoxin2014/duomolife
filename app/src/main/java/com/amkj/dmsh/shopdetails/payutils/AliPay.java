package com.amkj.dmsh.shopdetails.payutils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;

/**
 * 支付宝支付
 * Created by tsy on 16/6/1.
 */
public class AliPay {
    private String mParams;
    private PayTask mPayTask;
    private AliPayResultCallBack mCallback;

    public static final int ERROR_RESULT = 1;   //支付结果解析错误
    public static final int ERROR_PAY = 2;  //支付失败
    public static final int ERROR_NETWORK = 3;  //网络连接错误
    private final Context mContext;
    public static final int SDK_PAY_FLAG = 100;  //网络连接错误

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        //在handleMessage里面实现UI的更新
        public void handleMessage(Message msg) {
            final int type = msg.what;
            // 判断消息的类型
            switch (type) {
                case SDK_PAY_FLAG:
                    String resultStatus = (String) msg.obj;
                    if (TextUtils.equals(resultStatus, "9000")) {    //支付成功
                        mCallback.onSuccess();
                    } else if (TextUtils.equals(resultStatus, "8000")) { //支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        mCallback.onDealing();
                    } else if (TextUtils.equals(resultStatus, "6001")) {        //支付取消
                        mCallback.onCancel();
                    } else if (TextUtils.equals(resultStatus, "6002")) {     //网络连接出错
                        mCallback.onError(ERROR_NETWORK);
                    } else if (TextUtils.equals(resultStatus, "4000")) {        //支付错误
                        mCallback.onError(ERROR_PAY);
                    }
                    break;
            }
        }
    };

    public interface AliPayResultCallBack {
        void onSuccess(); //支付成功

        void onDealing();    //正在处理中 小概率事件 此时以验证服务端异步通知结果为准

        void onError(int error_code);   //支付失败

        void onCancel();    //支付取消
    }

    public AliPay(Context context, String params, AliPayResultCallBack callback) {
        mParams = params;
        mCallback = callback;
        mContext = context;
    }

    //支付
    public void doPay() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mPayTask == null) {
                    mPayTask = new PayTask((Activity) mContext);
                }
                String result = mPayTask.pay(mParams, true);
                final AliPayResult pay_result = new AliPayResult(result);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mCallback == null) {
                            return;
                        }
                        if (pay_result == null) {
                            mCallback.onError(ERROR_RESULT);
                            return;
                        }

                        String resultStatus = pay_result.getResultStatus();
                        Message msg = new Message();
                        msg.what = SDK_PAY_FLAG;
                        msg.obj = resultStatus;
                        handler.sendMessage(msg);
                    }
                });
            }
        }).start();
    }
}
