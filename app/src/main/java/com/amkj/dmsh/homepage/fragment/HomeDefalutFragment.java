package com.amkj.dmsh.homepage.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.CommonPagerAdapter;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.HomeWelfareEntity;
import com.amkj.dmsh.bean.HomeWelfareEntity.HomeWelfareBean;
import com.amkj.dmsh.bean.QualityTypeEntity;
import com.amkj.dmsh.bean.QualityTypeEntity.QualityTypeBean;
import com.amkj.dmsh.constant.CommunalAdHolderView;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dao.AddClickDao;
import com.amkj.dmsh.dominant.activity.DoMoLifeWelfareActivity;
import com.amkj.dmsh.dominant.adapter.GoodProductAdapter;
import com.amkj.dmsh.dominant.adapter.HomeCatergoryAdapter;
import com.amkj.dmsh.homepage.activity.ArticleOfficialActivity;
import com.amkj.dmsh.homepage.activity.ArticleTypeActivity;
import com.amkj.dmsh.homepage.activity.HomeCatergoryActivity;
import com.amkj.dmsh.homepage.activity.QualityGoodActivity;
import com.amkj.dmsh.homepage.adapter.HomeArticleNewAdapter;
import com.amkj.dmsh.homepage.adapter.HomeTopAdapter;
import com.amkj.dmsh.homepage.adapter.HomeWelfareAdapter;
import com.amkj.dmsh.homepage.adapter.HomeZoneAdapter;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity.CommunalADActivityBean;
import com.amkj.dmsh.homepage.bean.CommunalArticleEntity;
import com.amkj.dmsh.homepage.bean.CommunalArticleEntity.CommunalArticleBean;
import com.amkj.dmsh.homepage.bean.HomeCommonEntity;
import com.amkj.dmsh.homepage.bean.HomeCommonEntity.HomeCommonBean;
import com.amkj.dmsh.homepage.bean.HomeCommonEntity.ProductInfoListBean;
import com.amkj.dmsh.homepage.bean.HomeDynamicEntity;
import com.amkj.dmsh.network.NetCacheLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.user.bean.UserLikedProductEntity;
import com.amkj.dmsh.user.bean.LikedProductBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tencent.bugly.beta.tinker.TinkerManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getShowNumber;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.CATEGORY_ID;
import static com.amkj.dmsh.constant.ConstantVariable.CATEGORY_NAME;
import static com.amkj.dmsh.constant.ConstantVariable.CATEGORY_PID;
import static com.amkj.dmsh.constant.ConstantVariable.CATEGORY_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TEN;
import static com.amkj.dmsh.constant.Url.CATE_DOC_LIST;
import static com.amkj.dmsh.constant.Url.GTE_HOME_TOP;
import static com.amkj.dmsh.constant.Url.H_AD_LIST;
import static com.amkj.dmsh.constant.Url.H_DML_THEME;
import static com.amkj.dmsh.constant.Url.QUALITY_SHOP_GOODS_PRO;
import static com.amkj.dmsh.constant.Url.Q_PRODUCT_TYPE_LIST;
import static com.amkj.dmsh.dao.AddClickDao.addDynamicClick;


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
    @BindView(R.id.iv_dynamic_cover)
    ImageView mIvCover;
    @BindView(R.id.ll_dynamic)
    RelativeLayout mLlDynamic;
    @BindView(R.id.vp_welfare)
    ViewPager mVpFelware;
    @BindView(R.id.rv_nice)
    RecyclerView mRvNice;
    @BindView(R.id.ll_felware)
    LinearLayout mLlFelware;
    @BindView(R.id.ll_nice)
    LinearLayout mLlNice;
    @BindView(R.id.rv_artical)
    RecyclerView mRvArtical;
    @BindView(R.id.ll_artical)
    LinearLayout mLlArtical;
    @BindView(R.id.tv_dynamic_title)
    TextView mTvDynamicTitle;
    @BindView(R.id.tv_dynamic_desc)
    TextView mTvDynamicDesc;
    @BindView(R.id.tv_more_nice_topic)
    TextView mTvMoreNiceTopic;
    @BindView(R.id.tv_artical_topic)
    TextView mTvArticalTopic;
    @BindView(R.id.tv_refresh_artical)
    TextView mTvRefreshArtical;
    @BindView(R.id.rv_product)
    RecyclerView mRvProduct;
    @BindView(R.id.iv_dynamic_cover_left)
    ImageView mIvDynamicCoverLeft;
    @BindView(R.id.tv_dynamic_price_left)
    TextView mTvDynamicPriceLeft;
    @BindView(R.id.iv_dynamic_cover_right)
    ImageView mIvDynamicCoverRight;
    @BindView(R.id.tv_dynamic_priceright)
    TextView mTvDynamicPriceright;
    @BindView(R.id.rv_special_zone)
    RecyclerView mRvSpecialZone;
    @BindView(R.id.download_btn_communal)
    FloatingActionButton download_btn_communal;
    @BindView(R.id.scrollview)
    NestedScrollView mScrollview;
    private CBViewHolderCreator cbViewHolderCreator;
    private HomeCommonEntity mHomeCommonEntity;
    private HomeWelfareEntity mHomeWelfareEntity;
    private CommunalArticleEntity mCommunalArticleEntity;
    private HomeDynamicEntity mHomeDynamicEntity;
    private UserLikedProductEntity mUserLikedProductEntity;
    private HomeCatergoryAdapter mHomeCatergoryAdapter;
    private HomeZoneAdapter mHomeZoneAdapter;
    private HomeTopAdapter mHomeTopAdapter;
    private GoodProductAdapter qualityGoodNewProAdapter;
    private HomeArticleNewAdapter homeArticleAdapter;
    private List<CommunalADActivityBean> adBeanList = new ArrayList<>();
    private List<HomeCommonBean> mTopList = new ArrayList<>();
    private List<HomeWelfareBean> mThemeList = new ArrayList<>();
    private List<HomeCommonBean> mZoneList = new ArrayList<>();
    private List<LikedProductBean> goodsProList = new ArrayList<>();
    private List<CommunalArticleBean> articleTypeList = new ArrayList<>();
    private List<CommunalArticleBean> articleTypeAllList = new ArrayList<>();
    private List<QualityTypeBean> qualityTypeList = new ArrayList<>();
    private List<UserLikedProductEntity> mProductList = new ArrayList<>();
    private int articalPage = 1;
    private int mCatergoryPage = 0;
    private boolean isUpdateCache;
    private CommonPagerAdapter mHomeWelfareAdapter;
    private boolean isFirst = true;
    private GridLayoutManager mTopManager;

    @Override
    protected int getContentView() {
        return R.layout.fragment_home_default;
    }

    @Override
    protected void initViews() {
        int screenHeight = ((TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike()).getScreenHeight();
        mSmartLayout.setEnableLoadMore(true);
        mSmartLayout.setOnRefreshListener(refreshLayout -> {
            isUpdateCache = true;
            articalPage = 1;
            mCatergoryPage = 0;
            mProductList.clear();
            mHomeCatergoryAdapter.notifyDataSetChanged();
            loadData();
        });
        //一键回到顶部
        mScrollview.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (nestedScrollView, i, newY, i2, oldY) -> {
            if (newY > screenHeight * 1.5) {
                if (download_btn_communal.getVisibility() == GONE) {
                    download_btn_communal.setVisibility(VISIBLE);
                    download_btn_communal.show(false);
                }
                if (!download_btn_communal.isVisible()) {
                    download_btn_communal.show(false);
                }
            } else {
                if (download_btn_communal.isVisible()) {
                    download_btn_communal.hide(false);
                }
            }
        });
        download_btn_communal.setOnClickListener(v -> {
            mScrollview.fling(0);
            mScrollview.scrollTo(0, 0);
            download_btn_communal.hide(false);
        });
        //初始化Top适配器
        mTopManager = new GridLayoutManager(getActivity(), 5);
        mRvTop.setLayoutManager(mTopManager);
        mHomeTopAdapter = new HomeTopAdapter(getActivity(), mTopList);
        mRvTop.setAdapter(mHomeTopAdapter);
        mHomeTopAdapter.setOnItemClickListener((adapter, view, position) -> {
            HomeCommonBean homeCommonBean = (HomeCommonBean) view.getTag();
            if (homeCommonBean != null) {
                AddClickDao.adClickTotal(getActivity(), homeCommonBean.getLink(), homeCommonBean.getId(),false);
            }
        });

        //初始化专区适配器
        GridLayoutManager speicalZoneManager = new GridLayoutManager(getActivity()
                , 2);
        mRvSpecialZone.setLayoutManager(speicalZoneManager);
        mHomeZoneAdapter = new HomeZoneAdapter(getActivity(), mZoneList);
        mHomeZoneAdapter.setOnItemClickListener((adapter, view, position) -> {
            HomeCommonBean homeCommonBean = (HomeCommonBean) view.getTag();
            //跳转对应专区
            if (!TextUtils.isEmpty(homeCommonBean.getLink())) {
                AddClickDao.adClickTotal(getActivity(), homeCommonBean.getLink(), homeCommonBean.getId(),false);
            }
        });
        mRvSpecialZone.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_gray_f_one_px)
                .create());
        mRvSpecialZone.setNestedScrollingEnabled(false);
        mRvSpecialZone.setAdapter(mHomeZoneAdapter);

        //初始化精选专题（福利社）适配器
        mHomeWelfareAdapter = new HomeWelfareAdapter(getActivity(), mThemeList);
        mVpFelware.setAdapter(mHomeWelfareAdapter);
        mVpFelware.setPageMargin(AutoSizeUtils.mm2px(mAppContext, 22));
        mVpFelware.setOffscreenPageLimit(mHomeWelfareAdapter.getCount() - 1);

        //初始化好物适配器
        GridLayoutManager niceManager = new GridLayoutManager(getActivity()
                , 2);
        mRvNice.setLayoutManager(niceManager);
        mRvNice.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_gray_f)
                .create());
        mRvNice.setNestedScrollingEnabled(false);
        qualityGoodNewProAdapter = new GoodProductAdapter((getActivity()), goodsProList);
        mRvNice.setAdapter(qualityGoodNewProAdapter);

        //初始化文章适配器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRvArtical.setLayoutManager(linearLayoutManager);
        homeArticleAdapter = new HomeArticleNewAdapter(getActivity(), articleTypeList);
        homeArticleAdapter.setOnItemClickListener((adapter, view, position) -> {
            CommunalArticleBean communalArticleBean = (CommunalArticleBean) view.getTag();
            if (communalArticleBean != null) {
                Intent intent = new Intent(getActivity(), ArticleOfficialActivity.class);
                intent.putExtra("ArtId", String.valueOf(communalArticleBean.getId()));
                startActivity(intent);
            }
        });
        mRvArtical.setAdapter(homeArticleAdapter);

        //获取首页分类商品适配器
        LinearLayoutManager productManager = new LinearLayoutManager(getActivity());
        mRvProduct.setLayoutManager(productManager);
        mRvProduct.setNestedScrollingEnabled(false);
        mHomeCatergoryAdapter = new HomeCatergoryAdapter(getActivity(), mProductList);
        mRvProduct.setAdapter(mHomeCatergoryAdapter);
        mHomeCatergoryAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.rl_more_product) {
                UserLikedProductEntity entity = (UserLikedProductEntity) view.getTag();
                Intent intent = new Intent(getActivity(), HomeCatergoryActivity.class);
                intent.putExtra(CATEGORY_NAME, entity.getCatergoryName());
                intent.putExtra(CATEGORY_PID, entity.getId());
                intent.putExtra(CATEGORY_ID, entity.getPid());
                intent.putExtra(CATEGORY_TYPE, entity.getType());
                startActivity(intent);
            }
        });
        mSmartLayout.setOnLoadMoreListener(refreshLayout -> {
            mCatergoryPage = mCatergoryPage + 3;
            getProduct();
        });
    }

    @Override
    protected void loadData() {
        getAdLoop();
        getTop();
        getDynamic();
        getWelfare();
        getSpecialZone();
        getGoodsPro();
        getArticleTypeList(false);
        getProductTypeList();
    }


    //获取Banner
    private void getAdLoop() {
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("vidoShow", "1");
        NetLoadUtils.getNetInstance().loadNetDataGetCache(getActivity(), H_AD_LIST, params, isUpdateCache, new NetCacheLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                getADJsonData(result);
            }
        });
    }

    private void getADJsonData(String result) {
        CommunalADActivityEntity qualityAdLoop = GsonUtils.fromJson(result, CommunalADActivityEntity.class);
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
                            return new CommunalAdHolderView(itemView, getActivity(), mCbBanner, true);
                        }

                        @Override
                        public int getLayoutId() {
                            return R.layout.layout_ad_image_video;
                        }
                    };
                }
                mCbBanner.setPages(getActivity(), cbViewHolderCreator, adBeanList)
                        .startTurning(getShowNumber(adBeanList.get(0).getShowTime()) * 1000);
            } else {
                mCbBanner.setVisibility(View.GONE);
            }
        }
    }

    //Top活动位
    private void getTop() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        NetLoadUtils.getNetInstance().loadNetDataGetCache(getActivity(), GTE_HOME_TOP, map, isUpdateCache, new NetCacheLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {

                HomeCommonEntity homeNavbarEntity = GsonUtils.fromJson(result, HomeCommonEntity.class);
                if (homeNavbarEntity != null) {
                    List<HomeCommonBean> topList = homeNavbarEntity.getResult();
                    if (topList != null && topList.size() > 0) {
                        mTopList.clear();
                        for (int i = 0; i < (topList.size() > 5 ? 5 : topList.size()); i++) {
                            mTopList.add(topList.get(i));
                        }

                        mTopManager.setSpanCount(mTopList.size());
                        mHomeTopAdapter.notifyDataSetChanged();
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

    //动态专区(新人专享)
    private void getDynamic() {
        HashMap<String, Object> map = new HashMap<>();
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Url.GTE_HOME_DYNAMIC_AREA, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {

                mHomeDynamicEntity = GsonUtils.fromJson(result, HomeDynamicEntity.class);
                if (mHomeDynamicEntity != null) {
                    GlideImageLoaderUtil.loadImage(getActivity(), mIvCover, mHomeDynamicEntity.getCover());
                    mTvDynamicTitle.setText(getStrings(mHomeDynamicEntity.getTitle()));
                    mTvDynamicDesc.setText(getStrings(mHomeDynamicEntity.getDescription()));
                    List<ProductInfoListBean> productInfoList = mHomeDynamicEntity.getProductInfoList();
                    if (productInfoList != null && productInfoList.size() > 0) {
                        ProductInfoListBean productInfoListBean = productInfoList.get(0);
                        GlideImageLoaderUtil.loadImage(getActivity(), mIvDynamicCoverLeft, productInfoListBean.getCover());
                        mTvDynamicPriceLeft.setText(ConstantMethod.getRmbFormat(getActivity(), productInfoListBean.getPrice()));
                        if (productInfoList.size() > 1) {
                            productInfoListBean = productInfoList.get(1);
                            GlideImageLoaderUtil.loadImage(getActivity(), mIvDynamicCoverRight, productInfoListBean.getCover());
                            mTvDynamicPriceright.setText(ConstantMethod.getRmbFormat(getActivity(), productInfoListBean.getPrice()));
                        }
                    }

                    mLlDynamic.setVisibility("1".equals(mHomeDynamicEntity.getIsDisplay()) ? View.VISIBLE : View.GONE);
                }
            }

            @Override
            public void onNotNetOrException() {
                if (mHomeDynamicEntity != null) {
                    mLlDynamic.setVisibility("1".equals(mHomeDynamicEntity.getIsDisplay()) ? View.VISIBLE : View.GONE);
                }
            }
        });
    }

    //获取并排专区
    private void getSpecialZone() {
        Map<String, Object> map = new HashMap<>();
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Url.GTE_HOME_SPECIAL_ZONE, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {

                mHomeCommonEntity = GsonUtils.fromJson(result, HomeCommonEntity.class);
                List<HomeCommonBean> homeCommonBeanList = mHomeCommonEntity.getResult();
                if (mHomeCommonEntity != null) {
                    if (homeCommonBeanList != null && homeCommonBeanList.size() > 0) {
                        mZoneList.clear();
                        for (int i = 0; i < homeCommonBeanList.size(); i++) {
                            List<ProductInfoListBean> productInfoList = homeCommonBeanList.get(i).getProductInfoList();
                            if (productInfoList != null && productInfoList.size() > 0) {
                                mZoneList.add(homeCommonBeanList.get(i));
                            }
                        }
                        mHomeZoneAdapter.notifyDataSetChanged();
                    }
                }
                mRvSpecialZone.setVisibility(mZoneList.size() > 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onNotNetOrException() {
                mRvSpecialZone.setVisibility(mZoneList.size() > 0 ? View.VISIBLE : View.GONE);
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

                        mHomeWelfareEntity = GsonUtils.fromJson(result, HomeWelfareEntity.class);
                        List<HomeWelfareBean> themeList = mHomeWelfareEntity.getResult();
                        if (mHomeWelfareEntity != null) {
                            if (themeList != null && themeList.size() > 0) {
                                mThemeList.clear();
                                mThemeList.addAll(themeList);
                                mHomeWelfareAdapter = new HomeWelfareAdapter(getActivity(), mThemeList);
                                mVpFelware.setAdapter(mHomeWelfareAdapter);
                            }
                        }

                        mLlFelware.setVisibility(mThemeList.size() > 0 ? View.VISIBLE : View.GONE);
                    }

                    @Override
                    public void onNotNetOrException() {
                        mLlFelware.setVisibility(mThemeList.size() > 0 ? View.VISIBLE : View.GONE);
                    }
                });
    }

    //获取好物商品
    private void getGoodsPro() {
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", 1);
        /**
         * version 1 区分是否带入广告页
         */
        params.put("version", 1);
        if (userId > 0) {
            params.put("uid", userId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), QUALITY_SHOP_GOODS_PRO
                , params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        mSmartLayout.finishRefresh();
                        UserLikedProductEntity userLikedProductEntity = GsonUtils.fromJson(result, UserLikedProductEntity.class);
                        if (userLikedProductEntity != null) {
                            List<LikedProductBean> goodProductList = userLikedProductEntity.getGoodsList();
                            if (goodProductList != null && goodProductList.size() > 0) {
                                goodsProList.clear();
                                for (int i = 0; i < (goodProductList.size() > 6 ? 6 : goodProductList.size()); i++) {
                                    goodsProList.add(goodProductList.get(i));
                                }
                            }
                        }
                        qualityGoodNewProAdapter.notifyDataSetChanged();
                        mLlNice.setVisibility(goodsProList.size() > 0 ? View.VISIBLE : View.GONE);
                    }

                    @Override
                    public void onNotNetOrException() {
                        mSmartLayout.finishRefresh();
                        mLlNice.setVisibility(goodsProList.size() > 0 ? View.VISIBLE : View.GONE);
                    }
                });
    }

    //获取全部类型文章
    private void getArticleTypeList(boolean isClick) {
        if (isClick && loadHud != null) {
            loadHud.show();
        }
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", articalPage);
        params.put("shouCount", 10);
        if (userId > 0) {
            params.put("uid", userId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), CATE_DOC_LIST, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {

                mCommunalArticleEntity = GsonUtils.fromJson(result, CommunalArticleEntity.class);
                if (mCommunalArticleEntity != null) {
                    List<CommunalArticleBean> communalArticleList = mCommunalArticleEntity.getCommunalArticleList();
                    if (communalArticleList != null && communalArticleList.size() >= 2) {
                        articleTypeAllList.clear();
                        articleTypeAllList.addAll(communalArticleList);
                        setArticalData();
                    }
                }
                loadHud.dismiss();
                mLlArtical.setVisibility(articleTypeList.size() > 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onNotNetOrException() {
                loadHud.dismiss();
                mLlArtical.setVisibility(articleTypeList.size() > 0 ? View.VISIBLE : View.GONE);
            }
        });
    }

    //获取商品分类集合
    public void getProductTypeList() {
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Url.HOME_CATERGORY_ONE_LIST, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                QualityTypeEntity qualityTypeEntity = GsonUtils.fromJson(result, QualityTypeEntity.class);
                if (qualityTypeEntity != null) {
                    List<QualityTypeBean> typeBeanList = qualityTypeEntity.getQualityTypeBeanList();
                    if (typeBeanList != null && typeBeanList.size() > 0) {
                        qualityTypeList.clear();
                        for (int i = 0; i < typeBeanList.size(); i++) {
                            QualityTypeBean qualityTypeBean = typeBeanList.get(i);
                            if (qualityTypeBean.getType() != 4) {
                                qualityTypeList.add(qualityTypeBean);
                            }
                        }
                        mCatergoryPage = mCatergoryPage + 3;
                        getProduct();
                    }
                }
            }
        });

    }


    //获取分类对应的商品
    private void getProduct() {
        for (int i = mCatergoryPage - 3; i < mCatergoryPage; i++) {
            int catergoryPage = i;
            if (i > qualityTypeList.size() - 1) {
                if (mCatergoryPage - qualityTypeList.size() >= 3) {
                    mSmartLayout.finishLoadMoreWithNoMoreData();
                    mSmartLayout.resetNoMoreData();
                }
                return;
            }

            QualityTypeBean qualityTypeBean = qualityTypeList.get(i);
            Map<String, Object> params = new HashMap<>();
            params.put("currentPage", 1);
            params.put("showCount", 6);
            if (!TextUtils.isEmpty(qualityTypeBean.getChildCategory())) {
                params.put("id", qualityTypeBean.getChildCategory());
                params.put("pid", qualityTypeBean.getId());
            } else {
                params.put("id", qualityTypeBean.getId());
                params.put("pid", 0);
            }
            params.put("orderTypeId", "1");
            NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Q_PRODUCT_TYPE_LIST,
                    params, new NetLoadListenerHelper() {
                        @Override
                        public void onSuccess(String result) {

                            mUserLikedProductEntity = GsonUtils.fromJson(result, UserLikedProductEntity.class);
                            if (mUserLikedProductEntity != null) {
                                mUserLikedProductEntity.setCatergoryName(qualityTypeBean.getName());
                                mUserLikedProductEntity.setId(String.valueOf(qualityTypeBean.getId()));
                                mUserLikedProductEntity.setPid(String.valueOf(qualityTypeBean.getPid()));
                                mUserLikedProductEntity.setType(String.valueOf(qualityTypeBean.getType()));
                                mUserLikedProductEntity.setPosition(catergoryPage);
                                List<LikedProductBean> likedProductBeanList = mUserLikedProductEntity.getGoodsList();
                                if (qualityTypeBean.getAd() != null) {
                                    List<UserLikedProductEntity.AdBean> ad = qualityTypeBean.getAd();
                                    mUserLikedProductEntity.setAdList(ad);
                                }

                                if (likedProductBeanList != null && likedProductBeanList.size() > 0 && ConstantVariable.SUCCESS_CODE.equals(mUserLikedProductEntity.getCode())) {
                                    //虽然showCount = 6，但是由于置顶商品的原因，返回的商品数量可能会超过showCount控制的数量
                                    mUserLikedProductEntity.setLikedProductBeanList(likedProductBeanList.subList(0, likedProductBeanList.size() > 6 ? 6 : likedProductBeanList.size()));
                                    mProductList.add(mUserLikedProductEntity);
                                    //按照分类名称排序，因为同步调用多个接口无法保证顺序
                                    Collections.sort(mProductList);
                                    mSmartLayout.finishLoadMore();
                                    mHomeCatergoryAdapter.notifyDataSetChanged();
                                } else if (ConstantVariable.ERROR_CODE.equals(mUserLikedProductEntity.getCode())) {
                                    mSmartLayout.finishLoadMore(false);
                                } else {
                                    mSmartLayout.finishLoadMore();
                                }
                            } else {
                                mSmartLayout.finishLoadMore(false);
                            }
                        }

                        @Override
                        public void onNotNetOrException() {
                            mSmartLayout.finishLoadMore(false);
                        }
                    });
        }
    }

    //设置文章列表数据并刷新
    private void setArticalData() {
        articleTypeList.clear();
        articleTypeList.add(articleTypeAllList.get(0));
        articleTypeList.add(articleTypeAllList.get(1));
        articleTypeAllList.removeAll(articleTypeList);
        homeArticleAdapter.notifyDataSetChanged();

    }

    @OnClick({R.id.rl_more_welfare_topic, R.id.rl_more_nice_topic, R.id.tv_more_nice_topic, R.id.rl_more_artical, R.id.tv_refresh_artical, R.id.ll_dynamic})
    public void onViewClicked(View view) {
        Intent intent;
        if (getActivity() == null) return;
        switch (view.getId()) {
            //跳转福利社列表
            case R.id.rl_more_welfare_topic:
                intent = new Intent(getActivity(), DoMoLifeWelfareActivity.class);
                startActivity(intent);
                break;
            //跳转好物列表
            case R.id.rl_more_nice_topic:
            case R.id.tv_more_nice_topic:
                intent = new Intent(getActivity(), QualityGoodActivity.class);
                startActivity(intent);
                break;
            //跳转文章列表
            case R.id.rl_more_artical:
                intent = new Intent(getActivity(), ArticleTypeActivity.class);
                startActivity(intent);
                break;
            //换一批文章
            case R.id.tv_refresh_artical:
                if (articleTypeAllList.size() >= 2) {
                    setArticalData();
                } else {
                    //加载下一页数据
                    articalPage++;
                    if (loadHud != null) {
                        loadHud.show();
                    }
                    getArticleTypeList(true);
                }
                break;
            //动态专区
            case R.id.ll_dynamic:
                if (mHomeDynamicEntity != null) {
                    addDynamicClick(getActivity(), mHomeDynamicEntity.getLink(),mHomeDynamicEntity.getId());
                }
                break;
        }
    }

    @Override
    public void onVisible() {
        super.onVisible();
        if (!isFirst) {
            getDynamic();
        }
        isFirst = false;
    }
}
