package com.amkj.dmsh.mine.fragment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.homepage.activity.ArticleOfficialActivity;
import com.amkj.dmsh.homepage.adapter.SpecialTopicAdapter;
import com.amkj.dmsh.homepage.bean.TopicSpecialEntity;
import com.amkj.dmsh.homepage.bean.TopicSpecialEntity.TopicSpecialBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
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

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TWENTY;
import static com.amkj.dmsh.constant.Url.COLLECT_SPECIAL;

;
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
        communal_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        smart_communal_refresh.setOnRefreshListener(refreshLayout -> loadData());
        topicListAdapter = new SpecialTopicAdapter(getActivity(), topicBeanList);
        communal_recycler.setAdapter(topicListAdapter);
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_ten_dp).create());
        topicListAdapter.setOnItemClickListener((adapter, view, position) -> {
            TopicSpecialBean topicDetailBean = (TopicSpecialBean) view.getTag();
            if (topicDetailBean != null) {
                Intent intent = new Intent(getActivity(), ArticleOfficialActivity.class);
                intent.putExtra("ArtId", String.valueOf(topicDetailBean.getId()));
                startActivity(intent);
            }
        });
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
        topicListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                getInvitationList();
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
        if (userId < 1) {
            NetLoadUtils.getNetInstance().showLoadSirEmpty(loadService);
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", page);
        params.put("count", TOTAL_COUNT_TWENTY);
        params.put("uid", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(mAppContext, COLLECT_SPECIAL,
                params, new NetLoadListenerHelper() {
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
                            } else {
                                topicListAdapter.loadMoreEnd();
                            }
                            topicListAdapter.notifyDataSetChanged();
                        }
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, topicBeanList, topicDetailEntity);
                    }

                    @Override
                    public void onNotNetOrException() {
                        smart_communal_refresh.finishRefresh();
                        topicListAdapter.loadMoreEnd(true);
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, topicBeanList, topicDetailEntity);
                    }
                });
    }
}
