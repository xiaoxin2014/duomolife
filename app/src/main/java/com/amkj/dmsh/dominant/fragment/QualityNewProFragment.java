package com.amkj.dmsh.dominant.fragment;

import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.constant.CommunalAdHolderView;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.dominant.adapter.GoodProductAdapter;
import com.amkj.dmsh.dominant.bean.QNewProTimeShaftEntity;
import com.amkj.dmsh.dominant.bean.QNewProTimeShaftEntity.QNewProTimeShaftBean;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity.CommunalADActivityBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.user.bean.UserLikedProductEntity;
import com.amkj.dmsh.user.bean.LikedProductBean;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.amkj.dmsh.views.CustomPopWindow;
import com.amkj.dmsh.views.flycoTablayout.CommonTabLayout;
import com.amkj.dmsh.views.convenientbanner.ConvenientBanner;
import com.amkj.dmsh.views.convenientbanner.holder.CBViewHolderCreator;
import com.amkj.dmsh.views.convenientbanner.holder.Holder;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tencent.bugly.beta.tinker.TinkerManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;
import q.rorbin.badgeview.Badge;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getShowNumber;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TWENTY;
import static com.amkj.dmsh.constant.Url.Q_NEW_PRO_AD;
import static com.amkj.dmsh.constant.Url.Q_NEW_PRO_LIST;
import static com.amkj.dmsh.constant.Url.Q_NEW_PRO_TIME_SHAFT;

;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/7/10
 * class description:新品发布
 */
public class QualityNewProFragment extends BaseFragment {
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    @BindView(R.id.fl_header_service)
    FrameLayout fl_header_service;
    @BindView(R.id.tl_quality_bar)
    Toolbar tl_quality_bar;
    @BindView(R.id.tv_ql_new_pro_time_tag)
    TextView tv_ql_new_pro_time_tag;
    private int page = 1;
    private int scrollY;
    private float screenHeight;
    private GoodProductAdapter qualityTypeProductAdapter;
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

    @Override
    protected int getContentView() {
        return R.layout.activity_ql_new_pro;
    }

    @Override
    protected void initViews() {
        tl_quality_bar.setVisibility(GONE);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadii(new float[]{0, 0, AutoSizeUtils.mm2px(mAppContext, 25), AutoSizeUtils.mm2px(mAppContext, 25)
                , AutoSizeUtils.mm2px(mAppContext, 25), AutoSizeUtils.mm2px(mAppContext, 25), 0, 0});
        drawable.setColor(0x7f000000);
        tv_ql_new_pro_time_tag.setBackground(drawable);
        communal_recycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        smart_communal_refresh.setOnRefreshListener(refreshLayout -> loadData());
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
        qualityTypeProductAdapter = new GoodProductAdapter(getActivity(), newProList);
        headerView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_qt_pro_ban_com, null, false);
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
//        时间轴
        productPopWindow = getLayoutInflater().inflate(R.layout.layout_communal_recycler_wrap_wrap, null, false);
        popupWindowView = new PopupWindowView();
        ButterKnife.bind(popupWindowView, productPopWindow);
        popupWindowView.initView();
        badge = ConstantMethod.getBadge(getActivity(), fl_header_service);

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
    }

    private void getNewProAd() {
        Map<String, Object> params = new HashMap<>();
        params.put("vidoShow", "1");
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Q_NEW_PRO_AD, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                smart_communal_refresh.finishRefresh();

                adBeanList.clear();
                CommunalADActivityEntity qualityAdLoop = GsonUtils.fromJson(result, CommunalADActivityEntity.class);
                if (qualityAdLoop != null) {
                    if (qualityAdLoop.getCode().equals(SUCCESS_CODE)) {
                        adBeanList.addAll(qualityAdLoop.getCommunalADActivityBeanList());
                        if (cbViewHolderCreator == null) {
                            cbViewHolderCreator = new CBViewHolderCreator() {
                                @Override
                                public Holder createHolder(View itemView) {
                                    return new CommunalAdHolderView(itemView, getActivity(), true);
                                }

                                @Override
                                public int getLayoutId() {
                                    return R.layout.layout_ad_image_video;
                                }
                            };
                        }
                        qNewProView.ad_communal_banner.setPages(getActivity(), cbViewHolderCreator, adBeanList)
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
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Q_NEW_PRO_TIME_SHAFT, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {

                QNewProTimeShaftEntity proTimeShaftEntity = GsonUtils.fromJson(result, QNewProTimeShaftEntity.class);
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

    private void getQualityNewPro() {
        Map<String, Object> params = new HashMap<>();
        params.put("newReleaseDay", timeShaftDay);
        params.put("currentPage", page);
        params.put("showCount", TOTAL_COUNT_TWENTY);
        if (userId > 0) {
            params.put("uid", userId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Q_NEW_PRO_LIST,
                params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();
                        qualityTypeProductAdapter.loadMoreComplete();
                        if (page == 1) {
                            newProList.clear();
                        }

                        likedProductEntity = GsonUtils.fromJson(result, UserLikedProductEntity.class);
                        if (likedProductEntity != null) {
                            if (likedProductEntity.getCode().equals(SUCCESS_CODE)) {
                                newProList.addAll(likedProductEntity.getGoodsList());
                            } else if (likedProductEntity.getCode().equals(EMPTY_CODE)) {
                                qualityTypeProductAdapter.loadMoreEnd();
                            } else {
                                showToast( likedProductEntity.getMsg());
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
                    public void onError(Throwable throwable) {
                        showToast( R.string.invalidData);
                    }
                });
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
        mCustomPopWindow = new CustomPopWindow.PopupWindowBuilder(getActivity())
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


    class PopupWindowView {
        @BindView(R.id.communal_recycler_wrap_wrap)
        RecyclerView communal_recycler_wrap_wrap;

        public void initView() {
            communal_recycler_wrap_wrap.setLayoutManager(new LinearLayoutManager(getActivity()));
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
}
