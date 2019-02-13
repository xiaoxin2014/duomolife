package com.amkj.dmsh.find.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.find.adapter.FindTopicListAdapter;
import com.amkj.dmsh.find.bean.FindHotTopicEntity;
import com.amkj.dmsh.find.bean.FindHotTopicEntity.FindHotTopicBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
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
import static android.view.View.VISIBLE;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TWENTY;
import static com.amkj.dmsh.constant.Url.F_TOPIC_LIST;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/11/22
 * class description:发现-热门话题列表
 */
public class FindHotTopicListActivity extends BaseActivity {
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
    private List<FindHotTopicBean> findTopicBeanList = new ArrayList();
    private FindTopicListAdapter findTopicListAdapter;
    private FindHotTopicEntity findHotTopicEntity;

    @Override
    protected int getContentView() {
        return R.layout.activity_communal_header_recycler_refresh;
    }

    @Override
    protected void initViews() {
        tv_header_shared.setVisibility(GONE);
        tv_header_titleAll.setText("话题");

        smart_communal_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                loadData();
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(FindHotTopicListActivity.this);
        communal_recycler.setLayoutManager(linearLayoutManager);
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_gray_f_two_px).create());
        findTopicListAdapter = new FindTopicListAdapter(FindHotTopicListActivity.this, findTopicBeanList);
        communal_recycler.setAdapter(findTopicListAdapter);
        findTopicListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                FindHotTopicBean hotTopicBean = (FindHotTopicBean) view.getTag();
                if (hotTopicBean != null) {
                    Intent intent = new Intent(FindHotTopicListActivity.this, FindTopicDetailsActivity.class);
                    intent.putExtra("topicId", String.valueOf(hotTopicBean.getId()));
                    startActivity(intent);
                }
            }
        });
        findTopicListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                getData();
            }
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
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", page);
        params.put("showCount", TOTAL_COUNT_TWENTY);
        NetLoadUtils.getNetInstance().loadNetDataPost(FindHotTopicListActivity.this, F_TOPIC_LIST
                , params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();
                        findTopicListAdapter.loadMoreComplete();
                        if (page == 1) {
                            findTopicBeanList.clear();
                        }
                        Gson gson = new Gson();
                        findHotTopicEntity = gson.fromJson(result, FindHotTopicEntity.class);
                        if (findHotTopicEntity != null) {
                            if (findHotTopicEntity.getCode().equals(SUCCESS_CODE)) {
                                findTopicBeanList.addAll(findHotTopicEntity.getHotTopicList());
                            } else if (findHotTopicEntity.getCode().equals(EMPTY_CODE)) {
                                findTopicListAdapter.loadMoreEnd();
                            }else{
                                showToast(FindHotTopicListActivity.this, findHotTopicEntity.getMsg());
                            }
                            findTopicListAdapter.notifyDataSetChanged();
                        }
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, findTopicBeanList, findHotTopicEntity);
                    }

                    @Override
                    public void onNotNetOrException() {
                        smart_communal_refresh.finishRefresh();
                        findTopicListAdapter.loadMoreEnd(true);
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, findTopicBeanList, findHotTopicEntity);
                    }

                    @Override
                    public void netClose() {
                        showToast(FindHotTopicListActivity.this, R.string.unConnectedNetwork);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        showToast(FindHotTopicListActivity.this, R.string.invalidData);
                    }
                });
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }
}
