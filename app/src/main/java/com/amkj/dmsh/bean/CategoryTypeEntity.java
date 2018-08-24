package com.amkj.dmsh.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by atd48 on 2016/8/27.
 */
public class CategoryTypeEntity {

    /**
     * result : [{"icon":"http://img.domolife.cn/platform/JYj2rs2wb21494487118056.png","id":48,"title":"多么优选"},{"icon":"http://img.domolife.cn/platform/YxrcEjtkBr.png","id":1,"title":"品生活"},{"icon":"http://img.domolife.cn/platform/BDApw4dES8.png","id":40,"title":"种草营"},{"icon":"http://img.domolife.cn/platform/NeNr3rMjxR.png","id":41,"title":"小书屋"},{"icon":"http://img.domolife.cn/platform/jyDAsGKNC2.png","id":47,"title":"下厨吧"},{"icon":"http://img.domolife.cn/platform/Ds3WFBfmpR.png","id":2,"title":"育儿"}]
     * msg : 请求成功
     * code : 01
     */

    private String msg;
    private String code;
    @SerializedName("result")
    private List<CategoryTypeBean> categoryTypeList;

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

    public List<CategoryTypeBean> getCategoryTypeList() {
        return categoryTypeList;
    }

    public void setCategoryTypeList(List<CategoryTypeBean> categoryTypeList) {
        this.categoryTypeList = categoryTypeList;
    }

    public static class CategoryTypeBean implements Parcelable {
        /**
         * icon : http://img.domolife.cn/platform/JYj2rs2wb21494487118056.png
         * id : 48
         * title : 多么优选
         */

        private String icon;
        private int id;
        private String title;

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

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

        public CategoryTypeBean() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.icon);
            dest.writeInt(this.id);
            dest.writeString(this.title);
        }

        protected CategoryTypeBean(Parcel in) {
            this.icon = in.readString();
            this.id = in.readInt();
            this.title = in.readString();
        }

        public static final Creator<CategoryTypeBean> CREATOR = new Creator<CategoryTypeBean>() {
            @Override
            public CategoryTypeBean createFromParcel(Parcel source) {
                return new CategoryTypeBean(source);
            }

            @Override
            public CategoryTypeBean[] newArray(int size) {
                return new CategoryTypeBean[size];
            }
        };
    }
}
