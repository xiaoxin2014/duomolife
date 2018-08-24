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
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.find.activity.ArticleDetailsImgActivity;
import com.amkj.dmsh.find.activity.ArticleInvitationDetailsActivity;
import com.amkj.dmsh.find.adapter.PullUserInvitationAdapter;
import com.amkj.dmsh.homepage.bean.InvitationDetailEntity;
import com.amkj.dmsh.homepage.bean.InvitationDetailEntity.InvitationDetailBean;
import com.amkj.dmsh.mine.activity.MineLoginActivity;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.utils.CommunalCopyTextUtils;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.amkj.dmsh.utils.inteface.MyProgressCallBack;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_TOTAL_COUNT;

;

/**
 * Created by atd48 on 2016/6/21.
 */
public class RecommendSuperFragment extends BaseFragment {
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    @BindView(R.id.communal_load)
    View communal_load;
    @BindView(R.id.communal_error)
    View communal_error;
    @BindView(R.id.communal_empty)
    View communal_empty;
    private List<InvitationDetailBean> invitationSearchList = new ArrayList();
    private int page = 1;
    private PullUserInvitationAdapter adapterInvitationAdapter;
    private String type = "recommend";
    private int uid;
    @Override
    protected int getContentView() {
        return R.layout.layout_communal_recycler_loading;
    }
    @Override
    protected void initViews() {
        isLoginStatus();
        type = "recommend";
        communal_recycler.setBackgroundColor(getResources().getColor(R.color.white));
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
        adapterInvitationAdapter = new PullUserInvitationAdapter(getActivity(), invitationSearchList, type);
        communal_recycler.setAdapter(adapterInvitationAdapter);
        adapterInvitationAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (page * DEFAULT_TOTAL_COUNT <= adapterInvitationAdapter.getItemCount()) {
                    page++;
                    loadData();
                } else {
                    adapterInvitationAdapter.loadMoreEnd();
                }
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
                            if (uid > 0) {
                                loadHud.show();
                                setArticleCollect(invitationDetailBean, view);
                            } else {
                                getLoginStatus();
                            }
                            break;
                        case R.id.tv_com_art_like_count:
                            if (uid > 0) {
                                setArticleLiked(invitationDetailBean, view);
                            } else {
                                getLoginStatus();
                            }
                            break;
                    }
                }
            }
        });
        communal_load.setVisibility(View.VISIBLE);
    }

    //    文章收藏
    private void setArticleCollect(final InvitationDetailBean invitationDetailBean, View view) {
        final TextView tv_collect = (TextView) view;
        String url = Url.BASE_URL + Url.F_ARTICLE_COLLECT;
        Map<String, Object> params = new HashMap<>();
        //用户id
        params.put("uid", uid);
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
        params.put("tuid", uid);
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

    //设置显示
    //第一个设置
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setUserVisibleHint(true);
        super.onActivityCreated(savedInstanceState);
    }
    private void isLoginStatus() {
        SavePersonalInfoBean personalInfo = ConstantMethod.getPersonalInfo(getActivity());
        if (personalInfo.isLogin()) {
            uid = personalInfo.getUid();
        } else {
            uid = 0;
        }
    }

    private void getLoginStatus() {
        SavePersonalInfoBean personalInfo = ConstantMethod.getPersonalInfo(getActivity());
        if (personalInfo.isLogin()) {
            uid = personalInfo.getUid();
        } else {
            //未登录跳转登录页
            Intent intent = new Intent(getActivity(), MineLoginActivity.class);
            startActivityForResult(intent, ConstantVariable.IS_LOGIN_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConstantVariable.IS_LOGIN_CODE) {
            getLoginStatus();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        isLoginStatus();
    }

    @Override
    protected void loadData() {
        String url = Url.BASE_URL + Url.FIND_RECOMMEND;
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", page);
        params.put("isFllow", "recommend");
        if (uid != 0) {
            params.put("fuid", uid);
        }
//        v3.1.3 1 文章帖子
        params.put("version", 1);
        if (NetWorkUtils.isConnectedByState(getActivity())) {
            XUtil.Post(url, params, new MyProgressCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    adapterInvitationAdapter.loadMoreComplete();
                    communal_load.setVisibility(View.GONE);
                    communal_error.setVisibility(View.GONE);
                    if (page == 1) {
                        invitationSearchList.clear();
                    }
                    Gson gson = new Gson();
                    InvitationDetailEntity invitationDetailEntity = gson.fromJson(result, InvitationDetailEntity.class);
                    if (invitationDetailEntity != null) {
                        if (invitationDetailEntity.getCode().equals("01")) {
                            invitationSearchList.addAll(invitationDetailEntity.getInvitationSearchList());
                        } else if (!invitationDetailEntity.getCode().equals("02")) {
                            showToast(getActivity(), invitationDetailEntity.getMsg());
                        }
                        if (page == 1) {
                            adapterInvitationAdapter.setNewData(invitationSearchList);
                        } else {
                            adapterInvitationAdapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onStarted() {
                    if (page == 1 && adapterInvitationAdapter.getItemCount() < 1) {
                        communal_load.setVisibility(View.VISIBLE);
                        communal_error.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    if (page == 1 && adapterInvitationAdapter.getItemCount() < 1) {
                        communal_load.setVisibility(View.GONE);
                        communal_error.setVisibility(View.VISIBLE);
                    }
                    adapterInvitationAdapter.loadMoreComplete();
                }
            });
        } else {
            adapterInvitationAdapter.loadMoreComplete();
            communal_load.setVisibility(View.GONE);
            communal_error.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.rel_communal_error, R.id.communal_empty})
    void refreshData(View view) {
        communal_load.setVisibility(View.VISIBLE);
        communal_error.setVisibility(View.GONE);
        page = 1;
        loadData();
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (message.type.equals("refreshFindData")) {
            page = (int) message.result;
            loadData();
        }
    }
}
