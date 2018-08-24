package com.amkj.dmsh.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by atd48 on 2016/8/29.
 */
public class HotSearchTagEntity {

    /**
     * result : [{"total":0,"tag_name":"创意家居","is_recommend":1,"tagProperty":2,"id":5,"status":1},{"total":0,"tag_name":"日韩","is_recommend":1,"tagProperty":2,"id":6,"status":1},{"total":0,"tag_name":"哈哈","is_recommend":1,"tagProperty":2,"id":226,"status":1},{"total":0,"app_skip_id":1,"ios_link":"app://DMLGoodsProductsInfoViewController?goodsId=11111","tag_name":"h","is_recommend":1,"tagType":0,"tagProperty":3,"android_link":"app://ShopScrollDetailsActivity?productId=11111","id":228,"object_id":11111,"status":1}]
     * msg : 请求成功
     * code : 01
     */

    private String msg;
    private String code;
    @SerializedName("result")
    private List<HotSearchTagBean> hotSearchTagList;

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

    public List<HotSearchTagBean> getHotSearchTagList() {
        return hotSearchTagList;
    }

    public void setHotSearchTagList(List<HotSearchTagBean> hotSearchTagList) {
        this.hotSearchTagList = hotSearchTagList;
    }

    public static class HotSearchTagBean {
        /**
         * total : 0
         * tag_name : 创意家居
         * is_recommend : 1
         * tagProperty : 2
         * id : 5
         * status : 1
         * app_skip_id : 1
         * ios_link : app://DMLGoodsProductsInfoViewController?goodsId=11111
         * tagType : 0
         * android_link : app://ShopScrollDetailsActivity?productId=11111
         * object_id : 11111
         */

        private int total;
        private String tag_name;
        private int is_recommend;
        private int tagProperty;
        private int id;
        private int status;
        private int app_skip_id;
        private String ios_link;
        private int tagType;
        private String android_link;
        private int object_id;

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

        public String getIos_link() {
            return ios_link;
        }

        public void setIos_link(String ios_link) {
            this.ios_link = ios_link;
        }

        public int getTagType() {
            return tagType;
        }

        public void setTagType(int tagType) {
            this.tagType = tagType;
        }

        public String getAndroid_link() {
            return android_link;
        }

        public void setAndroid_link(String android_link) {
            this.android_link = android_link;
        }

        public int getObject_id() {
            return object_id;
        }

        public void setObject_id(int object_id) {
            this.object_id = object_id;
        }
    }
}
