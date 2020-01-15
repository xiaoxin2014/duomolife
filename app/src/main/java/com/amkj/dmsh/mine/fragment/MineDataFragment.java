package com.amkj.dmsh.mine.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.text.emoji.widget.EmojiTextView;
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
import com.alibaba.baichuan.android.trade.AlibcTradeSDK;
import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.bean.CommunalUserInfoEntity;
import com.amkj.dmsh.bean.CommunalUserInfoEntity.CommunalUserInfoBean;
import com.amkj.dmsh.bean.MessageBean;
import com.amkj.dmsh.bean.QualityTypeEntity.QualityTypeBean;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.CommunalAdHolderView;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.dao.UserDao;
import com.amkj.dmsh.homepage.activity.AttendanceActivity;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity.CommunalADActivityBean;
import com.amkj.dmsh.message.activity.MessageActivity;
import com.amkj.dmsh.mine.activity.MineLoginActivity;
import com.amkj.dmsh.mine.activity.MyPostActivity;
import com.amkj.dmsh.mine.activity.PersonalBgImgActivity;
import com.amkj.dmsh.mine.activity.PersonalDataActivity;
import com.amkj.dmsh.mine.adapter.MineTypeAdapter;
import com.amkj.dmsh.mine.bean.MineTypeEntity;
import com.amkj.dmsh.mine.bean.MineTypeEntity.MineTypeBean;
import com.amkj.dmsh.mine.bean.OtherAccountBindEntity.OtherAccountBindInfo;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.network.NetCacheLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.qyservice.QyServiceUtils;
import com.amkj.dmsh.rxeasyhttp.cache.model.CacheResult;
import com.amkj.dmsh.shopdetails.activity.DirectGoodsSaleAfterActivity;
import com.amkj.dmsh.shopdetails.activity.DoMoIndentAllActivity;
import com.amkj.dmsh.shopdetails.adapter.IndentTypeAdapter;
import com.amkj.dmsh.shopdetails.bean.DirectIndentCountEntity;
import com.amkj.dmsh.shopdetails.bean.DirectIndentCountEntity.DirectIndentCountBean;
import com.amkj.dmsh.user.activity.UserFansAttentionActivity;
import com.amkj.dmsh.utils.ImageConverterUtils;
import com.amkj.dmsh.utils.SharedPreUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.qiyukf.unicorn.api.UnreadCountChangeListener;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import q.rorbin.badgeview.Badge;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.amkj.dmsh.constant.CommunalSavePutValueVariable.MINE_BOTTOM_TYPE;
import static com.amkj.dmsh.constant.ConstantMethod.getMessageCount;
import static com.amkj.dmsh.constant.ConstantMethod.getShowNumber;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getTopBadge;
import static com.amkj.dmsh.constant.ConstantMethod.setDeviceInfo;
import static com.amkj.dmsh.constant.ConstantMethod.setSkipPath;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.BASE_RESOURCE_DRAW;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.OTHER_WECHAT;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOKEN;
import static com.amkj.dmsh.constant.ConstantVariable.TOKEN_EXPIRE_LOG_OUT;
import static com.amkj.dmsh.constant.ConstantVariable.TOKEN_EXPIRE_TIME;
import static com.amkj.dmsh.constant.Url.MINE_BIND_ACCOUNT;
import static com.amkj.dmsh.constant.Url.MINE_BOTTOM_DATA;
import static com.amkj.dmsh.constant.Url.MINE_PAGE;
import static com.amkj.dmsh.constant.Url.MINE_PAGE_AD;
import static com.amkj.dmsh.constant.Url.Q_QUERY_CAR_COUNT;
import static com.amkj.dmsh.constant.Url.Q_QUERY_INDENT_COUNT;
import static com.amkj.dmsh.dao.UserDao.getPersonalInfo;
import static com.umeng.socialize.bean.SHARE_MEDIA.WEIXIN;

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
    RelativeLayout ll_mine_login;
    //头像
    @BindView(R.id.iv_mine_header)
    ImageView iv_mine_header;
    //名字
    @BindView(R.id.tv_mine_name)
    EmojiTextView tv_mine_name;
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
    @BindView(R.id.fl_msg)
    public FrameLayout fl_msg;
    //    头部背景
    @BindView(R.id.fl_mine_bg)
    public FrameLayout fl_mine_bg;
    //    订单模块
    @BindView(R.id.rv_mine_indent_item)
    public RecyclerView rv_mine_indent;
    //    个人信息布局
    @BindView(R.id.rel_mine_info)
    public RelativeLayout rel_mine_info;
    //    我的模块
    @BindView(R.id.communal_recycler_wrap)
    public RecyclerView communal_recycler_wrap;
    @BindView(R.id.ad_mine)
    public ConvenientBanner ad_mine;
    @BindView(R.id.tv_personal_data_sup)
    public TextView tv_personal_data_sup;
    @BindView(R.id.rel_personal_data_sup)
    public RelativeLayout rel_personal_data_sup;
    @BindView(R.id.tv_mine_get_score_more)
    public TextView tv_mine_get_score_more;
    @BindView(R.id.tv_bind_phone)
    TextView mTvBindPhone;
    private CommunalUserInfoBean communalUserInfoBean;
    private MineTypeAdapter typeMineAdapter;
    private List<MineTypeBean> mineTypeList = new ArrayList<>();
    private final String[] typeMineName = {"购物车", "优惠券", "分享赚", "积分订单", "秒杀提醒", "收藏商品", "收藏内容", "客服", "设置"};
    private final String[] typeMineUrl = {"app://ShopCarActivity", "app://DirectMyCouponActivity", MAKE_SHARE_URL, "app://IntegralProductIndentActivity",
            "app://ShopTimeMyWarmActivity", "app://MineCollectProductActivity",
            "app://MineCollectContentActivity", "app://ManagerServiceChat", "app://AppDataActivity"};
    private final String[] typeMinePic = {"m_s_car_icon", "m_coupon_icon", "m_share_icon", "m_i_integ_icon", "m_warm_icon", "m_c_pro_icon", "m_c_content_icon", "m_service_icon", "m_setting_icon"};
    //    订单模块
    private IndentTypeAdapter indentTypeAdapter;
    private List<QualityTypeBean> indentTypeList = new ArrayList<>();
    private List<CommunalADActivityBean> adBeanList = new ArrayList<>();
    private final String[] typeIndentName = {"待付款", "待发货", "待收货", "待点评", "退货/售后"};
    private final String[] typeIndentPic = {"i_w_pay_icon", "i_w_send_icon", "i_w_appraise_icon", "i_w_evaluate_icon", "i_s_af_icon"};
    //    跳转登录请求码
    private QualityTypeBean qualityTypeBean;
    private String packageName = "";
    private CBViewHolderCreator cbViewHolderCreator;
    private QyServiceUtils qyInstance;
    private SharedPreferences mineTypeShared;
    private MineTypeEntity mineTypeEntity;
    private DirectIndentCountEntity mDirectIndentCountEntity;
    private Badge badgeMsg;
    private boolean isFirst = true;

    @Override
    protected int getContentView() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initViews() {
        badgeMsg = getTopBadge(getActivity(), fl_msg);
        tv_mine_get_score_more.getPaint().setAntiAlias(true);//抗锯齿
        tv_mine_get_score_more.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        mTvBindPhone.getPaint().setAntiAlias(true);
        mTvBindPhone.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 4);
        communal_recycler_wrap.setLayoutManager(manager);
        communal_recycler_wrap.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_gray_f_two_px).create());
        communal_recycler_wrap.setNestedScrollingEnabled(false);
        mineTypeList = new ArrayList<>();
        MineTypeEntity bottomLocalData = getBottomLocalData();
        if (bottomLocalData != null && bottomLocalData.getMineTypeBeanList() != null
                && bottomLocalData.getMineTypeBeanList().size() > 0) {
            mineTypeList.addAll(bottomLocalData.getMineTypeBeanList());
        } else {
            if (getActivity() == null) {
                packageName = "com.amkj.dmsh";
            } else {
                packageName = getActivity().getPackageName();
            }
            for (int i = 0; i < typeMineName.length; i++) {
                MineTypeBean mineTypeBean = new MineTypeBean();
                mineTypeBean.setName(typeMineName[i]);
                mineTypeBean.setIconUrl("android.resource://" + packageName + "/drawable/" + typeMinePic[i]);
                mineTypeBean.setAndroidUrl(typeMineUrl[i]);
                mineTypeList.add(mineTypeBean);
            }
        }
        typeMineAdapter = new MineTypeAdapter(getActivity(), mineTypeList);
        communal_recycler_wrap.setAdapter(typeMineAdapter);
        typeMineAdapter.setOnItemClickListener((adapter, view, position) -> {
            MineTypeBean mineTypeBean = (MineTypeBean) view.getTag();
            if (mineTypeBean != null) {
                if (mineTypeBean.getAndroidUrl().equals("app://AppDataActivity") && communalUserInfoBean != null) {
                    String mobile = communalUserInfoBean.getMobile();
                    setSkipPath(getActivity(), "app://AppDataActivity?mobile=" + mobile, false);
                } else {
                    setSkipPath(getActivity(), mineTypeBean.getAndroidUrl(), false);
                }
            }
        });

