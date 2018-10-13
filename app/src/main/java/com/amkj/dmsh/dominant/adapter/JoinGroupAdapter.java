package com.amkj.dmsh.dominant.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.dominant.bean.GroupShopJoinEntity.GroupShopJoinBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.iwgang.countdownview.CountdownView;
import cn.iwgang.countdownview.DynamicConfig;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/6/7
 * class description:拼团-参与列表
 */

public class JoinGroupAdapter extends BaseMultiItemQuickAdapter<GroupShopJoinBean, BaseViewHolder> {
    private final Context context;
    private final List<GroupShopJoinBean> groupShopJoinList;
    private SparseArray<Object> sparseArray = new SparseArray<>();
    private Map<Integer, GroupShopJoinBean> beanMap = new HashMap<>();
    private ConstantMethod constantMethod;

    public JoinGroupAdapter(Context context, List<GroupShopJoinBean> groupShopJoinList) {
        super(groupShopJoinList);
        this.context = context;
        this.groupShopJoinList = groupShopJoinList;
        addItemType(ConstantVariable.TYPE_0, R.layout.adapter_layout_ql_group);
        addItemType(ConstantVariable.TYPE_1, R.layout.adapter_layout_ql_group_join);
        addItemType(ConstantVariable.TYPE_2, R.layout.adapter_layout_ql_group_share);
        getConstant();
    }

    private void getConstant() {
        if (constantMethod == null) {
            constantMethod = new ConstantMethod();
        }
    }

    @Override
    public int getItemCount() {
        if (groupShopJoinList != null && groupShopJoinList.size() > 0) {
            constantMethod.createSchedule();
            for (int i = 0; i < groupShopJoinList.size(); i++) {
                GroupShopJoinBean groupShopJoinBean = groupShopJoinList.get(i);
                beanMap.put(i, groupShopJoinBean);
            }
        }
        return super.getItemCount();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (constantMethod != null) {
            constantMethod.setRefreshTimeListener(new ConstantMethod.RefreshTimeListener() {
                @Override
                public void refreshTime() {
                    if (groupShopJoinList != null && groupShopJoinList.size() > 0) {
//                刷新数据
                        refreshData();
//                刷新倒计时
                        refreshSchedule();
                    }
                }
            });
        }
    }

    private void refreshData() {
        for (Map.Entry<Integer, GroupShopJoinBean> entry : beanMap.entrySet()) {
            GroupShopJoinBean groupShopJoinBean = entry.getValue();
            groupShopJoinBean.setSecond(groupShopJoinBean.getSecond() + 1);
            beanMap.put(entry.getKey(), groupShopJoinBean);
        }
    }

