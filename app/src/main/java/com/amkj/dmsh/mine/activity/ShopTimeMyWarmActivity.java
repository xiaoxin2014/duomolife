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
import com.amkj.dmsh.base.BaseApplication;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.dominant.activity.ShopTimeScrollDetailsActivity;
import com.amkj.dmsh.mine.adapter.ShopTimeMyWarmAdapter;
import com.amkj.dmsh.mine.bean.MineWarmEntity;
import com.amkj.dmsh.mine.bean.MineWarmEntity.MineWarmBean;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.utils.NetWorkUtils;
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

import static com.amkj.dmsh.constant.ConstantMethod.getPersonalInfo;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_TOTAL_COUNT;

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
    private List<MineWarmBean> mineWarmBeanList = new ArrayList();
    private int page = 1;
    private int uid;
    private ShopTimeMyWarmAdapter shopTimeMyWarmAdapter;
    private int scrollY = 0;
    private float screenHeight;
    private boolean isOnPause;
    private String timeWarm;
    @Override
    protected int getContentView() {
        return R.layout.layout_communal_refresh_floating_header;
    }
    @Override
    protected void initViews() {
        getLoginStatus();
        tv_header_titleAll.setText("秒杀提醒");
        tv_header_shared.setCompoundDrawables(null, null, null, null);
        tv_header_shared.setText("提醒时间");
        shopTimeMyWarmAdapter = new ShopTimeMyWarmAdapter(ShopTimeMyWarmActivity.this, mineWarmBeanList);
        communal_recycler.setLayoutManager(new LinearLayoutManager(this));
        communal_recycler.addItemDecoration(new PinnedHeaderItemDecoration.Builder(-1)
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_ten_dp)
                // 开启绘制分隔线，默认关闭
                .enableDivider(true)
                // 是否关闭标签点击事件，默认开启
                .disableHeaderClick(false)
                // 设置标签和其内部的子控件的监听，若设置点击监听不为null，但是disableHeaderClick(true)的话，还是不会响应点击事件
                .setHeaderClickListener(null)
                .create());
        communal_recycler.setAdapter(shopTimeMyWarmAdapter);

        smart_communal_refresh.setOnRefreshListener((refreshLayout) -> {

                //                滚动距离置0
                scrollY = 0;
                page = 1;
                loadData();

        });
        shopTimeMyWarmAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (page * DEFAULT_TOTAL_COUNT <= shopTimeMyWarmAdapter.getItemCount()) {
                    page++;
                    loadData();
                } else {
                    shopTimeMyWarmAdapter.loadMoreEnd();
                }
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
        communal_load.setVisibility(View.VISIBLE);
    }

    private void cancelWarm(int productId) {
        String url = Url.BASE_URL + Url.CANCEL_MINE_WARM;
        Map<String, Object> params = new HashMap<>();
        params.put("m_obj", productId);
        params.put("m_uid", uid);
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                RequestStatus status = gson.fromJson(result, RequestStatus.class);
                if (status != null) {
                    if (status.getCode().equals("01")) {
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
        getWarmTimeShop();
        getWarmTime();
    }

    private void getWarmTimeShop() {
        String url = Url.BASE_URL + Url.MINE_WARM + uid + "&currentPage=" + page;
        if (NetWorkUtils.checkNet(ShopTimeMyWarmActivity.this)) {
            XUtil.Get(url, null, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    smart_communal_refresh.finishRefresh();
                    shopTimeMyWarmAdapter.loadMoreComplete();
                    communal_load.setVisibility(View.GONE);
                    communal_error.setVisibility(View.GONE);
                    if (page == 1) {
                        mineWarmBeanList.clear();
                    }
                    Gson gson = new Gson();
                    MineWarmEntity mineWarmEntity = gson.fromJson(result, MineWarmEntity.class);
                    if (mineWarmEntity != null) {
                        if (mineWarmEntity.getCode().equals("01")) {
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
                        } else if (!mineWarmEntity.getCode().equals("02")) {
                            showToast(ShopTimeMyWarmActivity.this, mineWarmEntity.getMsg());
                        }
                        if (page == 1) {
                            shopTimeMyWarmAdapter.setNewData(mineWarmBeanList);
                        } else {
                            shopTimeMyWarmAdapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    if (page == 1 && shopTimeMyWarmAdapter.getItemCount() < 1) {
                        communal_load.setVisibility(View.GONE);
                        communal_error.setVisibility(View.VISIBLE);
                    }
                    smart_communal_refresh.finishRefresh();
                    shopTimeMyWarmAdapter.loadMoreComplete();
                    super.onError(ex, isOnCallback);
                }
            });
        } else {
            communal_load.setVisibility(View.GONE);
            communal_error.setVisibility(View.VISIBLE);
            smart_communal_refresh.finishRefresh();
            shopTimeMyWarmAdapter.loadMoreComplete();
        }
    }

    private void getWarmTime() {
        String url = Url.BASE_URL + Url.TIME_SHOW_PRO_WARM;
        Map<String, Object> params = new HashMap<>();
        params.put("uid", uid);
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                RequestStatus foreShowBean = gson.fromJson(result, RequestStatus.class);
                if (foreShowBean != null) {
                    if (foreShowBean.getCode().equals("01")) {
                        if (foreShowBean.getResult().isHadRemind()) {
                            timeWarm = String.valueOf(foreShowBean.getResult().getRemindtime());
                        }
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                showToast(ShopTimeMyWarmActivity.this, R.string.unConnectedNetwork);
            }
        });
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (message.type.equals("refreshTimeShop")) {
            page = 1;
            loadData();
        }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ConstantVariable.IS_LOGIN_CODE) {
                getLoginStatus();
                loadData();
            }
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
            page = 1;
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

    @OnClick({R.id.rel_communal_error, R.id.communal_empty})
    void refreshData(View view) {
        communal_load.setVisibility(View.VISIBLE);
        communal_empty.setVisibility(View.GONE);
        communal_error.setVisibility(View.GONE);
        page = 1;
        loadData();
    }

}
