package com.amkj.dmsh.shopdetails.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.bean.OrderProductNewBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.netloadpage.NetEmptyCallback;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.activity.DoMoIndentAllActivity;
import com.amkj.dmsh.shopdetails.activity.OrderSearchHelpActivity;
import com.amkj.dmsh.shopdetails.adapter.DirectIndentListAdapter;
import com.amkj.dmsh.shopdetails.bean.MainOrderListEntity;
import com.amkj.dmsh.shopdetails.bean.MainOrderListEntity.MainOrderBean;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.amkj.dmsh.views.OrderLoadMoreView;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import butterknife.BindView;

import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.isContextExisted;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SEARCH_DATA;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TEN;
import static com.amkj.dmsh.constant.ConstantVariable.UPDATE_INDENT_LIST;


/**
 * Created by xiaoxin on 2020/3/14
 * Version:v4.5.0
 * ClassDescription :订单列表重构
 */
public class DoMoIndentNewFragment extends BaseFragment {
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    List<MainOrderBean> orderListBeanList = new ArrayList<>();
    private int page = 1;
    private DirectIndentListAdapter doMoIndentListAdapter;
    private boolean isOnPause = false;
    private MainOrderListEntity mOrderListNewEntity;
    //0.全部订单  1.待付款  2.待发货  3.待收货  4.待评价
    private String[] types = new String[]{"all", "waitPay", "waitDelivery", "waitTakeDelivery", "waitEvaluate"};
    private int mType;

