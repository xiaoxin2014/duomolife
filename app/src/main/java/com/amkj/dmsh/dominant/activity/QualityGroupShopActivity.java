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
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.constant.CommunalAdHolderView;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.dominant.adapter.QualityGroupShopAdapter;
import com.amkj.dmsh.dominant.bean.QualityGroupEntity;
import com.amkj.dmsh.dominant.bean.QualityGroupEntity.QualityGroupBean;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity.CommunalADActivityBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tencent.bugly.beta.tinker.TinkerManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.amkj.dmsh.constant.ConstantMethod.getShowNumber;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TWENTY;
import static com.amkj.dmsh.constant.Url.GROUP_SHOP_JOIN_INDEX;
import static com.amkj.dmsh.constant.Url.GROUP_SHOP_LOOP_INDEX;

;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/6/6
 * class description:多么拼团
 */
public class QualityGroupShopActivity extends BaseActivity {
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
    private List<QualityGroupBean> qualityGroupBeanList = new ArrayList();
    private QualityGroupShopAdapter qualityGroupShopAdapter;
    private GroupShopHeaderView groupShopHeaderView;
    List<CommunalADActivityBean> adBeanList = new ArrayList<>();
    private CBViewHolderCreator cbViewHolderCreator;
    private QualityGroupEntity qualityGroupEntity;
    private ConstantMethod constantMethod;

    @Override
    protected int getContentView() {
        return R.layout.activity_quality_group_shop;
    }

    @Override
    protected void initViews() {
        getConstant();
        tv_header_shared.setVisibility(View.GONE);
        tv_header_titleAll.setText("Domo拼团");
        communal_recycler.setLayoutManager(new LinearLayoutManager(QualityGroupShopActivity.this));
        qualityGroupShopAdapter = new QualityGroupShopAdapter(QualityGroupShopActivity.this, constantMethod, qualityGroupBeanList);
        View view = LayoutInflater.from(QualityGroupShopActivity.this).inflate(R.layout.layout_al_new_sp_banner, (ViewGroup) communal_recycler.getParent(), false);
        groupShopHeaderView = new GroupShopHeaderView();
        ButterKnife.bind(groupShopHeaderView, view);
        qualityGroupShopAdapter.addHeaderView(view);
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_dp).create());
        communal_recycler.setAdapter(qualityGroupShopAdapter);

        smart_communal_refresh.setOnRefreshListener(refreshLayout -> loadData());
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
                page++;
                getOpenGroupShop();
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
        qualityGroupShopAdapter.openLoadAnimation(null);
    }

    private void getConstant() {
        if (constantMethod == null) {
            constantMethod = new ConstantMethod();
        }
    }

    @Override
    protected void loadData() {
        page = 1;
        getData();
    }

    @Override
    public View getLoadView() {
        return smart_communal_refresh;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    protected void getData() {
        getQualityADLoop();
        getOpenGroupShop();
    }

    private void getQualityADLoop() {
        Map<String, Object> params = new HashMap<>();
        params.put("vidoShow", "1");
        NetLoadUtils.getNetInstance().loadNetDataPost(this,GROUP_SHOP_LOOP_INDEX,params,new NetLoadListenerHelper(){
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                CommunalADActivityEntity adActivityEntity = gson.fromJson(result, CommunalADActivityEntity.class);
                if (adActivityEntity != null) {
                    if (adActivityEntity.getCode().equals(SUCCESS_CODE)) {
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
            if (cbViewHolderCreator == null) {
                cbViewHolderCreator = new CBViewHolderCreator() {
                    @Override
                    public Holder createHolder(View itemView) {
                        return new CommunalAdHolderView(itemView, QualityGroupShopActivity.this, true);
                    }

                    @Override
                    public int getLayoutId() {
                        return R.layout.layout_ad_image_video;
                    }
                };
            }
            groupShopHeaderView.ad_communal_banner.setPages(this, cbViewHolderCreator, adBeanList).setCanLoop(true)
                    .setPageIndicator(new int[]{R.drawable.unselected_radius, R.drawable.selected_radius})
                    .startTurning(getShowNumber(adBeanList.get(0).getShowTime()) * 1000);
        } else {
            groupShopHeaderView.rel_communal_banner.setVisibility(View.GONE);
        }
    }

    private void getOpenGroupShop() {
//        拼团首页
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", page);
        params.put("showCount", TOTAL_COUNT_TWENTY);
        //            区分新人团
        params.put("version", 1);
        if (userId > 0) {
            params.put("uid", userId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(QualityGroupShopActivity.this, GROUP_SHOP_JOIN_INDEX,
                params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();
                        qualityGroupShopAdapter.loadMoreComplete();
                        if (page == 1) {
                            qualityGroupBeanList.clear();
                        }
                        Gson gson = new Gson();
                        qualityGroupEntity = gson.fromJson(result, QualityGroupEntity.class);
                        if (qualityGroupEntity != null) {
                            if (qualityGroupEntity.getCode().equals(SUCCESS_CODE)) {
                                for (int i = 0; i < qualityGroupEntity.getQualityGroupBeanList().size(); i++) {
                                    QualityGroupBean qualityGroupBean = qualityGroupEntity.getQualityGroupBeanList().get(i);
                                    qualityGroupBean.setCurrentTime(qualityGroupEntity.getCurrentTime());
                                    qualityGroupBeanList.add(qualityGroupBean);
                                }
                            } else if (qualityGroupEntity.getCode().equals(EMPTY_CODE)) {
                                qualityGroupShopAdapter.loadMoreEnd();
                            } else {
                                showToast(QualityGroupShopActivity.this, qualityGroupEntity.getMsg());
                            }
                            NetLoadUtils.getNetInstance().showLoadSir(loadService, qualityGroupBeanList, qualityGroupEntity);
                            qualityGroupShopAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onNotNetOrException() {
                        smart_communal_refresh.finishRefresh();
                        qualityGroupShopAdapter.loadMoreEnd(true);
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, qualityGroupBeanList, qualityGroupEntity);
                    }

                    @Override
                    public void netClose() {
                        showToast(QualityGroupShopActivity.this, R.string.unConnectedNetwork);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        showToast(QualityGroupShopActivity.this, R.string.connectedFaile);
                    }
                });
    }

    //    我的拼团
    @OnClick(R.id.tv_quality_join_gp_sp)
    void getMineJoinGroup() {
        Intent intent = new Intent(QualityGroupShopActivity.this, QualityGroupShopMineActivity.class);
        startActivity(intent);
    }

    //    全部拼团
    @OnClick(R.id.tv_quality_all_gp_sp)
    void getAllGroup() {
        Intent intent = new Intent(QualityGroupShopActivity.this, QualityGroupShopAllActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_life_back)
    void goBack() {
        finish();
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (message.type.equals("refreshGroupShop")) {
            page = 1;
            loadData();
        }
    }

    class GroupShopHeaderView {
        @BindView(R.id.rel_communal_banner)
        RelativeLayout rel_communal_banner;
        @BindView(R.id.ad_communal_banner)
        ConvenientBanner ad_communal_banner;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (constantMethod != null) {
            constantMethod.stopSchedule();
            constantMethod.releaseHandlers();
        }
    }
}
