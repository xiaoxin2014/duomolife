package com.amkj.dmsh.views.JzVideo;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.amkj.dmsh.R;
import com.amkj.dmsh.homepage.activity.JzVideoFullScreenActivity;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;

import androidx.annotation.NonNull;
import cn.jzvd.JzvdStd;

import static cn.jzvd.JZDataSource.URL_KEY_DEFAULT;
import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.setSkipPath;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/5/14
 * version 3.1.3
 * class description:实现播放监听
 */
public class JzVideoPlayerProduct extends JzvdStd {
    private VideoStatusListener videoStatusListener;
    private View iv_video_product;
    private String skipUrl;
    private String cover;

    public JzVideoPlayerProduct(Context context) {
        super(context);
    }

    public JzVideoPlayerProduct(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public int getLayoutId() {
        return R.layout.jz_layout_std_product;
    }

    @Override
    public void init(Context context) {
        super.init(context);
        iv_video_product = findViewById(R.id.iv_video_product);
        iv_video_product.setOnClickListener(this);
    }

    /**
     * 跳转链接
     *
     * @param videoUrl 视频地址
     * @param skipUrl  跳转地址
     */
    public void setVideoSkipData(@NonNull String videoUrl, String skipUrl, String cover) {
        this.skipUrl = skipUrl;
        this.cover = cover;
        setUp(videoUrl, "", SCREEN_NORMAL);
        GlideImageLoaderUtil.loadCenterCrop(getContext(), posterImageView, cover);
    }

    @Override
    public void onStatePlaying() {
        super.onStatePlaying();
        getStopVideoStatusListener();
    }

    @Override
    public void onStatePause() {
        super.onStatePause();
        getStartVideoStatusListener();
    }

    @Override
    public void onStatePreparing() {
        super.onStatePreparing();
        getStopVideoStatusListener();
    }

    @Override
    public void onStateAutoComplete() {
        super.onStateAutoComplete();
        getStartVideoStatusListener();
    }

    @Override
    public void onStateError() {
        super.onStateError();
        getStartVideoStatusListener();
    }

    @Override
    public void onStateNormal() {
        super.onStateNormal();

    }

    @Override
    public void onError(int what, int extra) {
        super.onError(what, extra);
        getStartVideoStatusListener();
    }

    private void getStartVideoStatusListener() {
        iv_video_product.setVisibility(GONE);
        if (videoStatusListener != null) {
            videoStatusListener.startTurning();
        }
    }

    private void getStopVideoStatusListener() {
        setADVideoView();
        if (videoStatusListener != null) {
            videoStatusListener.stopTurning();
        }
    }

    /**
     * 设置广告栏 跳转详情按钮
     */
    private void setADVideoView() {
        if (!TextUtils.isEmpty(skipUrl)) {
            iv_video_product.setVisibility(VISIBLE);
        } else {
            iv_video_product.setVisibility(GONE);
        }
    }

    public void setVideoStatusListener(VideoStatusListener videoStatusListener) {
        this.videoStatusListener = videoStatusListener;
    }

    public interface VideoStatusListener {
        void startTurning();

        void stopTurning();
    }


    @Override
    public void onClick(View v) {
        //拦截点击全屏事件
        if (v.getId() == R.id.fullscreen) {
            if (!TextUtils.isEmpty(getVideoUrl())) {
                Intent intent = new Intent(getContext(), JzVideoFullScreenActivity.class);
                intent.putExtra("url", getVideoUrl());
                intent.putExtra("cover", cover);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mAppContext.getApplicationContext().startActivity(intent);
            }
            return;
        }
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_video_product:
                if (!TextUtils.isEmpty(skipUrl)) {
                    /**
                     * 当前状态在播放，先暂停再跳转
                     */
                    if (screen == STATE_PLAYING) {
                        onStatePause();
                    }
                    setSkipPath(getContext(), skipUrl, false);
                }
                break;
            default:
                break;
        }
    }

    public String getVideoUrl() {
        if (jzDataSource != null && jzDataSource.urlsMap != null && !TextUtils.isEmpty((String) jzDataSource.urlsMap.get(URL_KEY_DEFAULT))) {
            return (String) jzDataSource.urlsMap.get(URL_KEY_DEFAULT);
        }

        return "";
    }
}
