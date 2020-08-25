package com.amkj.dmsh.mine.bean;

import com.amkj.dmsh.base.BaseEntity;
import com.amkj.dmsh.find.bean.PostEntity;
import com.amkj.dmsh.user.bean.LikedProductBean;

import java.util.List;

/**
 * Created by xiaoxin on 2020/8/21
 * Version:v4.7.0
 */
public class ZeroReportDetailEntity extends BaseEntity {

    /**
     * sysTime : 2020-08-21 20:08:33
     * result : {"productInfo":{"productId":"18005","productName":"澳大利亚MacyMccoy撞色袖子打底衫","productImg":"http://image.domolife.cn/platform/Yt7QsJjzFw1552390659045.jpg","price":"251.00"},"reportList":[{"activityId":"","orderId":"2","content":"sfsdf","imgs":"http://image.domolife.cn/platform/20200820/20200820184822459.png,http://image.domolife.cn/platform/20200820/20200820184832568.jpeg","star":"5","likeCount":"2","isLike":"0","isCollect":"0","avatar":"http://image.domolife.cn/iosRelease/20200714163934.jpg","nickName":"剑辉","createTime":"2020-08-07 11:44:43"}]}
     */

    private ZeroReportDetailBean result;

    public ZeroReportDetailBean getResult() {
        return result;
    }

    public void setResult(ZeroReportDetailBean result) {
        this.result = result;
    }

    public static class ZeroReportDetailBean {
        /**
         * productInfo : {"productId":"18005","productName":"澳大利亚MacyMccoy撞色袖子打底衫","productImg":"http://image.domolife.cn/platform/Yt7QsJjzFw1552390659045.jpg","price":"251.00"}
         * reportList : [{"activityId":"","orderId":"2","content":"sfsdf","imgs":"http://image.domolife.cn/platform/20200820/20200820184822459.png,http://image.domolife.cn/platform/20200820/20200820184832568.jpeg","star":"5","likeCount":"2","isLike":"0","isCollect":"0","avatar":"http://image.domolife.cn/iosRelease/20200714163934.jpg","nickName":"剑辉","createTime":"2020-08-07 11:44:43"}]
         */

        private LikedProductBean productInfo;
        private List<PostEntity.PostBean> reportList;

        public LikedProductBean getProductInfo() {
            return productInfo;
        }

        public void setProductInfo(LikedProductBean productInfo) {
            this.productInfo = productInfo;
        }

        public List<PostEntity.PostBean> getReportList() {
            return reportList;
        }

        public void setReportList(List<PostEntity.PostBean> reportList) {
            this.reportList = reportList;
        }
    }
}
