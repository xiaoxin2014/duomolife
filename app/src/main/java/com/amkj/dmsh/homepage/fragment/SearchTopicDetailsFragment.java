package com.amkj.dmsh.homepage.fragment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseApplication;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.find.activity.FindTopicDetailsActivity;
import com.amkj.dmsh.find.adapter.FindTopicListAdapter;
import com.amkj.dmsh.find.bean.FindHotTopicEntity;
import com.amkj.dmsh.find.bean.FindHotTopicEntity.FindHotTopicBean;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.inteface.MyCallBack;
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

import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_TOTAL_COUNT;

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
    @BindView(R.id.communal_load)
    View communal_load;
    @BindView(R.id.communal_error)
    View communal_error;
    @BindView(R.id.communal_empty)
    View communal_empty;
    private int scrollY = 0;
    private int page = 1;
    private float screenHeight;
    private String topicTitle;
    private FindTopicListAdapter findTopicListAdapter;
    private List<FindHotTopicBean> findTopicBeanList = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_smart_refresh_recycler_float_loading;
    }

    @Override
    protected void initViews() {
        communal_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
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

        smart_communal_refresh.setOnRefreshListener((refreshLayout) -> {
            page = 1;
            loadData();
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
            if (page * DEFAULT_TOTAL_COUNT <= findTopicBeanList.size()) {
                page++;
                loadData();
            } else {
                findTopicListAdapter.loadMoreEnd();
            }
        }, communal_recycler);
        download_btn_communal.attachToRecyclerView(communal_recycler, null, new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                scrollY += dy;
                if (!recyclerView.canScrollVertically(-1)) {
                    scrollY = 0;
                }
                if (screenHeight == 0) {
                    BaseApplication app = (BaseApplication) getActivity().getApplication();
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
        communal_load.setVisibility(View.VISIBLE);
    }

    @Override
    protected void loadData() {
        getTopicList();
    }

    private void getTopicList() {
        if (!TextUtils.isEmpty(topicTitle)) {
            if (NetWorkUtils.isConnectedByState(getActivity())) {
                String url = Url.BASE_URL + Url.H_HOT_SEARCH_TOPIC;
                Map<String, Object> params = new HashMap<>();
                params.put("currentPage", page);
                params.put("count", DEFAULT_TOTAL_COUNT);
                params.put("keyword", topicTitle);
                XUtil.Post(url, params, new MyCallBack<String>() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();
                        findTopicListAdapter.loadMoreComplete();
                        communal_load.setVisibility(View.GONE);
                        communal_error.setVisibility(View.GONE);
                        if (page == 1) {
                            findTopicBeanList.clear();
                        }
                        Gson gson = new Gson();
                        FindHotTopicEntity findHotTopicEntity = gson.fromJson(result, FindHotTopicEntity.class);
                        if (findHotTopicEntity != null) {
                            if (findHotTopicEntity.getCode().equals("01")) {
                                findTopicBeanList.addAll(findHotTopicEntity.getHotTopicList());
                            } else if (!findHotTopicEntity.getCode().equals("02")) {
                                showToast(getActivity(), findHotTopicEntity.getMsg());
                            }
                            if (page == 1) {
                                findTopicListAdapter.setNewData(findTopicBeanList);
                            } else {
                                findTopicListAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        if (page == 1 && findTopicBeanList.size() < 1) {
                            communal_load.setVisibility(View.GONE);
                            communal_error.setVisibility(View.VISIBLE);
                        }
                        smart_communal_refresh.finishRefresh();
                        findTopicListAdapter.loadMoreComplete();
                    }
                });
            } else {
                smart_communal_refresh.finishRefresh();
                findTopicListAdapter.loadMoreComplete();
                communal_load.setVisibility(View.GONE);
                communal_error.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (message.type.equals("search3")) {
            String resultText = (String) message.result;
            if (!resultText.equals(topicTitle)) {
                page = 1;
                topicTitle = resultText;
                loadData();
            }
        }
    }

    @OnClick({R.id.rel_communal_error, R.id.communal_empty})
    void refreshData(View view) {
        communal_load.setVisibility(View.VISIBLE);
        communal_empty.setVisibility(View.GONE);
        communal_error.setVisibility(View.GONE);
        page = 1;
        loadData();
    }
}
