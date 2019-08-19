package com.amkj.dmsh.dominant.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.constant.BaseAddCarProInfoBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.dominant.adapter.QualityTypeProductAdapter;
import com.amkj.dmsh.dominant.bean.CustomCoverDesEntity;
import com.amkj.dmsh.dominant.bean.CustomCoverDesEntity.CustomCoverDesBean;
import com.amkj.dmsh.homepage.adapter.CommunalDetailAdapter;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean;
import com.amkj.dmsh.user.bean.UserLikedProductEntity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean;
import com.amkj.dmsh.utils.RemoveExistUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.amkj.dmsh.utils.webformatdata.CommunalWebDetailUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
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

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.insertFragmentNewTotalData;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TWENTY;
import static com.amkj.dmsh.constant.Url.Q_CUSTOM_PRO_COVER;
import static com.amkj.dmsh.constant.Url.Q_CUSTOM_PRO_LIST;

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
    private RemoveExistUtils removeExistUtils;

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_smart_refresh_recycler_float_loading;
    }

    @Override
    protected void initViews() {
        communal_recycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
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
        qualityTypeProductAdapter = new QualityTypeProductAdapter(getActivity(), customProList);
        headerView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_communal_detail_scroll_rec_cover_wrap, null, false);
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
                getQualityCustomPro();
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
        removeExistUtils = new RemoveExistUtils();
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
        if (TextUtils.isEmpty(productType)) {
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("productType", productType);
        params.put("version", 1);
        if (userId > 0) {
            params.put("uid", userId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Q_CUSTOM_PRO_COVER, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                if (descriptionList.size() > 0) {
                    descriptionList.clear();
                    communalDetailAdapter.notifyDataSetChanged();
                }
                Gson gson = new Gson();
                CustomCoverDesEntity customCoverDesEntity = gson.fromJson(result, CustomCoverDesEntity.class);
                if (customCoverDesEntity != null) {
                    if (customCoverDesEntity.getCode().equals(SUCCESS_CODE)
                            && customCoverDesEntity.getCoverDesList() != null
                            && customCoverDesEntity.getCoverDesList().size() > 0) {
                        CustomCoverDesBean customCoverDesBean = customCoverDesEntity.getCoverDesList().get(0);
                        if (!TextUtils.isEmpty(customCoverDesBean.getPicUrl())) {
                            qNewProView.iv_communal_cover_wrap.setVisibility(View.VISIBLE);
                            GlideImageLoaderUtil.loadImgDynamicDrawable(getActivity(), qNewProView.iv_communal_cover_wrap,
                                    getStrings(customCoverDesBean.getPicUrl()),-1);
                        } else {
                            qNewProView.iv_communal_cover_wrap.setVisibility(View.GONE);
                        }
                        if (customCoverDesBean.getDescriptionList() != null
                                && customCoverDesBean.getDescriptionList().size() > 0) {
                            qNewProView.communal_recycler_wrap.setVisibility(View.VISIBLE);
                            qNewProView.v_line_bottom.setVisibility(View.VISIBLE);
                            List<CommunalDetailObjectBean> detailsDataList = CommunalWebDetailUtils.getCommunalWebInstance().getWebDetailsFormatDataList(customCoverDesBean.getDescriptionList());
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
            public void onNotNetOrException() {
                qNewProView.iv_communal_cover_wrap.setVisibility(View.GONE);
                if (qualityTypeProductAdapter.getHeaderLayoutCount() > 0) {
                    qualityTypeProductAdapter.removeAllHeaderView();
                }
            }
        });
    }

    private void getQualityCustomPro() {
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", page);
        params.put("productType", productType);
        params.put("showCount", TOTAL_COUNT_TWENTY);
        if (userId > 0) {
            params.put("uid", userId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Q_CUSTOM_PRO_LIST, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                smart_communal_refresh.finishRefresh();
                qualityTypeProductAdapter.loadMoreComplete();
                if (page == 1) {
                    customProList.clear();
                    removeExistUtils.clearData();
                }
                Gson gson = new Gson();
                userLikedProductEntity = gson.fromJson(result, UserLikedProductEntity.class);
                if (userLikedProductEntity != null) {
                    if (userLikedProductEntity.getCode().equals(SUCCESS_CODE)) {
                        customProList.addAll(removeExistUtils.removeExistList(userLikedProductEntity.getGoodsList()));
                    } else if (userLikedProductEntity.getCode().equals(EMPTY_CODE)) {
                        qualityTypeProductAdapter.loadMoreEnd();
                    } else {
                        showToast(getActivity(), userLikedProductEntity.getMsg());
                    }
                    qualityTypeProductAdapter.notifyDataSetChanged();
                }
                NetLoadUtils.getNetInstance().showLoadSir(loadService, customProList, userLikedProductEntity);
            }

            @Override
            public void netClose() {
                showToast(getActivity(), R.string.unConnectedNetwork);
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(getActivity(), R.string.invalidData);
            }

            @Override
            public void onNotNetOrException() {
                smart_communal_refresh.finishRefresh();
                qualityTypeProductAdapter.loadMoreEnd(true);
                NetLoadUtils.getNetInstance().showLoadSir(loadService, customProList, userLikedProductEntity);
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
