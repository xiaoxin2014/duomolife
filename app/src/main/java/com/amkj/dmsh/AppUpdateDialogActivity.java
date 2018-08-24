package com.amkj.dmsh;

import android.content.Intent;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.utils.ServiceDownUtils;
import com.zhy.autolayout.utils.AutoUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.getAppendNumber;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getVersionName;
import static com.amkj.dmsh.constant.ConstantMethod.installApps;
import static com.amkj.dmsh.constant.ConstantMethod.isHeightVersion;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.APP_CURRENT_UPDATE_VERSION;
import static com.amkj.dmsh.constant.ConstantVariable.VERSION_DOWN_LINK;
import static com.amkj.dmsh.constant.ConstantVariable.VERSION_UPDATE_DESCRIPTION;
import static com.amkj.dmsh.constant.ConstantVariable.VERSION_UPDATE_LOW;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/4/27
 * version 3.1.2
 * class description:更新dialog
 */
public class AppUpdateDialogActivity extends BaseActivity {
    @BindView(R.id.tv_app_version_info)
    TextView tvAppVersionInfo;
    @BindView(R.id.tv_app_version_description)
    TextView tvAppVersionDescription;
    @BindView(R.id.tv_app_version_update)
    TextView tvAppVersionUpdate;
    @BindView(R.id.seek_bar_app_version_total)
    SeekBar seekBarAppVersionTotal;
    @BindView(R.id.tv_app_version_total_number)
    TextView tvAppVersionTotalNumber;
    @BindView(R.id.ll_app_version_down_total)
    LinearLayout llAppVersionDownTotal;
    private boolean exists;
    private boolean constraintUpdate;
    private String downLink;
    private String saveAppPath;
    private File downAppFile;

    @Override
    protected int getContentView() {
        return R.layout.dialog_activity_app_versions_dialog;
    }

    @Override
    protected void initViews() {
        Window window = getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = AutoUtils.getPercentWidthSize(500);
        window.setAttributes(params);
        Intent intent = getIntent();
        downLink = intent.getStringExtra(VERSION_DOWN_LINK);
        String versionDescription = intent.getStringExtra(VERSION_UPDATE_DESCRIPTION);
        String versionLow = intent.getStringExtra(VERSION_UPDATE_LOW);
        String appCurrentVersion = intent.getStringExtra(APP_CURRENT_UPDATE_VERSION);
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            saveAppPath = Environment.getExternalStorageDirectory() + "/download" + (
                    downLink.contains("/") ? downLink.substring(downLink.lastIndexOf("/")) : "domolife.apk");
            downAppFile = new File(saveAppPath);
//        是否已下载当前版本
            exists = downAppFile.exists();
        }
        tvAppVersionDescription.setMovementMethod(ScrollingMovementMethod.getInstance());
        tvAppVersionDescription.setText(getStrings(versionDescription));
        tvAppVersionUpdate.setText(exists ? "安装" : "更新");
        tvAppVersionInfo.setText(("多么生活 "+getStrings(appCurrentVersion)));
//        是否低于强制更新的版本
        constraintUpdate = isConstraintUpdate(versionLow);
        setFinishOnTouchOutside(!constraintUpdate);
    }

    @Override
    protected void loadData() {
    }


    @OnClick(R.id.tv_app_version_update)
    void updateInstall() {
        if (exists) {
            if (isHeightVersion(AppUpdateDialogActivity.this, saveAppPath)) {
                installApps(AppUpdateDialogActivity.this,downAppFile);
            } else {
                showToast(AppUpdateDialogActivity.this, R.string.app_version_tint);
                finish();
            }
        } else if (!TextUtils.isEmpty(downLink)) {
            Intent intent = new Intent(AppUpdateDialogActivity.this, ServiceDownUtils.class);
            intent.putExtra("downUrl", downLink);
            intent.putExtra("downFilePath", saveAppPath);
            if (constraintUpdate) {
                intent.putExtra("isInstallApp", true);
                intent.putExtra("isShowProgress", true);
            } else {
                showToast(AppUpdateDialogActivity.this, "正在下载……");
                intent.putExtra("isInstallApp", true);
            }
            AppUpdateDialogActivity.this.startService(intent);
            finish();
        }
    }

    /**
     * 版本比较
     * 强制更新版本
     *
     * @param lowestVersion 目标版本
     * @return
     */
    private boolean isConstraintUpdate(String lowestVersion) {
        String constraintVersion = getAppendNumber(lowestVersion);
        String currentVersion = getAppendNumber(getVersionName(AppUpdateDialogActivity.this));
        int constraintLength = constraintVersion.length();
        int currentLength = currentVersion.length();
        int absNumber = Math.abs(constraintLength - currentLength);
        if (absNumber > 0) {
            if (constraintLength > currentLength) {
                currentVersion += String.format("%1$0" + absNumber + "d", 0);
            } else {
                constraintVersion += String.format("%1$0" + absNumber + "d", 0);
            }
        }
        return Long.parseLong(constraintVersion) >= Long.parseLong(currentVersion);
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (message.type.equals("appVersionProgress")) {
            int total = (int) message.result;
            llAppVersionDownTotal.setVisibility(View.VISIBLE);
            tvAppVersionUpdate.setVisibility(View.GONE);
            seekBarAppVersionTotal.setProgress(total);
            tvAppVersionTotalNumber.setText(((total > 100 ? 100 : total) + "%"));
        }
    }
}
