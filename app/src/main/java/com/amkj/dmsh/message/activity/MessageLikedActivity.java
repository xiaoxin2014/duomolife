package com.amkj.dmsh.message.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.BaseApplication;
import com.amkj.dmsh.bean.ArticleCommentEntity;
import com.amkj.dmsh.bean.ArticleCommentEntity.ArticleCommentBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.dominant.activity.ShopTimeScrollDetailsActivity;
import com.amkj.dmsh.find.activity.ArticleDetailsImgActivity;
import com.amkj.dmsh.homepage.activity.ArticleOfficialActivity;
import com.amkj.dmsh.message.adapter.MessageCommunalAdapterNew;
import com.amkj.dmsh.mine.activity.MineLoginActivity;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.user.activity.UserPagerActivity;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.view.View.GONE;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_TOTAL_COUNT;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;

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
    RefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    @BindView(R.id.communal_load)
    View communal_load;
    @BindView(R.id.communal_error)
    View communal_error;
    @BindView(R.id.communal_empty)
    View communal_empty;
    List articleCommentList = new ArrayList();
    public static final String TYPE = "message_liked";
    private int uid;
    private int page = 1;
    private int scrollY = 0;
    private float screenHeight;
    private final int MESLIKED_CODE = 113;
    private MessageCommunalAdapterNew messageCommunalAdapter;
    @Override
    protected int getContentView() {
        return R.layout.activity_message_communal;
    }
    @Override
    protected void initViews() {
        getLoginStatus();
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
            page = 1;
                loadData();

        });
        messageCommunalAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (page * DEFAULT_TOTAL_COUNT <= messageCommunalAdapter.getItemCount()) {
                    page++;
                    loadData();
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
                    BaseApplication app = (BaseApplication) getApplication();
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
        communal_load.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.tv_life_back)
    void finish(View view) {
        finish();
    }

    @Override
    protected void loadData() {
        if (uid != 0) {
            String url = Url.BASE_URL + Url.H_MES_LIKED + uid + "&currentPage=" + page;
            if (NetWorkUtils.isConnectedByState(MessageLikedActivity.this)) {
                XUtil.Get(url, null, new MyCallBack<String>() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();
                        messageCommunalAdapter.loadMoreComplete();
                        communal_load.setVisibility(GONE);
                        communal_error.setVisibility(GONE);
                        if (page == 1) {
                            articleCommentList.clear();
                        }
                        Gson gson = new Gson();
                        //赞
                        ArticleCommentEntity articleCommentEntity = gson.fromJson(result, ArticleCommentEntity.class);
                        if (articleCommentEntity != null) {
                            if (articleCommentEntity.getCode().equals("01")) {
                                articleCommentList.addAll(articleCommentEntity.getArticleCommentList());
                            } else if (!articleCommentEntity.getCode().equals("02")) {
                                showToast(MessageLikedActivity.this, articleCommentEntity.getMsg());
                            }
                        }
                        if (page == 1) {
                            messageCommunalAdapter.setNewData(articleCommentList);
                        } else {
                            messageCommunalAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        super.onError(ex, isOnCallback);
                        smart_communal_refresh.finishRefresh();
                        messageCommunalAdapter.loadMoreComplete();
                        communal_load.setVisibility(GONE);
                        communal_error.setVisibility(View.VISIBLE);
                        showToast(MessageLikedActivity.this, R.string.invalidData);
                    }
                });
            } else {
                smart_communal_refresh.finishRefresh();
                messageCommunalAdapter.loadMoreComplete();
                communal_load.setVisibility(GONE);
                communal_error.setVisibility(View.VISIBLE);
                showToast(this, R.string.unConnectedNetwork);
            }
        }
    }

    private void getLoginStatus() {
        SavePersonalInfoBean personalInfo = ConstantMethod.getPersonalInfo(this);
        if (personalInfo.isLogin()) {
            uid = personalInfo.getUid();
        } else {
            //未登录跳转登录页
            Intent intent = new Intent(this, MineLoginActivity.class);
            startActivityForResult(intent, ConstantVariable.IS_LOGIN_CODE);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            if (requestCode == IS_LOGIN_CODE) {
                finish();
            } else {
                return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IS_LOGIN_CODE) {
                getLoginStatus();
                loadData();
            }
        }
    }
}
