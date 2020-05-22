package com.amkj.dmsh.homepage.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.homepage.activity.IntegralLotteryAwardGetActivity;
import com.amkj.dmsh.homepage.activity.IntegralLotteryAwardHistoryActivity;
import com.amkj.dmsh.homepage.activity.IntegralLotteryAwardPersonActivity;
import com.amkj.dmsh.homepage.bean.IntegralLotteryAwardEntity;
import com.amkj.dmsh.homepage.bean.IntegralLotteryEntity.PreviousInfoBean;
import com.amkj.dmsh.homepage.bean.IntegralLotteryEntity.PreviousInfoBean.WinListBean;
import com.amkj.dmsh.homepage.initviews.AttendanceLotteryCodePopWindow;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.alertdialog.AlertDialogHelper;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.android.flexbox.FlexboxLayout;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.iwgang.countdownview.CountdownView;
import cn.iwgang.countdownview.DynamicConfig;
import de.hdodenhof.circleimageview.CircleImageView;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.isEmptyStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.H_ATTENDANCE_JOIN_IN_INTEGRAL_LOTTERY;
import static com.amkj.dmsh.utils.TimeUtils.isEndOrStartTimeAddSeconds;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/7/25
 * version 3.1.5
 * class description:积分夺宝
 */
public class IntegralLotteryAdapter extends BaseQuickAdapter<PreviousInfoBean, IntegralLotteryAdapter.IntegralLotteryViewHolder> implements LifecycleObserver {
    private final Activity context;
    private final List<PreviousInfoBean> integralLotteryList;
    private final LayoutInflater inflater;
    private ConstantMethod constantMethod;
    private Map<Integer, PreviousInfoBean> beanMap = new HashMap<>();
    public String messageType = "refreshData";
    private AttendanceLotteryCodePopWindow attendanceLotteryCodePopWindow;
    private AttendanceLotteryCode attendanceLotteryCode;
    private LotteryAwardAdapter lotteryAwardAdapter;
    private List<String> lotteryCodeList = new ArrayList<>();
    private AlertDialogHelper alertDialogIntegral;

    public IntegralLotteryAdapter(Activity context, ConstantMethod constantMethod, List<PreviousInfoBean> integralLotteryList) {
        super(R.layout.adapter_integral_lottery, integralLotteryList);
        this.integralLotteryList = integralLotteryList;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.constantMethod = constantMethod;
        getConstant();
        setLotteryCodePopWindows();
        setIntegralPopWindows();
        ((AppCompatActivity) context).getLifecycle().addObserver(this);
    }

    /**
     * 设置夺宝码弹窗
     */
    private void setLotteryCodePopWindows() {
        attendanceLotteryCodePopWindow = new AttendanceLotteryCodePopWindow(context);
        attendanceLotteryCodePopWindow.getPopupWindow().setOutsideTouchable(true);
        View popupWindowView = attendanceLotteryCodePopWindow.getContentView();
        attendanceLotteryCode = new AttendanceLotteryCode();
        ButterKnife.bind(attendanceLotteryCode, popupWindowView);
        attendanceLotteryCode.initViews();
    }

    /**
     * 设置积分扣除提示弹窗
     */
    private void setIntegralPopWindows() {
        if (alertDialogIntegral == null) {
            alertDialogIntegral = new AlertDialogHelper(context, R.layout.layout_alert_dialog_new);
        }
    }

