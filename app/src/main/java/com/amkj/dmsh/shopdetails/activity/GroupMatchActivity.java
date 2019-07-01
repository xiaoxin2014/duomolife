package com.amkj.dmsh.shopdetails.activity;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.mine.activity.ShopCarActivity;
import com.amkj.dmsh.mine.bean.ShopCarEntity.ShopCartBean.CartBean.CartInfoBean;
import com.amkj.dmsh.mine.biz.ShopCarDao;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.adapter.GroupMatchAdapter;
import com.amkj.dmsh.shopdetails.bean.EditGoodsSkuEntity.EditGoodsSkuBean;
import com.amkj.dmsh.shopdetails.bean.GroupGoodsEntity;
import com.amkj.dmsh.shopdetails.bean.GroupGoodsEntity.GroupGoodsBean.CombineCommonBean;
import com.amkj.dmsh.views.bottomdialog.SkuDialog;
import com.google.gson.Gson;
import com.luck.picture.lib.decoration.RecycleViewDivider;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;
import q.rorbin.badgeview.Badge;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getBadge;
import static com.amkj.dmsh.constant.ConstantMethod.getCarCount;
import static com.amkj.dmsh.constant.ConstantMethod.getDoubleFormat;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getSpannableString;
import static com.amkj.dmsh.constant.ConstantMethod.getStringChangeDouble;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.showToastRequestMsg;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;

/**
 * Created by xiaoxin on 2019/5/30
 * Version:v4.1.0
 * ClassDescription :组合搭配
 */
public class GroupMatchActivity extends BaseActivity {
    @BindView(R.id.tv_life_back)
    TextView mTvLifeBack;
    @BindView(R.id.tv_header_title)
    TextView mTvHeaderTitle;
    @BindView(R.id.iv_img_service)
    ImageView mIvImgService;
    @BindView(R.id.fl_header_service)
    FrameLayout mFlHeaderService;
    @BindView(R.id.iv_img_share)
    ImageView mIvImgShare;
    @BindView(R.id.tl_quality_bar)
    Toolbar mTlQualityBar;
    @BindView(R.id.communal_recycler)
    RecyclerView mCommunalRecycler;
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout mSmartCommunalRefresh;
    @BindView(R.id.ll_bottom)
    LinearLayout mLlBottom;
    @BindView(R.id.tv_save_num)
    TextView mTvSaveNum;
    @BindView(R.id.tv_group_price)
    TextView mTvGroupPrice;
    @BindView(R.id.rl_main)
    RelativeLayout mRlMain;
    @BindView(R.id.tv_add_car)
    TextView mTvAddCar;
    @BindView(R.id.tv_buy)
    TextView mTvBuy;

    private Badge mBadge;
    private GroupMatchAdapter mGroupCollocaAdapter;
    private List<CombineCommonBean> groupGoods = new ArrayList<>();
    private GroupGoodsEntity mGroupGoodsEntity;
    private String mProductId;
    private SkuDialog skuDialog;
    private int mPosition;

    @Override
    protected int getContentView() {
        return R.layout.activity_group_collocation;
    }

