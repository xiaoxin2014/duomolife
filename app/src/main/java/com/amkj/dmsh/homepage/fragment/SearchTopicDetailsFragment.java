package com.amkj.dmsh.homepage.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.find.activity.FindTopicDetailsActivity;
import com.amkj.dmsh.find.adapter.FindTopicListAdapter;
import com.amkj.dmsh.find.bean.FindHotTopicEntity;
import com.amkj.dmsh.find.bean.FindHotTopicEntity.FindHotTopicBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.bugly.beta.tinker.TinkerManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TWENTY;
import static com.amkj.dmsh.constant.Url.H_HOT_SEARCH_TOPIC;
import static com.amkj.dmsh.homepage.activity.HomePageSearchActivity.SEARCH_DATA;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/7/27
 * class description:话题搜索
 */
public class SearchTopicDetailsFragment extends BaseFragment {
    @BindView(R.id.smart_communal_refresh)
    RefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    private int scrollY = 0;
    private int page = 1;
    private float screenHeight;
    private FindTopicListAdapter findTopicListAdapter;
    private List<FindHotTopicBean> findTopicBeanList = new ArrayList<>();
    private FindHotTopicEntity findHotTopicEntity;
    private String searchData;

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_smart_refresh_recycler_float_loading;
    }

    @Override
    protected void initViews() {
        communal_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_gray_f_two_px)


                .create());

        smart_communal_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                loadData();
            }
        });
        findTopicListAdapter = new FindTopicListAdapter(getActivity(), findTopicBeanList);
        communal_recycler.setAdapter(findTopicListAdapter);
        findTopicListAdapter.setOnItemClickListener((adapter, view, position) -> {
            FindHotTopicBean hotTopicBean = (FindHotTopicBean) view.getTag();
            if (hotTopicBean != null) {
                Intent intent = new Intent(getActivity(), FindTopicDetailsActivity.class);
                intent.putExtra("topicId", String.valueOf(hotTopicBean.getId()));
                startActivity(intent);
            }
        });
        findTopicListAdapter.setOnLoadMoreListener(() -> {
            page++;
            getTopicList();
        }, communal_recycler);
        download_btn_communal.attachToRecyclerView(communal_recycler, null, new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                scrollY += dy;
                if (!recyclerView.canScrollVertically(-1)) {
                    scrollY = 0;
                }
                if (screenHeight == 0) {
                    TinkerBaseApplicationLike app = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
                    screenHeight = app.getScreenHeight();
                }
                if (scrollY >= screenHeight) {
                    download_btn_communal.setVisibility(View.VISIBLE);
                } else {
                    download_btn_communal.setVisibility(View.GONE);
                }
            }
        });
        download_btn_communal.setOnClickListener(v -> {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) communal_recycler.getLayoutManager();
            int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
            int mVisibleCount = linearLayoutManager.findLastVisibleItemPosition()
                    - linearLayoutManager.findFirstVisibleItemPosition() + 1;
            if (firstVisibleItemPosition > mVisibleCount) {
                communal_recycler.scrollToPosition(mVisibleCount);
            }
            communal_recycler.smoothScrollToPosition(0);
        });
    }

    @Override
    protected void loadData() {
        page = 1;
        getTopicList();
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    private void getTopicList() {
        if (TextUtils.isEmpty(searchData)) {
            NetLoadUtils.getNetInstance().showLoadSir(loadService, findTopicBeanList, findHotTopicEntity);
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", page);
        params.put("count", TOTAL_COUNT_TWENTY);
        params.put("keyword", searchData);
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), H_HOT_SEARCH_TOPIC
                , params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();
                        findTopicListAdapter.loadMoreComplete();
                        if (page == 1) {
                            findTopicBeanList.clear();
                            findTopicListAdapter.notifyDataSetChanged();
                        }
                        Gson gson = new Gson();
                        findHotTopicEntity = gson.fromJson(result, FindHotTopicEntity.class);
                        if (findHotTopicEntity != null) {
                            if (findHotTopicEntity.getCode().equals(SUCCESS_CODE)) {
                                findTopicBeanList.addAll(findHotTopicEntity.getHotTopicList());
                            } else if (findHotTopicEntity.getCode().equals(EMPTY_CODE)) {
                                findTopicListAdapter.loadMoreEnd();
                            }else{
                                showToast(getActivity(), findHotTopicEntity.getMsg());
                            }
                            findTopicListAdapter.notifyDataSetChanged();
                        }
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, findTopicBeanList, findHotTopicEntity);
                    }

                    @Override
                    public void onNotNetOrException() {
                        smart_communal_refresh.finishRefresh();
                        findTopicListAdapter.loadMoreComplete();
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, findTopicBeanList, findHotTopicEntity);
                    }
                });
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (message.type.equals(SEARCH_DATA)) {
            String resultText = (String) message.result;
            if (!resultText.equals(searchData)) {
                searchData = resultText;
                NetLoadUtils.getNetInstance().showLoadSirLoading(loadService);
                loadData();
            }
        }
    }

    @Override
    protected void getReqParams(Bundle bundle) {
        searchData = (String) bundle.get(SEARCH_DATA);
    }
}
