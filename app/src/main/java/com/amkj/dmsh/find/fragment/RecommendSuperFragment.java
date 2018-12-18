package com.amkj.dmsh.find.fragment;

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
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.find.activity.ArticleDetailsImgActivity;
import com.amkj.dmsh.find.activity.ArticleInvitationDetailsActivity;
import com.amkj.dmsh.find.adapter.PullUserInvitationAdapter;
import com.amkj.dmsh.homepage.bean.InvitationDetailEntity;
import com.amkj.dmsh.homepage.bean.InvitationDetailEntity.InvitationDetailBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.CommunalCopyTextUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_C_ARTICLE;
import static com.amkj.dmsh.constant.Url.FIND_RECOMMEND;
import static com.amkj.dmsh.constant.Url.F_ARTICLE_COLLECT;
import static com.amkj.dmsh.constant.Url.F_ARTICLE_DETAILS_FAVOR;

;

/**
 * Created by atd48 on 2016/6/21.
 */
public class RecommendSuperFragment extends BaseFragment {
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    private List<InvitationDetailBean> invitationSearchList = new ArrayList();
    private int page = 1;
    private PullUserInvitationAdapter adapterInvitationAdapter;
    private String type = "recommend";
    private InvitationDetailEntity invitationDetailEntity;

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_recycler_loading;
    }

    @Override
    protected void initViews() {
        communal_recycler.setBackgroundColor(getResources().getColor(R.color.white));
        communal_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_dp).create());
        adapterInvitationAdapter = new PullUserInvitationAdapter(getActivity(), invitationSearchList, type);
        communal_recycler.setAdapter(adapterInvitationAdapter);
        adapterInvitationAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                getRecommendData();
            }
        }, communal_recycler);
        adapterInvitationAdapter.setOnItemChildLongClickListener(new BaseQuickAdapter.OnItemChildLongClickListener() {
            @Override
            public boolean onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {
                CommunalCopyTextUtils.showPopWindow(getActivity(), (TextView) view, (String) view.getTag(R.id.copy_text_content));
                return false;
            }
        });
        adapterInvitationAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
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
                                getLoginStatus(RecommendSuperFragment.this);
                            }
                            break;
                        case R.id.tv_com_art_like_count:
                            if (userId > 0) {
                                setArticleLiked(invitationDetailBean, view);
                            } else {
                                getLoginStatus(RecommendSuperFragment.this);
                            }
                            break;
                        case R.id.tv_com_art_comment_count:
                            Intent intent = new Intent();
                            switch (invitationDetailBean.getArticletype()) {
                                case 1:
                                    intent.setClass(getActivity(), ArticleInvitationDetailsActivity.class);
                                    break;
                                case 3:
                                    break;
                                default:
                                    intent.setClass(getActivity(), ArticleDetailsImgActivity.class);
                                    break;
                            }
                            intent.putExtra("ArtId", String.valueOf(invitationDetailBean.getId()));
                            intent.putExtra("scrollToComment", "1");
                            startActivity(intent);
                            break;
                    }
                }
            }
        });
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    //    文章收藏
    private void setArticleCollect(final InvitationDetailBean invitationDetailBean, View view) {
        final TextView tv_collect = (TextView) view;
        Map<String, Object> params = new HashMap<>();
        //用户id
        params.put("uid", userId);
        //文章id
        params.put("object_id", invitationDetailBean.getId());
        params.put("type", TYPE_C_ARTICLE);
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(),F_ARTICLE_COLLECT,params,new NetLoadListenerHelper(){
            @Override
            public void onSuccess(String result) {
                loadHud.dismiss();
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        tv_collect.setSelected(!tv_collect.isSelected());
                        tv_collect.setText(ConstantMethod.getNumCount(tv_collect.isSelected(), invitationDetailBean.isCollect(), invitationDetailBean.getCollect(), "收藏"));
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
                showToast(getActivity(),R.string.unConnectedNetwork);
            }
        });
    }

    private void setArticleLiked(InvitationDetailBean invitationDetailBean, View view) {
        TextView tv_like = (TextView) view;
        Map<String, Object> params = new HashMap<>();
        //用户id
        params.put("tuid", userId);
        //关注id
        params.put("id", invitationDetailBean.getId());
        params.put("favortype", "doc");
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(),F_ARTICLE_DETAILS_FAVOR,params,null);
        tv_like.setSelected(!tv_like.isSelected());
        tv_like.setText(ConstantMethod.getNumCount(tv_like.isSelected(), invitationDetailBean.isFavor(), invitationDetailBean.getFavor(), "赞"));
    }

    //设置显示
    //第一个设置
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setUserVisibleHint(true);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void loadData() {
        page = 1;
        getRecommendData();

    }

    private void getRecommendData() {
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", page);
        params.put("isFllow", "recommend");
        if (userId != 0) {
            params.put("fuid", userId);
        }
//        v3.1.3 1 文章帖子
        params.put("version", 1);
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), FIND_RECOMMEND, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                adapterInvitationAdapter.loadMoreComplete();
                if (page == 1) {
                    invitationSearchList.clear();
                }
                Gson gson = new Gson();
                invitationDetailEntity = gson.fromJson(result, InvitationDetailEntity.class);
                if (invitationDetailEntity != null) {
                    if (invitationDetailEntity.getCode().equals(SUCCESS_CODE)) {
                        invitationSearchList.addAll(invitationDetailEntity.getInvitationSearchList());
                    } else if (invitationDetailEntity.getCode().equals(EMPTY_CODE)) {
                        adapterInvitationAdapter.loadMoreEnd();
                    }else{
                        showToast(getActivity(), invitationDetailEntity.getMsg());
                    }
                    adapterInvitationAdapter.notifyDataSetChanged();
                }
                NetLoadUtils.getNetInstance().showLoadSir(loadService,invitationSearchList, invitationDetailEntity);
            }

            @Override
            public void onNotNetOrException() {
                adapterInvitationAdapter.loadMoreEnd(true);
                NetLoadUtils.getNetInstance().showLoadSir(loadService,invitationSearchList, invitationDetailEntity);
            }
        });
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (message.type.equals("refreshFindData")) {
            page = (int) message.result;
            getRecommendData();
        }
    }
}
