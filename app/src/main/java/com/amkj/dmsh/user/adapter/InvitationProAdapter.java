package com.amkj.dmsh.user.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.homepage.bean.InvitationDetailEntity.InvitationDetailBean.RelevanceProBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/8/11
 * class description:关联商品
 */

public class InvitationProAdapter extends BaseMultiItemQuickAdapter<RelevanceProBean, BaseViewHolder> {
    private final Context context;

    public InvitationProAdapter(Context context, List<RelevanceProBean> relevanceProList) {
        super(relevanceProList);
        addItemType(ConstantVariable.TYPE_0, R.layout.adapter_show_relevance_product);
        addItemType(ConstantVariable.TYPE_1, R.layout.adapter_inv_pro_gray_more);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, RelevanceProBean relevanceProBean) {
        switch (helper.getItemViewType()) {
            case ConstantVariable.TYPE_0:
                GlideImageLoaderUtil.loadCenterCrop(context, (ImageView) helper.getView(R.id.iv_inv_pro), relevanceProBean.getPictureUrl());
                helper.setText(R.id.tv_inv_pro_name, getStrings(relevanceProBean.getTitle()))
                        .setText(R.id.tv_inv_pro_price, "￥" + getStrings(relevanceProBean.getPrice()));
                helper.itemView.setTag(relevanceProBean);
                break;
            case ConstantVariable.TYPE_1:
                helper.itemView.setTag(relevanceProBean);
                break;
        }
    }
}
