package com.amkj.dmsh.mine.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.mine.adapter.MineBrowsingHistoryAdapter;
import com.amkj.dmsh.mine.bean.MineBrowsHistoryEntity;
import com.amkj.dmsh.mine.bean.MineBrowsHistoryEntity.MineBrowsHistoryBean;
import com.amkj.dmsh.mine.bean.MineBrowsHistoryEntity.MineBrowsHistoryBean.GoodsInfoListBean;
import com.amkj.dmsh.netloadpage.NetLoadCallback;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.OffsetLinearLayoutManager;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tencent.bugly.beta.tinker.TinkerManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_1;
import static com.amkj.dmsh.constant.Url.MINE_BROWSING_HISTORY;
import static com.amkj.dmsh.mine.activity.MineProductBrowsingHistoryActivity.SelectionStatusTypeEnum.AUTO_SELECTION;
import static com.amkj.dmsh.mine.activity.MineProductBrowsingHistoryActivity.SelectionStatusTypeEnum.MANUAL_SELECTION;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2019/1/10
 * version 3.2.0
 * class description:商品浏览记录
 */
public class MineProductBrowsingHistoryActivity extends BaseActivity {
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
    TextView tv_header_shared;
    @BindView(R.id.rel_del_shop_car)
    RelativeLayout rel_del_shop_car;
    // 全选 全不选
    @BindView(R.id.check_box_all_del)
    CheckBox check_box_all_del;
    private MineBrowsingHistoryAdapter mineBrowsingHistoryAdapter;
    private List<MultiItemEntity> mineBrowsHistoryBeanList = new ArrayList<>();
    private List<MineBrowsHistoryBean> parentBrowsHistoryBeanList = new ArrayList<>();
    private int page = 1;
    //    是否编辑状态
    private boolean isEditStatus = false;
    //    为空 代表未手动点击全选 注!!!不要默认赋值(涉及到多处判空选择)
    private SelectionStatusTypeEnum selectionStatusTypeEnum;
    private MineBrowsHistoryEntity mineBrowsHistoryEntity;

