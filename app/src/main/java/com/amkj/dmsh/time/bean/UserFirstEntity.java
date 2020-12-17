package com.amkj.dmsh.time.bean;

import com.amkj.dmsh.base.BaseEntity;

import java.util.List;

/**
 * Created by xiaoxin on 2020/12/15
 * Version:v4.8.2
 * ClassDescription :新人首单0元购数据实体
 */
public class UserFirstEntity extends BaseEntity {

    /**
     * sysTime : 2020-12-15 09:38:18
     * minStartPrice : 0.01
     * result : [{"productName":"澳大利亚MacyMccoy撞色袖子打底衫","startAmount":"0.01","picUrl":"http://image.domolife.cn/platform/Yt7QsJjzFw1552390659045.jpg"},{"productName":"丝滑羽毛纱可爱小鲸鱼纸抽套","startAmount":"100","picUrl":"http://image.domolife.cn/platform/20191224/20191224152232598.png"}]
     */

    private String minStartPrice;
    private List<UserFirstBean> result;

    public String getMinStartPrice() {
        return minStartPrice;
    }

    public void setMinStartPrice(String minStartPrice) {
        this.minStartPrice = minStartPrice;
    }

    public List<UserFirstBean> getResult() {
        return result;
    }

    public void setResult(List<UserFirstBean> result) {
        this.result = result;
    }

    public static class UserFirstBean {
        /**
         * productName : 澳大利亚MacyMccoy撞色袖子打底衫
         * startAmount : 0.01
         * picUrl : http://image.domolife.cn/platform/Yt7QsJjzFw1552390659045.jpg
         */

        private String productName;
        private String startAmount;
        private String picUrl;

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getStartAmount() {
            return startAmount;
        }

        public void setStartAmount(String startAmount) {
            this.startAmount = startAmount;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }
    }
}
