package com.amkj.dmsh.shopdetails.adapter;

import android.app.Activity;
import android.arch.lifecycle.LifecycleOwner;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dominant.bean.GroupShopDetailsEntity.GroupShopDetailsBean;
import com.amkj.dmsh.mine.adapter.ShopCarComPreProAdapter;
import com.amkj.dmsh.mine.bean.ActivityInfoBean;
import com.amkj.dmsh.mine.bean.CartProductInfoBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.activity.DirectApplyRefundActivity;
import com.amkj.dmsh.shopdetails.activity.DirectExchangeDetailsActivity;
import com.amkj.dmsh.shopdetails.activity.DoMoRefundDetailActivity;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.shopdetails.bean.ApplyRefundCheckEntity;
import com.amkj.dmsh.shopdetails.bean.ApplyRefundCheckEntity.ApplyRefundCheckBean;
import com.amkj.dmsh.shopdetails.bean.DirectApplyRefundBean;
import com.amkj.dmsh.shopdetails.bean.DirectApplyRefundBean.DirectRefundProBean;
import com.amkj.dmsh.shopdetails.bean.IndentInfoDetailEntity.IndentInfoDetailBean.OrderDetailBean.GoodsDetailBean.OrderProductInfoBean;
import com.amkj.dmsh.shopdetails.bean.IndentWriteEntity.IndentWriteBean.ProductsBean.ProductInfoBean;
import com.amkj.dmsh.shopdetails.bean.InquiryOrderEntry.OrderInquiryDateEntry.OrderListBean;
import com.amkj.dmsh.shopdetails.bean.PresentProductOrder;
import com.amkj.dmsh.shopdetails.bean.RefundApplyEntity.RefundApplyBean.ProductsBean;
import com.amkj.dmsh.utils.CountDownTimer;
import com.amkj.dmsh.utils.alertdialog.AlertDialogHelper;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.amkj.dmsh.constant.ConstantMethod.getFloatNumber;
import static com.amkj.dmsh.constant.ConstantMethod.getSpannableString;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsChNPrice;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsFormat;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_PRO_STATUS;
import static com.amkj.dmsh.constant.ConstantVariable.REFUND_REPAIR;
import static com.amkj.dmsh.constant.ConstantVariable.REFUND_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_0;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_1;
import static com.amkj.dmsh.shopdetails.activity.DirectApplyRefundActivity.APPLY_REFUND;
import static com.amkj.dmsh.shopdetails.activity.DirectExchangeDetailsActivity.INDENT_DETAILS_TYPE;
import static com.amkj.dmsh.shopdetails.activity.DirectIndentInvoiceActivity.INDENT_INVOICE;
import static com.amkj.dmsh.shopdetails.activity.DirectIndentWriteActivity.INDENT_GROUP_SHOP;
import static com.amkj.dmsh.shopdetails.activity.DirectIndentWriteActivity.INDENT_W_TYPE;
import static com.amkj.dmsh.shopdetails.activity.DoMoIndentAllActivity.INDENT_TYPE;


/**
 * Created by atd48 on 2016/8/17.
 */
public class DirectProductListAdapter extends BaseQuickAdapter<Object, BaseViewHolder> {
    private final String type;
    private final Activity context;
    private List<CartProductInfoBean> preComProInfoBeanList = new ArrayList<>();
    private AlertDialogHelper refundOrderDialogHelper;

