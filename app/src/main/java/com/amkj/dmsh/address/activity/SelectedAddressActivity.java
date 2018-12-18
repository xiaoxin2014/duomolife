package com.amkj.dmsh.address.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.address.adapter.SelectedAddressAdapter;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.integration.bean.AddressListEntity;
import com.amkj.dmsh.shopdetails.integration.bean.AddressListEntity.AddressListBean;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
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
import static com.amkj.dmsh.constant.ConstantMethod.getStringChangeBoolean;
import static com.amkj.dmsh.constant.ConstantMethod.getStringChangeIntegers;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.DEL_ADDRESS;
import static com.amkj.dmsh.constant.Url.UPDATE_DEFAULT_ADDRESS;

;

/**
 * @author Liuguipeng
 * description 地址选择
 */
public class SelectedAddressActivity extends BaseActivity {
    @BindView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @BindView(R.id.tv_header_shared)
    TextView header_shared;
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    private List<AddressListBean> addressAllBeanList = new ArrayList<>();
    private SelectedAddressAdapter selectedAddressAdapter;
    private int CREATE_ADDRESS_REQ = 103;
    private int EDIT_ADDRESS_REQ = 104;
    private boolean isMineSkip;
    //    是否展示 删除按钮
    private boolean isShowDel = true;
    private int addressId;
    private AlertDialog dialog;
    private AddressListEntity addressListEntity;

