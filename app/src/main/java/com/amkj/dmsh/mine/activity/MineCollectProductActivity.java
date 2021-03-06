package com.amkj.dmsh.mine.activity;

import android.content.Intent;
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
import com.amkj.dmsh.views.alertdialog.AlertDialogHelper;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tencent.bugly.beta.tinker.TinkerManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
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

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/8/10
 * class description:??? - ????????????
 */
public class MineCollectProductActivity extends BaseActivity {
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    ???????????????
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
    //    ?????????????????????
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
        tv_header_titleAll.setText("????????????");
        communal_recycler.setLayoutManager(new LinearLayoutManager(this));
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // ?????????????????????ID
                .setDividerId(R.drawable.item_divider_gray_f_one_px)


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
    public View getLoadView() {
        return smart_communal_refresh;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    protected String getEmptyText() {
        return "????????????????????????\n???????????????";
    }

    private void getCollectPro() {
        if (userId < 1) {
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("currentPage", page);
        params.put("count", TOTAL_COUNT_TWENTY);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, COLLECT_PRO, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                smart_communal_refresh.finishRefresh();
                mineCollectProAdapter.loadMoreComplete();
                if (page == 1) {
                    collectProList.clear();
                }

                collectProEntity = GsonUtils.fromJson(result, CollectProEntity.class);
                if (collectProEntity != null) {
                    if (collectProEntity.getCode().equals(SUCCESS_CODE)) {
                        collectProList.addAll(collectProEntity.getCollectProList());
                        tv_header_titleAll.setText("????????????(" + collectProEntity.getCount() + ")");
                    } else if (collectProEntity.getCode().equals(EMPTY_CODE)) {
                        mineCollectProAdapter.loadMoreEnd();
                    } else {
                        showToast(collectProEntity.getMsg());
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
            header_shared.setText("??????");
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
            header_shared.setText("??????");
            rel_del_shop_car.setVisibility(View.VISIBLE);
        } else {
            header_shared.setText("??????");
            rel_del_shop_car.setVisibility(View.GONE);
        }
    }

    /**
     * ?????????????????? ????????????
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

    //    ?????? /?????????
    @OnCheckedChanged(R.id.check_box_all_del)
    void allCheckDel(CompoundButton buttonView, boolean isChecked) {
        for (int i = 0; i < collectProList.size(); i++) {
            collectProList.get(i).setCheckStatus(isChecked);
        }
        mineCollectProAdapter.notifyDataSetChanged();
    }

    //    ??????
    @OnClick(R.id.tv_shop_car_del)
    void delGoods(View view) {
        productIds = getCheckDelGoods(collectProList);
        if (!TextUtils.isEmpty(productIds)) {
            if (delGoodsDialogHelper == null) {
                delGoodsDialogHelper = new AlertDialogHelper(MineCollectProductActivity.this);
                delGoodsDialogHelper.setTitleVisibility(View.GONE).setMsgTextGravity(Gravity.CENTER)
                        .setMsg("????????????????????????").setCancelText("??????").setConfirmText("??????")
                        .setCancelTextColor(getResources().getColor(R.color.text_login_gray_s));
                delGoodsDialogHelper.setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                    @Override
                    public void confirm() {
                        //            ??????????????????
                        delMultiSelGoods(collectProList);
                    }

                    @Override
                    public void cancel() {
                    }
                });
            }
            delGoodsDialogHelper.show();
        } else {
            showToast("??????????????????????????????");
        }
    }

    /**
     * ????????????????????????
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
        NetLoadUtils.getNetInstance().loadNetDataPost(this, CANCEL_MULTI_COLLECT_PRO, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }

                RequestStatus status = GsonUtils.fromJson(result, RequestStatus.class);
                if (status != null) {
                    if (status.getCode().equals(SUCCESS_CODE)) {
                        showToast("???????????????");
                        page = 1;
                        loadData();
                    } else {
                        showToastRequestMsg(status);
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
                showToast(R.string.do_failed);
            }
        });
    }

    /**
     * ??????????????????
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
