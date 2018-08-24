package com.amkj.dmsh.homepage.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.bean.IntegrationProEntity.IntegrationBean;
import com.amkj.dmsh.constant.BaseViewHolderHelper;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/8/4
 * version 3.1.5
 * class description:积分商品
 */
public class IntegralProductAdapter extends BaseQuickAdapter<IntegrationBean,BaseViewHolderHelper>{

    private final Context context;

    public IntegralProductAdapter(Context context, List<IntegrationBean> integrationBeanList) {
        super(R.layout.adapter_integral_product_list,integrationBeanList);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolderHelper helper, IntegrationBean integrationBean) {
        ImageView iv_integral_shop_product_img = helper.getView(R.id.iv_integral_shop_product_tag_out);
        iv_integral_shop_product_img.setImageResource(R.drawable.goods_sold_out);
        if (integrationBean.getQuantity() < 1) {
            iv_integral_shop_product_img.setVisibility(View.VISIBLE);
        } else {
            iv_integral_shop_product_img.setVisibility(View.GONE);
        }
        GlideImageLoaderUtil.loadCenterCrop(context, helper.getView(R.id.iv_integral_shop_product_img)
                , integrationBean.getPicUrl());
        helper.setText(R.id.tv_integral_shop_product_name, getStrings(integrationBean.getName()));
        String priceName;
        if(integrationBean.getIntegralType() ==0){
            priceName = integrationBean.getIntegralPrice() + "积分";
        }else{
            priceName = String.format(context.getResources().getString(R.string.integral_product_and_price)
                    ,integrationBean.getIntegralPrice(),getStrings(integrationBean.getMoneyPrice()));
        }
        helper.setText(R.id.tv_integral_shop_product_price, priceName);
        helper.itemView.setTag(integrationBean);
    }
}
