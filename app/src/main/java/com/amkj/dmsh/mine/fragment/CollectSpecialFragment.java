package com.amkj.dmsh.mine.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseApplication;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.NetLoadUtils;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.homepage.activity.ArticleOfficialActivity;
import com.amkj.dmsh.homepage.adapter.SpecialTopicAdapter;
import com.amkj.dmsh.homepage.bean.TopicSpecialEntity;
import com.amkj.dmsh.homepage.bean.TopicSpecialEntity.TopicSpecialBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;
import static com.amkj.dmsh.base.BaseApplication.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_TOTAL_COUNT;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/8/10
 * class description:收藏文章
 */
public class CollectSpecialFragment extends BaseFragment {
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
    private SpecialTopicAdapter topicListAdapter;
    private List<TopicSpecialBean> topicBeanList = new ArrayList<>();
    private TopicSpecialEntity topicDetailEntity;

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_smart_refresh_recycler_float_loading;
    }

    @Override
    protected void initViews() {
        getLoginStatus(CollectSpecialFragment.this);
        communal_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        smart_communal_refresh.setOnRefreshListener((refreshLayout) -> {
            loadData();
        });
        topicListAdapter = new SpecialTopicAdapter(getActivity(), topicBeanList);
        communal_recycler.setAdapter(topicListAdapter);
        communal_recycler.addItemDecoration(new PinnedHeaderItemDecoration.Builder(-1)
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_ten_dp)
                // 开启绘制分隔线，默认关闭
                .enableDivider(true)
                // 是否关闭标签点击事件，默认开启
                .disableHeaderClick(false)
                // 设置标签和其内部的子控件的监听，若设置点击监听不为null，但是disableHeaderClick(true)的话，还是不会响应点击事件
                .setHeaderClickListener(null)
                .create());
        topicListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                TopicSpecialBean topicDetailBean = (TopicSpecialBean) view.getTag();
                if (topicDetailBean != null) {
                    Intent intent = new Intent(getActivity(), ArticleOfficialActivity.class);
                    intent.putExtra("ArtId", String.valueOf(topicDetailBean.getId()));
                    startActivity(intent);
                }
            }
        });
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
        topicListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (page * DEFAULT_TOTAL_COUNT <= topicBeanList.size()) {
                    page++;
                    getInvitationList();
                } else {
                    topicListAdapter.loadMoreEnd();
                }
            }
        }, communal_recycler);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IS_LOGIN_CODE) {
            loadData();
        }
    }

    @Override
    protected void loadData() {
        page = 1;
        getInvitationList();
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    private void getInvitationList() {
        String url = Url.BASE_URL + Url.COLLECT_SPECIAL;
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", page);
        params.put("count", DEFAULT_TOTAL_COUNT);
        params.put("uid", userId);
        NetLoadUtils.getQyInstance().loadNetDataPost(mAppContext, url, params, new NetLoadUtils.NetLoadListener() {
            @Override
            public void onSuccess(String result) {
                smart_communal_refresh.finishRefresh();
                topicListAdapter.loadMoreComplete();
                if (page == 1) {
                    topicBeanList.clear();
                }
                Gson gson = new Gson();
                topicDetailEntity = gson.fromJson(result, TopicSpecialEntity.class);
                if (topicDetailEntity != null) {
                    if (topicDetailEntity.getCode().equals(SUCCESS_CODE)) {
                        topicBeanList.addAll(topicDetailEntity.getTopicSpecialBeanList());
                    } else if (!topicDetailEntity.getCode().equals(EMPTY_CODE)) {
                        showToast(getActivity(), topicDetailEntity.getMsg());
                    }
                    topicListAdapter.notifyDataSetChanged();
                }
                NetLoadUtils.getQyInstance().showLoadSir(loadService,topicBeanList, topicDetailEntity);
            }

            @Override
            public void netClose() {
                smart_communal_refresh.finishRefresh();
                topicListAdapter.loadMoreComplete();
                NetLoadUtils.getQyInstance().showLoadSir(loadService,topicBeanList, topicDetailEntity);
            }

            @Override
            public void onError(Throwable throwable) {
                smart_communal_refresh.finishRefresh();
                topicListAdapter.loadMoreComplete();
                NetLoadUtils.getQyInstance().showLoadSir(loadService,topicBeanList, topicDetailEntity);
            }
        });
    }
}
