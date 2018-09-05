package com.amkj.dmsh.dominant.bean;

import com.amkj.dmsh.base.BaseEntity;
import com.amkj.dmsh.constant.CommunalDetailBean;
import com.amkj.dmsh.dominant.bean.DmlSearchDetailEntity.DmlSearchDetailBean.ProductListBean;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/7/10
 * class description:请输入类描述
 */

public class DmlOptimizedSelDetailEntity extends BaseEntity{

    /**
     * result : {"picUrl":"http://img.domolife.cn/platform/5TSj8KDC2p1499259278069.jpg","subtitle":"多么优选2副标题","description":[{"type":"text","content":"<p>成都市 &nbsp;<\/p>"},{"type":"text","content":"<p><br/><\/p>"},{"type":"goods","content":{"picUrl":"http://img.domolife.cn/platform/20170309/20170309102440006.jpg","marketPrice":"49.00","price":"35.00","name":"可爱河马北极熊苹果手机壳软壳 带支架","newPicUrl":"http://img.domolife.cn/platform/C6ZdcZp8YH1499400239765.jpg","maxPrice":"35.00","id":5964,"itemTypeId":1}},{"type":"text","content":"<p><br/><\/p>"},{"type":"text","content":"<p><br/><\/p>"},{"type":"coupon","content":{"amount":"45.00","count":999998,"title":"阿斯顿","totalCount":1000000,"mode":1,"picUrl":"http://img.domolife.cn/platform/Qxpr8G6maG.jpg","newPirUrl":"http://img.domolife.cn/platform/Qxpr8G6maG.jpg","useRange":0,"startFee":"10.00","startTime":"2017-03-30 00:00:00","id":17,"endTime":"2018-04-28 00:00:00","desc":""}}],"rank":0,"id":3,"title":"多么优选2主标题","tid":1,"status":1}
     * msg : 请求成功
     * code : 01
     */

    @SerializedName("result")
    private DmlOptimizedSelDetailBean dmlOptimizedSelDetailBean;

    public DmlOptimizedSelDetailBean getDmlOptimizedSelDetailBean() {
        return dmlOptimizedSelDetailBean;
    }

    public void setDmlOptimizedSelDetailBean(DmlOptimizedSelDetailBean dmlOptimizedSelDetailBean) {
        this.dmlOptimizedSelDetailBean = dmlOptimizedSelDetailBean;
    }

    public static class DmlOptimizedSelDetailBean {
        /**
         * picUrl : http://img.domolife.cn/platform/5TSj8KDC2p1499259278069.jpg
         * subtitle : 多么优选2副标题
         * description : [{"type":"text","content":"<p>成都市 &nbsp;<\/p>"},{"type":"text","content":"<p><br/><\/p>"},{"type":"goods","content":{"picUrl":"http://img.domolife.cn/platform/20170309/20170309102440006.jpg","marketPrice":"49.00","price":"35.00","name":"可爱河马北极熊苹果手机壳软壳 带支架","newPicUrl":"http://img.domolife.cn/platform/C6ZdcZp8YH1499400239765.jpg","maxPrice":"35.00","id":5964,"itemTypeId":1}},{"type":"text","content":"<p><br/><\/p>"},{"type":"text","content":"<p><br/><\/p>"},{"type":"coupon","content":{"amount":"45.00","count":999998,"title":"阿斯顿","totalCount":1000000,"mode":1,"picUrl":"http://img.domolife.cn/platform/Qxpr8G6maG.jpg","newPirUrl":"http://img.domolife.cn/platform/Qxpr8G6maG.jpg","useRange":0,"startFee":"10.00","startTime":"2017-03-30 00:00:00","id":17,"endTime":"2018-04-28 00:00:00","desc":""}}]
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
        @SerializedName("description")
        private List<CommunalDetailBean> descriptionList;
        private List<ProductListBean> productList;

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

        public List<CommunalDetailBean> getDescriptionList() {
            return descriptionList;
        }

        public void setDescriptionList(List<CommunalDetailBean> descriptionList) {
            this.descriptionList = descriptionList;
        }

        public List<ProductListBean> getProductList() {
            return productList;
        }

        public void setProductList(List<ProductListBean> productList) {
            this.productList = productList;
        }
    }
}
