package com.amkj.dmsh.homepage.fragment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseApplication;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.bean.UserSearchEntity;
import com.amkj.dmsh.bean.UserSearchEntity.UserSearchBean;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.homepage.adapter.SearchDetailsUserAdapter;
import com.amkj.dmsh.mine.activity.MineLoginActivity;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.mine.bean.UserAttentionFansEntity.UserAttentionFansBean;
import com.amkj.dmsh.user.activity.UserPagerActivity;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static com.amkj.dmsh.constant.ConstantMethod.getPersonalInfo;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_TOTAL_COUNT;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;

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
    @BindView(R.id.communal_load)
    View communal_load;
    @BindView(R.id.communal_error)
    View communal_error;
    @BindView(R.id.communal_empty)
    View communal_empty;
    List<UserAttentionFansBean> userAttentionFansList = new ArrayList();
    int page = 1;
    //当前用户ID
    private int uid;
    private String data;
    private String type = "search";
    private SearchDetailsUserAdapter userRecyclerAdapter;
    private int scrollY = 0;
    private float screenHeight;

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_smart_refresh_recycler_float_loading;
    }

    @Override
    protected void initViews() {
        isLoginStatus();
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
            page = 1;
            loadData();
        });
        userRecyclerAdapter.setOnLoadMoreListener(() -> {
            if (page * DEFAULT_TOTAL_COUNT <= userRecyclerAdapter.getItemCount()) {
                page++;
                loadData();
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
                if (uid > 0) {
                    if (type.equals("attention")) {
                        if (uid != userAttentionFansBean.getBuid()) {
                            intent.setClass(getActivity(), UserPagerActivity.class);
                            intent.putExtra("userId", String.valueOf(userAttentionFansBean.getBuid()));
                            startActivity(intent);
                        } else {
                            showToast(getActivity(), R.string.not_attention_self);
                        }
                    } else {
                        if (uid != userAttentionFansBean.getFuid()) {
                            intent.setClass(getActivity(), UserPagerActivity.class);
                            intent.putExtra("userId", String.valueOf(userAttentionFansBean.getFuid()));
                            startActivity(intent);
                        } else {
                            showToast(getActivity(), R.string.not_attention_self);
                        }
                    }
                } else {
                    getLoginStatus();
                }
            }
        });
        communal_load.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        isLoginStatus();
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (message.type.equals("search4")) {
            String resultText = (String) message.result;
            if (!resultText.equals(data)) {
                page = 1;
                data = resultText;
                loadData();
            }
        }
    }

    private void isLoginStatus() {
        SavePersonalInfoBean personalInfo = getPersonalInfo(getActivity());
        if (personalInfo.isLogin()) {
            uid = personalInfo.getUid();
        } else {
            uid = 0;
        }
    }

    private void getLoginStatus() {
        SavePersonalInfoBean personalInfo = getPersonalInfo(getActivity());
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
        if (requestCode == IS_LOGIN_CODE) {
            getLoginStatus();
        }
    }

    @Override
    protected void loadData() {
        getUserDetails();
    }

    private void getUserDetails() {
        if (!TextUtils.isEmpty(data)) {
            String url = Url.BASE_URL + Url.H_HOT_SEARCH_USER;
            if (NetWorkUtils.checkNet(getActivity())) {
                Map<String, Object> params = new HashMap<>();
                if (uid > 0) {
                    //当前用户ID
                    params.put("uid", uid);
                }
                params.put("keyword", data);
                params.put("currentPage", page);
                XUtil.Post(url, params, new MyCallBack<String>() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();
                        userRecyclerAdapter.loadMoreComplete();
                        communal_load.setVisibility(View.GONE);
                        communal_error.setVisibility(View.GONE);
                        communal_empty.setVisibility(View.GONE);
                        if (page == 1) {
                            userAttentionFansList.clear();
                        }
                        Gson gson = new Gson();
                        UserSearchEntity userSearchEntity = gson.fromJson(result, UserSearchEntity.class);
                        if (userSearchEntity != null) {
                            if (userSearchEntity.getCode().equals("01")) {
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
                            } else if (!userSearchEntity.getCode().equals("02")) {
                                showToast(getActivity(), userSearchEntity.getMsg());
                            }
                            if (page == 1) {
                                userRecyclerAdapter.setNewData(userAttentionFansList);
                            } else {
                                userRecyclerAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        if (page == 1 && userRecyclerAdapter.getItemCount() < 1) {
                            communal_load.setVisibility(View.GONE);
                            communal_error.setVisibility(View.VISIBLE);
                        }
                        smart_communal_refresh.finishRefresh();
                        userRecyclerAdapter.loadMoreComplete();
                        super.onError(ex, isOnCallback);
                    }
                });
            } else {
                smart_communal_refresh.finishRefresh();
                userRecyclerAdapter.loadMoreComplete();
                showToast(getActivity(), R.string.unConnectedNetwork);
                communal_load.setVisibility(View.GONE);
                communal_error.setVisibility(View.VISIBLE);
            }
        }
    }

    @OnClick({R.id.rel_communal_error, R.id.communal_empty})
    void refreshData(View view) {
        communal_load.setVisibility(View.VISIBLE);
        communal_empty.setVisibility(View.GONE);
        communal_error.setVisibility(View.GONE);
        page = 1;
        loadData();
    }
}
