package com.amkj.dmsh.dominant.activity;

import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.constant.CommunalAdHolderView;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.UMShareAction;
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

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.constant.ConstantMethod.getShowNumber;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.BASE_SHARE_PAGE_TWO;
import static com.amkj.dmsh.constant.Url.Q_POINT_SPIKE_AD;
import static com.amkj.dmsh.constant.Url.Q_POINT_SPIKE_TIME_SHAFT;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2019/3/1
 * version 3.3.0
 * class description:整点秒杀
 */
public class WholePointSpikeProductActivity extends BaseActivity {
    @BindView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @BindView(R.id.ad_point_spike)
    ConvenientBanner adPointSpike;
    @BindView(R.id.std_point_spike_type)
    SlidingTabLayoutDouble stdPointSpikeType;
    @BindView(R.id.vp_point_spike_container)
    ViewPager vpPointSpikeContainer;
    @BindView(R.id.smart_point_spike)
    SmartRefreshLayout smartPointSpike;
    @BindView(R.id.tv_header_shared)
    TextView tv_header_shared;
    //    时间轴数据
    private List<TimeAxisInfoListBean> timeAxisInfoList = new ArrayList<>();
    private List<CommunalADActivityBean> adBeanList = new ArrayList<>();
    private CBViewHolderCreator cbViewHolderCreator;
    private int screenWidth;

    @Override
    protected int getContentView() {
        return R.layout.activity_point_spike_product;
    }

    @Override
    protected void initViews() {
        tv_header_shared.setEnabled(false);
        tvHeaderTitle.setText("整点秒");
        stdPointSpikeType.setTextsize(AutoSizeUtils.mm2px(this, 28));
        stdPointSpikeType.setSubTextsize(AutoSizeUtils.mm2px(this, 24));
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
        NetLoadUtils.getNetInstance().loadNetDataPost(WholePointSpikeProductActivity.this, Q_POINT_SPIKE_TIME_SHAFT
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
            if (stdPointSpikeType.getVisibility() == View.GONE) {
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

            float sWidth = AutoSizeUtils.mm2px(this, 178);
            int sItemCount = (int) (screenWidth / sWidth);
            if (sItemCount >= timeAxisInfoList.size()) {
                sWidth = (int) (screenWidth * 1f / timeAxisInfoList.size());
            }
            stdPointSpikeType.setTabWidth(sWidth);
            //清空FragmentManager中的缓存(否则Viewpager中的fragment页面无法刷新)
            ConstantMethod.clearFragmentCache(getSupportFragmentManager());
            PointSpikeTimeShaftAdapter spikeTimeShaftAdapter = new PointSpikeTimeShaftAdapter(getSupportFragmentManager(), timeAxisInfoList);
            vpPointSpikeContainer.setAdapter(spikeTimeShaftAdapter);
            vpPointSpikeContainer.setOffscreenPageLimit(spikeTimeShaftAdapter.getCount() - 1);
            stdPointSpikeType.setViewPager(vpPointSpikeContainer, customTabDoubleEntities);

            int defaultItemPosition = stdPointSpikeType.getCurrentTab();
            //默认选中抢购中
            for (int i = 0; i < timeAxisInfoList.size(); i++) {
                if (timeAxisInfoList.get(i).getStatusCode() == 1) {
                    defaultItemPosition = i;
                    break;
                }
            }
            stdPointSpikeType.setCurrentTab(defaultItemPosition);
        } else {
            if (stdPointSpikeType.getVisibility() == View.VISIBLE) {
                stdPointSpikeType.setVisibility(View.GONE);
                vpPointSpikeContainer.setVisibility(View.GONE);
            }
        }
    }


    /**
     * 获取整点秒广告
     */
    private void getPointSpikeAd() {
        NetLoadUtils.getNetInstance().loadNetDataPost(WholePointSpikeProductActivity.this, Q_POINT_SPIKE_AD
                , null, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        tv_header_shared.setEnabled(true);
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
                                        return new CommunalAdHolderView(itemView, WholePointSpikeProductActivity.this, true);
                                    }

                                    @Override
                                    public int getLayoutId() {
                                        return R.layout.layout_ad_image_video;
                                    }
                                };
                            }
                            adPointSpike.setPages(WholePointSpikeProductActivity.this, cbViewHolderCreator, adBeanList)
                                    .startTurning(getShowNumber(adBeanList.get(0).getShowTime()) * 1000);
                        }
                    }

                    @Override
                    public void onNotNetOrException() {
                        tv_header_shared.setEnabled(true);
                        if (adPointSpike.getVisibility() == View.VISIBLE) {
                            adPointSpike.setVisibility(View.GONE);
                        }
                    }
                });
    }

    @Override
    public View getLoadView() {
        return smartPointSpike;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @OnClick(R.id.tv_life_back)
    void goBack() {
        finish();
    }

    @OnClick(R.id.tv_header_shared)
    void setShare() {
        CommunalADActivityBean communalADActivityBean = null;
        if (adBeanList.size() > 0) {
            communalADActivityBean = adBeanList.get(0);
        }
        String title = "限时限量秒杀";
        String description = "每天都有惊喜价，买到就是省钱，手慢无!";
        String picUrl = "";
        if (communalADActivityBean != null) {
            picUrl = communalADActivityBean.getPicUrl();
        }
        new UMShareAction(WholePointSpikeProductActivity.this
                , picUrl
                , title
                , description
                , BASE_SHARE_PAGE_TWO + "m/template/common/hours_activity.html?nav=1", 1);
    }
}
