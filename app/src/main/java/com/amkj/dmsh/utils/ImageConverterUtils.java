package com.amkj.dmsh.utils;

import android.text.TextUtils;

import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.tencent.bugly.beta.tinker.TinkerManager;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/1/7
 * class description:图片格式转换
 */

public class ImageConverterUtils {
    private static final String formatImg = "?x-oss-process=image/format,jpg";

    public static String getFormatImg(String url) {
        if (!TextUtils.isEmpty(url)) {
            TinkerBaseApplicationLike applicationLike = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
            String ossDataUrl = applicationLike.getOSSDataUrl();
            if (url.contains(ossDataUrl) && (url.contains(".jpg") || url.contains(".JPG"))) {
                return url + formatImg;
            } else {
                return url;
            }
        }
        return "";
    }
}
