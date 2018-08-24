package com.amkj.dmsh.homepage.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.bean.DMLRecommend.ProductBean;
import com.amkj.dmsh.constant.BaseViewHolderHelper;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/5/12
 * class description:请输入类描述
 */

public class DoMoLifeWelfareProAdapter extends BaseQuickAdapter<ProductBean, BaseViewHolderHelper> {
    private final Context context;

    public DoMoLifeWelfareProAdapter(Context context, List<ProductBean> brandProductList) {
        super(R.layout.adapter_promotion_pro_item, brandProductList);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolderHelper helper, ProductBean productBean) {
        if (productBean.getQuantity() < 1) {
            helper.setGone(R.id.img_spring_sale_tag_out, true);
        } else {
            helper.setGone(R.id.img_spring_sale_tag_out, false);
        }
        GlideImageLoaderUtil.loadCenterCrop(context, (ImageView) helper.getView(R.id.img_springSale_product), productBean.getPicUrl());
        helper.setText(R.id.tv_product_duomolife_price, "￥" + productBean.getPrice());
        helper.setText(R.id.tv_springSale_introduce, getStrings(productBean.getName()));
        TextView tv_product_original_price = helper.getView(R.id.tv_product_original_price);
        tv_product_original_price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tv_product_original_price.setText("￥" + productBean.getMarketPrice());
        helper.itemView.setTag(productBean);
    }
}
