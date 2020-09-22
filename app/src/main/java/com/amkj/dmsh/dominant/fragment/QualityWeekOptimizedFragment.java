package com.amkj.dmsh.dominant.fragment;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.BaseAddCarProInfoBean;
import com.amkj.dmsh.bean.CommunalDetailBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dominant.adapter.QualityBuyListAdapter;
import com.amkj.dmsh.dominant.bean.QualityBuyListEntity;
import com.amkj.dmsh.dominant.bean.QualityBuyListEntity.QualityBuyListBean;
import com.amkj.dmsh.dominant.bean.ShopBuyDetailEntity;
import com.amkj.dmsh.dominant.bean.ShopBuyDetailEntity.ShopBuyDetailBean;
import com.amkj.dmsh.homepage.adapter.CommunalDetailAdapter;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import q.rorbin.badgeview.Badge;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.amkj.dmsh.constant.ConstantMethod.getBadge;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TWENTY;
import static com.amkj.dmsh.constant.Url.QUALITY_WEEK_OPTIMIZED_DETAIL;
import static com.amkj.dmsh.constant.Url.QUALITY_WEEK_OPTIMIZED_PRO;
import static com.amkj.dmsh.dao.OrderDao.addShopCarGetSku;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/9/15
 * class description:每周优选
 */
public class QualityWeekOptimizedFragment extends BaseFragment {
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
    @BindView(R.id.tl_quality_bar)
    Toolbar mTlQualityBar;
    private int page = 1;
    private int scrollY;
    private float screenHeight;
    private List<QualityBuyListBean> qualityBuyListBeanList = new ArrayList();
    private List<CommunalDetailObjectBean> itemDescriptionList = new ArrayList();
    private QualityBuyListAdapter qualityBuyListAdapter;
    private CommunalDetailAdapter communalDetailAdapter;
    private ShopBuyListView shopBuyListView;
    private Badge badge;
    private ShopBuyDetailBean shopBuyDetailBean;
    private QualityBuyListEntity qualityBuyListEntity;

    @Override
    protected int getContentView() {
        return R.layout.activity_communal_ql_shop_car;
    }

