package com.amkj.dmsh.mine.activity;

import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.BaseApplication;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.dominant.activity.QualityProductActActivity;
import com.amkj.dmsh.dominant.activity.ShopTimeScrollDetailsActivity;
import com.amkj.dmsh.homepage.adapter.ProNoShopCarAdapter;
import com.amkj.dmsh.mine.adapter.ShopCarGoodsAdapter;
import com.amkj.dmsh.mine.bean.EvenBusTransmitObject;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.mine.bean.ShopCarNewInfoEntity;
import com.amkj.dmsh.mine.bean.ShopCarNewInfoEntity.ShopCarNewInfoBean;
import com.amkj.dmsh.mine.bean.ShopCarNewInfoEntity.ShopCarNewInfoBean.ActivityInfoBean;
import com.amkj.dmsh.mine.bean.ShopCarNewInfoEntity.ShopCarNewInfoBean.CartInfoBean;
import com.amkj.dmsh.mine.biz.ShoppingCartBiz;
import com.amkj.dmsh.release.dialogutils.AlertSettingBean;
import com.amkj.dmsh.release.dialogutils.AlertView;
import com.amkj.dmsh.release.dialogutils.OnAlertItemClickListener;
import com.amkj.dmsh.shopdetails.activity.DirectIndentWriteActivity;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.shopdetails.bean.EditGoodsSkuEntity;
import com.amkj.dmsh.shopdetails.bean.ShopCarGoodsSku;
import com.amkj.dmsh.shopdetails.integration.IntegralScrollDetailsActivity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.amkj.dmsh.views.bottomdialog.SkuDialog;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

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

import static com.amkj.dmsh.R.id.ll_communal_settlement;
import static com.amkj.dmsh.constant.ConstantMethod.getPersonalInfo;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.RECOMMEND_CAR;
import static com.amkj.dmsh.constant.ConstantVariable.RECOMMEND_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_FORTY;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TWENTY;

;

/**
 * Created by atd48 on 2016/10/22.
 */
