package com.amkj.dmsh.dominant.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.constant.CommunalDetailBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dominant.bean.GroupShopCommunalInfoEntity;
import com.amkj.dmsh.dominant.bean.GroupShopCommunalInfoEntity.GroupShopCommunalInfoBean;
import com.amkj.dmsh.dominant.bean.QualityGroupShareEntity;
import com.amkj.dmsh.dominant.bean.QualityGroupShareEntity.QualityGroupShareBean;
import com.amkj.dmsh.dominant.bean.QualityGroupShareEntity.QualityGroupShareBean.MemberListBean;
import com.amkj.dmsh.homepage.adapter.CommunalDetailAdapter;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.webformatdata.CommunalWebDetailUtils;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.JustifyContent;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

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
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.GROUP_MINE_SHARE;
import static com.amkj.dmsh.constant.Url.GROUP_SHOP_COMMUNAL;
import static com.amkj.dmsh.utils.ProductLabelCreateUtils.getLabelInstance;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/6/8
 * class description:多么生活拼团 分享跳转详情
 */
public class DoMoGroupJoinShareActivity extends BaseActivity {
    @BindView(R.id.smart_scroll_communal_refresh)
    SmartRefreshLayout smart_scroll_communal_refresh;
    @BindView(R.id.communal_recycler_wrap)
    RecyclerView communal_recycler_wrap;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    //    拼团规则
    private List<CommunalDetailObjectBean> gpRuleList = new ArrayList<>();
    private GroupShareJoinView groupShareJoinView;
    private CommunalDetailAdapter gpRuleDetailsAdapter;
    private GroupShopCommunalInfoEntity groupShopJoinEntity;
    private String orderNo;
    private QualityGroupShareBean qualityGroupShareBean;
    private QualityGroupShareEntity qualityGroupShareEntity;
    private ConstantMethod constantMethod;

