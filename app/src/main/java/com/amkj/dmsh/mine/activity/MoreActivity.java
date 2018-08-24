package com.amkj.dmsh.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.MainActivity;
import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.utils.FileCacheUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.tencent.bugly.beta.Beta;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.getPersonalInfo;
import static com.amkj.dmsh.utils.FileCacheUtils.getFolderSize;

;

/**
 * Created by atd48 on 2016/10/19.
 */
public class MoreActivity extends BaseActivity {
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tv_header_shared)
    TextView header_shared;
    //    缓存大小
    @BindView(R.id.tv_mine_more_clean_cache)
    TextView tv_mine_more_clean_cache;
    //    当前版本号
    @BindView(R.id.tv_mine_more_version_num)
    TextView tv_mine_more_version_num;
    private String Img_PATH;
    private List<File> files = new ArrayList<>();
    private int uid;
    @Override
    protected int getContentView() {
        return R.layout.activity_mine_more;
    }
    @Override
    protected void initViews() {
        isLoginStatus();
        tv_header_titleAll.setText("更多");
        header_shared.setVisibility(View.INVISIBLE);
        Img_PATH = getFilesDir().getAbsolutePath() + "/ImgArticle";
        files.add(new File(Img_PATH));
        files.add(MoreActivity.this.getCacheDir());
        if (getGlideCacheFile(MoreActivity.this) != null) {
            files.add(getGlideCacheFile(MoreActivity.this));
        }
//        查看文件夹的缓存大小
        try {
            tv_mine_more_clean_cache.setText(FileCacheUtils.getCacheListSize(files));
        } catch (Exception e) {
            e.printStackTrace();
        }
        tv_mine_more_version_num.setText(getVersionName());
    }

    private void isLoginStatus() {
        SavePersonalInfoBean personalInfo = getPersonalInfo(this);
        if (personalInfo.isLogin()) {
            uid = personalInfo.getUid();
        } else {
            uid = 0;
        }
    }

    /**
     * 获取Glide造成的缓存大小
     *
     * @return CacheSize
     */
    public File getGlideCacheFile(Context context) {
        try {
            return new File(context.getCacheDir() + "/" + InternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //    返回
    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }

    //    清理缓存
    @OnClick(R.id.tv_mine_more_clean_cache)
    void cleanCache(View view) {
        try {
            if (getFolderSize(new File(Img_PATH)) > 0 || getFolderSize(MoreActivity.this.getCacheDir()) > 0) {
                if (getFolderSize(new File(Img_PATH)) > 0) {
                    FileCacheUtils.deleteFolderFile(Img_PATH, false);
                }
                if (getFolderSize(MoreActivity.this.getCacheDir()) > 0) {
                    FileCacheUtils.cleanInternalCache(MoreActivity.this);
                }
                GlideImageLoaderUtil.clearMemoryByActivity(MoreActivity.this);
                tv_mine_more_clean_cache.setText(FileCacheUtils.getCacheListSize(files));
            } else {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //    意见反馈
    @OnClick(R.id.tv_mine_more_suggestion_feedback)
   void suggestionFeedBack(View view) {
        Intent intent = new Intent();
        intent.setClass(MoreActivity.this, SuggestionFeedBackActivity.class);
        startActivity(intent);
    }

    //   常见问题 关于我们 加入我们
    @OnClick({R.id.tv_mine_more_normal_question, R.id.tv_mine_more_about, R.id.tv_mine_more_join})
    void imgArtMode(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
//            常见问题
            case R.id.tv_mine_more_normal_question:
                break;
//            关于我们
            case R.id.tv_mine_more_about:
                intent.setClass(MoreActivity.this, AboutUsDoMoLifeActivity.class);
                startActivity(intent);
                break;
//            加入我们
            case R.id.tv_mine_more_join:
                if (uid != 0) {
                    intent.setClass(MoreActivity.this, MainActivity.class);
                } else {
                    intent.setClass(MoreActivity.this, MineLoginActivity.class);
                }
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);
                break;
        }
    }

    //    版本更新
    @OnClick(R.id.ll_update_version)
    void versionNum(View view) {
        /***** 检查更新 *****/
        Beta.checkUpgrade();
    }

    private String getVersionName() {
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
            return packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void loadData() {
    }
}
