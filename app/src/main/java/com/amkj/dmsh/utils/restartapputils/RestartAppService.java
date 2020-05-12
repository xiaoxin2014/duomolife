package com.amkj.dmsh.utils.restartapputils;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.NonNull;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/2/6
 * class description:请输入类描述
 */

public class RestartAppService extends Service {
    /**
     * 关闭应用后多久重新启动
     */
//    private static long stopDelayed = 2000;
//    private Handler handler;
    private String PackageName;

//    public RestartAppService() {
//        handler = new Handler();
//    }

    @Override
    public int onStartCommand(@NonNull final Intent intent, int flags, int startId) {
//        stopDelayed = intent.getLongExtra("Delayed", 2000);
        PackageName = intent.getStringExtra("PackageName");
        Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage(PackageName);
        startActivity(LaunchIntent);
        RestartAppService.this.stopSelf();
        /**杀死整个进程**/
        android.os.Process.killProcess(android.os.Process.myPid());
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }, stopDelayed);
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
