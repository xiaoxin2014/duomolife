package com.amkj.dmsh.dominant.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dao.GroupDao;
import com.amkj.dmsh.dominant.adapter.QualityGroupMineAdapter;
import com.amkj.dmsh.dominant.bean.GroupShopDetailsEntity.GroupShopDetailsBean;
import com.amkj.dmsh.dominant.bean.QualityGroupMineEntity;
import com.amkj.dmsh.dominant.bean.QualityGroupMineEntity.QualityGroupMineBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.activity.DirectExchangeDetailsActivity;
import com.amkj.dmsh.views.alertdialog.AlertDialogGoPay;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.umeng.socialize.UMShareAPI;

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
import static com.amkj.dmsh.constant.ConstantMethod.showLoadhud;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TEN;
import static com.amkj.dmsh.constant.Url.GROUP_MINE_NEW_INDENT;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/6/8
 * class description:我的拼团
 */
public class QualityGroupShopMineActivity extends BaseActivity {
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
    private int page = 1;
    private List<QualityGroupMineBean> qualityGroupMineList = new ArrayList<>();
    private QualityGroupMineAdapter qualityGroupMineAdapter;
    private QualityGroupMineBean qualityGroupMineBean;
    private QualityGroupMineEntity qualityGroupMineEntity;
    private boolean firstSet = true;
    private AlertDialogGoPay mAlertDialogGoPay;

    @Override
    protected int getContentView() {
        return R.layout.activity_ql_gp_sp_list;
    }

