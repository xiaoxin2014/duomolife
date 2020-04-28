package com.amkj.dmsh.dominant.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.bean.TabNameBean;
import com.amkj.dmsh.constant.CommunalDetailBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.dao.GroupDao;
import com.amkj.dmsh.dominant.adapter.QualityCustomAdapter;
import com.amkj.dmsh.dominant.bean.GroupShopDetailsEntity;
import com.amkj.dmsh.dominant.bean.GroupShopDetailsEntity.GroupShopDetailsBean.ParticipantInfoBean.GroupShopJoinBean;
import com.amkj.dmsh.homepage.adapter.CommunalDetailAdapter;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean;
import com.amkj.dmsh.utils.TimeUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.webformatdata.CommunalWebDetailUtils;
import com.amkj.dmsh.views.flycoTablayout.SlidingTabLayout;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.iwgang.countdownview.CountdownView;
import cn.iwgang.countdownview.DynamicConfig;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static android.view.View.GONE;
import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getSpannableString;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.GROUP_SHOP_NEW_DETAILS;
import static com.amkj.dmsh.utils.TimeUtils.getTimeDifference;
import static com.amkj.dmsh.utils.TimeUtils.isEndOrStartTime;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/6/8
 * class description:开团成功
 */
public class DoMoGroupJoinShareActivity extends BaseActivity {
    @BindView(R.id.smart_scroll_communal_refresh)
    SmartRefreshLayout smart_scroll_communal_refresh;
    @BindView(R.id.communal_recycler_wrap)
    RecyclerView communal_recycler_wrap;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.sliding_tablayout)
    SlidingTabLayout mSlidingTablayout;
    @BindView(R.id.vp_custom)
    ViewPager mVpCustom;
    private List<CommunalDetailObjectBean> gpRuleList = new ArrayList<>();
    private GroupShareJoinView groupShareJoinView;
    private CommunalDetailAdapter gpRuleDetailsAdapter;
    private String orderNo;
    private ConstantMethod constantMethod;
    private QualityCustomAdapter qualityCustomAdapter;
    private String[] titles = {"自定义专区1", "自定义专区2", "自定义专区3"};
    private List<String> mProductTypeList = new ArrayList<>();
    private GroupShopDetailsEntity mGroupShopDetailsEntity;
    private GroupShopDetailsEntity.GroupShopDetailsBean mGroupShopDetailsBean;

    @Override
    protected int getContentView() {
        return R.layout.activity_ql_gp_sp_scroll;
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
        tv_header_titleAll.setText("拼团详情");
        Intent intent = getIntent();
        orderNo = intent.getStringExtra("orderNo");
        smart_scroll_communal_refresh.setOnRefreshListener(refreshLayout -> loadData());
        communal_recycler_wrap.setLayoutManager(new LinearLayoutManager(DoMoGroupJoinShareActivity.this));
        communal_recycler_wrap.setNestedScrollingEnabled(false);
        View headerView = LayoutInflater.from(DoMoGroupJoinShareActivity.this).inflate(R.layout.layout_ql_gp_sp_join_share, null);
        groupShareJoinView = new GroupShareJoinView();
        ButterKnife.bind(groupShareJoinView, headerView);
        DynamicConfig.Builder dynamic = new DynamicConfig.Builder();
        dynamic.setSuffixTextSize(AutoSizeUtils.mm2px(mAppContext, 28));
        dynamic.setTimeTextSize(AutoSizeUtils.mm2px(mAppContext, 28));
        groupShareJoinView.ct_time_communal_show_bg.dynamicShow(dynamic.build());
        communal_recycler_wrap.setNestedScrollingEnabled(false);
        gpRuleDetailsAdapter = new CommunalDetailAdapter(DoMoGroupJoinShareActivity.this, gpRuleList);
        gpRuleDetailsAdapter.addHeaderView(headerView);
        communal_recycler_wrap.setAdapter(gpRuleDetailsAdapter);
        //初始化自定义专区
        mProductTypeList.add("211");
        mProductTypeList.add("208");
        mProductTypeList.add("209");
        qualityCustomAdapter = new QualityCustomAdapter(getSupportFragmentManager(), mProductTypeList, getSimpleName());
        mVpCustom.setAdapter(qualityCustomAdapter);
        mVpCustom.setOffscreenPageLimit(titles.length - 1);
        mSlidingTablayout.setViewPager(mVpCustom, titles);
    }

    @Override
    protected void loadData() {
        getGroupShopDetails();
    }

    @Override
    public View getLoadView() {
        return smart_scroll_communal_refresh;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }


    private void getGroupShopDetails() {
        Map<String, Object> params = new HashMap<>();
        params.put("orderNo", orderNo);
        params.put("gpInfoId", 0);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, GROUP_SHOP_NEW_DETAILS,
                params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        smart_scroll_communal_refresh.finishRefresh();
                        Gson gson = new Gson();
                        mGroupShopDetailsEntity = gson.fromJson(result, GroupShopDetailsEntity.class);
                        if (mGroupShopDetailsEntity != null) {
                            mGroupShopDetailsBean = mGroupShopDetailsEntity.getGroupShopDetailsBean();
                            if (mGroupShopDetailsEntity.getCode().equals(SUCCESS_CODE)) {
                                if (mGroupShopDetailsBean != null) {
                                    setGroupShopDetailsData();
                                }
                            } else {
                                showToast( mGroupShopDetailsEntity.getMsg());
                            }
                        }
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, mGroupShopDetailsBean, mGroupShopDetailsEntity);
                    }

                    @Override
                    public void onNotNetOrException() {
                        smart_scroll_communal_refresh.finishRefresh();
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, mGroupShopDetailsBean, mGroupShopDetailsEntity);
                    }
                });
    }

    private void setGroupShopDetailsData() {
        //        拼团规则
        List<CommunalDetailBean> gpRuleBeanList = mGroupShopDetailsBean.getGpRule();
        if (gpRuleBeanList != null && gpRuleBeanList.size() > 0) {
            groupShareJoinView.mViewDividerGprule.setVisibility(View.VISIBLE);
            gpRuleList.clear();
            gpRuleList.addAll(CommunalWebDetailUtils.getCommunalWebInstance().getWebDetailsFormatDataList(gpRuleBeanList));
            gpRuleDetailsAdapter.notifyDataSetChanged();
        } else {
            groupShareJoinView.mViewDividerGprule.setVisibility(GONE);
        }

        //        参团列表
        GroupShopDetailsEntity.GroupShopDetailsBean.ParticipantInfoBean participantInfo = mGroupShopDetailsBean.getParticipantInfo();
        if (participantInfo != null) {
            List<GroupShopJoinBean> userInfoList = participantInfo.getUserInfoList();
            if (userInfoList != null && userInfoList.size() > 0) {
                GroupShopJoinBean groupShopJoinBean = userInfoList.get(0);
                if (groupShopJoinBean != null) {
                    GlideImageLoaderUtil.loadRoundImg(this, groupShareJoinView.iv_leader
                            , groupShopJoinBean.getAvatar(), AutoSizeUtils.mm2px(this, 100), R.drawable.default_ava_img);
                }
            }

            String endTime = participantInfo.getEndTime();
            if (!TextUtils.isEmpty(endTime)) {
                getConstant();
                setCountTime(groupShareJoinView.ct_time_communal_show_bg, endTime);
                constantMethod.createSchedule();
                constantMethod.setRefreshTimeListener(new ConstantMethod.RefreshTimeListener() {
                    @Override
                    public void refreshTime() {
                        mGroupShopDetailsEntity.setSecond(mGroupShopDetailsEntity.getSecond() + 1);
                        setCountTime(groupShareJoinView.ct_time_communal_show_bg, endTime);
                    }
                });
            }
        }

        String leftParticipant = getString(R.string.share_join_group, mGroupShopDetailsBean.getRequireCount() - 1);
        groupShareJoinView.tv_invite_fr_join_gp.setText(getSpannableString(leftParticipant, 2, 4, -1, "#ff5e6b"));
        groupShareJoinView.tv_new_user.setVisibility(mGroupShopDetailsBean.isNewUserGroup() ? View.VISIBLE : GONE);
        //开团时间
        if (mGroupShopDetailsBean.getParticipantInfo() != null && !TextUtils.isEmpty(mGroupShopDetailsBean.getParticipantInfo().getStartTime())) {
            groupShareJoinView.tv_gp_ql_share_product_c_time.setText(mGroupShopDetailsBean.getParticipantInfo().getStartTime());
        }
        //拼团封面
        GlideImageLoaderUtil.loadCenterCrop(DoMoGroupJoinShareActivity.this
                , groupShareJoinView.iv_gp_ql_share_product, mGroupShopDetailsBean.getCoverImage());
        //设置商品标题
        String gpName = mGroupShopDetailsBean.getGpName();
        String productName = mGroupShopDetailsBean.getProductName();
        String subTitle = mGroupShopDetailsBean.getSubTitle();
        groupShareJoinView.tv_gp_ql_share_pro_name.setText(getStrings(!TextUtils.isEmpty(gpName) ? gpName : (TextUtils.isEmpty(subTitle) ? productName : (subTitle + "•" + productName))));
        groupShareJoinView.tv_gp_ql_share_price.setText(("拼团价¥" + getStrings(mGroupShopDetailsBean.getGpPrice())));
        groupShareJoinView.tv_gp_ql_share_only_price.setText(("单买价¥" + getStrings(mGroupShopDetailsBean.getPrice())));
    }

    public void setCountTime(CountdownView countdownView, String gpEndTime) {
        //格式化结束时间
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date startTime = null;
        Date endTime = null;
        try {
            if (TextUtils.isEmpty(mGroupShopDetailsEntity.getCurrentTime())) {
                startTime = new Date();
            } else {
                startTime = formatter.parse(mGroupShopDetailsEntity.getCurrentTime());
            }
            endTime = formatter.parse(gpEndTime);
            if (startTime != null && endTime != null && endTime.getTime() > startTime.getTime()) {
                DynamicConfig.Builder dynamic = new DynamicConfig.Builder();
                dynamic.setTimeTextColor(0xffffffff);
                dynamic.setSuffixTextColor(0xff333333);
                DynamicConfig.BackgroundInfo backgroundInfo = new DynamicConfig.BackgroundInfo();
                backgroundInfo.setColor(getResources().getColor(R.color.text_normal_red));
                dynamic.setBackgroundInfo(backgroundInfo);
                countdownView.dynamicShow(dynamic.build());
                String timeformat = TimeUtils.getTimeDifferenceText(getTimeDifference(gpEndTime, formatter.format(startTime)));
                //倒计时样式格式化
                countdownView.customTimeShow(timeformat.contains("天"), timeformat.contains("时"), timeformat.contains("分"), !timeformat.contains("天"), false);
                countdownView.updateShow(endTime.getTime() - startTime.getTime() - mGroupShopDetailsEntity.getSecond() * 1000);
                groupShareJoinView.tv_show_communal_time_status.setText("剩余");
                if (!isEndOrStartTime(mGroupShopDetailsEntity.getCurrentTime()
                        , gpEndTime)) {
                    countdownView.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                        @Override
                        public void onEnd(CountdownView cv) {
                            cv.setOnCountdownEndListener(null);
                            loadData();
                        }
                    });
                } else {
                    countdownView.setOnCountdownEndListener(null);
                }
            } else {
                groupShareJoinView.tv_show_communal_time_status.setText("已过期");
                groupShareJoinView.ct_time_communal_show_bg.setVisibility(GONE);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    class GroupShareJoinView {
        //         倒计时状态
        @BindView(R.id.tv_show_communal_time_status)
        TextView tv_show_communal_time_status;
        //        剩余时间
        @BindView(R.id.ct_time_communal_show_bg)
        CountdownView ct_time_communal_show_bg;
        //        差成团
        @BindView(R.id.tv_invite_fr_join_gp)
        TextView tv_invite_fr_join_gp;
        //       新人团标志
        @BindView(R.id.tv_new_user)
        TextView tv_new_user;
        @BindView(R.id.rel_group_join_share)
        LinearLayout rel_group_join_share;
        //        创建时间
        @BindView(R.id.tv_gp_ql_share_product_c_time)
        TextView tv_gp_ql_share_product_c_time;
        //        商品图片
        @BindView(R.id.iv_gp_ql_share_product)
        ImageView iv_gp_ql_share_product;
        //        商品名字
        @BindView(R.id.tv_gp_ql_share_pro_name)
        TextView tv_gp_ql_share_pro_name;
        //        团购价
        @BindView(R.id.tv_gp_ql_share_price)
        TextView tv_gp_ql_share_price;
        //        单买价
        @BindView(R.id.tv_gp_ql_share_only_price)
        TextView tv_gp_ql_share_only_price;
        //        团长头像
        @BindView(R.id.iv_leader)
        ImageView iv_leader;

        //    页面分享
        @OnClick(R.id.tv_join_share)
        void sendShare(View view) {
            invateJoin();
        }

        @BindView(R.id.view_divider_gprule)
        View mViewDividerGprule;
    }

    private void invateJoin() {
        GroupDao.invitePartnerGroup(getActivity(), mGroupShopDetailsBean);
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }

    //    页面分享
    @OnClick(R.id.tv_header_shared)
    void sendShare(View view) {
        invateJoin();
    }


    private void getConstant() {
        if (constantMethod == null) {
            constantMethod = new ConstantMethod();
        }
    }

    @Override
    protected void onDestroy() {
        getConstant();
        constantMethod.stopSchedule();
        constantMethod.releaseHandlers();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            if (requestCode == IS_LOGIN_CODE) {
                finish();
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (message.type.equals(ConstantVariable.UPDATE_CUSTOM_NAME)) {
            try {
                if (mSlidingTablayout != null) {
                    TabNameBean tabNameBean = (TabNameBean) message.result;
                    String simpleName = tabNameBean.getSimpleName();
                    if (getSimpleName().equals(simpleName) && !TextUtils.isEmpty(simpleName)) {
                        TextView titleView = mSlidingTablayout.getTitleView(tabNameBean.getPosition());
                        titleView.setText(simpleName);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
