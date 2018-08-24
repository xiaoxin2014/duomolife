package com.amkj.dmsh.homepage.bean;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/7/30
 * version 3.1.5
 * class description:积分夺宝领取
 */
public class IntegralLotteryAwardGetEntity {

    /**
     * code : 01
     * msg : 请求成功
     * sysTime : 2018-07-30 15:40:05
     * id : 2
     * activityCode : 2018072101
     * activityStatus : 上架
     * activityId : 2
     * image : http://image.domolife.cn/platform/TCsSKc6nkc1532154235910.png
     * prizeName : 我是已结束的抽奖
     * status : 3
     * StatusStr : 已兑奖
     * name : 收货人
     * mobile : 13838384388
     * address : 地址
     * logisticsname : 全峰快递
     * logisticsno : jjefjwefjkfjksdjfjksdfjsd
     * addressId : 0
     * statusStr : 已兑奖
     */

    private String code;
    private String msg;
    private int id;
    private String activityCode;
    private int activityId;
    private String image;
    private String prizeName;
    private int status;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
