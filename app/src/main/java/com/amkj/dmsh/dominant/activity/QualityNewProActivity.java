package com.amkj.dmsh.dominant.activity;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.BaseAddCarProInfoBean;
import com.amkj.dmsh.constant.CommunalAdHolderView;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.dominant.adapter.QualityTypeProductAdapter;
import com.amkj.dmsh.dominant.bean.QNewProTimeShaftEntity;
import com.amkj.dmsh.dominant.bean.QNewProTimeShaftEntity.QNewProTimeShaftBean;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity.CommunalADActivityBean;
import com.amkj.dmsh.mine.activity.ShopCarActivity;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.amkj.dmsh.views.CustomPopWindow;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.flyco.tablayout.CommonTabLayout;
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
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;
import q.rorbin.badgeview.Badge;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getShowNumber;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.insertNewTotalData;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.START_AUTO_PAGE_TURN;
import static com.amkj.dmsh.constant.ConstantVariable.STOP_AUTO_PAGE_TURN;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TWENTY;
import static com.amkj.dmsh.constant.Url.Q_NEW_PRO_AD;
import static com.amkj.dmsh.constant.Url.Q_NEW_PRO_LIST;
import static com.amkj.dmsh.constant.Url.Q_NEW_PRO_TIME_SHAFT;
import static com.amkj.dmsh.constant.Url.Q_QUERY_CAR_COUNT;

;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/7/10
 * class description:新品发布
 */
public class QualityNewProActivity extends BaseActivity {
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.iv_img_service)
    ImageView iv_img_service;
    @BindView(R.id.fl_header_service)
    FrameLayout fl_header_service;
    @BindView(R.id.iv_img_share)
    ImageView iv_img_share;
    @BindView(R.id.tl_quality_bar)
    Toolbar tl_quality_bar;
    @BindView(R.id.tv_ql_new_pro_time_tag)
    TextView tv_ql_new_pro_time_tag;
    private int page = 1;
    private int scrollY;
    private float screenHeight;
    private QualityTypeProductAdapter qualityTypeProductAdapter;
    private List<LikedProductBean> newProList = new ArrayList();
    private List<QNewProTimeShaftBean> timeShaftList = new ArrayList();
    private List<CommunalADActivityBean> adBeanList = new ArrayList<>();
    private String timeShaftDay;
    private QNewProView qNewProView;
    private Badge badge;
    private CustomPopWindow mCustomPopWindow;
    private View productPopWindow;
    private PopupWindowView popupWindowView;
    private TimeShaftAdapter timeShaftAdapter;
    private View headerView;
    private CBViewHolderCreator cbViewHolderCreator;
    private UserLikedProductEntity likedProductEntity;
    private int[] location = new int[2];

    @Override
    protected int getContentView() {
        return R.layout.activity_ql_new_pro;
    }

    @Override
    protected void initViews() {
        iv_img_share.setVisibility(View.GONE);
        tv_header_titleAll.setText("新品发布");
        tl_quality_bar.setSelected(true);
        iv_img_service.setImageResource(R.drawable.shop_car_gray_icon);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadii(new float[]{0, 0, AutoSizeUtils.mm2px(mAppContext, 25), AutoSizeUtils.mm2px(mAppContext, 25)
                , AutoSizeUtils.mm2px(mAppContext, 25), AutoSizeUtils.mm2px(mAppContext, 25), 0, 0});
        drawable.setColor(0x7f000000);
        tv_ql_new_pro_time_tag.setBackground(drawable);
        communal_recycler.setLayoutManager(new GridLayoutManager(QualityNewProActivity.this, 2));

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
        qualityTypeProductAdapter = new QualityTypeProductAdapter(QualityNewProActivity.this, newProList);
        headerView = LayoutInflater.from(QualityNewProActivity.this).inflate(R.layout.layout_qt_pro_ban_com, null, false);
        qNewProView = new QNewProView();
        ButterKnife.bind(qNewProView, headerView);
        qNewProView.initViews();
        communal_recycler.setAdapter(qualityTypeProductAdapter);
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_dp)


                .create());
        qualityTypeProductAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                getQualityNewPro();
            }
        }, communal_recycler);
        qualityTypeProductAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                LikedProductBean likedProductBean = (LikedProductBean) view.getTag();
                if (likedProductBean != null) {
                    Intent intent = new Intent(QualityNewProActivity.this, ShopScrollDetailsActivity.class);
                    intent.putExtra("productId", String.valueOf(likedProductBean.getId()));
                    startActivity(intent);
                }
            }
        });
        qualityTypeProductAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                loadHud.show();
                LikedProductBean likedProductBean = (LikedProductBean) view.getTag();
                if (likedProductBean != null) {
                    if (userId > 0) {
                        switch (view.getId()) {
                            case R.id.iv_pro_add_car:
                                BaseAddCarProInfoBean baseAddCarProInfoBean = new BaseAddCarProInfoBean();
                                baseAddCarProInfoBean.setProductId(likedProductBean.getId());
                                baseAddCarProInfoBean.setActivityCode(getStrings(likedProductBean.getActivityCode()));
                                baseAddCarProInfoBean.setProName(getStrings(likedProductBean.getName()));
                                baseAddCarProInfoBean.setProPic(getStrings(likedProductBean.getPicUrl()));
                                ConstantMethod constantMethod = new ConstantMethod();
                                constantMethod.addShopCarGetSku(QualityNewProActivity.this, baseAddCarProInfoBean, loadHud);
                                constantMethod.setAddOnCarListener(new ConstantMethod.OnAddCarListener() {
                                    @Override
                                    public void onAddCarSuccess() {
                                        getCarCount();
                                    }
                                });
                                break;
                        }
                    } else {
                        loadHud.dismiss();
                        getLoginStatus(QualityNewProActivity.this);
                    }
                }
            }
        });
