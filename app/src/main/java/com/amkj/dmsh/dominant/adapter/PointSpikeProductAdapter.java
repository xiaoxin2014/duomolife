package com.amkj.dmsh.dominant.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.dominant.bean.PointSpikeProductEntity.TimeAxisProductListBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsChNPrice;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2019/3/2
 * version 3.3.0
 * class description:整点秒杀
 */
public class PointSpikeProductAdapter extends BaseQuickAdapter<TimeAxisProductListBean, BaseViewHolder> {

    private final Context context;

    public PointSpikeProductAdapter(Context context, List<TimeAxisProductListBean> data) {
        super(R.layout.adapter_layout_point_spike, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, TimeAxisProductListBean timeAxisProductListBean) {
        CheckedTextView checkedTextView = helper.getView(R.id.tv_point_spike_done);
        TextView tv_point_spike_market_price = helper.getView(R.id.tv_point_spike_market_price);
        tv_point_spike_market_price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tv_point_spike_market_price.setText(getStringsChNPrice(context, timeAxisProductListBean.getMarketPrice()));
//        先判断抢购状态
        String proStatus;
        if (timeAxisProductListBean.getStatusCode() == 0) {
            checkedTextView.setChecked(timeAxisProductListBean.getIsNotice() == 0);
            proStatus = timeAxisProductListBean.getIsNotice() == 0?"提醒我":"已设置";
            checkedTextView.setChecked(timeAxisProductListBean.getIsNotice() == 1);
        } else if (timeAxisProductListBean.getStatusCode() == 1) {
            checkedTextView.setSelected(true);
            proStatus = "马上抢";
        } else {
            proStatus = "已过期";
            checkedTextView.setChecked(true);
        }
        checkedTextView.setText(proStatus);
        GlideImageLoaderUtil.loadCenterCrop(context, helper.getView(R.id.iv_point_spike_img), timeAxisProductListBean.getPath());
        helper.setText(R.id.tv_point_spike_price, getStringsChNPrice(context, timeAxisProductListBean.getPrice()))
                .setText(R.id.tv_point_spike_title,getStrings(timeAxisProductListBean.getTitle()))
                .setText(R.id.tv_point_spike_subtitle,getStrings(timeAxisProductListBean.getSubtitle()))
                .addOnClickListener(R.id.tv_point_spike_done)
                .setTag(R.id.tv_point_spike_done,timeAxisProductListBean)
                .itemView.setTag(timeAxisProductListBean);
    }
}
