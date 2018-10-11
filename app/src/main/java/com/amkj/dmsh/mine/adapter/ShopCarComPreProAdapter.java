package com.amkj.dmsh.mine.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.mine.bean.ShopCarNewInfoEntity.ShopCarNewInfoBean.CartInfoBean.CartProductInfoBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_PRO_STATUS;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_0;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_1;
import static com.amkj.dmsh.shopdetails.activity.DirectExchangeDetailsActivity.INDENT_DETAILS_TYPE;
import static com.amkj.dmsh.shopdetails.activity.DoMoIndentAllActivity.INDENT_TYPE;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/12/13
 * version 1.0
 * class description:购物车组合赠品适配器
 */

public class ShopCarComPreProAdapter extends BaseMultiItemQuickAdapter<CartProductInfoBean, BaseViewHolder> {
    private final Context context;

    public ShopCarComPreProAdapter(Context context, List<CartProductInfoBean> cartProductInfoBeanList) {
        super(cartProductInfoBeanList);
        addItemType(TYPE_1, R.layout.adapter_indent_pro_com_pre);
        addItemType(TYPE_0, R.layout.adapter_shop_car_com_pre);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, CartProductInfoBean cartProductInfoBean) {
        switch (helper.getItemViewType()) {
            case TYPE_1:
                GlideImageLoaderUtil.loadFitCenter(context, (ImageView) helper.getView(R.id.iv_cart_com_pre)
                        , getStrings(cartProductInfoBean.getPicUrl()));
                if (cartProductInfoBean.isPresentProduct()) {
                    helper.setGone(R.id.rel_cart_price, false)
                            .setGone(R.id.tv_cart_cp_value, false)
                            .setText(R.id.tv_cart_com_pre_title, getStrings(cartProductInfoBean.getName()))
                            .itemView.setTag(cartProductInfoBean);
                } else {
                    helper.setGone(R.id.rel_cart_price, true)
                            .setGone(R.id.tv_cart_cp_value, true)
                            .setText(R.id.tv_cart_com_pre_title, getStrings(cartProductInfoBean.getName()))
                            .setVisible(R.id.tv_cart_cp_price_discount, !TextUtils.isEmpty(cartProductInfoBean.getCombineDecreasePrice()))
                            .setText(R.id.tv_cart_cp_price_discount, String.format(context.getResources().getString(R.string.car_discount_price)
                                    , getStrings(cartProductInfoBean.getCombineDecreasePrice())))
                            .setText(R.id.tv_cart_cp_value, getStrings(cartProductInfoBean.getSaleSkuValue()))
                            .setText(R.id.tv_cart_cp_price, ("￥" + getStrings(cartProductInfoBean.getPrice())))
                            .itemView.setTag(cartProductInfoBean);
                    if (!TextUtils.isEmpty(cartProductInfoBean.getIndentType())) {
                        setIndentProStatus(helper, cartProductInfoBean);
                    }
                }
                helper.setGone(R.id.tv_cart_com_pre_count, cartProductInfoBean.getCount() > 0)
                        .setText(R.id.tv_cart_com_pre_count, ("x" + cartProductInfoBean.getCount()))
                        .itemView.setTag(cartProductInfoBean);
                break;
            default:
                GlideImageLoaderUtil.loadFitCenter(context, (ImageView) helper.getView(R.id.iv_cart_com_pre)
                        , getStrings(cartProductInfoBean.getPicUrl()));
                if (cartProductInfoBean.isPresentProduct()) {
                    helper.setGone(R.id.rel_cart_price, false)
                            .setGone(R.id.tv_cart_cp_value, false)
                            .setText(R.id.tv_cart_com_pre_title, getStrings(cartProductInfoBean.getName()));
                } else {
                    helper.setGone(R.id.rel_cart_price, true)
                            .setGone(R.id.tv_cart_cp_value, true)
                            .setText(R.id.tv_cart_com_pre_title, getStrings(cartProductInfoBean.getName()))
                            .setVisible(R.id.tv_cart_cp_price_discount, !TextUtils.isEmpty(cartProductInfoBean.getCombineDecreasePrice()))
                            .setText(R.id.tv_cart_cp_price_discount, String.format(context.getResources().getString(R.string.car_discount_price)
                                    , getStrings(cartProductInfoBean.getCombineDecreasePrice())))
                            .setText(R.id.tv_cart_cp_value, getStrings(cartProductInfoBean.getSaleSkuValue()))
                            .setText(R.id.tv_cart_cp_price, ("￥" + getStrings(cartProductInfoBean.getPrice())))
                            .itemView.setTag(cartProductInfoBean);
                }
                helper.setGone(R.id.tv_cart_com_pre_count, cartProductInfoBean.getCount() > 0)
                        .setText(R.id.tv_cart_com_pre_count, ("x" + cartProductInfoBean.getCount()));
                break;
        }
    }

