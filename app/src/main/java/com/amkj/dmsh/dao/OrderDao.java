package com.amkj.dmsh.dao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.bean.BaseAddCarProInfoBean;
import com.amkj.dmsh.bean.MessageBean;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.mine.activity.ShopCarActivity;
import com.amkj.dmsh.mine.bean.ShopCarEntity.ShopCartBean.CartBean.CartInfoBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.activity.DirectExchangeDetailsActivity;
import com.amkj.dmsh.shopdetails.bean.EditGoodsSkuEntity;
import com.amkj.dmsh.shopdetails.bean.ShopCarGoodsSku;
import com.amkj.dmsh.shopdetails.bean.SkuSaleBean;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.views.bottomdialog.SkuDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.amkj.dmsh.constant.ConstantMethod.dismissLoadhud;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.isContextExisted;
import static com.amkj.dmsh.constant.ConstantMethod.showLoadhud;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.showToastRequestMsg;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_W_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.Q_INQUIRY_DELAY_TAKE_TIME;
import static com.amkj.dmsh.constant.Url.Q_INQUIRY_WAIT_SEND_EXPEDITING;
import static com.amkj.dmsh.constant.Url.Q_QUERY_CAR_COUNT;

/**
 * Created by xiaoxin on 2020/3/19
 * Version:v4.5.0
 * ClassDescription :??????????????????Dao???
 */
