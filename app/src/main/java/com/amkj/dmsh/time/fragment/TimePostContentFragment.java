package com.amkj.dmsh.time.fragment;

import android.os.Bundle;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.find.bean.PostEntity;
import com.amkj.dmsh.find.bean.PostEntity.PostBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.time.adapter.TimePostAdapter;
import com.amkj.dmsh.utils.gson.GsonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import butterknife.BindView;

import static com.amkj.dmsh.constant.ConstantVariable.ERROR_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TWENTY;

/**
 * Created by xiaoxin on 2019/7/9
 * Version:v4.8.0
 * ClassDescription :团购商品相关帖子
 */
public class TimePostContentFragment extends BaseFragment {
    @BindView(R.id.rv_topic_content)
    RecyclerView rvTopicContent;
    TimePostAdapter postAdapter;
    private String id;
    private int page = 1;
    private PostEntity mPostEntity;
    List<PostBean> mPostList = new ArrayList<>();


    @Override
    protected int getContentView() {
        return R.layout.layout_post_content;
    }

    @Override
    protected void initViews() {
        postAdapter = new TimePostAdapter(getActivity(), mPostList,false);
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
        map.put("id", id);
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Url.GET_CATEGORY_DOCLIST_PAGE, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mPostEntity = GsonUtils.fromJson(result, PostEntity.class);
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
    protected boolean isLazy() {
        return true;
    }


    @Override
    protected void getReqParams(Bundle bundle) {
        if (bundle != null) {
            id = bundle.getString("id");
        }
    }
}
