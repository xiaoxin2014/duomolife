package com.amkj.dmsh.shopdetails.bean;

import com.amkj.dmsh.base.BaseEntity;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import static com.amkj.dmsh.shopdetails.bean.DirectLogisticsPacketEntity.DirectLogisticsPacketBean.LogisticsEntity.LogisticsBean.ListBean;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/3/28
 * version 3.1.1
 * class description:维修物流信息
 */

public class DirectRepairLogisticsEntity extends BaseEntity{

    /**
     * directRepairLogisticsBean : {"repairReturnExpressCompany":"顺丰","logistics":[{"status":"在官网\"运单资料&签收图\",可查看签收人信息","time":"2018-03-25 11:39:36"},{"status":"已签收,感谢使用顺丰,期待再次为您服务","time":"2018-03-25 11:38:49"},{"status":"快件交给秦罗卫，正在派送途中（联系电话：13812966007）","time":"2018-03-25 10:36:01"},{"status":"快件交给孙亮，正在派送途中（联系电话：13222227911）","time":"2018-03-25 10:04:38"},{"status":"","time":"2018-03-25 09:19:22"},{"status":"正在派送途中,请您准备签收(派件人:孙亮,电话:13222227911)","time":"2018-03-25 09:19:15"},{"status":"快件到达 【苏州市常熟市练塘大道营业点】","time":"2018-03-25 09:10:05"},{"status":"快件在【苏州张家港集散中心】装车，已发往下一站","time":"2018-03-25 07:54:49"},{"status":"快件到达 【苏州张家港集散中心】","time":"2018-03-25 07:21:01"},{"status":"快件到达 【苏州吴中集散中心】","time":"2018-03-25 04:45:22"},{"status":"快件在【苏州吴中集散中心】装车，已发往 【苏州张家港集散中心】","time":"2018-03-25 04:45:22"},{"status":"快件在【苏州常熟中转场】装车，已发往 【苏州吴中集散中心】","time":"2018-03-24 22:47:17"},{"status":"快件到达 【苏州常熟中转场】","time":"2018-03-24 22:17:58"},{"status":"快件在【苏州昆太大客户营业部】装车，已发往下一站","time":"2018-03-24 17:52:11"},{"status":"顺丰速运 已收取快件","time":"2018-03-24 17:52:01"}],"repairReturnExpressNo":"457765726728","repairReturnExpressType":""}
     * msg : 请求成功
     * code : 01
     */
    @SerializedName("result")
    private DirectRepairLogisticsBean directRepairLogisticsBean;

    public static DirectRepairLogisticsEntity objectFromData(String str) {

        return new Gson().fromJson(str, DirectRepairLogisticsEntity.class);
    }

    public DirectRepairLogisticsBean getDirectRepairLogisticsBean() {
        return directRepairLogisticsBean;
    }

    public void setDirectRepairLogisticsBean(DirectRepairLogisticsBean directRepairLogisticsBean) {
        this.directRepairLogisticsBean = directRepairLogisticsBean;
    }

    public static class DirectRepairLogisticsBean {
        /**
         * repairReturnExpressCompany : 顺丰
         * logistics : [{"status":"在官网\"运单资料&签收图\",可查看签收人信息","time":"2018-03-25 11:39:36"},{"status":"已签收,感谢使用顺丰,期待再次为您服务","time":"2018-03-25 11:38:49"},{"status":"快件交给秦罗卫，正在派送途中（联系电话：13812966007）","time":"2018-03-25 10:36:01"},{"status":"快件交给孙亮，正在派送途中（联系电话：13222227911）","time":"2018-03-25 10:04:38"},{"status":"","time":"2018-03-25 09:19:22"},{"status":"正在派送途中,请您准备签收(派件人:孙亮,电话:13222227911)","time":"2018-03-25 09:19:15"},{"status":"快件到达 【苏州市常熟市练塘大道营业点】","time":"2018-03-25 09:10:05"},{"status":"快件在【苏州张家港集散中心】装车，已发往下一站","time":"2018-03-25 07:54:49"},{"status":"快件到达 【苏州张家港集散中心】","time":"2018-03-25 07:21:01"},{"status":"快件到达 【苏州吴中集散中心】","time":"2018-03-25 04:45:22"},{"status":"快件在【苏州吴中集散中心】装车，已发往 【苏州张家港集散中心】","time":"2018-03-25 04:45:22"},{"status":"快件在【苏州常熟中转场】装车，已发往 【苏州吴中集散中心】","time":"2018-03-24 22:47:17"},{"status":"快件到达 【苏州常熟中转场】","time":"2018-03-24 22:17:58"},{"status":"快件在【苏州昆太大客户营业部】装车，已发往下一站","time":"2018-03-24 17:52:11"},{"status":"顺丰速运 已收取快件","time":"2018-03-24 17:52:01"}]
         * repairReturnExpressNo : 457765726728
         * repairReturnExpressType :
         */

        private String repairReturnExpressCompany;
        private String repairReturnExpressNo;
        private String repairReturnExpressType;
        private List<ListBean> logistics;

        public static DirectRepairLogisticsBean objectFromData(String str) {

            return new Gson().fromJson(str, DirectRepairLogisticsBean.class);
        }

        public String getRepairReturnExpressCompany() {
            return repairReturnExpressCompany;
        }

        public void setRepairReturnExpressCompany(String repairReturnExpressCompany) {
            this.repairReturnExpressCompany = repairReturnExpressCompany;
        }

        public String getRepairReturnExpressNo() {
            return repairReturnExpressNo;
        }

        public void setRepairReturnExpressNo(String repairReturnExpressNo) {
            this.repairReturnExpressNo = repairReturnExpressNo;
        }

        public String getRepairReturnExpressType() {
            return repairReturnExpressType;
        }

        public void setRepairReturnExpressType(String repairReturnExpressType) {
            this.repairReturnExpressType = repairReturnExpressType;
        }

        public List<ListBean> getLogistics() {
            return logistics;
        }

        public void setLogistics(List<ListBean> logistics) {
            this.logistics = logistics;
        }
    }
}