    @Override
    protected int getContentView() {
        return R.layout.activity_mine_brows_history;
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
        tv_header_shared.setCompoundDrawables(null, null, null, null);
        setEditStatus(isEditStatus);
        smart_communal_refresh.setOnRefreshListener(refreshLayout -> loadData());
        OffsetLinearLayoutManager offsetLinearLayoutManager = new OffsetLinearLayoutManager(MineProductBrowsingHistoryActivity.this);
        communal_recycler.setLayoutManager(offsetLinearLayoutManager);
        mineBrowsingHistoryAdapter = new MineBrowsingHistoryAdapter(MineProductBrowsingHistoryActivity.this, mineBrowsHistoryBeanList);
        communal_recycler.setAdapter(mineBrowsingHistoryAdapter);
        mineBrowsingHistoryAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
        mineBrowsingHistoryAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                MultiItemEntity multiItemEntity = (MultiItemEntity) view.getTag();
                if (multiItemEntity != null) {
                    switch (view.getId()) {
                        case R.id.cb_browse_history_header:
                            MineBrowsHistoryBean mineBrowsHistoryBean = (MineBrowsHistoryBean) multiItemEntity;
                            mineBrowsHistoryBean.setSelectStatus(!mineBrowsHistoryBean.isSelectStatus());
                            for (GoodsInfoListBean goodsInfoListBean : mineBrowsHistoryBean.getSubItems()) {
                                goodsInfoListBean.setSelectStatus(mineBrowsHistoryBean.isSelectStatus());
                            }
                            setSelectionType(mineBrowsHistoryBean.isSelectStatus());
                            adapter.notifyDataSetChanged();
                            break;
                        case R.id.cb_browse_history_product:
                            GoodsInfoListBean goodsInfoListBean = (GoodsInfoListBean) multiItemEntity;
                            int parentPosition = adapter.getParentPosition(goodsInfoListBean);
                            boolean currentChildStatus = !goodsInfoListBean.isSelectStatus();
                            goodsInfoListBean.setSelectStatus(currentChildStatus);
                            if (parentPosition != -1) {
                                /**
                                 * 更改父类状态
                                 * @子类为true 如果子类全部为true, 更改父类为选中状态
                                 * @子类为false 父类如是选中状态，取消选中状态
                                 */
                                MineBrowsHistoryBean parentMineBrowsHistoryBean = (MineBrowsHistoryBean) mineBrowsHistoryBeanList.get(parentPosition);
                                if (currentChildStatus) {
                                    boolean parentStatus = true;
                                    for (GoodsInfoListBean goodsChildInfoListBean : parentMineBrowsHistoryBean.getSubItems()) {
                                        if (!goodsChildInfoListBean.isSelectStatus()) {
                                            parentStatus = false;
                                            break;
                                        }
                                    }
                                    parentMineBrowsHistoryBean.setSelectStatus(parentStatus);
                                } else {
                                    if (parentMineBrowsHistoryBean.isSelectStatus()) {
                                        parentMineBrowsHistoryBean.setSelectStatus(false);
                                    }
                                }
                            }
                            setSelectionType(currentChildStatus);
                            adapter.notifyDataSetChanged();
                            break;
                    }
                }
            }
        });
        mineBrowsingHistoryAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                getMineBrowsingHistory();
            }
        }, communal_recycler);
        TinkerBaseApplicationLike tinkerBaseApplicationLike = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
        int screenHeight = tinkerBaseApplicationLike.getScreenHeight();
        communal_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                OffsetLinearLayoutManager layoutManager = (OffsetLinearLayoutManager) recyclerView.getLayoutManager();
                int scrollY = layoutManager.computeVerticalScrollOffset();
                if (scrollY > screenHeight * 1.5) {
                    if (download_btn_communal.getVisibility() == GONE) {
                        download_btn_communal.setVisibility(VISIBLE);
                        download_btn_communal.hide(false);
                    }
                    if (!download_btn_communal.isVisible()) {
                        download_btn_communal.show(true);
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
                download_btn_communal.hide();
                linearLayoutManager.scrollToPositionWithOffset(0, 0);
            }
        });
    }

    /**
     * 是否编辑状态
     *
     * @param isEditStatus
     */
    private void setEditStatus(boolean isEditStatus) {
        this.isEditStatus = isEditStatus;
        tv_header_shared.setSelected(isEditStatus);
        tv_header_shared.setText(isEditStatus ? "完成" : "编辑");
        rel_del_shop_car.setVisibility(isEditStatus ? VISIBLE : GONE);
        check_box_all_del.setChecked(false);
        setProductSelectStatus(check_box_all_del.isChecked());
    }

    /**
     * 设置商品选择状态
     *
     * @param isProductSelectStatus
     */
    private void setProductSelectStatus(boolean isProductSelectStatus) {
        if (mineBrowsingHistoryAdapter != null) {
            for (MultiItemEntity multiItemEntity : mineBrowsHistoryBeanList) {
                if (multiItemEntity instanceof MineBrowsHistoryBean) {
                    MineBrowsHistoryBean mineBrowsHistoryBean = (MineBrowsHistoryBean) multiItemEntity;
                    mineBrowsHistoryBean.setEditStatus(isEditStatus);
                    mineBrowsHistoryBean.setSelectStatus(isProductSelectStatus);
                } else if (multiItemEntity instanceof GoodsInfoListBean) {
                    GoodsInfoListBean goodsInfoListBean = (GoodsInfoListBean) multiItemEntity;
                    goodsInfoListBean.setEditStatus(isEditStatus);
                    goodsInfoListBean.setSelectStatus(isProductSelectStatus);
                }
            }
            mineBrowsingHistoryAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 设置底栏全选按钮--选择类型
     *
     * @param selectionStatus true 为选中 false 为取消选择
     */
    private void setSelectionType(boolean selectionStatus) {
        if (selectionStatusTypeEnum != null) {
            selectionStatusTypeEnum = AUTO_SELECTION;
            if (check_box_all_del.isChecked() && !selectionStatus) {
                check_box_all_del.setChecked(false);
            } else if (!check_box_all_del.isChecked() && selectionStatus) {
                boolean isAllSelection = true;
                for (MineBrowsHistoryBean mineBrowsHistoryBean : parentBrowsHistoryBeanList) {
                    if (!mineBrowsHistoryBean.isSelectStatus()) {
                        isAllSelection = false;
                        break;
                    }
                }
                check_box_all_del.setChecked(isAllSelection);
            }
        }
    }

    @Override
    protected void loadData() {
        page = 1;
        getMineBrowsingHistory();
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    protected View getLoadView() {
        return smart_communal_refresh;
    }

    /**
     * 获取我的足迹数据
     */
    private void getMineBrowsingHistory() {
        if (userId < 1) {
            NetLoadUtils.getNetInstance().showLoadSir(loadService, mineBrowsHistoryBeanList, mineBrowsHistoryEntity);
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("currentPage", page);
//        params.put("showCount", TOTAL_COUNT_TWENTY);
        params.put("showCount", 2);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, MINE_BROWSING_HISTORY, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mineBrowsingHistoryAdapter.loadMoreComplete();
                if (page == 1) {
                    smart_communal_refresh.finishRefresh();
                    mineBrowsHistoryBeanList.clear();
                    parentBrowsHistoryBeanList.clear();
                }
                Gson gson = new Gson();
                mineBrowsHistoryEntity = gson.fromJson(result, MineBrowsHistoryEntity.class);
                if (mineBrowsHistoryEntity != null) {
                    if (mineBrowsHistoryEntity.getCode().equals(SUCCESS_CODE)) {
                        for (MineBrowsHistoryBean mineBrowsHistoryBean : mineBrowsHistoryEntity.getMineBrowsHistoryList()) {
                            mineBrowsHistoryBean.setItemType(TYPE_1);
                            mineBrowsHistoryBean.setSubItems(mineBrowsHistoryBean.getGoodsInfoList());
                            /**
                             * 是否为编辑状态
                             */
                            if (isEditStatus) {
                                mineBrowsHistoryBean.setSelectStatus(isEditStatus);
                                for (GoodsInfoListBean goodsInfoListBean : mineBrowsHistoryBean.getGoodsInfoList()) {
                                    /**
                                     * 如果已手动点击为全选状态，上拉加载的数据也应该为选中状态
                                     */
                                    if (selectionStatusTypeEnum != null) {
                                        goodsInfoListBean.setSelectStatus(true);
                                    }
                                    goodsInfoListBean.setSelectStatus(isEditStatus);
                                }
                            }
                        }
                        if (mineBrowsHistoryBeanList.size() > 0) {

                        }
                        mineBrowsHistoryBeanList.addAll(mineBrowsHistoryEntity.getMineBrowsHistoryList());
                        parentBrowsHistoryBeanList.addAll(mineBrowsHistoryEntity.getMineBrowsHistoryList());
                    } else if (!mineBrowsHistoryEntity.getCode().equals(EMPTY_CODE)) {
                        showToast(MineProductBrowsingHistoryActivity.this, mineBrowsHistoryEntity.getMsg());
                    }
                    mineBrowsingHistoryAdapter.expandAll();
                    mineBrowsingHistoryAdapter.notifyDataSetChanged();
                }
                if (mineBrowsHistoryEntity == null ||
                        mineBrowsHistoryEntity.getMineBrowsHistoryList() == null ||
                        mineBrowsHistoryEntity.getMineBrowsHistoryList().size() < 1) {
                    mineBrowsingHistoryAdapter.loadMoreEnd();
                }
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mineBrowsHistoryBeanList, mineBrowsHistoryEntity);
            }

            @Override
            public void onNotNetOrException() {
                mineBrowsingHistoryAdapter.loadMoreEnd(true);
                smart_communal_refresh.finishRefresh();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mineBrowsHistoryBeanList, mineBrowsHistoryEntity);
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(MineProductBrowsingHistoryActivity.this, R.string.invalidData);
            }

            @Override
            public void netClose() {
                showToast(MineProductBrowsingHistoryActivity.this, R.string.unConnectedNetwork);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            if (requestCode == IS_LOGIN_CODE) {
                finish();
            }
            return;
        }
        if (requestCode == IS_LOGIN_CODE) {
            if (loadService.getCurrentCallback() != NetLoadCallback.class) {
                NetLoadUtils.getNetInstance().showLoadSirLoading(loadService);
            }
            loadData();
        }
    }

    @OnClick(R.id.tv_life_back)
    void goBack() {
        finish();
    }

    @OnClick(R.id.tv_header_shared)
    void changeMode(View view) {
        setEditStatus(!isEditStatus);
    }

    //    删除
    @OnClick(R.id.tv_shop_car_del)
    void delGoods() {

    }

    //    全选 /全不选
    @OnClick(R.id.check_box_all_del)
    void allCheckBuy(CheckBox checkBox) {
        if (isEditStatus) {
            boolean checked = checkBox.isChecked();
            checkBox.setChecked(checked);
            setProductSelectStatus(checked);
            if (checked) {
                selectionStatusTypeEnum = MANUAL_SELECTION;
            } else {
                selectionStatusTypeEnum = null;
            }
        }
    }

    enum SelectionStatusTypeEnum {
        AUTO_SELECTION,//自动选择
        MANUAL_SELECTION//手动选择
    }
}
