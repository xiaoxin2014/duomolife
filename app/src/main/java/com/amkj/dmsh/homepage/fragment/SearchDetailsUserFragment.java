package com.amkj.dmsh.homepage.fragment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.base.NetLoadUtils;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.UserSearchEntity;
import com.amkj.dmsh.bean.UserSearchEntity.UserSearchBean;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.homepage.adapter.SearchDetailsUserAdapter;
import com.amkj.dmsh.mine.bean.UserAttentionFansEntity.UserAttentionFansBean;
import com.amkj.dmsh.user.activity.UserPagerActivity;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.amkj.dmsh.utils.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.tencent.bugly.beta.tinker.TinkerManager;

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
public class SearchDetailsUserFragment extends BaseFragment {
    @BindView(R.id.smart_communal_refresh)
    RefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    List<UserAttentionFansBean> userAttentionFansList = new ArrayList();
    private int page = 1;
    //当前用户ID
    private String data;
    private String type = "search";
    private SearchDetailsUserAdapter userRecyclerAdapter;
    private int scrollY = 0;
    private float screenHeight;
    private UserSearchEntity userSearchEntity;

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_smart_refresh_recycler_float_loading;
    }

    @Override
    protected void initViews() {
        communal_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        communal_recycler.addItemDecoration(new PinnedHeaderItemDecoration.Builder(-1)
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_gray_f_two_px)
                // 开启绘制分隔线，默认关闭
                .enableDivider(true)
                // 是否关闭标签点击事件，默认开启
                .disableHeaderClick(false)
                // 设置标签和其内部的子控件的监听，若设置点击监听不为null，但是disableHeaderClick(true)的话，还是不会响应点击事件
                .setHeaderClickListener(null)
                .create());
        userRecyclerAdapter = new SearchDetailsUserAdapter(getActivity(), userAttentionFansList, type);
        communal_recycler.setAdapter(userRecyclerAdapter);
        userRecyclerAdapter.setEmptyView(R.layout.layout_search_user_empty, (ViewGroup) communal_recycler.getParent());

        smart_communal_refresh.setOnRefreshListener((refreshLayout) -> {
            loadData();
        });
        userRecyclerAdapter.setOnLoadMoreListener(() -> {
            if (page * DEFAULT_TOTAL_COUNT <= userRecyclerAdapter.getItemCount()) {
                page++;
                getUserDetails();
            } else {
                userRecyclerAdapter.loadMoreEnd();
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
                    download_btn_communal.setVisibility(View.GONE);
                }
            }
        });
        download_btn_communal.setOnClickListener(v -> {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) communal_recycler.getLayoutManager();
            int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
            int mVisibleCount = linearLayoutManager.findLastVisibleItemPosition()
                    - linearLayoutManager.findFirstVisibleItemPosition() + 1;
            if (firstVisibleItemPosition > mVisibleCount) {
                communal_recycler.scrollToPosition(mVisibleCount);
            }
            communal_recycler.smoothScrollToPosition(0);
        });
        userRecyclerAdapter.setOnItemClickListener((adapter, view, position) -> {
            UserAttentionFansBean userAttentionFansBean = (UserAttentionFansBean) view.getTag();
            if (userAttentionFansBean != null) {
                Intent intent = new Intent();
                if (userId > 0) {
                    if (type.equals("attention")) {
                        if (userId != userAttentionFansBean.getBuid()) {
                            intent.setClass(getActivity(), UserPagerActivity.class);
                            intent.putExtra("userId", String.valueOf(userAttentionFansBean.getBuid()));
                            startActivity(intent);
                        } else {
                            showToast(getActivity(), R.string.not_attention_self);
                        }
                    } else {
                        if (userId != userAttentionFansBean.getFuid()) {
                            intent.setClass(getActivity(), UserPagerActivity.class);
                            intent.putExtra("userId", String.valueOf(userAttentionFansBean.getFuid()));
                            startActivity(intent);
                        } else {
                            showToast(getActivity(), R.string.not_attention_self);
                        }
                    }
                } else {
                    getLoginStatus(SearchDetailsUserFragment.this);
                }
            }
        });
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (message.type.equals("search4")) {
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
        getUserDetails();
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    private void getUserDetails() {
        if (TextUtils.isEmpty(data)) {
            return;
        }
        String url = Url.BASE_URL + Url.H_HOT_SEARCH_USER;
        Map<String, Object> params = new HashMap<>();
        if (userId > 0) {
            //当前用户ID
            params.put("uid", userId);
        }
        params.put("keyword", data);
        params.put("currentPage", page);
        NetLoadUtils.getQyInstance().loadNetDataPost(getActivity(), url
                , params, new NetLoadUtils.NetLoadListener() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();
                        userRecyclerAdapter.loadMoreComplete();
                        if (page == 1) {
                            userAttentionFansList.clear();
                        }
                        Gson gson = new Gson();
                        userSearchEntity = gson.fromJson(result, UserSearchEntity.class);
                        if (userSearchEntity != null) {
                            if (userSearchEntity.getCode().equals(SUCCESS_CODE)) {
                                List<UserSearchBean> userSearchList = userSearchEntity.getUserSearchList();
                                UserAttentionFansBean userAttentionFansBean;
                                for (int i = 0; i < userSearchList.size(); i++) {
                                    userAttentionFansBean = new UserAttentionFansBean();
                                    UserSearchBean user = userSearchList.get(i);
                                    userAttentionFansBean.setFavatar(user.getAvatar());
                                    userAttentionFansBean.setFlag(user.isFlag());
                                    userAttentionFansBean.setFuid(user.getUid());
                                    userAttentionFansBean.setFnickname(user.getNickname());
                                    userAttentionFansList.add(userAttentionFansBean);
                                }
                            } else if (!userSearchEntity.getCode().equals(EMPTY_CODE)) {
                                showToast(getActivity(), userSearchEntity.getMsg());
                            }
                            userRecyclerAdapter.notifyDataSetChanged();
                        }
                        NetLoadUtils.getQyInstance().showLoadSir(loadService, userAttentionFansList, userSearchEntity);
                    }

                    @Override
                    public void netClose() {
                        smart_communal_refresh.finishRefresh();
                        userRecyclerAdapter.loadMoreComplete();
                        NetLoadUtils.getQyInstance().showLoadSir(loadService, userAttentionFansList, userSearchEntity);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        smart_communal_refresh.finishRefresh();
                        userRecyclerAdapter.loadMoreComplete();
                        showToast(getActivity(), R.string.unConnectedNetwork);
                        NetLoadUtils.getQyInstance().showLoadSir(loadService, userAttentionFansList, userSearchEntity);
                    }
                });
    }
}
