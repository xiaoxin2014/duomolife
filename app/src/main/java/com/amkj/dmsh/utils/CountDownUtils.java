package com.amkj.dmsh.utils;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by xiaoxin on 2019/9/3
 * Version:v4.2.1
 * ClassDescription :倒计时相关工具类
 */
public class CountDownUtils {
    //判断活动是否开始
    public static boolean isTimeStart(String startTime, String curentTime) {
        try {
            //格式化开始时间
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            Date dateStart = formatter.parse(startTime);
            Date dateCurrent = !TextUtils.isEmpty(curentTime) ? formatter.parse(curentTime) : new Date();
            return dateCurrent.getTime() >= dateStart.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //判断活动是否结束
    public static boolean isTimeEnd(String endTime, String curentTime) {
        try {
            //格式化开始时间
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            Date dateEnd = formatter.parse(endTime);
            Date dateCurrent = !TextUtils.isEmpty(curentTime) ? formatter.parse(curentTime) : new Date();
            return dateCurrent.getTime() > dateEnd.getTime();//活动已结束
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 毫秒转换成天 时分秒
     * @param showZeroDay 是否显示0天
     */
    public static String getCoutDownTime(long coutTime, boolean showZeroDay) {
        try {
            int day = (int) (coutTime / (1000 * 60 * 60 * 24));
            int hour = (int) ((coutTime % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
            int minute = (int) ((coutTime % (1000 * 60 * 60)) / (1000 * 60));
            int second = (int) ((coutTime % (1000 * 60)) / 1000);
            return (!showZeroDay && day == 0 ? "" : day + "天") + hour + ":" + minute + ":" + second;
        } catch (Exception e) {
            return "";
        }
    }
}
