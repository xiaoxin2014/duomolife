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
import com.amkj.dmsh.base.NetLoadUtils;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.homepage.adapter.IntegralGetAdapter;
import com.amkj.dmsh.homepage.bean.IntegralGetEntity;
import com.amkj.dmsh.homepage.bean.IntegralGetEntity.IntegralGetBean;
import com.amkj.dmsh.utils.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

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
        integralGetAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                IntegralGetBean integralGetBean = (IntegralGetBean) view.getTag();
                if (integralGetBean != null && integralGetBean.getButtonFlag() == 0) {
                    setSkipPath(IntegralGetActivity.this, integralGetBean.getAndroidLink(), false);
                }
            }
        });
        smart_communal_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                loadData();
            }
        });
    }

    @Override
    protected void loadData() {
        getIntegralGetData();
    }

    @Override
    protected View getLoadView() {
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
        if (userId > 0) {
            String url = Url.BASE_URL + Url.H_ATTENDANCE_GET;
            Map<String, Object> params = new HashMap<>();
            params.put("uid", userId);
            NetLoadUtils.getQyInstance().loadNetDataPost(IntegralGetActivity.this, url
                    , params, new NetLoadUtils.NetLoadListener() {
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
                    NetLoadUtils.getQyInstance().showLoadSir(loadService,integralGetBeanList,attendanceDetailEntity);
                }

                @Override
                public void netClose() {
                    smart_communal_refresh.finishRefresh();
                    fl_integral_get_hint.setVisibility(View.GONE);
                    showToast(IntegralGetActivity.this, R.string.unConnectedNetwork);
                    NetLoadUtils.getQyInstance().showLoadSir(loadService,integralGetBeanList,attendanceDetailEntity);
                }

                @Override
                public void onError(Throwable throwable) {
                    smart_communal_refresh.finishRefresh();
                    fl_integral_get_hint.setVisibility(View.GONE);
                    NetLoadUtils.getQyInstance().showLoadSir(loadService,integralGetBeanList,attendanceDetailEntity);
                }
            });
        }else{
            NetLoadUtils.getQyInstance().showLoadSirSuccess(loadService);
        }
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