    public DirectProductListAdapter(Activity context, List list, String type) {
        super(R.layout.layout_direct_indent_product_item, list);
        this.context = context;
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, Object item) {
        if (item == null) return;
        TextView tv_direct_indent_product_price = helper.getView(R.id.tv_direct_indent_pro_price);
        tv_direct_indent_product_price.setSelected(false);
        switch (type) {
            case INDENT_TYPE:
                //            订单列表
                OrderListBean.GoodsBean goodsBean = (OrderListBean.GoodsBean) item;
                GlideImageLoaderUtil.loadCenterCrop(context, helper.getView(R.id.iv_direct_indent_pro), goodsBean.getPicUrl());
                helper.setText(R.id.tv_direct_indent_pro_name, getStrings(goodsBean.getName()))
                        .setText(R.id.tv_direct_indent_pro_sku, getStrings(goodsBean.getSaleSkuValue()));
                tv_direct_indent_product_price.setSelected(false);
                setIndentProStatus(helper, goodsBean);
                String priceChnText;
                if (goodsBean.getCombineProductInfoList() != null && goodsBean.getCombineProductInfoList().size() > 0) {
                    helper.setGone(R.id.tv_direct_pro_count, goodsBean.getStatus() == 0)
                            .setText(R.id.tv_direct_pro_count, "x" + goodsBean.getCount());
                    //组合价为0时不显示
                    if (TextUtils.isEmpty(goodsBean.getPrice()) || "0".equals(goodsBean.getPrice())) {
                        priceChnText = "";
                    } else {
                        priceChnText = context.getString(R.string.combine_price) + getStringsChNPrice(context, goodsBean.getPrice());
                    }
                } else {
                    priceChnText = getStringsChNPrice(context, goodsBean.getPrice());
                    helper.setText(R.id.tv_direct_pro_count, "x" + goodsBean.getCount());
                }

                tv_direct_indent_product_price.setText(priceChnText);

                //组合购以及赠品
                if (goodsBean.getCombineProductInfoList() != null
                        || goodsBean.getPresentProductInfoList() != null) {
                    helper.setGone(R.id.rel_indent_com_pre_pro, true);
                    preComProInfoBeanList = new ArrayList<>();
                    if (goodsBean.getPresentProductInfoList() != null && goodsBean.getPresentProductInfoList().size() > 0) {
                        preComProInfoBeanList.addAll(goodsBean.getPresentProductInfoList());
                    }
                    if (goodsBean.getCombineProductInfoList() != null && goodsBean.getCombineProductInfoList().size() > 0) {
                        for (int i = 0; i < goodsBean.getCombineProductInfoList().size(); i++) {
                            CartProductInfoBean cartProductInfoBean = goodsBean.getCombineProductInfoList().get(i);
                            cartProductInfoBean.setItemType(TYPE_1);
                            if (goodsBean.getStatus() == 0) {
                                cartProductInfoBean.setCount(0);
                            }
                            cartProductInfoBean.setIndentType(INDENT_TYPE);
                            preComProInfoBeanList.add(cartProductInfoBean);
                        }
                    }
                    setComPreData(helper, preComProInfoBeanList, true, true, false);
                } else {
                    helper.setGone(R.id.rel_indent_com_pre_pro, false);
                }

                //普通赠品信息
                PresentProductOrder presentProductOrder = goodsBean.getPresentProductOrder();
                helper.setGone(R.id.ll_present, presentProductOrder != null);
                if (presentProductOrder != null) {
                    helper.setText(R.id.tv_name, presentProductOrder.getPresentName())
                            .setText(R.id.tv_count, "x" + presentProductOrder.getPresentCount());
                    GlideImageLoaderUtil.loadCenterCrop(context, helper.getView(R.id.iv_cover), presentProductOrder.getPresentPicUrl());
                }
                helper.itemView.setTag(goodsBean);
                break;
            case INDENT_DETAILS_TYPE:
                //            商品订单详情
                OrderProductInfoBean orderProductInfoBean = (OrderProductInfoBean) item;
                GlideImageLoaderUtil.loadCenterCrop(context, (ImageView) helper.getView(R.id.iv_direct_indent_pro), orderProductInfoBean.getPicUrl());
                helper.setText(R.id.tv_direct_indent_pro_name, getStrings(orderProductInfoBean.getName()))
                        .setText(R.id.tv_direct_indent_pro_sku, getStrings(orderProductInfoBean.getSaleSkuValue()));
                if (orderProductInfoBean.getActivityInfoDetailBean() != null) {
                    String activityTag = getStrings(orderProductInfoBean.getActivityInfoDetailBean().getActivityTag());
                    helper.setGone(R.id.ll_communal_activity_topic_tag, true)
                            .setGone(R.id.tv_communal_activity_tag, !TextUtils.isEmpty(activityTag))
                            .setText(R.id.tv_communal_activity_tag, activityTag)
                            .setGone(R.id.tv_details_gray, orderProductInfoBean.getShowLine() == 1)
                            .setGone(R.id.ll_communal_activity_tag_rule, false);
                    //订单详情暂时不显示活动规则，也不可以跳转专场
//                            .setText(R.id.tv_communal_activity_tag_rule, getStrings(orderProductInfoBean.getActivityInfoDetailBean().getActivityRule()))
//                            .setGone(R.id.tv_communal_activity_tag_next,
//                                    !TextUtils.isEmpty(orderProductInfoBean.getActivityInfoDetailBean().getActivityRule()))
//                            .addOnClickListener(R.id.ll_communal_activity_topic_tag).setTag(R.id.ll_communal_activity_topic_tag, orderProductInfoBean);
                } else {
                    helper.setGone(R.id.ll_communal_activity_topic_tag, false);
                }
                setIndentProStatus(helper, orderProductInfoBean);
                if (orderProductInfoBean.getCombineProductInfoList() != null && orderProductInfoBean.getCombineProductInfoList().size() > 0) {
                    helper.setText(R.id.tv_direct_indent_pro_price, context.getString(R.string.combine_price) + getStringsChNPrice(context, orderProductInfoBean.getPrice()))
                            .setGone(R.id.tv_direct_pro_count, orderProductInfoBean.getStatus() == 0)
                            .setText(R.id.tv_direct_pro_count, "x" + orderProductInfoBean.getCount());
                } else {
                    helper.setText(R.id.tv_direct_indent_pro_price, getStringsChNPrice(context, orderProductInfoBean.getPrice()))
                            .setText(R.id.tv_direct_pro_count, "x" + orderProductInfoBean.getCount());
                }
                if (orderProductInfoBean.getCombineProductInfoList() != null
                        || orderProductInfoBean.getPresentProductInfoList() != null) {
                    helper.setGone(R.id.rel_indent_com_pre_pro, true);
                    preComProInfoBeanList = new ArrayList<>();
                    if (orderProductInfoBean.getPresentProductInfoList() != null && orderProductInfoBean.getPresentProductInfoList().size() > 0) {
                        preComProInfoBeanList.addAll(orderProductInfoBean.getPresentProductInfoList());
                    }
                    if (orderProductInfoBean.getCombineProductInfoList() != null && orderProductInfoBean.getCombineProductInfoList().size() > 0) {
                        for (int i = 0; i < orderProductInfoBean.getCombineProductInfoList().size(); i++) {
                            CartProductInfoBean cartProductInfoBean = orderProductInfoBean.getCombineProductInfoList().get(i);
                            cartProductInfoBean.setItemType(TYPE_1);
                            cartProductInfoBean.setIndentType(INDENT_DETAILS_TYPE);
                            if (orderProductInfoBean.getStatus() == 0) {
                                cartProductInfoBean.setCount(0);
                            }
                            preComProInfoBeanList.add(cartProductInfoBean);
                        }
                    }
                    setComPreData(helper, preComProInfoBeanList, true, false, true);
                } else {
                    helper.setGone(R.id.rel_indent_com_pre_pro, false);
                }
                //显示赠品信息
                PresentProductOrder presentProduct = orderProductInfoBean.getPresentProductOrder();
                helper.setGone(R.id.ll_present, presentProduct != null);
                if (presentProduct != null) {
                    helper.setText(R.id.tv_name, presentProduct.getPresentName())
                            .setText(R.id.tv_count, "x" + presentProduct.getPresentCount());
                    GlideImageLoaderUtil.loadCenterCrop(context, helper.getView(R.id.iv_cover), presentProduct.getPresentPicUrl());
                }
                helper.itemView.setTag(orderProductInfoBean);
                break;
            case INDENT_W_TYPE:
                //            订单填写
                ProductInfoBean productInfoBean = (ProductInfoBean) item;
                GlideImageLoaderUtil.loadCenterCrop(context, (ImageView) helper.getView(R.id.iv_direct_indent_pro), productInfoBean.getPicUrl());
                helper.setText(R.id.tv_direct_indent_pro_name, getStrings(productInfoBean.getName()))
                        .setText(R.id.tv_direct_indent_pro_sku, getStrings(productInfoBean.getSkuName()))
                        .setText(R.id.tv_direct_pro_count, "x" + productInfoBean.getCount())
                        .setGone(R.id.iv_indent_pro_use_cp, productInfoBean.getAllowCoupon() != 0)
                        .setGone(R.id.tv_details_gray, productInfoBean.getShowLine() == 1)
                        .setGone(R.id.iv_indent_product_del, !TextUtils.isEmpty(productInfoBean.getNotBuyAreaInfo()))
                        .addOnClickListener(R.id.iv_indent_product_del).setTag(R.id.iv_indent_product_del, productInfoBean)
                        .setGone(R.id.tv_indent_write_reason, !TextUtils.isEmpty(productInfoBean.getNotBuyAreaInfo()))
                        .setText(R.id.tv_indent_write_reason, getStrings(productInfoBean.getNotBuyAreaInfo()))
                        .setGone(R.id.rel_indent_com_pre_pro, false);
                if (productInfoBean.getActivityInfoBean() != null && productInfoBean.getShowActInfo() == 1) {
                    ActivityInfoBean activityInfoData = productInfoBean.getActivityInfoBean();
                    String activityTag = getStrings(activityInfoData.getActivityTag());
                    helper.setGone(R.id.ll_communal_activity_topic_tag, true)
                            .setGone(R.id.tv_communal_activity_tag, !TextUtils.isEmpty(activityTag))
                            .setText(R.id.tv_communal_activity_tag, activityTag);
                    //订单填写暂时都不允许跳转专场
//                            .addOnClickListener(R.id.ll_communal_activity_tag_rule).setTag(R.id.ll_communal_activity_tag_rule, productInfoBean);
                    helper.setGone(R.id.tv_communal_activity_tag_next, false);
                    //设置规则
                    switch (activityInfoData.getActivityType()) {
                        //显示规则，可进入专场
                        case 0:
                        case 1:
                        case 2:
                        case 4:
                        case 5:
                        case 6:
                        case 8:
                            helper.setText(R.id.tv_communal_activity_tag_rule, getStrings(activityInfoData.getActivityRule()));
                            break;
                        //不显示规则，显示倒计时，可以进入专场
                        case 3:
                            setCountTime(helper, activityInfoData);
                            break;
                        //不显示规则，也不能进入专场
                        case 7:
                            helper.setText(R.id.tv_communal_activity_tag_rule, "");
                            break;
                    }
                } else {
                    helper.setGone(R.id.ll_communal_activity_topic_tag, false);
                }
                String activityPriceDesc = productInfoBean.getActivitypriceDesc();
                String price = getStringsFormat(context, R.string.shop_cart_rmb_price, getStrings(activityPriceDesc), productInfoBean.getPrice());
                if (!TextUtils.isEmpty(productInfoBean.getActivitypriceDesc())) {
                    helper.setText(R.id.tv_direct_indent_pro_price, getSpannableString(price, 0, activityPriceDesc.length(), 0.86f, null))
                            .setTextColor(R.id.tv_direct_indent_pro_price, context.getResources().getColor(R.color.text_normal_red));
                } else {
                    helper.setText(R.id.tv_direct_indent_pro_price, price)
                            .setTextColor(R.id.tv_direct_indent_pro_price, context.getResources().getColor(R.color.text_black_t));
                }

                //赠品
                helper.setGone(R.id.ll_present, productInfoBean.getPresentInfo() != null);
                if (productInfoBean.getPresentInfo() != null) {
                    ProductInfoBean.PresentInfo presentInfo = productInfoBean.getPresentInfo();
                    helper.setText(R.id.tv_name, presentInfo.getName());
                    GlideImageLoaderUtil.loadCenterCrop(context, helper.getView(R.id.iv_cover), presentInfo.getPicUrl());
                }
                helper.itemView.setTag(productInfoBean);
                break;
            case INDENT_GROUP_SHOP:
                GroupShopDetailsBean groupShopDetailsBean = (GroupShopDetailsBean) item;
                GlideImageLoaderUtil.loadCenterCrop(context, helper.getView(R.id.iv_direct_indent_pro),
                        !TextUtils.isEmpty(groupShopDetailsBean.getGpPicUrl()) ? groupShopDetailsBean.getGpPicUrl() : groupShopDetailsBean.getCoverImage());
                helper.setText(R.id.tv_direct_indent_pro_name, getStrings(groupShopDetailsBean.getName()))
                        .setText(R.id.tv_direct_indent_pro_sku, getStrings(groupShopDetailsBean.getProductSkuValue()))
                        .setText(R.id.tv_direct_indent_pro_price, getStringsChNPrice(context, groupShopDetailsBean.getGpPrice()))
                        .setText(R.id.tv_direct_pro_count, "x" + 1);
                helper.itemView.setTag(groupShopDetailsBean);
                break;
            case APPLY_REFUND:
                ProductsBean productsBean = (ProductsBean) item;
                GlideImageLoaderUtil.loadCenterCrop(context, helper.getView(R.id.iv_direct_indent_pro), productsBean.getPicUrl());
                helper.setText(R.id.tv_direct_indent_pro_name, getStrings(productsBean.getProductName()))
                        .setText(R.id.tv_direct_indent_pro_sku, getStrings(productsBean.getProductSkuValue()))
                        .setText(R.id.tv_direct_pro_count, "x" + productsBean.getCount());
                String priceName;
                if (productsBean.getIntegralPrice() > 0) {
                    float moneyPrice = getFloatNumber(productsBean.getPrice());
                    if (moneyPrice > 0) {
                        priceName = String.format(context.getResources().getString(R.string.integral_product_and_price)
                                , productsBean.getIntegralPrice(), getStrings(productsBean.getPrice()));
                    } else {
                        priceName = String.format(context.getResources().getString(R.string.integral_indent_product_price)
                                , productsBean.getIntegralPrice());
                    }
                } else {
                    priceName = getStringsChNPrice(context, productsBean.getPrice());
                }
                helper.setText(R.id.tv_direct_indent_pro_price, priceName);
//                if (productsBean.getCartProductInfoList() != null) {
//                    helper.setGone(R.id.rel_indent_com_pre_pro, true);
//                    preComProInfoBeanList = new ArrayList<>();
//                    if (productsBean.getCartProductInfoList() != null && productsBean.getCartProductInfoList().size() > 0) {
//                        preComProInfoBeanList.addAll(productsBean.getCartProductInfoList());
//                    }
//                    setComPreData(helper, preComProInfoBeanList, false, false, false);
//                } else {
                helper.setGone(R.id.rel_indent_com_pre_pro, false);
//                }
                helper.itemView.setTag(productsBean);
                break;
            case INDENT_INVOICE:
                //            发票订单详情
                orderProductInfoBean = (OrderProductInfoBean) item;
                GlideImageLoaderUtil.loadCenterCrop(context, helper.getView(R.id.iv_direct_indent_pro), orderProductInfoBean.getPicUrl());
                helper.setText(R.id.tv_direct_indent_pro_name, getStrings(orderProductInfoBean.getName()))
                        .setText(R.id.tv_direct_indent_pro_sku, getStrings(orderProductInfoBean.getSaleSkuValue()))
                        .setText(R.id.tv_direct_indent_pro_price, getStringsChNPrice(context, orderProductInfoBean.getPrice()))
                        .setText(R.id.tv_direct_pro_count, "x" + orderProductInfoBean.getCount());
                if (orderProductInfoBean.getActivityInfoDetailBean() != null) {
                    String activityTag = getStrings(orderProductInfoBean.getActivityInfoDetailBean().getActivityTag());
                    helper.setGone(R.id.ll_communal_activity_topic_tag, true)
                            .setGone(R.id.tv_communal_activity_tag, !TextUtils.isEmpty(activityTag))
                            .setText(R.id.tv_communal_activity_tag, activityTag)
                            .setGone(R.id.tv_details_gray, orderProductInfoBean.getShowLine() == 1)
                            .setGone(R.id.ll_communal_activity_tag_rule, true)
                            .setText(R.id.tv_communal_activity_tag_rule, getStrings(orderProductInfoBean.getActivityInfoDetailBean().getActivityRule()))
                            .setGone(R.id.tv_communal_activity_tag_next, false);
                } else {
                    helper.setGone(R.id.ll_communal_activity_topic_tag, false);
                }
                if (orderProductInfoBean.getCombineProductInfoList() != null
                        || orderProductInfoBean.getPresentProductInfoList() != null) {
                    helper.setGone(R.id.rel_indent_com_pre_pro, true);
                    preComProInfoBeanList = new ArrayList<>();
//                    发票只展示组合购商品
                    if (orderProductInfoBean.getPresentProductInfoList() != null
                            && orderProductInfoBean.getPresentProductInfoList().size() > 0) {
                        for (CartProductInfoBean cartProductInfoBean : orderProductInfoBean.getPresentProductInfoList()) {
                            cartProductInfoBean.setItemType(TYPE_0);
                            preComProInfoBeanList.add(cartProductInfoBean);
                        }
                    }

                    if (orderProductInfoBean.getCombineProductInfoList() != null
                            && orderProductInfoBean.getCombineProductInfoList().size() > 0) {
                        for (CartProductInfoBean cartProductInfoBean : orderProductInfoBean.getCombineProductInfoList()) {
                            cartProductInfoBean.setItemType(TYPE_0);
                            preComProInfoBeanList.add(cartProductInfoBean);
                        }
                    }
                    setComPreData(helper, preComProInfoBeanList, false, false, false);
                } else {
                    helper.setGone(R.id.rel_indent_com_pre_pro, false);
                }
                helper.itemView.setTag(orderProductInfoBean);
                break;
        }
    }

