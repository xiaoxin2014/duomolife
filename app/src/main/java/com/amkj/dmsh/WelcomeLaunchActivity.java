package com.amkj.dmsh;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amkj.dmsh.base.BaseActivity;
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
    private boolean isShow;
    private String skipUrlPath;
    private int launcherAdId;
    @Override
    protected int getContentView() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_launch_welcome;
    }
    @Override
    protected void initViews() {
        sharedPreferences = getSharedPreferences("launchAD", Context.MODE_PRIVATE);
        imgPath = sharedPreferences.getString(ImgKey, "");
        skipUrlPath = sharedPreferences.getString(SkipUrlKey, "");
        launcherAdId = sharedPreferences.getInt(LauncherAdIdKey, 0);
        if (!TextUtils.isEmpty(imgPath)) {
            showSeconds = sharedPreferences.getString(TimeKey, "5");
            GlideImageLoaderUtil.loadCenterCrop(WelcomeLaunchActivity.this,iv_launch_wel_page,"file://"+imgPath);
            if (Integer.parseInt(getNorNumber(showSeconds)) > 0) {
                setAdDataShow(showSeconds);
            } else {
                isOnPause = true;
                skipMainActivity();
            }
        } else {
            Intent intent = new Intent(WelcomeLaunchActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(0, 0);
        }
    }

    private void setAdDataShow(String showSeconds) {
        fl_skip.setVisibility(View.VISIBLE);
        isShow = true;
        show_time = Integer.parseInt(getNumber(!TextUtils.isEmpty(showSeconds) ? showSeconds : "3"));
        show_time = (show_time > 4 ? 5 : show_time < 1 ? 5 : show_time);
        tv_launch_wel_skip_main.setText(show_time + " 跳过");
        handler.postDelayed(runnable, 0);
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
        if (runnable != null) {
            runnable = null;
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
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
    protected void onPause() {
        super.onPause();
        isOnPause = true;
    }

    @Override
    protected void onResume() {
        if (isOnPause) {
            skipMainActivity();
        }
        super.onResume();
    }

    @OnClick(R.id.tv_launch_wel_skip_main)
    void skipMain(View view) {
        tv_launch_wel_skip_main.setVisibility(View.GONE);
        reg_req_code_gif_skip.setVisibility(View.VISIBLE);
        isOnPause = true;
        skipMainActivity();
    }

    @OnClick(R.id.iv_launch_wel_page)
    void skipPath(View view) {
        setSkipLocalPath(skipUrlPath);
    }

    private Handler handler = new Handler();

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (show_time >= 0) {
                tv_launch_wel_skip_main.setText(show_time + " 跳过");
            }
            if (show_time == 0) {
                skipMainActivity();
            }
            show_time--;
            handler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        if (runnable != null) {
            runnable = null;
        }
        super.onDestroy();
    }
}
