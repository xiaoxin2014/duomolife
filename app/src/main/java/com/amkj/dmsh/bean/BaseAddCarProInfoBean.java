package com.amkj.dmsh.bean;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/10/18
 * class description:请输入类描述
 */

public class BaseAddCarProInfoBean {
    private String proName;
    private String proPic;
    private int productId;
    private String activityCode;
    private boolean isShowSingle = true;//默认仅展示加购按钮

    public boolean isShowSingle() {
        return isShowSingle;
    }

    public void setShowSingle(boolean showSingle) {
        isShowSingle = showSingle;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getProPic() {
        return proPic;
    }

    public void setProPic(String proPic) {
        this.proPic = proPic;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }
}
