package com.amkj.dmsh.homepage.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.UserSearchEntity;
import com.amkj.dmsh.bean.UserSearchEntity.UserSearchBean;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.homepage.adapter.SearchDetailsUserAdapter;
import com.amkj.dmsh.mine.bean.UserAttentionFansEntity.UserAttentionFansBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.user.activity.UserPagerActivity;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.bugly.beta.tinker.TinkerManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.homepage.activity.HomePageSearchActivity.SEARCH_DATA;

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
    private String type = "search";
    private SearchDetailsUserAdapter userRecyclerAdapter;
    private int scrollY = 0;
    private float screenHeight;
    private UserSearchEntity userSearchEntity;
    private String searchData;

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_smart_refresh_recycler_float_loading;
    }

    @Override
    protected void initViews() {
        communal_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_gray_f_two_px)


                .create());
        userRecyclerAdapter = new SearchDetailsUserAdapter(getActivity(), userAttentionFansList, type);
        communal_recycler.setAdapter(userRecyclerAdapter);
        userRecyclerAdapter.setEmptyView(R.layout.layout_search_user_empty, (ViewGroup) communal_recycler.getParent());

        smart_communal_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                loadData();
            }
        });
        userRecyclerAdapter.setOnLoadMoreListener(() -> {
            page++;
            getUserDetails();
        }, communal_recycler);
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
        if (message.type.equals(SEARCH_DATA)) {
            String resultText = (String) message.result;
            if (!resultText.equals(searchData)) {
                searchData = resultText;
                NetLoadUtils.getNetInstance().showLoadSirLoading(loadService);
                loadData();
            }
        }
    }

    @Override
    protected void getReqParams(Bundle bundle) {
        searchData = (String) bundle.get(SEARCH_DATA);
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
        if (TextUtils.isEmpty(searchData)) {
            NetLoadUtils.getNetInstance().showLoadSir(loadService, userAttentionFansList, userSearchEntity);
            return;
        }
        String url = Url.BASE_URL + Url.H_HOT_SEARCH_USER;
        Map<String, Object> params = new HashMap<>();
        if (userId > 0) {
            //当前用户ID
            params.put("uid", userId);
        }
        params.put("keyword", searchData);
        params.put("currentPage", page);
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), url
                , params, new NetLoadListenerHelper() {
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
                            } else if (userSearchEntity.getCode().equals(EMPTY_CODE)) {
                                userRecyclerAdapter.loadMoreEnd(true);
                            } else {
                                showToast(getActivity(), userSearchEntity.getMsg());
                            }
                            userRecyclerAdapter.notifyDataSetChanged();
                        }
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, userAttentionFansList, userSearchEntity);
                    }

                    @Override
                    public void onNotNetOrException() {
                        smart_communal_refresh.finishRefresh();
                        userRecyclerAdapter.loadMoreEnd(true);
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, userAttentionFansList, userSearchEntity);
                    }

                    @Override
                    public void netClose() {
                        showToast(getActivity(), R.string.unConnectedNetwork);
                    }
                });
    }
}
