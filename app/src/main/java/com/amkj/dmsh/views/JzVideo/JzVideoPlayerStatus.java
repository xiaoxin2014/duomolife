package com.amkj.dmsh.views.JzVideo;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.amkj.dmsh.R;

import java.util.LinkedHashMap;

import cn.jzvd.JZDataSource;
import cn.jzvd.JZMediaManager;

import static cn.jzvd.JZDataSource.URL_KEY_DEFAULT;
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
    private final String urlType = "skipUrl";
    private final String listenerType = "videoListener";
    private LinkedHashMap linkedHashMap = new LinkedHashMap();
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
        if (!TextUtils.isEmpty(skipUrl)) {
            iv_video_product.setTag(skipUrl);
        }
        linkedHashMap.put(URL_KEY_DEFAULT, videoUrl);
        if (!TextUtils.isEmpty(skipUrl)) {
            linkedHashMap.put(urlType, skipUrl);
        }
//        第一个可变参数 为标题 第二个为跳转地址
        JZDataSource jzDataSource = new JZDataSource(linkedHashMap, "");
        setUp(jzDataSource, SCREEN_WINDOW_NORMAL);
    }

    @Override
    public void setUp(JZDataSource jzDataSource, int screen) {
        super.setUp(jzDataSource, screen);
        if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
            setADVideoView();
        }
        Log.d(TAG + "volume", "setUp: " + currentScreen + "控件状态->"
                + iv_video_volume.isSelected() + "\t"+"音量->"+volumeOn);
        syncVideoVolume();
    }

    /**
     *
     */
    @Override
    public void startWindowFullscreen() {
        super.startWindowFullscreen();
        Log.d(TAG + "volume", "startWindowFullscreen: " + currentScreen + "控件状态->"
                + iv_video_volume.isSelected() + "\t"+"音量->"+volumeOn);
    }

    @Override
    public void playOnThisJzvd() {
        super.playOnThisJzvd();
        Log.d(TAG + "volume", "playOnThisJzvd: " + currentScreen + "控件状态->"
                + iv_video_volume.isSelected() + "\t"+"音量->"+volumeOn);
    }

    /**
     * 设置广告栏 跳转详情按钮
     */
    private void setADVideoView() {
        if (linkedHashMap != null && linkedHashMap.get(urlType) != null &&
                !TextUtils.isEmpty(linkedHashMap.get(urlType).toString())) {
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
        } else if (linkedHashMap != null && linkedHashMap.get(listenerType) != null) {
            videoStatusListener = (VideoStatusListener) linkedHashMap.get(listenerType);
            videoStatusListener.startTurning();
        }
    }

    private void getStopVideoStatusListener() {
        setADVideoView();
        if (videoStatusListener != null) {
            videoStatusListener.stopTurning();
        } else if (linkedHashMap != null && linkedHashMap.get(listenerType) != null) {
            videoStatusListener = (VideoStatusListener) linkedHashMap.get(listenerType);
            videoStatusListener.stopTurning();
        }
    }

    public void setVideoStatusListener(VideoStatusListener videoStatusListener) {
        this.videoStatusListener = videoStatusListener;
        if (linkedHashMap != null && linkedHashMap.containsKey(urlType)) {
            linkedHashMap.put(listenerType, videoStatusListener);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_video_product:
                if (linkedHashMap != null && linkedHashMap.containsKey(urlType)
                        && !TextUtils.isEmpty(linkedHashMap.get(urlType).toString())) {
                    /**
                     * 当前状态在播放，先暂停再跳转
                     */
                    if (currentState == CURRENT_STATE_PLAYING) {
                        onStatePause();
                    }
                    setSkipPath(getContext(), linkedHashMap.get(urlType).toString(), true, false);
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
        if(iv_video_volume.getVisibility() == VISIBLE){
            iv_video_volume.setSelected(volumeOn);
        }
    }

    /**
     * 设置视频音量
     *
     * @param volumeOn
     */
    private void adjustVolume(boolean volumeOn) {
        if (iv_video_volume.getVisibility() == VISIBLE) {
            float volumeValue = volumeOn ? 1f : 0f;
            JZMediaManager.instance().jzMediaInterface.setVolume(volumeValue, volumeValue);
        }
    }

    public interface VideoStatusListener {
        void startTurning();

        void stopTurning();
    }
}
