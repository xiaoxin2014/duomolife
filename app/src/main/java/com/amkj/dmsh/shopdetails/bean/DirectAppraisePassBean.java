package com.amkj.dmsh.shopdetails.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by atd48 on 2016/10/28.
 */
public class DirectAppraisePassBean implements Parcelable {
    private int productId;
    private int orderProductId;
    private int star = 5;
    private String images;
    private String imagePaths;
    private String content;
    private String path;
    private String id;
    private String count;
    private String reason;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getOrderProductId() {
        return orderProductId;
    }

    public void setOrderProductId(int orderProductId) {
        this.orderProductId = orderProductId;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public String getImagePaths() {
        return imagePaths;
    }

    public void setImagePaths(String imagePaths) {
        this.imagePaths = imagePaths;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public DirectAppraisePassBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.productId);
        dest.writeInt(this.orderProductId);
        dest.writeInt(this.star);
        dest.writeString(this.images);
        dest.writeString(this.imagePaths);
        dest.writeString(this.content);
        dest.writeString(this.path);
        dest.writeString(this.id);
        dest.writeString(this.count);
        dest.writeString(this.reason);
    }

    protected DirectAppraisePassBean(Parcel in) {
        this.productId = in.readInt();
        this.orderProductId = in.readInt();
        this.star = in.readInt();
        this.images = in.readString();
        this.imagePaths = in.readString();
        this.content = in.readString();
        this.path = in.readString();
        this.id = in.readString();
        this.count = in.readString();
        this.reason = in.readString();
    }

    public static final Creator<DirectAppraisePassBean> CREATOR = new Creator<DirectAppraisePassBean>() {
        @Override
        public DirectAppraisePassBean createFromParcel(Parcel source) {
            return new DirectAppraisePassBean(source);
        }

        @Override
        public DirectAppraisePassBean[] newArray(int size) {
            return new DirectAppraisePassBean[size];
        }
    };
}
