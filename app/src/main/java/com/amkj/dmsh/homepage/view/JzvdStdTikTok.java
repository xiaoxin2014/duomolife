package com.amkj.dmsh.homepage.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.amkj.dmsh.R;

import cn.jzvd.JzvdStd;

public class JzvdStdTikTok extends JzvdStd {

    private onStartClickListener mOnStartClickListener;
    private ImageView mIvPlay;

    public JzvdStdTikTok(Context context) {
        super(context);
    }

    public JzvdStdTikTok(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init(Context context) {
        super.init(context);
        SAVE_PROGRESS = false;//每次重新播放不要保存进度
        bottomContainer.setVisibility(GONE);
        topContainer.setVisibility(GONE);
        bottomProgressBar.setVisibility(GONE);
        fullscreenButton.setVisibility(INVISIBLE);
        posterImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mIvPlay = findViewById(R.id.iv_play);
        mIvPlay.setOnClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.jz_layout_std_tiktok;
    }

    //changeUiTo 真能能修改ui的方法
    @Override
    public void changeUiToNormal() {//4
        super.changeUiToNormal();
//        bottomContainer.setVisibility(GONE);
        topContainer.setVisibility(GONE);
    }

    @Override//3
    public void setAllControlsVisiblity(int topCon, int bottomCon, int startBtn, int loadingPro,
                                        int posterImg, int bottomPro, int retryLayout) {
        topContainer.setVisibility(INVISIBLE);
//        bottomContainer.setVisibility(INVISIBLE);
//        startButton.setVisibility(startBtn);
        startButton.setVisibility(GONE);
        loadingProgressBar.setVisibility(loadingPro);
        posterImageView.setVisibility(posterImg);
        bottomProgressBar.setVisibility(GONE);
        mRetryLayout.setVisibility(retryLayout);
    }

    @Override
    public void dissmissControlView() {//2
        if (state != STATE_NORMAL
                && state != STATE_ERROR
                && state != STATE_AUTO_COMPLETE) {
            post(() -> {
//                bottomContainer.setVisibility(INVISIBLE);
                topContainer.setVisibility(View.INVISIBLE);
                startButton.setVisibility(View.INVISIBLE);
                if (clarityPopWindow != null) {
                    clarityPopWindow.dismiss();
                }
                if (screen != SCREEN_TINY) {
                    bottomProgressBar.setVisibility(View.GONE);
                }
            });
        }
    }


    @Override
    public void onClickUiToggle() {//1
        super.onClickUiToggle();
        Log.i(TAG, "click blank");
        startButton.performClick();//performClick相当于触发了一次点击事件
        bottomContainer.setVisibility(bottomContainer.getVisibility() == VISIBLE ? GONE : VISIBLE);
        topContainer.setVisibility(GONE);
        if (mOnStartClickListener != null) {
            mOnStartClickListener.click(bottomContainer.getVisibility() == GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.start) {//拦截startButton点击事件
            return;
        } else if (v.getId() == R.id.iv_play) {
            clickStart();
        }
        super.onClick(v);
    }

    @Override
    public void clickStart() {
        super.clickStart();
    }

    public void setOnStartClickListener(onStartClickListener onStartClickListener) {
        mOnStartClickListener = onStartClickListener;
    }

    public void updateStartImage() {
        if (state == STATE_PLAYING) {
            startButton.setVisibility(GONE);
            startButton.setImageResource(R.drawable.tiktok_play_tiktok);
            replayTextView.setVisibility(GONE);
        } else if (state == STATE_ERROR) {
            startButton.setVisibility(INVISIBLE);
            replayTextView.setVisibility(GONE);
        } else if (state == STATE_AUTO_COMPLETE) {
            startButton.setVisibility(GONE);
            startButton.setImageResource(R.drawable.tiktok_play_tiktok);
            replayTextView.setVisibility(VISIBLE);
        } else {
            startButton.setImageResource(R.drawable.tiktok_play_tiktok);
            replayTextView.setVisibility(GONE);
        }
    }

    public interface onStartClickListener {
        void click(boolean showProduct);
    }


    @Override
    public void onStatePlaying() {
        super.onStatePlaying();
        mIvPlay.setImageResource(R.drawable.pause);
    }

    @Override
    public void onStatePause() {
        super.onStatePause();
        mIvPlay.setImageResource(R.drawable.play_video);
    }


    public void setTouchingProgressBar() {
        mTouchingProgressBar = false;
    }


    /**
     * 视频来回切换之后播放进度就停止了，查看源码后发现mTouchingProgressBar为false时才会更新这个进度，
     * 什么原因导致的那就只有写这个库的人才知道了，暂时手动修改为false解决
     */
    @Override
    public void onProgress(int progress, long position, long duration) {
        mTouchingProgressBar = false;
        super.onProgress(progress, position, duration);
    }
}