    @Override
    protected void initViews() {
        mTlQualityBar.setVisibility(GONE);
        tv_header_titleAll.setText("每周优选");
        iv_img_service.setImageResource(R.drawable.shop_car_gray_icon);
        communal_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        smart_communal_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                loadData();
            }
        });
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
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_gray_f_one_px)
                .create());
        qualityBuyListAdapter = new QualityBuyListAdapter(getActivity(), qualityBuyListBeanList);
        qualityBuyListAdapter.setHeaderAndEmpty(true);
        View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_communal_detail_scroll_rec_cover_wrap, null);
        shopBuyListView = new ShopBuyListView();
        ButterKnife.bind(shopBuyListView, headerView);
        shopBuyListView.initView();
        qualityBuyListAdapter.addHeaderView(headerView);
        communal_recycler.setAdapter(qualityBuyListAdapter);
        qualityBuyListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                getBuyListRecommend();
            }
        }, communal_recycler);

        qualityBuyListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                QualityBuyListBean qualityBuyListBean = (QualityBuyListBean) view.getTag();
                if (qualityBuyListBean != null) {
                    switch (view.getId()) {
                        case R.id.iv_ql_bl_add_car:
                            BaseAddCarProInfoBean baseAddCarProInfoBean = new BaseAddCarProInfoBean();
                            baseAddCarProInfoBean.setProductId(qualityBuyListBean.getId());
                            baseAddCarProInfoBean.setActivityCode(getStrings(qualityBuyListBean.getActivityCode()));
                            baseAddCarProInfoBean.setProName(getStrings(qualityBuyListBean.getName()));
                            baseAddCarProInfoBean.setProPic(getStrings(qualityBuyListBean.getPicUrl()));
                            addShopCarGetSku(getActivity(), baseAddCarProInfoBean, loadHud);
                            break;
                    }
                }
            }
        });
        badge = getBadge(getActivity(), fl_header_service);
    }


    @Override
    protected void loadData() {
        page = 1;
        getBuyListDetailData();
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    private void getBuyListDetailData() {
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), QUALITY_WEEK_OPTIMIZED_DETAIL, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                itemDescriptionList.clear();

                ShopBuyDetailEntity shopDetailsEntity = GsonUtils.fromJson(result, ShopBuyDetailEntity.class);
                if (shopDetailsEntity != null) {
                    if (shopDetailsEntity.getCode().equals(SUCCESS_CODE)) {
                        shopBuyDetailBean = shopDetailsEntity.getShopBuyDetailBean();
                        //记录埋点参数sourceId(每周优选id)
                        ConstantMethod.saveSourceId(QualityWeekOptimizedFragment.class.getSimpleName(), String.valueOf(shopBuyDetailBean.getId()));
//                        //配置封面图
                        GlideImageLoaderUtil.loadImgDynamicDrawable(getActivity(), shopBuyListView.iv_communal_cover_wrap
                                , shopBuyDetailBean.getCoverImgUrl(), -1);
                        List<CommunalDetailBean> descriptionBeanList = shopBuyDetailBean.getDescriptionBeanList();
                        if (descriptionBeanList != null) {
//                            //筛选空行
//                            Iterator<CommunalDetailBean> iterator = descriptionBeanList.iterator();
//                            while (iterator.hasNext()) {
//                                CommunalDetailBean bean = iterator.next();
//                                if ("text".equals(bean.getType()) && bean.getContent() instanceof String && "<p><br/></p>".equals(bean.getContent())) {
//                                    iterator.remove();
//                                }
//                            }

                            itemDescriptionList.addAll(CommunalWebDetailUtils.getCommunalWebInstance().getWebDetailsFormatDataList(descriptionBeanList));
                        }
                    } else if (!shopDetailsEntity.getCode().equals(EMPTY_CODE)) {
                        showToast(shopDetailsEntity.getMsg());
                    }
                    communalDetailAdapter.setNewData(itemDescriptionList);
                    getBuyListRecommend();
                }
            }

            @Override
            public void onNotNetOrException() {
                getBuyListRecommend();
            }
        });
    }

    private void getBuyListRecommend() {
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", page);
        params.put("showCount", TOTAL_COUNT_TWENTY);
        if (userId > 0) {
            params.put("uid", userId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), QUALITY_WEEK_OPTIMIZED_PRO
                , params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        qualityBuyListAdapter.loadMoreComplete();
                        smart_communal_refresh.finishRefresh();
                        if (page == 1) {
                            qualityBuyListBeanList.clear();
                        }

                        qualityBuyListEntity = GsonUtils.fromJson(result, QualityBuyListEntity.class);
                        if (qualityBuyListEntity != null) {
                            if (qualityBuyListEntity.getCode().equals(SUCCESS_CODE)) {
                                qualityBuyListBeanList.addAll(qualityBuyListEntity.getQualityBuyListBeanList());
                            } else if (qualityBuyListEntity.getCode().equals(EMPTY_CODE)) {
                                qualityBuyListAdapter.loadMoreEnd();
                            } else {
                                showToast( qualityBuyListEntity.getMsg());
                            }
                            qualityBuyListAdapter.notifyDataSetChanged();
                        }
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, qualityBuyListBeanList, qualityBuyListEntity);
                    }

                    @Override
                    public void onNotNetOrException() {
                        smart_communal_refresh.finishRefresh();
                        qualityBuyListAdapter.loadMoreEnd(true);
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, qualityBuyListBeanList, qualityBuyListEntity);
                    }
                });
    }


    class ShopBuyListView {
        @BindView(R.id.communal_recycler_wrap)
        RecyclerView communal_recycler_wrap;
        @BindView(R.id.iv_communal_cover_wrap)
        ImageView iv_communal_cover_wrap;

        public void initView() {
            iv_communal_cover_wrap.setVisibility(View.VISIBLE);
            communal_recycler_wrap.setNestedScrollingEnabled(false);
            communal_recycler_wrap.setLayoutManager(new LinearLayoutManager((getActivity())));
            communalDetailAdapter = new CommunalDetailAdapter(getActivity(), itemDescriptionList);
            communal_recycler_wrap.setAdapter(communalDetailAdapter);
            communalDetailAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    ShareDataBean shareDataBean = null;
                    if (view.getId() == R.id.tv_communal_share && shopBuyDetailBean != null) {
                        shareDataBean = new ShareDataBean(shopBuyDetailBean.getCoverImgUrl()
                                , "每周优选"
                                , "摸透你的心，为你精选最应季最实用最优质的热门精品"
                                , Url.BASE_SHARE_PAGE_TWO + "home/weekly_optimization.html");

                    }
                    CommunalWebDetailUtils.getCommunalWebInstance()
                            .setWebDataClick(getActivity(), shareDataBean, view, loadHud);
                }
            });
        }
    }
}
