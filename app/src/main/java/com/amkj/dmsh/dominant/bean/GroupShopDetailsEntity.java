package com.amkj.dmsh.dominant.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.amkj.dmsh.base.BaseTimeEntity;
import com.amkj.dmsh.bean.CommunalDetailBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.shopdetails.bean.PropsBean;
import com.amkj.dmsh.shopdetails.bean.PropvaluesBean;
import com.amkj.dmsh.shopdetails.bean.SkuSaleBean;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/6/15
 * class description:请输入类描述
 */

public class GroupShopDetailsEntity extends BaseTimeEntity {

    /**
     * result : {"gpCount":12,"gpPicUrl":"http://img.domolife.cn/platform/ZZWGaMTANz.jpg","gpStartTime":"2017-06-01 00:00:00","gpCreateStatus":"还差一人成团","gpInfoId":1,"productSkuValue":"默认:蓝色","gpProductQuantity":123,"gpType":"2人团","price":"1098.0","coverImage":"http://img.domolife.cn/platform/F4bZMMKEFF1497410875842.jpeg","quantityStatus":{"quantityStatusMsg":"库存足够，可开团和参团","quantityStatusId":1002},"images":"http://img.domolife.cn/platform/F4bZMMKEFF1497410875842.jpeg,http://img.domolife.cn/platform/NBCDtS3nBa1497410876188.jpeg,http://img.domolife.cn/platform/3atQYAZQ4X1497410876254.jpeg","productId":4282,"gpProductId":17,"gpEndTime":"2017-06-09 00:00:00","gpSkuId":112,"skuQuantity":998,"propValueId":"5","goodsAreaLabel":"拼团","subtitle":"","name":"北鼎K206钻石电热水壶礼盒装","gpPrice":"12.0"}
     * currentTime : 2017-06-16 10:56:31
     * msg : 请求成功
     * code : 01
     */
    private long second;
    @SerializedName("result")
    private GroupShopDetailsBean groupShopDetailsBean;

    public long getSecond() {
        return second;
    }

    public void setSecond(long second) {
        this.second = second;
    }

    public GroupShopDetailsBean getGroupShopDetailsBean() {
        return groupShopDetailsBean;
    }

    public void setGroupShopDetailsBean(GroupShopDetailsBean groupShopDetailsBean) {
        this.groupShopDetailsBean = groupShopDetailsBean;
    }

