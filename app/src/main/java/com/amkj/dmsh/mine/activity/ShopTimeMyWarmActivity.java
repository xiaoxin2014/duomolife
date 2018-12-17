package com.amkj.dmsh.mine.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dominant.activity.ShopTimeScrollDetailsActivity;
import com.amkj.dmsh.mine.adapter.ShopTimeMyWarmAdapter;
import com.amkj.dmsh.mine.bean.MineWarmEntity;
import com.amkj.dmsh.mine.bean.MineWarmEntity.MineWarmBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
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

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.MINE_WARM;
import static com.amkj.dmsh.constant.Url.TIME_SHOW_PRO_WARM;

;
;

/**
 * Created by atd48 on 2016/10/19.
 * 我的提醒列表
 */
public class ShopTimeMyWarmActivity extends BaseActivity {
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tv_header_shared)
    TextView tv_header_shared;
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    private List<MineWarmBean> mineWarmBeanList = new ArrayList();
    private int page = 1;
    private ShopTimeMyWarmAdapter shopTimeMyWarmAdapter;
    private int scrollY = 0;
    private float screenHeight;
    private boolean isOnPause;
    private String timeWarm;
    private MineWarmEntity mineWarmEntity;

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_refresh_floating_header;
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
        tv_header_titleAll.setText("秒杀提醒");
        tv_header_shared.setCompoundDrawables(null, null, null, null);
        tv_header_shared.setText("提醒时间");
        shopTimeMyWarmAdapter = new ShopTimeMyWarmAdapter(ShopTimeMyWarmActivity.this, mineWarmBeanList);
        communal_recycler.setLayoutManager(new LinearLayoutManager(this));
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_ten_dp)


                .create());
        communal_recycler.setAdapter(shopTimeMyWarmAdapter);

        smart_communal_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                scrollY = 0;
                loadData();
            }
        });
        shopTimeMyWarmAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                getWarmTimeShop();
            }
        }, communal_recycler);

        shopTimeMyWarmAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MineWarmBean mineWarmBean = (MineWarmBean) view.getTag();
                if (mineWarmBean != null && mineWarmBean.getStatus() != 3 && mineWarmBean.getStatus() != 5) {
                    Intent intent = new Intent(ShopTimeMyWarmActivity.this, ShopTimeScrollDetailsActivity.class);
                    intent.putExtra("productId", String.valueOf(mineWarmBean.getId()));
                    startActivity(intent);
                } else {
                    showToast(ShopTimeMyWarmActivity.this, "商品已失效");
                }
            }
        });
        shopTimeMyWarmAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            MineWarmBean mineWarmBean = (MineWarmBean) view.getTag();
            if (mineWarmBean != null) {
                if (mineWarmBean.getStatus() == 2) {
                    Intent intent = new Intent(ShopTimeMyWarmActivity.this, ShopTimeScrollDetailsActivity.class);
                    intent.putExtra("productId", String.valueOf(mineWarmBean.getId()));
                    startActivity(intent);
                } else {
//                    删除
                    page = 1;
                    cancelWarm(mineWarmBean.getId());
                }
            }
        });
        shopTimeMyWarmAdapter.setEmptyView(R.layout.layout_shop_time_empty, (ViewGroup) communal_recycler.getParent());
        download_btn_communal.attachToRecyclerView(communal_recycler, null, new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!recyclerView.canScrollVertically(-1)) {
                    scrollY = 0;
                }
                scrollY += dy;
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
    }

    private void cancelWarm(int productId) {
        String url = Url.BASE_URL + Url.CANCEL_MINE_WARM;
        Map<String, Object> params = new HashMap<>();
        params.put("m_obj", productId);
        params.put("m_uid", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this,url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                RequestStatus status = gson.fromJson(result, RequestStatus.class);
                if (status != null) {
                    if (status.getCode().equals(SUCCESS_CODE)) {
                        loadData();
                    } else {
                        showToast(ShopTimeMyWarmActivity.this, status.getMsg());
                    }
                }
            }
        });
    }

    @Override
    protected void loadData() {
        page = 1;
        getWarmTimeShop();
        getWarmTime();
    }

    @Override
    protected View getLoadView() {
        return smart_communal_refresh;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    private void getWarmTimeShop() {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("currentPage", page);
        NetLoadUtils.getNetInstance().loadNetDataPost(mAppContext, MINE_WARM, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                smart_communal_refresh.finishRefresh();
                shopTimeMyWarmAdapter.loadMoreComplete();
                if (page == 1) {
                    mineWarmBeanList.clear();
                }
                Gson gson = new Gson();
                mineWarmEntity = gson.fromJson(result, MineWarmEntity.class);
                if (mineWarmEntity != null) {
                    if (mineWarmEntity.getCode().equals(SUCCESS_CODE)) {
                        if (!TextUtils.isEmpty(mineWarmEntity.getCurrentTime())) {
                            for (int i = 0; i < mineWarmEntity.getMineWarmList().size(); i++) {
                                MineWarmBean mineWarmBean = mineWarmEntity.getMineWarmList().get(i);
                                mineWarmBean.setCurrentTime(mineWarmEntity.getCurrentTime());
                                mineWarmBeanList.add(mineWarmBean);
                            }
                        } else {
                            mineWarmBeanList.addAll(mineWarmEntity.getMineWarmList());
                        }
                        tv_header_titleAll.setText("秒杀提醒(" + mineWarmEntity.getCount() + ")");
                    } else if (!mineWarmEntity.getCode().equals(EMPTY_CODE)) {
                        showToast(ShopTimeMyWarmActivity.this, mineWarmEntity.getMsg());
                    }else{
                        shopTimeMyWarmAdapter.loadMoreEnd();
                    }
                    shopTimeMyWarmAdapter.notifyDataSetChanged();
                }
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mineWarmBeanList, mineWarmEntity);
            }

            @Override
            public void onNotNetOrException() {
                smart_communal_refresh.finishRefresh();
                shopTimeMyWarmAdapter.loadMoreEnd(true);
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mineWarmBeanList, mineWarmEntity);
            }

            @Override
            public void netClose() {
                showToast(ShopTimeMyWarmActivity.this,R.string.unConnectedNetwork);
            }
        });
    }

    private void getWarmTime() {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this,TIME_SHOW_PRO_WARM,params,new NetLoadListenerHelper(){
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                RequestStatus foreShowBean = gson.fromJson(result, RequestStatus.class);
                if (foreShowBean != null) {
                    if (foreShowBean.getCode().equals(SUCCESS_CODE)) {
                        if (foreShowBean.getResult().isHadRemind()) {
                            timeWarm = String.valueOf(foreShowBean.getResult().getRemindtime());
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (message.type.equals("refreshTimeShop")) {
            loadData();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IS_LOGIN_CODE) {
            loadData();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isOnPause = true;
    }

    @Override
    protected void onResume() {
        if (isOnPause) {
            loadData();
            isOnPause = false;
        }
        super.onResume();
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }

    @OnClick(R.id.tv_header_shared)
    void setWarmTime(View view) {
        Intent intent = new Intent(ShopTimeMyWarmActivity.this, MessageWarmActivity.class);
        intent.putExtra("warmTime", !TextUtils.isEmpty(timeWarm) ? timeWarm : "3");
        startActivity(intent);
    }
}
