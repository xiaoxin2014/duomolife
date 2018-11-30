package com.amkj.dmsh.message.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.constant.CommunalDetailBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.homepage.adapter.CommunalDetailAdapter;
import com.amkj.dmsh.message.adapter.PlatformDataEntity;
import com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean;
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

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;

;
;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/1/11
 * class description:官方通知详情
 */
public class PlatformNotifyDetailsActivity extends BaseActivity {
    @BindView(R.id.tv_header_title)
    TextView tv_header_title;
    @BindView(R.id.tv_header_shared)
    TextView tv_header_shared;
    //主：评论列表
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_official_details;
    //    文章详情
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    private int scrollY;
    private float screenHeight;
    private String platformId;
    private CommunalDetailAdapter contentPlatformAdapter;
    private List<CommunalDetailObjectBean> platformNotifyList = new ArrayList<>();
    private PlatformDataEntity platformDataEntity;

    @Override
    protected int getContentView() {
        return R.layout.activity_message_official_notify;
    }

    @Override
    protected void initViews() {
        tv_header_title.setText("通知");
        tv_header_shared.setVisibility(View.INVISIBLE);
        Intent intent = getIntent();
        platformId = intent.getStringExtra("platformId");
        if (TextUtils.isEmpty(platformId)) {
            Toast.makeText(this, R.string.invalidData, Toast.LENGTH_SHORT).show();
            finish();
        }
        contentPlatformAdapter = new CommunalDetailAdapter(this, platformNotifyList);
        communal_recycler.setNestedScrollingEnabled(false);
        communal_recycler.setLayoutManager(new LinearLayoutManager(PlatformNotifyDetailsActivity.this));
        communal_recycler.setAdapter(contentPlatformAdapter);
        smart_official_details.setOnRefreshListener(new OnRefreshListener() {
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
    }

    @Override
    protected void loadData() {
        String url = Url.BASE_URL + Url.H_MES_PLATFORM_DETAILS;
        Map<String, Object> params = new HashMap<>();
        params.put("id", platformId);
        NetLoadUtils.getQyInstance().loadNetDataPost(mAppContext, url, params, new NetLoadUtils.NetLoadListener() {
            @Override
            public void onSuccess(String result) {
                smart_official_details.finishRefresh();
                platformNotifyList.clear();
                Gson gson = new Gson();
                platformDataEntity = gson.fromJson(result, PlatformDataEntity.class);
                if (platformDataEntity != null) {
                    if (platformDataEntity.getCode().equals(SUCCESS_CODE)
                            && platformDataEntity.getPlatformDataBean() != null) {
                        List<CommunalDetailBean> contentBeanList = platformDataEntity.getPlatformDataBean().getDescriptionList();
                        if (contentBeanList != null) {
                            platformNotifyList.clear();
                            platformNotifyList.addAll(ConstantMethod.getDetailsDataList(contentBeanList));
                        }
                    } else if (!platformDataEntity.getCode().equals(EMPTY_CODE)) {
                        showToast(PlatformNotifyDetailsActivity.this, platformDataEntity.getMsg());
                    }
                    contentPlatformAdapter.notifyDataSetChanged();
                }
                NetLoadUtils.getQyInstance().showLoadSir(loadService, platformNotifyList, platformDataEntity);
            }

            @Override
            public void netClose() {
                smart_official_details.finishRefresh();
                showToast(PlatformNotifyDetailsActivity.this, R.string.unConnectedNetwork);
                NetLoadUtils.getQyInstance().showLoadSir(loadService, platformNotifyList, platformDataEntity);
            }

            @Override
            public void onError(Throwable throwable) {
                smart_official_details.finishRefresh();
                showToast(PlatformNotifyDetailsActivity.this, R.string.invalidData);
                NetLoadUtils.getQyInstance().showLoadSir(loadService, platformNotifyList, platformDataEntity);
            }
        });
    }

    @Override
    protected View getLoadView() {
        return smart_official_details;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }
}
