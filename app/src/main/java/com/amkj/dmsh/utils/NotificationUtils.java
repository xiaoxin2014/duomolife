package com.amkj.dmsh.utils;

import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;

import java.lang.reflect.Method;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/8/21
 * version 3.1.5
 * class description:通知栏工具
 */
public class NotificationUtils {
    /**
     * 8.0以上获取通知栏状态
     *
     * @param context
     * @return
     */
    public static boolean isEnableV26(Context context) {
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;
        try {
            NotificationManager notificationManager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);
            Method sServiceField = notificationManager.getClass().getDeclaredMethod("getService");
            sServiceField.setAccessible(true);
            Object sService = sServiceField.invoke(notificationManager);

            Method method = sService.getClass().getDeclaredMethod("areNotificationsEnabledForPackage"
                    , String.class, Integer.TYPE);
            method.setAccessible(true);
            return (boolean) method.invoke(sService, pkg, uid);
        } catch (Exception e) {
            return true;
        }
    }
}