    private String CurrentOrderNo;
    private int CurrentPosition;

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_smart_refresh_recycler_float_loading;
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
        communal_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_ten_dp).create());
        doMoIndentListAdapter = new DirectIndentListAdapter(this, getActivity(), orderListBeanList);
        doMoIndentListAdapter.setLoadMoreView(new OrderLoadMoreView(getActivity()));
        communal_recycler.setAdapter(doMoIndentListAdapter);
        //解决调用notifyItemChanged闪烁问题
        SimpleItemAnimator itemAnimator = (SimpleItemAnimator) communal_recycler.getItemAnimator();
        if (itemAnimator != null) {
            itemAnimator.setSupportsChangeAnimations(false);
        }
        setFloatingButton(download_btn_communal, communal_recycler);
        smart_communal_refresh.setOnRefreshListener(refreshLayout -> {
            loadData();
        });
        doMoIndentListAdapter.setOnLoadMoreListener(() -> {
            page++;
            getIndentList();
        }, communal_recycler);
    }

    //未搜索到订单时
    private void setEmptyCallback() {
        loadService.setCallBack(NetEmptyCallback.class, (context, view) -> {
            TextView tv_communal_net_tint = view.findViewById(R.id.tv_communal_net_tint);
            if (!TextUtils.isEmpty(getKeywords())) {
                String tips = getResources().getString(R.string.no_order_found);
                tv_communal_net_tint.setText(ConstantMethod.getSpannableString(tips, 8, tips.length(), -1, "#0a88fa"));
                tv_communal_net_tint.setOnClickListener(v -> {
                    Intent intent = new Intent(getActivity(), OrderSearchHelpActivity.class);
                    startActivity(intent);
                });
            }
        });
    }

    @Override
    protected void loadData() {
        page = 1;
        getIndentList();
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    //获取订单列表数据
    private void getIndentList() {
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", page);
        params.put("showCount", TOTAL_COUNT_TEN);
        params.put("orderStatusText", types[mType]);
        if (!TextUtils.isEmpty(getKeywords())) {
            params.put("keyWord", getKeywords());
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Url.Q_GET_ORDER_LIST, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                smart_communal_refresh.finishRefresh();
                mOrderListNewEntity = GsonUtils.fromJson(result, MainOrderListEntity.class);
                if (page == 1) {
                    orderListBeanList.clear();
                }
                String code = mOrderListNewEntity.getCode();
                if (SUCCESS_CODE.equals(code)) {
                    List<MainOrderBean> orderList = mOrderListNewEntity.getResult();
                    if (orderList != null) {
                        if (!TextUtils.isEmpty(mOrderListNewEntity.getCurrentTime())) {
                            for (int i = 0; i < orderList.size(); i++) {
                                MainOrderBean mainOrderBean = orderList.get(i);
                                mainOrderBean.setCurrentTime(mOrderListNewEntity.getCurrentTime());
                                //组装赠品数据
                                List<OrderProductNewBean> presentProductList = mainOrderBean.getPresentProductList();
                                for (OrderProductNewBean productNewBean : presentProductList) {
                                    productNewBean.setIsPresent(1);
                                }
                                mainOrderBean.getOrderProductList().addAll(presentProductList);
                            }
                        }
                        orderListBeanList.addAll(mOrderListNewEntity.getResult());
                        doMoIndentListAdapter.notifyDataSetChanged();
                        if (orderList.size() >= TOTAL_COUNT_TEN) {
                            doMoIndentListAdapter.loadMoreComplete();
                        } else {
                            doMoIndentListAdapter.loadMoreEnd();
                        }
                    }
                } else {
                    doMoIndentListAdapter.notifyDataSetChanged();
                    doMoIndentListAdapter.loadMoreEnd();
                    if (!EMPTY_CODE.equals(code)) showToast(mOrderListNewEntity.getMsg());
                }
                NetLoadUtils.getNetInstance().showLoadSir(loadService, orderListBeanList, mOrderListNewEntity);
                setEmptyCallback();
            }

            @Override
            public void onNotNetOrException() {
                smart_communal_refresh.finishRefresh();
                doMoIndentListAdapter.loadMoreFail();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, orderListBeanList, mOrderListNewEntity);
                setEmptyCallback();
            }
        });
    }

    @Override
    protected boolean isDataInitiated() {
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        //onResume方法跟随所属的Actvity,Activity获取焦点时viewpager下的所有fragment都会调用onResume，所以这里多加一个判断当前fragment是否可见
        if (isOnPause && isVisibleToUser) {
            updateIndentItem(CurrentOrderNo, CurrentPosition);
        }
        isOnPause = true;
    }

    @Override
    protected void getReqParams(Bundle bundle) {
        if (bundle != null) {
            mType = (int) bundle.get("type");
        }
    }

    //针对单条订单进行更新
    private void updateIndentItem(String orderNo, int position) {
        if (TextUtils.isEmpty(orderNo)) return;
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", 1);
        params.put("showCount", 1);
        params.put("orderStatusText", types[mType]);
        params.put("keyWord", orderNo);
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Url.Q_GET_ORDER_LIST, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                MainOrderListEntity mOrderListNewEntity = GsonUtils.fromJson(result, MainOrderListEntity.class);
                if (mOrderListNewEntity != null) {
                    List<MainOrderBean> orderList = mOrderListNewEntity.getResult();
                    if (orderListBeanList != null && orderListBeanList.size() - 1 >= position) {
                        if (orderListBeanList.get(position).getOrderNo().equals(orderNo)) {
                            if (orderList != null && orderList.size() > 0) {
                                MainOrderBean mainOrderBean = orderList.get(0);
                                mainOrderBean.setCurrentTime(mOrderListNewEntity.getCurrentTime());
                                //组装赠品数据
                                List<OrderProductNewBean> presentProductList = mainOrderBean.getPresentProductList();
                                for (OrderProductNewBean productNewBean : presentProductList) {
                                    productNewBean.setIsPresent(1);
                                }
                                mainOrderBean.getOrderProductList().addAll(presentProductList);
                                //更新单条数据
                                if (orderListBeanList != null && orderListBeanList.size() - 1 >= position) {
                                    if (orderListBeanList.get(position).getOrderNo().equals(orderNo)) {
                                        orderListBeanList.set(position, mainOrderBean);
                                        doMoIndentListAdapter.notifyItemChanged(position);
                                    }
                                }
                            } else if (EMPTY_CODE.equals(mOrderListNewEntity.getCode())) {
                                CurrentOrderNo = "";
                                doMoIndentListAdapter.remove(position);
                            }
                        }
                    }
                }
            }
        });
    }

    public void setCurrentOrderNo(String currentOrderNo) {
        CurrentOrderNo = currentOrderNo;
    }

    public void setCurrentPosition(int currentPosition) {
        CurrentPosition = currentPosition;
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (isVisibleToUser) {
            if (UPDATE_INDENT_LIST.equals(message.type)) {
                if (getSimpleName().equals(message.result)) {
                    updateIndentItem(CurrentOrderNo, CurrentPosition);
                }
            } else if (SEARCH_DATA.equals(message.type)) {
                page = 1;
                NetLoadUtils.getNetInstance().showLoadSirLoading(loadService);
                getIndentList();
            }
        }
    }


    //获取关键字
    protected String getKeywords() {
        if (isContextExisted(getActivity())) {
            return ((DoMoIndentAllActivity) getActivity()).getKewords();
        }
        return "";
    }

}
