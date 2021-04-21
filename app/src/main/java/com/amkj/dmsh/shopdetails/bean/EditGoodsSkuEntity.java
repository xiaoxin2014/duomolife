package com.amkj.dmsh.shopdetails.bean;

import android.text.TextUtils;

import com.amkj.dmsh.shopdetails.bean.GroupGoodsEntity.GroupGoodsBean.CombineCommonBean;
import com.amkj.dmsh.shopdetails.bean.IndentWriteEntity.IndentWriteBean.PrerogativeActivityInfo.GoodsListBean;
import com.amkj.dmsh.shopdetails.bean.ShopDetailsEntity.ShopPropertyBean.CombineProductInfoBean;
import com.amkj.dmsh.shopdetails.bean.ShopDetailsEntity.ShopPropertyBean.PresentProductInfoBean;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by atd48 on 2016/10/24.
 */
public class EditGoodsSkuEntity {

    /**
     * id : 4182
     * skuSale : [{"id":665,"price":"2.70","propValues":"3,12","quantity":17},{"id":26,"price":"0.01","propValues":"5,9","quantity":26},{"id":22,"price":"0.01","propValues":"4,11","quantity":22},{"id":25,"price":"0.01","propValues":"5,8","quantity":25},{"id":23,"price":"0.01","propValues":"4,12","quantity":23},{"id":670,"price":"3.20","propValues":"4,11","quantity":22},{"id":682,"price":"4.40","propValues":"6,11","quantity":34},{"id":657,"price":"1.90","propValues":"2,10","quantity":9},{"id":21,"price":"0.01","propValues":"4,10","quantity":21},{"id":8,"price":"0.01","propValues":"2,9","quantity":8},{"id":659,"price":"2.10","propValues":"2,12","quantity":11},{"id":675,"price":"3.70","propValues":"5,10","quantity":27},{"id":19,"price":"0.01","propValues":"4,8","quantity":19},{"id":42,"price":"0.01","propValues":"7,13","quantity":42},{"id":690,"price":"5.20","propValues":"7,13","quantity":42},{"id":24,"price":"0.01","propValues":"4,13","quantity":24},{"id":40,"price":"0.01","propValues":"7,11","quantity":40},{"id":651,"price":"1.20","propValues":"1,10","quantity":3},{"id":679,"price":"4.10","propValues":"6,8","quantity":31},{"id":7,"price":"0.01","propValues":"2,8","quantity":7},{"id":41,"price":"0.01","propValues":"7,12","quantity":41},{"id":667,"price":"2.90","propValues":"4,8","quantity":19},{"id":33,"price":"0.01","propValues":"6,10","quantity":33},{"id":658,"price":"2.00","propValues":"2,11","quantity":10},{"id":34,"price":"0.01","propValues":"6,11","quantity":34},{"id":35,"price":"0.01","propValues":"6,12","quantity":35},{"id":669,"price":"3.10","propValues":"4,10","quantity":21},{"id":4,"price":"0.01","propValues":"1,11","quantity":4},{"id":650,"price":"1.10","propValues":"1,9","quantity":2},{"id":655,"price":"1.70","propValues":"2,8","quantity":7},{"id":666,"price":"2.80","propValues":"3,13","quantity":18},{"id":680,"price":"4.20","propValues":"6,9","quantity":32},{"id":39,"price":"0.01","propValues":"7,10","quantity":39},{"id":660,"price":"2.20","propValues":"2,13","quantity":12},{"id":674,"price":"3.60","propValues":"5,9","quantity":26},{"id":672,"price":"3.40","propValues":"4,13","quantity":24},{"id":652,"price":"1.30","propValues":"1,11","quantity":4},{"id":686,"price":"4.80","propValues":"7,9","quantity":38},{"id":685,"price":"4.70","propValues":"7,8","quantity":37},{"id":661,"price":"2.30","propValues":"3,8","quantity":13},{"id":36,"price":"0.01","propValues":"6,13","quantity":36},{"id":683,"price":"4.50","propValues":"6,12","quantity":35},{"id":689,"price":"5.10","propValues":"7,12","quantity":41},{"id":13,"price":"0.01","propValues":"3,8","quantity":13},{"id":649,"price":"1.00","propValues":"1,8","quantity":1},{"id":673,"price":"3.50","propValues":"5,8","quantity":25},{"id":676,"price":"3.80","propValues":"5,11","quantity":28},{"id":664,"price":"2.60","propValues":"3,11","quantity":16},{"id":688,"price":"5.00","propValues":"7,11","quantity":40},{"id":687,"price":"4.90","propValues":"7,10","quantity":39},{"id":14,"price":"0.01","propValues":"3,9","quantity":14},{"id":3,"price":"0.01","propValues":"1,10","quantity":3},{"id":31,"price":"0.01","propValues":"6,8","quantity":31},{"id":662,"price":"2.40","propValues":"3,9","quantity":14},{"id":656,"price":"1.80","propValues":"2,9","quantity":8},{"id":678,"price":"4.00","propValues":"5,13","quantity":30},{"id":6,"price":"0.01","propValues":"1,13","quantity":6},{"id":653,"price":"1.40","propValues":"1,12","quantity":5},{"id":5,"price":"0.01","propValues":"1,12","quantity":5},{"id":27,"price":"0.01","propValues":"5,10","quantity":27},{"id":671,"price":"3.30","propValues":"4,12","quantity":23},{"id":28,"price":"0.01","propValues":"5,11","quantity":28},{"id":15,"price":"0.01","propValues":"3,10","quantity":15},{"id":681,"price":"4.30","propValues":"6,10","quantity":33},{"id":668,"price":"3.00","propValues":"4,9","quantity":20},{"id":1,"price":"0.01","propValues":"1,8","quantity":0},{"id":2,"price":"0.01","propValues":"1,9","quantity":2},{"id":30,"price":"0.01","propValues":"5,13","quantity":30},{"id":29,"price":"0.01","propValues":"5,12","quantity":29},{"id":677,"price":"3.90","propValues":"5,12","quantity":29},{"id":20,"price":"0.01","propValues":"4,9","quantity":20},{"id":11,"price":"0.01","propValues":"2,12","quantity":11},{"id":37,"price":"0.01","propValues":"7,8","quantity":37},{"id":9,"price":"0.01","propValues":"2,10","quantity":9},{"id":10,"price":"0.01","propValues":"2,11","quantity":10},{"id":16,"price":"0.01","propValues":"3,11","quantity":16},{"id":17,"price":"0.01","propValues":"3,12","quantity":17},{"id":663,"price":"2.50","propValues":"3,10","quantity":15},{"id":38,"price":"0.01","propValues":"7,9","quantity":38},{"id":12,"price":"0.01","propValues":"2,13","quantity":12},{"id":654,"price":"1.50","propValues":"1,13","quantity":6},{"id":18,"price":"0.01","propValues":"3,13","quantity":18},{"id":32,"price":"0.01","propValues":"6,9","quantity":32},{"id":684,"price":"4.60","propValues":"6,13","quantity":36}]
     * propvalues : [{"propId":1,"propValueName":"赤","propValueId":1},{"propId":1,"propValueName":"橙","propValueId":2},{"propId":1,"propValueName":"黄","propValueId":3},{"propId":1,"propValueName":"绿","propValueId":4},{"propId":1,"propValueName":"青","propValueId":5},{"propId":1,"propValueName":"蓝","propValueId":6},{"propId":1,"propValueName":"紫","propValueId":7},{"propId":2,"propValueName":"S","propValueId":8},{"propId":2,"propValueName":"M","propValueId":9},{"propId":2,"propValueName":"L","propValueId":10},{"propId":2,"propValueName":"XL","propValueId":11},{"propId":2,"propValueName":"XXL","propValueId":12},{"propId":2,"propValueName":"XXXL","propValueId":13},{"propId":3,"propValueName":"你妹","propValueId":14},{"propId":3,"propValueName":"你大爷","propValueId":15},{"propId":3,"propValueName":"你二大爷","propValueId":16},{"propId":3,"propValueName":"你三大爷","propValueId":17},{"propId":3,"propValueName":"没词了","propValueId":18},{"propId":11,"propValueName":"圆领","propValueId":27},{"propId":12,"propValueName":"常规","propValueId":28},{"propId":13,"propValueName":"常规","propValueId":29},{"propId":14,"propValueName":"芳苑阁","propValueId":30},{"propId":15,"propValueName":"几何图案 拼色 拼接","propValueId":31}]
     * quantity : 1805
     * delivery : 直邮
     * props : [{"parentId":0,"propId":1,"propName":"颜色"},{"parentId":0,"propId":2,"propName":"尺码"},{"parentId":0,"propId":3,"propName":"逗比"},{"parentId":0,"propId":11,"propName":"领型"},{"parentId":0,"propId":12,"propName":"袖型"},{"parentId":0,"propId":13,"propName":"品牌"},{"parentId":0,"propId":14,"propName":"图案"},{"parentId":0,"propId":15,"propName":"图案文化"}]
     */

