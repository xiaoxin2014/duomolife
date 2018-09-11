package com.amkj.dmsh.mine.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ali.auth.third.ui.context.CallbackContext;
import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.AlibcTradeSDK;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.page.AlibcBasePage;
import com.alibaba.baichuan.android.trade.page.AlibcMyOrdersPage;
import com.alibaba.baichuan.trade.biz.AlibcConstants;
import com.alibaba.baichuan.trade.biz.context.AlibcTradeResult;
import com.alibaba.baichuan.trade.biz.login.AlibcLogin;
import com.alibaba.baichuan.trade.biz.login.AlibcLoginCallback;
import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.base.NetLoadUtils;
import com.amkj.dmsh.bean.CommunalUserInfoEntity;
import com.amkj.dmsh.bean.QualityTypeEntity;
import com.amkj.dmsh.bean.QualityTypeEntity.QualityTypeBean;
import com.amkj.dmsh.constant.CommunalAdHolderView;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.homepage.activity.AttendanceActivity;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity.CommunalADActivityBean;
import com.amkj.dmsh.message.activity.MessageActivity;
import com.amkj.dmsh.mine.activity.AppDataActivity;
import com.amkj.dmsh.mine.activity.MineCollectContentActivity;
import com.amkj.dmsh.mine.activity.MineCollectProductActivity;
import com.amkj.dmsh.mine.activity.MineInvitationListActivity;
import com.amkj.dmsh.mine.activity.MineLoginActivity;
import com.amkj.dmsh.mine.activity.PersonalBgImgActivity;
import com.amkj.dmsh.mine.activity.PersonalDataActivity;
import com.amkj.dmsh.mine.activity.ShopCarActivity;
import com.amkj.dmsh.mine.activity.ShopTimeMyWarmActivity;
import com.amkj.dmsh.mine.adapter.MineTypeAdapter;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.qyservice.QyServiceUtils;
import com.amkj.dmsh.shopdetails.activity.DirectGoodsSaleAfterActivity;
import com.amkj.dmsh.shopdetails.activity.DirectMyCouponActivity;
import com.amkj.dmsh.shopdetails.activity.DoMoIndentAllActivity;
import com.amkj.dmsh.shopdetails.adapter.IndentTypeAdapter;
import com.amkj.dmsh.shopdetails.bean.DirectIndentCountEntity;
import com.amkj.dmsh.shopdetails.bean.DirectIndentCountEntity.DirectIndentCountBean;
import com.amkj.dmsh.shopdetails.integration.IntegralProductIndentActivity;
import com.amkj.dmsh.user.activity.UserFansAttentionActivity;
import com.amkj.dmsh.utils.ImageConverterUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.amkj.dmsh.views.SystemBarHelper;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.google.gson.Gson;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
import com.qiyukf.unicorn.api.UnreadCountChangeListener;
import com.zhy.autolayout.AutoLayoutInfo;
import com.zhy.autolayout.attr.HeightAttr;
import com.zhy.autolayout.utils.AutoLayoutHelper;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static com.amkj.dmsh.base.BaseApplication.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getPersonalInfo;
import static com.amkj.dmsh.constant.ConstantMethod.getShowNumber;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.setSkipPath;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.BASE_RESOURCE_DRAW;
import static com.amkj.dmsh.constant.ConstantVariable.START_AUTO_PAGE_TURN;
import static com.amkj.dmsh.constant.ConstantVariable.STOP_AUTO_PAGE_TURN;

;

/**
 * Created by atd48 on 2016/8/17.
 * 主页-我的
 */
