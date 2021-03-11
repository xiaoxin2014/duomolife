package com.amkj.dmsh.find.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.CommunalAdHolderView;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.find.adapter.HotTopicAdapter;
import com.amkj.dmsh.find.adapter.ScoreGoodsAdapter;
import com.amkj.dmsh.find.bean.HotTopicEntity;
import com.amkj.dmsh.find.bean.HotTopicEntity.HotTopicBean;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity.CommunalADActivityBean;
import com.amkj.dmsh.homepage.bean.ScoreGoodsEntity;
import com.amkj.dmsh.homepage.bean.ScoreGoodsEntity.ScoreGoodsBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.LifecycleHandler;
import com.amkj.dmsh.utils.SharedPreUtils;
import com.amkj.dmsh.views.alertdialog.AlertDialogImage;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.views.guideview.Component;
import com.amkj.dmsh.views.guideview.FindComponent4;
import com.amkj.dmsh.views.guideview.Guide;
import com.amkj.dmsh.views.guideview.GuideBuilder;
import com.amkj.dmsh.views.convenientbanner.ConvenientBanner;
import com.amkj.dmsh.views.convenientbanner.holder.CBViewHolderCreator;
import com.amkj.dmsh.views.convenientbanner.holder.Holder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getShowNumber;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.skipJoinTopic;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.DEMO_LIFE_FILE;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.ERROR_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.UPDATE_SCORE_LIST;

/**
 * Created by xiaoxin on 2019/7/17
 * Version:v4.1.0
 * ClassDescription :已购买商品列表评分入口
 */
public class GoodsScoreListActivity extends BaseActivity {
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
    @BindView(R.id.rel_communal_banner)
    RelativeLayout mRelCommunalBanner;
    @BindView(R.id.rel_find_ad)
    RelativeLayout mRelFindAd;
    @BindView(R.id.tv_refresh)
    TextView mTvRefresh;
    @BindView(R.id.ll_goods_list)
    LinearLayout mLlGoodsList;
    @BindView(R.id.rv_goods)
    RecyclerView mRvGoods;
    @BindView(R.id.communal_recycler_wrap)
    RecyclerView mCommunalRecyclerWrap;
    @BindView(R.id.ll_find_hot_topic)
    LinearLayout mLlFindHotTopic;
    @BindView(R.id.smart_layout)
    SmartRefreshLayout mSmartLayout;
    @BindView(R.id.tv_hot_topic)
    TextView mTvHotTopic;
    @BindView(R.id.tv_topic_catergory)
    TextView mTvTopicCatergory;

    private List<CommunalADActivityBean> adBeanList = new ArrayList<>();
    private List<HotTopicBean> hotTopicList = new ArrayList<>();
    private List<ScoreGoodsBean> mGoodsList = new ArrayList<>();
    private List<ScoreGoodsBean> totalGoodsList = new ArrayList<>();
    private HotTopicAdapter findHotTopicAdapter;
    private CBViewHolderCreator cbViewHolderCreator;
    private ScoreGoodsAdapter mScoreGoodsAdapter;
    private ScoreGoodsEntity mScoreGoodsEntity;
    private String orderNo;
    private AlertDialogImage alertDialogScore;

    @Override
    protected int getContentView() {
        return R.layout.activity_goods_score_list;
    }

