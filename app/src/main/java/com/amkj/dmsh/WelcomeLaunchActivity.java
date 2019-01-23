package com.amkj.dmsh;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.MainActivity.ImgKey;
import static com.amkj.dmsh.MainActivity.LauncherAdIdKey;
import static com.amkj.dmsh.MainActivity.SkipUrlKey;
import static com.amkj.dmsh.MainActivity.TimeKey;
import static com.amkj.dmsh.constant.ConstantMethod.adClickTotal;
import static com.amkj.dmsh.constant.ConstantMethod.setSkipPath;

;

/**
 * @author Liuguipeng
 * @email liuguipeng163@163.com
 * created on 2018/4/27
 * version 3.1.2
 * class description 欢迎页面 启动广告
 */

public class WelcomeLaunchActivity extends BaseActivity {
    //    跳过
    @BindView(R.id.tv_launch_wel_skip_main)
    TextView tv_launch_wel_skip_main;
    //    启动页
    @BindView(R.id.iv_launch_wel_page)
    ImageView iv_launch_wel_page;

    @BindView(R.id.reg_req_code_gif_skip)
    ProgressBar reg_req_code_gif_skip;
    @BindView(R.id.fl_skip)
    FrameLayout fl_skip;
    private int show_time;
    private boolean isOnPause;
    private String imgPath;
    private String showSeconds;
    private SharedPreferences sharedPreferences;
    private String skipUrlPath;
    private int launcherAdId;
    private ConstantMethod constantMethod;

    @Override
    protected int getContentView() {
        return R.layout.activity_launch_welcome;
    }

    @Override
    protected void initViews() {
        hideNavStatus();
        sharedPreferences = getSharedPreferences("launchAD", Context.MODE_PRIVATE);
        if (sharedPreferences == null) {
            return;
        }
        imgPath = sharedPreferences.getString(ImgKey, "");
        skipUrlPath = sharedPreferences.getString(SkipUrlKey, "");
        launcherAdId = sharedPreferences.getInt(LauncherAdIdKey, 0);
    }

    @Override
    public void setStatusBar() {
    }

    private void hideNavStatus() {
        View decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == 0) {
                    fl_skip.setFocusable(true);
                    fl_skip.setFocusableInTouchMode(true);
                    fl_skip.requestFocus();
                }
            }
        });
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT < 19) { // lower api
            decorView.setSystemUiVisibility(View.GONE);
        } else {
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    private void setAdDataShow(String showSeconds) {
        fl_skip.setVisibility(View.VISIBLE);
        show_time = Integer.parseInt(getNumber(!TextUtils.isEmpty(showSeconds) ? showSeconds : "3"));
        show_time = (show_time > 4 ? 5 : show_time < 1 ? 5 : show_time);
        tv_launch_wel_skip_main.setText((show_time + " 跳过"));
        constantMethod = new ConstantMethod();
        constantMethod.setRefreshTimeListener(new ConstantMethod.RefreshTimeListener() {
            @Override
            public void refreshTime() {
                --show_time;
                if (show_time >= 0) {
                    tv_launch_wel_skip_main.setText((show_time + " 跳过"));
                }
                if (show_time == 0) {
                    constantMethod.stopSchedule();
                    skipMainActivity();
                }
            }
        });
        constantMethod.createSchedule();
    }

    @Override
    protected void loadData() {
    }

    private String getNumber(String str) {
        String regex = "\\d*";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        while (m.find()) {
            String findData = m.group();
            if (!"".equals(findData)) {
                return !findData.equals("0") ? findData : "3";
            }
        }
        return "5";
    }

    private String getNorNumber(String str) {
        String regex = "\\d*";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        while (m.find()) {
            String findData = m.group();
            if (!"".equals(findData)) {
                return findData;
            }
        }
        return "0";
    }

    private void setSkipLocalPath(String link) {
//        启东广告点击统计
        adClickTotal(launcherAdId);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        setSkipPath(WelcomeLaunchActivity.this, link, false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isOnPause = true;
    }

    @Override
    protected void onResume() {
        if (isOnPause) {
            skipMainActivity();
        } else {
            setLaunchImage();
        }
        super.onResume();
    }

    /**
     * 设置启动图片
     */
    private void setLaunchImage() {
        if (!TextUtils.isEmpty(imgPath)&&sharedPreferences!=null) {
            showSeconds = sharedPreferences.getString(TimeKey, "5");
            GlideImageLoaderUtil.loadCenterCropListener(WelcomeLaunchActivity.this, iv_launch_wel_page
                    , "file://" + imgPath, new GlideImageLoaderUtil.ImageLoaderListener() {
                        @Override
                        public void onSuccess() {
                            if (Integer.parseInt(getNorNumber(showSeconds)) > 0) {
                                setAdDataShow(showSeconds);
                            } else {
                                setSkipClickPath(null);
                            }
                        }

                        @Override
                        public void onError() {
                            setSkipClickPath(null);
                        }
                    });
        } else {
            setSkipClickPath(null);
        }
    }

    @OnClick(value = {R.id.fl_skip, R.id.tv_launch_wel_skip_main})
    void skipMain() {
        setSkipClickPath(null);
    }

    @OnClick(R.id.iv_launch_wel_page)
    void skipPath(View view) {
        view.setEnabled(false);
        setSkipClickPath(skipUrlPath);
    }

    /**
     * 设置主动点击 被动跳转
     *
     * @param path
     */
    private void setSkipClickPath(String path) {
        tv_launch_wel_skip_main.setVisibility(View.GONE);
        reg_req_code_gif_skip.setVisibility(View.VISIBLE);
        isOnPause = true;
        if (constantMethod != null) {
            constantMethod.stopSchedule();
        }
        if (!TextUtils.isEmpty(path)) {
            setSkipLocalPath(path);
        } else {
            skipMainActivity();
        }
    }

    private void skipMainActivity() {
        Intent intent = new Intent(WelcomeLaunchActivity.this, MainActivity.class);
        startActivity(intent);
        if (isOnPause) {
            finish();
            overridePendingTransition(0, 0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (constantMethod != null) {
            constantMethod.releaseHandlers();
        }
    }
}
