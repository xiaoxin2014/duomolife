package com.amkj.dmsh.mine.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.mine.bean.ActivityInfoBean;
import com.amkj.dmsh.mine.bean.ShopCarEntity.ShopCartBean.CartBean.CartInfoBean;
import com.amkj.dmsh.mine.biz.ShopCarDao;
import com.amkj.dmsh.shopdetails.bean.SkuSaleBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.views.RectAddAndSubViewCommunal;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

import me.jessyan.autosize.utils.AutoSizeUtils;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.amkj.dmsh.constant.ConstantMethod.getSpannableString;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsFormat;


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
                CheckBox cb_shop_car_sel = helper.getView(R.id.cb_shop_car_sel);
                //        数量增减
                final RectAddAndSubViewCommunal rect_shop_car_item = helper.getView(R.id.communal_rect_add_sub);
                helper.setChecked(R.id.cb_shop_car_sel, isEditStatus ? cartInfoBean.isDelete() : cartInfoBean.isSelected());
                //商品有效或者在编辑状态时(编辑状态搭配商品单独选中)
                if (cartInfoBean.isValid() || isEditStatus) {
                    cb_shop_car_sel.setEnabled(true);
                } else {
                    cb_shop_car_sel.setEnabled(false);
                    cb_shop_car_sel.setChecked(false);
                }

                GlideImageLoaderUtil.loadCenterCrop(context, helper.getView(R.id.img_shop_car_product), cartInfoBean.getPicUrl());
                //        "activityType": { "0": "满减", "1": "折扣", "2": "立减", "3": "限时购", "4": "满赠", "5": "首单赠", "6": "组合商品", "7": "赠品", "8": "第二件半价" }
                helper.addOnClickListener(R.id.cb_shop_car_sel).setTag(R.id.cb_shop_car_sel, R.id.shop_car_cb, cartInfoBean)//是否选中商品
                        .setText(R.id.tv_shop_car_name, getStrings(cartInfoBean.getName()))
                        .setText(R.id.tv_shop_car_product_sku, getStrings(cartInfoBean.getSaleSkuValue()))
                        .addOnClickListener(R.id.tv_shop_car_product_sku).setTag(R.id.tv_shop_car_product_sku, cartInfoBean)
                        .setGone(R.id.tv_w_buy_tag, cartInfoBean.isForSale())// 待售状态
                        .setText(R.id.tv_shop_car_pro_discount, getStrings(cartInfoBean.getPriceTag())).setTag(R.id.communal_rect_add_sub, cartInfoBean)//降价优惠
                        .addOnClickListener(R.id.img_integration_details_credits_add).setTag(R.id.img_integration_details_credits_add, cartInfoBean)
                        .addOnClickListener(R.id.img_integration_details_credits_minus).setTag(R.id.img_integration_details_credits_minus, cartInfoBean);

                //设置间距
                TextView dividerView = helper.getView(R.id.tv_line_ten);
                ViewGroup.LayoutParams dividerLayoutParams = dividerView.getLayoutParams();
                int parentPosition = getParentPosition(multiItemEntity);
                if (parentPosition != -1) {
                    ActivityInfoBean activityInfoBean = (ActivityInfoBean) getData().get(parentPosition);
                    List<CartInfoBean> subItems = activityInfoBean.getSubItems();
                    if (!TextUtils.isEmpty(activityInfoBean.getActivityCode()) && subItems != null && subItems.size() > 0 && subItems.get(subItems.size() - 1) == cartInfoBean) {
                        dividerLayoutParams.height = AutoSizeUtils.dp2px(context, 10);
                        dividerLayoutParams.width = MATCH_PARENT;
                    } else {
                        dividerLayoutParams.height = AutoSizeUtils.dp2px(context, 0.5f);
                        dividerLayoutParams.width = MATCH_PARENT;
                    }
                    dividerView.setLayoutParams(dividerLayoutParams);
                }

                //商品状态
                if (cartInfoBean.getStatus() == 1 && cartInfoBean.getSaleSku() != null) {
                    if (cartInfoBean.getSaleSku().getQuantity() > 0) {
                        helper.setGone(R.id.tv_buy_sack_tag, false);
                    } else {
                        helper.setGone(R.id.tv_buy_sack_tag, true)
                                .setText(R.id.tv_buy_sack_tag, "已抢光");
                    }
                } else {
                    helper.setGone(R.id.tv_buy_sack_tag, true)
                            .setText(R.id.tv_buy_sack_tag, "已下架");
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
                break;
            case ConstantVariable.TITLE:
                helper.setGone(R.id.ll_communal_activity_topic_tag, true)
                        .setGone(R.id.blank, true);

                ActivityInfoBean activityInfoData = (ActivityInfoBean) multiItemEntity;
                ViewGroup.LayoutParams layoutParams = helper.itemView.getLayoutParams();
                if (TextUtils.isEmpty(activityInfoData.getActivityCode()) || activityInfoData.getSubItems().size() == 0) {
                    layoutParams.height = 0;
                    layoutParams.width = 0;
                } else {
                    layoutParams.height = WRAP_CONTENT;
                    layoutParams.width = MATCH_PARENT;
                }
                helper.itemView.setLayoutParams(layoutParams);
                if (!TextUtils.isEmpty(activityInfoData.getActivityCode()) && activityInfoData.getSubItems() != null && activityInfoData.getSubItems().size() > 0) {
                    //设置标签
                    helper.setGone(R.id.tv_communal_activity_tag, !TextUtils.isEmpty(activityInfoData.getActivityTag()))
                            .setText(R.id.tv_communal_activity_tag_next, "凑单")
                            .setText(R.id.tv_communal_activity_tag, getStrings(activityInfoData.getActivityTag()));

                    //设置规则
                    switch (activityInfoData.getActivityType()) {
                        case 0:
                        case 1:
                        case 4:
                        case 8:
                            helper.setGone(R.id.ll_communal_activity_tag_rule, true)
                                    .setText(R.id.tv_communal_activity_tag_rule, ShopCarDao.subItemCheceked(activityInfoData) ? getStrings(activityInfoData.getActivityRule()) : getStrings(activityInfoData.getPreActivityRule()))
                                    .addOnClickListener(R.id.tv_communal_activity_tag_next).setTag(R.id.tv_communal_activity_tag_next, activityInfoData)
                                    .addOnClickListener(R.id.tv_communal_activity_tag_rule).setTag(R.id.tv_communal_activity_tag_rule, activityInfoData);
                            break;
                        case 2:
                        case 3:
                        case 5:
                        case 6:
                        case 7:
                            helper.setGone(R.id.ll_communal_activity_tag_rule, false);
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
