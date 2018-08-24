package com.amkj.dmsh.message.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.BaseApplication;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.message.adapter.MessageOfficialAdapter;
import com.amkj.dmsh.message.bean.MessageOfficialEntity;
import com.amkj.dmsh.message.bean.MessageOfficialEntity.MessageOfficialBean;
import com.amkj.dmsh.mine.activity.MineLoginActivity;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static android.view.View.GONE;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_TOTAL_COUNT;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/8/1
 * class description:活动消息
 */
public class MessageHotActivity extends BaseActivity {
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
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tv_header_shared)
    TextView tv_header_shared;
    private int page = 1;
    private int scrollY;
    private float screenHeight;
    private int uid;
    private MessageOfficialAdapter messageOfficialAdapter;
    //官方通知数据
    private List<MessageOfficialBean> messageArticleList = new ArrayList();
    @Override
    protected int getContentView() {
        return R.layout.activity_communal_header_recycler_refresh;
    }
    @Override
    protected void initViews() {
        getLoginStatus();
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
                    loadData();
                } else {
                    messageOfficialAdapter.loadMoreEnd();
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

    private void getLoginStatus() {
        SavePersonalInfoBean personalInfo = ConstantMethod.getPersonalInfo(this);
        if (personalInfo.isLogin()) {
            uid = personalInfo.getUid();
        } else {
            //未登录跳转登录页
            Intent intent = new Intent(this, MineLoginActivity.class);
            startActivityForResult(intent, ConstantVariable.IS_LOGIN_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            if (requestCode == IS_LOGIN_CODE) {
                finish();
            } else {
                return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IS_LOGIN_CODE) {
                getLoginStatus();
                loadData();
            }
        }
    }

    @Override
    protected void loadData() {
        if (NetWorkUtils.checkNet(MessageHotActivity.this)) {
            String url = Url.BASE_URL + Url.H_MES_OFFICIAL;
            Map<String, Object> params = new HashMap<>();
            params.put("currentPage", page);
            if (uid > 0) {
                params.put("uid", uid);
            }
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    smart_communal_refresh.finishRefresh();
                    communal_load.setVisibility(GONE);
                    communal_error.setVisibility(GONE);
                    messageOfficialAdapter.loadMoreComplete();
                    if (page == 1) {
                        messageArticleList.clear();
                    }
                    Gson gson = new Gson();
                    MessageOfficialEntity messageOfficialEntity = gson.fromJson(result, MessageOfficialEntity.class);
                    if (messageOfficialEntity != null) {
                        if (messageOfficialEntity.getCode().equals("01")) {
                            messageArticleList.addAll(messageOfficialEntity.getMessageOfficialList());
                        } else if (!messageOfficialEntity.getCode().equals("02")) {
                            showToast(MessageHotActivity.this, messageOfficialEntity.getMsg());
                        }
                    }
                    if (page == 1) {
                        messageOfficialAdapter.setNewData(messageArticleList);
                    } else {
                        messageOfficialAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    smart_communal_refresh.finishRefresh();
                    if (messageArticleList.size() < 1) {
                        communal_load.setVisibility(GONE);
                        communal_error.setVisibility(View.VISIBLE);
                    } else {
                        showToast(MessageHotActivity.this, R.string.invalidData);
                    }
                    super.onError(ex, isOnCallback);
                }
            });
        } else {
            smart_communal_refresh.finishRefresh();
            communal_load.setVisibility(GONE);
            messageOfficialAdapter.loadMoreComplete();
            showToast(MessageHotActivity.this, R.string.unConnectedNetwork);
        }
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }

    @OnClick({R.id.rel_communal_error, R.id.communal_empty})
    void refreshData(View view) {
        communal_load.setVisibility(View.VISIBLE);
        communal_empty.setVisibility(GONE);
        communal_error.setVisibility(GONE);
        page = 1;
        loadData();
    }

}
