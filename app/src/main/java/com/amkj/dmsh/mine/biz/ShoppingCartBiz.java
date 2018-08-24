package com.amkj.dmsh.mine.biz;

import android.widget.CheckBox;

import com.amkj.dmsh.mine.bean.ShopCarNewInfoEntity.ShopCarNewInfoBean.CartInfoBean;
import com.amkj.dmsh.mine.bean.ShopCarNewInfoEntity.ShopCarNewInfoBean.CartInfoBean.CartProductInfoBean;
import com.amkj.dmsh.shopdetails.GoodsPriceCalculate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 选择后 都要调用更新
 */
public class ShoppingCartBiz {

    /**
     * 选择全部，点下全部按钮，改变所有商品选中状态
     */
    public static boolean selectAll(List<CartInfoBean> list, boolean isSelectAll) {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setSelected(isSelectAll);
        }
        return !isSelectAll;
    }

    public static void isEditStatus(List<CartInfoBean> list, boolean isEditStatus) {
        for (int i = 0; i < list.size(); i++) {
            CartInfoBean cartInfoBean = list.get(i);
            if (!isEditStatus) {
                cartInfoBean.setEditing(isEditStatus);
                if (cartInfoBean.getStatus() == 1 && cartInfoBean.getSaleSku() != null
                        && cartInfoBean.getSaleSku().getQuantity() > 0 && !cartInfoBean.isForSale()) {
                    cartInfoBean.setSelected(!isEditStatus);
                } else {
                    cartInfoBean.setSelected(isEditStatus);
                }
            } else {
                cartInfoBean.setEditing(isEditStatus);
                cartInfoBean.setSelected(!isEditStatus);
            }
            list.set(i, cartInfoBean);
        }
    }

    /**
     * 正常商品选择
     *
     * @param shopGoodsList
     * @param isChecked
     */
    public static void isNorMalAll(List<CartInfoBean> shopGoodsList, boolean isChecked) {
        for (int i = 0; i < shopGoodsList.size(); i++) {
            CartInfoBean cartInfoBean = shopGoodsList.get(i);
            if (isChecked) {
                if (cartInfoBean.getStatus() == 1 && cartInfoBean.getSaleSku() != null
                        && cartInfoBean.getSaleSku().getQuantity() > 0 && !cartInfoBean.isForSale()) {
                    cartInfoBean.setSelected(isChecked);
                } else {
                    cartInfoBean.setSelected(false);
                }
            } else {
                cartInfoBean.setSelected(isChecked);
            }
            shopGoodsList.set(i, cartInfoBean);
        }
    }

    /**
     * 删除选中商品
     *
     * @param list
     * @return
     */
    public static StringBuffer delSelGoods(List<CartInfoBean> list) {
        StringBuffer carIds = new StringBuffer();
        int index = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isSelected()) {
                index++;
                if (index == 1) {
                    carIds.append(list.get(i).getId());
                } else {
                    carIds.append("," + list.get(i).getId());
                }
            }
        }
        return carIds;
    }

    /**
     * @param list
     * @param position 单选
     * @return
     */
    public static boolean selectOne(List<CartInfoBean> list, int position) {
        boolean isSelectedOne = !(list.get(position).isSelected());
        //单个图标的处理
        list.get(position).setSelected(isSelectedOne);
        return isSelectedOne;
    }

    /**
     * 勾与不勾选中选项
     *
     * @param isSelect 原先状态
     * @param ivCheck
     * @return 是否勾上，之后状态
     */
    public static boolean checkItem(boolean isSelect, CheckBox ivCheck) {
        if (isSelect) {
            ivCheck.setChecked(true);
        } else {
            ivCheck.setChecked(false);
        }
        return isSelect;
    }

    /**=====================上面是界面改动部分，下面是数据变化部分=========================*/

    /**
     * 获取结算信息，肯定需要获取总价和数量，但是数据结构改变了，这里处理也要变；
     *
     * @return 0=选中的商品数量；1=选中的商品总价
     */
    public static String[] getShoppingCount(List<CartInfoBean> listGoods) {
        String[] infos = new String[2];
        int selectedCount = 0;
        double totalPrice = 0;
        for (int i = 0; i < listGoods.size(); i++) {
            boolean isSelected = listGoods.get(i).isSelected();
            if (isSelected && listGoods.get(i).getSaleSku() != null && listGoods.get(i).getSaleSku().getQuantity() > 0) {
                String price = listGoods.get(i).getSaleSku().getPrice();
                int selectedCount1 = listGoods.get(i).getCount();
                double totalPrice1 = GoodsPriceCalculate.getPrice(selectedCount1, Double.parseDouble(price));
                totalPrice = totalPrice + totalPrice1;
                selectedCount = selectedCount + selectedCount1;
            }
        }
        infos[0] = (selectedCount + "");
        infos[1] = new BigDecimal(totalPrice).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "";
        return infos;
    }

    public static String[] getShoppingCountAndCoupon(List<CartInfoBean> listGoods, float couponPrice) {
        String[] infos = new String[2];
        int selectedCount = 0;
        double totalPrice = 0;
        for (int i = 0; i < listGoods.size(); i++) {
            boolean isSelected = listGoods.get(i).isSelected();
            if (isSelected) {
                String price = listGoods.get(i).getSaleSku().getPrice();
                int selectedCount1 = listGoods.get(i).getCount();
                double totalPrice1 = GoodsPriceCalculate.getPrice(selectedCount1, Double.parseDouble(price));
                totalPrice = totalPrice + totalPrice1;
                selectedCount = selectedCount + selectedCount1;
            }
        }
        infos[0] = (selectedCount + "");
        double value = new BigDecimal(totalPrice - couponPrice).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        infos[1] = (value <= 0 ? 0.1 : value) + "";
        return infos;
    }

    /**
     * 获取结算商品
     *
     * @param listGoods
     * @return
     */
    public static List<CartInfoBean> getSettlementGoods(List<CartInfoBean> listGoods) {
        List<CartInfoBean> shopCarGoodsSkuList = new ArrayList<>();
        for (int i = 0; i < listGoods.size(); i++) {
            CartInfoBean cartInfoBean = listGoods.get(i);
            boolean isSelected = cartInfoBean.isSelected();
            if (isSelected) {
                cartInfoBean.setCartId(cartInfoBean.getId());
                shopCarGoodsSkuList.add(cartInfoBean);
            }
        }
        return shopCarGoodsSkuList;
    }

    /**
     * 获取购物车商品件数
     *
     * @param cartInfoBeanList
     * @return
     */
    public static int getCartCount(List<CartInfoBean> cartInfoBeanList) {
        int count = 0;
        if (cartInfoBeanList.size() > 0) {
            for (int i = 0; i < cartInfoBeanList.size(); i++) {
                CartInfoBean cartInfoBean = cartInfoBeanList.get(i);
                if (cartInfoBean.getCombineProductInfoList() != null && cartInfoBean.getCombineProductInfoList().size() > 0) {
                    for (int j = 0; j < cartInfoBean.getCombineProductInfoList().size(); j++) {
                        CartProductInfoBean cartProductInfoBean = cartInfoBean.getCombineProductInfoList().get(j);
                        count += cartProductInfoBean.getCount();
                    }
                }
                if (cartInfoBean.getPresentProductInfoList() != null && cartInfoBean.getPresentProductInfoList().size() > 0) {
                    for (int j = 0; j < cartInfoBean.getPresentProductInfoList().size(); j++) {
                        CartProductInfoBean cartProductInfoBean = cartInfoBean.getPresentProductInfoList().get(j);
                        count += cartProductInfoBean.getCount();
                    }
                }
                count += cartInfoBean.getCount();
            }
            return count;
        }
        return count;
    }
//    //
//    public static List<ShopCarGoodsSkuTransmit> getSettlementGoodsData(List<CartInfoBean> listGoods) {
//        List<ShopCarGoodsSkuTransmit> shopCarGoodsSkuList = new ArrayList<>();
//        ShopCarGoodsSkuTransmit shopCarGoodsSku;
//        for (int i = 0; i < listGoods.size(); i++) {
//            boolean isSelect = listGoods.get(i).isSelect();
//            if (isSelect) {
//                shopCarGoodsSku = new ShopCarGoodsSkuTransmit();
//                shopCarGoodsSku.setSaleSkuId(listGoods.get(i).getSaleSku() != null ? listGoods.get(i).getSaleSku().getId() : 0);
//                shopCarGoodsSku.setCount(listGoods.get(i).getCount());
//                shopCarGoodsSku.setId(listGoods.get(i).getProductId());
//                if (listGoods.get(i).getId() != 0) {
//                    shopCarGoodsSku.setCartId(listGoods.get(i).getId());
//                }
//                shopCarGoodsSkuList.add(shopCarGoodsSku);
//            }
//        }
//        return shopCarGoodsSkuList;
//    }
}
