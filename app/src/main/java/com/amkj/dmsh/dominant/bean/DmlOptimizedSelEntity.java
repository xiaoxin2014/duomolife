package com.amkj.dmsh.dominant.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/7/10
 * class description:请输入类描述
 */

public class DmlOptimizedSelEntity {

    /**
     * result : [{"picUrl":"http://img.domolife.cn/platform/5TSj8KDC2p1499259278069.jpg","subtitle":"多么优选2副标题","rank":0,"id":3,"title":"多么优选2主标题","tid":1,"status":1},{"picUrl":"http://img.domolife.cn/platform/XWtiNhefiC1499254463735.jpg","subtitle":"多么优选副标题","rank":1,"id":2,"title":"多么优选主标题","tid":1,"status":1},{"picUrl":"http://img.domolife.cn/platform/6hNdh2xSp61499313141775.jpg","subtitle":"副标题3","rank":2,"id":4,"title":"主标题3","tid":1,"status":1}]
     * msg : 请求成功
     * code : 01
     */

    private String msg;
    private String code;
    private String title;
    @SerializedName("result")
    private List<DmlOptimizedSelBean> dmlOptimizedSelList;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<DmlOptimizedSelBean> getDmlOptimizedSelList() {
        return dmlOptimizedSelList;
    }

    public void setDmlOptimizedSelList(List<DmlOptimizedSelBean> dmlOptimizedSelList) {
        this.dmlOptimizedSelList = dmlOptimizedSelList;
    }

    public static class DmlOptimizedSelBean {
        /**
         * picUrl : http://img.domolife.cn/platform/5TSj8KDC2p1499259278069.jpg
         * subtitle : 多么优选2副标题
         * rank : 0
         * id : 3
         * title : 多么优选2主标题
         * tid : 1
         * status : 1
         */

        private String picUrl;
        private String subtitle;
        private int rank;
        private int id;
        private String title;
        private int tid;
        private int status;

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getTid() {
            return tid;
        }

        public void setTid(int tid) {
            this.tid = tid;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
