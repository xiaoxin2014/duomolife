package com.amkj.dmsh;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/4/28
 * version 1.0
 * class description:请输入类描述
 */
public class TotalActionEntity {

    /**
     * result : [{"name":"首页","id":1},{"name":"签到","id":2},{"name":"积分明细","id":3},{"name":"热销单品","id":4},{"name":"分享赚","id":5},{"name":"抽奖","id":6},{"name":"每周优选","id":7},{"name":"领券中心","id":8},{"name":"每日优惠","id":9},{"name":"文章详情","id":10},{"name":"消息中心","id":2000},{"name":"订单消息","id":2001},{"name":"通知消息","id":2002},{"name":"活动消息","id":2003},{"name":"评论消息","id":2004},{"name":"赞消息","id":2005},{"name":"良品","id":3000},{"name":"分类","id":3001},{"name":"分类详情","id":3002},{"name":"购物车","id":3003},{"name":"自营商品详情","id":3004},{"name":"多么拼团","id":3005},{"name":"我的拼团","id":3006},{"name":"全部拼团","id":3007},{"name":"香港直邮-自定义专区","id":3500},{"name":"福利社","id":3501},{"name":"新品专区","id":3502},{"name":"生活好物-自定义专区","id":3503},{"name":"美食专区-自定义专区","id":3504},{"name":"新人专区","id":3505},{"name":"多么定制","id":3506},{"name":"优惠专区","id":3507},{"name":"好物专区","id":3508},{"name":"活动专区","id":3509},{"name":"必买清单","id":3510},{"name":"发现","id":4000},{"name":"话题帖子列表","id":4001},{"name":"话题详情","id":4002},{"name":"标签详情","id":4003},{"name":"发布","id":4004},{"name":"帖子详情","id":4005},{"name":"限时特惠","id":5000},{"name":"淘宝商品详情","id":5001},{"name":"主题团详情","id":5002},{"name":"我","id":6000},{"name":"我的关注","id":6001},{"name":"我的粉丝","id":6002},{"name":"设置","id":6003},{"name":"个人信息","id":6004},{"name":"个人主页背景","id":6005},{"name":"主页背景更换","id":6006},{"name":"昵称修改","id":6007},{"name":"兴趣爱好","id":6008},{"name":"账户安全","id":6009},{"name":"绑定手机","id":6010},{"name":"修改密码","id":6011},{"name":"消息推送设置","id":6012},{"name":"地址新建编辑","id":6013},{"name":"地址选择查看","id":6014},{"name":"登录界面","id":6015},{"name":"注册界面","id":6016},{"name":"地址选择","id":6017},{"name":"地址新增编辑","id":6018},{"name":"意见反馈","id":6019},{"name":"关于多么生活","id":6020},{"name":"我的帖子","id":6021},{"name":"购物车","id":6022},{"name":"优惠券","id":6023},{"name":"秒杀提醒","id":6024},{"name":"购物车","id":6025},{"name":"收藏商品","id":6026},{"name":"收藏内容","id":6027},{"name":"收藏帖子","id":6028},{"name":"收藏专题","id":6029},{"name":"收藏话题","id":6030},{"name":"多么订单","id":7000},{"name":"全部订单","id":7001},{"name":"待付款订单","id":7002},{"name":"待发货订单","id":7003},{"name":"待收货订单","id":7004},{"name":"售后退款订单","id":7005},{"name":"订单详情","id":7006},{"name":"退款详情","id":7007},{"name":"发票详情","id":7008},{"name":"订单结算","id":7009},{"name":"售后详情","id":7010},{"name":"积分订单","id":8000},{"name":"积分商品详情","id":8001},{"name":"","id":8002},{"name":"搜索","id":9000},{"name":"多么留言","id":9001},{"name":"搜索-商品","id":9002},{"name":"搜索-专题","id":9003},{"name":"搜索-帖子","id":9004},{"name":"搜索-话题","id":9005},{"name":"搜索-用户","id":9006},{"name":"绑定手机","id":20000},{"name":"关联账号","id":20001},{"name":"迁移账号","id":20002}]
     * code : 01
     * msg : 请求成功
     */

    private String code;
    private String msg;
    @SerializedName("result")
    private List<TotalActionBean> totalActionList;

    public static TotalActionEntity objectFromData(String str) {

        return new Gson().fromJson(str, TotalActionEntity.class);
    }

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

    public List<TotalActionBean> getTotalActionList() {
        return totalActionList;
    }

    public void setTotalActionList(List<TotalActionBean> totalActionList) {
        this.totalActionList = totalActionList;
    }

    public static class TotalActionBean {
        /**
         * name : 首页
         * id : 1
         */

        private String name;
        private int id;

        public static TotalActionBean objectFromData(String str) {

            return new Gson().fromJson(str, TotalActionBean.class);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
