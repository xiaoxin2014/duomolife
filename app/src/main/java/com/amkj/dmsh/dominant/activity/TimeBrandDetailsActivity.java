package com.amkj.dmsh.dominant.activity;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.DMLThemeDetail;
import com.amkj.dmsh.bean.DMLThemeDetail.ThemeDataBean;
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dominant.bean.DMLTimeDetailEntity;
import com.amkj.dmsh.dominant.bean.DMLTimeDetailEntity.DMLTimeDetailBean;
import com.amkj.dmsh.homepage.adapter.DoMoLifeTimeBrandAdapter;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.amkj.dmsh.views.CustomPopWindow;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
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
import static android.view.View.VISIBLE;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TWENTY;
import static com.amkj.dmsh.constant.Url.H_TIME_BRAND_DETAILS;
import static com.amkj.dmsh.constant.Url.H_TIME_BRAND_DETAILS_REC;

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
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_white).create());
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

        smart_communal_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                loadData();
            }
        });
        duoMoLifeTimeBrandAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                getRecommendData();
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
                    , Url.BASE_SHARE_PAGE_TWO + "m/template/limit_time_template/brand.html?id=" + themeDataBean.getId(),themeDataBean.getId());
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
    public View getLoadView() {
        return smart_communal_refresh;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    private void getRecommendData() {
        Map<String, Object> params = new HashMap<>();
        if (!TextUtils.isEmpty(brandId)) {
            params.put("id", brandId);
        }
        params.put("showCount", TOTAL_COUNT_TWENTY);
        params.put("currentPage", page);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, H_TIME_BRAND_DETAILS_REC, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                smart_communal_refresh.finishRefresh();
                duoMoLifeTimeBrandAdapter.loadMoreComplete();
                if (page == 1) {
                    brandProductList.clear();
                }

                DMLTimeDetailEntity dmlTimeDetailEntity = GsonUtils.fromJson(result, DMLTimeDetailEntity.class);
                if (dmlTimeDetailEntity != null) {
                    if (dmlTimeDetailEntity.getCode().equals(SUCCESS_CODE)) {
                        for (int i = 0; i < dmlTimeDetailEntity.getDmlTimeDetailBeanList().size(); i++) {
                            DMLTimeDetailBean dmlTimeDetailBean = dmlTimeDetailEntity.getDmlTimeDetailBeanList().get(i);
                            if (!TextUtils.isEmpty(dmlTimeDetailEntity.getCurrentTime())) {
                                dmlTimeDetailBean.setCurrentTime(dmlTimeDetailEntity.getCurrentTime());
                            }
                            brandProductList.add(dmlTimeDetailBean);
                        }
                    } else if (dmlTimeDetailEntity.getCode().equals(EMPTY_CODE)) {
                        duoMoLifeTimeBrandAdapter.loadMoreEnd();
                    } else {
                        showToast(dmlTimeDetailEntity.getMsg());
                    }
                }
                duoMoLifeTimeBrandAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNotNetOrException() {
                smart_communal_refresh.finishRefresh();
                duoMoLifeTimeBrandAdapter.loadMoreEnd(true);
            }
        });
    }

    private void getThemeDetailsData() {
        Map<String, Object> params = new HashMap<>();
        params.put("id", brandId);
        NetLoadUtils.getNetInstance().loadNetDataPost(TimeBrandDetailsActivity.this, H_TIME_BRAND_DETAILS
                , params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();
                        duoMoLifeTimeBrandAdapter.loadMoreComplete();

                        dmlThemeDetail = GsonUtils.fromJson(result, DMLThemeDetail.class);
                        if (dmlThemeDetail != null) {
                            if (dmlThemeDetail.getCode().equals(SUCCESS_CODE)) {
                                setData(dmlThemeDetail);
                            } else if (!dmlThemeDetail.getCode().equals(EMPTY_CODE)) {
                                showToast( dmlThemeDetail.getMsg());
                            }
                        }
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, themeBean, dmlThemeDetail);
                    }

                    @Override
                    public void onNotNetOrException() {
                        smart_communal_refresh.finishRefresh();
                        duoMoLifeTimeBrandAdapter.loadMoreEnd(true);
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, themeBean, dmlThemeDetail);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        showToast(R.string.invalidData);
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

}
