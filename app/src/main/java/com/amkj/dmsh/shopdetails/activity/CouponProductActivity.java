package com.amkj.dmsh.shopdetails.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.dominant.adapter.GoodProductAdapter;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.user.bean.UserLikedProductEntity;
import com.amkj.dmsh.user.bean.LikedProductBean;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TWENTY;
import static com.amkj.dmsh.constant.Url.Q_COUPON_PRODUCT_LIST;


/**
 * Created by xiaoxin on 2020/1/17
 * Version:v4.4.0
 * ClassDescription :优惠券-选择指定商品优惠券-可用券商品
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
    private List<LikedProductBean> couponProductList = new ArrayList<>();
    private int page = 1;
    private String userCouponId;
    private GoodProductAdapter mGoodProductAdapter;
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
            showToast(R.string.unConnectedNetwork);
            finish();
            return;
        }
        tv_header_titleAll.setText("可用券商品");
        tl_normal_bar.setSelected(true);
        header_shared.setVisibility(View.INVISIBLE);
        smart_communal_refresh.setOnRefreshListener(refreshLayout -> loadData());
        communal_recycler.setLayoutManager(new GridLayoutManager(CouponProductActivity.this, 2));
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_dp)
                .create());
        mGoodProductAdapter = new GoodProductAdapter(CouponProductActivity.this, couponProductList);
        communal_recycler.setAdapter(mGoodProductAdapter);
        mGoodProductAdapter.setOnLoadMoreListener(() -> {
            page++;
            getCouponProductData();
        }, communal_recycler);
    }

    @Override
    protected void loadData() {
        page = 1;
        getCouponProductData();
    }

    private void getCouponProductData() {
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", page);
        params.put("showCount", TOTAL_COUNT_TWENTY);
        params.put("user_coupon_id", userCouponId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Q_COUPON_PRODUCT_LIST,
                params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();
                        likedProductEntity = GsonUtils.fromJson(result, UserLikedProductEntity.class);
                        if (page == 1) {
                            couponProductList.clear();
                        }
                        String code = likedProductEntity.getCode();
                        if (SUCCESS_CODE.equals(code)) {
                            List<LikedProductBean> goodsList = likedProductEntity.getGoodsList();
                            if (goodsList != null) {
                                couponProductList.addAll(likedProductEntity.getGoodsList());
                                mGoodProductAdapter.notifyDataSetChanged();
                                if (goodsList.size() >= TOTAL_COUNT_TWENTY) {
                                    mGoodProductAdapter.loadMoreComplete();
                                } else {
                                    mGoodProductAdapter.loadMoreEnd();
                                }
                            }
                        } else {
                            mGoodProductAdapter.notifyDataSetChanged();
                            mGoodProductAdapter.loadMoreEnd();
                            if (!EMPTY_CODE.equals(code)) showToast(likedProductEntity.getMsg());
                        }
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, couponProductList, likedProductEntity);
                    }

                    @Override
                    public void onNotNetOrException() {
                        smart_communal_refresh.finishRefresh();
                        mGoodProductAdapter.loadMoreFail();
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, couponProductList, likedProductEntity);
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

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    public View getLoadView() {
        return smart_communal_refresh;
    }

    @Override
    protected String getEmptyText() {
        return "暂无可用券商品";
    }
}
