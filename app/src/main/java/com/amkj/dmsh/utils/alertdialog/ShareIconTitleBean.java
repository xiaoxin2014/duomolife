package com.amkj.dmsh.utils.alertdialog;

import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/9/27
 * version 3.1.6
 * class description:分享图标跟标题
 */
public class ShareIconTitleBean {
    private int shareIconResId;
    private String shareTitle;
    private SHARE_MEDIA sharePlatformType;

    public int getShareIconResId() {
        return shareIconResId;
    }

    public void setShareIconResId(int shareIconResId) {
        this.shareIconResId = shareIconResId;
    }

    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public SHARE_MEDIA getSharePlatformType() {
        return sharePlatformType;
    }

    public void setSharePlatformType(SHARE_MEDIA sharePlatformType) {
        this.sharePlatformType = sharePlatformType;
    }
}
