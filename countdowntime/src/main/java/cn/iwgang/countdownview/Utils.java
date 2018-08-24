package cn.iwgang.countdownview;

import android.content.Context;

/**
 * Utils
 * Created by iWgang on 16/6/19.
 * https://github.com/iwgang/CountdownView
 */
final class Utils {

    public static int dp2px(Context context, float dpValue) {
        if (dpValue <= 0) return 0;
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static String formatNum(int time) {
        return time < 10 ? "0" + time : String.valueOf(time);
    }

    public static String formatMillisecond(int millisecond) {
        String retMillisecondStr;

        if (millisecond > 99) {
            retMillisecondStr = String.valueOf(millisecond / 10);
        } else if (millisecond <= 9) {
            retMillisecondStr = "0" + millisecond;
        } else {
            retMillisecondStr = String.valueOf(millisecond);
        }

        return retMillisecondStr;
    }

    //  仅修改此方法
    public static float autoPx(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().widthPixels / 750f;
        if (px <= 0) return scale * 28;
        return px * scale;
    }
}
