package com.amkj.dmsh.address.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.address.adapter.SelectedAddressAdapter;
import com.amkj.dmsh.address.bean.AddressInfoEntity.AddressInfoBean;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.integration.bean.AddressListEntity;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
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
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStringChangeBoolean;
import static com.amkj.dmsh.constant.ConstantMethod.getStringChangeIntegers;
import static com.amkj.dmsh.constant.ConstantMethod.showLoadhud;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.showToastRequestMsg;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.CREATE_ADDRESS_REQ;
import static com.amkj.dmsh.constant.ConstantVariable.EDIT_ADDRESS_REQ;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.CHANGE_ORDER_ADDRESS;
import static com.amkj.dmsh.constant.Url.DEL_ADDRESS;
import static com.amkj.dmsh.constant.Url.UPDATE_DEFAULT_ADDRESS;


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
    private List<AddressInfoBean> addressAllBeanList = new ArrayList<>();
    private SelectedAddressAdapter selectedAddressAdapter;
    private boolean isMineSkip;
    //    是否展示 删除按钮
    private boolean isShowDel = true;
    private int addressId;
    private String orderNo;//不为空表示修改订单收货地址
    private AlertDialog dialog;
    private AddressListEntity addressListEntity;
    private boolean isFirst = true;

    @Override
    protected int getContentView() {
        return R.layout.activity_selected_address;
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
        header_shared.setCompoundDrawables(null, null, null, null);
        header_shared.setText("新增");
        Intent intent = getIntent();
        isMineSkip = getStringChangeBoolean(intent.getStringExtra("isMineSkip"));
        tvHeaderTitle.setText(isMineSkip ? "我的地址" : "选择地址");
        addressId = getStringChangeIntegers(intent.getStringExtra("addressId"));
        orderNo = intent.getStringExtra("orderNo");
        if (!isMineSkip || addressId > 0) {
            isShowDel = false;
        }
        selectedAddressAdapter = new SelectedAddressAdapter(addressAllBeanList, isShowDel, isMineSkip);
        //        获取头部消息列表
        communal_recycler.setLayoutManager(new LinearLayoutManager(this));
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_ten_dp).create());
        communal_recycler.setAdapter(selectedAddressAdapter);
        smart_communal_refresh.setOnRefreshListener(refreshLayout -> loadData());
        selectedAddressAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                AddressInfoBean addressInfoBean = (AddressInfoBean) view.getTag();
                if (addressInfoBean != null && !isMineSkip) {
                    goBackAddress(addressInfoBean);
                }
            }
        });
        selectedAddressAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                final AddressInfoBean addressInfoBean = (AddressInfoBean) view.getTag();
                if (addressInfoBean != null) {
                    switch (view.getId()) {
                        //修改默认地址
                        case R.id.cb_address_selected_item:
                            CheckBox cb_address_selected_item = (CheckBox) view;
                            if (addressInfoBean.getStatus() != 1) {
                                for (int i = 0; i < addressAllBeanList.size(); i++) {
                                    if (i == position) {
                                        addressAllBeanList.get(i).setStatus(1);
                                    } else {
                                        addressAllBeanList.get(i).setStatus(0);
                                    }
                                }
                                selectedAddressAdapter.notifyDataSetChanged();
                                upDateDefaultAddress(addressInfoBean);
                            } else {
                                cb_address_selected_item.setChecked(true);
                            }
                            break;
                        //编辑地址
                        case R.id.tv_address_item_edit:
                            Intent addressData = new Intent(SelectedAddressActivity.this, AddressNewCreatedActivity.class);
                            addressData.putExtra("addressId", String.valueOf(addressInfoBean.getId()));
                            startActivityForResult(addressData, EDIT_ADDRESS_REQ);
                            break;
                        //删除地址
                        case R.id.tv_address_item_del:
                            if (addressInfoBean.getStatus() == 1) {
                                showToast("不能删除默认地址,请先设置其它地址为默认地址");
                            } else {
                                // 创建
                                dialog = new AlertDialog.Builder(SelectedAddressActivity.this)
                                        .setTitle("删除收货地址")
                                        .setMessage("确定删除这个收货地址吗?")
                                        .setPositiveButton("确定", (dialog, which) -> delAddress(addressInfoBean.getId()))
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
    protected void loadData() {
        if (userId < 1) {
            return;
        }
        String url = Url.ADDRESS_LIST;
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(SelectedAddressActivity.this, url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                smart_communal_refresh.finishRefresh();
                selectedAddressAdapter.loadMoreComplete();
                addressListEntity = GsonUtils.fromJson(result, AddressListEntity.class);
                if (addressListEntity != null) {
                    if (addressListEntity.getCode().equals(SUCCESS_CODE)) {
                        addressAllBeanList.clear();
                        addressAllBeanList.addAll(addressListEntity.getAddressAllBeanList());
                        selectedAddressAdapter.notifyDataSetChanged();
                    } else if (!addressListEntity.getCode().equals(EMPTY_CODE)) {
                        showToast( addressListEntity.getMsg());
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
            public void onError(Throwable throwable) {
                showToast( R.string.invalidData);
            }
        });
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
            AddressInfoBean addressInfoBean = data.getParcelableExtra("addressInfoBean");
            if (!isMineSkip && addressInfoBean != null) {
                goBackAddress(addressInfoBean);
            }
        }
    }

    private void goBackAddress(AddressInfoBean addressInfoBean) {
        if (!TextUtils.isEmpty(orderNo)) {
            changeOrderAddress(addressInfoBean.getId());
        } else {
            Intent dataIntent = new Intent();
            dataIntent.putExtra("addressInfoBean", addressInfoBean);
            setResult(RESULT_OK, dataIntent);
            finish();
        }
    }

    //  修改收货地址
    public void changeOrderAddress(int userAddressId) {
        showLoadhud(getActivity());
        Map<String, Object> params = new HashMap<>();
        params.put("orderNo", orderNo);
        params.put("userAddressId", userAddressId);
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), CHANGE_ORDER_ADDRESS, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                dismissLoadhud(getActivity());

                RequestStatus status = GsonUtils.fromJson(result, RequestStatus.class);
                if (status != null) {
                    if (SUCCESS_CODE.equals(status.getCode())) {
                        Intent dataIntent = new Intent();
                        setResult(RESULT_OK, dataIntent);
                        finish();
                    } else {
                        showToast( status.getMsg());
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                dismissLoadhud(getActivity());
                showToast( R.string.do_failed);
            }
        });
    }


    //  删除地址
    private void delAddress(int id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, DEL_ADDRESS, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {

                RequestStatus status = GsonUtils.fromJson(result, RequestStatus.class);
                if (status != null) {
                    if (status.getCode().equals(SUCCESS_CODE)) {
                        showToast( "删除地址完成");
                        loadData();
                    } else if (!status.getCode().equals(EMPTY_CODE)) {
                        showToastRequestMsg( status);
                    }
                }
            }
        });
    }

    private void upDateDefaultAddress(final AddressInfoBean addressInfoBean) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", addressInfoBean.getId());
        params.put("user_id", addressInfoBean.getUser_id());
        NetLoadUtils.getNetInstance().loadNetDataPost(this, UPDATE_DEFAULT_ADDRESS, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {

                RequestStatus status = GsonUtils.fromJson(result, RequestStatus.class);
                if (status != null) {
                    if (status.getCode().equals(SUCCESS_CODE)) {
                        showToast("修改默认地址完成");
                        if (!isMineSkip) {
                            goBackAddress(addressInfoBean);
                        } else {
                            loadData();
                        }
                    } else if (!status.getCode().equals(EMPTY_CODE)) {
                        showToastRequestMsg( status);
                    }
                }
            }
        });
    }


    //  新增地址
    @OnClick(R.id.tv_header_shared)
    void addAddress() {
        Intent skipNewCreate = new Intent(SelectedAddressActivity.this, AddressNewCreatedActivity.class);
        startActivityForResult(skipNewCreate, CREATE_ADDRESS_REQ);
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
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
        if (!isFirst) {
            loadData();
        }
        isFirst = false;
    }
}
