package com.amkj.dmsh.dominant.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.dominant.bean.QualityGroupEntity.QualityGroupBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.utils.TimeUtils.getCurrentTime;
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
        helper.setText(R.id.tv_gp_sp_name, getStrings(TextUtils.isEmpty(qualityGroupBean.getSubtitle()) ? qualityGroupBean.getName() : (qualityGroupBean.getSubtitle() + "•" + qualityGroupBean.getName())))
                .setText(R.id.tv_gp_sp_per_count, qualityGroupBean.getGpType())//拼团人数
                .setText(R.id.tv_gp_sp_per_price, "¥" + qualityGroupBean.getGpPrice())//团购价
                .setText(R.id.tv_gp_sp_nor_price, "单买价¥" + qualityGroupBean.getPrice())//单买价
                .setText(R.id.tv_gp_sp_open_count, ConstantMethod.getStringsFormat(context, R.string.already_join_group_num, qualityGroupBean.getGpCount()))//参与人数
                .setText(R.id.tv_gp_sp_count, qualityGroupBean.getGpProductQuantity() > 0 ? "去开团" : "已抢光")
//                .setGone(R.id.tv_ql_gp_sp_new, qualityGroupBean.getRange() == 1)
                .setText(R.id.tv_ql_gp_sp_new, qualityGroupBean.getRange() == 1 ? "邀新团" : "特价团")
                .setEnabled(R.id.tv_gp_sp_count, qualityGroupBean.getGpProductQuantity() > 0);//团购类型
        TextView tvGroupTime = helper.getView(R.id.tv_group_time);
        String timeStatus;
        String timeDifferent = "";
        String currentTime = getCurrentTime(qualityGroupBean);
        //未结束
        if (!isEndOrStartTime(currentTime, qualityGroupBean.getGpEndTime())) {
            if (isEndOrStartTime(qualityGroupBean.getGpStartTime(), getCurrentTime(qualityGroupBean))) {
                timeStatus = "距开始:";
                timeDifferent = getTimeDifference(qualityGroupBean.getGpStartTime(), currentTime);
            } else {
                timeStatus = "距结束:";
                timeDifferent = getTimeDifference(qualityGroupBean.getGpEndTime(), currentTime);
            }
        } else {
            timeStatus = "已结束";
        }

        tvGroupTime.setText(getStrings(timeStatus + timeDifferent));
        helper.itemView.setTag(qualityGroupBean);
    }

    //获取两个时间之间的时间差
    private String getTimeDifference(String t1, String t2) {
        String timeDifference = "";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        try {
            long time1 = formatter.parse(t1).getTime();
            long time2 = formatter.parse(t2).getTime();
            long abs = Math.abs(time1 - time2);
            int day = (int) (abs / (1000 * 60 * 60 * 24));
            int hour = (int) ((abs % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
            int minute = (int) ((abs % (1000 * 60 * 60)) / (1000 * 60));
            int second = (int) ((abs % (1000 * 60)) / 1000);

            if (day != 0) {
                timeDifference = day + "天" + hour + "时";
            } else if (hour != 0) {
                timeDifference = hour + "时" + minute + "分";
            } else {
                timeDifference = minute + "分" + second + "秒";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return timeDifference;
    }
}
