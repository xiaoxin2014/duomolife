package com.amkj.dmsh.shopdetails.integration.bean;


import com.amkj.dmsh.address.bean.AddressInfoEntity.AddressInfoBean;
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
    private List<AddressInfoBean > addressAllBeanList;

    public List<AddressInfoBean> getAddressAllBeanList() {
        return addressAllBeanList;
    }

    public void setAddressAllBeanList(List<AddressInfoBean> addressAllBeanList) {
        this.addressAllBeanList = addressAllBeanList;
    }
}
