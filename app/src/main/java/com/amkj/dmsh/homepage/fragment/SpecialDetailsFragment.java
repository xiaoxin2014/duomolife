package com.amkj.dmsh.homepage.fragment;

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
import com.amkj.dmsh.homepage.adapter.SpecialTopicAdapter;
import com.amkj.dmsh.homepage.bean.TopicSpecialEntity;
import com.amkj.dmsh.homepage.bean.TopicSpecialEntity.TopicSpecialBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
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

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.amkj.dmsh.constant.ConstantMethod.setSkipPath;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.H_HOT_SEARCH_SPECIAL;
import static com.amkj.dmsh.homepage.activity.HomePageSearchActivity.SEARCH_DATA;

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
    private int scrollY = 0;
    private SpecialTopicAdapter specialTopicAdapter;
    private float screenHeight;
    private TopicSpecialEntity topicSpecialEntity;
    private String searchData;

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
                    setSkipPath(getActivity(), topicSpecialBean.getAndroidLink(), false);
                }
            }
        });
        communal_recycler.setAdapter(specialTopicAdapter);

        smart_communal_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                loadData();
            }
        });
        specialTopicAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                getSpecialData();
            }
        }, communal_recycler);
        TinkerBaseApplicationLike app = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
        screenHeight = app.getScreenHeight();
        communal_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                scrollY += dy;
                if (!recyclerView.canScrollVertically(-1)) {
                    scrollY = 0;
                }
                if (scrollY > screenHeight * 1.5 && dy < 0) {
                    if (download_btn_communal.getVisibility() == GONE) {
                        download_btn_communal.setVisibility(VISIBLE);
                        download_btn_communal.hide(false);
                    }
                    if (!download_btn_communal.isVisible()) {
                        download_btn_communal.show();
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
        if (message.type.equals(SEARCH_DATA)) {
            String resultText = (String) message.result;
            if (!resultText.equals(searchData)) {
                NetLoadUtils.getNetInstance().showLoadSirLoading(loadService);
                searchData = resultText;
                loadData();
            }
        }
    }

    @Override
    protected void getReqParams(Bundle bundle) {
        searchData = (String) bundle.get(SEARCH_DATA);
    }

    private void getSpecialData() {
        if (TextUtils.isEmpty(searchData)) {
            NetLoadUtils.getNetInstance().showLoadSir(loadService, specialSearList, topicSpecialEntity);
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("keyword", searchData);
        params.put("currentPage", page);
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), H_HOT_SEARCH_SPECIAL,
                params, new NetLoadListenerHelper() {
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
                            } else if (topicSpecialEntity.getCode().equals(EMPTY_CODE)) {
                                specialTopicAdapter.loadMoreEnd();
                            } else {
                                showToast(getActivity(), topicSpecialEntity.getMsg());
                            }
                            specialTopicAdapter.notifyDataSetChanged();
                        }
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, specialSearList, topicSpecialEntity);
                    }

                    @Override
                    public void onNotNetOrException() {
                        smart_communal_refresh.finishRefresh();
                        specialTopicAdapter.loadMoreEnd(true);
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, specialSearList, topicSpecialEntity);
                    }
                });
    }
}
