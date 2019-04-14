package com.amkj.dmsh.homepage.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.bean.DMLThemeEntity;
import com.amkj.dmsh.bean.DMLThemeEntity.DMLThemeBean;
import com.amkj.dmsh.constant.CommunalAdHolderView;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.homepage.adapter.HomeNewUserAdapter;
import com.amkj.dmsh.homepage.adapter.HomeTopAdapter;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity.CommunalADActivityBean;
import com.amkj.dmsh.homepage.bean.HomeNewUserEntity;
import com.amkj.dmsh.network.NetCacheLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.google.gson.Gson;
import com.luck.picture.lib.decoration.RecycleViewDivider;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.Unbinder;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getShowNumber;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.setSkipPath;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TEN;
import static com.amkj.dmsh.constant.Url.GTE_HOME_TOP;
import static com.amkj.dmsh.constant.Url.H_DML_THEME;
import static com.amkj.dmsh.constant.Url.Q_HOME_AD_LOOP;


/**
 * Created by xiaoxin on 2019/4/13 0013
 * Version:v4.0.0
 * ClassDescription :首页默认Frament(良品优选)
 */
public class HomeDefalutFragment extends BaseFragment {
    @BindView(R.id.cb_banner)
    ConvenientBanner mCbBanner;
    @BindView(R.id.rv_top)
    RecyclerView mRvTop;
    @BindView(R.id.smart_layout)
    SmartRefreshLayout mSmartLayout;
    @BindView(R.id.iv_cover)
    ImageView mIvCover;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_desc)
    TextView mTvDesc;
    @BindView(R.id.rv_new_goods)
    RecyclerView mRvNewGoods;
    Unbinder unbinder;
    @BindView(R.id.ll_new_user)
    LinearLayout mLlNewUser;
    @BindView(R.id.tv_welfare_title)
    TextView mTvWelfareTitle;
    @BindView(R.id.tv_welfare_desc)
    TextView mTvWelfareDesc;
    @BindView(R.id.rv_felware)
    RecyclerView mRvFelware;
    @BindView(R.id.ll_felware)
    LinearLayout mLlFelware;
    private boolean isUpdateCache;
    private CBViewHolderCreator cbViewHolderCreator;
    private List<CommunalADActivityBean> adBeanList = new ArrayList<>();
    private List<CommunalADActivityBean> mTopList = new ArrayList<>();
    private List<DMLThemeBean> mThemeList = new ArrayList<>();
    private List<HomeNewUserEntity.HomeNewUserBean> mNewUserGoodsList = new ArrayList<>();
    private HomeTopAdapter mHomeTopAdapter;
    private HomeNewUserAdapter mHomeNewUserAdapter;
    private HomeWelfareAdapter mHomeWelfareAdapter;

    @Override
    protected int getContentView() {
        return R.layout.fragment_home_default;
    }

    @Override
    protected void initViews() {
        //初始化Top适配器
        LinearLayoutManager topManager = new LinearLayoutManager(getActivity()
                , LinearLayoutManager.HORIZONTAL, false);
        mRvTop.setLayoutManager(topManager);
        mHomeTopAdapter = new HomeTopAdapter(getActivity(), mTopList);
        mRvTop.setAdapter(mHomeTopAdapter);
        mHomeTopAdapter.setOnItemClickListener((adapter, view, position) -> {
            CommunalADActivityBean communalADActivityBean = (CommunalADActivityBean) view.getTag();
            if (communalADActivityBean != null) {
                setSkipPath(getActivity(), communalADActivityBean.getAndroidLink(), false);
            }
        });
        mSmartLayout.setOnRefreshListener(refreshLayout -> {
            isUpdateCache = true;
            loadData();
        });


        //初始化新人专享适配器
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity()
                , 2);
        mRvNewGoods.setLayoutManager(gridLayoutManager);
        mRvNewGoods.addItemDecoration(new RecycleViewDivider(
                getActivity(), LinearLayoutManager.HORIZONTAL, AutoSizeUtils.mm2px(mAppContext, 29), getResources().getColor(R.color.white)));
        mHomeNewUserAdapter = new HomeNewUserAdapter(getActivity(), mNewUserGoodsList);
        mHomeNewUserAdapter.setOnItemClickListener((adapter, view, position) -> {
            //跳转商品详情
        });
        mIvCover.setOnClickListener(view -> {
            //跳转新人专区
        });
        mRvNewGoods.setAdapter(mHomeNewUserAdapter);

        //福利精选专题
        LinearLayoutManager felwareManager = new LinearLayoutManager(getActivity()
                , LinearLayoutManager.HORIZONTAL, false);
        mRvFelware.setLayoutManager(felwareManager);
        mRvFelware.addItemDecoration(new RecycleViewDivider(
                getActivity(), LinearLayoutManager.HORIZONTAL, AutoSizeUtils.mm2px(mAppContext, 22), getResources().getColor(R.color.white)));
        mHomeWelfareAdapter = new HomeWelfareAdapter(getActivity(), mThemeList);
        mRvFelware.setAdapter(mHomeWelfareAdapter);
    }

    @Override
    protected void loadData() {
        getAdLoop();
        getHomeIndexType();
        getNewUserGoods();
        getWelfare();
    }


    //获取Banner
    private void getAdLoop() {
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("vidoShow", "1");
        NetLoadUtils.getNetInstance().loadNetDataGetCache(getActivity(), Q_HOME_AD_LOOP, params, isUpdateCache, new NetCacheLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                getADJsonData(result);
            }
        });
    }

    private void getADJsonData(String result) {
        Gson gson = new Gson();
        CommunalADActivityEntity qualityAdLoop = gson.fromJson(result, CommunalADActivityEntity.class);
        if (qualityAdLoop != null) {
            List<CommunalADActivityBean> communalAdList = qualityAdLoop.getCommunalADActivityBeanList();
            if (communalAdList != null && communalAdList.size() > 0) {
                adBeanList.clear();
                adBeanList.addAll(qualityAdLoop.getCommunalADActivityBeanList());
                mCbBanner.setVisibility(View.VISIBLE);
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
                mCbBanner.setPages(getActivity(), cbViewHolderCreator, adBeanList).setCanLoop(true)
                        .setPointViewVisible(true).setCanScroll(true)
                        .setPageIndicator(new int[]{R.drawable.unselected_radius, R.drawable.selected_radius})
                        .startTurning(getShowNumber(adBeanList.get(0).getShowTime()) * 1000);
            } else {
                mCbBanner.setVisibility(View.GONE);
            }
        }
    }

    //第二栏
    private void getHomeIndexType() {
        NetLoadUtils.getNetInstance().loadNetDataGetCache(getActivity(), GTE_HOME_TOP, isUpdateCache, new NetCacheLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                CommunalADActivityEntity homeTypeEntity = gson.fromJson(result, CommunalADActivityEntity.class);
                if (homeTypeEntity != null) {
                    List<CommunalADActivityBean> topList = homeTypeEntity.getCommunalADActivityBeanList();
                    if (topList != null && topList.size() > 0) {
                        mTopList.clear();
                        mTopList.addAll(topList);
                        mHomeTopAdapter.notifyDataSetChanged();
                    } else if (!homeTypeEntity.getCode().equals(EMPTY_CODE)) {
                        showToast(getActivity(), homeTypeEntity.getMsg());
                    }
                }

                mRvTop.setVisibility(mTopList.size() > 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onNotNetOrException() {
                mRvTop.setVisibility(mTopList.size() > 0 ? View.VISIBLE : View.GONE);
            }
        });
    }

    //获取新人专享商品
    private void getNewUserGoods() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("source", 1);
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Url.GTE_NEW_USER_GOODS, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                HomeNewUserEntity homeTypeEntity = gson.fromJson(result, HomeNewUserEntity.class);
                if (homeTypeEntity != null) {
                    GlideImageLoaderUtil.loadImage(getActivity(), mIvCover, homeTypeEntity.getCover());
                    mTvTitle.setText(getStrings(homeTypeEntity.getTitle()));
                    mTvDesc.setText(getStrings(homeTypeEntity.getDesc()));
                    List<HomeNewUserEntity.HomeNewUserBean> homeNewUserGoods = homeTypeEntity.getHomeNewUserGoods();
                    if (homeNewUserGoods != null && homeNewUserGoods.size() > 0) {
                        mLlNewUser.setVisibility(View.VISIBLE);
                        mNewUserGoodsList.clear();
                        mNewUserGoodsList.add(homeNewUserGoods.get(0));
                        mNewUserGoodsList.add(homeNewUserGoods.get(1));
                        mHomeNewUserAdapter.notifyDataSetChanged();
                    }
                    mLlNewUser.setVisibility(mNewUserGoodsList.size() > 0 ? View.VISIBLE : View.GONE);

                }
            }

            @Override
            public void onNotNetOrException() {
                mLlNewUser.setVisibility(mNewUserGoodsList.size() > 0 ? View.VISIBLE : View.GONE);
            }
        });
    }

    //获取福利社商品
    private void getWelfare() {
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", 1);
        params.put("showCount", TOTAL_COUNT_TEN);
        params.put("goodsCurrentPage", 1);
        params.put("goodsShowCount", 8);
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), H_DML_THEME
                , params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        Gson gson = new Gson();
                        DMLThemeEntity dmlTheme = gson.fromJson(result, DMLThemeEntity.class);
                        if (dmlTheme != null && dmlTheme.getThemeList().size() > 0) {

                        }
                    }
                });
    }

}
