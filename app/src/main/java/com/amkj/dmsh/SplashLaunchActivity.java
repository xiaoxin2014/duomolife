package com.amkj.dmsh;

import android.content.Intent;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.SharedPreUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static android.support.v4.view.ViewPager.SCROLL_STATE_IDLE;
import static com.amkj.dmsh.constant.ConstantVariable.GET_FIRST_INSTALL_INFO;
import static com.amkj.dmsh.constant.ConstantVariable.IS_NEW_USER;
import static com.amkj.dmsh.constant.ConstantVariable.TOKEN_EXPIRE_TIME;
import static com.amkj.dmsh.constant.ConstantVariable.TOKEN_REFRESH_TIME;

/**
 * 闪屏页
 */
public class SplashLaunchActivity extends BaseActivity {
    @BindView(R.id.vp_guide_images)
    ViewPager vp_guide_images;
    //    网络图片
    //    本地图片
    private List<Integer> localImages = new ArrayList<>();
    private long oldSlidingGap = 0;
    private boolean isConform = false;

    @Override
    protected int getContentView() {
        return R.layout.activity_launch_guide;
    }

    protected void initViews() {
        hideNavStatus();
        if (!this.isTaskRoot()) {
            finish();
            return;
        }
        long tokenExpireTime = ((long) SharedPreUtils.getParam(TOKEN_EXPIRE_TIME, 0L));
        long tokenRefreshTime = ((long) SharedPreUtils.getParam(TOKEN_REFRESH_TIME, 0L));
        //如果Token有效刷新Token(每24个小时刷新一次)
        if (tokenExpireTime != 0 && System.currentTimeMillis()/1000 < tokenExpireTime && System.currentTimeMillis()- tokenRefreshTime > 86400000) {
            NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.FLUSH_LOGIN_TOKEN, null);
            //记录刷新时间
            SharedPreUtils.setParam(TOKEN_REFRESH_TIME, System.currentTimeMillis());
        }

        //是否进入引导页
        if (isFirstRun()) {
            //保存新用户标志
            SharedPreUtils.setParam(ConstantVariable.DEMO_LIFE_FILE, IS_NEW_USER, true);
            //统计首次安装设备信息
            getFirstInstallInfo();
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
            boolean isNewUser = (boolean) SharedPreUtils.getParam(ConstantVariable.DEMO_LIFE_FILE, IS_NEW_USER, false);
            boolean GetInfo = (boolean) SharedPreUtils.getParam(ConstantVariable.DEMO_LIFE_FILE, GET_FIRST_INSTALL_INFO, false);
            //如果是新用户并且没有成功调用统计接口
            if (isNewUser && !GetInfo) {
                getFirstInstallInfo();
            }
            skipWelcome();
        }
    }

    private void getFirstInstallInfo() {
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.FIRST_INSTALL_DEVICE_INFO, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                SharedPreUtils.setParam(ConstantVariable.DEMO_LIFE_FILE, IS_NEW_USER, false);
                SharedPreUtils.setParam(ConstantVariable.DEMO_LIFE_FILE, GET_FIRST_INSTALL_INFO, true);
            }
        });
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
        Intent intent = new Intent(SplashLaunchActivity.this, WelcomeLaunchActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void loadData() {
    }

    // 添加此处目的是针对后台APP通过uri scheme唤起的情况，
    // 注意：即使不区分用户是否登录也需要添加此设置，也可以添加到基类中
    private boolean isFirstRun() {
        return (boolean) SharedPreUtils.getParam(ConstantVariable.DEMO_LIFE_FILE, "isFirstRun", true);
    }
}
