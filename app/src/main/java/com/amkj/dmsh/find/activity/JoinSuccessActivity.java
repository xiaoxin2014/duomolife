package com.amkj.dmsh.find.activity;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.find.adapter.PostContentAdapter;
import com.amkj.dmsh.find.bean.PostEntity;
import com.amkj.dmsh.find.bean.PostEntity.PostBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.SharedPreUtils;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.itemdecoration.StaggeredDividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantVariable.ERROR_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_FORTY;

/**
 * Created by xiaoxin on 2019/7/22
 * Version:v4.1.0
 * ClassDescription :发现-参与话题/商品评价-发布成功
 */
public class JoinSuccessActivity extends BaseActivity {
    @BindView(R.id.tv_header_shared)
    TextView mTvHeaderShared;
    @BindView(R.id.tl_normal_bar)
    Toolbar mTlNormalBar;
    @BindView(R.id.rv_topic_content)
    RecyclerView mRvRecommend;
    List<PostBean> mPostList = new ArrayList<>();
    PostContentAdapter postAdapter;
    private PostEntity mPostEntity;
    private JoinSuccessHeadView joinSuccessHeadView;
    private int mPostId;
    private String mDigest;
    private String mCoverPath;
    private String mTopicTitle;


    @Override
    protected int getContentView() {
        return R.layout.activity_join_success;
    }

    @Override
    protected void initViews() {
        mTvHeaderShared.setVisibility(View.GONE);
        mTlNormalBar.setSelected(true);

        //初始化帖子详情
        View view = LayoutInflater.from(this).inflate(R.layout.view_join_success, (ViewGroup) mRvRecommend.getParent(), false);
        joinSuccessHeadView = new JoinSuccessHeadView();
        ButterKnife.bind(joinSuccessHeadView, view);
        if (getIntent() != null) {
            int score = getIntent().getIntExtra("score", 0);
            String reminder = getIntent().getStringExtra("reminder");
            mPostId = getIntent().getIntExtra("postId", -1);
            mDigest = getIntent().getStringExtra("digest");
            mCoverPath = getIntent().getStringExtra("coverPath");
            mTopicTitle = getIntent().getStringExtra("topicTitle");

            joinSuccessHeadView.mTvScoreTips.setVisibility(score > 0 ? View.VISIBLE : View.GONE);
            joinSuccessHeadView.mTvScoreTips.setText(ConstantMethod.getIntegralFormat(this, R.string.post_pass_get_score, score));
            joinSuccessHeadView.mTvShareTips.setText(getStrings(reminder));
        }

        //初始化推荐列表
        postAdapter = new PostContentAdapter(getActivity(), mPostList, false);
        postAdapter.addHeaderView(view);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mRvRecommend.setItemAnimator(null);
        mRvRecommend.setLayoutManager(layoutManager);
        mRvRecommend.addItemDecoration(new StaggeredDividerItemDecoration(AutoSizeUtils.mm2px(mAppContext, 10), true));
        mRvRecommend.setAdapter(postAdapter);
        mRvRecommend.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                int[] first = new int[layoutManager.getSpanCount()];
                layoutManager.findFirstCompletelyVisibleItemPositions(first);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && (first[0] == 1 || first[1] == 1)) {
                    layoutManager.invalidateSpanAssignments();
                }
            }
        });
    }

    @Override
    protected void loadData() {
        getRecommend();
    }

    //获取推荐帖子
    private void getRecommend() {
        Map<String, Object> map = new HashMap<>();
        map.put("currentPage", 1);
        map.put("showCount", TOTAL_COUNT_FORTY);
        map.put("type", 7);
        map.put("version", 1);
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Url.GET_POST_LIST, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mPostEntity = GsonUtils.fromJson(result, PostEntity.class);
                mPostList.clear();
                if (mPostEntity != null) {
                    String code = mPostEntity.getCode();
                    List<PostBean> postList = mPostEntity.getPostList();
                    if (SUCCESS_CODE.equals(code) && postList != null && postList.size() > 0) {
                        mPostList.addAll(postList);
                    } else if (ERROR_CODE.equals(code)) {
                        ConstantMethod.showToast(mPostEntity.getMsg());
                    }
                }

                postAdapter.notifyItemRangeChanged(0, mPostList.size());
                joinSuccessHeadView.mTvSpeak.setVisibility(mPostList.size() > 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onNotNetOrException() {
                joinSuccessHeadView.mTvSpeak.setVisibility(mPostList.size() > 0 ? View.VISIBLE : View.GONE);
            }
        });


    }

    @OnClick({R.id.tv_life_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_life_back:
                finish();
                break;
        }
    }


    class JoinSuccessHeadView {

        @BindView(R.id.tv_score_tips)
        TextView mTvScoreTips;
        @BindView(R.id.tv_share)
        TextView mTvShare;
        @BindView(R.id.tv_share_tips)
        TextView mTvShareTips;
        @BindView(R.id.tv_speak)
        TextView mTvSpeak;

        @OnClick(R.id.tv_share)
        void share() {
            if (mPostEntity != null) {
                String nickName = (String) SharedPreUtils.getParam("nickName", "");
                UMShareAction umShareAction = new UMShareAction(getActivity()
                        , mCoverPath
                        , mTopicTitle
                        , "@" + nickName + ":" + (TextUtils.isEmpty(mDigest) ? "我刚刚完成了一个分享，去看看吧" : mDigest)
                        , Url.BASE_SHARE_PAGE_TWO + "find_template/find_detail.html" + "?id=" + mPostId + "&isShare=1"
                        , "pages/findDetail/findDetail?id=" + mPostId + "&isShare=1", mPostId);
            }
        }
    }
}
