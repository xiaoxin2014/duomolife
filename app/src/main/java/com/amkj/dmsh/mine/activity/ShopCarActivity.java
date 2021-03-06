package com.amkj.dmsh.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.dao.ShopCarDao;
import com.amkj.dmsh.dominant.activity.QualityProductActActivity;
import com.amkj.dmsh.dominant.adapter.GoodProductAdapter;
import com.amkj.dmsh.mine.adapter.ShopCarGoodsAdapter;
import com.amkj.dmsh.mine.bean.ActivityInfoBean;
import com.amkj.dmsh.mine.bean.ShopCarEntity;
import com.amkj.dmsh.mine.bean.ShopCarEntity.ShopCartBean;
import com.amkj.dmsh.mine.bean.ShopCarEntity.ShopCartBean.CartBean;
import com.amkj.dmsh.mine.bean.ShopCarEntity.ShopCartBean.CartBean.CartInfoBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.shopdetails.bean.CombineGoodsBean;
import com.amkj.dmsh.shopdetails.bean.EditGoodsSkuEntity;
import com.amkj.dmsh.shopdetails.bean.ShopCarGoodsSku;
import com.amkj.dmsh.shopdetails.bean.SkuSaleBean;
import com.amkj.dmsh.user.bean.UserLikedProductEntity;
import com.amkj.dmsh.user.bean.LikedProductBean;
import com.amkj.dmsh.utils.DoubleUtil;
import com.amkj.dmsh.utils.KeyboardUtils;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.amkj.dmsh.views.RectAddAndSubShopcarView;
import com.amkj.dmsh.views.alertdialog.AlertDialogEcm;
import com.amkj.dmsh.views.alertdialog.AlertDialogHelper;
import com.amkj.dmsh.views.bottomdialog.SkuDialog;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static android.view.View.GONE;
import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStringChangeDouble;
import static com.amkj.dmsh.constant.ConstantMethod.getStringChangeIntegers;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsFormat;
import static com.amkj.dmsh.constant.ConstantMethod.showImportantToast;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.showToastRequestMsg;
import static com.amkj.dmsh.constant.ConstantMethod.stripTrailingZeros;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.ADD_NUM;
import static com.amkj.dmsh.constant.ConstantVariable.CHANGE_SKU;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_W_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.REDUCE_NUM;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_EIGHTY;
import static com.amkj.dmsh.constant.Url.MINE_SHOP_CAR_RECOMMEND_GOODS;
import static com.amkj.dmsh.constant.Url.NEW_MINE_SHOP_CAR_GOODS;
import static com.amkj.dmsh.constant.Url.NEW_PRO_SETTLE_PRICE;
import static com.amkj.dmsh.constant.Url.Q_SHOP_DETAILS_CHANGE_CAR;
import static com.amkj.dmsh.constant.Url.Q_SHOP_DETAILS_DEL_CAR;
import static com.amkj.dmsh.constant.Url.Q_SHOP_DETAILS_GET_SKU_CAR;
import static com.amkj.dmsh.dao.ShopCarDao.MATCH_INVALID;
import static com.amkj.dmsh.dao.ShopCarDao.NORMAL;
import static com.amkj.dmsh.dao.ShopCarDao.getCartIds;
import static com.amkj.dmsh.dao.ShopCarDao.isMatchInValid;
import static com.amkj.dmsh.dao.ShopCarDao.removeSelGoods;
import static com.amkj.dmsh.dao.ShopCarDao.updateGoodsInfo;

/**
 * Created by atd48 on 2016/10/22.
 */
