package com.amkj.dmsh.mine.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
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
import com.amkj.dmsh.bean.QualityTypeEntity.QualityTypeBean;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.CommunalAdHolderView;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dao.AddClickDao;
import com.amkj.dmsh.dao.UserDao;
import com.amkj.dmsh.homepage.activity.AttendanceActivity;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity.CommunalADActivityBean;
import com.amkj.dmsh.message.activity.MessageActivity;
import com.amkj.dmsh.mine.activity.DomolifeVipActivity;
import com.amkj.dmsh.mine.activity.MineLoginActivity;
import com.amkj.dmsh.mine.activity.MyPostActivity;
import com.amkj.dmsh.mine.activity.OpenVipActivity;
import com.amkj.dmsh.mine.activity.PersonalBgImgActivity;
import com.amkj.dmsh.mine.activity.PersonalDataActivity;
import com.amkj.dmsh.mine.adapter.MineTypeAdapter;
import com.amkj.dmsh.mine.bean.MineTypeEntity;
import com.amkj.dmsh.mine.bean.MineTypeEntity.MineTypeBean;
import com.amkj.dmsh.mine.bean.OtherAccountBindEntity.OtherAccountBindInfo;
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
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.amkj.dmsh.views.convenientbanner.ConvenientBanner;
import com.amkj.dmsh.views.convenientbanner.holder.CBViewHolderCreator;
import com.amkj.dmsh.views.convenientbanner.holder.Holder;
import com.gyf.barlibrary.ImmersionBar;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.emoji.widget.EmojiTextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;
import q.rorbin.badgeview.Badge;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.CommunalSavePutValueVariable.MINE_BOTTOM_TYPE;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getMessageCount;
import static com.amkj.dmsh.constant.ConstantMethod.getShowNumber;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsFormat;
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
import static com.amkj.dmsh.constant.Url.Q_QUERY_INDENT_COUNT;
import static com.amkj.dmsh.dao.OrderDao.getCarCount;
import static com.umeng.socialize.bean.SHARE_MEDIA.WEIXIN;

/**
 * Created by atd48 on 2016/8/17.
 * ??????-??????
 */
