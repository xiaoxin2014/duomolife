package com.amkj.dmsh.dominant.activity;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.find.adapter.PostContentAdapter;
import com.amkj.dmsh.find.bean.PostEntity;
import com.amkj.dmsh.find.bean.PostEntity.PostBean;
import com.amkj.dmsh.find.view.PostGoodsView;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.itemdecoration.StaggeredDividerItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantVariable.ERROR_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TEN;


/**
 * Created by xiaoxin on 2019/8/21
 * Version:v4.2.0
 * ClassDescription :商品更多评论
 */
public class DirectProductEvaluationActivity extends BaseActivity {
    @BindView(R.id.tv_header_shared)
    TextView header_shared;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tl_normal_bar)
    Toolbar tl_normal_bar;
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView mRvComment;
    private int page = 1;
    private String productId;

    List<PostBean> commentList = new ArrayList<>();
    PostContentAdapter postAdapter;
    private PostEntity mPostEntity;
    private PostGoodsView postGoodsView;

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_refresh_recycle_header;
    }

    @Override
    protected void initViews() {
        Intent intent = getIntent();
        productId = intent.getStringExtra("productId");
        tv_header_titleAll.setText("Ta们都在说");
        tl_normal_bar.setSelected(true);
        header_shared.setVisibility(View.INVISIBLE);
        mRvComment.setBackgroundResource(R.color.light_gray_f);
        //初始化推荐列表
        postAdapter = new PostContentAdapter(getActivity(), commentList, false);
        postGoodsView = new PostGoodsView(this, null);
        postGoodsView.findViewById(R.id.rl_goods).setBackgroundResource(R.color.white);
        postAdapter.addHeaderView(postGoodsView);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mRvComment.setItemAnimator(null);
        mRvComment.setLayoutManager(layoutManager);
        mRvComment.addItemDecoration(new StaggeredDividerItemDecoration(AutoSizeUtils.mm2px(mAppContext, 10), true));
        mRvComment.setAdapter(postAdapter);
        mRvComment.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        }, mRvComment);
    }

    @Override
    public View getLoadView() {
        return smart_communal_refresh;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    protected void loadData() {
        getEvaluationData();
    }

    private void getEvaluationData() {
        Map<String, Object> map = new HashMap<>();
        map.put("showCount", TOTAL_COUNT_TEN);
        map.put("currentPage", page);
        map.put("productId", productId);
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Url.GET_PRODUCT_POST, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mPostEntity = GsonUtils.fromJson(result, PostEntity.class);
                if (page == 1) {
                    commentList.clear();
                }
                int positionStart = commentList.size();
                if (mPostEntity != null) {
                    postGoodsView.updateData(getActivity(), mPostEntity.getProductInfo());
                    String code = mPostEntity.getCode();
                    List<PostBean> postList = mPostEntity.getPostList();
                    if (postList != null && postList.size() > 0) {
                        commentList.addAll(postList);
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
                    postAdapter.notifyItemRangeChanged(0, commentList.size());
                } else {
                    postAdapter.notifyItemRangeInserted(positionStart, commentList.size());
                }

                NetLoadUtils.getNetInstance().showLoadSir(loadService, commentList, mPostEntity);
            }

            @Override
            public void onNotNetOrException() {
                NetLoadUtils.getNetInstance().showLoadSir(loadService, commentList, mPostEntity);
            }
        });
    }

    @OnClick(R.id.tv_life_back)
    void goBack() {
        finish();
    }
}