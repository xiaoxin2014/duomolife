package com.amkj.dmsh.dominant.fragment;

import android.app.Activity;
import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.constant.CommunalAdHolderView;
import com.amkj.dmsh.dominant.adapter.PointSpikeTimeShaftAdapter;
import com.amkj.dmsh.dominant.bean.PointSpikeTimeShaftEntity;
import com.amkj.dmsh.dominant.bean.PointSpikeTimeShaftEntity.TimeAxisInfoListBean;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity.CommunalADActivityBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.views.flycoTablayout.SlidingTabLayoutDouble;
import com.amkj.dmsh.views.flycoTablayout.listener.CustomTabDoubleEntity;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.bugly.beta.tinker.TinkerManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.constant.ConstantMethod.getShowNumber;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.Q_POINT_SPIKE_AD;
import static com.amkj.dmsh.constant.Url.Q_POINT_SPIKE_TIME_SHAFT;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2019/3/5
 * version 3.3.0
 * class description:整点秒杀Fragment
 */
public class PointSpikeMainFragment extends BaseFragment {
    @BindView(R.id.ad_point_spike)
    ConvenientBanner adPointSpike;
    @BindView(R.id.std_point_spike_type)
    SlidingTabLayoutDouble stdPointSpikeType;
    @BindView(R.id.vp_point_spike_container)
    ViewPager vpPointSpikeContainer;
    @BindView(R.id.smart_point_spike)
    SmartRefreshLayout smartPointSpike;
    //    时间轴数据
    private List<TimeAxisInfoListBean> timeAxisInfoList = new ArrayList<>();
    private List<CommunalADActivityBean> adBeanList = new ArrayList<>();
    private CBViewHolderCreator cbViewHolderCreator;
    private int screenWidth;
    private WeakReference<Activity> activityWeakReference;

    @Override
    protected int getContentView() {
        return R.layout.layout_whole_point_spike;
    }