public class MineDataFragment extends BaseFragment {
    private static final String MAKE_SHARE_URL = "http://www.domolife.cn/m/template/home/shareHome.html";
    //    ??????
    @BindView(R.id.iv_mine_page_bg)
    ImageView iv_mine_page_bg;
    //    ????????????
    @BindView(R.id.ll_mine_login)
    RelativeLayout ll_mine_login;
    //??????
    @BindView(R.id.iv_mine_header)
    ImageView iv_mine_header;
    //??????
    @BindView(R.id.tv_mine_name)
    EmojiTextView tv_mine_name;
    //?????????
    @BindView(R.id.tv_mine_fans_count)
    TextView tv_mine_fans_count;
    //?????????
    @BindView(R.id.tv_mine_att_count)
    TextView tv_mine_att_count;
    //    ?????????
    @BindView(R.id.tv_mine_inv_count)
    TextView tv_mine_inv_count;
    //????????????
    @BindView(R.id.tv_mine_score)
    TextView tv_mine_score;
    //    ???????????????
    @BindView(R.id.ll_mime_no_login)
    LinearLayout ll_mime_no_login;
    @BindView(R.id.rel_header_title_mine)
    public RelativeLayout rel_header_mine;
    @BindView(R.id.fl_msg)
    public FrameLayout fl_msg;
    //    ????????????
    @BindView(R.id.rv_mine_indent_item)
    public RecyclerView rv_mine_indent;
    //    ??????????????????
    @BindView(R.id.rel_mine_info)
    public RelativeLayout rel_mine_info;
    //    ????????????
    @BindView(R.id.communal_recycler_wrap)
    public RecyclerView communal_recycler_wrap;
    @BindView(R.id.cardview)
    public CardView cardview;
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
    @BindView(R.id.tv_total_save)
    TextView mTvTotalSave;
    @BindView(R.id.tv_end_time)
    TextView mTvEndTime;
    @BindView(R.id.tv_renewal)
    TextView mTvRenewal;
    @BindView(R.id.rl_vip_info)
    RelativeLayout mRlVipInfo;
    @BindView(R.id.rl_no_vip_info)
    RelativeLayout mRlNoVipInfo;
    @BindView(R.id.fl_mine_bg)
    FrameLayout mFlMineBg;
    private CommunalUserInfoBean communalUserInfoBean;
    private MineTypeAdapter typeMineAdapter;
    private List<MineTypeBean> mineTypeList = new ArrayList<>();
    private final String[] typeMineName = {"?????????", "?????????", "?????????", "????????????", "????????????", "????????????", "????????????", "??????", "??????"};
    private final String[] typeMineUrl = {"app://ShopCarActivity", "app://DirectMyCouponActivity", MAKE_SHARE_URL, "app://IntegralProductIndentActivity",
            "app://ShopTimeMyWarmActivity", "app://MineCollectProductActivity",
            "app://MineCollectContentActivity", "app://ManagerServiceChat", "app://AppDataActivity"};
    private final String[] typeMinePic = {"m_s_car_icon", "m_coupon_icon", "m_share_icon", "m_i_integ_icon", "m_warm_icon", "m_c_pro_icon", "m_c_content_icon", "m_service_icon", "m_setting_icon"};
    //    ????????????
    private IndentTypeAdapter indentTypeAdapter;
    private List<QualityTypeBean> indentTypeList = new ArrayList<>();
    private List<CommunalADActivityBean> adBeanList = new ArrayList<>();
    private final String[] typeIndentName = {"?????????", "?????????", "?????????", "?????????", "??????/??????"};
    private final String[] typeIndentPic = {"i_w_pay_icon", "i_w_send_icon", "i_w_appraise_icon", "i_w_evaluate_icon", "i_s_af_icon"};
    //    ?????????????????????
    private QualityTypeBean qualityTypeBean;
    private CBViewHolderCreator cbViewHolderCreator;
    private QyServiceUtils qyInstance;
    private SharedPreferences mineTypeShared;
    private MineTypeEntity mineTypeEntity;
    private DirectIndentCountEntity mDirectIndentCountEntity;
    private Badge badgeMsg;
    private RequestStatus.Result mVipInfoBean;
    private boolean isFirst = true;