    public static class GroupShopDetailsBean {
        /**
         * gpInfoId : 532
         * productId : 9531
         * productName : 日本樱花香皂 深粉色 30g
         * subTitle : 天然植物洁净配方
         * gpStartTime : 2019-11-25 17:13:06
         * gpEndTime : 2020-11-25 17:13:08
         * requireCount : 2
         * type : 0
         * gpPrice : 0.1
         * price : 21.9
         * gpQuantity : 82
         * gpMaxCreateCount : 58
         * gpCreateCount : 32
         * picUrlList : ["http://image.domolife.cn/platform/20180410/20180410113635192.jpg","http://image.domolife.cn/platform/20180410/20180410113635211.jpg","http://image.domolife.cn/platform/20180410/20180410113637278.jpg","http://image.domolife.cn/platform/20180410/20180410113638536.jpg"]
         * tagText : ["正品保证","海外直邮","30天退货","48小时发货","48小时发货","一件包邮"]
         * skuSale : [{"skuId":"2508","productId":"9531","quantity":"10","price":"0.01","propValues":"1"}]
         * propvalues : [{"propValueId":1,"propId":1,"propValueName":"默认","propValueUrl":""}]
         * props : [{"propId":1,"propName":"默认","praentId":0}]
         * itemBody : [{"type":"text","content":"<p><img alt=\"1_01.jpg\" src=\"http://image.domolife.cn/platfrom/20170921/1505964937448059157.jpg\" title=\"1505964937448059157.jpg\" /><\/p>"},{"type":"text","content":"<p><img alt=\"1_02.jpg\" src=\"http://image.domolife.cn/platfrom/20170921/1505964941920010891.jpg\" title=\"1505964941920010891.jpg\" /><\/p>"},{"type":"text","content":"<p><img alt=\"1_03.jpg\" src=\"http://image.domolife.cn/platfrom/20170921/1505964945824080294.jpg\" title=\"1505964945824080294.jpg\" /><\/p>"},{"type":"text","content":"<p><img alt=\"1_04.jpg\" src=\"http://image.domolife.cn/platfrom/20170921/1505964950199009503.jpg\" title=\"1505964950199009503.jpg\" /><\/p>"},{"type":"text","content":"<p><img alt=\"1_05.jpg\" src=\"http://image.domolife.cn/platfrom/20170921/1505964954931094010.jpg\" title=\"1505964954931094010.jpg\" /><\/p>"},{"type":"text","content":"<p><img alt=\"1505964958653046282.jpg\" border=\"0\" height=\"\" src=\"http://image.domolife.cn/platfrom/20170921/1505964958653046282.jpg\" title=\"1505964958653046282.jpg\" vspace=\"0\" width=\"\" /><\/p>"},{"type":"text","content":"<p><img alt=\"1_07.jpg\" src=\"http://image.domolife.cn/platfrom/20170921/1505964967481078748.jpg\" title=\"1505964967481078748.jpg\" /><\/p>"},{"type":"text","content":"<p><img alt=\"1_08.jpg\" src=\"http://image.domolife.cn/platfrom/20170921/1505964971972030849.jpg\" title=\"1505964971972030849.jpg\" /><\/p>"},{"type":"text","content":"<p><img alt=\"1_09.jpg\" src=\"http://image.domolife.cn/platfrom/20170921/1505964977390000117.jpg\" title=\"1505964977390000117.jpg\" /><\/p>"},{"type":"text","content":"<p><img alt=\"1_10.jpg\" src=\"http://image.domolife.cn/platfrom/20170921/1505964981361026443.jpg\" title=\"1505964981361026443.jpg\" /><\/p>"}]
         * recordList : [{"nickName":"瘾姓埋名","avatar":"https://thirdwx.qlogo.cn/mmopen/vi_32/ZKBPWJ8UBPI4uPzDMiaOuUobnABZQpSCscZOp4Vo1bOuLhVjCV6CPzZ71zbquia7qicXBac6dHNJx5hehoj4bLpaQ/132","endTime":"2019-12-14 14:22:53","gpRecordId":"18786","remainNum":"1"}]
         */

        private String gpInfoId;
        private String gpRecordId;
        private String productId;
        private String gpProductId;
        private String productName;
        private String gpName;
        private String subTitle;
        private String gpStartTime;
        private String gpEndTime;
        private String requireCount;
        private String type;//0商品团，1抽奖团
        private String range;//0特价团，1邀新团
        private String gpPrice;
        private String price;
        private String gpQuantity;
        private String gpMaxCreateCount;
        private String gpCreateCount;
        private String videoUrl;
        private String coverImage;
        private String tipText;
        private String isBtUsable;
        private List<String> picUrlList;
        private List<String> tagText;
        private List<SkuSaleBean> skuSale;
        private List<PropvaluesBean> propvalues;
        private List<PropsBean> props;
        private List<CommunalDetailBean> itemBody;
        private List<ParticipantInfoBean.GroupShopJoinBean> recordList;
        private List<ParticipantInfoBean.GroupShopJoinBean> luckUserList;
        private ParticipantInfoBean participantInfo;
        private List<CommunalDetailBean> gpRule;
        private List<CommunalDetailBean> servicePromise;
        private List<Map<String, String>> preSaleInfos;

        private int gpSkuId;
        private int gpStatus = ConstantVariable.OPEN_GROUP;//开团 1 参团 2

        public List<Map<String, String>> getPreSaleInfo() {
            return preSaleInfos;
        }

