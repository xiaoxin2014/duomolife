package com.amkj.dmsh.shopdetails.integration.bean;

import com.amkj.dmsh.base.BaseEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by atd48 on 2016/10/17.
 */
public class AddressListEntity extends BaseEntity{

    /**
     * result : [{"id":1089,"status":0,"address":"soho100","consignee":"萧文辉","community":"0","province":"110000","user_id":23317,"district":"110101","address_com":"广东深圳福田区","city":"110100","mobile":"13713732676"},{"id":1093,"status":0,"address":"零零落落零零落落啦啦啦","consignee":"萧文辉","community":"0","province":"440000","user_id":23317,"district":"440306","address_com":"广东省深圳市宝安区","city":"440300","mobile":"13713732676"},{"id":1094,"status":1,"address":"Gggbbbcccx","consignee":"Cxxff","community":"0","province":"530000","user_id":23317,"district":"530102","address_com":"云南省昆明市五华区","city":"530100","mobile":"13713732676"}]
     * code : 01
     * msg : 请求成功
     */
    /**
     * id : 1089
     * status : 0
     * address : soho100
     * consignee : 萧文辉
     * community : 0
     * province : 110000
     * user_id : 23317
     * district : 110101
     * address_com : 广东深圳福田区
     * city : 110100
     * mobile : 13713732676
     */

    @SerializedName("result")
    private List<AddressListBean> addressAllBeanList;

    public List<AddressListBean> getAddressAllBeanList() {
        return addressAllBeanList;
    }

    public void setAddressAllBeanList(List<AddressListBean> addressAllBeanList) {
        this.addressAllBeanList = addressAllBeanList;
    }

    public static class AddressListBean {
        private int id;
        private int status;
        private String address;
        private String consignee;
        private String community;
        private String province;
        private int user_id;
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
