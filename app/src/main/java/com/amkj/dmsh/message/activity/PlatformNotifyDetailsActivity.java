package com.amkj.dmsh.message.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.constant.CommunalDetailBean;
import com.amkj.dmsh.homepage.adapter.CommunalDetailAdapter;
import com.amkj.dmsh.message.adapter.PlatformDataEntity;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.webformatdata.CommunalWebDetailUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
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
import static com.amkj.dmsh.constant.Url.H_MES_PLATFORM_DETAILS;

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
        contentPlatformAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.tv_communal_share ) {
                    return;
                }
                CommunalWebDetailUtils.getCommunalWebInstance()
                        .setWebDataClick(PlatformNotifyDetailsActivity.this, view, loadHud);
            }
        });
        communal_recycler.setNestedScrollingEnabled(false);
        communal_recycler.setLayoutManager(new LinearLayoutManager(PlatformNotifyDetailsActivity.this));
        communal_recycler.setAdapter(contentPlatformAdapter);
        smart_official_details.setOnRefreshListener(refreshLayout -> loadData());
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
        Map<String, Object> params = new HashMap<>();
        params.put("id", platformId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, H_MES_PLATFORM_DETAILS,
                params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        smart_official_details.finishRefresh();
                        platformNotifyList.clear();

                        platformDataEntity = GsonUtils.fromJson(result, PlatformDataEntity.class);
                        if (platformDataEntity != null) {
                            if (platformDataEntity.getCode().equals(SUCCESS_CODE)
                                    && platformDataEntity.getPlatformDataBean() != null) {
                                List<CommunalDetailBean> contentBeanList = platformDataEntity.getPlatformDataBean().getDescriptionList();
                                if (contentBeanList != null) {
                                    platformNotifyList.clear();
                                    platformNotifyList.addAll(CommunalWebDetailUtils.getCommunalWebInstance().getWebDetailsFormatDataList(contentBeanList));
                                }
                            } else if (!platformDataEntity.getCode().equals(EMPTY_CODE)) {
                                showToast( platformDataEntity.getMsg());
                            }
                            contentPlatformAdapter.notifyDataSetChanged();
                        }
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, platformNotifyList, platformDataEntity);
                    }

                    @Override
                    public void onNotNetOrException() {
                        smart_official_details.finishRefresh();
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, platformNotifyList, platformDataEntity);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        showToast(R.string.invalidData);
                    }
                });
    }

    @Override
    public View getLoadView() {
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
