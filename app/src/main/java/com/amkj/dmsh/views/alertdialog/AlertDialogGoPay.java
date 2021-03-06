package com.amkj.dmsh.views.alertdialog;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseAlertDialog;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.mine.activity.ZeroIndentDetailActivity;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static android.view.View.GONE;
import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.dismissLoadhud;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showLoadhud;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_PRODUCT_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_PROPRIETOR_PRODUCT;
import static com.amkj.dmsh.constant.ConstantVariable.PAY_ALI_PAY;
import static com.amkj.dmsh.constant.ConstantVariable.PAY_UNION_PAY;
import static com.amkj.dmsh.constant.ConstantVariable.PAY_WX_PAY;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;

/**
 * Created by xiaoxin on 2020/4/3
 * Version:v4.5.0
 * ClassDescription :???????????????
 */
public class AlertDialogGoPay extends BaseAlertDialog {
    @BindView(R.id.ll_pay_ali)
    LinearLayout mLlPayAli;
    @BindView(R.id.ll_pay_we_chat)
    LinearLayout mLlPayWeChat;
    @BindView(R.id.ll_pay_union)
    LinearLayout mLlPayUnion;
    private String orderNo;
    private boolean isZero;//?????????0?????????
    private AppCompatActivity mActicity;
    private OnPaySuccessListener mOnPaySuccessListener;


    public AlertDialogGoPay(AppCompatActivity activity, Object object, String simpleName) {
        super(activity);
        mActicity = activity;
        isZero = ZeroIndentDetailActivity.class.getSimpleName().equals(simpleName);
        if (object instanceof List && ((List) object).size() > 0) {
            List payWays = (List) object;
            //??????
            if (!payWays.contains("1")) {
                mLlPayWeChat.setVisibility(GONE);
            }
            //?????????
            if (!payWays.contains("2")) {
                mLlPayAli.setVisibility(GONE);
            }
            //??????
            if (!payWays.contains("3")) {
                mLlPayUnion.setVisibility(GONE);
            }
        }
    }


    /**
     * ??????dialog
     */
    public void show(String orderNo) {
        show();
        this.orderNo = orderNo;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_indent_pay_pop;
    }

