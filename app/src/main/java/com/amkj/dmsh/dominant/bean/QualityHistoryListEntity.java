package com.amkj.dmsh.dominant.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/10/10
 * class description:历史清单
 */

public class QualityHistoryListEntity {

    /**
     * result : [{"coverImgUrl":"http://image.domolife.cn/platform/kZ337Ttk4x1505994839741.jpg","name":"名称","id":3}]
     * msg : 请求成功
     * code : 01
     */

    private String msg;
    private String code;
    @SerializedName("result")
    private List<QualityHistoryListBean> qualityHistoryListBeanList;

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

    public List<QualityHistoryListBean> getQualityHistoryListBeanList() {
        return qualityHistoryListBeanList;
    }

    public void setQualityHistoryListBeanList(List<QualityHistoryListBean> qualityHistoryListBeanList) {
        this.qualityHistoryListBeanList = qualityHistoryListBeanList;
    }

    public static class QualityHistoryListBean {
        /**
         * coverImgUrl : http://image.domolife.cn/platform/kZ337Ttk4x1505994839741.jpg
         * name : 名称
         * id : 3
         */

        private String picUrl;
        private String coverImgUrl;
        private String name;
        private String title;
        private int id;

        public String getCoverImgUrl() {
            return coverImgUrl;
        }

        public void setCoverImgUrl(String coverImgUrl) {
            this.coverImgUrl = coverImgUrl;
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

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
