package com.amkj.dmsh.shopdetails.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.address.activity.SelectedAddressActivity;
import com.amkj.dmsh.address.bean.AddressInfoEntity.AddressInfoBean;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.bean.AreaTipsEntity;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dao.ShopCarDao;
import com.amkj.dmsh.dominant.activity.DoMoGroupJoinShareActivity;
import com.amkj.dmsh.dominant.activity.QualityGroupShopMineActivity;
import com.amkj.dmsh.dominant.activity.QualityProductActActivity;
import com.amkj.dmsh.dominant.bean.GroupShopDetailsEntity.GroupShopDetailsBean;
import com.amkj.dmsh.mine.bean.ActivityInfoBean;
import com.amkj.dmsh.mine.bean.ShopCarEntity.ShopCartBean.CartBean.CartInfoBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.adapter.IndentDiscountAdapter;
import com.amkj.dmsh.shopdetails.adapter.WriteProductListAdapter;
import com.amkj.dmsh.shopdetails.bean.CombineGoodsBean;
import com.amkj.dmsh.shopdetails.bean.EditGoodsSkuEntity.EditGoodsSkuBean;
import com.amkj.dmsh.shopdetails.bean.IndentProDiscountBean;
import com.amkj.dmsh.shopdetails.bean.IndentWriteEntity;
import com.amkj.dmsh.shopdetails.bean.IndentWriteEntity.IndentWriteBean;
import com.amkj.dmsh.shopdetails.bean.IndentWriteEntity.IndentWriteBean.PrerogativeActivityInfo;
import com.amkj.dmsh.shopdetails.bean.IndentWriteEntity.IndentWriteBean.PrerogativeActivityInfo.GoodsListBean;
import com.amkj.dmsh.shopdetails.bean.IndentWriteEntity.IndentWriteBean.ProductsBean;
import com.amkj.dmsh.shopdetails.bean.IndentWriteEntity.IndentWriteBean.ProductsBean.ProductInfoBean;
import com.amkj.dmsh.shopdetails.bean.IndentWriteEntity.IndentWriteBean.UserCouponInfoBean;
import com.amkj.dmsh.shopdetails.bean.PriceInfoBean;
import com.amkj.dmsh.shopdetails.bean.QualityCreateAliPayIndentBean;
import com.amkj.dmsh.shopdetails.bean.QualityCreateUnionPayIndentEntity;
import com.amkj.dmsh.shopdetails.bean.QualityCreateWeChatPayIndentBean;
import com.amkj.dmsh.shopdetails.bean.SkuSaleBean;
import com.amkj.dmsh.shopdetails.integration.bean.AddressListEntity;
import com.amkj.dmsh.shopdetails.payutils.AliPay;
import com.amkj.dmsh.shopdetails.payutils.UnionPay;
import com.amkj.dmsh.shopdetails.payutils.WXPay;
import com.amkj.dmsh.utils.KeyboardUtils;
import com.amkj.dmsh.utils.LifecycleHandler;
import com.amkj.dmsh.utils.TextWatchListener;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.amkj.dmsh.views.RectAddAndSubWriteView;
import com.amkj.dmsh.views.alertdialog.AlertDialogHelper;
import com.amkj.dmsh.views.alertdialog.AlertDialogPurchase;
import com.amkj.dmsh.views.alertdialog.AlertDialogRealName;
import com.amkj.dmsh.views.bottomdialog.SkuDialog;
import com.google.gson.reflect.TypeToken;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfigC;
import com.luck.picture.lib.entity.LocalMediaC;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.dismissLoadhud;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStringChangeIntegers;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showLoadhud;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.DIRECT_COUPON_REQ;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_GROUP_SHOP;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_PRODUCT_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_PROPRIETOR_PRODUCT;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_W_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.JOIN_GROUP;
import static com.amkj.dmsh.constant.ConstantVariable.NEW_CRE_ADDRESS_REQ;
import static com.amkj.dmsh.constant.ConstantVariable.OPEN_GROUP;
import static com.amkj.dmsh.constant.ConstantVariable.PAY_ALI_PAY;
import static com.amkj.dmsh.constant.ConstantVariable.PAY_UNION_PAY;
import static com.amkj.dmsh.constant.ConstantVariable.PAY_WX_PAY;
import static com.amkj.dmsh.constant.ConstantVariable.SEL_ADDRESS_REQ;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.ADDRESS_LIST;
import static com.amkj.dmsh.constant.Url.PAY_CANCEL;
import static com.amkj.dmsh.constant.Url.Q_CREATE_GROUP_NEW_INDENT;
import static com.amkj.dmsh.constant.Url.Q_CREATE_INDENT;
import static com.amkj.dmsh.constant.Url.Q_NEW_RE_BUY_INDENT;
import static com.amkj.dmsh.constant.Url.Q_PAYMENT_INDENT;
import static com.amkj.dmsh.dao.ShopCarDao.getCouponGoodsInfo;


/**
 * Created by atd48 on 2016/8/17.
 * ????????????
 */
