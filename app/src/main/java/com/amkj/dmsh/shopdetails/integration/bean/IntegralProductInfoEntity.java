package com.amkj.dmsh.shopdetails.integration.bean;

import com.amkj.dmsh.constant.CommunalDetailBean;
import com.amkj.dmsh.shopdetails.bean.ShopDetailsEntity.ShopPropertyBean.SkuSaleBean;
import com.amkj.dmsh.shopdetails.bean.ShopDetailsEntity.ShopPropertyBean.PropsBean;
import com.amkj.dmsh.shopdetails.bean.ShopDetailsEntity.ShopPropertyBean.PropvaluesBean;
import com.amkj.dmsh.shopdetails.bean.ShopDetailsEntity.ShopPropertyBean.TagsBean;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/8/6
 * version 3.1.5
 * class description:积分商品详情
 */
public class IntegralProductInfoEntity {

    /**
     * code : 01
     * msg : 请求成功
     * sysTime : 2018-08-06 11:25:28
     * result : {"picUrl":"http://image.domolife.cn/platform/20170519/20170519171428009.jpg","marketPrice":"599.00","moneyPrice":"0.00","waterRemark":"","userScore":0,"propvalues":[{"propValueId":1,"propId":1,"propValueName":"默认"}],"id":7339,"integralPrice":16999,"allowCoupon":0,"skuSale":[{"quantity":2,"propValues":"1","price":"16999.00","id":1282,"isNotice":0,"moneyPrice":"0.00"}],"images":"http://image.domolife.cn/platform/20170519/20170519171425802.jpg,http://image.domolife.cn/platform/20170519/20170519171425820.jpg,http://image.domolife.cn/platform/20170519/20170519171426920.jpg,http://image.domolife.cn/platform/20170519/20170519171428009.jpg","quantity":2,"props":[{"propId":1,"propName":"默认"}],"itemBody":[{"type":"text","content":"<p style=\"white-space: normal; line-height: 1.5em;\"><br/><\/p>"},{"type":"text","content":"<p style=\"white-space: normal;\"><a class=\"spec-control disabled\" id=\"spec-forward\" style=\"margin: 0px; padding: 0px; cursor: default; left: 14px; display: block; position: absolute; top: 0px; width: 14px; height: 54px; background-image: url(https://static.360buyimg.com/item/main/1.0.34/css/i/newicon20140910.png); background-position: -56px -346px; background-repeat: no-repeat;\"><\/a><a class=\"spec-control disabled\" id=\"spec-backward\" style=\"margin: 0px; padding: 0px; cursor: default; right: 0px; display: block; position: absolute; top: 0px; width: 14px; height: 54px; background-image: url(https://static.360buyimg.com/item/main/1.0.34/css/i/newicon20140910.png); background-position: -70px -346px; background-repeat: no-repeat;\"><br/><\/a><\/p>"},{"type":"text","content":"<p style=\"white-space: normal;\"><br/><\/p>"},{"type":"text","content":"<p style=\"white-space: normal;\"><span style=\"font-family: 微软雅黑, &#39;Microsoft YaHei&#39;; font-size: 14px;\">·&nbsp;SLEEP模式（10分钟）：睡眠模式力度轻柔，智能压力按摩；轻轻地、缓缓地，一松一紧间特别舒服，刚好可以罩住整个眼部。睡前按摩10分钟，可以很好地帮助咱们入睡。<\/span><br/><\/p>"},{"type":"text","content":"<p style=\"white-space: normal;\"><span style=\"font-family: 微软雅黑, &#39;Microsoft YaHei&#39;; font-size: 14px;\">·&nbsp;MED模式 （15分钟）：这个模式的力度介于二者之间，振动频率特别快，像是小锤子轻轻在你眼周敲打，有节奏的让我们放松下来，提升眼周的肌肉活力。&nbsp;<\/span><\/p>"},{"type":"text","content":"<p style=\"white-space: normal;\"><span style=\"font-family: 微软雅黑, &#39;Microsoft YaHei&#39;; font-size: 14px;\">·&nbsp;HARD模式（20分钟）：力度比较强劲，适合用眼过度疲劳时，产生眼睛肿痛、干涩的情况，可以有效的按摩眼部9大穴位，缓解疼痛。<br/><\/span><\/p>"},{"type":"text","content":"<p style=\"white-space: normal;\"><img src=\"http://image.domolife.cn/platfrom/20170428/1493378820438055179.jpg\" title=\"1493378820438055179.jpg\" alt=\"未标题-1_01.jpg\"/><\/p>"},{"type":"text","content":"<p style=\"white-space: normal;\"><img src=\"http://image.domolife.cn/platfrom/20170428/1493378830329074501.jpg\" title=\"1493378830329074501.jpg\" alt=\"未标题-1_02.jpg\"/><\/p>"},{"type":"text","content":"<p style=\"white-space: normal;\"><img src=\"http://image.domolife.cn/platfrom/20170428/1493378846270042820.jpg\" title=\"1493378846270042820.jpg\" alt=\"未标题-1_03.jpg\"/><\/p>"},{"type":"text","content":"<p style=\"margin-top: -8px; white-space: normal;\"><img src=\"http://image.domolife.cn/platfrom/20170428/1493378858085093727.jpg\" title=\"1493378858085093727.jpg\"/><\/p>"},{"type":"text","content":"<p style=\"margin-top: -8px; white-space: normal;\"><br/><\/p>"},{"type":"text","content":"<p style=\"margin-top: -8px; white-space: normal;\"><img src=\"http://image.domolife.cn/platfrom/20170428/1493379025590036000.jpg\" title=\"1493379025590036000.jpg\" alt=\"未标题-1_05.jpg\"/><\/p>"},{"type":"text","content":"<p style=\"margin-top: -8px; white-space: normal;\"><img src=\"http://image.domolife.cn/platfrom/20170428/1493379039626043669.jpg\" title=\"1493379039626043669.jpg\" alt=\"未标题-1_06.jpg\"/><\/p>"},{"type":"text","content":"<p style=\"margin-top: -8px; white-space: normal;\"><img src=\"http://image.domolife.cn/platfrom/20170428/1493379052713036933.jpg\" title=\"1493379052713036933.jpg\" alt=\"未标题-1_07.jpg\"/><\/p>"},{"type":"text","content":"<p style=\"margin-top: -8px; white-space: normal;\"><img src=\"http://image.domolife.cn/platfrom/20170428/1493379063298041637.jpg\" title=\"1493379063298041637.jpg\" alt=\"未标题-1_08.jpg\"/><\/p>"},{"type":"text","content":"<p style=\"margin-top: -8px; white-space: normal;\"><img src=\"http://image.domolife.cn/platfrom/20170428/1493379070236060917.jpg\" title=\"1493379070236060917.jpg\" alt=\"未标题-1_09.jpg\"/><\/p>"},{"type":"text","content":"<p style=\"margin-top: -8px; white-space: normal;\"><img src=\"http://image.domolife.cn/platfrom/20170428/1493379082133062186.jpg\" title=\"1493379082133062186.jpg\" alt=\"未标题-1_10.jpg\"/><\/p>"},{"type":"text","content":"<p style=\"margin-top: -8px; white-space: normal;\"><img src=\"http://image.domolife.cn/platfrom/20170428/1493379093303000802.jpg\" title=\"1493379093303000802.jpg\" alt=\"未标题-1_11.jpg\"/><\/p>"},{"type":"text","content":"<p style=\"margin-top: -8px; white-space: normal;\"><img src=\"http://image.domolife.cn/platfrom/20170428/1493379141165049895.jpg\" title=\"1493379141165049895.jpg\" alt=\"未标题-1_12.jpg\"/><\/p>"},{"type":"text","content":"<p style=\"margin-top: -8px; white-space: normal;\"><img src=\"http://image.domolife.cn/platfrom/20170428/1493379149696009304.jpg\" title=\"1493379149696009304.jpg\" alt=\"未标题-1_13.jpg\"/><\/p>"},{"type":"text","content":"<p style=\"margin-top: -8px; white-space: normal;\"><img src=\"http://image.domolife.cn/platfrom/20170428/1493379168178070011.jpg\" title=\"1493379168178070011.jpg\" alt=\"未标题-1_14.jpg\"/><\/p>"},{"type":"text","content":"<p style=\"margin-top: -8px; white-space: normal;\"><br/><\/p>"},{"type":"text","content":"<p><br/><\/p>"}],"subtitle":null,"name":"倍轻松isee16 眼部按摩器","tags":[{"name":"24小时发货","id":1},{"name":"72小时发货","id":9},{"name":"一件包邮","id":8},{"name":"48小时发货","id":7},{"name":"正品保证","id":2},{"name":"海外直邮","id":3},{"name":"30天退货","id":4},{"name":"香港直邮","id":5},{"name":"不支持7天无理由退货\r\n","id":6},{"name":"全国包邮","id":11},{"name":"包邮除偏","id":10},{"name":"全国包邮（偏远地区不接单）","id":12}],"integralType":0,"integralProductType":0}
     */

