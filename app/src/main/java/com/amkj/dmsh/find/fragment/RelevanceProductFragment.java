package com.amkj.dmsh.find.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.base.NetLoadUtils;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.release.adapter.RelevanceProAdapter;
import com.amkj.dmsh.release.bean.RelevanceProEntity;
import com.amkj.dmsh.release.bean.RelevanceProEntity.RelevanceProBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;
import static com.amkj.dmsh.constant.ConstantMethod.changeRelevanceProduct;
import static com.amkj.dmsh.constant.ConstantMethod.getEmptyView;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.CART_PRODUCT;
import static com.amkj.dmsh.constant.ConstantVariable.COLLECT_PRODUCT;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.RELEVANCE_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TWENTY;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/1/23
 * version 3.7
 * class description:优惠券
 */

public class RelevanceProductFragment extends BaseFragment {
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;

    private RelevanceProAdapter relevanceProAdapter;
    //    订单条数
    private List<RelevanceProBean> relevanceIndentProList = new ArrayList<>();
    //    购物车条数
    private List<RelevanceProBean> relevanceCarProList = new ArrayList<>();
    //    收藏条数
    private List<RelevanceProBean> relevanceColProList = new ArrayList<>();
    //    总条数
    private List<RelevanceProBean> relevanceProList = new ArrayList<>();
    //    选择条数
    private List<RelevanceProBean> relevanceSelProList = new ArrayList<>();
    //    订单分页
    private int indentProPage = 1;
    //    购物车分页
    private int carProPage = 1;
    //    收藏分页
    private int colProPage = 1;
    private List<RelevanceProBean> relevanceProBeanList;
    private String relevanceType;
    private RelevanceProEntity relevanceProEntity;

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_smart_refresh_recycler_float_loading;
    }

    @Override
    protected void initViews() {
        getLoginStatus(RelevanceProductFragment.this);
        if (TextUtils.isEmpty(relevanceType)) {
            return;
        }

        if (relevanceProBeanList != null) {
            relevanceSelProList.addAll(relevanceProBeanList);
        }
        communal_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        relevanceProAdapter = new RelevanceProAdapter(getActivity(), relevanceProList);
        communal_recycler.setAdapter(relevanceProAdapter);
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
        relevanceProAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.cb_rev_pro_sel) {
                    int proPosition = (int) view.getTag(R.id.shop_car_cb);
                    RelevanceProBean relevanceProBean = relevanceProList.get(proPosition);
                    if (relevanceProBean != null) {
                        if (!relevanceProBean.isSelPro() && relevanceSelProList.size() > 4) {
                            CheckBox checkBox = (CheckBox) view;
                            checkBox.setChecked(relevanceProBean.isSelPro());
                            showToast(getActivity(), R.string.relevance_pro_more);
                        } else {
                            relevanceProBean.setSelPro(!relevanceProBean.isSelPro());
                            setSelRelevancePro(relevanceProBean);
                            relevanceProList.set(proPosition, relevanceProBean);
                        }
                    }
                }
            }
        });
        smart_communal_refresh.setOnRefreshListener((refreshLayout) -> {
            loadData();
        });
        relevanceProAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                switch (relevanceType) {
                    case COLLECT_PRODUCT:
                        colProPage++;
                        getColProData();
                        break;
                    case CART_PRODUCT:
                        carProPage++;
                        getShopCarProData();
                        break;
                    default:
                        indentProPage++;
                        getDoMoIndentProData();
                        break;
                }
            }
        }, communal_recycler);
        relevanceProAdapter.setEmptyView(getEmptyView(getActivity(), "商品"));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IS_LOGIN_CODE) {
            loadData();
        }
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        super.postEventResult(message);
        if ("relevanceProductAll".equals(message.type)) {
            List<RelevanceProBean> relevanceSelProList = (List<RelevanceProBean>) message.result;
            this.relevanceSelProList.clear();
            if (relevanceSelProList != null && relevanceSelProList.size() > 0) {
                this.relevanceSelProList.addAll(relevanceSelProList);
                changeRelevanceProduct(relevanceProList, this.relevanceSelProList);
            }
        }
    }

    @Override
    protected void loadData() {
        switch (relevanceType) {
            case COLLECT_PRODUCT:
                colProPage = 1;
                getColProData();
                break;
            case CART_PRODUCT:
                carProPage = 1;
                getShopCarProData();
                break;
            default:
                indentProPage = 1;
                getDoMoIndentProData();
                break;
        }
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    //  订单关联商品
    private void getDoMoIndentProData() {
        String url = Url.BASE_URL + Url.REL_INDENT_PRO;
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("currentPage", indentProPage);
        params.put("showCount", TOTAL_COUNT_TWENTY);
        NetLoadUtils.getQyInstance().loadNetDataPost(getActivity(), url
                , params, new NetLoadUtils.NetLoadListener() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();
                        relevanceProAdapter.loadMoreComplete();
                        if (indentProPage == 1) {
                            relevanceProList.clear();
                            relevanceIndentProList.clear();
                        }
                        Gson gson = new Gson();
                        relevanceProEntity = gson.fromJson(result, RelevanceProEntity.class);
                        if (relevanceProEntity != null) {
                            if (relevanceProEntity.getCode().equals(SUCCESS_CODE)) {
                                relevanceIndentProList.addAll(relevanceProEntity.getRelevanceProList());
                                relevanceProList.removeAll(relevanceIndentProList);
                                relevanceProList.addAll(changeRelevanceProduct(relevanceIndentProList, relevanceSelProList));
                            } else if (relevanceProEntity.getCode().equals(EMPTY_CODE)) {
                                relevanceProAdapter.loadMoreEnd(false);
                            } else {
                                relevanceProAdapter.loadMoreEnd(false);
                                showToast(getActivity(), relevanceProEntity.getMsg());
                            }
                            relevanceProAdapter.notifyDataSetChanged();
                        }
                        NetLoadUtils.getQyInstance().showLoadSir(loadService, relevanceProList, relevanceProEntity);
                    }

                    @Override
                    public void netClose() {
                        smart_communal_refresh.finishRefresh();
                        relevanceProAdapter.loadMoreComplete();
                        relevanceProAdapter.loadMoreEnd(false);
                        NetLoadUtils.getQyInstance().showLoadSir(loadService, relevanceProList, relevanceProEntity);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        smart_communal_refresh.finishRefresh();
                        relevanceProAdapter.loadMoreComplete();
                        relevanceProAdapter.loadMoreEnd(false);
                        showToast(getActivity(), R.string.invalidData);
                        NetLoadUtils.getQyInstance().showLoadSir(loadService, relevanceProList, relevanceProEntity);
                    }
                });
    }

    //      取消或添加选择
    private void setSelRelevancePro(RelevanceProBean relevanceProBean) {
        if (relevanceProBean.isSelPro()) {
            relevanceSelProList.add(relevanceProBean);
        } else {
            for (RelevanceProBean selectProductBean : relevanceSelProList) {
                if (selectProductBean.getId() == relevanceProBean.getId()) {
                    relevanceSelProList.remove(selectProductBean);
                    break;
                }
            }
        }
        EventBus.getDefault().post(new EventMessage("relevanceProduct", relevanceSelProList));
    }

    private void getShopCarProData() {
        String url = Url.BASE_URL + Url.REL_SHOP_CAR_PRO;
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("currentPage", carProPage);
        params.put("showCount", TOTAL_COUNT_TWENTY);
        NetLoadUtils.getQyInstance().loadNetDataPost(getActivity(), url, params, new NetLoadUtils.NetLoadListener() {
            @Override
            public void onSuccess(String result) {
                smart_communal_refresh.finishRefresh();
                relevanceProAdapter.loadMoreComplete();
                if (carProPage == 1) {
                    relevanceCarProList.clear();
                    relevanceProList.clear();
                }
                Gson gson = new Gson();
                relevanceProEntity = gson.fromJson(result, RelevanceProEntity.class);
                if (relevanceProEntity != null) {
                    if (relevanceProEntity.getCode().equals(SUCCESS_CODE)) {
                        relevanceCarProList.addAll(relevanceProEntity.getRelevanceProList());
                        relevanceProList.removeAll(relevanceCarProList);
                        relevanceProList.addAll(changeRelevanceProduct(relevanceCarProList, relevanceSelProList));
                    } else if (relevanceProEntity.getCode().equals(EMPTY_CODE)) {
                        relevanceProAdapter.loadMoreEnd(false);
                    } else {
                        relevanceProAdapter.loadMoreEnd(false);
                        showToast(getActivity(), relevanceProEntity.getMsg());
                    }
                    relevanceProAdapter.notifyDataSetChanged();
                }
                NetLoadUtils.getQyInstance().showLoadSir(loadService, relevanceProList, relevanceProEntity);
            }

            @Override
            public void netClose() {
                relevanceProAdapter.loadMoreComplete();
                relevanceProAdapter.loadMoreEnd(false);
                smart_communal_refresh.finishRefresh();
                showToast(getActivity(), R.string.unConnectedNetwork);
                NetLoadUtils.getQyInstance().showLoadSir(loadService, relevanceProList, relevanceProEntity);
            }

            @Override
            public void onError(Throwable throwable) {
                relevanceProAdapter.loadMoreComplete();
                relevanceProAdapter.loadMoreEnd(false);
                smart_communal_refresh.finishRefresh();
                showToast(getActivity(), R.string.invalidData);
                NetLoadUtils.getQyInstance().showLoadSir(loadService, relevanceProList, relevanceProEntity);
            }
        });
    }

    private void getColProData() {
        String url = Url.BASE_URL + Url.REL_COLLECT_PRO;
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("currentPage", colProPage);
        params.put("showCount", TOTAL_COUNT_TWENTY);
        NetLoadUtils.getQyInstance().loadNetDataPost(getActivity(), url
                , params, new NetLoadUtils.NetLoadListener() {
            @Override
            public void onSuccess(String result) {
                relevanceProAdapter.loadMoreComplete();
                smart_communal_refresh.finishRefresh();
                if (colProPage == 1) {
                    relevanceColProList.clear();
                    relevanceProList.clear();
                }
                Gson gson = new Gson();
                RelevanceProEntity relevanceProEntity = gson.fromJson(result, RelevanceProEntity.class);
                if (relevanceProEntity != null) {
                    if (relevanceProEntity.getCode().equals(SUCCESS_CODE)) {
                        relevanceColProList.addAll(relevanceProEntity.getRelevanceProList());
                        relevanceProList.removeAll(relevanceColProList);
                        relevanceProList.addAll(changeRelevanceProduct(relevanceColProList, relevanceSelProList));
                    } else if (relevanceProEntity.getCode().equals(EMPTY_CODE)) {
                        relevanceProAdapter.loadMoreEnd(false);
                    } else {
                        relevanceProAdapter.loadMoreEnd(false);
                        showToast(getActivity(), relevanceProEntity.getMsg());
                    }
                    relevanceProAdapter.notifyDataSetChanged();
                }
                NetLoadUtils.getQyInstance().showLoadSir(loadService, relevanceProList, relevanceProEntity);
            }

            @Override
            public void netClose() {
                relevanceProAdapter.loadMoreEnd(false);
                smart_communal_refresh.finishRefresh();
                relevanceProAdapter.loadMoreComplete();
                showToast(getActivity(), R.string.unConnectedNetwork);
                NetLoadUtils.getQyInstance().showLoadSir(loadService, relevanceProList, relevanceProEntity);
            }

            @Override
            public void onError(Throwable throwable) {
                relevanceProAdapter.loadMoreEnd(false);
                smart_communal_refresh.finishRefresh();
                relevanceProAdapter.loadMoreComplete();
                showToast(getActivity(), R.string.invalidData);
                NetLoadUtils.getQyInstance().showLoadSir(loadService, relevanceProList, relevanceProEntity);
            }
        });
    }

    @Override
    protected void getReqParams(Bundle bundle) {
        relevanceType = bundle.getString(RELEVANCE_TYPE);
        relevanceProBeanList = bundle.getParcelableArrayList("relevanceProduct");
    }
}
