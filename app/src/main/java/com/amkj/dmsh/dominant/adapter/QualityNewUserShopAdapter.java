package com.amkj.dmsh.dominant.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.BaseViewHolderHelper;
import com.amkj.dmsh.dominant.bean.QualityNewUserShopEntity.QualityNewUserShopBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsChNPrice;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_0;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_1;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_2;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/7/5
 * class description:新人专享
 */

public class QualityNewUserShopAdapter extends BaseMultiItemQuickAdapter<QualityNewUserShopBean, BaseViewHolderHelper> {
    private final Context context;

    public QualityNewUserShopAdapter(Context context, List<QualityNewUserShopBean> qualityNewUserShopList) {
        super(qualityNewUserShopList);
        addItemType(TYPE_0, R.layout.adapter_layout_qt_pro);
//        头部标题栏
        addItemType(TYPE_1, R.layout.adapter_ql_goods_pro_header);
//        购物车商品
        addItemType(TYPE_2, R.layout.adapter_layout_qt_pro);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolderHelper helper, QualityNewUserShopBean qualityNewUserShopBean) {
        if (helper.getItemViewType() == TYPE_0) {
            GlideImageLoaderUtil.loadCenterCrop(context, helper.getView(R.id.iv_qt_pro_img)
                    , qualityNewUserShopBean.getPicUrl());
            TextView tv_qt_pro_wait_buy = helper.getView(R.id.tv_qt_pro_wait_buy);
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setCornerRadius(AutoUtils.getPercentWidthSize(16));
            gradientDrawable.setColor(context.getResources().getColor(R.color.yellow_bg_ffae));
            tv_qt_pro_wait_buy.setBackground(gradientDrawable);
            helper.setGone(R.id.iv_com_pro_tag_out, qualityNewUserShopBean.getQuantity() < 1)
                    .setText(R.id.tv_qt_pro_descrip, getStrings(qualityNewUserShopBean.getName()))
                    .setText(R.id.tv_qt_pro_price, "￥" + qualityNewUserShopBean.getPrice())
                    .setText(R.id.tv_qt_pro_wait_buy, "新人专享")
                    .setGone(R.id.tv_qt_pro_wait_buy, true)
                    .setGone(R.id.iv_pro_add_car, false)
                    .setGone(R.id.tv_qt_pro_name, false)
                    .itemView.setTag(qualityNewUserShopBean);
        } else if (helper.getItemViewType() == TYPE_1) {
            TextView textView = helper.getView(R.id.tv_pro_title);
            textView.setBackgroundColor(context.getResources().getColor(R.color.white));
            helper.setText(R.id.tv_pro_title, getStrings(qualityNewUserShopBean.getcTitle()));
        } else if (helper.getItemViewType() == TYPE_2) {
            GlideImageLoaderUtil.loadCenterCrop(context, helper.getView(R.id.iv_qt_pro_img)
                    , qualityNewUserShopBean.getPicUrl());
            helper.setGone(R.id.iv_com_pro_tag_out, qualityNewUserShopBean.getQuantity() < 1)
                    .setText(R.id.tv_qt_pro_descrip, getStrings(qualityNewUserShopBean.getName()))
                    .setText(R.id.tv_qt_pro_price, getStringsChNPrice(context, qualityNewUserShopBean.getPrice()))
                    .setGone(R.id.tv_qt_pro_wait_buy, false)
                    .setGone(R.id.iv_pro_add_car, true)
                    .addOnClickListener(R.id.iv_pro_add_car)
                    .setTag(R.id.iv_pro_add_car, R.id.iv_tag, qualityNewUserShopBean)
                    .setGone(R.id.tv_qt_pro_name, false)
                    .itemView.setTag(qualityNewUserShopBean);
        }
    }
}
