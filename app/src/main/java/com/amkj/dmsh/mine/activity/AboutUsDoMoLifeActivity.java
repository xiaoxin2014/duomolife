package com.amkj.dmsh.mine.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.constant.AppUpdateUtils;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantVariable.WEB_TYPE_PRIVACY_POLICY;
import static com.amkj.dmsh.constant.ConstantVariable.WEB_TYPE_REG_AGREEMENT;
import static com.amkj.dmsh.constant.ConstantVariable.WEB_VALUE_TYPE;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/3/6
 * class description:关于我们
 */
public class AboutUsDoMoLifeActivity extends BaseActivity {
    //    获取版本号
    @BindView(R.id.tv_mine_more_about_version_num)
    TextView tv_mine_more_about_version_num;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tv_header_shared)
    TextView header_shared;
    @BindView(R.id.tl_normal_bar)
    Toolbar tl_normal_bar;

    @Override
    protected int getContentView() {
        return R.layout.activity_mine_more_about_us;
    }

    @Override
    protected void initViews() {
        tv_header_titleAll.setText("关于多么生活");
        header_shared.setVisibility(View.INVISIBLE);
        tl_normal_bar.setSelected(true);
        tv_mine_more_about_version_num.setText(("当前版本: v" + getVersionName()));
    }

    @Override
    protected void loadData() {
    }

    //    返回
    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }

    //    获取版本号
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

    @OnClick(R.id.tv_check_update)
    public void updateApp() {
        /***** 检查更新 *****/
        AppUpdateUtils.getInstance().getAppUpdate(AboutUsDoMoLifeActivity.this, true);
    }

    @OnClick({R.id.tv_user_register_agreement, R.id.tv_user_privacy_policy})
    public void agreementPolicy(View view) {
        Intent intent = new Intent(this, WebRuleCommunalActivity.class);
        intent.putExtra(WEB_VALUE_TYPE, view.getId() == R.id.tv_user_register_agreement ?
                WEB_TYPE_REG_AGREEMENT : WEB_TYPE_PRIVACY_POLICY);
        startActivity(intent);
    }
}
