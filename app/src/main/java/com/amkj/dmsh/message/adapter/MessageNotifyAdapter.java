package com.amkj.dmsh.message.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.message.bean.MessageNotifyEntity.MessageNotifyBean;
import com.amkj.dmsh.message.bean.MessageNotifyEntity.MessageNotifyBean.ProductPriceBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/8/1
 * class description:请输入类描述
 */

public class MessageNotifyAdapter extends BaseQuickAdapter<MessageNotifyBean, BaseViewHolder> {
    private final Context context;

    public MessageNotifyAdapter(Context context, List<MessageNotifyBean> officialBeanList) {
        super(R.layout.adapter_mes_notify, officialBeanList);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, MessageNotifyBean messageIndentBean) {
        ImageView iv_mes_notify_cover_pro = helper.getView(R.id.iv_mes_notify_cover_pro);
        ImageView iv_mes_notify_cover_icon = helper.getView(R.id.iv_mes_notify_cover_icon);
        helper.setGone(R.id.fl_mes_sys, true);
        helper.setText(R.id.tv_mes_notify_look, !TextUtils.isEmpty(messageIndentBean.getButtonName()) ? messageIndentBean.getButtonName() : "立即查看");
        switch (getStrings(messageIndentBean.getObj())) {
            case "goods":
                GlideImageLoaderUtil.loadCenterCrop(context, iv_mes_notify_cover_pro, messageIndentBean.getImage());
                iv_mes_notify_cover_pro.setVisibility(View.VISIBLE);
                iv_mes_notify_cover_icon.setVisibility(View.GONE);
                break;
            case "coupon":
                iv_mes_notify_cover_pro.setVisibility(View.GONE);
                iv_mes_notify_cover_icon.setVisibility(View.VISIBLE);
                iv_mes_notify_cover_icon.setImageResource(R.drawable.mes_coupon);
                break;
            case "score":
                iv_mes_notify_cover_pro.setVisibility(View.GONE);
                iv_mes_notify_cover_icon.setVisibility(View.VISIBLE);
                iv_mes_notify_cover_icon.setImageResource(R.drawable.mes_integ);
                break;
            case "prize":
                iv_mes_notify_cover_pro.setVisibility(View.GONE);
                iv_mes_notify_cover_icon.setVisibility(View.VISIBLE);
                iv_mes_notify_cover_icon.setImageResource(R.drawable.mes_gift);
                break;
            case "csnotice":
                helper.setGone(R.id.fl_mes_sys, false)
                        .setText(R.id.tv_mes_notify_look, "联系客服");
                break;
            case "platformnotice":
                helper.setGone(R.id.fl_mes_sys, false);
                break;
            case "vip":
                GlideImageLoaderUtil.loadCenterCrop(context, iv_mes_notify_cover_pro, messageIndentBean.getPath());
                iv_mes_notify_cover_pro.setVisibility(View.VISIBLE);
                iv_mes_notify_cover_icon.setVisibility(View.GONE);
                break;
            default:
                helper.setGone(R.id.fl_mes_sys, false);
                break;
        }

        if (messageIndentBean.getProductPriceBean() != null
                && !TextUtils.isEmpty(messageIndentBean.getTitle())) {
            ProductPriceBean productPriceBean = messageIndentBean.getProductPriceBean();
            TextView tv_mes_product_market_price = helper.getView(R.id.tv_mes_product_market_price);
            tv_mes_product_market_price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            helper.setText(R.id.tv_mes_notify_content, getStrings(messageIndentBean.getTitle()))
                    .setGone(R.id.tv_mes_notify_product_title, !TextUtils.isEmpty(messageIndentBean.getTitle()))
                    .setText(R.id.tv_mes_notify_product_title, getStrings(messageIndentBean.getM_content()))
                    .setGone(R.id.tv_mes_notify_look, !TextUtils.isEmpty(messageIndentBean.getAndroidLink()))
                    .setGone(R.id.rel_product_price, true)
                    .setText(R.id.tv_mes_product_price, context.getString(R.string.money_price_chn, getStrings(productPriceBean.getNowPrice())))
                    .setText(R.id.tv_mes_product_market_price, context.getString(R.string.money_price_chn, getStrings(productPriceBean.getOldPrice())));
        } else if ("birthday".equals(messageIndentBean.getObj())) {
            helper.setText(R.id.tv_mes_notify_content, getStrings(messageIndentBean.getM_content()))
                    .setGone(R.id.tv_mes_notify_look, !TextUtils.isEmpty(messageIndentBean.getAndroidLink()))
                    .setText(R.id.tv_mes_notify_look, "查看更多")
                    .setGone(R.id.rel_product_price, false)
                    .setText(R.id.tv_mes_notify_product_title, getStrings(messageIndentBean.getM_title()))
                    .setGone(R.id.tv_mes_notify_product_title, !TextUtils.isEmpty(messageIndentBean.getM_title()));
        } else {
            helper.setText(R.id.tv_mes_notify_content, getStrings(messageIndentBean.getM_content()))
                    .setGone(R.id.tv_mes_notify_look, !TextUtils.isEmpty(messageIndentBean.getAndroidLink())
                            || "csnotice".equals(getStrings(messageIndentBean.getObj())))
                    .setGone(R.id.rel_product_price, false)
                    .setGone(R.id.tv_mes_notify_product_title, false);
        }
        helper.setText(R.id.tv_mes_notify_time, getStrings(messageIndentBean.getCtime()))
                .setGone(R.id.tv_mes_notify_type_name, !TextUtils.isEmpty(messageIndentBean.getFlagName()))
                .setText(R.id.tv_mes_notify_type_name, getStrings(messageIndentBean.getFlagName()))
                .itemView.setTag(messageIndentBean);
    }
}
