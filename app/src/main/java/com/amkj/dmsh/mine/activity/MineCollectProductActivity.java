package com.amkj.dmsh.mine.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.mine.adapter.MineCollectProAdapter;
import com.amkj.dmsh.mine.bean.CollectProEntity;
import com.amkj.dmsh.mine.bean.CollectProEntity.CollectProBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.utils.alertdialog.AlertDialogHelper;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tencent.bugly.beta.tinker.TinkerManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.showToastRequestMsg;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TWENTY;
import static com.amkj.dmsh.constant.Url.CANCEL_MULTI_COLLECT_PRO;
import static com.amkj.dmsh.constant.Url.COLLECT_PRO;

;
;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/8/10
 * class description:我 - 产品收藏
 */
public class MineCollectProductActivity extends BaseActivity {
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
    private StringBuffer productIds;
    private CollectProEntity collectProEntity;
    private AlertDialogHelper delGoodsDialogHelper;

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
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_gray_f_two_px)


                .create());
        mineCollectProAdapter = new MineCollectProAdapter(MineCollectProductActivity.this, collectProList);
        communal_recycler.setAdapter(mineCollectProAdapter);
        mineCollectProAdapter.setOnLoadMoreListener(() -> {
            page++;
            getCollectPro();
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
        smart_communal_refresh.setOnRefreshListener(refreshLayout -> loadData());
        download_btn_communal.attachToRecyclerView(communal_recycler, null, new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                scrollY += dy;
                if (!recyclerView.canScrollVertically(-1)) {
                    scrollY = 0;
                }
                if (screenHeight == 0) {
                    TinkerBaseApplicationLike app = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
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
        if (userId > 0) {
            page = 1;
            getCollectPro();
        }
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
        if(userId<1){
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("currentPage", page);
        params.put("count", TOTAL_COUNT_TWENTY);
        NetLoadUtils.getNetInstance().loadNetDataPost(mAppContext, COLLECT_PRO, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                smart_communal_refresh.finishRefresh();
                mineCollectProAdapter.loadMoreComplete();
                if (page == 1) {
                    collectProList.clear();
                }
                Gson gson = new Gson();
                collectProEntity = gson.fromJson(result, CollectProEntity.class);
                if (collectProEntity != null) {
                    if (collectProEntity.getCode().equals(SUCCESS_CODE)) {
                        collectProList.addAll(collectProEntity.getCollectProList());
                        tv_header_titleAll.setText("收藏商品(" + collectProEntity.getCount() + ")");
                    } else if (collectProEntity.getCode().equals(EMPTY_CODE)) {
                        mineCollectProAdapter.loadMoreEnd();
                    }else{
                        showToast(MineCollectProductActivity.this, collectProEntity.getMsg());
                    }
                    mineCollectProAdapter.notifyDataSetChanged();
                }
                setEditStatusVisible();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, collectProList, collectProEntity);
            }

            @Override
            public void onNotNetOrException() {
                smart_communal_refresh.finishRefresh();
                mineCollectProAdapter.loadMoreEnd(true);
                setEditStatusVisible();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, collectProList, collectProEntity);
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
            if (delGoodsDialogHelper == null) {
                delGoodsDialogHelper = new AlertDialogHelper(MineCollectProductActivity.this);
                delGoodsDialogHelper.setTitleVisibility(View.GONE).setMsgTextGravity(Gravity.CENTER)
                        .setMsg("确定删除选中商品").setCancelText("取消").setConfirmText("确定")
                        .setCancelTextColor(getResources().getColor(R.color.text_login_gray_s));
                delGoodsDialogHelper.setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                    @Override
                    public void confirm() {
                        //            确定删除商品
                        delMultiSelGoods(collectProList);
                    }

                    @Override
                    public void cancel() {
                    }
                });
            }
            delGoodsDialogHelper.show();
        } else {
            showToast(this, "请选择你要删除的商品");
        }
    }

    /**
     * 收藏商品多个取消
     *
     * @param collectProList
     */
    private void delMultiSelGoods(List<CollectProBean> collectProList) {
        if (loadHud != null) {
            loadHud.show();
        }
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("ids", getCancelProId(collectProList));
        NetLoadUtils.getNetInstance().loadNetDataPost(this,CANCEL_MULTI_COLLECT_PRO,params,new NetLoadListenerHelper(){
            @Override
            public void onSuccess(String result) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                Gson gson = new Gson();
                RequestStatus status = gson.fromJson(result, RequestStatus.class);
                if (status != null) {
                    if (status.getCode().equals(SUCCESS_CODE)) {
                        showToast(MineCollectProductActivity.this, "已取消收藏");
                        page = 1;
                        loadData();
                    } else {
                        showToastRequestMsg(MineCollectProductActivity.this, status);
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(MineCollectProductActivity.this, R.string.do_failed);
            }

            @Override
            public void netClose() {
                showToast(MineCollectProductActivity.this, R.string.unConnectedNetwork);
            }
        });
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (delGoodsDialogHelper != null && delGoodsDialogHelper.getAlertDialog() != null
                && delGoodsDialogHelper.getAlertDialog().isShowing()) {
            delGoodsDialogHelper.dismiss();
        }
    }
}
