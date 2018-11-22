package com.amkj.dmsh.find.fragment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.constant.CommunalAdHolderView;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.find.activity.FindHotTopicListActivity;
import com.amkj.dmsh.find.activity.FindTopicDetailsActivity;
import com.amkj.dmsh.find.adapter.FindHotTopicAdapter;
import com.amkj.dmsh.find.adapter.FindPagerAdapter;
import com.amkj.dmsh.find.adapter.RecyclerFindHotAdapter;
import com.amkj.dmsh.find.bean.FindHotTopicEntity;
import com.amkj.dmsh.find.bean.FindHotTopicEntity.FindHotTopicBean;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity.CommunalADActivityBean;
import com.amkj.dmsh.message.activity.MessageActivity;
import com.amkj.dmsh.message.bean.MessageTotalEntity;
import com.amkj.dmsh.release.activity.ReleaseImgArticleActivity;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.inteface.MyCacheCallBack;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.amkj.dmsh.utils.inteface.MyProgressCallBack;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.amkj.dmsh.views.SystemBarHelper;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.tablayout.SlidingTabLayout;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.tencent.bugly.beta.tinker.TinkerManager;

import org.greenrobot.eventbus.EventBus;
import org.xutils.ex.HttpException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;
import q.rorbin.badgeview.Badge;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getShowNumber;
import static com.amkj.dmsh.constant.ConstantMethod.getTopBadge;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.REFRESH_MESSAGE_TOTAL;
import static com.amkj.dmsh.constant.ConstantVariable.START_AUTO_PAGE_TURN;
import static com.amkj.dmsh.constant.ConstantVariable.STOP_AUTO_PAGE_TURN;

;

/**
 * Created by atd48 on 2016/6/21.
 */
public class FindFragment extends BaseFragment {
    @BindView(R.id.smart_refresh_find)
    public RefreshLayout smart_refresh_find;
    @BindView(R.id.std_find_art_type)
    public SlidingTabLayout std_find_art_type;
    //    发现头部
    @BindView(R.id.ll_find_header)
    public LinearLayout ll_find_header;
    //    发现广告位
    @BindView(R.id.rel_find_ad)
    public RelativeLayout rel_find_ad;
    @BindView(R.id.ad_communal_banner)
    public ConvenientBanner ad_communal_banner;
    //    热门话题
    @BindView(R.id.ll_find_hot_topic)
    public LinearLayout ll_find_hot_topic;
    @BindView(R.id.communal_recycler_wrap)
    public RecyclerView communal_recycler_wrap;
    //    首页活动图
    @BindView(R.id.rv_find_act)
    public RecyclerView rv_find_act;
    @BindView(R.id.find_container)
    public ViewPager viewPager;
    @BindView(R.id.tl_find_header)
    public Toolbar tl_find_header;
    @BindView(R.id.fra_find_message_total)
    public FrameLayout fra_find_message_total;
    @BindView(R.id.ab_find_layout)
    public AppBarLayout ab_find_layout;

    private FindPagerAdapter findPagerAdapter;
    private List<CommunalADActivityBean> adBeanList = new ArrayList<>();
    //    热门主题
    private List<FindHotTopicBean> hotTopicList = new ArrayList<>();
    private Badge badge;
    private boolean isOnPause;
    private boolean isCache;
    private FindHotTopicAdapter findHotTopicAdapter;
    private int topicPage = 1;
    public static final String FIND_TYPE = "find";
    private RecyclerFindHotAdapter recyclerFindHotAdapter;
    //    发现活动图
    private List<CommunalADActivityBean> findActivityList = new ArrayList<>();
    private CBViewHolderCreator cbViewHolderCreator;

    @Override
    protected int getContentView() {
        return R.layout.fragment_find;
    }

