package com.amkj.dmsh.find.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.find.adapter.PostPagerAdapter;
import com.amkj.dmsh.find.bean.EventMessageBean;
import com.amkj.dmsh.find.bean.TopicDetailEntity;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.views.JzVideo.JzVideoPlayerWifi;
import com.amkj.dmsh.views.flycoTablayout.SlidingTabLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.gyf.barlibrary.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.emoji.widget.EmojiTextView;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.OnClick;
import cn.jzvd.Jzvd;

import static com.amkj.dmsh.constant.ConstantMethod.getIntegralFormat;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.DELETE_POST;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.UPDATE_POST_CONTENT;
import static com.amkj.dmsh.constant.Url.GET_TOPIC_DETAIL;
import static com.amkj.dmsh.dao.SoftApiDao.followTopic;

/**
 * Created by xiaoxin on 2019/7/16
 * Version:v4.1.0
 * ClassDescription :??????-????????????
 */
public class TopicDetailActivity extends BaseActivity {
    @BindView(R.id.tv_life_back)
    TextView mTvLifeBack;
    @BindView(R.id.tv_header_shared)
    TextView mTvHeaderShared;
    @BindView(R.id.tv_topic_name)
    EmojiTextView mTvTopicName;
    @BindView(R.id.tv_topic_desc)
    EmojiTextView mTvTopicDesc;
    @BindView(R.id.jvp_find_video_play)
    JzVideoPlayerWifi mJvpFindVideoPlay;
    @BindView(R.id.tv_join_num)
    TextView mTvJoinNum;
    @BindView(R.id.tv_topic_num)
    TextView mTvTopicNum;
    @BindView(R.id.tv_follow)
    TextView mTvFollow;
    @BindView(R.id.sliding_tablayout)
    SlidingTabLayout mSlidingTabLayout;
    @BindView(R.id.vp_post)
    ViewPager vp_post;
    @BindView(R.id.tv_find_release_topic)
    TextView mTvFindReleaseTopic;
    @BindView(R.id.smart_refresh)
    SmartRefreshLayout mSmartRefreshLayout;
    @BindView(R.id.tv_score_tips)
    TextView mTvScoreTips;
    @BindView(R.id.ab_find_topic_layout)
    AppBarLayout mAbFindTopicLayout;
    @BindView(R.id.cardview)
    CardView mCardview;
    @BindView(R.id.coordinator)
    CoordinatorLayout mCoordinator;
    @BindView(R.id.rl_find_release_topic)
    RelativeLayout mRlFindReleaseTopic;
    private String topicId;
    private TopicDetailEntity topicDetailEntity;
    private String[] titles = {"??????", "??????"};

    @Override
    protected int getContentView() {
        return R.layout.activity_topic_detail;
    }

    @Override
    public void setStatusBar() {
        ImmersionBar.with(this).keyboardEnable(true).navigationBarEnable(false).statusBarDarkFont(true).fullScreen(true).init();
    }