    @Override
    protected int getLayoutWith() {
        return AutoSizeUtils.mm2px(mAppContext, 540);
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
        showLoadhud(mActicity);
        Map<String, Object> params = new HashMap<>();
        params.put(isZero ? "orderNo" : "no", orderNo);
        params.put("userId", userId);
        //        2019.1.16 ??????????????????
        params.put("buyType", payWay);
        if (payWay.equals(PAY_UNION_PAY)) {
            params.put("paymentLinkType", 2);
            params.put("isApp", true);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(context, isZero ? Url.GET_ZERO_GO_PAY : Url.Q_PAYMENT_INDENT, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                dismissLoadhud(mActicity);

                if (payWay.equals(PAY_WX_PAY)) {
                    QualityCreateWeChatPayIndentBean qualityWeChatIndent = GsonUtils.fromJson(result, QualityCreateWeChatPayIndentBean.class);
                    if (qualityWeChatIndent != null) {
                        if (qualityWeChatIndent.getCode().equals(SUCCESS_CODE)) {
                            //???????????????????????????????????????
                            doWXPay(qualityWeChatIndent.getResult());
                        } else {
                            showToast(qualityWeChatIndent.getResult() == null
                                    ? qualityWeChatIndent.getMsg() : qualityWeChatIndent.getResult().getMsg());
                        }
                    }
                } else if (payWay.equals(PAY_ALI_PAY)) {
                    QualityCreateAliPayIndentBean qualityAliPayIndent = GsonUtils.fromJson(result, QualityCreateAliPayIndentBean.class);
                    if (qualityAliPayIndent != null) {
                        if (qualityAliPayIndent.getCode().equals(SUCCESS_CODE)) {
                            //??????????????????????????????????????????
                            doAliPay(qualityAliPayIndent.getResult());
                        } else {
                            showToast(qualityAliPayIndent.getResult() == null
                                    ? qualityAliPayIndent.getMsg() : qualityAliPayIndent.getResult().getMsg());
                        }
                    }
                } else if (payWay.equals(PAY_UNION_PAY)) {
                    QualityCreateUnionPayIndentEntity qualityUnionIndent = GsonUtils.fromJson(result, QualityCreateUnionPayIndentEntity.class);
                    if (qualityUnionIndent != null) {
                        if (qualityUnionIndent.getCode().equals(SUCCESS_CODE)) {
                            //???????????????????????????????????????
                            unionPay(qualityUnionIndent);
                        } else {
                            showToast(qualityUnionIndent.getQualityCreateUnionPayIndent() != null &&
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
                dismissLoadhud(mActicity);
                showToast(R.string.do_failed);
            }
        });
    }

    private void doWXPay(QualityCreateWeChatPayIndentBean.ResultBean pay_param) {
        WXPay.init(mActicity);//?????????????????????
        WXPay.getInstance().doPayDateObject(pay_param.getNo(), pay_param.getPayKey(), new WXPay.WXPayResultCallBack() {
            @Override
            public void onSuccess() {
                showToast("????????????");
                skipPaySuccess();
            }

            @Override
            public void onError(int error_code) {
                switch (error_code) {
                    case WXPay.NO_OR_LOW_WX:
                        showToast("????????????????????????????????????");
                        break;
                    case WXPay.ERROR_PAY_PARAM:
                        showToast("????????????");
                        break;
                    case WXPay.ERROR_PAY:
                        showToast("????????????");
                        break;
                }
            }

            @Override
            public void onCancel() {
                showToast("????????????");
            }
        });
    }

    private void skipPaySuccess() {
        if (!isZero) {
            if (mOnPaySuccessListener == null) {
                new LifecycleHandler(mActicity).postDelayed(() -> {
                    Intent intent = new Intent(context, DirectPaySuccessActivity.class);
                    intent.putExtra("indentNo", orderNo);
                    intent.putExtra(INDENT_PRODUCT_TYPE, INDENT_PROPRIETOR_PRODUCT);
                    context.startActivity(intent);
                }, 1000);
            } else {
                mOnPaySuccessListener.paySuccess();
            }
        }
    }

    public void setOnPaySuccessListener(OnPaySuccessListener paySuccessListener) {
        mOnPaySuccessListener = paySuccessListener;
    }

    public interface OnPaySuccessListener {
        void paySuccess();
    }

    private void doAliPay(QualityCreateAliPayIndentBean.ResultBean resultBean) {
        new AliPay(mActicity, resultBean.getNo(), resultBean.getPayKey(), new AliPay.AliPayResultCallBack() {
            @Override
            public void onSuccess() {
                showToast("????????????");
                skipPaySuccess();
            }

            @Override
            public void onDealing() {
                showToast("???????????????...");
            }

            @Override
            public void onError(int error_code) {
                switch (error_code) {
                    case AliPay.ERROR_RESULT:
                        showToast("????????????:????????????????????????");
                        break;
                    case AliPay.ERROR_NETWORK:
                        showToast("????????????:??????????????????");
                        break;
                    case AliPay.ERROR_PAY:
                        showToast("????????????:?????????????????????");
                        break;
                    default:
                        showToast("????????????");
                        break;
                }
            }

            @Override
            public void onCancel() {
                showToast("????????????");
            }
        }).doPay();
    }

    /**
     * ????????????
     *
     * @param qualityUnionIndent
     */
    private void unionPay(@NonNull QualityCreateUnionPayIndentEntity qualityUnionIndent) {
        if (qualityUnionIndent.getQualityCreateUnionPayIndent().getPayKeyBean() != null &&
                !TextUtils.isEmpty(qualityUnionIndent.getQualityCreateUnionPayIndent().getPayKeyBean().getPaymentUrl())) {
            new UnionPay(mActicity, qualityUnionIndent.getQualityCreateUnionPayIndent().getNo(),
                    qualityUnionIndent.getQualityCreateUnionPayIndent().getPayKeyBean().getPaymentUrl(),
                    new UnionPay.UnionPayResultCallBack() {
                        @Override
                        public void onUnionPaySuccess(String webResultValue) {
                            showToast("????????????");
                            skipPaySuccess();
                        }

                        @Override
                        public void onUnionPayError(String errorMes) {
                            showToast("????????????");
                        }
                    });
        } else {
            showToast("???????????????????????????????????????????????????");
        }
    }
}
