package com.amkj.dmsh.find.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.appbar.AppBarLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.emoji.widget.EmojiTextView;
import androidx.viewpager.widget.ViewPager;
import androidx.cardview.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.find.activity.JoinTopicActivity;
import com.amkj.dmsh.find.adapter.PostPagerAdapter;
import com.amkj.dmsh.find.bean.EventMessageBean;
import com.amkj.dmsh.find.bean.TopicDetailEntity;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.views.JzVideo.JzVideoPlayerStatusDialog;
import com.amkj.dmsh.views.flycoTablayout.SlidingTabLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jzvd.Jzvd;

import static com.amkj.dmsh.constant.ConstantMethod.getIntegralFormat;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.DELETE_POST;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.UPDATE_POST_CONTENT;
import static com.amkj.dmsh.constant.Url.GET_TOPIC_DETAIL;
import static com.amkj.dmsh.dao.SoftApiDao.followTopic;

/**
 * Created by xiaoxin on 2019/7/16
 * Version:v4.1.0
 * ClassDescription :发现-话题详情
 */
public class TopicDetailFragment extends BaseFragment {
    @BindView(R.id.tv_topic_name)
    EmojiTextView mTvTopicName;
    @BindView(R.id.tv_topic_desc)
    EmojiTextView mTvTopicDesc;
    @BindView(R.id.jvp_find_video_play)
    JzVideoPlayerStatusDialog mJvpFindVideoPlay;
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
    private String[] titles = {"最新", "最热"};

    @Override
    protected int getContentView() {
        return R.layout.fragment_topic_detail;
    }

    @Override
    protected void initViews() {
        mJvpFindVideoPlay.thumbImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        mSmartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            loadData();
            updateCurrentPostFragment();
        });

        PostPagerAdapter adapter = new PostPagerAdapter(getChildFragmentManager(), titles, topicId);
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
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), GET_TOPIC_DETAIL, params, new NetLoadListenerHelper() {
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
            mJvpFindVideoPlay.setUp(getStrings(topicDetailEntity.getVideoUrl()), "", Jzvd.SCREEN_WINDOW_NORMAL);
            GlideImageLoaderUtil.loadCenterCrop(getActivity(), mJvpFindVideoPlay.thumbImageView
                    , getStrings(topicDetailEntity.getFirstImgUrl()));
        } else {
            mJvpFindVideoPlay.setVisibility(View.GONE);
        }
        mTvTopicName.setText(getStrings(topicDetailEntity.getTitle()));
        mTvScoreTips.setVisibility(topicDetailEntity.getScore() > 0 ? View.VISIBLE : View.GONE);
        mTvTopicDesc.setText(getStrings(topicDetailEntity.getContent()));
        mTvJoinNum.setText(getIntegralFormat(getActivity(), R.string.topic_join_num, topicDetailEntity.getParticipantNum()));
        mTvTopicNum.setText(topicDetailEntity.getNicePostNum() > 0 ? getIntegralFormat(getActivity(), R.string.topic_conten_num, topicDetailEntity.getNicePostNum()) : "");
        mTvFollow.setSelected(topicDetailEntity.getIsFocus());
        mRlFindReleaseTopic.setVisibility(topicDetailEntity.isProductTopic() ? View.GONE : View.VISIBLE);
    }

    @OnClick({R.id.tv_follow, R.id.tv_find_release_topic})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //关注话题
            case R.id.tv_follow:
                if (topicDetailEntity != null) {
                    followTopic((BaseActivity) getActivity(), topicDetailEntity.getId(), mTvFollow);
                }
                break;
            //参与话题
            case R.id.tv_find_release_topic:
                if (userId > 0) {
                    if (topicDetailEntity != null) {
                        Intent intent = new Intent(getActivity(), JoinTopicActivity.class);
                        intent.putExtra("topicId", String.valueOf(topicId));
                        intent.putExtra("topicTitle", topicDetailEntity.getTitle());
                        intent.putExtra("reminder", topicDetailEntity.getReminder());
                        intent.putExtra("score", topicDetailEntity.getScore());
                        intent.putExtra("rewardtip", topicDetailEntity.getRewardTip());
                        //记录当前选中帖子列表类型
                        intent.putExtra("type", titles[vp_post.getCurrentItem()]);
                        startActivity(intent);
                    }
                } else {
                    getLoginStatus(getActivity());
                }

                break;
        }
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (message.type.equals(DELETE_POST)) {
            updateCurrentPostFragment();
        }
    }

    //通知当前选中的帖子类型列表刷新
    private void updateCurrentPostFragment() {
        EventBus.getDefault().post(new EventMessage(UPDATE_POST_CONTENT, new EventMessageBean(getActivity().getClass().getSimpleName(), titles[vp_post.getCurrentItem()])));
    }


    @Override
    protected void getReqParams(Bundle bundle) {
        topicId = bundle.getString("topicId");
    }
}
