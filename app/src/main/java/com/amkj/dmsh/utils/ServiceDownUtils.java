package com.amkj.dmsh.utils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.utils.inteface.MyProgressCallBack;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import static com.amkj.dmsh.constant.ConstantMethod.installApps;
import static com.amkj.dmsh.constant.ConstantMethod.isHeightVersion;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/4/24
 * version 3.1.2
 * class description:服务下载文件
 */
public class ServiceDownUtils extends Service {
    public static final String INSTALL_APP_PROGRESS = "install_app_progress";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent!=null){
            String downUrl = null;
            String downFilePath = null;
            boolean isInstallApp = false;
            boolean isShowProgress = false;
            try {
                downUrl = intent.getStringExtra("downUrl");
                downFilePath = intent.getStringExtra("downFilePath");
                isInstallApp = intent.getBooleanExtra("isInstallApp", false);
                isShowProgress = intent.getBooleanExtra("isShowProgress", false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!TextUtils.isEmpty(downUrl) && !TextUtils.isEmpty(downFilePath)) {
                downAppFile(downUrl, downFilePath, isInstallApp, isShowProgress);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public void downAppFile(String url, String downUrl, boolean isInstallApp, boolean isShowProgress) {
        String filePath = downUrl.substring(0, downUrl.lastIndexOf("/"));
        File pathFile = new File(filePath);
        if (!pathFile.exists()) {
            pathFile.mkdir();
        }
        XUtil.DownLoadFile(url, downUrl, new MyProgressCallBack<File>() {
            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                if (isShowProgress) {
                    EventBus.getDefault().post(new EventMessage("appVersionProgress", current*1f / total * 100 + 1));
                }
            }

            @Override
            public void onSuccess(File file) {
                if (isInstallApp && file.getAbsolutePath().contains(".apk")) {
                    openFile(file, ServiceDownUtils.this);
                    stopSelf();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                showToast(ServiceDownUtils.this, "下载失败");
                stopSelf();
            }
        });
    }

    //打开当前APK程序代码
    public void openFile(File file, Context context) {
        if (isHeightVersion(context, file.getAbsolutePath())) {
            installApps(context,file);
        }else{
            EventBus.getDefault().post(new EventMessage("finishUpdateDialog","updateVersion"));
            showToast(context, R.string.app_version_tint);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
    }
}
