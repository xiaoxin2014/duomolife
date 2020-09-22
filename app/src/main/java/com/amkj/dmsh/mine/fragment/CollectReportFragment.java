package com.amkj.dmsh.mine.fragment;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.find.bean.PostEntity;
import com.amkj.dmsh.mine.adapter.ReportContentAdapter;
import com.amkj.dmsh.mine.bean.CollectReportEntity;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_FORTY;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TEN;

/**
 * Created by xiaoxin on 2020/9/1
 * Version:v4.7.0
 * ClassDescription :心得收藏列表
 */
public class CollectReportFragment extends BaseFragment {
    @BindView(R.id.communal_recycler)
    RecyclerView mRvReport;
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout mSmartCommunalRefresh;
    @BindView(R.id.download_btn_communal)
    FloatingActionButton mDownloadBtnCommunal;
    private ReportContentAdapter mReportContentAdapter;
    private List<PostEntity.PostBean> reports = new ArrayList<>();
    private int page = 1;
    private CollectReportEntity mCollectReportEntity;

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_smart_refresh_recycler_float_loading;
    }

    @Override
    protected void initViews() {
        //初始化报告列表
        mReportContentAdapter = new ReportContentAdapter(getActivity(), reports);
        mRvReport.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvReport.setAdapter(mReportContentAdapter);
        mReportContentAdapter.setOnLoadMoreListener(() -> {
            page++;
            loadData();
        }, mRvReport);
        mSmartCommunalRefresh.setOnRefreshListener(refreshLayout -> loadData());
        setFloatingButton(mDownloadBtnCommunal, mRvReport);
    }

    @Override
    protected void loadData() {
        Map<String, Object> map = new HashMap<>();
        map.put("currentPage", page);
        map.put("showCount", TOTAL_COUNT_FORTY);
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Url.GET_COLLECT_REPORT_LIST, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mSmartCommunalRefresh.finishRefresh();
                if (page == 1) {
                    reports.clear();
                }
                mCollectReportEntity = GsonUtils.fromJson(result, CollectReportEntity.class);
                if (mCollectReportEntity != null) {
                    String code = mCollectReportEntity.getCode();
                    if (SUCCESS_CODE.equals(code)) {
                        List<PostEntity.PostBean> postBeanList = mCollectReportEntity.getResult();
                        if (postBeanList != null) {
                            reports.addAll(postBeanList);
                            mReportContentAdapter.notifyDataSetChanged();
                            if (postBeanList.size() >= TOTAL_COUNT_TEN) {
                                mReportContentAdapter.loadMoreComplete();
                            } else {
                                mReportContentAdapter.loadMoreEnd();
                            }
                        }
                    } else {
                        mReportContentAdapter.notifyDataSetChanged();
                        mReportContentAdapter.loadMoreEnd();
                        if (!EMPTY_CODE.equals(code)) showToast(mCollectReportEntity.getMsg());
                    }
                }
                NetLoadUtils.getNetInstance().showLoadSir(loadService, reports, mCollectReportEntity);
            }

            @Override
            public void onNotNetOrException() {
                mSmartCommunalRefresh.finishRefresh();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, reports, mCollectReportEntity);
            }
        });
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }
}
