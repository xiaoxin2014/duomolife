package com.amkj.dmsh.shopdetails.adapter;

import com.amkj.dmsh.R;
import com.amkj.dmsh.shopdetails.bean.RefundTypeBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/2/2
 * version 3.9
 * class description:售后类型数据
 */

public class RefundTypeAdapter extends BaseQuickAdapter<RefundTypeBean,BaseViewHolder>{
    public RefundTypeAdapter(List<RefundTypeBean> refundTypeBeans) {
        super(R.layout.adapter_refund_date,refundTypeBeans);
    }

    @Override
    protected void convert(BaseViewHolder helper, RefundTypeBean refundTypeBean) {
        helper.setText(R.id.tv_refund_type,refundTypeBean.getRefundType())
                .setText(R.id.tv_refund_date,refundTypeBean.getRefundDate());
    }
}
