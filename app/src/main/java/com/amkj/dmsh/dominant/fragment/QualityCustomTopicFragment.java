package com.amkj.dmsh.dominant.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dominant.adapter.GoodProductAdapter;
import com.amkj.dmsh.dominant.bean.CustomCoverDesEntity;
import com.amkj.dmsh.dominant.bean.CustomCoverDesEntity.CustomCoverDesBean;
import com.amkj.dmsh.homepage.adapter.CommunalDetailAdapter;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean;
import com.amkj.dmsh.user.bean.UserLikedProductEntity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean;
import com.amkj.dmsh.utils.CountDownTimer;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.amkj.dmsh.utils.webformatdata.CommunalWebDetailUtils;
import com.amkj.dmsh.utils.webformatdata.ShareDataBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.getCarCount;
import static com.amkj.dmsh.constant.ConstantMethod.getIntegralFormat;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TWENTY;
import static com.amkj.dmsh.constant.Url.GET_SHOPPING_REWARD;
import static com.amkj.dmsh.constant.Url.Q_CUSTOM_PRO_COVER;
import static com.amkj.dmsh.constant.Url.Q_CUSTOM_PRO_LIST;

;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/04/03
 * class description:自定义专区
 */
public class QualityCustomTopicFragment extends BaseFragment {
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    @BindView(R.id.tl_quality_bar)
    Toolbar mTlQualityBar;
    private int page = 1;
    private GoodProductAdapter qualityCustomTopicAdapter;
    private List<LikedProductBean> customProList = new ArrayList<>();
    private List<CommunalDetailObjectBean> descriptionList = new ArrayList<>();
    private QNewProView qNewProView;
    private IntegralView mIntegralView;
    private String productType;
    private View headViewCover;
    private View headViewIntegral;
    private CommunalDetailAdapter communalDetailAdapter;
    private UserLikedProductEntity userLikedProductEntity;
    private GridLayoutManager mGridLayoutManager;

    @Override
    protected int getContentView() {
        return R.layout.activity_communal_ql_shop_car;
    }