    private void setCountTime(final BaseViewHolder helper, ActivityInfoBean activityInfoData) {
        String startTime = activityInfoData.getActivityStartTime();
        String endTime = activityInfoData.getActivityEndTime();
        long currentTime = System.currentTimeMillis();
        try {
            //格式化开始时间
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            Date dateStart = formatter.parse(startTime);
            Date dateEnd = formatter.parse(endTime);
            if (currentTime >= dateStart.getTime() && currentTime < dateEnd.getTime()) {
                CountDownTimer countDownTimer;
                countDownTimer = new CountDownTimer((LifecycleOwner) context, dateEnd.getTime() - currentTime, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        helper.setText(R.id.tv_communal_activity_tag_rule, getCoutDownTime(millisUntilFinished));
                    }

                    @Override
                    public void onFinish() {
                        cancel();
                        helper.setText(R.id.tv_communal_activity_tag_rule, "已结束");
                    }
                };

                countDownTimer.start();
            } else {
                helper.setText(R.id.tv_communal_activity_tag_rule, "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            helper.setText(R.id.tv_communal_activity_tag_rule, "");
        }
    }

    private static String getCoutDownTime(long coutTime) {
        try {
            int day = (int) (coutTime / (1000 * 60 * 60 * 24));
            int hour = (int) ((coutTime % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
            int minute = (int) ((coutTime % (1000 * 60 * 60)) / (1000 * 60));
            int second = (int) ((coutTime % (1000 * 60)) / 1000);
            return "距结束 " + day + "天" + hour + ":" + minute + ":" + second;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * @param helper
     * @param presentProductInfoBeanList 组合赠品集合
     * @param isParentClick              是否可点击父级跳转
     * @param isChildrenClick            是否可点击子控件跳转
     */
    private void setComPreData(BaseViewHolder helper, List<CartProductInfoBean> presentProductInfoBeanList
            , boolean isParentClick, final boolean isSkipIndentDetail, boolean isChildrenClick) {
        RecyclerView communal_recycler_wrap = helper.getView(R.id.communal_recycler_wrap);
        communal_recycler_wrap.setLayoutManager(new LinearLayoutManager(context));
        ShopCarComPreProAdapter shopCarComPreProAdapter = new ShopCarComPreProAdapter(context, presentProductInfoBeanList);
        communal_recycler_wrap.setAdapter(shopCarComPreProAdapter);
        if (isParentClick) {
            shopCarComPreProAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    CartProductInfoBean cartProductInfoBean = (CartProductInfoBean) view.getTag();
                    if (cartProductInfoBean != null) {
                        if (isSkipIndentDetail && !TextUtils.isEmpty(cartProductInfoBean.getOrderNo())) {
                            Intent intent = new Intent(context, DirectExchangeDetailsActivity.class);
                            intent.putExtra("orderNo", cartProductInfoBean.getOrderNo());
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        } else if (!cartProductInfoBean.isPresentProduct()) {
                            Intent intent = new Intent(context, ShopScrollDetailsActivity.class);
                            intent.putExtra("productId", String.valueOf(cartProductInfoBean.getId()));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                    }
                }
            });
        }
        if (isChildrenClick) {
            shopCarComPreProAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    CartProductInfoBean cartProductInfoBean = (CartProductInfoBean) view.getTag();
                    if (cartProductInfoBean != null && !cartProductInfoBean.isPresentProduct()) {
                        setChildClickDispose(cartProductInfoBean);
                    }
                }
            });
        }
    }

