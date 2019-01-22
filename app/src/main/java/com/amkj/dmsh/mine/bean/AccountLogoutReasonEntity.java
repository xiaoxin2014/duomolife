package com.amkj.dmsh.mine.bean;

import com.amkj.dmsh.base.BaseEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2019/1/22
 * version 3.2.0
 * class description:注销原因
 */
public class AccountLogoutReasonEntity extends BaseEntity{

    /**
     * code : 01
     * msg : 请求成功
     * sysTime : 2019-01-22 16:47:55
     * result : [{"id":1,"content":"安全/隐私顾虑"},{"id":2,"content":"这是多余的账户"},{"id":3,"content":"在多么生活购物不适合我"},{"id":4,"content":"其他"}]
     */
    @SerializedName("result")
    private List<AccountLogoutReasonBean> logoutReasonList;

    public List<AccountLogoutReasonBean> getLogoutReasonList() {
        return logoutReasonList;
    }

    public void setLogoutReasonList(List<AccountLogoutReasonBean> logoutReasonList) {
        this.logoutReasonList = logoutReasonList;
    }

    public static class AccountLogoutReasonBean {
        /**
         * id : 1
         * content : 安全/隐私顾虑
         */

        private int id;
        private String content;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