    private String code;
    private String msg;
    private String sysTime;
    @SerializedName("result")
    private IntegralProductInfoBean integralProductInfoBean;

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

    public String getSysTime() {
        return sysTime;
    }

    public void setSysTime(String sysTime) {
        this.sysTime = sysTime;
    }

    public IntegralProductInfoBean getIntegralProductInfoBean() {
        return integralProductInfoBean;
    }

    public void setIntegralProductInfoBean(IntegralProductInfoBean integralProductInfoBean) {
        this.integralProductInfoBean = integralProductInfoBean;
    }

    public static class IntegralProductInfoBean {
        /**
         * picUrl : http://image.domolife.cn/platform/20170519/20170519171428009.jpg
         * marketPrice : 599.00
         * moneyPrice : 0.00
         * waterRemark :
         * userScore : 0
         * propvalues : [{"propValueId":1,"propId":1,"propValueName":"默认"}]
         * id : 7339
         * integralPrice : 16999
         * allowCoupon : 0
         * skuSale : [{"quantity":2,"propValues":"1","price":"16999.00","id":1282,"isNotice":0,"moneyPrice":"0.00"}]
         * images : http://image.domolife.cn/platform/20170519/20170519171425802.jpg,http://image.domolife.cn/platform/20170519/20170519171425820.jpg,http://image.domolife.cn/platform/20170519/20170519171426920.jpg,http://image.domolife.cn/platform/20170519/20170519171428009.jpg
         * quantity : 2
         * props : [{"propId":1,"propName":"默认"}]
         * itemBody : [{"type":"text","content":"<p style=\"white-space: normal; line-height: 1.5em;\"><br/><\/p>"},{"type":"text","content":"<p style=\"white-space: normal;\"><a class=\"spec-control disabled\" id=\"spec-forward\" style=\"margin: 0px; padding: 0px; cursor: default; left: 14px; display: block; position: absolute; top: 0px; width: 14px; height: 54px; background-image: url(https://static.360buyimg.com/item/main/1.0.34/css/i/newicon20140910.png); background-position: -56px -346px; background-repeat: no-repeat;\"><\/a><a class=\"spec-control disabled\" id=\"spec-backward\" style=\"margin: 0px; padding: 0px; cursor: default; right: 0px; display: block; position: absolute; top: 0px; width: 14px; height: 54px; background-image: url(https://static.360buyimg.com/item/main/1.0.34/css/i/newicon20140910.png); background-position: -70px -346px; background-repeat: no-repeat;\"><br/><\/a><\/p>"},{"type":"text","content":"<p style=\"white-space: normal;\"><br/><\/p>"},{"type":"text","content":"<p style=\"white-space: normal;\"><span style=\"font-family: 微软雅黑, &#39;Microsoft YaHei&#39;; font-size: 14px;\">·&nbsp;SLEEP模式（10分钟）：睡眠模式力度轻柔，智能压力按摩；轻轻地、缓缓地，一松一紧间特别舒服，刚好可以罩住整个眼部。睡前按摩10分钟，可以很好地帮助咱们入睡。<\/span><br/><\/p>"},{"type":"text","content":"<p style=\"white-space: normal;\"><span style=\"font-family: 微软雅黑, &#39;Microsoft YaHei&#39;; font-size: 14px;\">·&nbsp;MED模式 （15分钟）：这个模式的力度介于二者之间，振动频率特别快，像是小锤子轻轻在你眼周敲打，有节奏的让我们放松下来，提升眼周的肌肉活力。&nbsp;<\/span><\/p>"},{"type":"text","content":"<p style=\"white-space: normal;\"><span style=\"font-family: 微软雅黑, &#39;Microsoft YaHei&#39;; font-size: 14px;\">·&nbsp;HARD模式（20分钟）：力度比较强劲，适合用眼过度疲劳时，产生眼睛肿痛、干涩的情况，可以有效的按摩眼部9大穴位，缓解疼痛。<br/><\/span><\/p>"},{"type":"text","content":"<p style=\"white-space: normal;\"><img src=\"http://image.domolife.cn/platfrom/20170428/1493378820438055179.jpg\" title=\"1493378820438055179.jpg\" alt=\"未标题-1_01.jpg\"/><\/p>"},{"type":"text","content":"<p style=\"white-space: normal;\"><img src=\"http://image.domolife.cn/platfrom/20170428/1493378830329074501.jpg\" title=\"1493378830329074501.jpg\" alt=\"未标题-1_02.jpg\"/><\/p>"},{"type":"text","content":"<p style=\"white-space: normal;\"><img src=\"http://image.domolife.cn/platfrom/20170428/1493378846270042820.jpg\" title=\"1493378846270042820.jpg\" alt=\"未标题-1_03.jpg\"/><\/p>"},{"type":"text","content":"<p style=\"margin-top: -8px; white-space: normal;\"><img src=\"http://image.domolife.cn/platfrom/20170428/1493378858085093727.jpg\" title=\"1493378858085093727.jpg\"/><\/p>"},{"type":"text","content":"<p style=\"margin-top: -8px; white-space: normal;\"><br/><\/p>"},{"type":"text","content":"<p style=\"margin-top: -8px; white-space: normal;\"><img src=\"http://image.domolife.cn/platfrom/20170428/1493379025590036000.jpg\" title=\"1493379025590036000.jpg\" alt=\"未标题-1_05.jpg\"/><\/p>"},{"type":"text","content":"<p style=\"margin-top: -8px; white-space: normal;\"><img src=\"http://image.domolife.cn/platfrom/20170428/1493379039626043669.jpg\" title=\"1493379039626043669.jpg\" alt=\"未标题-1_06.jpg\"/><\/p>"},{"type":"text","content":"<p style=\"margin-top: -8px; white-space: normal;\"><img src=\"http://image.domolife.cn/platfrom/20170428/1493379052713036933.jpg\" title=\"1493379052713036933.jpg\" alt=\"未标题-1_07.jpg\"/><\/p>"},{"type":"text","content":"<p style=\"margin-top: -8px; white-space: normal;\"><img src=\"http://image.domolife.cn/platfrom/20170428/1493379063298041637.jpg\" title=\"1493379063298041637.jpg\" alt=\"未标题-1_08.jpg\"/><\/p>"},{"type":"text","content":"<p style=\"margin-top: -8px; white-space: normal;\"><img src=\"http://image.domolife.cn/platfrom/20170428/1493379070236060917.jpg\" title=\"1493379070236060917.jpg\" alt=\"未标题-1_09.jpg\"/><\/p>"},{"type":"text","content":"<p style=\"margin-top: -8px; white-space: normal;\"><img src=\"http://image.domolife.cn/platfrom/20170428/1493379082133062186.jpg\" title=\"1493379082133062186.jpg\" alt=\"未标题-1_10.jpg\"/><\/p>"},{"type":"text","content":"<p style=\"margin-top: -8px; white-space: normal;\"><img src=\"http://image.domolife.cn/platfrom/20170428/1493379093303000802.jpg\" title=\"1493379093303000802.jpg\" alt=\"未标题-1_11.jpg\"/><\/p>"},{"type":"text","content":"<p style=\"margin-top: -8px; white-space: normal;\"><img src=\"http://image.domolife.cn/platfrom/20170428/1493379141165049895.jpg\" title=\"1493379141165049895.jpg\" alt=\"未标题-1_12.jpg\"/><\/p>"},{"type":"text","content":"<p style=\"margin-top: -8px; white-space: normal;\"><img src=\"http://image.domolife.cn/platfrom/20170428/1493379149696009304.jpg\" title=\"1493379149696009304.jpg\" alt=\"未标题-1_13.jpg\"/><\/p>"},{"type":"text","content":"<p style=\"margin-top: -8px; white-space: normal;\"><img src=\"http://image.domolife.cn/platfrom/20170428/1493379168178070011.jpg\" title=\"1493379168178070011.jpg\" alt=\"未标题-1_14.jpg\"/><\/p>"},{"type":"text","content":"<p style=\"margin-top: -8px; white-space: normal;\"><br/><\/p>"},{"type":"text","content":"<p><br/><\/p>"}]
         * subtitle : null
         * name : 倍轻松isee16 眼部按摩器
         * tags : [{"name":"24小时发货","id":1},{"name":"72小时发货","id":9},{"name":"一件包邮","id":8},{"name":"48小时发货","id":7},{"name":"正品保证","id":2},{"name":"海外直邮","id":3},{"name":"30天退货","id":4},{"name":"香港直邮","id":5},{"name":"不支持7天无理由退货\r\n","id":6},{"name":"全国包邮","id":11},{"name":"包邮除偏","id":10},{"name":"全国包邮（偏远地区不接单）","id":12}]
         * integralType : 0
         * integralProductType : 0
         */