public class MineDataFragment extends BaseFragment {
    private static final String MAKE_SHARE_URL = "http://www.domolife.cn/m/template/home/shareHome.html";
    //    背景
    @BindView(R.id.iv_mine_page_bg)
    ImageView iv_mine_page_bg;
    //    登录布局
    @BindView(R.id.ll_mine_login)
    LinearLayout ll_mine_login;
    //头像
    @BindView(R.id.iv_mine_header)
    ImageView iv_mine_header;
    //名字
    @BindView(R.id.tv_mine_name)
    TextView tv_mine_name;
    //    获取更多积分
    @BindView(R.id.tv_mine_get_score_more)
    TextView tv_mine_get_score_more;
    //粉丝数
    @BindView(R.id.tv_mine_fans_count)
    TextView tv_mine_fans_count;
    //关注数
    @BindView(R.id.tv_mine_att_count)
    TextView tv_mine_att_count;
    //    帖子数
    @BindView(R.id.tv_mine_inv_count)
    TextView tv_mine_inv_count;
    //现有积分
    @BindView(R.id.tv_mine_score)
    TextView tv_mine_score;
    //    非登录状态
    @BindView(R.id.ll_mime_no_login)
    LinearLayout ll_mime_no_login;
    @BindView(R.id.rel_header_title_mine)
    public RelativeLayout rel_header_mine;
    //    头部背景
    @BindView(R.id.fl_mine_bg)
    public FrameLayout fl_mine_bg;
    //    订单模块
    @BindView(R.id.rv_mine_indent_item)
    public RecyclerView rv_mine_indent;
    //    我的模块
    @BindView(R.id.communal_recycler_wrap)
    public RecyclerView communal_recycler_wrap;
    @BindView(R.id.ad_mine)
    public ConvenientBanner ad_mine;
    @BindView(R.id.rel_personal_data_sup)
    public RelativeLayout rel_personal_data_sup;
    @BindView(R.id.tv_personal_data_sup)
    public TextView tv_personal_data_sup;
    private final int LOGIN_REQ_CODE = 66;
    private CommunalUserInfoEntity.CommunalUserInfoBean communalUserInfoBean;
    private MineTypeAdapter typeMineAdapter;
    private List<QualityTypeBean> mineTypeList = new ArrayList();
    private final String[] typeMineName = {"购物车", "优惠券", "分享赚", "积分订单", "秒杀提醒", "收藏商品", "收藏内容", "客服",};
    private final String[] typeMinePic = {"m_s_car_icon", "m_coupon_icon", "m_share_icon", "m_i_integ_icon", "m_warm_icon", "m_c_pro_icon", "m_c_content_icon", "m_service_icon",};
    //    订单模块
    private IndentTypeAdapter indentTypeAdapter;
    private List<QualityTypeBean> indentTypeList = new ArrayList();
    private List<CommunalADActivityBean> adBeanList = new ArrayList<>();
    private final String[] typeIndentName = {"淘宝订单", "待付款", "待发货", "待收货", "退货/售后"};
    private final String[] typeIndentPic = {"i_tb_icon", "i_w_pay_icon", "i_w_send_icon", "i_w_appraise_icon", "i_s_af_icon"};
    //    跳转登录请求码
    private QualityTypeBean qualityTypeBean;
    private String avatar;
    private CBViewHolderCreator cbViewHolderCreator;
    private QyServiceUtils qyInstance;

