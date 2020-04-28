package com.amkj.dmsh.dominant.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.constant.CommunalAdHolderView;
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.dominant.adapter.QualityGroupShopAdapter;
import com.amkj.dmsh.dominant.bean.QualityGroupEntity;
import com.amkj.dmsh.dominant.bean.QualityGroupEntity.QualityGroupBean;
import com.amkj.dmsh.dominant.initviews.GroupBottomView;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity.CommunalADActivityBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.views.arclayout.ArcLayout;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.getShowNumber;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TWENTY;
import static com.amkj.dmsh.constant.Url.GROUP_SHOP_JOIN_NEW_INDEX;
import static com.amkj.dmsh.constant.Url.GROUP_SHOP_LOOP_INDEX;


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
    @BindView(R.id.group_bottomview)
    GroupBottomView mGroupBottomview;
    private int page = 1;
    private List<QualityGroupBean> qualityGroupBeanList = new ArrayList<>();
    private QualityGroupShopAdapter qualityGroupShopAdapter;
    private GroupShopHeaderView groupShopHeaderView;
    List<CommunalADActivityBean> adBeanList = new ArrayList<>();
    private CBViewHolderCreator cbViewHolderCreator;
    private QualityGroupEntity qualityGroupEntity;

    @Override
    protected int getContentView() {
        return R.layout.activity_quality_group_shop;
    }

    @Override
    protected void initViews() {
        tv_header_titleAll.setText("多么拼团");
        mGroupBottomview.setHomeColor();
        communal_recycler.setLayoutManager(new LinearLayoutManager(QualityGroupShopActivity.this));
        qualityGroupShopAdapter = new QualityGroupShopAdapter(QualityGroupShopActivity.this, qualityGroupBeanList);
        View view = LayoutInflater.from(QualityGroupShopActivity.this).inflate(R.layout.layout_group_banner, (ViewGroup) communal_recycler.getParent(), false);
        groupShopHeaderView = new GroupShopHeaderView();
        ButterKnife.bind(groupShopHeaderView, view);
        qualityGroupShopAdapter.addHeaderView(view);
        communal_recycler.setAdapter(qualityGroupShopAdapter);

        smart_communal_refresh.setOnRefreshListener(refreshLayout -> loadData());
        qualityGroupShopAdapter.setOnItemClickListener((adapter, view1, position) -> {
            QualityGroupBean qualityGroupBean = (QualityGroupBean) view1.getTag();
            if (qualityGroupBean != null) {
                Intent intent = new Intent(QualityGroupShopActivity.this, QualityGroupShopDetailActivity.class);
                intent.putExtra("gpInfoId", String.valueOf(qualityGroupBean.getGpInfoId()));
                intent.putExtra("productId", qualityGroupBean.getProductId());
                startActivity(intent);
            }
        });
        qualityGroupShopAdapter.setOnLoadMoreListener(() -> {
            page++;
            getOpenGroupShop();
        }, communal_recycler);
        setFloatingButton(download_btn_communal, communal_recycler);
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
        NetLoadUtils.getNetInstance().loadNetDataPost(this, GROUP_SHOP_LOOP_INDEX, params, new NetLoadListenerHelper() {
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

    //拼团首页
    private void getOpenGroupShop() {
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", page);
        params.put("showCount", TOTAL_COUNT_TWENTY);
        NetLoadUtils.getNetInstance().loadNetDataPost(QualityGroupShopActivity.this, GROUP_SHOP_JOIN_NEW_INDEX,
                params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();
                        qualityGroupShopAdapter.loadMoreComplete();
                        if (page == 1) {
                            qualityGroupBeanList.clear();
                        }
                        qualityGroupEntity = new Gson().fromJson(result, QualityGroupEntity.class);
                        if (qualityGroupEntity != null) {
                            if (qualityGroupEntity.getCode().equals(SUCCESS_CODE)) {
                                for (int i = 0; i < qualityGroupEntity.getQualityGroupBeanList().size(); i++) {
                                    QualityGroupBean qualityGroupBean = qualityGroupEntity.getQualityGroupBeanList().get(i);
                                    qualityGroupBean.setCurrentTime(qualityGroupEntity.getCurrentTime());
                                    qualityGroupBeanList.add(qualityGroupBean);
                                    if (i == 0) qualityGroupBean.setItemType(1);
                                }
                            } else if (qualityGroupEntity.getCode().equals(EMPTY_CODE)) {
                                qualityGroupShopAdapter.loadMoreEnd();
                            } else {
                                showToast( qualityGroupEntity.getMsg());
                            }
                        }

                        qualityGroupShopAdapter.notifyDataSetChanged();
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, qualityGroupBeanList, qualityGroupEntity);
                    }

                    @Override
                    public void onNotNetOrException() {
                        smart_communal_refresh.finishRefresh();
                        qualityGroupShopAdapter.loadMoreEnd(true);
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, qualityGroupBeanList, qualityGroupEntity);
                    }
                });
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (message.type.equals("refreshGroupShop")) {
            page = 1;
            loadData();
        }
    }


    @OnClick({R.id.tv_life_back, R.id.tv_header_shared})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_life_back:
                finish();
                break;
            case R.id.tv_header_shared:
                new UMShareAction(this, "",
                        "每天都有【9块9起】超值拼团购",
                        "邀请好友一起团购，团的超级实用，买对了就是省钱",
                        "https://www.domolife.cn/m/template/share_template/group.html", "pages/group/group", -1);
                break;
        }
    }

    class GroupShopHeaderView {
        @BindView(R.id.rel_communal_banner)
        ArcLayout rel_communal_banner;
        @BindView(R.id.ad_communal_banner)
        ConvenientBanner ad_communal_banner;
    }
}
