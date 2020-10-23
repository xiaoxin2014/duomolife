package com.amkj.dmsh.time.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.constant.CommunalAdHolderView;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.find.bean.PostEntity;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity.CommunalADActivityBean;
import com.amkj.dmsh.mine.activity.WebRuleCommunalActivity;
import com.amkj.dmsh.network.NetCacheLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.time.adapter.TimePagerAdapter;
import com.amkj.dmsh.time.bean.TimeAxisEntity;
import com.amkj.dmsh.time.bean.TimeAxisEntity.TimeAxisBean;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.views.flycoTablayout.SlidingDoubleTextTabLayout;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.gyf.barlibrary.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.clearFragmentCache;
import static com.amkj.dmsh.constant.ConstantMethod.getShowNumber;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;


/**
 * Created by xiaoxin on 2020/9/23
 * Version:v4.8.0
 * ClassDescription :淘好货首页
 */
public class TimeShowNewFragment extends BaseFragment {
    @BindView(R.id.tv_life_back)
    TextView tv_life_back;
    @BindView(R.id.tv_header_title)
    TextView tv_header_title;
    @BindView(R.id.tv_header_shared)
    TextView tv_header_shared;
    @BindView(R.id.tl_normal_bar)
    Toolbar mTlNormalBar;
    @BindView(R.id.ad_communal_banner)
    ConvenientBanner mAdCommunalBanner;
    @BindView(R.id.rel_communal_banner)
    RelativeLayout mRelCommunalBanner;
    @BindView(R.id.collapsing_view)
    CollapsingToolbarLayout mCollapsingView;
    @BindView(R.id.sliding_tablayout)
    SlidingDoubleTextTabLayout mSlidingTablayout;
    @BindView(R.id.appBarLayout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout mSmartCommunalRefresh;
    private boolean isUpdateCache;
    private CBViewHolderCreator cbViewHolderCreator;
    private TimePagerAdapter mTimePagerAdapter;
    private List<CommunalADActivityBean> adBeanList = new ArrayList<>();
    private List<TimeAxisBean> mTimeAxisList = new ArrayList<>();
    private TimeAxisEntity mTimeAxisEntity;


    @Override
    protected int getContentView() {
        return R.layout.fragment_time_show_shaft_new;
    }

    @Override
    protected void initViews() {
        tv_life_back.setVisibility(View.GONE);
        tv_header_shared.setVisibility(View.GONE);
        tv_header_title.setText("淘好货");
        mSlidingTablayout.setTextsize(AutoSizeUtils.mm2px(mAppContext, 30));
        mSlidingTablayout.setTextUnselectColor(getResources().getColor(R.color.text_login_gray_s));
        mSmartCommunalRefresh.setOnRefreshListener(refreshLayout -> {
            isUpdateCache = true;
            loadData();
        });
    }

    @Override
    protected void loadData() {
        getHomeAd();
        getTimeShaft();
    }

    //获取种草帖子
    private void getRecommendGoods() {
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Url.GET_TIME_DOCUMENT_LIST, null, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                PostEntity postEntity = GsonUtils.fromJson(result, PostEntity.class);
                if (postEntity != null) {
                    if (postEntity.getCode().equals(SUCCESS_CODE)) {
                        List<PostEntity.PostBean> postList = postEntity.getPostList();
                        if (postList != null && postList.size() > 0) {
                            EventBus.getDefault().post(new EventMessage(ConstantVariable.UPDATE_RECOMMEND_LIST, postList));
                        }
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
            }
        });
    }

