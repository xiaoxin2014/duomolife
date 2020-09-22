package com.amkj.dmsh.mine.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.mine.activity.CommitZeroReportActivity;
import com.amkj.dmsh.mine.activity.LookMyReportActivity;
import com.amkj.dmsh.mine.activity.ZeroIndentDetailActivity;
import com.amkj.dmsh.mine.adapter.MyZeroApplyAdapter;
import com.amkj.dmsh.mine.bean.MyZeroApplyEntity;
import com.amkj.dmsh.mine.bean.MyZeroApplyEntity.MyZeroApplyBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.activity.DirectZeroWriteActivity;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TEN;

/**
 * Created by xiaoxin on 2020/8/17
 * Version:v4.7.0
 */
public class ZeroApplyListFragment extends BaseFragment {
    @BindView(R.id.communal_recycler)
    RecyclerView mCommunalRecycler;
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout mSmartCommunalRefresh;
    private String mStatus;
    private int page = 1;
    private MyZeroApplyAdapter mMyZeroApplyAdapter;
    private List<MyZeroApplyBean> zeroApplyList = new ArrayList<>();
    private MyZeroApplyEntity mMyZeroApplyEntity;
    private boolean isOnPause;

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_refresh_recycler_base;
    }

    @Override
    protected void initViews() {
        mCommunalRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCommunalRecycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_gray_f)
                .setFirstDraw(true)
                .create());
        mMyZeroApplyAdapter = new MyZeroApplyAdapter(getActivity(), zeroApplyList, mStatus);
        mCommunalRecycler.setAdapter(mMyZeroApplyAdapter);
        mMyZeroApplyAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent;
                MyZeroApplyBean zeroApplyBean = (MyZeroApplyBean) view.getTag();
                if (zeroApplyBean != null) {
                    if (view.getId() == R.id.tv_button) {
                        switch (zeroApplyBean.getStatus()) {
                            case "1": //去领取
                                intent = new Intent(getActivity(), DirectZeroWriteActivity.class);
                                intent.putExtra("activityId", zeroApplyBean.getActivityId());
                                startActivity(intent);
                                break;
                            case "2": //提交报告
                            case "3": //修改报告
                                intent = new Intent(getActivity(), CommitZeroReportActivity.class);
                                intent.putExtra("status", zeroApplyBean.getStatus());
                                intent.putExtra("activityId", zeroApplyBean.getActivityId());
                                intent.putExtra("orderId", zeroApplyBean.getOrderId());
                                intent.putExtra("productName", zeroApplyBean.getProductName());
                                intent.putExtra("productImg", zeroApplyBean.getProductImg());
                                startActivity(intent);
                                break;
                            case "4": //查看报告
                                intent = new Intent(getActivity(), LookMyReportActivity.class);
                                intent.putExtra("orderId", zeroApplyBean.getOrderId());
                                intent.putExtra("activityId", zeroApplyBean.getActivityId());
                                startActivity(intent);
                                break;
                        }
                    } else if (view.getId() == R.id.tv_detail) {
                        intent = new Intent(getActivity(), ZeroIndentDetailActivity.class);
                        intent.putExtra("orderId", zeroApplyBean.getOrderId());
                        startActivity(intent);
                    }
                }
            }
        });
        mSmartCommunalRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                loadData();
            }
        });
    }

    @Override
    protected void loadData() {
        if (userId == 0) return;
        Map<String, Object> map = new HashMap<>();
        map.put("status", mStatus);
        map.put("currentPage", page);
        map.put("showCount", 20);
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Url.GET_MY_APPLY_LIST, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mSmartCommunalRefresh.finishRefresh();
                if (page == 1) {
                    zeroApplyList.clear();
                }
                mMyZeroApplyEntity = GsonUtils.fromJson(result, MyZeroApplyEntity.class);
                if (mMyZeroApplyEntity != null) {
                    String code = mMyZeroApplyEntity.getCode();
                    if (SUCCESS_CODE.equals(code)) {
                        List<MyZeroApplyBean> zeroApplyBeans = mMyZeroApplyEntity.getResult();
                        if (zeroApplyBeans != null) {
                            zeroApplyList.addAll(zeroApplyBeans);
                            mMyZeroApplyAdapter.notifyDataSetChanged();
                            if (zeroApplyBeans.size() >= TOTAL_COUNT_TEN) {
                                mMyZeroApplyAdapter.loadMoreComplete();
                            } else {
                                mMyZeroApplyAdapter.loadMoreEnd();
                            }
                        }
                    } else {
                        mMyZeroApplyAdapter.notifyDataSetChanged();
                        mMyZeroApplyAdapter.loadMoreEnd();
                        if (!EMPTY_CODE.equals(code)) showToast(mMyZeroApplyEntity.getMsg());
                    }
                    mMyZeroApplyAdapter.setCurrentTime(mMyZeroApplyEntity.getCurrentTime());
                }
                NetLoadUtils.getNetInstance().showLoadSir(loadService, zeroApplyList, mMyZeroApplyEntity);

            }

            @Override
            public void onNotNetOrException() {
                mSmartCommunalRefresh.finishRefresh();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, zeroApplyList, mMyZeroApplyEntity);
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
            page = 1;
            loadData();
        }
        isOnPause = true;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    protected void getReqParams(Bundle bundle) {
        if (bundle != null) {
            mStatus = (String) bundle.get("status");
        }
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (ConstantVariable.UPDATE_ZERO_APPLY_LIST.equals(message.type)) {
            loadData();
        }
    }
}
