package com.amkj.dmsh.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.amkj.dmsh.base.BaseEntity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by atd48 on 2016/9/13.
 */
public class QualityTypeEntity extends BaseEntity {

    /**
     * result : [{"id":28,"picUrl":"/Uploads/goods_img/2016-04-21/571844b592752.png","category_name":"文体"},{"id":30,"picUrl":"/Uploads/goods_img/2016-04-21/571844ad9f042.png","category_name":"家电"},{"id":29,"picUrl":"/Uploads/goods_img/2016-04-21/5718449badabb.png","category_name":"数码"},{"id":18,"picUrl":"/Uploads/goods_img/2016-04-21/5718449343d0f.png","category_name":"母婴"},{"id":2,"picUrl":"/Uploads/goods_img/2016-04-21/57184486b6870.png","category_name":"男装"},{"id":4,"picUrl":"/Uploads/goods_img/2016-04-21/5718447e952e1.png","category_name":"家居"},{"id":31,"picUrl":"/Uploads/goods_img/2016-04-21/5718447739dc4.png","category_name":"化妆品"},{"id":25,"picUrl":"/Uploads/goods_img/2016-04-21/5718446f99e0c.png","category_name":"鞋包"},{"id":26,"picUrl":"/Uploads/goods_img/2016-04-21/57184468eb566.png","category_name":"配饰"},{"id":27,"picUrl":"/Uploads/goods_img/2016-04-21/5718445b1d04f.png","category_name":"美食"}]
     * code : 01
     * msg : 请求成功
     */
    /**
     * id : 28
     * picUrl : /Uploads/goods_img/2016-04-21/571844b592752.png
     * category_name : 文体
     */

    @SerializedName("result")
    private List<QualityTypeBean> qualityTypeBeanList;

    public List<QualityTypeBean> getQualityTypeBeanList() {
        return qualityTypeBeanList;
    }

    public void setQualityTypeBeanList(List<QualityTypeBean> qualityTypeBeanList) {
        this.qualityTypeBeanList = qualityTypeBeanList;
    }

    public static class QualityTypeBean implements Parcelable, MultiItemEntity {
        private int id;
        private String picUrl;
        private String name;
        private String description;
        private int type;
        private int categoryId;
        private int categoryType;
        private String childCategory;
        private String relateName;
        private int relateId;
        private String webLink;
        /**
         * 自定义参数
         */
        private boolean select;
        public final String PRODUCT_TYPE = "productType";
        public final String SORT_TYPE = "sortType";
        private String dataType;
        private String sortType;
        private String sortName;
        private String childName;
        private int itemType;


        /**
         * childCategoryList : [{"picUrl":"http://image.domolife.cn/platform/445b8Rj24P1523673457180.jpeg","name":"布艺家纺","pid":4,"id":48,"type":1},{"picUrl":"","name":"家居家具","pid":4,"id":49,"type":1},{"picUrl":"","name":"收纳整理","pid":4,"id":50,"type":1},{"picUrl":"","name":"居家日化","pid":4,"id":51,"type":1},{"picUrl":"","name":"家庭清洁","pid":4,"id":52,"type":1},{"picUrl":"","name":"厨房用品","pid":4,"id":53,"type":1}]
         * pid : 0
         */

        private int pid;
        private String pName;


        private List<ChildCategoryListBean> childCategoryList;
        private List<UserLikedProductEntity.AdBean> ad;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        /**
         * ad : {"picUrl":"http://image.domolife.cn/platform/mW6GEFis3N1555323545506.jpg","web":"http://www.domolife.cn/m/template/common/proprietary.html?id=14634","android":"app://ShopScrollDetailsActivity?productId=14634","wechat":"/pages/goodsDetails/goodsDetails?id=14634","ios":"app://DMLGoodsProductsInfoViewController?goodsId=14634"}
         */




        public int getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(int categoryId) {
            this.categoryId = categoryId;
        }

        public String getpName() {
            return pName;
        }

        public void setpName(String pName) {
            this.pName = pName;
        }

        public int getCategoryType() {
            return categoryType;
        }

        public void setCategoryType(int categoryType) {
            this.categoryType = categoryType;
        }

        public String getRelateName() {
            return relateName;
        }

        public void setRelateName(String relateName) {
            this.relateName = relateName;
        }

        public int getRelateId() {
            return relateId;
        }

        public void setRelateId(int relateId) {
            this.relateId = relateId;
        }

        public String getWebLink() {
            return webLink;
        }

        public void setWebLink(String webLink) {
            this.webLink = webLink;
        }

        public String getChildName() {
            return childName;
        }

        public void setChildName(String childName) {
            this.childName = childName;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isSelect() {
            return select;
        }

        public void setSelect(boolean select) {
            this.select = select;
        }

        public String getDataType() {
            return dataType;
        }

        public void setDataType(String dataType) {
            this.dataType = dataType;
        }

        public String getSortType() {
            return sortType;
        }

        public void setSortType(String sortType) {
            this.sortType = sortType;
        }

        public String getSortName() {
            return sortName;
        }

        public void setSortName(String sortName) {
            this.sortName = sortName;
        }

        public String getChildCategory() {
            return childCategory;
        }

        public void setChildCategory(String childCategory) {
            this.childCategory = childCategory;
        }

        public QualityTypeBean() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.picUrl);
            dest.writeString(this.name);
            dest.writeInt(this.type);
            dest.writeString(this.childCategory);
            dest.writeString(this.relateName);
            dest.writeInt(this.relateId);
            dest.writeString(this.webLink);
        }

        protected QualityTypeBean(Parcel in) {
            this.id = in.readInt();
            this.picUrl = in.readString();
            this.name = in.readString();
            this.type = in.readInt();
            this.childCategory = in.readString();
            this.relateName = in.readString();
            this.relateId = in.readInt();
            this.webLink = in.readString();
        }

        public static final Creator<QualityTypeBean> CREATOR = new Creator<QualityTypeBean>() {
            @Override
            public QualityTypeBean createFromParcel(Parcel source) {
                return new QualityTypeBean(source);
            }

            @Override
            public QualityTypeBean[] newArray(int size) {
                return new QualityTypeBean[size];
            }
        };

        @Override
        public int getItemType() {
            return itemType;
        }

        public void setItemType(int itemType) {
            this.itemType = itemType;
        }

        public int getPid() {
            return pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        public List<ChildCategoryListBean> getChildCategoryList() {
            return childCategoryList;
        }

        public void setChildCategoryList(List<ChildCategoryListBean> childCategoryList) {
            this.childCategoryList = childCategoryList;
        }

        public List<UserLikedProductEntity.AdBean> getAd() {
            return ad;
        }

        public void setAd(List<UserLikedProductEntity.AdBean> ad) {
            this.ad = ad;
        }


        public static class ChildCategoryListBean {
            /**
             * picUrl : http://image.domolife.cn/platform/445b8Rj24P1523673457180.jpeg
             * name : 布艺家纺
             * pid : 4
             * id : 48
             * type : 1
             */
            private String picUrl;
            private String name;
            private int pid;
            private int id;
            private int type;

            public String getPicUrl() {
                return picUrl;
            }

            public void setPicUrl(String picUrl) {
                this.picUrl = picUrl;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getPid() {
                return pid;
            }

            public void setPid(int pid) {
                this.pid = pid;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }
        }
    }
}
