package com.amkj.dmsh.mine.biz;

import com.amkj.dmsh.mine.adapter.ShopCarGoodsAdapter;
import com.amkj.dmsh.mine.bean.ActivityInfoBean;
import com.amkj.dmsh.mine.bean.ShopCarEntity.ShopCartBean;
import com.amkj.dmsh.mine.bean.ShopCarEntity.ShopCartBean.CartBean;
import com.amkj.dmsh.mine.bean.ShopCarEntity.ShopCartBean.CartBean.CartInfoBean;
import com.amkj.dmsh.shopdetails.GoodsPriceCalculate;
import com.amkj.dmsh.shopdetails.bean.CombineBean;
import com.amkj.dmsh.shopdetails.bean.CombineBean.CombineMatchsBean;
import com.amkj.dmsh.shopdetails.bean.GroupGoodsEntity.GroupGoodsBean.CombineCommonBean;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.amkj.dmsh.constant.ConstantVariable.PRODUCT;


public class ShopCarDao {

    /**
     * 选择全部，点下全部按钮，改变所有商品选中状态
     */
    public static void selectDeleteAll(List<MultiItemEntity> list, boolean isSelect) {
        for (MultiItemEntity multiItemEntity : list) {
            if (multiItemEntity.getItemType() == PRODUCT) {
                ((CartInfoBean) multiItemEntity).setDelete(isSelect);
            }
        }
    }

    /**
     * 正常商品选择
     *
     * @param isChecked true全部选中 false全部取消选中
     */
    public static void selectBuyAll(List<MultiItemEntity> shopGoodsList, boolean isChecked) {
        for (int i = 0; i < shopGoodsList.size(); i++) {
            if (shopGoodsList.get(i).getItemType() == PRODUCT) {
                CartInfoBean cartInfoBean = (CartInfoBean) shopGoodsList.get(i);
                if (cartInfoBean.getStatus() == 1 && isValid(cartInfoBean)) {
                    cartInfoBean.setSelected(isChecked);
                }
            }

        }
    }

    /**
     * 获取所有被选中的删除商品的购物车id以及数量总和
     */
    public static String[] getSelGoodsInfo(List<MultiItemEntity> cartInfoBeans) {
        String[] infos = new String[2];
        StringBuilder carIds = new StringBuilder();
        int totalDelNum = 0;
        int index = 0;
        for (MultiItemEntity cartInfoBean : cartInfoBeans) {
            if (cartInfoBean.getItemType() == PRODUCT && ((CartInfoBean) cartInfoBean).isDelete()) {
                index++;
                if (index == 1) {
                    carIds.append(((CartInfoBean) cartInfoBean).getId());
                } else {
                    carIds.append(",").append(((CartInfoBean) cartInfoBean).getId());
                }
                totalDelNum = totalDelNum + ((CartInfoBean) cartInfoBean).getCount();
            }
        }

        infos[0] = carIds.toString();
        infos[1] = String.valueOf(totalDelNum);
        return infos;
    }


    /**
     * 删除选中的商品
     */
    public static void removeSelGoods(List<MultiItemEntity> cartInfoBeans, ShopCarGoodsAdapter shopCarGoodsAdapter) {
        List<Integer> mainCartIds = new ArrayList<>();
        Iterator<MultiItemEntity> iterator = cartInfoBeans.iterator();
        while (iterator.hasNext()) {
            CartInfoBean cartInfoBean = (CartInfoBean) iterator.next();
            if (cartInfoBean.isDelete() || (cartInfoBean.isCombineProduct() && mainCartIds.contains(cartInfoBean.getId()))) {
                if (cartInfoBean.isMainProduct()) {
                    mainCartIds.add(cartInfoBean.getId());
                }
                iterator.remove();
            }
        }
        shopCarGoodsAdapter.notifyDataSetChanged();
    }


    /**
     * 单个图标的处理
     *
     * @param isEditStatus 是否编辑状态
     */
    public static void selectOne(List<MultiItemEntity> list, int position, boolean isEditStatus) {
        CartInfoBean cartInfoBean = (CartInfoBean) list.get(position);
        if (isEditStatus) {
            boolean isDelete = !(cartInfoBean.isDelete());
            cartInfoBean.setDelete(isDelete);
        } else {
            boolean isSelected = !(cartInfoBean.isSelected());
            cartInfoBean.setSelected(isSelected);
        }
    }

