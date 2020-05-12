package com.amkj.dmsh.shopdetails.payutils;

import android.app.Activity;
import android.content.Intent;
import androidx.annotation.NonNull;
import android.text.TextUtils;

import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.homepage.activity.DoMoLifeCommunalActivity;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.UNION_PAY_CALLBACK;
import static com.amkj.dmsh.constant.Url.Q_UNIONPAY_PAYMENT_INDENT;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2019/1/16
 * version 3.2.0
 * class description:银联支付
 */
public class UnionPay {
    private UnionPayResultCallBack unionPayResultCallBack;
    private Activity context;
    private String orderNo;

    /**
     * @param activity
     * @param loadUrl                银联支付返回跳转地址
     * @param unionPayResultCallBack 订单查询回调
     */
    public UnionPay(Activity activity, String orderNo, String loadUrl, @NonNull UnionPayResultCallBack unionPayResultCallBack) {
        context = activity;
        this.orderNo = orderNo;
        // 注册当前Activity为订阅者
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        this.unionPayResultCallBack = unionPayResultCallBack;
        if (!TextUtils.isEmpty(loadUrl)) {
            Intent intent = new Intent(activity, DoMoLifeCommunalActivity.class);
            intent.putExtra("loadUrl", loadUrl);
            intent.putExtra("backResult", "1");
            activity.startActivity(intent);
        }
    }

    /**
     * 根据回调 查看订单状态
     */
    public void unionPayResult(boolean isWebManualFinish) {
        if (unionPayResultCallBack == null) {
            throw new NullPointerException("支付回调监听不能为空");
        }

        if (isWebManualFinish) {
            unionPayResultCallBack.onUnionPaySuccess("");
        } else {
            Map<String, Object> params = new HashMap<>();
            params.put("uid", userId);
            //        版本号控制 3 组合商品赠品
            params.put("orderNo", orderNo);
            NetLoadUtils.getNetInstance().loadNetDataPost(context, Q_UNIONPAY_PAYMENT_INDENT
                    , params, new NetLoadListenerHelper() {
                        @Override
                        public void onSuccess(String result) {
                            String code = "";
                            String msg = "";
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                code = (String) jsonObject.get("code");
                                msg = (String) jsonObject.get("msg");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (SUCCESS_CODE.equals(code)) {
                                unionPayResultCallBack.onUnionPaySuccess("");
                            } else {
                                unionPayResultCallBack.onUnionPayError(TextUtils.isEmpty(msg) ? "订单查询失败!" : msg);
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            unionPayResultCallBack.onUnionPayError("数据异常，请稍后重试！");
                        }
                    });
        }
    }

    public interface UnionPayResultCallBack {
        void onUnionPaySuccess(String webFinishResult); //支付成功

        void onUnionPayError(String errorMes);    //支付取消
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetResult(EventMessage message) {
        if (message == null) {
            return;
        }

        if (UNION_PAY_CALLBACK.equals(message.type)) {
            unionPayResult((Boolean) message.result);
        }
    }
}
