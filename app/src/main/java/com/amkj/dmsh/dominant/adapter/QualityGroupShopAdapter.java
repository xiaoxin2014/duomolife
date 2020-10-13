package com.amkj.dmsh.dominant.adapter;

import android.content.Context;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.dominant.bean.QualityGroupEntity.QualityGroupBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.utils.TimeUtils.getCurrentTime;
import static com.amkj.dmsh.utils.TimeUtils.getTimeDifference;
import static com.amkj.dmsh.utils.TimeUtils.getTimeDifferenceText;
import static com.amkj.dmsh.utils.TimeUtils.isEndOrStartTime;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/6/6
 * class description:拼团列表
 */

public class QualityGroupShopAdapter extends BaseMultiItemQuickAdapter<QualityGroupBean, BaseViewHolder> {
    private final Context context;

    public QualityGroupShopAdapter(Context context, List<QualityGroupBean> data) {
        super(data);
        addItemType(1, R.layout.adapter_layout_ql_gp_first);
        addItemType(0, R.layout.adapter_layout_ql_gp_sp);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, QualityGroupBean qualityGroupBean) {
        GlideImageLoaderUtil.loadCenterCrop(context, helper.getView(R.id.iv_communal_img_bg), qualityGroupBean.getCoverImage());
        helper.setText(R.id.tv_gp_sp_name, getStrings(qualityGroupBean.getGpName()))
                .setText(R.id.tv_gp_sp_per_count, getStrings(qualityGroupBean.getRequireCount()) + "人团")//拼团人数
                .setText(R.id.tv_gp_sp_per_price, "¥" + qualityGroupBean.getGpPrice())//团购价
                .setText(R.id.tv_gp_sp_nor_price, "单买价¥" + qualityGroupBean.getPrice())//单买价
                .setText(R.id.tv_gp_sp_open_count, ConstantMethod.getStringsFormat(context, R.string.already_join_group_num, qualityGroupBean.getPartakeCount()))//参与人数
                .setText(R.id.tv_gp_sp_count, qualityGroupBean.getGpQuantity() > 0 ? "去开团" : "库存不足")
                .setText(R.id.tv_ql_gp_sp_new, getStrings(qualityGroupBean.getGpTypeText()))
                .setEnabled(R.id.tv_gp_sp_count, qualityGroupBean.getGpQuantity() > 0);//团购类型
        TextView tvGroupTime = helper.getView(R.id.tv_group_time);
        String timeStatus;
        String timeDifferent = "";
        String currentTime = getCurrentTime(qualityGroupBean);
        //已结束
        if (isEndOrStartTime(currentTime, qualityGroupBean.getEndTime())) {
            timeStatus = "已结束";
        } else if (isEndOrStartTime(currentTime, qualityGroupBean.getStartTime())) {
            //已开始未结束
            timeStatus = "距结束:";
            timeDifferent = getTimeDifferenceText(getTimeDifference(qualityGroupBean.getEndTime(), currentTime));
        } else {
            //未开始
            timeStatus = "距开始:";
            timeDifferent = getTimeDifferenceText(getTimeDifference(qualityGroupBean.getStartTime(), currentTime));
        }
        tvGroupTime.setText(getStrings(timeStatus + timeDifferent));
        helper.itemView.setTag(qualityGroupBean);
    }
}
