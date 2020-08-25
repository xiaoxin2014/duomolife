package com.amkj.dmsh.mine.activity;

import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.find.adapter.PostContentAdapter;
import com.amkj.dmsh.find.bean.PostEntity;
import com.amkj.dmsh.find.bean.PostEntity.PostBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.itemdecoration.StaggeredDividerItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantVariable.ERROR_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TWENTY;

/**
 * Created by xiaoxin on 2020/8/21
 * Version:v4.7.0
 * ClassDescription :0元试用-心得列表
 */
public class ZeroReportListActivity extends BaseActivity {
    @BindView(R.id.communal_recycler)
    RecyclerView mCommunalRecycler;
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout mSmartCommunalRefresh;
    @BindView(R.id.tv_header_title)
    TextView mTvHeaderTitle;
    @BindView(R.id.tv_header_shared)
    TextView mTvHeaderShared;
    private PostContentAdapter postAdapter;
    private List<PostBean> reportList = new ArrayList<>();
    private int page = 1;
    private PostEntity mPostEntity;

    @Override
    protected int getContentView() {
        return R.layout.activity_zero_idea;
    }

    @Override
    protected void initViews() {
        mTvHeaderTitle.setText("试用心得");
        mTvHeaderShared.setVisibility(View.GONE);
        //初始化推荐列表
        mCommunalRecycler.setBackgroundResource(R.color.light_gray_f);
        postAdapter = new PostContentAdapter(getActivity(), reportList, false, true);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mCommunalRecycler.setItemAnimator(null);
        mCommunalRecycler.setLayoutManager(layoutManager);
        mCommunalRecycler.addItemDecoration(new StaggeredDividerItemDecoration(AutoSizeUtils.mm2px(mAppContext, 10), true));
        mCommunalRecycler.setAdapter(postAdapter);
        mCommunalRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                int[] first = new int[layoutManager.getSpanCount()];
                layoutManager.findFirstCompletelyVisibleItemPositions(first);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && (first[0] == 1 || first[1] == 1)) {
                    layoutManager.invalidateSpanAssignments();
                }
            }
        });

        mSmartCommunalRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                loadData();
            }
        });

        postAdapter.setOnLoadMoreListener(() -> {
            page++;
            getPostList();
        }, mCommunalRecycler);
    }

    @Override
    protected void loadData() {
        page = 1;
        getPostList();
    }

    //获取帖子列表
    private void getPostList() {
        Map<String, Object> map = new HashMap<>();
        map.put("currentPage", page);
        map.put("showCount", TOTAL_COUNT_TWENTY);
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Url.GET_REPORT_LIST, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mSmartCommunalRefresh.finishRefresh();
                mPostEntity = GsonUtils.fromJson(result, PostEntity.class);
                if (page == 1) {
                    reportList.clear();
                }
                int positionStart = reportList.size();
                if (mPostEntity != null) {
                    String code = mPostEntity.getCode();
                    List<PostBean> postList = mPostEntity.getPostList();
                    if (postList != null && postList.size() > 0) {
                        reportList.addAll(postList);
                        postAdapter.loadMoreComplete();
                    } else if (ERROR_CODE.equals(code)) {
                        ConstantMethod.showToast(mPostEntity.getMsg());
                        postAdapter.loadMoreFail();
                    } else {
                        postAdapter.loadMoreEnd();
                    }
                } else {
                    postAdapter.loadMoreEnd();
                }

                if (page == 1) {
                    postAdapter.notifyItemRangeChanged(0, reportList.size());
                } else {
                    postAdapter.notifyItemRangeInserted(positionStart, reportList.size());
                }

                NetLoadUtils.getNetInstance().showLoadSir(loadService, reportList, mPostEntity);
            }

            @Override
            public void onNotNetOrException() {
                mSmartCommunalRefresh.finishRefresh();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, reportList, mPostEntity);
            }
        });
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    public View getLoadView() {
        return mSmartCommunalRefresh;
    }

    @OnClick(R.id.tv_life_back)
    public void onViewClicked() {
        finish();
    }
}
