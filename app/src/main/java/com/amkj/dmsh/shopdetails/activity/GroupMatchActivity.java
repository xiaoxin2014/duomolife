package com.amkj.dmsh.shopdetails.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
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
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.mine.activity.ShopCarActivity;
import com.amkj.dmsh.dao.ShopCarDao;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.adapter.GroupMatchAdapter;
import com.amkj.dmsh.shopdetails.bean.CombineGoodsBean;
import com.amkj.dmsh.shopdetails.bean.EditGoodsSkuEntity.EditGoodsSkuBean;
import com.amkj.dmsh.shopdetails.bean.GroupGoodsEntity;
import com.amkj.dmsh.shopdetails.bean.GroupGoodsEntity.GroupGoodsBean.CombineCommonBean;
import com.amkj.dmsh.utils.DoubleUtil;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.views.bottomdialog.SkuDialog;
import com.luck.picture.lib.decoration.RecycleViewDivider;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;
import q.rorbin.badgeview.Badge;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getBadge;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getSpannableString;
import static com.amkj.dmsh.constant.ConstantMethod.getStringChangeDouble;
import static com.amkj.dmsh.constant.ConstantMethod.getStringChangeIntegers;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsFormat;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.showToastRequestMsg;
import static com.amkj.dmsh.constant.ConstantMethod.stripTrailingZeros;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_W_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.dao.OrderDao.getCarCount;