public class OrderDao {
    //  ????????????
    public static void delOrder(Activity activity, String orderNo, String simpleName) {
        showLoadhud(activity);
        String url = Url.Q_INDENT_DEL;
        Map<String, Object> params = new HashMap<>();
        params.put("no", orderNo);
        params.put("userId", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(activity, url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                dismissLoadhud(activity);
                RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        showToast("??????????????????");
                        if (DirectExchangeDetailsActivity.class.getSimpleName().equals(simpleName)) {
                            activity.finish();
                        } else {
                            EventBus.getDefault().post(new EventMessage(ConstantVariable.UPDATE_INDENT_LIST, simpleName));
                        }
                    } else {
                        showToastRequestMsg(requestStatus);
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                dismissLoadhud(activity);
                showToast(R.string.do_failed);
            }
        });
    }

    //  ????????????
    public static void cancelOrder(Activity activity, String orderNo, String simpleName) {
        showLoadhud(activity);
        String url = Url.Q_INDENT_CANCEL;
        Map<String, Object> params = new HashMap<>();
        params.put("no", orderNo);
        params.put("userId", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(activity, url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                dismissLoadhud(activity);
                RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        showToastRequestMsg(requestStatus);
                        EventBus.getDefault().post(new EventMessage(ConstantVariable.UPDATE_INDENT_LIST, simpleName));
                    } else {
                        showToastRequestMsg(requestStatus);
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                dismissLoadhud(activity);
                showToast(R.string.do_failed);
            }
        });
    }

    //??????????????????
    public static void delayTakeTime(Activity activity, String orderNo) {
        showLoadhud(activity);
        Map<String, Object> params = new HashMap<>();
        params.put("orderNo", orderNo);
        NetLoadUtils.getNetInstance().loadNetDataPost(activity, Q_INQUIRY_DELAY_TAKE_TIME, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                dismissLoadhud(activity);
                RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                showToast(requestStatus.getMsg());
            }

            @Override
            public void onNotNetOrException() {
                dismissLoadhud(activity);
                showToast(R.string.do_failed);
            }
        });
    }

    //????????????
    public static void setRemindDelivery(Activity activity, String orderNo) {
        showLoadhud(activity);
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("orderNo", orderNo);
        NetLoadUtils.getNetInstance().loadNetDataPost(activity, Q_INQUIRY_WAIT_SEND_EXPEDITING, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                dismissLoadhud(activity);
                RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                showToast(requestStatus.getMsg());
            }

            @Override
            public void onNotNetOrException() {
                dismissLoadhud(activity);
                showToast(R.string.do_failed);
            }
        });
    }


    //  ????????????
    public static void urgeRefund(Activity activity, String refundNo) {
        showLoadhud(activity);
        Map<String, Object> params = new HashMap<>();
        params.put("refundNo", refundNo);
        NetLoadUtils.getNetInstance().loadNetDataPost(activity, Url.Q_INDENT_URGE_REFUND_PRICE, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                dismissLoadhud(activity);
                RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                showToast(requestStatus.getMsg());
            }

            @Override
            public void onNotNetOrException() {
                dismissLoadhud(activity);
                showToast(R.string.do_failed);
            }
        });
    }

    //  ???????????????????????????
    public static void addOrderCart(Activity activity, String orderNo) {
        showLoadhud(activity);
        Map<String, Object> params = new HashMap<>();
        params.put("orderNo", orderNo);
        NetLoadUtils.getNetInstance().loadNetDataPost(activity, Url.Q_ADD_ORDER_PRODUCT_TOCART, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                dismissLoadhud(activity);
                RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (SUCCESS_CODE.equals(requestStatus.getCode())) {
                        showToast("?????????????????????~");
                        Intent intent = new Intent(activity, ShopCarActivity.class);
                        activity.startActivity(intent);
                        getCarCount(activity);
                    } else {
                        showToast(requestStatus.getMsg());
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                dismissLoadhud(activity);
                showToast(R.string.do_failed);
            }
        });
    }


    /**
     * @param baseAddCarProInfoBean ??????????????????
     */
    public static void addShopCarGetSku(final Activity context, final BaseAddCarProInfoBean baseAddCarProInfoBean) {
        if (userId <= 0 || !isContextExisted(context)) {
            getLoginStatus(context);
            return;
        }
        showLoadhud(context);
        //??????????????????
        String url = Url.Q_SHOP_DETAILS_GET_SKU_CAR;
        Map<String, Object> params = new HashMap<>();
        params.put("productId", baseAddCarProInfoBean.getProductId());
        params.put("uid", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(context, url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                dismissLoadhud(context);
                EditGoodsSkuEntity editGoodsSkuEntity = GsonUtils.fromJson(result, EditGoodsSkuEntity.class);
                if (editGoodsSkuEntity != null) {
                    if (editGoodsSkuEntity.getCode().equals(SUCCESS_CODE)) {
                        EditGoodsSkuEntity.EditGoodsSkuBean editGoodsSkuBean = editGoodsSkuEntity.getEditGoodsSkuBean();
                        List<SkuSaleBean> skuSaleBeanList = editGoodsSkuBean.getSkuSale();
                        if (skuSaleBeanList == null) {
                            return;
                        }
                        //????????????????????????
                        if (editGoodsSkuBean.getQuantity() > 0) {
                            //?????????????????????sku,???????????????????????????
                            if (skuSaleBeanList.size() == 1 && baseAddCarProInfoBean.isShowSingle()) {
                                //????????????
                                ShopCarGoodsSku shopCarGoodsSkuDif = new ShopCarGoodsSku();
                                shopCarGoodsSkuDif.setCount(1);
                                shopCarGoodsSkuDif.setSaleSkuId(editGoodsSkuBean.getSkuSale().get(0).getId());
                                shopCarGoodsSkuDif.setPrice(Double.parseDouble(editGoodsSkuBean.getSkuSale().get(0).getPrice()));
                                shopCarGoodsSkuDif.setProductId(editGoodsSkuBean.getId());
                                shopCarGoodsSkuDif.setPicUrl(editGoodsSkuBean.getPicUrl());
                                shopCarGoodsSkuDif.setActivityCode(getStrings(baseAddCarProInfoBean.getActivityCode()));
                                shopCarGoodsSkuDif.setValuesName(!TextUtils.isEmpty(editGoodsSkuBean.getPropvalues().get(0).getPropValueName())
                                        ? editGoodsSkuBean.getPropvalues().get(0).getPropValueName() : "??????");
                                addShopCar(context, shopCarGoodsSkuDif);
                            } else {
                                setSkuValue(context, editGoodsSkuBean, baseAddCarProInfoBean);
                            }
                        } else {
                            showToast("???????????????????????????????????????~~~");
                        }
                    } else {
                        showToast(editGoodsSkuEntity.getMsg());
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                dismissLoadhud(context);
            }
        });
    }

    private static void setSkuValue(Activity context, final EditGoodsSkuEntity.EditGoodsSkuBean editGoodsSkuBean, final BaseAddCarProInfoBean baseAddCarProInfoBean) {
        //        sku ??????
        SkuDialog skuDialog = new SkuDialog(context);
        if (!TextUtils.isEmpty(baseAddCarProInfoBean.getProPic())) {
            editGoodsSkuBean.setPicUrl(baseAddCarProInfoBean.getProPic());
        }
        if (TextUtils.isEmpty(editGoodsSkuBean.getProductName())) {
            editGoodsSkuBean.setProductName(getStrings(baseAddCarProInfoBean.getProName()));
        }
        editGoodsSkuBean.setShowBottom(true);
        skuDialog.refreshView(editGoodsSkuBean);
        skuDialog.show(baseAddCarProInfoBean.isShowSingle(), "???????????????");
        skuDialog.getGoodsSKu(shopCarGoodsSku -> {
            if (shopCarGoodsSku != null) {
                if (!TextUtils.isEmpty(shopCarGoodsSku.getProType())) {
                    switch (shopCarGoodsSku.getProType()) {
                        //???????????????
                        case "addCar":
                            showLoadhud(context);
                            shopCarGoodsSku.setProductId(baseAddCarProInfoBean.getProductId());
                            shopCarGoodsSku.setActivityCode(getStrings(baseAddCarProInfoBean.getActivityCode()));
                            addShopCar(context, shopCarGoodsSku);
                            break;
                        //????????????
                        case "buyGoIt":
                            buyGoIt(context, shopCarGoodsSku, baseAddCarProInfoBean.getProductId());
                            break;
                    }
                }
            }
        });
    }

    //????????????
    private static void buyGoIt(Activity context, ShopCarGoodsSku shopCarGoodsSku, int productId) {
        List<CartInfoBean> settlementGoods = new ArrayList<>();
        CartInfoBean cartInfoBean = new CartInfoBean();
        cartInfoBean.setProductId(productId);
        cartInfoBean.setCount(shopCarGoodsSku.getCount());
        cartInfoBean.setId(shopCarGoodsSku.getSaleSkuId());
        cartInfoBean.setSaleSku(new SkuSaleBean(shopCarGoodsSku.getQuantity(), shopCarGoodsSku.getPrice() + "", shopCarGoodsSku.getSaleSkuId()));
        settlementGoods.add(cartInfoBean);
        //???????????? ??????????????????
        Bundle bundle = new Bundle();
        bundle.putString("goods", GsonUtils.toJson(settlementGoods));
        ConstantMethod.skipIndentWrite(context, INDENT_W_TYPE, bundle);
    }

    //???????????????
    private static void addShopCar(Activity activity, final ShopCarGoodsSku shopCarGoodsSku) {
        if (userId != 0) {
            String url = Url.Q_SHOP_DETAILS_ADD_CAR;
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            params.put("productId", shopCarGoodsSku.getProductId());
            params.put("saleSkuId", shopCarGoodsSku.getSaleSkuId());
            params.put("count", shopCarGoodsSku.getCount());
            params.put("price", shopCarGoodsSku.getPrice());
            if (!TextUtils.isEmpty(shopCarGoodsSku.getActivityCode())) {
                params.put("activityCode", shopCarGoodsSku.getActivityCode());
            }
            NetLoadUtils.getNetInstance().loadNetDataPost(activity, url, params, new NetLoadListenerHelper() {
                @Override
                public void onSuccess(String result) {
                    dismissLoadhud(activity);
                    RequestStatus status = GsonUtils.fromJson(result, RequestStatus.class);
                    if (status != null) {
                        if (status.getCode().equals(SUCCESS_CODE)) {
                            showToast("?????????????????????~");
                            //???????????????????????????
                            getCarCount(activity);
                        } else {
                            showToastRequestMsg(status);
                        }
                    }
                }

                @Override
                public void onNotNetOrException() {
                    dismissLoadhud(activity);
                }
            });
        }
    }

    //???????????????????????????
    public static void getCarCount(Activity activity) {
        if (userId > 0) {
            //?????????????????????
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            NetLoadUtils.getNetInstance().loadNetDataPost(activity, Q_QUERY_CAR_COUNT, params, new NetLoadListenerHelper() {
                @Override
                public void onSuccess(String result) {

                    MessageBean requestStatus = GsonUtils.fromJson(result, MessageBean.class);
                    if (requestStatus != null) {
                        if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                            int cartNumber = requestStatus.getResult();
                            EventBus.getDefault().post(new EventMessage(ConstantVariable.UPDATE_CAR_NUM, cartNumber));
                        }
                    }
                }
            });
        }
    }


}