    @SerializedName("result")
    private EditGoodsSkuBean editGoodsSkuBean;
    /**
     * result : {"id":4182,"skuSale":[{"id":665,"price":"2.70","propValues":"3,12","quantity":17},{"id":26,"price":"0.01","propValues":"5,9","quantity":26},{"id":22,"price":"0.01","propValues":"4,11","quantity":22},{"id":25,"price":"0.01","propValues":"5,8","quantity":25},{"id":23,"price":"0.01","propValues":"4,12","quantity":23},{"id":670,"price":"3.20","propValues":"4,11","quantity":22},{"id":682,"price":"4.40","propValues":"6,11","quantity":34},{"id":657,"price":"1.90","propValues":"2,10","quantity":9},{"id":21,"price":"0.01","propValues":"4,10","quantity":21},{"id":8,"price":"0.01","propValues":"2,9","quantity":8},{"id":659,"price":"2.10","propValues":"2,12","quantity":11},{"id":675,"price":"3.70","propValues":"5,10","quantity":27},{"id":19,"price":"0.01","propValues":"4,8","quantity":19},{"id":42,"price":"0.01","propValues":"7,13","quantity":42},{"id":690,"price":"5.20","propValues":"7,13","quantity":42},{"id":24,"price":"0.01","propValues":"4,13","quantity":24},{"id":40,"price":"0.01","propValues":"7,11","quantity":40},{"id":651,"price":"1.20","propValues":"1,10","quantity":3},{"id":679,"price":"4.10","propValues":"6,8","quantity":31},{"id":7,"price":"0.01","propValues":"2,8","quantity":7},{"id":41,"price":"0.01","propValues":"7,12","quantity":41},{"id":667,"price":"2.90","propValues":"4,8","quantity":19},{"id":33,"price":"0.01","propValues":"6,10","quantity":33},{"id":658,"price":"2.00","propValues":"2,11","quantity":10},{"id":34,"price":"0.01","propValues":"6,11","quantity":34},{"id":35,"price":"0.01","propValues":"6,12","quantity":35},{"id":669,"price":"3.10","propValues":"4,10","quantity":21},{"id":4,"price":"0.01","propValues":"1,11","quantity":4},{"id":650,"price":"1.10","propValues":"1,9","quantity":2},{"id":655,"price":"1.70","propValues":"2,8","quantity":7},{"id":666,"price":"2.80","propValues":"3,13","quantity":18},{"id":680,"price":"4.20","propValues":"6,9","quantity":32},{"id":39,"price":"0.01","propValues":"7,10","quantity":39},{"id":660,"price":"2.20","propValues":"2,13","quantity":12},{"id":674,"price":"3.60","propValues":"5,9","quantity":26},{"id":672,"price":"3.40","propValues":"4,13","quantity":24},{"id":652,"price":"1.30","propValues":"1,11","quantity":4},{"id":686,"price":"4.80","propValues":"7,9","quantity":38},{"id":685,"price":"4.70","propValues":"7,8","quantity":37},{"id":661,"price":"2.30","propValues":"3,8","quantity":13},{"id":36,"price":"0.01","propValues":"6,13","quantity":36},{"id":683,"price":"4.50","propValues":"6,12","quantity":35},{"id":689,"price":"5.10","propValues":"7,12","quantity":41},{"id":13,"price":"0.01","propValues":"3,8","quantity":13},{"id":649,"price":"1.00","propValues":"1,8","quantity":1},{"id":673,"price":"3.50","propValues":"5,8","quantity":25},{"id":676,"price":"3.80","propValues":"5,11","quantity":28},{"id":664,"price":"2.60","propValues":"3,11","quantity":16},{"id":688,"price":"5.00","propValues":"7,11","quantity":40},{"id":687,"price":"4.90","propValues":"7,10","quantity":39},{"id":14,"price":"0.01","propValues":"3,9","quantity":14},{"id":3,"price":"0.01","propValues":"1,10","quantity":3},{"id":31,"price":"0.01","propValues":"6,8","quantity":31},{"id":662,"price":"2.40","propValues":"3,9","quantity":14},{"id":656,"price":"1.80","propValues":"2,9","quantity":8},{"id":678,"price":"4.00","propValues":"5,13","quantity":30},{"id":6,"price":"0.01","propValues":"1,13","quantity":6},{"id":653,"price":"1.40","propValues":"1,12","quantity":5},{"id":5,"price":"0.01","propValues":"1,12","quantity":5},{"id":27,"price":"0.01","propValues":"5,10","quantity":27},{"id":671,"price":"3.30","propValues":"4,12","quantity":23},{"id":28,"price":"0.01","propValues":"5,11","quantity":28},{"id":15,"price":"0.01","propValues":"3,10","quantity":15},{"id":681,"price":"4.30","propValues":"6,10","quantity":33},{"id":668,"price":"3.00","propValues":"4,9","quantity":20},{"id":1,"price":"0.01","propValues":"1,8","quantity":0},{"id":2,"price":"0.01","propValues":"1,9","quantity":2},{"id":30,"price":"0.01","propValues":"5,13","quantity":30},{"id":29,"price":"0.01","propValues":"5,12","quantity":29},{"id":677,"price":"3.90","propValues":"5,12","quantity":29},{"id":20,"price":"0.01","propValues":"4,9","quantity":20},{"id":11,"price":"0.01","propValues":"2,12","quantity":11},{"id":37,"price":"0.01","propValues":"7,8","quantity":37},{"id":9,"price":"0.01","propValues":"2,10","quantity":9},{"id":10,"price":"0.01","propValues":"2,11","quantity":10},{"id":16,"price":"0.01","propValues":"3,11","quantity":16},{"id":17,"price":"0.01","propValues":"3,12","quantity":17},{"id":663,"price":"2.50","propValues":"3,10","quantity":15},{"id":38,"price":"0.01","propValues":"7,9","quantity":38},{"id":12,"price":"0.01","propValues":"2,13","quantity":12},{"id":654,"price":"1.50","propValues":"1,13","quantity":6},{"id":18,"price":"0.01","propValues":"3,13","quantity":18},{"id":32,"price":"0.01","propValues":"6,9","quantity":32},{"id":684,"price":"4.60","propValues":"6,13","quantity":36}],"propvalues":[{"propId":1,"propValueName":"赤","propValueId":1},{"propId":1,"propValueName":"橙","propValueId":2},{"propId":1,"propValueName":"黄","propValueId":3},{"propId":1,"propValueName":"绿","propValueId":4},{"propId":1,"propValueName":"青","propValueId":5},{"propId":1,"propValueName":"蓝","propValueId":6},{"propId":1,"propValueName":"紫","propValueId":7},{"propId":2,"propValueName":"S","propValueId":8},{"propId":2,"propValueName":"M","propValueId":9},{"propId":2,"propValueName":"L","propValueId":10},{"propId":2,"propValueName":"XL","propValueId":11},{"propId":2,"propValueName":"XXL","propValueId":12},{"propId":2,"propValueName":"XXXL","propValueId":13},{"propId":3,"propValueName":"你妹","propValueId":14},{"propId":3,"propValueName":"你大爷","propValueId":15},{"propId":3,"propValueName":"你二大爷","propValueId":16},{"propId":3,"propValueName":"你三大爷","propValueId":17},{"propId":3,"propValueName":"没词了","propValueId":18},{"propId":11,"propValueName":"圆领","propValueId":27},{"propId":12,"propValueName":"常规","propValueId":28},{"propId":13,"propValueName":"常规","propValueId":29},{"propId":14,"propValueName":"芳苑阁","propValueId":30},{"propId":15,"propValueName":"几何图案 拼色 拼接","propValueId":31}],"quantity":1805,"delivery":"直邮","props":[{"parentId":0,"propId":1,"propName":"颜色"},{"parentId":0,"propId":2,"propName":"尺码"},{"parentId":0,"propId":3,"propName":"逗比"},{"parentId":0,"propId":11,"propName":"领型"},{"parentId":0,"propId":12,"propName":"袖型"},{"parentId":0,"propId":13,"propName":"品牌"},{"parentId":0,"propId":14,"propName":"图案"},{"parentId":0,"propId":15,"propName":"图案文化"}]}
     * code : 01
     * msg : 请求成功
     */