    @Override
    protected void initViews() {
        if (getIntent() != null) {
            orderNo = getIntent().getStringExtra("orderNo");
        }
        mTvHeaderShared.setText("奖励规则");
        mTvHotTopic.setText("大家都在讨论");
        mTvTopicCatergory.setText("更多");
        mTvHeaderShared.setCompoundDrawables(null, null, null, null);
        mSmartLayout.setOnRefreshListener(refreshLayout -> {
            loadData();
        });

        //初始化评分商品
        mRvGoods.setLayoutManager(new LinearLayoutManager(this));
        mScoreGoodsAdapter = new ScoreGoodsAdapter(this, mGoodsList);
        mRvGoods.setAdapter(mScoreGoodsAdapter);
        mScoreGoodsAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            ScoreGoodsBean scoreGoodsBean = (ScoreGoodsBean) view.getTag();
            getScorePop(scoreGoodsBean);
        });


        //初始化热门专题
        mCommunalRecyclerWrap.setLayoutManager(new LinearLayoutManager(this));
        findHotTopicAdapter = new HotTopicAdapter(this, hotTopicList, true);
        mCommunalRecyclerWrap.setAdapter(findHotTopicAdapter);
    }

    @Override
    protected void loadData() {
        getFindAd();
        getGoods();
    }

    //获取已购商品
    private void getGoods() {
        if (userId < 0) return;
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", 1);
        params.put("showCount", ConstantVariable.TOTAL_COUNT_FORTY);
        if (!TextUtils.isEmpty(orderNo)) {
            params.put("orderNo", orderNo);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.GET_SCORE_PRODUCT, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                loadHud.dismiss();
                mGoodsList.clear();
                totalGoodsList.clear();
                mScoreGoodsEntity = GsonUtils.fromJson(result, ScoreGoodsEntity.class);
                if (mScoreGoodsEntity != null) {
                    mScoreGoodsAdapter.setRewardReminder(mScoreGoodsEntity.getMaxRewardTip());
                    List<ScoreGoodsBean> goodsList = mScoreGoodsEntity.getGoodsList();
                    if (goodsList != null && goodsList.size() > 0) {
                        totalGoodsList.addAll(goodsList);
                        mGoodsList.addAll(totalGoodsList.subList(0, totalGoodsList.size() > 3 ? 3 : totalGoodsList.size()));
                    } else if (ERROR_CODE.equals(mScoreGoodsEntity.getCode())) {
                        showToast(mScoreGoodsEntity.getMsg());
                    }
                }
                mLlGoodsList.setVisibility(mGoodsList.size() > 0 ? View.VISIBLE : View.GONE);
                mTvRefresh.setVisibility(totalGoodsList.size() > 3 ? View.VISIBLE : View.GONE);
                totalGoodsList.removeAll(mGoodsList);
                mScoreGoodsAdapter.notifyDataSetChanged();
                getHotTopic();
            }

            @Override
            public void onNotNetOrException() {
                loadHud.dismiss();
                mLlGoodsList.setVisibility(mGoodsList.size() > 0 ? View.VISIBLE : View.GONE);
                mTvRefresh.setVisibility(totalGoodsList.size() > 3 ? View.VISIBLE : View.GONE);
                getHotTopic();
            }
        });
    }

    //获广告位
    private void getFindAd() {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("vidoShow", "1");
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Url.FIND_AD2, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {

                adBeanList.clear();
                CommunalADActivityEntity adActivityEntity = GsonUtils.fromJson(result, CommunalADActivityEntity.class);
                if (adActivityEntity != null) {
                    if (adActivityEntity.getCode().equals(SUCCESS_CODE)) {
                        adBeanList.addAll(adActivityEntity.getCommunalADActivityBeanList());
                        mRelFindAd.setVisibility(View.VISIBLE);
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
                        mAdCommunalBanner.setPages(getActivity(), cbViewHolderCreator, adBeanList)
                                .startTurning(getShowNumber(adBeanList.get(0).getShowTime()) * 1000);
                    } else {
                        mRelFindAd.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                mRelFindAd.setVisibility(View.GONE);
            }
        });
    }

    //获取热门主题
    private void getHotTopic() {
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Url.GET_HOT_TOPIC, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mSmartLayout.finishRefresh();
                hotTopicList.clear();
                HotTopicEntity hotTopicEntity = GsonUtils.fromJson(result, HotTopicEntity.class);
                if (hotTopicEntity != null) {
                    if (hotTopicEntity.getCode().equals(SUCCESS_CODE)) {
                        hotTopicList.addAll(hotTopicEntity.getHotTopicList());
                    } else if (!hotTopicEntity.getCode().equals(EMPTY_CODE)) {
                        showToast(hotTopicEntity.getMsg());
                    }
                }
                mLlFindHotTopic.setVisibility(hotTopicList.size() == 0 ? View.GONE : View.VISIBLE);
                findHotTopicAdapter.notifyDataSetChanged();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, hotTopicList.size() > 0 || mGoodsList.size() > 0, mScoreGoodsEntity);
                new LifecycleHandler(getActivity()).postDelayed(() -> showGuideView(), 500);
            }

            @Override
            public void onNotNetOrException() {
                mSmartLayout.finishRefresh();
                mLlFindHotTopic.setVisibility(hotTopicList.size() == 0 ? View.GONE : View.VISIBLE);
                NetLoadUtils.getNetInstance().showLoadSir(loadService, hotTopicList.size() > 0 || mGoodsList.size() > 0, mScoreGoodsEntity);

            }
        });
    }

    //获取点评弹窗
    private void getScorePop(ScoreGoodsBean scoreGoodsBean) {
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Url.Q_GET_TAKE_DELIVERY_POPUP, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                boolean isOpen = requestStatus.isOpen();
                String imgUrl = requestStatus.getImgUrl();
                if (isOpen && !TextUtils.isEmpty(imgUrl)) {
                    GlideImageLoaderUtil.setLoadImgFinishListener(getActivity(), imgUrl, new GlideImageLoaderUtil.ImageLoaderFinishListener() {
                        @Override
                        public void onSuccess(Bitmap bitmap) {
                            int joinCount = (int) SharedPreUtils.getParam(DEMO_LIFE_FILE, "IndentJoinCount", 0);
                            if (joinCount < 2) {
                                if (alertDialogScore == null) {
                                    alertDialogScore = new AlertDialogImage(getActivity());
                                }

                                alertDialogScore.setAlertClickListener(() -> {
                                    //写点评
                                    skipJoinTopic(getActivity(), scoreGoodsBean, null);
                                });
                                alertDialogScore.setImage(bitmap);
                                alertDialogScore.show();
                                SharedPreUtils.setParam(DEMO_LIFE_FILE, "IndentJoinCount", joinCount + 1);
                            } else {
                                //写点评
                                skipJoinTopic(getActivity(), scoreGoodsBean, null);
                            }
                        }

                        @Override
                        public void onError() {

                        }
                    });

                }
            }
        });
    }

    @OnClick({R.id.tv_life_back, R.id.tv_header_shared, R.id.tv_refresh, R.id.tv_topic_catergory})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_life_back:
                finish();
                break;
            //奖励规则
            case R.id.tv_header_shared:
                ConstantMethod.setSkipPath(this, Url.REWARD_RULE, false);
                break;
            //换一批已购商品
            case R.id.tv_refresh:
                mGoodsList.clear();
                mGoodsList.addAll(totalGoodsList.subList(0, totalGoodsList.size() > 3 ? 3 : totalGoodsList.size()));
                totalGoodsList.removeAll(mGoodsList);
                if (mGoodsList.size() > 0) {
                    mScoreGoodsAdapter.notifyDataSetChanged();
                } else {
                    getGoods();
                }
                break;
            //话题分类
            case R.id.tv_topic_catergory:
                Intent intent = new Intent(getActivity(), TopicCatergoryActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    public View getLoadView() {
        return mSmartLayout;
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (UPDATE_SCORE_LIST.equals(message.type)) {
            getGoods();
        }
    }

    public void showGuideView() {
        boolean showFindGuide = (boolean) SharedPreUtils.getParam(DEMO_LIFE_FILE, "showFindGuide4", false);
        if (!showFindGuide && !RefreshState.Refreshing.equals(mSmartLayout.getState())) {
            if (mGoodsList.size() > 0) {
                View tvWrite = mScoreGoodsAdapter.getViewByPosition(mRvGoods, 0, R.id.tv_write);
                if (tvWrite != null) {
                    GuideBuilder builder = new GuideBuilder();
                    builder.setTargetView(tvWrite)
                            .setAlpha(125)
                            .setAutoDismiss(true)
                            .setHighTargetGraphStyle(Component.ROUNDRECT)
                            .setHighTargetCorner(AutoSizeUtils.mm2px(mAppContext, 30));
                    builder.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
                        @Override
                        public void onShown() {
                            SharedPreUtils.setParam(DEMO_LIFE_FILE, "showFindGuide4", true);
                        }

                        @Override
                        public void onDismiss() {
                        }
                    });
                    builder.addComponent(new FindComponent4());
                    Guide guide = builder.createGuide();
                    guide.show(getActivity());
                }
            }
        }
    }
}
