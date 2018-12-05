package com.amkj.dmsh.shopdetails.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.shopdetails.bean.DirectLogisticsEntity.DirectLogisticsBean.LogisticsProductPacketBean.LogisticsDetailsBean.LogisticsBean.LogisticTextBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

;

/**
 * Created by atd48 on 2016/10/31.
 * 物流信息
 */
public class DirectLogisticsAdapter extends BaseQuickAdapter<LogisticTextBean, BaseViewHolder> {

    public DirectLogisticsAdapter(List<LogisticTextBean> logistics) {
        super(R.layout.adapter_direct_logistics_info, logistics);
    }

    @Override
    protected void convert(BaseViewHolder helper, LogisticTextBean logistic) {
        ImageView iv_direct_logistic_icon = helper.getView(R.id.iv_direct_logistic_icon);
        TextView tv_direct_logist_info = helper.getView(R.id.tv_direct_logist_info);
        TextView tv_direct_logist_info_time = helper.getView(R.id.tv_direct_logist_info_time);
        if (helper.getAdapterPosition() - getHeaderLayoutCount() == 0) {
            iv_direct_logistic_icon.setSelected(true);
            tv_direct_logist_info.setSelected(true);
            tv_direct_logist_info_time.setSelected(true);
        }else{
            iv_direct_logistic_icon.setSelected(false);
            tv_direct_logist_info.setSelected(false);
            tv_direct_logist_info_time.setSelected(false);
        }
        helper.setText(R.id.tv_direct_logist_info, getStrings(logistic.getStatus()))
                .setText(R.id.tv_direct_logist_info_time, getStrings(logistic.getTime()));
    }
}
