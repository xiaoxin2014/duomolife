package com.amkj.dmsh.find.adapter;

import android.content.Context;

import com.amkj.dmsh.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2019/3/7
 * version 3.3.0
 * class description:精选文章-关联商品
 */
public class FeaturedRelevanceProductAdapter extends BaseQuickAdapter<Object,BaseViewHolder>{
    private final Context context;

    public FeaturedRelevanceProductAdapter(Context context, List<Object> data) {
        super(R.layout.adapter_show_relevance_product, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, Object item) {
//        TextView textView = helper.getView(R.id.tv_inv_pro_price);
//        textView.setTextColor(context.getResources().getColor(R.color.text_gray_hint_n));
//        GlideImageLoaderUtil.loadCenterCrop(context, (ImageView) helper.getView(R.id.iv_inv_pro), relevanceProBean.getPictureUrl());
//        helper.setText(R.id.tv_inv_pro_name, getStrings(relevanceProBean.getTitle()))
//                .setText(R.id.tv_inv_pro_price, "￥" + getStrings(relevanceProBean.getPrice()));
//        helper.itemView.setTag(relevanceProBean);
    }
}