    @Override
    protected int getContentView() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initViews() {
        isLoginStatus();
        tv_mine_get_score_more.getPaint().setAntiAlias(true);//抗锯齿
        tv_mine_get_score_more.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 4);
        communal_recycler_wrap.setLayoutManager(manager);
        communal_recycler_wrap.addItemDecoration(new PinnedHeaderItemDecoration.Builder(-1)
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_gray_f_two_px)
                // 开启绘制分隔线，默认关闭
                .enableDivider(true)
                // 是否关闭标签点击事件，默认开启
                .disableHeaderClick(false)
                // 设置标签和其内部的子控件的监听，若设置点击监听不为null，但是disableHeaderClick(true)的话，还是不会响应点击事件
                .setHeaderClickListener(null)
                .create());
        communal_recycler_wrap.setNestedScrollingEnabled(false);
        mineTypeList = new ArrayList<>();
        for (int i = 0; i < typeMineName.length; i++) {
            qualityTypeBean = new QualityTypeBean();
            qualityTypeBean.setName(typeMineName[i]);
            qualityTypeBean.setPicUrl(typeMinePic[i]);
            qualityTypeBean.setId(i);
            mineTypeList.add(qualityTypeBean);
        }
        if (AutoUtils.getPercentWidth1px() < AutoUtils.getPercentHeight1px()) {
            fl_mine_bg.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
                AutoLayoutHelper.AutoLayoutParams autoLayoutHelper = (AutoLayoutHelper.AutoLayoutParams) fl_mine_bg.getLayoutParams();
                AutoLayoutInfo autoLayoutInfo = autoLayoutHelper.getAutoLayoutInfo();
                autoLayoutInfo.addAttr(new HeightAttr(480, 0, 1) {
                });
                fl_mine_bg.setLayoutParams(fl_mine_bg.getLayoutParams());
            });
        }
        typeMineAdapter = new MineTypeAdapter(getActivity(), mineTypeList);
        communal_recycler_wrap.setAdapter(typeMineAdapter);
        typeMineAdapter.setOnItemClickListener((adapter, view, position) -> {
            QualityTypeBean qualityTypeBean = (QualityTypeBean) view.getTag();
            if (qualityTypeBean != null) {
                if (qualityTypeBean.getId() == 7) {
                    conversation();
                } else {
                    Intent intent = new Intent();
//                    if (uid != 0) {
                    switch (qualityTypeBean.getId()) {
                        case 0: //购物车
                            intent.setClass(getActivity(), ShopCarActivity.class);
                            startActivity(intent);
                            break;
                        case 1: //优惠券
                            intent.setClass(getActivity(), DirectMyCouponActivity.class);
                            startActivity(intent);
                            break;
                        case 2: //分享赚
                            setSkipPath(getActivity(), MAKE_SHARE_URL, false);
                            break;
                        case 3: //积分订单
                            intent.setClass(getActivity(), IntegralProductIndentActivity.class);
                            startActivity(intent);
                            break;
                        case 4://秒杀提醒
                            intent.setClass(getActivity(), ShopTimeMyWarmActivity.class);
                            startActivity(intent);
                            break;
                        case 5://收藏商品
                            intent.setClass(getActivity(), MineCollectProductActivity.class);
                            startActivity(intent);
                            break;
                        case 6://收藏内容
                            intent.setClass(getActivity(), MineCollectContentActivity.class);
                            startActivity(intent);
                            break;
                    }
//                    } else {
//                        skipLoginPage();
//                    }
                }
            }
        });

