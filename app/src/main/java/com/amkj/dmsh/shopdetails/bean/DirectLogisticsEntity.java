package com.amkj.dmsh.shopdetails.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.amkj.dmsh.base.BaseEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by atd48 on 2016/10/31.
 */
public class DirectLogisticsEntity extends BaseEntity{

    @SerializedName("result")
    private DirectLogisticsBean directLogisticsBean;

    public DirectLogisticsBean getDirectLogisticsBean() {
        return directLogisticsBean;
    }

    public void setDirectLogisticsBean(DirectLogisticsBean directLogisticsBean) {
        this.directLogisticsBean = directLogisticsBean;
    }

    public static class DirectLogisticsBean {
        @SerializedName("logistics")
        private List<List<LogisticsProductPacketBean>> logisticsProductPacketList;

        public List<List<LogisticsProductPacketBean>> getLogisticsProductPacketList() {
            return logisticsProductPacketList;
        }

        public void setLogisticsProductPacketList(List<List<LogisticsProductPacketBean>> logisticsProductPacketList) {
            this.logisticsProductPacketList = logisticsProductPacketList;
        }

        public static class LogisticsProductPacketBean implements Parcelable{
            private String deliverTime;
            private String marketPrice;
            private String expressNo;
            private String saleSkuValue;
            private int count;
            @SerializedName("logistics")
            private LogisticsDetailsBean logisticsDetailsBean;
            private String picUrl;
            private String price;
            private String expressCompany;
            private String name;
            private int id;
            private int integralPrice;

            public String getDeliverTime() {
                return deliverTime;
            }

            public void setDeliverTime(String deliverTime) {
                this.deliverTime = deliverTime;
            }

            public String getMarketPrice() {
                return marketPrice;
            }

            public void setMarketPrice(String marketPrice) {
                this.marketPrice = marketPrice;
            }

            public String getExpressNo() {
                return expressNo;
            }

            public void setExpressNo(String expressNo) {
                this.expressNo = expressNo;
            }

            public String getSaleSkuValue() {
                return saleSkuValue;
            }

            public void setSaleSkuValue(String saleSkuValue) {
                this.saleSkuValue = saleSkuValue;
            }

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public LogisticsDetailsBean getLogisticsDetailsBean() {
                return logisticsDetailsBean;
            }

            public void setLogisticsDetailsBean(LogisticsDetailsBean logisticsDetailsBean) {
                this.logisticsDetailsBean = logisticsDetailsBean;
            }

            public String getPicUrl() {
                return picUrl;
            }

