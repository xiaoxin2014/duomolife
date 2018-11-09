package com.amkj.dmsh.homepage.fragment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.base.NetLoadUtils;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.find.activity.FindTopicDetailsActivity;
import com.amkj.dmsh.find.adapter.FindTopicListAdapter;
import com.amkj.dmsh.find.bean.FindHotTopicEntity;
import com.amkj.dmsh.find.bean.FindHotTopicEntity.FindHotTopicBean;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.amkj.dmsh.utils.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.tencent.bugly.beta.tinker.TinkerManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_TOTAL_COUNT;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;

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
    private String topicTitle;
    private FindTopicListAdapter findTopicListAdapter;
    private List<FindHotTopicBean> findTopicBeanList = new ArrayList<>();
    private FindHotTopicEntity findHotTopicEntity;

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
                getTopicList();
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
        if (TextUtils.isEmpty(topicTitle)) {
            return;
        }
        String url = Url.BASE_URL + Url.H_HOT_SEARCH_TOPIC;
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", page);
        params.put("count", DEFAULT_TOTAL_COUNT);
        params.put("keyword", topicTitle);
        NetLoadUtils.getQyInstance().loadNetDataPost(getActivity(), url
                , params, new NetLoadUtils.NetLoadListener() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();
                        findTopicListAdapter.loadMoreComplete();
                        if (page == 1) {
                            findTopicBeanList.clear();
                        }
                        Gson gson = new Gson();
                        findHotTopicEntity = gson.fromJson(result, FindHotTopicEntity.class);
                        if (findHotTopicEntity != null) {
                            if (findHotTopicEntity.getCode().equals(SUCCESS_CODE)) {
                                findTopicBeanList.addAll(findHotTopicEntity.getHotTopicList());
                            } else if (!findHotTopicEntity.getCode().equals(EMPTY_CODE)) {
                                showToast(getActivity(), findHotTopicEntity.getMsg());
                            }
                            findTopicListAdapter.notifyDataSetChanged();
                        }
                        NetLoadUtils.getQyInstance().showLoadSir(loadService, findTopicBeanList, findHotTopicEntity);
                    }

                    @Override
                    public void netClose() {
                        smart_communal_refresh.finishRefresh();
                        findTopicListAdapter.loadMoreComplete();
                        NetLoadUtils.getQyInstance().showLoadSir(loadService, findTopicBeanList, findHotTopicEntity);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        smart_communal_refresh.finishRefresh();
                        findTopicListAdapter.loadMoreComplete();
                        NetLoadUtils.getQyInstance().showLoadSir(loadService, findTopicBeanList, findHotTopicEntity);
                    }
                });
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (message.type.equals("search3")) {
            String resultText = (String) message.result;
            if (!resultText.equals(topicTitle)) {
                topicTitle = resultText;
                loadData();
            }
        }
    }
}
