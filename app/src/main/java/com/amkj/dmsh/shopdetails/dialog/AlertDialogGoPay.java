package com.amkj.dmsh.shopdetails.dialog;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.amkj.dmsh.R;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.activity.DirectPaySuccessActivity;
import com.amkj.dmsh.shopdetails.bean.QualityCreateAliPayIndentBean;
import com.amkj.dmsh.shopdetails.bean.QualityCreateUnionPayIndentEntity;
import com.amkj.dmsh.shopdetails.bean.QualityCreateWeChatPayIndentBean;
import com.amkj.dmsh.shopdetails.payutils.AliPay;
import com.amkj.dmsh.shopdetails.payutils.UnionPay;
import com.amkj.dmsh.shopdetails.payutils.WXPay;
import com.amkj.dmsh.utils.LifecycleHandler;
import com.amkj.dmsh.utils.gson.GsonUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.jessyan.autosize.AutoSize;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static android.view.View.GONE;
import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.dismissLoadhud;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.isContextExisted;
import static com.amkj.dmsh.constant.ConstantMethod.showLoadhud;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_PRODUCT_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_PROPRIETOR_PRODUCT;
import static com.amkj.dmsh.constant.ConstantVariable.PAY_ALI_PAY;
import static com.amkj.dmsh.constant.ConstantVariable.PAY_UNION_PAY;
import static com.amkj.dmsh.constant.ConstantVariable.PAY_WX_PAY;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.Q_PAYMENT_INDENT;

/**
 * Created by xiaoxin on 2020/4/3
 * Version:v4.5.0
 * ClassDescription :去支付弹窗
 */
public class AlertDialogGoPay {
    @BindView(R.id.ll_pay_ali)
    LinearLayout mLlPayAli;
    @BindView(R.id.ll_pay_we_chat)
    LinearLayout mLlPayWeChat;
    @BindView(R.id.ll_pay_union)
    LinearLayout mLlPayUnion;
    private AppCompatActivity context;
    private AlertDialog imageAlertDialog;
    private View dialogView;
    private boolean isFirstSet = true;
    private String orderNo;


    public AlertDialogGoPay(AppCompatActivity context, Object object) {
        if (!isContextExisted(context)) {
            return;
        }
        this.context = context;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        dialogView = LayoutInflater.from(context).inflate(R.layout.layout_indent_pay_pop, null, false);
        imageAlertDialog = builder.create();
        ButterKnife.bind(this, dialogView);
        if (object instanceof List && ((List) object).size() > 0) {
            List payWays = (List) object;
            //微信
            if (!payWays.contains("1")) {
                mLlPayWeChat.setVisibility(GONE);
            }
            //支付宝
            if (!payWays.contains("2")) {
                mLlPayAli.setVisibility(GONE);
            }
            //银联
            if (!payWays.contains("3")) {
                mLlPayUnion.setVisibility(GONE);
            }
        }
    }

    @OnClick({R.id.ll_pay_ali, R.id.ll_pay_we_chat, R.id.ll_pay_union, R.id.tv_pay_cancel})
    public void onViewClicked(View view) {
        dismiss();
        switch (view.getId()) {
            case R.id.ll_pay_ali:
                paymentIndent(PAY_ALI_PAY);
                break;
            case R.id.ll_pay_we_chat:
                paymentIndent(PAY_WX_PAY);
                break;
            case R.id.ll_pay_union:
                paymentIndent(PAY_UNION_PAY);
                break;
        }
    }