    private void setIndentProStatus(BaseViewHolder helper, Object obj) {
        switch (type) {
            case INDENT_TYPE:
                TextView tv_dir_indent_pro_status = helper.getView(R.id.tv_dir_indent_pro_status);
                tv_dir_indent_pro_status.setBackground(null);
                FrameLayout fl_dir_indent_pro_status = helper.getView(R.id.fl_dir_indent_pro_status);
                OrderListBean.GoodsBean goodsBean = (OrderListBean.GoodsBean) obj;
                tv_dir_indent_pro_status.setEnabled(false);
                if (goodsBean.getStatus() == 11 || goodsBean.getStatus() == 13) {
                    helper.setGone(R.id.fl_dir_indent_pro_status, true)
                            .setText(R.id.tv_dir_indent_pro_status, INDENT_PRO_STATUS != null ?
                                    INDENT_PRO_STATUS.get(String.valueOf(goodsBean.getStatus())) : "");
                } else if (goodsBean.getStatus() == -10 ||
                        (goodsBean.getStatus() <= -26 && -40 <= goodsBean.getStatus())
                        || (50 <= goodsBean.getStatus() && goodsBean.getStatus() <= 58)) {
                    fl_dir_indent_pro_status.setVisibility(View.VISIBLE);
                    tv_dir_indent_pro_status.setText(INDENT_PRO_STATUS != null ?
                            INDENT_PRO_STATUS.get(String.valueOf(goodsBean.getStatus())) : "");
                } else {
                    helper.setGone(R.id.tv_dir_indent_pro_status, false);
                }
                break;
//                订单详情
            case INDENT_DETAILS_TYPE:
                /**
                 * 订单详情商品子状态处理
                 * status 10 申请退款
                 *        30-40 申请售后
                 *        -30 -- -40 售后处理 可以点击
                 *        50-58 维修状态
                 * 不可点击，仅做展示 11 13 -10 -26 -50
                 */
                OrderProductInfoBean orderProductInfoBean = (OrderProductInfoBean) obj;
                fl_dir_indent_pro_status = helper.getView(R.id.fl_dir_indent_pro_status);
                tv_dir_indent_pro_status = helper.getView(R.id.tv_dir_indent_pro_status);

                if (orderProductInfoBean.getStatus() == 10) {
                    fl_dir_indent_pro_status.setVisibility(View.VISIBLE);
                    tv_dir_indent_pro_status.setEnabled(true);
                    tv_dir_indent_pro_status.setText("申请退款");
                    helper.addOnClickListener(R.id.fl_dir_indent_pro_status).setTag(R.id.fl_dir_indent_pro_status, orderProductInfoBean);
                } else if (orderProductInfoBean.getStatus() <= 40 && 30 <= orderProductInfoBean.getStatus()) {
                    fl_dir_indent_pro_status.setVisibility(View.VISIBLE);
                    tv_dir_indent_pro_status.setEnabled(true);
                    tv_dir_indent_pro_status.setText("申请售后");
                    helper.addOnClickListener(R.id.fl_dir_indent_pro_status).setTag(R.id.fl_dir_indent_pro_status, orderProductInfoBean);
                } else if (orderProductInfoBean.getStatus() == 11
                        || orderProductInfoBean.getStatus() == 13
                        || orderProductInfoBean.getStatus() == -10
                        || orderProductInfoBean.getStatus() == -26
                        || orderProductInfoBean.getStatus() == -50) {
                    fl_dir_indent_pro_status.setVisibility(View.VISIBLE);
                    tv_dir_indent_pro_status.setEnabled(false);
                    tv_dir_indent_pro_status.setText(INDENT_PRO_STATUS != null ?
                            INDENT_PRO_STATUS.get(String.valueOf(orderProductInfoBean.getStatus())) : "");
                } else if ((orderProductInfoBean.getStatus() <= -30 && -40 <= orderProductInfoBean.getStatus()) ||
                        (50 <= orderProductInfoBean.getStatus() && orderProductInfoBean.getStatus() <= 58)) {
                    fl_dir_indent_pro_status.setVisibility(View.VISIBLE);
                    tv_dir_indent_pro_status.setText(INDENT_PRO_STATUS != null ?
                            INDENT_PRO_STATUS.get(String.valueOf(orderProductInfoBean.getStatus())) : "");
                    tv_dir_indent_pro_status.setEnabled(true);
                    helper.addOnClickListener(R.id.fl_dir_indent_pro_status).setTag(R.id.fl_dir_indent_pro_status, orderProductInfoBean);
                } else {
                    helper.setGone(R.id.fl_dir_indent_pro_status, false);
                }
                break;
        }
    }

