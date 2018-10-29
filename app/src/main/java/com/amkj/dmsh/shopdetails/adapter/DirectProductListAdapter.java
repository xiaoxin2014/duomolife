package com.amkj.dmsh.shopdetails.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.dominant.bean.GroupShopDetailsEntity.GroupShopDetailsBean;
import com.amkj.dmsh.mine.adapter.ShopCarComPreProAdapter;
import com.amkj.dmsh.mine.bean.ShopCarNewInfoEntity.ShopCarNewInfoBean.CartInfoBean.CartProductInfoBean;
import com.amkj.dmsh.release.dialogutils.AlertSettingBean;
import com.amkj.dmsh.release.dialogutils.AlertView;
import com.amkj.dmsh.release.dialogutils.OnAlertItemClickListener;
import com.amkj.dmsh.shopdetails.activity.DirectApplyRefundActivity;
import com.amkj.dmsh.shopdetails.activity.DirectExchangeDetailsActivity;
import com.amkj.dmsh.shopdetails.activity.DoMoRefundDetailActivity;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.shopdetails.bean.ApplyRefundCheckEntity;
import com.amkj.dmsh.shopdetails.bean.ApplyRefundCheckEntity.ApplyRefundCheckBean;
import com.amkj.dmsh.shopdetails.bean.DirectApplyRefundBean;
import com.amkj.dmsh.shopdetails.bean.DirectApplyRefundBean.DirectRefundProBean;
import com.amkj.dmsh.shopdetails.bean.IndentDiscountsEntity.IndentDiscountsBean.ProductInfoBean.ActivityProductInfoBean;
import com.amkj.dmsh.shopdetails.bean.IndentInfoDetailEntity.IndentInfoDetailBean.OrderDetailBean.GoodsDetailBean.OrderProductInfoBean;
import com.amkj.dmsh.shopdetails.bean.InquiryOrderEntry.OrderInquiryDateEntry.OrderListBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getFloatNumber;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsChNPrice;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_PRO_STATUS;
import static com.amkj.dmsh.constant.ConstantVariable.REFUND_REPAIR;
import static com.amkj.dmsh.constant.ConstantVariable.REFUND_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_0;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_1;
import static com.amkj.dmsh.shopdetails.activity.DirectApplyRefundActivity.APPLY_REFUND;
import static com.amkj.dmsh.shopdetails.activity.DirectExchangeDetailsActivity.INDENT_DETAILS_TYPE;
import static com.amkj.dmsh.shopdetails.activity.DirectIndentInvoiceActivity.INDENT_INVOICE;
import static com.amkj.dmsh.shopdetails.activity.DirectIndentWriteActivity.INDENT_GROUP_SHOP;
import static com.amkj.dmsh.shopdetails.activity.DirectIndentWriteActivity.INDENT_W_TYPE;
import static com.amkj.dmsh.shopdetails.activity.DoMoIndentAllActivity.INDENT_TYPE;

;

/**
 * Created by atd48 on 2016/8/17.
 */
public class DirectProductListAdapter extends BaseQuickAdapter<Object, BaseViewHolder> {
    private final String type;
    private final Context context;
    private List<CartProductInfoBean> preComProInfoBeanList = new ArrayList<>();

