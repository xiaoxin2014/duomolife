package com.amkj.dmsh.views.JzVideo;

import android.content.Context;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;

import java.util.LinkedHashMap;

import cn.jzvd.JZDataSource;
import cn.jzvd.JZMediaManager;
import cn.jzvd.JZUserAction;
import cn.jzvd.JZUtils;
import cn.jzvd.Jzvd;

import static cn.jzvd.JZDataSource.URL_KEY_DEFAULT;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;

;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/2/5
 * version 3.9
 * class description:自定义音频播放
 */

public class CustomAudioPlayer extends JzVideoPlayerStatusDialog {

    private ImageView ib_audio_player;
    private TextView tv_audio_player_title,tv_audio_player_source;
    private RelativeLayout rel_audio;
    private LinkedHashMap linkedHashMap = new LinkedHashMap();

    public CustomAudioPlayer(Context context) {
        super(context);
    }
    public CustomAudioPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.jz_layout_standard_audio;
    }

    @Override
    public void init(Context context) {
        super.init(context);
        ib_audio_player = findViewById(R.id.ib_audio_player);
        tv_audio_player_title = findViewById(R.id.tv_audio_player_title);
        tv_audio_player_source = findViewById(R.id.tv_audio_player_source);
        rel_audio = findViewById(R.id.rel_audio);
        rel_audio.setVisibility(GONE);
        ib_audio_player.setOnClickListener(this);
    }

    /**
     * 设置音频数据
     * @param url
     * @param title
     * @param sourceTitle
     */
    public void setAudioData(@NonNull String url,String title,String sourceTitle){
        linkedHashMap.put(URL_KEY_DEFAULT, url);
        if(!TextUtils.isEmpty(title)){
            tv_audio_player_title.setVisibility(VISIBLE);
            tv_audio_player_title.setText(title);
        }else{
            tv_audio_player_title.setVisibility(GONE);
        }
        if(!TextUtils.isEmpty(sourceTitle)){
            tv_audio_player_source.setVisibility(VISIBLE);
            tv_audio_player_source.setText(sourceTitle);
        }else{
            tv_audio_player_source.setVisibility(GONE);
        }
        JZDataSource jzDataSource = new JZDataSource(linkedHashMap,"");
        setUp(jzDataSource, Jzvd.SCREEN_WINDOW_NORMAL);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(v.getId() == R.id.ib_audio_player){
            if (currentState == CURRENT_STATE_NORMAL) {
                if (!JZUtils.isWifiConnected(getContext()) && !WIFI_TIP_DIALOG_SHOWED) {
                    showWifiDialog();
                    return;
                }
                startVideo();
                onEvent(JZUserAction.ON_CLICK_START_ICON);
            } else if (currentState == CURRENT_STATE_PLAYING) {
                onEvent(JZUserAction.ON_CLICK_PAUSE);
                JZMediaManager.pause();
                onStatePause();
            } else if (currentState == CURRENT_STATE_PAUSE) {
                onEvent(JZUserAction.ON_CLICK_RESUME);
                JZMediaManager.start();
                onStatePlaying();
            } else if (currentState == CURRENT_STATE_AUTO_COMPLETE) {
                onEvent(JZUserAction.ON_CLICK_START_AUTO_COMPLETE);
                startVideo();
            }
        }
    }

    @Override
    public void startVideo() {
        super.startVideo();
        ib_audio_player.setSelected(true);
    }

    @Override
    public void onStateNormal() {
        super.onStateNormal();
        ib_audio_player.setSelected(false);
    }

    @Override
    public void onStatePlaying() {
        super.onStatePlaying();
        ib_audio_player.setSelected(true);
    }
//
    @Override
    public void onStatePause() {
        super.onStatePause();
        ib_audio_player.setSelected(false);
    }

    @Override
    public void onStateAutoComplete() {
        super.onStateAutoComplete();
        ib_audio_player.setSelected(false);
    }

    @Override
    public void onError(int what, int extra) {
        ib_audio_player.setSelected(false);
        showToast("播放异常，请重新刷新重试");
    }

    @Override
    public void onStateError() {
        super.onStateError();
        ib_audio_player.setSelected(false);
        showToast("播放异常，请重新刷新重试");
    }
}