    private void setIndentProStatus(BaseViewHolder helper, CartProductInfoBean cartProductInfoBean) {
        TextView tv_indent_com_pre_pro_status = helper.getView(R.id.tv_indent_com_pre_pro_status);
        switch (cartProductInfoBean.getIndentType()) {
            case INDENT_TYPE:
                if (cartProductInfoBean.getStatus() == 11 || cartProductInfoBean.getStatus() == 13) {
                    helper.setGone(R.id.tv_indent_com_pre_pro_status, true).setText(R.id.tv_indent_com_pre_pro_status, INDENT_PRO_STATUS != null ?
                            INDENT_PRO_STATUS.get(String.valueOf(cartProductInfoBean.getStatus())) : "");
                } else if (cartProductInfoBean.getStatus() == -10 ||
                        cartProductInfoBean.getStatus() <= -26 && -40 <= cartProductInfoBean.getStatus()) {
                    tv_indent_com_pre_pro_status.setVisibility(View.VISIBLE);
                    tv_indent_com_pre_pro_status.setText(INDENT_PRO_STATUS != null ?
                            INDENT_PRO_STATUS.get(String.valueOf(cartProductInfoBean.getStatus())) : "");
                }else if (cartProductInfoBean.getStatus() == -10 ||
                        cartProductInfoBean.getStatus() <= -30 && -40 <= cartProductInfoBean.getStatus()
                        || 50 <= cartProductInfoBean.getStatus() && cartProductInfoBean.getStatus() <= 58) {
                    tv_indent_com_pre_pro_status.setVisibility(View.VISIBLE);
                    tv_indent_com_pre_pro_status.setText(INDENT_PRO_STATUS != null ?
                            INDENT_PRO_STATUS.get(String.valueOf(cartProductInfoBean.getStatus())) : "");
                    tv_indent_com_pre_pro_status.setEnabled(true);
                    tv_indent_com_pre_pro_status.setSelected(true);
                    helper.addOnClickListener(R.id.tv_indent_com_pre_pro_status).setTag(R.id.tv_indent_com_pre_pro_status, cartProductInfoBean);
                } else {
                    helper.setGone(R.id.tv_indent_com_pre_pro_status, false);
                }
                break;
            case INDENT_DETAILS_TYPE:
                if (cartProductInfoBean.getStatus() == 10) {
                    tv_indent_com_pre_pro_status.setVisibility(View.VISIBLE);
                    tv_indent_com_pre_pro_status.setEnabled(true);
                    tv_indent_com_pre_pro_status.setSelected(false);
                    tv_indent_com_pre_pro_status.setText("申请退款");
                    helper.addOnClickListener(R.id.tv_dir_indent_pro_status).setTag(R.id.tv_dir_indent_pro_status, cartProductInfoBean);
                } else if (cartProductInfoBean.getStatus() <= 40 && 30 <= cartProductInfoBean.getStatus()) {
                    tv_indent_com_pre_pro_status.setVisibility(View.VISIBLE);
                    tv_indent_com_pre_pro_status.setEnabled(true);
                    tv_indent_com_pre_pro_status.setSelected(false);
                    tv_indent_com_pre_pro_status.setText("申请售后");
                    helper.addOnClickListener(R.id.tv_indent_com_pre_pro_status).setTag(R.id.tv_indent_com_pre_pro_status, cartProductInfoBean);
                } else if (cartProductInfoBean.getStatus() == 11 || cartProductInfoBean.getStatus() == 13) {
                    tv_indent_com_pre_pro_status.setVisibility(View.VISIBLE);
                    tv_indent_com_pre_pro_status.setEnabled(false);
                    tv_indent_com_pre_pro_status.setSelected(false);
                    tv_indent_com_pre_pro_status.setText(INDENT_PRO_STATUS != null ?
                            INDENT_PRO_STATUS.get(String.valueOf(cartProductInfoBean.getStatus())) : "");
                } else if (cartProductInfoBean.getStatus() == -10 ||
                        cartProductInfoBean.getStatus() <= -30 && -40 <= cartProductInfoBean.getStatus()
                        || 50 <= cartProductInfoBean.getStatus() && cartProductInfoBean.getStatus() <= 58) {
                    tv_indent_com_pre_pro_status.setVisibility(View.VISIBLE);
                    tv_indent_com_pre_pro_status.setText(INDENT_PRO_STATUS != null ?
                            INDENT_PRO_STATUS.get(String.valueOf(cartProductInfoBean.getStatus())) : "");
                    tv_indent_com_pre_pro_status.setEnabled(true);
                    tv_indent_com_pre_pro_status.setSelected(true);
                    helper.addOnClickListener(R.id.tv_indent_com_pre_pro_status).setTag(R.id.tv_indent_com_pre_pro_status, cartProductInfoBean);
                } else {
                    helper.setGone(R.id.tv_indent_com_pre_pro_status, false);
                }
                break;
        }
    }
}