    private void setChildClickDispose(CartProductInfoBean cartProductInfoBean) {
        Intent intent = new Intent();
        if (cartProductInfoBean.getStatus() == 10) { //申请退款
            requestRefundData(cartProductInfoBean);
        } else if (cartProductInfoBean.getStatus() == -40 ||
                cartProductInfoBean.getStatus() <= -30 && -32 <= cartProductInfoBean.getStatus()
                || cartProductInfoBean.getStatus() == -35) {
//                                退款处理中 被驳回 退货申请通过
            if (cartProductInfoBean.getOrderRefundProductId() > 0) {
                intent.setClass(context, DoMoRefundDetailActivity.class);
                intent.putExtra("no", cartProductInfoBean.getOrderNo());
                intent.putExtra("orderProductId", String.valueOf(cartProductInfoBean.getOrderProductId()));
                intent.putExtra("orderRefundProductId", String.valueOf(cartProductInfoBean.getOrderRefundProductId()));
                intent.putExtra(REFUND_TYPE, REFUND_TYPE);
                context.startActivity(intent);
            }
        } else if (50 <= cartProductInfoBean.getStatus() && cartProductInfoBean.getStatus() <= 58) {
            if (cartProductInfoBean.getOrderRefundProductId() > 0) {
                intent.setClass(context, DoMoRefundDetailActivity.class);
                intent.putExtra("no", cartProductInfoBean.getOrderNo());
                intent.putExtra("orderProductId", String.valueOf(cartProductInfoBean.getOrderProductId()));
                intent.putExtra("orderRefundProductId", String.valueOf(cartProductInfoBean.getOrderRefundProductId()));
                intent.putExtra(REFUND_TYPE, REFUND_REPAIR);
                context.startActivity(intent);
            }
        } else if (cartProductInfoBean.getStatus() <= 40 && 30 <= cartProductInfoBean.getStatus()) {
//                                申请售后
            DirectApplyRefundBean refundBean = new DirectApplyRefundBean();
            refundBean.setType(2);
            refundBean.setOrderNo(cartProductInfoBean.getOrderNo());
            DirectRefundProBean directRefundProBean = new DirectRefundProBean();
            directRefundProBean.setId(cartProductInfoBean.getId());
            directRefundProBean.setOrderProductId(cartProductInfoBean.getOrderProductId());
            directRefundProBean.setCount(cartProductInfoBean.getCount());
            directRefundProBean.setName(cartProductInfoBean.getName());
            directRefundProBean.setPicUrl(cartProductInfoBean.getPicUrl());
            directRefundProBean.setSaleSkuValue(cartProductInfoBean.getSaleSkuValue());
            directRefundProBean.setPrice(cartProductInfoBean.getPrice());
            List<DirectRefundProBean> directProList = new ArrayList<>();
            directProList.add(directRefundProBean);
            refundBean.setDirectRefundProList(directProList);
            intent.setClass(context, DirectApplyRefundActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("refundPro", refundBean);
            intent.putExtra(REFUND_TYPE, REFUND_TYPE);
            intent.putExtras(bundle);
            context.startActivity(intent);
        }
    }

    private void requestRefundData(final CartProductInfoBean cartProductInfoBean) {
        String url =  Url.Q_INDENT_APPLY_REFUND_CHECK;
        Map<String, Object> params = new HashMap<>();
        params.put("no", cartProductInfoBean.getOrderNo());
        params.put("userId", userId);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", cartProductInfoBean.getId());
            jsonObject.put("orderProductId", cartProductInfoBean.getOrderProductId());
            jsonObject.put("count", cartProductInfoBean.getCount());
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(jsonObject);
            params.put("goods", jsonArray.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(context, url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                ApplyRefundCheckEntity refundCheckEntity = gson.fromJson(result, ApplyRefundCheckEntity.class);
                if (refundCheckEntity != null) {
                    if (refundCheckEntity.getCode().equals(SUCCESS_CODE)) {
                        ApplyRefundCheckBean applyRefundCheckBean = refundCheckEntity.getApplyRefundCheckBean();
                        final DirectApplyRefundBean refundBean = new DirectApplyRefundBean();
                        refundBean.setType(1);
                        refundBean.setOrderNo(cartProductInfoBean.getOrderNo());
                        DirectRefundProBean directRefundProBean = new DirectRefundProBean();
                        directRefundProBean.setId(cartProductInfoBean.getId());
                        directRefundProBean.setOrderProductId(cartProductInfoBean.getOrderProductId());
                        directRefundProBean.setCount(cartProductInfoBean.getCount());
                        directRefundProBean.setName(cartProductInfoBean.getName());
                        directRefundProBean.setPicUrl(cartProductInfoBean.getPicUrl());
                        directRefundProBean.setSaleSkuValue(cartProductInfoBean.getSaleSkuValue());
                        directRefundProBean.setPrice(cartProductInfoBean.getPrice());
                        List<DirectRefundProBean> directProList = new ArrayList<>();
                        directProList.add(directRefundProBean);
                        refundBean.setDirectRefundProList(directProList);
                        refundBean.setRefundPrice(applyRefundCheckBean.getRefundPrice());
                        final Intent intent = new Intent();
                        switch (applyRefundCheckBean.getNoticeFlagType()) {
                            case 0:
                                intent.setClass(context, DirectApplyRefundActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putParcelable("refundPro", refundBean);
                                intent.putExtras(bundle);
                                context.startActivity(intent);
                                break;
                            case 1:
                                showToast(context, refundCheckEntity.getApplyRefundCheckBean() == null
                                        ? refundCheckEntity.getMsg() : refundCheckEntity.getApplyRefundCheckBean().getMsg()
                                );
                                break;
                            case 2:
                                if (refundOrderDialogHelper == null) {
                                    refundOrderDialogHelper = new AlertDialogHelper(context);
                                    refundOrderDialogHelper.setTitleVisibility(View.GONE).setMsgTextGravity(Gravity.CENTER)
                                            .setMsg(getStrings(applyRefundCheckBean.getMsg())).setCancelText("取消")
                                            .setConfirmText("确定")
                                            .setCancelTextColor(context.getResources().getColor(R.color.text_login_gray_s));
                                    refundOrderDialogHelper.setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                                        @Override
                                        public void confirm() {
                                            intent.setClass(context, DirectApplyRefundActivity.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putParcelable("refundPro", refundBean);
                                            intent.putExtra(REFUND_TYPE, REFUND_TYPE);
                                            intent.putExtras(bundle);
                                            context.startActivity(intent);
                                        }

                                        @Override
                                        public void cancel() {
                                        }
                                    });
                                } else {
                                    refundOrderDialogHelper.setMsg(getStrings(applyRefundCheckBean.getMsg()));
                                }
                                refundOrderDialogHelper.show();
                                break;
                        }
                    } else {
                        showToast(context, refundCheckEntity.getApplyRefundCheckBean() == null
                                ? refundCheckEntity.getMsg() : refundCheckEntity.getApplyRefundCheckBean().getMsg()
                        );
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(context, R.string.do_failed);
            }

            @Override
            public void netClose() {
                showToast(context, R.string.unConnectedNetwork);
            }
        });
    }


}