public class ShopCarActivity extends BaseActivity {
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tv_header_shared)
    TextView header_shared;
    //    结算页面
    @BindView(R.id.ll_communal_settlement)
    LinearLayout ll_settlement_shop_car;
    // 全选 全不选
    @BindView(R.id.check_box_all_buy)
    CheckBox check_box_all_buy;
    //总价
    @BindView(R.id.tv_communal_total_price)
    TextView tv_cart_total;
    //结算
    @BindView(R.id.tv_communal_buy_or_count)
    TextView tv_cart_buy_orCount;
    //    优惠金额
    @BindView(R.id.tv_settlement_dis_car_price)
    public TextView tv_settlement_dis_car_price;

    //    删除页面
    @BindView(R.id.rel_del_shop_car)
    RelativeLayout rel_del_shop_car;
    // 全选 全不选
    @BindView(R.id.check_box_all_del)
    CheckBox check_box_all_del;

    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    //    底栏
    @BindView(R.id.rel_shop_car_bottom)
    public RelativeLayout rel_shop_car_bottom;
    @BindView(R.id.tl_normal_bar)
    Toolbar tl_normal_bar;
    private int page = 1;
    private int shopCartNum = 0;
    private Boolean isEditStatus = Boolean.FALSE;
    private List<MultiItemEntity> shopGoodsList = new ArrayList<>();
    private List<LikedProductBean> cartProRecommendList = new ArrayList<>();
    private ShopCarGoodsAdapter shopCarGoodsAdapter;
    private boolean isOnPause;
    private RecommendHeaderView recommendHeaderView;
    private View cartHeaderView;
    private GoodProductAdapter mGoodProductAdapter;
    private UserLikedProductEntity likedProduct;
    private AlertDialogHelper alertDialogHelper;
    private ShopCarEntity mShopCarNewInfoEntity;
    private boolean first = true;
    private int mNum;
    private int mPosition;
    private AlertDialogEcm mAlertDialogEcm;

    @Override
    protected int getContentView() {
        return R.layout.activity_shop_car;
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
        rel_shop_car_bottom.setVisibility(View.GONE);
        tl_normal_bar.setSelected(true);
        tv_header_titleAll.setText("购物车");
        header_shared.setCompoundDrawables(null, null, null, null);
        header_shared.setText("编辑");
        header_shared.setSelected(false);

        setFloatingButton(download_btn_communal, communal_recycler);
        smart_communal_refresh.setOnRefreshListener(refreshLayout -> {
            check_box_all_buy.setChecked(false);
            check_box_all_del.setChecked(false);
            loadData();
        });

        //初始化购物车商品列表
        shopCarGoodsAdapter = new ShopCarGoodsAdapter(ShopCarActivity.this, shopGoodsList);
        communal_recycler.setLayoutManager(new LinearLayoutManager(this));
        communal_recycler.setAdapter(shopCarGoodsAdapter);
        shopCarGoodsAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            CartInfoBean cartInfoBean = null;
            ActivityInfoBean activityInfoBean = null;
            if (view.getId() == R.id.ll_communal_activity_tag_rule) {
                activityInfoBean = (ActivityInfoBean) view.getTag();
            } else {
                cartInfoBean = (CartInfoBean) view.getTag();
                if (cartInfoBean == null) {
                    cartInfoBean = (CartInfoBean) view.getTag(R.id.shop_car_cb);
                }
            }

            switch (view.getId()) {
                //单个选中或者取消
                case R.id.cb_shop_car_sel:
                    int status = ShopCarDao.isMatchInValid(shopCarGoodsAdapter, cartInfoBean);
                    if (status == NORMAL || isEditStatus) {
                        ShopCarDao.selectOne(shopGoodsList, cartInfoBean, isEditStatus);
                        //商品有效并且不在编辑状态时更新结算价格
                        if (isEditStatus) {
                            shopCarGoodsAdapter.notifyDataSetChanged();
                        } else if (cartInfoBean.isValid()) {
                            getSettlePrice(cartInfoBean, null, true);
                        }
                    } else {
                        showToast(status == MATCH_INVALID ? "搭配商品失效" : "搭配商品无库存");
                    }

                    break;
                //修改购物车商品属性
                case R.id.tv_shop_car_product_sku:
                    if (isEditStatus) {
                        getGoodsSkuDetails(cartInfoBean, view);
                    } else {
                        skipProDetail(cartInfoBean);
                    }
                    break;
                //跳转活动专场
                case R.id.ll_communal_activity_tag_rule:
                    Intent intent = new Intent(ShopCarActivity.this, QualityProductActActivity.class);
                    intent.putExtra("activityCode", activityInfoBean.getActivityCode());
                    startActivity(intent);
                    break;
                //增加数量
                case R.id.img_integration_details_credits_add:
                    int oldCount = cartInfoBean.getCount();
                    int newNum = oldCount + 1;
                    if (oldCount > 0 && cartInfoBean.isValid()) {
                        int quantity = cartInfoBean.getSaleSku().getQuantity();
                        if (newNum <= quantity) {
                            if (cartInfoBean.isMainProduct() || cartInfoBean.isCombineProduct()) {
                                showToast("组合商品无法修改数量");
                            } else {
                                changeGoods(null, newNum, ADD_NUM, cartInfoBean);
                            }
                        } else {
                            showToast(R.string.product_sell_out);
                        }
                    }
                    break;
                //减少数量
                case R.id.img_integration_details_credits_minus:
                    oldCount = cartInfoBean.getCount();
                    newNum = oldCount - 1;
                    if (cartInfoBean.isValid()) {
                        if (newNum > 0) {
                            if (cartInfoBean.isMainProduct() || cartInfoBean.isCombineProduct()) {
                                showToast("组合商品无法修改数量");
                            } else {
                                changeGoods(null, newNum, REDUCE_NUM, cartInfoBean);
                            }
                        } else {
                            showToast(R.string.product_small_count);
                        }
                    }
                    break;
            }
        });
        shopCarGoodsAdapter.setOnLoadMoreListener(() -> {
            page++;
            getShopCarProInfo();
        }, communal_recycler);

        //初始化推荐商品列表
        cartHeaderView = LayoutInflater.from(ShopCarActivity.this).
                inflate(R.layout.layout_cart_recommend, null, false);
        recommendHeaderView = new RecommendHeaderView();
        ButterKnife.bind(recommendHeaderView, cartHeaderView);
        recommendHeaderView.initViews();

        //监听软键盘
        KeyboardUtils.registerSoftInputChangedListener(this, height -> {
            try {
                if (!first) {//默认会调用一次
                    if (height == 0) {
                        //软键盘隐藏
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, AutoSizeUtils.mm2px(mAppContext, 98));
                        rel_shop_car_bottom.setLayoutParams(params);
                        RectAddAndSubShopcarView rectAddAndSubView = (RectAddAndSubShopcarView) shopCarGoodsAdapter.getViewByPosition(mPosition, R.id.communal_rect_add_sub);
                        MultiItemEntity item = shopCarGoodsAdapter.getItem(mPosition);
                        if (rectAddAndSubView != null) {
                            if (mNum <= 0) {//最小购买数量为1件
                                mNum = 1;
                                rectAddAndSubView.setNum(1);
                            } else if (mNum > rectAddAndSubView.getMaxNum()) {//最大数量不能超过库存
                                mNum = rectAddAndSubView.getMaxNum();
                                rectAddAndSubView.setNum(mNum);
                                showImportantToast(getActivity(), R.string.product_sell_out);
                            }
                            if (item instanceof CartInfoBean && ((CartInfoBean) item).getCount() != mNum) {
                                if (mNum <= rectAddAndSubView.getMaxNum()) {
                                    changeGoods(null, mNum, -1, (CartInfoBean) item);
                                }
                            }
                        }
                    } else {
                        //软键盘显示
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
                        rel_shop_car_bottom.setLayoutParams(params);
                    }
                }
                first = false;
            } catch (Exception e) {
                CrashReport.postCatchedException(new Exception("购物车手动修改数量异常：" + e.getMessage()));
            }
        });
    }

    private void skipProDetail(CartInfoBean cartInfoBean) {
        if (cartInfoBean != null && cartInfoBean.getStatus() == 1 && cartInfoBean.getSaleSku() != null && !isEditStatus) {
            Intent intent = new Intent(ShopCarActivity.this, ShopScrollDetailsActivity.class);
            intent.putExtra("productId", String.valueOf(cartInfoBean.getProductId()));
            startActivity(intent);
        }
    }

    private void setEditStatus() {
        isEditStatus = !isEditStatus;
        header_shared.setText(isEditStatus ? "完成" : "编辑");
        header_shared.setSelected(isEditStatus);
        //保存状态
        shopCarGoodsAdapter.setEditStatus(isEditStatus);
        shopCarGoodsAdapter.notifyDataSetChanged();
        if (isEditStatus) {
            //编辑状态
            check_box_all_del.setChecked(false);
            ll_settlement_shop_car.setVisibility(View.GONE);
            rel_del_shop_car.setVisibility(View.VISIBLE);
        } else {
            //完成编辑
            ll_settlement_shop_car.setVisibility(View.VISIBLE);
            rel_del_shop_car.setVisibility(View.GONE);
        }
    }

    @Override
    protected void loadData() {
        page = 1;
        if (userId > 0) {
            getShopCarProInfo();
        }
    }

    @Override
    public View getLoadView() {
        return smart_communal_refresh;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    protected int getEmptyResId() {
        return R.drawable.cart_empty_icon;
    }

    private void getShopCarProInfo() {
        boolean allCheckedStatus = isEditStatus ? check_box_all_del.isChecked() : check_box_all_buy.isChecked();
        Map<String, Object> params = new HashMap<>();
        params.put("showCount", TOTAL_COUNT_EIGHTY);
        params.put("currentPage", page);
        params.put("userId", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, NEW_MINE_SHOP_CAR_GOODS,
                params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();

                        mShopCarNewInfoEntity = GsonUtils.fromJson(result, ShopCarEntity.class);
                        if (mShopCarNewInfoEntity != null) {
                            ShopCartBean shopCartBean = mShopCarNewInfoEntity.getResult();
                            String code = mShopCarNewInfoEntity.getCode();

                            if (shopCartBean == null || shopCartBean.getCarts() == null || shopCartBean.getCarts().size() == 0 || EMPTY_CODE.equals(code)) {
                                shopCarGoodsAdapter.loadMoreEnd();
                                getCartRecommend();
                            } else if (SUCCESS_CODE.equals(code)) {
                                if (page == 1) {
                                    shopGoodsList.clear();
                                }
                                List<CartBean> carts = shopCartBean.getCarts();
                                List<CartBean> rubbishCarts = shopCartBean.getRubbishCarts();

                                //添加有效商品
                                addShopCartInfo(true, carts);
                                //添加失效商品
                                addShopCartInfo(false, rubbishCarts);

                                shopCarGoodsAdapter.expandAll();
                                shopCarGoodsAdapter.notifyDataSetChanged();
                                shopCarGoodsAdapter.loadMoreComplete();
                                //更新购物车数量
                                shopCartNum = shopCartBean.getTotalCount();
                                tv_header_titleAll.setText(shopCartNum < 1 ? "购物车" : "购物车(" + shopCartNum + ")");

                                if (page == 1) {
                                    updatePrice(shopCartBean, true);
                                } else {
                                    //如果加载第二页时，选中了全选
                                    if (allCheckedStatus) {
                                        getSettlePrice(null, null, false);
                                    } else {
                                        updatePrice(shopCartBean, false);
                                    }
                                }
                            } else {
                                shopCarGoodsAdapter.loadMoreFail();
                                getCartRecommend();
                                showToast(mShopCarNewInfoEntity.getMsg());
                            }
                        } else {
                            shopCarGoodsAdapter.loadMoreFail();
                            getCartRecommend();
                        }

                        rel_shop_car_bottom.setVisibility(shopGoodsList.size() < 1 ? View.GONE : View.VISIBLE);
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, shopGoodsList, mShopCarNewInfoEntity);
                    }

                    @Override
                    public void onNotNetOrException() {
                        smart_communal_refresh.finishRefresh();
                        shopCarGoodsAdapter.loadMoreFail();
                        rel_shop_car_bottom.setVisibility(View.GONE);
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, shopGoodsList, mShopCarNewInfoEntity);
                    }
                });
    }

    private void addShopCartInfo(boolean isValid, List<CartBean> carts) {
        for (int i = 0; i < carts.size(); i++) {
            CartBean cartBean = carts.get(i);
            if (cartBean != null) {
                ActivityInfoBean activityInfoBean = cartBean.getActivityInfo();
                if (activityInfoBean == null) {
                    activityInfoBean = new ActivityInfoBean();
                }
                List<CartInfoBean> cartInfoList = cartBean.getCartInfoList();
                CartInfoBean combineMainProduct = cartBean.getCombineMainProduct();
                List<CartInfoBean> combineMatchProducts = cartBean.getCombineMatchProducts();
                //判断是否是组合搭配商品
                if (combineMainProduct != null) {
                    cartInfoList = new ArrayList<>();
                    //添加主商品
                    combineMainProduct.setMainProduct(true);//设置主商品标志
                    combineMainProduct.setValid(isValid);
                    cartInfoList.add(combineMainProduct);

                    //添加搭配商品
                    if (combineMatchProducts != null && combineMatchProducts.size() > 0) {
                        //当天加入购物车的组合商品如果失效，不会默认选中
                        if (isValid && !isMatchInValid(combineMatchProducts)) {
                            combineMainProduct.setSelected(false);
                        }
                        for (CartBean.CartInfoBean cartInfoBean : combineMatchProducts) {
                            cartInfoBean.setId(combineMainProduct.getId());
                            cartInfoBean.setCombineProduct(true);
                            cartInfoBean.setCount(combineMainProduct.getCount());
                            cartInfoBean.setSelected(combineMainProduct.isSelected());
                            cartInfoBean.setValid(isValid);

                        }
                        //只要主商品失效，所有的搭配商品都会失效
                        if (!combineMainProduct.isValid()) {
                            for (CartBean.CartInfoBean cartInfoBean : combineMatchProducts) {
                                cartInfoBean.setValid(false);
                            }
                        }
                        cartInfoList.addAll(combineMatchProducts);
                    }
                }

                //普通商品
                if (cartInfoList != null && cartInfoList.size() > 0) {
                    for (int j = 0; j < cartInfoList.size(); j++) {
                        CartInfoBean cartInfoBean = cartInfoList.get(j);
                        if (!cartInfoBean.isMainProduct() && !cartInfoBean.isCombineProduct()) {
                            cartInfoBean.setValid(isValid);
                        }


                        if (!isValid) {
                            cartInfoBean.setSelected(false);
                        } else if (!isEditStatus && check_box_all_buy.isChecked()) {
                            //加载数据时如果是全选状态,手动选中所有有效商品
                            cartInfoBean.setSelected(true);
                        }

                        activityInfoBean.addSubItem(cartInfoBean);
                    }
                }

                shopGoodsList.add(activityInfoBean);

            }

        }
    }

    private void updatePrice(ShopCartBean shopCartBean, boolean isPage1) {
        if (shopCartBean != null) {
            //结算商品金额
            double price = shopCartBean.getTotalProductPrice();
            double discount = shopCartBean.getTotalProductDiscountPrice();
            if (!isPage1) {
                price = DoubleUtil.add(getStringChangeDouble(tv_cart_total.getText().toString().trim()), price);
                discount = DoubleUtil.add(getStringChangeDouble(tv_settlement_dis_car_price.getText().toString().trim()), discount);
            }
            tv_cart_total.setText(getStringsFormat(this, R.string.group_total_price, stripTrailingZeros(String.valueOf(price))));
            //优惠金额
            tv_settlement_dis_car_price.setText(getStringsFormat(this, R.string.newshopcar_discount_price, stripTrailingZeros(String.valueOf(discount))));
            tv_settlement_dis_car_price.setVisibility(discount != 0 ? View.VISIBLE : GONE);
            //结算商品件数
            String[] shoppingInfo = ShopCarDao.getShoppingCount(shopGoodsList);
            tv_cart_buy_orCount.setText(("去结算(" + shoppingInfo[0] + ")"));
        }
    }

    //本地计算结算金额
    private void updateLocalPrice() {
        showToast(R.string.refrence_only);
        String[] shoppingInfo = ShopCarDao.getShoppingCount(shopGoodsList);
        tv_settlement_dis_car_price.setVisibility(View.GONE);
        tv_cart_buy_orCount.setText(("去结算(" + shoppingInfo[0] + ")")); //结算商品件数
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            if (requestCode == ConstantVariable.IS_LOGIN_CODE) {
                finish();
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.tv_life_back, R.id.tv_communal_buy_or_count, R.id.tv_header_shared, R.id.tv_shop_car_del})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_life_back:
                finish();
                break;
            //结算
            case R.id.tv_communal_buy_or_count:
                List<CartInfoBean> settlementGoods = ShopCarDao.getSettlementGoods(shopGoodsList)[0];
                List<CartInfoBean> settlementEcmGoods = ShopCarDao.getSettlementGoods(shopGoodsList)[1];
                List<CombineGoodsBean> combineGoods = ShopCarDao.getCombineGoods(shopGoodsList)[0];
                List<CombineGoodsBean> combineEcmGoods = ShopCarDao.getCombineGoods(shopGoodsList)[1];
                //包含保税仓商品以及普通商品需要分开结算
                if (settlementGoods.size() + combineGoods.size() > 0 && settlementEcmGoods.size() + combineEcmGoods.size() > 0) {
                    if (mAlertDialogEcm == null) {
                        mAlertDialogEcm = new AlertDialogEcm(this);
                    }
                    mAlertDialogEcm.setOnSettleListener(isEcm -> {
                        skipWrite(isEcm ? settlementEcmGoods : settlementGoods, isEcm ? combineEcmGoods : combineGoods);
                    });
                    mAlertDialogEcm.show();
                } else if (settlementGoods.size() + combineGoods.size() > 0) {//只有普通商品
                    skipWrite(settlementGoods, combineGoods);
                } else {//只有保税仓商品
                    skipWrite(settlementEcmGoods, combineEcmGoods);
                }
                break;
            //点击编辑或者完成
            case R.id.tv_header_shared:
                setEditStatus();
                break;
            //删除
            case R.id.tv_shop_car_del:
                setDeleteGoodsDialog();
                break;
        }
    }

    //结算商品 跳转订单填写
    private void skipWrite(List<CartInfoBean> settlementGoods, List<CombineGoodsBean> combineGoods) {
        if (settlementGoods.size() > 0 || combineGoods.size() > 0) {
            Bundle bundle = new Bundle();
            if (settlementGoods.size() > 0) {
                bundle.putString("goods", GsonUtils.toJson(settlementGoods));
            }

            if (combineGoods.size() > 0) {
                bundle.putString("combineGoods", GsonUtils.toJson(combineGoods));
            }
            ConstantMethod.skipIndentWrite(getActivity(), INDENT_W_TYPE, bundle);
        } else {
            showToast("请先选择商品");
        }
    }


    //    全选删除
    @OnCheckedChanged(R.id.check_box_all_del)
    void allCheckDel(boolean isChecked) {
        if (smart_communal_refresh.getState() == RefreshState.None) {
            ShopCarDao.selectDeleteAll(shopGoodsList, isChecked);
            shopCarGoodsAdapter.notifyDataSetChanged();
        }
    }

    //    全选结算
    @OnCheckedChanged(R.id.check_box_all_buy)
    void allCheckBuy(boolean isChecked) {
        if (!isEditStatus && smart_communal_refresh.getState() == RefreshState.None) {
            ShopCarDao.selectBuyAll(shopCarGoodsAdapter, shopGoodsList, isChecked);
            shopCarGoodsAdapter.notifyDataSetChanged();
            if (isChecked) {
                getSettlePrice(null, ShopCarDao.getActivityInfos(shopGoodsList), true);
            } else {
                tv_cart_total.setText(("¥0.00"));
                tv_settlement_dis_car_price.setVisibility(View.GONE);
                tv_cart_buy_orCount.setText(("去结算(" + 0 + ")"));
            }
        }
    }


    //删除选中商品弹窗
    private void setDeleteGoodsDialog() {
        String[] selGoodsInfo = ShopCarDao.getSelGoodsInfo(shopGoodsList);
        if (getStringChangeDouble(selGoodsInfo[1]) > 0) {
            if (alertDialogHelper == null) {
                alertDialogHelper = new AlertDialogHelper(ShopCarActivity.this);
                alertDialogHelper.setTitleVisibility(View.GONE).setMsgTextGravity(Gravity.CENTER)
                        .setMsg("确定删除选中商品").setCancelText("取消").setConfirmText("确定")
                        .setCancelTextColor(getResources().getColor(R.color.text_login_gray_s));
                alertDialogHelper.setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                    @Override
                    public void confirm() {
                        //            确定删除商品
                        delSelGoods();
                    }

                    @Override
                    public void cancel() {
                    }
                });
            }
            alertDialogHelper.show();
        } else {
            showToast("请选择你要删除的商品");
        }
    }

    //删除商品
    private void delSelGoods() {
        if (loadHud == null) return;
        loadHud.show();
        Map<String, Object> params = new HashMap<>();
        String[] selGoodsInfo = ShopCarDao.getSelGoodsInfo(shopGoodsList);
        params.put("userId", userId);
        params.put("ids", selGoodsInfo[0]);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Q_SHOP_DETAILS_DEL_CAR,
                params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        RequestStatus status = GsonUtils.fromJson(result, RequestStatus.class);
                        if (status != null && status.getCode().equals(SUCCESS_CODE)) {
                            shopCartNum = shopCartNum - getStringChangeIntegers(selGoodsInfo[1]);
                            tv_header_titleAll.setText(shopCartNum < 1 ? "购物车" : "购物车(" + shopCartNum + ")");
                            List<ActivityInfoBean> activityList = removeSelGoods(shopGoodsList, shopCarGoodsAdapter);
                            getSettlePrice(null, activityList, true);
                        } else {
                            loadHud.dismiss();
                            showToastRequestMsg(status);
                        }
                    }

                    @Override
                    public void onNotNetOrException() {
                        loadHud.dismiss();
                    }
                });
    }

    //获取商品全部sku
    private void getGoodsSkuDetails(CartInfoBean cartInfoBean, View textView) {
        Map<String, Object> params = new HashMap<>();
        params.put("productId", cartInfoBean.getProductId());
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Q_SHOP_DETAILS_GET_SKU_CAR, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {

                EditGoodsSkuEntity editGoodsSkuEmpty = GsonUtils.fromJson(result, EditGoodsSkuEntity.class);
                if (editGoodsSkuEmpty != null && editGoodsSkuEmpty.getEditGoodsSkuBean() != null) {
                    List<SkuSaleBean> skuSale = editGoodsSkuEmpty.getEditGoodsSkuBean().getSkuSale();
                    if (editGoodsSkuEmpty.getCode().equals(SUCCESS_CODE)) {
                        if (skuSale != null && skuSale.size() > 1) {
                            getProperty(editGoodsSkuEmpty.getEditGoodsSkuBean(), cartInfoBean);
                        } else {
                            textView.setSelected(false);
                        }
                    } else {
                        showToast(editGoodsSkuEmpty.getMsg());
                    }
                }
            }
        });
    }

    private void getProperty(/*商品属性*/final EditGoodsSkuEntity.EditGoodsSkuBean shopProperty, /*购物车*/final CartInfoBean cartInfoBean) {
//        sku 展示
        SkuDialog skuDialog = new SkuDialog(ShopCarActivity.this);
        if (!TextUtils.isEmpty(cartInfoBean.getPicUrl())) {
            shopProperty.setPicUrl(cartInfoBean.getPicUrl());
        }
        if (TextUtils.isEmpty(shopProperty.getProductName())) {
            shopProperty.setProductName(cartInfoBean.getName() == null ? "" : cartInfoBean.getName());
        }
        if (cartInfoBean.getSaleSku() != null) {
            if (cartInfoBean.getSaleSku().getId() != 0) {
                shopProperty.setShopCarEdit(true);
                shopProperty.setSkuId(cartInfoBean.getSaleSku().getId());
            }
        }
        if (cartInfoBean.getCount() > 0) {
            shopProperty.setOldCount(cartInfoBean.getCount());
        }
        shopProperty.setShowBottom(true);
        skuDialog.refreshView(shopProperty);
        skuDialog.show();
        skuDialog.getGoodsSKu(shopCarGoodsSku -> {
            if (shopCarGoodsSku != null && (shopCarGoodsSku.getSaleSkuId() != shopProperty.getSkuId() ||
                    shopCarGoodsSku.getCount() != shopProperty.getOldCount())) {
                //修改商品属性
                changeGoods(shopCarGoodsSku, null, CHANGE_SKU, cartInfoBean);
            }
        });
    }


    /**
     * 修改商品信息
     *
     * @param shopCarGoodsSku 修改后的sku
     * @param newNum          修改后的数量
     * @param type            修改类型
     */
    private void changeGoods(ShopCarGoodsSku shopCarGoodsSku, Integer newNum, int type, CartInfoBean cartInfoBean) {
        loadHud.show();
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("count", newNum != null ? newNum : cartInfoBean.getCount());
        params.put("productId", cartInfoBean.getProductId());
        params.put("saleSkuId", shopCarGoodsSku != null ? shopCarGoodsSku.getSaleSkuId() : cartInfoBean.getSaleSku().getId());
        params.put("price", shopCarGoodsSku != null ? shopCarGoodsSku.getPrice() + "" : cartInfoBean.getSaleSku().getPrice());
        params.put("id", cartInfoBean.getId());
        try {
            ActivityInfoBean activityInfoBean = (ActivityInfoBean) shopGoodsList.get(shopCarGoodsAdapter.getParentPosition(cartInfoBean));
            if (activityInfoBean != null) {
                params.put("activityCode", activityInfoBean.getActivityCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Q_SHOP_DETAILS_CHANGE_CAR, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                RequestStatus status = GsonUtils.fromJson(result, RequestStatus.class);
                if (status != null && status.getCode().equals(SUCCESS_CODE)) {
                    //修改完成后台会默认选中（自动添加到当天加入购物车的商品）
                    cartInfoBean.setSelected(true);
                    //修改完成更新结算金额
                    getSettlePrice(cartInfoBean, null, true);
                    //更新购物车商品数量
                    if (type == ADD_NUM) {
                        shopCartNum++;
                        tv_header_titleAll.setText(shopCartNum < 1 ? "购物车" : "购物车(" + shopCartNum + ")");
                    } else if (type == REDUCE_NUM) {
                        shopCartNum--;
                        tv_header_titleAll.setText(shopCartNum < 1 ? "购物车" : "购物车(" + shopCartNum + ")");
                    }

                } else {
                    loadHud.dismiss();
                    showToastRequestMsg(status);
                }
            }

            @Override
            public void onNotNetOrException() {
                loadHud.dismiss();
            }
        });
    }


    /**
     * 获取结算金额
     *
     * @param cartInfoBean 被修改的商品
     * @param delGoods     需要更新的活动集合
     * @param isNotifyItem 是否需要通知刷新商品(单个选中或取消选中，修改数量以及sku时需要刷新)
     */
    private void getSettlePrice(CartInfoBean cartInfoBean, List<ActivityInfoBean> delGoods, boolean isNotifyItem) {
        if (shopGoodsList.size() > 0) {
            if (!loadHud.isShowing()) {
                loadHud.show();
            }
            Map<String, Object> params = new HashMap<>();
            List<Integer> cartIds = getCartIds(shopGoodsList);
            params.put("cartIds", cartIds);
            params.put("userId", userId);
            NetLoadUtils.getNetInstance().loadNetDataPost(this, NEW_PRO_SETTLE_PRICE, params, new NetLoadListenerHelper() {
                @Override
                public void onSuccess(String result) {
                    loadHud.dismiss();

                    ShopCarEntity shopCarNewInfoEntity = GsonUtils.fromJson(result, ShopCarEntity.class);
                    if (shopCarNewInfoEntity != null) {
                        ShopCartBean shopCartBean = shopCarNewInfoEntity.getResult();
                        if (SUCCESS_CODE.equals(shopCarNewInfoEntity.getCode()) && shopCartBean != null) {
                            //刷新条目
                            if (isNotifyItem) {
                                updateGoodsInfo(shopCarGoodsAdapter, shopCartBean, cartInfoBean, delGoods);
                            }
                            //更新结算金额
                            updatePrice(shopCarNewInfoEntity.getResult(), true);
                        } else if (EMPTY_CODE.equals(shopCarNewInfoEntity.getCode())) {
                            tv_cart_total.setText(("¥" + "0.00"));//结算金额
                            tv_settlement_dis_car_price.setVisibility(View.GONE);
                            tv_cart_buy_orCount.setText(("去结算(" + 0 + ")")); //结算商品件数
                            shopCarGoodsAdapter.notifyDataSetChanged();
                        } else {
                            updateLocalPrice();
                        }
                    }
                }

                @Override
                public void onNotNetOrException() {
                    loadHud.dismiss();
                }
            });
        }
    }

    /**
     * 购物车有商品推荐
     */
    private void getCartRecommend() {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, MINE_SHOP_CAR_RECOMMEND_GOODS, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                cartProRecommendList.clear();
                likedProduct = GsonUtils.fromJson(result, UserLikedProductEntity.class);
                if (likedProduct != null) {
                    if (likedProduct.getCode().equals(SUCCESS_CODE)) {
                        cartProRecommendList.addAll(likedProduct.getGoodsList());
                        if (cartProRecommendList.size() > 0) {
                            if (shopCarGoodsAdapter.getFooterLayoutCount() == 0) {
                                shopCarGoodsAdapter.addFooterView(cartHeaderView);
                                recommendHeaderView.tv_pro_title.setText("-猜你喜欢-");
                            }
                            mGoodProductAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });
    }

    public class RecommendHeaderView {
        @BindView(R.id.tv_pro_title)
        TextView tv_pro_title;
        @BindView(R.id.communal_recycler_wrap)
        RecyclerView communal_recycler_wrap;

        public void initViews() {
            communal_recycler_wrap.setLayoutManager(new GridLayoutManager(ShopCarActivity.this, 2));
            communal_recycler_wrap.addItemDecoration(new ItemDecoration.Builder()
                    // 设置分隔线资源ID
                    .setDividerId(R.drawable.item_divider_five_gray_f).create());
            mGoodProductAdapter = new GoodProductAdapter(ShopCarActivity.this, cartProRecommendList);
            communal_recycler_wrap.setAdapter(mGoodProductAdapter);
            mGoodProductAdapter.setEnableLoadMore(false);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        isOnPause = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isOnPause) {
            //默认不要全选
            check_box_all_buy.setChecked(false);
            loadData();
        }
        isOnPause = false;
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

    public void changeSkuNum(int position, int num) {
        mPosition = position;
        mNum = num;
    }

}
