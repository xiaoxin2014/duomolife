package com.amkj.dmsh.mine.bean;

import com.amkj.dmsh.base.BaseTimeEntity;

import java.util.List;

/**
 * Created by xiaoxin on 2020/8/11
 * Version:v4.7.0
 */
public class ZeroListEntity extends BaseTimeEntity {

    /**
     * sysTime : 2020-08-11 15:18:37
     * result : {"currentActivityList":[{"endTime":"2020-08-21 00:00:00","goodsList":[{"productId":"17486","productName":"蜡笔小新手机卡扣式导航车载支架","subtitle":"","productImg":"http://image.domolife.cn/platform/20200806/20200806153914217.jpeg","count":"10","activityId":"1","marketPrice":"45"}]}],"overdueGoodsList":[{"productId":"17486","productName":"蜡笔小新手机卡扣式导航车载支架","subtitle":"","productImg":"","count":"20","activityId":"2","marketPrice":"45"}]}
     */

    private ZeroListBean result;

    public ZeroListBean getResult() {
        return result;
    }

    public void setResult(ZeroListBean result) {
        this.result = result;
    }

    public static class ZeroListBean {
        private List<CurrentActivityListBean> currentActivityList;
        private List<ZeroInfoBean> overdueGoodsList;

        public List<CurrentActivityListBean> getCurrentActivityList() {
            return currentActivityList;
        }

        public void setCurrentActivityList(List<CurrentActivityListBean> currentActivityList) {
            this.currentActivityList = currentActivityList;
        }

        public List<ZeroInfoBean> getOverdueGoodsList() {
            return overdueGoodsList;
        }

        public void setOverdueGoodsList(List<ZeroInfoBean> overdueGoodsList) {
            this.overdueGoodsList = overdueGoodsList;
        }

        public static class CurrentActivityListBean {
            /**
             * endTime : 2020-08-21 00:00:00
             * goodsList : [{"productId":"17486","productName":"蜡笔小新手机卡扣式导航车载支架","subtitle":"","productImg":"http://image.domolife.cn/platform/20200806/20200806153914217.jpeg","count":"10","activityId":"1","marketPrice":"45"}]
             */

            private String endTime;
            private List<ZeroInfoBean> goodsList;

            public String getEndTime() {
                return endTime;
            }

            public void setEndTime(String endTime) {
                this.endTime = endTime;
            }

            public List<ZeroInfoBean> getGoodsList() {
                return goodsList;
            }

            public void setGoodsList(List<ZeroInfoBean> goodsList) {
                this.goodsList = goodsList;
            }
        }
    }
}
