package com.amkj.dmsh.homepage.activity;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.bean.IntegrationProEntity;
import com.amkj.dmsh.bean.IntegrationProEntity.IntegrationBean;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dominant.activity.QualityCustomTopicActivity;
import com.amkj.dmsh.homepage.AttendanceMarqueeView;
import com.amkj.dmsh.homepage.adapter.AttendanceAwardAdapter;
import com.amkj.dmsh.homepage.adapter.HomeImgActivityAdapter;
import com.amkj.dmsh.homepage.adapter.IntegralLotteryAdapter;
import com.amkj.dmsh.homepage.adapter.IntegrationRecyclerAdapter;
import com.amkj.dmsh.homepage.adapter.QualityCustomTopicAdapter;
import com.amkj.dmsh.homepage.bean.AttendanceDetailEntity;
import com.amkj.dmsh.homepage.bean.AttendanceDetailEntity.AttendanceDetailBean;
import com.amkj.dmsh.homepage.bean.AttendanceDetailEntity.LogListBean;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity.CommunalADActivityBean;
import com.amkj.dmsh.homepage.bean.CommunalRuleEntity;
import com.amkj.dmsh.homepage.bean.IntegralLotteryEntity;
import com.amkj.dmsh.homepage.bean.IntegralLotteryEntity.PreviousInfoBean;
import com.amkj.dmsh.mine.activity.MineLoginActivity;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean;
import com.amkj.dmsh.shopdetails.integration.IntegralScrollDetailsActivity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean;
import com.amkj.dmsh.utils.alertdialog.AlertDialogHelper;
import com.amkj.dmsh.utils.alertdialog.AlertRuleDialogHelper;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.amkj.dmsh.utils.webformatdata.CommunalWebDetailUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gongwen.marqueen.MarqueeFactory;
import com.gongwen.marqueen.MarqueeView;
import com.google.gson.Gson;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getDataFormatWeek;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.isEmptyStrings;
import static com.amkj.dmsh.constant.ConstantMethod.isEndOrStartTime;
import static com.amkj.dmsh.constant.ConstantMethod.setSkipPath;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.showToastRequestMsg;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.DOUBLE_INTEGRAL_PREFECTURE;
import static com.amkj.dmsh.constant.ConstantVariable.DOUBLE_INTEGRAL_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.REGEX_NUM;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TEN;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_1;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_2;
import static com.amkj.dmsh.constant.Url.H_ATTENDANCE;
import static com.amkj.dmsh.constant.Url.H_ATTENDANCE_AWARD;
import static com.amkj.dmsh.constant.Url.H_ATTENDANCE_DETAIL;
import static com.amkj.dmsh.constant.Url.H_ATTENDANCE_INTEGRAL_LOTTERY;
import static com.amkj.dmsh.constant.Url.H_ATTENDANCE_INTEGRAL_LOTTERY_RULE;
import static com.amkj.dmsh.constant.Url.H_ATTENDANCE_MORE_ACTIVITY;
import static com.amkj.dmsh.constant.Url.H_ATTENDANCE_RULE;
import static com.amkj.dmsh.constant.Url.H_ATTENDANCE_WARM;
import static com.amkj.dmsh.constant.Url.H_INTEGRAL_PRODUCT_FILTRATE;
import static com.amkj.dmsh.constant.Url.Q_CUSTOM_PRO_LIST;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/7/19
 * version 3.1.5
 * class description:首页-签到
 */
