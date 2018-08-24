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
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.mine.activity.MineLoginActivity;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.release.adapter.RelevanceProAdapter;
import com.amkj.dmsh.release.bean.RelevanceProEntity;
import com.amkj.dmsh.release.bean.RelevanceProEntity.RelevanceProBean;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;
import static com.amkj.dmsh.constant.ConstantMethod.changeRelevanceProduct;
import static com.amkj.dmsh.constant.ConstantMethod.getEmptyView;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.CART_PRODUCT;
import static com.amkj.dmsh.constant.ConstantVariable.COLLECT_PRODUCT;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.RELEVANCE_TYPE;
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
    private int uid;
    private List<RelevanceProBean> relevanceProBeanList;
    private String relevanceType;

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_smart_refresh_recycler_float_loading;
    }

    @Override
    protected void initViews() {
        getLoginStatus();
        if(TextUtils.isEmpty(relevanceType)){
            return;
        }

        if(relevanceProBeanList!=null){
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
            indentProPage = 1;
                carProPage = 1;
                colProPage = 1;
                loadData();
        });
        relevanceProAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                switch (relevanceType){
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
        communal_load.setVisibility(View.VISIBLE);
        relevanceProAdapter.setEmptyView(getEmptyView(getActivity(), "商品"));
    }

    private void getLoginStatus() {
        SavePersonalInfoBean personalInfo = ConstantMethod.getPersonalInfo(getActivity());
        if (personalInfo.isLogin()) {
            uid = personalInfo.getUid();
        } else {
            //未登录跳转登录页
            Intent intent = new Intent(getActivity(), MineLoginActivity.class);
            startActivityForResult(intent, IS_LOGIN_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IS_LOGIN_CODE) {
            getLoginStatus();
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
                changeRelevanceProduct(relevanceProList,this.relevanceSelProList);
            }
        }
    }

    @Override
    protected void loadData() {
        switch (relevanceType){
                case COLLECT_PRODUCT:
                    getColProData();
                    break;
                case CART_PRODUCT:
                    getShopCarProData();
                    break;
                default:
                    getDoMoIndentProData();
                    break;
        }
    }
    //  订单关联商品
    private void getDoMoIndentProData() {
        if (NetWorkUtils.checkNet(getActivity())) {
            String url = Url.BASE_URL + Url.REL_INDENT_PRO;
            Map<String, Object> params = new HashMap<>();
            params.put("uid", uid);
            params.put("currentPage", indentProPage);
            params.put("showCount", TOTAL_COUNT_TWENTY);
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    communal_load.setVisibility(View.GONE);
                    smart_communal_refresh.finishRefresh();
                    relevanceProAdapter.loadMoreComplete();
                    if (indentProPage == 1) {
                        relevanceProList.clear();
                        relevanceIndentProList.clear();
                    }
                    Gson gson = new Gson();
                    RelevanceProEntity relevanceProEntity = gson.fromJson(result, RelevanceProEntity.class);
                    if (relevanceProEntity != null) {
                        if (relevanceProEntity.getCode().equals("01")) {
                            relevanceIndentProList.addAll(relevanceProEntity.getRelevanceProList());
                            relevanceProList.removeAll(relevanceIndentProList);
                            relevanceProList.addAll(changeRelevanceProduct(relevanceIndentProList,relevanceSelProList));
                        } else if (relevanceProEntity.getCode().equals("02")) {
                            relevanceProAdapter.loadMoreEnd(false);
                        } else {
                            relevanceProAdapter.loadMoreEnd(false);
                            showToast(getActivity(), relevanceProEntity.getMsg());
                        }
                        if (indentProPage == 1) {
                            relevanceProAdapter.setNewData(relevanceProList);
                        } else {
                            relevanceProAdapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    smart_communal_refresh.finishRefresh();
                    relevanceProAdapter.loadMoreComplete();
                    communal_load.setVisibility(View.GONE);
                    relevanceProAdapter.loadMoreEnd(false);
                    showToast(getActivity(), R.string.invalidData);
                    super.onError(ex, isOnCallback);
                }
            });
        } else {
            smart_communal_refresh.finishRefresh();
            communal_load.setVisibility(View.GONE);
            relevanceProAdapter.loadMoreComplete();
            relevanceProAdapter.loadMoreEnd(false);
        }
    }

    //      取消或添加选择
    private void setSelRelevancePro(RelevanceProBean relevanceProBean) {
        if (relevanceProBean.isSelPro()) {
            relevanceSelProList.add(relevanceProBean);
        } else {
            for (RelevanceProBean selectProductBean:relevanceSelProList) {
                if(selectProductBean.getId() == relevanceProBean.getId()){
                    relevanceSelProList.remove(selectProductBean);
                    break;
                }
            }
        }
        EventBus.getDefault().post(new EventMessage("relevanceProduct",relevanceSelProList));
    }

    private void getShopCarProData() {
        if (NetWorkUtils.checkNet(getActivity())) {
            String url = Url.BASE_URL + Url.REL_SHOP_CAR_PRO;
            Map<String, Object> params = new HashMap<>();
            params.put("uid", uid);
            params.put("currentPage", carProPage);
            params.put("showCount", TOTAL_COUNT_TWENTY);
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    communal_load.setVisibility(View.GONE);
                    smart_communal_refresh.finishRefresh();
                    relevanceProAdapter.loadMoreComplete();
                    if (carProPage == 1) {
                        relevanceCarProList.clear();
                        relevanceProList.clear();
                    }
                    Gson gson = new Gson();
                    RelevanceProEntity relevanceProEntity = gson.fromJson(result, RelevanceProEntity.class);
                    if (relevanceProEntity != null) {
                        if (relevanceProEntity.getCode().equals("01")) {
                            relevanceCarProList.addAll(relevanceProEntity.getRelevanceProList());
                            relevanceProList.removeAll(relevanceCarProList);
                            relevanceProList.addAll(changeRelevanceProduct(relevanceCarProList,relevanceSelProList));
                        }  else if (relevanceProEntity.getCode().equals("02")) {
                            relevanceProAdapter.loadMoreEnd(false);
                        } else {
                            relevanceProAdapter.loadMoreEnd(false);
                            showToast(getActivity(), relevanceProEntity.getMsg());
                        }
                        relevanceProAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    relevanceProAdapter.loadMoreComplete();
                    relevanceProAdapter.loadMoreEnd(false);
                    communal_load.setVisibility(View.GONE);
                    smart_communal_refresh.finishRefresh();
                    showToast(getActivity(), R.string.invalidData);
                    super.onError(ex, isOnCallback);
                }
            });
        } else {
            relevanceProAdapter.loadMoreComplete();
            relevanceProAdapter.loadMoreEnd(false);
            communal_load.setVisibility(View.GONE);
            smart_communal_refresh.finishRefresh();
        }
    }

    private void getColProData() {
        if (NetWorkUtils.checkNet(getActivity())) {
            String url = Url.BASE_URL + Url.REL_COLLECT_PRO;
            Map<String, Object> params = new HashMap<>();
            params.put("uid", uid);
            params.put("currentPage", colProPage);
            params.put("showCount", TOTAL_COUNT_TWENTY);
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    if(colProPage == 1){
                        relevanceColProList.clear();
                        relevanceProList.clear();
                    }
                    relevanceProAdapter.loadMoreComplete();
                    communal_load.setVisibility(View.GONE);
                    smart_communal_refresh.finishRefresh();
                    Gson gson = new Gson();
                    RelevanceProEntity relevanceProEntity = gson.fromJson(result, RelevanceProEntity.class);
                    if (relevanceProEntity != null) {
                        if (relevanceProEntity.getCode().equals("01")) {
                            relevanceColProList.addAll(relevanceProEntity.getRelevanceProList());
                            relevanceProList.removeAll(relevanceColProList);
                            relevanceProList.addAll(changeRelevanceProduct(relevanceColProList,relevanceSelProList));
                        } else if (relevanceProEntity.getCode().equals("02")) {
                            relevanceProAdapter.loadMoreEnd(false);
                        } else {
                            relevanceProAdapter.loadMoreEnd(false);
                            showToast(getActivity(), relevanceProEntity.getMsg());
                        }
                        relevanceProAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    relevanceProAdapter.loadMoreEnd(false);
                    communal_load.setVisibility(View.GONE);
                    smart_communal_refresh.finishRefresh();
                    relevanceProAdapter.loadMoreComplete();
                    showToast(getActivity(), R.string.invalidData);
                    super.onError(ex, isOnCallback);
                }
            });
        } else {
            relevanceProAdapter.loadMoreEnd(false);
            communal_load.setVisibility(View.GONE);
            smart_communal_refresh.finishRefresh();
            relevanceProAdapter.loadMoreComplete();
        }
    }
    
    @Override
    protected void getReqParams(Bundle bundle) {
        relevanceType = bundle.getString(RELEVANCE_TYPE);
        relevanceProBeanList = bundle.getParcelableArrayList("relevanceProduct");
    }
}