//        我的订单模块
        indentTypeList = new ArrayList<>();
        for (int i = 0; i < typeIndentName.length; i++) {
            qualityTypeBean = new QualityTypeBean();
            qualityTypeBean.setName(typeIndentName[i]);
            qualityTypeBean.setPicUrl(typeIndentPic[i]);
            qualityTypeBean.setId(i);
            indentTypeList.add(qualityTypeBean);
        }
        rv_mine_indent.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        indentTypeAdapter = new IndentTypeAdapter(getActivity(), indentTypeList);
        rv_mine_indent.setAdapter(indentTypeAdapter);
        rv_mine_indent.setNestedScrollingEnabled(false);
        indentTypeAdapter.setOnItemClickListener((adapter, view, position) -> {
            QualityTypeBean qualityTypeBean = (QualityTypeBean) view.getTag();
            if (qualityTypeBean != null) {
                Intent intent = new Intent();
                switch (qualityTypeBean.getId()) {
                    case 0: //待付款
                        intent.setClass(getActivity(), DoMoIndentAllActivity.class);
                        intent.putExtra("tab", "waitPay");
                        startActivity(intent);
                        break;
                    case 1: //待发货
                        intent.setClass(getActivity(), DoMoIndentAllActivity.class);
                        intent.putExtra("tab", "waitSend");
                        startActivity(intent);
                        break;
                    case 2: //待收货
                        intent.setClass(getActivity(), DoMoIndentAllActivity.class);
                        intent.putExtra("tab", "delivered");
                        startActivity(intent);
                        break;
                    case 3: //待评价
                        intent.setClass(getActivity(), DoMoIndentAllActivity.class);
                        intent.putExtra("tab", "appraise");
                        startActivity(intent);
                        break;
                    case 4://退货/售后
                        intent.setClass(getActivity(), DirectGoodsSaleAfterActivity.class);
                        intent.putExtra("type", "goodsReturn");
                        startActivity(intent);
                        break;
                }
            }
        });
        setQyService();
        ad_mine.setVisibility(View.GONE);
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
     *
     * @param serviceTotalCount 客服未读消息条数
     */
    private void setServiceUnread(int serviceTotalCount) {
        for (int i = 0; i < mineTypeList.size(); i++) {
            MineTypeBean mineTypeBean = mineTypeList.get(i);
            String androidLink = getStrings(mineTypeBean.getAndroidUrl());
            if (androidLink.contains("ManagerServiceChat")) {
                if (serviceTotalCount != mineTypeBean.getMesCount()) {
                    mineTypeBean.setMesCount(serviceTotalCount);
                    typeMineAdapter.notifyDataSetChanged();
                }
                break;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //刷新我的界面
        getLoginStatus();
        getMineAd();
        if (!isFirst) {
            getMessageCount(getActivity(), badgeMsg);
        }
        isFirst = false;
    }


    private void getLoginStatus() {
        SavePersonalInfoBean personalInfo = getPersonalInfo(getActivity());
        if (personalInfo.isLogin()) {
            getNetDataInfo();
        } else {
            try {
                setErrorUserData();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 个人数据
     *
     * @param communalUserInfoBean 用户信息
     */
    private void setData(final CommunalUserInfoBean communalUserInfoBean) {
        tv_mine_name.setText(getStrings(communalUserInfoBean.getNickname()));
        tv_mine_att_count.setText(String.valueOf(communalUserInfoBean.getFllow()));
        tv_mine_fans_count.setText(String.valueOf(communalUserInfoBean.getFans()));
        tv_mine_inv_count.setText(String.valueOf(communalUserInfoBean.getDocumentcount()));
        tv_mine_score.setText(String.valueOf(communalUserInfoBean.getScore()));
        boolean bindingWx = communalUserInfoBean.isBindingWx();
        if (!bindingWx) {//没有绑定微信
            mTvBindPhone.setText("关联微信账号");
            mTvBindPhone.setVisibility(View.VISIBLE);
        } else if (TextUtils.isEmpty(communalUserInfoBean.getMobile())) {//没有绑定手机号
            mTvBindPhone.setText("关联手机账号");
            mTvBindPhone.setVisibility(View.VISIBLE);
        } else {
            mTvBindPhone.setVisibility(View.GONE);
        }
        GlideImageLoaderUtil.loadHeaderImg(getActivity(), iv_mine_header, !TextUtils.isEmpty(communalUserInfoBean.getAvatar())
                ? ImageConverterUtils.getFormatImg(communalUserInfoBean.getAvatar()) : "");
        GlideImageLoaderUtil.loadImage(getActivity(), iv_mine_page_bg, !TextUtils.isEmpty(communalUserInfoBean.getBgimg_url())
                ? ImageConverterUtils.getFormatImg(communalUserInfoBean.getBgimg_url()) : BASE_RESOURCE_DRAW + R.drawable.mine_no_login_bg);
        setBottomTypeCount(communalUserInfoBean);
        CommunalUserInfoBean.NoticeInfoBean noticeInfo = communalUserInfoBean.getNoticeInfo();
        if (noticeInfo != null && !TextUtils.isEmpty(noticeInfo.getContent())) {
            rel_personal_data_sup.setVisibility(View.VISIBLE);
            tv_personal_data_sup.setText(getStrings(noticeInfo.getContent()));
        } else {
            rel_personal_data_sup.setVisibility(View.GONE);
        }
    }

    /**
     * 设置底栏购物车 优惠券 积分订单数量
     *
     * @param userData
     */
    private void setBottomTypeCount(CommunalUserInfoBean userData) {
        if (userData != null) {
            for (int i = 0; i < mineTypeList.size(); i++) {
                MineTypeBean mineTypeBean = mineTypeList.get(i);
                String androidLink = getStrings(mineTypeBean.getAndroidUrl());
                if (androidLink.contains("ShopCarActivity")) {
                    mineTypeBean.setGetCartTip(false);
                } else if (androidLink.contains("DirectMyCouponActivity")) {
                    mineTypeBean.setMesCount(userData.getCouponTotal());
                } else if (androidLink.contains("IntegralProductIndentActivity")) {
                    mineTypeBean.setMesCount(userData.getJfTotal());
                } else {
                    mineTypeBean.setMesCount(0);
                }
            }
        } else {
            for (int i = 0; i < mineTypeList.size(); i++) {
                MineTypeBean mineTypeBean = mineTypeList.get(i);
                mineTypeBean.setMesCount(0);
            }
        }
        typeMineAdapter.notifyDataSetChanged();
        getCarCount(getActivity());
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
        getBottomTypeNetData();
        //获取消息数量
        getMessageCount(getActivity(), badgeMsg);
    }


    /**
     * 获取订单数量显示
     */
    private void getDoMeIndentDataCount() {
        if (userId < 1) {
            setCountData(null);
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Q_QUERY_INDENT_COUNT, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                mDirectIndentCountEntity = gson.fromJson(result, DirectIndentCountEntity.class);
                if (mDirectIndentCountEntity != null) {
                    if (mDirectIndentCountEntity.getCode().equals(SUCCESS_CODE)) {
                        setCountData(mDirectIndentCountEntity.getDirectIndentCountBean());
                    }
                }
            }
        });
    }

    /**
     * 获取底部数据
     */
    private void getBottomTypeNetData() {
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), MINE_BOTTOM_DATA, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                MineTypeEntity mineTypeEntity = new Gson().fromJson(result, MineTypeEntity.class);
                if (mineTypeEntity != null && SUCCESS_CODE.equals(mineTypeEntity.getCode())) {
                    MineTypeEntity bottomLocalData = getBottomLocalData();
                    if (bottomLocalData == null ||
                            !getStrings(bottomLocalData.getUpdateTime()).equals(getStrings(mineTypeEntity.getUpdateTime()))) {
                        saveBottomLocalData(result);
                    }
                } else {
//                    清除底部宫格数据
                    if (getBottomShared() != null) {
                        getBottomShared().edit().clear().apply();
                    }
                }
            }
        });
    }

    private void setCountData(DirectIndentCountBean messageTotalBean) {
        if (messageTotalBean != null) {
            //待付款
            indentTypeList.get(0).setType(messageTotalBean.getWaitPayNum());
            //待发货
            indentTypeList.get(1).setType(messageTotalBean.getWaitDeliveryNum());
            //待收货
            indentTypeList.get(2).setType(messageTotalBean.getWaitTakeDeliveryNum());
            //待评价
            indentTypeList.get(3).setType(messageTotalBean.getWaitEvaluateNum());
            //退货售后
            indentTypeList.get(4).setType(messageTotalBean.getWaitAfterSaleNum());
        } else {
            for (int i = 0; i < indentTypeList.size(); i++) {
                QualityTypeBean qualityTypeBean = indentTypeList.get(i);
                qualityTypeBean.setType(0);
            }
        }
        indentTypeAdapter.notifyDataSetChanged();
    }

    /**
     * 请求用户数据
     */
    private void getNetDataInfo() {
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("uid", String.valueOf(userId));
        NetLoadUtils.getNetInstance().loadNetDataGetCache(getActivity(), MINE_PAGE
                , params, false, new NetCacheLoadListenerHelper() {
                    @Override
                    public void onSuccessCacheResult(CacheResult<String> cacheResult) {
                        if (cacheResult == null || TextUtils.isEmpty(cacheResult.data)) {
                            return;
                        }
                        Gson gson = new Gson();
                        CommunalUserInfoEntity minePageData = gson.fromJson(cacheResult.data, CommunalUserInfoEntity.class);
                        communalUserInfoBean = minePageData.getCommunalUserInfoBean();
                        if (communalUserInfoBean != null) {
                            if (minePageData.getCode().equals(SUCCESS_CODE)) {
                                ll_mime_no_login.setVisibility(View.GONE);
                                ll_mine_login.setVisibility(View.VISIBLE);
                                setData(communalUserInfoBean);
                                getDoMeIndentDataCount();
                                setDeviceInfo(getActivity(), communalUserInfoBean.getApp_version_no()
                                        , communalUserInfoBean.getDevice_model()
                                        , communalUserInfoBean.getDevice_sys_version(), communalUserInfoBean.getSysNotice());
                            } else {
                                setErrorUserData();
                                showToast(getActivity(), minePageData.getMsg());
                            }
                        } else {
                            setErrorUserData();
                            showToast(getActivity(), minePageData.getMsg());
                        }
                    }

                    @Override
                    public void onNotNetOrException() {
                        setErrorUserData();
                    }
                });
    }

    private void setErrorUserData() {
        initLoggedView();
        setBottomTypeCount(null);
        getDoMeIndentDataCount();
    }

    //    我的模块 广告
    private void getMineAd() {
        Map<String, Object> params = new HashMap<>();
        params.put("vidoShow", "1");
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), MINE_PAGE_AD, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                adBeanList.clear();
                CommunalADActivityEntity adActivityEntity = gson.fromJson(result, CommunalADActivityEntity.class);
                if (adActivityEntity != null) {
                    if (adActivityEntity.getCode().equals(SUCCESS_CODE)) {
                        setMineAdData(adActivityEntity);
                    } else {
                        ad_mine.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                ad_mine.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 设置我-广告
     *
     * @param adActivityEntity
     */
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
        ad_mine.setPages(getActivity(), cbViewHolderCreator, adBeanList).setCanLoop(true).setPointViewVisible(true)
                .setPageIndicator(new int[]{R.drawable.unselected_radius, R.drawable.selected_radius})
                .startTurning(getShowNumber(adBeanList.get(0).getShowTime()) * 1000);
    }

    /**
     * 初始化用户数据
     */
    private void initLoggedView() {
        if (ll_mine_login == null) {
            return;
        }
        ll_mine_login.setVisibility(View.GONE);
        ll_mime_no_login.setVisibility(View.VISIBLE);
        rel_personal_data_sup.setVisibility(View.GONE);
        GlideImageLoaderUtil.loadCenterCrop(getActivity(), iv_mine_page_bg, BASE_RESOURCE_DRAW + R.drawable.mine_no_login_bg);
    }

    public void skipLoginPage() {
        ConstantMethod.getLoginStatus(this);
    }

    // 个人资料设置 -》头像设置
    @OnClick({R.id.iv_mine_header})
    void skipPersonal(View view) {
        Intent intent = new Intent(getActivity(), PersonalDataActivity.class);
        startActivity(intent);
    }

    // 2016/8/18 跳转粉丝页
    @OnClick(R.id.ll_mine_fans_count)
    void skipFans(View view) {
        if (userId != 0) {
            Intent intent = new Intent(getActivity(), UserFansAttentionActivity.class);
            intent.putExtra("type", "fans");
            intent.putExtra("fromPage", "mine");
            intent.putExtra("userId", userId);
            startActivity(intent);
        } else {
            skipLoginPage();
        }
    }

    //  2016/8/18 跳转关注页
    @OnClick(R.id.ll_mine_att_count)
    void skipAttention(View view) {
        if (userId != 0) {
            Intent intent = new Intent(getActivity(), UserFansAttentionActivity.class);
            intent.putExtra("type", "attention");
            intent.putExtra("fromPage", "mine");
            intent.putExtra("userId", userId);
            startActivity(intent);
        } else {
            skipLoginPage();
        }
    }

    //    我的帖子
    @OnClick(R.id.ll_mine_inv_count)
    void skipInvitation(View view) {
        Intent intent = new Intent(getActivity(), MyPostActivity.class);
        startActivity(intent);
    }

    //    全部订单
    @OnClick({R.id.tv_mine_all_indent})
    void skipAllIndent(View view) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), DoMoIndentAllActivity.class);
        intent.putExtra("tab", "all");
        if (mDirectIndentCountEntity != null && mDirectIndentCountEntity.getDirectIndentCountBean() != null) {
            intent.putExtra("waitEvaluateNum", mDirectIndentCountEntity.getDirectIndentCountBean().getWaitEvaluateNum());
        }
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
        startActivityForResult(intent, IS_LOGIN_CODE);
    }


    //    登录点击背景
    @OnClick({R.id.rel_mine_info, R.id.ll_mime_no_login})
    void changeBg(View view) {
        if (userId > 0 && communalUserInfoBean != null) {
            Intent intent = new Intent(getActivity(), PersonalBgImgActivity.class);
            intent.putExtra("imgUrl", getStrings(communalUserInfoBean.getBgimg_url()));
            startActivity(intent);
        }
    }

    //    跳转签到
    @OnClick(R.id.rel_integral_more)
    void skipAtt(View view) {
        Intent intent = new Intent(getActivity(), AttendanceActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_personal_data_sup)
    void personalSupply(View view) {
        if (communalUserInfoBean != null && communalUserInfoBean.getNoticeInfo() != null
                && !TextUtils.isEmpty(communalUserInfoBean.getNoticeInfo().getAndroid_link())) {
            setSkipPath(getActivity(), communalUserInfoBean.getNoticeInfo().getAndroid_link(), false);
        }
    }

    //微信登录绑定手机号或者绑定微信
    @OnClick(R.id.tv_bind_phone)
    void bindPhone(View view) {
        if (userId > 0) {
            if (communalUserInfoBean != null) {
                boolean bindingWx = communalUserInfoBean.isBindingWx();
                if (!bindingWx) {
                    //绑定微信
                    UMShareConfig config = new UMShareConfig();
                    config.isNeedAuthOnGetUserInfo(true);
                    UMShareAPI.get(getActivity()).setShareConfig(config);
                    // 打开微信授权
                    UMShareAPI.get(getActivity()).getPlatformInfo(getActivity(), WEIXIN, getDataInfoListener);
                } else {
                    UserDao.bindPhoneByWx(getActivity());
                }
            }

        } else {
            getLoginStatus();
        }
    }

    UMAuthListener getDataInfoListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            if (loadHud != null) {
                loadHud.show();
            }
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            loadHud.dismiss();
            if (data != null) {
                if (WEIXIN == platform) {
                    final OtherAccountBindInfo info = new OtherAccountBindInfo();
                    info.setOpenid(data.get("openid"));
                    info.setUnionId(getStrings(data.get("uid")));
                    info.setType(OTHER_WECHAT);
                    info.setNickname(data.get("name"));
                    info.setAvatar(!TextUtils.isEmpty(data.get("iconurl")) ? data.get("iconurl") : "");
                    bindOtherAccount(info);
                }
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            showToast(getActivity(), "授权失败");
            if (loadHud != null) {
                loadHud.dismiss();
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            showToast(getActivity(), "授权取消");
            if (loadHud != null) {
                loadHud.dismiss();
            }
        }
    };

    /**
     * 绑定第三方账号
     *
     * @param otherAccountBindInfo
     */
    private void bindOtherAccount(OtherAccountBindInfo otherAccountBindInfo) {
        Map<String, Object> params = new HashMap<>();
        params.put("openid", otherAccountBindInfo.getOpenid());
        params.put("type", otherAccountBindInfo.getType());
        if (OTHER_WECHAT.equals(otherAccountBindInfo.getType())) {
            params.put("unionid", otherAccountBindInfo.getUnionId());
        }
        params.put("nickname", otherAccountBindInfo.getNickname());
        params.put("avatar", otherAccountBindInfo.getAvatar());
        params.put("id", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), MINE_BIND_ACCOUNT, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        showToast(getActivity(), requestStatus.getMsg());
                        //更新本地Token信息
                        if (!TextUtils.isEmpty(requestStatus.getToken())) {
                            SharedPreUtils.setParam(TOKEN, getStrings(requestStatus.getToken()));
                            SharedPreUtils.setParam(TOKEN_EXPIRE_TIME, System.currentTimeMillis() + requestStatus.getTokenExpireSeconds());
                        }
                        /**
                         * 第三方账号登录统计
                         * 3.2.0 修改为统计自己传入的参数 以前版本根据后台返回的数据类型进行跟uid进行统计
                         */
                        MobclickAgent.onProfileSignIn(getStrings(otherAccountBindInfo.getType()), String.valueOf(userId));
                        mTvBindPhone.setVisibility(View.GONE);
                    } else {
                        showToast(getActivity(), requestStatus.getMsg());
                    }
                }
            }
        });
    }


    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (TOKEN_EXPIRE_LOG_OUT.equals(message.type)) {
            setErrorUserData();
        }
    }

    /**
     * 获取底部轻量级存储对象
     *
     * @return
     */
    public SharedPreferences getBottomShared() {
        if (mineTypeShared == null) {
            if (getActivity() == null) {
                return null;
            }
            mineTypeShared = getActivity().getSharedPreferences(MINE_BOTTOM_TYPE, MODE_PRIVATE);
        }
        return mineTypeShared;
    }

    /**
     * 保存底部数据
     *
     * @param typeData
     */
    public void saveBottomLocalData(String typeData) {
        SharedPreferences bottomShared = getBottomShared();
        if (bottomShared != null && !TextUtils.isEmpty(typeData)) {
            SharedPreferences.Editor edit = bottomShared.edit();
            edit.putString(MINE_BOTTOM_TYPE, typeData);
            edit.apply();
        }
    }

    /**
     * 获取底部数据
     */
    public MineTypeEntity getBottomLocalData() {
        try {
            if (mineTypeEntity == null) {
                SharedPreferences bottomShared = getBottomShared();
                if (bottomShared != null) {
                    String typeJson = bottomShared.getString(MINE_BOTTOM_TYPE, "");
                    if (!TextUtils.isEmpty(typeJson)) {
                        mineTypeEntity = new Gson().fromJson(typeJson, MineTypeEntity.class);
                        return mineTypeEntity;
                    }
                }
                return null;
            }
            return mineTypeEntity;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void initImmersionBar() {
        ImmersionBar.with(this).titleBar(rel_header_mine)
                .statusBarDarkFont(true).keyboardEnable(true).navigationBarEnable(false).init();
    }

    @Override
    public boolean immersionBarEnabled() {
        return true;
    }

    @Override
    protected boolean isLazy() {
        return false;
    }

    //更新购物车商品数量
    public void getCarCount(Activity activity) {
        if (userId > 0) {
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            NetLoadUtils.getNetInstance().loadNetDataPost(activity, Q_QUERY_CAR_COUNT, params, new NetLoadListenerHelper() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    MessageBean requestStatus = gson.fromJson(result, MessageBean.class);
                    if (requestStatus != null) {
                        if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                            int cartNumber = requestStatus.getResult();
                            updateShopCartNum(cartNumber);
                        }
                    }
                }
            });
        } else {
            updateShopCartNum(0);
        }
    }

    private void updateShopCartNum(int num) {
        int position = 0;
        if (mineTypeList != null && mineTypeList.size() > 0 && typeMineAdapter != null) {
            for (int i = 0; i < mineTypeList.size(); i++) {
                MineTypeBean mineTypeBean = mineTypeList.get(i);
                String androidLink = getStrings(mineTypeBean.getAndroidUrl());
                if (androidLink.contains("ShopCarActivity")) {
                    mineTypeBean.setGetCartTip(true);
                    mineTypeBean.setMesCount(num);
                    position = i;
                    break;
                }
            }
            typeMineAdapter.notifyItemChanged(position);
        }
    }
}
