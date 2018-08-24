package com.amkj.dmsh.shopdetails.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.BaseViewHolderHelper;
import com.amkj.dmsh.shopdetails.bean.DirectLogisticsEntity.DirectLogisticsBean.LogisticsBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * Created by atd48 on 2016/10/31.
 */
public class DirectLogisticsHeaderAdapter extends BaseQuickAdapter<LogisticsBean, BaseViewHolderHelper> {
    private final Context context;

    public DirectLogisticsHeaderAdapter(Context context, List<LogisticsBean> logisticsBeanList) {
        super(R.layout.adapter_direct_logistics_goods, logisticsBeanList);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolderHelper helper, LogisticsBean logisticsBean) {
        GlideImageLoaderUtil.loadCenterCrop(context, (ImageView) helper.getView(R.id.iv_logistic_indent_pro), logisticsBean.getPicUrl());
        helper.setText(R.id.tv_logistic_indent_pro_name, getStrings(logisticsBean.getName()))
                .setText(R.id.tv_logistics_indent_time, "下单时间：" + logisticsBean.getDeliverTime());
    }
}
