package com.amkj.dmsh.views;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.address.activity.SelectedAddressActivity;
import com.amkj.dmsh.bean.OrderProductNewBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dao.GroupDao;
import com.amkj.dmsh.dao.OrderDao;
import com.amkj.dmsh.dominant.bean.GroupShopDetailsEntity.GroupShopDetailsBean;
import com.amkj.dmsh.find.activity.IndentScoreListActivity;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.activity.DirectApplyRefundActivity;
import com.amkj.dmsh.shopdetails.activity.DirectExchangeDetailsActivity;
import com.amkj.dmsh.shopdetails.activity.DirectIndentWriteActivity;
import com.amkj.dmsh.shopdetails.activity.DirectLogisticsDetailsActivity;
import com.amkj.dmsh.shopdetails.activity.InvoiceDetailActivity;
import com.amkj.dmsh.shopdetails.activity.SelectRefundGoodsActivity;
import com.amkj.dmsh.shopdetails.activity.SelectRefundTypeActivity;
import com.amkj.dmsh.shopdetails.adapter.MainOrderButtonAdapter;
import com.amkj.dmsh.shopdetails.bean.ButtonListBean;
import com.amkj.dmsh.shopdetails.bean.MainOrderListEntity.MainOrderBean;
import com.amkj.dmsh.shopdetails.bean.RefundProductsEntity;
import com.amkj.dmsh.shopdetails.dialog.AlertDialogGoPay;
import com.amkj.dmsh.shopdetails.dialog.AlertDialogWheel;
import com.amkj.dmsh.utils.SharedPreUtils;
import com.amkj.dmsh.utils.WindowUtils;
import com.amkj.dmsh.utils.alertdialog.AlertDialogHelper;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.dismissLoadhud;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.isContextExisted;
import static com.amkj.dmsh.constant.ConstantMethod.showLoadhud;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.skipRefundAspect;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.ADD_CART;
import static com.amkj.dmsh.constant.ConstantVariable.APPLY_REFUND;
import static com.amkj.dmsh.constant.ConstantVariable.BATCH_REFUND;
import static com.amkj.dmsh.constant.ConstantVariable.CANCEL_ORDER_NEW;
import static com.amkj.dmsh.constant.ConstantVariable.CHANGE_ORDER_ADDRESS;
import static com.amkj.dmsh.constant.ConstantVariable.CHECK_LOGISTICS;
import static com.amkj.dmsh.constant.ConstantVariable.CHECK_REFUND_LOGISTICS;
import static com.amkj.dmsh.constant.ConstantVariable.CONFIRM_TAKE;
import static com.amkj.dmsh.constant.ConstantVariable.CUSTOMER_SERVICE_DETAIL;
import static com.amkj.dmsh.constant.ConstantVariable.DELAY_TAKE;
import static com.amkj.dmsh.constant.ConstantVariable.DELETE_ORDER;
import static com.amkj.dmsh.constant.ConstantVariable.DEMO_LIFE_FILE;
import static com.amkj.dmsh.constant.ConstantVariable.EDIT_ADDRESS;
import static com.amkj.dmsh.constant.ConstantVariable.GO_PAY;
import static com.amkj.dmsh.constant.ConstantVariable.INVITE_JOIN_GROUP;
import static com.amkj.dmsh.constant.ConstantVariable.REFUND_ASPECT;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TO_BUY;
import static com.amkj.dmsh.constant.ConstantVariable.URGE_REFUND;
import static com.amkj.dmsh.constant.ConstantVariable.VIEW_INVOICE;
import static com.amkj.dmsh.constant.ConstantVariable.WAITDELIVERY;
import static com.amkj.dmsh.constant.ConstantVariable.WAIT_COMMENT;

/**
 * Created by xiaoxin on 2020/3/25
 * Version:v4.4.3
 * ClassDescription :主订单按钮组合控件
 */
public class MainButtonView extends LinearLayout {
    @BindView(R.id.tv_more_button)
    TextView tv_more_button;
    @BindView(R.id.rv_button)
    RecyclerView rv_button;
    private AlertDialogHelper cancelOrderDialogHelper;
    private AlertDialogHelper confirmOrderDialogHelper;
    private AlertDialogHelper delOrderDialogHelper;
    private PopupWindow mPwMoreButton;
    private PopupWindow mPwScore;
    private int buttonLimit = 4;
    private AppCompatActivity context;
    private AlertDialogGoPay mAlertDialogGoPay;