    @Override
    protected void initViews() {
        if (getIntent() != null && !TextUtils.isEmpty(getIntent().getStringExtra("productId"))) {
            mProductId = getIntent().getStringExtra("productId");
        } else {
            showToast("商品信息有误，请重试");
            finish();
        }
        mTvHeaderTitle.setText("组合搭配");
        mIvImgService.setImageResource(R.drawable.shop_car_gray_icon);
        mBadge = getBadge(this, mFlHeaderService);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mGroupCollocaAdapter = new GroupMatchAdapter(this, groupGoods);
        mCommunalRecycler.addItemDecoration(new RecycleViewDivider(
                this, LinearLayoutManager.HORIZONTAL, AutoSizeUtils.mm2px(this, 1), getResources().getColor(R.color.text_color_e_s)));
        mCommunalRecycler.setLayoutManager(layoutManager);
        mCommunalRecycler.setAdapter(mGroupCollocaAdapter);
        mGroupCollocaAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            CombineCommonBean combineCommonBean = (CombineCommonBean) view.getTag();
            if (combineCommonBean == null) return;
            switch (view.getId()) {
                //选择sku
                case R.id.tv_select_sku:
                    selectSku(combineCommonBean, position, view);
                    break;
                //选中或取消
                case R.id.rl_cover:
                    int productId = combineCommonBean.getProductId();
                    Intent intent = new Intent(getActivity(), ShopScrollDetailsActivity.class);
                    intent.putExtra("productId", String.valueOf(productId));
                    startActivity(intent);
                    break;
            }
        });

        //选中和取消选中
        mGroupCollocaAdapter.setOnItemClickListener((adapter, view, position) -> {
            CombineCommonBean combineCommonBean = (CombineCommonBean) view.getTag();
            if (combineCommonBean != null && !combineCommonBean.isMainProduct()) {
                //已选择sku的情况下才能直接选中或取消选中
                if (combineCommonBean.getSkuId() > 0) {
                    view.setSelected(!view.isSelected());
                    combineCommonBean.setSelected(view.isSelected());
                    updateTotalPrice();
                } else {
                    selectSku(combineCommonBean, position, view);
                }
            }
        });
        mSmartCommunalRefresh.setOnRefreshListener((refreshLayout) -> {
            getGroupGoods(mProductId);
            mTvSaveNum.setVisibility(View.GONE);
        });

    }

    private void selectSku(CombineCommonBean combineCommonBean, int position, View view) {
        if (skuDialog == null || position != mPosition) {
            skuDialog = new SkuDialog(this);
            EditGoodsSkuBean editGoodsSkuBean = new EditGoodsSkuBean(combineCommonBean);
            editGoodsSkuBean.setShowBottom(true);
            skuDialog.refreshView(editGoodsSkuBean);
            skuDialog.getGoodsSKu(shopCarGoodsSku -> {
                if (shopCarGoodsSku != null && shopCarGoodsSku.getSaleSkuId() != combineCommonBean.getSkuId()) {
                    //选择sku商品默认选中
                    view.setSelected(true);
                    if (!combineCommonBean.isMainProduct()) {
                        combineCommonBean.setSelected(true);
                    }
                    //选择规格后更新商品价格和封面图
                    combineCommonBean.setPicUrl(shopCarGoodsSku.getPicUrl());
                    combineCommonBean.setMinPrice(String.valueOf(shopCarGoodsSku.getPrice()));
                    combineCommonBean.setMaxPrice(combineCommonBean.getMinPrice());
                    combineCommonBean.setSkuId(shopCarGoodsSku.getSaleSkuId());
                    combineCommonBean.setCount(shopCarGoodsSku.getCount());
                    combineCommonBean.setSkuValue(shopCarGoodsSku.getValuesName());
                    combineCommonBean.setQuantity(shopCarGoodsSku.getQuantity());
                    combineCommonBean.setSaveMoney(getStringChangeDouble(shopCarGoodsSku.getOriginalPrice()) - shopCarGoodsSku.getPrice());
                    mGroupCollocaAdapter.notifyItemChanged(position);
                    //更新组合价
                    updateTotalPrice();
                }
            });
            mPosition = position;
        }

        skuDialog.show();
    }

    //更新总价格
    private void updateTotalPrice() {
        try {
            double groupPrice = 0;
            double totalSave = 0;
            //组合商品价格计算方案（如果选择了组合商品，计算选中的组合商品总价格，否则筛选出最低的组合商品价格）
            if (isSelectGroup()) {
                for (CombineCommonBean bean : groupGoods) {
                    if (bean.isSelected() && bean.getSkuId() > 0 && !bean.isMainProduct()) {
                        groupPrice += Double.parseDouble(bean.getMinPrice());
//                        if (!TextUtils.isEmpty(bean.getTag())) {
//                            Matcher m = Pattern.compile("[\\u4e00-\\u9fa5]").matcher(bean.getTag());
//                            double saveNum = Double.parseDouble(m.replaceAll("").trim());
//                            totalSave += saveNum;
//                        }

                        totalSave += bean.getSaveMoney();
                    }
                }
                mTvSaveNum.setVisibility(View.VISIBLE);
                mTvSaveNum.setText(getDoubleFormat(this, R.string.save_money, totalSave));
            } else {
                groupPrice = getMinGroup();
                mTvSaveNum.setVisibility(View.GONE);
            }

            //获取主商品价格
            CombineCommonBean combineMainProduct = groupGoods.get(0);
            double mainPrice = getStringChangeDouble(combineMainProduct.getMinPrice());
            String end = (combineMainProduct.getSkuId() <= 0 || !isSelectGroup()) ? "起" : "";

            //组合价=主商品价格+组合商品价格
            String totalPrice = getDoubleFormat(this, R.string.group_total_price, mainPrice + groupPrice) + end;
            CharSequence rmbFormat = getSpannableString(totalPrice, 1, TextUtils.isEmpty(end) ? totalPrice.length() : totalPrice.length() - 1, 1.6f, null);
            mTvGroupPrice.setText(rmbFormat);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void loadData() {
        getCarCount(this, mBadge);
        getGroupGoods(mProductId);
    }

    //获取组合商品详细信息
    private void getGroupGoods(String id) {
        String url = Url.BASE_URL + Url.Q_GROUP_GOODS_DETAIL;
        Map<String, Object> params = new HashMap<>();
        params.put("productId", id);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mSmartCommunalRefresh.finishRefresh();
                mGroupGoodsEntity = new Gson().fromJson(result, GroupGoodsEntity.class);
                CombineCommonBean combineMainProduct = null;
                if (mGroupGoodsEntity != null && mGroupGoodsEntity.getResult() != null) {
                    GroupGoodsEntity.GroupGoodsBean groupGoodsBean = mGroupGoodsEntity.getResult();
                    if (mGroupGoodsEntity.getCode().equals(SUCCESS_CODE)) {
                        groupGoods.clear();
                        combineMainProduct = groupGoodsBean.getCombineMainProduct();
                        if (combineMainProduct != null) {
                            combineMainProduct.setMainProduct(true);
                            combineMainProduct.setActivityCode(groupGoodsBean.getActivityCode());
                            groupGoods.add(combineMainProduct);
                        }
                        List<CombineCommonBean> combineProductList = groupGoodsBean.getCombineMatchProductList();
                        if (combineProductList != null && combineProductList.size() > 0) {
                            groupGoods.addAll(combineProductList.subList(0, combineProductList.size() > 20 ? 20 : combineProductList.size()));
                        }
                        mGroupCollocaAdapter.notifyDataSetChanged();
                    } else if (!mGroupGoodsEntity.getCode().equals(EMPTY_CODE)) {
                        ConstantMethod.showToast(mGroupGoodsEntity.getMsg());
                    }
                }

                mCommunalRecycler.setPadding(AutoSizeUtils.mm2px(mAppContext, 24), 0, AutoSizeUtils.mm2px(mAppContext, 24), AutoSizeUtils.mm2px(mAppContext, groupGoods.size() > 0 ? 98 : 0));
                if (combineMainProduct != null) {
                    NetLoadUtils.getNetInstance().showLoadSir(loadService, groupGoods, mGroupGoodsEntity);
                    updateTotalPrice();
//                    mTvGroupPrice.setText(getDoubleFormat(getActivity(), R.string.group_total_price_start, getStringChangeDouble(combineMainProduct.getMinPrice()) + getMinGroup()));
                } else {
                    NetLoadUtils.getNetInstance().showLoadSirLoadFailed(loadService);
                }
            }

            @Override
            public void onNotNetOrException() {
                mSmartCommunalRefresh.finishRefresh();
                mLlBottom.setVisibility(View.GONE);
                mCommunalRecycler.setPadding(AutoSizeUtils.mm2px(mAppContext, 24), 0, AutoSizeUtils.mm2px(mAppContext, 24), 0);
                NetLoadUtils.getNetInstance().showLoadSir(loadService, groupGoods, mGroupGoodsEntity);
            }
        });
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    public View getLoadView() {
        return mRlMain;
    }

    /**
     * @param view
     */
    @OnClick({R.id.tv_life_back, R.id.iv_img_service, R.id.iv_img_share, R.id.tv_add_car, R.id.tv_buy})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_life_back:
                finish();
                break;
            //跳转购物车
            case R.id.iv_img_service:
                intent = new Intent(getActivity(), ShopCarActivity.class);
                startActivity(intent);
                break;
            //分享组合商品
            case R.id.iv_img_share:
                break;
            //加入购物车
            case R.id.tv_add_car:
                if (userId > 0) {
                    if (groupGoods.size() > 0) {
                        //先判断主商品是否已选择sku
                        if (groupGoods.get(0).getSkuId() > 0) {
                            //再判断是否搭配组合商品
                            if (isSelectGroup()) {
                                mTvAddCar.setEnabled(false);
                                //加入购物车
                                Map<String, Object> params = new HashMap<>();
                                params.put("userId", userId);
                                String combines = ShopCarDao.getCombines(groupGoods);
                                if (!TextUtils.isEmpty(combines)) {
                                    params.put("combines", combines);
                                }
                                //添加埋点来源参数
                                ConstantMethod.addSourceParameter(params);
                                NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.Q_COMBINE_PRODUCT_ADD_CAR, params, new NetLoadListenerHelper() {
                                    @Override
                                    public void onSuccess(String result) {
                                        mTvAddCar.setEnabled(true);
                                        RequestStatus status = new Gson().fromJson(result, RequestStatus.class);
                                        if (status != null && SUCCESS_CODE.equals(status.getCode())) {
                                            showToast(getActivity(), "添加商品成功");
                                            //通知刷新购物车数量
                                            EventBus.getDefault().post(new EventMessage(ConstantVariable.UPDATE_CAR_NUM, ""));
                                        } else {
                                            showToastRequestMsg(getActivity(), status);
                                        }
                                    }

                                    @Override
                                    public void onNotNetOrException() {
                                        mTvAddCar.setEnabled(true);
                                    }
                                });
                            } else {
                                ConstantMethod.showToast("至少选择一个组合商品");
                            }
                        } else {
                            ConstantMethod.showToast("请选择主商品属性");
//                            selectSku(groupGoods.get(0), 0, false, view);
                        }
                    }
                } else {
                    getLoginStatus(this);
                }

                break;
            //立即购买
            case R.id.tv_buy:
                if (userId > 0) {
                    if (groupGoods.size() > 0) {
                        //先判断主商品是否已选择sku
                        if (groupGoods.get(0).getSkuId() > 0) {
                            //再判断是否搭配组合商品
                            if (isSelectGroup()) {
                                //立即购买
                                List<CartInfoBean> cartInfoList = new ArrayList<>();
                                for (int i = 0; i < groupGoods.size(); i++) {
                                    CombineCommonBean combineCommonBean = groupGoods.get(i);
                                    if ((combineCommonBean.isSelected() && combineCommonBean.getSkuId() > 0) || combineCommonBean.isMainProduct()) {
                                        cartInfoList.add(new CartInfoBean(groupGoods.get(i)));
                                    }
                                }

                                intent = new Intent(this, DirectIndentWriteActivity.class);
                                intent.putParcelableArrayListExtra("productDate", (ArrayList<? extends Parcelable>) cartInfoList);
                                startActivity(intent);
                            } else {
                                ConstantMethod.showToast("至少选择一个组合商品");
                            }
                        } else {
                            ConstantMethod.showToast("请选择主商品属性");
//                            //弹窗选择主商品sku
//                            selectSku(groupGoods.get(0), 0, false, view);
                        }
                    }
                } else {
                    getLoginStatus(this);
                }
                break;
        }
    }

    //是否搭配组合商品
    private boolean isSelectGroup() {
        int selectNum = 0;
        for (CombineCommonBean commonBean : groupGoods) {
            if (commonBean.isSelected() && !commonBean.isMainProduct()) {
                selectNum++;
            }
        }
        return selectNum > 0;
    }

    //获取最低价格的组合商品
    private double getMinGroup() {
        if (groupGoods != null && groupGoods.size() > 0) {
            List<CombineCommonBean> groupList = new ArrayList<>(groupGoods);
            Collections.sort(groupList);
            return getStringChangeDouble(groupList.get(0).getMinPrice());
        } else {
            return 0;
        }
    }
}
