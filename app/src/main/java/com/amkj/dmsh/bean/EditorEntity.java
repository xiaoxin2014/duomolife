package com.amkj.dmsh.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.amkj.dmsh.base.BaseEntity;
import com.amkj.dmsh.constant.CommunalDetailBean;
import com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean;

import java.util.ArrayList;
import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * Created by xiaoxin on 2019/3/20 0020
 * ClassDescription :
 */
public class EditorEntity extends BaseEntity {


    /**
     * sysTime : 2019-04-03 19:19:37
     * title : #小编精选#
     * description : 小编每天会看到同事找回来的各种各样的宝贝，真想推荐给大家，特开此专栏，每日一更精选好物，欢迎订阅
     * isSubscribe : 0
     * score : 50
     * result : [{"id":20,"title":"短信营销优化","content":[{"picUrl":"http://image.domolife.cn/platform/f75pJ3CT3M1554278369158.jpg","id":"14132","type":"pictureGoods"}],"likeNum":0,"messageCount":0,"createTime":"2019-04-03 15:59:45.0","publishTime":"2019-04-03 15:59:54.0","isFavor":0,"coverImg":"http://image.domolife.cn/platform/mEF8YBEJHG1554278343968.jpg","attachProductList":[{"productId":15871,"productName":"Let's diet恒温蕾丝内衣","productImg":"http://image.domolife.cn/platform/RQH35GYifW1539334557372.jpg"}]},{"id":19,"title":"标题","content":[{"picUrl":"http://image.domolife.cn/platform/8x5Nij6tY61554187110824.jpg","id":"6968","type":"pictureGoods"},{"picUrl":"http://image.domolife.cn/platform/ZZyDGKJCkJ1554187134471.png","id":"6968","type":"pictureGoods"},{"type":"text","content":"<p><br/><\/p>"},{"type":"text","content":"<p>\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014上面是商品，下面是图片\u2014\u2014\u2014\u2014<\/p>"},{"type":"text","content":"<p><img src=\"http://image.domolife.cn/platfrom/20190402/1554187173750044043.png\" title=\"1554187173750044043.png\" alt=\"极米2.png\"/><\/p>"}],"likeNum":1,"messageCount":0,"createTime":"2019-04-02 14:39:44.0","publishTime":"2019-04-02 14:40:14.0","isFavor":0,"coverImg":"http://image.domolife.cn/platform/zB6tjZdpjC1554186835828.png","attachProductList":[{"productId":6968,"productName":"ERGO CHEF 全自动榨汁机My Juicer S三代（两个600ml杯子）","productImg":"http://image.domolife.cn/platform/20180423/20180423153958456.jpg"}]},{"id":18,"title":"洗面奶","content":[{"picUrl":"http://image.domolife.cn/platform/kswXCGkPMx1554177065565.png","id":"7062","type":"pictureGoods"}],"likeNum":0,"messageCount":0,"createTime":"2019-04-02 11:51:16.0","publishTime":"2019-04-02 11:51:16.0","isFavor":0,"coverImg":"http://image.domolife.cn/platform/isprpBFs4r1554177050109.png","attachProductList":null}]
     */

    private String sysTime;
    private String title;
    private String description;
    private int isSubscribe;
    private int score;
    private List<EditorBean> result;

    public boolean getIsSubscribe() {
        return isSubscribe == 1;
    }

    public int getSubscriberStatus(Boolean isSelected) {
        return isSelected ? 1 : 0;
    }


    public String getSysTime() {
        return sysTime;
    }