    @Override
    protected void initViews() {
        tl_find_header.setSelected(true);
        findPagerAdapter = new FindPagerAdapter(getChildFragmentManager(), FIND_TYPE, null);
        viewPager.setAdapter(findPagerAdapter);
        std_find_art_type.setTextsize(AutoSizeUtils.mm2px(mAppContext, 28));
        std_find_art_type.setTextUnselectColor(getResources().getColor(R.color.text_login_gray_s));
        std_find_art_type.setViewPager(viewPager);
        smart_refresh_find.setOnRefreshListener((refreshLayout) -> {
            loadData();
            EventBus.getDefault().post(new EventMessage("refreshFindData", 1));
        });
        badge = getTopBadge(getActivity(), fra_find_message_total);
        setStatusColor();
        TinkerBaseApplicationLike app = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
        ll_find_header.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ll_find_header.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) ll_find_header.getLayoutParams();
                layoutParams.setMargins(0, tl_find_header.getMeasuredHeight(), 0, 0);
                ll_find_header.setLayoutParams(layoutParams);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        communal_recycler_wrap.setLayoutManager(linearLayoutManager);
        communal_recycler_wrap.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_nine_dp_white)






                .create());
        findHotTopicAdapter = new FindHotTopicAdapter(getActivity(), hotTopicList);
        communal_recycler_wrap.setAdapter(findHotTopicAdapter);
        findHotTopicAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                FindHotTopicBean hotTopicBean = (FindHotTopicBean) view.getTag();
                if (hotTopicBean != null) {
                    Intent intent = new Intent(getActivity(), FindTopicDetailsActivity.class);
                    intent.putExtra("topicId", String.valueOf(hotTopicBean.getId()));
                    startActivity(intent);
                }
            }
        });
        rv_find_act.setLayoutManager(new LinearLayoutManager(getActivity()
                , LinearLayoutManager.HORIZONTAL, false));
        recyclerFindHotAdapter = new RecyclerFindHotAdapter(getActivity(), findActivityList);
        rv_find_act.setAdapter(recyclerFindHotAdapter);
        recyclerFindHotAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CommunalADActivityBean communalADActivityBean = (CommunalADActivityBean) view.getTag();
                if (communalADActivityBean != null) {
                    ConstantMethod.setSkipPath(getActivity(), communalADActivityBean.getAndroidLink(), false);
                }
            }
        });
        findHotTopicAdapter.loadMoreEnd(true);
    }

    private void setStatusColor() {
        SystemBarHelper.immersiveStatusBar(getActivity());
        SystemBarHelper.setPadding(getActivity(), tl_find_header);
    }

    @OnClick(R.id.iv_find_message_total)
    void skipMessage(View view) {
        Intent intent = new Intent(getActivity(), MessageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @OnClick(R.id.iv_find_release)
    void releaseInvitation(View view) {
        Intent intent = new Intent(getActivity(), ReleaseImgArticleActivity.class);
        startActivity(intent);
    }

    @Override
    public void onPause() {
        isOnPause = true;
        super.onPause();
    }

    @Override
    public void onResume() {
        if (isOnPause) {
            isOnPause = false;
            getMessageWarm();
        }
        super.onResume();
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (message.type.equals(REFRESH_MESSAGE_TOTAL)) {
            getMessageWarm();
        } else if (START_AUTO_PAGE_TURN.equals(message.type)) {
            if (adBeanList.size() > 0 && ad_communal_banner != null && !ad_communal_banner.isTurning()) {
                ad_communal_banner.setCanScroll(true);
                ad_communal_banner.startTurning(getShowNumber(adBeanList.get(0).getShowTime()) * 1000);
                ad_communal_banner.setPointViewVisible(true);
            }
        } else if (STOP_AUTO_PAGE_TURN.equals(message.type)) {
            if (ad_communal_banner != null && ad_communal_banner.isTurning()) {
                ad_communal_banner.setCanScroll(false);
                ad_communal_banner.stopTurning();
                ad_communal_banner.setPointViewVisible(false);
            }
        }
    }

    private void getMessageWarm() {
        if (userId < 1) {
            badge.setBadgeNumber(0);
            return;
        }
        String url = Url.BASE_URL + Url.H_MESSAGE_WARM + userId;
        XUtil.Get(url, null, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                MessageTotalEntity messageTotalEntity = gson.fromJson(result, MessageTotalEntity.class);
                if (messageTotalEntity != null) {
                    MessageTotalEntity.MessageTotalBean messageTotalBean = messageTotalEntity.getMessageTotalBean();
                    if (badge != null) {
                        int total = messageTotalBean.getSmTotal() + messageTotalBean.getLikeTotal()
                                + messageTotalBean.getOrderTotal() + messageTotalBean.getCommentTotal()
                                + messageTotalBean.getCommOffifialTotal();
                        badge.setBadgeNumber(total);
                    }
                }
            }
        });
    }

    @Override
    protected void loadData() {
        getFindAd();
        getFindActivity();
        getMessageWarm();
        getHotTopic();
    }

    private void getFindAd() {
        String url = Url.BASE_URL + Url.FIND_AD;
        Map<String, String> params = new HashMap<>();
        params.put("vidoShow", "1");
        XUtil.GetCache(url, 0, params, new MyCacheCallBack<String>() {
            private boolean hasError = false;
            private String result = null;

            @Override
            public boolean onCache(String result) { //得到缓存数据, 缓存过期后不会进入
                this.result = result;
                isCache = true;
//                getADJsonData(result);
//                判断当前网络是否连接
                return true; //true: 信任缓存数据, 不再发起网络请求; false不信任缓存数据
            }

            @Override
            public void onSuccess(String result) {
                //如果服务返回304或onCache选择了信任缓存,这时result为null
                if (result != null) {
                    this.result = result;
                    isCache = false;
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
                smart_refresh_find.finishRefresh();
                if (!hasError && result != null) {
                    //成功获取数据
//                    showToast(x.app(), result);

                    if (isCache && NetWorkUtils.checkNet(getActivity())) {
                        getFindAdNoCache();
                    } else {
                        getADJsonData(result);
                    }
                }
            }
        });
    }

    /**
     * 发现-活动图
     */
    private void getFindActivity() {
        String url = Url.BASE_URL + Url.F_ACTIVITY_AD;
        if (NetWorkUtils.isConnectedByState(getActivity())) {
            XUtil.Post(url, null, new MyProgressCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    findActivityList.clear();
                    Gson gson = new Gson();
                    CommunalADActivityEntity communalADActivityEntity = gson.fromJson(result, CommunalADActivityEntity.class);
                    if (communalADActivityEntity != null) {
                        if (communalADActivityEntity.getCode().equals("01")) {
                            findActivityList.addAll(communalADActivityEntity.getCommunalADActivityBeanList());
                        }
                        recyclerFindHotAdapter.setNewData(findActivityList);
                        findActivityEmptyError();
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    findActivityEmptyError();
                }
            });
        } else {
            findActivityEmptyError();
        }
    }

    private void getFindAdNoCache() {
        String url = Url.BASE_URL + Url.FIND_AD;
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
                rel_find_ad.setVisibility(View.VISIBLE);
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
                ad_communal_banner.setPages(getActivity(), cbViewHolderCreator, adBeanList).setCanLoop(true).setPointViewVisible(true).setCanScroll(true)
                        .setPageIndicator(new int[]{R.drawable.unselected_radius, R.drawable.selected_radius})
                        .startTurning(getShowNumber(adBeanList.get(0).getShowTime()) * 1000);

            } else if (!adActivityEntity.getCode().equals("02")) {
                showToast(getActivity(), adActivityEntity.getMsg());
                rel_find_ad.setVisibility(View.GONE);
            } else {
                rel_find_ad.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 获取热门主题
     */
    private void getHotTopic() {
        String url = Url.BASE_URL + Url.F_HOT_TOPIC_LIST;
        if (NetWorkUtils.isConnectedByState(getActivity())) {
            XUtil.Post(url, null, new MyProgressCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    if (topicPage == 1) {
                        hotTopicList.clear();
                    }
                    Gson gson = new Gson();
                    FindHotTopicEntity findHotTopicEntity = gson.fromJson(result, FindHotTopicEntity.class);
                    if (findHotTopicEntity != null) {
                        if (findHotTopicEntity.getCode().equals("01")) {
                            hotTopicList.addAll(findHotTopicEntity.getHotTopicList());
                        } else if (!findHotTopicEntity.getCode().equals("02")) {
                            showToast(getActivity(), findHotTopicEntity.getMsg());
                        }
                        if (topicPage == 1) {
                            findHotTopicAdapter.setNewData(hotTopicList);
                        } else {
                            findHotTopicAdapter.notifyDataSetChanged();
                        }
                        topicEmptyError();
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    topicEmptyError();
                }
            });
        } else {
            topicEmptyError();
        }
    }

    private void topicEmptyError() {
        if (hotTopicList.size() == 0) {
            ll_find_hot_topic.setVisibility(View.GONE);
        } else {
            ll_find_hot_topic.setVisibility(View.VISIBLE);
        }
    }

    private void findActivityEmptyError() {
        if (findActivityList.size() == 0) {
            rv_find_act.setVisibility(View.GONE);
        } else {
            rv_find_act.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.tv_find_hot_topic)
    void skipHotTopic(View view) {
        Intent intent = new Intent(getActivity(), FindHotTopicListActivity.class);
        startActivity(intent);
    }
}
