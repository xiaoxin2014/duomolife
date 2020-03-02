package com.amkj.dmsh.dominant.bean;

import com.amkj.dmsh.base.BaseTimeEntity;
import com.amkj.dmsh.shopdetails.bean.PropsBean;
import com.amkj.dmsh.shopdetails.bean.PropvaluesBean;
import com.amkj.dmsh.shopdetails.bean.SkuSaleBean;

import java.util.List;

/**
 * Created by xiaoxin on 2019/12/13
 * Version:v4.4.0
 * ClassDescription :团购商品详情实体类
 */
public class GroupShopInfoEntity extends BaseTimeEntity {

    /**
     * sysTime : 2019-12-13 15:25:19
     * result : {"gpInfoId":"532","productId":"9531","productName":"日本樱花香皂 深粉色 30g","subTitle":"天然植物洁净配方","gpStartTime":"2019-11-25 17:13:06","gpEndTime":"2020-11-25 17:13:08","requireCount":"2","type":"0","gpPrice":"0.1","price":"21.9","gpQuantity":"82","gpMaxCreateCount":"58","gpCreateCount":"32","picUrlList":["http://image.domolife.cn/platform/20180410/20180410113635192.jpg","http://image.domolife.cn/platform/20180410/20180410113635211.jpg","http://image.domolife.cn/platform/20180410/20180410113637278.jpg","http://image.domolife.cn/platform/20180410/20180410113638536.jpg"],"tagText":["正品保证","海外直邮","30天退货","48小时发货","48小时发货","一件包邮"],"skuSale":[{"skuId":"2508","productId":"9531","quantity":"10","price":"0.01","propValues":"1"}],"propvalues":[{"propValueId":1,"propId":1,"propValueName":"默认","propValueUrl":""}],"props":[{"propId":1,"propName":"默认","praentId":0}],"itemBody":[{"type":"text","content":"<p><img alt=\"1_01.jpg\" src=\"http://image.domolife.cn/platfrom/20170921/1505964937448059157.jpg\" title=\"1505964937448059157.jpg\" /><\/p>"},{"type":"text","content":"<p><img alt=\"1_02.jpg\" src=\"http://image.domolife.cn/platfrom/20170921/1505964941920010891.jpg\" title=\"1505964941920010891.jpg\" /><\/p>"},{"type":"text","content":"<p><img alt=\"1_03.jpg\" src=\"http://image.domolife.cn/platfrom/20170921/1505964945824080294.jpg\" title=\"1505964945824080294.jpg\" /><\/p>"},{"type":"text","content":"<p><img alt=\"1_04.jpg\" src=\"http://image.domolife.cn/platfrom/20170921/1505964950199009503.jpg\" title=\"1505964950199009503.jpg\" /><\/p>"},{"type":"text","content":"<p><img alt=\"1_05.jpg\" src=\"http://image.domolife.cn/platfrom/20170921/1505964954931094010.jpg\" title=\"1505964954931094010.jpg\" /><\/p>"},{"type":"text","content":"<p><img alt=\"1505964958653046282.jpg\" border=\"0\" height=\"\" src=\"http://image.domolife.cn/platfrom/20170921/1505964958653046282.jpg\" title=\"1505964958653046282.jpg\" vspace=\"0\" width=\"\" /><\/p>"},{"type":"text","content":"<p><img alt=\"1_07.jpg\" src=\"http://image.domolife.cn/platfrom/20170921/1505964967481078748.jpg\" title=\"1505964967481078748.jpg\" /><\/p>"},{"type":"text","content":"<p><img alt=\"1_08.jpg\" src=\"http://image.domolife.cn/platfrom/20170921/1505964971972030849.jpg\" title=\"1505964971972030849.jpg\" /><\/p>"},{"type":"text","content":"<p><img alt=\"1_09.jpg\" src=\"http://image.domolife.cn/platfrom/20170921/1505964977390000117.jpg\" title=\"1505964977390000117.jpg\" /><\/p>"},{"type":"text","content":"<p><img alt=\"1_10.jpg\" src=\"http://image.domolife.cn/platfrom/20170921/1505964981361026443.jpg\" title=\"1505964981361026443.jpg\" /><\/p>"}],"recordList":[{"nickName":"瘾姓埋名","avatar":"https://thirdwx.qlogo.cn/mmopen/vi_32/ZKBPWJ8UBPI4uPzDMiaOuUobnABZQpSCscZOp4Vo1bOuLhVjCV6CPzZ71zbquia7qicXBac6dHNJx5hehoj4bLpaQ/132","endTime":"2019-12-14 14:22:53","gpRecordId":"18786","remainNum":"1"}]}
     */


    private GroupShopInfoBean result;

    public GroupShopInfoBean getResult() {
        return result;
    }

    public void setResult(GroupShopInfoBean result) {
        this.result = result;
    }

    public static class GroupShopInfoBean {
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
        private String productId;
        private String productName;
        private String subTitle;
        private String gpStartTime;
        private String gpEndTime;
        private String requireCount;
        private String type;
        private String gpPrice;
        private String price;
        private String gpQuantity;
        private String gpMaxCreateCount;
        private String gpCreateCount;
        private List<String> picUrlList;
        private List<String> tagText;
        private List<SkuSaleBean> skuSale;
        private List<PropvaluesBean> propvalues;
        private List<PropsBean> props;
        private List<ItemBodyBean> itemBody;
        private List<RecordListBean> recordList;

        public String getGpInfoId() {
            return gpInfoId;
        }

        public void setGpInfoId(String gpInfoId) {
            this.gpInfoId = gpInfoId;
        }

        public String getProductId() {
            return productId;
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

        public String getRequireCount() {
            return requireCount;
        }

        public void setRequireCount(String requireCount) {
            this.requireCount = requireCount;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
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

        public String getGpQuantity() {
            return gpQuantity;
        }

        public void setGpQuantity(String gpQuantity) {
            this.gpQuantity = gpQuantity;
        }

        public String getGpMaxCreateCount() {
            return gpMaxCreateCount;
        }

        public void setGpMaxCreateCount(String gpMaxCreateCount) {
            this.gpMaxCreateCount = gpMaxCreateCount;
        }

        public String getGpCreateCount() {
            return gpCreateCount;
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

        public List<ItemBodyBean> getItemBody() {
            return itemBody;
        }

        public void setItemBody(List<ItemBodyBean> itemBody) {
            this.itemBody = itemBody;
        }

        public List<RecordListBean> getRecordList() {
            return recordList;
        }

        public void setRecordList(List<RecordListBean> recordList) {
            this.recordList = recordList;
        }

        public static class ItemBodyBean {
            /**
             * type : text
             * content : <p><img alt="1_01.jpg" src="http://image.domolife.cn/platfrom/20170921/1505964937448059157.jpg" title="1505964937448059157.jpg" /></p>
             */

            private String type;
            private String content;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }
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
    }
}
