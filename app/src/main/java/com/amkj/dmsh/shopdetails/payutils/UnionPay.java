package com.amkj.dmsh.shopdetails.payutils;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.amkj.dmsh.homepage.activity.DoMoLifeCommunalActivity;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.ERROR_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.UNION_RESULT_CODE;
import static com.amkj.dmsh.constant.Url.Q_UNIONPAY_PAYMENT_INDENT;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2019/1/16
 * version 3.2.0
 * class description:银联支付
 */
public class UnionPay {
    private final UnionPayResultCallBack unionPayResultCallBack;

    /**
     * @param activity
     * @param resultUrl              银联支付返回跳转地址
     * @param unionPayResultCallBack 订单查询回调
     */
    public UnionPay(Activity activity, String resultUrl, @NonNull UnionPayResultCallBack unionPayResultCallBack) {
        this.unionPayResultCallBack = unionPayResultCallBack;
        if (!TextUtils.isEmpty(resultUrl)) {
            Intent intent = new Intent(activity, DoMoLifeCommunalActivity.class);
            intent.putExtra("loadUrl", resultUrl);
            intent.putExtra("backResult", "1");
            activity.startActivityForResult(intent, UNION_RESULT_CODE);
        }
    }

    /**
     * @param fragment
     * @param resultUrl              银联支付返回跳转地址
     * @param unionPayResultCallBack 订单查询回调
     */
    public UnionPay(Fragment fragment, String resultUrl, @NonNull UnionPayResultCallBack unionPayResultCallBack) {
        this.unionPayResultCallBack = unionPayResultCallBack;
        if (!TextUtils.isEmpty(resultUrl)) {
            Intent intent = new Intent(fragment.getContext(), DoMoLifeCommunalActivity.class);
            intent.putExtra("loadUrl", resultUrl);
            fragment.startActivityForResult(intent, UNION_RESULT_CODE);
        }
    }

    /**
     * 根据回调 查看订单状态
     *
     * @param orderNo
     */
    public void unionPayResult(Activity activity,String orderNo) {
        unionPayResult(activity,orderNo, "");
    }

    /**
     * 根据回调 查看订单状态
     *
     * @param orderNo
     * @param webResultValue web调用返回值
     */
    public void unionPayResult(Activity activity,String orderNo, String webResultValue) {
        if (unionPayResultCallBack == null) {
            throw new NullPointerException("支付回调监听不能为空");
        }
        if (TextUtils.isEmpty(orderNo)) {
            showToast("订单号不存在，请退出重试！");
            return;
        }
        if (userId < 1) {
            showToast("用户信息有误，请重新登录！");
            return;
        }
        if (!TextUtils.isEmpty(webResultValue) && "1".equals(webResultValue)) {
            unionPayResultCallBack.onUnionPaySuccess(webResultValue);
        } else {
            Map<String, Object> params = new HashMap<>();
            params.put("uid", userId);
            //        版本号控制 3 组合商品赠品
            params.put("orderNo", orderNo);
            NetLoadListenerHelper netLoadListenerHelper = new NetLoadListenerHelper() {
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
                    if (!code.equals(ERROR_CODE)) {
                        unionPayResultCallBack.onUnionPaySuccess(webResultValue);
                    } else {
                        unionPayResultCallBack.onUnionPayError(TextUtils.isEmpty(msg) ? "订单查询失败!" : msg);
                    }
                }

                @Override
                public void netClose() {
                    unionPayResultCallBack.onUnionPayError("暂无联网，请稍后重试！");
                }

                @Override
                public void onError(Throwable throwable) {
                    unionPayResultCallBack.onUnionPayError("数据异常，请稍后重试！");
                }
            };
            NetLoadUtils.getNetInstance().loadNetDataPost(activity, Q_UNIONPAY_PAYMENT_INDENT
                    , params, netLoadListenerHelper);
        }
    }

    public interface UnionPayResultCallBack {
        void onUnionPaySuccess(String webFinishResult); //支付成功

        void onUnionPayError(String errorMes);    //支付取消
    }
}
