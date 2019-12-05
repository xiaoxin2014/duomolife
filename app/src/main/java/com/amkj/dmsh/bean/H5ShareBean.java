package com.amkj.dmsh.bean;

import android.text.TextUtils;

import static com.amkj.dmsh.constant.ConstantMethod.getStringChangeIntegers;

/**
 * Created by xiaoxin on 2019/9/25
 * Version:v4.2.2
 * ClassDescription :h5页面嵌入的分享交互
 */
public class H5ShareBean {

    /**
     * title : 每天1小时，跟含胸驼背say byebye！
     * imageUrl : http://image.domolife.cn/platform/20190807/20190807193508579.jpeg
     * content : 改善身姿从宝宝抓起！
     * url : https://www.domolife.cn/m/template/goods/study_detail.html?id=22027
     * routineUrl : pages/article/article?id=22027
     * shareType : 2
     * objName : 每天1小时，跟含胸驼背say byebye！
     * objId : 22027
     */

    private String title;
    private String imageUrl;
    private String content;
    private String description;
    private String url;
    private String routineUrl;
    private int shareType = -1;
    private String objName;
    private String objId;
    private String platform;
    private String turnId;

    public H5ShareBean(String title, String imageUrl, String content, String url, String routineUrl, String objId, int shareType, String platform) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.content = content;
        this.url = url;
        this.routineUrl = routineUrl;
        this.shareType = shareType;
        this.objId = objId;
        this.platform = platform;
    }

    public H5ShareBean(String title, String imageUrl, String content, String url, String routineUrl, String objId, String platform) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.content = content;
        this.url = url;
        this.routineUrl = routineUrl;
        this.objId = objId;
        this.platform = platform;
    }


    public String getTurnId() {
        return turnId;
    }

    public void setTurnId(String turnId) {
        this.turnId = turnId;
    }

    public String getDescription() {
        return TextUtils.isEmpty(description) ? content : description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return TextUtils.isEmpty(title) ? objName : title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRoutineUrl() {
        return routineUrl;
    }

    public void setRoutineUrl(String routineUrl) {
        this.routineUrl = routineUrl;
    }

    public int getShareType() {
        return shareType;
    }

    public void setShareType(int shareType) {
        this.shareType = shareType;
    }

    public int getObjId() {
        return getStringChangeIntegers(objId);
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}
