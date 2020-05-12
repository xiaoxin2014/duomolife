package com.amkj.dmsh.dominant.fragment;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.DMLThemeEntity;
import com.amkj.dmsh.bean.DMLThemeEntity.DMLThemeBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.dominant.activity.DoMoLifeWelfareDetailsActivity;
import com.amkj.dmsh.dominant.adapter.GoodProductAdapter;
import com.amkj.dmsh.dominant.adapter.QualityHistoryAdapter;
import com.amkj.dmsh.dominant.adapter.QualityOsMailHeaderAdapter;
import com.amkj.dmsh.dominant.bean.QualityHistoryListEntity;
import com.amkj.dmsh.dominant.bean.QualityHistoryListEntity.QualityHistoryListBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.user.bean.UserLikedProductEntity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kingja.loadsir.callback.SuccessCallback;
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
import me.jessyan.autosize.utils.AutoSizeUtils;
import q.rorbin.badgeview.Badge;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_FORTY;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TEN;
import static com.amkj.dmsh.constant.Url.H_DML_PREVIOUS_THEME;
import static com.amkj.dmsh.constant.Url.H_DML_RECOMMEND;
import static com.amkj.dmsh.constant.Url.H_DML_THEME;

/**
 * Created by xiaoxin on 2019/4/12 0012
 * Version:v4.0.0
 * ClassDescription :福利社专题列表抽取Fragment
 */
