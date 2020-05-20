package com.amkj.dmsh.message.adapter;

import com.amkj.dmsh.base.BaseEntity;
import com.amkj.dmsh.bean.CommunalDetailBean;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/5/19
 * version 3.1.3
 * class description:平台通知数据
 */
public class PlatformDataEntity extends BaseEntity{

    /**
     * platformDataBean : {"m_uid":87889,"utime":"2018-05-14 18:27:22.735","m_status":true,"ctime":"2018-05-14 18:27:22.735","description":[{"type":"text","content":"<p>平台通知平台通知平台通知<\/p>"},{"type":"text","content":"<p>平台通知<\/p>"},{"type":"text","content":"<p><br/><\/p>"},{"type":"text","content":"<p><br/><\/p>"}],"m_type":9999,"m_content":"平台通知平台通知平台通知","ptime":"2018-05-14 18:27:22.735","m_id":2943812}
     * msg : 请求成功
     * code : 01
     */
    @SerializedName("result")
    private PlatformDataBean platformDataBean;

    public static PlatformDataEntity objectFromData(String str) {

        return GsonUtils.fromJson(str, PlatformDataEntity.class);
    }

    public PlatformDataBean getPlatformDataBean() {
        return platformDataBean;
    }

    public void setPlatformDataBean(PlatformDataBean platformDataBean) {
        this.platformDataBean = platformDataBean;
    }

    public static class PlatformDataBean {
        /**
         * m_uid : 87889
         * utime : 2018-05-14 18:27:22.735
         * m_status : true
         * ctime : 2018-05-14 18:27:22.735
         * description : [{"type":"text","content":"<p>平台通知平台通知平台通知<\/p>"},{"type":"text","content":"<p>平台通知<\/p>"},{"type":"text","content":"<p><br/><\/p>"},{"type":"text","content":"<p><br/><\/p>"}]
         * m_type : 9999
         * m_content : 平台通知平台通知平台通知
         * ptime : 2018-05-14 18:27:22.735
         * m_id : 2943812
         */

        private int m_uid;
        private String utime;
        private boolean m_status;
        private String ctime;
        private int m_type;
        private String m_content;
        private String ptime;
        private int m_id;
        @SerializedName("description")
        private List<CommunalDetailBean> descriptionList;

        public static PlatformDataBean objectFromData(String str) {

            return GsonUtils.fromJson(str, PlatformDataBean.class);
        }

        public int getM_uid() {
            return m_uid;
        }

        public void setM_uid(int m_uid) {
            this.m_uid = m_uid;
        }

        public String getUtime() {
            return utime;
        }

        public void setUtime(String utime) {
            this.utime = utime;
        }

        public boolean isM_status() {
            return m_status;
        }

        public void setM_status(boolean m_status) {
            this.m_status = m_status;
        }

        public String getCtime() {
            return ctime;
        }

        public void setCtime(String ctime) {
            this.ctime = ctime;
        }

        public int getM_type() {
            return m_type;
        }

        public void setM_type(int m_type) {
            this.m_type = m_type;
        }

        public String getM_content() {
            return m_content;
        }

        public void setM_content(String m_content) {
            this.m_content = m_content;
        }

        public String getPtime() {
            return ptime;
        }

        public void setPtime(String ptime) {
            this.ptime = ptime;
        }

        public int getM_id() {
            return m_id;
        }

        public void setM_id(int m_id) {
            this.m_id = m_id;
        }

        public List<CommunalDetailBean> getDescriptionList() {
            return descriptionList;
        }

        public void setDescriptionList(List<CommunalDetailBean> descriptionList) {
            this.descriptionList = descriptionList;
        }
    }
}
