package com.amkj.dmsh.homepage.fragment;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.bean.AllSearchEntity;
import com.amkj.dmsh.find.adapter.HotTopicAdapter;
import com.amkj.dmsh.find.adapter.PostContentAdapter;
import com.amkj.dmsh.find.bean.HotTopicEntity.HotTopicBean;
import com.amkj.dmsh.find.bean.PostEntity.PostBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.itemdecoration.StaggeredDividerItemDecoration;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.ERROR_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOPIC_SEARCH_KEY;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TEN;
import static com.amkj.dmsh.constant.Url.H_HOT_SEARCH_ALL;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/7/27
 * class description:话题搜索
 */
public class SearchDetailsTopicFragment extends BaseSearchDetailFragment {
    private PostContentAdapter postAdapter;
    private HotTopicAdapter findHotTopicAdapter;
    private List<HotTopicBean> hotTopicList = new ArrayList<>();
    List<PostBean> mPostList = new ArrayList<>();
    private HotTopicView hotTopicView;


    @Override
    protected void initRv() {
        postAdapter = new PostContentAdapter(getActivity(), mPostList, true);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        communal_recycler.setBackgroundResource(R.color.light_gray_f);
        communal_recycler.setItemAnimator(null);
        communal_recycler.setLayoutManager(layoutManager);
        communal_recycler.addItemDecoration(new StaggeredDividerItemDecoration(AutoSizeUtils.mm2px(mAppContext, 10), true));
        communal_recycler.setAdapter(postAdapter);
        communal_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                int[] first = new int[layoutManager.getSpanCount()];
                layoutManager.findFirstCompletelyVisibleItemPositions(first);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && (first[0] == 1 || first[1] == 1)) {
                    layoutManager.invalidateSpanAssignments();
                }
            }
        });
        postAdapter.setOnLoadMoreListener(() -> {
            page++;
            getSearchDetail();
        }, communal_recycler);

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_find_hot_topic, (ViewGroup) communal_recycler.getParent(), false);
        hotTopicView = new HotTopicView();
        ButterKnife.bind(hotTopicView, view);
        hotTopicView.init();
        postAdapter.addHeaderView(view);
    }

    protected void getSearchDetail() {
        if (TextUtils.isEmpty(getKeywords())) return;
        Map<String, Object> params = new HashMap<>();
        params.put("keyword", getKeywords());
        params.put("searchType", getSearchType());
        params.put("currentPage", page);
        params.put("showCount", TOTAL_COUNT_TEN);
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), H_HOT_SEARCH_ALL
                , params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        searchSucess = true;
                        smart_communal_refresh.setVisibility(View.VISIBLE);
                        mNestedScrollview.setVisibility(View.GONE);
                        smart_communal_refresh.finishRefresh();
                        if (page == 1) {
                            mPostList.clear();
                            hotTopicList.clear();
                            removeExistUtils.clearData();
                            removeExistTopicUtils.clearData();
                        }
                        int positionStart = mPostList.size();
                        allSearchEntity = new Gson().fromJson(result, AllSearchEntity.class);
                        if (allSearchEntity != null && allSearchEntity.getSearchBean() != null) {
                            String code = allSearchEntity.getCode();
                            searchBean = allSearchEntity.getSearchBean();
                            setWordData(searchBean.getWatchword());
                            List<HotTopicBean> topicList = searchBean.getTopicList();
                            List<PostBean> postList = searchBean.getPostList();
                            if (topicList != null && topicList.size() > 0) {
                                hotTopicList.addAll(removeExistTopicUtils.removeExistList(topicList));
                            }
                            if (SUCCESS_CODE.equals(code) && postList != null && postList.size() > 0) {
                                mPostList.addAll(removeExistUtils.removeExistList(postList));
                                postAdapter.loadMoreComplete();
                            } else if (ERROR_CODE.equals(code)) {
                                showToast(getActivity(), allSearchEntity.getMsg());
                                postAdapter.loadMoreFail();
                            } else {
                                postAdapter.loadMoreEnd();
                            }
                        } else {
                            postAdapter.loadMoreEnd();
                        }

                        findHotTopicAdapter.notifyDataSetChanged();
                        if (page == 1) {
                            postAdapter.notifyItemRangeChanged(0, mPostList.size());
                        } else {
                            postAdapter.notifyItemRangeInserted(positionStart, mPostList.size());
                        }
                        hotTopicView.mLlFindHotTopic.setVisibility(hotTopicList.size() > 0 ? View.VISIBLE : View.GONE);
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, mPostList.size() > 0 || hotTopicList.size() > 0, allSearchEntity);
                        updateSearchNum();
                    }

                    @Override
                    public void onNotNetOrException() {
                        smart_communal_refresh.setVisibility(View.VISIBLE);
                        mNestedScrollview.setVisibility(View.GONE);
                        smart_communal_refresh.finishRefresh();
                        postAdapter.loadMoreFail();
                        hotTopicView.mLlFindHotTopic.setVisibility(hotTopicList.size() > 0 ? View.VISIBLE : View.GONE);
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, mPostList.size() > 0 || hotTopicList.size() > 0, allSearchEntity);
                    }
                });
    }

    @Override
    protected String getSearchKey() {
        return TOPIC_SEARCH_KEY;
    }

    class HotTopicView {
        @BindView(R.id.tv_hot_topic)
        TextView mTvHotTopic;
        @BindView(R.id.tv_topic_catergory)
        TextView mTvTopicCatergory;
        @BindView(R.id.rv_topic)
        RecyclerView mCommunalRecyclerWrap;
        @BindView(R.id.ll_find_hot_topic)
        LinearLayout mLlFindHotTopic;

        void init() {
            mTvHotTopic.setText("话题");
            mTvTopicCatergory.setVisibility(View.GONE);
            //初始化热门专题
            mCommunalRecyclerWrap.setLayoutManager(new LinearLayoutManager(getActivity()));
            findHotTopicAdapter = new HotTopicAdapter(getActivity(), hotTopicList, false);
            mCommunalRecyclerWrap.setAdapter(findHotTopicAdapter);
        }
    }
}
