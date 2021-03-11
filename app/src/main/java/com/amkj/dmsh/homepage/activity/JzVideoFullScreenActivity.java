package com.amkj.dmsh.homepage.activity;

import android.content.Intent;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.views.JzVideo.JzVideoStdFullScrren;
import com.gyf.barlibrary.BarHide;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;

import static cn.jzvd.Jzvd.SCREEN_FULLSCREEN;


/**
 * Created by xiaoxin on 2020/6/11
 * Version:v4.6.1
 * ClassDescription :饺子视频全屏播放(单独开一个页面，解决视频全屏播放时页面适配失效的问题)
 */
public class JzVideoFullScreenActivity extends BaseActivity {
    @BindView(R.id.jvp_find_video_play)
    JzVideoStdFullScrren mJvpFindVideoPlay;

    @Override
    protected int getContentView() {
        return R.layout.activity_jz_video_fullscreen;
    }

    @Override
    protected void initViews() {
        if (getIntent() == null) return;
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        String cover = intent.getStringExtra("cover");
        mJvpFindVideoPlay.setUp(url, "", SCREEN_FULLSCREEN);
        GlideImageLoaderUtil.loadCenterCrop(this, mJvpFindVideoPlay.posterImageView, cover);
        mJvpFindVideoPlay.startVideoAfterPreloading();//开始播放
    }

    @Override
    protected void loadData() {
    }

    @Override
    public void setStatusBar() {
        //全屏|隐藏状态栏
        ImmersionBar.with(this).fullScreen(true).hideBar(BarHide.FLAG_HIDE_STATUS_BAR).init();
    }

    //拦截返回按键
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //释放视频资源，修复内存泄漏
        mJvpFindVideoPlay.reset();
    }
}