            public void setPicUrl(String picUrl) {
                this.picUrl = picUrl;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getExpressCompany() {
                return expressCompany;
            }

            public void setExpressCompany(String expressCompany) {
                this.expressCompany = expressCompany;
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

            public int getIntegralPrice() {
                return integralPrice;
            }

            public void setIntegralPrice(int integralPrice) {
                this.integralPrice = integralPrice;
            }

            public static class LogisticsDetailsBean implements Parcelable{
                @SerializedName("result")
                private LogisticsBean logisticsBean;

                public LogisticsBean getLogisticsBean() {
                    return logisticsBean;
                }

                public void setLogisticsBean(LogisticsBean logisticsBean) {
                    this.logisticsBean = logisticsBean;
                }

                public static class LogisticsBean implements Parcelable{
                    @SerializedName("list")
                    private List<LogisticTextBean> logisticTextBeanList;

                    public List<LogisticTextBean> getLogisticTextBeanList() {
                        return logisticTextBeanList;
                    }

                    public void setLogisticTextBeanList(List<LogisticTextBean> logisticTextBeanList) {
                        this.logisticTextBeanList = logisticTextBeanList;
                    }

                    public static class LogisticTextBean implements Parcelable{
                        /**
                         * time : 2018-12-04 21:10:56
                         * status : 该订单暂无流转信息，请耐心等待或联系客服！
                         */

                        private String time;
                        private String status;

                        public String getTime() {
                            return time;
                        }

                        public void setTime(String time) {
                            this.time = time;
                        }

                        public String getStatus() {
                            return status;
                        }

                        public void setStatus(String status) {
                            this.status = status;
                        }

                        @Override
                        public int describeContents() {
                            return 0;
                        }

                        @Override
                        public void writeToParcel(Parcel dest, int flags) {
                            dest.writeString(this.time);
                            dest.writeString(this.status);
                        }

                        public LogisticTextBean() {
                        }

                        protected LogisticTextBean(Parcel in) {
                            this.time = in.readString();
                            this.status = in.readString();
                        }

                        public static final Creator<LogisticTextBean> CREATOR = new Creator<LogisticTextBean>() {
                            @Override
                            public LogisticTextBean createFromParcel(Parcel source) {
                                return new LogisticTextBean(source);
                            }

                            @Override
                            public LogisticTextBean[] newArray(int size) {
                                return new LogisticTextBean[size];
                            }
                        };
                    }

                    @Override
                    public int describeContents() {
                        return 0;
                    }

                    @Override
                    public void writeToParcel(Parcel dest, int flags) {
                        dest.writeTypedList(this.logisticTextBeanList);
                    }

                    public LogisticsBean() {
                    }

                    protected LogisticsBean(Parcel in) {
                        this.logisticTextBeanList = in.createTypedArrayList(LogisticTextBean.CREATOR);
                    }

                    public static final Creator<LogisticsBean> CREATOR = new Creator<LogisticsBean>() {
                        @Override
                        public LogisticsBean createFromParcel(Parcel source) {
                            return new LogisticsBean(source);
                        }

                        @Override
                        public LogisticsBean[] newArray(int size) {
                            return new LogisticsBean[size];
                        }
                    };
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeParcelable(this.logisticsBean, flags);
                }

                public LogisticsDetailsBean() {
                }

                protected LogisticsDetailsBean(Parcel in) {
                    this.logisticsBean = in.readParcelable(LogisticsBean.class.getClassLoader());
                }

                public static final Creator<LogisticsDetailsBean> CREATOR = new Creator<LogisticsDetailsBean>() {
                    @Override
                    public LogisticsDetailsBean createFromParcel(Parcel source) {
                        return new LogisticsDetailsBean(source);
                    }

                    @Override
                    public LogisticsDetailsBean[] newArray(int size) {
                        return new LogisticsDetailsBean[size];
                    }
                };
            }

            public LogisticsProductPacketBean() {
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.deliverTime);
                dest.writeString(this.marketPrice);
                dest.writeString(this.expressNo);
                dest.writeString(this.saleSkuValue);
                dest.writeInt(this.count);
                dest.writeParcelable(this.logisticsDetailsBean, flags);
                dest.writeString(this.picUrl);
                dest.writeString(this.price);
                dest.writeString(this.expressCompany);
                dest.writeString(this.name);
                dest.writeInt(this.id);
                dest.writeInt(this.integralPrice);
            }

            protected LogisticsProductPacketBean(Parcel in) {
                this.deliverTime = in.readString();
                this.marketPrice = in.readString();
                this.expressNo = in.readString();
                this.saleSkuValue = in.readString();
                this.count = in.readInt();
                this.logisticsDetailsBean = in.readParcelable(LogisticsDetailsBean.class.getClassLoader());
                this.picUrl = in.readString();
                this.price = in.readString();
                this.expressCompany = in.readString();
                this.name = in.readString();
                this.id = in.readInt();
                this.integralPrice = in.readInt();
            }

            public static final Creator<LogisticsProductPacketBean> CREATOR = new Creator<LogisticsProductPacketBean>() {
                @Override
                public LogisticsProductPacketBean createFromParcel(Parcel source) {
                    return new LogisticsProductPacketBean(source);
                }

                @Override
                public LogisticsProductPacketBean[] newArray(int size) {
                    return new LogisticsProductPacketBean[size];
                }
            };
        }
    }
}