    private void paymentIndent(String payWay) {
        showLoadhud(context);
        Map<String, Object> params = new HashMap<>();
        params.put("no", orderNo);
        params.put("userId", userId);
        //        2019.1.16 新增银联支付
        params.put("buyType", payWay);
        if (payWay.equals(PAY_UNION_PAY)) {
            params.put("paymentLinkType", 2);
            params.put("isApp", true);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(context, Q_PAYMENT_INDENT, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                dismissLoadhud(context);

                if (payWay.equals(PAY_WX_PAY)) {
                    QualityCreateWeChatPayIndentBean qualityWeChatIndent = GsonUtils.fromJson(result, QualityCreateWeChatPayIndentBean.class);
                    if (qualityWeChatIndent != null) {
                        if (qualityWeChatIndent.getCode().equals(SUCCESS_CODE)) {
                            //返回成功，调起微信支付接口
                            doWXPay(qualityWeChatIndent.getResult());
                        } else {
                            showToast( qualityWeChatIndent.getResult() == null
                                    ? qualityWeChatIndent.getMsg() : qualityWeChatIndent.getResult().getMsg());
                        }
                    }
                } else if (payWay.equals(PAY_ALI_PAY)) {
                    QualityCreateAliPayIndentBean qualityAliPayIndent = GsonUtils.fromJson(result, QualityCreateAliPayIndentBean.class);
                    if (qualityAliPayIndent != null) {
                        if (qualityAliPayIndent.getCode().equals(SUCCESS_CODE)) {
                            //返回成功，调起支付宝支付接口
                            doAliPay(qualityAliPayIndent.getResult());
                        } else {
                            showToast( qualityAliPayIndent.getResult() == null
                                    ? qualityAliPayIndent.getMsg() : qualityAliPayIndent.getResult().getMsg());
                        }
                    }
                } else if (payWay.equals(PAY_UNION_PAY)) {
                    QualityCreateUnionPayIndentEntity qualityUnionIndent = GsonUtils.fromJson(result, QualityCreateUnionPayIndentEntity.class);
                    if (qualityUnionIndent != null) {
                        if (qualityUnionIndent.getCode().equals(SUCCESS_CODE)) {
                            //返回成功，调起银联支付接口
                            unionPay(qualityUnionIndent);
                        } else {
                            showToast( qualityUnionIndent.getQualityCreateUnionPayIndent() != null &&
                                    qualityUnionIndent.getQualityCreateUnionPayIndent() != null &&
                                    !TextUtils.isEmpty(qualityUnionIndent.getQualityCreateUnionPayIndent().getMsg()) ?
                                    getStrings(qualityUnionIndent.getQualityCreateUnionPayIndent().getMsg()) :
                                    getStrings(qualityUnionIndent.getMsg()));
                        }
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                dismissLoadhud(context);
                showToast(R.string.do_failed);
            }
        });
    }

    private void doWXPay(QualityCreateWeChatPayIndentBean.ResultBean pay_param) {
        WXPay.init(context);//要在支付前调用
        WXPay.getInstance().doPayDateObject(pay_param.getNo(), pay_param.getPayKey(), new WXPay.WXPayResultCallBack() {
            @Override
            public void onSuccess() {
                showToast("支付成功");
                skipPaySuccess();
            }

            @Override
            public void onError(int error_code) {
                switch (error_code) {
                    case WXPay.NO_OR_LOW_WX:
                        showToast("未安装微信或微信版本过低");
                        break;
                    case WXPay.ERROR_PAY_PARAM:
                        showToast("参数错误");
                        break;
                    case WXPay.ERROR_PAY:
                        showToast("支付失败");
                        break;
                }
            }

            @Override
            public void onCancel() {
                showToast("支付取消");
            }
        });
    }

    private void skipPaySuccess() {
        new LifecycleHandler(context).postDelayed(() -> {
            Intent intent = new Intent(context, DirectPaySuccessActivity.class);
            intent.putExtra("indentNo", orderNo);
            intent.putExtra(INDENT_PRODUCT_TYPE, INDENT_PROPRIETOR_PRODUCT);
            context.startActivity(intent);
        }, 1000);
    }

    private void doAliPay(QualityCreateAliPayIndentBean.ResultBean resultBean) {
        new AliPay(context, resultBean.getNo(), resultBean.getPayKey(), new AliPay.AliPayResultCallBack() {
            @Override
            public void onSuccess() {
                showToast("支付成功");
                skipPaySuccess();
            }

            @Override
            public void onDealing() {
                showToast("支付处理中...");
            }

            @Override
            public void onError(int error_code) {
                switch (error_code) {
                    case AliPay.ERROR_RESULT:
                        showToast("支付失败:支付结果解析错误");
                        break;
                    case AliPay.ERROR_NETWORK:
                        showToast("支付失败:网络连接错误");
                        break;
                    case AliPay.ERROR_PAY:
                        showToast("支付错误:支付码支付失败");
                        break;
                    default:
                        showToast("支付错误");
                        break;
                }
            }

            @Override
            public void onCancel() {
                showToast("支付取消");
            }
        }).doPay();
    }

    /**
     * 银联支付
     *
     * @param qualityUnionIndent
     */
    private void unionPay(@NonNull QualityCreateUnionPayIndentEntity qualityUnionIndent) {
        if (qualityUnionIndent.getQualityCreateUnionPayIndent().getPayKeyBean() != null &&
                !TextUtils.isEmpty(qualityUnionIndent.getQualityCreateUnionPayIndent().getPayKeyBean().getPaymentUrl())) {
            new UnionPay(context, qualityUnionIndent.getQualityCreateUnionPayIndent().getNo(),
                    qualityUnionIndent.getQualityCreateUnionPayIndent().getPayKeyBean().getPaymentUrl(),
                    new UnionPay.UnionPayResultCallBack() {
                        @Override
                        public void onUnionPaySuccess(String webResultValue) {
                            showToast("支付成功");
                            skipPaySuccess();
                        }

                        @Override
                        public void onUnionPayError(String errorMes) {
                            showToast("支付取消");
                        }
                    });
        } else {
            showToast("缺少重要参数，请选择其它支付渠道！");
        }
    }

    /**
     * 展示dialog
     */
    public void show(String orderNo) {
        if (imageAlertDialog == null) {
            return;
        }

        this.orderNo = orderNo;
        if (!imageAlertDialog.isShowing() && isContextExisted(context)) {
            AutoSize.autoConvertDensityOfGlobal(context);
            imageAlertDialog.show();
        }
        if (isFirstSet) {
            Window window = imageAlertDialog.getWindow();
            if (window != null) {
                window.setBackgroundDrawableResource(android.R.color.transparent);
                WindowManager.LayoutParams params = window.getAttributes();
                params.width = AutoSizeUtils.mm2px(mAppContext, 540);
                window.setAttributes(params);
                window.setContentView(dialogView);
            }
        }
        isFirstSet = false;
    }

    public void dismiss() {
        if (imageAlertDialog != null
                && isContextExisted(context) && imageAlertDialog.isShowing()) {
            imageAlertDialog.dismiss();
        }
    }
}
