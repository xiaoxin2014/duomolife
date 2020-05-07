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
import com.amkj.dmsh.mine.biz.ShopCarDao;
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
                //        产品属性
                TextView tv_shop_car_product_sku = helper.getView(R.id.tv_shop_car_product_sku);
                //        选择按钮
                TextView cb_shop_car_sel = helper.getView(R.id.cb_shop_car_sel);
                //        数量增减
                final RectAddAndSubShopcarView rect_shop_car_item = helper.getView(R.id.communal_rect_add_sub);
                cb_shop_car_sel.setSelected(isEditStatus ? cartInfoBean.isDelete() : cartInfoBean.isSelected());
                //商品有效并且不在待售状态 或者在编辑状态时(编辑状态搭配商品单独选中)
                if ((cartInfoBean.isValid() && !cartInfoBean.isForSale()) || isEditStatus) {
                    cb_shop_car_sel.setEnabled(true);
                } else {
                    cb_shop_car_sel.setEnabled(false);
                    cb_shop_car_sel.setSelected(false);
                }

                GlideImageLoaderUtil.loadCenterCrop(context, helper.getView(R.id.img_shop_car_product), cartInfoBean.getPicUrl());
                //"activityType": { "0": "满减", "1": "折扣", "2": "立减", "3": "限时购", "4": "满赠", "5": "首单赠", "6": "组合商品", "7": "赠品", "8": "第二件半价" }
                helper.addOnClickListener(R.id.cb_shop_car_sel).setTag(R.id.cb_shop_car_sel, R.id.shop_car_cb, cartInfoBean)//是否选中商品
                        .setText(R.id.tv_shop_car_name, getStrings(cartInfoBean.getName()))
                        .setText(R.id.tv_shop_car_product_sku, getStrings(cartInfoBean.getSaleSkuValue()))
                        .addOnClickListener(R.id.tv_shop_car_product_sku).setTag(R.id.tv_shop_car_product_sku, cartInfoBean)
                        .setGone(R.id.tv_w_buy_tag, cartInfoBean.isForSale())// 待售状态
                        .setText(R.id.tv_shop_car_pro_discount, getStrings(cartInfoBean.getPriceTag())).setTag(R.id.communal_rect_add_sub, cartInfoBean)//降价优惠
                        .addOnClickListener(R.id.img_integration_details_credits_add).setTag(R.id.img_integration_details_credits_add, cartInfoBean)
                        .addOnClickListener(R.id.img_integration_details_credits_minus).setTag(R.id.img_integration_details_credits_minus, cartInfoBean);

                //设置间距
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

                //rubbishCarts商品状态(如果商品状态为0，显示已下架，totalQuantity即总库存为0显示已抢光，其他状态显示失效)
                if (!cartInfoBean.isValid()) {
                    if (cartInfoBean.getStatus() == 0) {
                        helper.setGone(R.id.tv_buy_sack_tag, true);
                        helper.setText(R.id.tv_buy_sack_tag, "已下架");
                        helper.setGone(R.id.iv_com_pro_tag_out, false);
                    } else if (cartInfoBean.getSaleSku().getQuantity() <= 0) {
                        helper.setGone(R.id.tv_buy_sack_tag, false);
                        helper.setGone(R.id.iv_com_pro_tag_out, true);
                    } else {
                        helper.setGone(R.id.tv_buy_sack_tag, true);
                        helper.setText(R.id.tv_buy_sack_tag, "已失效");
                        helper.setGone(R.id.iv_com_pro_tag_out, false);
                    }
                } else {//carts状态(不为1显示已失效,为1时判断库存，无库存显示已抢光)
                    if (cartInfoBean.getStatus() == 1) {
                        helper.setGone(R.id.tv_buy_sack_tag, false);
                        helper.setGone(R.id.iv_com_pro_tag_out, cartInfoBean.getSaleSku().getQuantity() <= 0);
                    } else {
                        helper.setGone(R.id.tv_buy_sack_tag, true);
                        helper.setText(R.id.tv_buy_sack_tag, "已失效");
                        helper.setGone(R.id.iv_com_pro_tag_out, false);
                    }

                }

                //编辑状态&&sku不为空&&有效&&有更多属性
                if (isEditStatus && cartInfoBean.getSaleSku() != null && cartInfoBean.getStatus() == 1 && cartInfoBean.isMore()) {
                    tv_shop_car_product_sku.setSelected(true);
                } else {
                    tv_shop_car_product_sku.setSelected(false);
                }
                if (cartInfoBean.getSaleSku() != null && cartInfoBean.getStatus() == 1) {
                    helper.setText(R.id.tv_shop_car_product_sku, cartInfoBean.getSaleSku().getQuantity() > 0
                            ? getStrings(cartInfoBean.getSaleSkuValue()) : (cartInfoBean.isMore() ? "对不起，该产品已经卖光了" : getStrings(cartInfoBean.getSaleSkuValue())));
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

                //监听手动修改数量(有效并且非组合商品才能修改数量)
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
                //是否显示活动信息
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

                if (showActivityInfo) {
                    //设置标签
                    helper.setText(R.id.tv_communal_activity_tag, getStrings(activityInfoData.getActivityTag()))
                            .setGone(R.id.tv_communal_activity_tag, !TextUtils.isEmpty(activityInfoData.getActivityTag()))
                            .setText(R.id.tv_communal_activity_tag_next, activityInfoData.isNeedMore() ? "凑单" : "")
                            .addOnClickListener(R.id.ll_communal_activity_tag_rule).setTag(R.id.ll_communal_activity_tag_rule, activityInfoData);
                    //设置规则
                    switch (activityInfoData.getActivityType()) {
                        //显示规则，可进入专场
                        case 0:
                        case 1:
                        case 2:
                        case 4:
                        case 5:
                        case 8:
                            helper.setText(R.id.tv_communal_activity_tag_rule, ShopCarDao.subItemCheceked(activityInfoData) ? getStrings(activityInfoData.getActivityRule()) : getStrings(activityInfoData.getPreActivityRule()))
                                    .setGone(R.id.tv_communal_activity_tag_next, true)
                                    .setEnabled(R.id.ll_communal_activity_tag_rule, true);
                            break;
                        //显示规则，不能进入专场
                        case 6:
                            helper.setText(R.id.tv_communal_activity_tag_rule, ShopCarDao.subItemCheceked(activityInfoData) ? getStrings(activityInfoData.getActivityRule()) : getStrings(activityInfoData.getPreActivityRule()))
                                    .setGone(R.id.tv_communal_activity_tag_next, false)
                                    .setEnabled(R.id.ll_communal_activity_tag_rule, false);
                            break;
                        //不显示规则，可以进入专场
                        case 3:
                            helper.setText(R.id.tv_communal_activity_tag_rule, "")
                                    .setGone(R.id.tv_communal_activity_tag_next, true)
                                    .setEnabled(R.id.ll_communal_activity_tag_rule, true);
                            break;
                        //不显示规则，也不能进入专场
                        case 7:
                            helper.setText(R.id.tv_communal_activity_tag_rule, "")
                                    .setGone(R.id.tv_communal_activity_tag_next, false)
                                    .setEnabled(R.id.ll_communal_activity_tag_rule, false);
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
