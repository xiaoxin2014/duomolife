package com.amkj.dmsh.dominant.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.dominant.bean.QualityBuyListEntity.QualityBuyListBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/6/26
 * class description:请输入类描述
 */

public class QualityBuyListAdapter extends BaseQuickAdapter<QualityBuyListBean, BaseViewHolder> {
    private final Context context;

    public QualityBuyListAdapter(Context context, List<QualityBuyListBean> qualityBuyListBeanList) {
        super(R.layout.adapter_ql_shop_buy_rec, qualityBuyListBeanList);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, QualityBuyListBean qualityBuyListBean) {
        GlideImageLoaderUtil.loadFitCenter(context, (ImageView) helper.getView(R.id.iv_ql_bl_product), qualityBuyListBean.getPicUrl());
        helper.setText(R.id.tv_ql_bl_pro_name, getStrings(qualityBuyListBean.getName()))
                .setText(R.id.tv_ql_bl_pro_rec, "推荐理由：" + getStrings(qualityBuyListBean.getRecommendReason()))
                .setText(R.id.tv_ql_bl_pro_price, "￥ " + qualityBuyListBean.getPrice())
                .setGone(R.id.iv_com_pro_tag_out, qualityBuyListBean.getQuantity() < 1 )
                .addOnClickListener(R.id.iv_ql_bl_add_car).setTag(R.id.iv_ql_bl_add_car, qualityBuyListBean);
        ImageView iv_ql_bl_add_car = helper.getView(R.id.iv_ql_bl_add_car);
        iv_ql_bl_add_car.setSelected(qualityBuyListBean.isInCart());
        helper.itemView.setTag(qualityBuyListBean);
    }
}