//        我的订单模块
        indentTypeList = new ArrayList<>();
        for (int i = 0; i < typeIndentName.length; i++) {
            qualityTypeBean = new QualityTypeEntity.QualityTypeBean();
            qualityTypeBean.setName(typeIndentName[i]);
            qualityTypeBean.setPicUrl(typeIndentPic[i]);
            qualityTypeBean.setId(i);
            indentTypeList.add(qualityTypeBean);
        }
        rv_mine_indent.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        indentTypeAdapter = new IndentTypeAdapter(getActivity(), indentTypeList);
        rv_mine_indent.setAdapter(indentTypeAdapter);
        indentTypeAdapter.setOnItemClickListener((adapter, view, position) -> {
            QualityTypeBean qualityTypeBean = (QualityTypeBean) view.getTag();
            if (qualityTypeBean != null) {
                if (userId > 0) {
                    Intent intent = new Intent();
                    switch (qualityTypeBean.getId()) {
                        case 0: //淘宝订单
                            skipNewTaoBao();
                            break;
                        case 1: //待付款
                            intent.setClass(getActivity(), DoMoIndentAllActivity.class);
                            intent.putExtra("tab", "waitPay");
                            startActivity(intent);
                            break;
                        case 2: //待发货
                            intent.setClass(getActivity(), DoMoIndentAllActivity.class);
                            intent.putExtra("tab", "waitSend");
                            startActivity(intent);
                            break;
                        case 3: //待收货
                            intent.setClass(getActivity(), DoMoIndentAllActivity.class);
                            intent.putExtra("tab", "delivered");
                            startActivity(intent);
                            break;
                        case 4://退货/售后
                            intent.setClass(getActivity(), DirectGoodsSaleAfterActivity.class);
                            intent.putExtra("type", "goodsReturn");
                            startActivity(intent);
                            break;
                    }
                } else {
                    skipLoginPage();
                }
            }
        });
        setStatusColor();
        setQyService();
    }

    /**
     * 设置客服消息监听
     */
    private void setQyService() {
        qyInstance = QyServiceUtils.getQyInstance();
        setServiceUnread(qyInstance.getServiceTotalCount());
        qyInstance.getServiceCount(new UnreadCountChangeListener() {
            @Override
            public void onUnreadCountChange(int count) {
                setServiceUnread(count);
            }
        });
    }

    /**
     * 更新客服未读消息
//     * @param serviceTotalCount 未读消息条数
     */
    private void setServiceUnread(int serviceTotalCount) {
        if (mineTypeList.size() > 7) {
            if (serviceTotalCount > 0) {
                mineTypeList.get(7).setType(serviceTotalCount);
            } else {
                mineTypeList.get(7).setType(0);
            }
            typeMineAdapter.notifyItemChanged(7);
        }
    }
    private void setStatusColor() {
        SystemBarHelper.setStatusBarDarkMode(getActivity());
        SystemBarHelper.setPadding(getActivity(), rel_header_mine);
        SystemBarHelper.immersiveStatusBar(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoginStatus();
        getDuoMeIndentDataCount();
        getMineAd();
    }

    private void getLoginStatus() {
        SavePersonalInfoBean personalInfo = getPersonalInfo(getActivity());
        if (personalInfo.isLogin()) {
            avatar = personalInfo.getAvatar();
            getNetDataInfo();
        } else {
            initLoggedView();
            setBottomCount(null);
        }
    }

    private void setData(final CommunalUserInfoEntity.CommunalUserInfoBean userData) {
        tv_mine_name.setText(getStrings(userData.getNickname()));
        tv_mine_att_count.setText(String.valueOf(userData.getFllow()));
        tv_mine_fans_count.setText(String.valueOf(userData.getFans()));
        tv_mine_inv_count.setText(String.valueOf(userData.getDocumentcount()));
        tv_mine_score.setText(("积分：" + userData.getScore()));
        GlideImageLoaderUtil.loadHeaderImg(getActivity(), iv_mine_header, !TextUtils.isEmpty(userData.getAvatar())
                ? ImageConverterUtils.getFormatImg(userData.getAvatar()) : "");
        GlideImageLoaderUtil.loadCenterCrop(getActivity(), iv_mine_page_bg, !TextUtils.isEmpty(userData.getBgimg_url())
                ? ImageConverterUtils.getFormatImg(userData.getBgimg_url()) : BASE_RESOURCE_DRAW + R.drawable.mine_no_login_bg);
        setBottomCount(userData);
        if (userData.getNoticeInfo() != null && !TextUtils.isEmpty(userData.getNoticeInfo().getContent())) {
            rel_personal_data_sup.setVisibility(View.VISIBLE);
            tv_personal_data_sup.setText(getStrings(userData.getNoticeInfo().getContent()));
        } else {
            rel_personal_data_sup.setVisibility(View.GONE);
        }
    }

    /**
     * 设置底栏购物车 优惠券 积分订单数量
     *
     * @param userData
     */
    private void setBottomCount(CommunalUserInfoEntity.CommunalUserInfoBean userData) {
        if (userData != null && mineTypeList.size() > 3) {
            if (userData.getCartTotal() > 0) {
                mineTypeList.get(0).setType(userData.getCartTotal());
            } else {
                mineTypeList.get(0).setType(0);
            }
            if (userData.getCouponTotal() > 0) {
                mineTypeList.get(1).setType(userData.getCouponTotal());
            } else {
                mineTypeList.get(1).setType(0);
            }
            if (userData.getJfTotal() > 0) {
                mineTypeList.get(3).setType(userData.getJfTotal());
            } else {
                mineTypeList.get(3).setType(0);
            }
        } else {
            for (int i = 0; i < mineTypeList.size(); i++) {
                QualityTypeBean qualityTypeBean = mineTypeList.get(i);
                qualityTypeBean.setType(0);
            }
        }
        typeMineAdapter.notifyDataSetChanged();
    }

    //启动客服，登录
    private void conversation() {
        QyServiceUtils qyServiceUtils = QyServiceUtils.getQyInstance();
        qyServiceUtils.openQyServiceChat(getActivity(), "首页-我的");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        try {
            CallbackContext.onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /**
         * 销毁电商SDK相关资源引用，防止内存泄露
         */
        AlibcTradeSDK.destory();
    }

    @Override
    protected void loadData() {
    }

    private void isLoginStatus() {
        SavePersonalInfoBean personalInfo = getPersonalInfo(getActivity());
        if (personalInfo.isLogin()) {
            avatar = personalInfo.getAvatar();
            ConstantMethod constantMethod = new ConstantMethod();
            constantMethod.getNewUserCouponDialog(getActivity());
        }
    }

    private void getDuoMeIndentDataCount() {
        if (userId > 0) {
            String url = Url.BASE_URL + Url.Q_QUERY_INDENT_COUNT;
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    DirectIndentCountEntity directIndentCountEntity = gson.fromJson(result, DirectIndentCountEntity.class);
                    if (directIndentCountEntity != null) {
                        if (directIndentCountEntity.getCode().equals("01")) {
                            setCountData(directIndentCountEntity.getDirectIndentCountBean());
                        } else {
                            if (directIndentCountEntity.getCode().equals("02")) {
                                return;
                            } else {
                                showToast(getActivity(), R.string.invalidData);
                            }
                        }
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                }
            });
        } else {
            setCountData(null);
        }
    }

    private void setCountData(DirectIndentCountBean messageTotalBean) {
        if (messageTotalBean != null) {
            //待付款
            indentTypeList.get(1).setType(messageTotalBean.getWaitPayNum());
            //待发货
            indentTypeList.get(2).setType(messageTotalBean.getWaitDeliveryNum());
            //待收货
            indentTypeList.get(3).setType(messageTotalBean.getWaitTakeDeliveryNum());
            //退货售后
            indentTypeList.get(4).setType(messageTotalBean.getWaitAfterSaleNum());
        } else {
            int totalCount = 0;
            //待付款
            indentTypeList.get(1).setType(totalCount);
            //待发货
            indentTypeList.get(2).setType(totalCount);
            //待收货
            indentTypeList.get(3).setType(totalCount);
            //退货售后
            indentTypeList.get(4).setType(totalCount);
        }
        indentTypeAdapter.setNewData(indentTypeList);
    }

    private void getNetDataInfo() {
        String url = Url.BASE_URL + Url.MINE_PAGE;
        Map<String,Object> params = new HashMap<>();
        params.put("uid",userId);
        NetLoadUtils.getQyInstance().loadNetDataPost(mAppContext, url
                , params, new NetLoadUtils.NetLoadListener() {
                    @Override
                    public void onSuccess(String result) {
                        getUserDataInfo(result, false);
                    }

                    @Override
                    public void netClose() {

                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }
                });
    }

    //    我的模块 广告
    private void getMineAd() {
        String url = Url.BASE_URL + Url.MINE_PAGE_AD;
        Map<String, String> params = new HashMap<>();
        params.put("vidoShow", "1");
        XUtil.Get(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                adBeanList.clear();
                CommunalADActivityEntity adActivityEntity = gson.fromJson(result, CommunalADActivityEntity.class);
                if (adActivityEntity != null) {
                    if (adActivityEntity.getCode().equals("01")) {
                        setMineAdData(adActivityEntity);
                    } else if (!adActivityEntity.getCode().equals("02")) {
                        showToast(getActivity(), adActivityEntity.getMsg());
                        ad_mine.setVisibility(View.GONE);
                    } else {
                        ad_mine.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ad_mine.setVisibility(View.GONE);
                super.onError(ex, isOnCallback);
            }
        });
    }

    private void setMineAdData(CommunalADActivityEntity adActivityEntity) {
        adBeanList.addAll(adActivityEntity.getCommunalADActivityBeanList());
        ad_mine.setVisibility(View.VISIBLE);
        if (cbViewHolderCreator == null) {
            cbViewHolderCreator = new CBViewHolderCreator() {
                @Override
                public Holder createHolder(View itemView) {
                    return new CommunalAdHolderView(itemView, getActivity(), true);
                }

                @Override
                public int getLayoutId() {
                    return R.layout.layout_ad_image_video;
                }
            };
        }
        ad_mine.setPages(getActivity(), cbViewHolderCreator, adBeanList).setCanLoop(true).setPointViewVisible(true).setCanScroll(true)
                .setPageIndicator(new int[]{R.drawable.unselected_radius, R.drawable.selected_radius})
                .startTurning(getShowNumber(adBeanList.get(0).getShowTime()) * 1000);
    }

    private void initLoggedView() {
        if (ll_mine_login == null) {
            return;
        }
        ll_mine_login.setVisibility(View.GONE);
        ll_mime_no_login.setVisibility(View.VISIBLE);
        rel_personal_data_sup.setVisibility(View.GONE);
        GlideImageLoaderUtil.loadCenterCrop(getActivity(), iv_mine_page_bg, BASE_RESOURCE_DRAW + R.drawable.mine_no_login_bg);
    }

    private void getUserDataInfo(String result, boolean isCache) {
        Gson gson = new Gson();
        CommunalUserInfoEntity minePageData = gson.fromJson(result, CommunalUserInfoEntity.class);
        communalUserInfoBean = minePageData.getCommunalUserInfoBean();
        if (communalUserInfoBean != null) {
            if (minePageData.getCode().equals("01")) {
                ll_mime_no_login.setVisibility(View.GONE);
                ll_mine_login.setVisibility(View.VISIBLE);
                setData(communalUserInfoBean);
            } else {
                initLoggedView();
                if (!isCache) {
                    showToast(getActivity(), minePageData.getMsg());
                }
            }
        }
    }

    public void skipLoginPage() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), MineLoginActivity.class);
        startActivityForResult(intent, LOGIN_REQ_CODE);
    }

    // 个人资料设置 -》头像设置
    @OnClick({R.id.iv_mine_header})
    void skipPersonal(View view) {
        Intent intent = new Intent(getActivity(), PersonalDataActivity.class);
        startActivity(intent);
    }

    // 2016/8/18 跳转粉丝页
    @OnClick({R.id.rel_mine_fans, R.id.tv_mine_fans_count, R.id.tv_mine_fans})
    void skipFans(View view) {
        if (userId != 0) {
            Intent intent = new Intent(getActivity(), UserFansAttentionActivity.class);
            intent.putExtra("type", "fans");
            intent.putExtra("fromPage", "mine");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            skipLoginPage();
        }
    }

    //  2016/8/18 跳转关注页
    @OnClick({R.id.rel_mine_att, R.id.tv_mine_att_count, R.id.tv_mine_att})
    void skipAttention(View view) {
        if (userId != 0) {
            Intent intent = new Intent(getActivity(), UserFansAttentionActivity.class);
            intent.putExtra("type", "attention");
            intent.putExtra("fromPage", "mine");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            skipLoginPage();
        }
    }

    //    我的帖子
    @OnClick({R.id.rel_mine_inv, R.id.tv_mine_inv_count, R.id.tv_mine_inv})
    void skipInvitation(View view) {
        Intent intent = new Intent(getActivity(), MineInvitationListActivity.class);
        startActivity(intent);
    }

    //    全部订单
    @OnClick({R.id.tv_mine_all_indent})
    void skipAllIndent(View view) {
        if (userId > 0) {
            Intent intent = new Intent();
            intent.setClass(getActivity(), DoMoIndentAllActivity.class);
            intent.putExtra("tab", "all");
            startActivity(intent);
        } else {
            skipLoginPage();
        }
    }

    //  资料设置
    @OnClick({R.id.iv_user_back})
    void setUserData(View view) {
        Intent intent = new Intent(getActivity(), AppDataActivity.class);
        startActivity(intent);
    }

    //    跳转消息
    @OnClick(R.id.iv_mine_mes)
    void skipMes(View view) {
        Intent intent = new Intent(getActivity(), MessageActivity.class);
        startActivity(intent);
    }

    //    跳转登录
    @OnClick(R.id.tv_no_login_show)
    void skipLogin(View view) {
        Intent intent = new Intent(getActivity(), MineLoginActivity.class);
        startActivityForResult(intent, ConstantVariable.IS_LOGIN_CODE);
    }

    //    跳转签到
    @OnClick(R.id.rel_integral_more)
    void skipAtt(View view) {
        Intent intent = new Intent(getActivity(), AttendanceActivity.class);
        startActivity(intent);
    }

    //    登录点击背景
    @OnClick({R.id.fl_mine_info, R.id.ll_mime_no_login})
    void changeBg(View view) {
        if (userId > 0 && communalUserInfoBean != null) {
            Intent intent = new Intent(getActivity(), PersonalBgImgActivity.class);
            intent.putExtra("imgUrl", getStrings(communalUserInfoBean.getBgimg_url()));
            startActivity(intent);
        }
    }

    @OnClick(R.id.rel_personal_data_sup)
    void personalSupply(View view) {
        if (communalUserInfoBean != null && communalUserInfoBean.getNoticeInfo() != null
                && !TextUtils.isEmpty(communalUserInfoBean.getNoticeInfo().getAndroid_link())) {
            setSkipPath(getActivity(), communalUserInfoBean.getNoticeInfo().getAndroid_link(), false);
        }
    }


    private void skipNewTaoBao() {
        /**
         * 打开电商组件, 使用默认的webview打开
         *
         * @param activity             必填
         * @param tradePage            页面类型,必填，不可为null，详情见下面tradePage类型介绍
         * @param showParams           show参数
         * @param taokeParams          淘客参数
         * @param trackParam           yhhpass参数
         * @param tradeProcessCallback 交易流程的回调，必填，不允许为null；
         * @return 0标识跳转到手淘打开了, 1标识用h5打开,-1标识出错
         */
        AlibcLogin alibcLogin = AlibcLogin.getInstance();
        alibcLogin.showLogin(new AlibcLoginCallback() {
            @Override
            public void onSuccess(int i) {
//                showToast(getActivity(), "登录成功");
                skipNewMyIndent();
            }

            @Override
            public void onFailure(int code, String msg) {
                showToast(getActivity(), "登录失败 ");
            }
        });
    }

    private void skipNewMyIndent() {
        //提供给三方传递配置参数
        final Map<String, String> exParams = new HashMap<>();
        exParams.put(AlibcConstants.ISV_CODE, "appisvcode");
        //设置页面打开方式
        final AlibcShowParams showParams = new AlibcShowParams(OpenType.Native, false);
//        final AlibcShowParams showParams = new AlibcShowParams(OpenType.Native, false);
        //实例化我的订单打开page
        final AlibcBasePage ordersPage = new AlibcMyOrdersPage(0, true);
        AlibcTrade.show(getActivity(), ordersPage, showParams, null, exParams, new AlibcTradeCallback() {
            @Override
            public void onTradeSuccess(AlibcTradeResult alibcTradeResult) {
                //打开电商组件，用户操作中成功信息回调。tradeResult：成功信息（结果类型：加购，支付；支付结果）
                showToast(getActivity(), "获取订单成功");
            }

            @Override
            public void onFailure(int code, String msg) {
                //打开电商组件，用户操作中错误信息回调。code：错误码；msg：错误信息
                showToast(getActivity(), msg);
            }
        });
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (START_AUTO_PAGE_TURN.equals(message.type)) {
            if (adBeanList.size() > 0 && ad_mine != null && !ad_mine.isTurning()) {
                ad_mine.setCanScroll(true);
                ad_mine.startTurning(getShowNumber(adBeanList.get(0).getShowTime()) * 1000);
                ad_mine.setPointViewVisible(true);
            }
        } else if (STOP_AUTO_PAGE_TURN.equals(message.type)) {
            if (ad_mine != null && ad_mine.isTurning()) {
                ad_mine.setCanScroll(false);
                ad_mine.stopTurning();
                ad_mine.setPointViewVisible(false);
            }
        }
    }
}
