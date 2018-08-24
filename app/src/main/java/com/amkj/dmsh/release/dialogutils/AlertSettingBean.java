package com.amkj.dmsh.release.dialogutils;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/8/8
 * class description:标准数据
 */

public class AlertSettingBean {
    AlertView.Style style;
    AlertData alertData;
    AlertInitView alertInitView;

    public AlertView.Style getStyle() {
        return style;
    }

    public void setStyle(AlertView.Style style) {
        this.style = style;
    }

    public AlertData getAlertData() {
        return alertData;
    }

    public void setAlertData(AlertData alertData) {
        this.alertData = alertData;
    }

    public AlertInitView getAlertInitView() {
        return alertInitView;
    }

    public void setAlertInitView(AlertInitView alertInitView) {
        this.alertInitView = alertInitView;
    }

    public static class AlertData {
        String title;
        String msg;
        String[] normalData;
        String[] otherData;
        //        展示数据
        boolean firstNormal;
        String cancelStr;
        String determineStr;
        //        是否第一个按钮展示确定
        boolean firstDet;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String[] getNormalData() {
            return normalData;
        }

        public void setNormalData(String[] normalData) {
            this.normalData = normalData;
        }

        public String[] getOtherData() {
            return otherData;
        }

        public void setOtherData(String[] otherData) {
            this.otherData = otherData;
        }

        public boolean isFirstNormal() {
            return firstNormal;
        }

        public void setFirstNormal(boolean firstNormal) {
            this.firstNormal = firstNormal;
        }

        public String getCancelStr() {
            return cancelStr;
        }

        public void setCancelStr(String cancelStr) {
            this.cancelStr = cancelStr;
        }

        public String getDetermineStr() {
            return determineStr;
        }

        public void setDetermineStr(String determineStr) {
            this.determineStr = determineStr;
        }

        public boolean isFirstDet() {
            return firstDet;
        }

        public void setFirstDet(boolean firstDet) {
            this.firstDet = firstDet;
        }
    }

    public static class AlertInitView {
        String cancelColorValue;
        int cancelSize;
        String detColorValue;
        int detSize;
        String norColorValue;
        /**
         * 字体大小已配置px,已处理，不需要额外处理
         */
        int norSize;
        String othColorValue;
        int othSize;
        String titleColorValue;
        int titleSize;
        String msgColorValue;
        int msgSize;

        public String getCancelColorValue() {
            return cancelColorValue;
        }

        public void setCancelColorValue(String cancelColorValue) {
            this.cancelColorValue = cancelColorValue;
        }

        public int getCancelSize() {
            return cancelSize;
        }

        public void setCancelSize(int cancelSize) {
            this.cancelSize = cancelSize;
        }

        public String getDetColorValue() {
            return detColorValue;
        }

        public void setDetColorValue(String detColorValue) {
            this.detColorValue = detColorValue;
        }

        public int getDetSize() {
            return detSize;
        }

        public void setDetSize(int detSize) {
            this.detSize = detSize;
        }

        public String getNorColorValue() {
            return norColorValue;
        }

        public void setNorColorValue(String norColorValue) {
            this.norColorValue = norColorValue;
        }

        public int getNorSize() {
            return norSize;
        }

        public void setNorSize(int norSize) {
            this.norSize = norSize;
        }

        public String getOthColorValue() {
            return othColorValue;
        }

        public void setOthColorValue(String othColorValue) {
            this.othColorValue = othColorValue;
        }

        public int getOthSize() {
            return othSize;
        }

        public void setOthSize(int othSize) {
            this.othSize = othSize;
        }

        public String getTitleColorValue() {
            return titleColorValue;
        }

        public void setTitleColorValue(String titleColorValue) {
            this.titleColorValue = titleColorValue;
        }

        public int getTitleSize() {
            return titleSize;
        }

        public void setTitleSize(int titleSize) {
            this.titleSize = titleSize;
        }

        public String getMsgColorValue() {
            return msgColorValue;
        }

        public void setMsgColorValue(String msgColorValue) {
            this.msgColorValue = msgColorValue;
        }

        public int getMsgSize() {
            return msgSize;
        }

        public void setMsgSize(int msgSize) {
            this.msgSize = msgSize;
        }
    }
}
