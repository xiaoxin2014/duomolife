package com.amkj.dmsh.mine.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.BaseApplication;
import com.amkj.dmsh.base.NetLoadUtils;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.mine.adapter.MineCollectProAdapter;
import com.amkj.dmsh.mine.bean.CollectProEntity;
import com.amkj.dmsh.mine.bean.CollectProEntity.CollectProBean;
import com.amkj.dmsh.release.dialogutils.AlertSettingBean;
import com.amkj.dmsh.release.dialogutils.AlertView;
import com.amkj.dmsh.release.dialogutils.OnAlertItemClickListener;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

import static com.amkj.dmsh.base.BaseApplication.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_TOTAL_COUNT;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;

;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/8/10
 * class description:我 - 产品收藏
 */
public class MineCollectProductActivity extends BaseActivity implements OnAlertItemClickListener {
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    @BindView(R.id.tv_header_shared)
    TextView header_shared;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.rel_del_shop_car)
    RelativeLayout rel_del_shop_car;
    private int page = 1;
    private int scrollY = 0;
    private float screenHeight;
    private MineCollectProAdapter mineCollectProAdapter;
    private List<CollectProBean> collectProList = new ArrayList();
    //    是否是编辑模式
    private boolean isEditStatus;
    private AlertView dlDelGoods;
    private StringBuffer productIds;

    @Override
    protected int getContentView() {
        return R.layout.activity_collect_product;
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
        rel_del_shop_car.setVisibility(View.GONE);
        header_shared.setCompoundDrawables(null, null, null, null);
        tv_header_titleAll.setText("收藏商品");
        communal_recycler.setLayoutManager(new LinearLayoutManager(this));
        communal_recycler.addItemDecoration(new PinnedHeaderItemDecoration.Builder(-1)
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_gray_f_two_px)
                // 开启绘制分隔线，默认关闭
                .enableDivider(true)
                // 是否关闭标签点击事件，默认开启
                .disableHeaderClick(false)
                // 设置标签和其内部的子控件的监听，若设置点击监听不为null，但是disableHeaderClick(true)的话，还是不会响应点击事件
                .setHeaderClickListener(null)
                .create());
        mineCollectProAdapter = new MineCollectProAdapter(MineCollectProductActivity.this, collectProList);
        communal_recycler.setAdapter(mineCollectProAdapter);
        mineCollectProAdapter.setOnLoadMoreListener(() -> {
            if (page * DEFAULT_TOTAL_COUNT <= collectProList.size()) {
                page++;
                getCollectPro();
            } else {
                mineCollectProAdapter.loadMoreEnd();
            }
        }, communal_recycler);
        mineCollectProAdapter.setOnItemClickListener((adapter, view, position) -> {
            CollectProBean collectProBean = (CollectProBean) view.getTag();
            if (collectProBean != null) {
                Intent intent = new Intent(MineCollectProductActivity.this, ShopScrollDetailsActivity.class);
                intent.putExtra("productId", String.valueOf(collectProBean.getId()));
                startActivity(intent);
            }
        });
        mineCollectProAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            CollectProBean collectProBean = (CollectProBean) view.getTag();
            if (collectProBean != null) {
                switch (view.getId()) {
                    case R.id.tv_mine_col_pro_buy:
                        Intent intent = new Intent(MineCollectProductActivity.this, ShopScrollDetailsActivity.class);
                        intent.putExtra("productId", String.valueOf(collectProBean.getId()));
                        startActivity(intent);
                        break;
                    case R.id.cb_collect_product:
                        CheckBox checkBox = (CheckBox) view;
                        for (int i = 0; i < collectProList.size(); i++) {
                            CollectProBean collectPro = collectProList.get(i);
                            if (collectPro.getId() == collectProBean.getId()) {
                                collectPro.setCheckStatus(checkBox.isChecked());
                                break;
                            }
                        }
                        break;
                }
            }
        });
        smart_communal_refresh.setOnRefreshListener((refreshLayout) -> {
            loadData();
        });
        download_btn_communal.attachToRecyclerView(communal_recycler, null, new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                scrollY += dy;
                if (!recyclerView.canScrollVertically(-1)) {
                    scrollY = 0;
                }
                if (screenHeight == 0) {
                    BaseApplication app = (BaseApplication) getApplication();
                    screenHeight = app.getScreenHeight() * 0.5f;
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
                }
        );
    }

    @Override
    protected void loadData() {
        page = 1;
        getCollectPro();
    }

    @Override
    protected View getLoadView() {
        return smart_communal_refresh;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    private void getCollectPro() {
        String url = Url.BASE_URL + Url.COLLECT_PRO;
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("currentPage", page);
        params.put("count", DEFAULT_TOTAL_COUNT);
        NetLoadUtils.getQyInstance().loadNetDataPost(mAppContext, url, params, new NetLoadUtils.NetLoadListener() {
            @Override
            public void onSuccess(String result) {
                smart_communal_refresh.finishRefresh();
                mineCollectProAdapter.loadMoreComplete();
                Gson gson = new Gson();
                CollectProEntity collectProEntity = gson.fromJson(result, CollectProEntity.class);
                if (collectProEntity != null) {
                    if (collectProEntity.getCode().equals(SUCCESS_CODE)) {
                        if (page == 1) {
                            collectProList.clear();
                        }
                        collectProList.addAll(collectProEntity.getCollectProList());
                        tv_header_titleAll.setText("收藏商品(" + collectProEntity.getCount() + ")");
                    } else if (!collectProEntity.getCode().equals("02")) {
                        showToast(MineCollectProductActivity.this, collectProEntity.getMsg());
                    }
                    mineCollectProAdapter.notifyDataSetChanged();
                }
                setEditStatusVisible();
                NetLoadUtils.getQyInstance().showLoadSirSuccess(loadService);
            }

            @Override
            public void netClose() {
                smart_communal_refresh.finishRefresh();
                mineCollectProAdapter.loadMoreComplete();
                setEditStatusVisible();
                NetLoadUtils.getQyInstance().showLoadSirSuccess(loadService);
            }

            @Override
            public void onError(Throwable throwable) {
                smart_communal_refresh.finishRefresh();
                mineCollectProAdapter.loadMoreComplete();
                setEditStatusVisible();
                NetLoadUtils.getQyInstance().showLoadSirSuccess(loadService);
            }
        });
    }

    private void setEditStatusVisible() {
        if (collectProList.size() > 0) {
            header_shared.setText("编辑");
        } else {
            header_shared.setText("");
        }
        isEditStatus = false;
        rel_del_shop_car.setVisibility(View.GONE);
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

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }

    @OnClick(R.id.tv_header_shared)
    void editFinish(View view) {
        if (collectProList.size() < 1) {
            return;
        }
        isEditStatus = !isEditStatus;
        setEditStatus(isEditStatus);
        if (isEditStatus) {
            header_shared.setText("完成");
            rel_del_shop_car.setVisibility(View.VISIBLE);
        } else {
            header_shared.setText("编辑");
            rel_del_shop_car.setVisibility(View.GONE);
        }
    }

    /**
     * 点击收藏编辑 变更布局
     */
    private void setEditStatus(boolean isEditStatus) {
        for (CollectProBean collectProBean : collectProList) {
            collectProBean.setEditStatus(isEditStatus);
            if (isEditStatus) {
                collectProBean.setCheckStatus(false);
            }
        }
        mineCollectProAdapter.notifyDataSetChanged();
    }

    //    全选 /全不选
    @OnCheckedChanged(R.id.check_box_all_del)
    void allCheckDel(CompoundButton buttonView, boolean isChecked) {
        for (int i = 0; i < collectProList.size(); i++) {
            collectProList.get(i).setCheckStatus(isChecked);
        }
        mineCollectProAdapter.notifyDataSetChanged();
    }

    //    删除
    @OnClick(R.id.tv_shop_car_del)
    void delGoods(View view) {
        productIds = getCheckDelGoods(collectProList);
        if (!TextUtils.isEmpty(productIds)) {
            AlertSettingBean alertSettingBean = new AlertSettingBean();
            AlertSettingBean.AlertData alertData = new AlertSettingBean.AlertData();
            alertData.setCancelStr("取消");
            alertData.setDetermineStr("确定");
            alertData.setFirstDet(true);
            alertData.setMsg("确定删除选中商品");
            alertSettingBean.setStyle(AlertView.Style.Alert);
            alertSettingBean.setAlertData(alertData);
            dlDelGoods = new AlertView(alertSettingBean, MineCollectProductActivity.this, MineCollectProductActivity.this);
            dlDelGoods.setCancelable(true);
            dlDelGoods.show();
        } else {
            showToast(this, "请选择你要删除的商品");
        }
    }

    @Override
    public void onAlertItemClick(Object o, int position) {
        if (o == dlDelGoods && position != AlertView.CANCELPOSITION) {
//            确定删除商品
            delMultiSelGoods(collectProList);
        }
    }

    /**
     * 收藏商品多个取消
     *
     * @param collectProList
     */
    private void delMultiSelGoods(List<CollectProBean> collectProList) {
        if (NetWorkUtils.checkNet(MineCollectProductActivity.this)) {
            if (loadHud != null) {
                loadHud.show();
            }
            String url = Url.BASE_URL + Url.CANCEL_MULTI_COLLECT_PRO;
            Map<String, Object> params = new HashMap<>();
            params.put("uid", userId);
            params.put("ids", getCancelProId(collectProList));
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    if (loadHud != null) {
                        loadHud.dismiss();
                    }
                    Gson gson = new Gson();
                    RequestStatus status = gson.fromJson(result, RequestStatus.class);
                    if (status != null) {
                        if (status.getCode().equals("01")) {
                            showToast(MineCollectProductActivity.this, "已取消收藏");
                            page = 1;
                            loadData();
                        } else {
                            showToast(MineCollectProductActivity.this, status.getMsg());
                        }
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    if (loadHud != null) {
                        loadHud.dismiss();
                    }
                    showToast(MineCollectProductActivity.this, R.string.do_failed);
                    super.onError(ex, isOnCallback);
                }
            });
        } else {
            showToast(MineCollectProductActivity.this, R.string.unConnectedNetwork);
        }
    }

    /**
     * 删除选中商品
     *
     * @param list
     * @return
     */
    private StringBuffer getCheckDelGoods(List<CollectProBean> list) {
        StringBuffer productIds = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isCheckStatus()) {
                if (i > 0) {
                    productIds.append("," + list.get(i).getId());
                } else {
                    productIds.append(list.get(i).getId());
                }
            }
        }
        return productIds;
    }

    public String getCancelProId(List<CollectProBean> collectProList) {
        StringBuilder stringBuilder = new StringBuilder();
        for (CollectProBean collectPro : collectProList) {
            if (collectPro.isCheckStatus()) {
                if (stringBuilder.toString().isEmpty()) {
                    stringBuilder.append(collectPro.getCollectId());
                } else {
                    stringBuilder.append("," + collectPro.getCollectId());
                }
            }
        }
        return stringBuilder.toString();
    }
}
