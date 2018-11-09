package com.amkj.dmsh.homepage.fragment;

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
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.homepage.adapter.SpecialTopicAdapter;
import com.amkj.dmsh.homepage.bean.TopicSpecialEntity;
import com.amkj.dmsh.homepage.bean.TopicSpecialEntity.TopicSpecialBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
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
 * class description:搜索专题
 */
public class SpecialDetailsFragment extends BaseFragment {
    @BindView(R.id.smart_communal_refresh)
    RefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;

    private List<TopicSpecialBean> specialSearList = new ArrayList<>();
    private int page = 1;
    private String keyWord;
    private int scrollY = 0;
    private SpecialTopicAdapter specialTopicAdapter;
    private float screenHeight;
    private TopicSpecialEntity topicSpecialEntity;

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_smart_refresh_recycler_float_loading;
    }

    @Override
    protected void initViews() {
        communal_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        specialTopicAdapter = new SpecialTopicAdapter(getActivity(), specialSearList);
        specialTopicAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                TopicSpecialBean topicSpecialBean = (TopicSpecialBean) view.getTag();
                if (topicSpecialBean != null) {
                    ConstantMethod.setSkipPath(getActivity(), topicSpecialBean.getAndroidLink(), false);
                }
            }
        });
        communal_recycler.setAdapter(specialTopicAdapter);

        smart_communal_refresh.setOnRefreshListener((refreshLayout) -> {
            loadData();
        });
        specialTopicAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (page * DEFAULT_TOTAL_COUNT <= specialSearList.size()) {
                    page++;
                    getSpecialData();
                } else {
                    specialTopicAdapter.loadMoreEnd();
                }
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
                    screenHeight = app.getScreenHeight() * 0.5f;
                }
                if (scrollY >= screenHeight) {
                    download_btn_communal.setVisibility(View.VISIBLE);
                } else {
                    download_btn_communal.setVisibility(View.GONE);
                }
            }
        });
        download_btn_communal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) communal_recycler.getLayoutManager();
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                int mVisibleCount = linearLayoutManager.findLastVisibleItemPosition()
                        - linearLayoutManager.findFirstVisibleItemPosition() + 1;
                if (firstVisibleItemPosition > mVisibleCount) {
                    communal_recycler.scrollToPosition(mVisibleCount);
                }
                communal_recycler.smoothScrollToPosition(0);
            }
        });
    }

    //设置显示

    @Override
    protected void loadData() {
        page = 1;
        getSpecialData();
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (message.type.equals("search1")) {
            String resultText = (String) message.result;
            if (!resultText.equals(keyWord)) {
                keyWord = resultText;
                loadData();
            }
        }
    }

    private void getSpecialData() {
        if (TextUtils.isEmpty(keyWord)) {
            return;
        }
        String url = Url.BASE_URL + Url.H_HOT_SEARCH_SPECIAL;
        Map<String, Object> params = new HashMap<>();
        params.put("keyword", keyWord);
        params.put("currentPage", page);
        NetLoadUtils.getQyInstance().loadNetDataPost(getActivity(), url, params, new NetLoadUtils.NetLoadListener() {
            @Override
            public void onSuccess(String result) {
                smart_communal_refresh.finishRefresh();
                specialTopicAdapter.loadMoreComplete();
                if (page == 1) {
                    specialSearList.clear();
                }
                Gson gson = new Gson();
                topicSpecialEntity = gson.fromJson(result, TopicSpecialEntity.class);
                if (topicSpecialEntity != null) {
                    if (topicSpecialEntity.getCode().equals(SUCCESS_CODE)) {
                        specialSearList.addAll(topicSpecialEntity.getTopicSpecialBeanList());
                    } else if (!topicSpecialEntity.getCode().equals(EMPTY_CODE)) {
                        showToast(getActivity(), topicSpecialEntity.getMsg());
                    }
                    specialTopicAdapter.notifyDataSetChanged();
                }
                NetLoadUtils.getQyInstance().showLoadSir(loadService, specialSearList, topicSpecialEntity);
            }

            @Override
            public void netClose() {
                smart_communal_refresh.finishRefresh();
                specialTopicAdapter.loadMoreComplete();
                NetLoadUtils.getQyInstance().showLoadSir(loadService, specialSearList, topicSpecialEntity);
            }

            @Override
            public void onError(Throwable throwable) {
                smart_communal_refresh.finishRefresh();
                specialTopicAdapter.loadMoreComplete();
                NetLoadUtils.getQyInstance().showLoadSir(loadService, specialSearList, topicSpecialEntity);
            }
        });
    }
}
