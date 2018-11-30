package com.amkj.dmsh.message.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.message.adapter.MessageOfficialAdapter;
import com.amkj.dmsh.message.bean.MessageOfficialEntity;
import com.amkj.dmsh.message.bean.MessageOfficialEntity.MessageOfficialBean;
import com.amkj.dmsh.network.NetLoadUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.bugly.beta.tinker.TinkerManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static android.view.View.GONE;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_TOTAL_COUNT;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/8/1
 * class description:活动消息
 */
public class MessageHotActivity extends BaseActivity {
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tv_header_shared)
    TextView tv_header_shared;
    private int page = 1;
    private int scrollY;
    private float screenHeight;
    private MessageOfficialAdapter messageOfficialAdapter;
    //官方通知数据
    private List<MessageOfficialBean> messageArticleList = new ArrayList();
    private MessageOfficialEntity messageOfficialEntity;

    @Override
    protected int getContentView() {
        return R.layout.activity_communal_header_recycler_refresh;
    }

    @Override
    protected void initViews() {
        getLoginStatus(MessageHotActivity.this);
        tv_header_shared.setVisibility(GONE);
        tv_header_titleAll.setText("活动消息");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MessageHotActivity.this);
        communal_recycler.setLayoutManager(linearLayoutManager);
        messageOfficialAdapter = new MessageOfficialAdapter(MessageHotActivity.this, messageArticleList);
        communal_recycler.setAdapter(messageOfficialAdapter);
        messageOfficialAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MessageOfficialBean messageOfficialBean = (MessageOfficialBean) view.getTag();
                if (messageOfficialBean != null) {
                    if (!TextUtils.isEmpty(messageOfficialBean.getAndroid_link())) {
                        ConstantMethod.setSkipPath(MessageHotActivity.this, messageOfficialBean.getAndroid_link(), false);
                    } else {
                        Intent intent = new Intent();
                        intent.setClass(MessageHotActivity.this, OfficialNotifyDetailsActivity.class);
                        intent.putExtra("notifyId", String.valueOf(messageOfficialBean.getId()));
                        startActivity(intent);
                    }
                    if (!messageOfficialBean.isRead()) {
                        messageOfficialBean.setRead(!messageOfficialBean.isRead());
                        messageArticleList.set(position, messageOfficialBean);
                        messageOfficialAdapter.notifyItemChanged(position);
                    }
                }
            }
        });
        messageOfficialAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (page * DEFAULT_TOTAL_COUNT <= messageArticleList.size()) {
                    page++;
                    getData();
                } else {
                    messageOfficialAdapter.loadMoreEnd();
                }
            }
        }, communal_recycler);

        smart_communal_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                loadData();
            }
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
                    screenHeight = app.getScreenHeight();
                }
                if (scrollY >= screenHeight) {
                    download_btn_communal.setVisibility(View.VISIBLE);
                } else {
                    download_btn_communal.setVisibility(GONE);
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            if (requestCode == IS_LOGIN_CODE) {
                finish();
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IS_LOGIN_CODE) {
            loadData();
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
        String url = Url.BASE_URL + Url.H_MES_OFFICIAL;
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", page);
        if (userId > 0) {
            params.put("uid", userId);
        }
        NetLoadUtils.getQyInstance().loadNetDataPost(MessageHotActivity.this, url
                , params, new NetLoadUtils.NetLoadListener() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();
                        messageOfficialAdapter.loadMoreComplete();
                        if (page == 1) {
                            messageArticleList.clear();
                        }
                        Gson gson = new Gson();
                        messageOfficialEntity = gson.fromJson(result, MessageOfficialEntity.class);
                        if (messageOfficialEntity != null) {
                            if (messageOfficialEntity.getCode().equals(SUCCESS_CODE)) {
                                messageArticleList.addAll(messageOfficialEntity.getMessageOfficialList());
                            } else if (!messageOfficialEntity.getCode().equals(EMPTY_CODE)) {
                                showToast(MessageHotActivity.this, messageOfficialEntity.getMsg());
                            }
                        }
                        messageOfficialAdapter.notifyDataSetChanged();
                        NetLoadUtils.getQyInstance().showLoadSir(loadService, messageArticleList, messageOfficialEntity);
                    }

                    @Override
                    public void netClose() {
                        smart_communal_refresh.finishRefresh();
                        messageOfficialAdapter.loadMoreComplete();
                        showToast(MessageHotActivity.this, R.string.unConnectedNetwork);
                        NetLoadUtils.getQyInstance().showLoadSir(loadService, messageArticleList, messageOfficialEntity);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        smart_communal_refresh.finishRefresh();
                        messageOfficialAdapter.loadMoreComplete();
                        showToast(MessageHotActivity.this, R.string.invalidData);
                        NetLoadUtils.getQyInstance().showLoadSir(loadService, messageArticleList, messageOfficialEntity);
                    }
                });
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }

}