    private void refreshSchedule() {
        for (int i = 0; i < sparseArray.size(); i++) {
            CountdownView countdownView = (CountdownView) sparseArray.get(sparseArray.keyAt(i));
            GroupShopJoinBean groupShopJoinBean = beanMap.get(sparseArray.keyAt(i));
            if (groupShopJoinBean.getItemType() == ConstantVariable.TYPE_1) {
                setJoinGroupCountTime(countdownView, groupShopJoinBean);
            } else {
                setGroupOpenCountTime(countdownView, groupShopJoinBean);
            }
        }
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        constantMethod.releaseHandlers();
        constantMethod.stopSchedule();
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    protected void convert(BaseViewHolder helper, GroupShopJoinBean groupShopJoinBean) {
        DynamicConfig.Builder dynamic = new DynamicConfig.Builder();
        switch (helper.getItemViewType()) {
            case ConstantVariable.TYPE_0:
                GlideImageLoaderUtil.loadHeaderImg(context, (ImageView) helper.getView(R.id.cir_ql_gp_ava), groupShopJoinBean.getAvatar());
                helper.setText(R.id.tv_ql_gp_name, getStrings(groupShopJoinBean.getNickname()))
                        .setText(R.id.tv_join_dif_per_count, getStrings("还差一人成团"))
                        .setTag(R.id.cv_countdownTime_white_hours, groupShopJoinBean)
                        .setText(R.id.tv_count_time_before_white, "距结束");
                TextView tv_ql_gp_sp_join = helper.getView(R.id.tv_ql_gp_sp_join);
                GradientDrawable drawable = new GradientDrawable();
                drawable.setCornerRadii(new float[]{0, 0, AutoSizeUtils.mm2px(mAppContext, 40), AutoSizeUtils.mm2px(mAppContext, 40)
                        , AutoSizeUtils.mm2px(mAppContext, 40), AutoSizeUtils.mm2px(mAppContext, 40), 0, 0});
                drawable.setColor(context.getResources().getColor(R.color.text_normal_red));
                tv_ql_gp_sp_join.setBackground(drawable);
                tv_ql_gp_sp_join.setText(groupShopJoinBean.getRange() == 1 ? R.string.join_new_group : R.string.join_group);
                CountdownView cv_countdownTime_white_hours = helper.getView(R.id.cv_countdownTime_white_hours);
                dynamic.setSuffixTextSize(AutoSizeUtils.mm2px(mAppContext,22));
                dynamic.setTimeTextSize(AutoSizeUtils.mm2px(mAppContext,22));
                cv_countdownTime_white_hours.dynamicShow(dynamic.build());
                setGroupOpenCountTime(cv_countdownTime_white_hours, groupShopJoinBean);
                setCountDownView(helper.getAdapterPosition() - getHeaderLayoutCount(), cv_countdownTime_white_hours);
                helper.itemView.setTag(groupShopJoinBean);
                break;
            case ConstantVariable.TYPE_1:
                helper.setText(R.id.tv_show_communal_time_status, isJoinGroupEnd(groupShopJoinBean) ? "距结束：" : "已结束")
                        .setTag(R.id.ct_time_communal_show_bg, groupShopJoinBean)
                        .setGone(R.id.ct_time_communal_show_bg, isJoinGroupEnd(groupShopJoinBean));
                CountdownView ct_time_communal_show_bg = helper.getView(R.id.ct_time_communal_show_bg);
                dynamic.setTimeTextColor(0xffffffff);
                dynamic.setSuffixTextColor(0xff333333);
                dynamic.setSuffixTextSize(AutoSizeUtils.mm2px(mAppContext,28));
                dynamic.setTimeTextSize(AutoSizeUtils.mm2px(mAppContext,28));
                DynamicConfig.BackgroundInfo backgroundInfo = new DynamicConfig.BackgroundInfo();
                backgroundInfo.setColor(context.getResources().getColor(R.color.text_normal_red));
                dynamic.setBackgroundInfo(backgroundInfo);
                ct_time_communal_show_bg.dynamicShow(dynamic.build());
                setJoinGroupCountTime(ct_time_communal_show_bg, groupShopJoinBean);
                setCountDownView(helper.getAdapterPosition() - getHeaderLayoutCount(), ct_time_communal_show_bg);
                break;
            case ConstantVariable.TYPE_2:
                RecyclerView communal_recycler_wrap = helper.getView(R.id.communal_recycler_wrap);
                helper.setGone(R.id.ll_communal_count_time,true);
                communal_recycler_wrap.setNestedScrollingEnabled(false);
                FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(context);
                flexboxLayoutManager.setJustifyContent(JustifyContent.CENTER);
                flexboxLayoutManager.setFlexWrap(FlexWrap.WRAP);
                communal_recycler_wrap.setLayoutManager(flexboxLayoutManager);
                CountdownView cv_countdownTime_red_hours = helper.getView(R.id.cv_countdownTime_red_hours);
                dynamic.setSuffixTextSize(AutoSizeUtils.mm2px(mAppContext,28));
                dynamic.setTimeTextSize(AutoSizeUtils.mm2px(mAppContext,28));
                cv_countdownTime_red_hours.dynamicShow(dynamic.build());
                GroupTagAdapter groupTagAdapter = new GroupTagAdapter(context,groupShopJoinBean.getMemberListBeans());
                communal_recycler_wrap.setAdapter(groupTagAdapter);
                TextView tv_count_time_before_hours = helper.getView(R.id.tv_count_time_before_hours);
                tv_count_time_before_hours.setTextColor(context.getResources().getColor(R.color.text_red_color));
                helper.setText(R.id.tv_count_time_before_hours, "剩余")
                        .setTag(R.id.cv_countdownTime_red_hours, groupShopJoinBean)
                        .setGone(R.id.cv_countdownTime_red_hours, isJoinGroupEnd(groupShopJoinBean))
                        .setGone(R.id.ll_communal_count_time,isJoinGroupEnd(groupShopJoinBean));
                setJoinGroupCountTime(cv_countdownTime_red_hours, groupShopJoinBean);
                setCountDownView(helper.getAdapterPosition() - getHeaderLayoutCount(), cv_countdownTime_red_hours);
                break;
        }
    }

    private void setCountDownView(int i, CountdownView countdownView) {
        sparseArray.put(i, countdownView);
    }


    public boolean isJoinGroupEnd(GroupShopJoinBean groupShopJoinBean) {
        //格式化结束时间
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date startTime;
        Date endTime;
        try {
            if (TextUtils.isEmpty(groupShopJoinBean.getCurrentTime())) {
                startTime = new Date();
            } else {
                startTime = formatter.parse(groupShopJoinBean.getCurrentTime());
            }
            endTime = formatter.parse(groupShopJoinBean.getGpEndTime());
            if (startTime != null && endTime != null && endTime.getTime() > startTime.getTime()) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 正常参团列表
     *
     * @param cv_countdownTime
     */
    private void setGroupOpenCountTime(CountdownView cv_countdownTime, GroupShopJoinBean groupShopJoinBean) {
        if (groupShopJoinBean != null) {
            if (isTimeStart(groupShopJoinBean)) {
                try {
                    //格式化结束时间
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
                    Date dateEnd = formatter.parse(groupShopJoinBean.getGpEndTime());
                    Date dateCurrent;
                    if (!TextUtils.isEmpty(groupShopJoinBean.getCurrentTime())) {
                        dateCurrent = formatter.parse(groupShopJoinBean.getCurrentTime());
                    } else {
                        dateCurrent = new Date();
                    }
                    cv_countdownTime.updateShow(dateEnd.getTime() - dateCurrent.getTime()
                            - groupShopJoinBean.getSecond() * 1000);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    //格式化开始时间
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
                    Date dateStart = formatter.parse(groupShopJoinBean.getGpCreateTime());
                    Date dateCurrent;
                    if (!TextUtils.isEmpty(groupShopJoinBean.getCurrentTime())) {
                        dateCurrent = formatter.parse(groupShopJoinBean.getCurrentTime());
                    } else {
                        dateCurrent = new Date();
                    }
                    cv_countdownTime.updateShow(dateStart.getTime() - dateCurrent.getTime()
                            - groupShopJoinBean.getSecond() * 1000);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (!ConstantMethod.isEndOrStartTimeAddSeconds(groupShopJoinBean.getCurrentTime()
                    , groupShopJoinBean.getGpEndTime()
                    , groupShopJoinBean.getSecond())) {
                cv_countdownTime.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                    @Override
                    public void onEnd(CountdownView cv) {
                        cv.setOnCountdownEndListener(null);
                        EventBus.getDefault().post(new EventMessage("refreshGroupShopPerson", "refreshGroupShopPerson"));
                    }
                });
            } else {
                cv_countdownTime.setOnCountdownEndListener(null);
            }
        }
    }

    /**
     * 好友邀请参团
     *
     * @param countdownView
     */
    public void setJoinGroupCountTime(final CountdownView countdownView, GroupShopJoinBean groupShopJoinBean) {
        //格式化结束时间
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
        Date startTime;
        Date endTime;
        try {
            if (TextUtils.isEmpty(groupShopJoinBean.getCurrentTime())) {
                startTime = new Date();
            } else {
                startTime = formatter.parse(groupShopJoinBean.getCurrentTime());
            }
            endTime = formatter.parse(groupShopJoinBean.getGpEndTime());
            if (startTime != null && endTime != null && endTime.getTime() > startTime.getTime()) {
                countdownView.updateShow(endTime.getTime() - startTime.getTime() - groupShopJoinBean.getSecond() * 1000);
            }
            if (!ConstantMethod.isEndOrStartTimeAddSeconds(groupShopJoinBean.getCurrentTime()
                    , groupShopJoinBean.getGpEndTime()
                    , groupShopJoinBean.getSecond())) {
                countdownView.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                    @Override
                    public void onEnd(CountdownView cv) {
                        cv.setOnCountdownEndListener(null);
                    }
                });
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private boolean isTimeStart(GroupShopJoinBean qualityGroupBean) {
        try {
            //格式化开始时间
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
            Date dateStart = formatter.parse(qualityGroupBean.getGpCreateTime());
            Date dateCurrent;
            if (!TextUtils.isEmpty(qualityGroupBean.getCurrentTime())) {
                dateCurrent = formatter.parse(qualityGroupBean.getCurrentTime());
            } else {
                dateCurrent = new Date();
            }
            if (dateCurrent.getTime() >= dateStart.getTime()) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}