    @Override
    protected int getContentView() {
        return R.layout.activity_ql_gp_sp_scroll;
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
        tv_header_titleAll.setText("DoMo拼团");
        Intent intent = getIntent();
        orderNo = intent.getStringExtra("orderNo");
        smart_scroll_communal_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                loadData();
            }
        });
        communal_recycler_wrap.setLayoutManager(new LinearLayoutManager(DoMoGroupJoinShareActivity.this));
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
    }

    @Override
    protected void loadData() {
        getGroupShareInfo();
        getCommunalInfo();
    }

    @Override
    public View getLoadView() {
        return smart_scroll_communal_refresh;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    private void getCommunalInfo() {
        NetLoadUtils.getNetInstance().loadNetDataPost(this, GROUP_SHOP_COMMUNAL, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                groupShopJoinEntity = gson.fromJson(result, GroupShopCommunalInfoEntity.class);
                if (groupShopJoinEntity != null) {
                    if (groupShopJoinEntity.getCode().equals(SUCCESS_CODE)) {
                        setCommunalInfo(groupShopJoinEntity.getGroupShopCommunalInfoBean());
                    }
                }
            }
        });
    }

    private void setCommunalInfo(GroupShopCommunalInfoBean groupShopCommunalInfoBean) {
        List<CommunalDetailBean> gpRuleBeanList = groupShopCommunalInfoBean.getGpRule();
//        拼团规则
        if (gpRuleBeanList != null && gpRuleBeanList.size() > 0) {
            gpRuleList.clear();
            gpRuleList.addAll(CommunalWebDetailUtils.getCommunalWebInstance().getWebDetailsFormatDataList(gpRuleBeanList));
            gpRuleDetailsAdapter.notifyDataSetChanged();
        }
    }

    private void getGroupShareInfo() {
        Map<String, Object> params = new HashMap<>();
        params.put("orderNo", orderNo);
        NetLoadUtils.getNetInstance().loadNetDataPost(DoMoGroupJoinShareActivity.this, GROUP_MINE_SHARE
                , params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        smart_scroll_communal_refresh.finishRefresh();
                        Gson gson = new Gson();
                        qualityGroupShareEntity = gson.fromJson(result, QualityGroupShareEntity.class);
                        if (qualityGroupShareEntity != null) {
                            if (qualityGroupShareEntity.getCode().equals(SUCCESS_CODE)) {
                                setGpDataInfo(qualityGroupShareEntity);
                            } else if (!qualityGroupShareEntity.getCode().equals(EMPTY_CODE)) {
                                showToast(DoMoGroupJoinShareActivity.this, qualityGroupShareEntity.getMsg());
                            }
                        }
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, qualityGroupShareBean, qualityGroupShareEntity);
                    }

                    @Override
                    public void onNotNetOrException() {
                        smart_scroll_communal_refresh.finishRefresh();
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, qualityGroupShareBean, qualityGroupShareEntity);
                    }

                    @Override
                    public void netClose() {
                        showToast(DoMoGroupJoinShareActivity.this, R.string.unConnectedNetwork);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        showToast(DoMoGroupJoinShareActivity.this, R.string.connectedFaile);
                    }
                });
    }

    private void setGpDataInfo(final QualityGroupShareEntity qualityGroupShareEntity) {
        qualityGroupShareBean = qualityGroupShareEntity.getQualityGroupShareBean();
        getConstant();
        setCountTime(groupShareJoinView.ct_time_communal_show_bg, qualityGroupShareEntity);
        constantMethod.createSchedule();
        constantMethod.setRefreshTimeListener(new ConstantMethod.RefreshTimeListener() {
            @Override
            public void refreshTime() {
                qualityGroupShareEntity.setSecond(qualityGroupShareEntity.getSecond() + 1);
                setCountTime(groupShareJoinView.ct_time_communal_show_bg, qualityGroupShareEntity);
            }
        });
        //        参团列表
        List<MemberListBean> memberList = qualityGroupShareBean.getMemberList();
        if (memberList != null && memberList.size() > 0) {
            for (int i = 0; i < (qualityGroupShareBean.getMemberCount() - memberList.size()); i++) {
                MemberListBean memberListBean = new MemberListBean();
                memberListBean.setAvatar("android.resource://com.amkj.dmsh/drawable/" + R.drawable.dm_gp_join);
                memberList.add(memberListBean);
            }
        } else {
            showToast(DoMoGroupJoinShareActivity.this, "拼团信息获取失败，请刷新重试");
        }
        if (memberList != null && memberList.size() > 0) {
            groupShareJoinView.rel_group_join_share.setVisibility(View.VISIBLE);
            groupShareJoinView.flex_communal_tag.setJustifyContent(JustifyContent.CENTER);
            groupShareJoinView.flex_communal_tag.removeAllViews();
            groupShareJoinView.flex_communal_tag.setShowDivider(FlexboxLayout.SHOW_DIVIDER_MIDDLE);
            groupShareJoinView.flex_communal_tag.setDividerDrawable(getResources().getDrawable(R.drawable.item_divider_nine_dp_white));
            for (int i = 0; i < memberList.size(); i++) {
                groupShareJoinView.flex_communal_tag.addView(getLabelInstance()
                        .createOpenGroupUserInfo(DoMoGroupJoinShareActivity.this, memberList.get(i)));
            }
        } else {
            groupShareJoinView.rel_group_join_share.setVisibility(GONE);
        }
        gpRuleDetailsAdapter.notifyDataSetChanged();
        groupShareJoinView.tv_invite_fr_join_gp.setText(getString(R.string.share_join_group, qualityGroupShareBean.getLeftParticipant()));
        groupShareJoinView.tv_gp_ql_share_product_c_time.setText(getStrings(qualityGroupShareBean.getGpStartTime()));
        GlideImageLoaderUtil.loadCenterCrop(DoMoGroupJoinShareActivity.this
                , groupShareJoinView.iv_gp_ql_share_product, qualityGroupShareBean.getGpPicUrl());
        groupShareJoinView.tv_gp_ql_share_pro_name.setText(getStrings(qualityGroupShareBean.getName()));
        groupShareJoinView.tv_gp_ql_share_count.setText(getStrings(qualityGroupShareBean.getGpType()));
        groupShareJoinView.tv_gp_ql_share_open_count.setText(getString(R.string.group_buy_count, qualityGroupShareBean.getGpCount()));
        groupShareJoinView.tv_gp_ql_share_price.setText(getString(R.string.money_price_chn, getStrings(qualityGroupShareBean.getGpPrice())));
        groupShareJoinView.tv_gp_ql_share_only_price.setText(("单买价￥" + getStrings(qualityGroupShareBean.getPrice())));
    }

    public void setCountTime(CountdownView countdownView, QualityGroupShareEntity qualityGroupShareEntity) {
        //格式化结束时间
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date startTime = null;
        Date endTime = null;
        QualityGroupShareBean qualityGroupShareBean = qualityGroupShareEntity.getQualityGroupShareBean();
        try {
            if (TextUtils.isEmpty(qualityGroupShareEntity.getCurrentTime())) {
                startTime = new Date();
            } else {
                startTime = formatter.parse(qualityGroupShareEntity.getCurrentTime());
            }
            endTime = formatter.parse(qualityGroupShareBean.getGpEndTime());
            if (startTime != null && endTime != null && endTime.getTime() > startTime.getTime()) {
                DynamicConfig.Builder dynamic = new DynamicConfig.Builder();
                dynamic.setTimeTextColor(0xffffffff);
                dynamic.setSuffixTextColor(0xff333333);
                DynamicConfig.BackgroundInfo backgroundInfo = new DynamicConfig.BackgroundInfo();
                backgroundInfo.setColor(getResources().getColor(R.color.text_normal_red));
                dynamic.setBackgroundInfo(backgroundInfo);
                countdownView.dynamicShow(dynamic.build());
                countdownView.updateShow(endTime.getTime() - startTime.getTime() - qualityGroupShareEntity.getSecond() * 1000);
                groupShareJoinView.tv_show_communal_time_status.setText("剩余");
                if (!ConstantMethod.isEndOrStartTime(qualityGroupShareEntity.getCurrentTime()
                        , qualityGroupShareBean.getGpEndTime())) {
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
                groupShareJoinView.tv_show_communal_time_status.setText("已结束");
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
        @BindView(R.id.rel_group_join_share)
        RelativeLayout rel_group_join_share;
        //        人数头像
        @BindView(R.id.flex_communal_tag)
        FlexboxLayout flex_communal_tag;
        //        创建时间
        @BindView(R.id.tv_gp_ql_share_product_c_time)
        TextView tv_gp_ql_share_product_c_time;
        //        商品图片
        @BindView(R.id.iv_gp_ql_share_product)
        ImageView iv_gp_ql_share_product;
        //        商品名字
        @BindView(R.id.tv_gp_ql_share_pro_name)
        TextView tv_gp_ql_share_pro_name;
        //        两人团
        @BindView(R.id.tv_gp_ql_share_count)
        TextView tv_gp_ql_share_count;
        //        已开团人数
        @BindView(R.id.tv_gp_ql_share_open_count)
        TextView tv_gp_ql_share_open_count;
        //        团购价
        @BindView(R.id.tv_gp_ql_share_price)
        TextView tv_gp_ql_share_price;
        //        单买价
        @BindView(R.id.tv_gp_ql_share_only_price)
        TextView tv_gp_ql_share_only_price;

        //    页面分享
        @OnClick(R.id.tv_join_share)
        void sendShare(View view) {
            if (qualityGroupShareBean != null) {
                new UMShareAction(DoMoGroupJoinShareActivity.this
                        , qualityGroupShareBean.getGpPicUrl()
                        , qualityGroupShareBean.getName()
                        , getStrings(qualityGroupShareBean.getSubtitle())
                        , Url.BASE_SHARE_PAGE_TWO + "m/template/share_template/groupShare.html?id=" + qualityGroupShareBean.getGpInfoId()
                        + "&record=" + qualityGroupShareBean.getGpRecordId(), "pages/groupshare/groupshare?id=" + qualityGroupShareBean.getGpInfoId()
                        + (TextUtils.isEmpty(orderNo) ? "&gpRecordId=" + qualityGroupShareBean.getGpRecordId() : "&order=" + orderNo), qualityGroupShareBean.getGpInfoId());
            }
        }
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }

    //    页面分享
    @OnClick(R.id.tv_header_shared)
    void sendShare(View view) {
        if (qualityGroupShareBean != null) {
            new UMShareAction(DoMoGroupJoinShareActivity.this
                    , qualityGroupShareBean.getGpPicUrl()
                    , qualityGroupShareBean.getName()
                    , getStrings(qualityGroupShareBean.getSubtitle())
                    , Url.BASE_SHARE_PAGE_TWO + "m/template/share_template/groupShare.html?id=" + qualityGroupShareBean.getGpInfoId()
                    + "&record=" + qualityGroupShareBean.getGpRecordId(), "pages/groupshare/groupshare?id=" + qualityGroupShareBean.getGpInfoId()
                    + (TextUtils.isEmpty(orderNo) ? "&gpRecordId=" + qualityGroupShareBean.getGpRecordId() : "&order=" + orderNo), qualityGroupShareBean.getGpInfoId());
        }
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
}
