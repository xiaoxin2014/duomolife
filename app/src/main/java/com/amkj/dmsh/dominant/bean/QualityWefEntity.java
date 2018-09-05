package com.amkj.dmsh.dominant.bean;

import com.amkj.dmsh.base.BaseEntity;
import com.amkj.dmsh.constant.CommunalDetailBean;
import com.amkj.dmsh.dominant.bean.DmlSearchDetailEntity.DmlSearchDetailBean.ProductListBean;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/7/6
 * class description:请输入类描述
 */

public class QualityWefEntity extends BaseEntity{

    /**
     * result : {"picUrl":"http://img.domolife.cn/platform/FECRdfda3j.jpg","discription":"3232323232对方的身份v","created":"2017-02-25 16:14:05","subtitle":" 粉红趴","description":[{"type":"text","content":"<p>三毛曾经说过：\u201c读书多了，容颜自然改变，在气质里，在谈吐上，在胸襟的无涯，当然也可能显露在生活和文字里。\u201d阅读已成为不断提升自我的修养的一项重要课程，但是很多爱读书的人不见得喜欢买书，看完的书需要地方整理，出门带着又略嫌重，所以这周福利社，我们给大家带来的是这台史上最in的外挂阅读神器\u2014\u2014亚马逊kindle电子书。 &nbsp;第一代Kindle于2007年11月19日发布，瞬间掀起了一阵阅读狂潮，别看它轻薄小巧，却能装下上千本书，至今上市近10年，一代接一代的更新，让它在阅读界始终热度不减，一直占据着电子书老大的头把交椅。<\/p>"},{"type":"text","content":"<p><br/><\/p>"},{"type":"text","content":"<p><img src=\"http://img.domolife.cn/platfrom/20170703/1499066052493016182.jpg\" title=\"1499066052493016182.jpg\" alt=\"20170616170130597.jpg\"/><\/p>"},{"type":"text","content":"<p><br/><\/p>"},{"type":"goods","content":{"picUrl":"http://img.domolife.cn/platform/20170309/20170309095153446.jpg","marketPrice":"38.00","price":"23.00","name":" 蝴蝶恋iphone6苹果手机壳清新文艺花朵甜美软包壳","maxPrice":"23.00","id":5961,"itemTypeId":1}},{"type":"text","content":"<p><br/><\/p>"},{"type":"coupon","content":{"amount":"50.00","count":49981,"title":"新人礼包499-50","totalCount":49983,"mode":1,"picUrl":"http://img.domolife.cn/platform/MF3eP7GFAG1499065718958.jpg","newPirUrl":"http://img.domolife.cn/platform/MF3eP7GFAG1499065718958.jpg","useRange":0,"startFee":"499.00","startTime":"2017-02-21 09:43:33","id":10,"endTime":"2017-10-01 00:00:00","desc":""}}],"id":221,"title":"春暖花开 粉嫩起来"}
     * msg : 请求成功
     * code : 01
     */

    @SerializedName("result")
    private QualityWefBean qualityWefBean;

    public QualityWefBean getQualityWefBean() {
        return qualityWefBean;
    }

    public void setQualityWefBean(QualityWefBean qualityWefBean) {
        this.qualityWefBean = qualityWefBean;
    }

    public static class QualityWefBean {
        /**
         * picUrl : http://img.domolife.cn/platform/FECRdfda3j.jpg
         * discription : 3232323232对方的身份v
         * created : 2017-02-25 16:14:05
         * subtitle :  粉红趴
         * description : [{"type":"text","content":"<p>三毛曾经说过：\u201c读书多了，容颜自然改变，在气质里，在谈吐上，在胸襟的无涯，当然也可能显露在生活和文字里。\u201d阅读已成为不断提升自我的修养的一项重要课程，但是很多爱读书的人不见得喜欢买书，看完的书需要地方整理，出门带着又略嫌重，所以这周福利社，我们给大家带来的是这台史上最in的外挂阅读神器\u2014\u2014亚马逊kindle电子书。 &nbsp;第一代Kindle于2007年11月19日发布，瞬间掀起了一阵阅读狂潮，别看它轻薄小巧，却能装下上千本书，至今上市近10年，一代接一代的更新，让它在阅读界始终热度不减，一直占据着电子书老大的头把交椅。<\/p>"},{"type":"text","content":"<p><br/><\/p>"},{"type":"text","content":"<p><img src=\"http://img.domolife.cn/platfrom/20170703/1499066052493016182.jpg\" title=\"1499066052493016182.jpg\" alt=\"20170616170130597.jpg\"/><\/p>"},{"type":"text","content":"<p><br/><\/p>"},{"type":"goods","content":{"picUrl":"http://img.domolife.cn/platform/20170309/20170309095153446.jpg","marketPrice":"38.00","price":"23.00","name":" 蝴蝶恋iphone6苹果手机壳清新文艺花朵甜美软包壳","maxPrice":"23.00","id":5961,"itemTypeId":1}},{"type":"text","content":"<p><br/><\/p>"},{"type":"coupon","content":{"amount":"50.00","count":49981,"title":"新人礼包499-50","totalCount":49983,"mode":1,"picUrl":"http://img.domolife.cn/platform/MF3eP7GFAG1499065718958.jpg","newPirUrl":"http://img.domolife.cn/platform/MF3eP7GFAG1499065718958.jpg","useRange":0,"startFee":"499.00","startTime":"2017-02-21 09:43:33","id":10,"endTime":"2017-10-01 00:00:00","desc":""}}]
         * id : 221
         * title : 春暖花开 粉嫩起来
         */

        private String picUrl;
        private String created;
        private String subtitle;
        private int id;
        private String title;
        @SerializedName("description")
        private List<CommunalDetailBean> descriptionList;
        private List<ProductListBean> productList;


        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
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