/**
 * Created by xiaoxin on 2019/5/30
 * Version:v4.1.0
 * ClassDescription :????????????
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
    private List<CombineCommonBean> goods = new ArrayList<>();
    private GroupGoodsEntity mGroupGoodsEntity;
    private String mProductId;
    private SkuDialog skuDialog;
    private int mPosition;
    private CombineCommonBean mCombineMainProduct = null;

    @Override
    protected int getContentView() {
        return R.layout.activity_group_collocation;
    }

    @Override
    protected void initViews() {
        if (getIntent() != null && !TextUtils.isEmpty(getIntent().getStringExtra("productId"))) {
            mProductId = getIntent().getStringExtra("productId");
        } else {
            showToast("??????????????????????????????");
            finish();
        }
        mTvHeaderTitle.setText("????????????");
        mIvImgService.setImageResource(R.drawable.shop_car_gray_icon);
        mBadge = getBadge(this, mFlHeaderService);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mGroupCollocaAdapter = new GroupMatchAdapter(this, goods);
        mCommunalRecycler.addItemDecoration(new RecycleViewDivider(
                this, LinearLayoutManager.HORIZONTAL, AutoSizeUtils.mm2px(this, 1), getResources().getColor(R.color.text_color_e_s)));
        mCommunalRecycler.setLayoutManager(layoutManager);
        mCommunalRecycler.setAdapter(mGroupCollocaAdapter);
        mGroupCollocaAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            CombineCommonBean combineCommonBean = (CombineCommonBean) view.getTag();
            if (combineCommonBean == null) return;
            switch (view.getId()) {
                //??????sku
                case R.id.tv_select_sku:
                    selectSku(combineCommonBean, position, view);
                    break;
                //??????????????????
                case R.id.rl_cover:
                    int productId = combineCommonBean.getProductId();
                    Intent intent = new Intent(getActivity(), ShopScrollDetailsActivity.class);
                    intent.putExtra("productId", String.valueOf(productId));
                    startActivity(intent);
                    break;
            }
        });

        //?????????????????????
        mGroupCollocaAdapter.setOnItemClickListener((adapter, view, position) -> {
            CombineCommonBean combineCommonBean = (CombineCommonBean) view.getTag();
            if (combineCommonBean != null && !combineCommonBean.isMainProduct() && ((ViewGroup) view).getChildAt(0).isEnabled()) {
                //?????????sku?????????????????????????????????????????????
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
            skuDialog = null;
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
                    //??????sku??????????????????
                    view.setSelected(true);
                    if (!combineCommonBean.isMainProduct()) {
                        combineCommonBean.setSelected(true);
                    }
                    //?????????????????????????????????????????????
                    combineCommonBean.setPicUrl(shopCarGoodsSku.getPicUrl());
                    combineCommonBean.setMinPrice(String.valueOf(shopCarGoodsSku.getPrice()));
                    combineCommonBean.setMaxPrice(combineCommonBean.getMinPrice());
                    combineCommonBean.setSkuId(shopCarGoodsSku.getSaleSkuId());
                    combineCommonBean.setCount(shopCarGoodsSku.getCount());
                    combineCommonBean.setSkuValue(shopCarGoodsSku.getValuesName());
                    combineCommonBean.setStock(shopCarGoodsSku.getQuantity());
                    combineCommonBean.setSaveMoney(getStringChangeDouble(shopCarGoodsSku.getOriginalPrice()) - shopCarGoodsSku.getPrice());
                    mGroupCollocaAdapter.notifyItemChanged(position);
                    //???????????????
                    updateTotalPrice();
                }
            });
            mPosition = position;
        }

        skuDialog.show();
    }

    //???????????????
    private void updateTotalPrice() {
        try {
            CombineCommonBean mainBan = goods.get(0);
            //???????????????
            double mainPrice = getStringChangeDouble(mainBan.getMinPrice());

            //?????????????????????
            double matchTotalPrice = 0;
            if (isSelectGroup()) {
                for (CombineCommonBean bean : goods) {
                    if (bean.isSelected() && bean.getSkuId() > 0 && !bean.isMainProduct()) {
                        matchTotalPrice = DoubleUtil.add(matchTotalPrice, getStringChangeDouble(bean.getMinPrice()));
                    }
                }
            } else {
                matchTotalPrice = getMinMatch();
            }

            //???????????????(?????????+????????????)
            double totalSave = 0;
            for (CombineCommonBean bean : goods) {
                if (bean.isSelected() && bean.getSkuId() > 0) {
                    totalSave = DoubleUtil.add(totalSave, bean.getSaveMoney());
                }
            }
            String end = (mainBan.getSkuId() <= 0 || !isSelectGroup()) ? "???" : "";
            String totalPrice = getStringsFormat(this, R.string.group_total_price, stripTrailingZeros(String.valueOf(DoubleUtil.add(mainPrice, matchTotalPrice)))) + end;
            CharSequence rmbFormat = getSpannableString(totalPrice, 1, TextUtils.isEmpty(end) ? totalPrice.length() : totalPrice.length() - 1, 1.6f, null);
            mTvGroupPrice.setText(rmbFormat);
            mTvSaveNum.setVisibility(isSelectGroup() ? View.VISIBLE : View.GONE);
            mTvSaveNum.setText(getStringsFormat(this, R.string.save_money, stripTrailingZeros(String.valueOf(totalSave))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void loadData() {
        getCarCount(this);
        getGroupGoods(mProductId);
    }

    //??????????????????????????????
    private void getGroupGoods(String id) {
        String url = Url.Q_GROUP_GOODS_DETAIL;
        Map<String, Object> params = new HashMap<>();
        params.put("productId", id);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mSmartCommunalRefresh.finishRefresh();
                mGroupGoodsEntity = GsonUtils.fromJson(result, GroupGoodsEntity.class);
                if (mGroupGoodsEntity != null && mGroupGoodsEntity.getResult() != null) {
                    GroupGoodsEntity.GroupGoodsBean groupGoodsBean = mGroupGoodsEntity.getResult();
                    if (mGroupGoodsEntity.getCode().equals(SUCCESS_CODE)) {
                        goods.clear();
                        mCombineMainProduct = groupGoodsBean.getCombineMainProduct();
                        if (mCombineMainProduct != null) {
                            mCombineMainProduct.setMainProduct(true);
                            mCombineMainProduct.setActivityCode(groupGoodsBean.getActivityCode());
                            mCombineMainProduct.setSelected(true);//?????????????????????????????????????????????
                            goods.add(mCombineMainProduct);
                        }
                        List<CombineCommonBean> combineProductList = groupGoodsBean.getCombineMatchProductList();
                        if (combineProductList != null && combineProductList.size() > 0) {
                            goods.addAll(combineProductList.subList(0, combineProductList.size() > 20 ? 20 : combineProductList.size()));
                        }
                        mGroupCollocaAdapter.notifyDataSetChanged();
                    } else if (!mGroupGoodsEntity.getCode().equals(EMPTY_CODE)) {
                        ConstantMethod.showToast(mGroupGoodsEntity.getMsg());
                    }
                }

                mCommunalRecycler.setPadding(AutoSizeUtils.mm2px(mAppContext, 24), 0, AutoSizeUtils.mm2px(mAppContext, 24), AutoSizeUtils.mm2px(mAppContext, goods.size() > 0 ? 98 : 0));
                if (mCombineMainProduct != null) {
                    NetLoadUtils.getNetInstance().showLoadSir(loadService, goods, mGroupGoodsEntity);
                    updateTotalPrice();
                } else {
                    NetLoadUtils.getNetInstance().showLoadSirLoadFailed(loadService);
                }

                //????????????
                boolean hasStock = ShopCarDao.checkStock(goods);
                mTvAddCar.setEnabled(hasStock);
                mTvBuy.setEnabled(hasStock);
                mTvBuy.setText(hasStock ? "????????????" : "?????????");
            }

            @Override
            public void onNotNetOrException() {
                mSmartCommunalRefresh.finishRefresh();
                mLlBottom.setVisibility(View.GONE);
                mCommunalRecycler.setPadding(AutoSizeUtils.mm2px(mAppContext, 24), 0, AutoSizeUtils.mm2px(mAppContext, 24), 0);
                NetLoadUtils.getNetInstance().showLoadSir(loadService, goods, mGroupGoodsEntity);
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

    @OnClick({R.id.tv_life_back, R.id.iv_img_service, R.id.iv_img_share, R.id.tv_add_car, R.id.tv_buy})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_life_back:
                finish();
                break;
            //???????????????
            case R.id.iv_img_service:
                intent = new Intent(getActivity(), ShopCarActivity.class);
                startActivity(intent);
                break;
            //??????????????????
            case R.id.iv_img_share:
                if (mCombineMainProduct != null) {
                    new UMShareAction(this,
                            mCombineMainProduct.getPicUrl(), "??????????????????" + mCombineMainProduct.getName(),
                            "??????????????????" + mCombineMainProduct.getName(),
                            "https://www.domolife.cn/m/template/common/combination_match.html?id=" + mProductId,
                            "pages/combineGoods/combineGoods?id=" + mProductId, getStringChangeIntegers(mProductId));
                }
                break;
            //???????????????
            case R.id.tv_add_car:
                if (userId > 0) {
                    if (goods.size() > 0) {
                        //?????????????????????????????????sku
                        if (goods.get(0).getSkuId() > 0) {
                            //?????????????????????????????????
                            if (isSelectGroup()) {
                                addCombineCart();
                            } else {
                                ConstantMethod.showToast("??????????????????????????????");
                            }
                        } else {
                            ConstantMethod.showToast("????????????????????????");
                        }
                    }
                } else {
                    getLoginStatus(this);
                }

                break;
            //????????????
            case R.id.tv_buy:
                if (userId > 0) {
                    if (goods.size() > 0) {
                        //?????????????????????????????????sku
                        if (goods.get(0).getSkuId() > 0) {
                            //?????????????????????????????????
                            if (isSelectGroup()) {
                                buyNow();
                            } else {
                                ConstantMethod.showToast("??????????????????????????????");
                            }
                        } else {
                            ConstantMethod.showToast("????????????????????????");
//                            //?????????????????????sku
//                            selectSku(goods.get(0), 0, false, view);
                        }
                    }
                } else {
                    getLoginStatus(this);
                }
                break;
        }
    }

    //????????????
    private void buyNow() {
        List<CombineGoodsBean> combineGoods = new ArrayList<>();
        CombineGoodsBean combineGoodsBean = new CombineGoodsBean();
        for (CombineCommonBean commonBean : goods) {
            if (commonBean.isMainProduct()) {
                combineGoodsBean.setMainId(0);
                combineGoodsBean.setCount(commonBean.getCount());
                combineGoodsBean.setProductId(commonBean.getProductId());
                combineGoodsBean.setSkuId(commonBean.getSkuId());
            } else {
                if ((commonBean.isSelected() && commonBean.getStock() > 0 && commonBean.getSkuId() > 0)) {
                    CombineGoodsBean.MatchProductsBean matchProductsBean = new CombineGoodsBean.MatchProductsBean();
                    matchProductsBean.setCombineMatchId(0);
                    matchProductsBean.setProductId(commonBean.getProductId());
                    matchProductsBean.setSkuId(commonBean.getSkuId());
                    combineGoodsBean.getMatchProducts().add(matchProductsBean);
                }
            }
        }

        combineGoods.add(combineGoodsBean);
        Bundle bundle = new Bundle();
        bundle.putString("combineGoods", GsonUtils.toJson(combineGoods));
        ConstantMethod.skipIndentWrite(getActivity(), INDENT_W_TYPE, bundle);
    }

    private void addCombineCart() {
        mTvAddCar.setEnabled(false);
        //???????????????
        Map<String, Object> params = new HashMap<>();
        String combines = ShopCarDao.getCombinesCart(goods);
        if (!TextUtils.isEmpty(combines)) {
            params.put("combines", combines);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.Q_COMBINE_PRODUCT_ADD_CAR, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mTvAddCar.setEnabled(true);
                RequestStatus status = GsonUtils.fromJson(result, RequestStatus.class);
                if (status != null && SUCCESS_CODE.equals(status.getCode())) {
                    showToast(getString(R.string.AddCarSuccess));
                    //???????????????????????????
                    getCarCount(getActivity());
                } else {
                    showToastRequestMsg(status);
                }
            }

            @Override
            public void onNotNetOrException() {
                mTvAddCar.setEnabled(true);
            }
        });
    }

    //????????????????????????
    private boolean isSelectGroup() {
        int selectNum = 0;
        for (CombineCommonBean commonBean : goods) {
            if (commonBean.isSelected() && !commonBean.isMainProduct()) {
                selectNum++;
            }
        }
        return selectNum > 0;
    }

    //?????????????????????????????????
    private double getMinMatch() {
        if (goods != null && goods.size() > 1) {
            List<CombineCommonBean> groupList = new ArrayList<>(goods.subList(1, goods.size()));
            Collections.sort(groupList);
            return getStringChangeDouble(groupList.get(0).getMinPrice());
        } else {
            return 0;
        }
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (message.type.equals(ConstantVariable.UPDATE_CAR_NUM)) {
            if (mBadge != null) {
                mBadge.setBadgeNumber((int) message.result);
            }
        }
    }
}
