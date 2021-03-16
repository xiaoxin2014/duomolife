package com.amkj.dmsh.homepage.activity;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
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
import com.amkj.dmsh.time.adapter.BrandAdapter;
import com.amkj.dmsh.time.adapter.SingleProductAdapter;
import com.amkj.dmsh.time.adapter.TimePostAdapter;
import com.amkj.dmsh.time.bean.AllGroupEntity;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.amkj.dmsh.views.convenientbanner.ConvenientBanner;
import com.amkj.dmsh.views.convenientbanner.holder.CBViewHolderCreator;
import com.amkj.dmsh.views.convenientbanner.holder.Holder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.getShowNumber;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;


/**
 * Created by xiaoxin on 2020/9/23
 * Version:v4.8.0
 * ClassDescription :淘好货首页
 */
public class TimeShowNewActivity extends BaseActivity {


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
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout mSmartCommunalRefresh;
    @BindView(R.id.rv_single)
    RecyclerView mRvSingle;
    @BindView(R.id.rv_brand)
    RecyclerView mRvBrand;
    @BindView(R.id.ad_recommend_banner)
    ConvenientBanner mAdRecommendBanner;
    @BindView(R.id.rv_recommend)
    RecyclerView mRvRecommend;
    @BindView(R.id.tv_more)
    TextView mTvMore;

    private boolean isUpdateCache;
    private CBViewHolderCreator cbViewHolderCreator;
    private AllGroupEntity mTimeAxisEntity;
    private List<CommunalADActivityBean> adBeanList = new ArrayList<>();
    private List<CommunalADActivityBean> postAdBeanList = new ArrayList<>();
    private List<PostEntity.PostBean> mPostList = new ArrayList<>();
    private List<AllGroupEntity.BrandProductBean> mProductList = new ArrayList<>();
    private List<AllGroupEntity.BrandBean> mTopicList = new ArrayList<>();
    private SingleProductAdapter mSingleProductAdapter;
    private BrandAdapter mBrandAdapter;
    private TimePostAdapter mTimePostAdapter;

    @Override
    protected int getContentView() {
        return R.layout.fragment_time_show_shaft_new;
    }

    @Override
    protected void initViews() {
        mTvLifeBack.setVisibility(View.GONE);
        mTvHeaderShared.setVisibility(View.GONE);
        mTvHeaderTitle.setText("淘好货");

        mSmartCommunalRefresh.setOnRefreshListener(refreshLayout -> {
            isUpdateCache = true;
            loadData();
        });

        //初始化单品列表
        ItemDecoration itemDecoration = new ItemDecoration.Builder()
                .setDividerId(R.drawable.item_divider_five_gray_f)
                .create();
        mRvSingle.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mRvSingle.addItemDecoration(itemDecoration);
        mSingleProductAdapter = new SingleProductAdapter(getActivity(), mProductList);
        mRvSingle.setAdapter(mSingleProductAdapter);

        //初始化品牌团列表
        mRvBrand.setVisibility(View.VISIBLE);
        mRvBrand.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBrandAdapter = new BrandAdapter(getActivity(), mTopicList);
        mRvBrand.setAdapter(mBrandAdapter);

        //初始化推荐帖子列表
        ItemDecoration newGridItemDecoration = new ItemDecoration.Builder()
                .setDividerId(R.drawable.item_divider_five_gray_f)
                .create();
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        mRvRecommend.setLayoutManager(layoutManager);
        mRvRecommend.addItemDecoration(newGridItemDecoration);
        mTimePostAdapter = new TimePostAdapter(getActivity(), mPostList, true);
        mRvRecommend.setAdapter(mTimePostAdapter);
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
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Url.GET_ALL_GROUP_INFO, null, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mSmartCommunalRefresh.finishRefresh();
                mProductList.clear();
                mTopicList.clear();
                mTimeAxisEntity = GsonUtils.fromJson(result, AllGroupEntity.class);
                if (mTimeAxisEntity != null) {
                    if (SUCCESS_CODE.equals(mTimeAxisEntity.getCode())) {
                        List<AllGroupEntity.BrandProductBean> productList = mTimeAxisEntity.getProductList();
                        List<AllGroupEntity.BrandBean> topicList = mTimeAxisEntity.getTopicList();
                        if (productList != null) {
                            mProductList.addAll(productList);
                        }

                        if (topicList != null) {
                            mTopicList.addAll(topicList);
                        }
                    }
                }
                mSingleProductAdapter.notifyDataSetChanged();
                mBrandAdapter.notifyDataSetChanged();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mProductList.size() > 0 || mTopicList.size() > 0, mTimeAxisEntity);
                getRecommendAd();
                getRecommendGoods();
            }

            @Override
            public void onNotNetOrException() {
                mSmartCommunalRefresh.finishRefresh();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mProductList.size() > 0 || mTopicList.size() > 0, mTimeAxisEntity);
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
                mTvMore.setVisibility(mPostList.size() > 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onNotNetOrException() {
                mTvMore.setVisibility(mPostList.size() > 0 ? View.VISIBLE : View.GONE);
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
                                        return new CommunalAdHolderView(itemView, getActivity(), mAdRecommendBanner, true);
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
                mAdRecommendBanner.setPages(getActivity(), cbViewHolderCreator, postAdBeanList)
                        .startTurning(getShowNumber(postAdBeanList.get(0).getShowTime()) * 1000);
                mAdRecommendBanner.setVisibility(postAdBeanList.size() > 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onNotNetOrException() {
                mAdRecommendBanner.setVisibility(postAdBeanList.size() > 0 ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @OnClick({R.id.tv_life_back, R.id.ll_rule, R.id.tv_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_life_back:
                finish();
                break;
            case R.id.ll_rule:
                Intent intent = new Intent(getActivity(), WebRuleCommunalActivity.class);
                intent.putExtra(ConstantVariable.WEB_VALUE_TYPE, ConstantVariable.WEB_TYPE_GROUP_BUY);
                startActivity(intent);
                break;
            case R.id.tv_more:
                Intent intent1 = new Intent(getActivity(), TimePostCatergoryActivity.class);
                startActivity(intent1);
                break;
        }
    }
}