    @Override
    protected void convert(IntegralLotteryViewHolder helper, PreviousInfoBean previousInfoBean) {
        TextView tv_integral_lottery_status = helper.getView(R.id.tv_integral_lottery_status);
        ImageView iv_lottery_product_cover = helper.getView(R.id.iv_lottery_product_cover);
        tv_integral_lottery_status.setText(getStrings(previousInfoBean.getActivityStatus()));
        GlideImageLoaderUtil.loadCenterCrop(context, iv_lottery_product_cover, getStrings(previousInfoBean.getImage()));
        tv_integral_lottery_status.setSelected(isTimeStarting(previousInfoBean.getStartTime(), previousInfoBean.getmCurrentTime(), previousInfoBean.getEndTime()));
        setIntegralLottery(helper, previousInfoBean);
        helper.setText(R.id.tv_integral_lottery_number, getStrings(previousInfoBean.getActivityCode()))
                .setText(R.id.tv_lottery_product_name, getStrings(previousInfoBean.getPrizeName()))
                .setText(R.id.tv_integral_lottery_total, context.getResources().getString(R.string.integral_lottery_number
                        , previousInfoBean.getPrizeNum(), previousInfoBean.getRecordNum()))
                .setText(R.id.tv_join_type, previousInfoBean.getScore() > 0 ? "积分参与" : "免费参与");

        if (!isEndOrStartTimeAddSeconds(previousInfoBean.getmCurrentTime()
                , previousInfoBean.getEndTime(), previousInfoBean.getmSeconds())) {
            helper.ct_integral_lottery_time.setTag(previousInfoBean.getPrizeName());
            helper.ct_integral_lottery_time.setVisibility(View.VISIBLE);
            previousInfoBean.setTimeObject(helper.ct_integral_lottery_time);
            setCountDownLotteryData(helper.ct_integral_lottery_time, previousInfoBean);
        } else {
            helper.ct_integral_lottery_time.setVisibility(View.GONE);
        }
        if (!isEmptyStrings(previousInfoBean.getProductId())) {
            helper.setTag(R.id.rel_integral_lottery_product, previousInfoBean)
                    .addOnClickListener(R.id.rel_integral_lottery_product);
        }
    }