    @Override
    protected int getContentView() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initViews() {
        badgeMsg = getTopBadge(getActivity(), fl_msg);
        tv_mine_get_score_more.getPaint().setAntiAlias(true);//?????????
        tv_mine_get_score_more.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //?????????
        mTvBindPhone.getPaint().setAntiAlias(true);
        mTvBindPhone.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        //???????????????????????????
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
                    case 0: //?????????
                        intent.setClass(getActivity(), DoMoIndentAllActivity.class);
                        intent.putExtra("tab", "waitPay");
                        startActivity(intent);
                        break;
                    case 1: //?????????
                        intent.setClass(getActivity(), DoMoIndentAllActivity.class);
                        intent.putExtra("tab", "waitSend");
                        startActivity(intent);
                        break;
                    case 2: //?????????
                        intent.setClass(getActivity(), DoMoIndentAllActivity.class);
                        intent.putExtra("tab", "delivered");
                        startActivity(intent);
                        break;
                    case 3: //?????????
                        intent.setClass(getActivity(), DoMoIndentAllActivity.class);
                        intent.putExtra("tab", "appraise");
                        startActivity(intent);
                        break;
                    case 4://??????/??????
                        intent.setClass(getActivity(), DirectGoodsSaleAfterActivity.class);
                        intent.putExtra("type", "goodsReturn");
                        startActivity(intent);
                        break;
                }
            }
        });

        //?????????????????????
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 4);
        communal_recycler_wrap.setLayoutManager(manager);
        communal_recycler_wrap.addItemDecoration(new ItemDecoration.Builder()
                // ?????????????????????ID
                .setDividerId(R.drawable.item_divider_gray_f_one_px).create());
        communal_recycler_wrap.setNestedScrollingEnabled(false);
        mineTypeList = new ArrayList<>();
        MineTypeEntity bottomLocalData = getBottomLocalData();
        if (bottomLocalData != null && bottomLocalData.getMineTypeBeanList() != null) {
            mineTypeList.addAll(bottomLocalData.getMineTypeBeanList());
        } else {
            for (int i = 0; i < typeMineName.length; i++) {
                MineTypeBean mineTypeBean = new MineTypeBean();
                mineTypeBean.setName(typeMineName[i]);
                mineTypeBean.setIconUrl("android.resource://" + "com.amkj.dmsh" + "/drawable/" + typeMinePic[i]);
                mineTypeBean.setAndroidUrl(typeMineUrl[i]);
                mineTypeList.add(mineTypeBean);
            }
        }
        typeMineAdapter = new MineTypeAdapter(getActivity(), mineTypeList);
        communal_recycler_wrap.setAdapter(typeMineAdapter);
        //????????????notifyItemChanged????????????
        SimpleItemAnimator itemAnimator = (SimpleItemAnimator) communal_recycler_wrap.getItemAnimator();
        if (itemAnimator != null) {
            itemAnimator.setSupportsChangeAnimations(false);
        }
        typeMineAdapter.setOnItemClickListener((adapter, view, position) -> {
            MineTypeBean mineTypeBean = (MineTypeBean) view.getTag();
            if (mineTypeBean != null) {
                if ("app://AppDataActivity".equals(mineTypeBean.getAndroidUrl()) && communalUserInfoBean != null) {
                    String mobile = communalUserInfoBean.getMobile();
                    setSkipPath(getActivity(), "app://AppDataActivity?mobile=" + mobile, false);
                } else {
                    setSkipPath(getActivity(), mineTypeBean.getAndroidUrl(), false);
                }
                //????????????????????????
                AddClickDao.addMyDefinedIconClick(getActivity(), mineTypeBean.getId());
            }
        });

        //????????????????????????
        setQyService();
    }

    @Override
    protected void loadData() {
        getBottomTypeNetData();
        getMineData();
    }

    private void getMineData() {
        if (userId > 0) {
            getNetDataInfo();
            getMineAd();
            getDoMeIndentDataCount();
            getMessageCount(getActivity(), badgeMsg);
            getCarCount(getActivity());
        } else {
            setErrorUserData();
        }
    }


    //??????????????????
    private void getNetDataInfo() {
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("uid", String.valueOf(userId));
        NetLoadUtils.getNetInstance().loadNetDataGetCache(getActivity(), MINE_PAGE, params, false, new NetCacheLoadListenerHelper() {
                    @Override
                    public void onSuccessCacheResult(CacheResult<String> cacheResult) {
                        if (cacheResult == null || TextUtils.isEmpty(cacheResult.data)) {
                            return;
                        }
                        CommunalUserInfoEntity minePageData = GsonUtils.fromJson(cacheResult.data, CommunalUserInfoEntity.class);
                        communalUserInfoBean = minePageData.getCommunalUserInfoBean();
                        if (communalUserInfoBean != null && minePageData.getCode().equals(SUCCESS_CODE)) {
                            ll_mime_no_login.setVisibility(View.GONE);
                            ll_mine_login.setVisibility(View.VISIBLE);
                            setData(communalUserInfoBean);
                            setDeviceInfo(getActivity(), communalUserInfoBean.getApp_version_no()
                                    , communalUserInfoBean.getDevice_model()
                                    , communalUserInfoBean.getDevice_sys_version(), communalUserInfoBean.getSysNotice());
                        } else {
                            setErrorUserData();
                            showToast(minePageData.getMsg());
                        }
                    }

                    @Override
                    public void onNotNetOrException() {
                        setErrorUserData();
                    }
                });
    }

    //??????????????????
    private void setData(final CommunalUserInfoBean communalUserInfoBean) {
        SharedPreUtils.setParam("isVip", communalUserInfoBean.isVip());
        SharedPreUtils.setParam("vipLevel", communalUserInfoBean.getVipLevel());
        SharedPreUtils.setParam("isWhiteUser", communalUserInfoBean.isWhiteUser());
        ConstantMethod.setIsVip(communalUserInfoBean.isVip());
        //?????????????????????
        if (communalUserInfoBean.isVip()) {
            getVipInfo();
            mRlNoVipInfo.setVisibility(View.GONE);
        } else {
            mRlVipInfo.setVisibility(View.GONE);
            mRlNoVipInfo.setVisibility(communalUserInfoBean.isWhiteUser() ? View.VISIBLE : View.GONE);
            setBgHeight();
        }

        //?????????????????????
        updateBottomNum("DirectMyCouponActivity", communalUserInfoBean.getCouponTotal());
        tv_mine_name.setText(getStrings(communalUserInfoBean.getNickname()));
        tv_mine_att_count.setText(String.valueOf(communalUserInfoBean.getFllow()));
        tv_mine_fans_count.setText(String.valueOf(communalUserInfoBean.getFans()));
        tv_mine_inv_count.setText(String.valueOf(communalUserInfoBean.getDocumentcount()));
        tv_mine_score.setText(String.valueOf(communalUserInfoBean.getScore()));
        boolean bindingWx = communalUserInfoBean.isBindingWx();
        if (!bindingWx) {//??????????????????
            mTvBindPhone.setText("??????????????????");
            mTvBindPhone.setVisibility(View.VISIBLE);
        } else if (TextUtils.isEmpty(communalUserInfoBean.getMobile())) {//?????????????????????
            mTvBindPhone.setText("??????????????????");
            mTvBindPhone.setVisibility(View.VISIBLE);
        } else {
            mTvBindPhone.setVisibility(View.GONE);
        }
        GlideImageLoaderUtil.loadHeaderImg(getActivity(), iv_mine_header, !TextUtils.isEmpty(communalUserInfoBean.getAvatar())
                ? ImageConverterUtils.getFormatImg(communalUserInfoBean.getAvatar()) : "");
        GlideImageLoaderUtil.loadImage(getActivity(), iv_mine_page_bg, !TextUtils.isEmpty(communalUserInfoBean.getBgimg_url())
                ? ImageConverterUtils.getFormatImg(communalUserInfoBean.getBgimg_url()) : BASE_RESOURCE_DRAW + R.drawable.mine_no_login_bg);
        CommunalUserInfoBean.NoticeInfoBean noticeInfo = communalUserInfoBean.getNoticeInfo();
        if (noticeInfo != null && !TextUtils.isEmpty(noticeInfo.getContent())) {
            rel_personal_data_sup.setVisibility(View.VISIBLE);
            tv_personal_data_sup.setText(getStrings(noticeInfo.getContent()));
        } else {
            rel_personal_data_sup.setVisibility(View.GONE);
        }
    }

    private void setBgHeight() {
        ViewGroup.LayoutParams layoutParams = mFlMineBg.getLayoutParams();
        layoutParams.height = AutoSizeUtils.mm2px(mAppContext, mRlNoVipInfo.getVisibility() == View.VISIBLE || mRlVipInfo.getVisibility() == View.VISIBLE ? 453 : 353);
        mFlMineBg.setLayoutParams(layoutParams);
    }

    //??????????????????
    private void getVipInfo() {
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Url.GET_VIP_USER_INFO, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                RequestStatus mVipInfoEntity = GsonUtils.fromJson(result, RequestStatus.class);
                if (mVipInfoEntity != null) {
                    String code = mVipInfoEntity.getCode();
                    String msg = mVipInfoEntity.getMsg();
                    if (SUCCESS_CODE.equals(code)) {
                        mVipInfoBean = mVipInfoEntity.getResult();
                        if (mVipInfoBean != null) {
                            String beEconomical = mVipInfoBean.getBeEconomical();
                            mTvTotalSave.setVisibility(!TextUtils.isEmpty(beEconomical) ? View.VISIBLE : View.GONE);
                            mTvTotalSave.setText(getStringsFormat(getActivity(), R.string.vip_save_money_total, mVipInfoBean.getBeEconomical()));
                            mTvEndTime.setText(getStringsFormat(getActivity(), R.string.vip_record, mVipInfoBean.getEndTime()));
                        }
                    } else {
                        showToast(msg);
                    }
                }

                mRlVipInfo.setVisibility(mVipInfoBean != null && communalUserInfoBean.isWhiteUser() ? View.VISIBLE : View.GONE);
                setBgHeight();
            }

            @Override
            public void onNotNetOrException() {
                mRlVipInfo.setVisibility(mVipInfoBean != null && communalUserInfoBean.isWhiteUser() ? View.VISIBLE : View.GONE);
                setBgHeight();
            }
        });
    }


    //??????????????????????????????
    private void clearBottomTypeCount() {
        for (MineTypeBean mineTypeBean : mineTypeList) {
            mineTypeBean.setMesCount(0);
        }
        typeMineAdapter.notifyDataSetChanged();
        //???????????????????????????
        EventBus.getDefault().post(new EventMessage(ConstantVariable.UPDATE_CAR_NUM, 0));
    }

    /**
     * ????????????????????????
     */
    private void setQyService() {
        qyInstance = QyServiceUtils.getQyInstance();
        updateBottomNum("ManagerServiceChat", qyInstance.getServiceTotalCount());
        qyInstance.getServiceCount(count -> updateBottomNum("ManagerServiceChat", count));
    }


    //????????????????????????
    private void updateBottomNum(String link, int num) {
        if (mineTypeList != null && mineTypeList.size() > 0 && typeMineAdapter != null) {
            for (int i = 0; i < mineTypeList.size(); i++) {
                MineTypeBean mineTypeBean = mineTypeList.get(i);
                String androidLink = getStrings(mineTypeBean.getAndroidUrl());
                if (!TextUtils.isEmpty(androidLink) && androidLink.contains(link)) {
                    mineTypeBean.setMesCount(num);
                    typeMineAdapter.notifyItemChanged(i);
                    break;
                }
            }
        }
    }

    /**
     * ????????????????????????
     */
    private void getDoMeIndentDataCount() {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Q_QUERY_INDENT_COUNT, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {

                mDirectIndentCountEntity = GsonUtils.fromJson(result, DirectIndentCountEntity.class);
                if (mDirectIndentCountEntity != null) {
                    if (mDirectIndentCountEntity.getCode().equals(SUCCESS_CODE)) {
                        setIndentCount(mDirectIndentCountEntity.getDirectIndentCountBean());
                    }
                }
            }
        });
    }

    //??????????????????
    private void setIndentCount(DirectIndentCountBean messageTotalBean) {
        if (messageTotalBean != null) {
            //??????????????????
            updateBottomNum("app://IntegralProductIndentActivity", messageTotalBean.getIntegralTakeDeliveryNum());
            //?????????
            indentTypeList.get(0).setType(messageTotalBean.getWaitPayNum());
            //?????????
            indentTypeList.get(1).setType(messageTotalBean.getWaitDeliveryNum());
            //?????????
            indentTypeList.get(2).setType(messageTotalBean.getWaitTakeDeliveryNum());
            //?????????
            indentTypeList.get(3).setType(messageTotalBean.getWaitEvaluateNum());
            //????????????
            indentTypeList.get(4).setType(messageTotalBean.getWaitAfterSaleNum());
        } else {
            for (int i = 0; i < indentTypeList.size(); i++) {
                QualityTypeBean qualityTypeBean = indentTypeList.get(i);
                qualityTypeBean.setType(0);
            }
        }
        indentTypeAdapter.notifyDataSetChanged();
    }

    //????????????????????????
    private void getBottomTypeNetData() {
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), MINE_BOTTOM_DATA, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                MineTypeEntity mineTypeEntity = GsonUtils.fromJson(result, MineTypeEntity.class);
                if (mineTypeEntity != null && SUCCESS_CODE.equals(mineTypeEntity.getCode())) {
                    MineTypeEntity bottomLocalData = getBottomLocalData();
                    if (bottomLocalData == null ||
                            !getStrings(bottomLocalData.getUpdateTime()).equals(getStrings(mineTypeEntity.getUpdateTime()))) {
                        saveBottomLocalData(result);
                    }
                } else {
                    //????????????????????????
                    if (getBottomShared() != null) {
                        getBottomShared().edit().clear().apply();
                    }
                }
            }
        });
    }


    //?????????????????????
    private void setErrorUserData() {
        try {
            initLoggedView();
            clearBottomTypeCount();
            setIndentCount(null);
            badgeMsg.setBadgeNumber(0);//???????????????
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //    ???????????? ??????
    private void getMineAd() {
        Map<String, Object> params = new HashMap<>();
        params.put("vidoShow", "1");
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), MINE_PAGE_AD, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {

                adBeanList.clear();
                CommunalADActivityEntity adActivityEntity = GsonUtils.fromJson(result, CommunalADActivityEntity.class);
                if (adActivityEntity != null) {
                    if (adActivityEntity.getCode().equals(SUCCESS_CODE)) {
                        setMineAdData(adActivityEntity);
                    }
                }
                cardview.setVisibility(adBeanList.size() > 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onNotNetOrException() {
                cardview.setVisibility(adBeanList.size() > 0 ? View.VISIBLE : View.GONE);
            }
        });
    }

    /**
     * ?????????-??????
     *
     * @param adActivityEntity
     */
    private void setMineAdData(CommunalADActivityEntity adActivityEntity) {
        List<CommunalADActivityBean> adList = adActivityEntity.getCommunalADActivityBeanList();
        if (adList != null && adList.size() > 0) {
            adBeanList.addAll(adList);
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
            ad_mine.setPages(getActivity(), cbViewHolderCreator, adBeanList)
                    .startTurning(getShowNumber(adBeanList.get(0).getShowTime()) * 1000);
        }
    }

    /**
     * ?????????????????????
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


    @OnClick({R.id.iv_mine_header, R.id.ll_mine_fans_count, R.id.ll_mine_att_count, R.id.ll_mine_inv_count, R.id.tv_mine_all_indent, R.id.iv_mine_mes,
            R.id.tv_no_login_show, R.id.rel_mine_info, R.id.ll_mime_no_login, R.id.rel_integral_more, R.id.tv_personal_data_sup, R.id.tv_bind_phone, R.id.tv_renewal, R.id.rl_no_vip_info, R.id.rl_vip_info})
    public void onViewClicked(View view) {
        if (userId < 0) {
            getLoginStatus(this);
            return;
        }
        Intent intent = null;
        switch (view.getId()) {
            //????????????
            case R.id.iv_mine_header:
                intent = new Intent(getActivity(), PersonalDataActivity.class);
                startActivity(intent);
                break;
            //???????????????
            case R.id.ll_mine_fans_count:
                intent = new Intent(getActivity(), UserFansAttentionActivity.class);
                intent.putExtra("type", "fans");
                intent.putExtra("fromPage", "mine");
                intent.putExtra("userId", userId);
                startActivity(intent);
                break;
            //???????????????
            case R.id.ll_mine_att_count:
                intent = new Intent(getActivity(), UserFansAttentionActivity.class);
                intent.putExtra("type", "attention");
                intent.putExtra("fromPage", "mine");
                intent.putExtra("userId", userId);
                startActivity(intent);
                break;
            //????????????
            case R.id.ll_mine_inv_count:
                intent = new Intent(getActivity(), MyPostActivity.class);
                startActivity(intent);
                break;
            //????????????
            case R.id.tv_mine_all_indent:
                intent = new Intent();
                intent.setClass(getActivity(), DoMoIndentAllActivity.class);
                intent.putExtra("tab", "all");
                if (mDirectIndentCountEntity != null && mDirectIndentCountEntity.getDirectIndentCountBean() != null) {
                    intent.putExtra("waitEvaluateNum", mDirectIndentCountEntity.getDirectIndentCountBean().getWaitEvaluateNum());
                }
                startActivity(intent);
                break;
            //????????????
            case R.id.iv_mine_mes:
                intent = new Intent(getActivity(), MessageActivity.class);
                startActivity(intent);
                break;
            //???????????????
            case R.id.tv_no_login_show:
                intent = new Intent(getActivity(), MineLoginActivity.class);
                startActivityForResult(intent, IS_LOGIN_CODE);
                break;
            //????????????
            case R.id.rel_mine_info:
            case R.id.ll_mime_no_login:
                if (communalUserInfoBean != null) {
                    intent = new Intent(getActivity(), PersonalBgImgActivity.class);
                    intent.putExtra("imgUrl", getStrings(communalUserInfoBean.getBgimg_url()));
                    startActivity(intent);
                }
                break;
            //??????????????????
            case R.id.rel_integral_more:
                intent = new Intent(getActivity(), AttendanceActivity.class);
                startActivity(intent);
                break;
            //????????????
            case R.id.tv_personal_data_sup:
                if (communalUserInfoBean != null && communalUserInfoBean.getNoticeInfo() != null
                        && !TextUtils.isEmpty(communalUserInfoBean.getNoticeInfo().getAndroid_link())) {
                    setSkipPath(getActivity(), communalUserInfoBean.getNoticeInfo().getAndroid_link(), false);
                }
                break;
            //???????????????
            case R.id.tv_bind_phone:
                if (communalUserInfoBean != null) {
                    boolean bindingWx = communalUserInfoBean.isBindingWx();
                    if (!bindingWx) {
                        //????????????
                        UMShareConfig config = new UMShareConfig();
                        config.isNeedAuthOnGetUserInfo(true);
                        UMShareAPI.get(getActivity()).setShareConfig(config);
                        // ??????????????????
                        UMShareAPI.get(getActivity()).getPlatformInfo(getActivity(), WEIXIN, getDataInfoListener);
                    } else {
                        UserDao.bindPhoneByWx(getActivity());
                    }
                }
                break;
            //????????????
            case R.id.rl_no_vip_info:
            case R.id.rl_vip_info:
                intent = new Intent(getActivity(), DomolifeVipActivity.class);
                startActivity(intent);
                break;
            //????????????
            case R.id.tv_renewal:
                intent = new Intent(getActivity(), OpenVipActivity.class);
                startActivity(intent);
                break;
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
            showToast("????????????");
            if (loadHud != null) {
                loadHud.dismiss();
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            showToast("????????????");
            if (loadHud != null) {
                loadHud.dismiss();
            }
        }
    };

    /**
     * ?????????????????????
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

                RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        showToast(requestStatus.getMsg());
                        //????????????Token??????
                        if (!TextUtils.isEmpty(requestStatus.getToken())) {
                            SharedPreUtils.setParam(TOKEN, getStrings(requestStatus.getToken()));
                            SharedPreUtils.setParam(TOKEN_EXPIRE_TIME, System.currentTimeMillis() + requestStatus.getTokenExpireSeconds());
                        }
                        /**
                         * ???????????????????????????
                         * 3.2.0 ???????????????????????????????????? ??????????????????????????????????????????????????????uid????????????
                         */
                        MobclickAgent.onProfileSignIn(getStrings(otherAccountBindInfo.getType()), String.valueOf(userId));
                        mTvBindPhone.setVisibility(View.GONE);
                    } else {
                        showToast(requestStatus.getMsg());
                    }
                }
            }
        });
    }

    /**
     * ?????????????????????????????????
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

    //??????????????????
    public void saveBottomLocalData(String typeData) {
        SharedPreferences bottomShared = getBottomShared();
        if (bottomShared != null && !TextUtils.isEmpty(typeData)) {
            SharedPreferences.Editor edit = bottomShared.edit();
            edit.putString(MINE_BOTTOM_TYPE, typeData);
            edit.apply();
        }
    }

    //??????????????????????????????
    public MineTypeEntity getBottomLocalData() {
        try {
            if (mineTypeEntity == null) {
                SharedPreferences bottomShared = getBottomShared();
                if (bottomShared != null) {
                    String typeJson = bottomShared.getString(MINE_BOTTOM_TYPE, "");
                    if (!TextUtils.isEmpty(typeJson)) {
                        mineTypeEntity = GsonUtils.fromJson(typeJson, MineTypeEntity.class);
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


    @Override
    public void onVisible() {
        super.onVisible();
        if (!isFirst) {
            getMineData();
        }
        isFirst = false;
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
         * ????????????SDK???????????????????????????????????????
         */
        AlibcTradeSDK.destory();
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (TOKEN_EXPIRE_LOG_OUT.equals(message.type)) {
            setErrorUserData();
        } else if (ConstantVariable.UPDATE_CAR_NUM.equals(message.type)) {
            updateBottomNum("ShopCarActivity", (int) message.result);
        }
    }
}
