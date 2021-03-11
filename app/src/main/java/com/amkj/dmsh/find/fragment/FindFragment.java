package com.amkj.dmsh.find.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.MainActivity;
import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.constant.CommunalAdHolderView;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.find.activity.GoodsScoreListActivity;
import com.amkj.dmsh.find.activity.TopicCatergoryActivity;
import com.amkj.dmsh.find.adapter.HotTopicAdapter;
import com.amkj.dmsh.find.adapter.PostContentAdapter;
import com.amkj.dmsh.find.adapter.PostPagerAdapter;
import com.amkj.dmsh.find.bean.EventMessageBean;
import com.amkj.dmsh.find.bean.HotTopicEntity;
import com.amkj.dmsh.find.bean.HotTopicEntity.HotTopicBean;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity.CommunalADActivityBean;
import com.amkj.dmsh.message.activity.MessageActivity;
import com.amkj.dmsh.network.NetCacheLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.LifecycleHandler;
import com.amkj.dmsh.utils.SharedPreUtils;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.views.flycoTablayout.SlidingTabLayout;
import com.amkj.dmsh.views.guideview.Component;
import com.amkj.dmsh.views.guideview.FindComponent;
import com.amkj.dmsh.views.guideview.FindComponent2;
import com.amkj.dmsh.views.guideview.FindComponent3;
import com.amkj.dmsh.views.guideview.Guide;
import com.amkj.dmsh.views.guideview.GuideBuilder;
import com.amkj.dmsh.views.convenientbanner.ConvenientBanner;
import com.amkj.dmsh.views.convenientbanner.holder.CBViewHolderCreator;
import com.amkj.dmsh.views.convenientbanner.holder.Holder;
import com.gyf.barlibrary.ImmersionBar;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;
import q.rorbin.badgeview.Badge;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getMessageCount;
import static com.amkj.dmsh.constant.ConstantMethod.getShowNumber;
import static com.amkj.dmsh.constant.ConstantMethod.getTopBadge;
import static com.amkj.dmsh.constant.ConstantMethod.isContextExisted;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.DELETE_POST;
import static com.amkj.dmsh.constant.ConstantVariable.DEMO_LIFE_FILE;
import static com.amkj.dmsh.constant.ConstantVariable.REFRESH_MESSAGE_TOTAL;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.UPDATE_POST_CONTENT;

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
    @BindView(R.id.rv_topic)
    public RecyclerView communal_recycler_wrap;
    @BindView(R.id.find_container)
    public ViewPager vp_post;
    @BindView(R.id.tl_find_header)
    public Toolbar tl_find_header;
    @BindView(R.id.fra_find_message_total)
    public FrameLayout fra_find_message_total;
    @BindView(R.id.iv_find_release)
    ImageView mIvFindRelease;

    private List<CommunalADActivityBean> adBeanList = new ArrayList<>();
    //    热门主题
    private List<HotTopicBean> hotTopicList = new ArrayList<>();
    private Badge msgBadge;
    private boolean isFirst = true;
    private HotTopicAdapter findHotTopicAdapter;
    private CBViewHolderCreator cbViewHolderCreator;
    private boolean isUpdateCache;
    private String[] titles = {"精选", "最新", "一周最热", "关注"};
    private PostPagerAdapter postPagerAdapter;

    @Override
    protected int getContentView() {
        return R.layout.fragment_find;
    }

    @Override
    protected void initViews() {
        tl_find_header.setSelected(true);
        smart_refresh_find.setOnRefreshListener(refreshLayout -> {
            isUpdateCache = true;
            loadData();
            updateCurrentPostFragment();
        });
        msgBadge = getTopBadge(getActivity(), fra_find_message_total);
        initPostList();
        //初始化热门专题
        communal_recycler_wrap.setLayoutManager(new LinearLayoutManager(getActivity()));
        findHotTopicAdapter = new HotTopicAdapter(getActivity(), hotTopicList, true);
        communal_recycler_wrap.setAdapter(findHotTopicAdapter);
    }

    //初始化帖子列表
    private void initPostList() {
        postPagerAdapter = new PostPagerAdapter(getChildFragmentManager(), titles, "");
        vp_post.setAdapter(postPagerAdapter);
        vp_post.setOffscreenPageLimit(titles.length - 1);
        std_find_art_type.setTextsize(AutoSizeUtils.mm2px(mAppContext, 30));
        std_find_art_type.setTextUnselectColor(getResources().getColor(R.color.text_login_gray_s));
        std_find_art_type.setViewPager(vp_post);
    }

    @Override
    protected void loadData() {
        getFindAd();
        getHotTopic();
        getMessageCount(getActivity(), msgBadge);
    }

    //获广告位
    private void getFindAd() {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("vidoShow", "1");
        NetLoadUtils.getNetInstance().loadNetDataGetCache(getActivity(), Url.FIND_AD, params, isUpdateCache, new NetCacheLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {

                adBeanList.clear();
                CommunalADActivityEntity adActivityEntity = GsonUtils.fromJson(result, CommunalADActivityEntity.class);
                if (adActivityEntity != null) {
                    if (adActivityEntity.getCode().equals(SUCCESS_CODE)) {
                        adBeanList.addAll(adActivityEntity.getCommunalADActivityBeanList());
                        rel_find_ad.setVisibility(View.VISIBLE);
                        if (cbViewHolderCreator == null) {
                            cbViewHolderCreator = new CBViewHolderCreator() {
                                @Override
                                public Holder createHolder(View itemView) {
                                    return new CommunalAdHolderView(itemView, getActivity(), ad_communal_banner, true);
                                }

                                @Override
                                public int getLayoutId() {
                                    return R.layout.layout_ad_image_video;
                                }
                            };
                        }
                        ad_communal_banner.setPages(getActivity(), cbViewHolderCreator, adBeanList)
                                .startTurning(getShowNumber(adBeanList.get(0).getShowTime()) * 1000);
                    } else {
                        rel_find_ad.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                rel_find_ad.setVisibility(View.GONE);
            }
        });
    }


    //获取热门主题
    private void getHotTopic() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("isHot", "1");
        NetLoadUtils.getNetInstance().loadNetDataGetCache(getActivity(), Url.GET_HOT_TOPIC, map, isUpdateCache, new NetCacheLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                smart_refresh_find.finishRefresh();
                hotTopicList.clear();
                HotTopicEntity hotTopicEntity = GsonUtils.fromJson(result, HotTopicEntity.class);
                if (hotTopicEntity != null) {
                    List<HotTopicBean> topics = hotTopicEntity.getHotTopicList();
                    if (hotTopicEntity.getCode().equals(SUCCESS_CODE) && topics != null && topics.size() > 0) {
                        hotTopicList.addAll(topics.subList(0, topics.size() > 3 ? 3 : topics.size()));
                    }
                }

                findHotTopicAdapter.notifyDataSetChanged();
                ll_find_hot_topic.setVisibility(hotTopicList.size() == 0 ? View.GONE : View.VISIBLE);
                new LifecycleHandler(getActivity()).postDelayed(() -> {
                    if (!RefreshState.Refreshing.equals(smart_refresh_find.getState())) {
                        showGuideView1();
                    }
                }, 300);
            }

            @Override
            public void onNotNetOrException() {
                smart_refresh_find.finishRefresh();
                ll_find_hot_topic.setVisibility(hotTopicList.size() == 0 ? View.GONE : View.VISIBLE);
            }
        });
    }


    @OnClick({R.id.tv_topic_catergory, R.id.iv_find_message_total, R.id.iv_find_release})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            //消息
            case R.id.iv_find_message_total:
                intent = new Intent(getActivity(), MessageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            //已购买商品列表评分入口
            case R.id.iv_find_release:
                if (userId > 0) {
                    intent = new Intent(getActivity(), GoodsScoreListActivity.class);
                    startActivity(intent);
                } else {
                    getLoginStatus(this);
                }

                break;
            //话题分类
            case R.id.tv_topic_catergory:
                intent = new Intent(getActivity(), TopicCatergoryActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void initImmersionBar() {
        ImmersionBar.with(this).titleBar(tl_find_header)
                .statusBarDarkFont(true).keyboardEnable(true).navigationBarEnable(false).init();
    }

    @Override
    public boolean immersionBarEnabled() {
        return true;
    }

    @Override
    protected boolean isLazy() {
        return false;
    }

    @Override
    public void onVisible() {
        super.onVisible();
        if (!isFirst) {
            getMessageCount(getActivity(), msgBadge);
        }
        isFirst = false;
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (message.type.equals(REFRESH_MESSAGE_TOTAL)) {
            getMessageCount(getActivity(), msgBadge);
        } else if (message.type.equals(DELETE_POST)) {
            updateCurrentPostFragment();
        }
    }

    public void showGuideView1() {
        boolean showFindGuide1 = (boolean) SharedPreUtils.getParam(DEMO_LIFE_FILE, "showFindGuide1", false);
        if (!showFindGuide1 && isFindSelected()) {
            GuideBuilder builder = new GuideBuilder();
            builder.setTargetView(mIvFindRelease)
                    .setAlpha(125)
                    .setAutoDismiss(true)
                    .setHighTargetGraphStyle(Component.CIRCLE);
            builder.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
                @Override
                public void onShown() {
                    SharedPreUtils.setParam(DEMO_LIFE_FILE, "showFindGuide1", true);
                }

                @Override
                public void onDismiss() {
                    showGuideView2();
                }
            });

            builder.addComponent(new FindComponent());
            Guide guide = builder.createGuide();
            guide.show(getActivity());
        } else {
            showGuideView2();
        }
    }


    public void showGuideView2() {
        boolean showFindGuide2 = (boolean) SharedPreUtils.getParam(DEMO_LIFE_FILE, "showFindGuide2", false);
        if (!showFindGuide2 && isFindSelected()) {
            List<HotTopicBean> data = findHotTopicAdapter.getData();
            if (data.size() > 0) {
                TextView tvGetIntegral = (TextView) findHotTopicAdapter.getViewByPosition(communal_recycler_wrap, 0, R.id.tv_get_integral);
                if (tvGetIntegral != null && tvGetIntegral.getVisibility() == View.VISIBLE) {
                    GuideBuilder builder = new GuideBuilder();
                    builder.setTargetView(tvGetIntegral)
                            .setAlpha(125)
                            .setAutoDismiss(true)
                            .setHighTargetGraphStyle(Component.ROUNDRECT)
                            .setHighTargetCorner(AutoSizeUtils.mm2px(mAppContext, 15));
                    builder.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
                        @Override
                        public void onShown() {
                            SharedPreUtils.setParam(DEMO_LIFE_FILE, "showFindGuide2", true);
                        }

                        @Override
                        public void onDismiss() {
                            showGuideView3();
                        }
                    });

                    builder.addComponent(new FindComponent2());
                    Guide guide = builder.createGuide();
                    guide.show(getActivity());
                } else {
                    showGuideView3();
                }
            }
        } else {
            showGuideView3();
        }
    }


    public void showGuideView3() {
        boolean showFindGuide3 = (boolean) SharedPreUtils.getParam(DEMO_LIFE_FILE, "showFindGuide3", false);
        if (!showFindGuide3 && isFindSelected()) {
            View view = vp_post.getChildAt(std_find_art_type.getCurrentTab());
            if (view != null) {
                RecyclerView recyclerView = view.findViewById(R.id.rv_topic_content);
                PostContentAdapter postContentAdapter = (PostContentAdapter) recyclerView.getAdapter();
                if (postContentAdapter != null && postContentAdapter.getItemCount() > 1) {
                    View topicNameView = postContentAdapter.getViewByPosition(recyclerView, 1, R.id.tv_topic_name);
                    if (topicNameView != null && topicNameView.getVisibility() == View.VISIBLE) {
                        GuideBuilder builder = new GuideBuilder();
                        builder.setTargetView(topicNameView)
                                .setAlpha(125)
                                .setAutoDismiss(true)
                                .setHighTargetGraphStyle(Component.ROUNDRECT)
                                .setHighTargetCorner(AutoSizeUtils.mm2px(mAppContext, 12));
                        builder.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
                            @Override
                            public void onShown() {
                                SharedPreUtils.setParam(DEMO_LIFE_FILE, "showFindGuide3", true);
                            }

                            @Override
                            public void onDismiss() {
                            }
                        });

                        builder.addComponent(new FindComponent3());
                        Guide guide = builder.createGuide();
                        guide.show(getActivity());
                    }
                }
            }
        }
    }

    //判断MainActivity当前选中的是否是FindFragment
    private boolean isFindSelected() {
        if (isContextExisted(getActivity())) {
            Fragment fragment = ((MainActivity) getActivity()).getFragment();
            return fragment instanceof FindFragment;
        }
        return false;
    }


    private void updateCurrentPostFragment() {
        //通知当前选中的帖子类型列表刷新
        EventBus.getDefault().post(new EventMessage(UPDATE_POST_CONTENT, new EventMessageBean(getActivity().getClass().getSimpleName(), titles[vp_post.getCurrentItem()])));
    }
}
