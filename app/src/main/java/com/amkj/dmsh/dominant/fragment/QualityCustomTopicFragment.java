package com.amkj.dmsh.dominant.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseApplication;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.base.NetLoadUtils;
import com.amkj.dmsh.constant.BaseAddCarProInfoBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.dominant.adapter.QualityTypeProductAdapter;
import com.amkj.dmsh.dominant.bean.CustomCoverDesEntity;
import com.amkj.dmsh.dominant.bean.CustomCoverDesEntity.CustomCoverDesBean;
import com.amkj.dmsh.homepage.adapter.CommunalDetailAdapter;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean;
import com.amkj.dmsh.user.bean.UserLikedProductEntity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.insertFragmentNewTotalData;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_TOTAL_COUNT;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.dominant.fragment.QualityFragment.updateCarNum;

;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/04/03
 * class description:自定义专区
 */
public class QualityCustomTopicFragment extends BaseFragment {
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    private int page = 1;
    private int scrollY;
    private float screenHeight;
    private QualityTypeProductAdapter qualityTypeProductAdapter;
    private List<LikedProductBean> customProList = new ArrayList();
    private List<CommunalDetailObjectBean> descriptionList = new ArrayList();
    private QNewProView qNewProView;
    private String productType;
    private View headerView;
    private CommunalDetailAdapter communalDetailAdapter;
    private UserLikedProductEntity userLikedProductEntity;

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_smart_refresh_recycler_float_loading;
    }

    @Override
    protected void initViews() {
        communal_recycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        smart_communal_refresh.setOnRefreshListener((refreshLayout) -> {
            loadData();
        });
        download_btn_communal.attachToRecyclerView(communal_recycler, null, new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                scrollY += dy;
                if (!recyclerView.canScrollVertically(-1)) {
                    scrollY = 0;
                }
                if (screenHeight == 0) {
                    BaseApplication app = (BaseApplication) getActivity().getApplication();
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
        qualityTypeProductAdapter = new QualityTypeProductAdapter(getActivity(), customProList);
        headerView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_communal_detail_scroll_rec_cover_wrap, null, false);
        qNewProView = new QNewProView();
        ButterKnife.bind(qNewProView, headerView);
        qNewProView.initViews();
        communal_recycler.setAdapter(qualityTypeProductAdapter);
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
        qualityTypeProductAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (page * DEFAULT_TOTAL_COUNT <= customProList.size()) {
                    page++;
                    getQualityCustomPro();
                } else {
                    qualityTypeProductAdapter.loadMoreEnd();
                }
            }
        }, communal_recycler);
        qualityTypeProductAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                LikedProductBean likedProductBean = (LikedProductBean) view.getTag();
                if (likedProductBean != null) {
                    Intent intent = new Intent(getActivity(), ShopScrollDetailsActivity.class);
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
                                constantMethod.addShopCarGetSku(getActivity(), baseAddCarProInfoBean, loadHud);
                                constantMethod.setAddOnCarListener(new ConstantMethod.OnAddCarListener() {
                                    @Override
                                    public void onAddCarSuccess() {
                                        EventBus.getDefault().post(new EventMessage(updateCarNum, updateCarNum));
                                    }
                                });
                                break;
                        }
                    } else {
                        loadHud.dismiss();
                        getLoginStatus(QualityCustomTopicFragment.this);
                    }
                }
            }
        });
        totalPersonalTrajectory = insertFragmentNewTotalData(getActivity(), this.getClass().getSimpleName(), productType);
    }

    @Override
    protected void loadData() {
        page = 1;
        getCustomCoverDescription();
        getQualityCustomPro();
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    /**
     * 获取自定义专区封面描述
     */
    private void getCustomCoverDescription() {
        String url = Url.BASE_URL + Url.Q_CUSTOM_PRO_COVER;
        if (NetWorkUtils.checkNet(getActivity())
                && !TextUtils.isEmpty(productType)) {
            Map<String, Object> params = new HashMap<>();
            params.put("productType", productType);
            if (userId > 0) {
                params.put("uid", userId);
            }
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    if (descriptionList.size() > 0) {
                        descriptionList.clear();
                        communalDetailAdapter.notifyDataSetChanged();
                    }
                    Gson gson = new Gson();
                    CustomCoverDesEntity customCoverDesEntity = gson.fromJson(result, CustomCoverDesEntity.class);
                    if (customCoverDesEntity != null) {
                        if (customCoverDesEntity.getCode().equals("01")
                                && customCoverDesEntity.getCoverDesList() != null
                                && customCoverDesEntity.getCoverDesList().size() > 0) {
                            CustomCoverDesBean customCoverDesBean = customCoverDesEntity.getCoverDesList().get(0);
                            if (!TextUtils.isEmpty(customCoverDesBean.getPicUrl())) {
                                qNewProView.iv_communal_cover_wrap.setVisibility(View.VISIBLE);
                                GlideImageLoaderUtil.loadImgDynamicDrawable(getActivity(), qNewProView.iv_communal_cover_wrap,
                                        getStrings(customCoverDesBean.getPicUrl()));
                            } else {
                                qNewProView.iv_communal_cover_wrap.setVisibility(View.GONE);
                            }
                            if (customCoverDesBean.getDescriptionList() != null
                                    && customCoverDesBean.getDescriptionList().size() > 0) {
                                qNewProView.communal_recycler_wrap.setVisibility(View.VISIBLE);
                                qNewProView.v_line_bottom.setVisibility(View.VISIBLE);
                                List<CommunalDetailObjectBean> detailsDataList = ConstantMethod.getDetailsDataList(customCoverDesBean.getDescriptionList());
                                if (detailsDataList != null) {
                                    descriptionList.addAll(detailsDataList);
                                    communalDetailAdapter.notifyDataSetChanged();
                                }
                            } else {
                                qNewProView.v_line_bottom.setVisibility(View.GONE);
                                qNewProView.communal_recycler_wrap.setVisibility(View.GONE);
                            }
                            if (qualityTypeProductAdapter.getHeaderLayoutCount() < 1) {
                                qualityTypeProductAdapter.addHeaderView(headerView);
                                qualityTypeProductAdapter.notifyDataSetChanged();
                            }
                        } else {
                            qualityTypeProductAdapter.removeAllHeaderView();
                            qualityTypeProductAdapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    super.onError(ex, isOnCallback);
                    qNewProView.iv_communal_cover_wrap.setVisibility(View.GONE);
                }
            });
        } else {
            qualityTypeProductAdapter.removeAllHeaderView();
        }
    }

    private void getQualityCustomPro() {
        String url = Url.BASE_URL + Url.Q_CUSTOM_PRO_LIST;
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", page);
        params.put("productType", productType);
        params.put("showCount", DEFAULT_TOTAL_COUNT);
        if (userId > 0) {
            params.put("uid", userId);
        }
        NetLoadUtils.getQyInstance().loadNetDataPost(getActivity(), url, params, new NetLoadUtils.NetLoadListener() {
            @Override
            public void onSuccess(String result) {
                smart_communal_refresh.finishRefresh();
                qualityTypeProductAdapter.loadMoreComplete();
                if (page == 1) {
                    customProList.clear();
                }
                Gson gson = new Gson();
                userLikedProductEntity = gson.fromJson(result, UserLikedProductEntity.class);
                if (userLikedProductEntity != null) {
                    if (userLikedProductEntity.getCode().equals(SUCCESS_CODE)) {
                        customProList.addAll(userLikedProductEntity.getLikedProductBeanList());
                    } else if (!userLikedProductEntity.getCode().equals(EMPTY_CODE)) {
                        showToast(getActivity(), userLikedProductEntity.getMsg());
                    }
                    qualityTypeProductAdapter.notifyDataSetChanged();
                }
                NetLoadUtils.getQyInstance().showLoadSir(loadService,customProList, userLikedProductEntity);
            }

            @Override
            public void netClose() {
                smart_communal_refresh.finishRefresh();
                qualityTypeProductAdapter.loadMoreComplete();
                showToast(getActivity(), R.string.unConnectedNetwork);
                NetLoadUtils.getQyInstance().showLoadSir(loadService,customProList, userLikedProductEntity);
            }

            @Override
            public void onError(Throwable throwable) {
                smart_communal_refresh.finishRefresh();
                qualityTypeProductAdapter.loadMoreComplete();
                showToast(getActivity(), R.string.invalidData);
                NetLoadUtils.getQyInstance().showLoadSir(loadService,customProList, userLikedProductEntity);
            }
        });
    }

    class QNewProView {
        @BindView(R.id.iv_communal_cover_wrap)
        ImageView iv_communal_cover_wrap;
        @BindView(R.id.communal_recycler_wrap)
        RecyclerView communal_recycler_wrap;
        @BindView(R.id.v_line_bottom)
        View v_line_bottom;

        public void initViews() {
            communal_recycler_wrap.setNestedScrollingEnabled(false);
            communal_recycler_wrap.setLayoutManager(new LinearLayoutManager(getActivity()));
            communalDetailAdapter = new CommunalDetailAdapter(getActivity(), descriptionList);
            communal_recycler_wrap.setAdapter(communalDetailAdapter);
        }
    }

    @Override
    protected void getReqParams(Bundle bundle) {
        productType = bundle.getString("productType");
    }

}
