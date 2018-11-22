package com.amkj.dmsh.shopdetails.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.NetLoadUtils;
import com.amkj.dmsh.constant.BaseAddCarProInfoBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dominant.adapter.QualityTypeProductAdapter;
import com.amkj.dmsh.user.bean.UserLikedProductEntity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean;
import com.google.gson.Gson;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TWENTY;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/1/2
 * version 3.0.6
 * class description:更多推荐 商品 专题
 */

public class CouponProductActivity extends BaseActivity {
    @BindView(R.id.tv_header_shared)
    TextView header_shared;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tl_normal_bar)
    Toolbar tl_normal_bar;
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    商品列表
    private List<LikedProductBean> couponProductList = new ArrayList();
    private int page = 1;
    private String userCouponId;
    private QualityTypeProductAdapter qualityTypeProductAdapter;
    private UserLikedProductEntity likedProductEntity;

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_refresh_recycle_header;
    }

    @Override
    protected void initViews() {
        Intent intent = getIntent();
        userCouponId = intent.getStringExtra("userCouponId");
        if (TextUtils.isEmpty(userCouponId)) {
            showToast(this, R.string.unConnectedNetwork);
            finish();
            return;
        }
        tl_normal_bar.setSelected(true);
        header_shared.setVisibility(View.INVISIBLE);
        smart_communal_refresh.setOnRefreshListener((refreshLayout) -> {
            loadData();
        });
        communal_recycler.setLayoutManager(new GridLayoutManager(CouponProductActivity.this, 2));
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_dp)






                .create());
        qualityTypeProductAdapter = new QualityTypeProductAdapter(CouponProductActivity.this, couponProductList);
        communal_recycler.setAdapter(qualityTypeProductAdapter);
        qualityTypeProductAdapter.setOnLoadMoreListener(() -> {
            page++;
            getCouponProductData();
        }, communal_recycler);
        qualityTypeProductAdapter.setOnItemClickListener((adapter, view, position) -> {
            LikedProductBean likedProductBean = (LikedProductBean) view.getTag();
            if (likedProductBean != null) {
                Intent intent1 = new Intent(CouponProductActivity.this, ShopScrollDetailsActivity.class);
                intent1.putExtra("productId", String.valueOf(likedProductBean.getId()));
                startActivity(intent1);
            }
        });
        qualityTypeProductAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            loadHud.show();
            LikedProductBean likedProductBean = (LikedProductBean) view.getTag();
            if (likedProductBean != null) {
                if (userId > 0) {
                    switch (view.getId()) {
                        case R.id.iv_pro_add_car:
                            BaseAddCarProInfoBean baseAddCarProInfoBean = new BaseAddCarProInfoBean();
                            baseAddCarProInfoBean.setProductId(likedProductBean.getId());
                            baseAddCarProInfoBean.setActivityCode(getStrings(likedProductBean.getActivityCode()));
                            baseAddCarProInfoBean.setProName(getStrings(likedProductBean.getName()));
                            baseAddCarProInfoBean.setProPic(getStrings(likedProductBean.getPicUrl()));
                            ConstantMethod constantMethod = new ConstantMethod();
                            constantMethod.addShopCarGetSku(CouponProductActivity.this, baseAddCarProInfoBean, loadHud);
                            break;
                    }
                } else {
                    loadHud.dismiss();
                    getLoginStatus(this);
                }
            }
        });
    }

    @Override
    protected void loadData() {
        page = 1;
        getCouponProductData();
    }

    private void getCouponProductData() {
        String url = Url.BASE_URL + Url.Q_COUPON_PRODUCT_LIST;
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", page);
        params.put("showCount", TOTAL_COUNT_TWENTY);
        params.put("user_coupon_id", userCouponId);
        NetLoadUtils.getQyInstance().loadNetDataPost(mAppContext, url, params, new NetLoadUtils.NetLoadListener() {
            @Override
            public void onSuccess(String result) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                smart_communal_refresh.finishRefresh();
                qualityTypeProductAdapter.loadMoreComplete();
                if (page == 1) {
                    couponProductList.clear();
                }
                Gson gson = new Gson();
                likedProductEntity = gson.fromJson(result, UserLikedProductEntity.class);
                if (likedProductEntity != null) {
                    if (likedProductEntity.getCode().equals(SUCCESS_CODE)) {
                        couponProductList.addAll(likedProductEntity.getLikedProductBeanList());
                    } else if (likedProductEntity.getCode().equals(EMPTY_CODE)) {
                        qualityTypeProductAdapter.loadMoreEnd();
                    } else {
                        qualityTypeProductAdapter.loadMoreEnd();
                        showToast(CouponProductActivity.this, likedProductEntity.getMsg());
                    }
                    qualityTypeProductAdapter.notifyDataSetChanged();
                }
                NetLoadUtils.getQyInstance().showLoadSir(loadService,couponProductList,likedProductEntity);
            }

            @Override
            public void netClose() {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                smart_communal_refresh.finishRefresh();
                qualityTypeProductAdapter.loadMoreComplete();
                showToast(CouponProductActivity.this, R.string.unConnectedNetwork);
                NetLoadUtils.getQyInstance().showLoadSir(loadService,couponProductList,likedProductEntity);
            }

            @Override
            public void onError(Throwable throwable) {
                smart_communal_refresh.finishRefresh();
                qualityTypeProductAdapter.loadMoreComplete();
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                showToast(CouponProductActivity.this, R.string.invalidData);
                NetLoadUtils.getQyInstance().showLoadSir(loadService,couponProductList,likedProductEntity);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.tv_life_back)
    void goBack() {
        finish();
    }
}
