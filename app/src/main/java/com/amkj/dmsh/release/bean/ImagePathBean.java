package com.amkj.dmsh.release.bean;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/10/29
 * version 3.1.8
 * class description:图片展示
 */
public class ImagePathBean {
    private String path;
    private boolean showDelIcon;

    public ImagePathBean(String path, boolean showDelIcon) {
        this.path = path;
        this.showDelIcon = showDelIcon;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isShowDelIcon() {
        return showDelIcon;
    }

    public void setShowDelIcon(boolean showDelIcon) {
        this.showDelIcon = showDelIcon;
    }
}
