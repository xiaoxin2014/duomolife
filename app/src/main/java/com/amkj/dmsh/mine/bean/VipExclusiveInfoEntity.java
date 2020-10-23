package com.amkj.dmsh.mine.bean;

import com.amkj.dmsh.base.BaseEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by xiaoxin on 2020/9/24
 * Version:v4.7.0
 */
public class VipExclusiveInfoEntity extends BaseEntity {

    private List<VipExclusiveInfoBean> result;

    public List<VipExclusiveInfoBean> getResult() {
        return result;
    }

    public void setResult(List<VipExclusiveInfoBean> result) {
        this.result = result;
    }

    public static class VipExclusiveInfoBean {
        /**
         * title : 美妆护理
         * categoryId : 2
         */

        @SerializedName(value = "title", alternate = "name")
        private String title;
        @SerializedName(value = "categoryId", alternate = "id")
        private String categoryId;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }
    }
}
