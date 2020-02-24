package com.amkj.dmsh.shopdetails.payutils;

import android.app.Activity;

import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.bean.QualityCreateWeChatPayIndentBean.ResultBean.PayKeyBean;
import com.google.gson.Gson;
import com.tencent.bugly.beta.tinker.TinkerManager;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;

import java.util.HashMap;
import java.util.Map;

import static com.amkj.dmsh.constant.Url.Q_UPDATE_PAY_RESULT;


/**
 * 微信支付
 * Created by tsy on 16/6/1.
 */
public class WXPay {
    private static WXPay mWXPay;
    private IWXAPI mWXApi;
    //    private String mPayParam;
    private PayKeyBean mPayParam;
    private WXPayResultCallBack mCallback;

    public static final int NO_OR_LOW_WX = 1;   //未安装微信或微信版本过低
    public static final int ERROR_PAY_PARAM = 2;  //支付参数错误
    public static final int ERROR_PAY = 3;  //支付失败
    private final Activity mContext;
    private String mOrderNo;

    public interface WXPayResultCallBack {
        void onSuccess(); //支付成功

        void onError(int error_code);   //支付失败

        void onCancel();    //支付取消
    }

    private WXPay(Activity context) {
        TinkerBaseApplicationLike app = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
        mWXApi = app.getApi();
        mContext = context;
    }

    public static void init(Activity activity) {
        if (mWXPay == null) {
            mWXPay = new WXPay(activity);
        }
    }

    public static WXPay getInstance() {
        return mWXPay;
    }

    public IWXAPI getWXApi() {
        return mWXApi;
    }

    public void doPayDateObject(String orderNo, PayKeyBean pay_param, WXPayResultCallBack callback) {
        mOrderNo = orderNo;
        mPayParam = pay_param;
        mCallback = callback;
        if (!check()) {
            if (mCallback != null) {
                mCallback.onError(NO_OR_LOW_WX);
            }
            return;
        }

        PayReq req = new PayReq();
        req.appId = mPayParam.getAppid();
        req.partnerId = mPayParam.getPartnerid();
        req.prepayId = mPayParam.getPrepayid();
        req.packageValue = mPayParam.getPackageX();
        req.nonceStr = mPayParam.getNoncestr();
        req.timeStamp = mPayParam.getTimestamp();
        req.sign = mPayParam.getSign();

        updatePayResult(0, new Gson().toJson(pay_param));
        mWXApi.sendReq(req);
    }

    //支付回调响应
    public void onResp(String baseResp, int error_code) {
        if (mCallback == null) {
            return;
        }
        if (error_code == 0) {   //成功
            mCallback.onSuccess();
        } else if (error_code == -1) {   //错误
            mCallback.onError(ERROR_PAY);
        } else if (error_code == -2) {   //取消
            mCallback.onCancel();
        }

        updatePayResult(1, baseResp);
        mCallback = null;
    }

    //检测是否支持微信支付
    private boolean check() {
        return mWXApi.isWXAppInstalled() && mWXApi.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
    }

    private void updatePayResult(int type, String result) {
        Map<String, Object> map = new HashMap<>();
        map.put("orderNo", mOrderNo);
//        map.put("outTradeNo", outTradeNo);
//        map.put("tradeNo", tradeNo);
        map.put("result", result);
        map.put("type", type);//支付结果，0代表是请求支付，1代表是响应支付
        map.put("payType", 1);//0代表是app的支付宝支付，1代表是app的微信支付
        NetLoadUtils.getNetInstance().loadNetDataPost(mContext, Q_UPDATE_PAY_RESULT, map, null);
    }
}