        public void setPreSaleInfo(List<Map<String, String>> preSaleInfo) {
            this.preSaleInfos = preSaleInfo;
        }

        public boolean isBtUsable() {
            return "1".equals(isBtUsable);
        }

        public boolean isLotteryGroup() {
            return ConstantVariable.GROUP_LOTTERY.equals(type);
        }

        public boolean isProductGroup() {
            return ConstantVariable.GROUP_PRODUCT.equals(type);
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public boolean isNewUserGroup() {
            return "1".equals(range);
        }

        public List<ParticipantInfoBean.GroupShopJoinBean> getLuckUserList() {
            return luckUserList;
        }

        public void setLuckUserList(List<ParticipantInfoBean.GroupShopJoinBean> luckUserList) {
            this.luckUserList = luckUserList;
        }

        public List<CommunalDetailBean> getServicePromise() {
            return servicePromise;
        }

        public void setServicePromise(List<CommunalDetailBean> servicePromise) {
            this.servicePromise = servicePromise;
        }

        public List<CommunalDetailBean> getGpRule() {
            return gpRule;
        }

        public void setGpRule(List<CommunalDetailBean> gpRule) {
            this.gpRule = gpRule;
        }

        public String getGpName() {
            return gpName;
        }

        public void setGpName(String gpName) {
            this.gpName = gpName;
        }

        public String getTipText() {
            return tipText;
        }

        public void setTipText(String tipText) {
            this.tipText = tipText;
        }

        public String getGpProductId() {
            return gpProductId;
        }

        public void setGpProductId(String gpProductId) {
            this.gpProductId = gpProductId;
        }

        public String getCoverImage() {
            return coverImage;
        }

        public void setCoverImage(String coverImage) {
            this.coverImage = coverImage;
        }

        public String getGpRecordId() {
            return gpRecordId;
        }

        public void setGpRecordId(String gpRecordId) {
            this.gpRecordId = gpRecordId;
        }

        public int getGpStatus() {
            return gpStatus;
        }

        public void setGpStatus(int gpStatus) {
            this.gpStatus = gpStatus;
        }

        public int getGpSkuId() {
            return gpSkuId;
        }

        public void setGpSkuId(int gpSkuId) {
            this.gpSkuId = gpSkuId;
        }

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }

        public int getGpInfoId() {
            return ConstantMethod.getStringChangeIntegers(gpInfoId);
        }

        public void setGpInfoId(String gpInfoId) {
            this.gpInfoId = gpInfoId;
        }

