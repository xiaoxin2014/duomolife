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
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.homepage.adapter.IntegralGetAdapter;
import com.amkj.dmsh.homepage.bean.IntegralGetEntity;
import com.amkj.dmsh.homepage.bean.IntegralGetEntity.IntegralGetBean;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
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
    RefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    @BindView(R.id.fl_integral_get_hint)
    FrameLayout fl_integral_get_hint;

    @BindView(R.id.communal_load)
    View communal_load;
    @BindView(R.id.communal_error)
    View communal_error;
    @BindView(R.id.communal_empty)
    View communal_empty;
    private IntegralGetAdapter integralGetAdapter;
    private List<IntegralGetBean> integralGetBeanList = new ArrayList<>();

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
                if(integralGetBean!=null&&integralGetBean.getButtonFlag()==0){
                    setSkipPath(IntegralGetActivity.this,integralGetBean.getAndroidLink(),false);
                }
            }
        });
        smart_communal_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                getIntegralGetData();
            }
        });
        communal_load.setVisibility(View.VISIBLE);
    }

    @Override
    protected void loadData() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        getIntegralGetData();
    }

    private void getIntegralGetData() {
        if(userId>0){
            if (NetWorkUtils.checkNet(IntegralGetActivity.this)) {
                String url = Url.BASE_URL + Url.H_ATTENDANCE_GET;
                Map<String, Object> params = new HashMap<>();
                params.put("uid", userId);
                XUtil.Post(url, params, new MyCallBack<String>() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();
                        communal_load.setVisibility(View.GONE);
                        communal_empty.setVisibility(View.GONE);
                        communal_error.setVisibility(View.GONE);
                        integralGetBeanList.clear();
                        Gson gson = new Gson();
                        IntegralGetEntity attendanceDetailEntity = gson.fromJson(result, IntegralGetEntity.class);
                        if (attendanceDetailEntity != null) {
                            if (SUCCESS_CODE.equals(attendanceDetailEntity.getCode())) {
                                integralGetBeanList.addAll(attendanceDetailEntity.getIntegralGetList());
                                integralGetAdapter.notifyDataSetChanged();
                                fl_integral_get_hint.setVisibility(View.VISIBLE);
                            }else{
                                communal_error.setVisibility(View.VISIBLE);
                                smart_communal_refresh.finishRefresh();
                                fl_integral_get_hint.setVisibility(View.GONE);
                                showToast(IntegralGetActivity.this,attendanceDetailEntity.getMsg());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        super.onError(ex, isOnCallback);
                        communal_error.setVisibility(View.VISIBLE);
                        smart_communal_refresh.finishRefresh();
                        fl_integral_get_hint.setVisibility(View.GONE);
                    }
                });
            }else{
                smart_communal_refresh.finishRefresh();
                communal_load.setVisibility(View.GONE);
                communal_empty.setVisibility(View.VISIBLE);
                fl_integral_get_hint.setVisibility(View.GONE);
                showToast(IntegralGetActivity.this, R.string.unConnectedNetwork);
            }
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
            getIntegralGetData();
        }
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }

    @OnClick({R.id.rel_communal_error, R.id.communal_empty})
    void refreshData(View view) {
        communal_load.setVisibility(View.VISIBLE);
        communal_empty.setVisibility(View.GONE);
        communal_error.setVisibility(View.GONE);
        loadData();
    }
}
