package com.amkj.dmsh.dominant.fragment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.dominant.activity.DmlOptimizedSelDetailActivity;
import com.amkj.dmsh.dominant.adapter.DmlOptimizedSelAdapter;
import com.amkj.dmsh.dominant.bean.DmlOptimizedSelEntity;
import com.amkj.dmsh.dominant.bean.DmlOptimizedSelEntity.DmlOptimizedSelBean;
import com.amkj.dmsh.mine.activity.ShopCarActivity;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.OffsetLinearLayoutManager;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.bugly.beta.tinker.TinkerManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import q.rorbin.badgeview.Badge;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TWENTY;
import static com.amkj.dmsh.constant.Url.Q_DML_OPTIMIZED_LIST;

/**
 * Created by xiaoxin on 2019/4/12 0012
 * Version:v4.0.0
 * ClassDescription :多么定制抽取Fragment
 */
public class DmlOptimizedSelFragment extends BaseFragment {
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.fl_header_service)
    FrameLayout fl_header_service;
    @BindView(R.id.tl_quality_bar)
    Toolbar mTlQualityBar;
    Unbinder unbinder;
    private int page = 1;
    private float screenHeight;
    private Badge badge;
    private List<DmlOptimizedSelBean> dmlOptimizedSelList = new ArrayList<>();
    private DmlOptimizedSelAdapter dmlOptimizedSelAdapter;
    private DmlOptimizedSelEntity optimizedSelEntity;

    @Override
    protected int getContentView() {
        return R.layout.activity_communal_ql_shop_car;
    }

    @Override
    protected void initViews() {
        mTlQualityBar.setVisibility(GONE);
        communal_recycler.setLayoutManager(new OffsetLinearLayoutManager(getActivity()));
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_nine_dp_white).create());
        dmlOptimizedSelAdapter = new DmlOptimizedSelAdapter(getActivity(), dmlOptimizedSelList);
        communal_recycler.setAdapter(dmlOptimizedSelAdapter);
        dmlOptimizedSelAdapter.setOnItemClickListener((adapter, view, position) -> {
            DmlOptimizedSelBean dmlOptimizedSelBean = (DmlOptimizedSelBean) view.getTag();
            if (dmlOptimizedSelBean != null) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), DmlOptimizedSelDetailActivity.class);
                intent.putExtra("optimizedId", String.valueOf(dmlOptimizedSelBean.getId()));
                startActivity(intent);
            }
        });
        dmlOptimizedSelAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                getOptimizedData();
            }
        }, communal_recycler);
        smart_communal_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                loadData();
            }
        });
        TinkerBaseApplicationLike app = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
        screenHeight = app.getScreenHeight();
        communal_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                OffsetLinearLayoutManager layoutManager = (OffsetLinearLayoutManager) recyclerView.getLayoutManager();
                int scrollY = layoutManager.computeVerticalScrollOffset();
                if (scrollY > screenHeight * 1.5) {
                    if (download_btn_communal.getVisibility() == GONE) {
                        download_btn_communal.setVisibility(VISIBLE);
                        download_btn_communal.hide(false);
                    }
                    if (!download_btn_communal.isVisible()) {
                        download_btn_communal.show(true);
                    }
                } else {
                    if (download_btn_communal.isVisible()) {
                        download_btn_communal.hide();
                    }
                }
            }
        });
        download_btn_communal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) communal_recycler.getLayoutManager();
                download_btn_communal.hide();
                communal_recycler.stopScroll();
                linearLayoutManager.scrollToPositionWithOffset(0, 0);
            }
        });
        badge = ConstantMethod.getBadge(getActivity(), fl_header_service);
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    protected void loadData() {
        page = 1;
        getData();
    }

    private void getData() {
        getOptimizedData();
    }

    private void getOptimizedData() {
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", page);
        params.put("showCount", TOTAL_COUNT_TWENTY);
        if (userId > 0) {
            params.put("uid", userId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Q_DML_OPTIMIZED_LIST, params,
                new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();
                        dmlOptimizedSelAdapter.loadMoreComplete();
                        if (page == 1) {
                            dmlOptimizedSelList.clear();
                        }
                        Gson gson = new Gson();
                        optimizedSelEntity = gson.fromJson(result, DmlOptimizedSelEntity.class);
                        if (optimizedSelEntity != null) {
                            if (optimizedSelEntity.getCode().equals(SUCCESS_CODE)) {
                                tv_header_titleAll.setText(getStrings(optimizedSelEntity.getTitle()));
                                dmlOptimizedSelList.addAll(optimizedSelEntity.getDmlOptimizedSelList());
                            } else if (optimizedSelEntity.getCode().equals(EMPTY_CODE)) {
                                dmlOptimizedSelAdapter.loadMoreEnd();
                            } else {
                                showToast(getActivity(), optimizedSelEntity.getMsg());
                            }
                            dmlOptimizedSelAdapter.notifyDataSetChanged();
                        }
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, dmlOptimizedSelList, optimizedSelEntity);
                    }

                    @Override
                    public void onNotNetOrException() {
                        smart_communal_refresh.finishRefresh();
                        dmlOptimizedSelAdapter.loadMoreEnd(true);
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, dmlOptimizedSelList, optimizedSelEntity);
                    }

                    @Override
                    public void netClose() {
                        showToast(getActivity(), R.string.unConnectedNetwork);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        showToast(getActivity(), R.string.invalidData);
                    }
                });
    }


    @OnClick(R.id.iv_img_service)
    void skipService(View view) {
        Intent intent = new Intent(getActivity(), ShopCarActivity.class);
        startActivity(intent);
    }
}
