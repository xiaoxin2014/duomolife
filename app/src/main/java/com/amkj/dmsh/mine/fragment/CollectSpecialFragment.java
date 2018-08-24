package com.amkj.dmsh.mine.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseApplication;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.homepage.activity.ArticleOfficialActivity;
import com.amkj.dmsh.homepage.adapter.SpecialTopicAdapter;
import com.amkj.dmsh.homepage.bean.TopicSpecialEntity;
import com.amkj.dmsh.homepage.bean.TopicSpecialEntity.TopicSpecialBean;
import com.amkj.dmsh.mine.activity.MineLoginActivity;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.utils.NetWorkUtils;
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
 * class description:收藏文章
 */
public class CollectSpecialFragment extends BaseFragment {
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
    private int scrollY = 0;
    private int page = 1;
    private int uid;
    private float screenHeight;
    private SpecialTopicAdapter topicListAdapter;
    private List<TopicSpecialBean> topicBeanList = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_smart_refresh_recycler_float_loading;
    }

    @Override
    protected void initViews() {
        getLoginStatus();
        communal_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        smart_communal_refresh.setOnRefreshListener((refreshLayout) -> {
            page = 1;
            loadData();
        });
        topicListAdapter = new SpecialTopicAdapter(getActivity(), topicBeanList);
        communal_recycler.setAdapter(topicListAdapter);
        communal_recycler.addItemDecoration(new PinnedHeaderItemDecoration.Builder(-1)
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_ten_dp)
                // 开启绘制分隔线，默认关闭
                .enableDivider(true)
                // 是否关闭标签点击事件，默认开启
                .disableHeaderClick(false)
                // 设置标签和其内部的子控件的监听，若设置点击监听不为null，但是disableHeaderClick(true)的话，还是不会响应点击事件
                .setHeaderClickListener(null)
                .create());
        topicListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                TopicSpecialBean topicDetailBean = (TopicSpecialBean) view.getTag();
                if (topicDetailBean != null) {
                    Intent intent = new Intent(getActivity(), ArticleOfficialActivity.class);
                    intent.putExtra("ArtId", String.valueOf(topicDetailBean.getId()));
                    startActivity(intent);
                }
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
                    BaseApplication app = (BaseApplication) getActivity().getApplication();
                    screenHeight = app.getScreenHeight();
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
        topicListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (page * DEFAULT_TOTAL_COUNT<=topicBeanList.size()){
                    page++;
                    getInvitationList();
                }else{
                    topicListAdapter.loadMoreEnd();
                }
            }
        }, communal_recycler);
        communal_load.setVisibility(View.VISIBLE);
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
        if (resultCode == RESULT_OK && requestCode == IS_LOGIN_CODE) {
            getLoginStatus();
            loadData();
        }
    }

    @Override
    protected void loadData() {
        getInvitationList();
    }

    private void getInvitationList() {
        if (NetWorkUtils.isConnectedByState(getActivity())) {
            String url = Url.BASE_URL + Url.COLLECT_SPECIAL;
            Map<String, Object> params = new HashMap<>();
            params.put("currentPage", page);
            params.put("count", DEFAULT_TOTAL_COUNT);
            params.put("uid", uid);
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    smart_communal_refresh.finishRefresh();
                    topicListAdapter.loadMoreComplete();
                    communal_load.setVisibility(View.GONE);
                    communal_error.setVisibility(View.GONE);
                    if (page == 1) {
                        topicBeanList.clear();
                    }
                    Gson gson = new Gson();
                    TopicSpecialEntity topicDetailEntity = gson.fromJson(result, TopicSpecialEntity.class);
                    if (topicDetailEntity != null) {
                        if (topicDetailEntity.getCode().equals("01")) {
                            topicBeanList.addAll(topicDetailEntity.getTopicSpecialBeanList());
                        } else if (!topicDetailEntity.getCode().equals("02")) {
                            showToast(getActivity(), topicDetailEntity.getMsg());
                        }
                        if (page == 1) {
                            topicListAdapter.setNewData(topicBeanList);
                        } else {
                            topicListAdapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    if (page == 1 && topicBeanList.size() < 1) {
                        communal_load.setVisibility(View.GONE);
                        communal_error.setVisibility(View.VISIBLE);
                    }
                    smart_communal_refresh.finishRefresh();
                    topicListAdapter.loadMoreComplete();
                }
            });
        } else {
            smart_communal_refresh.finishRefresh();
            topicListAdapter.loadMoreComplete();
            communal_load.setVisibility(View.GONE);
            communal_error.setVisibility(View.VISIBLE);
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
