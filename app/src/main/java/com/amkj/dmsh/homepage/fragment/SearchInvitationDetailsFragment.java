package com.amkj.dmsh.homepage.fragment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseApplication;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.base.NetLoadUtils;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.find.activity.ArticleDetailsImgActivity;
import com.amkj.dmsh.find.activity.ArticleInvitationDetailsActivity;
import com.amkj.dmsh.find.adapter.PullUserInvitationAdapter;
import com.amkj.dmsh.homepage.bean.InvitationDetailEntity;
import com.amkj.dmsh.homepage.bean.InvitationDetailEntity.InvitationDetailBean;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_TOTAL_COUNT;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;

;

/**
 * Created by atd48 on 2016/7/4.
 */
public class SearchInvitationDetailsFragment extends BaseFragment {
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    private String type = "invitation";
    private PullUserInvitationAdapter adapterInvitation;
    private int scrollY = 0;
    private List<InvitationDetailBean> invitationSearchList = new ArrayList();
    private int page = 1;
    private String data;
    private final int SHARECODE = 100;
    private float screenHeight;
    private InvitationDetailEntity invitationDetailEntity;

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_smart_refresh_recycler_float_loading;
    }
    @Override
    protected void initViews() {
        communal_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
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
        adapterInvitation = new PullUserInvitationAdapter(getActivity(), invitationSearchList, type);
        communal_recycler.setAdapter(adapterInvitation);

        smart_communal_refresh.setOnRefreshListener((refreshLayout) -> {
                loadData();
        });
        adapterInvitation.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (page * DEFAULT_TOTAL_COUNT <= adapterInvitation.getItemCount()) {
                    page++;
                    getInvitationDetails();
                } else {
                    adapterInvitation.loadMoreEnd();
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
                    BaseApplication app = (BaseApplication) getActivity().getApplication();
                    screenHeight = app.getScreenHeight() * 0.5f;
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
        adapterInvitation.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                InvitationDetailBean invitationSearch = (InvitationDetailBean) view.getTag();
                if (invitationSearch != null) {
                    Intent intent = new Intent();
                    switch (invitationSearch.getArticletype()) {
                        case 1:
                            intent.setClass(getActivity(), ArticleInvitationDetailsActivity.class);
                            break;
                        case 3:
                            break;
                        default:
                            intent.setClass(getActivity(), ArticleDetailsImgActivity.class);
                            break;
                    }
                    intent.putExtra("ArtId", String.valueOf(invitationSearch.getId()));
                    startActivity(intent);
                }
            }
        });
        adapterInvitation.setOnItemChildClickListener((adapter, view, position) -> {
            InvitationDetailBean invitationDetailBean = (InvitationDetailBean) view.getTag();
            if (invitationDetailBean != null) {
                switch (view.getId()) {
                    case R.id.tv_com_art_collect_count:
                        if (userId > 0) {
                            loadHud.show();
                            setArticleCollect(invitationDetailBean, view);
                        } else {
                            getLoginStatus(SearchInvitationDetailsFragment.this);
                        }
                        break;
                    case R.id.tv_com_art_like_count:
                        if (userId > 0) {
                            setArticleLiked(invitationDetailBean, view);
                        } else {
                            getLoginStatus(SearchInvitationDetailsFragment.this);
                        }
                        break;
                }
            }
        });
    }

    //    文章收藏
    private void setArticleCollect(final InvitationDetailBean invitationDetailBean, View view) {
        final TextView tv_collect = (TextView) view;
        String url = Url.BASE_URL + Url.F_ARTICLE_COLLECT;
        Map<String, Object> params = new HashMap<>();
        //用户id
        params.put("uid", userId);
        //文章id
        params.put("object_id", invitationDetailBean.getId());
        params.put("type", ConstantVariable.TYPE_C_ARTICLE);
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                loadHud.dismiss();
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals("01")) {
                        tv_collect.setSelected(!tv_collect.isSelected());
                        tv_collect.setText(ConstantMethod.getNumCount(tv_collect.isSelected(), invitationDetailBean.isCollect(), invitationDetailBean.getCollect(), "收藏"));
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                loadHud.dismiss();
                showToast(getActivity(), String.format(getResources().getString(R.string.collect_failed), "文章"));
                super.onError(ex, isOnCallback);
            }
        });
    }

    private void setArticleLiked(InvitationDetailBean invitationDetailBean, View view) {
        TextView tv_like = (TextView) view;
        String url = Url.BASE_URL + Url.F_ARTICLE_DETAILS_FAVOR;
        Map<String, Object> params = new HashMap<>();
        //用户id
        params.put("tuid", userId);
        //关注id
        params.put("id", invitationDetailBean.getId());
        params.put("favortype", "doc");
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
            }
        });
        tv_like.setSelected(!tv_like.isSelected());
        tv_like.setText(ConstantMethod.getNumCount(tv_like.isSelected(), invitationDetailBean.isFavor(), invitationDetailBean.getFavor(), "赞"));
    }
    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (message.type.equals("search2")) {
            String resultText = (String) message.result;
            if (!resultText.equals(data)) {
                data = resultText;
                loadData();
            }
        }
    }

    @Override
    protected void loadData() {
        page = 1;
        getInvitationDetails();
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    private void getInvitationDetails() {
        if (!TextUtils.isEmpty(data)) {
            String url = Url.BASE_URL + Url.H_HOT_SEARCH_INVITATION;
            Map<String, Object> params = new HashMap();
            if (userId != 0) {
                params.put("fuid", userId);
            }
            params.put("keyword", data);
            params.put("searchType", 1);
            params.put("currentPage", page);
            params.put("version", 1);
            NetLoadUtils.getQyInstance().loadNetDataPost(getActivity(), url
                    , params, new NetLoadUtils.NetLoadListener() {
                @Override
                public void onSuccess(String result) {
                    smart_communal_refresh.finishRefresh();
                    adapterInvitation.loadMoreComplete();
                    Gson gson = new Gson();
                    invitationDetailEntity = gson.fromJson(result, InvitationDetailEntity.class);
                    if (invitationDetailEntity != null) {
                        if (invitationDetailEntity.getCode().equals(SUCCESS_CODE)) {
                            if (page == 1) {
                                invitationSearchList.clear();
                            }
                            invitationSearchList.addAll(invitationDetailEntity.getInvitationSearchList());
                        } else if (!invitationDetailEntity.getCode().equals(EMPTY_CODE)) {
                            showToast(getActivity(), invitationDetailEntity.getMsg());
                        }
                        adapterInvitation.notifyDataSetChanged();
                        NetLoadUtils.getQyInstance().showLoadSir(loadService,invitationSearchList,invitationDetailEntity);
                    }
                }

                @Override
                public void netClose() {
                    smart_communal_refresh.finishRefresh();
                    adapterInvitation.loadMoreComplete();
                    NetLoadUtils.getQyInstance().showLoadSir(loadService,invitationSearchList,invitationDetailEntity);
                }

                @Override
                public void onError(Throwable throwable) {
                    smart_communal_refresh.finishRefresh();
                    adapterInvitation.loadMoreComplete();
                    NetLoadUtils.getQyInstance().showLoadSir(loadService,invitationSearchList,invitationDetailEntity);
                }
            });
        }else{
            NetLoadUtils.getQyInstance().showLoadSir(loadService,invitationSearchList,invitationDetailEntity);
        }
    }
}
