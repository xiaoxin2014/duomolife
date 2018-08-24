package com.amkj.dmsh.shopdetails;

import java.math.BigDecimal;

/**
 * Created by atd48 on 2016/10/14.
 */
public class GoodsPriceCalculate {
    public static double getPrice(int count, double price) {
//        保留两位小数
        return new BigDecimal(count * price).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    //    添加优惠券面额计算
    public static double getPriceAddCoupon(int count, double price, float couponAmount) {
//        保留两位小数
        return new BigDecimal(count * price - couponAmount).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
