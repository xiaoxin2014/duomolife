package com.amkj.dmsh.user.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.BaseApplication;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.homepage.adapter.SearchDetailsUserAdapter;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.mine.bean.UserAttentionFansEntity;
import com.amkj.dmsh.mine.bean.UserAttentionFansEntity.UserAttentionFansBean;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.chad.library.adapter.base.BaseQuickAdapter;
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

import static com.amkj.dmsh.constant.ConstantMethod.getPersonalInfo;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_TOTAL_COUNT;

;

public class UserFansAttentionActivity extends BaseActivity {
    @BindView(R.id.smart_communal_refresh)
    RefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    @BindView(R.id.tv_header_shared)
    TextView header_shared;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.communal_load)
    View communal_load;
    @BindView(R.id.communal_error)
    View communal_error;
    @BindView(R.id.communal_empty)
    View communal_empty;
    private List<UserAttentionFansBean> attentionFansList = new ArrayList();
    private int uid;
    private SearchDetailsUserAdapter detailsUserAdapter;
    private String type;
    private String fromPage;
    private int mid;
    private int page = 1;
    private int scrollY = 0;
    private float screenHeight;
    @Override
    protected int getContentView() {
        return R.layout.layout_communal_refresh_floating_header;
    }
    @Override
    protected void initViews() {
        isLoginStatus();
        header_shared.setVisibility(View.INVISIBLE);
        Intent intent = getIntent();
//        产看的用户Id
        uid = intent.getIntExtra("uid", 0);
        type = intent.getStringExtra("type");
        fromPage = intent.getStringExtra("fromPage");
        if (fromPage != null && fromPage.equals("mine")) {
            if ("fans".equals(type)) {
                tv_header_titleAll.setText("我的粉丝");
            } else {
                tv_header_titleAll.setText("我的关注");
            }
        } else {
            if ("fans".equals(type)) {
                tv_header_titleAll.setText("Ta的粉丝");
            } else {
                tv_header_titleAll.setText("Ta的关注");
            }
        }
        detailsUserAdapter = new SearchDetailsUserAdapter(UserFansAttentionActivity.this, attentionFansList, type);
        communal_recycler.setLayoutManager(new LinearLayoutManager(this));
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
                if (page * DEFAULT_TOTAL_COUNT <= detailsUserAdapter.getItemCount()) {
                    page++;
                    loadData();
                } else {
                    detailsUserAdapter.loadMoreEnd();
                }
            }
        }, communal_recycler);

        smart_communal_refresh.setOnRefreshListener((refreshLayout) -> {

                page = 1;
                loadData();
        });
        download_btn_communal.attachToRecyclerView(communal_recycler, null, new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                scrollY += dy;
                if (!recyclerView.canScrollVertically(-1)) {
                    scrollY = 0;
                }
                if (screenHeight == 0) {
                    BaseApplication app = (BaseApplication) getApplication();
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
                    if (type.equals("attention")) {
                        if (uid > 0 && uid != userAttentionFansBean.getBuid()) {
                            intent.setClass(UserFansAttentionActivity.this, UserPagerActivity.class);
                            intent.putExtra("userId", String.valueOf(userAttentionFansBean.getBuid()));
                            startActivity(intent);
                        }
                    } else {
                        if (uid > 0 && uid != userAttentionFansBean.getFuid()) {
                            intent.setClass(UserFansAttentionActivity.this, UserPagerActivity.class);
                            intent.putExtra("userId", String.valueOf(userAttentionFansBean.getFuid()));
                            startActivity(intent);
                        }
                    }
                }
            }
        });
        communal_load.setVisibility(View.VISIBLE);
    }

    @Override
    protected void loadData() {
        String url;
        if ("fans".equals(type)) {
            url = Url.BASE_URL + Url.MINE_FANS;
        } else {
            url = Url.BASE_URL + Url.MINE_ATTENTION;
        }
        Map<String, Object> params = new HashMap<>();
//            查看用户ID
        params.put("uid", uid);
        if (type.equals("fans")) {
            if (mid != 0) {
                params.put("buid", mid);
            }
        } else {
            if (mid != 0) {
                params.put("fuid", mid);
            }
        }
        params.put("currentPage", page);
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                smart_communal_refresh.finishRefresh();
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
                        showToast(UserFansAttentionActivity.this, userAttentionFansEntity.getMsg());
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
                smart_communal_refresh.finishRefresh();
                detailsUserAdapter.loadMoreComplete();
                communal_load.setVisibility(View.GONE);
                communal_error.setVisibility(View.GONE);
                communal_empty.setVisibility(View.GONE);
                super.onError(ex, isOnCallback);
            }
        });
    }

    private void isLoginStatus() {
        SavePersonalInfoBean personalInfo = getPersonalInfo(this);
        if (personalInfo.isLogin()) {
            mid = personalInfo.getUid();
        } else {
            mid = 0;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ConstantVariable.IS_LOGIN_CODE) {
                isLoginStatus();
                loadData();
            }
        }
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (message.type.equals("attentionRefresh")) {
            if (message.result.equals("refresh")) {
                page = 1;
                loadData();
            }
        }
    }

    @OnClick({R.id.tv_life_back})
    void goBack(View view) {
        finish();
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
