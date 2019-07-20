package com.amkj.dmsh.shopdetails.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoxin on 2019/7/3
 * Version:v4.1.0
 * ClassDescription :获取订单结算信息传入的组合商品数据
 */
public class CombineGoodsBean implements Parcelable {

    /**
     * mainId : 51
     * productId : 18001
     * skuId : 10745
     * count : 6
     * matchProducts : [{"combineMatchId":108,"productId":17996,"skuId":10728},{"combineMatchId":109,"productId":17996,"skuId":10729},{"combineMatchId":111,"productId":17998,"skuId":10738}]
     */

    private int mainId;
    private int productId;
    private int skuId;
    private int count;
    private List<MatchProductsBean> matchProducts;


    public int getMainId() {
        return mainId;
    }

    public void setMainId(int mainId) {
        this.mainId = mainId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getSkuId() {
        return skuId;
    }

    public void setSkuId(int skuId) {
        this.skuId = skuId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<MatchProductsBean> getMatchProducts() {
        if (matchProducts == null) {
            matchProducts = new ArrayList<>();
        }

        return matchProducts;
    }

    public void setMatchProducts(List<MatchProductsBean> matchProducts) {
        this.matchProducts = matchProducts;
    }

    public static class MatchProductsBean implements Parcelable {
        /**
         * combineMatchId : 108
         * productId : 17996
         * skuId : 10728
         */

        private int combineMatchId;
        private int productId;
        private int skuId;

        public MatchProductsBean() {
        }

        public MatchProductsBean(int combineMatchId, int productId, int skuId) {
            this.combineMatchId = combineMatchId;
            this.productId = productId;
            this.skuId = skuId;
        }

        public int getCombineMatchId() {
            return combineMatchId;
        }

        public void setCombineMatchId(int combineMatchId) {
            this.combineMatchId = combineMatchId;
        }

        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }

        public int getSkuId() {
            return skuId;
        }

        public void setSkuId(int skuId) {
            this.skuId = skuId;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.combineMatchId);
            dest.writeInt(this.productId);
            dest.writeInt(this.skuId);
        }

        protected MatchProductsBean(Parcel in) {
            this.combineMatchId = in.readInt();
            this.productId = in.readInt();
            this.skuId = in.readInt();
        }

        public static final Creator<MatchProductsBean> CREATOR = new Creator<MatchProductsBean>() {
            @Override
            public MatchProductsBean createFromParcel(Parcel source) {
                return new MatchProductsBean(source);
            }

            @Override
            public MatchProductsBean[] newArray(int size) {
                return new MatchProductsBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mainId);
        dest.writeInt(this.productId);
        dest.writeInt(this.skuId);
        dest.writeInt(this.count);
        dest.writeList(this.matchProducts);
    }

    public CombineGoodsBean() {
    }

    protected CombineGoodsBean(Parcel in) {
        this.mainId = in.readInt();
        this.productId = in.readInt();
        this.skuId = in.readInt();
        this.count = in.readInt();
        this.matchProducts = new ArrayList<MatchProductsBean>();
        in.readList(this.matchProducts, MatchProductsBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<CombineGoodsBean> CREATOR = new Parcelable.Creator<CombineGoodsBean>() {
        @Override
        public CombineGoodsBean createFromParcel(Parcel source) {
            return new CombineGoodsBean(source);
        }

        @Override
        public CombineGoodsBean[] newArray(int size) {
            return new CombineGoodsBean[size];
        }
    };
}
