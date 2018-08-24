package com.amkj.dmsh.release.adapter;

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
 * created on 2017/8/29
 * class description:关联商品选择
 */

public class RelevanceProAdapter extends BaseQuickAdapter<RelevanceProBean, BaseViewHolderHelper> {

    private final Context context;

    public RelevanceProAdapter(Context context, List<RelevanceProBean> relevanceProList) {
        super(R.layout.layout_relevance_pro_sel_item, relevanceProList);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolderHelper helper, RelevanceProBean relevanceProBean) {
        GlideImageLoaderUtil.loadCenterCrop(context, (ImageView) helper.getView(R.id.iv_rev_pro_img), getStrings(relevanceProBean.getPictureUrl()));
        helper.setText(R.id.tv_rev_pro_name, getStrings(relevanceProBean.getTitle()))
                .setText(R.id.tv_rev_pro_price, "￥" + getStrings(relevanceProBean.getPrice()))
                .setChecked(R.id.cb_rev_pro_sel, relevanceProBean.isSelPro())
                .addOnClickListener(R.id.cb_rev_pro_sel)
                .setTag(R.id.cb_rev_pro_sel, relevanceProBean)
                .setTag(R.id.cb_rev_pro_sel, R.id.shop_car_cb, helper.getLayoutPosition() - getHeaderLayoutCount());
    }
}
