package com.amkj.dmsh.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.amkj.dmsh.base.BaseTimeEntity;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * Created by xiaoxin on 2019/3/20 0020
 * ClassDescription :
 */
public class EditorEntity extends BaseTimeEntity {

    /**
     * title : #小编精选#
     * description : 小编每天会看到同事找回来的各种各样的宝贝，真想推荐给大家，特开此专栏，每日一更精选好物，欢迎订阅
     * isSubscribe : 0
     * result : [{"id":4,"title":"孩子更爱阅读2","summary":" 给孩子一个阅读的天堂吧~来看看这个XXXXX2","content":"全橡木无甲醛书架......2","status":1,"pv":25,"likeNum":2,"messageCount":2,"createTime":"2019-03-19 16:47:54.0","publishTime":"2019-03-20 11:05:51.0","mainProduct":{"productId":8644,"productName":"夕染彩绘全包硅胶防摔苹果手机壳 带支架（测试）","productImg":"http://image.domolife.cn/2016-06-21_5768e3ed169f5.jpg"},"attachProductList":[{"productId":4181,"productName":"测试商品垃圾桶","productImg":"http://image.domolife.cn/2016-07-18_578cdabf8b38f.JPG"},{"productId":20295,"productName":"测试产品  我才是正确的","productImg":"http://image.domolife.cn/platform/20181130/20181130151841817.jpeg"}]}]
     */

    private String title;
    private String description;
    private int isSubscribe;
    private int score;
    private List<EditorBean> result;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
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

    public boolean getIsSubscribe() {
        return isSubscribe == 1;
    }

    public int getSubscriberStatus(Boolean isSelected) {
        return isSelected ? 1 : 0;
    }

    public void setIsSubscribe(int isSubscribe) {
        this.isSubscribe = isSubscribe;
    }

    public List<EditorBean> getResult() {
        return result;
    }

    public void setResult(List<EditorBean> result) {
        this.result = result;
    }

    public static class EditorBean implements Parcelable {
        /**
         * id : 4
         * title : 孩子更爱阅读2
         * summary :  给孩子一个阅读的天堂吧~来看看这个XXXXX2
         * content : 全橡木无甲醛书架......2
         * status : 1
         * pv : 25
         * likeNum : 2
         * messageCount : 2
         * createTime : 2019-03-19 16:47:54.0
         * publishTime : 2019-03-20 11:05:51.0
         * mainProduct : {"productId":8644,"productName":"夕染彩绘全包硅胶防摔苹果手机壳 带支架（测试）","productImg":"http://image.domolife.cn/2016-06-21_5768e3ed169f5.jpg"}
         * attachProductList : [{"productId":4181,"productName":"测试商品垃圾桶","productImg":"http://image.domolife.cn/2016-07-18_578cdabf8b38f.JPG"},{"productId":20295,"productName":"测试产品  我才是正确的","productImg":"http://image.domolife.cn/platform/20181130/20181130151841817.jpeg"}]
         */

        private int id;
        private String title;
        private String content;
        private int status;
        private int pv;
        private int likeNum;
        private int isFavor;
        private int messageCount;
        private String coverImg;
        private String createTime;
        private String publishTime;
        private MainProductBean mainProduct;
        private List<AttachProductListBean> attachProductList;

        public String getCoverImg() {
            return coverImg;
        }

        public void setCoverImg(String coverImg) {
            this.coverImg = coverImg;
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

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getPv() {
            return pv;
        }

        public void setPv(int pv) {
            this.pv = pv;
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

        public MainProductBean getMainProduct() {
            return mainProduct;
        }

        public void setMainProduct(MainProductBean mainProduct) {
            this.mainProduct = mainProduct;
        }

        public List<AttachProductListBean> getAttachProductList() {
            return attachProductList;
        }

        public void setAttachProductList(List<AttachProductListBean> attachProductList) {
            this.attachProductList = attachProductList;
        }

        public static class MainProductBean implements Parcelable {
            /**
             * productId : 8644
             * productName : 夕染彩绘全包硅胶防摔苹果手机壳 带支架（测试）
             * productImg : http://image.domolife.cn/2016-06-21_5768e3ed169f5.jpg
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

            public MainProductBean() {
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

            protected MainProductBean(Parcel in) {
                this.productId = in.readInt();
                this.productName = in.readString();
                this.productImg = in.readString();
            }

            public static final Creator<MainProductBean> CREATOR = new Creator<MainProductBean>() {
                @Override
                public MainProductBean createFromParcel(Parcel source) {
                    return new MainProductBean(source);
                }

                @Override
                public MainProductBean[] newArray(int size) {
                    return new MainProductBean[size];
                }
            };
        }

        public static class AttachProductListBean implements Parcelable {
            /**
             * productId : 4181
             * productName : 测试商品垃圾桶
             * productImg : http://image.domolife.cn/2016-07-18_578cdabf8b38f.JPG
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

            public static final Creator<AttachProductListBean> CREATOR = new Creator<AttachProductListBean>() {
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


        public EditorBean() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.title);
            dest.writeString(this.content);
            dest.writeInt(this.status);
            dest.writeInt(this.pv);
            dest.writeInt(this.likeNum);
            dest.writeInt(this.isFavor);
            dest.writeInt(this.messageCount);
            dest.writeString(this.coverImg);
            dest.writeString(this.createTime);
            dest.writeString(this.publishTime);
            dest.writeParcelable(this.mainProduct, flags);
            dest.writeTypedList(this.attachProductList);
        }

        protected EditorBean(Parcel in) {
            this.id = in.readInt();
            this.title = in.readString();
            this.content = in.readString();
            this.status = in.readInt();
            this.pv = in.readInt();
            this.likeNum = in.readInt();
            this.isFavor = in.readInt();
            this.messageCount = in.readInt();
            this.coverImg = in.readString();
            this.createTime = in.readString();
            this.publishTime = in.readString();
            this.mainProduct = in.readParcelable(MainProductBean.class.getClassLoader());
            this.attachProductList = in.createTypedArrayList(AttachProductListBean.CREATOR);
        }

        public static final Creator<EditorBean> CREATOR = new Creator<EditorBean>() {
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
