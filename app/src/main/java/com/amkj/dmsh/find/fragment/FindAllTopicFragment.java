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
import com.amkj.dmsh.base.NetLoadUtils;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.find.activity.ArticleDetailsImgActivity;
import com.amkj.dmsh.find.activity.ArticleInvitationDetailsActivity;
import com.amkj.dmsh.find.adapter.PullUserInvitationAdapter;
import com.amkj.dmsh.homepage.bean.InvitationDetailEntity;
import com.amkj.dmsh.homepage.bean.InvitationDetailEntity.InvitationDetailBean;
import com.amkj.dmsh.utils.CommunalCopyTextUtils;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.google.gson.Gson;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getNumCount;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_TOTAL_COUNT;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.find.activity.FindTopicDetailsActivity.TOPIC_TYPE;

;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/11/23
 * class description:推荐话题
 */
public class FindAllTopicFragment extends BaseFragment {
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    private List<InvitationDetailBean> invitationSearchList = new ArrayList();
    private int page = 1;
    private PullUserInvitationAdapter adapterInvitationAdapter;
    private String topicId;
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
                .setDividerId(R.drawable.item_divider_five_dp)






                .create());
        adapterInvitationAdapter = new PullUserInvitationAdapter(getActivity(), invitationSearchList, TOPIC_TYPE);
        communal_recycler.setAdapter(adapterInvitationAdapter);
        adapterInvitationAdapter.setOnLoadMoreListener(() -> {
            if (page * DEFAULT_TOTAL_COUNT <= adapterInvitationAdapter.getItemCount()) {
                page++;
                getTopicData();
            } else {
                adapterInvitationAdapter.loadMoreEnd();
            }
        }, communal_recycler);
        adapterInvitationAdapter.setOnItemChildLongClickListener((adapter, view, position) -> {
            CommunalCopyTextUtils.showPopWindow(getActivity(), (TextView) view, (String) view.getTag(R.id.copy_text_content));
            return false;
        });
        adapterInvitationAdapter.setOnItemClickListener((adapter, view, position) -> {
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
        });
        adapterInvitationAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            InvitationDetailBean invitationDetailBean = (InvitationDetailBean) view.getTag();
            if (invitationDetailBean != null) {
                switch (view.getId()) {
                    case R.id.tv_com_art_collect_count:
                        if (userId > 0) {
                            loadHud.show();
                            setArticleCollect(invitationDetailBean, view);
                        } else {
                            getLoginStatus(FindAllTopicFragment.this);
                        }
                        break;
                    case R.id.tv_com_art_like_count:
                        if (userId > 0) {
                            setArticleLiked(invitationDetailBean, view);
                        } else {
                            getLoginStatus(FindAllTopicFragment.this);
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
                        tv_collect.setText(getNumCount(tv_collect.isSelected(), invitationDetailBean.isCollect(), invitationDetailBean.getCollect(), "收藏"));
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
        tv_like.setText(getNumCount(tv_like.isSelected(), invitationDetailBean.isFavor(), invitationDetailBean.getFavor(), "赞"));
    }

    @Override
    protected void loadData() {
        page = 1;
        getTopicData();
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    private void getTopicData() {
        String url = Url.BASE_URL + Url.F_TOPIC_RECOMMEND;
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", page);
        params.put("topic_id", topicId);
        params.put("all", 1);
        params.put("version", 1);
        if (userId != 0) {
            params.put("fuid", userId);
        }
        NetLoadUtils.getQyInstance().loadNetDataPost(getActivity(), url, params, new NetLoadUtils.NetLoadListener() {
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
                    } else if (!invitationDetailEntity.getCode().equals(EMPTY_CODE)) {
                        showToast(getActivity(), invitationDetailEntity.getMsg());
                    }
                    adapterInvitationAdapter.notifyDataSetChanged();
                }
                NetLoadUtils.getQyInstance().showLoadSir(loadService,invitationSearchList, invitationDetailEntity);
            }

            @Override
            public void netClose() {
                adapterInvitationAdapter.loadMoreComplete();
                NetLoadUtils.getQyInstance().showLoadSir(loadService,invitationSearchList, invitationDetailEntity);
            }

            @Override
            public void onError(Throwable throwable) {
                adapterInvitationAdapter.loadMoreComplete();
                NetLoadUtils.getQyInstance().showLoadSir(loadService,invitationSearchList, invitationDetailEntity);
            }
        });
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (message.type.equals("refreshFindTopicData")) {
            page = (int) message.result;
            getTopicData();
        }
    }

    @Override
    protected void getReqParams(Bundle bundle) {
        super.getReqParams(bundle);
        topicId = bundle.getString("topicId");
    }
}
