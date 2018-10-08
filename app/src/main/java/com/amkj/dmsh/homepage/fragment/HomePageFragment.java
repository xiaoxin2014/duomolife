package com.amkj.dmsh.homepage.fragment;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.base.NetLoadUtils;
import com.amkj.dmsh.bean.CategoryTypeEntity;
import com.amkj.dmsh.bean.CategoryTypeEntity.CategoryTypeBean;
import com.amkj.dmsh.bean.HomeQualityFloatAdEntity;
import com.amkj.dmsh.constant.CommunalAdHolderView;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.homepage.activity.HomePageSearchActivity;
import com.amkj.dmsh.homepage.adapter.HomeArticleTypeAdapter;
import com.amkj.dmsh.homepage.adapter.HomeImgActivityAdapter;
import com.amkj.dmsh.homepage.adapter.RecyclerHotAdapterNew;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity.CommunalADActivityBean;
import com.amkj.dmsh.homepage.bean.MarqueeTextEntity;
import com.amkj.dmsh.message.bean.MessageTotalEntity;
import com.amkj.dmsh.message.bean.MessageTotalEntity.MessageTotalBean;
import com.amkj.dmsh.snackbar.BaseTransientBottomBar;
import com.amkj.dmsh.snackbar.Snackbar;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.inteface.MyCacheCallBack;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.amkj.dmsh.views.MarqueeTextView;
import com.amkj.dmsh.views.SystemBarHelper;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.flyco.tablayout.SlidingTabLayout;
import com.google.gson.Gson;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.xutils.ex.HttpException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;
import me.leolin.shortcutbadger.ShortcutBadger;
import q.rorbin.badgeview.Badge;

import static android.app.Activity.RESULT_OK;
import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.adClickTotal;
import static com.amkj.dmsh.constant.ConstantMethod.getShowNumber;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getTopBadge;
import static com.amkj.dmsh.constant.ConstantMethod.setSkipPath;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.REFRESH_MESSAGE_TOTAL;
import static com.amkj.dmsh.constant.ConstantVariable.SEARCH_ALL;
import static com.amkj.dmsh.constant.ConstantVariable.SEARCH_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.START_AUTO_PAGE_TURN;
import static com.amkj.dmsh.constant.ConstantVariable.STOP_AUTO_PAGE_TURN;
import static com.amkj.dmsh.constant.Url.BASE_URL;

;
;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/7/28
 * class description:二期首页
 */
public class HomePageFragment extends BaseFragment {
    @BindView(R.id.smart_refresh_home)
    SmartRefreshLayout smart_refresh_home;
    @BindView(R.id.communal_recycler_wrap)
    RecyclerView communal_recycler_wrap;
    @BindView(R.id.rv_home_activity)
    RecyclerView rv_home_activity;
    //    滚动至顶部
    @BindView(R.id.std_home_art_type)
    public SlidingTabLayout std_home_art_type;
    @BindView(R.id.viewpager_container)
    public ViewPager viewpager_container;
    @BindView(R.id.home_title_collapsing_view)
    public CollapsingToolbarLayout home_title_collapsing_view;
    @BindView(R.id.ab_layout)
    public AppBarLayout ab_layout;
    @BindView(R.id.rel_communal_banner)
    public RelativeLayout rel_communal_banner;
    @BindView(R.id.ad_communal_banner)
    public ConvenientBanner ad_communal_banner;
    @BindView(R.id.fra_home_message_total)
    FrameLayout fra_home_message_total;
    @BindView(R.id.iv_home_message_total)
    ImageView iv_home_message_total;
    //    跑马灯布局
    @BindView(R.id.ll_home_marquee)
    LinearLayout ll_home_marquee;
    @BindView(R.id.tv_marquee_text)
    MarqueeTextView tv_marquee_text;
    @BindView(R.id.tb_tool_home)
    Toolbar tb_tool_home;
    @BindView(R.id.rel_home_page)
    RelativeLayout rel_home_page;
    @BindView(R.id.iv_float_ad_icon)
    ImageView iv_float_ad_icon;
    private Badge badge;
    private List<CategoryTypeBean> categoryList = new ArrayList<>();
    private List<CommunalADActivityBean> adBeanList = new ArrayList<>();
    private List<CommunalADActivityBean> hotActivityList = new ArrayList<>();
    private List<CommunalADActivityBean> activityList = new ArrayList<>();
    //    热门活动
    private RecyclerHotAdapterNew hotActivityAdapter;
    private HomeImgActivityAdapter homeImgActivityAdapter;
    private boolean isAutoClose;
    private CBViewHolderCreator cbViewHolderCreator;

