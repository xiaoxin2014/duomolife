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
 * 订单填写
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
    //结算
    @BindView(R.id.tv_indent_write_commit)
    TextView tv_indent_write_commit;
    //    商品结算金额
    @BindView(R.id.tv_indent_total_price)
    TextView tv_indent_total_price;
    @BindView(R.id.ll_write_commit)
    LinearLayout mLlWriteCommit;
    private boolean isFirst = true;//获取默认地址时为true,修改地址为false
    private int addressId;
    private String type = "";
    private PullHeaderView pullHeaderView;
    private PullFootView pullFootView;
    private QualityCreateWeChatPayIndentBean qualityWeChatIndent;
    private QualityCreateAliPayIndentBean qualityAliPayIndent;
    private String payWay = PAY_ALI_PAY;//默认支付宝付款
    //    创建订单 未结算
    private String orderCreateNo = "";
    //     完成支付订单编号
    private String orderNo = "";

    //    优惠券id
    private int couponId;
    private GroupShopDetailsBean groupShopDetailsBean;
    private IndentDiscountAdapter indentDiscountAdapter;
    private IndentWriteEntity identWriteEntity;
    private boolean isReal = false;
    private IndentWriteBean indentWriteBean;
    private AlertDialogHelper payCancelDialogHelper;
    private QualityCreateUnionPayIndentEntity qualityUnionIndent;
    private WriteProductListAdapter directProductAdapter;
    //    普通订单数据
    private List<CartInfoBean> passGoods = new ArrayList<>();
    //    组合订单数据
    private List<CombineGoodsBean> combineGoods = new ArrayList<>();
    //    订单优惠信息必传参数
    private List<IndentProDiscountBean> discountBeanList = new ArrayList<>();
    //    订单价格优惠列表
    private List<PriceInfoBean> priceInfoList = new ArrayList<>();
    //    商品列表
    private List<ProductInfoBean> productInfoList = new ArrayList<>();
    private int mNum;
    private boolean first = true;
    private AlertDialogPurchase mAlertDialogPurchase;
    //选中的加价购商品id
    private int purchaseProductId;
    private AlertDialogRealName mAlertDialogRealName;
    private AlertDialogHelper mAlertDialogRealNameError;
    private AlertDialogHelper mAlertDialogRealNameDiffer;


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
        tv_header_titleAll.setText("订单填写");
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
            showToast("商品信息有误，请重试");
            finish();
        }
//        地址栏
        View headerView = LayoutInflater.from(this).inflate(R.layout.layout_direct_indent_write_header_address, (ViewGroup) communal_recycler.getParent(), false);
        pullHeaderView = new PullHeaderView();
        ButterKnife.bind(pullHeaderView, headerView);