    //获取推荐广告位
    private void getRecommendAd() {
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Url.GET_RECOMMNED_AD, null, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                CommunalADActivityEntity adActivityEntity = GsonUtils.fromJson(result, CommunalADActivityEntity.class);
                if (adActivityEntity != null) {
                    if (adActivityEntity.getCode().equals(SUCCESS_CODE)) {
                        List<CommunalADActivityBean> adlist = adActivityEntity.getCommunalADActivityBeanList();
                        if (adlist != null && adlist.size() > 0) {
                            EventBus.getDefault().post(new EventMessage(ConstantVariable.UPDATE_RECOMMEND_AD, adlist));
                        }
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
            }
        });
    }

    //获取团购广告位
    private void getHomeAd() {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        NetLoadUtils.getNetInstance().loadNetDataGetCache(getActivity(), Url.GET_TIME_HOME_AD, params, isUpdateCache, new NetCacheLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                adBeanList.clear();
                CommunalADActivityEntity adActivityEntity = GsonUtils.fromJson(result, CommunalADActivityEntity.class);
                if (adActivityEntity != null) {
                    if (adActivityEntity.getCode().equals(SUCCESS_CODE)) {
                        List<CommunalADActivityBean> adlist = adActivityEntity.getCommunalADActivityBeanList();
                        if (adlist != null && adlist.size() > 0) {
                            adBeanList.addAll(adlist);
                            if (cbViewHolderCreator == null) {
                                cbViewHolderCreator = new CBViewHolderCreator() {
                                    @Override
                                    public Holder createHolder(View itemView) {
                                        return new CommunalAdHolderView(itemView, getActivity(), mAdCommunalBanner, true);
                                    }

                                    @Override
                                    public int getLayoutId() {
                                        return R.layout.layout_ad_image_video;
                                    }
                                };
                            }
                            mAdCommunalBanner.setPages(getActivity(), cbViewHolderCreator, adBeanList)
                                    .startTurning(getShowNumber(adBeanList.get(0).getShowTime()) * 1000);
                        }
                    }
                }

                mRelCommunalBanner.setVisibility(adBeanList.size() > 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onNotNetOrException() {
                mRelCommunalBanner.setVisibility(adBeanList.size() > 0 ? View.VISIBLE : View.GONE);
            }
        });
    }

    private void getTimeShaft() {
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Url.GET_TIME_AXIS, null, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                getRecommendAd();
                getRecommendGoods();
                mSmartCommunalRefresh.finishRefresh();
                mTimeAxisList.clear();
                mTimeAxisEntity = GsonUtils.fromJson(result, TimeAxisEntity.class);
                if (mTimeAxisEntity != null) {
                    if (SUCCESS_CODE.equals(mTimeAxisEntity.getCode())) {
                        List<TimeAxisBean> timeAxisInfoList = mTimeAxisEntity.getTimeAxisInfoList();
                        if (timeAxisInfoList != null) {
                            mTimeAxisList.addAll(timeAxisInfoList);
                        }
                    }
                }
                clearFragmentCache(getChildFragmentManager());
                mTimePagerAdapter = new TimePagerAdapter(getChildFragmentManager(), mTimeAxisList);
                mViewPager.setAdapter(mTimePagerAdapter);
                mViewPager.setOffscreenPageLimit(mTimeAxisList.size() - 1);
                mSlidingTablayout.setViewPager(mViewPager, mTimeAxisList);
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mTimeAxisList, mTimeAxisEntity);
            }

            @Override
            public void onNotNetOrException() {
                mSmartCommunalRefresh.finishRefresh();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mTimeAxisList, mTimeAxisEntity);
            }
        });
    }


    @Override
    protected void postEventResult(@NonNull EventMessage message) {
    }

    @Override
    public boolean immersionBarEnabled() {
        return true;
    }

    @Override
    public void initImmersionBar() {
        ImmersionBar.with(this).statusBarColor(R.color.colorPrimary).keyboardEnable(true)
                .statusBarDarkFont(true).fitsSystemWindows(true).navigationBarEnable(false).init();
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    protected boolean isLazy() {
        return false;
    }

    @OnClick(R.id.ll_rule)
    public void onViewClicked() {
        Intent intent = new Intent(getActivity(), WebRuleCommunalActivity.class);
        intent.putExtra(ConstantVariable.WEB_VALUE_TYPE, ConstantVariable.WEB_TYPE_GROUP_BUY);
        startActivity(intent);
    }
}
