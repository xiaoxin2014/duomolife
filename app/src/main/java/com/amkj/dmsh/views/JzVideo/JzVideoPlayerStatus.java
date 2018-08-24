package com.amkj.dmsh.views.JzVideo;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.amkj.dmsh.R;

import java.util.LinkedHashMap;

import cn.jzvd.JZVideoPlayer;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
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
        iv_video_product.setOnClickListener(this);
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
        LinkedHashMap map = new LinkedHashMap();
        map.put(URL_KEY_DEFAULT, videoUrl);
        Object[] dataSourceObjects = new Object[1];
        dataSourceObjects[0] = map;
//        第一个可变参数 为标题 第二个为跳转地址
        setUp(dataSourceObjects, 0, JZVideoPlayer.SCREEN_WINDOW_NORMAL, "",getStrings(skipUrl),videoStatusListener);
    }

    @Override
    public void setUp(Object[] dataSourceObjects, int defaultUrlMapIndex, int screen, Object... objects) {
        super.setUp(dataSourceObjects, defaultUrlMapIndex, screen, objects);
        if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
            if(objects.length>1&&!TextUtils.isEmpty(objects[1].toString())){
                iv_video_product.setVisibility(VISIBLE);
            }else{
                iv_video_product.setVisibility(GONE);
            }
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
        if(wifiAlertView!=null){
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

    private void getStartVideoStatusListener() {
        iv_video_product.setVisibility(GONE);
        if (videoStatusListener != null) {
            videoStatusListener.startTurning();
        }else if(objects!=null&&objects.length>2&&objects[2]!=null){
            videoStatusListener = (VideoStatusListener) objects[2];
            videoStatusListener.startTurning();
        }
    }

    private void getStopVideoStatusListener() {
        if(objects!=null&&objects.length>1&&!TextUtils.isEmpty(objects[1].toString())){
            iv_video_product.setVisibility(VISIBLE);
        }else{
            iv_video_product.setVisibility(GONE);
        }
        if (videoStatusListener != null) {
            videoStatusListener.stopTurning();
        }else if(objects!=null&&objects.length>2&&objects[2]!=null){
            videoStatusListener = (VideoStatusListener) objects[2];
            videoStatusListener.stopTurning();
        }
    }

    public void setVideoStatusListener(VideoStatusListener videoStatusListener) {
        this.videoStatusListener = videoStatusListener;
        if (objects!=null&&objects.length>2){
            objects[2] = videoStatusListener;
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.iv_video_product) {
            if (objects!=null&&objects.length>1&&!TextUtils.isEmpty(objects[1].toString())) {
                /**
                 * 当前状态在播放，先暂停再跳转
                 */
                if (currentState == CURRENT_STATE_PLAYING) {
                    onStatePause();
                }
                setSkipPath(getContext(), objects[1].toString(),true, false);
            }
        }
    }

    public interface VideoStatusListener {
        void startTurning();

        void stopTurning();
    }
}
