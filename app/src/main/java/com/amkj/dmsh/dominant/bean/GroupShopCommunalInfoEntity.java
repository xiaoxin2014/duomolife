package com.amkj.dmsh.dominant.bean;

import com.amkj.dmsh.constant.CommunalDetailBean;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/6/17
 * class description:请输入类描述
 */

public class GroupShopCommunalInfoEntity {

    /**
     * result : {"gpRule":[{"type":"text","content":"<p>222<\/p>"}],"servicePromiseTitle":"保证正品,7天无理由退货","servicePromise":[{"type":"text","content":"<p>123<\/p>"},{"type":"text","content":"<p><br/><\/p>"},{"type":"text","content":"<p>测试测试<\/p>"},{"type":"text","content":"<p><br/><\/p>"},{"type":"text","content":"<p>测试123<\/p>"}]}
     * currentTime : 2017-06-17 11:30:09
     * msg : 请求成功
     * code : 01
     */

    @SerializedName("result")
    private GroupShopCommunalInfoBean groupShopCommunalInfoBean;
    private String currentTime;
    private String msg;
    private String code;

    public GroupShopCommunalInfoBean getGroupShopCommunalInfoBean() {
        return groupShopCommunalInfoBean;
    }

    public void setGroupShopCommunalInfoBean(GroupShopCommunalInfoBean groupShopCommunalInfoBean) {
        this.groupShopCommunalInfoBean = groupShopCommunalInfoBean;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static class GroupShopCommunalInfoBean {
        /**
         * gpRule : [{"type":"text","content":"<p>222<\/p>"}]
         * servicePromiseTitle : 保证正品,7天无理由退货
         * servicePromise : [{"type":"text","content":"<p>123<\/p>"},{"type":"text","content":"<p><br/><\/p>"},{"type":"text","content":"<p>测试测试<\/p>"},{"type":"text","content":"<p><br/><\/p>"},{"type":"text","content":"<p>测试123<\/p>"}]
         */

        private String servicePromiseTitle;
        private List<CommunalDetailBean> gpRule;
        private List<CommunalDetailBean> servicePromise;

        public String getServicePromiseTitle() {
            return servicePromiseTitle;
        }

        public void setServicePromiseTitle(String servicePromiseTitle) {
            this.servicePromiseTitle = servicePromiseTitle;
        }

        public List<CommunalDetailBean> getGpRule() {
            return gpRule;
        }

        public void setGpRule(List<CommunalDetailBean> gpRule) {
            this.gpRule = gpRule;
        }

        public List<CommunalDetailBean> getServicePromise() {
            return servicePromise;
        }

        public void setServicePromise(List<CommunalDetailBean> servicePromise) {
            this.servicePromise = servicePromise;
        }
    }
}
