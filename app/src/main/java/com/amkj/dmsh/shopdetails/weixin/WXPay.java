package com.amkj.dmsh.shopdetails.weixin;

import android.content.Context;

import com.amkj.dmsh.base.BaseApplication;
import com.amkj.dmsh.shopdetails.bean.QualityCreateWeChatPayIndentBean.ResultBean.PayKeyBean;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;


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

    public interface WXPayResultCallBack {
        void onSuccess(); //支付成功

        void onError(int error_code);   //支付失败

        void onCancel();    //支付取消
    }

    public WXPay(Context context) {
        BaseApplication app = (BaseApplication) context;
        mWXApi = app.getApi();
    }

    public static void init(Context context) {
        if (mWXPay == null) {
            mWXPay = new WXPay(context);
        }
    }

    public static WXPay getInstance() {
        return mWXPay;
    }

    public IWXAPI getWXApi() {
        return mWXApi;
    }

    public void doPayDateObject(PayKeyBean pay_param, WXPayResultCallBack callback) {
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

        mWXApi.sendReq(req);
    }

    //支付回调响应
    public void onResp(int error_code) {
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

        mCallback = null;
    }

    //检测是否支持微信支付
    private boolean check() {
        return mWXApi.isWXAppInstalled() && mWXApi.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
    }
}
