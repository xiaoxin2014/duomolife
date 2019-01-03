package com.amkj.dmsh;

import android.content.Intent;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.utils.ServiceDownUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getAppendNumber;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getVersionName;
import static com.amkj.dmsh.constant.ConstantMethod.installApps;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.APP_CURRENT_UPDATE_VERSION;
import static com.amkj.dmsh.constant.ConstantVariable.APP_MANDATORY_UPDATE_VERSION;
import static com.amkj.dmsh.constant.ConstantVariable.MANDATORY_UPDATE_DESCRIPTION;
import static com.amkj.dmsh.constant.ConstantVariable.MANDATORY_UPDATE_LAST_VERSION;
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
    @BindView(R.id.rel_layout_update_version)
    RelativeLayout rel_layout_update_version;
    private boolean constraintUpdate;
    private String downLink;
    private String saveAppPath;
    private ConstantMethod constantMethod;
    private String filePath;

    @Override
    protected int getContentView() {
        return R.layout.dialog_activity_app_versions_dialog;
    }

    @Override
    protected void initViews() {
        Window window = getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = AutoSizeUtils.mm2px(mAppContext, 500);
        window.setAttributes(params);
        Intent intent = getIntent();
        downLink = intent.getStringExtra(VERSION_DOWN_LINK);
        String versionDescription = intent.getStringExtra(VERSION_UPDATE_DESCRIPTION);
        String versionLow = intent.getStringExtra(VERSION_UPDATE_LOW);
        String appCurrentVersion = intent.getStringExtra(APP_CURRENT_UPDATE_VERSION);
        String appLastVersion = intent.getStringExtra(MANDATORY_UPDATE_LAST_VERSION);
//        强制更新此版本详情
        String mandatoryDescription = intent.getStringExtra(MANDATORY_UPDATE_DESCRIPTION);
//        是否强制更新此版本
        String mandatoryUpdateCode = intent.getStringExtra(APP_MANDATORY_UPDATE_VERSION);
        boolean isMandatoryUpdate = !TextUtils.isEmpty(mandatoryUpdateCode) && Integer.parseInt(mandatoryUpdateCode) == 1;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            saveAppPath = Environment.getExternalStorageDirectory() + "/download";
        }
        tvAppVersionDescription.setMovementMethod(ScrollingMovementMethod.getInstance());
        tvAppVersionDescription.setText(isMandatoryUpdate ? (!TextUtils.isEmpty(mandatoryDescription) ?
                getStrings(mandatoryDescription) : getStrings(versionDescription)) : getStrings(versionDescription));
        tvAppVersionUpdate.setText("更新");
        tvAppVersionInfo.setText(("多么生活 " + getHeightVersionNumber(appCurrentVersion, appLastVersion)));
//        是否强制更新的版本
        if (isMandatoryUpdate) {
            constraintUpdate = true;
        } else if (isConstraintUpdate(versionLow)) {
            constraintUpdate = true;
        }
        setFinishOnTouchOutside(!constraintUpdate);
    }

    @Override
    protected void loadData() {
    }


    @OnClick(R.id.tv_app_version_update)
    void updateInstall() {
        if (!TextUtils.isEmpty(downLink)) {
            if (constantMethod == null) {
                constantMethod = new ConstantMethod();
            }
            constantMethod.setOnGetPermissionsSuccess(new ConstantMethod.OnGetPermissionsSuccessListener() {
                @Override
                public void getPermissionsSuccess() {
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
                    if (!constraintUpdate) {
                        finish();
                    }
                }
            });
            constantMethod.getPermissions(AppUpdateDialogActivity.this, com.yanzhenjie.permission.Permission.WRITE_EXTERNAL_STORAGE);
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
            int total = 0;
            try {
                total = (int) message.result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            llAppVersionDownTotal.setVisibility(View.VISIBLE);
            tvAppVersionUpdate.setVisibility(View.GONE);
            seekBarAppVersionTotal.setProgress(total);
            tvAppVersionTotalNumber.setText(((total > 100 ? 100 : total) + "%"));
        } else if (message.type.equals("finishUpdateDialog")) {
            finish();
        } else if (message.type.equals("downSuccess")) {
            filePath = (String) message.result;
            if (!TextUtils.isEmpty(filePath)) {
                rel_layout_update_version.setClickable(true);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (constraintUpdate) {
                return true;
            } else {
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 点击安装
     */
    @OnClick(value = R.id.rel_layout_update_version)
    void updateVersion() {
        if (!TextUtils.isEmpty(filePath)) {
            rel_layout_update_version.setClickable(false);
            installApps(this, new File(filePath));
        }
    }

    /**
     * 两版本比较
     *
     * @param targetVersion
     * @param lastVersion
     * @return
     */
    private String getHeightVersionNumber(String targetVersion, String lastVersion) {
        if (TextUtils.isEmpty(targetVersion) || TextUtils.isEmpty(lastVersion)) {
            return getStrings(targetVersion);
        }
        String constraintVersion = getAppendNumber(targetVersion);
        String currentVersion = getAppendNumber(lastVersion);
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
        return Long.parseLong(constraintVersion) > Long.parseLong(currentVersion) ? targetVersion : lastVersion;
    }
}
