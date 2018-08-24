package com.amkj.dmsh.find.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.BaseViewHolderHelper;
import com.amkj.dmsh.release.bean.RelevanceProEntity.RelevanceProBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/11/4
 * class description:标签关联商品列表
 */

public class TagRelProAdapter extends BaseQuickAdapter<RelevanceProBean, BaseViewHolderHelper> {
    private final Context context;

    public TagRelProAdapter(Context context, List<RelevanceProBean> relevanceProBeanList) {
        super(R.layout.adapter_tag_rel_pro, relevanceProBeanList);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolderHelper helper, RelevanceProBean relevanceProBean) {
        GlideImageLoaderUtil.loadCenterCrop(context, (ImageView) helper.getView(R.id.iv_tag_rel_pro), getStrings(relevanceProBean.getPictureUrl()));
        helper.setText(R.id.tv_tag_rel_pro_name, getStrings(relevanceProBean.getTitle()))
                .setText(R.id.tv_tag_rel_pro_price, "￥" + getStrings(relevanceProBean.getPrice()))
                .itemView.setTag(relevanceProBean);
    }
}