    @Override
    protected int getContentView() {
        return R.layout.activity_homepage_new;
    }

    @Override
    protected void initViews() {
        smart_refresh_home.setOnRefreshListener((refreshLayout) -> {
            loadData();
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity()
                , LinearLayoutManager.HORIZONTAL, false);
        communal_recycler_wrap.setLayoutManager(linearLayoutManager);
        hotActivityAdapter = new RecyclerHotAdapterNew(getActivity(), hotActivityList);
        communal_recycler_wrap.setAdapter(hotActivityAdapter);
        hotActivityAdapter.setOnItemClickListener((adapter, view, position) -> {
            CommunalADActivityBean communalADActivityBean = (CommunalADActivityBean) view.getTag();
            if (communalADActivityBean != null) {
                setSkipPath(getActivity(), communalADActivityBean.getAndroidLink(), false);
            }
        });
        rv_home_activity.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        rv_home_activity.addItemDecoration(new PinnedHeaderItemDecoration.Builder(-1)
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_img_white)
                // 开启绘制分隔线，默认关闭
                .enableDivider(true)
                // 是否关闭标签点击事件，默认开启
                .disableHeaderClick(false)
                // 设置标签和其内部的子控件的监听，若设置点击监听不为null，但是disableHeaderClick(true)的话，还是不会响应点击事件
                .setHeaderClickListener(null)
                .create());
        homeImgActivityAdapter = new HomeImgActivityAdapter(getActivity(), activityList);
        rv_home_activity.setAdapter(homeImgActivityAdapter);
        homeImgActivityAdapter.setOnItemClickListener((adapter, view, position) -> {
            CommunalADActivityBean communalADActivityBean = (CommunalADActivityBean) view.getTag();
            if (communalADActivityBean != null) {
                setSkipPath(getActivity(), communalADActivityBean.getAndroidLink(), false);
            }
        });

        badge = getTopBadge(getActivity(), fra_home_message_total);
        std_home_art_type.setTextsize(AutoSizeUtils.mm2px(mAppContext,28) );
        std_home_art_type.setTabPadding(AutoSizeUtils.mm2px(mAppContext,40));
        std_home_art_type.setIndicatorHeight(AutoSizeUtils.mm2px(mAppContext,1));
        std_home_art_type.setIndicatorCornerRadius(AutoSizeUtils.mm2px(mAppContext,1));
        setStatusColor();
        rel_home_page.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rel_home_page.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) rel_home_page.getLayoutParams();
                int measuredHeight = tb_tool_home.getMeasuredHeight();
                layoutParams.setMargins(0,measuredHeight,0,0);
                rel_home_page.setLayoutParams(layoutParams);
            }
        });
    }

    private void setStatusColor() {
        SystemBarHelper.setPadding(getActivity(), tb_tool_home);
        SystemBarHelper.immersiveStatusBar(getActivity());
    }

    @Override
    protected void loadData() {
        //获取广告轮播图片
        getAdLoop();
        //获取文章分类列表
        getArticleTypeList();
        //获取热门活动列表
        getHotActivityList();
        //消息提醒
        getMessageWarm();
//        专区活动
        getRegionActivity();
//        浮窗广告
        getFloatAd();
//        跑马灯
        if (!isAutoClose) {
            getMarqueeData();
        }
    }

    private void getFloatAd() {
        if (NetWorkUtils.checkNet(getActivity())) {
            String url = Url.BASE_URL + Url.H_Q_FLOAT_AD;
            XUtil.Get(url, null, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    HomeQualityFloatAdEntity floatAdEntity = gson.fromJson(result, HomeQualityFloatAdEntity.class);
                    if (floatAdEntity != null) {
                        if (floatAdEntity.getCode().equals("01")) {
                            if (floatAdEntity.getCommunalADActivityBean() != null) {
                                iv_float_ad_icon.setVisibility(View.VISIBLE);
                                GlideImageLoaderUtil.loadFitCenter(getActivity(), iv_float_ad_icon,
                                        getStrings(floatAdEntity.getCommunalADActivityBean().getPicUrl()));
                                iv_float_ad_icon.setTag(R.id.iv_tag, floatAdEntity.getCommunalADActivityBean());
                            }
                        } else {
                            iv_float_ad_icon.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    iv_float_ad_icon.setVisibility(View.GONE);
                    super.onError(ex, isOnCallback);
                }
            });
        }
    }

    private void getMarqueeData() {
        if (NetWorkUtils.checkNet(getActivity())) {
            String url = Url.BASE_URL + Url.H_Q_MARQUEE_AD;
            XUtil.Get(url, null, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    MarqueeTextEntity marqueeTextEntity = MarqueeTextEntity.objectFromData(result);
                    if (marqueeTextEntity != null) {
                        if (marqueeTextEntity.getCode().equals("01")) {
                            if (marqueeTextEntity.getMarqueeTextList() != null && marqueeTextEntity.getMarqueeTextList().size() > 0) {
                                ll_home_marquee.setVisibility(View.VISIBLE);
                                tv_marquee_text.setText(getStrings(marqueeTextEntity.getMarqueeTextList().get(0).getContent()));
                                tv_marquee_text.setMarqueeRepeatLimit(marqueeTextEntity.getMarqueeTextList().get(0).getShow_count());
                            } else {
                                ll_home_marquee.setVisibility(View.GONE);
                            }
                        } else {
                            ll_home_marquee.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    ll_home_marquee.setVisibility(View.GONE);
                    super.onError(ex, isOnCallback);
                }
            });
        } else {
            ll_home_marquee.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IS_LOGIN_CODE) {
            //我的
            getMessageWarm();
        }
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (message.type.equals(REFRESH_MESSAGE_TOTAL)) {
            if (userId != 0) {
                getMessageWarm();
            }
        } else if (START_AUTO_PAGE_TURN.equals(message.type)) {
            if (adBeanList.size() > 0 && ad_communal_banner != null && !ad_communal_banner.isTurning()) {
                startTurningBanner();
            }
        } else if (STOP_AUTO_PAGE_TURN.equals(message.type)) {
            if (ad_communal_banner != null && ad_communal_banner.isTurning()) {
                stopTurningBanner();
            }
        }
    }

    private void stopTurningBanner() {
        ad_communal_banner.setCanScroll(false);
        ad_communal_banner.stopTurning();
        ad_communal_banner.setPointViewVisible(false);
    }

    private void startTurningBanner() {
        ad_communal_banner.setCanScroll(true);
        ad_communal_banner.startTurning(getShowNumber(adBeanList.get(0).getShowTime()) * 1000);
        ad_communal_banner.setPointViewVisible(true);
    }

    //
    private void getMessageWarm() {
        if (userId > 0) {
            String url = Url.BASE_URL + Url.H_MES_STATISTICS;
            Map<String, Object> params = new HashMap<>();
            params.put("uid", userId);
            NetLoadUtils.getQyInstance().loadNetDataPost(mAppContext,url
                    , params, new NetLoadUtils.NetLoadListener() {
                        @Override
                        public void onSuccess(String result) {
                            Gson gson = new Gson();
                            MessageTotalEntity messageTotalEntity = gson.fromJson(result, MessageTotalEntity.class);
                            if (messageTotalEntity != null) {
                                if (messageTotalEntity.getCode().equals("01")) {
                                    MessageTotalBean messageTotalBean = messageTotalEntity.getMessageTotalBean();
                                    int totalCount = messageTotalBean.getSmTotal() + messageTotalBean.getLikeTotal()
                                            + messageTotalBean.getCommentTotal() + messageTotalBean.getOrderTotal()
                                            + messageTotalBean.getCommOffifialTotal();
                                    if (!Build.MANUFACTURER.equalsIgnoreCase("Xiaomi")) {
                                        ShortcutBadger.applyCount(mAppContext, totalCount);
                                    }
                                }
                            }
                        }

                        @Override
                        public void netClose() {

                        }

                        @Override
                        public void onError(Throwable throwable) {
                            ShortcutBadger.removeCount(mAppContext);
                        }
                    });
        }else{
            ShortcutBadger.removeCount(mAppContext);
        }
    }

    private void getHotActivityList() {
        String url = BASE_URL + Url.H_HOT_ACTIVITY_LIST;
        XUtil.GetCache(url, 0, null, new MyCacheCallBack<String>() {
            private boolean hasError = false;
            private String result = null;

            @Override
            public boolean onCache(String result) { //得到缓存数据, 缓存过期后不会进入
                this.result = result;
//                getHotActivityListJson(result);
//                判断当前网络是否连接
                return !NetWorkUtils.isConnectedByState(getActivity()); //true: 信任缓存数据, 不再发起网络请求; false不信任缓存数据
            }

            @Override
            public void onSuccess(String result) {
                //如果服务返回304或onCache选择了信任缓存,这时result为null
                if (result != null) {
                    this.result = result;
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                hasError = true;
                home_title_collapsing_view.setEnabled(true);
//                showToast(x.app(), ex.getMessage());
                if (ex instanceof HttpException) { //网络错误
                    HttpException httpEx = (HttpException) ex;
                    int responseCode = httpEx.getCode();
                    String responseMsg = httpEx.getMessage();
                    String errorResult = httpEx.getResult();
                    //...
                } else { //其他错误
                    //...
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
                if (!hasError && result != null) {
                    //成功获取数据
//                    showToast(x.app(), result);
                    getHotActivityListJson(result);
                }
            }
        });
    }

    private void getHotActivityListJson(String result) {
        Gson gson = new Gson();
        hotActivityList.clear();
        CommunalADActivityEntity communalADActivityEntity = gson.fromJson(result, CommunalADActivityEntity.class);
        if (communalADActivityEntity != null) {
            if (communalADActivityEntity.getCode().equals("01")) {
                hotActivityList.addAll(communalADActivityEntity.getCommunalADActivityBeanList());
            } else if (!communalADActivityEntity.getCode().equals("02")) {
                showToast(getActivity(), communalADActivityEntity.getMsg());
            }
            hotActivityAdapter.notifyDataSetChanged();

            if (hotActivityList.size() > 0) {
                communal_recycler_wrap.setVisibility(View.VISIBLE);
            } else {
                communal_recycler_wrap.setVisibility(View.GONE);
            }
        }
    }

    private void getAdLoop() {
        String url = Url.BASE_URL + Url.H_AD_LIST;
        Map<String, String> params = new HashMap<>();
        params.put("vidoShow", "1");
        XUtil.GetCache(url, 0, params, new MyCacheCallBack<String>() {
            private boolean hasError = false;
            private String result = null;

            @Override
            public boolean onCache(String result) { //得到缓存数据, 缓存过期后不会进入
                this.result = result;
//                判断当前网络是否连接
                return !NetWorkUtils.isConnectedByState(getActivity()); //true: 信任缓存数据, 不再发起网络请求; false不信任缓存数据
            }

            @Override
            public void onSuccess(String result) {
                //如果服务返回304或onCache选择了信任缓存,这时result为null
                if (result != null) {
                    this.result = result;
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                hasError = true;
//                showToast(x.app(), ex.getMessage());
                if (ex instanceof HttpException) { //网络错误
                    HttpException httpEx = (HttpException) ex;
                    int responseCode = httpEx.getCode();
                    String responseMsg = httpEx.getMessage();
                    String errorResult = httpEx.getResult();
                    //...
                } else { //其他错误
                    //...
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
                if (!hasError && result != null) {
                    //成功获取数据
//                    showToast(x.app(), result);
                    if (NetWorkUtils.checkNet(getActivity())) {
                        getHomeAdNoCache();
                    } else {
                        getADJsonData(result);
                    }
                }
            }
        });
    }

    private void getHomeAdNoCache() {
        String url = Url.BASE_URL + Url.H_AD_LIST;
        Map<String, Object> params = new HashMap<>();
        params.put("vidoShow", "1");
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                getADJsonData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }
        });
    }

    private void getADJsonData(String result) {
        Gson gson = new Gson();
        adBeanList.clear();
        CommunalADActivityEntity adActivityEntity = gson.fromJson(result, CommunalADActivityEntity.class);
        if (adActivityEntity != null) {
            if (adActivityEntity.getCode().equals("01")) {
                adBeanList.addAll(adActivityEntity.getCommunalADActivityBeanList());
                rel_communal_banner.setVisibility(View.VISIBLE);
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
                ad_communal_banner.setPages(getActivity(),cbViewHolderCreator, adBeanList).setCanLoop(true).setPointViewVisible(true).setCanScroll(true)
                        .setPageIndicator(new int[]{R.drawable.unselected_radius, R.drawable.selected_radius})
                        .startTurning(getShowNumber(adBeanList.get(0).getShowTime()) * 1000);
            } else if (adActivityEntity.getCode().equals("02")) {
                showToast(getActivity(), adActivityEntity.getMsg());
                rel_communal_banner.setVisibility(View.GONE);
            }
        }
    }

    private void getRegionActivity() {
        String url = Url.BASE_URL + Url.H_REGION_ACTIVITY;
        XUtil.Get(url, null, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                activityList.clear();
                CommunalADActivityEntity activityEntity = gson.fromJson(result, CommunalADActivityEntity.class);
                if (activityEntity != null) {
                    if (activityEntity.getCode().equals("01")) {
                        for (int i = 0; i < activityEntity.getCommunalADActivityBeanList().size() / 2 * 2; i++) {
                            activityList.add(activityEntity.getCommunalADActivityBeanList().get(i));
                        }
                    }
                    if (activityList.size() > 0) {
                        rv_home_activity.setVisibility(View.VISIBLE);
                        homeImgActivityAdapter.setNewData(activityList);
                    } else {
                        rv_home_activity.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                rv_home_activity.setVisibility(View.GONE);
                super.onError(ex, isOnCallback);
            }
        });
    }

    private void getArticleTypeList() {
        String url = BASE_URL + Url.H_CATEGORY_LIST;
        XUtil.GetCache(url, 0, null, new MyCacheCallBack<String>() {
            private boolean hasError = false;
            private String result = null;

            @Override
            public boolean onCache(String result) { //得到缓存数据, 缓存过期后不会进入
                this.result = result;
//                getArticleTypeListJson(result);
//                判断当前网络是否连接
                return !NetWorkUtils.isConnectedByState(getActivity()); //true: 信任缓存数据, 不再发起网络请求; false不信任缓存数据
            }

            @Override
            public void onSuccess(String result) {
                //如果服务返回304或onCache选择了信任缓存,这时result为null
                if (result != null) {
                    this.result = result;
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                hasError = true;
//                showToast(x.app(), ex.getMessage());
                if (ex instanceof HttpException) { //网络错误
                    HttpException httpEx = (HttpException) ex;
                    int responseCode = httpEx.getCode();
                    String responseMsg = httpEx.getMessage();
                    String errorResult = httpEx.getResult();
                    //...
                } else { //其他错误
                    //...
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
                smart_refresh_home.finishRefresh();
                if (!hasError && result != null) {
                    //成功获取数据
//                    showToast(x.app(), result);
                    getArticleTypeListJson(result);
                }
            }
        });
    }

    private void getArticleTypeListJson(String result) {
        Gson gson = new Gson();
        categoryList.clear();
        CategoryTypeEntity categoryTypeEntity = gson.fromJson(result, CategoryTypeEntity.class);
        if (categoryTypeEntity != null) {
            if (categoryTypeEntity.getCode().equals("01")) {
                categoryList.addAll(categoryTypeEntity.getCategoryTypeList());
            } else if (!categoryTypeEntity.getCode().equals("02")) {
                showToast(getActivity(), categoryTypeEntity.getMsg());
            }
            if (categoryList.size() > 0) {
                HomeArticleTypeAdapter qualityPageAdapter = new HomeArticleTypeAdapter(getFragmentManager(), categoryList);
                viewpager_container.setAdapter(qualityPageAdapter);
                std_home_art_type.setViewPager(viewpager_container);
                std_home_art_type.setCurrentTab(0);
                viewpager_container.setCurrentItem(0);
            }
        }
    }

    @OnClick(R.id.tv_home_search)
    void skipSearch(View view) {
        Intent intent = new Intent(getActivity(), HomePageSearchActivity.class);
        intent.putExtra(SEARCH_TYPE, SEARCH_ALL);
        startActivity(intent);
    }

    //    跳转消息
    @OnClick(R.id.iv_home_message_total)
    void skipMessage(View view) {
//        Intent intent = new Intent(getActivity(), MessageActivity.class);
//        startActivity(intent);
        Snackbar.make(view,"snackbar 点击", BaseTransientBottomBar.LENGTH_LONG).show();
    }

    @OnClick(R.id.iv_float_ad_icon)
    void floatAdSkip(View view) {
        CommunalADActivityBean communalADActivityBean = (CommunalADActivityBean) view.getTag(R.id.iv_tag);
        if (communalADActivityBean != null) {
            adClickTotal(communalADActivityBean.getObjectId());
            setSkipPath(getActivity(), getStrings(communalADActivityBean.getAndroidLink()), false);
        }
    }

    /**
     * 关闭跑马灯
     *
     * @param view
     */
    @OnClick(R.id.iv_home_marquee_close)
    void closeMarquee(View view) {
        isAutoClose = true;
        ll_home_marquee.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        getMessageWarm();
        super.onResume();
    }
}
