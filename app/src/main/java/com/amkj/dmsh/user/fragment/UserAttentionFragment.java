package com.amkj.dmsh.user.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseApplication;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.homepage.adapter.SearchDetailsUserAdapter;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.mine.bean.UserAttentionFansEntity;
import com.amkj.dmsh.mine.bean.UserAttentionFansEntity.UserAttentionFansBean;
import com.amkj.dmsh.user.activity.UserPagerActivity;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;

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
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/8/10
 * class description:用户关注
 */
public class UserAttentionFragment extends BaseFragment {
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
    private List<UserAttentionFansBean> attentionFansList = new ArrayList();
    private int uid;
    private SearchDetailsUserAdapter detailsUserAdapter;
    private int mid;
    private int page = 1;
    private int scrollY = 0;
    private float screenHeight;
    private String type = "attention";
    @Override
    protected int getContentView() {
        return R.layout.layout_communal_recycler_floating_loading;
    }
    @Override
    protected void initViews() {
        isLoginStatus();
        detailsUserAdapter = new SearchDetailsUserAdapter(getActivity(), attentionFansList, type);
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
        communal_recycler.setAdapter(detailsUserAdapter);
        detailsUserAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (page * DEFAULT_TOTAL_COUNT <= attentionFansList.size()) {
                    page++;
                    loadData();
                } else {
                    detailsUserAdapter.loadMoreEnd();
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
        detailsUserAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                UserAttentionFansBean userAttentionFansBean = (UserAttentionFansBean) view.getTag();
                if (userAttentionFansBean != null) {
                    Intent intent = new Intent();
                    if (uid > 0 && uid != userAttentionFansBean.getBuid()) {
                        intent.setClass(getActivity(), UserPagerActivity.class);
                        intent.putExtra("userId", String.valueOf(userAttentionFansBean.getBuid()));
                        startActivity(intent);
                    }
                }
            }
        });
        communal_load.setVisibility(View.VISIBLE);
    }

    private void isLoginStatus() {
        SavePersonalInfoBean personalInfo = getPersonalInfo(getActivity());
        if (personalInfo.isLogin()) {
            mid = personalInfo.getUid();
        } else {
            mid = 0;
        }
    }

    @Override
    protected void loadData() {
        String url = Url.BASE_URL + Url.MINE_ATTENTION;
        Map<String, Object> params = new HashMap<>();
//            查看用户ID
        params.put("uid", uid);
        if (mid != 0) {
            params.put("fuid", mid);
        }
        params.put("currentPage", page);
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                detailsUserAdapter.loadMoreComplete();
                communal_load.setVisibility(View.GONE);
                communal_error.setVisibility(View.GONE);
                communal_empty.setVisibility(View.GONE);
                if (page == 1) {
                    attentionFansList.clear();
                }
                Gson gson = new Gson();
                UserAttentionFansEntity userAttentionFansEntity = gson.fromJson(result, UserAttentionFansEntity.class);
                if (userAttentionFansEntity != null) {
                    if (userAttentionFansEntity.getCode().equals("01")) {
                        attentionFansList.addAll(userAttentionFansEntity.getUserAttentionFansList());
                    } else if (!userAttentionFansEntity.getCode().equals("02")) {
                        showToast(getActivity(), userAttentionFansEntity.getMsg());
                    }
                }
                if (page == 1) {
                    detailsUserAdapter.setNewData(attentionFansList);
                } else {
                    detailsUserAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                detailsUserAdapter.loadMoreComplete();
                communal_load.setVisibility(View.GONE);
                communal_error.setVisibility(View.GONE);
                communal_empty.setVisibility(View.GONE);
                super.onError(ex, isOnCallback);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IS_LOGIN_CODE && resultCode == RESULT_OK) {
            isLoginStatus();
            loadData();
        }
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (message.type.equals("attentionRefresh")) {
            if (message.result.equals("refresh")) {
                page = 1;
                loadData();
            }
        } else if (message.type.equals("refreshMineData")) {
            page = (int) message.result;
            loadData();
        }
    }

    @Override
    protected void getReqParams(Bundle bundle) {
        String userId = bundle.getString("userId");
        if (!TextUtils.isEmpty(userId)) {
            uid = Integer.parseInt(userId);
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
