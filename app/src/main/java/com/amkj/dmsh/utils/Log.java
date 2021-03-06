package com.amkj.dmsh.utils;

import static com.amkj.dmsh.constant.ConstantVariable.isDebugTag;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/5/17
 * class description:log详情
 */

public final class Log {

    /**
     * isPrint: print switch, true will print. false not print
     */
    private static String defaultTag = "Log";

    private Log() {
    }

    public static void setTag(String tag) {
        defaultTag = tag;
    }

    public static int i(Object o) {
        return isDebugTag && o != null ? android.util.Log.i(defaultTag, o.toString()) : -1;
    }

    public static int i(String m) {
        return isDebugTag && m != null ? android.util.Log.i(defaultTag, m) : -1;
    }

    /**
     * ******************** Log **************************
     */
    public static int v(String tag, String msg) {
        return isDebugTag && msg != null ? android.util.Log.v(tag, msg) : -1;
    }

    public static int d(String tag, String msg) {
        return isDebugTag && msg != null ? android.util.Log.d(tag, msg) : -1;
    }

    public static int i(String tag, String msg) {
        return isDebugTag && msg != null ? android.util.Log.i(tag, msg) : -1;
    }

    public static int w(String tag, String msg) {
        return isDebugTag && msg != null ? android.util.Log.w(tag, msg) : -1;
    }

    public static int e(String tag, String msg) {
        return isDebugTag && msg != null ? android.util.Log.e(tag, msg) : -1;
    }

    /**
     * ******************** Log with object list **************************
     */
    public static int v(String tag, Object... msg) {
        return isDebugTag ? android.util.Log.v(tag, getLogMessage(msg)) : -1;
    }

    public static int d(String tag, Object... msg) {
        return isDebugTag ? android.util.Log.d(tag, getLogMessage(msg)) : -1;
    }

    public static int i(String tag, Object... msg) {
        return isDebugTag ? android.util.Log.i(tag, getLogMessage(msg)) : -1;
    }

    public static int w(String tag, Object... msg) {
        return isDebugTag ? android.util.Log.w(tag, getLogMessage(msg)) : -1;
    }

    public static int e(String tag, Object... msg) {
        return isDebugTag ? android.util.Log.e(tag, getLogMessage(msg)) : -1;
    }

    private static String getLogMessage(Object... msg) {
        if (msg != null && msg.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (Object s : msg) {
                if (s != null) {
                    sb.append(s.toString());
                }
            }
            return sb.toString();
        }
        return "";
    }

    /**
     * ******************** Log with Throwable **************************
     */
    public static int v(Object tag, String msg, Throwable tr) {
        return isDebugTag && msg != null ? android.util.Log.v(tag.getClass().getSimpleName(), msg, tr) : -1;
    }

    public static int d(Object tag, String msg, Throwable tr) {
        return isDebugTag && msg != null ? android.util.Log.d(tag.getClass().getSimpleName(), msg, tr) : -1;
    }

    public static int i(Object tag, String msg, Throwable tr) {
        return isDebugTag && msg != null ? android.util.Log.i(tag.getClass().getSimpleName(), msg, tr) : -1;
    }

    public static int w(Object tag, String msg, Throwable tr) {
        return isDebugTag && msg != null ? android.util.Log.w(tag.getClass().getSimpleName(), msg, tr) : -1;
    }

    public static int e(Object tag, String msg, Throwable tr) {
        return isDebugTag && msg != null ? android.util.Log.e(tag.getClass().getSimpleName(), msg, tr) : -1;
    }

    /**
     * ******************** TAG use Object Tag **************************
     */
    public static int v(Object tag, String msg) {
        return isDebugTag ? android.util.Log.v(tag.getClass().getSimpleName(), msg) : -1;
    }

    public static int d(Object tag, String msg) {
        return isDebugTag ? android.util.Log.d(tag.getClass().getSimpleName(), msg) : -1;
    }

    public static int i(Object tag, String msg) {
        return isDebugTag ? android.util.Log.i(tag.getClass().getSimpleName(), msg) : -1;
    }

    public static int w(Object tag, String msg) {
        return isDebugTag ? android.util.Log.w(tag.getClass().getSimpleName(), msg) : -1;
    }

    public static int e(Object tag, String msg) {
        return isDebugTag ? android.util.Log.e(tag.getClass().getSimpleName(), msg) : -1;
    }

    public static int longLogW(String tag, String msg) {
        if (msg.length() > 4000) {
            for (int i = 0; i < msg.length(); i += 4000) {
                if (i + 4000 < msg.length())
                    android.util.Log.w(tag + i, msg.substring(i, i + 4000));
                else
                    android.util.Log.w(tag + i, msg.substring(i, msg.length()));
            }
        } else {
            android.util.Log.w(tag, msg);
        }
        return isDebugTag ? android.util.Log.e(tag.getClass().getSimpleName(), msg) : -1;
    }


}
