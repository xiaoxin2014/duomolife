package com.amkj.dmsh.mine.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.mine.bean.ShopCarNewInfoEntity.ShopCarNewInfoBean.CartInfoBean;
import com.amkj.dmsh.mine.bean.ShopCarNewInfoEntity.ShopCarNewInfoBean.CartInfoBean.CartProductInfoBean;
import com.amkj.dmsh.mine.bean.ShopCarNewInfoEntity.ShopCarNewInfoBean.CartInfoBean.SaleSkuBean;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.views.RectAddAndSubViewCommunal;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;

import java.util.ArrayList;
import java.util.List;

import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;


/**
 * Created by atd48 on 2016/10/22.
 */
public class ShopCarGoodsAdapter extends BaseQuickAdapter<CartInfoBean, ShopCarGoodsAdapter.ShopCarViewHolder> {
    private final Context context;
    private List<CartProductInfoBean> presentProductInfoBeanList = new ArrayList<>();

    public ShopCarGoodsAdapter(Context context, List<CartInfoBean> shopGoodsList) {
        super(R.layout.adapter_shop_car_product_item, shopGoodsList);
        this.context = context;
    }

    @Override
    protected void convert(ShopCarViewHolder helper, final CartInfoBean cartInfoBean) {
//        获取控件
//        产品属性
        TextView tv_shop_car_product_sku = helper.getView(R.id.tv_shop_car_product_sku);
//        选择按钮
        CheckBox cb_shop_car_sel = helper.getView(R.id.cb_shop_car_sel);
//        数量增减
        final RectAddAndSubViewCommunal rect_shop_car_item = helper.getView(R.id.communal_rect_add_sub);
        helper.setChecked(R.id.cb_shop_car_sel, cartInfoBean.isEditing() ? cartInfoBean.isDelete() : cartInfoBean.isSelected());
        //1.待售并且不在编辑状态   2.失效并且不在编辑状态   3.没有库存并且不在编辑状态时   （无法选中进行购买）
        if (cartInfoBean.isForSale() && !cartInfoBean.isEditing() || !cartInfoBean.isEditing() && cartInfoBean.getStatus() != 1 ||
                !cartInfoBean.isEditing() && cartInfoBean.getStatus() == 1 && cartInfoBean.getSaleSku() != null && cartInfoBean.getSaleSku().getQuantity() < 1) {
            cb_shop_car_sel.setEnabled(false);
            cb_shop_car_sel.setChecked(false);
        } else {
            cb_shop_car_sel.setEnabled(true);
        }

//        产品图片
        GlideImageLoaderUtil.loadCenterCrop(context, (ImageView) helper.getView(R.id.img_shop_car_product), cartInfoBean.getPicUrl());
        if (cartInfoBean.getActivityInfoData() != null && cartInfoBean.getActivityInfoData().getShowActInfo() == 1) {
            helper.setGone(R.id.ll_communal_activity_topic_tag, true)
                    .setGone(R.id.tv_communal_activity_tag, true)
                    .setText(R.id.tv_communal_activity_tag_next, "凑单")
                    .setText(R.id.tv_communal_activity_tag, getStrings(cartInfoBean.getActivityInfoData().getActivityTag()));
            switch (cartInfoBean.getActivityInfoData().getActivityType()) {
                case 0:
                case 1:
                    helper.setGone(R.id.ll_communal_activity_tag_rule, true)
                            .setText(R.id.tv_communal_activity_tag_rule, getStrings(cartInfoBean.getActivityInfoData().getActivityRule()))
                            .addOnClickListener(R.id.tv_communal_activity_tag_next).setTag(R.id.tv_communal_activity_tag_next, cartInfoBean)
                            .addOnClickListener(R.id.tv_communal_activity_tag_rule).setTag(R.id.tv_communal_activity_tag_rule, cartInfoBean);
                    break;
                case 2:
                case 3:
                case 4:
                case 5:
                    helper.setGone(R.id.ll_communal_activity_tag_rule, false);
                    break;
            }
        } else {
            helper.setGone(R.id.ll_communal_activity_topic_tag, false);
        }
//        是否选中商品
        helper.setTag(R.id.cb_shop_car_sel, helper.getAdapterPosition())
                .setTag(R.id.cb_shop_car_sel, R.id.shop_car_cb, cartInfoBean)
                .addOnClickListener(R.id.cb_shop_car_sel)
                .setText(R.id.tv_shop_car_name, getStrings(cartInfoBean.getName()))
                .setText(R.id.tv_shop_car_product_sku, getStrings(cartInfoBean.getSaleSkuValue()))
                .setGone(R.id.tv_w_buy_tag, cartInfoBean.isForSale())// 待售状态
//                降价优惠
                .setText(R.id.tv_shop_car_pro_discount, getStrings(cartInfoBean.getPriceTag()))
                .setTag(R.id.tv_shop_car_product_sku, cartInfoBean)
                .setTag(R.id.communal_rect_add_sub, cartInfoBean)
//                展示间距？
                .setGone(R.id.tv_line_ten, cartInfoBean.getShowLine() == 1)
                .setTag(R.id.communal_rect_add_sub, R.id.shop_car_position, helper.getAdapterPosition())
                .addOnClickListener(R.id.img_integration_details_credits_add)
                .setTag(R.id.img_integration_details_credits_add, cartInfoBean)
                .addOnClickListener(R.id.img_integration_details_credits_minus)
                .setTag(R.id.img_integration_details_credits_minus, cartInfoBean);
//        商品状态
//        tv_w_buy_tag 待售状态
        if (cartInfoBean.getStatus() == 1 && cartInfoBean.getSaleSku() != null) {
            if (cartInfoBean.getSaleSku().getQuantity() > 0) {
                helper.setGone(R.id.tv_buy_sack_tag, false);
            } else {
                helper.setGone(R.id.tv_buy_sack_tag, true)
                        .setText(R.id.tv_buy_sack_tag, "已抢光");
            }
        } else {
            helper.setGone(R.id.tv_buy_sack_tag, true)
                    .setText(R.id.tv_buy_sack_tag, "已失效");
        }
//        是否编辑状态
        if (!cartInfoBean.isEditing() && cartInfoBean.getSaleSku() == null ||
                cartInfoBean.isEditing() && cartInfoBean.getSaleSku() != null &&
                        cartInfoBean.getStatus() == 1 && cartInfoBean.isMore()) {
            tv_shop_car_product_sku.setSelected(true);
            helper.addOnClickListener(R.id.tv_shop_car_product_sku);
        } else {
            tv_shop_car_product_sku.setSelected(false);
        }
        if (cartInfoBean.getSaleSku() != null && cartInfoBean.getStatus() == 1) {
            helper.setText(R.id.tv_shop_car_product_sku, cartInfoBean.getSaleSku().getQuantity() > 0
                    ? getStrings(cartInfoBean.getSaleSkuValue()) : (cartInfoBean.isMore() ? "对不起，该产品已经卖光了" : getStrings(cartInfoBean.getSaleSkuValue())));
        }
        rect_shop_car_item.setNum(cartInfoBean.getCount());
        if (cartInfoBean.getSaleSku() != null) {
            SaleSkuBean saleSku = cartInfoBean.getSaleSku();
            rect_shop_car_item.setMaxNum(saleSku.getQuantity());
        }
        rect_shop_car_item.setAutoChangeNumber(false);
        /**
         * 价格显示
         */
        if (cartInfoBean.getCombineProductInfoList() != null && cartInfoBean.getCombineProductInfoList().size() > 0) {
            helper.setText(R.id.tv_shop_car_product_price, context.getString(R.string.combine_price) +
                    "￥" + (cartInfoBean.getSaleSku() != null
                    ? cartInfoBean.getSaleSku().getPrice() : cartInfoBean.getPrice()));
        } else {
            if (!TextUtils.isEmpty(cartInfoBean.getActivityPriceDesc())) {
                setReallyPrice((TextView) helper.getView(R.id.tv_shop_car_product_price), cartInfoBean);
            } else {
                helper.setText(R.id.tv_shop_car_product_price, "￥" + (cartInfoBean.getSaleSku() != null
                        ? cartInfoBean.getSaleSku().getPrice() : cartInfoBean.getPrice()));
            }
        }
        if (cartInfoBean.getPresentProductInfoList() != null
                || cartInfoBean.getCombineProductInfoList() != null) {
            presentProductInfoBeanList.clear();
            helper.communal_recycler_wrap.setLayoutManager(new LinearLayoutManager(context));
            if (cartInfoBean.getPresentProductInfoList() != null && cartInfoBean.getPresentProductInfoList().size() > 0) {
                presentProductInfoBeanList.addAll(cartInfoBean.getPresentProductInfoList());
            }
            if (cartInfoBean.getCombineProductInfoList() != null && cartInfoBean.getCombineProductInfoList().size() > 0) {
                for (CartProductInfoBean cartProductInfoBean : cartInfoBean.getCombineProductInfoList()) {
                    cartProductInfoBean.setCount(0);
                }
                presentProductInfoBeanList.addAll(cartInfoBean.getCombineProductInfoList());
            }
            if (presentProductInfoBeanList.size() > 0) {
                helper.communal_recycler_wrap.setVisibility(View.VISIBLE);
                ShopCarComPreProAdapter shopCarComPreProAdapter = new ShopCarComPreProAdapter(context, presentProductInfoBeanList);
                helper.communal_recycler_wrap.setAdapter(shopCarComPreProAdapter);
                shopCarComPreProAdapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        CartProductInfoBean cartProductInfoBean = (CartProductInfoBean) view.getTag();
                        if (cartProductInfoBean != null && !cartProductInfoBean.isPresentProduct()) {
                            Intent intent = new Intent(context, ShopScrollDetailsActivity.class);
                            intent.putExtra("productId", String.valueOf(cartProductInfoBean.getProductId()));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                    }
                });
            } else {
                helper.communal_recycler_wrap.setVisibility(View.GONE);
            }
        } else {
            helper.communal_recycler_wrap.setVisibility(View.GONE);
        }
        helper.itemView.setTag(cartInfoBean);
    }

    private void setReallyPrice(TextView tv_shop_car_product_price, CartInfoBean cartInfoBean) {
        tv_shop_car_product_price.setSelected(true);
        Link link = new Link(cartInfoBean.getActivityPriceDesc());
        link.setTextColor(Color.parseColor("#ff5a6b"));
        link.setTextSize(AutoSizeUtils.mm2px(mAppContext, 22));
        link.setBgColor(Color.parseColor("#ffffff"));
        link.setUnderlined(false);
        link.setHighlightAlpha(0f);
        String priceText = cartInfoBean.getActivityPriceDesc()
                + String.format(context.getResources().getString(R.string.money_price_chn)
                , (cartInfoBean.getSaleSku() != null
                        ? cartInfoBean.getSaleSku().getPrice() : cartInfoBean.getPrice()));
        CharSequence price = LinkBuilder.from(context, priceText)
                .addLink(link)
                .build();
        tv_shop_car_product_price.setText(price);
    }

    public class ShopCarViewHolder extends BaseViewHolder {
        private RecyclerView communal_recycler_wrap;

        public ShopCarViewHolder(View view) {
            super(view);
            communal_recycler_wrap = view.findViewById(R.id.communal_recycler_wrap);
        }
    }
}
