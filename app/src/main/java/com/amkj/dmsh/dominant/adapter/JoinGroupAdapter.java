package com.amkj.dmsh.dominant.adapter;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.support.annotation.NonNull;

import com.amkj.dmsh.R;
import com.amkj.dmsh.dominant.bean.GroupShopJoinEntity.GroupShopJoinBean;
import com.amkj.dmsh.utils.CountDownTimer;
import com.amkj.dmsh.utils.ProductLabelCreateUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.JustifyContent;

import java.util.List;

import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.constant.ConstantMethod.getSpannableString;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_0;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_2;
import static com.amkj.dmsh.utils.TimeUtils.getCoutDownTime;
import static com.amkj.dmsh.utils.TimeUtils.getCurrentTime;
import static com.amkj.dmsh.utils.TimeUtils.getTimeDifference;
import static com.amkj.dmsh.utils.TimeUtils.isEndOrStartTime;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/6/7
 * class description:拼团-参与列表
 */

public class JoinGroupAdapter extends BaseMultiItemQuickAdapter<GroupShopJoinBean, BaseViewHolder> {
    private final Context context;

    public JoinGroupAdapter(Context context, List<GroupShopJoinBean> groupShopJoinList) {
        super(groupShopJoinList);
        this.context = context;
        addItemType(TYPE_0, R.layout.adapter_layout_ql_group);
        addItemType(TYPE_2, R.layout.adapter_layout_ql_group_share);
    }

    @Override
    protected void convert(BaseViewHolder helper, GroupShopJoinBean groupShopJoinBean) {
        if (groupShopJoinBean == null) return;
        switch (helper.getItemViewType()) {
            case TYPE_0:
                GlideImageLoaderUtil.loadRoundImg(context, helper.getView(R.id.cir_ql_gp_ava), groupShopJoinBean.getAvatar(), AutoSizeUtils.mm2px(context, 70));
                helper.setText(R.id.tv_ql_gp_name, getStrings(groupShopJoinBean.getNickname()))
                        .setText(R.id.tv_join_dif_per_count, getStrings(groupShopJoinBean.getGpCreateStatus()))
                        .setText(R.id.tv_ql_gp_sp_join, groupShopJoinBean.getRange() == 1 ? R.string.join_new_group : R.string.join_group);
                setCountTime(helper, groupShopJoinBean);
                helper.itemView.setTag(groupShopJoinBean);
                break;
            case TYPE_2:
                FlexboxLayout flex_communal_tag = helper.getView(R.id.flex_communal_tag);
                flex_communal_tag.setJustifyContent(JustifyContent.CENTER);
                flex_communal_tag.removeAllViews();
                flex_communal_tag.setShowDivider(FlexboxLayout.SHOW_DIVIDER_MIDDLE);
                flex_communal_tag.setDividerDrawable(context.getResources().getDrawable(R.drawable.item_divider_nine_dp_white));
                for (int i = 0; i < groupShopJoinBean.getMemberListBeans().size(); i++) {
                    flex_communal_tag.addView(ProductLabelCreateUtils
                            .createOpenGroupUserInfo(context, groupShopJoinBean.getMemberListBeans().get(i)));
                }
                String groupStatus = groupShopJoinBean.getGroupStatus();
                int start = groupStatus.indexOf("“");
                int end = groupStatus.indexOf("”");
                helper.setText(R.id.tv_group_status, groupStatus.contains("“") ? getSpannableString(groupStatus.replace("“", "").replace("”", ""), start, end - 1, 0, "#ff5e6b") : getStrings(groupStatus));
                break;
        }
    }


    private void setCountTime(final BaseViewHolder helper, GroupShopJoinBean groupShopJoinBean) {
        try {
            long timeDifference = getTimeDifference(groupShopJoinBean.getGpEndTime(), getCurrentTime(groupShopJoinBean));
            //格式化开始时间
            if (isEndOrStartTime(groupShopJoinBean.getGpEndTime(), getCurrentTime(groupShopJoinBean))) {
                helper.setEnabled(R.id.tv_ql_gp_sp_join, true);
                helper.itemView.setEnabled(true);
                CountDownTimer countDownTimer = new CountDownTimer((LifecycleOwner) context, timeDifference, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        String downTime = "倒计时 " + getCoutDownTime(millisUntilFinished, true);
                        helper.setText(R.id.tv_countdownTime, getSpannableString(downTime, 4, downTime.length(), 0, "#ff5e6b"));
                    }

                    @Override
                    public void onFinish() {
                        cancel();
                        helper.setText(R.id.tv_countdownTime, "已结束");
                    }
                };

                countDownTimer.start();
                helper.itemView.setTag(R.id.id_tag, countDownTimer);
            } else {
                helper.setEnabled(R.id.tv_ql_gp_sp_join, false);
                helper.itemView.setEnabled(false);
                helper.setText(R.id.tv_countdownTime, "已结束");
            }

        } catch (Exception e) {
            e.printStackTrace();
            helper.setText(R.id.tv_countdownTime, "");
        }
    }


    //视图被回收时，取消定时器，防止列表滚动复用导致错乱
    @Override
    public void onViewRecycled(@NonNull BaseViewHolder holder) {
        super.onViewRecycled(holder);
        Object tag = holder.itemView.getTag(R.id.id_tag);
        if (tag != null) {
            ((CountDownTimer) tag).cancel();
        }
    }
}
