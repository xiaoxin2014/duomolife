package com.amkj.dmsh.dominant.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.bean.DMLThemeEntity;
import com.amkj.dmsh.bean.DMLThemeEntity.DMLThemeBean;
import com.amkj.dmsh.bean.DMLThemeEntity.DMLThemeBean.DMLGoodsBean;
import com.amkj.dmsh.bean.QualityTypeEntity.QualityTypeBean;
import com.amkj.dmsh.constant.BaseAddCarProInfoBean;
import com.amkj.dmsh.constant.CommunalAdHolderView;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.dominant.adapter.QualityOsMailHeaderAdapter;
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
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static com.amkj.dmsh.constant.ConstantMethod.getShowNumber;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.insertFragmentNewTotalData;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.CATEGORY_ID;
import static com.amkj.dmsh.constant.ConstantVariable.CATEGORY_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_TOTAL_COUNT;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.START_AUTO_PAGE_TURN;
import static com.amkj.dmsh.constant.ConstantVariable.STOP_AUTO_PAGE_TURN;
import static com.amkj.dmsh.dominant.fragment.QualityFragment.updateCarNum;

;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/6/29
 * class description:活动专区
 */

public class QualityOverseasMailFragment extends BaseFragment {
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
    //
    private List<LikedProductBean> typeDetails = new ArrayList();
    private QualityTypeProductAdapter qualityTypeProductAdapter;
    private QualityTypeBean qualityTypeBean;
    private int uid;
    private int themePage = 1;
    private int productPage = 1;
    private List<DMLThemeBean> themeList = new ArrayList();
    private List<CommunalADActivityBean> adBeanList = new ArrayList<>();
    //    头部
    private QualityOsMailHeaderAdapter qualityOsMailHeaderAdapter;
    private OverseasHeaderView overseasHeaderView;
    private String type = "overseas";
    private boolean isLoadThemeData = true;
    private boolean isLoadProData = true;
    private String categoryId;
    private String categoryType;
    private View headerView;
    private CBViewHolderCreator cbViewHolderCreator;

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_smart_refresh_recycler_float_loading;
    }

    @Override
    protected void initViews() {
        isLoginStatus();
        communal_recycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        headerView = LayoutInflater.from(getActivity())
                .inflate(R.layout.layout_overseas_mail_header, (ViewGroup) communal_recycler.getParent(), false);
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
                isLoadThemeData = true;
                isLoadProData = true;
                loadData();
        });
        qualityTypeProductAdapter = new QualityTypeProductAdapter(getActivity(), typeDetails);
        communal_recycler.setAdapter(qualityTypeProductAdapter);
        qualityTypeProductAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (isLoadProData || isLoadThemeData) {
                    if (isLoadProData && isLoadThemeData) {
                        themePage++;
                        productPage++;
                        getOverseasThemeData();
                        getOverseasProData();
                    } else if (isLoadThemeData) {
                        themePage++;
                        getOverseasThemeData();
                    } else {
                        productPage++;
                        getOverseasProData();
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
                    Intent intent = new Intent(getActivity(), ShopScrollDetailsActivity.class);
                    intent.putExtra("productId", String.valueOf(likedProductBean.getId()));
                    startActivity(intent);
                }
            }
        });
        overseasHeaderView.communal_recycler_wrap.setLayoutManager(new LinearLayoutManager(getActivity()));
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
        qualityOsMailHeaderAdapter = new QualityOsMailHeaderAdapter(getActivity(), themeList, type);
        overseasHeaderView.communal_recycler_wrap.setAdapter(qualityOsMailHeaderAdapter);
        communal_load.setVisibility(View.VISIBLE);
        totalPersonalTrajectory = insertFragmentNewTotalData(getActivity(),this.getClass().getSimpleName(),categoryId);
    }

    //    海外直邮商品列表
    private void getOverseasProData() {
        String url = Url.BASE_URL + Url.QUALITY_OVERSEAS_LIST;
        if (NetWorkUtils.checkNet(getActivity())) {
            Map<String, Object> params = new HashMap<>();
            params.put("showCount", DEFAULT_TOTAL_COUNT);
            params.put("currentPage", productPage);
            if(userId>0){
                params.put("uid",userId);
            }
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
                            if (productPage == 1) {
                                qualityTypeProductAdapter.setNewData(typeDetails);
                            } else {
                                qualityTypeProductAdapter.notifyDataSetChanged();
                            }
                        } else if (likedProductEntity.getCode().equals("02")) {
                            isLoadProData = false;
                            qualityTypeProductAdapter.loadMoreEnd();
                        } else {
                            qualityTypeProductAdapter.loadMoreEnd();
                            showToast(getActivity(), likedProductEntity.getMsg());
                        }
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    qualityTypeProductAdapter.loadMoreComplete();
                    super.onError(ex, isOnCallback);
                }
            });
        }
    }

    //    海外直邮主题商品列表
    private void getOverseasThemeData() {
        String url = Url.BASE_URL + Url.QUALITY_OVERSEAS_THEME;
        if (NetWorkUtils.checkNet(getActivity())) {
            Map<String, Object> params = new HashMap<>();
            params.put("currentPage", themePage);
            params.put("showCount", DEFAULT_TOTAL_COUNT);
            params.put("goodsCurrentPage", 1);
            params.put("goodsShowCount", 8);
            if(userId>0){
                params.put("uid",userId);
            }
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
                            for (int i = 0; i < dmlTheme.getThemeList().size(); i++) {
                                DMLThemeBean dmlThemeBean = dmlTheme.getThemeList().get(i);
                                List<DMLGoodsBean> dmlGoodsBeanList = dmlThemeBean.getGoods();
                                if (dmlGoodsBeanList != null && dmlGoodsBeanList.size() > 7) {
                                    DMLGoodsBean dmlGoodsBean = new DMLGoodsBean();
                                    dmlGoodsBean.setItemType(ConstantVariable.TYPE_1);
                                    dmlGoodsBean.setId(dmlThemeBean.getId());
                                    dmlGoodsBeanList.add(dmlGoodsBean);
                                    dmlThemeBean.setGoods(dmlGoodsBeanList);
                                }
                                themeList.add(dmlThemeBean);
                            }
                            if (themePage == 1) {
                                qualityOsMailHeaderAdapter.setNewData(themeList);
                            } else {
                                qualityOsMailHeaderAdapter.notifyDataSetChanged();
                            }
                        } else if (dmlTheme.getCode().equals("02")) {
                            isLoadThemeData = false;
                            if (themePage == 1) {
                                overseasHeaderView.communal_recycler_wrap.setVisibility(View.GONE);
                            } else {
                                overseasHeaderView.communal_recycler_wrap.setVisibility(View.VISIBLE);
                            }
                        } else {
                            showToast(getActivity(), dmlTheme.getMsg());
                        }
                        addRemoveHeaderView(themeList);
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
                    addRemoveHeaderView(themeList);
                    showToast(getActivity(), R.string.invalidData);
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
                showToast(getActivity(), R.string.unConnectedNetwork);
            }
        }
    }

    /**
     * 新增移除头部布局
     */
    private void addRemoveHeaderView(List list) {
        if (list.size() > 0) {
            if (headerView != null) {
                if (headerView.getParent() == null) {
                    qualityTypeProductAdapter.addHeaderView(headerView);
                }
            }
        } else {
            if (headerView != null && headerView.getParent() == null) {
                qualityTypeProductAdapter.removeHeaderView(headerView);
            }
        }
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
            getOverseasProData();
        }
    }

    @Override
    protected void loadData() {
        getOverseasThemeData();
        getOverseasProData();
        getAdTypeData();
    }

    private void getAdTypeData() {
        String url = Url.BASE_URL + Url.Q_QUALITY_TYPE_AD;
        if (NetWorkUtils.checkNet(getActivity())) {
            Map<String, Object> params = new HashMap<>();
            params.put("categoryType", categoryType);
            params.put("categoryId", categoryId);
            params.put("vidoShow","1");
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
                            overseasHeaderView.rel_communal_banner.setVisibility(View.VISIBLE);
                            if(cbViewHolderCreator ==null){
                                cbViewHolderCreator = new CBViewHolderCreator() {
                                    @Override
                                    public Holder createHolder(View itemView) {
                                        return new CommunalAdHolderView(itemView,getContext(),true);
                                    }

                                    @Override
                                    public int getLayoutId() {
                                        return R.layout.layout_ad_image_video;
                                    }
                                };
                            }
                            overseasHeaderView.ad_communal_banner.setPages(getActivity(),cbViewHolderCreator,adBeanList).setCanLoop(true)
                                    .setPointViewVisible(true).setCanScroll(true).setPageIndicator(new int[]{R.drawable.unselected_radius,R.drawable.selected_radius})
                                    .startTurning(getShowNumber(adBeanList.get(0).getShowTime()) * 1000);
                        } else {
                            if (adBeanList.size() < 1) {
                                overseasHeaderView.rel_communal_banner.setVisibility(View.GONE);
                            }
                        }
                        addRemoveHeaderView(adBeanList);
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    smart_communal_refresh.finishRefresh();
                    addRemoveHeaderView(adBeanList);
                    super.onError(ex, isOnCallback);
                }
            });
        }
    }

    @Override
    protected void getReqParams(Bundle bundle) {
        categoryId = bundle.getString(CATEGORY_ID);
        categoryType = bundle.getString(CATEGORY_TYPE);
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
        @BindView(R.id.rel_communal_banner)
        public RelativeLayout rel_communal_banner;
        @BindView(R.id.ad_communal_banner)
        public ConvenientBanner ad_communal_banner;
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if(START_AUTO_PAGE_TURN.equals(message.type)){
            if(adBeanList.size()>0&&overseasHeaderView.ad_communal_banner!=null&&!overseasHeaderView.ad_communal_banner.isTurning()){
                overseasHeaderView.ad_communal_banner.setCanScroll(true);
                overseasHeaderView.ad_communal_banner.startTurning(getShowNumber(adBeanList.get(0).getShowTime()) * 1000);
                overseasHeaderView.ad_communal_banner.setPointViewVisible(true);
            }
        }else if(STOP_AUTO_PAGE_TURN.equals(message.type)){
            if(overseasHeaderView.ad_communal_banner!=null&&overseasHeaderView.ad_communal_banner.isTurning()){
                overseasHeaderView.ad_communal_banner.setCanScroll(false);
                overseasHeaderView.ad_communal_banner.stopTurning();
                overseasHeaderView.ad_communal_banner.setPointViewVisible(false);
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            if (totalPersonalTrajectory != null) {
                Map<String,String> map = new HashMap<>();
                map.put("categoryId",categoryId);
                totalPersonalTrajectory.stopTotal(map);
            }
        }
    }
}
