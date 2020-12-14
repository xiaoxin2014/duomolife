package com.amkj.dmsh.address.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.amkj.dmsh.base.BaseEntity;
import com.google.gson.annotations.SerializedName;

/**
 * Created by atd48 on 2016/10/17.
 */
public class AddressInfoEntity extends BaseEntity {

    @SerializedName("result")
    private AddressInfoBean addressInfoBean;

    public AddressInfoBean getAddressInfoBean() {
        return addressInfoBean;
    }

    public void setAddressInfoBean(AddressInfoBean addressInfoBean) {
        this.addressInfoBean = addressInfoBean;
    }

    public static class AddressInfoBean implements Parcelable {
        private int id;
        private int status;
        private int user_id;
        private String address;
        private String consignee;
        private String community;
        private String province;
        private String postcode;
        private String district;
        private String address_com;
        private String city;
        private String mobile;

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

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getConsignee() {
            return consignee;
        }

        public void setConsignee(String consignee) {
            this.consignee = consignee;
        }

        public String getCommunity() {
            return community;
        }

        public void setCommunity(String community) {
            this.community = community;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getPostcode() {
            return postcode;
        }

        public void setPostcode(String postcode) {
            this.postcode = postcode;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getAddress_com() {
            return address_com;
        }

        public void setAddress_com(String address_com) {
            this.address_com = address_com;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeInt(this.status);
            dest.writeInt(this.user_id);
            dest.writeString(this.address);
            dest.writeString(this.consignee);
            dest.writeString(this.community);
            dest.writeString(this.province);
            dest.writeString(this.postcode);
            dest.writeString(this.district);
            dest.writeString(this.address_com);
            dest.writeString(this.city);
            dest.writeString(this.mobile);
        }

        public AddressInfoBean() {
        }

        protected AddressInfoBean(Parcel in) {
            this.id = in.readInt();
            this.status = in.readInt();
            this.user_id = in.readInt();
            this.address = in.readString();
            this.consignee = in.readString();
            this.community = in.readString();
            this.province = in.readString();
            this.postcode = in.readString();
            this.district = in.readString();
            this.address_com = in.readString();
            this.city = in.readString();
            this.mobile = in.readString();
        }

        public static final Parcelable.Creator<AddressInfoBean> CREATOR = new Parcelable.Creator<AddressInfoBean>() {
            @Override
            public AddressInfoBean createFromParcel(Parcel source) {
                return new AddressInfoBean(source);
            }

            @Override
            public AddressInfoBean[] newArray(int size) {
                return new AddressInfoBean[size];
            }
        };
    }
}
