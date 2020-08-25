package com.amkj.dmsh.mine.bean;

import com.amkj.dmsh.base.BaseEntity;
import com.amkj.dmsh.shopdetails.bean.PriceInfoBean;

import java.util.List;

/**
 * Created by xiaoxin on 2020/8/21
 * Version:v4.7.0
 */
public class ZeroWriteEntity extends BaseEntity {


    /**
     * sysTime : 2020-08-21 15:14:39
     * result : {"zeroGoodsInfo":{"productId":"18005","productName":"澳大利亚MacyMccoy撞色袖子打底衫","subtitle":"好就完事","productImg":"http://image.domolife.cn/platform/20200821/20200821114905818.jpeg","count":"1","activityId":"3","marketPrice":"29900"},"priceInfoList":[{"name":"商品总额","color":"#000000","priceText":"￥299"},{"name":"试用商品","color":"#000000","priceText":"-￥299"},{"name":"保证金","color":"#000000","priceText":"￥10"},{"name":"实付","color":"#000000","priceText":"￥10"}]}
     */

    private ZeroWriteBean result;


    public ZeroWriteBean getResult() {
        return result;
    }

    public void setResult(ZeroWriteBean result) {
        this.result = result;
    }

    public static class ZeroWriteBean {
        /**
         * zeroGoodsInfo : {"productId":"18005","productName":"澳大利亚MacyMccoy撞色袖子打底衫","subtitle":"好就完事","productImg":"http://image.domolife.cn/platform/20200821/20200821114905818.jpeg","count":"1","activityId":"3","marketPrice":"29900"}
         * priceInfoList : [{"name":"商品总额","color":"#000000","priceText":"￥299"},{"name":"试用商品","color":"#000000","priceText":"-￥299"},{"name":"保证金","color":"#000000","priceText":"￥10"},{"name":"实付","color":"#000000","priceText":"￥10"}]
         */

        private ZeroGoodsInfoBean zeroGoodsInfo;
        private List<PriceInfoBean> priceInfoList;

        public ZeroGoodsInfoBean getZeroGoodsInfo() {
            return zeroGoodsInfo;
        }

        public void setZeroGoodsInfo(ZeroGoodsInfoBean zeroGoodsInfo) {
            this.zeroGoodsInfo = zeroGoodsInfo;
        }

        public List<PriceInfoBean> getPriceInfoList() {
            return priceInfoList;
        }

        public void setPriceInfoList(List<PriceInfoBean> priceInfoList) {
            this.priceInfoList = priceInfoList;
        }
    }
}
