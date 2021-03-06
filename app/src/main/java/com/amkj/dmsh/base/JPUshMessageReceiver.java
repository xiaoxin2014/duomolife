package com.amkj.dmsh.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.amkj.dmsh.MainActivity;
import com.amkj.dmsh.qyservice.QyServiceUtils;
import com.amkj.dmsh.utils.Log;
import com.tencent.bugly.beta.tinker.TinkerManager;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.installApps;
import static com.amkj.dmsh.constant.ConstantMethod.setSkipPath;
import static com.amkj.dmsh.constant.ConstantVariable.BROADCAST_NOTIFY;
import static com.amkj.dmsh.constant.ConstantVariable.REFRESH_MESSAGE_TOTAL;
import static com.amkj.dmsh.dao.AddClickDao.clickTotalPush;


/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class JPUshMessageReceiver extends BroadcastReceiver {

    private static final String TAG = "JPush";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getExtras() == null) return;
        Bundle bundle = intent.getExtras();
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
//            接收到的通知消息
            try {
                Map map = (Map) JSON.parse(bundle.getString(JPushInterface.EXTRA_EXTRA));
                String pushType = map.get("pushType") + "";
//                if (0 < Integer.parseInt(pushType) && Integer.parseInt(pushType) <= 35) {//暂时先注释
                //通知消息角标更新
                EventBus.getDefault().post(new EventMessage(REFRESH_MESSAGE_TOTAL, REFRESH_MESSAGE_TOTAL));
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            try {
                JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
//                消息类型
                String pushType = null;
                try {
                    pushType = json.getString("pushType");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String mContent = "";
                try {
                    mContent = json.getString("m_content");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String objId = "";
                String pushId = "";
                try {
                    objId = json.getString("objId");
                    pushId = json.getString("pushId");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (!TextUtils.isEmpty(pushType)) {
                    clickTotalPush(pushType, objId);
                }
                if (!TextUtils.isEmpty(pushType) && "999".equals(pushType)) { //跳转客服
                    QyServiceUtils qyServiceUtils = QyServiceUtils.getQyInstance();
                    qyServiceUtils.openQyServiceChat(context, "推送-客服通知-" + getStrings(mContent), "");
                } else {
                    newTaskActivity(context);
                    setSkipPath(context, json.getString("androidLink"), false);
                }
            } catch (JSONException e) {
                newTaskActivity(context);
                e.printStackTrace();
            }
            //打开自定义的Activity

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
//            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
//        	Log.w(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
        } else if (BROADCAST_NOTIFY.equals(intent.getAction())) {
            String filePath = intent.getStringExtra("filePath");
            if (!TextUtils.isEmpty(filePath) && filePath.contains(".apk")) {
                installApps(context, new File(filePath));
            }
        }
    }

    /**
     * 启动主页面
     *
     * @param context
     */
    private void newTaskActivity(Context context) {
        TinkerBaseApplicationLike tinkerBaseApplicationLike = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
        if (!tinkerBaseApplicationLike.existActivity(MainActivity.class.getName())) {
            Intent data = new Intent(context, MainActivity.class);
            // 说明系统中不存在这个activity
            data.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(data);
        }
    }
}
