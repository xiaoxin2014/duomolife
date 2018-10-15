package com.amkj.dmsh.dominant.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.dominant.bean.QualityGroupMineEntity.QualityGroupMineBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.R.id.tv_ql_gp_mine_inv_join;
import static com.amkj.dmsh.constant.ConstantMethod.getIntegralFormat;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

;
;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/6/8
 * class description:拼团订单
 */

public class QualityGroupMineAdapter extends BaseQuickAdapter<QualityGroupMineBean, BaseViewHolder> {
    private final Context context;

    public QualityGroupMineAdapter(Context context, List<QualityGroupMineBean> qualityGroupMineList) {
        super(R.layout.adapter_ql_mine_gp_indent, qualityGroupMineList);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, QualityGroupMineBean qualityGroupMineBean) {
        GlideImageLoaderUtil.loadCenterCrop(context, (ImageView) helper.getView(R.id.iv_gp_product), qualityGroupMineBean.getGpPicUrl());
        helper.setText(R.id.tv_ql_mine_time, !TextUtils.isEmpty(qualityGroupMineBean.getPayTime())
                ? qualityGroupMineBean.getPayTime() : getStrings(qualityGroupMineBean.getCreateTime()))
                .setText(R.id.tv_gp_indent_name, getStrings(qualityGroupMineBean.getName()))
                .setText(R.id.tv_direct_indent_pro_sku, getStrings(qualityGroupMineBean.getProductSkuValue()))
                .addOnClickListener(tv_ql_gp_mine_inv_join).setTag(tv_ql_gp_mine_inv_join, qualityGroupMineBean)
                .addOnClickListener(R.id.tv_ql_gp_mine_details).setTag(R.id.tv_ql_gp_mine_details, qualityGroupMineBean);
        if (qualityGroupMineBean.getGpStatus() == 3 || qualityGroupMineBean.getGpStatus() == 4) {
            helper.setText(R.id.tv_ql_gp_mine_inv_join, "去支付").setGone(R.id.tv_ql_gp_mine_inv_join, true);
        } else if (qualityGroupMineBean.getGpStatus() == 1) {
            helper.setText(R.id.tv_ql_gp_mine_inv_join, "邀请参团")
                    .setGone(R.id.tv_ql_gp_mine_inv_join, true);
        } else {
            helper.setGone(R.id.tv_ql_gp_mine_inv_join, false);
        }
        TextView tv_ql_mine_status = helper.getView(R.id.tv_ql_mine_status);
        if (qualityGroupMineBean.getGpStatus() == 1) {
            tv_ql_mine_status.setText(getIntegralFormat(context,R.string.group_residue_join_count,qualityGroupMineBean.getLeftParticipant()));
            tv_ql_mine_status.setSelected(true);
        } else {
            tv_ql_mine_status.setSelected(false);
            tv_ql_mine_status.setText(qualityGroupMineBean.getGpStatusMsg());
        }
        helper.itemView.setTag(qualityGroupMineBean);
    }
}
