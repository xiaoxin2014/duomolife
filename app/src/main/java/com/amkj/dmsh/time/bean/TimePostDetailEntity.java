package com.amkj.dmsh.time.bean;

import com.amkj.dmsh.base.BaseEntity;
import com.amkj.dmsh.dominant.bean.PostCommentEntity;
import com.amkj.dmsh.find.bean.RelatedGoodsBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoxin on 2020/9/30
 * Version:v4.8.0
 */
public class TimePostDetailEntity extends BaseEntity {

    /**
     * sysTime : 2020-09-30 11:42:09
     * showCount : 10
     * totalPage : 0
     * totalResult : 0
     * currentPage : 1
     * list : [{"id":1,"title":"1231313","categoryId":123,"categoryName":"日用小道具","coverPath":"http://image.domolife.cn/platform/20200902/20200902174331533.jpeg","images":"http://image.domolife.cn/platform/20200902/20200902174331533.jpeg,http://image.domolife.cn/platform/20200928/20200928153455742.jpeg,http://image.domolife.cn/platform/20200928/20200928153520812.jpeg","description":"<p>13131<\/p>\n131231323","tagId":null,"tagName":"22","authorUid":35806,"authorNickName":"德艺双馨","authorAvatar":"http://image.domolife.cn/iosRelease/20180830093833.jpg","view":0,"fictitiousView":0,"comment":3,"favor":0,"uv":0,"shareCount":0,"recommend":null,"createUser":null,"createTime":"2020-09-02 17:48:32","productList":[],"commList":[{"nickname":"1767949****","avatar":"https://image.domolife.cn/test/20200807152848b3282.jpg","content":"222"},{"nickname":"1767949****","avatar":"https://image.domolife.cn/test/20200807152848b3282.jpg","content":"111"}],"isFocus":"0","isFavor":"0"}]
     */

    private List<TimePostDetailBean> list;

    public List<TimePostDetailBean> getList() {
        return list;
    }

    public void setList(List<TimePostDetailBean> list) {
        this.list = list;
    }

    public static class TimePostDetailBean {
        /**
         * id : 1
         * title : 1231313
         * categoryId : 123
         * categoryName : 日用小道具
         * coverPath : http://image.domolife.cn/platform/20200902/20200902174331533.jpeg
         * images : http://image.domolife.cn/platform/20200902/20200902174331533.jpeg,http://image.domolife.cn/platform/20200928/20200928153455742.jpeg,http://image.domolife.cn/platform/20200928/20200928153520812.jpeg
         * description : <p>13131</p>
         * 131231323
         * tagId : null
         * tagName : 22
         * authorUid : 35806
         * authorNickName : 德艺双馨
         * authorAvatar : http://image.domolife.cn/iosRelease/20180830093833.jpg
         * view : 0
         * fictitiousView : 0
         * comment : 3
         * favor : 0
         * uv : 0
         * shareCount : 0
         * recommend : null
         * createUser : null
         * createTime : 2020-09-02 17:48:32
         * productList : []
         * commList : [{"nickname":"1767949****","avatar":"https://image.domolife.cn/test/20200807152848b3282.jpg","content":"222"},{"nickname":"1767949****","avatar":"https://image.domolife.cn/test/20200807152848b3282.jpg","content":"111"}]
         * isFocus : 0
         * isFavor : 0
         */

        private int id;
        private String title;
        private int categoryId;
        private String categoryName;
        private String coverPath;
        private String images;
        private String description;
        private String tagId;
        private String tagName;
        private int authorUid;
        private String authorNickName;
        private String authorAvatar;
        private int view;
        private int fictitiousView;
        private int comment;
        private int favor;
        private int uv;
        private int shareCount;
        private String createTime;
        private String isFocus;
        private String isFavor;
        private List<RelatedGoodsBean> productList;
        private List<PostCommentEntity.PostCommentBean> commList;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(int categoryId) {
            this.categoryId = categoryId;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public String getCoverPath() {
            return coverPath;
        }

        public void setCoverPath(String coverPath) {
            this.coverPath = coverPath;
        }

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getTagId() {
            return tagId;
        }

        public void setTagId(String tagId) {
            this.tagId = tagId;
        }

        public String getTagName() {
            return tagName;
        }

        public void setTagName(String tagName) {
            this.tagName = tagName;
        }

        public int getAuthorUid() {
            return authorUid;
        }

        public void setAuthorUid(int authorUid) {
            this.authorUid = authorUid;
        }

        public String getAuthorNickName() {
            return authorNickName;
        }

        public void setAuthorNickName(String authorNickName) {
            this.authorNickName = authorNickName;
        }

        public String getAuthorAvatar() {
            return authorAvatar;
        }

        public void setAuthorAvatar(String authorAvatar) {
            this.authorAvatar = authorAvatar;
        }

        public int getView() {
            return view;
        }

        public void setView(int view) {
            this.view = view;
        }

        public int getFictitiousView() {
            return fictitiousView;
        }

        public void setFictitiousView(int fictitiousView) {
            this.fictitiousView = fictitiousView;
        }

        public int getComment() {
            return comment;
        }

        public void setComment(int comment) {
            this.comment = comment;
        }

        public int getFavor() {
            return favor;
        }

        public void setFavor(int favor) {
            this.favor = favor;
        }

        public int getUv() {
            return uv;
        }

        public void setUv(int uv) {
            this.uv = uv;
        }

        public int getShareCount() {
            return shareCount;
        }

        public void setShareCount(int shareCount) {
            this.shareCount = shareCount;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getIsFocus() {
            return isFocus;
        }

        public void setIsFocus(String isFocus) {
            this.isFocus = isFocus;
        }

        public String getIsFavor() {
            return isFavor;
        }

        public void setIsFavor(String isFavor) {
            this.isFavor = isFavor;
        }

        public List<RelatedGoodsBean> getProductList() {
            return productList;
        }

        public void setProductList(List<RelatedGoodsBean> productList) {
            this.productList = productList;
        }

        public List<PostCommentEntity.PostCommentBean> getCommList() {
            return commList == null ? new ArrayList<>() : commList;
        }

        public void setCommList(List<PostCommentEntity.PostCommentBean> commList) {
            this.commList = commList;
        }
    }
}
