package com.amkj.dmsh;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.view.ViewPager;

import com.amkj.dmsh.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static android.support.v4.view.ViewPager.SCROLL_STATE_IDLE;

public class GuideLaunchActivity extends BaseActivity {
    @BindView(R.id.vp_guide_images)
    ViewPager vp_guide_images;
    //    网络图片
    //    本地图片
    private List<Integer> localImages = new ArrayList<>();
    private boolean firstRun;
    private long oldSlidingGap = 0;
    private boolean isConform = false;

    @Override
    protected int getContentView() {
        return R.layout.activity_launch_guide;
    }

    protected void initViews() {
        if (!this.isTaskRoot()) {
            finish();
            return;
        }
        SharedPreferences setIcon = getSharedPreferences("set_icon", MODE_PRIVATE);
        int iconCode = setIcon.getInt("iconCode", 3);
        switchIcon(iconCode);
        firstRun = isFirstRun();
        if (firstRun) {
            localImages.add(R.mipmap.guide1);
            localImages.add(R.mipmap.guide2);
            localImages.add(R.mipmap.guide3);
            localImages.add(R.mipmap.guide4);
            vp_guide_images.setAdapter(new GuideImagesPagerAdapter(GuideLaunchActivity.this, localImages));
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
                        SharedPreferences sp = getSharedPreferences("duomolife", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putBoolean("isFirstRun", false);
                        edit.apply();
                        skipWelcome();
                    }
                }
            });
        } else {
            skipWelcome();
        }
    }

    private void skipWelcome() {
        Intent intent = new Intent(GuideLaunchActivity.this, WelcomeLaunchActivity.class);
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
        SharedPreferences sp = getSharedPreferences("duomolife", MODE_PRIVATE);
        return sp.getBoolean("isFirstRun", true);
    }


    /**
     * @param useCode =1、为活动图标 = 2 为用普通图标 =3、不启用判断
     */
    private void switchIcon(int useCode) {
        SharedPreferences setIcon = getSharedPreferences("set_icon", MODE_PRIVATE);
        SharedPreferences.Editor edit = setIcon.edit();
        edit.putInt("iconCode",useCode-1>0?--useCode:3);
        edit.apply();
        com.amkj.dmsh.utils.Log.d("启动", "当前状态 switchIcon: " + useCode);
        try {
            //要跟manifest的activity-alias 的name保持一致
            String icon_festival = "com.amkj.dmsh.FestivalActivity";
            String icon_normal = "com.amkj.dmsh.NormalActivity";
            if (useCode != 3) {
                PackageManager pm = getPackageManager();
                ComponentName normalComponentName = new ComponentName(
                        getBaseContext(),
                        icon_normal);
                //正常图标新状态
                int normalNewState = useCode == 2 ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                        : PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
                if (pm.getComponentEnabledSetting(normalComponentName) != normalNewState) {//新状态跟当前状态不一样才执行
                    pm.setComponentEnabledSetting(
                            normalComponentName,
                            normalNewState,
                            PackageManager.DONT_KILL_APP);
                    com.amkj.dmsh.utils.Log.d("启动", "设置 switchIcon: " + "正常状态");
                }
                ComponentName actComponentName = new ComponentName(
                        getBaseContext(),
                        icon_festival);
                //正常图标新状态
                int actNewState = useCode == 1 ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                        : PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
                if (pm.getComponentEnabledSetting(actComponentName) != actNewState) {//新状态跟当前状态不一样才执行
                    pm.setComponentEnabledSetting(
                            actComponentName,
                            actNewState,
                            PackageManager.DONT_KILL_APP);
                    com.amkj.dmsh.utils.Log.d("启动", "设置 switchIcon: " + "活动状态");
                }
            }
        } catch (Exception e) {
            com.amkj.dmsh.utils.Log.d("启动", "switchIcon: " + e.getMessage());
        }
    }
}
