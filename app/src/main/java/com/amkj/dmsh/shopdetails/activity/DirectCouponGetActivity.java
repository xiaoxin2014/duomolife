package com.amkj.dmsh.shopdetails.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.mine.bean.SelCouponGoodsEntity;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.adapter.DirectMyCouponAdapter;
import com.amkj.dmsh.shopdetails.bean.DirectCouponEntity;
import com.amkj.dmsh.shopdetails.bean.DirectCouponEntity.DirectCouponBean;
import com.amkj.dmsh.shopdetails.bean.IndentDiscountsEntity.IndentDiscountsBean.ProductInfoBean.ActivityProductInfoBean;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.bugly.beta.tinker.TinkerManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_1;

;
;
;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/1/23
 * version 3.1.5
 * class description:购物获取优惠券
 */

public class DirectCouponGetActivity extends BaseActivity {
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tv_header_shared)
    TextView header_shared;
    private List<DirectCouponBean> couponList = new ArrayList();
    private int page = 1;
    private DirectMyCouponAdapter directMyCouponAdapter;
    private List<ActivityProductInfoBean> settlementGoods;
    private int scrollY;
    private float screenHeight;
    private DirectCouponEntity directCouponEntity;

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_refresh_floating_header;
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
        tv_header_titleAll.setText("选择优惠券");
        header_shared.setVisibility(View.INVISIBLE);
        Intent intent = getIntent();
        settlementGoods = intent.getParcelableArrayListExtra("couponGoods");
        communal_recycler.setLayoutManager(new LinearLayoutManager(DirectCouponGetActivity.this));
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_twenty_white)


                .create());
        directMyCouponAdapter = new DirectMyCouponAdapter(couponList, "couponGet");
        communal_recycler.setAdapter(directMyCouponAdapter);
        smart_communal_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                //                滚动距离置0
                scrollY = 0;
                loadData();
            }
        });
        directMyCouponAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                selfChoiceCoupon();
            }
        }, communal_recycler);
        TinkerBaseApplicationLike app = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
        screenHeight = app.getScreenHeight();
        communal_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                scrollY += dy;
                if (!recyclerView.canScrollVertically(-1)) {
                    scrollY = 0;
                }
                if (scrollY > screenHeight * 1.5 && dy < 0) {
                    if (download_btn_communal.getVisibility() == GONE) {
                        download_btn_communal.setVisibility(VISIBLE);
                        download_btn_communal.hide(false);
                    }
                    if (!download_btn_communal.isVisible()) {
                        download_btn_communal.show();
                    }
                } else {
                    if (download_btn_communal.isVisible()) {
                        download_btn_communal.hide();
                    }
                }
            }
        });
        download_btn_communal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) communal_recycler.getLayoutManager();
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                int mVisibleCount = linearLayoutManager.findLastVisibleItemPosition()
                        - linearLayoutManager.findFirstVisibleItemPosition() + 1;
                if (firstVisibleItemPosition > mVisibleCount) {
                    communal_recycler.scrollToPosition(mVisibleCount);
                }
                communal_recycler.smoothScrollToPosition(0);
            }
        });
        directMyCouponAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                DirectCouponBean directCouponBean = (DirectCouponBean) view.getTag();
                if (directCouponBean != null) {
                    if (directCouponBean.getItemType() == TYPE_1 || directCouponBean.getUsable() == 1) {
                        Intent intentDate = new Intent();
                        if (directCouponBean.getItemType() != TYPE_1) {
                            intentDate.putExtra("couponId", directCouponBean.getId());
                            intentDate.putExtra("couponAmount", directCouponBean.getAmount());
                        }
                        setResult(RESULT_OK, intentDate);
                        finish();
                    } else {
                        for (DirectCouponBean directCoupon : couponList) {
                            if (directCoupon.getId() == directCouponBean.getId()) {
                                directCoupon.setCheckStatus(!directCoupon.isCheckStatus());
                                break;
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            if (requestCode == IS_LOGIN_CODE) {
                finish();
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IS_LOGIN_CODE) {
            selfChoiceCoupon();
        }
    }

    @OnClick(R.id.tv_life_back)
    void finish(View view) {
        finish();
    }

    @Override
    protected void loadData() {
        page = 1;
        selfChoiceCoupon();
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    protected View getLoadView() {
        return smart_communal_refresh;
    }

    private void selfChoiceCoupon() {
        if (settlementGoods != null
                && userId > 0) {
            List<SelCouponGoodsEntity> selCouponGoodsEntityList = new ArrayList<>();
            SelCouponGoodsEntity selCouponGoodsEntity;
            for (int i = 0; i < settlementGoods.size(); i++) {
                selCouponGoodsEntity = new SelCouponGoodsEntity();
                ActivityProductInfoBean activityProductInfoBean = settlementGoods.get(i);
                selCouponGoodsEntity.setPrice(!TextUtils.isEmpty(activityProductInfoBean.getNewPrice())
                        ? activityProductInfoBean.getNewPrice() : activityProductInfoBean.getPrice());
                selCouponGoodsEntity.setCount(activityProductInfoBean.getCount());
//            产品ID
                selCouponGoodsEntity.setId(activityProductInfoBean.getId());
                selCouponGoodsEntityList.add(selCouponGoodsEntity);
            }
            String url = Url.BASE_URL + Url.Q_SELF_SHOP_DETAILS_COUPON;
            Map<String, Object> params = new HashMap<>();
            params.put("uid", userId);
            params.put("isApp", 1);
            params.put("orderList", new Gson().toJson(selCouponGoodsEntityList));
            NetLoadUtils.getNetInstance().loadNetDataPost(mAppContext, url, params, new NetLoadListenerHelper() {
                @Override
                public void onSuccess(String result) {
                    smart_communal_refresh.finishRefresh();
                    directMyCouponAdapter.loadMoreComplete();
                    couponList.clear();
                    Gson gson = new Gson();
                    directCouponEntity = gson.fromJson(result, DirectCouponEntity.class);
                    if (directCouponEntity != null) {
                        if (directCouponEntity.getCode().equals(SUCCESS_CODE)) {
                            DirectCouponBean directCouponBean;
                            String colorValue = "";
                            for (int i = 0; i < directCouponEntity.getDirectCouponBeanList().size(); i++) {
                                DirectCouponBean directCoupon = directCouponEntity.getDirectCouponBeanList().get(i);
                                if (i == 0) {
                                    colorValue = directCoupon.getBgColor();
                                }
                                if (directCoupon.getUsable() == 1) {
                                    colorValue = directCoupon.getBgColor();
                                    break;
                                }
                            }
                            directCouponBean = new DirectCouponBean();
                            directCouponBean.setItemType(1);
                            directCouponBean.setBgColor(colorValue);
                            couponList.add(directCouponBean);
                            couponList.addAll(directCouponEntity.getDirectCouponBeanList());
                        } else if (!directCouponEntity.getCode().equals(EMPTY_CODE)) {
                            showToast(DirectCouponGetActivity.this, directCouponEntity.getMsg());
                        }else{
                            directMyCouponAdapter.loadMoreEnd();
                        }
                        directMyCouponAdapter.notifyDataSetChanged();
                    }
                    NetLoadUtils.getNetInstance().showLoadSir(loadService, couponList, directCouponEntity);
                }

                @Override
                public void onNotNetOrException() {
                    smart_communal_refresh.finishRefresh();
                    directMyCouponAdapter.loadMoreComplete();
                    NetLoadUtils.getNetInstance().showLoadSir(loadService, couponList, directCouponEntity);
                }

                @Override
                public void netClose() {
                    showToast(mAppContext, R.string.unConnectedNetwork);
                }

                @Override
                public void onError(Throwable throwable) {
                    showToast(mAppContext, R.string.invalidData);
                }
            });
        } else {
            NetLoadUtils.getNetInstance().showLoadSirLoadFailed(loadService);
        }
    }
}
