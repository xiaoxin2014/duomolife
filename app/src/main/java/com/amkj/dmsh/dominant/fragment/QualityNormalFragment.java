package com.amkj.dmsh.dominant.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.bean.QualityTypeEntity;
import com.amkj.dmsh.bean.QualityTypeEntity.QualityTypeBean;
import com.amkj.dmsh.constant.BaseAddCarProInfoBean;
import com.amkj.dmsh.constant.CommunalAdHolderView;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.dominant.adapter.ChildProductTypeAdapter;
import com.amkj.dmsh.dominant.adapter.QualityTypeProductAdapter;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity.CommunalADActivityBean;
import com.amkj.dmsh.mine.activity.MineLoginActivity;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.google.gson.Gson;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.zhy.autolayout.utils.AutoUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static com.amkj.dmsh.constant.ConstantMethod.getIntegers;
import static com.amkj.dmsh.constant.ConstantMethod.getShowNumber;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.insertFragmentNewTotalData;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.CATEGORY_CHILD;
import static com.amkj.dmsh.constant.ConstantVariable.CATEGORY_ID;
import static com.amkj.dmsh.constant.ConstantVariable.CATEGORY_NAME;
import static com.amkj.dmsh.constant.ConstantVariable.CATEGORY_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.START_AUTO_PAGE_TURN;
import static com.amkj.dmsh.constant.ConstantVariable.STOP_AUTO_PAGE_TURN;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TWENTY;
import static com.amkj.dmsh.dominant.fragment.QualityFragment.updateCarNum;

;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/6/29
 * class description:良品分类
 */

