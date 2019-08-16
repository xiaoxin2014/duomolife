package com.amkj.dmsh.find.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.find.adapter.PostContentAdapter;
import com.amkj.dmsh.find.bean.PostEntity;
import com.amkj.dmsh.find.bean.PostEntity.PostBean;
import com.amkj.dmsh.find.bean.PostTypeBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static com.amkj.dmsh.constant.ConstantMethod.getPostType;
import static com.amkj.dmsh.constant.ConstantMethod.isContextExisted;
import static com.amkj.dmsh.constant.ConstantVariable.ERROR_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TWENTY;
import static com.amkj.dmsh.constant.ConstantVariable.UPDATE_POST_CONTENT;

/**
 * Created by xiaoxin on 2019/7/9
 * Version:v4.1.0
 * ClassDescription :话题内容
 */
public class PostContentFragment extends BaseFragment {
    @BindView(R.id.rv_topic_content)
    RecyclerView rvTopicContent;
    PostContentAdapter postAdapter;
    private String topicId;
    private int page = 1;
    private String title;
    private PostEntity mPostEntity;
    List<PostBean> mPostList = new ArrayList<>();
    private View TopicNameView;


    @Override
    protected int getContentView() {
        return R.layout.layout_post_content;
    }

    @Override
    protected void initViews() {
        postAdapter = new PostContentAdapter(getActivity(), mPostList, TextUtils.isEmpty(topicId));
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        rvTopicContent.setItemAnimator(null);
        rvTopicContent.setLayoutManager(layoutManager);
        rvTopicContent.setAdapter(postAdapter);
        rvTopicContent.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
            loadData();
        }, rvTopicContent);
    }

    @Override
    protected void loadData() {
        getPostList();
    }

    //获取帖子列表
    private void getPostList() {
        Map<String, Object> map = new HashMap<>();
        map.put("currentPage", page);
        map.put("showCount", TOTAL_COUNT_TWENTY);
        map.put("type", getPostType(title));
        //topicId不为空时，表示获取该话题相关的帖子
        if (!TextUtils.isEmpty(topicId)) {
            map.put("topicId", topicId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Url.GET_POST_LIST, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mPostEntity = new Gson().fromJson(result, PostEntity.class);
                if (page == 1) {
                    mPostList.clear();
                }
                int positionStart = mPostList.size();
                if (mPostEntity != null) {
                    String code = mPostEntity.getCode();
                    List<PostBean> postList = mPostEntity.getPostList();
                    if (postList != null && postList.size() > 0) {
                        mPostList.addAll(postList);
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
                    postAdapter.notifyItemRangeChanged(0, mPostList.size());
                } else {
                    postAdapter.notifyItemRangeInserted(positionStart, mPostList.size());
                }

                NetLoadUtils.getNetInstance().showLoadSir(loadService, mPostList, mPostEntity);
            }

            @Override
            public void onNotNetOrException() {
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mPostList, mPostEntity);
            }
        });
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (message.type.equals(UPDATE_POST_CONTENT)) {
            PostTypeBean postTypeBean = (PostTypeBean) message.result;
            if (isContextExisted(getActivity()) && getActivity().getClass().getSimpleName().equals(postTypeBean.getSimpleName()) && title.equals(postTypeBean.getTitle())) {
                page = 1;
                loadData();
            }
        }
    }

    @Override
    protected boolean isLazy() {
        return true;
    }


    @Override
    protected void getReqParams(Bundle bundle) {
        if (bundle != null) {
            topicId = bundle.getString("topicId");
            title = bundle.getString("title");
        }
    }
}
