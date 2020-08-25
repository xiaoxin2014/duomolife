package com.amkj.dmsh.mine.activity;

import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.mine.adapter.ZeroProductAdapter;
import com.amkj.dmsh.mine.adapter.ZeroProductListAdapter;
import com.amkj.dmsh.mine.bean.ZeroInfoBean;
import com.amkj.dmsh.mine.bean.ZeroListEntity;
import com.amkj.dmsh.mine.bean.ZeroListEntity.ZeroListBean.CurrentActivityListBean;
import com.amkj.dmsh.mine.bean.ZeroLotteryEntity;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.views.alertdialog.AlertDialogZeroLottery;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.dismissLoadhud;
import static com.amkj.dmsh.constant.ConstantMethod.showLoadhud;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.mine.adapter.ZeroProductAdapter.ZERO_BEFORE;

/**
 * Created by xiaoxin on 2020/8/11
 * Version:v4.7.0
 * ClassDescription :0元活动活动列表
 */
public class ZeroActivityListActivity extends BaseActivity {
    @BindView(R.id.tv_header_title)
    TextView mTvHeaderTitle;
    @BindView(R.id.tv_header_shared)
    TextView mTvHeaderShared;
    @BindView(R.id.rv_current)
    RecyclerView mRvCurrent;
    @BindView(R.id.rv_before)
    RecyclerView mRvBefore;
    @BindView(R.id.ll_zero_list)
    LinearLayout mLlZeroList;
    @BindView(R.id.ll_zero_current)
    LinearLayout mLlZeroCurrent;
    @BindView(R.id.ll_zero_before)
    LinearLayout mLlZeroBefore;
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout mSmartCommunalRefresh;
    private List<CurrentActivityListBean> currentList = new ArrayList<>();
    private List<ZeroInfoBean> beforeList = new ArrayList<>();
    private ZeroProductAdapter mZeroProductAdapter;
    private ZeroProductListAdapter mZeroProductListAdapter;
    private ZeroListEntity mZeroListEntity;
    private AlertDialogZeroLottery mAlertDialogZeroLottery;

    @Override
    protected int getContentView() {
        return R.layout.activity_zero_list;
    }

    @Override
    protected void initViews() {
        mTvHeaderTitle.setText("精选好物 0元试用");
        mTvHeaderShared.setVisibility(View.GONE);
        mSmartCommunalRefresh.setOnRefreshListener(refreshLayout -> loadData());
        //初始化本期试用列表
        mRvCurrent.setLayoutManager(new LinearLayoutManager(this));
        mZeroProductListAdapter = new ZeroProductListAdapter(this, currentList);
        mRvCurrent.setAdapter(mZeroProductListAdapter);
        //初始化往期试用列表
        mRvBefore.setLayoutManager(new LinearLayoutManager(this));
        mZeroProductAdapter = new ZeroProductAdapter(this, beforeList, ZERO_BEFORE);
        mZeroProductAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                getLotteryList((String) view.getTag());
            }
        });
        mRvBefore.setAdapter(mZeroProductAdapter);
    }

    private void getLotteryList(String activityId) {
        showLoadhud(this);
        Map<String, Object> map = new HashMap<>();
        map.put("activityId", activityId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.GET_WINNERS_INFO_LIST, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                dismissLoadhud(getActivity());
                ZeroLotteryEntity zeroLotteryEntity = GsonUtils.fromJson(result, ZeroLotteryEntity.class);
                if (zeroLotteryEntity != null) {
                    if (SUCCESS_CODE.equals(zeroLotteryEntity.getCode())) {
                        List<ZeroLotteryEntity.ZeroLotteryBean> zeroLotteryBeans = zeroLotteryEntity.getResult();
                        if (zeroLotteryBeans != null && zeroLotteryBeans.size() > 0) {
                            if (mAlertDialogZeroLottery == null) {
                                mAlertDialogZeroLottery = new AlertDialogZeroLottery(getActivity());
                            }
                            mAlertDialogZeroLottery.update(zeroLotteryBeans);
                            mAlertDialogZeroLottery.show(Gravity.BOTTOM);
                        }
                    } else {
                        showToast(zeroLotteryEntity.getMsg());
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                dismissLoadhud(getActivity());
            }
        });
    }

    @Override
    protected void loadData() {
        getZeroList();
    }

    private void getZeroList() {
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.GET_ZERO_LIST, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mSmartCommunalRefresh.finishRefresh();
                currentList.clear();
                beforeList.clear();
                mZeroListEntity = GsonUtils.fromJson(result, ZeroListEntity.class);
                if (mZeroListEntity != null) {
                    if (SUCCESS_CODE.equals(mZeroListEntity.getCode())) {
                        mZeroProductListAdapter.setCurrentTime(mZeroListEntity.getCurrentTime());
                        ZeroListEntity.ZeroListBean zeroListBean = mZeroListEntity.getResult();
                        if (zeroListBean != null) {
                            List<CurrentActivityListBean> currentActivityList = zeroListBean.getCurrentActivityList();
                            List<ZeroInfoBean> overdueGoodsList = zeroListBean.getOverdueGoodsList();
                            if (currentActivityList != null) {
                                currentList.addAll(currentActivityList);
                            }
                            if (overdueGoodsList != null) {
                                beforeList.addAll(overdueGoodsList);
                            }
                        }
                    } else {
                        showToast(mZeroListEntity.getMsg());
                    }
                }
                mZeroProductListAdapter.notifyDataSetChanged();
                mZeroProductAdapter.notifyDataSetChanged();
                showVisible();
            }

            @Override
            public void onNotNetOrException() {
                mSmartCommunalRefresh.finishRefresh();
                showVisible();
            }
        });
    }

    private void showVisible() {
        mLlZeroCurrent.setVisibility(currentList.size() > 0 ? View.VISIBLE : View.GONE);
        mLlZeroBefore.setVisibility(beforeList.size() > 0 ? View.VISIBLE : View.GONE);
        NetLoadUtils.getNetInstance().showLoadSir(loadService, currentList.size() != 0 || beforeList.size() != 0, mZeroListEntity);
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    public View getLoadView() {
        return mSmartCommunalRefresh;
    }

    @OnClick(R.id.tv_life_back)
    public void onViewClicked() {
        finish();
    }
}
