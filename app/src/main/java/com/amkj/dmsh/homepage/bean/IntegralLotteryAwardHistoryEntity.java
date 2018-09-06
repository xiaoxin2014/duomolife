package com.amkj.dmsh.homepage.bean;

import com.amkj.dmsh.base.BaseEntity;
import com.amkj.dmsh.homepage.bean.IntegralLotteryEntity.PreviousInfoBean;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/7/30
 * version 3.1.5
 * class description:往期夺宝
 */
public class IntegralLotteryAwardHistoryEntity extends BaseEntity{

    /**
     * code : 01
     * msg : 请求成功
     * sysTime : 2018-07-30 19:14:29
     * overLotteryInfoList : [{"id":1,"activityCode":"2018072001","activityStatus":"已结束","image":"http://image.domolife.cn/platform/Qi4tdnWXcF1532078469292.jpg","prizeName":"随缘","prizeNum":1,"startTime":"2018-07-01 00:00:00","endTime":"2018-07-28 00:00:00","lotteryCode":[],"winList":[{"winningCode":"2018072001105","nickName":"1314586****","avatar":""}],"winning":false},{"id":3,"activityCode":"2018072102","activityStatus":"已结束","image":"http://image.domolife.cn/platform/j8xAFByXta1532154249173.jpg","prizeName":"我还没开始","prizeNum":2,"startTime":"2018-07-26 00:00:00","endTime":"2018-07-27 00:00:00","lotteryCode":[],"winList":[{"winningCode":"2018072102101","nickName":"阿飞","avatar":"http://image.domolife.cn/iosRelease/20180522105324.jpg"}],"winning":false},{"id":2,"activityCode":"2018072101","activityStatus":"已结束","image":"http://image.domolife.cn/platform/TCsSKc6nkc1532154235910.png","prizeName":"我是已结束的抽奖","prizeNum":2,"startTime":"2018-07-01 00:00:00","endTime":"2018-07-02 00:00:00","lotteryCode":[],"winList":[{"winningCode":"2018072002101","nickName":"穆茨d'god like","avatar":"http://image.domolife.cn/201704280903427554142183.png"},{"winningCode":"2018072002102","nickName":"阿飞","avatar":"http://image.domolife.cn/iosRelease/20180522105324.jpg"}],"winning":false}]
     */
    private List<PreviousInfoBean> overLotteryInfoList;

    public List<PreviousInfoBean> getOverLotteryInfoList() {
        return overLotteryInfoList;
    }

    public void setOverLotteryInfoList(List<PreviousInfoBean> overLotteryInfoList) {
        this.overLotteryInfoList = overLotteryInfoList;
    }
}
