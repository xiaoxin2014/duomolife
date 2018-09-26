package com.amkj.dmsh.dominant.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.dominant.bean.DmlSearchDetailEntity.DmlSearchDetailBean.ProductListBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/7/7
 * class description:侧滑商品
 */

public class WelfareSlideProAdapter extends BaseQuickAdapter<ProductListBean, BaseViewHolder> {
    private final Context context;

    public WelfareSlideProAdapter(Context context, List<ProductListBean> welfareProductList) {
        super(R.layout.adapter_wel_slide_pro, welfareProductList);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, ProductListBean productListBean) {
        GlideImageLoaderUtil.loadHeaderImg(context, (ImageView) helper.getView(R.id.iv_wel_slide_pro), productListBean.getPicUrl());
        helper.setText(R.id.tv_wel_slide_pro_name, getStrings(productListBean.getName()))
                .setText(R.id.tv_wel_slide_pro_price, "￥ " + productListBean.getPrice());
        helper.itemView.setTag(productListBean);
    }
}
