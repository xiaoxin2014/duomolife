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
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dominant.activity.QualityProductActActivity;
import com.amkj.dmsh.dominant.activity.ShopTimeScrollDetailsActivity;
import com.amkj.dmsh.homepage.adapter.ProNoShopCarAdapter;
import com.amkj.dmsh.mine.adapter.ShopCarGoodsAdapter;
import com.amkj.dmsh.mine.bean.ShopCarNewInfoEntity;
import com.amkj.dmsh.mine.bean.ShopCarNewInfoEntity.ShopCarNewInfoBean;
import com.amkj.dmsh.mine.bean.ShopCarNewInfoEntity.ShopCarNewInfoBean.ActivityInfoBean;
import com.amkj.dmsh.mine.bean.ShopCarNewInfoEntity.ShopCarNewInfoBean.CartInfoBean;
import com.amkj.dmsh.mine.biz.ShoppingCartBiz;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.activity.DirectIndentWriteActivity;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.shopdetails.bean.EditGoodsSkuEntity;
import com.amkj.dmsh.shopdetails.bean.ShopCarGoodsSku;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.showToastRequestMsg;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.RECOMMEND_CAR;
import static com.amkj.dmsh.constant.ConstantVariable.RECOMMEND_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_FORTY;
import static com.amkj.dmsh.constant.Url.MINE_SHOP_CAR_GOODS;
import static com.amkj.dmsh.constant.Url.MINE_SHOP_CAR_RECOMMEND_GOODS;
import static com.amkj.dmsh.constant.Url.PRO_SETTLE_PRICE;
import static com.amkj.dmsh.constant.Url.Q_SHOP_DETAILS_CHANGE_CAR;
import static com.amkj.dmsh.constant.Url.Q_SHOP_DETAILS_DEL_CAR;
import static com.amkj.dmsh.constant.Url.Q_SHOP_DETAILS_GET_SKU_CAR;
import static com.amkj.dmsh.constant.Url.SHOP_CART_RECOMMEND_EMPTY_GOODS;

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
    private int cartPage = 1;
    private boolean isEditStatus;
    private List<CartInfoBean> shopGoodsList = new ArrayList<>();
    private List<LikedProductBean> cartProRecommendList = new ArrayList<>();
    private ShopCarGoodsAdapter shopCarGoodsAdapter;
    private StringBuffer carIds;
    private boolean isOnPause;
    private RecommendHeaderView recommendHeaderView;
    private View cartHeaderView;
    private ProNoShopCarAdapter proNoShopCarAdapter;
    private UserLikedProductEntity likedProduct;
    private AlertDialogHelper alertDialogHelper;

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
                switch (view.getId()) {
                    case R.id.cb_shop_car_sel:
                        ShoppingCartBiz.selectOne(shopGoodsList, (Integer) view.getTag());
                        if (cartInfoBean.getSaleSku() != null && cartInfoBean.getStatus() == 1 && !cartInfoBean.isEditing()) {
                            getSettlePrice();
                        }
                        break;
                    case R.id.tv_shop_car_product_sku:
                        //        修改购物车商品属性
                        if (cartInfoBean.isEditing()) {
                            getGoodsSkuDetails(view);
                        } else {
                            skipProDetail(cartInfoBean);
                        }
                        break;
                    case R.id.tv_communal_activity_tag_next:
                    case R.id.tv_communal_activity_tag_rule:
                        Intent intent = new Intent(ShopCarActivity.this, QualityProductActActivity.class);
                        intent.putExtra("activityCode", cartInfoBean.getActivityInfoData().getActivityCode());
                        startActivity(intent);
                        break;
                    case R.id.img_integration_details_credits_add:
                        int oldCount = cartInfoBean.getCount();
                        int newNum = oldCount + 1;
                        if (oldCount > 0 && cartInfoBean.getStatus() == 1
                                && cartInfoBean.getSaleSku() != null
                                && cartInfoBean.getSaleSku().getQuantity() > 0) {
                            int quantity = cartInfoBean.getSaleSku().getQuantity();
                            if (newNum <= quantity) {
                                cartInfoBean.setCount(newNum);
                                addGoodsCount(cartInfoBean);
                            } else {
                                showToast(ShopCarActivity.this, R.string.product_sell_out);
                            }
                        } else {
                            if (cartInfoBean.getId() > 0) {
                                carIds = new StringBuffer(String.valueOf(cartInfoBean.getId()));
                                setDeleteGoodsDialog();
                            }
                        }
                        break;
                    case R.id.img_integration_details_credits_minus:
                        oldCount = cartInfoBean.getCount();
                        newNum = oldCount - 1;
                        if (cartInfoBean.getStatus() == 1
                                && cartInfoBean.getSaleSku() != null
                                && cartInfoBean.getSaleSku().getQuantity() > 0) {
                            if (newNum > 0) {
                                cartInfoBean.setCount(newNum);
                                addGoodsCount(cartInfoBean);
                            } else {
                                showToast(ShopCarActivity.this, R.string.product_small_count);
                            }
                        } else {
                            if (cartInfoBean.getId() > 0) {
                                carIds = new StringBuffer(String.valueOf(cartInfoBean.getId()));
                                setDeleteGoodsDialog();
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
//                特殊布局 特殊处理
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
        if (cartInfoBean != null && cartInfoBean.getStatus() == 1 && cartInfoBean.getSaleSku() != null && !cartInfoBean.isEditing()) {
            Intent intent = new Intent(ShopCarActivity.this, ShopScrollDetailsActivity.class);
            intent.putExtra("productId", String.valueOf(cartInfoBean.getProductId()));
            startActivity(intent);
        }
    }

    private void setEditStatus() {
        isEditStatus = !isEditStatus;
        header_shared.setText(isEditStatus ? "完成" : "编辑");
        header_shared.setSelected(isEditStatus);
        ShoppingCartBiz.saveEditStatus(shopGoodsList, isEditStatus);
        if (isEditStatus) {
            check_box_all_del.setChecked(false);
            ll_settlement_shop_car.setVisibility(View.GONE);
            rel_del_shop_car.setVisibility(View.VISIBLE);
        } else {
            ll_settlement_shop_car.setVisibility(View.VISIBLE);
            rel_del_shop_car.setVisibility(View.GONE);
            getSettlePrice();
        }
        shopCarGoodsAdapter.setNewData(shopGoodsList);
    }

    /**
     * 更新购物车数据
     *
     * @param cartInfoBean
     */
    private void updateCartData(CartInfoBean cartInfoBean) {
        if (cartInfoBean == null) {
            showToast(this, "数据异常，请刷新重试！");
            return;
        }
        if (loadHud != null) {
            loadHud.show();
        }
        String url = Url.BASE_URL + MINE_SHOP_CAR_GOODS;
        Map<String, Object> params = new HashMap<>();
        params.put("showCount", TOTAL_COUNT_FORTY);
        params.put("currentPage", cartInfoBean.getCurrentPage());
        params.put("userId", userId);
        params.put("version", "v3.1.5");
        NetLoadUtils.getNetInstance().loadNetDataPost(this, url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                Gson gson = new Gson();
                ShopCarNewInfoEntity shopCarNewInfoEntity = gson.fromJson(result, ShopCarNewInfoEntity.class);
                if (shopCarNewInfoEntity != null) {
                    if (shopCarNewInfoEntity.getCode().equals(SUCCESS_CODE)) {
                        if (shopCarNewInfoEntity.getShopCarNewInfoList().size() > 0 &&
                                cartInfoBean.getParentPosition() < shopCarNewInfoEntity.getShopCarNewInfoList().size()) {
                            ConstantVariable.CAR_PRO_STATUS = shopCarNewInfoEntity.getActivityTypeMap();
                            ShopCarNewInfoBean shopCarNewInfoBean = shopCarNewInfoEntity.getShopCarNewInfoList().get(cartInfoBean.getParentPosition());
                            if (shopCarNewInfoBean.getCartInfoBeanList().size() > cartInfoBean.getCurrentPosition()) {
                                CartInfoBean newCartInfoBean = shopCarNewInfoBean.getCartInfoBeanList().get(cartInfoBean.getCurrentPosition());
//                                id 是否相等
                                if (cartInfoBean.getId() == newCartInfoBean.getId()) {
                                    newCartInfoBean.setCurrentPosition(cartInfoBean.getCurrentPosition());
                                    newCartInfoBean.setCurrentPage(cartInfoBean.getCurrentPage());
                                    newCartInfoBean.setSelected(cartInfoBean.isSelected());
                                    newCartInfoBean.setActivityInfoData(cartInfoBean.getActivityInfoData());
                                    int cartPosition = shopGoodsList.indexOf(cartInfoBean);
                                    if (cartPosition == -1) {
                                        page = 1;
                                        loadData();
                                    } else {
                                        shopGoodsList.set(cartPosition, newCartInfoBean);
                                        shopCarGoodsAdapter.notifyItemChanged(cartPosition);
                                        setCartCount(shopCarNewInfoEntity.getTotalCount());
                                        if (!isEditStatus) {
                                            getSettlePrice();
                                        }
                                    }
                                } else {
                                    page = 1;
                                    loadData();
                                }
                            } else {
                                page = 1;
                                loadData();
                            }
                        } else {
                            page = 1;
                            loadData();
                        }
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
            }

            @Override
            public void onError(Throwable ex) {
                showToast(ShopCarActivity.this, R.string.do_failed);
            }

            @Override
            public void netClose() {
                showToast(ShopCarActivity.this, R.string.unConnectedNetwork);
            }
        });
    }

    private void updatePrice(ShopCarNewInfoEntity shopCarNewInfoEntity) {
        String[] shoppingInfo = ShoppingCartBiz.getShoppingCount(shopGoodsList);
//        选择商品件数
        String selectedCount = shoppingInfo[0];
//        结算价格
        tv_cart_total.setText(("¥" + getStrings(shopCarNewInfoEntity.getTotalProductPrice())));

        if (!TextUtils.isEmpty(shopCarNewInfoEntity.getTotalProductDiscountPrice())
                && (Double.parseDouble(shopCarNewInfoEntity.getTotalProductDiscountPrice()) > 0)) {
            tv_settlement_dis_car_price.setVisibility(View.VISIBLE);
            tv_settlement_dis_car_price.setText(String.format(getResources().getString(R.string.car_discount_price)
                    , getStrings(shopCarNewInfoEntity.getTotalProductDiscountPrice())));
        } else {
            tv_settlement_dis_car_price.setVisibility(View.GONE);
        }
//        结算商品件数
        tv_cart_buy_orCount.setText(("去结算(" + selectedCount + ")"));
    }

    private void updateLocalPrice() {
        String[] shoppingInfo = ShoppingCartBiz.getShoppingCount(shopGoodsList);
//        选择商品件数
        String selectedCount = shoppingInfo[0];
//        选择商品总价
        String totalPrice = shoppingInfo[1];
//        结算价格
        tv_cart_total.setText(("¥" + totalPrice));
//        结算商品件数
        tv_cart_buy_orCount.setText(("去结算(" + selectedCount + ")"));
    }

    @Override
    protected void loadData() {
        page = 1;
        cartPage = 1;
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
        params.put("version", "v3.1.5");
        NetLoadUtils.getNetInstance().loadNetDataPost(this, MINE_SHOP_CAR_GOODS,
                params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        if (loadHud != null) {
                            loadHud.dismiss();
                        }
                        smart_communal_refresh.finishRefresh();
                        shopCarGoodsAdapter.loadMoreComplete();
                        if (page == 1) {
                            shopGoodsList.clear();
                        }
                        Gson gson = new Gson();
                        ShopCarNewInfoEntity shopCarNewInfoEntity = gson.fromJson(result, ShopCarNewInfoEntity.class);
                        if (shopCarNewInfoEntity != null) {
                            if (shopCarNewInfoEntity.getCode().equals(SUCCESS_CODE)) {
                                rel_shop_car_bottom.setVisibility(View.VISIBLE);
                                for (int i = 0; i < shopCarNewInfoEntity.getShopCarNewInfoList().size(); i++) {
                                    ShopCarNewInfoBean shopCarNewInfoBean = shopCarNewInfoEntity.getShopCarNewInfoList().get(i);
                                    for (int j = 0; j < shopCarNewInfoBean.getCartInfoBeanList().size(); j++) {
                                        CartInfoBean cartInfoBean = shopCarNewInfoBean.getCartInfoBeanList().get(j);
                                        cartInfoBean.setEditing(isEditStatus);
                                        cartInfoBean.setCurrentPosition(j);
                                        cartInfoBean.setParentPosition(i);
                                        cartInfoBean.setCurrentPage(page);
                                        if (shopCarNewInfoBean.getActivityInfoBean() != null) {
                                            ActivityInfoBean activityInfoBean = shopCarNewInfoBean.getActivityInfoBean();
                                            if (j == 0) {
//                                            是否显示活动消息
                                                ActivityInfoBean activityInfo = new ActivityInfoBean();
                                                activityInfo.setActivityCode(activityInfoBean.getActivityCode());
                                                activityInfo.setShowActInfo(1);
                                                activityInfo.setActivityRule(activityInfoBean.getActivityRule());
                                                activityInfo.setLimitBuy(activityInfoBean.getLimitBuy());
                                                activityInfo.setActivityTag(activityInfoBean.getActivityTag());
                                                activityInfo.setActivityType(activityInfoBean.getActivityType());
                                                cartInfoBean.setActivityInfoData(activityInfo);
                                            } else {
                                                activityInfoBean.setShowActInfo(0);
                                                cartInfoBean.setActivityInfoData(activityInfoBean);
                                            }
                                        }
                                        if (!isEditStatus && cartInfoBean.getStatus() == 1 && cartInfoBean.getSaleSku() != null
                                                && cartInfoBean.getSaleSku().getQuantity() > 0 && !cartInfoBean.isForSale() && check_box_all_buy.isChecked()) {
                                            cartInfoBean.setSelected(true);
                                        }
                                        if (shopGoodsList.size() > 0) {
                                            cartInfoBean.setCurrentPosition(shopGoodsList.size());
                                        }
                                        shopGoodsList.add(cartInfoBean);
                                    }
                                    if (shopCarNewInfoBean.getActivityInfoBean() != null && shopGoodsList.size() > 0
                                            && shopCarNewInfoEntity.getShopCarNewInfoList().size() > i + 1) {
                                        CartInfoBean cartInfoBean = shopGoodsList.get(shopGoodsList.size() - 1);
                                        cartInfoBean.setShowLine(1);
                                    }
                                }
                                ConstantVariable.CAR_PRO_STATUS = shopCarNewInfoEntity.getActivityTypeMap();
                            } else if (!shopCarNewInfoEntity.getCode().equals(EMPTY_CODE)) {
                                showToast(ShopCarActivity.this, shopCarNewInfoEntity.getMsg());
                            } else {
                                shopCarGoodsAdapter.loadMoreEnd(true);
                            }
                            if (shopGoodsList.size() < 1) {
                                rel_shop_car_bottom.setVisibility(View.GONE);
                            } else {
                                rel_shop_car_bottom.setVisibility(View.VISIBLE);
                            }
                            if (!isEditStatus) {
                                if (page == 1) {
                                    updatePrice(shopCarNewInfoEntity);
                                } else if (allCheckedStatus) {
                                    getSettlePrice();
                                }
                            }
                            shopCarGoodsAdapter.notifyDataSetChanged();
                            setCartCount(shopCarNewInfoEntity.getTotalCount());
                        }
                        if (shopGoodsList.size() < 1) {
                            shopCarGoodsAdapter.setEmptyView(R.layout.adapter_car_pro_empty, communal_recycler);
                            shopCarGoodsAdapter.setHeaderFooterEmpty(false, true);
                            getCartRecommendEmpty();
                        } else if (shopGoodsList.size() < page * TOTAL_COUNT_FORTY) {
                            shopCarGoodsAdapter.loadMoreEnd(true);
                            getCartRecommend();
                        }
                        NetLoadUtils.getNetInstance().showLoadSirSuccess(loadService);
                    }

                    @Override
                    public void onNotNetOrException() {
                        smart_communal_refresh.finishRefresh();
                        shopCarGoodsAdapter.loadMoreEnd(true);
                        rel_shop_car_bottom.setVisibility(View.GONE);
                        NetLoadUtils.getNetInstance().showLoadSirSuccess(loadService);
                    }

                    @Override
                    public void netClose() {
                        showToast(ShopCarActivity.this, R.string.unConnectedNetwork);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        showToast(ShopCarActivity.this, R.string.invalidData);
                    }
                });
    }

    /**
     * 购物车无商品推荐
     */
    private void getCartRecommendEmpty() {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, SHOP_CART_RECOMMEND_EMPTY_GOODS, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                cartProRecommendList.clear();
                Gson gson = new Gson();
                likedProduct = gson.fromJson(result, UserLikedProductEntity.class);
                if (likedProduct != null) {
                    if (likedProduct.getCode().equals(SUCCESS_CODE)) {
                        cartProRecommendList.addAll(likedProduct.getGoodsList());
                        if (cartProRecommendList.size() > 0) {
                            if (cartHeaderView.getParent() == null) {
                                shopCarGoodsAdapter.addFooterView(cartHeaderView);
                            }
                            recommendHeaderView.tv_pro_title.setText("-商品推荐-");
                            proNoShopCarAdapter.notifyDataSetChanged();
                        }
                    } else if (!likedProduct.getCode().equals(EMPTY_CODE)) {
                        showToast(ShopCarActivity.this, likedProduct.getMsg());
                    }
                }
            }
        });
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
                proNoShopCarAdapter.loadMoreComplete();
                if (cartPage == 1) {
                    cartProRecommendList.clear();
                    shopCarGoodsAdapter.notifyDataSetChanged();
                }
                Gson gson = new Gson();
                likedProduct = gson.fromJson(result, UserLikedProductEntity.class);
                if (likedProduct != null) {
                    if (likedProduct.getCode().equals(SUCCESS_CODE)) {
                        cartProRecommendList.addAll(likedProduct.getGoodsList());
                        if (cartProRecommendList.size() > 0) {
                            shopCarGoodsAdapter.addFooterView(cartHeaderView);
                            recommendHeaderView.tv_pro_title.setText("-商品推荐-");
                            proNoShopCarAdapter.notifyDataSetChanged();
                        }
                    } else if (!likedProduct.getCode().equals(EMPTY_CODE)) {
                        showToast(ShopCarActivity.this, likedProduct.getMsg());
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                proNoShopCarAdapter.loadMoreEnd(true);
            }
        });
    }

    private void setCartCount(int totalCount) {
        tv_header_titleAll.setText(totalCount < 1
                ? "购物车" : "购物车(" + totalCount + ")");
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            if (requestCode == ConstantVariable.IS_LOGIN_CODE) {
                finish();
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //  结算商品
    @OnClick(R.id.tv_communal_buy_or_count)
    void goBuyGoods(View view) {
        List<CartInfoBean> settlementGoods = ShoppingCartBiz.getSettlementGoods(shopGoodsList);
        if (settlementGoods != null && settlementGoods.size() > 0) {
//            结算商品 跳转订单填写
            Intent intent = new Intent(ShopCarActivity.this, DirectIndentWriteActivity.class);
            intent.putParcelableArrayListExtra("productDate", (ArrayList<? extends Parcelable>) settlementGoods);
            startActivity(intent);
        } else {
            showToast(this, "请先选择商品");
        }
    }

    //    全选删除
    @OnCheckedChanged(R.id.check_box_all_del)
    void allCheckDel(CompoundButton buttonView, boolean isChecked) {
        if (smart_communal_refresh.getState() == RefreshState.None) {
            ShoppingCartBiz.selectDeleteAll(shopGoodsList, isChecked);
            shopCarGoodsAdapter.setNewData(shopGoodsList);
        }

    }

    //    全选结算
    @OnCheckedChanged(R.id.check_box_all_buy)
    void allCheckBuy(CompoundButton buttonView, boolean isChecked) {
        if (!isEditStatus && smart_communal_refresh.getState() == RefreshState.None) {
            ShoppingCartBiz.selectBuyAll(shopGoodsList, isChecked);
            shopCarGoodsAdapter.setNewData(shopGoodsList);
            getSettlePrice();
        }
    }

    //编辑
    @OnClick(R.id.tv_header_shared)
    void changeMode(View view) {
        setEditStatus();
    }

    //    删除
    @OnClick(R.id.tv_shop_car_del)
    void delGoods() {
        carIds = ShoppingCartBiz.delSelGoods(shopGoodsList);
        setDeleteGoodsDialog();
    }

    /**
     * 删除选中商品
     */
    private void setDeleteGoodsDialog() {
        if (!TextUtils.isEmpty(carIds)) {
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
            showToast(this, "请选择你要删除的商品");
        }
    }

    private void delSelGoods() {
        if (loadHud != null) {
            loadHud.show();
        }
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("ids", carIds);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Q_SHOP_DETAILS_DEL_CAR,
                params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        if (loadHud != null) {
                            loadHud.dismiss();
                        }
                        Gson gson = new Gson();
                        RequestStatus status = gson.fromJson(result, RequestStatus.class);
                        if (status != null) {
                            if (status.getCode().equals(SUCCESS_CODE)) {
                                loadData();
                            } else if (!status.getCode().equals(EMPTY_CODE)) {
                                showToastRequestMsg(ShopCarActivity.this, status);
                            }
                        }
                    }

                    @Override
                    public void onNotNetOrException() {
                        if (loadHud != null) {
                            loadHud.dismiss();
                        }
                    }

                    @Override
                    public void netClose() {
                        showToast(ShopCarActivity.this, R.string.unConnectedNetwork);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        showToast(ShopCarActivity.this, R.string.do_failed);
                    }
                });
    }

    private void getGoodsSkuDetails(final View view) {
        final TextView textView = (TextView) view;
        final CartInfoBean cartInfoBean = (CartInfoBean) view.getTag();
        if (cartInfoBean != null && cartInfoBean.getSaleSku() != null) {
            //商品详情内容
            Map<String, Object> params = new HashMap<>();
            params.put("productId", cartInfoBean.getProductId());
            NetLoadUtils.getNetInstance().loadNetDataPost(this, Q_SHOP_DETAILS_GET_SKU_CAR, params, new NetLoadListenerHelper() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    EditGoodsSkuEntity editGoodsSkuEmpty = gson.fromJson(result, EditGoodsSkuEntity.class);
                    if (editGoodsSkuEmpty != null) {
                        if (editGoodsSkuEmpty.getCode().equals(SUCCESS_CODE)) {
                            if (editGoodsSkuEmpty.getEditGoodsSkuBean().getSkuSale().size() > 1) {
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
        } else {
            textView.setSelected(false);
        }
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
                changeGoodsSku(shopProperty.getId(), shopCarGoodsSku, cartInfoBean);
            }
        });
    }

    private void changeGoodsSku(int productId, ShopCarGoodsSku shopCarGoodsSku, final CartInfoBean cartInfoBean) {
        //商品属性修改
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("count", shopCarGoodsSku.getCount() < 1 ? cartInfoBean.getCount() : shopCarGoodsSku.getCount());
        params.put("productId", productId);
        params.put("saleSkuId", shopCarGoodsSku.getSaleSkuId());
        params.put("price", shopCarGoodsSku.getPrice());
        params.put("id", cartInfoBean.getId());
        if (cartInfoBean.getActivityInfoData() != null) {
            ActivityInfoBean activityInfoData = cartInfoBean.getActivityInfoData();
            params.put("activityCode", activityInfoData.getActivityCode());
            try {
                JSONObject jsonObject = new JSONObject();
                JSONArray jsonArray = new JSONArray();
                jsonObject.put("productId", cartInfoBean.getProductId());
                jsonObject.put("saleSkuId", cartInfoBean.getSaleSku().getId());
                jsonObject.put("price", cartInfoBean.getSaleSku().getPrice());
                jsonObject.put("count", shopCarGoodsSku.getCount() == 1 ? cartInfoBean.getCount() : shopCarGoodsSku.getCount());
                jsonArray.put(jsonObject);
                params.put("activityProducts", jsonArray.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Q_SHOP_DETAILS_CHANGE_CAR, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                RequestStatus status = gson.fromJson(result, RequestStatus.class);
                if (status != null) {
                    if (status.getCode().equals(SUCCESS_CODE)) {
                        updateCartData(cartInfoBean);
                    } else {
                        showToastRequestMsg(ShopCarActivity.this, status);
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(ShopCarActivity.this, R.string.do_failed);
            }

            @Override
            public void netClose() {
                showToast(ShopCarActivity.this, R.string.unConnectedNetwork);
            }
        });
    }

    /**
     * 获取结算金额
     */
    private void getSettlePrice() {
        if (shopGoodsList.size() > 0) {
            if (isEditStatus) {
                return;
            }
            loadHud.show();
            Map<String, Object> params = new HashMap<>();
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject;
            for (int i = 0; i < shopGoodsList.size(); i++) {
                CartInfoBean cartInfoBean = shopGoodsList.get(i);
                if (cartInfoBean.getSaleSku() != null) {
                    try {
                        jsonObject = new JSONObject();
                        jsonObject.put("productId", cartInfoBean.getProductId());
                        jsonObject.put("productSkuId", cartInfoBean.getSaleSku().getId());
                        jsonObject.put("price", cartInfoBean.getSaleSku().getPrice());
                        jsonObject.put("count", cartInfoBean.getCount());
                        jsonObject.put("isCheck", cartInfoBean.isSelected());
                        jsonArray.put(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            params.put("cartProductInfo", jsonArray.toString().trim());
            params.put("userId", userId);
            params.put("version", "v3.1.5");
            NetLoadUtils.getNetInstance().loadNetDataPost(this, PRO_SETTLE_PRICE, params, new NetLoadListenerHelper() {
                @Override
                public void onSuccess(String result) {
                    loadHud.dismiss();
                    Gson gson = new Gson();
                    ShopCarNewInfoEntity shopCarNewInfoEntity = gson.fromJson(result, ShopCarNewInfoEntity.class);
                    if (shopCarNewInfoEntity != null) {
                        if (shopCarNewInfoEntity.getCode().equals(SUCCESS_CODE)) {
                            updatePrice(shopCarNewInfoEntity);
                        } else {
                            updateLocalPrice();
                            showToast(ShopCarActivity.this, R.string.refrence_only);
                        }
                    }
                }

                @Override
                public void onNotNetOrException() {
                    loadHud.dismiss();
                }

                @Override
                public void onError(Throwable throwable) {
                    updateLocalPrice();
                    showToast(ShopCarActivity.this, R.string.refrence_only);
                }

                @Override
                public void netClose() {
                    showToast(ShopCarActivity.this, R.string.unConnectedNetwork);
                }
            });
        }
    }

    /**
     * 添加购物车
     *
     * @param cartInfoBean
     */
    private void addGoodsCount(CartInfoBean cartInfoBean) {
        if (loadHud != null) {
            loadHud.show();
        }
        //商品数量修改
        String url = Url.BASE_URL + Url.Q_SHOP_DETAILS_CHANGE_CAR;
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("count", cartInfoBean.getCount());
        params.put("productId", cartInfoBean.getProductId());
        params.put("saleSkuId", cartInfoBean.getSaleSku().getId());
        params.put("price", cartInfoBean.getSaleSku().getPrice());
        params.put("id", cartInfoBean.getId());
        if (cartInfoBean.getActivityInfoData() != null) {
            ActivityInfoBean activityInfoData = cartInfoBean.getActivityInfoData();
            params.put("activityCode", activityInfoData.getActivityCode());
            try {
                JSONObject jsonObject = new JSONObject();
                JSONArray jsonArray = new JSONArray();
                jsonObject.put("productId", cartInfoBean.getProductId());
                jsonObject.put("saleSkuId", cartInfoBean.getSaleSku().getId());
                jsonObject.put("price", cartInfoBean.getSaleSku().getPrice());
                jsonObject.put("count", cartInfoBean.getCount());
                jsonArray.put(jsonObject);
                params.put("activityProducts", jsonArray.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(this, url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                Gson gson = new Gson();
                RequestStatus status = gson.fromJson(result, RequestStatus.class);
                if (status != null) {
                    if (status.getCode().equals(SUCCESS_CODE)) {
                        updateCartData(cartInfoBean);
                    } else {
                        showToastRequestMsg(ShopCarActivity.this, status);
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
            }

            @Override
            public void onError(Throwable ex) {
                showToast(ShopCarActivity.this, R.string.do_failed);
            }

            @Override
            public void netClose() {
                showToast(ShopCarActivity.this, R.string.unConnectedNetwork);
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
    protected void onDestroy() {
        super.onDestroy();
        if (alertDialogHelper != null) {
            alertDialogHelper.dismiss();
        }
    }
}
