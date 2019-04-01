package com.amkj.dmsh.utils.webformatdata;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/12/20
 * version 3.2.0
 * class description:分享携带数据类型
 */
public class ShareDataBean {
    private String imgUrl;
    private String title;
    private String description;
    private String urlLink;
//    回调传的Id--> 奖励 没有该值 分享不会回调获取奖励
    private int backId;
    private int id;  //对应的内容ID，如-分享文章类型，则传文章ID，对于一些固定的专区，如每周优选，传0即可

    /**
     *
     * @param imgUrl
     * @param title
     * @param description
     * @param urlLink
     */
    public ShareDataBean(String imgUrl, String title, String description, String urlLink) {
        this.imgUrl = imgUrl;
        this.title = title;
        this.description = description;
        this.urlLink = urlLink;
    }

    /**
     *
     * @param imgUrl
     * @param title
     * @param description
     * @param urlLink
     * @param backId
     */
    public ShareDataBean(String imgUrl, String title, String description, String urlLink, int backId) {
        this.imgUrl = imgUrl;
        this.title = title;
        this.description = description;
        this.urlLink = urlLink;
        this.backId = backId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrlLink() {
        return urlLink;
    }

    public void setUrlLink(String urlLink) {
        this.urlLink = urlLink;
    }

    public int getBackId() {
        return backId;
    }

    public void setBackId(int backId) {
        this.backId = backId;
    }
}
