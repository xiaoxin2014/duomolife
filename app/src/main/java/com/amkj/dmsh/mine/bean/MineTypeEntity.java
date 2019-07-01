package com.amkj.dmsh.mine.bean;

import com.amkj.dmsh.base.BaseTimeEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/11/28
 * version 3.1.9
 * class description:我-底栏
 */
public class MineTypeEntity extends BaseTimeEntity{

    /**
     * sysTime : 2018-11-28 17:48:35
     * updateTime : 2018-11-28 16:57:51
     * result : [{"sort":1,"name":"购物车","iosUrl":"app://DMLShoppingCartViewController","androidUrl":"app://ShopCarActivity","webUrl":"https://www.domolife.cn/m/template/order_template/cart.html","iconUrl":"http://image.domolife.cn/platform/4hZtyZzJPn1543390477783.png"},{"sort":2,"name":"优惠券列表","iosUrl":"app://DMLCheckMyCouponViewController","androidUrl":"app://DirectMyCouponActivityundefined","webUrl":"http://www.domolife.cn/m/template/order_template/coupon.htmlundefined","iconUrl":"http://image.domolife.cn/platform/QkfP8XxXwN1543390573742.png"},{"sort":3,"name":"分享赚","iosUrl":"http://www.domolife.cn/m/template/home/shareHome.html","androidUrl":"http://www.domolife.cn/m/template/home/shareHome.html","webUrl":"https://www.domolife.cn/m/template/home/shareHome.html","iconUrl":"http://image.domolife.cn/platform/i2B8yZ2zrS1543390631645.png"},{"sort":4,"name":"积分订单","iosUrl":"app://DMLDOPointsOrderViewController","androidUrl":"http://baidu.com","webUrl":"http://baidu.com","iconUrl":"http://image.domolife.cn/platform/3BZGZwJX4G1543391296709.png"},{"sort":5,"name":"我的提醒列表","iosUrl":"app://DMLRemindListViewController","androidUrl":"app://ShopTimeMyWarmActivity","webUrl":"http://baidu.com","iconUrl":"http://image.domolife.cn/platform/a8bTyJREe71543391373684.png"},{"sort":6,"name":"收藏商品","iosUrl":"app://DMLGoodsCollectViewController","androidUrl":"http://baidu.com","webUrl":"http://baidu.com","iconUrl":"http://image.domolife.cn/platform/dGDY5R4tCR1543391849192.png"},{"sort":7,"name":"收藏内容","iosUrl":"app://DMLOtherCollectViewController","androidUrl":"http://baidu.com","webUrl":"http://baidu.com","iconUrl":"http://image.domolife.cn/platform/c3XM4hd5eY1543391905439.png"},{"sort":8,"name":"客服","iosUrl":"app://MQChatViewManager","androidUrl":"app://ManagerServiceChat","webUrl":"http://baidu.com","iconUrl":"http://image.domolife.cn/platform/2YcCnndXTA1543391956950.png"},{"sort":9,"name":"客服","iosUrl":"app://MQChatViewManager","androidUrl":"app://ManagerServiceChat","webUrl":"http://baidu.com","iconUrl":"http://image.domolife.cn/platform/amEtwY4JEK1543392010606.png"}]
     */
    private String updateTime;
    @SerializedName("result")
    private List<MineTypeBean> mineTypeBeanList;

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public List<MineTypeBean> getMineTypeBeanList() {
        return mineTypeBeanList;
    }

    public void setMineTypeBeanList(List<MineTypeBean> mineTypeBeanList) {
        this.mineTypeBeanList = mineTypeBeanList;
    }

    public static class MineTypeBean {
        /**
         * sort : 1
         * name : 购物车
         * iosUrl : app://DMLShoppingCartViewController
         * androidUrl : app://ShopCarActivity
         * webUrl : https://www.domolife.cn/m/template/order_template/cart.html
         * iconUrl : http://image.domolife.cn/platform/4hZtyZzJPn1543390477783.png
         */
        private boolean isGetCartTip;//自定义参数，调GetCartTip接口时才需要更新购物车数量
        private String name;
        private String androidUrl;
        private String iconUrl;

        public boolean isGetCartTip() {
            return isGetCartTip;
        }

        public void setGetCartTip(boolean getCartTip) {
            isGetCartTip = getCartTip;
        }

        //        自定义参数 消息提醒
        private int mesCount;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAndroidUrl() {
            return androidUrl;
        }

        public void setAndroidUrl(String androidUrl) {
            this.androidUrl = androidUrl;
        }

        public String getIconUrl() {
            return iconUrl;
        }

        public void setIconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
        }

        public int getMesCount() {
            return mesCount;
        }

        public void setMesCount(int mesCount) {
            this.mesCount = mesCount;
        }
    }
}
