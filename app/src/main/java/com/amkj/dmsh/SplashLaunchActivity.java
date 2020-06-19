package com.amkj.dmsh;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.utils.SharedPreUtils;
import com.gyf.barlibrary.BarHide;
import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static androidx.viewpager.widget.ViewPager.SCROLL_STATE_IDLE;
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
    private Unbinder mBind;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_guide);
        mBind = ButterKnife.bind(this);
        initViews();
        if (BuildConfig.DEBUG) Log.d("className", getClass().getSimpleName());
    }


    protected void initViews() {
        //全屏|隐藏导航栏
        ImmersionBar.with(this).fullScreen(true).hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR).init();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBind.unbind();
        ImmersionBar.with(this).destroy();
    }
}
