package com.amkj.dmsh.user.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.base.NetLoadUtils;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.homepage.adapter.SearchDetailsUserAdapter;
import com.amkj.dmsh.mine.bean.UserAttentionFansEntity;
import com.amkj.dmsh.mine.bean.UserAttentionFansEntity.UserAttentionFansBean;
import com.amkj.dmsh.netloadpage.NetEmptyCallback;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.kingja.loadsir.core.Transport;
import com.melnykov.fab.FloatingActionButton;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tencent.bugly.beta.tinker.TinkerManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_TOTAL_COUNT;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;

;
;

public class UserFansAttentionActivity extends BaseActivity {
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    @BindView(R.id.tv_header_shared)
    TextView header_shared;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    private List<UserAttentionFansBean> attentionFansList = new ArrayList();
    private SearchDetailsUserAdapter detailsUserAdapter;
    private String type;
    private int page = 1;
    private int scrollY = 0;
    private float screenHeight;
    private UserAttentionFansEntity userAttentionFansEntity;

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_refresh_floating_header;
    }

    @Override
    protected void initViews() {
        header_shared.setVisibility(View.INVISIBLE);
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        if ("fans".equals(type)) {
            tv_header_titleAll.setText("我的粉丝");
        } else {
            tv_header_titleAll.setText("我的关注");
        }
        detailsUserAdapter = new SearchDetailsUserAdapter(UserFansAttentionActivity.this, attentionFansList, type);
        communal_recycler.setLayoutManager(new LinearLayoutManager(this));
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_gray_f_two_px)






                .create());
        communal_recycler.setAdapter(detailsUserAdapter);
        detailsUserAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (page * DEFAULT_TOTAL_COUNT <= detailsUserAdapter.getItemCount()) {
                    page++;
                    getData();
                } else {
                    detailsUserAdapter.loadMoreEnd();
                }
            }
        }, communal_recycler);

        smart_communal_refresh.setOnRefreshListener((refreshLayout) -> {
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
                        if (userId != userAttentionFansBean.getBuid()) {
                            intent.setClass(UserFansAttentionActivity.this, UserPagerActivity.class);
                            intent.putExtra("userId", String.valueOf(userAttentionFansBean.getBuid()));
                            startActivity(intent);
                        }
                    } else {
                        if (userId != userAttentionFansBean.getFuid()) {
                            intent.setClass(UserFansAttentionActivity.this, UserPagerActivity.class);
                            intent.putExtra("userId", String.valueOf(userAttentionFansBean.getFuid()));
                            startActivity(intent);
                        }
                    }
                }
            }
        });
        if (loadService != null) {
            loadService.setCallBack(NetEmptyCallback.class, new Transport() {
                @Override
                public void order(Context context, View view) {
                    TextView tv_communal_net_tint = view.findViewById(R.id.tv_communal_net_tint);
                    tv_communal_net_tint.setText("attention".equals(type) ? "你还没有关注Ta人\n赶快去围观各位千手观音吧"
                            : "还没有人关注你\n赶快去吸引粉丝吧");
                }
            });
        }
    }

    @Override
    protected void loadData() {
        page = 1;
        getData();
    }

    @Override
    protected View getLoadView() {
        return smart_communal_refresh;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    protected void getData() {
        String url;
        Map<String, Object> params = new HashMap<>();
        if ("fans".equals(type)) {
            url = Url.BASE_URL + Url.MINE_FANS;
            if(userId>0){
                params.put("buid", userId);
            }
        } else {
            url = Url.BASE_URL + Url.MINE_ATTENTION;
            if(userId>0){
                params.put("fuid", userId);
            }
        }
//            查看用户ID
        params.put("uid", userId);

        params.put("currentPage", page);
        NetLoadUtils.getQyInstance().loadNetDataPost(mAppContext, url, params, new NetLoadUtils.NetLoadListener() {
            @Override
            public void onSuccess(String result) {
                smart_communal_refresh.finishRefresh();
                detailsUserAdapter.loadMoreComplete();
                if (page == 1) {
                    attentionFansList.clear();
                }
                Gson gson = new Gson();
                userAttentionFansEntity = gson.fromJson(result, UserAttentionFansEntity.class);
                if (userAttentionFansEntity != null) {
                    if (userAttentionFansEntity.getCode().equals(SUCCESS_CODE)) {
                        attentionFansList.addAll(userAttentionFansEntity.getUserAttentionFansList());
                    } else if (!userAttentionFansEntity.getCode().equals(EMPTY_CODE)) {
                        showToast(UserFansAttentionActivity.this, userAttentionFansEntity.getMsg());
                    }
                }
                detailsUserAdapter.notifyDataSetChanged();
                NetLoadUtils.getQyInstance().showLoadSir(loadService, attentionFansList,userAttentionFansEntity);
            }

            @Override
            public void netClose() {
                smart_communal_refresh.finishRefresh();
                detailsUserAdapter.loadMoreComplete();
                NetLoadUtils.getQyInstance().showLoadSir(loadService, attentionFansList,userAttentionFansEntity);
            }

            @Override
            public void onError(Throwable throwable) {
                smart_communal_refresh.finishRefresh();
                detailsUserAdapter.loadMoreComplete();
                NetLoadUtils.getQyInstance().showLoadSir(loadService, attentionFansList,userAttentionFansEntity);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IS_LOGIN_CODE) {
            loadData();
        }
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (message.type.equals("attentionRefresh")) {
            if (message.result.equals("refresh")) {
                loadData();
            }
        }
    }

    @OnClick({R.id.tv_life_back})
    void goBack(View view) {
        finish();
    }


}
