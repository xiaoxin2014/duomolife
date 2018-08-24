package com.luck.picture.lib.tools;

import android.content.Context;
import android.widget.Toast;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/1/18
 * version 1.0
 * class description:公用方法
 */

public class CommunalMethod {

    private static Toast toast;

    /**
     * 解决Toast重复弹出 长时间不消失的问题
     *
     * @param context
     * @param message
     */
    public static void showToast(Context context, String message) {
        if (toast == null) {
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        } else {
            toast.setText(message);
        }
        //设置新的消息提示
        toast.show();
    }
}
