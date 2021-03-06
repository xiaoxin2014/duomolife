package com.amkj.dmsh.dominant.activity;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.CommunalDetailBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dominant.adapter.GoodProductAdapter;
import com.amkj.dmsh.dominant.bean.QualityShopDescripEntity;
import com.amkj.dmsh.dominant.bean.QualityShopDescripEntity.QualityShopDescBean;
import com.amkj.dmsh.homepage.adapter.CommunalDetailAdapter;
import com.amkj.dmsh.mine.activity.ShopCarActivity;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean;
import com.amkj.dmsh.user.bean.UserLikedProductEntity;
import com.amkj.dmsh.user.bean.LikedProductBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.amkj.dmsh.utils.webformatdata.CommunalWebDetailUtils;
import com.amkj.dmsh.utils.webformatdata.ShareDataBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.bugly.beta.tinker.TinkerManager;
import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import q.rorbin.badgeview.Badge;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TWENTY;
import static com.amkj.dmsh.constant.Url.QUALITY_OVERSEAS_DETAIL_LIST;
import static com.amkj.dmsh.dao.OrderDao.getCarCount;

;

public class QualityOverseasDetailsActivity extends BaseActivity {
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    ???????????????
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.iv_img_service)
    ImageView iv_img_service;
    @BindView(R.id.fl_header_service)
    FrameLayout fl_header_service;
    private List<LikedProductBean> proDetailList = new ArrayList();
    //    ????????????
    private List<CommunalDetailObjectBean> descripDetailList = new ArrayList<>();
    private String overseasId;
    private GoodProductAdapter qualityTypeProductAdapter;
    private int page = 1;
    private int scrollY;
    private float screenHeight;
    private OverseasHeaderView overseasHeaderView;
    private QualityShopDescripEntity qualityShopDescripEntity;
    private CommunalDetailAdapter communalDescripAdapter;
    private Badge badge;
    private UserLikedProductEntity userLikedProductEntity;

    @Override
    protected int getContentView() {
        return R.layout.activity_communal_ql_shop_car;
    }

    @Override
    protected void initViews() {
        tv_header_titleAll.setText("");
        iv_img_service.setImageResource(R.drawable.shop_car_gray_icon);
        Intent intent = getIntent();
        overseasId = intent.getStringExtra("overseasId");
        View headerView = LayoutInflater.from(QualityOverseasDetailsActivity.this)
                .inflate(R.layout.layout_cover_title_descrip_header, (ViewGroup) communal_recycler.getParent(), false);
        overseasHeaderView = new OverseasHeaderView();
        ButterKnife.bind(overseasHeaderView, headerView);
        overseasHeaderView.initView();
        communal_recycler.setLayoutManager(new GridLayoutManager(QualityOverseasDetailsActivity.this, 2));
        qualityTypeProductAdapter = new GoodProductAdapter(QualityOverseasDetailsActivity.this, proDetailList);
        qualityTypeProductAdapter.addHeaderView(headerView);
        communal_recycler.setAdapter(qualityTypeProductAdapter);
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // ?????????????????????ID
                .setDividerId(R.drawable.item_divider_five_dp)
                .create());
        smart_communal_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                loadData();
                qualityTypeProductAdapter.loadMoreEnd(true);
            }
        });
        qualityTypeProductAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
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
        badge = ConstantMethod.getBadge(QualityOverseasDetailsActivity.this, fl_header_service);
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
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
    protected void loadData() {
        page = 1;
        //????????????
        getOverseasThemeDetailsData();
        //????????????
        getRecommendData();
//        ???????????????
        getCarCount(getActivity());
    }


    private void getOverseasThemeDetailsData() {
        String url = Url.QUALITY_OVERSEAS_THEME_DETAIL;
        Map<String, Object> params = new HashMap<>();
        params.put("id", overseasId);
        if (userId > 0) {
            params.put("uid", userId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(this, url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                smart_communal_refresh.finishRefresh();
                qualityTypeProductAdapter.loadMoreComplete();

                qualityShopDescripEntity = GsonUtils.fromJson(result, QualityShopDescripEntity.class);
                if (qualityShopDescripEntity != null) {
                    if (qualityShopDescripEntity.getCode().equals(SUCCESS_CODE)) {
                        setData(qualityShopDescripEntity.getQualityShopDescBean());
                    } else if (qualityShopDescripEntity.getCode().equals(EMPTY_CODE)) {
                        if (page == 1) {
                            showToast(R.string.invalidData);
                        }
                    } else {
                        showToast(qualityShopDescripEntity.getMsg());
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                smart_communal_refresh.finishRefresh();
                qualityTypeProductAdapter.loadMoreComplete();
            }

            @Override
            public void onError(Throwable ex) {
                smart_communal_refresh.finishRefresh();
                qualityTypeProductAdapter.loadMoreComplete();
                showToast(R.string.invalidData);
            }
        });
    }

    private void getRecommendData() {
        Map<String, Object> params = new HashMap<>();
        params.put("showCount", TOTAL_COUNT_TWENTY);
        params.put("currentPage", page);
        params.put("id", overseasId);
        if (userId > 0) {
            params.put("uid", userId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(QualityOverseasDetailsActivity.this, QUALITY_OVERSEAS_DETAIL_LIST
                , params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();
                        qualityTypeProductAdapter.loadMoreComplete();

                        if (page == 1) {
                            //??????????????????
                            proDetailList.clear();
                        }
                        userLikedProductEntity = GsonUtils.fromJson(result, UserLikedProductEntity.class);
                        if (userLikedProductEntity != null) {
                            if (userLikedProductEntity.getCode().equals(SUCCESS_CODE)) {
                                proDetailList.addAll(userLikedProductEntity.getGoodsList());
                            } else if (userLikedProductEntity.getCode().equals(EMPTY_CODE)) {
                                qualityTypeProductAdapter.loadMoreEnd();
                            } else {
                                showToast(userLikedProductEntity.getMsg());
                            }
                            qualityTypeProductAdapter.notifyDataSetChanged();
                        }
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, proDetailList, userLikedProductEntity);
                    }

                    @Override
                    public void onNotNetOrException() {
                        smart_communal_refresh.finishRefresh();
                        qualityTypeProductAdapter.loadMoreEnd(true);
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, proDetailList, userLikedProductEntity);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        showToast( R.string.invalidData);
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IS_LOGIN_CODE) {
            page = 1;
            getRecommendData();
        }
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    private void setData(QualityShopDescBean qualityShopDescBean) {
        GlideImageLoaderUtil.loadCenterCrop(QualityOverseasDetailsActivity.this, overseasHeaderView.iv_cover_detail_bg, qualityShopDescBean.getPicUrl());
        tv_header_titleAll.setText(getStrings(qualityShopDescBean.getTitle()));
//        if (!TextUtils.isEmpty(qualityShopDescBean.getSubtitle())) {
//            overseasHeaderView.tv_descrip_title.setVisibility(View.VISIBLE);
//            overseasHeaderView.tv_descrip_title.setText(qualityShopDescBean.getSubtitle());
//        } else {
        overseasHeaderView.tv_descrip_title.setVisibility(GONE);
//        }
        List<CommunalDetailBean> descripList = qualityShopDescBean.getDescripList();
        if (descripList != null) {
            descripDetailList.clear();
            descripDetailList.addAll(CommunalWebDetailUtils.getCommunalWebInstance().getWebDetailsFormatDataList(descripList));
            communalDescripAdapter.setNewData(descripDetailList);
        }
    }

    //    ????????????
    @OnClick(R.id.iv_img_share)
    void sendShare(View view) {
        if (qualityShopDescripEntity != null && qualityShopDescripEntity.getQualityShopDescBean() != null) {
            new UMShareAction(QualityOverseasDetailsActivity.this
                    , qualityShopDescripEntity.getQualityShopDescBean().getPicUrl()
                    , qualityShopDescripEntity.getQualityShopDescBean().getTitle()
                    , getStrings(qualityShopDescripEntity.getQualityShopDescBean().getSubtitle())
                    , Url.BASE_SHARE_PAGE_TWO + "goods/Project_details.html?id=" + qualityShopDescripEntity.getQualityShopDescBean().getId(), qualityShopDescripEntity.getQualityShopDescBean().getId());
        }
    }

    @OnClick(R.id.iv_img_service)
    void skipService(View view) {
        Intent intent = new Intent(QualityOverseasDetailsActivity.this, ShopCarActivity.class);
        startActivity(intent);
    }

    class OverseasHeaderView {
        //??????
        @BindView(R.id.tv_descrip_title)
        TextView tv_descrip_title;
        @BindView(R.id.communal_recycler_wrap)
        RecyclerView communal_recycler_wrap;
        //????????????
        @BindView(R.id.iv_cover_detail_bg)
        ImageView iv_cover_detail_bg;

        public void initView() {
            communal_recycler_wrap.setNestedScrollingEnabled(false);
            communalDescripAdapter = new CommunalDetailAdapter(QualityOverseasDetailsActivity.this, descripDetailList);
            communal_recycler_wrap.setLayoutManager(new LinearLayoutManager(QualityOverseasDetailsActivity.this));
            communal_recycler_wrap.setAdapter(communalDescripAdapter);
            communalDescripAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    ShareDataBean shareDataBean = null;
                    if (view.getId() == R.id.tv_communal_share
                            && qualityShopDescripEntity != null && qualityShopDescripEntity.getQualityShopDescBean() != null) {
                        QualityShopDescBean qualityShopDescBean = qualityShopDescripEntity.getQualityShopDescBean();
                        shareDataBean = new ShareDataBean(qualityShopDescBean.getPicUrl()
                                , qualityShopDescBean.getTitle()
                                , getStrings(qualityShopDescBean.getSubtitle())
                                , Url.BASE_SHARE_PAGE_TWO + "goods/Project_details.html?id=" + qualityShopDescBean.getId());

                    }
                    CommunalWebDetailUtils.getCommunalWebInstance()
                            .setWebDataClick(QualityOverseasDetailsActivity.this, shareDataBean, view, loadHud);
                }
            });
        }
    }


    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (message.type.equals(ConstantVariable.UPDATE_CAR_NUM)) {
            if (badge != null) {
                badge.setBadgeNumber((int) message.result);
            }
        }
    }
}