        private String picUrl;
        private String marketPrice;
        private String moneyPrice;
        private String waterRemark;
        private int userScore;
        private int id;
        private int integralPrice;
        private int allowCoupon;
        private String images;
        private int quantity;
        private Object subtitle;
        private String name;
        private int integralType;
        private int integralProductType;//积分商品类型,0是实物,1是虚拟商品
        private List<PropvaluesBean> propvalues;
        private List<SkuSaleBean> skuSale;
        private List<PropsBean> props;
        @SerializedName("itemBody")
        private List<CommunalDetailBean> productDetails;
        private List<TagsBean> tags;
        private String tagIds;

        public String getTagIds() {
            return tagIds;
        }

        public void setTagIds(String tagIds) {
            this.tagIds = tagIds;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public String getMarketPrice() {
            return marketPrice;
        }

        public void setMarketPrice(String marketPrice) {
            this.marketPrice = marketPrice;
        }

        public String getMoneyPrice() {
            return moneyPrice;
        }

        public void setMoneyPrice(String moneyPrice) {
            this.moneyPrice = moneyPrice;
        }

        public String getWaterRemark() {
            return waterRemark;
        }

        public void setWaterRemark(String waterRemark) {
            this.waterRemark = waterRemark;
        }

        public int getUserScore() {
            return userScore;
        }

        public void setUserScore(int userScore) {
            this.userScore = userScore;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getIntegralPrice() {
            return integralPrice;
        }

        public void setIntegralPrice(int integralPrice) {
            this.integralPrice = integralPrice;
        }

        public int getAllowCoupon() {
            return allowCoupon;
        }

        public void setAllowCoupon(int allowCoupon) {
            this.allowCoupon = allowCoupon;
        }

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public Object getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(Object subtitle) {
            this.subtitle = subtitle;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIntegralType() {
            return integralType;
        }

        public void setIntegralType(int integralType) {
            this.integralType = integralType;
        }

        public int getIntegralProductType() {
            return integralProductType;
        }

        public void setIntegralProductType(int integralProductType) {
            this.integralProductType = integralProductType;
        }

        public List<PropvaluesBean> getPropvalues() {
            return propvalues;
        }

        public void setPropvalues(List<PropvaluesBean> propvalues) {
            this.propvalues = propvalues;
        }

        public List<SkuSaleBean> getSkuSale() {
            return skuSale;
        }

        public void setSkuSale(List<SkuSaleBean> skuSale) {
            this.skuSale = skuSale;
        }

        public List<PropsBean> getProps() {
            return props;
        }

        public void setProps(List<PropsBean> props) {
            this.props = props;
        }

        public List<CommunalDetailBean> getProductDetails() {
            return productDetails;
        }

        public void setProductDetails(List<CommunalDetailBean> productDetails) {
            this.productDetails = productDetails;
        }

        public List<TagsBean> getTags() {
            return tags;
        }

        public void setTags(List<TagsBean> tags) {
            this.tags = tags;
        }
    }
}
