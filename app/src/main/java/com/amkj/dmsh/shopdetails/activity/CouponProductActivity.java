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
import com.amkj.dmsh.constant.BaseAddCarProInfoBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.dominant.adapter.QualityTypeProductAdapter;
import com.amkj.dmsh.mine.activity.MineLoginActivity;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.user.bean.UserLikedProductEntity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.google.gson.Gson;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.getPersonalInfo;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
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
    RefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    @BindView(R.id.communal_load)
    View communal_load;
    @BindView(R.id.communal_error)
    View communal_error;
    @BindView(R.id.communal_empty)
    View communal_empty;
    //    商品列表
    private List<LikedProductBean> couponProductList = new ArrayList();
    private int page = 1;
    private String userCouponId;
    private QualityTypeProductAdapter qualityTypeProductAdapter;

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
            page = 1;
            loadData();
        });
        communal_recycler.setLayoutManager(new GridLayoutManager(CouponProductActivity.this, 2));
        communal_recycler.addItemDecoration(new PinnedHeaderItemDecoration.Builder(-1)
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_dp)
                // 开启绘制分隔线，默认关闭
                .enableDivider(true)
                // 是否关闭标签点击事件，默认开启
                .disableHeaderClick(false)
                // 设置标签和其内部的子控件的监听，若设置点击监听不为null，但是disableHeaderClick(true)的话，还是不会响应点击事件
                .setHeaderClickListener(null)
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
                    getLoginStatus();
                }
            }
        });
    }

    @Override
    protected void loadData() {
        getCouponProductData();
    }

    private void getCouponProductData() {
        if (NetWorkUtils.checkNet(CouponProductActivity.this)) {
            String url = Url.BASE_URL + Url.Q_COUPON_PRODUCT_LIST;
            Map<String, Object> params = new HashMap<>();
            params.put("currentPage", page);
            params.put("showCount", TOTAL_COUNT_TWENTY);
            params.put("user_coupon_id", userCouponId);
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    if (loadHud != null) {
                        loadHud.dismiss();
                    }
                    smart_communal_refresh.finishRefresh();
                    qualityTypeProductAdapter.loadMoreComplete();
                    communal_load.setVisibility(View.GONE);
                    communal_error.setVisibility(View.GONE);
                    if (page == 1) {
                        couponProductList.clear();
                    }
                    Gson gson = new Gson();
                    UserLikedProductEntity typeBean = gson.fromJson(result, UserLikedProductEntity.class);
                    if (typeBean != null) {
                        if (typeBean.getCode().equals("01")) {
                            couponProductList.addAll(typeBean.getLikedProductBeanList());
                        } else if (typeBean.getCode().equals("02")) {
                            qualityTypeProductAdapter.loadMoreEnd();
                        } else {
                            qualityTypeProductAdapter.loadMoreEnd();
                            showToast(CouponProductActivity.this, typeBean.getMsg());
                        }
                        qualityTypeProductAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    smart_communal_refresh.finishRefresh();
                    qualityTypeProductAdapter.loadMoreComplete();
                    if (loadHud != null) {
                        loadHud.dismiss();
                    }
                    if (page == 1 && qualityTypeProductAdapter.getItemCount() < 1) {
                        communal_load.setVisibility(View.GONE);
                        communal_error.setVisibility(View.VISIBLE);
                    } else {
                        showToast(CouponProductActivity.this, R.string.invalidData);
                    }
                    super.onError(ex, isOnCallback);
                }
            });
        } else {
            if (loadHud != null) {
                loadHud.dismiss();
            }
            smart_communal_refresh.finishRefresh();
            qualityTypeProductAdapter.loadMoreComplete();
            communal_load.setVisibility(View.GONE);
            if (page == 1) {
                communal_error.setVisibility(View.VISIBLE);
            }
            showToast(CouponProductActivity.this, R.string.unConnectedNetwork);
        }
    }

    private void getLoginStatus() {
        SavePersonalInfoBean personalInfo = getPersonalInfo(this);
        if (personalInfo.isLogin()) {
            userId = personalInfo.getUid();
        } else {
            //未登录跳转登录页
            Intent intent = new Intent(this, MineLoginActivity.class);
            startActivityForResult(intent, IS_LOGIN_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IS_LOGIN_CODE) {
            getLoginStatus();
        }
    }

    @OnClick({R.id.rel_communal_error, R.id.communal_empty})
    void refreshData() {
        communal_load.setVisibility(View.VISIBLE);
        communal_error.setVisibility(View.GONE);
        page = 1;
        loadData();
    }

    @OnClick(R.id.tv_life_back)
    void goBack() {
        finish();
    }
}
