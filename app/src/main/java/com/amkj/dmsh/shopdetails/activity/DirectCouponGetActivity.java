package com.amkj.dmsh.shopdetails.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.BaseApplication;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.mine.bean.SelCouponGoodsEntity;
import com.amkj.dmsh.shopdetails.adapter.DirectMyCouponAdapter;
import com.amkj.dmsh.shopdetails.bean.DirectCouponEntity;
import com.amkj.dmsh.shopdetails.bean.DirectCouponEntity.DirectCouponBean;
import com.amkj.dmsh.shopdetails.bean.IndentDiscountsEntity.IndentDiscountsBean.ProductInfoBean.ActivityProductInfoBean;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_TOTAL_COUNT;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_1;

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
    RefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    @BindView(R.id.communal_load)
    View communal_load;
    @BindView(R.id.communal_error)
    View communal_error;
    @BindView(R.id.communal_empty)
    View communal_empty;
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

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_refresh_floating_header;
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
        communal_error.setVisibility(View.GONE);
        communal_empty.setVisibility(View.GONE);
        tv_header_titleAll.setText("选择优惠券");
        header_shared.setVisibility(View.INVISIBLE);
        Intent intent = getIntent();
        settlementGoods = intent.getParcelableArrayListExtra("couponGoods");
        communal_recycler.setLayoutManager(new LinearLayoutManager(DirectCouponGetActivity.this));
        communal_recycler.addItemDecoration(new PinnedHeaderItemDecoration.Builder(-1)
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_twenty_white)
                // 开启绘制分隔线，默认关闭
                .enableDivider(true)
                // 是否关闭标签点击事件，默认开启
                .disableHeaderClick(false)
                // 设置标签和其内部的子控件的监听，若设置点击监听不为null，但是disableHeaderClick(true)的话，还是不会响应点击事件
                .setHeaderClickListener(null)
                .create());
        directMyCouponAdapter = new DirectMyCouponAdapter(couponList,"couponGet");
        communal_recycler.setAdapter(directMyCouponAdapter);
        smart_communal_refresh.setOnRefreshListener((refreshLayout) -> {
                //                滚动距离置0
                scrollY = 0;
                page = 1;
                loadData();

        });
        directMyCouponAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (page * DEFAULT_TOTAL_COUNT <= directMyCouponAdapter.getItemCount()) {
                    page++;
                    loadData();
                } else {
                    directMyCouponAdapter.loadMoreEnd();
                }
            }
        }, communal_recycler);
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
                    if(directCouponBean.getItemType() == TYPE_1||directCouponBean.getUsable()==1){
                        Intent intentDate = new Intent();
                        if(directCouponBean.getItemType() != TYPE_1){
                            intentDate.putExtra("couponId",directCouponBean.getId());
                            intentDate.putExtra("couponAmount",directCouponBean.getAmount());
                        }
                        setResult(RESULT_OK, intentDate);
                        finish();
                    }else{
                        for (DirectCouponBean directCoupon:couponList) {
                            if(directCoupon.getId() == directCouponBean.getId()){
                                directCoupon.setCheckStatus(!directCoupon.isCheckStatus());
                                break;
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
        communal_load.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            finish();
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
        if (settlementGoods != null && userId > 0) {
            selfChoiceCoupon();
        }
    }

    private void selfChoiceCoupon() {
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
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                smart_communal_refresh.finishRefresh();
                directMyCouponAdapter.loadMoreComplete();
                communal_load.setVisibility(View.GONE);
                communal_error.setVisibility(View.GONE);
                couponList.clear();
                Gson gson = new Gson();
                DirectCouponEntity directCouponEntity = gson.fromJson(result, DirectCouponEntity.class);
                if (directCouponEntity != null) {
                    if (directCouponEntity.getCode().equals(SUCCESS_CODE)) {
                        DirectCouponBean directCouponBean;
                        String colorValue = "";
                        for (int i = 0; i < directCouponEntity.getDirectCouponBeanList().size(); i++) {
                            DirectCouponBean directCoupon = directCouponEntity.getDirectCouponBeanList().get(i);
                            if(i==0){
                                colorValue = directCoupon.getBgColor();
                            }
                            if(directCoupon.getUsable()==1){
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
                    }
                    directMyCouponAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (page == 1 && directMyCouponAdapter.getItemCount() < 1) {
                    communal_load.setVisibility(View.GONE);
                    communal_error.setVisibility(View.VISIBLE);
                }
                smart_communal_refresh.finishRefresh();
                directMyCouponAdapter.loadMoreComplete();
                super.onError(ex, isOnCallback);
            }
        });
    }
}
