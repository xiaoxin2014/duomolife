package com.amkj.dmsh.dominant.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.NetLoadUtils;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.DMLThemeDetail;
import com.amkj.dmsh.bean.DMLThemeDetail.ThemeDataBean;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.dominant.bean.DMLTimeDetailEntity;
import com.amkj.dmsh.dominant.bean.DMLTimeDetailEntity.DMLTimeDetailBean;
import com.amkj.dmsh.homepage.adapter.DoMoLifeTimeBrandAdapter;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.amkj.dmsh.views.CustomPopWindow;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.amkj.dmsh.utils.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tencent.bugly.beta.tinker.TinkerManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_TOTAL_COUNT;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/6/8
 * class description:限时特惠品牌团详情
 */
public class TimeBrandDetailsActivity extends BaseActivity {
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    List<DMLTimeDetailBean> brandProductList = new ArrayList();
    private String brandId;
    private DoMoLifeTimeBrandAdapter duoMoLifeTimeBrandAdapter;
    private int page = 1;
    private int scrollY;
    private float screenHeight;
    private DMLThemeDetail dmlThemeDetail;
    private TimeBrandHeaderView timeBrandHeaderView;
    private CustomPopWindow mCustomPopWindow;
    private ConstantMethod constantMethod;
    private ThemeDataBean themeBean;

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_refresh_floating_header;
    }

    @Override
    protected void initViews() {
        tv_header_titleAll.setText("");
        Intent intent = getIntent();
        brandId = intent.getStringExtra("brandId");
        View headerView = LayoutInflater.from(TimeBrandDetailsActivity.this)
                .inflate(R.layout.layout_time_topic_header, (ViewGroup) communal_recycler.getParent(), false);
        timeBrandHeaderView = new TimeBrandHeaderView();
        ButterKnife.bind(timeBrandHeaderView, headerView);
        communal_recycler.setLayoutManager(new GridLayoutManager(TimeBrandDetailsActivity.this, 2));
        duoMoLifeTimeBrandAdapter = new DoMoLifeTimeBrandAdapter(TimeBrandDetailsActivity.this, brandProductList);
        duoMoLifeTimeBrandAdapter.addHeaderView(headerView);
        communal_recycler.setAdapter(duoMoLifeTimeBrandAdapter);
        communal_recycler.addItemDecoration(new PinnedHeaderItemDecoration.Builder(-2)
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_product)
                // 开启绘制分隔线，默认关闭
                .enableDivider(true)
                // 是否关闭标签点击事件，默认开启
                .disableHeaderClick(false)
                // 设置标签和其内部的子控件的监听，若设置点击监听不为null，但是disableHeaderClick(true)的话，还是不会响应点击事件
                .setHeaderClickListener(null)
                .create());
        duoMoLifeTimeBrandAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //跳转自营商品详情
                DMLTimeDetailBean dmlTimeDetailBean = (DMLTimeDetailBean) view.getTag();
                if (dmlTimeDetailBean != null) {
                    Intent intent = new Intent(TimeBrandDetailsActivity.this, ShopTimeScrollDetailsActivity.class);
                    intent.putExtra("productId", String.valueOf(dmlTimeDetailBean.getId()));
                    startActivity(intent);
                }
            }
        });

        smart_communal_refresh.setOnRefreshListener((refreshLayout) -> {
            loadData();
        });
        duoMoLifeTimeBrandAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (page * DEFAULT_TOTAL_COUNT <= duoMoLifeTimeBrandAdapter.getItemCount()) {
                    page++;
                    getRecommendData();
                } else {
                    duoMoLifeTimeBrandAdapter.loadMoreEnd();
                }
            }
        }, communal_recycler);
//        duoMoLifeTimeBrandAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
//            @Override
//            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//                //跳转自营商品详情
//                DMLTimeDetailBean dmlTimeDetailBean = (DMLTimeDetailBean) view.getTag();
//                if (dmlTimeDetailBean != null) {
//                    switch (view.getId()) {
//                        case R.id.iv_pro_time_warm:
////                            设置提醒 取消提醒 是否是第一次设置
//                            if (userId != 0) {
//                                isFirstRemind(dmlTimeDetailBean, (ImageView) view);
//                            } else {
//                                getLoginStatus(TimeBrandDetailsActivity.this);
//                            }
//                            break;
//                    }
//                }
//            }
//        });
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

    private void isFirstRemind(final DMLTimeDetailBean dmlTimeDetailBean, final ImageView view) {
        String url = Url.BASE_URL + Url.TIME_SHOW_PRO_WARM;
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                RequestStatus foreShowBean = gson.fromJson(result, RequestStatus.class);
                if (foreShowBean != null) {
                    if (foreShowBean.getCode().equals("01")) {
                        if (foreShowBean.getResult().isHadRemind()) { //已设置过提醒
                            if (view.isSelected()) {
//                                取消提醒
                                cancelWarm(dmlTimeDetailBean.getId(), view);
                            } else {
//                                设置提醒
                                setWarm(dmlTimeDetailBean.getId(), view);
                            }
                        } else {
                            setDefaultWarm();
                        }
                    } else if (!foreShowBean.getCode().equals("02")) {
                        showToast(TimeBrandDetailsActivity.this, foreShowBean.getMsg());
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                showToast(TimeBrandDetailsActivity.this, R.string.unConnectedNetwork);
            }
        });
    }

    private void setDefaultWarm() {
//        设置提醒
        View indentPopWindow = LayoutInflater.from(TimeBrandDetailsActivity.this).inflate(R.layout.layout_first_time_product_warm, communal_recycler, false);
        PopupWindowView popupWindowView = new PopupWindowView();
        ButterKnife.bind(popupWindowView, indentPopWindow);
        mCustomPopWindow = new CustomPopWindow.PopupWindowBuilder(TimeBrandDetailsActivity.this)
                .setView(indentPopWindow)
                .enableBackgroundDark(true) //弹出popWindow时，背景是否变暗
                .setBgDarkAlpha(0.7f) // 控制亮度
                .create().showAtLocation((View) communal_recycler.getParent(), Gravity.CENTER, 0, 0);
        popupWindowView.rp_time_pro_warm.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                String numberWarm = radioButton.getText().toString().trim();
                Message message = new Message();
                message.arg1 = 1;
                message.obj = numberWarm;
                handler.sendMessageDelayed(message, 618);
            }
        });
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (message.arg1 == 1) {
                String number = (String) message.obj;
                mCustomPopWindow.dissmiss();
                setWarmTime(getNumber(number));
            }
            return false;
        }
    });

    private void setWarmTime(String number) {
        String url = Url.BASE_URL + Url.TIME_WARM_PRO;
        Map<String, Object> params = new HashMap<>();
        params.put("m_uid", userId);
        params.put("longtime", number);
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null && requestStatus.getCode().equals("01")) {
                    showToast(TimeBrandDetailsActivity.this, "已设置产品提醒时间，提前" + requestStatus.getLongtime() + "分钟");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }
        });
    }

    private void cancelWarm(int productId, View view) {
        final ImageView imageView = (ImageView) view;
        String url = Url.BASE_URL + Url.CANCEL_MINE_WARM;
        Map<String, Object> params = new HashMap<>();
        params.put("m_obj", productId);
        params.put("m_uid", userId);
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                loadHud.dismiss();
                Gson gson = new Gson();
                RequestStatus status = gson.fromJson(result, RequestStatus.class);
                if (status != null) {
                    if (status.getCode().equals("01")) {
                        imageView.setSelected(false);
                        showToast(TimeBrandDetailsActivity.this, "已取消提醒");
                    } else {
                        showToast(TimeBrandDetailsActivity.this, status.getMsg());
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                loadHud.dismiss();
                showToast(TimeBrandDetailsActivity.this, R.string.unConnectedNetwork);
            }
        });
    }

    private void setWarm(int productId, View view) {
        final ImageView imageView = (ImageView) view;
        String url = Url.BASE_URL + Url.ADD_MINE_WARM;
        Map<String, Object> params = new HashMap<>();
        params.put("m_obj", productId);
        params.put("m_uid", userId);
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                loadHud.dismiss();
                Gson gson = new Gson();
                RequestStatus status = gson.fromJson(result, RequestStatus.class);
                if (status != null) {
                    if (status.getCode().equals("01")) {
                        imageView.setSelected(true);
                        showToast(TimeBrandDetailsActivity.this, "已设置提醒");
                    } else {
                        showToast(TimeBrandDetailsActivity.this, status.getMsg());
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                loadHud.dismiss();
                showToast(TimeBrandDetailsActivity.this, R.string.unConnectedNetwork);
            }
        });
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }

    //    页面分享
    @OnClick(R.id.tv_header_shared)
    void sendShare(View view) {
        if (dmlThemeDetail != null && dmlThemeDetail.getThemeDataBean() != null) {
            ThemeDataBean themeDataBean = dmlThemeDetail.getThemeDataBean();
            new UMShareAction(TimeBrandDetailsActivity.this
                    , themeDataBean.getPicUrl()
                    , themeDataBean.getTitle()
                    , themeDataBean.getDiscription()
                    , Url.BASE_SHARE_PAGE_TWO + "m/template/limit_time_template/brand.html?id=" + themeDataBean.getId());
        }
    }

    @Override
    protected void loadData() {
        page = 1;
        //头部信息
        getThemeDetailsData();
        //商品列表
        getRecommendData();
    }

    @Override
    protected View getLoadView() {
        return smart_communal_refresh;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    private void getRecommendData() {
        String url = Url.BASE_URL + Url.H_TIME_BRAND_DETAILS_REC;
        if (NetWorkUtils.checkNet(TimeBrandDetailsActivity.this)) {
            Map<String, Object> params = new HashMap<>();
            if (!TextUtils.isEmpty(brandId)) {
                params.put("id", brandId);
            }
            params.put("showCount", DEFAULT_TOTAL_COUNT);
            params.put("currentPage", page);
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    smart_communal_refresh.finishRefresh();
                    duoMoLifeTimeBrandAdapter.loadMoreComplete();
                    if (page == 1) {
                        brandProductList.clear();
                    }
                    Gson gson = new Gson();
                    DMLTimeDetailEntity dmlTimeDetailEntity = gson.fromJson(result, DMLTimeDetailEntity.class);
                    if (dmlTimeDetailEntity != null) {
                        if (dmlTimeDetailEntity.getCode().equals("01")) {
                            for (int i = 0; i < dmlTimeDetailEntity.getDmlTimeDetailBeanList().size(); i++) {
                                DMLTimeDetailBean dmlTimeDetailBean = dmlTimeDetailEntity.getDmlTimeDetailBeanList().get(i);
                                if (!TextUtils.isEmpty(dmlTimeDetailEntity.getCurrentTime())) {
                                    dmlTimeDetailBean.setCurrentTime(dmlTimeDetailEntity.getCurrentTime());
                                }
                                brandProductList.add(dmlTimeDetailBean);
                            }
                        } else if (!dmlTimeDetailEntity.getCode().equals("02")) {
                            showToast(TimeBrandDetailsActivity.this, dmlTimeDetailEntity.getMsg());
                        }
                    }
                    duoMoLifeTimeBrandAdapter.notifyDataSetChanged();
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    smart_communal_refresh.finishRefresh();
                    duoMoLifeTimeBrandAdapter.loadMoreComplete();
                    super.onError(ex, isOnCallback);
                }
            });
        } else {
            smart_communal_refresh.finishRefresh();
            duoMoLifeTimeBrandAdapter.loadMoreComplete();
            showToast(this, R.string.unConnectedNetwork);
        }
    }

    private void getThemeDetailsData() {
        String url = Url.BASE_URL + Url.H_TIME_BRAND_DETAILS;
        Map<String, Object> params = new HashMap<>();
        params.put("id", brandId);
        NetLoadUtils.getQyInstance().loadNetDataPost(TimeBrandDetailsActivity.this, url
                , params, new NetLoadUtils.NetLoadListener() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();
                        duoMoLifeTimeBrandAdapter.loadMoreComplete();
                        Gson gson = new Gson();
                        dmlThemeDetail = gson.fromJson(result, DMLThemeDetail.class);
                        if (dmlThemeDetail != null) {
                            if (dmlThemeDetail.getCode().equals(SUCCESS_CODE)) {
                                setData(dmlThemeDetail);
                            } else if (dmlThemeDetail.getCode().equals(EMPTY_CODE)) {
                                showToast(TimeBrandDetailsActivity.this, R.string.invalidData);
                            } else {
                                showToast(TimeBrandDetailsActivity.this, dmlThemeDetail.getMsg());
                            }
                        }
                        NetLoadUtils.getQyInstance().showLoadSir(loadService,themeBean,dmlThemeDetail);
                    }

                    @Override
                    public void netClose() {
                        smart_communal_refresh.finishRefresh();
                        duoMoLifeTimeBrandAdapter.loadMoreComplete();
                        showToast(TimeBrandDetailsActivity.this, R.string.unConnectedNetwork);
                        NetLoadUtils.getQyInstance().showLoadSir(loadService,themeBean,dmlThemeDetail);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        smart_communal_refresh.finishRefresh();
                        duoMoLifeTimeBrandAdapter.loadMoreComplete();
                        showToast(TimeBrandDetailsActivity.this, R.string.invalidData);
                        NetLoadUtils.getQyInstance().showLoadSir(loadService,themeBean,dmlThemeDetail);
                    }
                });
    }

    private void setData(DMLThemeDetail dMLThemeDetail) {
        themeBean = dMLThemeDetail.getThemeDataBean();
        GlideImageLoaderUtil.loadCenterCrop(TimeBrandDetailsActivity.this, timeBrandHeaderView.img_domolife_headerbg, themeBean.getPicUrl());
        tv_header_titleAll.setText(getStrings(themeBean.getTitle()));
        if (!TextUtils.isEmpty(themeBean.getDiscription())) {
            timeBrandHeaderView.tv_topic_details_content.setText(themeBean.getDiscription());
        } else {
            if (!TextUtils.isEmpty(themeBean.getSubtitle())) {
                timeBrandHeaderView.tv_topic_details_content.setText(themeBean.getSubtitle());
            } else {
                timeBrandHeaderView.tv_topic_details_content.setVisibility(GONE);
            }
        }
        if (!TextUtils.isEmpty(themeBean.getIndexTitle())) {
            timeBrandHeaderView.rel_topic_details_title.setVisibility(View.VISIBLE);
            timeBrandHeaderView.tv_topic_details_title.setText(themeBean.getIndexTitle());
        } else {
            timeBrandHeaderView.rel_topic_details_title.setVisibility(GONE);
        }
        if (!TextUtils.isEmpty(themeBean.getPicUrlIndex())) {
            GlideImageLoaderUtil.loadCenterCrop(TimeBrandDetailsActivity.this, timeBrandHeaderView.iv_topic_details_index_bg, themeBean.getPicUrlIndex());
        } else {
            timeBrandHeaderView.iv_topic_details_index_bg.setVisibility(GONE);
        }
    }

    class TimeBrandHeaderView {
        //品牌团描述
        @BindView(R.id.tv_topic_details_content)
        TextView tv_topic_details_content;
        //头部背景
        @BindView(R.id.iv_communal_img_bg)
        ImageView img_domolife_headerbg;
        //        品牌团副标题布局
        @BindView(R.id.rel_topic_details_title)
        RelativeLayout rel_topic_details_title;
        //        品牌团副标题
        @BindView(R.id.tv_topic_details_title)
        TextView tv_topic_details_title;
        //        副背景图片
        @BindView(R.id.iv_topic_details_index_bg)
        ImageView iv_topic_details_index_bg;
    }

    class PopupWindowView {
        @BindView(R.id.rp_time_pro_warm)
        RadioGroup rp_time_pro_warm;
    }

    private String getNumber(String str) {
        String regex = "\\d*";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        while (m.find()) {
            if (!"".equals(m.group()))
                return m.group();
        }
        return "3";
    }

    private void getConstant() {
        if (constantMethod == null) {
            constantMethod = new ConstantMethod();
        }
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