    @Override
    protected void initViews() {
        Intent intent = getIntent();
        if (getIntent() == null || TextUtils.isEmpty(intent.getStringExtra("topicId")) || "0".equals(getIntent().getStringExtra("topicId"))) {
            showToast("????????????????????????");
            finish();
        }

        mJvpFindVideoPlay.posterImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        mSmartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            loadData();
            updateCurrentPostFragment();
        });

        //?????????????????????
        topicId = intent.getStringExtra("topicId");
        PostPagerAdapter adapter = new PostPagerAdapter(getSupportFragmentManager(), titles, topicId);
        vp_post.setOffscreenPageLimit(titles.length - 1);
        vp_post.setAdapter(adapter);
        mSlidingTabLayout.setViewPager(vp_post);
    }

    @Override
    protected void loadData() {
        getTopicDetail();
    }

    private void getTopicDetail() {
        Map<String, Object> params = new HashMap<>();
        params.put("id", topicId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, GET_TOPIC_DETAIL, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mSmartRefreshLayout.finishRefresh();
                topicDetailEntity = GsonUtils.fromJson(result, TopicDetailEntity.class);
                if (topicDetailEntity != null) {
                    if (topicDetailEntity.getCode().equals(SUCCESS_CODE)) {
                        setTopicData(topicDetailEntity);
                    }
                }
                NetLoadUtils.getNetInstance().showLoadSir(loadService, topicDetailEntity);
            }

            @Override
            public void onNotNetOrException() {
                mSmartRefreshLayout.finishRefresh();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, topicDetailEntity);
            }
        });
    }

    private void setTopicData(TopicDetailEntity topicDetailEntity) {
        if (!TextUtils.isEmpty(topicDetailEntity.getVideoUrl())) {
            mJvpFindVideoPlay.setVisibility(View.VISIBLE);
            mJvpFindVideoPlay.setUp(getStrings(topicDetailEntity.getVideoUrl()), "", Jzvd.SCREEN_NORMAL);
            GlideImageLoaderUtil.loadCenterCrop(this, mJvpFindVideoPlay.posterImageView
                    , getStrings(topicDetailEntity.getFirstImgUrl()));
        } else {
            mJvpFindVideoPlay.setVisibility(View.GONE);
        }
        mTvTopicName.setText(getStrings(topicDetailEntity.getTitle()));
        mTvScoreTips.setVisibility(topicDetailEntity.getScore() > 0 ? View.VISIBLE : View.GONE);
        mTvTopicDesc.setText(getStrings(topicDetailEntity.getContent()));
        mTvJoinNum.setText(getIntegralFormat(this, R.string.topic_join_num, topicDetailEntity.getParticipantNum()));
        mTvTopicNum.setText(topicDetailEntity.getNicePostNum() > 0 ? getIntegralFormat(this, R.string.topic_conten_num, topicDetailEntity.getNicePostNum()) : "");
        mTvFollow.setSelected(topicDetailEntity.getIsFocus());
        mRlFindReleaseTopic.setVisibility(topicDetailEntity.isProductTopic() ? View.GONE : View.VISIBLE);
    }

    @OnClick({R.id.tv_life_back, R.id.tv_header_shared, R.id.tv_follow, R.id.tv_find_release_topic})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_life_back:
                finish();
                break;
            //????????????
            case R.id.tv_header_shared:
                if (topicDetailEntity != null) {
                    UMShareAction umShareAction = new UMShareAction(getActivity()
                            , topicDetailEntity.getImgUrl()
                            , topicDetailEntity.getTitle()
                            , topicDetailEntity.getContent()
                            , Url.BASE_SHARE_PAGE_TWO + "find_template/themeDetails.html" + "?id=" + topicDetailEntity.getId()
                            , "pages/themeDetails/themeDetails?id=" + topicDetailEntity.getId(), topicDetailEntity.getId());
                }
                break;
            //????????????
            case R.id.tv_follow:
                if (topicDetailEntity != null) {
                    followTopic(this, topicDetailEntity.getId(), mTvFollow);
                }
                break;
            //????????????
            case R.id.tv_find_release_topic:
                if (userId > 0) {
                    if (topicDetailEntity != null) {
                        Intent intent = new Intent(this, JoinTopicActivity.class);
                        intent.putExtra("topicId", String.valueOf(topicId));
                        intent.putExtra("topicTitle", topicDetailEntity.getTitle());
                        intent.putExtra("reminder", topicDetailEntity.getReminder());
                        intent.putExtra("score", topicDetailEntity.getScore());
                        intent.putExtra("rewardtip", topicDetailEntity.getRewardTip());
                        //????????????????????????????????????
                        intent.putExtra("type", titles[vp_post.getCurrentItem()]);
                        startActivity(intent);
                    }
                } else {
                    getLoginStatus(this);
                }

                break;
        }
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    public View getLoadView() {
        return mSmartRefreshLayout;
    }

    @Override
    public View getTopView() {
        return mCardview;
    }


    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (message.type.equals(DELETE_POST)) {
            updateCurrentPostFragment();
        }
    }

    //?????????????????????????????????????????????
    private void updateCurrentPostFragment() {
        EventBus.getDefault().post(new EventMessage(UPDATE_POST_CONTENT, new EventMessageBean(getActivity().getClass().getSimpleName(), titles[vp_post.getCurrentItem()])));
    }
}