    private String code;
    private String msg;

    public EditGoodsSkuBean getEditGoodsSkuBean() {
        return editGoodsSkuBean;
    }

    public void setEditGoodsSkuBean(EditGoodsSkuBean editGoodsSkuBean) {
        this.editGoodsSkuBean = editGoodsSkuBean;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class EditGoodsSkuBean {
        private String picUrl;
        private int id;
        private int quantity;
        private String delivery;
        private String productName;
        //        自定义参数
        private String activityCode;
        //        原来数量
        private int oldCount = 1;
        private boolean isShopCarEdit;
        private int skuId;
        private String maxDiscounts;
        private boolean isShowBottom;
        private boolean isSellStatus;
        private String presentIds;
        //        积分商品专属
        private int userScore;
        private List<PresentProductInfoBean> presentProductInfoList;
        private List<SkuSaleBean> skuSale;
        private List<PropvaluesBean> propvalues;
        private List<PropsBean> props;
        //        组合优惠
        private List<CombineProductInfoBean> combineProductInfoList;
        //是否是组合搭配
        private boolean isCombine;
        //发货时间
        private String deliveryTime;

        //选中的sku属性
        private String selectedPropValues;

        public String getSelectedPropValues() {
            return selectedPropValues;
        }

        public void setSelectedPropValues(String selectedPropValues) {
            this.selectedPropValues = selectedPropValues;
        }

        public String getDeliveryTime() {
            return deliveryTime;
        }

        public void setDeliveryTime(String deliveryTime) {
            this.deliveryTime = deliveryTime;
        }

        public EditGoodsSkuBean() {
        }

        public EditGoodsSkuBean(CombineCommonBean CombineCommonBean) {
            this.picUrl = CombineCommonBean.getPicUrl();
            this.id = CombineCommonBean.getProductId();
            this.productName = CombineCommonBean.getName();
            this.quantity = CombineCommonBean.getStock();
            this.skuSale = CombineCommonBean.getSkuSale();
            this.propvalues = CombineCommonBean.getPropvalues();
            this.props = CombineCommonBean.getProps();
            //加上这两个字段以后，打开sku弹窗会默认选中当前的sku
            if (CombineCommonBean.getSkuId() > 0) {
                setShopCarEdit(true);
                this.skuId = CombineCommonBean.getSkuId();
            }
            this.isCombine = true;
        }


        public EditGoodsSkuBean(GoodsListBean goodsListBean) {
            if (!TextUtils.isEmpty(goodsListBean.getPicUrl())) {
                this.picUrl = goodsListBean.getPicUrl();
            }

            if (!TextUtils.isEmpty(goodsListBean.getProductName())) {
                this.productName = goodsListBean.getProductName();
            }
            this.id = goodsListBean.getProductId();
//            this.quantity = goodsListBean.get();
            this.skuSale = goodsListBean.getSkuSale();
            this.propvalues = goodsListBean.getPropValues();
            this.props = goodsListBean.getProps();
            this.isCombine = true;
        }

        public boolean isCombine() {
            return isCombine;
        }

        public void setCombine(boolean combine) {
            isCombine = combine;
        }

        public List<CombineProductInfoBean> getCombineProductInfoList() {
            return combineProductInfoList;
        }

        public void setCombineProductInfoList(List<CombineProductInfoBean> combineProductInfoList) {
            this.combineProductInfoList = combineProductInfoList;
        }

        public List<PresentProductInfoBean> getPresentProductInfoList() {
            return presentProductInfoList;
        }

        public void setPresentProductInfoList(List<PresentProductInfoBean> presentProductInfoList) {
            this.presentProductInfoList = presentProductInfoList;
        }

        public int getUserScore() {
            return userScore;
        }

        public void setUserScore(int userScore) {
            this.userScore = userScore;
        }

        public String getPresentIds() {
            return presentIds;
        }

        public void setPresentIds(String presentIds) {
            this.presentIds = presentIds;
        }

        public String getMaxDiscounts() {
            return maxDiscounts;
        }

        public void setMaxDiscounts(String maxDiscounts) {
            this.maxDiscounts = maxDiscounts;
        }

        public int getOldCount() {
            return oldCount;
        }

        public void setOldCount(int oldCount) {
            this.oldCount = oldCount;
        }

        public String getActivityCode() {
            return activityCode;
        }

        public void setActivityCode(String activityCode) {
            this.activityCode = activityCode;
        }

        public boolean isSellStatus() {
            return isSellStatus;
        }

        public void setSellStatus(boolean sellStatus) {
            isSellStatus = sellStatus;
        }

        public boolean isShowBottom() {
            return isShowBottom;
        }

        public void setShowBottom(boolean showBottom) {
            isShowBottom = showBottom;
        }

        public boolean isShopCarEdit() {
            return isShopCarEdit;
        }

        public void setShopCarEdit(boolean shopCarEdit) {
            isShopCarEdit = shopCarEdit;
        }

        public int getSkuId() {
            return skuId;
        }

        public void setSkuId(int skuId) {
            this.skuId = skuId;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public String getDelivery() {
            return delivery;
        }

        public void setDelivery(String delivery) {
            this.delivery = delivery;
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
    }
}
