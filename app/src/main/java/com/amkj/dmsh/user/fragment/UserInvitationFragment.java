package com.amkj.dmsh.user.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.find.adapter.PullUserInvitationAdapter;
import com.amkj.dmsh.homepage.bean.InvitationDetailEntity;
import com.amkj.dmsh.homepage.bean.InvitationDetailEntity.InvitationDetailBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.tencent.bugly.beta.tinker.TinkerManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getNumCount;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.skipPostDetail;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TEN;

;
;


/**
 * Created by atd48 on 2016/9/14.
 */
public class UserInvitationFragment extends BaseFragment {
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    private PullUserInvitationAdapter adapterInvitationAdapter;
    private List<InvitationDetailBean> invitationDetailList = new ArrayList();
    private int scrollY = 0;
    private int page = 1;
    private float screenHeight;
    private String type = "likedInvitation";
    private String uid;
    private InvitationDetailEntity invitationDetailEntity;

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_recycler_floating_loading;
    }

    @Override
    protected void initViews() {
        communal_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapterInvitationAdapter = new PullUserInvitationAdapter(getActivity(), invitationDetailList, type);
        communal_recycler.setAdapter(adapterInvitationAdapter);
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_ten_dp).create());
        adapterInvitationAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                InvitationDetailBean invitationDetailBean = (InvitationDetailBean) view.getTag();
                if (invitationDetailBean != null && invitationDetailBean.getArticletype() != 3) {
                    skipPostDetail(getActivity(), String.valueOf(invitationDetailBean.getId()), invitationDetailBean.getArticletype());
                }
            }
        });
        adapterInvitationAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                InvitationDetailBean invitationDetailBean = (InvitationDetailBean) view.getTag();
                if (invitationDetailBean != null) {
                    switch (view.getId()) {
                        case R.id.tv_com_art_collect_count:
                            if (userId > 0) {
                                loadHud.show();
                                setArticleCollect(invitationDetailBean, view);
                            } else {
                                getLoginStatus(UserInvitationFragment.this);
                            }
                            break;
                        case R.id.tv_com_art_like_count:
                            if (userId > 0) {
                                setArticleLiked(invitationDetailBean, view);
                            } else {
                                getLoginStatus(UserInvitationFragment.this);
                            }
                            break;
                    }
                }
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
        adapterInvitationAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                getInvitationList();
            }
        }, communal_recycler);
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
        String url = Url.BASE_URL + Url.USER_INVITATION_LIST;
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", page);
        params.put("showCount", TOTAL_COUNT_TEN);
        params.put("uid", uid);
        params.put("version", 1);
        if (userId > 0) {
            params.put("myUid", userId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                adapterInvitationAdapter.loadMoreComplete();
                if (page == 1) {
                    invitationDetailList.clear();
                }
                Gson gson = new Gson();
                invitationDetailEntity = gson.fromJson(result, InvitationDetailEntity.class);
                if (invitationDetailEntity != null) {
                    if (invitationDetailEntity.getCode().equals(SUCCESS_CODE)) {
                        invitationDetailList.addAll(invitationDetailEntity.getInvitationSearchList());
                    } else if (!invitationDetailEntity.getCode().equals(EMPTY_CODE)) {
                        showToast(getActivity(), invitationDetailEntity.getMsg());
                    } else {
                        adapterInvitationAdapter.loadMoreEnd();
                    }
                    adapterInvitationAdapter.notifyDataSetChanged();
                }
                NetLoadUtils.getNetInstance().showLoadSir(loadService, invitationDetailList, invitationDetailEntity);
            }

            @Override
            public void onNotNetOrException() {
                adapterInvitationAdapter.loadMoreEnd(true);
                NetLoadUtils.getNetInstance().showLoadSir(loadService, invitationDetailList, invitationDetailEntity);
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
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                loadHud.dismiss();
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        tv_collect.setSelected(!tv_collect.isSelected());
                        tv_collect.setText(getNumCount(tv_collect.isSelected(), invitationDetailBean.isCollect(), invitationDetailBean.getCollect(), "收藏"));
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                loadHud.dismiss();
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(getActivity(), String.format(getResources().getString(R.string.collect_failed), "文章"));
            }

            @Override
            public void netClose() {
                showToast(getActivity(), R.string.unConnectedNetwork);
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
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), url, params, null);
        tv_like.setSelected(!tv_like.isSelected());
        tv_like.setText(getNumCount(tv_like.isSelected(), invitationDetailBean.isFavor(), invitationDetailBean.getFavor(), "赞"));
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
    protected void getReqParams(Bundle bundle) {
        uid = bundle.getString("userId");
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (message.type.equals("refreshMineData")) {
            page = (int) message.result;
            getInvitationList();
        }
    }
}
