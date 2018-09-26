package com.amkj.dmsh.release.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.release.bean.RelevanceProEntity.RelevanceProBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/8/11
 * class description:帖子商品关联
 */

public class AddRelevanceProAdapter extends BaseQuickAdapter<RelevanceProBean, BaseViewHolder> {
    private final Context context;

    public AddRelevanceProAdapter(Context context, List<RelevanceProBean> relevanceProList) {
        super(R.layout.adapter_add_relevance_product, relevanceProList);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, RelevanceProBean relevanceProBean) {
        GlideImageLoaderUtil.loadCenterCrop(context, (ImageView) helper.getView(R.id.iv_release_product_img), relevanceProBean.getPictureUrl());
        helper.addOnClickListener(R.id.iv_release_product_img).setTag(R.id.iv_release_product_img, R.id.iv_tag, relevanceProBean)
                .setGone(R.id.iv_release_product_img_sel,relevanceProBean.isSelPro())
                .addOnClickListener(R.id.iv_release_product_img_sel).setTag(R.id.iv_release_product_img_sel, relevanceProBean);
        helper.itemView.setTag(relevanceProBean);
    }
}
