package com.amkj.dmsh.mine.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.mine.adapter.MesPushTypeSetAdapter;
import com.amkj.dmsh.mine.bean.MesPushTypeEntity;
import com.amkj.dmsh.mine.bean.MesPushTypeEntity.MesPushTypeBean;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static android.view.View.GONE;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;

;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/9/28
 * update on 2018/1/10
 * class description:消息推送类型
 */
public class MessagePushTypeActivity extends BaseActivity {
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
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tv_header_shared)
    TextView tv_header_shared;
    private int uid;
    private List<MesPushTypeBean> mesPushTypeList = new ArrayList<>();
    private MesPushTypeSetAdapter mesPushTypeSetAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_communal_header_recycler_refresh;
    }

    @Override
    protected void initViews() {
        getLoginStatus();
        tv_header_shared.setVisibility(GONE);
        tv_header_titleAll.setText("消息推送");

        smart_communal_refresh.setOnRefreshListener((refreshLayout) -> {
            loadData();

        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MessagePushTypeActivity.this);
        communal_recycler.setLayoutManager(linearLayoutManager);
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
        mesPushTypeSetAdapter = new MesPushTypeSetAdapter(mesPushTypeList);
        mesPushTypeSetAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MesPushTypeBean mesPushTypeBean = (MesPushTypeBean) view.getTag();
                if (mesPushTypeBean != null) {
                    loadHud.show();
                    changeMesPushType(mesPushTypeBean);
                }
            }
        });
        communal_recycler.setAdapter(mesPushTypeSetAdapter);
        communal_load.setVisibility(View.VISIBLE);
    }


    private void getLoginStatus() {
        SavePersonalInfoBean personalInfo = ConstantMethod.getPersonalInfo(this);
        if (personalInfo.isLogin()) {
            uid = personalInfo.getUid();
        } else {
            //未登录跳转登录页
            Intent intent = new Intent(this, MineLoginActivity.class);
            startActivityForResult(intent, ConstantVariable.IS_LOGIN_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            if (requestCode == IS_LOGIN_CODE) {
                finish();
            } else {
                return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IS_LOGIN_CODE) {
                getLoginStatus();
                loadData();
            }
        }
    }

    private void changeMesPushType(final MesPushTypeBean mesPushTypeOldBean) {
        String url = Url.BASE_URL + Url.MINE_MES_PUSH_SWITCH;
        Map<String, Object> params = new HashMap<>();
        //用户id
        params.put("uid", uid);
        StringBuffer stringBuffer = new StringBuffer();
        for (MesPushTypeBean mesPushTypeBean : mesPushTypeList) {
            if (mesPushTypeOldBean.getId() == mesPushTypeBean.getId()) {
                mesPushTypeBean.setIsOpen(mesPushTypeOldBean.getIsOpen() == 0 ? 1 : 0);
            }
            if (mesPushTypeBean.getIsOpen() == 1) {
                if (!TextUtils.isEmpty(stringBuffer.toString())) {
                    stringBuffer.append("," + mesPushTypeBean.getId());
                } else {
                    stringBuffer.append(mesPushTypeBean.getId());
                }
            }
        }
        params.put("ids", stringBuffer.toString().trim());
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                loadHud.dismiss();
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals("01")) {
                        loadData();
                        showToast(MessagePushTypeActivity.this, String.format(getResources().getString(R.string.doSuccess), "修改"));
                    } else {
                        showToast(MessagePushTypeActivity.this, requestStatus.getMsg());
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                loadHud.dismiss();
                showToast(MessagePushTypeActivity.this, R.string.do_failed);
                super.onError(ex, isOnCallback);
            }
        });
    }

    @Override
    protected void loadData() {
        if (NetWorkUtils.checkNet(MessagePushTypeActivity.this)) {
            String url = Url.BASE_URL + Url.MINE_MES_PUSH_LIST;
            Map<String, Object> params = new HashMap<>();
            params.put("uid", uid);
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    smart_communal_refresh.finishRefresh();
                    communal_load.setVisibility(GONE);
                    communal_error.setVisibility(GONE);
                    mesPushTypeList.clear();
                    Gson gson = new Gson();
                    MesPushTypeEntity mesPushTypeEntity = gson.fromJson(result, MesPushTypeEntity.class);
                    if (mesPushTypeEntity != null) {
                        if (mesPushTypeEntity.getCode().equals("01")) {
                            for (MesPushTypeBean mesPushTypeBean : mesPushTypeEntity.getMesPushTypeBeanList()) {
                                if (mesPushTypeBean.getStatus() == 1) {
                                    mesPushTypeList.add(mesPushTypeBean);
                                }
                            }
                            mesPushTypeSetAdapter.setNewData(mesPushTypeList);
                        } else if (!mesPushTypeEntity.getCode().equals("02")) {
                            showToast(MessagePushTypeActivity.this, mesPushTypeEntity.getMsg());
                        }
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    smart_communal_refresh.finishRefresh();
                    communal_load.setVisibility(GONE);
                    communal_error.setVisibility(View.VISIBLE);
                    showToast(MessagePushTypeActivity.this, R.string.invalidData);
                    super.onError(ex, isOnCallback);
                }
            });
        } else {
            smart_communal_refresh.finishRefresh();
            communal_load.setVisibility(GONE);
            communal_error.setVisibility(View.VISIBLE);
            showToast(MessagePushTypeActivity.this, R.string.unConnectedNetwork);
        }
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }

    @OnClick({R.id.rel_communal_error, R.id.communal_empty})
    void refreshData(View view) {
        communal_load.setVisibility(View.VISIBLE);
        communal_empty.setVisibility(GONE);
        communal_error.setVisibility(GONE);
        loadData();
    }

}
