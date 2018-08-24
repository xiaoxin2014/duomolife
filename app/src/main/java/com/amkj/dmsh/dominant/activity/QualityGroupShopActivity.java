package com.amkj.dmsh.dominant.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.BaseApplication;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.constant.CommunalAdHolderView;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.dominant.adapter.QualityGroupShopAdapter;
import com.amkj.dmsh.dominant.bean.QualityGroupEntity;
import com.amkj.dmsh.dominant.bean.QualityGroupEntity.QualityGroupBean;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity.CommunalADActivityBean;
import com.amkj.dmsh.mine.activity.MineLoginActivity;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
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
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.getShowNumber;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.START_AUTO_PAGE_TURN;
import static com.amkj.dmsh.constant.ConstantVariable.STOP_AUTO_PAGE_TURN;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TWENTY;

;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/6/6
 * class description:多么拼团
 */
public class QualityGroupShopActivity extends BaseActivity {
    @BindView(R.id.smart_communal_refresh)
    RefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tv_header_shared)
    TextView tv_header_shared;
    @BindView(R.id.communal_load)
    View communal_load;
    @BindView(R.id.communal_error)
    View communal_error;
    @BindView(R.id.communal_empty)
    View communal_empty;
    private int page = 1;
    private int scrollY;
    private float screenHeight;
    private List<QualityGroupBean> qualityGroupBeanList = new ArrayList();
    private int uid;
    private QualityGroupShopAdapter qualityGroupShopAdapter;
    private GroupShopHeaderView groupShopHeaderView;
    List<CommunalADActivityBean> adBeanList = new ArrayList<>();
    private CBViewHolderCreator cbViewHolderCreator;

    @Override
    protected int getContentView() {
        return R.layout.activity_quality_group_shop;
    }
    @Override
    protected void initViews() {
        isLoginStatus();
        tv_header_shared.setVisibility(View.GONE);
        tv_header_titleAll.setText("Domo拼团");
        communal_recycler.setLayoutManager(new LinearLayoutManager(QualityGroupShopActivity.this));
        qualityGroupShopAdapter = new QualityGroupShopAdapter(QualityGroupShopActivity.this, qualityGroupBeanList);
        View view = LayoutInflater.from(QualityGroupShopActivity.this).inflate(R.layout.layout_al_new_sp_banner, (ViewGroup) communal_recycler.getParent(), false);
        groupShopHeaderView = new GroupShopHeaderView();
        ButterKnife.bind(groupShopHeaderView, view);
        qualityGroupShopAdapter.addHeaderView(view);
        communal_recycler.addItemDecoration(new PinnedHeaderItemDecoration.Builder(-1)
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_dp)
                // 开启绘制分隔线，默认关闭
                .enableDivider(true)
                // 是否关闭标签点击事件，默认开启
                .disableHeaderClick(false)
                // 设置标签和其内部的子控件的监听，若设置点击监听不为null，但是disableHeaderClick(true)的话，还是不会响应点击事件
                .setHeaderClickListener(null)
                .create());
        communal_recycler.setAdapter(qualityGroupShopAdapter);

        smart_communal_refresh.setOnRefreshListener((refreshLayout) -> {
            page = 1;
                loadData();
        });
        qualityGroupShopAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                QualityGroupBean qualityGroupBean = (QualityGroupBean) view.getTag();
                if (qualityGroupBean != null) {
                    Intent intent = new Intent(QualityGroupShopActivity.this, QualityGroupShopDetailActivity.class);
                    intent.putExtra("gpInfoId", String.valueOf(qualityGroupBean.getGpInfoId()));
                    startActivity(intent);
                }
            }
        });
        qualityGroupShopAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (page * TOTAL_COUNT_TWENTY <= qualityGroupBeanList.size()) {
                    page++;
                    getOpenGroupShop();
                } else {
                    qualityGroupShopAdapter.loadMoreEnd();
                    qualityGroupShopAdapter.setEnableLoadMore(false);
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
                    BaseApplication app = (BaseApplication) getApplication();
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
        qualityGroupShopAdapter.openLoadAnimation(null);
        communal_load.setVisibility(View.VISIBLE);
    }

    private void isLoginStatus() {
        SavePersonalInfoBean personalInfo = ConstantMethod.getPersonalInfo(this);
        if (personalInfo.isLogin()) {
            uid = personalInfo.getUid();
        } else {
            uid = 0;
        }
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        } else if (resultCode == RESULT_OK) {
            if (requestCode == IS_LOGIN_CODE) {
                getLoginStatus();
            }
        }
    }

    @Override
    protected void loadData() {
        getQualityADLoop();
        getOpenGroupShop();
    }

    private void getQualityADLoop() {
        String url = Url.BASE_URL + Url.GROUP_SHOP_LOOP_INDEX;
        Map<String,String> params = new HashMap<>();
        params.put("vidoShow","1");
        XUtil.Get(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                CommunalADActivityEntity adActivityEntity = gson.fromJson(result, CommunalADActivityEntity.class);
                if (adActivityEntity != null) {
                    if (adActivityEntity.getCode().equals("01")) {
                        setGpLoopAD(adActivityEntity);
                    } else {
                        groupShopHeaderView.rel_communal_banner.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    private void setGpLoopAD(CommunalADActivityEntity adActivityEntity) {
        adBeanList.clear();
        adBeanList.addAll(adActivityEntity.getCommunalADActivityBeanList());
        if (adBeanList.size() > 0) {
            groupShopHeaderView.rel_communal_banner.setVisibility(View.VISIBLE);
            if(cbViewHolderCreator ==null){
                cbViewHolderCreator = new CBViewHolderCreator() {
                    @Override
                    public Holder createHolder(View itemView) {
                        return new CommunalAdHolderView(itemView,QualityGroupShopActivity.this,true);
                    }

                    @Override
                    public int getLayoutId() {
                        return R.layout.layout_ad_image_video;
                    }
                };
            }
            groupShopHeaderView.ad_communal_banner.setPages(this,cbViewHolderCreator,adBeanList).setCanLoop(true)
                    .setPageIndicator(new int[]{R.drawable.unselected_radius,R.drawable.selected_radius})
                    .startTurning(getShowNumber(adBeanList.get(0).getShowTime()) * 1000);
        } else {
            groupShopHeaderView.rel_communal_banner.setVisibility(View.GONE);
        }
    }

    private void getOpenGroupShop() {
//        拼团首页
        String url = Url.BASE_URL + Url.GROUP_SHOP_JOIN_INDEX;
        if (NetWorkUtils.checkNet(QualityGroupShopActivity.this)) {
            Map<String, Object> params = new HashMap<>();
            params.put("currentPage", page);
            params.put("showCount", ConstantVariable.TOTAL_COUNT_TWENTY);
            //            区分新人团
            params.put("version", 1);
            if(userId>0){
                params.put("uid",userId);
            }
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    communal_load.setVisibility(View.GONE);
                    communal_error.setVisibility(View.GONE);
                    smart_communal_refresh.finishRefresh();
                    qualityGroupShopAdapter.loadMoreComplete();
                    if (page == 1) {
                        qualityGroupBeanList.clear();
                    }
                    Gson gson = new Gson();
                    QualityGroupEntity qualityGroupEntity = gson.fromJson(result, QualityGroupEntity.class);
                    if (qualityGroupEntity != null) {
                        if (qualityGroupEntity.getCode().equals("01")) {
                            for (int i = 0; i < qualityGroupEntity.getQualityGroupBeanList().size(); i++) {
                                QualityGroupBean qualityGroupBean = qualityGroupEntity.getQualityGroupBeanList().get(i);
                                qualityGroupBean.setCurrentTime(qualityGroupEntity.getCurrentTime());
                                qualityGroupBeanList.add(qualityGroupBean);
                            }
                        } else if (qualityGroupEntity.getCode().equals("02")) {
                            if (page == 1) {
                                communal_empty.setVisibility(View.VISIBLE);
                            }
                            showToast(QualityGroupShopActivity.this, R.string.unConnectedNetwork);
                        } else {
                            showToast(QualityGroupShopActivity.this, qualityGroupEntity.getMsg());
                            communal_error.setVisibility(View.VISIBLE);
                        }
                        qualityGroupShopAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    smart_communal_refresh.finishRefresh();
                    qualityGroupShopAdapter.loadMoreComplete();
                    communal_load.setVisibility(View.GONE);
                    communal_error.setVisibility(View.VISIBLE);
                    showToast(QualityGroupShopActivity.this, R.string.connectedFaile);
                    super.onError(ex, isOnCallback);
                }
            });
        } else {
            smart_communal_refresh.finishRefresh();
            qualityGroupShopAdapter.loadMoreComplete();
            communal_load.setVisibility(View.GONE);
            communal_error.setVisibility(View.VISIBLE);
            showToast(QualityGroupShopActivity.this, R.string.unConnectedNetwork);
        }
    }

    //    我的拼团
    @OnClick(R.id.tv_quality_join_gp_sp)
    void getMineJoinGroup(View view) {
        if (uid != 0) {
            Intent intent = new Intent(QualityGroupShopActivity.this, QualityGroupShopMineActivity.class);
            startActivity(intent);
        } else {
            getLoginStatus();
        }
    }

    //    全部拼团
    @OnClick(R.id.tv_quality_all_gp_sp)
    void getAllGroup(View view) {
        Intent intent = new Intent(QualityGroupShopActivity.this, QualityGroupShopAllActivity.class);
        startActivity(intent);
    }

    @OnClick({R.id.rel_communal_error, R.id.communal_empty})
    void refreshData(View view) {
        communal_load.setVisibility(View.VISIBLE);
        communal_error.setVisibility(View.GONE);
        page = 1;
        loadData();
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (message.type.equals("refreshGroupShop")) {
            page = 1;
            loadData();
        }else if(START_AUTO_PAGE_TURN.equals(message.type)){
            if(adBeanList.size()>0&&groupShopHeaderView.ad_communal_banner!=null
                    &&!groupShopHeaderView.ad_communal_banner.isTurning()){
                groupShopHeaderView.ad_communal_banner.setCanScroll(true);
                groupShopHeaderView.ad_communal_banner.startTurning(getShowNumber(adBeanList.get(0).getShowTime()) * 1000);
                groupShopHeaderView.ad_communal_banner.setPointViewVisible(true);
            }
        }else if(STOP_AUTO_PAGE_TURN.equals(message.type)){
            if(groupShopHeaderView.ad_communal_banner!=null
                    &&groupShopHeaderView.ad_communal_banner.isTurning()){
                groupShopHeaderView.ad_communal_banner.setCanScroll(false);
                groupShopHeaderView.ad_communal_banner.stopTurning();
                groupShopHeaderView.ad_communal_banner.setPointViewVisible(false);
            }
        }
    }

    class GroupShopHeaderView {
        @BindView(R.id.rel_communal_banner)
        RelativeLayout rel_communal_banner;
        @BindView(R.id.ad_communal_banner)
        ConvenientBanner ad_communal_banner;
    }

}
