package com.amkj.dmsh.mine.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.mine.activity.ShopCarActivity;
import com.amkj.dmsh.mine.bean.ActivityInfoBean;
import com.amkj.dmsh.mine.bean.ShopCarEntity.ShopCartBean.CartBean.CartInfoBean;
import com.amkj.dmsh.dao.ShopCarDao;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.shopdetails.bean.SkuSaleBean;
import com.amkj.dmsh.utils.TextWatchListener;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.views.RectAddAndSubShopcarView;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

import me.jessyan.autosize.utils.AutoSizeUtils;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.amkj.dmsh.constant.ConstantMethod.getSpannableString;
import static com.amkj.dmsh.constant.ConstantMethod.getStringChangeIntegers;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsFormat;
import static com.amkj.dmsh.constant.ConstantMethod.isContextExisted;


/**
 * Created by atd48 on 2016/10/22.
 */
public class ShopCarGoodsAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    private final Context context;
    private boolean isEditStatus;

    public ShopCarGoodsAdapter(Context context, List<MultiItemEntity> shopGoodsList) {
        super(shopGoodsList);
        this.context = context;
        addItemType(ConstantVariable.PRODUCT, R.layout.adapter_shop_car_product_item);
        addItemType(ConstantVariable.TITLE, R.layout.layout_communal_act_desc);
    }

    @Override
    protected void convert(BaseViewHolder helper, final MultiItemEntity multiItemEntity) {
        if (multiItemEntity == null) return;
        switch (multiItemEntity.getItemType()) {
            case ConstantVariable.PRODUCT:
                CartInfoBean cartInfoBean = (CartInfoBean) multiItemEntity;
                //        ????????????
                TextView tv_shop_car_product_sku = helper.getView(R.id.tv_shop_car_product_sku);
                //        ????????????
                TextView cb_shop_car_sel = helper.getView(R.id.cb_shop_car_sel);
                //        ????????????
                final RectAddAndSubShopcarView rect_shop_car_item = helper.getView(R.id.communal_rect_add_sub);
                cb_shop_car_sel.setSelected(isEditStatus ? cartInfoBean.isDelete() : cartInfoBean.isSelected());
                //???????????????????????????????????? ????????????????????????(????????????????????????????????????)
                if ((cartInfoBean.isValid() && !cartInfoBean.isForSale()) || isEditStatus) {
                    cb_shop_car_sel.setEnabled(true);
                } else {
                    cb_shop_car_sel.setEnabled(false);
                    cb_shop_car_sel.setSelected(false);
                }

                GlideImageLoaderUtil.loadCenterCrop(context, helper.getView(R.id.img_shop_car_product), cartInfoBean.getPicUrl());
                helper.addOnClickListener(R.id.cb_shop_car_sel).setTag(R.id.cb_shop_car_sel, R.id.shop_car_cb, cartInfoBean)//??????????????????
                        .setText(R.id.tv_shop_car_name, getStrings(cartInfoBean.getName()))
                        .setText(R.id.tv_shop_car_product_sku, getStrings(cartInfoBean.getSaleSkuValue()))
                        .addOnClickListener(R.id.tv_shop_car_product_sku).setTag(R.id.tv_shop_car_product_sku, cartInfoBean)
                        .setGone(R.id.tv_w_buy_tag, cartInfoBean.isForSale())// ????????????
                        .setText(R.id.tv_shop_car_pro_discount, getStrings(cartInfoBean.getPriceTag())).setTag(R.id.communal_rect_add_sub, cartInfoBean)//????????????
                        .addOnClickListener(R.id.img_integration_details_credits_add).setTag(R.id.img_integration_details_credits_add, cartInfoBean)
                        .addOnClickListener(R.id.img_integration_details_credits_minus).setTag(R.id.img_integration_details_credits_minus, cartInfoBean);

                //????????????
                View dividerView = helper.getView(R.id.tv_line_ten);
                ViewGroup.LayoutParams dividerLayoutParams = dividerView.getLayoutParams();
                int parentPosition = getParentPosition(multiItemEntity);
                if (parentPosition != -1) {
                    ActivityInfoBean activityInfoBean = (ActivityInfoBean) getData().get(parentPosition);
                    List<CartInfoBean> subItems = activityInfoBean.getSubItems();
                    if (!TextUtils.isEmpty(activityInfoBean.getActivityCode()) && subItems != null && subItems.size() > 0 && subItems.get(subItems.size() - 1) == cartInfoBean) {
                        dividerLayoutParams.height = AutoSizeUtils.mm2px(context, 20);
                        dividerLayoutParams.width = MATCH_PARENT;
                        dividerView.setBackgroundColor(context.getResources().getColor(R.color.light_gray_f));
                    } else {
                        dividerLayoutParams.height = AutoSizeUtils.mm2px(context, 1);
                        dividerLayoutParams.width = MATCH_PARENT;
                        dividerView.setBackgroundColor(context.getResources().getColor(R.color.text_color_e_s));
                    }
                    dividerView.setLayoutParams(dividerLayoutParams);
                }

                //rubbishCarts????????????(?????????????????????0?????????????????????totalQuantity???????????????0??????????????????????????????????????????)
                if (!cartInfoBean.isValid()) {
                    if (cartInfoBean.getStatus() == 0) {
                        helper.setGone(R.id.tv_buy_sack_tag, true);
                        helper.setText(R.id.tv_buy_sack_tag, "?????????");
                        helper.setGone(R.id.iv_com_pro_tag_out, false);
                    } else if (cartInfoBean.getSaleSku().getQuantity() <= 0) {
                        helper.setGone(R.id.tv_buy_sack_tag, false);
                        helper.setGone(R.id.iv_com_pro_tag_out, true);
                    } else {
                        helper.setGone(R.id.tv_buy_sack_tag, true);
                        helper.setText(R.id.tv_buy_sack_tag, "?????????");
                        helper.setGone(R.id.iv_com_pro_tag_out, false);
                    }
                } else {//carts??????(??????1???????????????,???1??????????????????????????????????????????)
                    if (cartInfoBean.getStatus() == 1) {
                        helper.setGone(R.id.tv_buy_sack_tag, false);
                        helper.setGone(R.id.iv_com_pro_tag_out, cartInfoBean.getSaleSku().getQuantity() <= 0);
                    } else {
                        helper.setGone(R.id.tv_buy_sack_tag, true);
                        helper.setText(R.id.tv_buy_sack_tag, "?????????");
                        helper.setGone(R.id.iv_com_pro_tag_out, false);
                    }

                }

                //????????????&&sku?????????&&??????&&???????????????
                if (isEditStatus && cartInfoBean.getSaleSku() != null && cartInfoBean.getStatus() == 1 && cartInfoBean.isMore()) {
                    tv_shop_car_product_sku.setSelected(true);
                } else {
                    tv_shop_car_product_sku.setSelected(false);
                }
                if (cartInfoBean.getSaleSku() != null && cartInfoBean.getStatus() == 1) {
                    helper.setText(R.id.tv_shop_car_product_sku, cartInfoBean.getSaleSku().getQuantity() > 0
                            ? getStrings(cartInfoBean.getSaleSkuValue()) : (cartInfoBean.isMore() ? "????????????????????????????????????" : getStrings(cartInfoBean.getSaleSkuValue())));
                }
                rect_shop_car_item.setNum(cartInfoBean.getCount());
                if (cartInfoBean.getSaleSku() != null) {
                    SkuSaleBean saleSku = cartInfoBean.getSaleSku();
                    rect_shop_car_item.setMaxNum(saleSku.getQuantity());
                }
                rect_shop_car_item.setAutoChangeNumber(false);
                String activityPriceDesc = cartInfoBean.getActivityPriceDesc();
                String price = getStringsFormat(context, R.string.shop_cart_rmb_price, getStrings(activityPriceDesc), cartInfoBean.getSaleSku() != null ? cartInfoBean.getSaleSku().getPrice() : "--");
                if (!TextUtils.isEmpty(cartInfoBean.getActivityPriceDesc())) {
                    helper.setText(R.id.tv_shop_car_product_price, getSpannableString(price, 0, activityPriceDesc.length(), 0.8f, null))
                            .setTextColor(R.id.tv_shop_car_product_price, context.getResources().getColor(R.color.text_normal_red));
                } else {
                    helper.setText(R.id.tv_shop_car_product_price, price)
                            .setTextColor(R.id.tv_shop_car_product_price, context.getResources().getColor(R.color.text_black_t));
                }

                helper.itemView.setOnClickListener(v -> {
                    if (cartInfoBean.getStatus() == 1 && cartInfoBean.getSaleSku() != null && !isEditStatus) {
                        Intent intent = new Intent(context, ShopScrollDetailsActivity.class);
                        intent.putExtra("productId", String.valueOf(cartInfoBean.getProductId()));
                        context.startActivity(intent);
                    }
                });

                //????????????????????????(?????????????????????????????????????????????)
                rect_shop_car_item.findViewById(R.id.tv_integration_details_credits_count).setEnabled(cartInfoBean.isValid() && !cartInfoBean.isMainProduct() && !cartInfoBean.isCombineProduct());
                ((EditText) rect_shop_car_item.findViewById(R.id.tv_integration_details_credits_count)).addTextChangedListener(new TextWatchListener() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (isContextExisted(context) && context instanceof ShopCarActivity) {
                            ((ShopCarActivity) context).changeSkuNum(helper.getAdapterPosition(), getStringChangeIntegers(s.toString()));
                        }
                    }
                });
                break;
            case ConstantVariable.TITLE:
                //????????????????????????
                ActivityInfoBean activityInfoData = (ActivityInfoBean) multiItemEntity;
                boolean showActivityInfo = !TextUtils.isEmpty(activityInfoData.getActivityCode()) && activityInfoData.getSubItems() != null && activityInfoData.getSubItems().size() > 0;
                helper.setGone(R.id.ll_communal_activity_topic_tag, true);
                ViewGroup.LayoutParams layoutParams = helper.itemView.getLayoutParams();
                if (showActivityInfo) {
                    layoutParams.height = WRAP_CONTENT;
                    layoutParams.width = MATCH_PARENT;
                } else {
                    layoutParams.height = 0;
                    layoutParams.width = 0;
                }
                helper.itemView.setLayoutParams(layoutParams);
                //"activityType": { "0": "??????", "1": "??????", "2": "??????", "3": "?????????", "4": "??????", "5": "?????????", "6": "????????????", "7": "??????", "8": "???????????????", "11": "???????????????" }
                if (showActivityInfo) {
                    //????????????
                    helper.setText(R.id.tv_communal_activity_tag, getStrings(activityInfoData.getActivityTag()))
                            .setGone(R.id.tv_communal_activity_tag, !TextUtils.isEmpty(activityInfoData.getActivityTag()))
                            .setText(R.id.tv_communal_activity_tag_next, activityInfoData.isNeedMore() ? "??????" : "")
                            .addOnClickListener(R.id.ll_communal_activity_tag_rule).setTag(R.id.ll_communal_activity_tag_rule, activityInfoData);
                    //????????????
                    switch (activityInfoData.getActivityType()) {
                        //???????????????????????????????????????????????????
                        case 6:
                            helper.setText(R.id.tv_communal_activity_tag_rule, ShopCarDao.subItemCheceked(activityInfoData) ? getStrings(activityInfoData.getActivityRule()) : getStrings(activityInfoData.getPreActivityRule()))
                                    .setGone(R.id.tv_communal_activity_tag_next, false)
                                    .setEnabled(R.id.ll_communal_activity_tag_rule, false);
                            break;
                        //???????????????????????????????????????????????????
                        case 3:
                            helper.setText(R.id.tv_communal_activity_tag_rule, "")
                                    .setGone(R.id.tv_communal_activity_tag_next, true)
                                    .setEnabled(R.id.ll_communal_activity_tag_rule, true);
                            break;
                        //???????????????????????????????????????????????????
                        case 7:
                            helper.setText(R.id.tv_communal_activity_tag_rule, "")
                                    .setGone(R.id.tv_communal_activity_tag_next, false)
                                    .setEnabled(R.id.ll_communal_activity_tag_rule, false);
                            break;
                        //??????????????????????????????
                        default:
                            helper.setText(R.id.tv_communal_activity_tag_rule, ShopCarDao.subItemCheceked(activityInfoData) ? getStrings(activityInfoData.getActivityRule()) : getStrings(activityInfoData.getPreActivityRule()))
                                    .setGone(R.id.tv_communal_activity_tag_next, true)
                                    .setEnabled(R.id.ll_communal_activity_tag_rule, true);
                            break;
                    }
                }
                break;
        }

        helper.itemView.setTag(multiItemEntity);
    }

    public void setEditStatus(boolean editStatus) {
        isEditStatus = editStatus;
    }
}
