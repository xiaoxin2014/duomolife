package com.amkj.dmsh.time.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.base.MyPagerAdapter;
import com.amkj.dmsh.base.RecyclerViewScrollHelper;
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
import com.amkj.dmsh.time.activity.TimePostCatergoryActivity;
import com.amkj.dmsh.time.adapter.TimeAxisAdapter;
import com.amkj.dmsh.time.adapter.TimePostAdapter;
import com.amkj.dmsh.time.bean.TimeAxisEntity;
import com.amkj.dmsh.time.bean.TimeAxisEntity.TimeAxisBean;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.amkj.dmsh.views.flycoTablayout.SlidingDoubleTextTabLayout;
import com.amkj.dmsh.views.flycoTablayout.listener.OnTabClickListener;
import com.amkj.dmsh.views.convenientbanner.ConvenientBanner;
import com.amkj.dmsh.views.convenientbanner.holder.CBViewHolderCreator;
import com.amkj.dmsh.views.convenientbanner.holder.Holder;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.gyf.barlibrary.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
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
    TextView mTvLifeBack;
    @BindView(R.id.tv_header_title)
    TextView mTvHeaderTitle;
    @BindView(R.id.tv_header_shared)
    TextView mTvHeaderShared;
    @BindView(R.id.tl_normal_bar)
    Toolbar mTlNormalBar;
    @BindView(R.id.ad_communal_banner)
    ConvenientBanner mAdCommunalBanner;
    @BindView(R.id.ll_rule)
    LinearLayout mLlRule;
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
    @BindView(R.id.rv_goods)
    RecyclerView mRvGoods;
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout mSmartCommunalRefresh;

    private boolean isUpdateCache;
    private CBViewHolderCreator cbViewHolderCreator;
    private List<CommunalADActivityBean> adBeanList = new ArrayList<>();
    private List<CommunalADActivityBean> postAdBeanList = new ArrayList<>();
    private List<TimeAxisBean> mTimeAxisList = new ArrayList<>();
    private List<PostEntity.PostBean> mPostList = new ArrayList<>();
    private TimeAxisEntity mTimeAxisEntity;
    private TimeAxisAdapter mTimeAxisAdapter;
    private TimeAxisFootView mTimeAxisFootView;
    private TimePostAdapter mTimePostAdapter;
    private View mFootview;


    @Override
    protected int getContentView() {
        return R.layout.fragment_time_show_shaft_new;
    }

    @Override
    protected void initViews() {
        mTvLifeBack.setVisibility(View.GONE);
        mTvHeaderShared.setVisibility(View.GONE);
        mTvHeaderTitle.setText("淘好货");
        mSlidingTablayout.setTextsize(AutoSizeUtils.mm2px(mAppContext, 30));
        mSlidingTablayout.setTextUnselectColor(getResources().getColor(R.color.text_login_gray_s));
        mSmartCommunalRefresh.setOnRefreshListener(refreshLayout -> {
            isUpdateCache = true;
            loadData();
        });

        //初始化团购商品列表
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRvGoods.setLayoutManager(linearLayoutManager);
        mRvGoods.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_time_axis)
                .setLastDraw(false)
                .create());
        mTimeAxisAdapter = new TimeAxisAdapter(getActivity(), mTimeAxisList);
        mRvGoods.setAdapter(mTimeAxisAdapter);
        mSlidingTablayout.setOnTabClickListener(new OnTabClickListener() {
            @Override
            public void onClick(int position) {
                //手动切换tab时设置tag
                mRvGoods.setTag(position);
                RecyclerViewScrollHelper.scrollToPosition(mRvGoods, position);
            }
        });

        mRvGoods.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //只有手动滚动才需要监听
                if (mRvGoods != null && mRvGoods.getTag() == null) {
                    int position = linearLayoutManager.findFirstVisibleItemPosition();
                    mSlidingTablayout.setCurrentTab(position >= mTimeAxisList.size() ? mTimeAxisList.size() - 1 : position, true);
                }
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (mRvGoods != null && newState == 0) {
                    mRvGoods.setTag(null);
                }
            }
        });

        mFootview = LayoutInflater.from(getActivity()).inflate(R.layout.layout_time_axis_foot, null, false);
        mTimeAxisFootView = new TimeAxisFootView();
        ButterKnife.bind(mTimeAxisFootView, mFootview);

        //初始化推荐帖子列表
        ItemDecoration newGridItemDecoration = new ItemDecoration.Builder()
                .setDividerId(R.drawable.item_divider_five_gray_f)
                .create();
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        mTimeAxisFootView.mRvRecommend.setLayoutManager(layoutManager);
        mTimeAxisFootView.mRvRecommend.addItemDecoration(newGridItemDecoration);
        mTimePostAdapter = new TimePostAdapter(getActivity(), mPostList, true);
        mTimeAxisFootView.mRvRecommend.setAdapter(mTimePostAdapter);
    }


    class TimeAxisFootView {
        @BindView(R.id.ad_recommend_banner)
        ConvenientBanner mAdRecommendBanner;
        @BindView(R.id.rv_recommend)
        RecyclerView mRvRecommend;
        @BindView(R.id.tv_more)
        TextView mTvMore;

        @OnClick(R.id.tv_more)
        public void onViewClicked() {
            Intent intent = new Intent(getActivity(), TimePostCatergoryActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void loadData() {
        getHomeAd();
        getTimeShaft();
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
                mSmartCommunalRefresh.finishRefresh();
                mTimeAxisList.clear();
                mTimeAxisEntity = GsonUtils.fromJson(result, TimeAxisEntity.class);
                if (mTimeAxisEntity != null) {
                    if (SUCCESS_CODE.equals(mTimeAxisEntity.getCode())) {
                        List<TimeAxisBean> timeAxisInfoList = mTimeAxisEntity.getTimeAxisInfoList();
                        if (timeAxisInfoList != null) {
                            mTimeAxisList.addAll(timeAxisInfoList);
                            RecyclerViewScrollHelper.scrollToPosition(mRvGoods, 0);
                            //因为SlidingTabLayout对viewpager有依赖性，所以暂时创建空数据的viewpager进行关联
                            clearFragmentCache(getChildFragmentManager());
                            List<View> viewList = new ArrayList<>();
                            for (int i = 0; i < mTimeAxisList.size(); i++) {
                                ImageView imageView = new ImageView(getActivity());
                                imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                                viewList.add(imageView);
                            }
                            MyPagerAdapter myPagerAdapter = new MyPagerAdapter(viewList);
                            mViewPager.setAdapter(myPagerAdapter);
                            mViewPager.setOffscreenPageLimit(mTimeAxisList.size() - 1);
                            mSlidingTablayout.setViewPager(mViewPager, mTimeAxisList);
                        }
                    }
                }
                mTimeAxisAdapter.notifyDataSetChanged();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mTimeAxisList, mTimeAxisEntity);
                if (mTimeAxisAdapter.getFooterLayoutCount() == 0) {
                    mTimeAxisAdapter.addFooterView(mFootview);
                }
                getRecommendAd();
                getRecommendGoods();
            }

            @Override
            public void onNotNetOrException() {
                mSmartCommunalRefresh.finishRefresh();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mTimeAxisList, mTimeAxisEntity);
            }
        });
    }


    //获取种草帖子
    private void getRecommendGoods() {
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Url.GET_TIME_DOCUMENT_LIST, null, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mPostList.clear();
                PostEntity postEntity = GsonUtils.fromJson(result, PostEntity.class);
                if (postEntity != null) {
                    if (postEntity.getCode().equals(SUCCESS_CODE)) {
                        List<PostEntity.PostBean> postList = postEntity.getPostList();
                        if (postList != null && postList.size() > 0) {
                            mPostList.addAll(postList);
                        }
                    }
                }
                mTimePostAdapter.notifyDataSetChanged();
                mTimeAxisFootView.mTvMore.setVisibility(mPostList.size() > 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onNotNetOrException() {
                mTimeAxisFootView.mTvMore.setVisibility(mPostList.size() > 0 ? View.VISIBLE : View.GONE);
            }
        });
    }

    //获取推荐广告位
    private void getRecommendAd() {
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Url.GET_RECOMMNED_AD, null, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                postAdBeanList.clear();
                CommunalADActivityEntity adActivityEntity = GsonUtils.fromJson(result, CommunalADActivityEntity.class);
                if (adActivityEntity != null) {
                    if (adActivityEntity.getCode().equals(SUCCESS_CODE)) {
                        List<CommunalADActivityBean> adlist = adActivityEntity.getCommunalADActivityBeanList();
                        if (adlist != null && adlist.size() > 0) {
                            postAdBeanList.addAll(adlist);
                            if (cbViewHolderCreator == null) {
                                cbViewHolderCreator = new CBViewHolderCreator() {
                                    @Override
                                    public Holder createHolder(View itemView) {
                                        return new CommunalAdHolderView(itemView, getActivity(), mTimeAxisFootView.mAdRecommendBanner, true);
                                    }

                                    @Override
                                    public int getLayoutId() {
                                        return R.layout.layout_ad_image_video;
                                    }
                                };
                            }

                        }
                    }
                }
                mTimeAxisFootView.mAdRecommendBanner.setPages(getActivity(), cbViewHolderCreator, postAdBeanList)
                        .startTurning(getShowNumber(postAdBeanList.get(0).getShowTime()) * 1000);
                mTimeAxisFootView.mAdRecommendBanner.setVisibility(postAdBeanList.size() > 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onNotNetOrException() {
                mTimeAxisFootView.mAdRecommendBanner.setVisibility(postAdBeanList.size() > 0 ? View.VISIBLE : View.GONE);
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