        public int getProductId() {
            return ConstantMethod.getStringChangeIntegers(productId);
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getSubTitle() {
            return subTitle;
        }

        public void setSubTitle(String subTitle) {
            this.subTitle = subTitle;
        }

        public String getGpStartTime() {
            return gpStartTime;
        }

        public void setGpStartTime(String gpStartTime) {
            this.gpStartTime = gpStartTime;
        }

        public String getGpEndTime() {
            return gpEndTime;
        }

        public void setGpEndTime(String gpEndTime) {
            this.gpEndTime = gpEndTime;
        }

        public int getRequireCount() {
            return ConstantMethod.getStringChangeIntegers(requireCount);
        }

        public void setRequireCount(String requireCount) {
            this.requireCount = requireCount;
        }

        public String getGpPrice() {
            return gpPrice;
        }

        public void setGpPrice(String gpPrice) {
            this.gpPrice = gpPrice;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public int getGpQuantity() {
            return ConstantMethod.getStringChangeIntegers(gpQuantity);
        }

        public void setGpQuantity(String gpQuantity) {
            this.gpQuantity = gpQuantity;
        }

        public int getGpMaxCreateCount() {
            return ConstantMethod.getStringChangeIntegers(gpMaxCreateCount);
        }

        public void setGpMaxCreateCount(String gpMaxCreateCount) {
            this.gpMaxCreateCount = gpMaxCreateCount;
        }

        public int getGpCreateCount() {
            return ConstantMethod.getStringChangeIntegers(gpCreateCount);
        }

        public void setGpCreateCount(String gpCreateCount) {
            this.gpCreateCount = gpCreateCount;
        }

        public List<String> getPicUrlList() {
            return picUrlList;
        }

        public void setPicUrlList(List<String> picUrlList) {
            this.picUrlList = picUrlList;
        }

        public List<String> getTagText() {
            return tagText;
        }

        public void setTagText(List<String> tagText) {
            this.tagText = tagText;
        }

        public List<SkuSaleBean> getSkuSale() {
            return skuSale;
        }

        public void setSkuSale(List<SkuSaleBean> skuSale) {
            this.skuSale = skuSale;
        }

        public List<PropvaluesBean> getPropvalues() {
            return propvalues;
        }

        public void setPropvalues(List<PropvaluesBean> propvalues) {
            this.propvalues = propvalues;
        }

        public List<PropsBean> getProps() {
            return props;
        }

        public void setProps(List<PropsBean> props) {
            this.props = props;
        }

        public List<CommunalDetailBean> getItemBody() {
            return itemBody;
        }

        public void setItemBody(List<CommunalDetailBean> itemBody) {
            this.itemBody = itemBody;
        }

        public List<ParticipantInfoBean.GroupShopJoinBean> getRecordList() {
            return recordList;
        }

        public void setRecordList(List<ParticipantInfoBean.GroupShopJoinBean> recordList) {
            this.recordList = recordList;
        }

        public ParticipantInfoBean getParticipantInfo() {
            return participantInfo;
        }

        public void setParticipantInfo(ParticipantInfoBean participantInfo) {
            this.participantInfo = participantInfo;
        }

        public static class RecordListBean {
            /**
             * nickName : 瘾姓埋名
             * avatar : https://thirdwx.qlogo.cn/mmopen/vi_32/ZKBPWJ8UBPI4uPzDMiaOuUobnABZQpSCscZOp4Vo1bOuLhVjCV6CPzZ71zbquia7qicXBac6dHNJx5hehoj4bLpaQ/132
             * endTime : 2019-12-14 14:22:53
             * gpRecordId : 18786
             * remainNum : 1
             */

            private String nickName;
            private String avatar;
            private String endTime;
            private String gpRecordId;
            private String remainNum;

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getEndTime() {
                return endTime;
            }

            public void setEndTime(String endTime) {
                this.endTime = endTime;
            }

            public String getGpRecordId() {
                return gpRecordId;
            }

            public void setGpRecordId(String gpRecordId) {
                this.gpRecordId = gpRecordId;
            }

            public String getRemainNum() {
                return remainNum;
            }

            public void setRemainNum(String remainNum) {
                this.remainNum = remainNum;
            }
        }

        public static class ParticipantInfoBean {
            /**
             * endTime : 2019-12-16 11:39:45
             * statusText :
             * userInfoList : [{"userId":"266978","nickName":"疯狂的小新","avatar":"http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTIMYYs4bAvzFSCMosexyjBiazpCk2v93qfSqszWuib4c2tXlAzfrRqnArOeLNJ4xXPnHWn9KgqQ1kRw/132","statusText":""}]
             */

            private String endTime;
            private String startTime;
            private String statusText;
            private List<GroupShopJoinBean> userInfoList;

            public String getStartTime() {
                return startTime;
            }

            public void setStartTime(String startTime) {
                this.startTime = startTime;
            }

            public String getEndTime() {
                return endTime;
            }

            public void setEndTime(String endTime) {
                this.endTime = endTime;
            }

            public String getStatusText() {
                return statusText;
            }

            public void setStatusText(String statusText) {
                this.statusText = statusText;
            }

            public List<GroupShopJoinBean> getUserInfoList() {
                return userInfoList == null ? new ArrayList<>() : userInfoList;
            }

            public void setUserInfoList(List<GroupShopJoinBean> userInfoList) {
                this.userInfoList = userInfoList;
            }

            public static class GroupShopJoinBean extends BaseTimeEntity implements MultiItemEntity, Parcelable {
                /**
                 * userId : 266978
                 * nickName : 疯狂的小新
                 * avatar : http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTIMYYs4bAvzFSCMosexyjBiazpCk2v93qfSqszWuib4c2tXlAzfrRqnArOeLNJ4xXPnHWn9KgqQ1kRw/132
                 * statusText :
                 */

                private String userId;
                private String nickName;
                private String avatar;
                private String statusText;
                private String remainNum;
                private String endTime;
                private int itemType;
                private String groupStatus;
                private String gpRecordId;
                private List<GroupShopJoinBean> memberListBeans;

                private boolean isDownTime;

                public boolean isDownTime() {
                    return isDownTime;
                }

                public void setDownTime(boolean downTime) {
                    isDownTime = downTime;
                }

                public String getGpRecordId() {
                    return gpRecordId;
                }

                public void setGpRecordId(String gpRecordId) {
                    this.gpRecordId = gpRecordId;
                }

                public String getGroupStatus() {
                    return groupStatus;
                }

                public void setGroupStatus(String groupStatus) {
                    this.groupStatus = groupStatus;
                }

                public String getEndTime() {
                    return endTime;
                }

                public void setEndTime(String endTime) {
                    this.endTime = endTime;
                }

                public String getRemainNum() {
                    return remainNum;
                }

                public void setRemainNum(String remainNum) {
                    this.remainNum = remainNum;
                }

                public List<GroupShopJoinBean> getMemberListBeans() {
                    return memberListBeans;
                }

                public void setMemberListBeans(List<GroupShopJoinBean> memberListBeans) {
                    this.memberListBeans = memberListBeans;
                }

                public String getUserId() {
                    return userId;
                }

                public void setUserId(String userId) {
                    this.userId = userId;
                }

                public String getNickName() {
                    return nickName;
                }

                public void setNickName(String nickName) {
                    this.nickName = nickName;
                }

                public String getAvatar() {
                    return avatar;
                }

                public void setAvatar(String avatar) {
                    this.avatar = avatar;
                }

                public String getStatusText() {
                    return statusText;
                }

                public void setStatusText(String statusText) {
                    this.statusText = statusText;
                }

                @Override
                public int getItemType() {
                    return itemType;
                }

                public void setItemType(int itemType) {
                    this.itemType = itemType;
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(this.userId);
                    dest.writeString(this.nickName);
                    dest.writeString(this.avatar);
                    dest.writeString(this.statusText);
                    dest.writeString(this.remainNum);
                    dest.writeString(this.endTime);
                    dest.writeInt(this.itemType);
                    dest.writeString(this.groupStatus);
                    dest.writeString(this.gpRecordId);
                    dest.writeList(this.memberListBeans);
                    dest.writeByte(this.isDownTime ? (byte) 1 : (byte) 0);
                }

                public GroupShopJoinBean() {
                }

                protected GroupShopJoinBean(Parcel in) {
                    this.userId = in.readString();
                    this.nickName = in.readString();
                    this.avatar = in.readString();
                    this.statusText = in.readString();
                    this.remainNum = in.readString();
                    this.endTime = in.readString();
                    this.itemType = in.readInt();
                    this.groupStatus = in.readString();
                    this.gpRecordId = in.readString();
                    this.memberListBeans = new ArrayList<GroupShopJoinBean>();
                    in.readList(this.memberListBeans, GroupShopJoinBean.class.getClassLoader());
                    this.isDownTime = in.readByte() != 0;
                }

                public static final Parcelable.Creator<GroupShopJoinBean> CREATOR = new Parcelable.Creator<GroupShopJoinBean>() {
                    @Override
                    public GroupShopJoinBean createFromParcel(Parcel source) {
                        return new GroupShopJoinBean(source);
                    }

                    @Override
                    public GroupShopJoinBean[] newArray(int size) {
                        return new GroupShopJoinBean[size];
                    }
                };
            }
        }
    }
}
