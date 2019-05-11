package com.amkj.dmsh;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.utils.SharedPreUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v4.view.ViewPager.SCROLL_STATE_IDLE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_NEW_USER;

/**
 * 闪屏页
 */
public class SplashLaunchActivity extends AppCompatActivity {
    @BindView(R.id.vp_guide_images)
    ViewPager vp_guide_images;
    //    网络图片
    //    本地图片
    private List<Integer> localImages = new ArrayList<>();
    private long oldSlidingGap = 0;
    private boolean isConform = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_guide);
        ButterKnife.bind(this);
        initViews();
    }



    protected void initViews() {
        hideNavStatus();
        if (!this.isTaskRoot()) {
            finish();
            return;
        }

        //是否进入引导页
        if (isFirstRun()) {
            //保存新用户标志
            SharedPreUtils.setParam(ConstantVariable.DEMO_LIFE_FILE, IS_NEW_USER, true);
            localImages.add(R.mipmap.guide1);
            localImages.add(R.mipmap.guide2);
            localImages.add(R.mipmap.guide3);
            localImages.add(R.mipmap.guide4);
            vp_guide_images.setAdapter(new GuideImagesPagerAdapter(SplashLaunchActivity.this, localImages));
            vp_guide_images.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    if (position == localImages.size() - 1) {
                        long time = System.currentTimeMillis() - oldSlidingGap;
                        if (oldSlidingGap > 0 && time < 200) {
                            isConform = true;
                        }
                        oldSlidingGap = System.currentTimeMillis();
                    }
                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    if (isConform && state == SCROLL_STATE_IDLE) {
                        SharedPreUtils.setParam(ConstantVariable.DEMO_LIFE_FILE, "isFirstRun", false);
                        skipWelcome();
                    }
                }
            });
        } else {
            skipWelcome();
        }
    }


    private void hideNavStatus() {
        View decorView = getWindow().getDecorView();
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT < 19) { // lower api
            decorView.setSystemUiVisibility(View.GONE);
        } else {
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    private void skipWelcome() {
        Intent intent = new Intent(this, WelcomeLaunchActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(0, 0);
    }

    // 添加此处目的是针对后台APP通过uri scheme唤起的情况，
    // 注意：即使不区分用户是否登录也需要添加此设置，也可以添加到基类中
    private boolean isFirstRun() {
        return (boolean) SharedPreUtils.getParam(ConstantVariable.DEMO_LIFE_FILE, "isFirstRun", true);
    }
}
