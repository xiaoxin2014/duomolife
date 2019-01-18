package com.amkj.dmsh.mine.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.amkj.dmsh.mine.bean.MineBrowsHistoryTimeShaftEntity;
import com.amkj.dmsh.mine.enumutils.SelectionStatusTypeEnum;
import com.amkj.dmsh.netloadpage.NetLoadCallback;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.OffsetLinearLayoutManager;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
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
import static com.amkj.dmsh.constant.ConstantMethod.isSameTimeDay;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_1;
import static com.amkj.dmsh.constant.Url.MINE_BROWSING_HISTORY;
import static com.amkj.dmsh.constant.Url.MINE_BROWSING_HISTORY_TIME_SHAFT;
import static com.amkj.dmsh.mine.enumutils.SelectionStatusTypeEnum.AUTO_SELECTION;
import static com.amkj.dmsh.mine.enumutils.SelectionStatusTypeEnum.MANUAL_SELECTION;


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
    private String currentDay;
    private boolean isCleanData;
    private int totalCurrentDayPage;
    private boolean hasNextDay = true;
    //    足迹天数
    private List<String> historyTimeShaftList = new ArrayList<>();

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
                            boolean currentStatus = !mineBrowsHistoryBean.isSelectStatus();
                            mineBrowsHistoryBean.setSelectStatus(currentStatus);
                            if (currentStatus) {
                                mineBrowsHistoryBean.setStatusTypeEnum(MANUAL_SELECTION);
                            } else {
                                mineBrowsHistoryBean.setStatusTypeEnum(null);
                            }
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
                                        parentMineBrowsHistoryBean.setStatusTypeEnum(AUTO_SELECTION);
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
                loadNextDayData();
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
        smart_communal_refresh.setEnableRefresh(!isEditStatus);
        setDefaultSetting();
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
        setDefaultSetting();
        page = 1;
        isCleanData = true;
        hasNextDay = true;
        currentDay = "";
        getMineBrowsingHistoryTimeShaft();
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
     * 获取我的足迹时间轴
     */
    private void getMineBrowsingHistoryTimeShaft() {
        if (userId < 1) {
            NetLoadUtils.getNetInstance().showLoadSir(loadService, mineBrowsHistoryBeanList, mineBrowsHistoryEntity);
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, MINE_BROWSING_HISTORY_TIME_SHAFT, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                if (smart_communal_refresh.getState().equals(RefreshState.Refreshing)) {
                    smart_communal_refresh.finishRefresh();
                }
                MineBrowsHistoryTimeShaftEntity mineBrowsHistoryTimeShaftEntity = new Gson().fromJson(result, MineBrowsHistoryTimeShaftEntity.class);
                if (mineBrowsHistoryTimeShaftEntity != null
                        && SUCCESS_CODE.equals(mineBrowsHistoryTimeShaftEntity.getCode())
                        && mineBrowsHistoryTimeShaftEntity.getHistoryTimeShaftList() != null &&
                        mineBrowsHistoryTimeShaftEntity.getHistoryTimeShaftList().size() > 0) {
                    page = 1;
                    hasNextDay = true;
                    historyTimeShaftList.clear();
                    historyTimeShaftList.addAll(mineBrowsHistoryTimeShaftEntity.getHistoryTimeShaftList());
                    currentDay = mineBrowsHistoryTimeShaftEntity.getHistoryTimeShaftList().get(0);
                    getMineBrowsingHistory();
                } else {
                    setDefaultEmptyUI();
                }
            }

            @Override
            public void onNotNetOrException() {
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mineBrowsHistoryBeanList, mineBrowsHistoryEntity);
            }
        });
    }

    /**
     * 获取我的足迹数据
     */
    private void getMineBrowsingHistory() {
        if (TextUtils.isEmpty(currentDay)) {
            showToast(MineProductBrowsingHistoryActivity.this, "数据时间格式错误！");
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("currentPage", page);
//        params.put("showCount", TOTAL_COUNT_TWENTY);
        params.put("showCount", 2);
        params.put("date", currentDay);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, MINE_BROWSING_HISTORY, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mineBrowsingHistoryAdapter.loadMoreComplete();
                smart_communal_refresh.finishRefresh();
                if (isCleanData && page == 1) {
                    mineBrowsHistoryBeanList.clear();
                    parentBrowsHistoryBeanList.clear();
                }
                Gson gson = new Gson();
                mineBrowsHistoryEntity = gson.fromJson(result, MineBrowsHistoryEntity.class);
                if (mineBrowsHistoryEntity == null ||
                        mineBrowsHistoryEntity.getMineBrowsHistoryList() == null ||
                        mineBrowsHistoryEntity.getMineBrowsHistoryList().size() < 1) {
                    mineBrowsingHistoryAdapter.loadMoreEnd();
                    hasNextDay = false;
                }
                if (mineBrowsHistoryEntity != null) {
                    if (mineBrowsHistoryEntity.getCode().equals(SUCCESS_CODE)) {
                        /**
                         * 当前日期总页码
                         */
                        totalCurrentDayPage = mineBrowsHistoryEntity.getTotalPage();

                        for (MineBrowsHistoryBean mineBrowsHistoryBean : mineBrowsHistoryEntity.getMineBrowsHistoryList()) {
                            mineBrowsHistoryBean.setItemType(TYPE_1);
                            mineBrowsHistoryBean.setSubItems(mineBrowsHistoryBean.getGoodsInfoList());
                            /**
                             * 是否为编辑状态
                             */
                            if (isEditStatus) {
                                mineBrowsHistoryBean.setEditStatus(isEditStatus);
                                if (selectionStatusTypeEnum != null) {
                                    mineBrowsHistoryBean.setSelectStatus(true);
                                }
                                for (GoodsInfoListBean goodsInfoListBean : mineBrowsHistoryBean.getGoodsInfoList()) {
                                    /**
                                     * 如果已手动点击为全选状态，上拉加载的数据也应该为选中状态
                                     */
                                    if (selectionStatusTypeEnum != null) {
                                        goodsInfoListBean.setSelectStatus(true);
                                    }
                                    goodsInfoListBean.setEditStatus(isEditStatus);
                                }
                            }
                        }

//                        当天的数据
                        if (page != 1 && mineBrowsHistoryBeanList.size() > 0) {
                            MineBrowsHistoryBean mineOldBrowsHistoryBean = parentBrowsHistoryBeanList.get(parentBrowsHistoryBeanList.size() - 1);
                            MineBrowsHistoryBean mineNewBrowsHistoryBean = mineBrowsHistoryEntity.getMineBrowsHistoryList().get(0);
                            if (isSameTimeDay("yyyy-MM-dd", mineOldBrowsHistoryBean.getTime(), mineNewBrowsHistoryBean.getTime())) {
                                /**
                                 * 合并数据 如果已选择当天，更新新数据为选中状态
                                 */
                                int parentPosition = mineBrowsingHistoryAdapter.getParentPosition(mineBrowsHistoryBeanList.get(mineBrowsHistoryBeanList.size() - 1));
//                            当前数据
                                MineBrowsHistoryBean mineBrowsHistoryBean = (MineBrowsHistoryBean) mineBrowsHistoryBeanList.get(parentPosition);
                                mineBrowsHistoryBean.setExpanded(false);
                                List<GoodsInfoListBean> subItems = mineBrowsHistoryBean.getSubItems();
                                for (MineBrowsHistoryBean mineNewBrowsHistory : mineBrowsHistoryEntity.getMineBrowsHistoryList()) {
                                    for (GoodsInfoListBean goodsInfoListBean : mineNewBrowsHistory.getGoodsInfoList()) {
                                        if (mineBrowsHistoryBean.isSelectStatus() || mineBrowsHistoryBean.getStatusTypeEnum() != null) {
                                            goodsInfoListBean.setSelectStatus(true);
                                        }
                                    }
                                    subItems.addAll(mineNewBrowsHistory.getGoodsInfoList());
                                }
//                                移除当前天已展开的数据
                                List<MultiItemEntity> multiItemEntities = new ArrayList<>(mineBrowsHistoryBeanList.subList(0, parentPosition + 1));
                                mineBrowsHistoryBeanList.clear();
                                mineBrowsHistoryBeanList.addAll(multiItemEntities);
                                MineBrowsHistoryBean mineParentBrowsHistoryBean = parentBrowsHistoryBeanList.get(parentBrowsHistoryBeanList.size() - 1);
                                mineParentBrowsHistoryBean.setGoodsInfoList(subItems);
                            } else {
                                mineBrowsHistoryBeanList.addAll(mineBrowsHistoryEntity.getMineBrowsHistoryList());
                                parentBrowsHistoryBeanList.addAll(mineBrowsHistoryEntity.getMineBrowsHistoryList());
                            }
                        } else {
                            mineBrowsHistoryBeanList.addAll(mineBrowsHistoryEntity.getMineBrowsHistoryList());
                            parentBrowsHistoryBeanList.addAll(mineBrowsHistoryEntity.getMineBrowsHistoryList());
                        }
                    } else if (!mineBrowsHistoryEntity.getCode().equals(EMPTY_CODE)) {
                        showToast(MineProductBrowsingHistoryActivity.this, mineBrowsHistoryEntity.getMsg());
                    }
                    mineBrowsingHistoryAdapter.expandAll();
                    mineBrowsingHistoryAdapter.notifyDataSetChanged();
                }
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mineBrowsHistoryBeanList, mineBrowsHistoryEntity);
            }

            @Override
            public void onNotNetOrException() {
                mineBrowsingHistoryAdapter.loadMoreEnd(true);
                hasNextDay = false;
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

    /**
     * 更换页码
     *
     * @是 切换下一页
     * @否 不处理
     */
    private void changeNextDay(boolean isForceChangeNextDay) {
        int currentPosition = historyTimeShaftList.indexOf(currentDay);
//数据已加载完毕判断   @未找到当天日期 @当天是最后一天并且（已加载完或者强行更换下一天）
        if (currentPosition == -1 ||
                (currentPosition == historyTimeShaftList.size() - 1 &&
                        (page >= totalCurrentDayPage || isForceChangeNextDay))) {
            mineBrowsingHistoryAdapter.loadMoreEnd();
            hasNextDay = false;
            return;
        }
        if (page >= totalCurrentDayPage || isForceChangeNextDay) {
            currentDay = historyTimeShaftList.get(currentPosition + 1);
            page = 0;
            isCleanData = false;
        }
    }
    /**
     * 更换页面并且加载数据
     *
     * @是 切换下一页
     * @否 不处理
     */
    private void loadNextDayData() {
        changeNextDay(false);
        page++;
        getMineBrowsingHistory();
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

    /**
     * 删除足迹记录
     *
     * @全选 手动全选 全选是否为check@true;@false 找出要保留的日期和保留id
     * @非全选 先筛选
     */
    private void setDeleteBrowsHistory() {
        Map<String, Object> params = new HashMap<>();
        List<MineBrowsHistoryBean> newBrowsHistoryBeanList = new ArrayList<>();
        if (selectionStatusTypeEnum != null) {
            params.put("allSelect", 1);
            if (!check_box_all_del.isChecked()) {
//                非手动选择 需要保留数据
                String retainDate = "";
                String retainIds = "";
                for (MineBrowsHistoryBean mineBrowsHistoryBean : parentBrowsHistoryBeanList) {
                    if (!mineBrowsHistoryBean.isSelectStatus()) {
//                        避免移除 ConcurrentModificationException
                        mineBrowsHistoryBean.setGoodsInfoList(new ArrayList<>(mineBrowsHistoryBean.getSubItems()));
                        for (GoodsInfoListBean goodsInfoListBean : mineBrowsHistoryBean.getSubItems()) {
                            if (!goodsInfoListBean.isSelectStatus()) {
                                retainIds += (!TextUtils.isEmpty(retainIds) ? "," + goodsInfoListBean.getId() : goodsInfoListBean.getId());
                            }else{
                                mineBrowsHistoryBean.getGoodsInfoList().remove(goodsInfoListBean);
                            }
                        }
                        mineBrowsHistoryBean.setSubItems(mineBrowsHistoryBean.getGoodsInfoList());
                        mineBrowsHistoryBean.setExpanded(false);
                        newBrowsHistoryBeanList.add(mineBrowsHistoryBean);
                        if (TextUtils.isEmpty(retainIds)) {
                            retainDate += (!TextUtils.isEmpty(retainDate) ? "," + mineBrowsHistoryBean.getTime() : mineBrowsHistoryBean.getTime());
                        }
                        if(mineBrowsHistoryBean.getStatusTypeEnum()!=null){
                            if(currentDay.equals(mineBrowsHistoryBean.getTime())){
                                changeNextDay(true);
                            }
                        }
                    }else{
                        if(currentDay.equals(mineBrowsHistoryBean.getTime())){
                            changeNextDay(true);
                        }
                    }
                }
                if (!TextUtils.isEmpty(retainDate) || !TextUtils.isEmpty(retainIds)) {
                    if (!TextUtils.isEmpty(retainDate)) {
                        params.put("selectDate", retainDate);
                    }
                    if (!TextUtils.isEmpty(retainIds)) {
                        params.put("keepIds", retainIds);
                    }
                } else {
                    showToast(MineProductBrowsingHistoryActivity.this, "请选择需要删除的记录");
                    return;
                }

            } else {
                setDefaultEmptyUI();
            }
            hasNextDay = false;
            selectionStatusTypeEnum = null;
        } else {
            String deleteDate = "";
            String deleteIds = "";
            String retainIds = "";
            for (MineBrowsHistoryBean mineBrowsHistoryBean : parentBrowsHistoryBeanList) {
                if (!mineBrowsHistoryBean.isSelectStatus()) {
//                        避免移除 ConcurrentModificationException
                    mineBrowsHistoryBean.setGoodsInfoList(new ArrayList<>(mineBrowsHistoryBean.getSubItems()));
//                    删除当前日期，保留
                    if (mineBrowsHistoryBean.getStatusTypeEnum() != null) {
                        for (GoodsInfoListBean goodsInfoListBean : mineBrowsHistoryBean.getSubItems()) {
                            if (!goodsInfoListBean.isSelectStatus()) {
                                retainIds += (!TextUtils.isEmpty(retainIds) ? "," + goodsInfoListBean.getId() : goodsInfoListBean.getId());
                            } else {
                                mineBrowsHistoryBean.getGoodsInfoList().remove(goodsInfoListBean);
                            }
                        }
                        deleteDate += (!TextUtils.isEmpty(deleteDate) ? "," + mineBrowsHistoryBean.getTime() : mineBrowsHistoryBean.getTime());
                        if(currentDay.equals(mineBrowsHistoryBean.getTime())){
                            changeNextDay(true);
                        }
                    } else {
                        for (GoodsInfoListBean goodsInfoListBean : mineBrowsHistoryBean.getSubItems()) {
                            if (goodsInfoListBean.isSelectStatus()) {
                                deleteIds += (!TextUtils.isEmpty(deleteIds) ? "," + goodsInfoListBean.getId() : goodsInfoListBean.getId());
                                mineBrowsHistoryBean.getGoodsInfoList().remove(goodsInfoListBean);
                            }
                        }
                    }
                    mineBrowsHistoryBean.setSubItems(mineBrowsHistoryBean.getGoodsInfoList());
                    newBrowsHistoryBeanList.add(mineBrowsHistoryBean);
                    mineBrowsHistoryBean.setExpanded(false);
                } else {
                    deleteDate += (!TextUtils.isEmpty(deleteDate) ? "," + mineBrowsHistoryBean.getTime() : mineBrowsHistoryBean.getTime());
                    if(currentDay.equals(mineBrowsHistoryBean.getTime())){
                        changeNextDay(true);
                    }
                }
            }
            if (!TextUtils.isEmpty(deleteDate) || !TextUtils.isEmpty(deleteIds) || !TextUtils.isEmpty(retainIds)) {
                params.put("allSelect", 0);
                if (!TextUtils.isEmpty(deleteDate)) {
                    params.put("selectDate", deleteDate);
                }
                if (!TextUtils.isEmpty(deleteIds)) {
                    params.put("removeIds", deleteIds);
                }
                if (!TextUtils.isEmpty(retainIds)) {
                    params.put("keepIds", retainIds);
                }
            } else {
                showToast(MineProductBrowsingHistoryActivity.this, "请选择需要删除的记录");
                return;
            }
        }
//        params.put("uid", userId);
//        NetLoadUtils.getNetInstance().loadNetDataPost(this, DEL_MINE_BROWSING_HISTORY, params, new NetLoadListenerHelper());
        /**
         * 处理界面更新
         */
        parentBrowsHistoryBeanList.clear();
        mineBrowsHistoryBeanList.clear();
        if (newBrowsHistoryBeanList.size() > 0) {
            mineBrowsHistoryBeanList.addAll(newBrowsHistoryBeanList);
            parentBrowsHistoryBeanList.addAll(newBrowsHistoryBeanList);
            mineBrowsingHistoryAdapter.expandAll();
            mineBrowsingHistoryAdapter.notifyDataSetChanged();
        } else if (hasNextDay) {
            mineBrowsingHistoryAdapter.notifyLoadMoreToLoading();
        } else {
            setDefaultEmptyUI();
        }
//
//        /**
//         * 移除选择项
//         * @全选删除 自动恢复初始状态，禁用编辑
//         * @全选余部分 禁用加载，留存未选择部分，保持编辑状态
//         * @未点击全选 移除已选项，保持编辑状态
//         */
//        if (check_box_all_del.isChecked()) {
//            setDefaultEmptyUI();
//        } else {
//            tv_header_shared.setEnabled(true);
//            if (selectionStatusTypeEnum != null) {
//                mineBrowsingHistoryAdapter.loadMoreEnd();
//                mineBrowsingHistoryAdapter.setEnableLoadMore(false);
//                selectionStatusTypeEnum = null;
//            }
////            先找出保留数据
//            for (MineBrowsHistoryBean mineBrowsHistoryBean : parentBrowsHistoryBeanList) {
//                List<MineBrowsHistoryBean> newBrowsHistoryBeanList = new ArrayList<>();
//                if (!mineBrowsHistoryBean.isSelectStatus()) {
//                    List<GoodsInfoListBean> newGoodsInfoListBeanList = new ArrayList<>();
//                    for (GoodsInfoListBean goodsInfoListBean : mineBrowsHistoryBean.getSubItems()) {
//                        if (!goodsInfoListBean.isSelectStatus()) {
//                            newGoodsInfoListBeanList.add(goodsInfoListBean);
//                        }
//                    }
//                    mineBrowsHistoryBean.setExpanded(false);
//                    mineBrowsHistoryBean.setGoodsInfoList(newGoodsInfoListBeanList);
//                    mineBrowsHistoryBean.setSubItems(newGoodsInfoListBeanList);
//                    newBrowsHistoryBeanList.add(mineBrowsHistoryBean);
//                    mineBrowsHistoryBeanList.clear();
//                    parentBrowsHistoryBeanList.clear();
//                    if (newBrowsHistoryBeanList.size() > 0) {
//                        mineBrowsHistoryBeanList.addAll(newBrowsHistoryBeanList);
//                        parentBrowsHistoryBeanList.addAll(newBrowsHistoryBeanList);
//                        mineBrowsingHistoryAdapter.expandAll();
//                    }
//                    /*else {
//                        if (mineBrowsingHistoryAdapter.isLoadMoreEnable()) {
//                            page++;
//                            getMineBrowsingHistory();
//                        } else {
//                            setEditStatus(false);
//                            mineBrowsingHistoryAdapter.loadMoreEnd();
//                            mineBrowsingHistoryAdapter.setEnableLoadMore(false);
//                            NetLoadUtils.getNetInstance().showLoadSirEmpty(loadService);
//                            setEditStatus(false);
//                            tv_header_shared.setEnabled(false);
//                        }
//                    }*/
//                }else{
//                    if(page!=0&&currentDay.equals(mineBrowsHistoryBean.getTime())){
//
//                    }
//                }
//            }
//            mineBrowsingHistoryAdapter.notifyDataSetChanged();
//        }
    }

    /**
     * 设置默认空值界面
     */
    private void setDefaultEmptyUI() {
        page = 1;
        mineBrowsHistoryBeanList.clear();
        parentBrowsHistoryBeanList.clear();
        mineBrowsingHistoryAdapter.loadMoreEnd();
        mineBrowsingHistoryAdapter.notifyItemRangeRemoved(0, mineBrowsingHistoryAdapter.getItemCount());
        NetLoadUtils.getNetInstance().showLoadSirEmpty(loadService);
        setEditStatus(false);
        tv_header_shared.setVisibility(GONE);
    }

    /**
     * 配置设置默认值
     */
    private void setDefaultSetting() {
        selectionStatusTypeEnum = null;
        tv_header_shared.setSelected(isEditStatus);
        tv_header_shared.setText(isEditStatus ? "完成" : "编辑");
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
        setDeleteBrowsHistory();
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
                setDefaultSetting();
            }
        }
    }
}
