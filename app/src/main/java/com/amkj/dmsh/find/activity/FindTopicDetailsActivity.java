package com.amkj.dmsh.find.activity;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.find.adapter.FindPagerAdapter;
import com.amkj.dmsh.find.bean.FindHotTopicEntity.FindHotTopicBean;
import com.amkj.dmsh.find.bean.FindTopicDetailEntity;
import com.amkj.dmsh.release.activity.ReleaseImgArticleActivity;
import com.amkj.dmsh.utils.AppBarStateChangeListener;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.amkj.dmsh.views.JzVideo.JzVideoPlayerStatusDialog;
import com.flyco.tablayout.SlidingTabLayout;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.zhy.autolayout.utils.AutoUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jzvd.JZVideoPlayer;

import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.BASE_URL;
import static com.amkj.dmsh.utils.AppBarStateChangeListener.State.COLLAPSED;

;
;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/11/22
 * class description:话题详情
 */
public class FindTopicDetailsActivity extends BaseActivity {
    @BindView(R.id.smart_refresh_find_topic)
    public RefreshLayout smart_refresh_find;
    @BindView(R.id.std_find_topic_art_type)
    public SlidingTabLayout std_find_topic_art_type;
    @BindView(R.id.rel_find_topic_cover)
    public RelativeLayout rel_find_topic_cover;
    //    封面
    @BindView(R.id.iv_find_topic_details_cover)
    public ImageView iv_find_topic_details_cover;
    //    视频播放
    @BindView(R.id.jvp_find_video_play)
    public JzVideoPlayerStatusDialog jvp_find_video_play;
    //    标题
    @BindView(R.id.tv_find_topic_title)
    public TextView tv_find_topic_title;
    @BindView(R.id.tv_find_topic_collect)
    public TextView tv_find_topic_collect;
    //    话题描述
    @BindView(R.id.tv_find_topic_des)
    public TextView tv_find_topic_des;
    @BindView(R.id.find_topic_container)
    public ViewPager find_topic_container;
    @BindView(R.id.ab_find_topic_layout)
    public AppBarLayout ab_find_topic_layout;
    @BindView(R.id.tv_header_shared)
    public TextView tv_header_shared;
    @BindView(R.id.tv_find_release_topic)
    public TextView tv_find_release_topic;
    private FindPagerAdapter findPagerAdapter;
    public static final String TOPIC_TYPE = "topic";
    private String topicId;
    private FindHotTopicBean findHotTopicBean;

    @Override
    protected int getContentView() {
        return R.layout.activity_find_topic_details;
    }