public class DoMoLifeWelfareFragment extends BaseFragment {
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    @BindView(R.id.fl_header_service)
    FrameLayout fl_header_service;
    //    滑动布局
    @BindView(R.id.dr_communal_pro)
    DrawerLayout dr_communal_pro;
    @BindView(R.id.ll_communal_pro_list)
    LinearLayout ll_communal_pro_list;
    @BindView(R.id.tv_communal_pro_title)
    TextView tv_communal_pro_title;
    @BindView(R.id.rv_communal_pro)
    RecyclerView rv_communal_pro;
    //  打开标签
    @BindView(R.id.tv_communal_pro_tag)
    TextView tv_wel_pro_tag;
    @BindView(R.id.tl_quality_bar)
    Toolbar tl_quality_bar;
    private int scrollY;
    private float screenHeight;
    private GoodProductAdapter qualityTypeProductAdapter;
    private int page = 1;
    private int themePage = 1;
    private int productPage = 1;
    private List<LikedProductBean> typeDetails = new ArrayList<>();
    private List<DMLThemeBean> themeList = new ArrayList<>();
    private List<QualityHistoryListBean> welfarePreviousList = new ArrayList<>();
    //    头部
    private QualityOsMailHeaderAdapter qualityWelfareHeaderAdapter;
    private OverseasHeaderView overseasHeaderView;
    private Badge badge;
    private QualityHistoryAdapter qualityPreviousAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_communal_ql_draw_refresh;
    }

    @Override
    protected void initViews() {
        tl_quality_bar.setVisibility(GONE);
        communal_recycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        View headerView = LayoutInflater.from(getActivity())
                .inflate(R.layout.layout_communal_recycler_wrap, (ViewGroup) communal_recycler.getParent(), false);
        overseasHeaderView = new OverseasHeaderView();
        ButterKnife.bind(overseasHeaderView, headerView);
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_dp)
                .create());

        smart_communal_refresh.setOnRefreshListener(refreshLayout -> loadData());
        qualityTypeProductAdapter = new GoodProductAdapter(getActivity(), typeDetails);
        qualityTypeProductAdapter.addHeaderView(headerView);
        communal_recycler.setVerticalScrollBarEnabled(false);
        communal_recycler.setAdapter(qualityTypeProductAdapter);

        rv_communal_pro.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_communal_pro.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_gray_f_one_px)
                .create());
        qualityPreviousAdapter = new QualityHistoryAdapter(welfarePreviousList);
        qualityPreviousAdapter.setEnableLoadMore(false);
        rv_communal_pro.setAdapter(qualityPreviousAdapter);
        qualityPreviousAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                QualityHistoryListBean qualityHistoryListBean = (QualityHistoryListBean) view.getTag();
                if (qualityHistoryListBean != null) {
                    dr_communal_pro.closeDrawers();
                    Intent intent = new Intent(getActivity(), DoMoLifeWelfareDetailsActivity.class);
                    intent.putExtra("welfareId", String.valueOf(qualityHistoryListBean.getId()));
                    startActivity(intent);
                }
            }
        });

        qualityTypeProductAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (productPage * TOTAL_COUNT_TEN <= qualityTypeProductAdapter.getItemCount()) {
                    if (themeList.size() < themePage * TOTAL_COUNT_TEN) {
                        productPage++;
                        getWelfareProData();
                    } else {
                        themePage++;
                        getWelfareThemeData();
                    }
                } else {
                    qualityTypeProductAdapter.loadMoreEnd();
                }
            }
        }, communal_recycler);
        overseasHeaderView.communal_recycler_wrap.setLayoutManager(new LinearLayoutManager(getActivity()));
        overseasHeaderView.communal_recycler_wrap.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_gray_f).create());
        qualityWelfareHeaderAdapter = new QualityOsMailHeaderAdapter(getActivity(), themeList, "welfare");
        overseasHeaderView.communal_recycler_wrap.setAdapter(qualityWelfareHeaderAdapter);
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
                if (download_btn_communal.isVisible()) {
                    download_btn_communal.hide();
                }
                scrollY = 0;
                communal_recycler.smoothScrollToPosition(0);
            }
        });
        badge = ConstantMethod.getBadge(getActivity(), fl_header_service);
        //          关闭手势滑动
        dr_communal_pro.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.END);

    }

    //    商品列表
    private void getWelfareProData() {
        Map<String, Object> params = new HashMap<>();
        params.put("showCount", TOTAL_COUNT_TEN);
        params.put("currentPage", productPage);
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), H_DML_RECOMMEND, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                qualityTypeProductAdapter.loadMoreComplete();
                if (productPage == 1) {
                    //重新加载数据
                    typeDetails.clear();
                }

                UserLikedProductEntity likedProductEntity = GsonUtils.fromJson(result, UserLikedProductEntity.class);
                if (likedProductEntity != null) {
                    if (likedProductEntity.getCode().equals(SUCCESS_CODE)) {
                        typeDetails.addAll(likedProductEntity.getGoodsList());
                    } else if (!likedProductEntity.getCode().equals(EMPTY_CODE)) {
                        showToast( likedProductEntity.getMsg());
                    }
                }
                qualityTypeProductAdapter.notifyDataSetChanged();
            }
        });
    }

    //    福利社主题商品列表
    private void getWelfareThemeData() {
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", themePage);
        params.put("showCount", TOTAL_COUNT_FORTY);
        params.put("goodsCurrentPage", 1);
        params.put("goodsShowCount", 8);
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), H_DML_THEME
                , params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();
                        qualityTypeProductAdapter.loadMoreComplete();
                        if (themePage == 1) {
                            themeList.clear();
                        }

                        DMLThemeEntity dmlTheme = GsonUtils.fromJson(result, DMLThemeEntity.class);
                        if (dmlTheme != null) {
                            if (dmlTheme.getCode().equals(SUCCESS_CODE)) {
                                themeList.addAll(dmlTheme.getThemeList());
                                getWelfareProData();
                            } else if (dmlTheme.getCode().equals(EMPTY_CODE)) {
                                if (themePage == 1) {
                                    getWelfareProData();
                                    overseasHeaderView.communal_recycler_wrap.setVisibility(GONE);
                                }
                            } else {
                                showToast( dmlTheme.getMsg());
                            }
                            qualityWelfareHeaderAdapter.notifyDataSetChanged();
                        }
                        loadService.showCallback(SuccessCallback.class);
                    }

                    @Override
                    public void onNotNetOrException() {
                        smart_communal_refresh.finishRefresh();
                        qualityTypeProductAdapter.loadMoreEnd(true);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        showToast(R.string.invalidData);
                    }
                });
    }


    /**
     * 往期福利社
     */
    private void getPreviousTopic() {
        Map<String, Object> params = new HashMap<>();
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), H_DML_PREVIOUS_THEME, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                qualityPreviousAdapter.loadMoreComplete();

                if (page == 1) {
                    welfarePreviousList.clear();
                }
                QualityHistoryListEntity qualityHistoryListEntity = GsonUtils.fromJson(result, QualityHistoryListEntity.class);
                if (qualityHistoryListEntity != null) {
                    if (qualityHistoryListEntity.getCode().equals(SUCCESS_CODE)) {
                        welfarePreviousList.addAll(qualityHistoryListEntity.getQualityHistoryListBeanList());
                        setHistoryListData();
                    } else if (qualityHistoryListEntity.getCode().equals(EMPTY_CODE)) {
                        qualityPreviousAdapter.loadMoreEnd();
                    }
                    qualityPreviousAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNotNetOrException() {
                qualityPreviousAdapter.loadMoreEnd(true);
            }
        });
    }

    private void setHistoryListData() {
        tv_wel_pro_tag.setVisibility(VISIBLE);
        ll_communal_pro_list.setVisibility(VISIBLE);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        int radius = AutoSizeUtils.mm2px(mAppContext, 50);
        drawable.setCornerRadii(new float[]{radius, radius, 0, 0, 0, 0, radius, radius});
        try {
            drawable.setColor(0xffffffff);
            drawable.setStroke(1, 0xffcccccc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        tv_wel_pro_tag.setBackground(drawable);
        tv_wel_pro_tag.setText("更多专题");
        tv_communal_pro_title.setText("更多专题");
    }


    @Override
    protected void loadData() {
        productPage = 1;
        themePage = 1;
        page = 1;
        getData();
    }


    @Override
    protected boolean isAddLoad() {
        return true;
    }

    private void getData() {
        getWelfareThemeData();
        getPreviousTopic();
    }

    class OverseasHeaderView {
        @BindView(R.id.communal_recycler_wrap)
        RecyclerView communal_recycler_wrap;
    }

    @OnClick(R.id.tv_communal_pro_tag)
    void openSlideProList(View view) {
//            商品列表
        if (dr_communal_pro.isDrawerOpen(ll_communal_pro_list)) {
            dr_communal_pro.closeDrawers();
        } else {
            dr_communal_pro.openDrawer(ll_communal_pro_list);
        }
    }
}