public class DirectIndentWriteActivity extends BaseActivity {
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tv_header_shared)
    TextView header_shared;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    @BindView(R.id.ll_indent_details)
    LinearLayout ll_indent_details;
    //??????
    @BindView(R.id.tv_indent_write_commit)
    TextView tv_indent_write_commit;
    //    ??????????????????
    @BindView(R.id.tv_indent_total_price)
    TextView tv_indent_total_price;
    @BindView(R.id.ll_write_commit)
    LinearLayout mLlWriteCommit;
    private boolean isFirst = true;//????????????????????????true,???????????????false
    private int addressId;
    private String type = "";
    private PullHeaderView pullHeaderView;
    private PullFootView pullFootView;
    private QualityCreateWeChatPayIndentBean qualityWeChatIndent;
    private QualityCreateAliPayIndentBean qualityAliPayIndent;
    private String payWay = PAY_ALI_PAY;//?????????????????????
    //    ???????????? ?????????
    private String orderCreateNo = "";
    //     ????????????????????????
    private String orderNo = "";

    //    ?????????id
    private int couponId;
    private GroupShopDetailsBean groupShopDetailsBean;
    private IndentDiscountAdapter indentDiscountAdapter;
    private IndentWriteEntity identWriteEntity;
    private boolean isReal = false;
    private IndentWriteBean indentWriteBean;
    private AlertDialogHelper payCancelDialogHelper;
    private QualityCreateUnionPayIndentEntity qualityUnionIndent;
    private WriteProductListAdapter directProductAdapter;
    //    ??????????????????
    private List<CartInfoBean> passGoods = new ArrayList<>();
    //    ??????????????????
    private List<CombineGoodsBean> combineGoods = new ArrayList<>();
    //    ??????????????????????????????
    private List<IndentProDiscountBean> discountBeanList = new ArrayList<>();
    //    ????????????????????????
    private List<PriceInfoBean> priceInfoList = new ArrayList<>();
    //    ????????????
    private List<ProductInfoBean> productInfoList = new ArrayList<>();
    private int mNum;
    private boolean first = true;
    private AlertDialogPurchase mAlertDialogPurchase;
    //????????????????????????id
    private int purchaseProductId;
    private AlertDialogRealName mAlertDialogRealName;
    private AlertDialogHelper mAlertDialogRealNameError;
    private AlertDialogHelper mAlertDialogRealNameDiffer;
    //??????????????????
    private Map<String, Object> settleMap = new HashMap<>();
    private AlertDialogHelper mAlertDialogAreaTip;


    @Override
    protected int getContentView() {
        return R.layout.activity_direct_indent_write;
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
        if (loadHud != null) {
            loadHud.setCancellable(false);
        }
        isReal = false;
        tv_header_titleAll.setText("????????????");
        header_shared.setVisibility(View.INVISIBLE);
        Intent intent = getIntent();
        try {
            String goodsJson = intent.getStringExtra("goods");
            String combineGoodsJson = intent.getStringExtra("combineGoods");
            String gpShopInfo = intent.getStringExtra("gpShopInfo");
            passGoods = GsonUtils.fromJson(goodsJson, new TypeToken<List<CartInfoBean>>() {
            }.getType());
            combineGoods = GsonUtils.fromJson(combineGoodsJson, new TypeToken<List<CombineGoodsBean>>() {
            }.getType());
            groupShopDetailsBean = GsonUtils.fromJson(gpShopInfo, GroupShopDetailsBean.class);
            orderNo = intent.getStringExtra("orderNo");
        } catch (Exception e) {
            showToast("??????????????????????????????");
            finish();
        }
//        ?????????
        View headerView = LayoutInflater.from(this).inflate(R.layout.layout_direct_indent_write_header_address, (ViewGroup) communal_recycler.getParent(), false);
        pullHeaderView = new PullHeaderView();
        ButterKnife.bind(pullHeaderView, headerView);
//        ????????????
        View footView = LayoutInflater.from(this).inflate(R.layout.layout_direct_indent_write_foot, (ViewGroup) communal_recycler.getParent(), false);
        pullFootView = new PullFootView();
        ButterKnife.bind(pullFootView, footView);
        pullFootView.init();

        if ((passGoods != null && passGoods.size() > 0) || (combineGoods != null && combineGoods.size() > 0) || !TextUtils.isEmpty(orderNo)) {
            type = INDENT_W_TYPE;
            if (passGoods != null) {
                discountBeanList.clear();
                for (int i = 0; i < passGoods.size(); i++) {
                    CartInfoBean cartInfoBean = passGoods.get(i);
                    IndentProDiscountBean indentProBean = new IndentProDiscountBean();
                    indentProBean.setId(cartInfoBean.getProductId());
                    indentProBean.setSaleSkuId(cartInfoBean.getSaleSku().getId());
                    indentProBean.setCount(cartInfoBean.getCount());
                    indentProBean.setCartId(cartInfoBean.getId());
                    discountBeanList.add(indentProBean);
                }
            }
        } else if (groupShopDetailsBean != null) {
            type = INDENT_GROUP_SHOP;
            IndentProDiscountBean indentProBean = new IndentProDiscountBean();
            indentProBean.setId(groupShopDetailsBean.getProductId());
            indentProBean.setSaleSkuId(groupShopDetailsBean.getGpSkuId());
            indentProBean.setCount(1);
            discountBeanList.add(indentProBean);
        } else {
            showToast("??????????????????????????????");
            finish();
        }
        directProductAdapter = new WriteProductListAdapter(getActivity(), productInfoList, type);
        pullFootView.rect_indent_number.setFontColor(Color.parseColor("#333333"));
        communal_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // ?????????????????????ID
                .setDividerId(R.drawable.item_divider_gray_f_one_px).create());
        directProductAdapter.addHeaderView(headerView);
        directProductAdapter.addFooterView(footView);
        communal_recycler.setAdapter(directProductAdapter);
        directProductAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            ProductInfoBean productInfoBean = (ProductInfoBean) view.getTag();
            if (productInfoBean != null) {
                switch (view.getId()) {
                    case R.id.ll_communal_activity_tag_rule:
                        ActivityInfoBean activityInfoBean = productInfoBean.getActivityInfoBean();
                        if (activityInfoBean != null && !TextUtils.isEmpty(activityInfoBean.getActivityCode())) {
                            Intent intent1 = new Intent(this, QualityProductActActivity.class);
                            intent1.putExtra("activityCode", activityInfoBean.getActivityCode());
                            startActivity(intent1);
                        }
                        break;
                    //???????????????????????????????????????
                    case R.id.rl_cover:
                        if (!TextUtils.isEmpty(productInfoBean.getNotBuyAreaInfo())) {
                            for (IndentProDiscountBean discountBean : discountBeanList) {
                                if (productInfoBean.getId() == discountBean.getId()) {
                                    discountBeanList.remove(discountBean);
                                    if (discountBeanList.size() > 0 || combineGoods.size() > 0) {
                                        getIndentDiscounts(false);
                                    } else {
                                        finish();
                                    }
                                    break;
                                }
                            }
                        }
                        break;
                }
            }
        });

        pullFootView.rect_indent_number.setOnNumChangeListener(new RectAddAndSubWriteView.OnNumChangeListener() {
            @Override
            public void onNumChange(View view, int stype, int num) {
                if (discountBeanList.size() > 0 && discountBeanList.get(0).getCount() != num) {
                    if (loadHud != null) {
                        loadHud.show();
                    }
                    discountBeanList.get(0).setCount(num);
                    getIndentDiscounts(false);
                }
            }

            @Override
            public void onMaxQuantity(View view, int num) {
                showToast(R.string.product_sell_out);
            }
        });
        pullFootView.rv_indent_write_info.setLayoutManager(new LinearLayoutManager(getActivity()));
        indentDiscountAdapter = new IndentDiscountAdapter(this, priceInfoList);
        pullFootView.rv_indent_write_info.setAdapter(indentDiscountAdapter);

        //???????????????
        KeyboardUtils.registerSoftInputChangedListener(this, new KeyboardUtils.OnSoftInputChangedListener() {
            @Override
            public void onSoftInputChanged(int height) {
                try {
                    if (!first) {//?????????????????????
                        if (height == 0) {
                            //???????????????
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, AutoSizeUtils.mm2px(mAppContext, 98));
                            mLlWriteCommit.setLayoutParams(params);
                            if (mNum <= 0) {//?????????????????????1???
                                mNum = 1;
                                pullFootView.rect_indent_number.setNum(mNum);
                            } else if (mNum > pullFootView.rect_indent_number.getMaxNum()) {
                                mNum = pullFootView.rect_indent_number.getMaxNum();
                                pullFootView.rect_indent_number.setNum(mNum);
                                showToast(R.string.product_sell_out);
                            }
                            if (discountBeanList.size() > 0 && discountBeanList.get(0).getCount() != mNum) {
                                if (loadHud != null) {
                                    loadHud.show();
                                }
                                discountBeanList.get(0).setCount(mNum);
                                getIndentDiscounts(false);
                            }
                        } else {
                            //???????????????
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
                            mLlWriteCommit.setLayoutParams(params);
                        }
                    }
                    first = false;
                } catch (Exception e) {
                    CrashReport.postCatchedException(new Exception("???????????????????????????????????????" + e.getMessage()));
                }
            }
        });
    }


    @Override
    protected void loadData() {
        if (type.equals(INDENT_GROUP_SHOP)) {
            pullFootView.ll_layout_coupon.setVisibility(GONE);
        }
        getAddress();
    }

    /**
     * ????????????????????????
     *
     * @param updatePriceInfo (????????????????????????????????????,???????????????????????????????????????true)
     */
    private void getIndentDiscounts(boolean updatePriceInfo) {
        if (!TextUtils.isEmpty(type)) {
            settleMap.clear();
            settleMap.put("userId", userId);
            settleMap.put("addressId", addressId);
            if (updatePriceInfo) {
                settleMap.put("userCouponId", couponId);
            }
            if (groupShopDetailsBean != null) {
                settleMap.put("gpInfoId", groupShopDetailsBean.getGpInfoId());
                settleMap.put("gpRecordId", groupShopDetailsBean.getGpRecordId());
            }
            if (discountBeanList != null && discountBeanList.size() > 0) {
                settleMap.put("goods", GsonUtils.toJson(discountBeanList));
            }
            if (combineGoods != null && combineGoods.size() > 0) {
                settleMap.put("combineGoods", GsonUtils.toJson(combineGoods));
            }

            NetLoadUtils.getNetInstance().loadNetDataPost(this, updatePriceInfo ? Url.INDENT_DISCOUNTS_UPDATE_INFO : Url.INDENT_DISCOUNTS_NEW_INFO, settleMap, new NetLoadListenerHelper() {
                @Override
                public void onSuccess(String result) {
                    dismissLoadhud(getActivity());
                    productInfoList.clear();
                    identWriteEntity = GsonUtils.fromJson(result, IndentWriteEntity.class);
                    if (identWriteEntity != null) {
                        if (identWriteEntity.getCode().equals(SUCCESS_CODE)) {
                            indentWriteBean = identWriteEntity.getIndentWriteBean();
                            if (indentWriteBean != null) {
                                List<ProductsBean> products = indentWriteBean.getProducts();
                                if (products != null && products.size() > 0) {
                                    setDiscountsInfo(indentWriteBean);
                                }
                                showPayType(indentWriteBean);
                            }
                        } else if (identWriteEntity.getCode().equals(EMPTY_CODE)) {
                            showToast(R.string.invalidData);
                        } else {
                            showToast(identWriteEntity.getMsg());
                        }
                    }

                    directProductAdapter.notifyDataSetChanged();
                    NetLoadUtils.getNetInstance().showLoadSir(loadService, identWriteEntity);
                }

                @Override
                public void onNotNetOrException() {
                    dismissLoadhud(getActivity());
                    NetLoadUtils.getNetInstance().showLoadSir(loadService, identWriteEntity);
                }
            });
        } else {
            dismissLoadhud(getActivity());
            NetLoadUtils.getNetInstance().showLoadSir(loadService, identWriteEntity);
        }
    }

    //?????????????????????
    private void showPurchaseDialog(IndentWriteBean indentWriteBean) {
        PrerogativeActivityInfo purchaseBean = indentWriteBean.getPurchaseBean();
        if (purchaseBean != null) {
            List<GoodsListBean> goodsList = purchaseBean.getGoodsList();
            if (goodsList != null && goodsList.size() > 0) {
                if (mAlertDialogPurchase == null) {
                    mAlertDialogPurchase = new AlertDialogPurchase(this);
                }
                mAlertDialogPurchase.setAddOrderListener(() -> {
                    mAlertDialogPurchase.dismiss();
                    int currentItem = mAlertDialogPurchase.getCurrentItem();
                    GoodsListBean goodsListBean = goodsList.get(currentItem);
                    List<SkuSaleBean> skuSale = goodsListBean.getSkuSale();
                    if (skuSale == null) return;
                    //?????????sku
                    if (skuSale.size() > 1) {
                        EditGoodsSkuBean editGoodsSkuBean = new EditGoodsSkuBean(goodsListBean);
                        SkuDialog skuDialog = new SkuDialog(this);
                        editGoodsSkuBean.setShowBottom(true);
                        skuDialog.refreshView(editGoodsSkuBean);
                        skuDialog.show();
                        skuDialog.getGoodsSKu(shopCarGoodsSku -> {
                            if (shopCarGoodsSku != null) {
                                //????????????????????????
                                increasePurchase(goodsListBean.getProductId(), shopCarGoodsSku.getSaleSkuId());
                            }
                        });
                        //????????????sku
                    } else if (skuSale.size() == 1) {
                        SkuSaleBean skuSaleBean = skuSale.get(0);
                        //????????????????????????
                        increasePurchase(goodsListBean.getProductId(), skuSaleBean.getId());
                    }
                });
                mAlertDialogPurchase.show();
                mAlertDialogPurchase.updateData(purchaseBean);
            }
        }
    }


    //????????????????????????????????????????????????
    private void increasePurchase(int productId, int SaleSkuId) {
        loadHud.show();
        IndentProDiscountBean indentProBean = new IndentProDiscountBean();
        indentProBean.setId(productId);
        indentProBean.setSaleSkuId(SaleSkuId);
        indentProBean.setCount(1);
        indentProBean.setIsPrerogative(1);
        discountBeanList.add(indentProBean);
        purchaseProductId = productId;//??????????????????????????????id
        getIndentDiscounts(false);
    }

    //????????????????????????
    private void setDiscountsInfo(IndentWriteBean indentWriteBean) {
        showPurchaseDialog(indentWriteBean);
        //???????????????
        isReal = indentWriteBean.isReal();
        //?????????????????????????????????????????????????????????
        tv_indent_write_commit.setBackgroundResource(indentWriteBean.getAllProductNotBuy() == 0 ? R.color.text_normal_red : R.color.text_gray_c);
        //????????????
        setOverseaData(indentWriteBean);
        //????????????
        setDiscounts(indentWriteBean.getPriceInfos());
        //???????????????????????????
        UserCouponInfoBean userCouponInfo = indentWriteBean.getUserCouponInfo();
        if (userCouponInfo != null) {
            if (userCouponInfo.getAllowCouoon() == 1 && userCouponInfo.getId() != null && userCouponInfo.getId() > 0) {
                //??????????????????????????????   ?????????-xx ????????????xx-xx
                pullFootView.tv_direct_product_favorable.setText((userCouponInfo.getStartFee() + "-??" + userCouponInfo.getPrice()));
                pullFootView.tv_direct_product_favorable.setSelected(true);
                couponId = userCouponInfo.getId();
            } else {
                pullFootView.tv_direct_product_favorable.setSelected(false);
                pullFootView.tv_direct_product_favorable.setText(couponId == -1 ? "??????????????????" : userCouponInfo.getMsg());
            }
        }

        List<ProductsBean> products = indentWriteBean.getProducts();
        //?????????????????????????????????????????????????????????
        List[] indentInfo = ShopCarDao.getIndentGoodsInfo(products, purchaseProductId);
        discountBeanList = indentInfo[0];
        combineGoods = indentInfo[1];
        //????????????????????????
        for (int i = 0; i < products.size(); i++) {
            ProductsBean productsBean = products.get(i);
            List<ProductInfoBean> productInfos = productsBean.getProductInfos();
            ProductInfoBean combineMainProduct = productsBean.getCombineMainProduct();
            List<ProductInfoBean> combineMatchProducts = productsBean.getCombineMatchProducts();
            //????????????????????????
            if (combineMainProduct != null) {
                productInfos = new ArrayList<>();
                productInfos.add(combineMainProduct);
                if (combineMatchProducts != null) {
                    for (ProductInfoBean productInfoBean : combineMatchProducts) {
                        productInfoBean.setCount(combineMainProduct.getCount());
                        productInfos.add(productInfoBean);
                    }
                }
            }

            //????????????????????????????????????
            for (int j = 0; j < productInfos.size(); j++) {
                ProductInfoBean productInfoBean = productInfos.get(j);
                if (productsBean.getActivityInfo() != null && j == 0) {
                    productInfoBean.setShowActInfo(1);
                }
                productInfoBean.setActivityInfoBean(productsBean.getActivityInfo());
                productInfoList.add(productInfoBean);
            }

            if (productsBean.getActivityInfo() != null && productInfoList.size() > 0
                    && indentWriteBean.getProducts().size() > i + 1) {
                ProductInfoBean productInfoBean = productInfoList.get(productInfoList.size() - 1);
                productInfoBean.setShowLine(1);
                productInfoList.set(productInfoList.size() - 1, productInfoBean);
            }
        }

        //?????????????????????????????????????????????????????????
        if (INDENT_W_TYPE.equals(type) && productInfoList.size() == 1) {
            ProductInfoBean productInfoBean = productInfoList.get(0);
            pullFootView.rect_indent_number.setVisibility(VISIBLE);
            pullFootView.rect_indent_number.setNum(productInfoBean.getCount());
            if (passGoods != null) {
                for (CartInfoBean cartInfoBean : passGoods) {
                    SkuSaleBean saleSku = cartInfoBean.getSaleSku();
                    if (saleSku != null && saleSku.getId() == productInfoBean.getSaleSkuId()) {
                        pullFootView.rect_indent_number.setMaxNum(saleSku.getQuantity());
                    }
                }
            }
        } else {
            pullFootView.rect_indent_number.setVisibility(GONE);
        }

        //??????????????????
        List<ProductInfoBean> presentInfos = indentWriteBean.getPresentInfos();
        if (presentInfos != null) {
            for (ProductInfoBean productInfoBean : presentInfos) {
                productInfoBean.setIsPresent(1);
            }
            productInfoList.addAll(presentInfos);
        }
    }


    //????????????
    private void showPayType(IndentWriteBean indentWriteBean) {
        List<String> showPayTypeList = indentWriteBean.getShowPayTypeList();
        if (showPayTypeList != null && showPayTypeList.size() > 0) {
            //??????
            if (!showPayTypeList.contains("1")) {
                pullFootView.mRadioGroup.getChildAt(1).setVisibility(GONE);
                pullFootView.ll_pay_way.getChildAt(1).setVisibility(GONE);
            }
            //?????????
            if (!showPayTypeList.contains("2")) {
                pullFootView.mRadioGroup.getChildAt(0).setVisibility(GONE);
                pullFootView.ll_pay_way.getChildAt(0).setVisibility(GONE);
            }
            //??????
            if (!showPayTypeList.contains("3")) {
                pullFootView.mRadioGroup.getChildAt(2).setVisibility(GONE);
                pullFootView.ll_pay_way.getChildAt(2).setVisibility(GONE);
            }

            String payType = showPayTypeList.get(0);
            if ("1".equals(payType)) {
                payWay = PAY_WX_PAY;
                ((RadioButton) pullFootView.mRadioGroup.getChildAt(1)).setChecked(true);
            } else if ("2".equals(payType)) {
                payWay = PAY_ALI_PAY;
                ((RadioButton) pullFootView.mRadioGroup.getChildAt(0)).setChecked(true);
            } else if ("3".equals(payType)) {
                payWay = PAY_UNION_PAY;
                ((RadioButton) pullFootView.mRadioGroup.getChildAt(2)).setChecked(true);
            }
        }
    }

    private void setOverseaData(IndentWriteBean indentWriteBean) {
        //????????????
        if (!TextUtils.isEmpty(indentWriteBean.getPrompt())) {
            pullHeaderView.tv_oversea_buy_tint.setVisibility(VISIBLE);
            pullHeaderView.tv_oversea_buy_tint.setText(getStrings(indentWriteBean.getPrompt()));
        } else {
            pullHeaderView.tv_oversea_buy_tint.setVisibility(GONE);
        }
    }

    private void setDiscounts(List<PriceInfoBean> indentDiscountsList) {
        if (indentDiscountsList != null) {
            priceInfoList.clear();
            priceInfoList.addAll(indentDiscountsList);
            PriceInfoBean priceInfoBean = priceInfoList.get(priceInfoList.size() - 1);
            tv_indent_total_price.setText(getStrings(priceInfoBean.getTotalPriceName()));
//            priceInfoList.remove(priceInfoList.get(priceInfoList.size() - 1));????????????
            indentDiscountAdapter.setNewData(priceInfoList);
        }
    }

    //???????????????????????????????????????????????????
    private void getAddress() {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, ADDRESS_LIST, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                AddressListEntity addressListEntity = GsonUtils.fromJson(result, AddressListEntity.class);
                if (addressListEntity != null) {
                    if (addressListEntity.getCode().equals(SUCCESS_CODE)) {
                        List<AddressInfoBean> addressAllBeanList = addressListEntity.getAddressAllBeanList();
                        if (addressAllBeanList != null && addressAllBeanList.size() > 0) {
                            setAddressData(addressAllBeanList.get(0));
                        }
                    } else if (addressListEntity.getCode().equals(EMPTY_CODE)) {
                        setAddressData(null);
                    } else {
                        showToast(addressListEntity.getMsg());
                    }
                }
            }
        });
    }

    //??????????????????
    private void setAddressData(AddressInfoBean addressInfoBean) {
        if (addressInfoBean != null) {
            addressId = addressInfoBean.getId();
            pullHeaderView.ll_indent_address_default.setVisibility(VISIBLE);
            pullHeaderView.ll_indent_address_null.setVisibility(GONE);
            pullHeaderView.tv_consignee_name.setText(addressInfoBean.getConsignee());
            pullHeaderView.tv_address_mobile_number.setText(addressInfoBean.getMobile());
            pullHeaderView.tv_indent_details_address.setText((addressInfoBean.getAddress_com() + addressInfoBean.getAddress() + " "));
        } else {
            pullHeaderView.ll_indent_address_default.setVisibility(GONE);
            pullHeaderView.ll_indent_address_null.setVisibility(VISIBLE);
        }
        //????????????
        if (!TextUtils.isEmpty(orderNo) && isFirst) {
            getOrderData();
        } else {
            getIndentDiscounts(!isFirst);
        }
    }

    //  ?????????????????????????????????
    private void getOrderData() {
        passGoods = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        params.put("orderNo", orderNo);
        params.put("userId", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Q_NEW_RE_BUY_INDENT, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                productInfoList.clear();
                identWriteEntity = GsonUtils.fromJson(result, IndentWriteEntity.class);
                if (identWriteEntity != null && identWriteEntity.getIndentWriteBean() != null) {
                    indentWriteBean = identWriteEntity.getIndentWriteBean();
                    List<ProductsBean> products = indentWriteBean.getProducts();
                    if (identWriteEntity.getCode().equals(SUCCESS_CODE)) {
                        if (products != null && products.size() > 0) {
                            setDiscountsInfo(indentWriteBean);
                        }
                    } else if (identWriteEntity.getCode().equals(EMPTY_CODE)) {
                        showToast(R.string.invalidData);
                    } else {
                        showToast(identWriteEntity.getMsg());
                    }
                }

                directProductAdapter.notifyDataSetChanged();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, identWriteEntity);
            }

            @Override
            public void onNotNetOrException() {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                NetLoadUtils.getNetInstance().showLoadSir(loadService, identWriteEntity);
            }
        });
    }


    private void goExchange() {
        if (userId > 0) {
            //????????????,????????????
            if (!TextUtils.isEmpty(orderCreateNo)) {
                paymentIndent();
            } else if (indentWriteBean != null) {
                //?????????????????????
                if (addressId == 0) {
                    showToast("??????????????????");
                } else if (TextUtils.isEmpty(payWay)) {
                    showToast("?????????????????????");
                } else if (isReal && (TextUtils.isEmpty(indentWriteBean.getIdcardImg1()) || TextUtils.isEmpty(indentWriteBean.getIdcardImg2()))) {
                    //?????????????????????????????????????????????????????????
                    showAlertDialogRealName();
                } else if (indentWriteBean.getAllProductNotBuy() == 1) {
                    showToast("????????????????????????????????????????????????????????????");
                } else if (!TextUtils.isEmpty(type)) {
                    getAreaTip();
                }
            } else {
                showToast("????????????????????????");
            }
        } else {
            getLoginStatus(this);
        }
    }

    private void showAlertDialogRealName() {
        if (mAlertDialogRealName == null) {
            mAlertDialogRealName = new AlertDialogRealName(this, indentWriteBean);
        }

        //??????????????????
        mAlertDialogRealName.setOnCommitListener((name, idcard, idcardImg1, idcardImg2) -> {
            if (indentWriteBean != null) {
                indentWriteBean.setRealName(name);
                indentWriteBean.setIdCard(idcard);
                indentWriteBean.setIdcardImg1(idcardImg1);
                indentWriteBean.setIdcardImg2(idcardImg2);
                goExchange();
            }
        });
        mAlertDialogRealName.show(Gravity.BOTTOM);
    }

    //??????????????????????????????
    private void getAreaTip() {
        showLoadhud(this);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.GET_AREA_TIP, settleMap, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                dismissLoadhud(getActivity());
                AreaTipsEntity areaTipsEntity = GsonUtils.fromJson(result, AreaTipsEntity.class);
                if (areaTipsEntity != null) {
                    if (SUCCESS_CODE.equals(areaTipsEntity.getCode())) {
                        if (mAlertDialogAreaTip == null) {
                            mAlertDialogAreaTip = new AlertDialogHelper(getActivity());
                            mAlertDialogAreaTip.setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                                @Override
                                public void confirm() {
                                    createIndent();
                                }

                                @Override
                                public void cancel() {

                                }
                            });
                        }

                        mAlertDialogAreaTip.setMsg(areaTipsEntity.getResult());
                        mAlertDialogAreaTip.show();
                    } else {
                        createIndent();
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                createIndent();
            }
        });
    }

    /**
     * ??????????????????
     */
    private void createIndent() {
        showLoadhud(this);
        String message = pullFootView.edt_direct_product_note.getText().toString().trim();
        Map<String, Object> params = new HashMap<>();
        //??????ID
        params.put("userId", userId);
        //????????????
        params.put("userAddressId", addressId);
        //????????????(???????????????????????????????????????)
        if (discountBeanList != null && discountBeanList.size() > 0) {
            params.put("goods", GsonUtils.toJson(discountBeanList));
        }

        //????????????
        if (combineGoods != null && combineGoods.size() > 0) {
            params.put("combineGoods", GsonUtils.toJson(combineGoods));
        }

        //????????????
        if (groupShopDetailsBean != null) {
            //?????????????????? ?????? 1 ?????? 2
            params.put("gpStatus", groupShopDetailsBean.getGpStatus());
            params.put("gpInfoId", groupShopDetailsBean.getGpInfoId());
            if (groupShopDetailsBean.getGpStatus() == 2) {
                params.put("gpRecordId", groupShopDetailsBean.getGpRecordId());
            }
        }

        //????????????
        if (!TextUtils.isEmpty(message)) {
            params.put("remark", message);
        }

        //?????????????????????
        if (couponId > 0) {
            params.put("userCouponId", couponId);
        }

        //???????????? 2019.1.16 ??????????????????
        params.put("buyType", payWay);
        if (payWay.equals(PAY_UNION_PAY)) {
            params.put("paymentLinkType", 2);
            params.put("isApp", true);
        }

        //????????????
        if (isReal) {
            params.put("realName", indentWriteBean.getRealName());
            params.put("idcard", indentWriteBean.getIdCard());
            params.put("idcardImg1", indentWriteBean.getIdcardImg1());
            params.put("idcardImg2", indentWriteBean.getIdcardImg2());
        }
        //????????????
        params.put("isWeb", false);
        params.put("source", 0);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, getUrl(), params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                dealingIndentPayResult(result);
            }

            @Override
            public void onNotNetOrException() {
                dismissLoadhud(getActivity());
                showToast(R.string.do_failed);
            }
        });
    }

    private String getUrl() {
        if (INDENT_GROUP_SHOP.equals(type)) {//??????
            return Q_CREATE_GROUP_NEW_INDENT;
        } else {//????????????
            return Q_CREATE_INDENT;
        }
    }

    /**
     * ????????????
     */
    private void paymentIndent() {
        Map<String, Object> params = new HashMap<>();
        params.put("no", !TextUtils.isEmpty(orderCreateNo) ? orderCreateNo : orderNo);
        params.put("userId", userId);
        //        2019.1.16 ??????????????????
        params.put("buyType", payWay);
        if (payWay.equals(PAY_UNION_PAY)) {
            params.put("paymentLinkType", 2);
            params.put("isApp", true);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Q_PAYMENT_INDENT, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                dealingIndentPayResult(result);
            }

            @Override
            public void onNotNetOrException() {
                showToast(R.string.do_failed);
            }
        });
    }

    /**
     * ????????????????????????
     *
     * @param result ????????????????????????
     */
    private void dealingIndentPayResult(String result) {
        dismissLoadhud(this);
        if (mAlertDialogRealName != null) {
            mAlertDialogRealName.dismiss();
        }
        if (payWay.equals(PAY_WX_PAY)) {
            qualityWeChatIndent = GsonUtils.fromJson(result, QualityCreateWeChatPayIndentBean.class);
            if (qualityWeChatIndent != null) {
                String msg = qualityWeChatIndent.getResult() != null &&
                        !TextUtils.isEmpty(qualityWeChatIndent.getResult().getMsg()) ?
                        getStrings(qualityWeChatIndent.getResult().getMsg()) :
                        getStrings(qualityWeChatIndent.getMsg());
                if (qualityWeChatIndent.getCode().equals(SUCCESS_CODE)) {
                    //???????????????????????????????????????
                    orderCreateNo = qualityWeChatIndent.getResult().getNo();
                    doWXPay(qualityWeChatIndent.getResult());
                } else if ("75".equals(qualityWeChatIndent.getCode())) {//?????????????????????????????????
                    showRealNameDiffer(msg);
                } else if ("72".equals(qualityWeChatIndent.getCode())) {//??????????????????
                    showRealNameError(msg);
                } else {
                    showToast(msg);
//                            ????????????????????????
                    if (qualityWeChatIndent.getResult() != null) {
                        presentStatusUpdate(qualityWeChatIndent.getResult().getCode());
                    }
                }
            }
        } else if (payWay.equals(PAY_ALI_PAY)) {
            qualityAliPayIndent = GsonUtils.fromJson(result, QualityCreateAliPayIndentBean.class);
            if (qualityAliPayIndent != null) {
                String msg = qualityAliPayIndent.getResult() != null &&
                        !TextUtils.isEmpty(qualityAliPayIndent.getResult().getMsg()) ?
                        getStrings(qualityAliPayIndent.getResult().getMsg()) :
                        getStrings(qualityAliPayIndent.getMsg());
                if (qualityAliPayIndent.getCode().equals(SUCCESS_CODE)) {
                    //??????????????????????????????????????????
                    orderCreateNo = qualityAliPayIndent.getResult().getNo();
                    doAliPay(qualityAliPayIndent.getResult());
                } else if ("75".equals(qualityAliPayIndent.getCode())) {
                    showRealNameDiffer(msg);
                } else if ("72".equals(qualityAliPayIndent.getCode())) {
                    showRealNameError(msg);
                } else {
                    //                            ????????????????????????
                    if (qualityAliPayIndent.getResult() != null) {
                        presentStatusUpdate(qualityAliPayIndent.getResult().getCode());
                    }
                    showToast(msg);
                }
            }
        } else if (payWay.equals(PAY_UNION_PAY)) {
            qualityUnionIndent = GsonUtils.fromJson(result, QualityCreateUnionPayIndentEntity.class);
            if (qualityUnionIndent != null) {
                String msg = qualityUnionIndent.getQualityCreateUnionPayIndent() != null &&
                        !TextUtils.isEmpty(qualityUnionIndent.getQualityCreateUnionPayIndent().getMsg()) ?
                        getStrings(qualityUnionIndent.getQualityCreateUnionPayIndent().getMsg()) :
                        getStrings(qualityUnionIndent.getMsg());
                if (SUCCESS_CODE.equals(qualityUnionIndent.getCode())) {
                    //???????????????????????????????????????
                    orderCreateNo = qualityUnionIndent.getQualityCreateUnionPayIndent().getNo();
                    unionPay(qualityUnionIndent);
                } else if ("75".equals(qualityUnionIndent.getCode())) {
                    showRealNameDiffer(msg);
                } else if ("72".equals(qualityUnionIndent.getCode())) {
                    showRealNameError(msg);
                } else {
                    showToast(msg);
//                            ????????????????????????
                    if (qualityUnionIndent.getQualityCreateUnionPayIndent() != null) {
                        presentStatusUpdate(qualityUnionIndent.getQualityCreateUnionPayIndent().getCode());
                    }
                }
            }
        }
    }

    //?????????????????????????????????
    private void showRealNameDiffer(String msg) {
        if (mAlertDialogRealNameDiffer == null) {
            mAlertDialogRealNameDiffer = new AlertDialogHelper(this);
            mAlertDialogRealNameDiffer.setCancelText("??????????????????")
                    .setConfirmText("??????")
                    .setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                        @Override
                        public void confirm() {
                            Intent intent = new Intent(getActivity(), SelectedAddressActivity.class);
                            intent.putExtra("addressId", String.valueOf(addressId));
                            startActivityForResult(intent, SEL_ADDRESS_REQ);
                        }

                        @Override
                        public void cancel() {
                            showAlertDialogRealName();
                        }
                    });
        }
        mAlertDialogRealNameDiffer.setMsg(msg);
        mAlertDialogRealNameDiffer.show();
    }

    //??????????????????
    private void showRealNameError(String msg) {
        if (mAlertDialogRealNameError == null) {
            mAlertDialogRealNameError = new AlertDialogHelper(this);
            mAlertDialogRealNameError.setSingleButton(true)
                    .setConfirmText("??????????????????")
                    .setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                        @Override
                        public void confirm() {
                            showAlertDialogRealName();
                        }

                        @Override
                        public void cancel() {

                        }
                    });
        }
        mAlertDialogRealNameError.setMsg(msg);
        mAlertDialogRealNameError.show();
    }


    /**
     * ????????????????????????
     */
    private void presentStatusUpdate(String codeStatus) {
        try {
//            ????????????
            if ("10023".equals(codeStatus)) {
                getIndentDiscounts(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void doAliPay(QualityCreateAliPayIndentBean.ResultBean resultBean) {
        new AliPay(this, resultBean.getNo(), resultBean.getPayKey(), new AliPay.AliPayResultCallBack() {
            @Override
            public void onSuccess() {
                showToast("????????????");
//                ?????????????????????
                paySuccess();
            }

            @Override
            public void onDealing() {
                showToast("???????????????...");
            }

            @Override
            public void onError(int error_code) {
                switch (error_code) {
                    case AliPay.ERROR_RESULT:
                        showToast("????????????:????????????????????????");
                        break;
                    case AliPay.ERROR_NETWORK:
                        showToast("????????????:??????????????????");
                        break;
                    case AliPay.ERROR_PAY:
                        showToast("????????????:?????????????????????");
                        break;
                    default:
                        showToast("????????????");
                        break;
                }
                skipIndentDetail();
            }

            @Override
            public void onCancel() {
                skipIndentDetail();
                showToast("????????????");
            }
        }).doPay();
    }

    private void doWXPay(QualityCreateWeChatPayIndentBean.ResultBean resultBean) {
        WXPay.init(this);//?????????????????????
        WXPay.getInstance().doPayDateObject(resultBean.getNo(), resultBean.getPayKey(), new WXPay.WXPayResultCallBack() {
            @Override
            public void onSuccess() {
                showToast("????????????");
                paySuccess();
            }

            @Override
            public void onError(int error_code) {
                switch (error_code) {
                    case WXPay.NO_OR_LOW_WX:
                        showToast("????????????????????????????????????");
                        break;
                    case WXPay.ERROR_PAY_PARAM:
                        showToast("????????????");
                        break;
                    case WXPay.ERROR_PAY:
                        showToast("????????????");
                        break;
                }
//                payError();
                skipIndentDetail();
            }

            @Override
            public void onCancel() {
                skipIndentDetail();
                showToast("????????????");
            }
        });
    }

    private void unionPay(@NonNull QualityCreateUnionPayIndentEntity qualityUnionIndent) {
        if (qualityUnionIndent.getQualityCreateUnionPayIndent().getPayKeyBean() != null &&
                !TextUtils.isEmpty(qualityUnionIndent.getQualityCreateUnionPayIndent().getPayKeyBean().getPaymentUrl())) {
            if (loadHud != null) {
                loadHud.show();
            }
            //                ?????????????????????
            UnionPay unionPay = new UnionPay(getActivity(), qualityUnionIndent.getQualityCreateUnionPayIndent().getNo(),
                    qualityUnionIndent.getQualityCreateUnionPayIndent().getPayKeyBean().getPaymentUrl(),
                    new UnionPay.UnionPayResultCallBack() {
                        @Override
                        public void onUnionPaySuccess(String webResultValue) {
                            if (loadHud != null) {
                                loadHud.dismiss();
                            }
                            if (!TextUtils.isEmpty(webResultValue) && "1".equals(webResultValue)) {
                                finish();
                            } else {
                                //                ?????????????????????
                                paySuccess();
                            }
                        }

                        @Override
                        public void onUnionPayError(String errorMes) {
                            if (loadHud != null) {
                                loadHud.dismiss();
                            }
                            showToast(errorMes);
                            skipIndentDetail();
                        }
                    });
        } else {
            showToast("???????????????????????????????????????????????????");
        }
    }

    //?????????????????????????????????
    private void paySuccess() {
        if (type.equals(INDENT_GROUP_SHOP)) {
            switch (groupShopDetailsBean.getGpStatus()) {
                //??????
                case OPEN_GROUP:
                    skipGpShareIndent();
                    break;
                //??????
                case JOIN_GROUP:
                    skipMineGroupIndent();
                    break;
            }
        } else {
            skipDirectIndent();
        }
    }

    //??????????????????
    private void skipDirectIndent() {
        if (!TextUtils.isEmpty(orderCreateNo)) {
            Intent intent = new Intent(getActivity(), DirectPaySuccessActivity.class);
            intent.putExtra(INDENT_PRODUCT_TYPE, INDENT_PROPRIETOR_PRODUCT);
            intent.putExtra("indentNo", orderCreateNo);
            //???????????????????????????????????????????????????????????????????????????????????????
            new LifecycleHandler(this).postDelayed(() -> {
                startActivity(intent);
                finish();
            }, 1000);
        }
    }

    //??????????????????????????????????????????????????????
    private void skipIndentDetail() {
        if (!TextUtils.isEmpty(orderCreateNo)) {
            Intent intent = new Intent(this, DirectExchangeDetailsActivity.class);
            intent.putExtra("orderNo", orderCreateNo);
            //???????????????????????????????????????????????????????????????????????????????????????
            new LifecycleHandler(this).postDelayed(() -> {
                startActivity(intent);
                finish();
            }, 500);
        }
    }


    /**
     * ????????????????????????
     */
    private void payCancel() {
        if (payCancelDialogHelper == null) {
            NetLoadUtils.getNetInstance().loadNetDataPost(this, PAY_CANCEL, new NetLoadListenerHelper() {
                @Override
                public void onSuccess(String result) {
                    RequestStatus requestStatus = RequestStatus.objectFromData(result);
                    if (requestStatus != null && SUCCESS_CODE.equals(requestStatus.getCode())) {
                        payCancelDialogHelper = new AlertDialogHelper(getActivity());
                        payCancelDialogHelper.setMsgTextGravity(Gravity.CENTER).setTitleVisibility(GONE)
                                .setMsg(!TextUtils.isEmpty(requestStatus.getDescription()) ?
                                        requestStatus.getDescription() : "???????????????????????????????????????")
                                .setCancelText("????????????").setConfirmText("????????????")
                                .setCancelTextColor(getResources().getColor(R.color.text_login_gray_s))
                                .setCancelable(false);
                        payCancelDialogHelper.setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                            @Override
                            public void confirm() {
                            }

                            @Override
                            public void cancel() {
                                finish();
                            }
                        });
                        payCancelDialogHelper.show();
                    } else {
                        finish();
                    }
                }

                @Override
                public void onNotNetOrException() {
                    finish();
                }
            });
        } else {
            finish();
        }
    }

    //????????????????????????
    private void skipMineGroupIndent() {
        Intent intent = new Intent(this, QualityGroupShopMineActivity.class);
        new LifecycleHandler(this).postDelayed(() -> {
            startActivity(intent);
            finish();
        }, 500);
    }

    //????????????????????????
    private void skipGpShareIndent() {
        Intent intent = new Intent(this, DoMoGroupJoinShareActivity.class);
        intent.putExtra("orderNo", orderCreateNo);
        new LifecycleHandler(this).postDelayed(() -> {
            startActivity(intent);
            finish();
        }, 500);
    }


    @Override
    public View getLoadView() {
        return ll_indent_details;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case NEW_CRE_ADDRESS_REQ:
            case SEL_ADDRESS_REQ:
                //            ??????????????????
                AddressInfoBean addressInfoBean = data.getParcelableExtra("addressInfoBean");
                isFirst = false;
                setAddressData(addressInfoBean);
                break;
            case DIRECT_COUPON_REQ:
                //            ???????????????
                couponId = data.getIntExtra("couponId", -1);
                String couponAmount = data.getStringExtra("couponAmount");
                pullFootView.tv_direct_product_favorable.setText(couponId < 1 ? "??????????????????" : "-??" + couponAmount);
                getIndentDiscounts(true);
                break;
            case PictureConfigC.CHOOSE_REQUEST:
                List<LocalMediaC> localMediaList = PictureSelector.obtainMultipleResult(data);
                if (localMediaList != null && localMediaList.size() > 0) {
                    mAlertDialogRealName.update(localMediaList.get(0).getPath());
                }
                break;
        }
    }


    @OnClick({R.id.tv_life_back, R.id.tv_indent_write_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_life_back:
                payCancel();
                break;
            case R.id.tv_indent_write_commit:
                goExchange();
                break;
        }
    }

    class PullHeaderView {
        //    ????????????
        @BindView(R.id.ll_indent_address_null)
        LinearLayout ll_indent_address_null;
        //   ???????????????
        @BindView(R.id.ll_indent_address_default)
        LinearLayout ll_indent_address_default;
        //    ???????????????
        @BindView(R.id.tv_consignee_name)
        TextView tv_consignee_name;
        //    ?????????????????????
        @BindView(R.id.tv_consignee_mobile_number)
        TextView tv_address_mobile_number;
        //    ????????????
        @BindView(R.id.tv_indent_details_address)
        TextView tv_indent_details_address;
        //    ????????????
        @BindView(R.id.img_skip_address)
        ImageView img_skip_address;
        //        ?????????????????????
        @BindView(R.id.tv_oversea_buy_tint)
        TextView tv_oversea_buy_tint;

        //    ?????????????????? ??????????????????
        @OnClick(R.id.tv_lv_top)
        void skipNewAddress(View view) {
            Intent intent = new Intent(getActivity(), SelectedAddressActivity.class);
            intent.putExtra("hasDefaultAddress", false);
            startActivityForResult(intent, NEW_CRE_ADDRESS_REQ);
        }

        //  ????????????  ??????????????????
        @OnClick(R.id.ll_indent_address_default)
        void skipAddressList(View view) {
            if (TextUtils.isEmpty(orderCreateNo)) {
                if (type.equals(INDENT_W_TYPE) || type.equals(INDENT_GROUP_SHOP)) {
                    Intent intent = new Intent(getActivity(), SelectedAddressActivity.class);
                    intent.putExtra("addressId", String.valueOf(addressId));
                    startActivityForResult(intent, SEL_ADDRESS_REQ);
                } else {
                    img_skip_address.setVisibility(GONE);
                }
            }
        }
    }

    class PullFootView {
        //???????????????
        @BindView(R.id.tv_direct_product_favorable)
        TextView tv_direct_product_favorable;
        //????????????
        @BindView(R.id.edt_direct_product_note)
        EditText edt_direct_product_note;
        @BindView(R.id.ll_indent_product_note)
        LinearLayout ll_indent_product_note;
        //        ???????????????
        @BindView(R.id.ll_layout_coupon)
        LinearLayout ll_layout_coupon;
        //        ?????????
        @BindView(R.id.rv_indent_write_info)
        RecyclerView rv_indent_write_info;
        @BindView(R.id.rect_indent_number)
        RectAddAndSubWriteView rect_indent_number;
        @BindView(R.id.radio_group)
        RadioGroup mRadioGroup;
        @BindView(R.id.ll_pay_way)
        LinearLayout ll_pay_way;


        void init() {
            ((EditText) rect_indent_number.findViewById(R.id.tv_integration_details_credits_count)).addTextChangedListener(new TextWatchListener() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mNum = getStringChangeIntegers(s.toString());
                }
            });

            mRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                if (checkedId == R.id.rb_checked_alipay) {
                    payWay = PAY_ALI_PAY;
                } else if (checkedId == R.id.rb_checked_wechat_pay) {
                    payWay = PAY_WX_PAY;
                } else if (checkedId == R.id.rb_checked_union_pay) {
                    payWay = PAY_UNION_PAY;
                }
            });
        }

        //        ???????????????
        @OnClick(R.id.ll_layout_coupon)
        void selectFavorable() {
            if (indentWriteBean != null && indentWriteBean.getUserCouponInfo() != null && indentWriteBean.getUserCouponInfo().getAllowCouoon() == 1) {
                if (TextUtils.isEmpty(orderCreateNo)) {
                    Intent intent = new Intent(getActivity(), DirectCouponGetActivity.class);
                    intent.putExtra("couponGoods", getCouponGoodsInfo(productInfoList));
                    startActivityForResult(intent, DIRECT_COUPON_REQ);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(orderCreateNo)) {
//            ??????????????????
            if (pullFootView.rect_indent_number.getVisibility() == VISIBLE) {
                pullFootView.rect_indent_number.setVisibility(GONE);
            }
//            ??????????????????
            if (pullFootView.edt_direct_product_note.getText().toString().trim().length() < 1) {
                pullFootView.ll_indent_product_note.setVisibility(GONE);
            } else {
                pullFootView.ll_indent_product_note.setVisibility(VISIBLE);
                pullFootView.edt_direct_product_note.setEnabled(false);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            payCancel();
            return true;//???????????????????????????
        }
        return super.onKeyDown(keyCode, event);//????????????????????????????????????
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KeyboardUtils.unregisterSoftInputChangedListener(this);
    }

    /**
     * ???????????????????????????????????? ???????????????????????????????????????
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
        try {
            return super.dispatchTouchEvent(ev);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Return whether touch the view.
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v instanceof EditText) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }
}
