package com.amkj.dmsh.mine.activity;

import android.content.Intent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.mine.adapter.MesPushTypeSetAdapter;
import com.amkj.dmsh.mine.bean.MesPushTypeEntity;
import com.amkj.dmsh.mine.bean.MesPushTypeEntity.MesPushTypeBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static android.view.View.GONE;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.showToastRequestMsg;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.MINE_MES_PUSH_LIST;
import static com.amkj.dmsh.constant.Url.MINE_MES_PUSH_SWITCH;

;
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
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tv_header_shared)
    TextView tv_header_shared;
    private List<MesPushTypeBean> mesPushTypeList = new ArrayList<>();
    private MesPushTypeSetAdapter mesPushTypeSetAdapter;
    private MesPushTypeEntity mesPushTypeEntity;

    @Override
    protected int getContentView() {
        return R.layout.activity_communal_header_recycler_refresh;
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
        tv_header_shared.setVisibility(GONE);
        tv_header_titleAll.setText("消息推送");

        smart_communal_refresh.setOnRefreshListener(refreshLayout -> loadData());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MessagePushTypeActivity.this);
        communal_recycler.setLayoutManager(linearLayoutManager);
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_gray_f_one_px).create());
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            if (requestCode == IS_LOGIN_CODE) {
                finish();
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IS_LOGIN_CODE) {
            loadData();
        }
    }

    private void changeMesPushType(final MesPushTypeBean mesPushTypeOldBean) {
        Map<String, Object> params = new HashMap<>();
        //用户id
        params.put("uid", userId);
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
        NetLoadUtils.getNetInstance().loadNetDataPost(this, MINE_MES_PUSH_SWITCH, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                loadHud.dismiss();

                RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        loadData();
                        showToast(String.format(getResources().getString(R.string.doSuccess), "修改"));
                    } else {
                        showToastRequestMsg(requestStatus);
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                loadHud.dismiss();
            }

            @Override
            public void onError(Throwable throwable) {
                showToast( R.string.do_failed);
            }
        });
    }

    @Override
    protected void loadData() {
        if (userId < 1) {
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, MINE_MES_PUSH_LIST
                , params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();

                        mesPushTypeEntity = GsonUtils.fromJson(result, MesPushTypeEntity.class);
                        if (mesPushTypeEntity != null) {
                            if (mesPushTypeEntity.getCode().equals(SUCCESS_CODE)) {
                                mesPushTypeList.clear();
                                for (MesPushTypeBean mesPushTypeBean : mesPushTypeEntity.getMesPushTypeBeanList()) {
                                    if (mesPushTypeBean.getStatus() == 1) {
                                        mesPushTypeList.add(mesPushTypeBean);
                                    }
                                }
                                mesPushTypeSetAdapter.setNewData(mesPushTypeList);
                            } else if (!mesPushTypeEntity.getCode().equals(EMPTY_CODE)) {
                                showToast( mesPushTypeEntity.getMsg());
                            }
                        }
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, mesPushTypeList, mesPushTypeEntity);
                    }

                    @Override
                    public void onNotNetOrException() {
                        smart_communal_refresh.finishRefresh();
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, mesPushTypeList, mesPushTypeEntity);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        showToast( R.string.invalidData);
                    }
                });
    }

    @Override
    public View getLoadView() {
        return smart_communal_refresh;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }

}
