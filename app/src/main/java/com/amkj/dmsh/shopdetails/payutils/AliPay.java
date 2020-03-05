package com.amkj.dmsh.shopdetails.payutils;

import android.app.Activity;
import android.os.Handler;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.amkj.dmsh.network.NetLoadUtils;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import static com.amkj.dmsh.constant.Url.Q_UPDATE_PAY_RESULT;

/**
 * 支付宝支付
 * Created by tsy on 16/6/1.
 */
public class AliPay {
    private final Activity mContext;
    private final String mOrderNo;
    private String mParams;
    private PayTask mPayTask;
    private AliPayResultCallBack mCallback;

    public static final int ERROR_RESULT = 1;   //支付结果解析错误
    public static final int ERROR_PAY = 2;  //支付失败
    public static final int ERROR_NETWORK = 3;  //网络连接错误

    public interface AliPayResultCallBack {
        void onSuccess(); //支付成功

        void onDealing();    //正在处理中 小概率事件 此时以验证服务端异步通知结果为准

        void onError(int error_code);   //支付失败

        void onCancel();    //支付取消
    }

    public AliPay(Activity context, String orderNo, String params, AliPayResultCallBack callback) {
        mParams = params;
        mCallback = callback;
        mPayTask = new PayTask(context);
        mContext = context;
        mOrderNo = orderNo;
        updatePayResult(0, params, "");
    }

    //支付
    public void doPay() {
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = mPayTask.pay(mParams, true);
                final AliPayResult pay_result = new AliPayResult(result);
                String resultStatus = pay_result.getResultStatus();
                updatePayResult(1, pay_result.getResult(), resultStatus);
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
                    }
                });
            }
        }).start();
    }

    private void updatePayResult(int type, String result, String code) {
        Map<String, Object> map = new HashMap<>();
        map.put("orderNo", mOrderNo);


        if (type == 0) {
            try {
                String keyWord = URLDecoder.decode(result, "UTF-8");
                map.put("result", keyWord);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else if (type == 1 && !TextUtils.isEmpty(result)) {
            Map resultMap = new Gson().fromJson(result, Map.class);
            Map responseMap = (Map) resultMap.get("alipay_trade_app_pay_response");
            if (responseMap != null) {
                String outTradeNo = (String) responseMap.get("out_trade_no");
                String tradeNo = (String) responseMap.get("trade_no");
                if (!TextUtils.isEmpty(outTradeNo)) {
                    map.put("outTradeNo", outTradeNo);
                }

                if (!TextUtils.isEmpty(tradeNo)) {
                    map.put("tradeNo", tradeNo);
                }

                map.put("result", new Gson().toJson(responseMap));
            }
            map.put("status", "9000".equals(code) ? 1 : 0);

        }
        map.put("payType", 0);//0代表是app的支付宝支付，1代表是app的微信支付
        map.put("type", type);//支付结果，0代表是请求支付，1代表是响应支付
        NetLoadUtils.getNetInstance().loadNetDataPost(mContext, Q_UPDATE_PAY_RESULT, map, null);
    }
}
