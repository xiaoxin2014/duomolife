package com.amkj.dmsh.dominant.bean;

import com.amkj.dmsh.base.BaseTimeEntity;
import com.amkj.dmsh.constant.CommunalDetailBean;
import com.google.gson.annotations.SerializedName;

import java.util.List;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/9/19
 * version 3.1.6
 * class description:限时特惠商品详情
 */
public class PromotionProductDetailEntity extends BaseTimeEntity {

    /**
     * result : {"flashsaleInfo":[{"content":"<p>11123123<\/p>","type":"text"},{"content":"<p>12312312<\/p>","type":"text"},{"content":"<p>3123123123<\/p>","type":"text"}],"luckyMoney":[{"name":"jjyy","url":"ap"}],"marketPrice":"298.00","picUrl":"http://img.domolife.cn/2016-05-27_5748097e21b9d.jpg","thirdId":"534918771492","remind":false,"maxPrice":"0","endTime":"2017-11-01 23:59:59","id":1227,"startTime":"2016-07-18 20:00:00","commentNum":2,"price":"288.00","favor":false,"thirdUrl":"","favorNum":2,"itemBody":[{"content":"<p>日本超级难买到的红色蛇毒眼膜，由瑞典研制开发，结合了世界上最先进的生物技术，不含色素、不含香料。提取毒蛇身上提取的筋肉收缩抑制酵素，配合高丽人參， 胶原蛋白，植物海藻配合胎盘素、熊果素成分制成，具有使皮肤提升紧致，透亮光泽的功效。<\/p>","type":"text"},{"content":"<p>这款新版干细胞蛇毒眼膜添加人体脂肪自来干细胞抽取液HAS，美白滋润的同时还能修复肌肤，强力对抗眼纹、眼袋、黑眼圈、暗沉、干燥等眼部问题。还可以作为消除法令纹使用。因为蛇毒眼膜精华比较浓，首次使用的话可能会有刺刺的感觉，有的人觉得有些熏的难受的情况发生。但这是因为精华在发挥功效，纯属正常情况，所以不需要担心。<\/p>","type":"text"}],"name":"spa红色蛇毒眼膜60片","subtitle":"眼膜界的黑马，解决眼部五大问题","images":"\"\",\"http://o6wxayr69.bkt.clouddn.com/2016-05-27_5748097e21b9d.jpg\",\"http://o6wxayr69.bkt.clouddn.com/2016-05-27_5748098879e6d.jpg\",\"http://o6wxayr69.bkt.clouddn.com/2016-05-27_5748099b2c056.jpg\",\"http://o6wxayr69.bkt.clouddn.com/2016-05-27_5748099ba13f8.jpg\",\"http://o6wxayr69.bkt.clouddn.com/2016-05-27_5748099caa952.jpg\",\"http://o6wxayr69.bkt.clouddn.com/2016-05-27_574809a138a2a.jpg\"","quantity":1}
     * currentTime : 2017-08-02 12-08-0
     */

    @SerializedName("result")
    private PromotionProductDetailBean promotionProductDetailBean;

    public PromotionProductDetailBean getPromotionProductDetailBean() {
        return promotionProductDetailBean;
    }

    public void setPromotionProductDetailBean(PromotionProductDetailBean promotionProductDetailBean) {
        this.promotionProductDetailBean = promotionProductDetailBean;
    }

    public static class PromotionProductDetailBean {
        /**
         * flashsaleInfo : [{"content":"<p>11123123<\/p>","type":"text"},{"content":"<p>12312312<\/p>","type":"text"},{"content":"<p>3123123123<\/p>","type":"text"}]
         * luckyMoney : [{"name":"jjyy","url":"ap"}]
         * marketPrice : 298.00
         * picUrl : http://img.domolife.cn/2016-05-27_5748097e21b9d.jpg
         * thirdId : 534918771492
         * remind : false
         * maxPrice : 0
         * endTime : 2017-11-01 23:59:59
         * id : 1227
         * startTime : 2016-07-18 20:00:00
         * commentNum : 2
         * price : 288.00
         * favor : false
         * thirdUrl :
         * favorNum : 2
         * itemBody : [{"content":"<p>日本超级难买到的红色蛇毒眼膜，由瑞典研制开发，结合了世界上最先进的生物技术，不含色素、不含香料。提取毒蛇身上提取的筋肉收缩抑制酵素，配合高丽人參， 胶原蛋白，植物海藻配合胎盘素、熊果素成分制成，具有使皮肤提升紧致，透亮光泽的功效。<\/p>","type":"text"},{"content":"<p>这款新版干细胞蛇毒眼膜添加人体脂肪自来干细胞抽取液HAS，美白滋润的同时还能修复肌肤，强力对抗眼纹、眼袋、黑眼圈、暗沉、干燥等眼部问题。还可以作为消除法令纹使用。因为蛇毒眼膜精华比较浓，首次使用的话可能会有刺刺的感觉，有的人觉得有些熏的难受的情况发生。但这是因为精华在发挥功效，纯属正常情况，所以不需要担心。<\/p>","type":"text"}]
         * name : spa红色蛇毒眼膜60片
         * subtitle : 眼膜界的黑马，解决眼部五大问题
         * images : "","http://o6wxayr69.bkt.clouddn.com/2016-05-27_5748097e21b9d.jpg","http://o6wxayr69.bkt.clouddn.com/2016-05-27_5748098879e6d.jpg","http://o6wxayr69.bkt.clouddn.com/2016-05-27_5748099b2c056.jpg","http://o6wxayr69.bkt.clouddn.com/2016-05-27_5748099ba13f8.jpg","http://o6wxayr69.bkt.clouddn.com/2016-05-27_5748099caa952.jpg","http://o6wxayr69.bkt.clouddn.com/2016-05-27_574809a138a2a.jpg"
         * quantity : 1
         */