//        订单详情
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
            showToast("商品信息有误，请重试");
            finish();
        }
        directProductAdapter = new WriteProductListAdapter(getActivity(), productInfoList, type);
        pullFootView.rect_indent_number.setFontColor(Color.parseColor("#333333"));
        communal_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
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
                    //移除偏远地区无法配送的商品
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

        //监听软键盘
        KeyboardUtils.registerSoftInputChangedListener(this, new KeyboardUtils.OnSoftInputChangedListener() {
            @Override
            public void onSoftInputChanged(int height) {
                try {
                    if (!first) {//默认会调用一次
                        if (height == 0) {
                            //软键盘隐藏
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, AutoSizeUtils.mm2px(mAppContext, 98));
                            mLlWriteCommit.setLayoutParams(params);
                            if (mNum <= 0) {//最小购买数量为1件
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
                            //软键盘显示
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
                            mLlWriteCommit.setLayoutParams(params);
                        }
                    }
                    first = false;
                } catch (Exception e) {
                    CrashReport.postCatchedException(new Exception("订单填写手动修改数量异常：" + e.getMessage()));
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
     * 获取订单结算信息
     *
     * @param updatePriceInfo (是否需要修改订单结算信息,选择优惠券或者修改地址时为true)
     */
    private void getIndentDiscounts(boolean updatePriceInfo) {
        if (!TextUtils.isEmpty(type)) {
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            params.put("addressId", addressId);
            if (updatePriceInfo) {
                params.put("userCouponId", couponId);
            }
            if (groupShopDetailsBean != null) {
                params.put("gpInfoId", groupShopDetailsBean.getGpInfoId());
                params.put("gpRecordId", groupShopDetailsBean.getGpRecordId());
            }
            if (discountBeanList != null && discountBeanList.size() > 0) {
                params.put("goods", GsonUtils.toJson(discountBeanList));
            }
            if (combineGoods != null && combineGoods.size() > 0) {
                params.put("combineGoods", GsonUtils.toJson(combineGoods));
            }

            NetLoadUtils.getNetInstance().loadNetDataPost(this, updatePriceInfo ? Url.INDENT_DISCOUNTS_UPDATE_INFO : Url.INDENT_DISCOUNTS_NEW_INFO, params, new NetLoadListenerHelper() {
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

    //显示加价购弹窗
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
                    //有多个sku
                    if (skuSale.size() > 1) {
                        EditGoodsSkuBean editGoodsSkuBean = new EditGoodsSkuBean(goodsListBean);
                        SkuDialog skuDialog = new SkuDialog(this);
                        editGoodsSkuBean.setShowBottom(true);
                        skuDialog.refreshView(editGoodsSkuBean);
                        skuDialog.show();
                        skuDialog.getGoodsSKu(shopCarGoodsSku -> {
                            if (shopCarGoodsSku != null) {
                                //重新获取结算信息
                                increasePurchase(goodsListBean.getProductId(), shopCarGoodsSku.getSaleSkuId());
                            }
                        });
                        //只有一个sku
                    } else if (skuSale.size() == 1) {
                        SkuSaleBean skuSaleBean = skuSale.get(0);
                        //重新获取结算信息
                        increasePurchase(goodsListBean.getProductId(), skuSaleBean.getId());
                    }
                });
                mAlertDialogPurchase.show();
                mAlertDialogPurchase.updateData(purchaseBean);
            }
        }
    }


    //加入加价购商品并重新获取结算信息
    private void increasePurchase(int productId, int SaleSkuId) {
        loadHud.show();
        IndentProDiscountBean indentProBean = new IndentProDiscountBean();
        indentProBean.setId(productId);
        indentProBean.setSaleSkuId(SaleSkuId);
        indentProBean.setCount(1);
        indentProBean.setIsPrerogative(1);
        discountBeanList.add(indentProBean);
        purchaseProductId = productId;//记录选中的加价购商品id
        getIndentDiscounts(false);
    }

    //设置所有结算信息
    private void setDiscountsInfo(IndentWriteBean indentWriteBean) {
        showPurchaseDialog(indentWriteBean);
        //实名制相关
        isReal = indentWriteBean.isReal();
        //商品中包含失效，已抢光或者偏远地区商品
        tv_indent_write_commit.setBackgroundResource(indentWriteBean.getAllProductNotBuy() == 0 ? R.color.text_normal_red : R.color.text_gray_c);
        //实名信息
        setOverseaData(indentWriteBean);
        //金额信息
        setDiscounts(indentWriteBean.getPriceInfos());
        //是否有可用的优惠券
        UserCouponInfoBean userCouponInfo = indentWriteBean.getUserCouponInfo();
        if (userCouponInfo != null) {
            if (userCouponInfo.getAllowCouoon() == 1 && userCouponInfo.getId() != null && userCouponInfo.getId() > 0) {
                //判断优惠券是否有门槛   无门槛-xx 有门槛满xx-xx
                pullFootView.tv_direct_product_favorable.setText((userCouponInfo.getStartFee() + "-¥" + userCouponInfo.getPrice()));
                pullFootView.tv_direct_product_favorable.setSelected(true);
                couponId = userCouponInfo.getId();
            } else {
                pullFootView.tv_direct_product_favorable.setSelected(false);
                pullFootView.tv_direct_product_favorable.setText(couponId == -1 ? "不使用优惠券" : userCouponInfo.getMsg());
            }
        }

        List<ProductsBean> products = indentWriteBean.getProducts();
        //组装创建订单商品信息并更新结算商品信息
        List[] indentInfo = ShopCarDao.getIndentGoodsInfo(products, purchaseProductId);
        discountBeanList = indentInfo[0];
        combineGoods = indentInfo[1];
        //组装商品展示信息
        for (int i = 0; i < products.size(); i++) {
            ProductsBean productsBean = products.get(i);
            List<ProductInfoBean> productInfos = productsBean.getProductInfos();
            ProductInfoBean combineMainProduct = productsBean.getCombineMainProduct();
            List<ProductInfoBean> combineMatchProducts = productsBean.getCombineMatchProducts();
            //设置搭配商品数量
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

            //添加分割线和活动信息展示
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

        //只有一件商品需要结算时，可修改购买数量
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

        //组装赠品数据
        List<ProductInfoBean> presentInfos = indentWriteBean.getPresentInfos();
        if (presentInfos != null) {
            for (ProductInfoBean productInfoBean : presentInfos) {
                productInfoBean.setIsPresent(1);
            }
            productInfoList.addAll(presentInfos);
        }
    }


    //支付方式
    private void showPayType(IndentWriteBean indentWriteBean) {
        List<String> showPayTypeList = indentWriteBean.getShowPayTypeList();
        if (showPayTypeList != null && showPayTypeList.size() > 0) {
            //微信
            if (!showPayTypeList.contains("1")) {
                pullFootView.mRadioGroup.getChildAt(1).setVisibility(GONE);
                pullFootView.ll_pay_way.getChildAt(1).setVisibility(GONE);
            }
            //支付宝
            if (!showPayTypeList.contains("2")) {
                pullFootView.mRadioGroup.getChildAt(0).setVisibility(GONE);
                pullFootView.ll_pay_way.getChildAt(0).setVisibility(GONE);
            }
            //银联
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
        //顶部通知
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
//            priceInfoList.remove(priceInfoList.get(priceInfoList.size() - 1));实付金额
            indentDiscountAdapter.setNewData(priceInfoList);
        }
    }

    //获取默认地址（取地址列表的第一个）
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

    //更新地址信息
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
        //再次购买
        if (!TextUtils.isEmpty(orderNo) && isFirst) {
            getOrderData();
        } else {
            getIndentDiscounts(!isFirst);
        }
    }

    //  再次购买，获取商品信息
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
            //订单锁定,再次支付
            if (!TextUtils.isEmpty(orderCreateNo)) {
                paymentIndent();
            } else {
                //第一次提交订单
                if (addressId == 0) {
                    showToast("收货地址为空");
                } else if (TextUtils.isEmpty(payWay)) {
                    showToast("请选择支付方式");
                } else if (isReal && (TextUtils.isEmpty(indentWriteBean.getIdcardImg1()) || TextUtils.isEmpty(indentWriteBean.getIdcardImg2()))) {
                    //如果需要实名并且用户还没有完善实名信息
                    showAlertDialogRealName();
                } else if (indentWriteBean.getAllProductNotBuy() == 1) {
                    showToast("订单内含有无法购买的商品，请移除后再提交");
                } else if (!TextUtils.isEmpty(type)) {
                    createIndent();
                } else {
                    showToast("商品数据错误");
                }
            }
        } else {
            getLoginStatus(this);
        }
    }

    private void showAlertDialogRealName() {
        if (mAlertDialogRealName == null) {
            mAlertDialogRealName = new AlertDialogRealName(this, indentWriteBean);
        }

        //直接提交订单
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

    /**
     * 创建普通订单
     */
    private void createIndent() {
        showLoadhud(this);
        String message = pullFootView.edt_direct_product_note.getText().toString().trim();
        Map<String, Object> params = new HashMap<>();
        //用户ID
        params.put("userId", userId);
        //用户地址
        params.put("userAddressId", addressId);
        //普通商品(普通订单以及拼团订单都要传)
        if (discountBeanList != null && discountBeanList.size() > 0) {
            params.put("goods", GsonUtils.toJson(discountBeanList));
        }

        //组合商品
        if (combineGoods != null && combineGoods.size() > 0) {
            params.put("combineGoods", GsonUtils.toJson(combineGoods));
        }

        //拼团商品
        if (groupShopDetailsBean != null) {
            //拼团订单状态 开团 1 拼团 2
            params.put("gpStatus", groupShopDetailsBean.getGpStatus());
            params.put("gpInfoId", groupShopDetailsBean.getGpInfoId());
            if (groupShopDetailsBean.getGpStatus() == 2) {
                params.put("gpRecordId", groupShopDetailsBean.getGpRecordId());
            }
        }

        //用户留言
        if (!TextUtils.isEmpty(message)) {
            params.put("remark", message);
        }

        //是否使用优惠券
        if (couponId > 0) {
            params.put("userCouponId", couponId);
        }

        //付款方式 2019.1.16 新增银联支付
        params.put("buyType", payWay);
        if (payWay.equals(PAY_UNION_PAY)) {
            params.put("paymentLinkType", 2);
            params.put("isApp", true);
        }

        //实名信息
        if (isReal) {
            params.put("realName", indentWriteBean.getRealName());
            params.put("idcard", indentWriteBean.getIdCard());
            params.put("idcardImg1", indentWriteBean.getIdcardImg1());
            params.put("idcardImg2", indentWriteBean.getIdcardImg2());
        }
        //订单来源
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
        if (INDENT_GROUP_SHOP.equals(type)) {//拼团
            return Q_CREATE_GROUP_NEW_INDENT;
        } else {//普通订单
            return Q_CREATE_INDENT;
        }
    }

    /**
     * 再次支付
     */
    private void paymentIndent() {
        Map<String, Object> params = new HashMap<>();
        params.put("no", !TextUtils.isEmpty(orderCreateNo) ? orderCreateNo : orderNo);
        params.put("userId", userId);
        //        2019.1.16 新增银联支付
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
     * 处理订单支付数据
     *
     * @param result 处理订单返回数据
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
                    //返回成功，调起微信支付接口
                    orderCreateNo = qualityWeChatIndent.getResult().getNo();
                    doWXPay(qualityWeChatIndent.getResult());
                } else if ("75".equals(qualityWeChatIndent.getCode())) {//实名信息与收件人不一致
                    showRealNameDiffer(msg);
                } else if ("72".equals(qualityWeChatIndent.getCode())) {//实名信息有误
                    showRealNameError(msg);
                } else {
                    showToast(msg);
//                            赠品送完刷新数据
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
                    //返回成功，调起支付宝支付接口
                    orderCreateNo = qualityAliPayIndent.getResult().getNo();
                    doAliPay(qualityAliPayIndent.getResult());
                } else if ("75".equals(qualityAliPayIndent.getCode())) {
                    showRealNameDiffer(msg);
                } else if ("72".equals(qualityAliPayIndent.getCode())) {
                    showRealNameError(msg);
                } else {
                    //                            赠品送完刷新数据
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
                    //返回成功，调起银联支付接口
                    orderCreateNo = qualityUnionIndent.getQualityCreateUnionPayIndent().getNo();
                    unionPay(qualityUnionIndent);
                } else if ("75".equals(qualityUnionIndent.getCode())) {
                    showRealNameDiffer(msg);
                } else if ("72".equals(qualityUnionIndent.getCode())) {
                    showRealNameError(msg);
                } else {
                    showToast(msg);
//                            赠品送完刷新数据
                    if (qualityUnionIndent.getQualityCreateUnionPayIndent() != null) {
                        presentStatusUpdate(qualityUnionIndent.getQualityCreateUnionPayIndent().getCode());
                    }
                }
            }
        }
    }

    //收件人与实名信息不一致
    private void showRealNameDiffer(String msg) {
        if (mAlertDialogRealNameDiffer == null) {
            mAlertDialogRealNameDiffer = new AlertDialogHelper(this);
            mAlertDialogRealNameDiffer.setCancelText("重新实名认证")
                    .setConfirmText("同意")
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

    //实名信息有误
    private void showRealNameError(String msg) {
        if (mAlertDialogRealNameError == null) {
            mAlertDialogRealNameError = new AlertDialogHelper(this);
            mAlertDialogRealNameError.setSingleButton(true)
                    .setConfirmText("重新实名认证")
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
     * 创建订单返回状态
     */
    private void presentStatusUpdate(String codeStatus) {
        try {
//            赠品赠完
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
                showToast("支付成功");
//                跳转订单完成页
                paySuccess();
            }

            @Override
            public void onDealing() {
                showToast("支付处理中...");
            }

            @Override
            public void onError(int error_code) {
                switch (error_code) {
                    case AliPay.ERROR_RESULT:
                        showToast("支付失败:支付结果解析错误");
                        break;
                    case AliPay.ERROR_NETWORK:
                        showToast("支付失败:网络连接错误");
                        break;
                    case AliPay.ERROR_PAY:
                        showToast("支付错误:支付码支付失败");
                        break;
                    default:
                        showToast("支付错误");
                        break;
                }
                skipIndentDetail();
            }

            @Override
            public void onCancel() {
                skipIndentDetail();
                showToast("支付取消");
            }
        }).doPay();
    }

    private void doWXPay(QualityCreateWeChatPayIndentBean.ResultBean resultBean) {
        WXPay.init(this);//要在支付前调用
        WXPay.getInstance().doPayDateObject(resultBean.getNo(), resultBean.getPayKey(), new WXPay.WXPayResultCallBack() {
            @Override
            public void onSuccess() {
                showToast("支付成功");
                paySuccess();
            }

            @Override
            public void onError(int error_code) {
                switch (error_code) {
                    case WXPay.NO_OR_LOW_WX:
                        showToast("未安装微信或微信版本过低");
                        break;
                    case WXPay.ERROR_PAY_PARAM:
                        showToast("参数错误");
                        break;
                    case WXPay.ERROR_PAY:
                        showToast("支付失败");
                        break;
                }
//                payError();
                skipIndentDetail();
            }

            @Override
            public void onCancel() {
                skipIndentDetail();
                showToast("支付取消");
            }
        });
    }

    private void unionPay(@NonNull QualityCreateUnionPayIndentEntity qualityUnionIndent) {
        if (qualityUnionIndent.getQualityCreateUnionPayIndent().getPayKeyBean() != null &&
                !TextUtils.isEmpty(qualityUnionIndent.getQualityCreateUnionPayIndent().getPayKeyBean().getPaymentUrl())) {
            if (loadHud != null) {
                loadHud.show();
            }
            //                跳转订单完成页
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
                                //                跳转订单完成页
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
            showToast("缺少重要参数，请选择其它支付渠道！");
        }
    }

    //支付成功跳转订单完成页
    private void paySuccess() {
        if (type.equals(INDENT_GROUP_SHOP)) {
            switch (groupShopDetailsBean.getGpStatus()) {
                //开团
                case OPEN_GROUP:
                    skipGpShareIndent();
                    break;
                //参团
                case JOIN_GROUP:
                    skipMineGroupIndent();
                    break;
            }
        } else {
            skipDirectIndent();
        }
    }

    //支付成功跳转
    private void skipDirectIndent() {
        if (!TextUtils.isEmpty(orderCreateNo)) {
            Intent intent = new Intent(getActivity(), DirectPaySuccessActivity.class);
            intent.putExtra(INDENT_PRODUCT_TYPE, INDENT_PROPRIETOR_PRODUCT);
            intent.putExtra("indentNo", orderCreateNo);
            //延时跳转到支付成功页面（因为线程问题，立即跳转可能会失效）
            new LifecycleHandler(this).postDelayed(() -> {
                startActivity(intent);
                finish();
            }, 1000);
        }
    }

    //已创建订单，取消支付，跳转到订单详情
    private void skipIndentDetail() {
        if (!TextUtils.isEmpty(orderCreateNo)) {
            Intent intent = new Intent(this, DirectExchangeDetailsActivity.class);
            intent.putExtra("orderNo", orderCreateNo);
            //延时跳转到订单详情页面（因为线程问题，立即跳转可能会失效）
            new LifecycleHandler(this).postDelayed(() -> {
                startActivity(intent);
                finish();
            }, 500);
        }
    }


    /**
     * 订单支付取消弹窗
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
                                        requestStatus.getDescription() : "好货不等人哦，喜欢就入了吧")
                                .setCancelText("去意已决").setConfirmText("继续支付")
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

    //跳转我的拼团订单
    private void skipMineGroupIndent() {
        Intent intent = new Intent(this, QualityGroupShopMineActivity.class);
        new LifecycleHandler(this).postDelayed(() -> {
            startActivity(intent);
            finish();
        }, 500);
    }

    //跳转开团成功页面
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
                //            获取地址成功
                AddressInfoBean addressInfoBean = data.getParcelableExtra("addressInfoBean");
                isFirst = false;
                setAddressData(addressInfoBean);
                break;
            case DIRECT_COUPON_REQ:
                //            获取优惠券
                couponId = data.getIntExtra("couponId", -1);
                String couponAmount = data.getStringExtra("couponAmount");
                pullFootView.tv_direct_product_favorable.setText(couponId < 1 ? "不使用优惠券" : "-¥" + couponAmount);
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
        //    地址为空
        @BindView(R.id.ll_indent_address_null)
        LinearLayout ll_indent_address_null;
        //   有默认地址
        @BindView(R.id.ll_indent_address_default)
        LinearLayout ll_indent_address_default;
        //    收件人名字
        @BindView(R.id.tv_consignee_name)
        TextView tv_consignee_name;
        //    收件人手机号码
        @BindView(R.id.tv_consignee_mobile_number)
        TextView tv_address_mobile_number;
        //    订单地址
        @BindView(R.id.tv_indent_details_address)
        TextView tv_indent_details_address;
        //    订单地址
        @BindView(R.id.img_skip_address)
        ImageView img_skip_address;
        //        海外购实名提示
        @BindView(R.id.tv_oversea_buy_tint)
        TextView tv_oversea_buy_tint;

        //    地址列表为空 跳转新建地址
        @OnClick(R.id.tv_lv_top)
        void skipNewAddress(View view) {
            Intent intent = new Intent(getActivity(), SelectedAddressActivity.class);
            intent.putExtra("hasDefaultAddress", false);
            startActivityForResult(intent, NEW_CRE_ADDRESS_REQ);
        }

        //  更换地址  跳转地址列表
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
        //优惠券说明
        @BindView(R.id.tv_direct_product_favorable)
        TextView tv_direct_product_favorable;
        //留言编辑
        @BindView(R.id.edt_direct_product_note)
        EditText edt_direct_product_note;
        @BindView(R.id.ll_indent_product_note)
        LinearLayout ll_indent_product_note;
        //        优惠券布局
        @BindView(R.id.ll_layout_coupon)
        LinearLayout ll_layout_coupon;
        //        总优惠
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

        //        优惠券选择
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
//            数量选择隐藏
            if (pullFootView.rect_indent_number.getVisibility() == VISIBLE) {
                pullFootView.rect_indent_number.setVisibility(GONE);
            }
//            留言禁止编辑
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
            return true;//不执行父类点击事件
        }
        return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KeyboardUtils.unregisterSoftInputChangedListener(this);
    }

    /**
     * 点击编辑器外区域隐藏键盘 避免点击搜索完没有隐藏键盘
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
