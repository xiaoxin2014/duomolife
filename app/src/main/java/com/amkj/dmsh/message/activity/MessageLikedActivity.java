package com.amkj.dmsh.message.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.NetLoadUtils;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.ArticleCommentEntity;
import com.amkj.dmsh.bean.ArticleCommentEntity.ArticleCommentBean;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dominant.activity.ShopTimeScrollDetailsActivity;
import com.amkj.dmsh.find.activity.ArticleDetailsImgActivity;
import com.amkj.dmsh.homepage.activity.ArticleOfficialActivity;
import com.amkj.dmsh.message.adapter.MessageCommunalAdapterNew;
import com.amkj.dmsh.user.activity.UserPagerActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tencent.bugly.beta.tinker.TinkerManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static android.view.View.GONE;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_TOTAL_COUNT;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;

;

/**
 * Created by atd48 on 2016/7/11.
 */
public class MessageLikedActivity extends BaseActivity {
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tv_header_shared)
    TextView header_shared;
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;

    private List<ArticleCommentBean> articleCommentList = new ArrayList();
    public static final String TYPE = "message_liked";
    private int page = 1;
    private int scrollY = 0;
    private float screenHeight;
    private MessageCommunalAdapterNew messageCommunalAdapter;
    private ArticleCommentEntity articleCommentEntity;

    @Override
    protected int getContentView() {
        return R.layout.activity_message_communal;
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
        tv_header_titleAll.setText("赞");
        header_shared.setVisibility(View.INVISIBLE);
        messageCommunalAdapter = new MessageCommunalAdapterNew(MessageLikedActivity.this, articleCommentList, TYPE);
        //获取头部消息列表
        communal_recycler.setLayoutManager(new LinearLayoutManager(this));
        communal_recycler.addItemDecoration(new PinnedHeaderItemDecoration.Builder(-1)
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_dp)
                // 开启绘制分隔线，默认关闭
                .enableDivider(true)
                // 是否关闭标签点击事件，默认开启
                .disableHeaderClick(false)
                // 设置标签和其内部的子控件的监听，若设置点击监听不为null，但是disableHeaderClick(true)的话，还是不会响应点击事件
                .setHeaderClickListener(null)
                .create());
        communal_recycler.setAdapter(messageCommunalAdapter);

        smart_communal_refresh.setOnRefreshListener((refreshLayout) -> {
            loadData();

        });
        messageCommunalAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (page * DEFAULT_TOTAL_COUNT <= messageCommunalAdapter.getItemCount()) {
                    page++;
                    getData();
                } else {
                    messageCommunalAdapter.loadMoreEnd();
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
                    download_btn_communal.setVisibility(GONE);
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
        messageCommunalAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                ArticleCommentBean articleCommentBean = (ArticleCommentBean) view.getTag(R.id.iv_avatar_tag);
                if (articleCommentBean == null) {
                    articleCommentBean = (ArticleCommentBean) view.getTag();
                }
                if (articleCommentBean != null) {
                    Intent intent = new Intent();
                    switch (view.getId()) {
                        case R.id.iv_inv_user_avatar:
                            intent.setClass(MessageLikedActivity.this, UserPagerActivity.class);
                            intent.putExtra("userId", String.valueOf(articleCommentBean.getUid()));
                            startActivity(intent);
                            break;
                        case R.id.tv_user_product_description:
                            if (articleCommentBean.getStatus() == 1) {
                                switch (articleCommentBean.getObj()) {
                                    case "doc":
                                        intent.setClass(MessageLikedActivity.this, ArticleOfficialActivity.class);
                                        intent.putExtra("ArtId", String.valueOf(articleCommentBean.getObject_id()));
                                        startActivity(intent);
                                        break;
                                    case "post":
                                        intent.setClass(MessageLikedActivity.this, ArticleDetailsImgActivity.class);
                                        intent.putExtra("ArtId", String.valueOf(articleCommentBean.getObject_id()));
                                        startActivity(intent);
                                        break;
                                    case "goods":
                                        intent.setClass(MessageLikedActivity.this, ShopTimeScrollDetailsActivity.class);
                                        intent.putExtra("productId", String.valueOf(articleCommentBean.getObject_id()));
                                        startActivity(intent);
                                        break;
                                }
                            } else {
                                showToast(MessageLikedActivity.this, "已删除");
                            }
                    }
                }
            }
        });
    }

    @OnClick(R.id.tv_life_back)
    void finish(View view) {
        finish();
    }

    @Override
    protected void loadData() {
        page = 1;
        getData();
    }

    @Override
    protected View getLoadView() {
        return smart_communal_refresh;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    protected void getData() {
        if (userId != 0) {
            String url = Url.BASE_URL + Url.H_MES_LIKED;
            Map<String, Object> params = new HashMap<>();
            params.put("to_uid", userId);
            params.put("currentPage", page);
            NetLoadUtils.getQyInstance().loadNetDataPost(MessageLikedActivity.this, url
                    , params, new NetLoadUtils.NetLoadListener() {
                        @Override
                        public void onSuccess(String result) {
                            smart_communal_refresh.finishRefresh();
                            messageCommunalAdapter.loadMoreComplete();
                            if (page == 1) {
                                articleCommentList.clear();
                            }
                            Gson gson = new Gson();
                            //赞
                            articleCommentEntity = gson.fromJson(result, ArticleCommentEntity.class);
                            if (articleCommentEntity != null) {
                                if (articleCommentEntity.getCode().equals(SUCCESS_CODE)) {
                                    articleCommentList.addAll(articleCommentEntity.getArticleCommentList());
                                } else if (!articleCommentEntity.getCode().equals(EMPTY_CODE)) {
                                    showToast(MessageLikedActivity.this, articleCommentEntity.getMsg());
                                }
                            }
                            messageCommunalAdapter.notifyDataSetChanged();
                            NetLoadUtils.getQyInstance().showLoadSir(loadService, articleCommentList, articleCommentEntity);
                        }

                        @Override
                        public void netClose() {
                            smart_communal_refresh.finishRefresh();
                            messageCommunalAdapter.loadMoreComplete();
                            showToast(MessageLikedActivity.this, R.string.unConnectedNetwork);
                            NetLoadUtils.getQyInstance().showLoadSir(loadService, articleCommentList, articleCommentEntity);
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            smart_communal_refresh.finishRefresh();
                            messageCommunalAdapter.loadMoreComplete();
                            showToast(MessageLikedActivity.this, R.string.invalidData);
                            NetLoadUtils.getQyInstance().showLoadSir(loadService, articleCommentList, articleCommentEntity);
                        }
                    });
        } else {
            NetLoadUtils.getQyInstance().showLoadSirLoadFailed(loadService);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            if (requestCode == IS_LOGIN_CODE) {
                finish();
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IS_LOGIN_CODE) {
            loadData();
        }
    }
}
