package com.amkj.dmsh.qyservice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.amkj.dmsh.MainActivity;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.qiyukf.nimlib.sdk.NimIntent;
import com.tencent.bugly.beta.tinker.TinkerManager;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/8/27
 * version 3.1.6
 * class description:七鱼客服通知
 */
public class QyServiceNotifyReceiver extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent.hasExtra(NimIntent.EXTRA_NOTIFY_CONTENT)) {
            // 打开客服窗口
            newTaskActivity(QyServiceNotifyReceiver.this);

            QyServiceUtils.getQyInstance().openQyServiceChat(this);
        }
    }
    /**
     * 启动主页面
     *
     * @param context
     */
    private void newTaskActivity(Context context) {
        TinkerBaseApplicationLike tinkerBaseApplicationLike = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
        if(!tinkerBaseApplicationLike.existActivity(MainActivity.class.getName())){
            Intent data = new Intent(context, MainActivity.class);
            // 说明系统中不存在这个activity
            data.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(data);
        }
    }
}