    private void setIntegralLottery(IntegralLotteryViewHolder helper, PreviousInfoBean previousInfoBean) {
        if (TextUtils.isEmpty(previousInfoBean.getStartTime())
                || TextUtils.isEmpty(previousInfoBean.getEndTime())) {
            return;
        }
        String currentTime = previousInfoBean.getmCurrentTime();
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        if (TextUtils.isEmpty(previousInfoBean.getmCurrentTime())) {
            currentTime = timeFormat.format(Calendar.getInstance().getTime());
        }
        String prizeText = "";
        TextView tvIntegralPrizeStatus = helper.getView(R.id.tv_integral_lottery_time_status);
        tvIntegralPrizeStatus.setSelected(false);
        try {
            Date sTime = timeFormat.parse(previousInfoBean.getStartTime());
            Date cTime = timeFormat.parse(currentTime);
            Date eTime = timeFormat.parse(previousInfoBean.getEndTime());
            helper.rel_integral_lottery_prize.removeAllViews();
            if (eTime.getTime() < cTime.getTime()) { //已结束
                if (!previousInfoBean.isWinning()) {
                    if (previousInfoBean.getLotteryCode() == null
                            || previousInfoBean.getLotteryCode().size() < 1) {
                        prizeText = "未参与";
                    } else {
                        prizeText = "抱歉，本期您未中奖";
                    }
                    helper.rel_integral_lottery_prize.addView(helper.endLose);
                    if (previousInfoBean.getWinList() != null) {
                        helper.integralLotteryEndLoseHelper.fbl_integral_lottery_avatar.setVisibility(View.VISIBLE);
//                        清理子控件无效
                        helper.integralLotteryEndLoseHelper.fbl_integral_lottery_avatar.removeAllViews();
                        for (int i = 0; i < (previousInfoBean.getWinList().size() > 5 ? 5 : previousInfoBean.getWinList().size()); i++) {
                            WinListBean winListBean = previousInfoBean.getWinList().get(i);
                            helper.integralLotteryEndLoseHelper.fbl_integral_lottery_avatar.addView(createImageView(winListBean));
                        }
                        if (previousInfoBean.getWinList().size() > 5) {
                            helper.integralLotteryEndLoseHelper.tv_integral_lottery_prize_more.setVisibility(View.VISIBLE);
                        } else {
                            helper.integralLotteryEndLoseHelper.tv_integral_lottery_prize_more.setVisibility(View.GONE);
                        }
                    } else {
                        helper.integralLotteryEndLoseHelper.fbl_integral_lottery_avatar.setVisibility(View.GONE);
                    }
                    if (previousInfoBean.getLotteryCode() == null || previousInfoBean.getLotteryCode().size() < 1) {
                        helper.integralLotteryEndLoseHelper.tv_integral_lottery_mine_code.setVisibility(View.GONE);
                    } else {
                        helper.integralLotteryEndLoseHelper.tv_integral_lottery_mine_code.setVisibility(View.VISIBLE);
                    }
                    helper.integralLotteryEndLoseHelper.tv_integral_lottery_mine_code.setTag(previousInfoBean);
                    helper.integralLotteryEndLoseHelper.ll_integral_lottery_prize.setTag(previousInfoBean);
                } else {
                    prizeText = "恭喜您中奖了";
                    tvIntegralPrizeStatus.setSelected(true);
                    helper.rel_integral_lottery_prize.addView(helper.endPrize);
                    helper.integralLotteryEndPrizeHelper.fbl_integral_lottery_avatar.removeAllViews();
                    for (int i = 0; i < (previousInfoBean.getWinList().size() > 5 ? 5 : previousInfoBean.getWinList().size()); i++) {
                        WinListBean winListBean = previousInfoBean.getWinList().get(i);
                        helper.integralLotteryEndPrizeHelper.fbl_integral_lottery_avatar.addView(createImageView(winListBean));
                    }
                    if (previousInfoBean.getWinList().size() > 5) {
                        if (previousInfoBean.getWinList().size() > 5) {
                            helper.integralLotteryEndPrizeHelper.tv_integral_lottery_prize_more.setVisibility(View.VISIBLE);
                        } else {
                            helper.integralLotteryEndPrizeHelper.tv_integral_lottery_prize_more.setVisibility(View.GONE);
                        }
                    }
                    helper.integralLotteryEndPrizeHelper.ll_lottery_end_prize.setTag(previousInfoBean);
                }
            } else if (cTime.getTime() >= sTime.getTime()) { //进行中
                if (previousInfoBean.getLotteryCode() != null && previousInfoBean.getLotteryCode().size() > 0) {
                    helper.rel_integral_lottery_prize.addView(helper.joiningIn);
                    String lotteryCode = previousInfoBean.getLotteryCode().get(0);
                    helper.integralLotteryJoiningInHelper.tv_integral_lottery_code.setText(context.getString(R.string.integral_lottery_code, getStrings(lotteryCode)));
                    helper.integralLotteryJoiningInHelper.ll_integral_lottery_joined.setTag(previousInfoBean);
                } else {
                    helper.rel_integral_lottery_prize.addView(helper.joinIn);
                    helper.integralLotteryJoinInHelper.tv_integral_lottery_join_in.setTag(previousInfoBean);
                }

                helper.integralLotteryJoiningInHelper.tv_integral_lottery_invite_rule.setSelected(previousInfoBean.isShareNumMax());
                helper.integralLotteryJoiningInHelper.tv_integral_lottery_invite_rule.setText(
                        previousInfoBean.isShareNumMax() ? "本场活动你已达活动上限制" : "每邀请一位好友可额外获得一个抽奖码");
                prizeText = "开奖倒计时";
            } else { //未开始
                prizeText = "开始倒计时";
                helper.rel_integral_lottery_prize.addView(helper.wait);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        helper.setText(R.id.tv_integral_lottery_time_status, prizeText);
    }

    private void getConstant() {
        if (constantMethod != null) {
            constantMethod.createSchedule();
            if (constantMethod != null) {
                constantMethod.setRefreshTimeListener(new ConstantMethod.RefreshTimeListener() {
                    @Override
                    public void refreshTime() {
                        if (integralLotteryList != null && integralLotteryList.size() > 0) {
//                刷新数据
                            refreshData();
//                刷新倒计时
//                        refreshSchedule();
                        }
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        if (integralLotteryList != null && integralLotteryList.size() > 0) {
            for (int i = 0; i < integralLotteryList.size(); i++) {
                PreviousInfoBean previousInfoBean = integralLotteryList.get(i);
                beanMap.put(i, previousInfoBean);
            }
        }
        return super.getItemCount();
    }

    private void refreshData() {
        for (Map.Entry<Integer, PreviousInfoBean> entry : beanMap.entrySet()) {
            PreviousInfoBean previousInfoBean = entry.getValue();
            previousInfoBean.setmSeconds(previousInfoBean.getmSeconds() + 1);
            beanMap.put(entry.getKey(), previousInfoBean);
            if (previousInfoBean.getTimeObject() != null) {
                setCountDownLotteryData((CountdownView) previousInfoBean.getTimeObject(), previousInfoBean);
            }
        }
    }


    /**
     * 设置积分夺宝倒计时
     *
     * @param countdownView
     * @param previousInfoBean
     */
    private void setCountDownLotteryData(CountdownView countdownView, PreviousInfoBean previousInfoBean) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        if (isEndOrStartTimeAddSeconds(previousInfoBean.getmCurrentTime()
                , previousInfoBean.getEndTime()
                , previousInfoBean.getmSeconds())) {
            countdownView.setOnCountdownEndListener(null);
//            countdownView.setVisibility(View.GONE);
        } else if (isTimeStart(previousInfoBean)) {
//            countdownView.setVisibility(View.VISIBLE);
            try {
                //格式化结束时间
                Date dateEnd = formatter.parse(previousInfoBean.getEndTime());
                Date dateCurrent;
                if (!TextUtils.isEmpty(previousInfoBean.getmCurrentTime())) {
                    dateCurrent = formatter.parse(previousInfoBean.getmCurrentTime());
                } else {
                    dateCurrent = new Date();
                    previousInfoBean.setmCurrentTime(formatter.format(dateCurrent));
                    previousInfoBean.setmSeconds(previousInfoBean.getmSeconds() - 1);
                }
                countdownView.updateShow(dateEnd.getTime() - dateCurrent.getTime()
                        - previousInfoBean.getmSeconds() * 1000);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
//            countdownView.setVisibility(View.VISIBLE);
            try {
                //格式化开始时间
                Date dateStart = formatter.parse(previousInfoBean.getStartTime());
                Date dateCurrent;
                if (!TextUtils.isEmpty(previousInfoBean.getmCurrentTime())) {
                    dateCurrent = formatter.parse(previousInfoBean.getmCurrentTime());
                } else {
                    dateCurrent = new Date();
                    previousInfoBean.setmCurrentTime(formatter.format(dateCurrent));
                    previousInfoBean.setmSeconds(previousInfoBean.getmSeconds() - 1);
                }
                countdownView.updateShow(dateStart.getTime() - dateCurrent.getTime()
                        - previousInfoBean.getmSeconds() * 1000);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (!isEndOrStartTimeAddSeconds(previousInfoBean.getmCurrentTime()
                , previousInfoBean.getEndTime()
                , previousInfoBean.getmSeconds())) {
            countdownView.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                @Override
                public void onEnd(CountdownView cv) {
                    cv.setOnCountdownEndListener(null);
                    countdownView.setVisibility(View.GONE);
                    EventBus.getDefault().post(new EventMessage(messageType, "integralLotteryEnd"));
                }
            });
        }
    }

    private boolean isTimeStarting(String startTime, String currentTime, String endTime) {
        if (TextUtils.isEmpty(startTime) || TextUtils.isEmpty(endTime)) {
            return false;
        }
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        if (TextUtils.isEmpty(currentTime)) {
            currentTime = timeFormat.format(Calendar.getInstance().getTime());
        }

        try {
            Date sTime = timeFormat.parse(startTime);
            Date cTime = timeFormat.parse(currentTime);
            Date eTime = timeFormat.parse(endTime);
            if (sTime.getTime() <= cTime.getTime() && cTime.getTime() < eTime.getTime()) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isTimeStart(PreviousInfoBean previousInfoBean) {
        try {
            //格式化开始时间
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            Date dateStart = formatter.parse(previousInfoBean.getStartTime());
            Date dateCurrent;
            if (!TextUtils.isEmpty(previousInfoBean.getmCurrentTime())) {
                dateCurrent = formatter.parse(previousInfoBean.getmCurrentTime());
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

    public class IntegralLotteryViewHolder extends BaseViewHolder {
        private final CountdownView ct_integral_lottery_time;
        private final RelativeLayout rel_integral_lottery_prize;
        private IntegralLotteryEndLoseHelper integralLotteryEndLoseHelper;
        private View endLose;
        private IntegralLotteryEndPrizeHelper integralLotteryEndPrizeHelper;
        private View endPrize;
        private IntegralLotteryJoinInHelper integralLotteryJoinInHelper;
        private View joinIn;
        private IntegralLotteryJoiningInHelper integralLotteryJoiningInHelper;
        private View joiningIn;
        private View wait;

        public IntegralLotteryViewHolder(View view) {
            super(view);
            ct_integral_lottery_time = view.findViewById(R.id.ct_integral_lottery_time);
            rel_integral_lottery_prize = view.findViewById(R.id.rel_integral_lottery_prize);
//            结束 未中奖
            integralLotteryEndLoseHelper = new IntegralLotteryEndLoseHelper();
            endLose = inflater.inflate(R.layout.layout_integral_lottery_end_lose, (ViewGroup) view, false);
            ButterKnife.bind(integralLotteryEndLoseHelper, endLose);
//            结束 中奖
            integralLotteryEndPrizeHelper = new IntegralLotteryEndPrizeHelper();
            endPrize = inflater.inflate(R.layout.layout_integral_lottery_end_prize, (ViewGroup) view, false);
            ButterKnife.bind(integralLotteryEndPrizeHelper, endPrize);
//             进行中 待参与
            integralLotteryJoinInHelper = new IntegralLotteryJoinInHelper();
            joinIn = inflater.inflate(R.layout.layout_integral_lottery_join_in, (ViewGroup) view, false);
            ButterKnife.bind(integralLotteryJoinInHelper, joinIn);

//             进行中 已参与
            integralLotteryJoiningInHelper = new IntegralLotteryJoiningInHelper();
            joiningIn = inflater.inflate(R.layout.layout_integral_lottery_joining_in, (ViewGroup) view, false);
            ButterKnife.bind(integralLotteryJoiningInHelper, joiningIn);
//            待开始
            wait = inflater.inflate(R.layout.layout_integral_lottery_wait, (ViewGroup) view, false);
            if (ct_integral_lottery_time != null) {
                DynamicConfig.Builder dynamic = new DynamicConfig.Builder();
                dynamic.setSuffixTextSize(AutoSizeUtils.mm2px(mAppContext, 28));
                dynamic.setTimeTextSize(AutoSizeUtils.mm2px(mAppContext, 28));
                dynamic.setSuffixGravity(Gravity.CENTER);
                DynamicConfig.BackgroundInfo backgroundInfo = new DynamicConfig.BackgroundInfo();
                backgroundInfo.setColor(context.getResources().getColor(R.color.gray_d))
                        .setBorderRadius((float) AutoSizeUtils.mm2px(mAppContext, 8))
                        .setBorderColor(context.getResources().getColor(R.color.gray_d))
                        .setShowTimeBgBorder(true);
                dynamic.setBackgroundInfo(backgroundInfo);
                ct_integral_lottery_time.dynamicShow(dynamic.build());
            }
        }
    }


    private ImageView createImageView(WinListBean winListBean) {
        CircleImageView imageView = new CircleImageView(context);
        imageView.setBorderWidth(1);
        imageView.setBorderColor(context.getResources().getColor(R.color.text_gray_c));
        int size = AutoSizeUtils.mm2px(mAppContext, 60);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(size, size));
        GlideImageLoaderUtil.loadHeaderImg(context, imageView, getStrings(winListBean.getAvatar()));
        return imageView;
    }

    public class IntegralLotteryEndLoseHelper {
        @BindView(R.id.fbl_integral_lottery_avatar)
        FlexboxLayout fbl_integral_lottery_avatar;
        @BindView(R.id.tv_integral_lottery_prize_more)
        TextView tv_integral_lottery_prize_more;
        @BindView(R.id.tv_integral_lottery_mine_code)
        TextView tv_integral_lottery_mine_code;
        @BindView(R.id.ll_integral_lottery_prize)
        LinearLayout ll_integral_lottery_prize;

        //        我的夺宝码
        @OnClick(R.id.tv_integral_lottery_mine_code)
        void mineLotteryCode(TextView textView) {
            PreviousInfoBean previousInfoBean = (PreviousInfoBean) textView.getTag();
            setIntegralLotteryCode(previousInfoBean);
        }

        @OnClick(R.id.ll_integral_lottery_prize)
        void LotteryAwardPerson(LinearLayout linearLayout) {
            PreviousInfoBean previousInfoBean = (PreviousInfoBean) linearLayout.getTag();
            if (previousInfoBean != null && previousInfoBean.getWinList() != null && previousInfoBean.getWinList().size() > 0) {
                Intent intent = new Intent(context, IntegralLotteryAwardPersonActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("lotteryWinner", (ArrayList<? extends Parcelable>) previousInfoBean.getWinList());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        }
    }

    private void setIntegralLotteryCode(PreviousInfoBean previousInfoBean) {
        if (previousInfoBean != null && previousInfoBean.getLotteryCode() != null
                && previousInfoBean.getLotteryCode().size() > 0) {
            lotteryCodeList.clear();
            lotteryCodeList.addAll(previousInfoBean.getLotteryCode());
            lotteryAwardAdapter.notifyDataSetChanged();
            if (lotteryCodeList.size() > 1) {
                attendanceLotteryCode.tv_integral_lottery_invite.setVisibility(View.GONE);
            } else {
                attendanceLotteryCode.tv_integral_lottery_invite.setVisibility(View.VISIBLE);
            }

            attendanceLotteryCodePopWindow.showPopupWindow();
        }
    }

    public class IntegralLotteryEndPrizeHelper {
        @BindView(R.id.fbl_integral_lottery_avatar)
        FlexboxLayout fbl_integral_lottery_avatar;
        @BindView(R.id.tv_integral_lottery_prize_more)
        TextView tv_integral_lottery_prize_more;
        @BindView(R.id.ll_lottery_end_prize)
        LinearLayout ll_lottery_end_prize;

        //        去领取
        @OnClick(R.id.ll_lottery_end_prize)
        void mineLotteryCode(LinearLayout linearLayout) {
            PreviousInfoBean previousInfoBean = (PreviousInfoBean) linearLayout.getTag();
            if (previousInfoBean != null) {
                Intent intent = new Intent(context, IntegralLotteryAwardGetActivity.class);
                intent.putExtra("activityId", String.valueOf(previousInfoBean.getId()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }
    }

    public class IntegralLotteryJoinInHelper {
        @BindView(R.id.tv_integral_lottery_join_in)
        TextView tv_integral_lottery_join_in;

        //        去参与
        @OnClick(R.id.tv_integral_lottery_join_in)
        void lotteryJoinIn(TextView textView) {
            PreviousInfoBean previousInfoBean = (PreviousInfoBean) textView.getTag();
            if (previousInfoBean != null) {
                int score = previousInfoBean.getScore();
                //积分参与
                if (score > 0) {
                    alertDialogIntegral.setMsg(ConstantMethod.getStringsFormat(context, R.string.join_activity_need_integral, String.valueOf(score)))
                            .setTitleVisibility(View.GONE)
                            .setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                                @Override
                                public void confirm() {
                                    joinInIntegralLottery(previousInfoBean.getId(), textView);
                                }

                                @Override
                                public void cancel() {
                                }
                            });
                    alertDialogIntegral.show();
                } else {
                    //免费参与
                    joinInIntegralLottery(previousInfoBean.getId(), textView);
                }
            }
        }
    }


    public class IntegralLotteryJoiningInHelper {
        @BindView(R.id.tv_integral_lottery_code)
        TextView tv_integral_lottery_code;
        @BindView(R.id.ll_integral_lottery_joined)
        LinearLayout ll_integral_lottery_joined;
        @BindView(R.id.tv_integral_lottery_invite_rule)
        TextView tv_integral_lottery_invite_rule;

        @OnClick(R.id.ll_integral_lottery_joined)
        void mineLotteryCode(LinearLayout linearLayout) {
            PreviousInfoBean previousInfoBean = (PreviousInfoBean) linearLayout.getTag();
            setIntegralLotteryCode(previousInfoBean);
        }
    }

    public class AttendanceLotteryCode {
        @BindView(R.id.rv_integral_lottery_code)
        RecyclerView rv_integral_lottery_code;
        @BindView(R.id.tv_integral_lottery_invite)
        TextView tv_integral_lottery_invite;

        public void initViews() {
            rv_integral_lottery_code.setLayoutManager(new LinearLayoutManager(context));
            lotteryAwardAdapter = new LotteryAwardAdapter(lotteryCodeList);
            rv_integral_lottery_code.setAdapter(lotteryAwardAdapter);
        }

        @OnClick(R.id.tv_integral_lottery_history_award)
        void historyLotteryAward() {
            Intent intent = new Intent(context, IntegralLotteryAwardHistoryActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            attendanceLotteryCodePopWindow.dismiss();
        }

        /**
         * 积分夺宝-邀请好友
         *
         * @param textView
         */
        @OnClick(R.id.tv_integral_lottery_invite)
        void integralLotteryAwardInvite(TextView textView) {
            EventBus.getDefault().post(new EventMessage("invitePartner", ""));
            attendanceLotteryCodePopWindow.dismiss();
        }
    }


    private void joinInIntegralLottery(int activityId, TextView textView) {
        if (userId > 0) {
            String url = H_ATTENDANCE_JOIN_IN_INTEGRAL_LOTTERY;
            Map<String, Object> params = new HashMap<>();
            params.put("uid", userId);
            params.put("activityId", activityId);
            NetLoadUtils.getNetInstance().loadNetDataPost(context, url, params, new NetLoadListenerHelper() {
                @Override
                public void onSuccess(String result) {
                    textView.setEnabled(true);

                    IntegralLotteryAwardEntity integralLotteryAwardEntity = GsonUtils.fromJson(result, IntegralLotteryAwardEntity.class);
                    if (integralLotteryAwardEntity != null) {
                        if (SUCCESS_CODE.equals(integralLotteryAwardEntity.getCode())) {
                            showToast("夺宝参与成功");
                            EventBus.getDefault().post(new EventMessage(messageType, "joinInIntegralLottery"));
                        } else {
                            showToast(integralLotteryAwardEntity.getMsg());
                        }
                    }
                }

                @Override
                public void onNotNetOrException() {
                    textView.setEnabled(true);
                }

                @Override
                public void onError(Throwable throwable) {
                    showToast( R.string.do_failed);
                }
            });
        } else {
            getLoginStatus(context);
            textView.setEnabled(true);
        }
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private void onDestroy() {
        attendanceLotteryCodePopWindow.dismiss();
        ((AppCompatActivity) context).getLifecycle().removeObserver(this);
    }
}