//        时间轴
        productPopWindow = getLayoutInflater().inflate(R.layout.layout_communal_recycler_wrap_wrap, null, false);
        popupWindowView = new PopupWindowView();
        ButterKnife.bind(popupWindowView, productPopWindow);
        popupWindowView.initView();
        badge = ConstantMethod.getBadge(QualityNewProActivity.this, fl_header_service);
        totalPersonalTrajectory = insertNewTotalData(QualityNewProActivity.this);
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
    protected void loadData() {
        page = 1;
        getNewProShaft();
        getNewProAd();
        getCarCount();
    }

    private void getNewProAd() {
        Map<String, Object> params = new HashMap<>();
        params.put("vidoShow", "1");
        NetLoadUtils.getNetInstance().loadNetDataPost(this,Q_NEW_PRO_AD,params,new NetLoadListenerHelper(){
            @Override
            public void onSuccess(String result) {
                smart_communal_refresh.finishRefresh();
                Gson gson = new Gson();
                adBeanList.clear();
                CommunalADActivityEntity qualityAdLoop = gson.fromJson(result, CommunalADActivityEntity.class);
                if (qualityAdLoop != null) {
                    if (qualityAdLoop.getCode().equals(SUCCESS_CODE)) {
                        adBeanList.addAll(qualityAdLoop.getCommunalADActivityBeanList());
                        if (cbViewHolderCreator == null) {
                            cbViewHolderCreator = new CBViewHolderCreator() {
                                @Override
                                public Holder createHolder(View itemView) {
                                    return new CommunalAdHolderView(itemView, QualityNewProActivity.this, true);
                                }

                                @Override
                                public int getLayoutId() {
                                    return R.layout.layout_ad_image_video;
                                }
                            };
                        }
                        qNewProView.ad_communal_banner.setPages(QualityNewProActivity.this, cbViewHolderCreator, adBeanList).setCanLoop(true)
                                .setPointViewVisible(true).setCanScroll(true).setPageIndicator(new int[]{R.drawable.unselected_radius, R.drawable.selected_radius})
                                .startTurning(getShowNumber(adBeanList.get(0).getShowTime()) * 1000);
                    }
                    qualityTypeProductAdapter.removeAllFooterView();
                    if (adBeanList.size() > 0) {
                        qualityTypeProductAdapter.addHeaderView(headerView);
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                smart_communal_refresh.finishRefresh();
            }
        });
    }

    private void getNewProShaft() {
        NetLoadUtils.getNetInstance().loadNetDataPost(this,Q_NEW_PRO_TIME_SHAFT,new NetLoadListenerHelper(){
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                QNewProTimeShaftEntity proTimeShaftEntity = gson.fromJson(result, QNewProTimeShaftEntity.class);
                if (proTimeShaftEntity != null) {
                    if (proTimeShaftEntity.getCode().equals(SUCCESS_CODE)) {
                        timeShaftList.clear();
                        for (int i = 0; i < proTimeShaftEntity.getQNewProTimeShaftList().size(); i++) {
                            QNewProTimeShaftBean qNewProTimeShaftBean = proTimeShaftEntity.getQNewProTimeShaftList().get(i);
                            if (i == 0) {
                                qNewProTimeShaftBean.setSelected(true);
                            }
                            timeShaftList.add(qNewProTimeShaftBean);
                        }
                        QNewProTimeShaftBean qNewProTimeShaftBean = timeShaftList.get(0);
                        if (qNewProTimeShaftBean != null) {
                            tv_ql_new_pro_time_tag.setVisibility(View.VISIBLE);
                            timeShaftDay = qNewProTimeShaftBean.getNewReleaseDay();
                            tv_ql_new_pro_time_tag.setText(timeShaftDay + "天");
                            getQualityNewPro();
                        }
                    }
                    timeShaftAdapter.setNewData(timeShaftList);
                }
            }

            @Override
            public void onNotNetOrException() {
                smart_communal_refresh.finishRefresh();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCarCount();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IS_LOGIN_CODE) {
            page = 1;
            getQualityNewPro();
            getCarCount();
        }
    }

    private void getQualityNewPro() {
        Map<String, Object> params = new HashMap<>();
        params.put("newReleaseDay", timeShaftDay);
        params.put("currentPage", page);
        params.put("showCount", TOTAL_COUNT_TWENTY);
        if (userId > 0) {
            params.put("uid", userId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(QualityNewProActivity.this, Q_NEW_PRO_LIST,
                params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();
                        qualityTypeProductAdapter.loadMoreComplete();
                        if (page == 1) {
                            newProList.clear();
                        }
                        Gson gson = new Gson();
                        likedProductEntity = gson.fromJson(result, UserLikedProductEntity.class);
                        if (likedProductEntity != null) {
                            if (likedProductEntity.getCode().equals(SUCCESS_CODE)) {
                                newProList.addAll(likedProductEntity.getLikedProductBeanList());
                            } else if (likedProductEntity.getCode().equals(EMPTY_CODE)) {
                                qualityTypeProductAdapter.loadMoreEnd();
                            } else {
                                showToast(QualityNewProActivity.this, likedProductEntity.getMsg());
                            }
                            qualityTypeProductAdapter.notifyDataSetChanged();
                        }
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, newProList, likedProductEntity);
                    }

                    @Override
                    public void onNotNetOrException() {
                        smart_communal_refresh.finishRefresh();
                        qualityTypeProductAdapter.loadMoreEnd(true);
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, newProList, likedProductEntity);
                    }

                    @Override
                    public void netClose() {
                        showToast(QualityNewProActivity.this, R.string.unConnectedNetwork);
                    }

                    @Override
                    public void onError(Throwable throwable) {

                        showToast(QualityNewProActivity.this, R.string.invalidData);
                    }
                });
    }

    private void getCarCount() {
        if (userId > 0) {
            //购物车数量展示
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            NetLoadUtils.getNetInstance().loadNetDataPost(this,Q_QUERY_CAR_COUNT,params,new NetLoadListenerHelper(){
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                    if (requestStatus != null) {
                        if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                            int cartNumber = requestStatus.getResult().getCartNumber();
                            badge.setBadgeNumber(cartNumber);
                        } else if (!requestStatus.getCode().equals(EMPTY_CODE)) {
                            showToast(QualityNewProActivity.this, requestStatus.getMsg());
                        }
                    }
                }
            });
        }
    }

    class QNewProView {
        @BindView(R.id.rel_communal_banner)
        public RelativeLayout rel_communal_banner;
        @BindView(R.id.ad_communal_banner)
        public ConvenientBanner ad_communal_banner;
        @BindView(R.id.ctb_qt_pro_type)
        CommonTabLayout ctb_qt_pro_type;

        public void initViews() {
            ctb_qt_pro_type.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.tv_ql_new_pro_time_tag)
    void getSelTimeShaft(View view) {
        tv_ql_new_pro_time_tag.setVisibility(View.GONE);
        mCustomPopWindow = new CustomPopWindow.PopupWindowBuilder(QualityNewProActivity.this)
                .setView(productPopWindow)
                .setFocusable(true)
                .setOutsideTouchable(false)
                .setOnDissmissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        tv_ql_new_pro_time_tag.setVisibility(View.VISIBLE);
                    }
                })
                .create()
                .showAsDropDown(view, 0, -AutoSizeUtils.mm2px(mAppContext, 50));
    }

    @OnClick(R.id.iv_img_service)
    void skipShopCar(View view) {
        Intent intent = new Intent(QualityNewProActivity.this, ShopCarActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }

    class PopupWindowView {
        @BindView(R.id.communal_recycler_wrap_wrap)
        RecyclerView communal_recycler_wrap_wrap;

        public void initView() {
            communal_recycler_wrap_wrap.setLayoutManager(new LinearLayoutManager(QualityNewProActivity.this));
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setCornerRadii(new float[]{0, 0, AutoSizeUtils.mm2px(mAppContext, 15), AutoSizeUtils.mm2px(mAppContext, 15)
                    , AutoSizeUtils.mm2px(mAppContext, 15), AutoSizeUtils.mm2px(mAppContext, 15), 0, 0});
            gradientDrawable.setColor(0x7f000000);
            communal_recycler_wrap_wrap.setBackground(gradientDrawable);
            timeShaftAdapter = new TimeShaftAdapter(timeShaftList);
            communal_recycler_wrap_wrap.setAdapter(timeShaftAdapter);
            timeShaftAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    QNewProTimeShaftBean qNewProTimeShaftBean = (QNewProTimeShaftBean) view.getTag();
                    if (mCustomPopWindow != null) {
                        mCustomPopWindow.dissmiss();
                    }
                    tv_ql_new_pro_time_tag.setVisibility(View.VISIBLE);
                    if (qNewProTimeShaftBean != null && !timeShaftDay.equals(qNewProTimeShaftBean.getNewReleaseDay())) {
                        timeShaftDay = qNewProTimeShaftBean.getNewReleaseDay();
                        tv_ql_new_pro_time_tag.setText(timeShaftDay + "天");
                        qNewProTimeShaftBean.setSelected(true);
                        for (int i = 0; i < timeShaftList.size(); i++) {
                            QNewProTimeShaftBean qNewProTimeShaft = timeShaftList.get(i);
                            if (i == position) {
                                qNewProTimeShaft.setSelected(true);
                            } else {
                                qNewProTimeShaft.setSelected(false);
                            }
                            timeShaftList.set(i, qNewProTimeShaft);
                        }
                        timeShaftAdapter.notifyItemRangeChanged(0, timeShaftList.size());
                        page = 1;
                        getQualityNewPro();
                    }
                }
            });
        }
    }

    private class TimeShaftAdapter extends BaseQuickAdapter<QNewProTimeShaftBean, BaseViewHolder> {
        public TimeShaftAdapter(List<QNewProTimeShaftBean> timeShaftList) {
            super(R.layout.adapter_tv_time_shaft, timeShaftList);
        }

        @Override
        protected void convert(BaseViewHolder helper, QNewProTimeShaftBean qNewProTimeShaftBean) {
            TextView tv_ql_new_pro_time_tag = helper.getView(R.id.tv_ql_new_pro_time_tag);
            tv_ql_new_pro_time_tag.setText((getStrings(qNewProTimeShaftBean.getNewReleaseDay()) + "天"));
            tv_ql_new_pro_time_tag.setSelected(qNewProTimeShaftBean.isSelected());
            helper.itemView.setTag(qNewProTimeShaftBean);
        }
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (START_AUTO_PAGE_TURN.equals(message.type)) {
            if (adBeanList.size() > 0 && qNewProView.ad_communal_banner != null && !qNewProView.ad_communal_banner.isTurning()) {
                qNewProView.ad_communal_banner.setCanScroll(true);
                qNewProView.ad_communal_banner.startTurning(getShowNumber(adBeanList.get(0).getShowTime()) * 1000);
                qNewProView.ad_communal_banner.setPointViewVisible(true);
            }
        } else if (STOP_AUTO_PAGE_TURN.equals(message.type)) {
            if (qNewProView.ad_communal_banner != null && qNewProView.ad_communal_banner.isTurning()) {
                qNewProView.ad_communal_banner.setCanScroll(false);
                qNewProView.ad_communal_banner.stopTurning();
                qNewProView.ad_communal_banner.setPointViewVisible(false);
            }
        }
    }
}