    @Override
    protected int getContentView() {
        return R.layout.activity_selected_address;
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
        tvHeaderTitle.setText("选择地址");
        header_shared.setCompoundDrawables(null, null, null, null);
        header_shared.setText("新增");
        Intent intent = getIntent();
        isMineSkip = getStringChangeBoolean(intent.getStringExtra("isMineSkip"));
        addressId = getStringChangeIntegers(intent.getStringExtra("addressId"));
        if (!isMineSkip || addressId > 0) {
            isShowDel = false;
        }
        selectedAddressAdapter = new SelectedAddressAdapter(addressAllBeanList, isShowDel);
        //        获取头部消息列表
        communal_recycler.setLayoutManager(new LinearLayoutManager(this));
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_ten_dp).create());
        communal_recycler.setAdapter(selectedAddressAdapter);
        smart_communal_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                loadData();
            }
        });
        selectedAddressAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                AddressListBean addressAllBean = (AddressListBean) view.getTag();
                if (addressAllBean != null && !isMineSkip) {
                    goBackAddress(addressAllBean.getId());
                }
            }
        });
        selectedAddressAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                final AddressListBean addressAllBean = (AddressListBean) view.getTag();
                if (addressAllBean != null) {
                    switch (view.getId()) {
                        case R.id.cb_address_selected_item:
                            CheckBox cb_address_selected_item = (CheckBox) view;
                            if (addressAllBean.getStatus() != 1) {
                                for (int i = 0; i < addressAllBeanList.size(); i++) {
                                    if (i == position) {
                                        addressAllBeanList.get(i).setStatus(1);
                                    } else {
                                        addressAllBeanList.get(i).setStatus(0);
                                    }
                                }
                                selectedAddressAdapter.notifyDataSetChanged();
//                    修改默认地址
                                upDateDefaultAddress(addressAllBean);
                            } else {
                                cb_address_selected_item.setChecked(true);
                            }
                            break;
                        case R.id.tv_address_item_edit:
                            Intent addressData = new Intent(SelectedAddressActivity.this, AddressNewCreatedActivity.class);
                            addressData.putExtra("addressId", String.valueOf(addressAllBean.getId()));
                            addressData.putExtra("uid", addressAllBean.getUser_id());
                            startActivityForResult(addressData, EDIT_ADDRESS_REQ);
                            break;
                        case R.id.tv_address_item_del:
                            if (addressAllBean.getStatus() == 1) {
                                showToast(SelectedAddressActivity.this, "不能删除默认地址,请先设置其它地址为默认地址");
                            } else {
                                // 创建
                                dialog = new AlertDialog.Builder(SelectedAddressActivity.this)
                                        .setTitle("删除收货地址")
                                        .setMessage("确定删除这个收货地址吗?")
                                        .setPositiveButton("确定", (dialog, which) -> delAddress(addressAllBean.getId()))
                                        .setNegativeButton("取消", (dialog, which) -> dialog.cancel())
                                        .create();
                                dialog.setCanceledOnTouchOutside(false);
                                // 显示对话框
                                dialog.show();
                            }
                            break;
                    }
                }
            }
        });
    }

    @Override
    protected View getLoadView() {
        return smart_communal_refresh;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    //  新增地址

    @OnClick(R.id.tv_header_shared)
    void addAddress() {
        Intent skipNewCreate = new Intent(SelectedAddressActivity.this, AddressNewCreatedActivity.class);
        startActivityForResult(skipNewCreate, CREATE_ADDRESS_REQ);
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
        if (requestCode == CREATE_ADDRESS_REQ || requestCode == EDIT_ADDRESS_REQ) {
            int addressId = data.getIntExtra("addressId", 0);
            if (!isMineSkip && addressId != 0) {
                goBackAddress(addressId);
            } else {
                loadData();
            }
        }
    }

    private void goBackAddress(int addressId) {
        Intent dataIntent = new Intent();
        dataIntent.putExtra("addressId", addressId);
        setResult(RESULT_OK, dataIntent);
        finish();
    }
    //  删除地址

    private void delAddress(int id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        NetLoadUtils.getNetInstance().loadNetDataPost(this,DEL_ADDRESS,params,new NetLoadListenerHelper(){
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                RequestStatus status = gson.fromJson(result, RequestStatus.class);
                if (status != null) {
                    if (status.getCode().equals(SUCCESS_CODE)) {
                        showToast(SelectedAddressActivity.this, "删除地址完成");
                        loadData();
                    } else if (!status.getCode().equals(EMPTY_CODE)) {
                        showToast(SelectedAddressActivity.this, status.getMsg());
                    }
                }
            }
        });
    }

    @Override
    protected void loadData() {
        if(userId<0){
            return;
        }
        String url = Url.BASE_URL + Url.ADDRESS_LIST;
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(SelectedAddressActivity.this, url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                smart_communal_refresh.finishRefresh();
                selectedAddressAdapter.loadMoreComplete();
                Gson gson = new Gson();
                addressListEntity = gson.fromJson(result, AddressListEntity.class);
                if (addressListEntity != null) {
                    if (addressListEntity.getCode().equals(SUCCESS_CODE)) {
                        addressAllBeanList.clear();
                        addressAllBeanList.addAll(addressListEntity.getAddressAllBeanList());
                        selectedAddressAdapter.notifyDataSetChanged();
                    } else if (!addressListEntity.getCode().equals(EMPTY_CODE)) {
                        showToast(SelectedAddressActivity.this, addressListEntity.getMsg());
                    }
                }
                NetLoadUtils.getNetInstance().showLoadSir(loadService, addressAllBeanList, addressListEntity);
            }

            @Override
            public void onNotNetOrException() {
                smart_communal_refresh.finishRefresh();
                selectedAddressAdapter.loadMoreEnd(true);
                NetLoadUtils.getNetInstance().showLoadSir(loadService, addressAllBeanList, addressListEntity);
            }

            @Override
            public void netClose() {
                showToast(SelectedAddressActivity.this, R.string.unConnectedNetwork);
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(SelectedAddressActivity.this, R.string.invalidData);
            }
        });
    }

    private void upDateDefaultAddress(final AddressListBean addressAllBean) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", addressAllBean.getId());
        params.put("user_id", addressAllBean.getUser_id());
        NetLoadUtils.getNetInstance().loadNetDataPost(this,UPDATE_DEFAULT_ADDRESS,params,new NetLoadListenerHelper(){
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                RequestStatus status = gson.fromJson(result, RequestStatus.class);
                if (status != null) {
                    if (status.getCode().equals(SUCCESS_CODE)) {
                        showToast(SelectedAddressActivity.this, "修改默认地址完成");
                        if (!isMineSkip) {
                            goBackAddress(addressAllBean.getId());
                        } else {
                            loadData();
                        }
                    } else if (!status.getCode().equals(EMPTY_CODE)) {
                        showToast(SelectedAddressActivity.this, status.getMsg());
                    }
                }
            }

            @Override
            public void netClose() {
                showToast(SelectedAddressActivity.this,R.string.unConnectedNetwork);
            }
        });
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }
}