    @Override
    protected void initViews() {
        mTlQualityBar.setVisibility(View.GONE);
        smart_communal_refresh.setOnRefreshListener(refreshLayout -> loadData());
        setFloatingButton(download_btn_communal, communal_recycler);

        //初始化封面信息
        headViewCover = LayoutInflater.from(getActivity()).inflate(R.layout.layout_communal_detail_scroll_rec_cover_wrap, null, false);
        qNewProView = new QNewProView();
        ButterKnife.bind(qNewProView, headViewCover);
        qNewProView.initViews();

        //初始化商品列表
        mGridLayoutManager = new GridLayoutManager(getActivity(), 2);
        communal_recycler.setLayoutManager(mGridLayoutManager);
        qualityCustomTopicAdapter = new GoodProductAdapter(getActivity(), customProList);
        communal_recycler.setAdapter(qualityCustomTopicAdapter);
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_dp)
                .create());
        qualityCustomTopicAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                getQualityCustomPro();
            }
        }, communal_recycler);
    }

    @Override
    protected void loadData() {
        page = 1;
        getCustomCoverDescription();
        getCarCount(getActivity());
    }


    @Override
    protected boolean isAddLoad() {
        return true;
    }

    /**
     * 获取自定义专区封面描述
     */
    private void getCustomCoverDescription() {
        if (TextUtils.isEmpty(productType)) {
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("productType", productType);
        params.put("version", 1);
        if (userId > 0) {
            params.put("uid", userId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Q_CUSTOM_PRO_COVER, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                if (descriptionList.size() > 0) {
                    descriptionList.clear();
                    communalDetailAdapter.notifyDataSetChanged();
                }
                Gson gson = new Gson();
                CustomCoverDesEntity customCoverDesEntity = gson.fromJson(result, CustomCoverDesEntity.class);
                if (customCoverDesEntity != null) {
                    if (customCoverDesEntity.getCode().equals(SUCCESS_CODE)
                            && customCoverDesEntity.getCoverDesList() != null
                            && customCoverDesEntity.getCoverDesList().size() > 0) {
                        CustomCoverDesBean customCoverDesBean = customCoverDesEntity.getCoverDesList().get(0);
                        if (!TextUtils.isEmpty(customCoverDesBean.getPicUrl())) {
                            qNewProView.iv_communal_cover_wrap.setVisibility(View.VISIBLE);
                            GlideImageLoaderUtil.loadImgDynamicDrawable(getActivity(), qNewProView.iv_communal_cover_wrap,
                                    getStrings(customCoverDesBean.getPicUrl()), -1);
                        } else {
                            qNewProView.iv_communal_cover_wrap.setVisibility(View.GONE);
                        }
                        if (customCoverDesBean.getDescriptionList() != null
                                && customCoverDesBean.getDescriptionList().size() > 0) {
                            qNewProView.communal_recycler_wrap.setVisibility(View.VISIBLE);
                            qNewProView.v_line_bottom.setVisibility(View.VISIBLE);
                            List<CommunalDetailObjectBean> detailsDataList = CommunalWebDetailUtils.getCommunalWebInstance().getWebDetailsFormatDataList(customCoverDesBean.getDescriptionList());
                            if (detailsDataList != null) {
                                descriptionList.addAll(detailsDataList);
                                communalDetailAdapter.notifyDataSetChanged();
                            }
                        } else {
                            qNewProView.v_line_bottom.setVisibility(View.GONE);
                            qNewProView.communal_recycler_wrap.setVisibility(View.GONE);
                        }
                        if (qualityCustomTopicAdapter.getHeaderLayoutCount() < 1) {
                            qualityCustomTopicAdapter.addHeaderView(headViewCover);
                            qualityCustomTopicAdapter.notifyDataSetChanged();
                        }
                    } else {
                        qualityCustomTopicAdapter.removeAllHeaderView();
                        qualityCustomTopicAdapter.notifyDataSetChanged();
                    }
                }

                getQualityCustomPro();
            }

            @Override
            public void onNotNetOrException() {
                if (qualityCustomTopicAdapter.getHeaderLayoutCount() > 0) {
                    qualityCustomTopicAdapter.removeAllHeaderView();
                }
            }
        });
    }

    private void getQualityCustomPro() {
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", page);
        params.put("productType", productType);
        params.put("showCount", TOTAL_COUNT_TWENTY);
        if (userId > 0) {
            params.put("uid", userId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Q_CUSTOM_PRO_LIST
                , params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();
                        qualityCustomTopicAdapter.loadMoreComplete();
                        if (page == 1) {
                            customProList.clear();
                        }
                        Gson gson = new Gson();
                        userLikedProductEntity = gson.fromJson(result, UserLikedProductEntity.class);
                        if (userLikedProductEntity != null) {
                            if (userLikedProductEntity.getCode().equals(SUCCESS_CODE)) {
                                customProList.addAll(userLikedProductEntity.getGoodsList());
                            } else if (userLikedProductEntity.getCode().equals(EMPTY_CODE)) {
                                qualityCustomTopicAdapter.loadMoreEnd();
                            } else {
                                showToast(getActivity(), userLikedProductEntity.getMsg());
                            }

                            if (headViewIntegral != null) {
                                qualityCustomTopicAdapter.removeHeaderView(headViewIntegral);
                            }

                            //判断是否有积分奖励(该专区有奖励并且当天没有领取奖励)
                            if (userLikedProductEntity.isUserHaveReward()) {
                                headViewIntegral = LayoutInflater.from(getActivity()).inflate(R.layout.layout_get_integral_header, null, false);
                                mIntegralView = new IntegralView();
                                ButterKnife.bind(mIntegralView, headViewIntegral);
                                mIntegralView.mTvBrowse.setVisibility(View.VISIBLE);
                                mIntegralView.mTvBrowse.setVisibility(View.VISIBLE);
                                mIntegralView.mTvIntegralRule.setText(getStrings(userLikedProductEntity.getRewardInfo()));
                                mIntegralView.mViewTime = userLikedProductEntity.getViewTime();
                                qualityCustomTopicAdapter.addHeaderView(headViewIntegral, 0);
                            }

                            qualityCustomTopicAdapter.notifyDataSetChanged();
                        }
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, customProList.size() > 0 || descriptionList.size() > 0, userLikedProductEntity);
                    }

                    @Override
                    public void onNotNetOrException() {
                        smart_communal_refresh.finishRefresh();
                        qualityCustomTopicAdapter.loadMoreEnd(true);
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, customProList.size() > 0 || descriptionList.size() > 0, userLikedProductEntity);
                    }
                });
    }

    private void getReward() {
        Map<String, Object> map = new HashMap<>();
        if (userId > 0) {
            map.put("uid", userId);
        }
        map.put("zoneId", productType);
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), GET_SHOPPING_REWARD, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                RequestStatus requestStatus = new Gson().fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    String code = requestStatus.getCode();
                    if (SUCCESS_CODE.equals(code)) {
                        mIntegralView.mTvIntegralRule.setText("积分奖励已到账");
                    } else {
                        mIntegralView.mTvIntegralRule.setText(getStrings(requestStatus.getMsg()));
                    }
                } else {
                    mIntegralView.mTvIntegralRule.setText("积分发放失败");
                }
            }

            @Override
            public void onNotNetOrException() {
                mIntegralView.mTvIntegralRule.setText("积分发放失败");
            }
        });

    }

    class IntegralView {
        @BindView(R.id.ll_get_integral_header)
        LinearLayout mLLHeader;
        @BindView(R.id.tv_integral_rule)
        TextView mTvIntegralRule;
        @BindView(R.id.tv_browse)
        TextView mTvBrowse;
        private int mViewTime;

        @OnClick(R.id.tv_browse)
        void startBrowse(View view) {
            if (userId > 0) {
                mTvBrowse.setVisibility(View.GONE);
                CountDownTimer countDownTimer = new CountDownTimer(getActivity(), mViewTime * 1000 + 300, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        int second = (int) (millisUntilFinished / 1000);
                        if (second > 0) {
                            mTvIntegralRule.setText(getIntegralFormat(getActivity(), R.string.shoppig_reward_rule, second));
                        }
                    }

                    @Override
                    public void onFinish() {
                        cancel();
                        mLLHeader.setSelected(true);
                        mTvIntegralRule.setText("积分奖励发放中...");
                        getReward();
                    }
                };

                countDownTimer.start();
            } else {
                getLoginStatus(getActivity());
            }
        }
    }


    class QNewProView {
        @BindView(R.id.iv_communal_cover_wrap)
        ImageView iv_communal_cover_wrap;
        @BindView(R.id.communal_recycler_wrap)
        RecyclerView communal_recycler_wrap;
        @BindView(R.id.v_line_bottom)
        View v_line_bottom;

        public void initViews() {
            communal_recycler_wrap.setNestedScrollingEnabled(false);
            communal_recycler_wrap.setLayoutManager(new LinearLayoutManager((getActivity())));
            communalDetailAdapter = new CommunalDetailAdapter(getActivity(), descriptionList);
            communalDetailAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    ShareDataBean shareDataBean = null;
                    if (view.getId() == R.id.tv_communal_share && customProList.size() > 0) {
                        LikedProductBean likedProductBean = customProList.get(0);
                        shareDataBean = new ShareDataBean(likedProductBean.getPicUrl()
                                , getStrings(userLikedProductEntity.getZoneName())
                                , "我在多么生活发现这几样好物，性价比不错，还包邮"
                                , Url.BASE_SHARE_PAGE_TWO + "m/template/goods/CustomZone.html?id=" + productType);

                    }
                    CommunalWebDetailUtils.getCommunalWebInstance()
                            .setWebDataClick(getActivity(), shareDataBean, view, loadHud);
                }
            });
            communal_recycler_wrap.setAdapter(communalDetailAdapter);
        }
    }


    @Override
    protected void getReqParams(Bundle bundle) {
        productType = bundle.getString("productType");
    }
}