public class ShopCarActivity extends BaseActivity implements OnAlertItemClickListener {
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tv_header_shared)
    TextView header_shared;
    //    结算页面
    @BindView(ll_communal_settlement)
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
    RelativeLayout ll_del_shop_car;
    // 全选 全不选
    @BindView(R.id.check_box_all_del)
    CheckBox check_box_all_del;

    @BindView(R.id.smart_communal_refresh)
    RefreshLayout smart_communal_refresh;

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
    @BindView(R.id.communal_load)
    View communal_load;
    @BindView(R.id.communal_error)
    View communal_error;
    @BindView(R.id.communal_empty)
    View communal_empty;
    private int page = 1;
    private int cartPage = 1;
    private boolean isEditStatus;
    private int uid;
    private List<CartInfoBean> shopGoodsList = new ArrayList<>();
    private List<LikedProductBean> cartProRecommendList = new ArrayList<>();
    private ShopCarGoodsAdapter shopCarGoodsAdapter;
    private AlertView dlDelGoods;
    private StringBuffer carIds;
    private boolean isOnPause;
    private int scrollY = 0;
    private float screenHeight;
    private RecommendHeaderView recommendHeaderView;
    private View cartHeaderView;
    private ProNoShopCarAdapter proNoShopCarAdapter;
    private UserLikedProductEntity likedProduct;

    @Override
    protected int getContentView() {
        return R.layout.activity_shop_car;
    }

    @Override
    protected void initViews() {
        getLoginStatus();
        tl_normal_bar.setSelected(true);
        tv_header_titleAll.setText("购物车");
        header_shared.setCompoundDrawables(null, null, null, null);
        header_shared.setText("编辑");
        shopCarGoodsAdapter = new ShopCarGoodsAdapter(ShopCarActivity.this, shopGoodsList);
        communal_recycler.setLayoutManager(new LinearLayoutManager(this));
        communal_recycler.addItemDecoration(new PinnedHeaderItemDecoration.Builder(-1)
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_gray_f_two_px)
                // 开启绘制分隔线，默认关闭
                .enableDivider(true)
                // 是否关闭标签点击事件，默认开启
                .disableHeaderClick(false)
                // 设置标签和其内部的子控件的监听，若设置点击监听不为null，但是disableHeaderClick(true)的话，还是不会响应点击事件
                .setHeaderClickListener(null)
                .create());
        communal_recycler.setAdapter(shopCarGoodsAdapter);

        smart_communal_refresh.setOnRefreshListener((refreshLayout) -> {
            scrollY = 0;
            page = 1;
            cartPage = 1;
            loadData();
        });
        shopCarGoodsAdapter.setOnLoadMoreListener(() -> {
            if (page * TOTAL_COUNT_TWENTY <= shopGoodsList.size()) {
                page++;
                getShopCarProInfo();
            }else{
                shopCarGoodsAdapter.setEnableLoadMore(false);
            }
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
                }
            }
        });
        download_btn_communal.attachToRecyclerView(communal_recycler, null, new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!recyclerView.canScrollVertically(-1)) {
                    scrollY = 0;
                }
                scrollY += dy;
                if (screenHeight == 0) {
                    BaseApplication app = (BaseApplication) getApplication();
                    screenHeight = app.getScreenHeight();
                }
                if (scrollY >= screenHeight) {
                    download_btn_communal.setVisibility(View.VISIBLE);
                } else {
                    download_btn_communal.setVisibility(View.GONE);
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
        communal_load.setVisibility(View.VISIBLE);
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
        if (isEditStatus) {
            check_box_all_del.setChecked(false);
            ll_settlement_shop_car.setVisibility(View.GONE);
            ll_del_shop_car.setVisibility(View.VISIBLE);
            header_shared.setText("完成");
            header_shared.setSelected(isEditStatus);
            ShoppingCartBiz.isEditStatus(shopGoodsList, isEditStatus);
        } else {
            if (!check_box_all_buy.isChecked()) {
                check_box_all_buy.setChecked(true);
            }
            ll_settlement_shop_car.setVisibility(View.VISIBLE);
            ll_del_shop_car.setVisibility(View.GONE);
            header_shared.setSelected(isEditStatus);
            ShoppingCartBiz.isEditStatus(shopGoodsList, isEditStatus);
            header_shared.setText("编辑");
            getSettlePrice();
        }
        shopCarGoodsAdapter.setNewData(shopGoodsList);
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (message.type.equals("updateData")) {
            EvenBusTransmitObject transmitObject = (EvenBusTransmitObject) message.result;
//            更新购物车数据
            updateCartData(transmitObject.getPosition());
        } else if (message.type.equals("delProduct")) {
            CartInfoBean cartInfoBean = (CartInfoBean) message.result;
            if (!TextUtils.isEmpty(cartInfoBean.getId() + "")) {
                carIds = new StringBuffer(String.valueOf(cartInfoBean.getId()));
                AlertSettingBean alertSettingBean = new AlertSettingBean();
                AlertSettingBean.AlertData alertData = new AlertSettingBean.AlertData();
                alertData.setCancelStr("取消");
                alertData.setDetermineStr("确定");
                alertData.setFirstDet(true);
                alertData.setMsg("确定删除选中商品");
                alertSettingBean.setStyle(AlertView.Style.Alert);
                alertSettingBean.setAlertData(alertData);
                dlDelGoods = new AlertView(alertSettingBean, ShopCarActivity.this, ShopCarActivity.this);
                dlDelGoods.setCancelable(true);
                dlDelGoods.show();
            }
        }
    }

    /**
     * 更新购物车数据
     *
     * @param position
     */
    private void updateCartData(final int position) {
        if (shopGoodsList.size() > position) {
            final CartInfoBean oldCartInfoBean = shopGoodsList.get(position);
            String url = Url.BASE_URL + Url.MINE_SHOP_CAR_GOODS;
            Map<String, Object> params = new HashMap<>();
            params.put("showCount", TOTAL_COUNT_FORTY);
            params.put("currentPage", oldCartInfoBean.getCurrentPage());
            params.put("userId", uid);
            params.put("version","v3.1.5");
            if (NetWorkUtils.checkNet(this)) {
                XUtil.Post(url, params, new MyCallBack<String>() {
                    @Override
                    public void onSuccess(String result) {
                        if (loadHud != null) {
                            loadHud.dismiss();
                        }
                        Gson gson = new Gson();
                        ShopCarNewInfoEntity shopCarNewInfoEntity = gson.fromJson(result, ShopCarNewInfoEntity.class);
                        if (shopCarNewInfoEntity != null) {
                            if (shopCarNewInfoEntity.getCode().equals("01")) {
                                if (shopCarNewInfoEntity.getShopCarNewInfoList().size() > 0) {
                                    ConstantVariable.CAR_PRO_STATUS = shopCarNewInfoEntity.getActivityTypeMap();
                                    ShopCarNewInfoBean shopCarNewInfoBean = shopCarNewInfoEntity.getShopCarNewInfoList().get(oldCartInfoBean.getOldPosition());
                                    CartInfoBean newCartInfoBean = shopCarNewInfoBean.getCartInfoBeanList().get(position);
//                                id 是否相等
                                    if (oldCartInfoBean.getId() == newCartInfoBean.getId()) {
                                        newCartInfoBean.setCurrentPosition(oldCartInfoBean.getCurrentPosition());
                                        newCartInfoBean.setOldPosition(oldCartInfoBean.getOldPosition());
                                        newCartInfoBean.setCurrentPage(oldCartInfoBean.getCurrentPage());
                                        newCartInfoBean.setSelected(oldCartInfoBean.isSelected());
                                        shopGoodsList.set(position, newCartInfoBean);
                                        shopCarGoodsAdapter.notifyItemChanged(position);
                                        setCartCount();
                                        getSettlePrice();
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
                    public void onError(Throwable ex, boolean isOnCallback) {
                        showToast(ShopCarActivity.this, R.string.invalidData);
                        super.onError(ex, isOnCallback);
                    }
                });
            } else {
                showToast(ShopCarActivity.this, R.string.unConnectedNetwork);
            }
        }
    }

    private void updatePrice(ShopCarNewInfoEntity shopCarNewInfoEntity) {
        String[] shoppingInfo = ShoppingCartBiz.getShoppingCount(shopGoodsList);
//        选择商品件数
        String selectedCount = shoppingInfo[0];
//        结算价格
        tv_cart_total.setText(("￥" + getStrings(shopCarNewInfoEntity.getTotalProductPrice())));

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
        tv_cart_total.setText(("￥" + totalPrice));
//        结算商品件数
        tv_cart_buy_orCount.setText(("去结算(" + selectedCount + ")"));
    }

    @Override
    protected void loadData() {
        getShopCarProInfo();
    }

    private void getShopCarProInfo() {
        String url = Url.BASE_URL + Url.MINE_SHOP_CAR_GOODS;
        Map<String, Object> params = new HashMap<>();
        params.put("showCount", TOTAL_COUNT_FORTY);
        params.put("currentPage", page);
        params.put("userId", uid);
        params.put("version","v3.1.5");
        if (NetWorkUtils.checkNet(this)) {
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    if (loadHud != null) {
                        loadHud.dismiss();
                    }
                    if (page == 1) {
                        shopGoodsList.clear();
                    }
                    smart_communal_refresh.finishRefresh();
                    shopCarGoodsAdapter.loadMoreComplete();
                    communal_load.setVisibility(View.GONE);
                    communal_error.setVisibility(View.GONE);
                    communal_empty.setVisibility(View.GONE);
                    Gson gson = new Gson();
                    ShopCarNewInfoEntity shopCarNewInfoEntity = gson.fromJson(result, ShopCarNewInfoEntity.class);
                    if (shopCarNewInfoEntity != null) {
                        if (shopCarNewInfoEntity.getCode().equals("01")) {
                            rel_shop_car_bottom.setVisibility(View.VISIBLE);
                            for (int i = 0; i < shopCarNewInfoEntity.getShopCarNewInfoList().size(); i++) {
                                ShopCarNewInfoBean shopCarNewInfoBean = shopCarNewInfoEntity.getShopCarNewInfoList().get(i);
                                for (int j = 0; j < shopCarNewInfoBean.getCartInfoBeanList().size(); j++) {
                                    CartInfoBean cartInfoBean = shopCarNewInfoBean.getCartInfoBeanList().get(j);
                                    cartInfoBean.setOldPosition(i);
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
                                    if (cartInfoBean.getStatus() == 1 && cartInfoBean.getSaleSku() != null
                                            && cartInfoBean.getSaleSku().getQuantity() > 0) {
                                        cartInfoBean.setSelected(true);
                                    } else {
                                        cartInfoBean.setSelected(false);
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
                            updatePrice(shopCarNewInfoEntity);
                            isEditStatus = true;
                            setEditStatus();
                        } else if (!shopCarNewInfoEntity.getCode().equals("02")) {
                            showToast(ShopCarActivity.this, shopCarNewInfoEntity.getMsg());
                        }
                        if (shopGoodsList.size() < 1) {
                            rel_shop_car_bottom.setVisibility(View.GONE);
                        } else {
                            rel_shop_car_bottom.setVisibility(View.VISIBLE);
                        }
                        if (page == 1) {
                            shopCarGoodsAdapter.setNewData(shopGoodsList);
                        } else {
                            shopCarGoodsAdapter.notifyDataSetChanged();
                        }
                        setCartCount();
                    }
                    if (shopGoodsList.size() < 1) {
                        shopCarGoodsAdapter.setEmptyView(R.layout.adapter_car_pro_empty, communal_recycler);
                        shopCarGoodsAdapter.setHeaderFooterEmpty(false, true);
                        getCartRecommendEmpty();
                    } else if (shopGoodsList.size() < page * TOTAL_COUNT_FORTY) {
                        getCartRecommend();
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    smart_communal_refresh.finishRefresh();
                    shopCarGoodsAdapter.loadMoreComplete();
                    communal_load.setVisibility(View.GONE);
                    communal_empty.setVisibility(View.GONE);
                    communal_error.setVisibility(View.VISIBLE);
                    rel_shop_car_bottom.setVisibility(View.GONE);
                    showToast(ShopCarActivity.this, R.string.invalidData);
                    super.onError(ex, isOnCallback);
                }
            });
        } else {
            communal_load.setVisibility(View.GONE);
            communal_empty.setVisibility(View.GONE);
            communal_error.setVisibility(View.VISIBLE);
            rel_shop_car_bottom.setVisibility(View.GONE);
            showToast(ShopCarActivity.this, R.string.unConnectedNetwork);
        }
    }

    /**
     * 购物车无商品推荐
     */
    private void getCartRecommendEmpty() {
        if (NetWorkUtils.checkNet(ShopCarActivity.this)) {
            String url = Url.BASE_URL + Url.SHOP_CART_RECOMMEND_EMPTY_GOODS;
            Map<String, Object> params = new HashMap<>();
            params.put("uid", uid);
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    cartProRecommendList.clear();
                    Gson gson = new Gson();
                    likedProduct = gson.fromJson(result, UserLikedProductEntity.class);
                    if (likedProduct != null) {
                        if (likedProduct.getCode().equals("01")) {
                            cartProRecommendList.addAll(likedProduct.getLikedProductBeanList());
                            if (cartProRecommendList.size() > 0) {
                                if(cartHeaderView.getParent()==null){
                                    shopCarGoodsAdapter.addFooterView(cartHeaderView);
                                }
                                recommendHeaderView.tv_pro_title.setText("-商品推荐-");
                                proNoShopCarAdapter.notifyDataSetChanged();
                            }
                        } else if (!likedProduct.getCode().equals("02")) {
                            showToast(ShopCarActivity.this, likedProduct.getMsg());
                        }
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    super.onError(ex, isOnCallback);
                }
            });
        }
    }

    /**
     * 购物车有商品推荐
     */
    private void getCartRecommend() {
        if (NetWorkUtils.checkNet(ShopCarActivity.this)) {
            String url = Url.BASE_URL + Url.MINE_SHOP_CAR_RECOMMEND_GOODS;
            Map<String, Object> params = new HashMap<>();
            params.put("uid", uid);
            XUtil.Post(url, params, new MyCallBack<String>() {
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
                        if (likedProduct.getCode().equals("01")) {
                            cartProRecommendList.addAll(likedProduct.getLikedProductBeanList());
                            if (cartProRecommendList.size() > 0) {
                                shopCarGoodsAdapter.addFooterView(cartHeaderView);
                                recommendHeaderView.tv_pro_title.setText("-商品推荐-");
                                proNoShopCarAdapter.notifyDataSetChanged();
                            }
                        } else if (!likedProduct.getCode().equals("02")) {
                            showToast(ShopCarActivity.this, likedProduct.getMsg());
                        }
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    proNoShopCarAdapter.loadMoreComplete();
                    super.onError(ex, isOnCallback);
                }
            });
        }
    }

    private void setCartCount() {
        int cartCount = ShoppingCartBiz.getCartCount(shopGoodsList);
        tv_header_titleAll.setText(cartCount < 1
                ? "购物车" : "购物车(" + cartCount + ")");
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
            page = 1;
            loadData();
        }
        isOnPause = false;
    }

    private void getLoginStatus() {
        SavePersonalInfoBean personalInfo = getPersonalInfo(this);
        if (personalInfo.isLogin()) {
            uid = personalInfo.getUid();
        } else {
            //未登录跳转登录页
            Intent intent = new Intent(this, MineLoginActivity.class);
            startActivityForResult(intent, ConstantVariable.IS_LOGIN_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            if (requestCode == ConstantVariable.IS_LOGIN_CODE) {
                finish();
            } else {
                return;
            }
        } else if (requestCode == ConstantVariable.IS_LOGIN_CODE) {
            getLoginStatus();
        }
    }

    //  结算商品
    @OnClick(R.id.tv_communal_buy_or_count)
    void goBuyGoods(View view) {
        List<CartInfoBean> settlementGoods = ShoppingCartBiz.getSettlementGoods(shopGoodsList);
        if (settlementGoods != null && settlementGoods.size() > 0) {
//            结算商品 跳转订单填写
            Intent intent = new Intent(ShopCarActivity.this, DirectIndentWriteActivity.class);
            intent.putExtra("uid", uid);
            intent.putParcelableArrayListExtra("productDate", (ArrayList<? extends Parcelable>) settlementGoods);
            startActivity(intent);
        } else {
            showToast(this, "请先选择商品");
        }
    }

    //    全选 /全不选
    @OnCheckedChanged(R.id.check_box_all_del)
    void allCheckDel(CompoundButton buttonView, boolean isChecked) {
        ShoppingCartBiz.selectAll(shopGoodsList, isChecked);
        shopCarGoodsAdapter.setNewData(shopGoodsList);
    }

    //    全选 /全不选
    @OnCheckedChanged(R.id.check_box_all_buy)
    void allCheckBuy(CompoundButton buttonView, boolean isChecked) {
        if (!isEditStatus) {
            ShoppingCartBiz.isNorMalAll(shopGoodsList, isChecked);
            shopCarGoodsAdapter.setNewData(shopGoodsList);
            getSettlePrice();
        }
    }

    @OnClick(R.id.tv_header_shared)
    void changeMode(View view) {
        setEditStatus();
    }

    //    删除
    @OnClick(R.id.tv_shop_car_del)
    void delGoods(View view) {
        carIds = ShoppingCartBiz.delSelGoods(shopGoodsList);
        if (!TextUtils.isEmpty(carIds)) {
            AlertSettingBean alertSettingBean = new AlertSettingBean();
            AlertSettingBean.AlertData alertData = new AlertSettingBean.AlertData();
            alertData.setCancelStr("取消");
            alertData.setDetermineStr("确定");
            alertData.setFirstDet(true);
            alertData.setMsg("确定删除选中商品");
            alertSettingBean.setStyle(AlertView.Style.Alert);
            alertSettingBean.setAlertData(alertData);
            dlDelGoods = new AlertView(alertSettingBean, ShopCarActivity.this, ShopCarActivity.this);
            dlDelGoods.setCancelable(true);
            dlDelGoods.show();
        } else {
            showToast(this, "请选择你要删除的商品");
        }
    }

    @Override
    public void onAlertItemClick(Object o, int position) {
        if (o == dlDelGoods && position != AlertView.CANCELPOSITION) {
//            确定删除商品
            delSelGoods();
        }
    }

    private void delSelGoods() {
        String url = Url.BASE_URL + Url.Q_SHOP_DETAILS_DEL_CAR + uid + "&ids=" + carIds;
        XUtil.Get(url, null, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                RequestStatus status = gson.fromJson(result, RequestStatus.class);
                if (status != null) {
                    if (status.getCode().equals("01")) {
                        page = 1;
                        setEditStatus();
                        loadData();
                    } else {
                        showToast(ShopCarActivity.this, status.getMsg());
                    }
                }
            }
        });
    }

    private void getGoodsSkuDetails(final View view) {
        final TextView textView = (TextView) view;
        final CartInfoBean cartInfoBean = (CartInfoBean) view.getTag();
        if (cartInfoBean != null && cartInfoBean.getSaleSku() != null) {
            //商品详情内容
            String url = Url.BASE_URL + Url.Q_SHOP_DETAILS_GET_SKU_CAR;
            Map<String, Object> params = new HashMap<>();
            params.put("productId", cartInfoBean.getProductId());
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    EditGoodsSkuEntity editGoodsSkuEmpty = gson.fromJson(result, EditGoodsSkuEntity.class);
                    if (editGoodsSkuEmpty != null) {
                        if (editGoodsSkuEmpty.getCode().equals("01")) {
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
            if (shopCarGoodsSku != null && (shopCarGoodsSku.getSaleSkuId() != shopProperty.getSkuId()||
                    shopCarGoodsSku.getCount()!=shopProperty.getOldCount())) {
                changeGoodsSku(shopProperty.getId(), shopCarGoodsSku, cartInfoBean);
            }
        });
    }

    private void changeGoodsSku(int productId, ShopCarGoodsSku shopCarGoodsSku, final CartInfoBean cartInfoBean) {
        //商品属性修改
        String url = Url.BASE_URL + Url.Q_SHOP_DETAILS_CHANGE_CAR;
        Map<String, Object> params = new HashMap<>();
        params.put("userId", uid);
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
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                RequestStatus status = gson.fromJson(result, RequestStatus.class);
                if (status != null) {
                    if (status.getCode().equals("01")) {
                        updateCartData(cartInfoBean.getCurrentPosition());
                        setEditStatus();
                    } else {
                        showToast(ShopCarActivity.this, status.getResult() != null ? status.getResult().getMsg() : status.getMsg());
                    }
                }
            }
        });
    }

    /**
     * 获取结算金额
     */
    private void getSettlePrice() {
        if (shopGoodsList.size() > 0) {
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
            params.put("userId", uid);
            params.put("version","v3.1.5");
            XUtil.Post(Url.BASE_URL + Url.PRO_SETTLE_PRICE, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    loadHud.dismiss();
                    Gson gson = new Gson();
                    ShopCarNewInfoEntity shopCarNewInfoEntity = gson.fromJson(result, ShopCarNewInfoEntity.class);
                    if (shopCarNewInfoEntity != null) {
                        if (shopCarNewInfoEntity.getCode().equals("01")) {
                            updatePrice(shopCarNewInfoEntity);
                        } else {
                            updateLocalPrice();
                            showToast(ShopCarActivity.this, R.string.refrence_only);
                        }
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    loadHud.dismiss();
                    updateLocalPrice();
                    showToast(ShopCarActivity.this, R.string.refrence_only);
                    super.onError(ex, isOnCallback);
                }
            });
        }
    }

    @OnClick({R.id.rel_communal_error, R.id.communal_empty})
    void refreshData(View view) {
        communal_load.setVisibility(View.VISIBLE);
        communal_empty.setVisibility(View.GONE);
        communal_error.setVisibility(View.GONE);
        page = 1;
        loadData();
    }

    public class RecommendHeaderView {
        @BindView(R.id.tv_pro_title)
        TextView tv_pro_title;
        @BindView(R.id.communal_recycler_wrap)
        RecyclerView communal_recycler_wrap;

        public void initViews() {
            communal_recycler_wrap.setLayoutManager(new GridLayoutManager(ShopCarActivity.this, 2));
            communal_recycler_wrap.addItemDecoration(new PinnedHeaderItemDecoration.Builder(-1)
                    // 设置分隔线资源ID
                    .setDividerId(R.drawable.item_divider_five_gray_f)
                    // 开启绘制分隔线，默认关闭
                    .enableDivider(true)
                    // 是否关闭标签点击事件，默认开启
                    .disableHeaderClick(false)
                    // 设置标签和其内部的子控件的监听，若设置点击监听不为null，但是disableHeaderClick(true)的话，还是不会响应点击事件
                    .setHeaderClickListener(null)
                    .create());
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
                    if(likedProduct!=null&&!TextUtils.isEmpty(likedProduct.getRecommendFlag())){
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
}