    public DirectProductListAdapter(Context context, List list, String type) {
        super(R.layout.layout_direct_indent_product_item, list);
        this.context = context;
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, Object item) {
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
                    priceChnText = context.getString(R.string.combine_price) + getStringsChNPrice(context,goodsBean.getPrice());
                } else {
                    priceChnText = getStringsChNPrice(context,goodsBean.getPrice());
                    helper.setText(R.id.tv_direct_pro_count, "x" + goodsBean.getCount());
                }
                tv_direct_indent_product_price.setText(priceChnText);
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
                helper.itemView.setTag(goodsBean);
                break;
            case INDENT_DETAILS_TYPE:
                //            商品订单详情
                OrderProductInfoBean orderProductInfoBean = (OrderProductInfoBean) item;
                GlideImageLoaderUtil.loadCenterCrop(context, (ImageView) helper.getView(R.id.iv_direct_indent_pro), orderProductInfoBean.getPicUrl());
                helper.setText(R.id.tv_direct_indent_pro_name, getStrings(orderProductInfoBean.getName()))
                        .setText(R.id.tv_direct_indent_pro_sku, getStrings(orderProductInfoBean.getSaleSkuValue()));
                if (orderProductInfoBean.getActivityInfoDetailBean() != null) {
                    helper.setGone(R.id.ll_communal_activity_topic_tag, true)
                            .setGone(R.id.tv_communal_activity_tag, true)
                            .setText(R.id.tv_communal_activity_tag, getStrings(orderProductInfoBean.getActivityInfoDetailBean().getActivityTag()))
                            .setGone(R.id.tv_details_gray, orderProductInfoBean.getShowLine() == 1)
                            .setGone(R.id.ll_communal_activity_tag_rule, true)
                            .setText(R.id.tv_communal_activity_tag_rule, getStrings(orderProductInfoBean.getActivityInfoDetailBean().getActivityRule()))
                            .setGone(R.id.tv_communal_activity_tag_next,
                                    !TextUtils.isEmpty(orderProductInfoBean.getActivityInfoDetailBean().getActivityRule()))
                            .addOnClickListener(R.id.ll_communal_activity_topic_tag).setTag(R.id.ll_communal_activity_topic_tag, orderProductInfoBean);
                } else {
                    helper.setGone(R.id.ll_communal_activity_topic_tag, false);
                }
                setIndentProStatus(helper, orderProductInfoBean);
                if (orderProductInfoBean.getCombineProductInfoList() != null && orderProductInfoBean.getCombineProductInfoList().size() > 0) {
                    helper.setText(R.id.tv_direct_indent_pro_price, context.getString(R.string.combine_price) + getStringsChNPrice(context,orderProductInfoBean.getPrice()))
                            .setGone(R.id.tv_direct_pro_count, orderProductInfoBean.getStatus() == 0)
                            .setText(R.id.tv_direct_pro_count, "x" + orderProductInfoBean.getCount());
                } else {
                    helper.setText(R.id.tv_direct_indent_pro_price, getStringsChNPrice(context,orderProductInfoBean.getPrice()))
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
                helper.itemView.setTag(orderProductInfoBean);
                break;
            case INDENT_W_TYPE:
                //            订单填写
                ActivityProductInfoBean productInfoBean = (ActivityProductInfoBean) item;
                GlideImageLoaderUtil.loadCenterCrop(context, (ImageView) helper.getView(R.id.iv_direct_indent_pro), productInfoBean.getPicUrl());
                helper.setText(R.id.tv_direct_indent_pro_name, getStrings(productInfoBean.getName()))
                        .setText(R.id.tv_direct_indent_pro_sku, getStrings(productInfoBean.getSkuName()))
                        .setText(R.id.tv_direct_pro_count, "x" + productInfoBean.getCount())
                        .setGone(R.id.iv_indent_pro_use_cp, productInfoBean.getAllowCoupon() != 0)
                        .setGone(R.id.tv_details_gray, productInfoBean.getShowLine() == 1)
                        .setGone(R.id.iv_indent_product_del, productInfoBean.isShowDel() && !TextUtils.isEmpty(productInfoBean.getNotBuyAreaInfo()))
                        .addOnClickListener(R.id.iv_indent_product_del).setTag(R.id.iv_indent_product_del, productInfoBean)
                        .setGone(R.id.tv_indent_write_reason, !TextUtils.isEmpty(productInfoBean.getNotBuyAreaInfo()))
                        .setText(R.id.tv_indent_write_reason, getStrings(productInfoBean.getNotBuyAreaInfo()));
                if (productInfoBean.getActivityInfoBean() != null && productInfoBean.getShowActInfo() == 1) {
                    helper.setGone(R.id.ll_communal_activity_topic_tag, true)
                            .setGone(R.id.tv_communal_activity_tag, true)
                            .setText(R.id.tv_communal_activity_tag, getStrings(productInfoBean.getActivityInfoBean().getActivityTag()))
                            .setGone(R.id.ll_communal_activity_tag_rule, true)
                            .setText(R.id.tv_communal_activity_tag_rule, getStrings(productInfoBean.getActivityInfoBean().getActivityRule()))
                            .setGone(R.id.tv_communal_activity_tag_next,
                                    !TextUtils.isEmpty(productInfoBean.getActivityInfoBean().getActivityRule()))
                            .addOnClickListener(R.id.ll_communal_activity_topic_tag).setTag(R.id.ll_communal_activity_topic_tag, productInfoBean);
                } else {
                    helper.setGone(R.id.ll_communal_activity_topic_tag, false);
                }

                if (productInfoBean.getCombineProductInfoList() != null
                        && productInfoBean.getCombineProductInfoList().size() > 0) {
                    helper.setText(R.id.tv_direct_indent_pro_price,
                            context.getString(R.string.combine_price) + getStringsChNPrice(context,productInfoBean.getPrice()));
                } else {
                    if (!TextUtils.isEmpty(productInfoBean.getActivityPriceDesc())) {
                        TextView tv_direct_indent_pro_price = helper.getView(R.id.tv_direct_indent_pro_price);
                        tv_direct_indent_pro_price.setSelected(true);
                        Link link = new Link(productInfoBean.getActivityPriceDesc());
                        link.setTextColor(Color.parseColor("#ff5a6b"));
                        link.setTextSize(AutoSizeUtils.mm2px(mAppContext,22));
                        link.setBgColor(Color.parseColor("#ffffff"));
                        link.setUnderlined(false);
                        link.setHighlightAlpha(0f);
                        String priceText = productInfoBean.getActivityPriceDesc()
                                + String.format(context.getResources().getString(R.string.money_price_chn)
                                , productInfoBean.getPrice());
                        CharSequence price = LinkBuilder.from(context, priceText)
                                .addLink(link)
                                .build();
                        tv_direct_indent_pro_price.setText(price);
                    } else if (!TextUtils.isEmpty(productInfoBean.getNewUserTag())) {
                        TextView tv_direct_indent_pro_price = helper.getView(R.id.tv_direct_indent_pro_price);
                        tv_direct_indent_pro_price.setSelected(true);
                        String priceText = productInfoBean.getNewUserTag()
                                + String.format(context.getResources().getString(R.string.money_price_chn)
                                , productInfoBean.getPrice());
                        tv_direct_indent_pro_price.setText(priceText);
                    } else {
                        helper.setText(R.id.tv_direct_indent_pro_price,
                                String.format(context.getResources().getString(R.string.money_price_chn)
                                        , productInfoBean.getPrice()));
                    }
                }
                if (productInfoBean.getCombineProductInfoList() != null
                        || productInfoBean.getPresentProductInfoList() != null) {
                    helper.setGone(R.id.rel_indent_com_pre_pro, true);
                    preComProInfoBeanList = new ArrayList<>();
                    if (productInfoBean.getPresentProductInfoList() != null
                            && productInfoBean.getPresentProductInfoList().size() > 0) {
                        preComProInfoBeanList.addAll(productInfoBean.getPresentProductInfoList());
                    }
                    if (productInfoBean.getCombineProductInfoList() != null && productInfoBean.getCombineProductInfoList().size() > 0) {
                        for (CartProductInfoBean cartProductInfoBean : productInfoBean.getCombineProductInfoList()) {
                            cartProductInfoBean.setCount(0);
                        }
                        preComProInfoBeanList.addAll(productInfoBean.getCombineProductInfoList());
                    }
                    setComPreData(helper, preComProInfoBeanList, false, false, false);
                } else {
                    helper.setGone(R.id.rel_indent_com_pre_pro, false);
                }
                helper.itemView.setTag(productInfoBean);
                break;
            case INDENT_GROUP_SHOP:
                GroupShopDetailsBean groupShopDetailsBean = (GroupShopDetailsBean) item;
                GlideImageLoaderUtil.loadCenterCrop(context, helper.getView(R.id.iv_direct_indent_pro),
                        !TextUtils.isEmpty(groupShopDetailsBean.getGpPicUrl()) ? groupShopDetailsBean.getGpPicUrl() : groupShopDetailsBean.getCoverImage());
                helper.setText(R.id.tv_direct_indent_pro_name, getStrings(groupShopDetailsBean.getName()))
                        .setText(R.id.tv_direct_indent_pro_sku, getStrings(groupShopDetailsBean.getProductSkuValue()))
                        .setText(R.id.tv_direct_indent_pro_price, getStringsChNPrice(context,groupShopDetailsBean.getGpPrice()))
                        .setText(R.id.tv_direct_pro_count, "x" + 1);
                helper.itemView.setTag(groupShopDetailsBean);
                break;
            case APPLY_REFUND:
                DirectRefundProBean applyRefundBean = (DirectRefundProBean) item;
                GlideImageLoaderUtil.loadCenterCrop(context, (ImageView) helper.getView(R.id.iv_direct_indent_pro), applyRefundBean.getPicUrl());
                helper.setText(R.id.tv_direct_indent_pro_name, getStrings(applyRefundBean.getName()))
                        .setText(R.id.tv_direct_indent_pro_sku, getStrings(applyRefundBean.getSaleSkuValue()))
                        .setText(R.id.tv_direct_pro_count, "x" + applyRefundBean.getCount());
                String priceName;
                if(applyRefundBean.getIntegralPrice()>0){
                    float moneyPrice = getFloatNumber(applyRefundBean.getPrice());
                    if(moneyPrice>0){
                        priceName = String.format(context.getResources().getString(R.string.integral_product_and_price)
                                ,applyRefundBean.getIntegralPrice(),getStrings(applyRefundBean.getPrice()));
                    }else{
                        priceName = String.format(context.getResources().getString(R.string.integral_indent_product_price)
                                ,applyRefundBean.getIntegralPrice());
                    }
                }else{
                    priceName = getStringsChNPrice(context,applyRefundBean.getPrice());
                }
                helper.setText(R.id.tv_direct_indent_pro_price,priceName);
                if (applyRefundBean.getCartProductInfoList() != null) {
                    helper.setGone(R.id.rel_indent_com_pre_pro, true);
                    preComProInfoBeanList = new ArrayList<>();
                    if (applyRefundBean.getCartProductInfoList() != null && applyRefundBean.getCartProductInfoList().size() > 0) {
                        preComProInfoBeanList.addAll(applyRefundBean.getCartProductInfoList());
                    }
                    setComPreData(helper, preComProInfoBeanList, false, false, false);
                } else {
                    helper.setGone(R.id.rel_indent_com_pre_pro, false);
                }
                helper.itemView.setTag(applyRefundBean);
                break;
            case INDENT_INVOICE:
                //            发票订单详情
                orderProductInfoBean = (OrderProductInfoBean) item;
                GlideImageLoaderUtil.loadCenterCrop(context, (ImageView) helper.getView(R.id.iv_direct_indent_pro), orderProductInfoBean.getPicUrl());
                helper.setText(R.id.tv_direct_indent_pro_name, getStrings(orderProductInfoBean.getName()))
                        .setText(R.id.tv_direct_indent_pro_sku, getStrings(orderProductInfoBean.getSaleSkuValue()))
                        .setText(R.id.tv_direct_indent_pro_price, getStringsChNPrice(context,orderProductInfoBean.getPrice()))
                        .setText(R.id.tv_direct_pro_count, "x" + orderProductInfoBean.getCount());
                if (orderProductInfoBean.getActivityInfoDetailBean() != null) {
                    helper.setGone(R.id.ll_communal_activity_topic_tag, true)
                            .setGone(R.id.tv_communal_activity_tag, true)
                            .setText(R.id.tv_communal_activity_tag, getStrings(orderProductInfoBean.getActivityInfoDetailBean().getActivityTag()))
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
                        for (CartProductInfoBean cartProductInfoBean:orderProductInfoBean.getPresentProductInfoList()) {
                            cartProductInfoBean.setItemType(TYPE_0);
                            preComProInfoBeanList.add(cartProductInfoBean);
                        }
                    }

                    if (orderProductInfoBean.getCombineProductInfoList() != null
                            && orderProductInfoBean.getCombineProductInfoList().size() > 0) {
                        for (CartProductInfoBean cartProductInfoBean:orderProductInfoBean.getCombineProductInfoList()) {
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
            case INDENT_DETAILS_TYPE:
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
                } else if (orderProductInfoBean.getStatus() == 11 || orderProductInfoBean.getStatus() == 13 || orderProductInfoBean.getStatus() == -10) {
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
        }else if(50 <= cartProductInfoBean.getStatus() && cartProductInfoBean.getStatus() <= 58){
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
        String url = Url.BASE_URL + Url.Q_INDENT_APPLY_REFUND_CHECK;
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
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                ApplyRefundCheckEntity refundCheckEntity = gson.fromJson(result, ApplyRefundCheckEntity.class);
                if (refundCheckEntity != null) {
                    if (refundCheckEntity.getCode().equals("01")) {
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
                                AlertSettingBean alertSettingBean = new AlertSettingBean();
                                AlertSettingBean.AlertData alertData = new AlertSettingBean.AlertData();
                                alertData.setCancelStr("取消");
                                alertData.setDetermineStr("确定");
                                alertData.setFirstDet(true);
                                alertData.setTitle(getStrings(applyRefundCheckBean.getMsg()));
                                alertSettingBean.setStyle(AlertView.Style.Alert);
                                alertSettingBean.setAlertData(alertData);
                                AlertView refundDialog = new AlertView(alertSettingBean, context, new OnAlertItemClickListener() {
                                    @Override
                                    public void onAlertItemClick(Object o, int position) {
                                        if (position != AlertView.CANCELPOSITION) {
                                            intent.setClass(context, DirectApplyRefundActivity.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putParcelable("refundPro", refundBean);
                                            intent.putExtra(REFUND_TYPE, REFUND_TYPE);
                                            intent.putExtras(bundle);
                                            context.startActivity(intent);
                                        }
                                    }
                                });
                                refundDialog.setCancelable(true);
                                refundDialog.show();
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
            public void onError(Throwable ex, boolean isOnCallback) {
                showToast(context, R.string.unConnectedNetwork);
                super.onError(ex, isOnCallback);
            }
        });
    }
}
