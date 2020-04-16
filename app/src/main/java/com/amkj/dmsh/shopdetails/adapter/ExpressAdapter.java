package com.amkj.dmsh.shopdetails.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.amkj.dmsh.R;
import com.amkj.dmsh.bean.LogisticTextBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by xiaoxin on 2020/3/17
 * Version:v4.4.3
 * ClassDescription :物流记录适配器
 */
public class ExpressAdapter extends BaseQuickAdapter<LogisticTextBean, BaseViewHolder> {

    private final Context mContext;

    public ExpressAdapter(Context context, List<LogisticTextBean> logisticsBeanList) {
        super(R.layout.item_express_log, logisticsBeanList);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, LogisticTextBean logisticTextBean) {
        if (logisticTextBean == null) return;
        helper.setText(R.id.tv_status, logisticTextBean.getStatus())
                .setTextColor(R.id.tv_status, mContext.getResources().getColor(helper.getPosition() == 1 ? R.color.text_login_blue_z : R.color.text_login_gray_s))
                .setText(R.id.tv_time, logisticTextBean.getTime())
                .setGone(R.id.tv_time, !TextUtils.isEmpty(logisticTextBean.getTime()))
                .setImageResource(R.id.iv_icon, helper.getPosition() == 0 ? R.drawable.location : (helper.getPosition() == 1 ? R.drawable.express_blue : R.drawable.express));
    }
}