    /**
     * 获取结算信息，肯定需要获取总价和数量，但是数据结构改变了，这里处理也要变；
     *
     * @return 0=选中的商品数量；1=选中的商品总价 2=当前购物车商品总数量
     */
    public static String[] getShoppingCount(List<MultiItemEntity> listGoods) {
        String[] infos = new String[3];
        int selectedCount = 0;
        double totalPrice = 0;
        for (int i = 0; i < listGoods.size(); i++) {
            MultiItemEntity multiItemEntity = listGoods.get(i);
            if (multiItemEntity instanceof ActivityInfoBean) {
                ActivityInfoBean activityInfoBean = (ActivityInfoBean) multiItemEntity;
                List<CartInfoBean> subItems = activityInfoBean.getSubItems();
                for (CartInfoBean cartInfoBean : subItems) {
                    boolean isSelected = cartInfoBean.isSelected();
                    if (isSelected && isValid(cartInfoBean)) {
                        String price = cartInfoBean.getSaleSku().getPrice();
                        int selectedCount1 = cartInfoBean.getCount();
                        double totalPrice1 = GoodsPriceCalculate.getPrice(selectedCount1, Double.parseDouble(price));
                        totalPrice = totalPrice + totalPrice1;
                        selectedCount = selectedCount + selectedCount1;
                    }
                }
            }
        }
        infos[0] = (selectedCount + "");
        infos[1] = new BigDecimal(totalPrice).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "";
        return infos;
    }

    /**
     * 获取结算商品
     */
    public static List<CartInfoBean> getSettlementGoods(List<MultiItemEntity> shopGoodsList) {
        List<CartInfoBean> shopCarGoodsSkuList = new ArrayList<>();
        for (int i = 0; i < shopGoodsList.size(); i++) {
            CartInfoBean cartInfoBean = (CartInfoBean) shopGoodsList.get(i);
            boolean isSelected = cartInfoBean.isSelected();
            if (isSelected) {
                shopCarGoodsSkuList.add(cartInfoBean);
            }
        }
        return shopCarGoodsSkuList;
    }

    /**
     * 获取选中的商品购物车id集合
     */
    public static List<Integer> getCartIds(List<MultiItemEntity> shopGoodsList) {
        List<Integer> cartIds = new ArrayList<>();
        for (MultiItemEntity multiItemEntity : shopGoodsList) {
            if (multiItemEntity.getItemType() == PRODUCT && ((CartInfoBean) multiItemEntity).isSelected() && isValid((CartInfoBean) multiItemEntity)) {
                cartIds.add(((CartInfoBean) multiItemEntity).getId());
            }
        }
        return cartIds;
    }

    /**
     * 判断商品是否有效
     */
    public static boolean isValid(CartInfoBean cartInfoBean) {
        //1.没有失效 2.有库存 3.非待售 4.sku属性正常
        return cartInfoBean.getStatus() == 1 && cartInfoBean.getSaleSku().getQuantity() > 0 && !cartInfoBean.isForSale() && cartInfoBean.getSaleSku() != null && !cartInfoBean.isCombineProduct();
    }

    //匹配购物车id
    public static void notifyItemChange(ShopCarGoodsAdapter shopCarGoodsAdapter, ShopCartBean shopCartBean, CartInfoBean cartInfoBean) {
        List<CartBean> carts = shopCartBean.getCarts();
        //修改有活动信息的商品时，活动规则有可能会发生变化，所以需要更新
        if (carts != null && carts.size() > 0) {
            for (CartBean cartBean : carts) {
                List<CartInfoBean> cartInfoList = cartBean.getCartInfoList();
                if (cartInfoList != null && cartInfoList.size() > 0) {
                    for (CartInfoBean bean : cartInfoList) {
                        if (bean.getId() == cartInfoBean.getId()) {
                            cartInfoBean.update(bean);
                            ActivityInfoBean activityInfoBean = ((ActivityInfoBean) shopCarGoodsAdapter.getData().get(cartInfoBean.getParentPosition()));
                            if (cartBean.getActivityInfo() != null) {
                                activityInfoBean.setActivityRule(cartBean.getActivityInfo().getActivityRule());
                            }
                            shopCarGoodsAdapter.notifyItemChanged(cartInfoBean.getParentPosition());
                            shopCarGoodsAdapter.notifyItemChanged(cartInfoBean.getPosition());
                            break;
                        }
                    }
                }
            }
        }
    }

    /*=====================上面是普通商品购物车相关业务，下面是组合搭配商品购物车业务=========================*/
    public static String getCombines(List<CombineCommonBean> groupList) {
        try {
            CombineBean combineBean = new CombineBean();
            List<CombineMatchsBean> combineMatchs = new ArrayList<>();
            for (CombineCommonBean bean : groupList) {
                if (bean.isMainProduct()) {
                    combineBean.setCount(bean.getCount());
                    combineBean.setProductId(bean.getProductId());
                    combineBean.setSkuId(bean.getSkuId());
                    combineBean.setActivityCode(bean.getActivityCode());
                } else if (bean.isSelected()) {
                    CombineMatchsBean combineMatchsBean = new CombineMatchsBean();
                    combineMatchsBean.setProductId(bean.getProductId());
                    combineMatchsBean.setSkuId(bean.getSkuId());
                    combineMatchs.add(combineMatchsBean);
                }
            }
            combineBean.setCombineMatchs(combineMatchs);
            return new Gson().toJson(combineBean);
        } catch (Exception e) {
            return "";
        }
    }

}
