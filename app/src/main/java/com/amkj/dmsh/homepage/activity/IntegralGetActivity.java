package com.amkj.dmsh.homepage.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.homepage.adapter.IntegralGetAdapter;
import com.amkj.dmsh.homepage.bean.IntegralGetEntity;
import com.amkj.dmsh.homepage.bean.IntegralGetEntity.IntegralGetBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.setSkipPath;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.H_ATTENDANCE_GET;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/7/23
 * version 3.1.5
 * class description:积分获取
 */
public class IntegralGetActivity extends BaseActivity {
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
    @BindView(R.id.fl_integral_get_hint)
    FrameLayout fl_integral_get_hint;
    private IntegralGetAdapter integralGetAdapter;
    private List<IntegralGetBean> integralGetBeanList = new ArrayList<>();
    private boolean isOnPause;
    private IntegralGetEntity attendanceDetailEntity;

    @Override
    protected int getContentView() {
        return R.layout.activity_integral_get;
    }

    @Override
    protected void initViews() {
        getLoginStatus(IntegralGetActivity.this);
        header_shared.setVisibility(View.GONE);
        tv_header_titleAll.setText("赚积分");
        communal_recycler.setLayoutManager(new LinearLayoutManager(IntegralGetActivity.this));
        integralGetAdapter = new IntegralGetAdapter(integralGetBeanList);
        communal_recycler.setAdapter(integralGetAdapter);
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_gray_f_one_px).create());
        integralGetAdapter.setOnItemClickListener((adapter, view, position) -> {
            IntegralGetBean integralGetBean = (IntegralGetBean) view.getTag();
            if (integralGetBean != null && integralGetBean.getButtonFlag() == 0) {
                setSkipPath(IntegralGetActivity.this, integralGetBean.getAndroidLink(), false);
            }
        });
        smart_communal_refresh.setOnRefreshListener(refreshLayout -> loadData());
    }

    @Override
    protected void loadData() {
        getIntegralGetData();
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
    protected void onResume() {
        super.onResume();
        if (isOnPause) {
            loadData();
            isOnPause = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isOnPause = true;
    }

    private void getIntegralGetData() {
        if (userId < 1) {
            NetLoadUtils.getNetInstance().showLoadSirSuccess(loadService);
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(IntegralGetActivity.this, H_ATTENDANCE_GET
                , params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();
                        integralGetBeanList.clear();
                        Gson gson = new Gson();
                        attendanceDetailEntity = gson.fromJson(result, IntegralGetEntity.class);
                        if (attendanceDetailEntity != null) {
                            if (SUCCESS_CODE.equals(attendanceDetailEntity.getCode())) {
                                integralGetBeanList.addAll(attendanceDetailEntity.getIntegralGetList());
                                integralGetAdapter.notifyDataSetChanged();
                                fl_integral_get_hint.setVisibility(View.VISIBLE);
                            } else {
                                smart_communal_refresh.finishRefresh();
                                fl_integral_get_hint.setVisibility(View.GONE);
                                showToast(IntegralGetActivity.this, attendanceDetailEntity.getMsg());
                            }
                        }
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, integralGetBeanList, attendanceDetailEntity);
                    }

                    @Override
                    public void onNotNetOrException() {
                        smart_communal_refresh.finishRefresh();
                        fl_integral_get_hint.setVisibility(View.GONE);
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, integralGetBeanList, attendanceDetailEntity);

                    }

                    @Override
                    public void netClose() {
                        showToast(IntegralGetActivity.this, R.string.unConnectedNetwork);
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
