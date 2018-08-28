package com.amkj.dmsh.qyservice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.qiyukf.nimlib.sdk.NimIntent;

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
            QyServiceUtils.getQyInstance().openQyServiceChat(this);
        }
    }
}
