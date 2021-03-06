package com.amkj.dmsh.mine.activity;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.find.adapter.PullUserInvitationAdapter;
import com.amkj.dmsh.homepage.bean.InvitationDetailEntity;
import com.amkj.dmsh.homepage.bean.InvitationDetailEntity.InvitationDetailBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.amkj.dmsh.views.alertdialog.AlertDialogHelper;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.bugly.beta.tinker.TinkerManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.showToastRequestMsg;
import static com.amkj.dmsh.constant.ConstantMethod.skipPostDetail;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TWENTY;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_C_ARTICLE;
import static com.amkj.dmsh.constant.Url.F_ARTICLE_COLLECT;
import static com.amkj.dmsh.constant.Url.F_ARTICLE_DETAILS_FAVOR;
import static com.amkj.dmsh.constant.Url.MINE_INVITATION_DEL;
import static com.amkj.dmsh.constant.Url.MINE_INVITATION_LIST;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/8/24
 * class description:??????????????????
 */
public class MineInvitationListActivity extends BaseActivity {
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    ???????????????
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    @BindView(R.id.tv_header_shared)
    TextView header_shared;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    private int page = 1;
    private int scrollY = 0;
    private float screenHeight;
    private PullUserInvitationAdapter adapterInvitationAdapter;
    private List<InvitationDetailBean> invitationDetailList = new ArrayList();
    private String type = "??????";
    private InvitationDetailBean invitationDetailBean;
    private InvitationDetailEntity invitationDetailEntity;
    private AlertDialogHelper delArticleDialogHelper;

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_refresh_floating_header;
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
        header_shared.setVisibility(View.INVISIBLE);
        tv_header_titleAll.setText("????????????");
        communal_recycler.setLayoutManager(new LinearLayoutManager(MineInvitationListActivity.this));
        smart_communal_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                loadData();
            }
        });
        adapterInvitationAdapter = new PullUserInvitationAdapter(MineInvitationListActivity.this, invitationDetailList, type);
        communal_recycler.setAdapter(adapterInvitationAdapter);
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // ?????????????????????ID
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
                invitationDetailBean = (InvitationDetailBean) view.getTag();
                if (invitationDetailBean != null) {
                    switch (view.getId()) {
                        case R.id.tv_com_art_collect_count:
                            if (userId > 0) {
                                loadHud.show();
                                setArticleCollect(invitationDetailBean, view);
                            } else {
                                getLoginStatus(MineInvitationListActivity.this);
                            }
                            break;
                        case R.id.tv_com_art_like_count:
                            if (userId > 0) {
                                setArticleLiked(invitationDetailBean, view);
                            } else {
                                getLoginStatus(MineInvitationListActivity.this);
                            }
                            break;
//                        ????????????
                        case R.id.tv_inv_live_att:
                            if (delArticleDialogHelper == null) {
                                delArticleDialogHelper = new AlertDialogHelper(MineInvitationListActivity.this);
                                delArticleDialogHelper.setTitleVisibility(View.GONE).setMsgTextGravity(Gravity.CENTER)
                                        .setMsg("???????????????????????????").setCancelText("??????").setConfirmText("??????").setCancelable(false)
                                        .setCancelTextColor(getResources().getColor(R.color.text_login_gray_s));
                                delArticleDialogHelper.setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                                    @Override
                                    public void confirm() {
                                        delArticle();
                                    }

                                    @Override
                                    public void cancel() {
                                    }
                                });
                            }
                            delArticleDialogHelper.show();
                            break;
                        //????????????
                        case R.id.tv_com_art_comment_count:
                            if (invitationDetailBean != null && invitationDetailBean.getArticletype() != 3) {
                                skipPostDetail(getActivity(), String.valueOf(invitationDetailBean.getId()), invitationDetailBean.getArticletype());
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
                getMyInvitationData();
            }
        }, communal_recycler);
    }

    @Override
    protected void loadData() {
        page = 1;
        getMyInvitationData();
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
    protected String getEmptyText() {
        return "????????????????????????\n??????????????????????????????";
    }

    private void getMyInvitationData() {
        if (userId < 1) {
            NetLoadUtils.getNetInstance().showLoadSirLoadFailed(loadService);
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", page);
        params.put("showCount", TOTAL_COUNT_TWENTY);
        params.put("uid", userId);
        params.put("version", 1);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, MINE_INVITATION_LIST,
                params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();
                        adapterInvitationAdapter.loadMoreComplete();
                        if (page == 1) {
                            invitationDetailList.clear();
                        }

                        invitationDetailEntity = GsonUtils.fromJson(result, InvitationDetailEntity.class);
                        if (invitationDetailEntity != null) {
                            if (invitationDetailEntity.getCode().equals(SUCCESS_CODE)) {
                                invitationDetailList.addAll(invitationDetailEntity.getInvitationSearchList());
                            } else if (invitationDetailEntity.getCode().equals(EMPTY_CODE)) {
                                adapterInvitationAdapter.loadMoreEnd();
                            } else {
                                showToast( invitationDetailEntity.getMsg());
                            }
                            adapterInvitationAdapter.notifyDataSetChanged();
                        }
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, invitationDetailList, invitationDetailEntity);
                    }

                    @Override
                    public void onNotNetOrException() {
                        smart_communal_refresh.finishRefresh();
                        adapterInvitationAdapter.loadMoreEnd(true);
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, invitationDetailList, invitationDetailEntity);
                    }
                });
    }

    //    ????????????
    private void setArticleCollect(final InvitationDetailBean invitationDetailBean, View view) {
        final TextView tv_collect = (TextView) view;
        Map<String, Object> params = new HashMap<>();
        //??????id
        params.put("uid", userId);
        //??????id
        params.put("object_id", invitationDetailBean.getId());
        params.put("type", TYPE_C_ARTICLE);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, F_ARTICLE_COLLECT, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                loadHud.dismiss();

                RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        tv_collect.setSelected(!tv_collect.isSelected());
                        tv_collect.setText(ConstantMethod.getNumCount(tv_collect.isSelected(), invitationDetailBean.isCollect(), invitationDetailBean.getCollect(), "??????"));
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                loadHud.dismiss();
            }

            @Override
            public void onError(Throwable throwable) {
                showToast( R.string.collect_failed);
            }
        });
    }

    private void setArticleLiked(InvitationDetailBean invitationDetailBean, View view) {
        TextView tv_like = (TextView) view;
        Map<String, Object> params = new HashMap<>();
        //??????id
        params.put("tuid", userId);
        //??????id
        params.put("id", invitationDetailBean.getId());
        params.put("favortype", "doc");
        NetLoadUtils.getNetInstance().loadNetDataPost(this, F_ARTICLE_DETAILS_FAVOR, params, null);
        tv_like.setSelected(!tv_like.isSelected());
        tv_like.setText(ConstantMethod.getNumCount(tv_like.isSelected(), invitationDetailBean.isFavor(), invitationDetailBean.getFavor(), "???"));
    }

    private void delArticle() {
        Map<String, Object> params = new HashMap<>();
        params.put("id", invitationDetailBean.getId());
        params.put("uid", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, MINE_INVITATION_DEL, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {

                RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        showToast("??????????????????");
                        page = 1;
                        loadData();
                    } else {
                        showToastRequestMsg(requestStatus);
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IS_LOGIN_CODE) {
            loadData();
        }
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }
}
