package com.amkj.dmsh.address.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by atd48 on 2016/10/17.
 */
public class AddressInfoEntity {

    /**
     * id : 1078
     * status : 0
     * address : 广东省深圳市福田区理想公馆2806
     * consignee : LXQ
     * community : 理想公馆
     * province : 广东省
     * user_id : 23291
     * postcode : 621000
     * district : 福田区
     * address_com : 福田区
     * city : 深圳市
     * mobile : 18123934025
     */

    @SerializedName("result")
    private AddressInfoBean addressInfoBean;
    /**
     * result : {"id":1078,"status":0,"address":"广东省深圳市福田区理想公馆2806","consignee":"LXQ","community":"理想公馆","province":"广东省","user_id":23291,"postcode":"621000","district":"福田区","address_com":"福田区","city":"深圳市","mobile":"18123934025"}
     * code : 01
     * msg : 请求成功
     */

    private String code;
    private String msg;

    public AddressInfoBean getAddressInfoBean() {
        return addressInfoBean;
    }

    public void setAddressInfoBean(AddressInfoBean addressInfoBean) {
        this.addressInfoBean = addressInfoBean;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class AddressInfoBean {
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
    }
}
