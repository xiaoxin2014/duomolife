package com.amkj.dmsh.dominant.fragment;

import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.CommunalAdHolderView;
import com.amkj.dmsh.dominant.adapter.QualityHotSaleAdapter;
import com.amkj.dmsh.dominant.bean.QualityHotSaleShaftEntity;
import com.amkj.dmsh.dominant.bean.QualityHotSaleShaftEntity.HotSaleShaftBean;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity.CommunalADActivityBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.amkj.dmsh.views.flycoTablayout.SlidingTabLayout;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tencent.bugly.beta.tinker.TinkerManager;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import me.jessyan.autosize.utils.AutoSizeUtils;
import q.rorbin.badgeview.Badge;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getBadge;
import static com.amkj.dmsh.constant.ConstantMethod.getShowNumber;
import static com.amkj.dmsh.constant.ConstantMethod.insertNewTotalData;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.showToastRequestMsg;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.QUALITY_HOT_SALE_AD;
import static com.amkj.dmsh.constant.Url.QUALITY_HOT_SALE_SHAFT;
import static com.amkj.dmsh.constant.Url.Q_QUERY_CAR_COUNT;

;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/6/29
 * class description:热销商品
 */
public class QualityTypeHotSaleProFragment extends BaseFragment {
    @BindView(R.id.smart_refresh_hot_sale)
    SmartRefreshLayout smart_refresh_hot_sale;
    @BindView(R.id.tl_quality_bar)
    Toolbar tl_quality_bar;
    @BindView(R.id.fl_header_service)
    FrameLayout fl_header_service;
    @BindView(R.id.rel_communal_banner)
    RelativeLayout relCommunalBanner;
    @BindView(R.id.ad_communal_banner)
    ConvenientBanner adCommunalBanner;
    @BindView(R.id.std_dominant_hot_sale)
    SlidingTabLayout stdDominantHotSale;
    @BindView(R.id.vp_dominant_hot_sale)
    ViewPager vpDominantHotSale;
    private Badge badge;
    private QualityHotSaleShaftEntity qualityHotSaleShaftEntity;
    private CBViewHolderCreator cbViewHolderCreator;
    private List<CommunalADActivityBean> adBeanList = new ArrayList<>();
    private int screenWidth;

    @Override
    protected int getContentView() {
        return R.layout.activity_dominant_hot_sale;
    }

    @Override
    protected void initViews() {
        tl_quality_bar.setVisibility(View.GONE);
        stdDominantHotSale.setTextsize(AutoSizeUtils.mm2px(mAppContext, 28));
        smart_refresh_hot_sale.setOnRefreshListener(refreshLayout -> {
            loadData();
            EventBus.getDefault().post(new EventMessage("refresh", "hotSaleData"));
        });
        badge = getBadge(getActivity(), fl_header_service);
        totalPersonalTrajectory = insertNewTotalData(getActivity());
    }

    @Override
    protected void loadData() {
        getHotSaleAd();
        getQualityProductShaft();
        getCarCount();
    }

    private void getHotSaleAd() {
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), QUALITY_HOT_SALE_AD
                , null, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        adBeanList.clear();
                        Gson gson = new Gson();
                        CommunalADActivityEntity qualityAdLoop = gson.fromJson(result, CommunalADActivityEntity.class);
                        if (qualityAdLoop != null) {
                            if (qualityAdLoop.getCode().equals(SUCCESS_CODE)) {
                                adBeanList.addAll(qualityAdLoop.getCommunalADActivityBeanList());
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
                                adCommunalBanner.setPages(getActivity(), cbViewHolderCreator, adBeanList).setCanLoop(true).setPointViewVisible(true).setCanScroll(true)
                                        .setPageIndicator(new int[]{R.drawable.unselected_radius, R.drawable.selected_radius})
                                        .startTurning(getShowNumber(adBeanList.get(0).getShowTime()) * 1000);
                            }
                            if (adBeanList.size() > 0) {
                                relCommunalBanner.setVisibility(View.VISIBLE);
                            } else {
                                relCommunalBanner.setVisibility(View.GONE);
                            }
                            NetLoadUtils.getNetInstance().showLoadSir(loadService, qualityHotSaleShaftEntity);
                        }
                    }

                    @Override
                    public void onNotNetOrException() {
                        smart_refresh_hot_sale.finishRefresh();
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, qualityHotSaleShaftEntity);
                    }
                });
    }

    private void getQualityProductShaft() {
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), QUALITY_HOT_SALE_SHAFT
                , null, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        smart_refresh_hot_sale.finishRefresh();
                        Gson gson = new Gson();
                        qualityHotSaleShaftEntity = gson.fromJson(result, QualityHotSaleShaftEntity.class);
                        if (qualityHotSaleShaftEntity != null) {
                            if (qualityHotSaleShaftEntity.getCode().equals(SUCCESS_CODE)) {
                                setHotSaleShaftData(qualityHotSaleShaftEntity.getHotSaleShaft());
                            } else if (!qualityHotSaleShaftEntity.getCode().equals(EMPTY_CODE)) {
                                showToast(getActivity(), qualityHotSaleShaftEntity.getMsg());
                            }
                        }
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, qualityHotSaleShaftEntity);
                    }

                    @Override
                    public void netClose() {
                        smart_refresh_hot_sale.finishRefresh();
                        showToast(getActivity(), R.string.unConnectedNetwork);
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, qualityHotSaleShaftEntity);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        smart_refresh_hot_sale.finishRefresh();
                        showToast(getActivity(), R.string.invalidData);
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, qualityHotSaleShaftEntity);
                    }
                });
    }

    /**
     * 设置热销单品 时间轴
     *
     * @param hotSaleShaft
     */
    private void setHotSaleShaftData(List<HotSaleShaftBean> hotSaleShaft) {
        int currentTab = stdDominantHotSale.getCurrentTab();
        if (hotSaleShaft != null && hotSaleShaft.size() > 0) {
            QualityHotSaleAdapter qualityHotSaleAdapter = new QualityHotSaleAdapter(getActivity().getSupportFragmentManager(), hotSaleShaft);
            stdDominantHotSale.setVisibility(View.VISIBLE);
            if (screenWidth == 0) {
                TinkerBaseApplicationLike applicationLike = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
                screenWidth = applicationLike.getScreenWidth();
            }
            if (hotSaleShaft.size() > 3) {
                stdDominantHotSale.setTabWidth((float) (screenWidth / 3.5));
            } else {
                if (hotSaleShaft.size() == 1) {
                    stdDominantHotSale.setVisibility(View.GONE);
                } else {
                    stdDominantHotSale.setTabWidth((float) (screenWidth / hotSaleShaft.size()));
                }
            }
            vpDominantHotSale.setAdapter(qualityHotSaleAdapter);
            stdDominantHotSale.setViewPager(vpDominantHotSale);
            stdDominantHotSale.setCurrentTab(currentTab);
        }
    }

    private void getCarCount() {
        if (userId > 0) {
            //购物车数量展示
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Q_QUERY_CAR_COUNT, params, new NetLoadListenerHelper() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                    if (requestStatus != null) {
                        if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                            int cartNumber = requestStatus.getResult().getCartNumber();
                            badge.setBadgeNumber(cartNumber);
                        } else if (!requestStatus.getCode().equals(EMPTY_CODE)) {
                            showToastRequestMsg(getActivity(), requestStatus);
                        }
                    }
                }
            });
        }
    }
}
