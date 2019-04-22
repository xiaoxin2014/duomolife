package com.amkj.dmsh.dominant.bean;

import com.amkj.dmsh.base.BaseEntity;
import com.amkj.dmsh.base.BaseRemoveExistProductBean;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/7/11
 * version 3.1.5
 * class description:良品好物-商品&广告
 */
public class QualityGoodProductEntity extends BaseEntity {

    /**
     * result : [{"marketPrice":"200.00","quantity":39,"productId":20066,"waterRemark":"","picUrl":"http://image.domolife.cn/platform/Z7mBhy5FJF1530688656056.jpg","sellStatus":"","price":"0.01","subtitle":"测试文案2","isWeb":0,"name":"测试商品2","rank":0,"objectType":"product","id":20066},{"show_time":"0","sub_title":"好物1","link":"app://DMLGoodsProductsInfoViewController?goodsId=4324","end_time":"2100-10-10 10:10:10","begin_time":"2018-05-01 00:00:00","android_link":"app://ShopScrollDetailsActivity?productId=4324","title":"好物1","web_link":"http://www.domolife.cn/m/template/common/proprietary.html?id=4324","object_id":4324,"min_time":"2100-10-10 10:10:10","width":"10","ctime":"2018-07-10 11:44:31","web_pc_link":"http://www.domolife.cn/template/common/proprietary.html?id=4324","objectType":"ad","id":396,"pic_url":"http://image.domolife.cn/platform/AQzMxHA28A1531194231502.jpg","object":1,"height":"20"},{"marketPrice":"249.00","quantity":112,"productId":9963,"waterRemark":"","activity_code":"","picUrl":"http://image.domolife.cn/platform/20171020/20171020154120656.jpg","sellStatus":"","price":"229.00","subtitle":"经典通勤，单穿打底轻松驾驭","isWeb":0,"name":"UBRAS 通勤系列文胸","rank":0,"objectType":"product","id":9963},{"marketPrice":"36.00","quantity":160,"productId":7538,"waterRemark":"","activity_code":"","picUrl":"http://image.domolife.cn/platform/20170527/20170527143530594.jpg","sellStatus":"","price":"0.01","subtitle":"卡通趣味，俘虏你的少女心","isWeb":0,"name":"可妮兔 布朗熊 iphone卡通手机壳（带指环）","rank":0,"objectType":"product","id":7538},{"show_time":"0","sub_title":"好物2","link":"app://DMLGoodsProductsInfoViewController?goodsId=7062","end_time":"2100-10-10 10:10:10","begin_time":"2018-05-01 00:00:00","android_link":"app://ShopScrollDetailsActivity?productId=7062","title":"好物2","web_link":"http://www.domolife.cn/m/template/common/proprietary.html?id=7062","object_id":7062,"min_time":"2100-10-10 10:10:10","width":"100%","ctime":"2018-07-10 11:45:07","web_pc_link":"http://www.domolife.cn/template/common/proprietary.html?id=7062","objectType":"ad","id":397,"pic_url":"http://image.domolife.cn/platform/x3xTztXz4c1531194279615.jpg","object":1,"height":"100%"},{"marketPrice":"138.00","quantity":1,"productId":11614,"waterRemark":"","activity_code":"","picUrl":"http://image.domolife.cn/platform/20180126/20180126154313464.jpg","sellStatus":"","price":"128.00","subtitle":"日本女星御用面膜首选！","isWeb":0,"name":"日本新版LuLuLun粉色补水面膜42枚抽取式盒装","rank":0,"objectType":"product","id":11614}]
     * msg : 请求成功
     * code : 01
     */

    @SerializedName("result")
    private List<Attribute> goodProductList;

    public List<Attribute> getGoodProductList() {
        return goodProductList;
    }

    public void setGoodProductList(List<Attribute> goodProductList) {
        this.goodProductList = goodProductList;
    }

    public static class Attribute extends BaseRemoveExistProductBean{
        protected String objectType;

        public String getObjectType() {
            return objectType;
        }

        public void setObjectType(String objectType) {
            this.objectType = objectType;
        }
    }
}