    @Override
    protected void initViews() {
        getLoginStatus(QualityGroupShopMineActivity.this);
        tv_header_shared.setVisibility(View.GONE);
        tv_header_titleAll.setText("我的拼团");

        smart_communal_refresh.setOnRefreshListener(refreshLayout -> loadData());
        communal_recycler.setLayoutManager(new LinearLayoutManager(QualityGroupShopMineActivity.this));
        qualityGroupMineAdapter = new QualityGroupMineAdapter(QualityGroupShopMineActivity.this, qualityGroupMineList);
        communal_recycler.setAdapter(qualityGroupMineAdapter);
        qualityGroupMineAdapter.setOnLoadMoreListener(() -> {
            page++;
            getData();
        }, communal_recycler);
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_dp).create());
        setFloatingButton(download_btn_communal, communal_recycler);
        qualityGroupMineAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                qualityGroupMineBean = (QualityGroupMineBean) view.getTag();
                if (qualityGroupMineBean != null) {
                    switch (view.getId()) {
                        case R.id.tv_ql_gp_mine_inv_join:
//                            弹框
                            switch (qualityGroupMineBean.getGpStatus()) {
                                case 3:
                                case 4:
                                    if (userId > 0) {
                                        goPay(qualityGroupMineBean.getOrderNo());
                                    } else {
                                        getLoginStatus(QualityGroupShopMineActivity.this);
                                    }
                                    break;
                                case 1:
                                    if (qualityGroupMineBean != null) {
                                        GroupShopDetailsBean groupShopDetailsBean = new GroupShopDetailsBean();
                                        groupShopDetailsBean.setCoverImage(qualityGroupMineBean.getCoverImage());
                                        groupShopDetailsBean.setGpName(qualityGroupMineBean.getProductName());
                                        groupShopDetailsBean.setGpInfoId(qualityGroupMineBean.getGpInfoId());
                                        groupShopDetailsBean.setGpRecordId(qualityGroupMineBean.getGpRecordId());
                                        groupShopDetailsBean.setType(qualityGroupMineBean.getType());
                                        GroupDao.invitePartnerGroup(getActivity(), groupShopDetailsBean);
                                    }
                                    break;
                            }
                            break;
                        case R.id.tv_ql_gp_mine_details:
                            Intent intent = new Intent(QualityGroupShopMineActivity.this, DirectExchangeDetailsActivity.class);
                            intent.putExtra("orderNo", qualityGroupMineBean.getOrderNo());
                            intent.putExtra("gpType", qualityGroupMineBean.getType());//拼团类型
                            startActivity(intent);
                            break;
                    }
                }
            }
        });

        qualityGroupMineAdapter.setOnItemClickListener((adapter, view, position) -> {
            QualityGroupMineBean qualityGroupMineBean = (QualityGroupMineBean) view.getTag();
            if (qualityGroupMineBean != null) {
                Intent intent = new Intent(QualityGroupShopMineActivity.this, QualityGroupShopDetailActivity.class);
                intent.putExtra("gpInfoId", qualityGroupMineBean.getGpInfoId());
                intent.putExtra("productId", qualityGroupMineBean.getProductId());
                intent.putExtra("gpRecordId", qualityGroupMineBean.getGpRecordId());
                intent.putExtra("orderNo", qualityGroupMineBean.getOrderNo());
                intent.putExtra("gpStatus", String.valueOf(qualityGroupMineBean.getGpStatus()));
                startActivity(intent);
            }
        });
    }

    private void goPay(String orderNo) {
        showLoadhud(getActivity());
        Map<String, Object> map = new HashMap<>();
        map.put("orderNo", orderNo);
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Url.GET_PAYTYPE_LIST, map,new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                dismissLoadhud(getActivity());
                Map map = GsonUtils.fromJson(result, Map.class);
                if (mAlertDialogGoPay == null) {
                    mAlertDialogGoPay = new AlertDialogGoPay(getActivity(), map.get("result"));
                }
                mAlertDialogGoPay.show(orderNo);
            }

            @Override
            public void onNotNetOrException() {
                dismissLoadhud(getActivity());
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
            NetLoadUtils.getNetInstance().showLoadSirLoading(loadService);
            loadData();
        }
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void loadData() {
        page = 1;
        getData();
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
    protected void getData() {
        if (userId < 1) {
            NetLoadUtils.getNetInstance().showLoadSirLoadFailed(loadService);
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", page);
        params.put("showCount", TOTAL_COUNT_TEN);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, GROUP_MINE_NEW_INDENT
                , params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();
                        qualityGroupMineAdapter.loadMoreComplete();
                        if (page == 1) {
                            qualityGroupMineList.clear();
                        }

                        qualityGroupMineEntity = GsonUtils.fromJson(result, QualityGroupMineEntity.class);
                        if (qualityGroupMineEntity != null) {
                            if (qualityGroupMineEntity.getCode().equals(SUCCESS_CODE)) {
                                for (int i = 0; i < qualityGroupMineEntity.getQualityGroupMineBeanList().size(); i++) {
                                    QualityGroupMineBean qualityGroupMineBean = qualityGroupMineEntity.getQualityGroupMineBeanList().get(i);
                                    qualityGroupMineBean.setGpStatusMsg(qualityGroupMineEntity.getStatus()
                                            .get(String.valueOf(qualityGroupMineBean.getGpStatus())));
                                    qualityGroupMineList.add(qualityGroupMineBean);
                                }
                            } else if (qualityGroupMineEntity.getCode().equals(EMPTY_CODE)) {
                                qualityGroupMineAdapter.loadMoreEnd();
                            } else {
                                showToast(qualityGroupMineEntity.getMsg());
                            }
                            qualityGroupMineAdapter.notifyDataSetChanged();
                        }
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, qualityGroupMineList, qualityGroupMineEntity);
                    }

                    @Override
                    public void onNotNetOrException() {
                        qualityGroupMineAdapter.loadMoreEnd(true);
                        smart_communal_refresh.finishRefresh();
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, qualityGroupMineList, qualityGroupMineEntity);
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!firstSet) {
            loadData();
            firstSet = false;
        }
    }

    @OnClick(R.id.tv_life_back)
    void goBack() {
        finish();
    }
}
