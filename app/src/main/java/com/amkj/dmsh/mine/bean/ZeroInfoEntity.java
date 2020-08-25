package com.amkj.dmsh.mine.bean;

import com.amkj.dmsh.base.BaseTimeEntity;

/**
 * Created by xiaoxin on 2020/8/7
 * Version:v4.7.0
 * ClassDescription :0元试用活动信息实体类
 */
public class ZeroInfoEntity extends BaseTimeEntity {

    /**
     * sysTime : 2020-08-07 15:37:30
     * result : {"title":"0元试用","subtitle":"会员专享，0元试用","zeroInfo":{"productId":"17486","productName":"蜡笔小新手机卡扣式导航车载支架","subtitle":"","productImg":"http://image.domolife.cn/platform/20200806/20200806153914217.jpeg","count":"10","activityId":"1","marketPrice":"45"}}
     */

    private ResultBean result;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * title : 0元试用
         * subtitle : 会员专享，0元试用
         * zeroInfo : {"productId":"17486","productName":"蜡笔小新手机卡扣式导航车载支架","subtitle":"","productImg":"http://image.domolife.cn/platform/20200806/20200806153914217.jpeg","count":"10","activityId":"1","marketPrice":"45"}
         */

        private String title;
        private String subtitle;
        private ZeroInfoBean zeroInfo;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public ZeroInfoBean getZeroInfo() {
            return zeroInfo;
        }

        public void setZeroInfo(ZeroInfoBean zeroInfo) {
            this.zeroInfo = zeroInfo;
        }
    }
}
