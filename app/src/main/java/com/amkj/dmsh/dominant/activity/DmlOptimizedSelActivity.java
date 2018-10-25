package com.amkj.dmsh.dominant.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.NetLoadUtils;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.dominant.adapter.DmlOptimizedSelAdapter;
import com.amkj.dmsh.dominant.bean.DmlOptimizedSelEntity;
import com.amkj.dmsh.dominant.bean.DmlOptimizedSelEntity.DmlOptimizedSelBean;
import com.amkj.dmsh.mine.activity.ShopCarActivity;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.amkj.dmsh.utils.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tencent.bugly.beta.tinker.TinkerManager;
import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import q.rorbin.badgeview.Badge;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_TOTAL_COUNT;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;

;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/7/10
 * class description:多么定制列表
 */
public class DmlOptimizedSelActivity extends BaseActivity {
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.iv_img_service)
    ImageView iv_img_service;
    @BindView(R.id.fl_header_service)
    FrameLayout fl_header_service;
    @BindView(R.id.iv_img_share)
    ImageView iv_img_share;
    private int page = 1;
    private int scrollY;
    private float screenHeight;
    private Badge badge;
    private List<DmlOptimizedSelBean> dmlOptimizedSelList = new ArrayList<>();
    private DmlOptimizedSelAdapter dmlOptimizedSelAdapter;
    private DmlOptimizedSelEntity optimizedSelEntity;

    @Override
    protected int getContentView() {
        return R.layout.activity_communal_ql_shop_car;
    }

    @Override
    protected void initViews() {
        iv_img_share.setVisibility(View.GONE);
        iv_img_service.setImageResource(R.drawable.shop_car_gray_icon);
        tv_header_titleAll.setText("");
        communal_recycler.setLayoutManager(new LinearLayoutManager(DmlOptimizedSelActivity.this));
        communal_recycler.addItemDecoration(new PinnedHeaderItemDecoration.Builder(-1)
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_nine_dp_white)
                // 开启绘制分隔线，默认关闭
                .enableDivider(true)
                // 是否关闭标签点击事件，默认开启
                .disableHeaderClick(false)
                // 设置标签和其内部的子控件的监听，若设置点击监听不为null，但是disableHeaderClick(true)的话，还是不会响应点击事件
                .setHeaderClickListener(null)
                .create());
        dmlOptimizedSelAdapter = new DmlOptimizedSelAdapter(DmlOptimizedSelActivity.this, dmlOptimizedSelList);
        communal_recycler.setAdapter(dmlOptimizedSelAdapter);
        dmlOptimizedSelAdapter.setOnItemClickListener((adapter, view, position) -> {
            DmlOptimizedSelBean dmlOptimizedSelBean = (DmlOptimizedSelBean) view.getTag();
            if (dmlOptimizedSelBean != null) {
                Intent intent = new Intent();
                intent.setClass(DmlOptimizedSelActivity.this, DmlOptimizedSelDetailActivity.class);
                intent.putExtra("optimizedId", String.valueOf(dmlOptimizedSelBean.getId()));
                startActivity(intent);
            }
        });
        download_btn_communal.attachToRecyclerView(communal_recycler, null, new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                scrollY += dy;
                if (!recyclerView.canScrollVertically(-1)) {
                    scrollY = 0;
                }
                if (screenHeight == 0) {
                    TinkerBaseApplicationLike app = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
                    screenHeight = app.getScreenHeight();
                }
                if (scrollY >= screenHeight) {
                    download_btn_communal.setVisibility(View.VISIBLE);
                } else {
                    download_btn_communal.setVisibility(View.GONE);
                }
            }
        });
        download_btn_communal.setOnClickListener((v) -> {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) communal_recycler.getLayoutManager();
            int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
            int mVisibleCount = linearLayoutManager.findLastVisibleItemPosition()
                    - linearLayoutManager.findFirstVisibleItemPosition() + 1;
            if (firstVisibleItemPosition > mVisibleCount) {
                communal_recycler.scrollToPosition(mVisibleCount);
            }
            communal_recycler.smoothScrollToPosition(0);
        });
        badge = ConstantMethod.getBadge(DmlOptimizedSelActivity.this, fl_header_service);
    }

    @Override
    protected View getLoadView() {
        return smart_communal_refresh;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    protected void loadData() {
        page = 1;
        getData();
    }

    @Override
    protected void getData() {
        getOptimizedData();
        getCarCount();
    }

    private void getOptimizedData() {
        String url = Url.BASE_URL + Url.Q_DML_OPTIMIZED_LIST;
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", page);
        params.put("showCount", DEFAULT_TOTAL_COUNT);
        if (userId > 0) {
            params.put("uid", userId);
        }
        NetLoadUtils.getQyInstance().loadNetDataPost(DmlOptimizedSelActivity.this, url, params, new NetLoadUtils.NetLoadListener() {
            @Override
            public void onSuccess(String result) {
                smart_communal_refresh.finishRefresh();
                dmlOptimizedSelAdapter.loadMoreComplete();
                if (page == 1) {
                    dmlOptimizedSelList.clear();
                }
                Gson gson = new Gson();
                optimizedSelEntity = gson.fromJson(result, DmlOptimizedSelEntity.class);
                if (optimizedSelEntity != null) {
                    if (optimizedSelEntity.getCode().equals(SUCCESS_CODE)) {
                        tv_header_titleAll.setText(getStrings(optimizedSelEntity.getTitle()));
                        dmlOptimizedSelList.addAll(optimizedSelEntity.getDmlOptimizedSelList());
                    } else if (!optimizedSelEntity.getCode().equals(EMPTY_CODE)) {
                        showToast(DmlOptimizedSelActivity.this, optimizedSelEntity.getMsg());
                    }
                    dmlOptimizedSelAdapter.notifyDataSetChanged();
                }
                NetLoadUtils.getQyInstance().showLoadSir(loadService,dmlOptimizedSelList,optimizedSelEntity);
            }

            @Override
            public void netClose() {
                smart_communal_refresh.finishRefresh();
                dmlOptimizedSelAdapter.loadMoreComplete();
                showToast(DmlOptimizedSelActivity.this, R.string.unConnectedNetwork);
                NetLoadUtils.getQyInstance().showLoadSir(loadService,dmlOptimizedSelList,optimizedSelEntity);
            }

            @Override
            public void onError(Throwable throwable) {
                smart_communal_refresh.finishRefresh();
                dmlOptimizedSelAdapter.loadMoreComplete();
                showToast(DmlOptimizedSelActivity.this, R.string.invalidData);
                NetLoadUtils.getQyInstance().showLoadSir(loadService,dmlOptimizedSelList,optimizedSelEntity);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCarCount();
    }

    private void getCarCount() {
        if (userId > 0) {
            //购物车数量展示
            String url = Url.BASE_URL + Url.Q_QUERY_CAR_COUNT;
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                    if (requestStatus != null) {
                        if (requestStatus.getCode().equals("01")) {
                            int cartNumber = requestStatus.getResult().getCartNumber();
                            badge.setBadgeNumber(cartNumber);
                        } else if (!requestStatus.getCode().equals("02")) {
                            showToast(DmlOptimizedSelActivity.this, requestStatus.getMsg());
                        }
                    }
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        try {
            UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }

    @OnClick(R.id.iv_img_service)
    void skipService(View view) {
        Intent intent = new Intent(DmlOptimizedSelActivity.this, ShopCarActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