    public void setSysTime(String sysTime) {
        this.sysTime = sysTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public void setIsSubscribe(int isSubscribe) {
        this.isSubscribe = isSubscribe;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<EditorBean> getResult() {
        return result;
    }

    public void setResult(List<EditorBean> result) {
        this.result = result;
    }


    public class EditorBean implements Parcelable {
        /**
         * id : 20
         * title : 短信营销优化
         * content : [{"picUrl":"http://image.domolife.cn/platform/f75pJ3CT3M1554278369158.jpg","id":"14132","type":"pictureGoods"}]
         * likeNum : 0
         * messageCount : 0
         * createTime : 2019-04-03 15:59:45.0
         * publishTime : 2019-04-03 15:59:54.0
         * isFavor : 0
         * coverImg : http://image.domolife.cn/platform/mEF8YBEJHG1554278343968.jpg
         * attachProductList : [{"productId":15871,"productName":"Let's diet恒温蕾丝内衣","productImg":"http://image.domolife.cn/platform/RQH35GYifW1539334557372.jpg"}]
         */

        private int id;
        private String title;
        private int likeNum;
        private int messageCount;
        private String createTime;
        private String publishTime;
        private int isFavor;
        private String coverImg;
        private List<CommunalDetailBean> content;
        private List<AttachProductListBean> attachProductList;


        public class AttachProductListBean implements Parcelable {
            /**
             * productId : 15871
             * productName : Let's diet恒温蕾丝内衣
             * productImg : http://image.domolife.cn/platform/RQH35GYifW1539334557372.jpg
             */

            private int productId;
            private String productName;
            private String productImg;

            public int getProductId() {
                return productId;
            }

            public void setProductId(int productId) {
                this.productId = productId;
            }

            public String getProductName() {
                return productName;
            }

            public void setProductName(String productName) {
                this.productName = productName;
            }

            public String getProductImg() {
                return productImg;
            }

            public void setProductImg(String productImg) {
                this.productImg = productImg;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(this.productId);
                dest.writeString(this.productName);
                dest.writeString(this.productImg);
            }

            public AttachProductListBean() {
            }

            protected AttachProductListBean(Parcel in) {
                this.productId = in.readInt();
                this.productName = in.readString();
                this.productImg = in.readString();
            }

            public final Creator<AttachProductListBean> CREATOR = new Creator<AttachProductListBean>() {
                @Override
                public AttachProductListBean createFromParcel(Parcel source) {
                    return new AttachProductListBean(source);
                }

                @Override
                public AttachProductListBean[] newArray(int size) {
                    return new AttachProductListBean[size];
                }
            };
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

        public int getLikeNum() {
            return likeNum < 0 ? 0 : likeNum;
        }

        public String getLikeString() {
            return getStrings(getLikeNum() == 0 ? "赞" : String.valueOf(getLikeNum()));
        }

        public void setLikeNum(int likeNum) {
            this.likeNum = likeNum;
        }

        public int getMessageCount() {
            return messageCount;
        }

        public void setMessageCount(int messageCount) {
            this.messageCount = messageCount;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getPublishTime() {
            return publishTime;
        }

        public void setPublishTime(String publishTime) {
            this.publishTime = publishTime;
        }

        public boolean getIsFavor() {
            return isFavor == 1;
        }

        public int getFavorStatus() {
            return isFavor;
        }

        public void setIsFavor(boolean isFavor) {
            this.isFavor = isFavor ? 1 : 0;
        }

        public String getCoverImg() {
            return coverImg;
        }

        public void setCoverImg(String coverImg) {
            this.coverImg = coverImg;
        }

        public List<CommunalDetailBean> getContent() {
            return content;
        }

        public void setContent(List<CommunalDetailBean> content) {
            this.content = content;
        }

        public List<AttachProductListBean> getAttachProductList() {
            return attachProductList;
        }

        public void setAttachProductList(List<AttachProductListBean> attachProductList) {
            this.attachProductList = attachProductList;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.title);
            dest.writeInt(this.likeNum);
            dest.writeInt(this.messageCount);
            dest.writeString(this.createTime);
            dest.writeString(this.publishTime);
            dest.writeInt(this.isFavor);
            dest.writeString(this.coverImg);
            dest.writeList(this.content);
            dest.writeList(this.attachProductList);
        }

        public EditorBean() {
        }

        protected EditorBean(Parcel in) {
            this.id = in.readInt();
            this.title = in.readString();
            this.likeNum = in.readInt();
            this.messageCount = in.readInt();
            this.createTime = in.readString();
            this.publishTime = in.readString();
            this.isFavor = in.readInt();
            this.coverImg = in.readString();
            this.content = new ArrayList<CommunalDetailBean>();
            in.readList(this.content, CommunalDetailObjectBean.class.getClassLoader());
            this.attachProductList = new ArrayList<AttachProductListBean>();
            in.readList(this.attachProductList, AttachProductListBean.class.getClassLoader());
        }

        public final Parcelable.Creator<EditorBean> CREATOR = new Parcelable.Creator<EditorBean>() {
            @Override
            public EditorBean createFromParcel(Parcel source) {
                return new EditorBean(source);
            }

            @Override
            public EditorBean[] newArray(int size) {
                return new EditorBean[size];
            }
        };
    }
}
