package com.amkj.dmsh.views.JzVideo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.homepage.activity.JzVideoFullScreenActivity;

import androidx.annotation.NonNull;
import cn.jzvd.JZDataSource;
import cn.jzvd.JZMediaManager;
import cn.jzvd.JzvdMgr;

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
public class JzVideoPlayerStatus extends JzVideoPlayerStatusDialog {
    private VideoStatusListener videoStatusListener;
    private ImageView iv_video_product;
    private String skipUrl;
    private ImageView iv_video_volume;
    private boolean volumeOn = true;
    //    objects[] 1 视频标题 2 跳转地址 3 视频状态监听 4 是否已抢光

    public JzVideoPlayerStatus(Context context) {
        super(context);
    }

    public JzVideoPlayerStatus(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.jz_layout_standard_product;
    }

    @Override
    public void init(Context context) {
        super.init(context);
        iv_video_product = findViewById(R.id.iv_video_product);
        iv_video_volume = findViewById(R.id.iv_video_volume);
        iv_video_product.setOnClickListener(this);
        iv_video_volume.setOnClickListener(this);
    }

    /**
     * 跳转链接
     *
     * @param videoUrl 视频地址
     * @param skipUrl  跳转地址
     */
    public void setVideoSkipData(@NonNull String videoUrl, String skipUrl) {
        this.skipUrl = skipUrl;
        setUp(videoUrl, "", SCREEN_WINDOW_NORMAL);
    }

    @Override
    public void setUp(JZDataSource jzDataSource, int screen) {
        super.setUp(jzDataSource, screen);
        if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
            setADVideoView();
        }
        Log.d(TAG + "volume", "setUp: " + currentScreen + "控件状态->"
                + iv_video_volume.isSelected() + "\t" + "音量->" + volumeOn);
        syncVideoVolume();
    }

    /**
     * 进去全屏
     */
    @Override
    public void startWindowFullscreen() {
        super.startWindowFullscreen();
        ImageView imageView = JzvdMgr.getSecondFloor().findViewById(R.id.iv_video_volume);
        if (imageView.getVisibility() == VISIBLE) {
            imageView.setSelected(volumeOn);
        }
    }

    /**
     * 退出全屏
     */
    @Override
    public void playOnThisJzvd() {
        super.playOnThisJzvd();
        ImageView imageView = JzvdMgr.getFirstFloor().findViewById(R.id.iv_video_volume);
        if (imageView.getVisibility() == VISIBLE) {
            imageView.setSelected(volumeOn);
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

    @Override
    public void onStateNormal() {
        super.onStateNormal();
        getStopVideoStatusListener();
    }

    @Override
    public void showWifiDialog() {
        getStopVideoStatusListener();
        super.showWifiDialog();
        if (wifiAlertView != null) {
            wifiAlertView.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    getStartVideoStatusListener();
                }
            });
        }
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
    public void onError(int what, int extra) {
        super.onError(what, extra);
        getStartVideoStatusListener();
    }

    @Override
    public void onPrepared() {
        super.onPrepared();
        adjustVolume(volumeOn);
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

    public void setVideoStatusListener(VideoStatusListener videoStatusListener) {
        this.videoStatusListener = videoStatusListener;
    }

    @Override
    public void onClick(View v) {
        //拦截点击全屏事件
        if (v.getId() == R.id.fullscreen) {
            if (!TextUtils.isEmpty(getVideoUrl())) {
                Intent intent = new Intent(getContext(), JzVideoFullScreenActivity.class);
                intent.putExtra("url", getVideoUrl());
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
                    if (currentState == CURRENT_STATE_PLAYING) {
                        onStatePause();
                    }
                    setSkipPath(getContext(), skipUrl, false);
                }
                break;
            case R.id.iv_video_volume:
                v.setEnabled(false);
                volumeOn = !volumeOn;
                syncVideoVolume();
                adjustVolume(volumeOn);
                v.setEnabled(true);
                break;
            default:
                break;
        }
    }

    public boolean isVolumeOn() {
        return volumeOn;
    }

    /**
     * true 为打开 false静音
     *
     * @param volumeOn
     */
    public void setVolumeOn(boolean volumeOn) {
        // TODO: 2018/12/22 全屏非全屏音量键状态同步
        this.volumeOn = volumeOn;
        if (iv_video_volume.getVisibility() == GONE) {
            iv_video_volume.setVisibility(VISIBLE);
        }
        syncVideoVolume();
    }

    /**
     * 同步音量状态
     */
    private void syncVideoVolume() {
        if (iv_video_volume.getVisibility() == VISIBLE) {
            iv_video_volume.setSelected(volumeOn);
        }
    }

    /**
     * 设置视频音量
     *
     * @param volumeOn
     */
    private void adjustVolume(boolean volumeOn) {
        try {
            if (iv_video_volume.getVisibility() == VISIBLE) {
                float volumeValue = volumeOn ? 1f : 0f;
                JZMediaManager.instance().jzMediaInterface.setVolume(volumeValue, volumeValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface VideoStatusListener {
        void startTurning();

        void stopTurning();
    }

    public String getVideoUrl() {
        if (jzDataSource != null && jzDataSource.urlsMap != null && !TextUtils.isEmpty((String) jzDataSource.urlsMap.get(URL_KEY_DEFAULT))) {
            return (String) jzDataSource.urlsMap.get(URL_KEY_DEFAULT);
        }
        return "";
    }
}
