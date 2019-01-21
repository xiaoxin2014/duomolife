package com.amkj.dmsh.shopdetails.payutils;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.amkj.dmsh.homepage.activity.DoMoLifeCommunalActivity;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_PRO_STATUS;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.UNION_RESULT_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.isDebugTag;
import static com.amkj.dmsh.constant.Url.Q_INDENT_DETAILS;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2019/1/16
 * version 3.2.0
 * class description:银联支付
 */
public class UnionPay {
    private final UnionPayResultCallBack unionPayResultCallBack;
    private int continueTimes;

    /**
     * @param activity
     * @param resultUrl 银联支付返回跳转地址
     * @param unionPayResultCallBack 订单查询回调
     */
    public UnionPay(Activity activity, String resultUrl, @NonNull UnionPayResultCallBack unionPayResultCallBack) {
        this.unionPayResultCallBack = unionPayResultCallBack;
        if(!TextUtils.isEmpty(resultUrl)){
            Intent intent = new Intent(activity, DoMoLifeCommunalActivity.class);
            intent.putExtra("loadUrl", resultUrl);
            intent.putExtra("backResult","1");
            activity.startActivityForResult(intent, UNION_RESULT_CODE);
        }
    }
    /**
     *
     * @param fragment
     * @param resultUrl 银联支付返回跳转地址
     * @param unionPayResultCallBack 订单查询回调
     */
    public UnionPay(Fragment fragment, String resultUrl, @NonNull UnionPayResultCallBack unionPayResultCallBack) {
        this.unionPayResultCallBack = unionPayResultCallBack;
        if(!TextUtils.isEmpty(resultUrl)){
            Intent intent = new Intent(fragment.getContext(), DoMoLifeCommunalActivity.class);
            intent.putExtra("loadUrl", resultUrl);
            fragment.startActivityForResult(intent, UNION_RESULT_CODE);
        }
    }

    /**
     * 根据回调 查看订单状态
     * @param orderNo
     */
    public void unionPayResult(String orderNo){
        unionPayResult(orderNo,"");
    }
    /**
     * 根据回调 查看订单状态
     * @param orderNo
     * @param webResultValue web调用返回值
     */
    public void unionPayResult(String orderNo,String webResultValue){
        if(unionPayResultCallBack == null){
            throw new NullPointerException("支付回调监听不能为空");
        }
        if(TextUtils.isEmpty(orderNo)){
            showToast("订单号不存在，请退出重试！");
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("no", orderNo);
        //        版本号控制 3 组合商品赠品
        params.put("version", 3);
        NetLoadListenerHelper netLoadListenerHelper = new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                String code = "";
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    code = (String) jsonObject.get("code");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (code.equals(SUCCESS_CODE)) {
                    Gson gson = new Gson();
                    UnionCheckIndentStatusBean unionCheckIndentStatusBean = gson.fromJson(result, UnionCheckIndentStatusBean.class);
                    if (unionCheckIndentStatusBean != null) {
                        INDENT_PRO_STATUS = unionCheckIndentStatusBean.getUnionIndentInfoBean().getStatus();
                        if (unionCheckIndentStatusBean.getUnionIndentInfoBean().getUnionIndentStatusBean() != null
                                && unionCheckIndentStatusBean.getUnionIndentInfoBean().getUnionIndentStatusBean().getStatus() >= 10) {
                            unionPayResultCallBack.onUnionPaySuccess(webResultValue);
                        } else {
//                           如果订单状态为未完成，延时一秒再次执行
                            if(continueTimes==0){
                                if(isDebugTag){
                                    showToast("订单为未完成状态，延时一秒再次执行，请稍后……");
                                }
                                continueTimes++;
                                Timer timer = new Timer();
                                timer.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        timer.cancel();
                                        unionPayResult(orderNo,webResultValue);
                                    }
                                },1000);
                            }else{
                                unionPayResultCallBack.onUnionPayError("取消支付");
                            }
                        }
                    } else {
                        unionPayResultCallBack.onUnionPayError("数据异常，请稍后重试！");
                    }
                } else {
                    unionPayResultCallBack.onUnionPayError("订单查询失败!");
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
        NetLoadUtils.getNetInstance().loadNetDataPost(mAppContext, Q_INDENT_DETAILS
                , params, netLoadListenerHelper);
    }

    public interface UnionPayResultCallBack {
        void onUnionPaySuccess(String webFinishResult); //支付成功
        void onUnionPayError(String errorMes);    //支付取消
    }
}
