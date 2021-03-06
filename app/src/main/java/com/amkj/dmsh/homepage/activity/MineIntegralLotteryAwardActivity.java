package com.amkj.dmsh.homepage.activity;

import android.content.Intent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.homepage.adapter.IntegralLotteryAwardAdapter;
import com.amkj.dmsh.homepage.bean.IntegralLotteryAwardEntity;
import com.amkj.dmsh.homepage.bean.IntegralLotteryAwardEntity.LotteryInfoListBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

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
import static com.amkj.dmsh.constant.Url.H_ATTENDANCE_INTEGRAL_LOTTERY_AWARD;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/7/30
 * version 3.1.5
 * class description:积分夺宝奖励
 */
public class MineIntegralLotteryAwardActivity extends BaseActivity {
    @BindView(R.id.tv_header_shared)
    TextView header_shared;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tl_normal_bar)
    Toolbar tl_normal_bar;
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;

    private IntegralLotteryAwardAdapter integralLotteryAwardAdapter;
    private List<LotteryInfoListBean> lotteryInfoListBeanList = new ArrayList<>();
    private boolean isOnPause;
    private IntegralLotteryAwardEntity integralLotteryAwardEntity;

    @Override
    protected int getContentView() {
        return R.layout.activity_integral_get;
    }

    @Override
    protected void initViews() {
        getLoginStatus(MineIntegralLotteryAwardActivity.this);
        header_shared.setVisibility(View.GONE);
        tv_header_titleAll.setText("我的奖励");
        communal_recycler.setBackgroundColor(getResources().getColor(R.color.light_gray_f));
        communal_recycler.setPadding(0, AutoSizeUtils.mm2px(mAppContext, 10), 0, 0);
        communal_recycler.setLayoutManager(new LinearLayoutManager(MineIntegralLotteryAwardActivity.this));
        integralLotteryAwardAdapter = new IntegralLotteryAwardAdapter(MineIntegralLotteryAwardActivity.this, lotteryInfoListBeanList);
        communal_recycler.setAdapter(integralLotteryAwardAdapter);
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_gray_f).create());
        View emptyView = LayoutInflater.from(this).inflate(R.layout.layout_communal_empty_image, null, false);
        integralLotteryAwardAdapter.setEmptyView(emptyView);
        integralLotteryAwardAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                LotteryInfoListBean lotteryInfoListBean = (LotteryInfoListBean) view.getTag();
                if (lotteryInfoListBean != null && lotteryInfoListBean.getStatus() == 2) {
                    Intent intent = new Intent(MineIntegralLotteryAwardActivity.this, IntegralLotteryAwardGetActivity.class);
                    intent.putExtra("activityId", String.valueOf(lotteryInfoListBean.getActivityId()));
                    startActivity(intent);
                }
            }
        });
        smart_communal_refresh.setOnRefreshListener(refreshLayout -> loadData());
    }

    @Override
    protected void loadData() {
        getIntegralLotteryAwardData();
    }

    @Override
    public View getLoadView() {
        return smart_communal_refresh;
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

    private void getIntegralLotteryAwardData() {
        if (userId < 1) {
            NetLoadUtils.getNetInstance().showLoadSirLoadFailed(loadService);
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(MineIntegralLotteryAwardActivity.this, H_ATTENDANCE_INTEGRAL_LOTTERY_AWARD
                , params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();
                        lotteryInfoListBeanList.clear();

                        integralLotteryAwardEntity = GsonUtils.fromJson(result, IntegralLotteryAwardEntity.class);
                        if (integralLotteryAwardEntity != null) {
                            if (SUCCESS_CODE.equals(integralLotteryAwardEntity.getCode())) {
                                lotteryInfoListBeanList.addAll(integralLotteryAwardEntity.getLotteryInfoList());
                                integralLotteryAwardAdapter.notifyDataSetChanged();
                            } else {
                                smart_communal_refresh.finishRefresh();
                                showToast( integralLotteryAwardEntity.getMsg());
                            }
                        }
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, integralLotteryAwardEntity);
                    }

                    @Override
                    public void onNotNetOrException() {
                        smart_communal_refresh.finishRefresh();
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, integralLotteryAwardEntity);
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
            loadData();
        }
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }
}
