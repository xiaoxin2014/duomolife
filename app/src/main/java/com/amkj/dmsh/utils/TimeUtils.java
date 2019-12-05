package com.amkj.dmsh.utils;

import android.text.TextUtils;

import com.amkj.dmsh.base.BaseTimeEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by xiaoxin on 2019/11/27
 * Version:v4.3.7
 * ClassDescription :时间相关工具类
 */
public class TimeUtils {
    /**
     * @param time           时间参数
     * @param timeSwitchover 切换时间格式
     * @return
     */
    public static String getDateFormat(String time, String timeSwitchover) {
        if (!TextUtils.isEmpty(time)) {
            try {
                SimpleDateFormat timeReceiveFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                Date date = timeReceiveFormat.parse(time);
                SimpleDateFormat timeFormat = new SimpleDateFormat(!TextUtils.isEmpty(timeSwitchover)
                        ? timeSwitchover : "yyyy-MM-dd", Locale.CHINA);
                return timeFormat.format(date);
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        } else {
            return "";
        }
    }

    /**
     * 时间转换格式 默认当前时间跟转换时间 为年-月-日
     *
     * @param time
     * @param timeSwitchover
     * @return
     */
    public static String getDateFormat(String time, String currentSwitchover, String timeSwitchover) {
        if (!TextUtils.isEmpty(time)) {
            try {
                SimpleDateFormat timeReceiveFormat = new SimpleDateFormat(TextUtils.isEmpty(currentSwitchover) ? "yyyy-MM-dd HH:mm:ss" : currentSwitchover, Locale.CHINA);
                Date date = timeReceiveFormat.parse(time);
                SimpleDateFormat timeFormat = new SimpleDateFormat(!TextUtils.isEmpty(timeSwitchover)
                        ? timeSwitchover : "yyyy-MM-dd", Locale.CHINA);
                return timeFormat.format(date);
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        } else {
            return "";
        }
    }

    /**
     * 时间获取毫秒
     *
     * @param time
     * @return
     */
    public static long getDateMilliSecond(String time) {
        if (!TextUtils.isEmpty(time)) {
            try {
                SimpleDateFormat timeReceiveFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                Date date = timeReceiveFormat.parse(time);
                return date.getTime();
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        } else {
            return 0;
        }
    }

    /**
     * 获取毫秒 空值为当前默认时间
     *
     * @param time
     * @return
     */
    public static long getDateMilliSecondSystemTime(String time) {
        if (!TextUtils.isEmpty(time)) {
            try {
                SimpleDateFormat timeReceiveFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                Date date = timeReceiveFormat.parse(time);
                return date.getTime();
            } catch (Exception e) {
                e.printStackTrace();
                return new Date().getTime();
            }
        } else {
            return new Date().getTime();
        }
    }

    //时分秒获取 时
    private static int getDataFormatHour(String time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
        try {
            Date date = simpleDateFormat.parse(time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar.get(Calendar.HOUR_OF_DAY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    //获取当前星期几
    public static int getDataFormatWeek(String time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            Calendar calendar = Calendar.getInstance();
            if (TextUtils.isEmpty(time)) {
                Date date = calendar.getTime();
                time = simpleDateFormat.format(date);
            }
            Date date = simpleDateFormat.parse(time);
            calendar.setTime(date);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            calendar.set(Calendar.DAY_OF_MONTH, day);  //指定日
            int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            switch (week) {
                case 0:
                    week = 7;
                    break;
            }
            return week;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 时间是否已结束
     *
     * @param t1 当前时间 t2 结束时间 返回true表示活动结束
     * @param t1 当前时间 t2 开始时间 返回true表示活动开始
     */
    public static boolean isEndOrStartTime(String t1, String t2) {
        if (!TextUtils.isEmpty(t1) && !TextUtils.isEmpty(t2)) {
            try {
                SimpleDateFormat timeReceiveFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                Date d1 = timeReceiveFormat.parse(t1);
                Date d2 = timeReceiveFormat.parse(t2);
                return d1.getTime() >= d2.getTime();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    //获取两个时间之间的时间差(单位毫秒)
    public static long getTimeDifference(String t1, String t2) {
        long abs = 0;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        try {
            long time1 = formatter.parse(t1).getTime();
            long time2 = formatter.parse(t2).getTime();
            abs = Math.abs(time1 - time2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return abs;
    }

    /**
     * 毫秒转换成天 时分秒
     *
     * @param showZeroDay 是否显示0天
     */
    public static String getCoutDownTime(long coutTime, boolean showZeroDay) {
        try {
            int day = (int) (coutTime / (1000 * 60 * 60 * 24));
            int hour = (int) ((coutTime % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
            int minute = (int) ((coutTime % (1000 * 60 * 60)) / (1000 * 60));
            int second = (int) ((coutTime % (1000 * 60)) / 1000);
            return (!showZeroDay && day == 0 ? "" : day + "天") + (hour < 10 ? "0" + hour : hour) + ":" + (minute < 10 ? "0" + minute : minute) + ":" + (second < 10 ? "0" + second : second);
        } catch (Exception e) {
            return "";
        }
    }

    //获取当前时间 "yyyy-MM-dd HH:mm:ss"
    public static String getCurrentTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return simpleDateFormat.format(new Date());
    }

    public static String getCurrentTime(BaseTimeEntity baseTimeEntity) {
        if (baseTimeEntity != null && !TextUtils.isEmpty(baseTimeEntity.getCurrentTime())) {
            return baseTimeEntity.getCurrentTime();
        } else {
            return getCurrentTime();
        }
    }

    /**
     * 时间是否已结束
     *
     * @param t1        开始时间
     * @param t2        结束时间
     * @param addSecond 时间展示
     * @return
     */
    public static boolean isEndOrStartTimeAddSeconds(String t1, String t2, long addSecond) {
        if (!TextUtils.isEmpty(t1) && !TextUtils.isEmpty(t2)) {
            try {
                SimpleDateFormat timeReceiveFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                Date d1 = timeReceiveFormat.parse(t1);
                Date d2 = timeReceiveFormat.parse(t2);
                return (d1.getTime() + addSecond * 1000) >= d2.getTime();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * date 时间对比
     *
     * @param t1
     * @param t2
     * @param addSecond
     * @return
     */
    public static boolean isEndOrStartTimeAddSeconds(Date t1, Date t2, long addSecond) {
        if (t1 != null && t2 != null) {
            try {
                return (t1.getTime() + addSecond * 1000) >= t2.getTime();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }


    /**
     * 对比两个时间是否是同一天
     *
     * @param t1
     * @param t2
     * @return
     */
    public static boolean isSameTimeDay(String t1, String t2) {
        return isSameTimeDay("yyyy-MM-dd HH:mm:ss", t1, t2);
    }

    /**
     * @param formatType 数据格式类型
     * @param t1
     * @param t2
     * @return
     */
    public static boolean isSameTimeDay(String formatType, String t1, String t2) {
        if (!TextUtils.isEmpty(t1) && !TextUtils.isEmpty(t2)) {
            try {
                SimpleDateFormat timeFormat = new SimpleDateFormat(TextUtils.isEmpty(formatType) ? "yyyy-MM-dd HH:mm:ss" : formatType, Locale.CHINA);
                Date d1 = timeFormat.parse(t1);
                Date d2 = timeFormat.parse(t2);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(d1);
                int day1 = calendar.get(Calendar.DAY_OF_MONTH);
                calendar.setTime(d2);
                int day2 = calendar.get(Calendar.DAY_OF_MONTH);
                return day1 == day2;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * @param t1          起始时间
     * @param t2          当前时间
     * @param intervalDay 间隔天数
     * @return
     */
    public static boolean isTimeDayEligibility(String t1, String t2, int intervalDay) {
        if (!TextUtils.isEmpty(t1) && !TextUtils.isEmpty(t2)) {
            try {
                SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
                Date d1 = timeFormat.parse(t1);
                long milliseconds = d1.getTime() + intervalDay * 24 * 60 * 60 * 1000;
                d1.setTime(milliseconds);
                Date d2 = timeFormat.parse(t2);
                return d1.getTime() <= d2.getTime();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }
}
