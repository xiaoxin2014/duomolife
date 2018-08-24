package com.amkj.dmsh.utils.restartapputils;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/2/6
 * class description:请输入类描述
 */

import android.content.Context;
import android.content.Intent;

/**
 * 此工具类用来重启APP，只是单纯的重启，不做任何处理。
 * Created by 13itch on 2016/8/5.
 */
public class RestartAPPTool {
    /**
     * 重启整个APP
     *
     * @param context
     * @param Delayed 延迟多少毫秒
     */
    public static void restartAPP(Context context, long Delayed) {

        /**开启一个新的服务，用来重启本APP*/
        Intent intent1 = new Intent(context, RestartAppService.class);
        intent1.putExtra("PackageName", context.getPackageName());
        intent1.putExtra("Delayed", Delayed);
        context.startService(intent1);
    }

    /***重启整个APP*/
    public static void restartAPP(Context context) {
        restartAPP(context, 0);
    }
}