public class QualityNormalFragment extends BaseFragment {
    @BindView(R.id.smart_communal_refresh)
    RefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.communal_load)
    View communal_load;
    @BindView(R.id.communal_error)
    View communal_error;
    @BindView(R.id.communal_empty)
    View communal_empty;
    private int page = 1;
    private List<LikedProductBean> typeDetails = new ArrayList();
    private List<CommunalADActivityBean> adBeanList = new ArrayList<>();
    private QualityTypeProductAdapter qualityTypeProductAdapter;
    private QTypeView qTypeView;
    private QualityTypeBean qualityTypeBeanChange;
    //    子分类
    private List<QualityTypeBean> childTypeList = new ArrayList<>();
    private int uid;
    private View headerAdView;
    private View childTypeHeaderView;
    private ChildProductTypeAdapter childProductTypeAdapter;
    private CBViewHolderCreator cbViewHolderCreator;

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_smart_refresh_recycler_float_loading;
    }

    @Override
    protected void initViews() {
        isLoginStatus();
        communal_recycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
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
            page = 1;
            loadData();
        });

        qualityTypeProductAdapter = new QualityTypeProductAdapter(getActivity(), typeDetails);
        headerAdView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_al_new_sp_banner, null, false);
        childTypeHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_communal_recycler_wrap, null, false);
        qTypeView = new QTypeView();
        ButterKnife.bind(qTypeView, headerAdView);
        ButterKnife.bind(qTypeView, childTypeHeaderView);
        qTypeView.initViews();
        communal_recycler.setAdapter(qualityTypeProductAdapter);
        qualityTypeProductAdapter.setOnLoadMoreListener(() -> {
            page++;
            getQualityTypePro();
        }, communal_recycler);
        qualityTypeProductAdapter.setOnItemClickListener((adapter, view, position) -> {
            LikedProductBean likedProductBean = (LikedProductBean) view.getTag();
            if (likedProductBean != null) {
                Intent intent = new Intent(getActivity(), ShopScrollDetailsActivity.class);
                intent.putExtra("productId", String.valueOf(likedProductBean.getId()));
                startActivity(intent);
            }
        });
        qualityTypeProductAdapter.setOnItemChildClickListener((adapter, view, position) -> {
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
                            constantMethod.setAddOnCarListener(() -> EventBus.getDefault().post(new EventMessage(updateCarNum, updateCarNum)));
                            break;
                    }
                } else {
                    loadHud.dismiss();
                    getLoginStatus();
                }
            }
        });
        totalPersonalTrajectory = insertFragmentNewTotalData(getActivity(),this.getClass().getSimpleName(),String.valueOf(qualityTypeBeanChange.getId()));
        communal_load.setVisibility(View.VISIBLE);
    }

    private void isLoginStatus() {
        SavePersonalInfoBean personalInfo = ConstantMethod.getPersonalInfo(getActivity());
        if (personalInfo.isLogin()) {
            uid = personalInfo.getUid();
        } else {
            uid = 0;
        }
    }

    private void getLoginStatus() {
        SavePersonalInfoBean personalInfo = ConstantMethod.getPersonalInfo(getActivity());
        if (personalInfo.isLogin()) {
            uid = personalInfo.getUid();
        } else {
            //未登录跳转登录页
            Intent intent = new Intent(getActivity(), MineLoginActivity.class);
            startActivityForResult(intent, ConstantVariable.IS_LOGIN_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IS_LOGIN_CODE) {
            getLoginStatus();
            getQualityTypePro();
        }
    }

    @Override
    protected void loadData() {
        if (qualityTypeBeanChange != null) {
            getAdTypeData();
            getChildrenType();
            getQualityTypePro();
        } else {
            communal_load.setVisibility(View.GONE);
        }
    }

    private void getAdTypeData() {
        String url = Url.BASE_URL + Url.Q_QUALITY_TYPE_AD;
        if (NetWorkUtils.checkNet(getActivity())) {
            Map<String, Object> params = new HashMap<>();
            params.put("categoryType", qualityTypeBeanChange.getType());
            params.put("categoryId", qualityTypeBeanChange.getId());
            params.put("vidoShow", "1");
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    smart_communal_refresh.finishRefresh();
                    Gson gson = new Gson();
                    adBeanList.clear();
                    CommunalADActivityEntity qualityAdLoop = gson.fromJson(result, CommunalADActivityEntity.class);
                    if (qualityAdLoop != null) {
                        if (qualityAdLoop.getCode().equals("01")) {
                            adBeanList.addAll(qualityAdLoop.getCommunalADActivityBeanList());
                            if (cbViewHolderCreator == null) {
                                cbViewHolderCreator = new CBViewHolderCreator() {
                                    @Override
                                    public Holder createHolder(View itemView) {
                                        return new CommunalAdHolderView(itemView, getContext(), true);
                                    }

                                    @Override
                                    public int getLayoutId() {
                                        return R.layout.layout_ad_image_video;
                                    }
                                };
                            }
                            qTypeView.ad_communal_banner.setPages(getActivity(),cbViewHolderCreator, adBeanList).setCanLoop(true).setPointViewVisible(true).setCanScroll(true)
                                    .setPageIndicator(new int[]{R.drawable.unselected_radius, R.drawable.selected_radius})
                                    .startTurning(getShowNumber(adBeanList.get(0).getShowTime()) * 1000);
                        }
                        if (adBeanList.size() > 0) {
                            if (headerAdView.getParent() == null) {
                                qualityTypeProductAdapter.addHeaderView(headerAdView);
                            }
                        } else {
                            qualityTypeProductAdapter.removeHeaderView(headerAdView);
                        }
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    smart_communal_refresh.finishRefresh();
                    if (adBeanList.size() > 0) {
                        if (headerAdView.getParent() == null) {
                            qualityTypeProductAdapter.addHeaderView(headerAdView);
                        }
                    } else {
                        qualityTypeProductAdapter.removeHeaderView(headerAdView);
                    }
                    super.onError(ex, isOnCallback);
                }
            });
        }
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (START_AUTO_PAGE_TURN.equals(message.type)) {
            if (adBeanList.size() > 0 && qTypeView.ad_communal_banner != null && !qTypeView.ad_communal_banner.isTurning()) {
                qTypeView.ad_communal_banner.setCanScroll(true);
                qTypeView.ad_communal_banner.startTurning(getShowNumber(adBeanList.get(0).getShowTime()) * 1000);
                qTypeView.ad_communal_banner.setPointViewVisible(true);
            }
        } else if (STOP_AUTO_PAGE_TURN.equals(message.type)) {
            if (qTypeView.ad_communal_banner != null && !qTypeView.ad_communal_banner.isTurning()) {
                qTypeView.ad_communal_banner.setCanScroll(false);
                qTypeView.ad_communal_banner.stopTurning();
                qTypeView.ad_communal_banner.setPointViewVisible(false);
            }
        }
    }

    private void getQualityTypePro() {
        if (NetWorkUtils.checkNet(getActivity())) {
            String url = Url.BASE_URL + Url.Q_PRODUCT_TYPE_LIST;
            Map<String, Object> params = new HashMap<>();
            params.put("currentPage", page);
            params.put("showCount", TOTAL_COUNT_TWENTY);
            if (!TextUtils.isEmpty(qualityTypeBeanChange.getChildCategory())) {
                params.put("id", qualityTypeBeanChange.getChildCategory());
                params.put("pid", qualityTypeBeanChange.getId());
            } else {
                params.put("id", qualityTypeBeanChange.getId());
                params.put("pid", 0);
            }
            params.put("orderTypeId", "1");
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    if (loadHud != null) {
                        loadHud.dismiss();
                    }
                    smart_communal_refresh.finishRefresh();
                    qualityTypeProductAdapter.loadMoreComplete();
                    communal_load.setVisibility(View.GONE);
                    communal_error.setVisibility(View.GONE);
                    if (page == 1) {
                        typeDetails.clear();
                    }
                    Gson gson = new Gson();
                    UserLikedProductEntity typeBean = gson.fromJson(result, UserLikedProductEntity.class);
                    if (typeBean != null) {
                        if (typeBean.getCode().equals("01")) {
                            typeDetails.addAll(typeBean.getLikedProductBeanList());
                        } else if (typeBean.getCode().equals("02")) {
                            qualityTypeProductAdapter.loadMoreEnd();
                        } else {
                            showToast(getActivity(), typeBean.getMsg());
                        }
                        if (page == 1) {
                            qualityTypeProductAdapter.setNewData(typeDetails);
                        } else {
                            qualityTypeProductAdapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    smart_communal_refresh.finishRefresh();
                    qualityTypeProductAdapter.loadMoreComplete();
                    if (loadHud != null) {
                        loadHud.dismiss();
                    }
                    if (page == 1 && qualityTypeProductAdapter.getItemCount() < 1) {
                        communal_load.setVisibility(View.GONE);
                        communal_error.setVisibility(View.VISIBLE);
                    } else {
                        showToast(getActivity(), R.string.invalidData);
                    }
                    super.onError(ex, isOnCallback);
                }
            });
        } else {
            if (loadHud != null) {
                loadHud.dismiss();
            }
            smart_communal_refresh.finishRefresh();
            qualityTypeProductAdapter.loadMoreComplete();
            if (page == 1) {
                communal_load.setVisibility(View.GONE);
                communal_error.setVisibility(View.VISIBLE);
            }
            showToast(getActivity(), R.string.unConnectedNetwork);
        }
    }

    class QTypeView {
        @Nullable
        @BindView(R.id.ad_communal_banner)
        ConvenientBanner ad_communal_banner;
        @Nullable
        @BindView(R.id.communal_recycler_wrap)
        RecyclerView communal_recycler_wrap;

        public void initViews() {
            if (communal_recycler_wrap != null) {
                communal_recycler_wrap.setBackgroundColor(getResources().getColor(R.color.gray_bg));
                communal_recycler_wrap.setPadding((int) (AutoUtils.getPercentWidth1px() * 20), 0, 0, (int) (AutoUtils.getPercentWidth1px() * 20));
                // 这一步必须要做,否则不会显示.
                communal_recycler_wrap.setLayoutManager(new GridLayoutManager(getActivity(), 4));
                childProductTypeAdapter = new ChildProductTypeAdapter(childTypeList);
                communal_recycler_wrap.setAdapter(childProductTypeAdapter);
                childProductTypeAdapter.setOnItemClickListener((adapter, view, position) -> {
                    QualityTypeBean qualityTypeBean = (QualityTypeBean) view.getTag();
                    if (qualityTypeBean != null && !qualityTypeBean.isSelect()) {

//                        修改商品子分类
                        setChildProductType(qualityTypeBean);
                        if (loadHud != null) {
                            loadHud.show();
                        }
                        page = 1;
                        getQualityTypePro();
                    }
                });
            }
        }
    }

    /**
     * 获取子类类型
     */
    private void getChildrenType() {
        if (NetWorkUtils.checkNet(getActivity())) {
            String url = Url.BASE_URL + Url.Q_PRODUCT_TYPE;
            Map<String, Object> params = new HashMap<>();
            params.put("pid", qualityTypeBeanChange.getId());
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    childTypeList.clear();
                    Gson gson = new Gson();
                    QualityTypeEntity qualityTypeEntity = gson.fromJson(result, QualityTypeEntity.class);
                    if (qualityTypeEntity != null) {
                        if (qualityTypeEntity.getCode().equals("01")) {
                            if (qualityTypeEntity.getQualityTypeBeanList() != null) {
                                for (QualityTypeBean qualityTypeBean : qualityTypeEntity.getQualityTypeBeanList()) {
                                    qualityTypeBean.setChildCategory(String.valueOf(qualityTypeBean.getId()));
                                    qualityTypeBean.setChildName(getStrings(qualityTypeBean.getName()));
                                }
                                childTypeList.addAll(qualityTypeEntity.getQualityTypeBeanList());
                                if (childTypeList.size() > 0 && !TextUtils.isEmpty(qualityTypeBeanChange.getChildCategory())) {
                                    for (QualityTypeBean qualityTypeBean : childTypeList) {
                                        if (qualityTypeBeanChange.getChildCategory().equals(qualityTypeBean.getChildCategory())) {
                                            qualityTypeBean.setSelect(true);
                                            qualityTypeBeanChange.setChildName(getStrings(qualityTypeBean.getChildName()));
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        if (childTypeList.size() > 0) {
                            if (childTypeHeaderView != null) {
                                if (childTypeHeaderView.getParent() == null) {
                                    qualityTypeProductAdapter.addHeaderView(childTypeHeaderView);
                                    scrollHeader();
                                }
                            }
                        } else {
                            if (childTypeHeaderView != null) {
                                qualityTypeProductAdapter.removeHeaderView(childTypeHeaderView);
                            }
                        }
                        childProductTypeAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    super.onError(ex, isOnCallback);
                    if (childTypeList.size() > 0) {
                        if (childTypeHeaderView != null) {
                            if (childTypeHeaderView.getParent() == null) {
                                qualityTypeProductAdapter.addHeaderView(childTypeHeaderView);
                                scrollHeader();
                            }
                        }
                    } else {
                        if (childTypeHeaderView != null) {
                            qualityTypeProductAdapter.removeHeaderView(childTypeHeaderView);
                        }
                    }
                }
            });
        }
    }

    @Override
    protected void getReqParams(Bundle bundle) {
        if (bundle != null) {
            qualityTypeBeanChange = new QualityTypeBean();
            qualityTypeBeanChange.setChildCategory(getStrings(bundle.getString(CATEGORY_CHILD)));
            qualityTypeBeanChange.setId(getIntegers(bundle.getString(CATEGORY_ID)));
            qualityTypeBeanChange.setType(getIntegers(bundle.getString(CATEGORY_TYPE)));
            qualityTypeBeanChange.setName(getStrings(bundle.getString(CATEGORY_NAME)));
        } else {
            return;
        }
    }

    /**
     * 设置子分类数据
     *
     * @param qualityTypeBean
     */
    private void setChildProductType(QualityTypeBean qualityTypeBean) {
        if (qualityTypeBeanChange != null) {
            qualityTypeBeanChange.setChildCategory(getStrings(qualityTypeBean.getChildCategory()));
            qualityTypeBeanChange.setChildName(qualityTypeBean.getChildName());
        }
        if (childTypeList.size() > 0) {
            for (QualityTypeBean qualityType : childTypeList) {
                if (qualityTypeBean.getChildCategory().equals(qualityType.getChildCategory())) {
                    qualityType.setSelect(true);
                } else {
                    qualityType.setSelect(false);
                }
            }
        }
        childProductTypeAdapter.notifyDataSetChanged();
    }

    private void scrollHeader() {
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) communal_recycler.getLayoutManager();
        int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
        int mVisibleCount = linearLayoutManager.findLastVisibleItemPosition()
                - linearLayoutManager.findFirstVisibleItemPosition() + 1;
        if (firstVisibleItemPosition > mVisibleCount) {
            communal_recycler.scrollToPosition(mVisibleCount);
        }
        communal_recycler.smoothScrollToPosition(0);
    }

    @OnClick({R.id.rel_communal_error, R.id.communal_empty})
    void refreshData(View view) {
        communal_load.setVisibility(View.VISIBLE);
        communal_error.setVisibility(View.GONE);
        page = 1;
        loadData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            if (totalPersonalTrajectory != null && qualityTypeBeanChange != null) {
                Map<String, String> map = new HashMap<>();
                map.put("categoryId", String.valueOf(qualityTypeBeanChange.getId()));
                totalPersonalTrajectory.stopTotal(map);
            }
        }
    }
}
