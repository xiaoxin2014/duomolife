package com.amkj.dmsh.mine.activity;

import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.dominant.activity.QualityProductActActivity;
import com.amkj.dmsh.dominant.activity.ShopTimeScrollDetailsActivity;
import com.amkj.dmsh.homepage.adapter.ProNoShopCarAdapter;
import com.amkj.dmsh.mine.adapter.ShopCarGoodsAdapter;
import com.amkj.dmsh.mine.bean.ShopCarEntity;
import com.amkj.dmsh.mine.bean.ShopCarEntity.ShopCartBean;
import com.amkj.dmsh.mine.bean.ShopCarEntity.ShopCartBean.CartBean;
import com.amkj.dmsh.mine.bean.ShopCarEntity.ShopCartBean.CartBean.ActivityInfoBean;
import com.amkj.dmsh.mine.bean.ShopCarEntity.ShopCartBean.CartBean.CartInfoBean;
import com.amkj.dmsh.mine.biz.ShopCarDao;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.activity.DirectIndentWriteActivity;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.shopdetails.bean.EditGoodsSkuEntity;
import com.amkj.dmsh.shopdetails.bean.ShopCarGoodsSku;
import com.amkj.dmsh.shopdetails.bean.SkuSaleBean;
import com.amkj.dmsh.shopdetails.integration.IntegralScrollDetailsActivity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean;
import com.amkj.dmsh.utils.alertdialog.AlertDialogHelper;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.amkj.dmsh.views.bottomdialog.SkuDialog;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.amkj.dmsh.constant.ConstantMethod.getDoubleFormat;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStringChangeDouble;
import static com.amkj.dmsh.constant.ConstantMethod.getStringChangeIntegers;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.showToastRequestMsg;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.ADD_NUM;
import static com.amkj.dmsh.constant.ConstantVariable.CHANGE_SKU;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.RECOMMEND_CAR;
import static com.amkj.dmsh.constant.ConstantVariable.RECOMMEND_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.REDUCE_NUM;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_FORTY;
import static com.amkj.dmsh.constant.Url.MINE_SHOP_CAR_RECOMMEND_GOODS;
import static com.amkj.dmsh.constant.Url.NEW_MINE_SHOP_CAR_GOODS;
import static com.amkj.dmsh.constant.Url.NEW_PRO_SETTLE_PRICE;
import static com.amkj.dmsh.constant.Url.Q_SHOP_DETAILS_CHANGE_CAR;
import static com.amkj.dmsh.constant.Url.Q_SHOP_DETAILS_DEL_CAR;
import static com.amkj.dmsh.constant.Url.Q_SHOP_DETAILS_GET_SKU_CAR;
import static com.amkj.dmsh.mine.biz.ShopCarDao.getCartIds;
import static com.amkj.dmsh.mine.biz.ShopCarDao.isValid;
import static com.amkj.dmsh.mine.biz.ShopCarDao.matchCartId;
import static com.amkj.dmsh.mine.biz.ShopCarDao.removeSelGoods;

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
    private List<CartInfoBean> shopGoodsList = new ArrayList<>();
    private List<LikedProductBean> cartProRecommendList = new ArrayList<>();
    private ShopCarGoodsAdapter shopCarGoodsAdapter;
    private boolean isOnPause;
    private RecommendHeaderView recommendHeaderView;
    private View cartHeaderView;
    private ProNoShopCarAdapter proNoShopCarAdapter;
    private UserLikedProductEntity likedProduct;
    private AlertDialogHelper alertDialogHelper;
    private ShopCarEntity mShopCarNewInfoEntity;

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
        shopCarGoodsAdapter = new ShopCarGoodsAdapter(ShopCarActivity.this, shopGoodsList);
        communal_recycler.setLayoutManager(new LinearLayoutManager(this));
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_gray_f_two_px).create());
        communal_recycler.setAdapter(shopCarGoodsAdapter);

        smart_communal_refresh.setOnRefreshListener(refreshLayout -> {
            check_box_all_buy.setChecked(false);
            check_box_all_del.setChecked(false);
            loadData();
        });
        shopCarGoodsAdapter.setOnLoadMoreListener(() -> {
            page++;
            getShopCarProInfo();
        }, communal_recycler);

        shopCarGoodsAdapter.setOnItemClickListener((adapter, view, position) -> {
            CartInfoBean cartInfoBean = (CartInfoBean) view.getTag();
            skipProDetail(cartInfoBean);
        });
        shopCarGoodsAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            CartInfoBean cartInfoBean = (CartInfoBean) view.getTag(R.id.shop_car_cb);
            if (cartInfoBean == null) {
                cartInfoBean = (CartInfoBean) view.getTag();
            }
            if (cartInfoBean != null) {
                cartInfoBean.setPosition(position);
                switch (view.getId()) {
                    //单个选中或者取消
                    case R.id.cb_shop_car_sel:
                        ShopCarDao.selectOne(shopGoodsList, position, isEditStatus);
                        //商品有效并且不在编辑状态时更新结算价格
                        if (isValid(cartInfoBean) && !isEditStatus) {
                            getSettlePrice(cartInfoBean, true, true);
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
                    case R.id.tv_communal_activity_tag_next:
                    case R.id.tv_communal_activity_tag_rule:
                        Intent intent = new Intent(ShopCarActivity.this, QualityProductActActivity.class);
                        intent.putExtra("activityCode", cartInfoBean.getActivityInfoData().getActivityCode());
                        startActivity(intent);
                        break;
                    //增加数量
                    case R.id.img_integration_details_credits_add:
                        int oldCount = cartInfoBean.getCount();
                        int newNum = oldCount + 1;
                        if (oldCount > 0 && isValid(cartInfoBean)) {
                            int quantity = cartInfoBean.getSaleSku().getQuantity();
                            if (newNum <= quantity) {
                                if (cartInfoBean.isMainProduct()) {
                                    showToast(this, "组合商品无法修改数量");
                                } else {
                                    changeGoods(null, newNum, ADD_NUM, cartInfoBean);
                                }
                            } else {
                                showToast(this, R.string.product_sell_out);
                            }
                        }
                        break;
                    //减少数量
                    case R.id.img_integration_details_credits_minus:
                        oldCount = cartInfoBean.getCount();
                        newNum = oldCount - 1;
                        if (isValid(cartInfoBean)) {
                            if (newNum > 0) {
                                if (cartInfoBean.isMainProduct()) {
                                    showToast(this, "组合商品无法修改数量");
                                } else {
                                    changeGoods(null, newNum, REDUCE_NUM, cartInfoBean);
                                }
                            } else {
                                showToast(this, R.string.product_small_count);
                            }
                        }
                        break;
                }
            }
        });
        communal_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                //特殊布局 特殊处理
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                if (lastVisibleItemPosition > 15) {
                    if (download_btn_communal.getVisibility() == GONE) {
                        download_btn_communal.setVisibility(VISIBLE);
                        download_btn_communal.hide(false);
                    }
                    if (!download_btn_communal.isVisible()) {
                        download_btn_communal.show(true);
                    }
                } else {
                    if (download_btn_communal.isVisible()) {
                        download_btn_communal.hide();
                    }
                }
            }
        });
        download_btn_communal.setOnClickListener(v -> {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) communal_recycler.getLayoutManager();
            int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
            int mVisibleCount = linearLayoutManager.findLastVisibleItemPosition()
                    - linearLayoutManager.findFirstVisibleItemPosition() + 1;
            if (firstVisibleItemPosition > mVisibleCount) {
                communal_recycler.scrollToPosition(mVisibleCount);
            }
            communal_recycler.smoothScrollToPosition(0);
        });
        cartHeaderView = LayoutInflater.from(ShopCarActivity.this).inflate(R.layout.layout_cart_recommend, null, false);
        recommendHeaderView = new RecommendHeaderView();
        ButterKnife.bind(recommendHeaderView, cartHeaderView);
        recommendHeaderView.initViews();
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
        getShopCarProInfo();
    }

    @Override
    public View getLoadView() {
        return smart_communal_refresh;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    private void getShopCarProInfo() {
        boolean allCheckedStatus = isEditStatus ? check_box_all_del.isChecked() : check_box_all_buy.isChecked();
        Map<String, Object> params = new HashMap<>();
        params.put("showCount", TOTAL_COUNT_FORTY);
        params.put("currentPage", page);
        params.put("userId", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, NEW_MINE_SHOP_CAR_GOODS,
                params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();
                        Gson gson = new Gson();
                        mShopCarNewInfoEntity = gson.fromJson(result, ShopCarEntity.class);
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
                                //失效商品
                                if (rubbishCarts != null) {
                                    carts.addAll(shopCartBean.getRubbishCarts());
                                }

                                //有效商品
                                for (int i = 0; i < carts.size(); i++) {
                                    CartBean cartBean = carts.get(i);
                                    if (cartBean == null) return;
                                    ActivityInfoBean activityInfoBean = cartBean.getActivityInfo();
                                    List<CartInfoBean> cartInfoList = cartBean.getCartInfoList();
                                    CartInfoBean combineMainProduct = cartBean.getCombineMainProduct();
                                    List<CartInfoBean> combineMatchProducts = cartBean.getCombineMatchProducts();
                                    //判断是否是组合搭配商品
                                    if (combineMainProduct != null) {
                                        if (cartInfoList == null) {
                                            cartInfoList = new ArrayList<>();
                                        }
                                        cartInfoList.clear();
                                        combineMainProduct.setMainProduct(true);//设置主商品标志
                                        cartInfoList.add(combineMainProduct);
                                        if (combineMatchProducts != null && combineMatchProducts.size() > 0) {
                                            //设置搭配商品购物车id，与主商品进行绑定
                                            for (CartInfoBean CartInfoBean : combineMatchProducts) {
                                                CartInfoBean.setId(combineMainProduct.getId());
                                                CartInfoBean.setCombineProduct(true);
                                                CartInfoBean.setCount(1);
                                            }
//                                            combineMainProduct.setCombineMatchProducts(combineMatchProducts);
                                            cartInfoList.addAll(combineMatchProducts);
                                        }
                                    }

                                    //普通商品
                                    if (cartInfoList != null && cartInfoList.size() > 0) {
                                        for (int j = 0; j < cartInfoList.size(); j++) {
                                            CartInfoBean cartInfoBean = cartInfoList.get(j);
                                            //如果有活动信息就加在活动数组第一条数据上
                                            if (activityInfoBean != null && j == 0) {
                                                cartInfoBean.setShowActInfo(1);
                                                cartInfoBean.setActivityInfoData(activityInfoBean);
                                            }

                                            //加载数据时如果是全选状态,手动选中所有有效商品
                                            if (!isEditStatus && ShopCarDao.isValid(cartInfoBean) && check_box_all_buy.isChecked()) {
                                                cartInfoBean.setSelected(true);
                                            }
                                            shopGoodsList.add(cartInfoBean);
                                        }

                                        //设置分割线
                                        if (activityInfoBean != null && shopGoodsList.size() > 0 && shopCartBean.getCarts().size() > i + 1) {
                                            CartInfoBean cartInfoBean = shopGoodsList.get(shopGoodsList.size() - 1);
                                            cartInfoBean.setShowLine(1);
                                        }
                                    }
                                }

                                if (page == 1) {
                                    updatePrice(shopCartBean, true);
                                } else {
                                    //如果加载第二页时，选中了全选
                                    if (allCheckedStatus) {
                                        getSettlePrice(null, false, true);
                                    } else {
                                        updatePrice(shopCartBean, false);
                                    }
                                }

                                shopCarGoodsAdapter.notifyDataSetChanged();
                                shopCarGoodsAdapter.loadMoreComplete();
                                //更新购物车数量
                                shopCartNum = shopCartBean.getTotalCount();
                                tv_header_titleAll.setText(shopCartNum < 1 ? "购物车" : "购物车(" + shopCartNum + ")");
                            } else {
                                shopCarGoodsAdapter.loadMoreFail();
                                getCartRecommend();
                                showToast(ShopCarActivity.this, mShopCarNewInfoEntity.getMsg());
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

    private void updatePrice(ShopCartBean shopCartBean, boolean isPage1) {
        if (shopCartBean != null) {
            //结算商品金额
            double price = shopCartBean.getTotalProductPrice();
            double discount = shopCartBean.getTotalProductDiscountPrice();
            if (!isPage1) {
                price = getStringChangeDouble(tv_cart_total.getText().toString().trim()) + price;
                discount = getStringChangeDouble(tv_settlement_dis_car_price.getText().toString().trim()) + discount;
            }
            tv_cart_total.setText(getDoubleFormat(this, R.string.group_total_price, price));
            //优惠金额
            tv_settlement_dis_car_price.setText(getDoubleFormat(this, R.string.newshopcar_discount_price, discount));
            tv_settlement_dis_car_price.setVisibility(discount != 0 ? View.VISIBLE : GONE);
            //结算商品件数
            String[] shoppingInfo = ShopCarDao.getShoppingCount(shopGoodsList);
            tv_cart_buy_orCount.setText(("去结算(" + shoppingInfo[0] + ")"));
        }
    }

    //本地计算结算金额
    private void updateLocalPrice() {
        String[] shoppingInfo = ShopCarDao.getShoppingCount(shopGoodsList);
        tv_cart_total.setText(("￥" + shoppingInfo[1]));//结算金额
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
                List<CartInfoBean> settlementGoods = ShopCarDao.getSettlementGoods(shopGoodsList);
                if (settlementGoods != null && settlementGoods.size() > 0) {
                    //结算商品 跳转订单填写
                    Intent intent = new Intent(ShopCarActivity.this, DirectIndentWriteActivity.class);
                    intent.putParcelableArrayListExtra("productDate", (ArrayList<? extends Parcelable>) settlementGoods);
                    startActivity(intent);
                } else {
                    showToast(this, "请先选择商品");
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
            ShopCarDao.selectBuyAll(shopGoodsList, isChecked);
            shopCarGoodsAdapter.notifyDataSetChanged();
            if (isChecked) {
                getSettlePrice(null, false, true);
            } else {
                tv_cart_total.setText(("￥0.00"));
                tv_settlement_dis_car_price.setVisibility(View.GONE);
                tv_cart_buy_orCount.setText(("去结算(" + 0 + ")"));
            }
        }
    }


    //删除选中商品弹窗
    private void setDeleteGoodsDialog() {
        String[] selGoodsInfo = ShopCarDao.getSelGoodsInfo(shopGoodsList);
        if (!TextUtils.isEmpty(selGoodsInfo[0])) {
            if (alertDialogHelper == null) {
                alertDialogHelper = new AlertDialogHelper(ShopCarActivity.this);
                alertDialogHelper.setTitleVisibility(View.GONE).setMsgTextGravity(Gravity.CENTER)
                        .setMsg("确定删除选中商品").setCancelText("取消").setConfirmText("确定")
                        .setCancelTextColor(getResources().getColor(R.color.text_login_gray_s));
                alertDialogHelper.setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                    @Override
                    public void confirm() {
                        //            确定删除商品
                        delSelGoods(selGoodsInfo);
                    }

                    @Override
                    public void cancel() {
                    }
                });
            }
            alertDialogHelper.show();
        } else {
            showToast(this, "请选择你要删除的商品");
        }
    }

    //删除商品
    private void delSelGoods(String[] selGoodsInfo) {
        if (loadHud == null) return;
        loadHud.show();
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("ids", selGoodsInfo[0]);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Q_SHOP_DETAILS_DEL_CAR,
                params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        loadHud.dismiss();
                        RequestStatus status = new Gson().fromJson(result, RequestStatus.class);
                        if (status != null) {
                            if (status.getCode().equals(SUCCESS_CODE)) {
                                shopCartNum = shopCartNum - getStringChangeIntegers(selGoodsInfo[1]);
                                tv_header_titleAll.setText(shopCartNum < 1 ? "购物车" : "购物车(" + shopCartNum + ")");
                                removeSelGoods(shopGoodsList, shopCarGoodsAdapter);
                                getSettlePrice(null, false, false);
                            } else if (!status.getCode().equals(EMPTY_CODE)) {
                                showToastRequestMsg(ShopCarActivity.this, status);
                            }
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
                Gson gson = new Gson();
                EditGoodsSkuEntity editGoodsSkuEmpty = gson.fromJson(result, EditGoodsSkuEntity.class);
                if (editGoodsSkuEmpty != null && editGoodsSkuEmpty.getEditGoodsSkuBean() != null) {
                    List<SkuSaleBean> skuSale = editGoodsSkuEmpty.getEditGoodsSkuBean().getSkuSale();
                    if (editGoodsSkuEmpty.getCode().equals(SUCCESS_CODE)) {
                        if (skuSale != null && skuSale.size() > 1) {
                            getProperty(editGoodsSkuEmpty.getEditGoodsSkuBean(), cartInfoBean);
                        } else {
                            textView.setSelected(false);
                        }
                    } else {
                        showToast(ShopCarActivity.this, editGoodsSkuEmpty.getMsg());
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

        if (cartInfoBean.getActivityInfoData() != null) {
            ActivityInfoBean activityInfoData = cartInfoBean.getActivityInfoData();
            params.put("activityCode", activityInfoData.getActivityCode());
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Q_SHOP_DETAILS_CHANGE_CAR, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                RequestStatus status = new Gson().fromJson(result, RequestStatus.class);
                if (status != null && status.getCode().equals(SUCCESS_CODE)) {
                    //修改完成后台会默认选中（自动添加到当天加入购物车的商品）
                    cartInfoBean.setSelected(true);
                    //修改完成更新结算金额
                    getSettlePrice(cartInfoBean, true, false);
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
                    showToastRequestMsg(ShopCarActivity.this, status);
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
     * @param isNotifyItem 是否需要通知刷新商品(单个选中或取消选中，修改数量以及sku时需要刷新)
     */
    private void getSettlePrice(CartInfoBean cartInfoBean, boolean isNotifyItem, boolean show) {
        if (shopGoodsList.size() > 0) {
            if (show) {
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
                    Gson gson = new Gson();
                    ShopCarEntity shopCarNewInfoEntity = gson.fromJson(result, ShopCarEntity.class);
                    if (shopCarNewInfoEntity != null) {
                        ShopCartBean shopCartBean = shopCarNewInfoEntity.getResult();
                        if (shopCarNewInfoEntity.getCode().equals(SUCCESS_CODE) && shopCartBean != null) {
                            //更新结算金额
                            updatePrice(shopCarNewInfoEntity.getResult(), true);
                            //刷新条目
                            if (cartInfoBean != null && isNotifyItem && matchCartId(shopCartBean, cartInfoBean)) {
                                shopCarGoodsAdapter.notifyItemChanged(cartInfoBean.getPosition());
                            }
                        } else {
                            updateLocalPrice();
                            if (cartIds.size() != 0) {
                                showToast(ShopCarActivity.this, R.string.refrence_only);
                            }
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
                likedProduct = new Gson().fromJson(result, UserLikedProductEntity.class);
                if (likedProduct != null) {
                    if (likedProduct.getCode().equals(SUCCESS_CODE)) {
                        cartProRecommendList.addAll(likedProduct.getGoodsList());
                        if (cartProRecommendList.size() > 0) {
                            if (shopCarGoodsAdapter.getFooterLayoutCount() == 0) {
                                shopCarGoodsAdapter.addFooterView(cartHeaderView);
                                recommendHeaderView.tv_pro_title.setText("-商品推荐-");
                            }
                            proNoShopCarAdapter.notifyDataSetChanged();
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
            proNoShopCarAdapter = new ProNoShopCarAdapter(ShopCarActivity.this, cartProRecommendList);
            communal_recycler_wrap.setAdapter(proNoShopCarAdapter);
            proNoShopCarAdapter.setOnItemClickListener((adapter, view, position) -> {
                LikedProductBean likedProductBean = (LikedProductBean) view.getTag();
                if (likedProductBean != null) {
                    Intent intent = new Intent();
                    switch (likedProductBean.getType_id()) {
                        case 0:
                            intent.setClass(ShopCarActivity.this, ShopTimeScrollDetailsActivity.class);
                            break;
                        case 1:
                            intent.setClass(ShopCarActivity.this, ShopScrollDetailsActivity.class);
                            break;
                        case 2:
                            intent.setClass(ShopCarActivity.this, IntegralScrollDetailsActivity.class);
                            break;
                    }
                    if (likedProduct != null && !TextUtils.isEmpty(likedProduct.getRecommendFlag())) {
                        intent.putExtra("recommendFlag", likedProduct.getRecommendFlag());
                    }
                    intent.putExtra("productId", String.valueOf(likedProductBean.getId()));
                    intent.putExtra(RECOMMEND_TYPE, RECOMMEND_CAR);
                    startActivity(intent);
                }
            });
            proNoShopCarAdapter.setEnableLoadMore(false);
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
        if (alertDialogHelper != null) {
            alertDialogHelper.dismiss();
        }
    }
}
