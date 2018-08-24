package com.amkj.dmsh.dominant.activity;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.BaseApplication;
import com.amkj.dmsh.bean.DMLThemeEntity;
import com.amkj.dmsh.bean.DMLThemeEntity.DMLThemeBean;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.BaseAddCarProInfoBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.dominant.adapter.QualityHistoryAdapter;
import com.amkj.dmsh.dominant.adapter.QualityOsMailHeaderAdapter;
import com.amkj.dmsh.dominant.adapter.QualityTypeProductAdapter;
import com.amkj.dmsh.dominant.bean.QualityHistoryListEntity;
import com.amkj.dmsh.dominant.bean.QualityHistoryListEntity.QualityHistoryListBean;
import com.amkj.dmsh.mine.activity.MineLoginActivity;
import com.amkj.dmsh.mine.activity.ShopCarActivity;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import q.rorbin.badgeview.Badge;

import static com.amkj.dmsh.R.id.tv_communal_pro_tag;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.insertNewTotalData;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_TOTAL_COUNT;

;

/**
 * Created by atd48 on 2016/6/27.
 */
public class DoMoLifeWelfareActivity extends BaseActivity {
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
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.iv_img_service)
    ImageView iv_img_service;
    @BindView(R.id.iv_img_share)
    ImageView iv_img_share;
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
    @BindView(tv_communal_pro_tag)
    TextView tv_wel_pro_tag;
    private int page = 1;
    private int scrollY;
    private float screenHeight;
    private QualityTypeProductAdapter qualityTypeProductAdapter;
    private int uid;
    private int themePage = 1;
    private int productPage = 1;
    private List<LikedProductBean> typeDetails = new ArrayList();
    private List<DMLThemeBean> themeList = new ArrayList();
    private List<QualityHistoryListBean> welfarePreviousList = new ArrayList();
    //    头部
    private QualityOsMailHeaderAdapter qualityWelfareHeaderAdapter;
    private OverseasHeaderView overseasHeaderView;
    private String type = "welfare";
    private Badge badge;
    private QualityHistoryAdapter qualityPreviousAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_communal_ql_draw_refresh;
    }

    @Override
    protected void initViews() {
        isLoginStatus();
        iv_img_share.setVisibility(View.GONE);
        tv_header_titleAll.setText("多么福利社");
        iv_img_service.setImageResource(R.drawable.shop_car_gray_icon);
        communal_recycler.setLayoutManager(new GridLayoutManager(DoMoLifeWelfareActivity.this, 2));
        View headerView = LayoutInflater.from(DoMoLifeWelfareActivity.this)
                .inflate(R.layout.layout_communal_recycler_wrap, (ViewGroup) communal_recycler.getParent(), false);
        overseasHeaderView = new OverseasHeaderView();
        ButterKnife.bind(overseasHeaderView, headerView);
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

        smart_communal_refresh.setOnRefreshListener((refreshLayout) -> {
                themePage = 1;
                productPage = 1;
                loadData();
        });
        qualityTypeProductAdapter = new QualityTypeProductAdapter(DoMoLifeWelfareActivity.this, typeDetails);
        qualityTypeProductAdapter.addHeaderView(headerView);
        communal_recycler.setVerticalScrollBarEnabled(false);
        communal_recycler.setAdapter(qualityTypeProductAdapter);

        rv_communal_pro.setLayoutManager(new LinearLayoutManager(DoMoLifeWelfareActivity.this));
        rv_communal_pro.addItemDecoration(new PinnedHeaderItemDecoration.Builder(-1)
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_gray_f_two_px)
                // 开启绘制分隔线，默认关闭
                .enableDivider(true)
                // 是否关闭标签点击事件，默认开启
                .disableHeaderClick(false)
                // 设置标签和其内部的子控件的监听，若设置点击监听不为null，但是disableHeaderClick(true)的话，还是不会响应点击事件
                .setHeaderClickListener(null)
                .create());
        qualityPreviousAdapter = new QualityHistoryAdapter(welfarePreviousList);
        qualityPreviousAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (page * ConstantVariable.TOTAL_COUNT_TWENTY <= welfarePreviousList.size()) {
                    page++;
                    getPreviousTopic();
                } else {
                    qualityPreviousAdapter.loadMoreComplete();
                    qualityPreviousAdapter.setEnableLoadMore(false);
                }
            }
        }, rv_communal_pro);
        rv_communal_pro.setAdapter(qualityPreviousAdapter);
        qualityPreviousAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                QualityHistoryListBean qualityHistoryListBean = (QualityHistoryListBean) view.getTag();
                if (qualityHistoryListBean != null) {
                    dr_communal_pro.closeDrawers();
                    Intent intent = new Intent(DoMoLifeWelfareActivity.this, DoMoLifeWelfareDetailsActivity.class);
                    intent.putExtra("welfareId", String.valueOf(qualityHistoryListBean.getId()));
                    startActivity(intent);
                }
            }
        });

        qualityTypeProductAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (productPage * DEFAULT_TOTAL_COUNT <= qualityTypeProductAdapter.getItemCount()) {
                    if (themeList.size() < themePage * DEFAULT_TOTAL_COUNT) {
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
                                constantMethod.addShopCarGetSku(DoMoLifeWelfareActivity.this, baseAddCarProInfoBean, loadHud);
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
                        getLoginStatus();
                    }
                }
            }
        });
        qualityTypeProductAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                LikedProductBean likedProductBean = (LikedProductBean) view.getTag();
                if (likedProductBean != null) {
                    Intent intent = new Intent(DoMoLifeWelfareActivity.this, ShopScrollDetailsActivity.class);
                    intent.putExtra("productId", String.valueOf(likedProductBean.getId()));
                    startActivity(intent);
                }
            }
        });
        overseasHeaderView.communal_recycler_wrap.setLayoutManager(new LinearLayoutManager(DoMoLifeWelfareActivity.this));
        overseasHeaderView.communal_recycler_wrap.addItemDecoration(new PinnedHeaderItemDecoration.Builder(-1)
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_product)
                // 开启绘制分隔线，默认关闭
                .enableDivider(true)
                // 是否关闭标签点击事件，默认开启
                .disableHeaderClick(false)
                // 设置标签和其内部的子控件的监听，若设置点击监听不为null，但是disableHeaderClick(true)的话，还是不会响应点击事件
                .setHeaderClickListener(null)
                .create());
        qualityWelfareHeaderAdapter = new QualityOsMailHeaderAdapter(DoMoLifeWelfareActivity.this, themeList, type);
        overseasHeaderView.communal_recycler_wrap.setAdapter(qualityWelfareHeaderAdapter);
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
        badge = ConstantMethod.getBadge(DoMoLifeWelfareActivity.this, fl_header_service);
        //          关闭手势滑动
        dr_communal_pro.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.END);
        communal_load.setVisibility(View.VISIBLE);
        totalPersonalTrajectory = insertNewTotalData(DoMoLifeWelfareActivity.this);
    }

    //    商品列表
    private void getWelfareProData() {
        String url = Url.BASE_URL + Url.H_DML_RECOMMEND;
        if (NetWorkUtils.checkNet(DoMoLifeWelfareActivity.this)) {
            Map<String, Object> params = new HashMap<>();
            params.put("showCount", DEFAULT_TOTAL_COUNT);
            params.put("currentPage", productPage);
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    qualityTypeProductAdapter.loadMoreComplete();
                    if (productPage == 1) {
                        //重新加载数据
                        typeDetails.clear();
                    }
                    communal_load.setVisibility(View.GONE);
                    communal_empty.setVisibility(View.GONE);
                    communal_error.setVisibility(View.GONE);
                    Gson gson = new Gson();
                    UserLikedProductEntity likedProductEntity = gson.fromJson(result, UserLikedProductEntity.class);
                    if (likedProductEntity != null) {
                        if (likedProductEntity.getCode().equals("01")) {
                            typeDetails.addAll(likedProductEntity.getLikedProductBeanList());
                        } else if (!likedProductEntity.getCode().equals("02")) {
                            showToast(DoMoLifeWelfareActivity.this, likedProductEntity.getMsg());
                        }
                    }
                    if (productPage == 1) {
                        qualityTypeProductAdapter.setNewData(typeDetails);
                    } else {
                        qualityTypeProductAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    super.onError(ex, isOnCallback);
                }
            });
        }
    }

    //    福利社主题商品列表
    private void getWelfareThemeData() {
        String url = Url.BASE_URL + Url.H_DML_THEME;
        if (NetWorkUtils.checkNet(DoMoLifeWelfareActivity.this)) {
            Map<String, Object> params = new HashMap<>();
            params.put("currentPage", themePage);
            params.put("showCount", DEFAULT_TOTAL_COUNT);
            params.put("goodsCurrentPage", 1);
            params.put("goodsShowCount", 8);
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    smart_communal_refresh.finishRefresh();
                    qualityTypeProductAdapter.loadMoreComplete();
                    communal_load.setVisibility(View.GONE);
                    communal_error.setVisibility(View.GONE);
                    communal_empty.setVisibility(View.GONE);
                    if (themePage == 1) {
                        themeList.clear();
                    }
                    Gson gson = new Gson();
                    DMLThemeEntity dmlTheme = gson.fromJson(result, DMLThemeEntity.class);
                    if (dmlTheme != null) {
                        if (dmlTheme.getCode().equals("01")) {
                            themeList.addAll(dmlTheme.getThemeList());
                            getWelfareProData();
                        } else if (dmlTheme.getCode().equals("02")) {
                            if (themePage == 1) {
                                getWelfareProData();
                                overseasHeaderView.communal_recycler_wrap.setVisibility(View.GONE);
                            }
                        } else {
                            if (themePage == 1) {
                                communal_error.setVisibility(View.VISIBLE);
                            }
                            showToast(DoMoLifeWelfareActivity.this, dmlTheme.getMsg());
                        }
                    }
                    if (themePage == 1) {
                        qualityWelfareHeaderAdapter.setNewData(themeList);
                    } else {
                        qualityWelfareHeaderAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    smart_communal_refresh.finishRefresh();
                    qualityTypeProductAdapter.loadMoreComplete();
                    if (themePage == 1) {
                        communal_load.setVisibility(View.GONE);
                        communal_error.setVisibility(View.VISIBLE);
                    }
                    showToast(DoMoLifeWelfareActivity.this, R.string.invalidData);
                    super.onError(ex, isOnCallback);
                }
            });
        } else {
            smart_communal_refresh.finishRefresh();
            qualityTypeProductAdapter.loadMoreComplete();
            if (themePage == 1) {
                communal_load.setVisibility(View.GONE);
                communal_empty.setVisibility(View.GONE);
                communal_error.setVisibility(View.VISIBLE);
            } else {
                showToast(DoMoLifeWelfareActivity.this, R.string.unConnectedNetwork);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCarCount();
    }

    private void getCarCount() {
        if (uid < 1) {
            isLoginStatus();
        }
        if (uid > 0) {
            //购物车数量展示
            String url = Url.BASE_URL + Url.Q_QUERY_CAR_COUNT;
            Map<String, Object> params = new HashMap<>();
            params.put("userId", uid);
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                    if (requestStatus != null) {
                        if (requestStatus.getCode().equals("01")) {
                            int cartNumber = requestStatus.getResult().getCartNumber();
                            badge.setBadgeNumber(cartNumber);
                        } else if (!requestStatus.getCode().equals("02")) {
                            showToast(DoMoLifeWelfareActivity.this, requestStatus.getMsg());
                        }
                    }
                }
            });
        }
    }

    private void getPreviousTopic() {
        String url = Url.BASE_URL + Url.H_DML_PREVIOUS_THEME;
        if (NetWorkUtils.checkNet(DoMoLifeWelfareActivity.this)) {
            Map<String, Object> params = new HashMap<>();
            params.put("showCount", ConstantVariable.TOTAL_COUNT_TWENTY);
            params.put("currentPage", page);
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    qualityPreviousAdapter.loadMoreComplete();
                    Gson gson = new Gson();
                    if (page == 1) {
                        welfarePreviousList.clear();
                    }
                    QualityHistoryListEntity qualityHistoryListEntity = gson.fromJson(result, QualityHistoryListEntity.class);
                    if (qualityHistoryListEntity != null) {
                        if (qualityHistoryListEntity.getCode().equals("01")) {
                            welfarePreviousList.addAll(qualityHistoryListEntity.getQualityHistoryListBeanList());
                            setHistoryListData();
                        } else if (qualityHistoryListEntity.getCode().equals("02")) {
                            showToast(DoMoLifeWelfareActivity.this, R.string.unConnectedNetwork);
                        } else {
                            showToast(DoMoLifeWelfareActivity.this, qualityHistoryListEntity.getMsg());
                            communal_error.setVisibility(View.VISIBLE);
                        }
                        if (page == 1) {
                            qualityPreviousAdapter.setNewData(welfarePreviousList);
                        } else {
                            qualityPreviousAdapter.notifyDataSetChanged();
                        }
                    }
                }
            });
        }
    }

    private void setHistoryListData() {
        tv_wel_pro_tag.setVisibility(View.VISIBLE);
        ll_communal_pro_list.setVisibility(View.VISIBLE);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        int radius = (int) AutoUtils.getPercentWidth1px() * 50;
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
        if (resultCode != RESULT_OK) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ConstantVariable.IS_LOGIN_CODE) {
                getLoginStatus();
                getWelfareProData();
                getCarCount();
            }
        }
    }

    @Override
    protected void loadData() {
        getWelfareThemeData();
        getPreviousTopic();
    }

    @OnClick(R.id.iv_img_service)
    void skipShopCar(View view) {
        Intent intent = new Intent(DoMoLifeWelfareActivity.this, ShopCarActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }

    @OnClick({R.id.rel_communal_error, R.id.communal_empty})
     void refreshData(View view) {
        communal_load.setVisibility(View.VISIBLE);
        communal_error.setVisibility(View.GONE);
        productPage = 1;
        themePage = 1;
        loadData();
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

    @Override
    protected void onPause() {
        super.onPause();
    }
}
