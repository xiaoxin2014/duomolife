package com.amkj.dmsh.homepage.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.homepage.adapter.IntegralLotteryAwardHistoryAdapter;
import com.amkj.dmsh.homepage.bean.IntegralLotteryAwardHistoryEntity;
import com.amkj.dmsh.homepage.bean.IntegralLotteryEntity.PreviousInfoBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.H_ATTENDANCE_INTEGRAL_LOTTERY_AWARD_HISTORY;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/7/30
 * version 3.1.5
 * class description:往期夺宝
 */
public class IntegralLotteryAwardHistoryActivity extends BaseActivity {
    @BindView(R.id.tv_header_shared)
    TextView header_shared;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tl_normal_bar)
    Toolbar tl_normal_bar;
    @BindView(R.id.smart_communal_refresh)
    RefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;

    private IntegralLotteryAwardHistoryAdapter integralLotteryAwardHistoryAdapter;
    private List<PreviousInfoBean> lotteryInfoListBeanList = new ArrayList<>();
    private boolean isOnPause;
    private IntegralLotteryAwardHistoryEntity integralLotteryAwardEntity;

    @Override
    protected int getContentView() {
        return R.layout.activity_integral_get;
    }

    @Override
    protected void initViews() {
        getLoginStatus(IntegralLotteryAwardHistoryActivity.this);
        header_shared.setVisibility(View.GONE);
        tv_header_titleAll.setText("每日夺宝");
        communal_recycler.setBackgroundColor(getResources().getColor(R.color.light_gray_f));
        communal_recycler.setPadding(0, AutoSizeUtils.mm2px(mAppContext, 10), 0, 0);
        communal_recycler.setLayoutManager(new LinearLayoutManager(IntegralLotteryAwardHistoryActivity.this));
        integralLotteryAwardHistoryAdapter = new IntegralLotteryAwardHistoryAdapter(IntegralLotteryAwardHistoryActivity.this, lotteryInfoListBeanList);
        communal_recycler.setAdapter(integralLotteryAwardHistoryAdapter);
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_gray_f).create());
        integralLotteryAwardHistoryAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PreviousInfoBean previousInfoBean = (PreviousInfoBean) view.getTag();
                if (previousInfoBean != null && previousInfoBean.getWinList() != null
                        && previousInfoBean.getWinList().size() > 0) {
                    Intent intent = new Intent(IntegralLotteryAwardHistoryActivity.this, IntegralLotteryAwardPersonActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("lotteryWinner", (ArrayList<? extends Parcelable>) previousInfoBean.getWinList());
                    startActivity(intent);
                }
            }
        });
        integralLotteryAwardHistoryAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                PreviousInfoBean previousInfoBean = (PreviousInfoBean) view.getTag();
                if (previousInfoBean != null && previousInfoBean.getWinList() != null
                        && previousInfoBean.getWinList().size() > 0) {
                    if (view.getId() == R.id.ll_lottery_award) {
                        Intent intent = new Intent(IntegralLotteryAwardHistoryActivity.this, IntegralLotteryAwardPersonActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList("lotteryWinner", (ArrayList<? extends Parcelable>) previousInfoBean.getWinList());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
            }
        });
        smart_communal_refresh.setOnRefreshListener(refreshLayout -> getIntegralLotteryAwardData());
    }

    @Override
    protected void loadData() {
        getIntegralLotteryAwardData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isOnPause) {
            getIntegralLotteryAwardData();
            isOnPause = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isOnPause = true;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    private void getIntegralLotteryAwardData() {
        if (userId < 1) {
            NetLoadUtils.getNetInstance().showLoadSirSuccess(loadService);
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(IntegralLotteryAwardHistoryActivity.this,
                H_ATTENDANCE_INTEGRAL_LOTTERY_AWARD_HISTORY
                , params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();
                        lotteryInfoListBeanList.clear();
                        Gson gson = new Gson();
                        integralLotteryAwardEntity = gson.fromJson(result, IntegralLotteryAwardHistoryEntity.class);
                        if (integralLotteryAwardEntity != null) {
                            if (SUCCESS_CODE.equals(integralLotteryAwardEntity.getCode())) {
                                lotteryInfoListBeanList.addAll(integralLotteryAwardEntity.getOverLotteryInfoList());
                                integralLotteryAwardHistoryAdapter.notifyDataSetChanged();
                            } else {
                                smart_communal_refresh.finishRefresh();
                                showToast(IntegralLotteryAwardHistoryActivity.this, integralLotteryAwardEntity.getMsg());
                            }
                        }
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, lotteryInfoListBeanList,integralLotteryAwardEntity);
                    }

                    @Override
                    public void onNotNetOrException() {
                        smart_communal_refresh.finishRefresh();
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, lotteryInfoListBeanList,integralLotteryAwardEntity);
                    }

                    @Override
                    public void netClose() {
                        showToast(IntegralLotteryAwardHistoryActivity.this, R.string.unConnectedNetwork);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IS_LOGIN_CODE) {
            getIntegralLotteryAwardData();
        }
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }
}
