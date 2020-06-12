package com.amkj.dmsh.homepage.activity;

import android.content.Intent;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.views.JzVideo.JzVideoPlayerFullScrren;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;
import cn.jzvd.JZDataSource;
import cn.jzvd.JZUserAction;

import static cn.jzvd.Jzvd.SCREEN_WINDOW_NORMAL;

/**
 * Created by xiaoxin on 2020/6/11
 * Version:v4.6.1
 * ClassDescription :饺子视频全屏播放(单独开一个页面，解决视频全屏播放时页面适配失效的问题)
 */
public class JzVideoFullScreenActivity extends BaseActivity {
    @BindView(R.id.jvp_find_video_play)
    JzVideoPlayerFullScrren mJvpFindVideoPlay;

    @Override
    protected int getContentView() {
        return R.layout.activity_jz_video_fullscreen;
    }

    @Override
    protected void initViews() {
        if (getIntent() == null) return;
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        JZDataSource jzDataSource = new JZDataSource(url);
        mJvpFindVideoPlay.setUp(jzDataSource, SCREEN_WINDOW_NORMAL);
        mJvpFindVideoPlay.startVideo();//开始播放
        mJvpFindVideoPlay.onEvent(JZUserAction.ON_CLICK_START_ICON);
        mJvpFindVideoPlay.startWindowFullscreen();//全屏播放
    }

    @Override
    protected void loadData() {
        //全屏显示
        ImmersionBar.with(this).fullScreen(true).init();
    }

    //拦截返回按键
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void setStatusBar() {
    }
}