public class AttendanceActivity extends BaseActivity {
    @BindView(R.id.tv_header_shared)
    TextView header_shared;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tl_normal_bar)
    Toolbar tl_normal_bar;
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    private IntegrationRecyclerAdapter integrationRecyclerAdapter;
    //    积分商品
    private List<IntegrationBean> integrationBeanList = new ArrayList();
    private AttendanceHeader attendanceHeader;
    private MarqueeFactory<LinearLayout, LogListBean> attendanceMarqueeView;
    private List<LogListBean> logListBeans = new ArrayList<>();
    private List<AttendanceDetailBean> attendanceDetailBeanList = new ArrayList<>();
    private AttendanceAwardAdapter attendanceAwardAdapter;
    private List<PreviousInfoBean> integralLotteryList = new ArrayList();
    private IntegralLotteryAdapter integralLotteryAdapter;
    private AttendanceIntegralLottery attendanceIntegralLottery;
    private View integralLotteryView;
    //    签到-积分规则
    private List<CommunalDetailObjectBean> integralRuleList = new ArrayList();
    //    签到-夺宝规则
    private List<CommunalDetailObjectBean> lotteryRuleList = new ArrayList();
    private HotActivityHelper hotActivityHelper;
    private List<CommunalADActivityBean> adActivityBeans = new ArrayList<>();
    private HomeImgActivityAdapter homeImgActivityAdapter;
    private IntegralDoubleHelper integralDoubleHelper;
    private List<LikedProductBean> doubleIntegrationList = new ArrayList();
    private QualityCustomTopicAdapter qualityCustomTopicAdapter;
    private View hotActivityView;
    private AlertRuleDialogHelper alertLotteryRuleDialogHelper;
    private AlertRuleDialogHelper alertIntegralRuleDialogHelper;
    private AlertDialogHelper alertDialogHelper;
    private AttendanceDetailEntity attendanceDetailEntity;
    private ConstantMethod constantMethod;

    @Override
    protected int getContentView() {
        return R.layout.activity_attendance;
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
        tv_header_titleAll.setText("签到");
        header_shared.setCompoundDrawables(null, null, null, null);
        header_shared.setText("邀请好友");
        communal_recycler.setLayoutManager(new GridLayoutManager(AttendanceActivity.this, 3));
        integrationRecyclerAdapter = new IntegrationRecyclerAdapter(this, integrationBeanList);
        View attendanceView = LayoutInflater.from(AttendanceActivity.this).inflate(R.layout.layotu_attendance_info, null);
        attendanceHeader = new AttendanceHeader();
        ButterKnife.bind(attendanceHeader, attendanceView);
        attendanceHeader.initViews();
        integralLotteryView = LayoutInflater.from(AttendanceActivity.this).inflate(R.layout.layotu_attendance_integral_lottery, null);
        attendanceIntegralLottery = new AttendanceIntegralLottery();
        ButterKnife.bind(attendanceIntegralLottery, integralLotteryView);
        attendanceIntegralLottery.initViews();
        integrationRecyclerAdapter.addHeaderView(attendanceView);
        integrationRecyclerAdapter.addHeaderView(integralLotteryView);
        integrationRecyclerAdapter.setHeaderAndEmpty(true);
        hotActivityView = LayoutInflater.from(AttendanceActivity.this).inflate(R.layout.layotu_attendance_activity_prefecture, communal_recycler, false);
        hotActivityHelper = new HotActivityHelper();
        ButterKnife.bind(hotActivityHelper, hotActivityView);
        hotActivityHelper.initViews();
        View integralDoubleView = LayoutInflater.from(AttendanceActivity.this).inflate(R.layout.layotu_attendance_integral_double, communal_recycler, false);
        integralDoubleHelper = new IntegralDoubleHelper();
        ButterKnife.bind(integralDoubleHelper, integralDoubleView);
        integralDoubleHelper.initViews();
        integrationRecyclerAdapter.addFooterView(hotActivityView);
        integrationRecyclerAdapter.addFooterView(integralDoubleView);
        communal_recycler.setLayoutManager(new GridLayoutManager(AttendanceActivity.this, 3));
        communal_recycler.setAdapter(integrationRecyclerAdapter);
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_gray_f).create());
        integrationRecyclerAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                IntegrationBean integrationBean = (IntegrationBean) view.getTag();
                if (integrationBean != null) {
                    Intent intent = new Intent(AttendanceActivity.this, IntegralScrollDetailsActivity.class);
                    intent.putExtra("productId", String.valueOf(integrationBean.getId()));
                    startActivity(intent);
                }
            }
        });
        integrationRecyclerAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.tv_integral_pro_type:
                        Intent intent = new Intent(AttendanceActivity.this, IntegralProductShopActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
        integrationRecyclerAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                return integrationBeanList.get(position).getItemType() == TYPE_1 ? 3 : 1;
            }
        });
        smart_communal_refresh.setOnRefreshListener(refreshLayout -> loadData());
    }

    @Override
    protected void loadData() {
        if (userId > 0) {
//        积分奖励
            getAttendanceDetail();
//        积分夺宝
            getIntegralLottery();
        } else {
            NetLoadUtils.getNetInstance().showLoadSirSuccess(loadService);
        }
    }

    /**
     * 后加载数据
     */
    private void loadWaitData() {
        //        积分商品
        getIntegralPro();
//            签到-积分规则
        getIntegralRule();
//            签到-夺宝规则
        getLotteryRule();
//            更多专区
        getRegionActivity();
//            双倍积分
        getDoubleIntegration();
    }


    @Override
    protected View getLoadView() {
        return smart_communal_refresh;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    /**
     * 夺宝规则
     */
    private void getLotteryRule() {
        NetLoadUtils.getNetInstance().loadNetDataPost(this,H_ATTENDANCE_INTEGRAL_LOTTERY_RULE,new NetLoadListenerHelper(){
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                CommunalRuleEntity communalRuleEntity = gson.fromJson(result, CommunalRuleEntity.class);
                if (communalRuleEntity != null) {
                    if (communalRuleEntity.getCode().equals(SUCCESS_CODE)) {
                        if (communalRuleEntity.getCommunalRuleList() != null
                                && communalRuleEntity.getCommunalRuleList().size() > 0) {
                            lotteryRuleList.clear();
                            lotteryRuleList.addAll(CommunalWebDetailUtils.getCommunalWebInstance().getWebDetailsFormatDataList(communalRuleEntity.getCommunalRuleList()));
                            if (alertLotteryRuleDialogHelper == null) {
                                alertLotteryRuleDialogHelper = new AlertRuleDialogHelper(AttendanceActivity.this);
                            }
                            alertLotteryRuleDialogHelper.setRuleData("夺宝规则", lotteryRuleList);
                        }
                    }
                }
            }
        });
    }

    /**
     * 积分规则
     */
    private void getIntegralRule() {
        NetLoadUtils.getNetInstance().loadNetDataPost(this,H_ATTENDANCE_RULE,new NetLoadListenerHelper(){
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                CommunalRuleEntity communalRuleEntity = gson.fromJson(result, CommunalRuleEntity.class);
                if (communalRuleEntity != null) {
                    if (communalRuleEntity.getCode().equals(SUCCESS_CODE)) {
                        if (communalRuleEntity.getCommunalRuleList() != null
                                && communalRuleEntity.getCommunalRuleList().size() > 0) {
                            integralRuleList.clear();
                            integralRuleList.addAll(CommunalWebDetailUtils.getCommunalWebInstance().getWebDetailsFormatDataList(communalRuleEntity.getCommunalRuleList()));
                            if (alertIntegralRuleDialogHelper == null) {
                                alertIntegralRuleDialogHelper = new AlertRuleDialogHelper(AttendanceActivity.this);
                            }
                            alertIntegralRuleDialogHelper.setRuleData("积分规则", integralRuleList);
                        }
                    }
                }
            }
        });
    }

    private void getIntegralPro() {
        Map<String, Object> params = new HashMap<>();
        params.put("showCount", 9);
        params.put("currentPage", 1);
//            积分类型.-1为全部,0为纯积分,1为积分+金钱
        params.put("integralType", -1);
        NetLoadUtils.getNetInstance().loadNetDataPost(this,H_INTEGRAL_PRODUCT_FILTRATE,params,new NetLoadListenerHelper(){
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                integrationBeanList.clear();
                IntegrationProEntity integrationProEntity = gson.fromJson(result, IntegrationProEntity.class);
                if (integrationProEntity != null) {
                    if (integrationProEntity.getCode().equals(SUCCESS_CODE)) {
                        IntegrationBean integrationBean = new IntegrationBean();
                        integrationBean.setcItemType(TYPE_1);
                        integrationBeanList.add(integrationBean);
                        integrationBeanList.addAll(integrationProEntity.getIntegrationList().size() > 9
                                ? integrationProEntity.getIntegrationList().subList(0, 9) : integrationProEntity.getIntegrationList());
                    } else if (!integrationProEntity.getCode().equals(EMPTY_CODE)) {
                        showToast(AttendanceActivity.this, integrationProEntity.getMsg());
                    }
                }
                integrationRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNotNetOrException() {
                integrationRecyclerAdapter.loadMoreEnd(true);
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(AttendanceActivity.this, R.string.invalidData);
            }

            @Override
            public void netClose() {
                showToast(AttendanceActivity.this, R.string.unConnectedNetwork);
            }
        });
    }

    /**
     * 签到详情
     */
    private void getAttendanceDetail() {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(AttendanceActivity.this, H_ATTENDANCE_DETAIL
                , params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();
                        Gson gson = new Gson();
                        attendanceDetailEntity = gson.fromJson(result, AttendanceDetailEntity.class);
                        if (attendanceDetailEntity != null
                                && SUCCESS_CODE.equals(attendanceDetailEntity.getCode())) {
                            setAttendanceDetail(attendanceDetailEntity);
                            loadWaitData();
                        }
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, attendanceDetailEntity);
                    }

                    @Override
                    public void onNotNetOrException() {
                        smart_communal_refresh.finishRefresh();
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, attendanceDetailEntity);
                    }

                    @Override
                    public void netClose() {
                        showToast(AttendanceActivity.this, R.string.unConnectedNetwork);
                    }
                });
    }

    private void setAttendanceDetail(AttendanceDetailEntity attendanceDetailEntity) {
        attendanceHeader.tv_attendance_warm.setSelected(attendanceDetailEntity.isRemind());
        attendanceHeader.tv_attendance_sign_in.setEnabled(!attendanceDetailEntity.isSign());
        attendanceHeader.tv_attendance_sign_in.setText(attendanceDetailEntity.isSign() ? "已签到" : "签到");
        attendanceHeader.tv_attendance_integral_count.setText(String.valueOf(attendanceDetailEntity.getScore()));
        logListBeans.clear();
        if (attendanceDetailEntity.getLogList() != null && attendanceDetailEntity.getLogList().size() > 0) {
            attendanceHeader.marquee_attendance_text.setVisibility(View.VISIBLE);
            logListBeans.addAll(attendanceDetailEntity.getLogList());
            setMarqueeDataStart();
        } else {
            attendanceHeader.marquee_attendance_text.setVisibility(View.INVISIBLE);
        }
//        七天数据配置
        attendanceDetailBeanList.clear();
        int currentWeek = getDataFormatWeek(attendanceDetailEntity.getSysTime());
        for (int i = 1; i < 8; i++) {
            AttendanceDetailBean attendanceDetailBean = new AttendanceDetailBean();
            String week = "";
            switch (i) {
                case 1:
                    week = "周一";
                    break;
                case 2:
                    week = "周二";
                    break;
                case 3:
                    week = "周三";
                    break;
                case 4:
                    week = "周四";
                    break;
                case 5:
                    week = "周五";
                    break;
                case 6:
                    week = "周六";
                    break;
                case 7:
                    week = "周日";
                    break;
            }
            if (currentWeek > i) {
                attendanceDetailBean.setWeekCode(0);
            } else {
                attendanceDetailBean.setWeekCode(2);
            }
            if (attendanceDetailEntity.getAttendanceDetailList() != null
                    && attendanceDetailEntity.getAttendanceDetailList().size() > 0) {
//            代表七天数据
                for (AttendanceDetailBean detailBean : attendanceDetailEntity.getAttendanceDetailList()) {
                    if (getDataFormatWeek(getStrings(detailBean.getCtime())) == i) {
                        attendanceDetailBean.setWeekCode(1);
                    }
                }
            }
            attendanceDetailBean.setToWeek(week);
            attendanceDetailBeanList.add(attendanceDetailBean);
        }
        attendanceAwardAdapter.notifyDataSetChanged();
    }

    private void setMarqueeDataStart() {
        attendanceHeader.marquee_attendance_text.stopFlipping();
        attendanceMarqueeView.setData(logListBeans);
        if (logListBeans.size() > 1) {
            attendanceHeader.marquee_attendance_text.startFlipping();
        }
    }

    /**
     * 积分夺宝
     */
    private void getIntegralLottery() {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this,H_ATTENDANCE_INTEGRAL_LOTTERY,params,new NetLoadListenerHelper(){
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                IntegralLotteryEntity integralLotteryEntity = gson.fromJson(result, IntegralLotteryEntity.class);
                if (integralLotteryEntity != null) {
                    if (SUCCESS_CODE.equals(integralLotteryEntity.getCode())) {
                        integralLotteryList.clear();
                        integralLotteryList.addAll(integralLotteryEntity.getPreviousInfoList());
                        int scrollPosition = 0;
                        int lotteryStatus = 0;//0 已结束 1 待开始 2 进行中
                        for (int i = 0; i < integralLotteryList.size(); i++) {
                            PreviousInfoBean previousInfoBean = integralLotteryList.get(i);
                            previousInfoBean.setmCurrentTime(integralLotteryEntity.getSystemTime());
                            if (integralLotteryList.size() >= 2) {
                                if (lotteryStatus < 2 && isEndOrStartTime(previousInfoBean.getmCurrentTime(), previousInfoBean.getStartTime())
                                        && isEndOrStartTime(previousInfoBean.getEndTime(), previousInfoBean.getmCurrentTime())) {
                                    scrollPosition = i;
                                    lotteryStatus = 2;
                                } else if (lotteryStatus < 1 && isEndOrStartTime(previousInfoBean.getmCurrentTime(), previousInfoBean.getStartTime())) {
                                    scrollPosition = i;
                                    lotteryStatus = 1;
                                }
                            }
                        }
                        attendanceIntegralLottery.rvp_integral_lottery.scrollToPosition(scrollPosition < 1 ? (lotteryStatus > 0 ? scrollPosition : integralLotteryList.size() - 1) : scrollPosition);
                    }
                    if (integralLotteryList.size() < 1) {
                        integrationRecyclerAdapter.removeHeaderView(integralLotteryView);
                    }
                    integralLotteryAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNotNetOrException() {
                if (integralLotteryList.size() < 1) {
                    integrationRecyclerAdapter.removeHeaderView(integralLotteryView);
                }
            }
        });
    }

    //签到
    private void getAttendance() {
        if (loadHud != null) {
            loadHud.show();
        }
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this,H_ATTENDANCE,params,new NetLoadListenerHelper(){
            @Override
            public void onSuccess(String result) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                Gson gson = new Gson();
                AttendanceDetailEntity attendanceDetailEntity = gson.fromJson(result, AttendanceDetailEntity.class);
                if (attendanceDetailEntity != null) {
                    if (SUCCESS_CODE.equals(attendanceDetailEntity.getCode())) {
                        attendanceHeader.tv_attendance_sign_in.setEnabled(false);
                        attendanceHeader.tv_attendance_sign_in.setText(attendanceDetailEntity.isSign() ? "已签到" : "签到");
                        attendanceHeader.tv_attendance_integral_count.setText(String.valueOf(attendanceDetailEntity.getScore()));
                        if (attendanceDetailEntity.getAttendanceDetailList() != null
                                && attendanceDetailEntity.getAttendanceDetailList().size() > 0) {
                            for (int i = 0; i < attendanceDetailEntity.getAttendanceDetailList().size(); i++) {
                                AttendanceDetailBean attendanceDetailBean = attendanceDetailEntity.getAttendanceDetailList().get(i);
                                int dataFormatWeek = getDataFormatWeek(attendanceDetailBean.getCtime());
                                if (dataFormatWeek <= attendanceDetailBeanList.size()) {
                                    attendanceDetailBeanList.get(dataFormatWeek - 1).setWeekCode(1);
                                }
                            }
                        }
                        attendanceAwardAdapter.notifyDataSetChanged();
                        if (alertDialogHelper == null) {
                            alertDialogHelper = new AlertDialogHelper(AttendanceActivity.this)
                                    .setSingleButton(true)
                                    .setConfirmText("确认")
                                    .setConfirmTextColor(getResources().getColor(R.color.text_login_gray_s))
                                    .setTitle("签到成功")
                                    .setMsgTextGravity(Gravity.CENTER)
                                    .setTitleGravity(Gravity.CENTER)
                                    .setConfirmTextColor(getResources().getColor(R.color.text_gray_hint_n))
                                    .setCancelable(false);
                        }
                        TextView msgTextView = alertDialogHelper.getMsgTextView();
                        msgTextView.setText(getStrings(attendanceDetailEntity.getSignExplain()));
                        Link link = new Link(Pattern.compile(REGEX_NUM));
                        link.setTextColor(getResources().getColor(R.color.text_normal_red))
                                .setUnderlined(false)
                                .setHighlightAlpha(0f)
                                .setOnClickListener(null);
                        LinkBuilder.on(msgTextView)
                                .addLink(link)
                                .build();
                        alertDialogHelper.show();
                    } else {
                        showToast(AttendanceActivity.this, attendanceDetailEntity.getMsg());
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
            }

            @Override
            public void netClose() {
                showToast(AttendanceActivity.this, R.string.unConnectedNetwork);
            }
        });
    }

    /**
     * 更多积分
     */
    private void getRegionActivity() {
        NetLoadUtils.getNetInstance().loadNetDataPost(this,H_ATTENDANCE_MORE_ACTIVITY,new NetLoadListenerHelper(){
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                adActivityBeans.clear();
                CommunalADActivityEntity activityEntity = gson.fromJson(result, CommunalADActivityEntity.class);
                if (activityEntity != null) {
                    if (activityEntity.getCode().equals(SUCCESS_CODE)) {
                        for (int i = 0; i < activityEntity.getCommunalADActivityBeanList().size() / 2 * 2; i++) {
                            adActivityBeans.add(activityEntity.getCommunalADActivityBeanList().get(i));
                        }
                    }
                    if (adActivityBeans.size() > 0) {
                        if (hotActivityView.getParent() == null) {
                            integrationRecyclerAdapter.addFooterView(hotActivityView, 0);
                        }
                        hotActivityHelper.rv_activity_prefecture.setVisibility(View.VISIBLE);
                        homeImgActivityAdapter.notifyDataSetChanged();
                    } else {
                        integrationRecyclerAdapter.removeFooterView(hotActivityView);
                        hotActivityHelper.rv_activity_prefecture.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                integrationRecyclerAdapter.removeFooterView(hotActivityView);
                hotActivityHelper.rv_activity_prefecture.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 双倍积分
     */
    private void getDoubleIntegration() {
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", 1);
        params.put("productType", DOUBLE_INTEGRAL_PREFECTURE);
        params.put("showCount", TOTAL_COUNT_TEN);
        if (userId > 0) {
            params.put("uid", userId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(this,Q_CUSTOM_PRO_LIST,params,new NetLoadListenerHelper(){
            @Override
            public void onSuccess(String result) {
                doubleIntegrationList.clear();
                integralDoubleHelper.rv_integral_double.setVisibility(View.VISIBLE);
                Gson gson = new Gson();
                UserLikedProductEntity userLikedProductEntity = gson.fromJson(result, UserLikedProductEntity.class);
                if (userLikedProductEntity != null) {
                    if (userLikedProductEntity.getCode().equals(SUCCESS_CODE)) {
                        LikedProductBean likedProductBean = new LikedProductBean();
                        likedProductBean.setItemType(TYPE_2);
                        doubleIntegrationList.add(likedProductBean);
                        for (LikedProductBean likedProduct : userLikedProductEntity.getLikedProductBeanList()) {
                            likedProduct.setItemType(TYPE_1);
                            doubleIntegrationList.add(likedProduct);
                        }
                    }
                    qualityCustomTopicAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNotNetOrException() {
                integralDoubleHelper.rv_integral_double.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 签到头部
     */
    public class AttendanceHeader {
        @BindView(R.id.marquee_attendance_text)
        MarqueeView<LinearLayout, LogListBean> marquee_attendance_text;
        @BindView(R.id.tv_attendance_warm)
        TextView tv_attendance_warm;
        @BindView(R.id.communal_recycler_wrap)
        RecyclerView communal_recycler_wrap;
        @BindView(R.id.tv_attendance_sign_in)
        TextView tv_attendance_sign_in;
        @BindView(R.id.tv_attendance_integral_count)
        TextView tv_attendance_integral_count;

        /**
         * 签到提醒
         *
         * @param textView
         */
        @OnClick(R.id.tv_attendance_warm)
        void attendanceWarm(TextView textView) {
            if (loadHud != null) {
                loadHud.show();
            }
            textView.setEnabled(false);
            Map<String, Object> params = new HashMap<>();
            params.put("uid", userId);
            NetLoadUtils.getNetInstance().loadNetDataPost(AttendanceActivity.this,H_ATTENDANCE_WARM,
                    params,new NetLoadListenerHelper(){
                @Override
                public void onSuccess(String result) {
                    if (loadHud != null) {
                        loadHud.dismiss();
                    }
                    Gson gson = new Gson();
                    RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                    if (requestStatus != null) {
                        if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                            textView.setSelected(requestStatus.isSign());
                            if (requestStatus.isSign()) {
                                showToast(AttendanceActivity.this, "设置提醒成功");
                            } else {
                                showToast(AttendanceActivity.this, "已关闭签到提醒");
                            }
                        } else {
                            showToastRequestMsg(AttendanceActivity.this, requestStatus);
                        }
                        textView.setEnabled(true);
                    }
                }

                @Override
                public void onNotNetOrException() {
                    if (loadHud != null) {
                        loadHud.dismiss();
                    }
                    textView.setEnabled(true);
                }
            });
        }

        /**
         * 积分规则
         */
        @OnClick(R.id.tv_attendance_integral_count)
        void quesIntegral() {
            if (alertIntegralRuleDialogHelper != null) {
                alertIntegralRuleDialogHelper.show();
            }
        }

        /**
         * 签到
         */
        @OnClick(R.id.tv_attendance_sign_in)
        void attendanceSignIn() {
            if (userId > 0) {
                getAttendance();
            } else {
                //未登录跳转登录页
                Intent intent = new Intent(AttendanceActivity.this, MineLoginActivity.class);
                startActivityForResult(intent, IS_LOGIN_CODE);
            }
        }

        /**
         * 积分获取
         */
        @OnClick(R.id.ll_sign_get)
        void integralGet() {
            Intent intent = new Intent(AttendanceActivity.this, IntegralGetActivity.class);
            startActivity(intent);
        }

        /**
         * 积分明细
         */
        @OnClick(R.id.ll_sign_detail)
        void integralDetail() {
            Intent intent = new Intent(AttendanceActivity.this, IntegralDetailActivity.class);
            startActivity(intent);
        }

        public void initViews() {
            communal_recycler_wrap.setLayoutManager(new GridLayoutManager(AttendanceActivity.this, 7));
            attendanceAwardAdapter = new AttendanceAwardAdapter(attendanceDetailBeanList);
            communal_recycler_wrap.setAdapter(attendanceAwardAdapter);
            attendanceAwardAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    AttendanceDetailBean attendanceDetailBean = (AttendanceDetailBean) view.getTag();
                    if (attendanceDetailBean != null && "周日".equals(attendanceDetailBean.getToWeek())) {
                        getAttendanceAward();
                    }
                }
            });
            attendanceMarqueeView = new AttendanceMarqueeView(AttendanceActivity.this);
            marquee_attendance_text.setMarqueeFactory(attendanceMarqueeView);
            marquee_attendance_text.setInAndOutAnim(R.anim.in_bottom, R.anim.out_top);
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setShape(GradientDrawable.RECTANGLE);
            int radius = AutoSizeUtils.mm2px(mAppContext, 30);
            gradientDrawable.setCornerRadius(radius);
            try {
                gradientDrawable.setColor(getResources().getColor(R.color.light_gray_f));
            } catch (Exception e) {
                e.printStackTrace();
            }
            marquee_attendance_text.setBackground(gradientDrawable);
        }

        /**
         * 领取连续一周签到
         * 领取周日礼物
         */
        private void getAttendanceAward() {
            if (loadHud != null) {
                loadHud.show();
            }
            Map<String, Object> params = new HashMap<>();
            params.put("uid", userId);
            NetLoadUtils.getNetInstance().loadNetDataPost(AttendanceActivity.this,H_ATTENDANCE_AWARD,params,new NetLoadListenerHelper(){
                @Override
                public void onSuccess(String result) {
                    if (loadHud != null) {
                        loadHud.dismiss();
                    }
                    Gson gson = new Gson();
                    RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                    if (requestStatus != null) {
                        showToastRequestMsg(AttendanceActivity.this, requestStatus);
                    }
                }

                @Override
                public void onNotNetOrException() {
                    if (loadHud != null) {
                        loadHud.dismiss();
                    }
                }
            });
        }
    }

    public class AttendanceIntegralLottery {
        //        积分夺宝
        @BindView(R.id.rvp_integral_lottery)
        RecyclerViewPager rvp_integral_lottery;
        @BindView(R.id.ll_integral_lottery)
        LinearLayout ll_integral_lottery;

        public void initViews() {
            rvp_integral_lottery.setLayoutManager(new LinearLayoutManager(AttendanceActivity.this, LinearLayoutManager.HORIZONTAL, false));
            constantMethod = new ConstantMethod();
            integralLotteryAdapter = new IntegralLotteryAdapter(AttendanceActivity.this,constantMethod, integralLotteryList);
            rvp_integral_lottery.setAdapter(integralLotteryAdapter);
            integralLotteryAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    if(view.getId() == R.id.rel_integral_lottery_product){
                        PreviousInfoBean previousInfoBean = (PreviousInfoBean) view.getTag();
                        if(previousInfoBean!=null&& !isEmptyStrings(previousInfoBean.getProductId())){
                            Intent intent = new Intent(AttendanceActivity.this,ShopScrollDetailsActivity.class);
                            intent.putExtra("productId",previousInfoBean.getProductId());
                            startActivity(intent);
                        }
                    }
                }
            });
            rvp_integral_lottery.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    if (rvp_integral_lottery.getChildCount() < 3) {
                        if (rvp_integral_lottery.getChildAt(1) != null) {
                            if (rvp_integral_lottery.getCurrentPosition() == 0) {
                                View v1 = rvp_integral_lottery.getChildAt(1);
                                v1.setScaleY(1f);
                                v1.setScaleX(0.957f);
                            } else {
                                View v1 = rvp_integral_lottery.getChildAt(0);
                                v1.setScaleY(1f);
                                v1.setScaleX(0.957f);
                            }
                        }
                    } else {
                        if (rvp_integral_lottery.getChildAt(0) != null) {
                            View v0 = rvp_integral_lottery.getChildAt(0);
                            v0.setScaleY(1f);
                            v0.setScaleX(0.957f);
                        }
                        if (rvp_integral_lottery.getChildAt(2) != null) {
                            View v2 = rvp_integral_lottery.getChildAt(2);
                            v2.setScaleY(1f);
                            v2.setScaleX(0.957f);
                        }
                    }
                }
            });
        }

        /**
         * 积分夺宝规则
         */
        @OnClick(R.id.tv_integral_lottery_rule)
        void integralLottery() {
            if (alertLotteryRuleDialogHelper != null) {
                alertLotteryRuleDialogHelper.show();
            }
        }

        /**
         * 我的奖励
         */
        @OnClick(R.id.tv_integral_lottery_award)
        void integralLotteryAward() {
            Intent intent = new Intent(AttendanceActivity.this, MineIntegralLotteryAwardActivity.class);
            startActivity(intent);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IS_LOGIN_CODE) {
            NetLoadUtils.getNetInstance().showLoadSirLoading(loadService);
            loadData();
        }
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }

    @OnClick(R.id.tv_header_shared)
    void invitePartner() {
        attendanceInvitePartner();
    }

    private void attendanceInvitePartner() {
        if (userId > 0) {
            new UMShareAction(AttendanceActivity.this
                    , "http://image.domolife.cn/lottery_share.png"
                    , "送你100元多么生活现金券，领取了我也有奖励哈哈~"
                    , "多么生活精选全球好物，买对就是省钱~新人特价专享1折起！"
                    , Url.BASE_SHARE_PAGE_TWO + "m/template/home/inviteNewbie.html?shareid=" + userId);
        }
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (integralLotteryAdapter.messageType.equals(getStrings(message.type))) {
            String refreshType = (String) message.result;
            switch (refreshType) {
                case "joinInIntegralLottery":
                case "integralLotteryEnd":
                    getIntegralLottery();
                    break;
            }
        } else if ("invitePartner".equals(message.type)) {
            attendanceInvitePartner();
        }
    }

    public class HotActivityHelper {
        @BindView(R.id.rv_activity_prefecture)
        RecyclerView rv_activity_prefecture;

        public void initViews() {
            rv_activity_prefecture.setNestedScrollingEnabled(false);
            rv_activity_prefecture.setLayoutManager(new GridLayoutManager(AttendanceActivity.this, 2));
            rv_activity_prefecture.addItemDecoration(new ItemDecoration.Builder()
                    // 设置分隔线资源ID
                    .setDividerId(R.drawable.item_divider_img_white)


                    .create());
            homeImgActivityAdapter = new HomeImgActivityAdapter(AttendanceActivity.this, adActivityBeans);
            rv_activity_prefecture.setAdapter(homeImgActivityAdapter);
            homeImgActivityAdapter.setOnItemClickListener((adapter, view, position) -> {
                CommunalADActivityBean communalADActivityBean = (CommunalADActivityBean) view.getTag();
                if (communalADActivityBean != null) {
                    setSkipPath(AttendanceActivity.this, communalADActivityBean.getAndroidLink(), false);
                }
            });
        }
    }

    public class IntegralDoubleHelper {
        @BindView(R.id.rv_integral_double)
        RecyclerView rv_integral_double;

        public void initViews() {
            rv_integral_double.setNestedScrollingEnabled(false);
            rv_integral_double.setLayoutManager(new GridLayoutManager(AttendanceActivity.this, 2));
            rv_integral_double.addItemDecoration(new ItemDecoration.Builder()
                    // 设置分隔线资源ID
                    .setDividerId(R.drawable.item_divider_five_gray_f)


                    .create());
            qualityCustomTopicAdapter = new QualityCustomTopicAdapter(AttendanceActivity.this, doubleIntegrationList);
            rv_integral_double.setAdapter(qualityCustomTopicAdapter);
            qualityCustomTopicAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
                @Override
                public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                    return doubleIntegrationList.get(position).getItemType() == TYPE_2 ? 2 : 1;
                }
            });
            qualityCustomTopicAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    LikedProductBean likedProductBean = (LikedProductBean) view.getTag();
                    if (likedProductBean != null) {
                        Intent intent = new Intent(AttendanceActivity.this, ShopScrollDetailsActivity.class);
                        intent.putExtra("productId", String.valueOf(likedProductBean.getId()));
                        startActivity(intent);
                    }
                }
            });
            qualityCustomTopicAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    if (view.getId() == R.id.tv_integral_pro_type) {
                        Intent intent = new Intent(AttendanceActivity.this, QualityCustomTopicActivity.class);
                        intent.putExtra("productType", DOUBLE_INTEGRAL_PREFECTURE);
                        intent.putExtra("showType", DOUBLE_INTEGRAL_TYPE);
                        startActivity(intent);
                    }
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(constantMethod!=null){
            constantMethod.stopSchedule();
            constantMethod.releaseHandlers();
        }
    }
}