    @Override
    protected void initViews() {
        activityWeakReference = new WeakReference<Activity>(getActivity());
        stdPointSpikeType.setTextsize(AutoSizeUtils.mm2px(activityWeakReference.get(), 28));
        stdPointSpikeType.setSubTextsize(AutoSizeUtils.mm2px(activityWeakReference.get(), 24));
        smartPointSpike.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                loadData();
            }
        });
        TinkerBaseApplicationLike tinkerBaseApplicationLike = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
        screenWidth = tinkerBaseApplicationLike.getScreenWidth();
    }

    @Override
    protected void loadData() {
        getPointSpikeAd();
        getPointSpikeTimeShaft();
    }

    /**
     * 获取整点秒时间轴
     */
    private void getPointSpikeTimeShaft() {
        NetLoadUtils.getNetInstance().loadNetDataPost(activityWeakReference.get(), Q_POINT_SPIKE_TIME_SHAFT
                , null, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        smartPointSpike.finishRefresh();
                        timeAxisInfoList.clear();
                        PointSpikeTimeShaftEntity pointSpikeTimeShaftEntity = GsonUtils.fromJson(result, PointSpikeTimeShaftEntity.class);
                        if (pointSpikeTimeShaftEntity != null && pointSpikeTimeShaftEntity.getCode().equals(SUCCESS_CODE)) {
                            timeAxisInfoList.addAll(pointSpikeTimeShaftEntity.getTimeAxisInfoList());
                            setTimeShaftData(pointSpikeTimeShaftEntity.getTimeAxisInfoList());
                        }
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, pointSpikeTimeShaftEntity);
                    }

                    @Override
                    public void onNotNetOrException() {
                        smartPointSpike.finishRefresh();
                        if (timeAxisInfoList.size() < 1) {
                            NetLoadUtils.getNetInstance().showLoadSirLoadFailed(loadService);
                        }
                    }
                });
    }

    /**
     * 设置时间轴信息
     *
     * @param timeAxisInfoList
     */
    private void setTimeShaftData(List<TimeAxisInfoListBean> timeAxisInfoList) {
        if (timeAxisInfoList.size() > 0) {
            if(stdPointSpikeType.getVisibility() == View.GONE){
                stdPointSpikeType.setVisibility(View.VISIBLE);
                vpPointSpikeContainer.setVisibility(View.VISIBLE);
            }
            List<CustomTabDoubleEntity> customTabDoubleEntities = new ArrayList<>();
            for (TimeAxisInfoListBean timeAxisInfoListBean : timeAxisInfoList) {
                customTabDoubleEntities.add(new CustomTabDoubleEntity() {
                    @Override
                    public String getTabTitle() {
                        return timeAxisInfoListBean.getShowTime();
                    }

                    @Override
                    public String getTabSubtitle() {
                        return timeAxisInfoListBean.getStatus();
                    }
                });
            }
            int defaultItemPosition = 0;
            if (vpPointSpikeContainer.getAdapter() != null) {
                defaultItemPosition = vpPointSpikeContainer.getCurrentItem();
            }
            float sWidth = AutoSizeUtils.mm2px(activityWeakReference.get(), 178);
            int sItemCount = (int) (screenWidth / sWidth);
            if (sItemCount >= timeAxisInfoList.size()) {
                sWidth = (int) (screenWidth * 1f / timeAxisInfoList.size());
            }
            stdPointSpikeType.setTabWidth(sWidth);
            PointSpikeTimeShaftAdapter spikeTimeShaftAdapter = new PointSpikeTimeShaftAdapter(getChildFragmentManager(), timeAxisInfoList);
            vpPointSpikeContainer.setAdapter(spikeTimeShaftAdapter);
            stdPointSpikeType.setViewPager(vpPointSpikeContainer, customTabDoubleEntities);
            if (defaultItemPosition > 0) {
                stdPointSpikeType.setCurrentTab(defaultItemPosition);
                vpPointSpikeContainer.setCurrentItem(defaultItemPosition);
            } else {
                setDefaultCategoryType();
            }
        }else{
            if(stdPointSpikeType.getVisibility() == View.VISIBLE){
                stdPointSpikeType.setVisibility(View.GONE);
                vpPointSpikeContainer.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 刷新 tab恢复默认
     */
    private void setDefaultCategoryType() {
        if (vpPointSpikeContainer.getAdapter() != null
                && stdPointSpikeType.getCurrentTab() != 0
                && vpPointSpikeContainer.getCurrentItem() != 0) {
            stdPointSpikeType.setCurrentTab(0);
            vpPointSpikeContainer.setCurrentItem(0);
        }
    }

    /**
     * 获取整点秒广告
     */
    private void getPointSpikeAd() {
        NetLoadUtils.getNetInstance().loadNetDataPost(activityWeakReference.get(), Q_POINT_SPIKE_AD
                , null, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        adBeanList.clear();
                        CommunalADActivityEntity communalADActivityEntity = GsonUtils.fromJson(result, CommunalADActivityEntity.class);
                        if (communalADActivityEntity != null && communalADActivityEntity.getCode().equals(SUCCESS_CODE)) {
                            adBeanList.addAll(communalADActivityEntity.getCommunalADActivityBeanList());
                            if (adPointSpike.getVisibility() == View.GONE) {
                                adPointSpike.setVisibility(View.VISIBLE);
                            }
                            if (cbViewHolderCreator == null) {
                                cbViewHolderCreator = new CBViewHolderCreator() {
                                    @Override
                                    public Holder createHolder(View itemView) {
                                        return new CommunalAdHolderView(itemView, activityWeakReference.get(), true);
                                    }

                                    @Override
                                    public int getLayoutId() {
                                        return R.layout.layout_ad_image_video;
                                    }
                                };
                            }
                            adPointSpike.setPages((LifecycleOwner) activityWeakReference.get(), cbViewHolderCreator, adBeanList).setCanLoop(true)
                                    .setPointViewVisible(true)
                                    .setPageIndicator(new int[]{R.drawable.unselected_radius, R.drawable.selected_radius})
                                    .startTurning(getShowNumber(adBeanList.get(0).getShowTime()) * 1000);
                        }else{
                            if (adPointSpike.getVisibility() == View.VISIBLE) {
                                adPointSpike.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onNotNetOrException() {
                        if (adPointSpike.getVisibility() == View.VISIBLE) {
                            adPointSpike.setVisibility(View.GONE);
                        }
                    }
                });
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }
}
