package com.amkj.dmsh.find.bean;

import com.amkj.dmsh.release.bean.RelevanceProEntity.RelevanceProBean;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/11/4
 * class description:关联标签信息
 */

public class RelevanceTagInfoEntity {

    /**
     * result : {"docCount":71,"tag":{"total":0,"img_url":"","tag_name":"防嗮","is_recommend":1,"tagProperty":1,"id":1,"status":1},"topTagList":[{"total":0,"tag_name":"防嗮","is_recommend":1,"tagProperty":1,"id":1,"status":1,"app_skip_id":1,"img_url":"http://image.domolife.cn/platform/NRx6WTeDhx1507967911652.png","tagType":0},{"total":0,"tag_name":"美白","is_recommend":1,"tagProperty":1,"id":2,"status":1},{"total":0,"tag_name":"时尚","is_recommend":1,"tagProperty":1,"id":3,"status":1},{"total":0,"tag_name":"复古","is_recommend":1,"tagProperty":1,"id":4,"status":1},{"total":0,"app_skip_id":1,"img_url":"http://image.domolife.cn/platform/NRx6WTeDhx1507967911652.png","tag_name":"护肤","is_recommend":1,"tagType":0,"tagProperty":1,"id":218,"status":1}],"productList":[{"productId":4287,"price":"179.00","pictureUrl":"http://img.domolife.cn/platform/ijr3MeYjEx.jpg","id":118,"title":"倍轻松ISEE100青少年眼部按摩仪"},{"productId":4382,"price":"69.00","pictureUrl":"http://img.domolife.cn/platform/EmeXchTmY2.jpg","id":344,"title":"台湾Artiart花型桌面收纳"},{"productId":4318,"price":"62.00","pictureUrl":"http://img.domolife.cn/platform/85KRniwCQD.jpg","id":205,"title":"日本FaSoLa暖桌垫冬季加热电脑垫"}]}
     * msg : 请求成功
     * code : 01
     */

    @SerializedName("result")
    private RelevanceTagInfoBean relevanceTagInfoBean;
    private String msg;
    private String code;

    public static RelevanceTagInfoEntity objectFromData(String str) {

        return GsonUtils.fromJson(str, RelevanceTagInfoEntity.class);
    }

    public RelevanceTagInfoBean getRelevanceTagInfoBean() {
        return relevanceTagInfoBean;
    }

    public void setRelevanceTagInfoBean(RelevanceTagInfoBean relevanceTagInfoBean) {
        this.relevanceTagInfoBean = relevanceTagInfoBean;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static class RelevanceTagInfoBean {
        /**
         * docCount : 71
         * tag : {"total":0,"img_url":"","tag_name":"防嗮","is_recommend":1,"tagProperty":1,"id":1,"status":1}
         * topTagList : [{"total":0,"tag_name":"防嗮","is_recommend":1,"tagProperty":1,"id":1,"status":1},{"total":0,"tag_name":"美白","is_recommend":1,"tagProperty":1,"id":2,"status":1},{"total":0,"tag_name":"时尚","is_recommend":1,"tagProperty":1,"id":3,"status":1},{"total":0,"tag_name":"复古","is_recommend":1,"tagProperty":1,"id":4,"status":1},{"total":0,"app_skip_id":1,"img_url":"http://image.domolife.cn/platform/NRx6WTeDhx1507967911652.png","tag_name":"护肤","is_recommend":1,"tagType":0,"tagProperty":1,"id":218,"status":1}]
         * productList : [{"productId":4287,"price":"179.00","pictureUrl":"http://img.domolife.cn/platform/ijr3MeYjEx.jpg","id":118,"title":"倍轻松ISEE100青少年眼部按摩仪"},{"productId":4382,"price":"69.00","pictureUrl":"http://img.domolife.cn/platform/EmeXchTmY2.jpg","id":344,"title":"台湾Artiart花型桌面收纳"},{"productId":4318,"price":"62.00","pictureUrl":"http://img.domolife.cn/platform/85KRniwCQD.jpg","id":205,"title":"日本FaSoLa暖桌垫冬季加热电脑垫"}]
         */

        private int docCount;
        private TagBean tag;
        private List<TopTagListBean> topTagList;
        private List<RelevanceProBean> productList;

        public static RelevanceTagInfoBean objectFromData(String str) {

            return GsonUtils.fromJson(str, RelevanceTagInfoBean.class);
        }

        public int getDocCount() {
            return docCount;
        }

        public void setDocCount(int docCount) {
            this.docCount = docCount;
        }

        public TagBean getTag() {
            return tag;
        }

        public void setTag(TagBean tag) {
            this.tag = tag;
        }

        public List<TopTagListBean> getTopTagList() {
            return topTagList;
        }

        public void setTopTagList(List<TopTagListBean> topTagList) {
            this.topTagList = topTagList;
        }

        public List<RelevanceProBean> getProductList() {
            return productList;
        }

        public void setProductList(List<RelevanceProBean> productList) {
            this.productList = productList;
        }

        public static class TagBean {
            /**
             * total : 0
             * img_url :
             * tag_name : 防嗮
             * is_recommend : 1
             * tagProperty : 1
             * id : 1
             * status : 1
             */

            private int total;
            private String img_url;
            private String tag_name;
            private int is_recommend;
            private int tagProperty;
            private int id;
            private int status;

            public static TagBean objectFromData(String str) {

                return GsonUtils.fromJson(str, TagBean.class);
            }

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }

            public String getImg_url() {
                return img_url;
            }

            public void setImg_url(String img_url) {
                this.img_url = img_url;
            }

            public String getTag_name() {
                return tag_name;
            }

            public void setTag_name(String tag_name) {
                this.tag_name = tag_name;
            }

            public int getIs_recommend() {
                return is_recommend;
            }

            public void setIs_recommend(int is_recommend) {
                this.is_recommend = is_recommend;
            }

            public int getTagProperty() {
                return tagProperty;
            }

            public void setTagProperty(int tagProperty) {
                this.tagProperty = tagProperty;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }
        }

        public static class TopTagListBean {
            /**
             * total : 0
             * tag_name : 防嗮
             * is_recommend : 1
             * tagProperty : 1
             * id : 1
             * status : 1
             * app_skip_id : 1
             * img_url : http://image.domolife.cn/platform/NRx6WTeDhx1507967911652.png
             * tagType : 0
             */

            private int total;
            private String tag_name;
            private int is_recommend;
            private int tagProperty;
            private int id;
            private int status;
            private int app_skip_id;
            private String img_url;
            private int tagType;

            public static TopTagListBean objectFromData(String str) {

                return GsonUtils.fromJson(str, TopTagListBean.class);
            }

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }

            public String getTag_name() {
                return tag_name;
            }

            public void setTag_name(String tag_name) {
                this.tag_name = tag_name;
            }

            public int getIs_recommend() {
                return is_recommend;
            }

            public void setIs_recommend(int is_recommend) {
                this.is_recommend = is_recommend;
            }

            public int getTagProperty() {
                return tagProperty;
            }

            public void setTagProperty(int tagProperty) {
                this.tagProperty = tagProperty;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getApp_skip_id() {
                return app_skip_id;
            }

            public void setApp_skip_id(int app_skip_id) {
                this.app_skip_id = app_skip_id;
            }

            public String getImg_url() {
                return img_url;
            }

            public void setImg_url(String img_url) {
                this.img_url = img_url;
            }

            public int getTagType() {
                return tagType;
            }

            public void setTagType(int tagType) {
                this.tagType = tagType;
            }
        }
    }
}