        private String marketPrice;
        private String picUrl;
        private String thirdId;
        @SerializedName("isRemind")
        private boolean remind;
        private int skimEnable;
        private String maxPrice;
        private String endTime;
        private int id;
        private String startTime;
        private int commentNum;
        private String price;
        @SerializedName("isFavor")
        private boolean favor;
        private String thirdUrl;
        private int favorNum;
        private String name;
        private String subtitle;
        private String images;
        private int quantity;
        private int isTaobao;
        private String waterRemark;
        //        参团点击数
        private String flashBuyClickCount;
        //        售前内容
        @SerializedName("flashsaleInfo")
        private List<CommunalDetailBean> flashsaleInfoList;
        @SerializedName("luckyMoney")
        private List<LuckyMoneyBean> luckyMoneyList;
        @SerializedName("itemBody")
        private List<CommunalDetailBean> itemBodyList;
        //        自定义参数
        private long addSecond;

        public String getMarketPrice() {
            return marketPrice;
        }

        public void setMarketPrice(String marketPrice) {
            this.marketPrice = marketPrice;
        }

        public int getSkimEnable() {
            return skimEnable;
        }

        public void setSkimEnable(int skimEnable) {
            this.skimEnable = skimEnable;
        }

        public long getAddSecond() {
            return addSecond;
        }

        public void setAddSecond(long addSecond) {
            this.addSecond = addSecond;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public String getThirdId() {
            return thirdId;
        }

        public void setThirdId(String thirdId) {
            this.thirdId = thirdId;
        }

        public String getWaterRemark() {
            return waterRemark;
        }

        public void setWaterRemark(String waterRemark) {
            this.waterRemark = waterRemark;
        }

        public String getMaxPrice() {
            return maxPrice;
        }

        public void setMaxPrice(String maxPrice) {
            this.maxPrice = maxPrice;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public int getCommentNum() {
            return commentNum;
        }

        public void setCommentNum(int commentNum) {
            this.commentNum = commentNum;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public boolean isRemind() {
            return remind;
        }

        public void setRemind(boolean remind) {
            this.remind = remind;
        }

        public boolean isFavor() {
            return favor;
        }

        public void setFavor(boolean favor) {
            this.favor = favor;
        }

        public String getThirdUrl() {
            return thirdUrl;
        }

        public void setThirdUrl(String thirdUrl) {
            this.thirdUrl = thirdUrl;
        }

        public int getFavorNum() {
            return favorNum;
        }

        public void setFavorNum(int favorNum) {
            this.favorNum = favorNum;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
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

        public int getIsTaobao() {
            return isTaobao;
        }

        public void setIsTaobao(int isTaobao) {
            this.isTaobao = isTaobao;
        }

        public String getFlashBuyClickCount() {
            return flashBuyClickCount;
        }

        public void setFlashBuyClickCount(String flashBuyClickCount) {
            this.flashBuyClickCount = flashBuyClickCount;
        }

        public List<CommunalDetailBean> getFlashsaleInfoList() {
            return flashsaleInfoList;
        }

        public void setFlashsaleInfoList(List<CommunalDetailBean> flashsaleInfoList) {
            this.flashsaleInfoList = flashsaleInfoList;
        }

        public List<LuckyMoneyBean> getLuckyMoneyList() {
            return luckyMoneyList;
        }

        public void setLuckyMoneyList(List<LuckyMoneyBean> luckyMoneyList) {
            this.luckyMoneyList = luckyMoneyList;
        }

        public List<CommunalDetailBean> getItemBodyList() {
            return itemBodyList;
        }

        public void setItemBodyList(List<CommunalDetailBean> itemBodyList) {
            this.itemBodyList = itemBodyList;
        }

        public static class LuckyMoneyBean {
            /**
             * name : jjyy
             * url : ap
             */

            private String name;
            private String url;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