    public MainButtonView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View headView = LayoutInflater.from(context).inflate(R.layout.view_main_button, this, true);
        ButterKnife.bind(this, headView);
    }

    public void updateView(AppCompatActivity activity, MainOrderBean mainOrderBean, String simpleName) {
        updateView(activity, mainOrderBean, simpleName, null);
    }

    public void updateView(AppCompatActivity activity, MainOrderBean mainOrderBean, String simpleName, AlertDialogWheel.ClickConfirmListener confirmListener) {
        this.context = activity;
        boolean isExchangeDetail = simpleName.equals(DirectExchangeDetailsActivity.class.getSimpleName());
        if (!mainOrderBean.isSort()) {// 倒序排列
            Collections.reverse(mainOrderBean.getButtonList());
            mainOrderBean.setSort(true);
        }
        List<ButtonListBean> buttonList = mainOrderBean.getButtonList();
        String orderNo = mainOrderBean.getOrderNo();
        List<OrderProductNewBean> orderProductList = mainOrderBean.getOrderProductList();
        //订单详情不用展示更多按钮
        tv_more_button.setVisibility(buttonList != null && buttonList.size() > buttonLimit && !isExchangeDetail ? View.VISIBLE : View.GONE);
        if (buttonList != null && buttonList.size() > 0) {
            setVisibility(View.VISIBLE);
            if (rv_button.getItemDecorationCount() == 0) {
                rv_button.addItemDecoration(new ItemDecoration.Builder().setDividerId(R.drawable.item_divider_ten_white).create());
            }
            rv_button.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            rv_button.setNestedScrollingEnabled(false);
            MainOrderButtonAdapter buttonAdapter = new MainOrderButtonAdapter(context, buttonList.subList(buttonList.size() <= buttonLimit || isExchangeDetail ? 0 : buttonList.size() - buttonLimit, buttonList.size()));
            rv_button.setAdapter(buttonAdapter);
            BaseQuickAdapter.OnItemClickListener onItemClickListener = (adapter, view, position) -> {
                if (userId <= 0 || !isContextExisted(context)) {
                    getLoginStatus(context);
                    return;
                }

                if (mPwMoreButton != null) {
                    WindowUtils.closePw(mPwMoreButton);
                }

                if (confirmListener != null) {
                    confirmListener.confirm("", "");
                }
                ButtonListBean buttonListBean = (ButtonListBean) view.getTag();
                if (buttonListBean != null) {
                    Intent intent = new Intent();
                    switch (buttonListBean.getId()) {
                        //确认收货
                        case CONFIRM_TAKE:
                            if (confirmOrderDialogHelper == null) {
                                confirmOrderDialogHelper = new AlertDialogHelper(context);
                                confirmOrderDialogHelper.setTitleVisibility(View.GONE).setMsgTextGravity(Gravity.CENTER)
                                        .setMsg("确定已收到货物?").setCancelText("取消").setConfirmText("确定")
                                        .setCancelTextColor(context.getResources().getColor(R.color.text_login_gray_s));
                            }
                            confirmOrderDialogHelper.setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                                @Override
                                public void confirm() {
                                    OrderDao.confirmOrder(context, orderNo, simpleName);
                                }

                                @Override
                                public void cancel() {
                                }
                            });
                            confirmOrderDialogHelper.show();
                            break;
                        //查看物流
                        case CHECK_LOGISTICS:
                            intent.setClass(context, DirectLogisticsDetailsActivity.class);
                            intent.putExtra("orderNo", orderNo);
                            context.startActivity(intent);
                            break;
                        //退货物流
                        case CHECK_REFUND_LOGISTICS:
                            intent.setClass(context, DirectLogisticsDetailsActivity.class);
                            intent.putExtra("refundNo", mainOrderBean.getRefundNo());
                            context.startActivity(intent);
                            break;
                        //延迟收货
                        case DELAY_TAKE:
                            OrderDao.delayTakeTime(context, orderNo);
                            break;
                        //订单列表申请退款
                        case APPLY_REFUND:
                            getRefundProducts(mainOrderBean, isExchangeDetail);
                            break;
                        //订单详情批量退款
                        case BATCH_REFUND:
                            getRefundProducts(mainOrderBean, isExchangeDetail);
                            break;
                        //钱款去向
                        case REFUND_ASPECT:
                            skipRefundAspect(context, mainOrderBean.getRefundSuccessCount(), mainOrderBean.getOrderNo(), mainOrderBean.getRefundNo());
                            break;
                        //售后详情
                        case CUSTOMER_SERVICE_DETAIL:
                            int refundCount = mainOrderBean.getRefundCount();
                            if (refundCount > 1) {
                                //跳转订单详情
                                intent.setClass(context, DirectExchangeDetailsActivity.class);
                                intent.putExtra("orderNo", mainOrderBean.getOrderNo());
                                context.startActivity(intent);
                            } else {
                                //跳转售后详情
                                ConstantMethod.skipRefundDetail(context, mainOrderBean.getRefundNo());
                            }
                            break;
                        //待点评
                        case WAIT_COMMENT:
                            int joinCount = (int) SharedPreUtils.getParam(DEMO_LIFE_FILE, "IndentJoinCount", 0);
                            if (joinCount < 2) {
                                if (mPwScore == null) {
                                    mPwScore = WindowUtils.getFullPw(context, R.layout.pw_join_tips, Gravity.CENTER);
                                }
                                mPwScore.getContentView().setOnClickListener((View v1) -> {
                                    mPwScore.dismiss();
                                    //写点评
                                    skipIndentScoreList(orderNo);
                                });
                                WindowUtils.showPw(context, mPwScore, Gravity.CENTER);
                                SharedPreUtils.setParam(DEMO_LIFE_FILE, "IndentJoinCount", joinCount + 1);
                            } else {
                                //写点评
                                skipIndentScoreList(orderNo);
                            }
                            break;
                        //提醒发货
                        case WAITDELIVERY:
                            OrderDao.setRemindDelivery(context, orderNo);
                            break;
                        //订单商品加入购物车
                        case ADD_CART:
                            OrderDao.addOrderCart(context, orderNo);
                            break;
                        //删除订单
                        case DELETE_ORDER:
                            if (delOrderDialogHelper == null) {
                                delOrderDialogHelper = new AlertDialogHelper(context);
                                delOrderDialogHelper.setTitleVisibility(View.GONE).setMsgTextGravity(Gravity.CENTER)
                                        .setMsg("确定要删除该订单？").setCancelText("取消").setConfirmText("确定")
                                        .setCancelTextColor(context.getResources().getColor(R.color.text_login_gray_s));
                            }
                            delOrderDialogHelper.setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                                @Override
                                public void confirm() {
                                    OrderDao.delOrder(context, orderNo, simpleName);
                                }

                                @Override
                                public void cancel() {
                                }
                            });
                            delOrderDialogHelper.show();
                            break;
                        //取消订单（待支付）
                        case CANCEL_ORDER_NEW:
                            if (cancelOrderDialogHelper == null) {
                                cancelOrderDialogHelper = new AlertDialogHelper(context);
                                cancelOrderDialogHelper.setTitleVisibility(View.GONE).setMsgTextGravity(Gravity.CENTER)
                                        .setMsg("确定要取消当前订单？").setCancelText("取消").setConfirmText("确定")
                                        .setCancelTextColor(context.getResources().getColor(R.color.text_login_gray_s));
                            }
                            cancelOrderDialogHelper.setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                                @Override
                                public void confirm() {
                                    OrderDao.cancelOrder(context, orderNo, simpleName);
                                }

                                @Override
                                public void cancel() {
                                }
                            });
                            cancelOrderDialogHelper.show();
                            break;
                        //去支付
                        case GO_PAY:
                            goPay(orderNo);
                            break;
                        //再次购买
                        case TO_BUY:
                            intent.setClass(context, DirectIndentWriteActivity.class);
                            intent.putExtra("orderNo", orderNo);
                            context.startActivity(intent);
                            break;
                        //邀请参团
                        case INVITE_JOIN_GROUP:
                            if (orderProductList != null && orderProductList.size() > 0) {
                                OrderProductNewBean orderProductListBean = orderProductList.get(0);
                                GroupShopDetailsBean groupShopDetailsBean = new GroupShopDetailsBean();
                                groupShopDetailsBean.setCoverImage(orderProductListBean.getPicUrl());
                                groupShopDetailsBean.setGpName(orderProductListBean.getProductName());
                                groupShopDetailsBean.setType(mainOrderBean.getType());
                                GroupDao.invitePartnerGroup(context, groupShopDetailsBean, mainOrderBean.getOrderNo());
                            }
                            break;
                        //查看发票
                        case VIEW_INVOICE:
                            intent.setClass(context, InvoiceDetailActivity.class);
                            intent.putExtra("orderNo", mainOrderBean.getOrderNo());
                            context.startActivity(intent);
                            break;
                        //催促退款
                        case URGE_REFUND:
                            OrderDao.urgeRefund(context, mainOrderBean.getRefundNo());
                            break;
                        //修改地址
                        case EDIT_ADDRESS:
                            intent = new Intent(context, SelectedAddressActivity.class);
                            intent.putExtra("orderNo", mainOrderBean.getOrderNo());
                            context.startActivityForResult(intent, CHANGE_ORDER_ADDRESS);
                            break;
                    }
                }
            };
            buttonAdapter.setOnItemClickListener(onItemClickListener);

            tv_more_button.setOnClickListener(v -> {
                mPwMoreButton = WindowUtils.getAlphaPw(context, R.layout.pw_indent_more_button, Gravity.CENTER);
                RecyclerView rvMore = mPwMoreButton.getContentView().findViewById(R.id.rv_more_button);
                rvMore.setLayoutManager(new LinearLayoutManager(context));
                if (rvMore.getItemDecorationCount() == 0) {
                    rvMore.addItemDecoration(new ItemDecoration.Builder().setDividerId(R.drawable.item_divider_gray_f_one_px).create());
                }
                MainOrderButtonAdapter buttonAdapter1 = new MainOrderButtonAdapter(context, buttonList.subList(0, buttonList.size() - buttonLimit), true);
                rvMore.setAdapter(buttonAdapter1);
                buttonAdapter1.setOnItemClickListener(onItemClickListener);
                mPwMoreButton.showAsDropDown(tv_more_button, 0, 0);
            });
        } else {
            setVisibility(View.GONE);
        }
    }

    private void goPay(String orderNo) {
        showLoadhud(context);
        NetLoadUtils.getNetInstance().loadNetDataPost(context, Url.GET_PAYTYPE_LIST, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                dismissLoadhud(context);
                Map map = new Gson().fromJson(result, Map.class);
                if (mAlertDialogGoPay == null) {
                    mAlertDialogGoPay = new AlertDialogGoPay(context, map.get("result"));
                }
                mAlertDialogGoPay.show(orderNo);
            }

            @Override
            public void onNotNetOrException() {
                dismissLoadhud(context);
            }
        });
    }


    private void getRefundProducts(MainOrderBean mainOrderBean, boolean isExchangeDetail) {
        showLoadhud(context);
        Map<String, Object> map = new HashMap<>();
        map.put("orderNo", mainOrderBean.getOrderNo());
        NetLoadUtils.getNetInstance().loadNetDataPost(context, Url.Q_GET_REFUND_REFUND_PRODUCTS, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                dismissLoadhud(context);
                Intent intent1 = new Intent();
                RefundProductsEntity refundProductsEntity = new Gson().fromJson(result, RefundProductsEntity.class);
                if (refundProductsEntity != null) {
                    String code = refundProductsEntity.getCode();
                    String msg = refundProductsEntity.getMsg();
                    if (SUCCESS_CODE.equals(code)) {
                        List<OrderProductNewBean> refundProducts = refundProductsEntity.getRefundProducts();
                        if (refundProducts != null && refundProducts.size() > 0) {
                            //单件商品申请退款
                            if (refundProducts.size() == 1) {
                                int status = refundProducts.get(0).getStatus();
                                //待发货直接跳转申请退款界面,否则跳转选择售后类型界面
                                if (status == 10) {
                                    intent1.setClass(context, DirectApplyRefundActivity.class);
                                    intent1.putExtra("refundType", ConstantVariable.NOGOODS_REFUND);
                                } else {
                                    intent1.setClass(context, SelectRefundTypeActivity.class);
                                }
                            } else {
                                //多件商品申请退款
                                intent1.setClass(context, SelectRefundGoodsActivity.class);
                                intent1.putExtra("refundType", ConstantVariable.NOGOODS_REFUND);
                            }

                            intent1.putExtra("orderNo", mainOrderBean.getOrderNo());
                            intent1.putExtra("goods", new Gson().toJson(refundProducts));
                            context.startActivity(intent1);
                        }
                    } else {
                        if (isExchangeDetail) {
                            showToast(context, msg);
                        } else {
                            //跳转订单详情
                            intent1 = new Intent(context, DirectExchangeDetailsActivity.class);
                            intent1.putExtra("orderNo", mainOrderBean.getOrderNo());
                            context.startActivity(intent1);
                        }
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                dismissLoadhud(context);
                showToast(mAppContext, R.string.do_failed);
            }
        });
    }

    private void skipIndentScoreList(String orderNo) {
        //跳转评分列表
        Intent intent = new Intent(context, IndentScoreListActivity.class);
        intent.putExtra("orderNo", orderNo);
        context.startActivity(intent);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        WindowUtils.closePw(mPwScore);
        WindowUtils.closePw(mPwMoreButton);
        if (cancelOrderDialogHelper != null) {
            cancelOrderDialogHelper.dismiss();
        }
        if (confirmOrderDialogHelper != null) {
            confirmOrderDialogHelper.dismiss();
        }
        if (delOrderDialogHelper != null) {
            delOrderDialogHelper.dismiss();
        }
        if (mAlertDialogGoPay != null) {
            mAlertDialogGoPay.dismiss();
        }
    }
}