    @Override
    protected void initViews() {
        tv_header_shared.setVisibility(View.GONE);
        Intent intent = getIntent();
        topicId = intent.getStringExtra("topicId");
        if (TextUtils.isEmpty(topicId)) {
            showToast(this, R.string.unConnectedNetwork);
            finish();
        }
        findPagerAdapter = new FindPagerAdapter(getSupportFragmentManager(), TOPIC_TYPE, topicId);
        find_topic_container.setAdapter(findPagerAdapter);
        std_find_topic_art_type.setTextsize(AutoUtils.getPercentWidthSize(32));
        std_find_topic_art_type.setViewPager(find_topic_container);
        smart_refresh_find.setOnRefreshListener((refreshLayout) -> {
            loadData();
            EventBus.getDefault().post(new EventMessage("refreshFindTopicData", 1));
        });

        tv_find_release_topic.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                tv_find_release_topic.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                float cornerRadii = tv_find_release_topic.getMeasuredHeight() / 2f;
                GradientDrawable drawable = new GradientDrawable();
                drawable.setCornerRadius(cornerRadii);
                drawable.setColor(0xffdaeeff);
                tv_find_release_topic.setBackground(drawable);
            }
        });

        ab_find_topic_layout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state, int scrollY) {
                if(state != COLLAPSED){
                    EventBus.getDefault().post(new EventMessage("topicStopScroll","topicStopScroll"));
                }
            }
        });
    }

    @Override
    protected void loadData() {
        if (NetWorkUtils.checkNet(FindTopicDetailsActivity.this)) {
            String url = BASE_URL + Url.F_TOPIC_DES;
            Map<String, Object> params = new HashMap<>();
            params.put("id", topicId);
            if (userId > 0) {
                params.put("uid", userId);
            }
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    dismissDialog();
                    Gson gson = new Gson();
                    FindTopicDetailEntity findHotTopicEntity = gson.fromJson(result, FindTopicDetailEntity.class);
                    if (findHotTopicEntity != null) {
                        if (findHotTopicEntity.getCode().equals(SUCCESS_CODE)) {
                            findHotTopicBean = findHotTopicEntity.getFindHotTopicBean();
                            if (findHotTopicBean != null) {
                                setTopicData(findHotTopicBean);
                            }
                        } else if (!findHotTopicEntity.getCode().equals(EMPTY_CODE)) {
                            showToast(FindTopicDetailsActivity.this, findHotTopicEntity.getMsg());
                        }
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    dismissDialog();
                    super.onError(ex, isOnCallback);
                }
            });
        } else {
            dismissDialog();
        }
    }

    private void getCollectTopic() {
        if (loadHud != null) {
            loadHud.show();
        }
        String url = Url.BASE_URL + Url.F_TOPIC_COLLECT;
        Map<String, Object> params = new HashMap<>();
        //用户id
        params.put("uid", userId);
        //文章id
        params.put("object_id", findHotTopicBean.getId());
        params.put("type", "findtopic");
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                loadHud.dismiss();
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals("01")) {
                        tv_find_topic_collect.setSelected(!tv_find_topic_collect.isSelected());
                        tv_find_topic_collect.setText(tv_find_topic_collect.isSelected() ? "已收藏" : "值得收藏");
                        if (tv_find_topic_collect.isSelected()) {
                            showToast(FindTopicDetailsActivity.this, String.format(getResources().getString(R.string.collect_success), "话题"));
                        }
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                loadHud.dismiss();
                showToast(FindTopicDetailsActivity.this, String.format(getResources().getString(R.string.collect_failed), "话题"));
                super.onError(ex, isOnCallback);
            }
        });
    }

    private void setTopicData(FindHotTopicBean findHotTopicBean) {
        if (TextUtils.isEmpty(findHotTopicBean.getImg_url())
                && TextUtils.isEmpty(findHotTopicBean.getVideo_url())) {
            rel_find_topic_cover.setVisibility(View.GONE);
        } else {
            rel_find_topic_cover.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(findHotTopicBean.getVideo_url())) {
                jvp_find_video_play.setVisibility(View.VISIBLE);
                jvp_find_video_play.setUp(getStrings(findHotTopicBean.getVideo_url()), JZVideoPlayer.SCREEN_WINDOW_NORMAL, "");
                GlideImageLoaderUtil.loadCenterCrop(FindTopicDetailsActivity.this, jvp_find_video_play.thumbImageView
                        , getStrings(findHotTopicBean.getFirst_img_url()));
                iv_find_topic_details_cover.setVisibility(View.GONE);
            } else {
                iv_find_topic_details_cover.setVisibility(View.VISIBLE);
                GlideImageLoaderUtil.loadCenterCrop(FindTopicDetailsActivity.this, iv_find_topic_details_cover, getStrings(findHotTopicBean.getImg_url()));
                jvp_find_video_play.setVisibility(View.GONE);
            }
        }
        tv_find_topic_title.setText(String.format(getResources().getString(R.string.topic_format)
                , getStrings(findHotTopicBean.getTitle())));
        tv_find_topic_collect.setSelected(findHotTopicBean.isCollect());
        tv_find_topic_collect.setText(findHotTopicBean.isCollect() ? "已收藏" : "值得收藏");
        tv_find_topic_des.setText(getStrings(findHotTopicBean.getContent()));
    }

    private void dismissDialog() {
        smart_refresh_find.finishRefresh();
    }

    @OnClick(R.id.tv_life_back)
    void goBack() {
        finish();
    }

    @OnClick(R.id.tv_find_release_topic)
    void releaseInvitation() {
        if (findHotTopicBean != null) {
            Intent intent = new Intent(FindTopicDetailsActivity.this, ReleaseImgArticleActivity.class);
            intent.putExtra("topicId", String.valueOf(topicId));
            intent.putExtra("topicTitle", getStrings(findHotTopicBean.getTitle()));
            intent.putExtra("topicHint", getStrings(findHotTopicBean.getReminder()));
            startActivity(intent);
        }
    }

    @OnClick(R.id.tv_find_topic_collect)
    void collectTopic() {
        if (findHotTopicBean != null) {
            if (userId > 0) {
                getCollectTopic();
            } else {
                getLoginStatus(FindTopicDetailsActivity.this);
            }
        }
    }
}
