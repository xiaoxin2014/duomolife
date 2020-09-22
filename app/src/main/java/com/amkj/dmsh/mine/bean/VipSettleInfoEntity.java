package com.amkj.dmsh.mine.bean;

import com.amkj.dmsh.base.BaseEntity;

import java.util.List;

/**
 * Created by xiaoxin on 2020/7/28
 * Version:v4.7.0
 * ClassDescription :会员购买结算信息实体类
 */
public class VipSettleInfoEntity extends BaseEntity {

    /**
     * code : 01
     * msg : 请求成功
     * sysTime : 2020-07-28 20:00:30
     * result : {"remark":"友情提示：会员服务购买后不支持退款","cardList":[{"cardId":"1","cardName":"年卡","marketPrice":"￥199","price":"￥1/天","marketPriceMsg":"限时抢购减198元","priceMsg":"仅0.1元/天","maxCountGift":"1","giftList":[{"id":"1","productName":"纽西之谜温泉水咋弹冻膜 夜间补水保湿睡眠面膜 7粒","productImg":"http://image.domolife.cn/platform/20200716/20200716140951529.jpeg"},{"id":"2","productName":"日本Hakubaku 无盐素面 180","productImg":"http://image.domolife.cn/platform/20200605/20200605154758590.jpeg"},{"id":"3","productName":"丝滑羽毛纱可爱小鲸鱼纸抽套","productImg":"http://image.domolife.cn/platform/ry7eie4fNR1564973454461.jpg"}]},{"cardId":"2","cardName":"季卡","marketPrice":"￥60","price":"￥0.5/天","marketPriceMsg":"限时抢购减59.5元","priceMsg":"仅0.1元/天","maxCountGift":"","giftList":[]}]}
     */

    private VipSettleInfoBean result;

    public VipSettleInfoBean getResult() {
        return result;
    }

    public void setResult(VipSettleInfoBean result) {
        this.result = result;
    }

    public static class VipSettleInfoBean {
        /**
         * remark : 友情提示：会员服务购买后不支持退款
         * cardList : [{"cardId":"1","cardName":"年卡","marketPrice":"￥199","price":"￥1/天","marketPriceMsg":"限时抢购减198元","priceMsg":"仅0.1元/天","maxCountGift":"1","giftList":[{"id":"1","productName":"纽西之谜温泉水咋弹冻膜 夜间补水保湿睡眠面膜 7粒","productImg":"http://image.domolife.cn/platform/20200716/20200716140951529.jpeg"},{"id":"2","productName":"日本Hakubaku 无盐素面 180","productImg":"http://image.domolife.cn/platform/20200605/20200605154758590.jpeg"},{"id":"3","productName":"丝滑羽毛纱可爱小鲸鱼纸抽套","productImg":"http://image.domolife.cn/platform/ry7eie4fNR1564973454461.jpg"}]},{"cardId":"2","cardName":"季卡","marketPrice":"￥60","price":"￥0.5/天","marketPriceMsg":"限时抢购减59.5元","priceMsg":"仅0.1元/天","maxCountGift":"","giftList":[]}]
         */

        private String remark;
        private List<CardListBean> cardList;

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public List<CardListBean> getCardList() {
            return cardList;
        }

        public void setCardList(List<CardListBean> cardList) {
            this.cardList = cardList;
        }

        public static class CardListBean {

            /**
             * cardId : 1
             * cardName : 年卡
             * marketPrice : ￥199
             * price : ￥1/天
             * marketPriceMsg : 限时抢购减198元
             * priceMsg : 仅0.1元/天
             * maxCountGift : 1
             * giftList : [{"id":"1","productName":"纽西之谜温泉水咋弹冻膜 夜间补水保湿睡眠面膜 7粒","productImg":"http://image.domolife.cn/platform/20200716/20200716140951529.jpeg"},{"id":"2","productName":"日本Hakubaku 无盐素面 180","productImg":"http://image.domolife.cn/platform/20200605/20200605154758590.jpeg"},{"id":"3","productName":"丝滑羽毛纱可爱小鲸鱼纸抽套","productImg":"http://image.domolife.cn/platform/ry7eie4fNR1564973454461.jpg"}]
             * couponInfo : {"userCouponId":"12877299","amount":"20"}
             */

            private String cardId;
            private String cardName;
            private String marketPrice;
            private String price;
            private String marketPriceMsg;
            private String priceMsg;
            private String payPrice;
            private String maxCountGift;
            private List<GiftListBean> giftList;
            private CouponInfoBean couponInfo;

            public CouponInfoBean getCouponInfo() {
                return couponInfo;
            }

            public void setCouponInfo(CouponInfoBean couponInfo) {
                this.couponInfo = couponInfo;
            }

            public String getPayPrice() {
                return payPrice;
            }

            public void setPayPrice(String payPrice) {
                this.payPrice = payPrice;
            }

            public String getCardId() {
                return cardId;
            }

            public void setCardId(String cardId) {
                this.cardId = cardId;
            }

            public String getCardName() {
                return cardName;
            }

            public void setCardName(String cardName) {
                this.cardName = cardName;
            }

            public String getMarketPrice() {
                return marketPrice;
            }

            public void setMarketPrice(String marketPrice) {
                this.marketPrice = marketPrice;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getMarketPriceMsg() {
                return marketPriceMsg;
            }

            public void setMarketPriceMsg(String marketPriceMsg) {
                this.marketPriceMsg = marketPriceMsg;
            }

            public String getPriceMsg() {
                return priceMsg;
            }

            public void setPriceMsg(String priceMsg) {
                this.priceMsg = priceMsg;
            }

            public String getMaxCountGift() {
                return maxCountGift;
            }

            public void setMaxCountGift(String maxCountGift) {
                this.maxCountGift = maxCountGift;
            }

            public List<GiftListBean> getGiftList() {
                return giftList;
            }

            public void setGiftList(List<GiftListBean> giftList) {
                this.giftList = giftList;
            }

            public static class GiftListBean {
                /**
                 * id : 1
                 * productName : 纽西之谜温泉水咋弹冻膜 夜间补水保湿睡眠面膜 7粒
                 * productImg : http://image.domolife.cn/platform/20200716/20200716140951529.jpeg
                 */

                private String id;
                private String productName;
                private String productImg;
                private boolean selected;

                public GiftListBean(String id, String productName, String productImg) {
                    this.id = id;
                    this.productName = productName;
                    this.productImg = productImg;
                }

                public boolean isSelected() {
                    return selected;
                }

                public void setSelected(boolean selected) {
                    this.selected = selected;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getProductName() {
                    return productName;
                }

                public void setProductName(String productName) {
                    this.productName = productName;
                }

                public String getProductImg() {
                    return productImg;
                }

                public void setProductImg(String productImg) {
                    this.productImg = productImg;
                }
            }

            public static class CouponInfoBean {
                /**
                 * userCouponId : 12877299
                 * amount : 20
                 */

                private String userCouponId;
                private String amount;

                public String getUserCouponId() {
                    return userCouponId;
                }

                public void setUserCouponId(String userCouponId) {
                    this.userCouponId = userCouponId;
                }

                public String getAmount() {
                    return amount;
                }

                public void setAmount(String amount) {
                    this.amount = amount;
                }
            }
        }
    }
}
