package com.amkj.dmsh.dominant.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.bean.CommunalUserInfoEntity;
import com.amkj.dmsh.bean.HomeQualityFloatAdEntity;
import com.amkj.dmsh.bean.QualityTypeEntity;
import com.amkj.dmsh.bean.QualityTypeEntity.QualityTypeBean;
import com.amkj.dmsh.bean.QualityTypeEntity.QualityTypeBean.ChildCategoryListBean;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.dominant.activity.QualityTypeProductActivity;
import com.amkj.dmsh.dominant.adapter.QualityPageAdapter;
import com.amkj.dmsh.dominant.adapter.QualityProductTypeAdapter;
import com.amkj.dmsh.dominant.adapter.QualityProductTypeSpecificAdapter;
import com.amkj.dmsh.homepage.activity.HomePageSearchActivity;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity.CommunalADActivityBean;
import com.amkj.dmsh.mine.activity.ShopCarActivity;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.inteface.MyCacheCallBack;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.amkj.dmsh.views.SystemBarHelper;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.tablayout.SlidingTabLayout;
import com.google.gson.Gson;

import org.xutils.ex.HttpException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;
import q.rorbin.badgeview.Badge;

import static android.app.Activity.RESULT_OK;
import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getBadge;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.CATEGORY_CHILD;
import static com.amkj.dmsh.constant.ConstantVariable.CATEGORY_ID;
import static com.amkj.dmsh.constant.ConstantVariable.CATEGORY_NAME;
import static com.amkj.dmsh.constant.ConstantVariable.CATEGORY_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.SEARCH_ALL;
import static com.amkj.dmsh.constant.ConstantVariable.SEARCH_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_1;

;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/6/5
 * class description:良品首页
 */
public class QualityFragment extends BaseFragment {
    @BindView(R.id.dl_quality)
    public DrawerLayout dl_quality;
    @BindView(R.id.rel_quality_header)
    public RelativeLayout rel_quality_header;
    //    主视图
    @BindView(R.id.std_quality_type)
    public SlidingTabLayout std_quality_type;
    //    展示分类
    @BindView(R.id.vp_quality)
    public ViewPager vp_quality;
    //    侧滑视图
    @BindView(R.id.ll_drawer_quality)
    public LinearLayout ll_drawer_quality;
    //    侧边具体分类
    @BindView(R.id.rv_quality_product_type_specific)
    public RecyclerView rv_quality_product_type_specific;
    //    侧边大分类
    @BindView(R.id.rv_quality_product_type)
    public RecyclerView rv_quality_product_type;
    //    良品购物车
    @BindView(R.id.iv_quality_shop_car)
    public ImageView iv_quality_shop_car;
    @BindView(R.id.fl_shop_car_quality)
    public FrameLayout fl_shop_car_quality;
    @BindView(R.id.iv_float_ad_icon)
    ImageView iv_float_ad_icon;
    //良品侧边栏具体分类
    private List<QualityTypeBean> qualityTypeSpecificBeanList = new ArrayList();
    //    良品侧边栏分类
    private List<QualityTypeBean> qualityTypeBeanList = new ArrayList();
    //良品分类
    private List<String> qualityTypeList = new ArrayList();
    private QualityProductTypeSpecificAdapter productTypeSpecificAdapter;
    private QualityProductTypeAdapter productTypeAdapter;
    private Badge badge;
    private int uid;
    public final static String updateCarNum = "updateCarNum";
//    当前点击时间
    private long clickNowTime;
//    上一次点击时间
    private long clickOldTime;
    private boolean isClickSelect;

    @Override
    protected int getContentView() {
        return R.layout.fragment_quality_new;
    }

    @Override
    protected void initViews() {
        isLoginStatus();
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
        rv_quality_product_type_specific.setLayoutManager(manager);
        productTypeSpecificAdapter = new QualityProductTypeSpecificAdapter(getActivity(), qualityTypeSpecificBeanList);
        rv_quality_product_type_specific.setAdapter(productTypeSpecificAdapter);
        productTypeSpecificAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                return qualityTypeSpecificBeanList.get(position).getItemType() == TYPE_1 ? gridLayoutManager.getSpanCount() : 1;
            }
        });
        productTypeSpecificAdapter.setOnItemClickListener((adapter, view, position) -> {
            QualityTypeBean qualityTypeBean = (QualityTypeBean) view.getTag();
            if (qualityTypeBean != null) {
                Intent intent = new Intent(getActivity(), QualityTypeProductActivity.class);
                if (qualityTypeBean.getPid() > 0) {
                    intent.putExtra(CATEGORY_ID, String.valueOf(qualityTypeBean.getPid()));
                    intent.putExtra(CATEGORY_CHILD, String.valueOf(qualityTypeBean.getId()));
                    intent.putExtra(CATEGORY_NAME,getStrings(qualityTypeBean.getpName()));
                } else {
                    intent.putExtra(CATEGORY_ID, String.valueOf(qualityTypeBean.getId()));
                    intent.putExtra(CATEGORY_NAME,getStrings(qualityTypeBean.getName()));
                }
                intent.putExtra(CATEGORY_TYPE, String.valueOf(qualityTypeBean.getType()));
                startActivity(intent);
                dl_quality.closeDrawer(ll_drawer_quality);
            }
        });
        rv_quality_product_type_specific.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!isClickSelect){
                    GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();//可见范围内的第一项的位置
                    QualityTypeBean firstTypeBean = qualityTypeSpecificBeanList.get(firstVisibleItemPosition);
                    if (firstTypeBean.getItemType() == TYPE_1 && !firstTypeBean.isSelect()) {
                        scrollTypeVisibility(firstVisibleItemPosition);
                        for (QualityTypeBean qualityTypeBean : qualityTypeBeanList) {
                            if (qualityTypeBean.getId() == firstTypeBean.getId()) {
                                qualityTypeBean.setSelect(true);
                            } else {
                                qualityTypeBean.setSelect(false);
                            }
                        }
                        productTypeAdapter.notifyDataSetChanged();
                    }
                }else{
                    isClickSelect = false;
                }
            }
        });
//        良品侧边栏分类
        rv_quality_product_type.setLayoutManager(new LinearLayoutManager(getContext()));
        productTypeAdapter = new QualityProductTypeAdapter(qualityTypeBeanList);
        rv_quality_product_type.setAdapter(productTypeAdapter);
        productTypeAdapter.setOnItemClickListener((adapter, view, position) -> {
            QualityTypeBean qualityTypeBean = (QualityTypeBean) view.getTag();
            if (qualityTypeBean != null) {
                for (int i = 0; i < qualityTypeSpecificBeanList.size(); i++) {
                    QualityTypeBean qualityTypeSpecific = qualityTypeSpecificBeanList.get(i);
                    if (qualityTypeBean.getId() == qualityTypeSpecific.getId()) {
                        GridLayoutManager layoutManager = (GridLayoutManager) rv_quality_product_type_specific.getLayoutManager();
                        layoutManager.scrollToPositionWithOffset(i, 0);
                        break;
                    }
                }
                for (int i = 0; i < qualityTypeBeanList.size(); i++) {
                    QualityTypeBean qualityType = qualityTypeBeanList.get(i);
                    if (qualityTypeBean.getId() == qualityType.getId()) {
                        qualityType.setSelect(true);
                        isClickSelect = true;
                        scrollTypeVisibility(i);
                    } else {
                        qualityType.setSelect(false);
                    }
                }
                productTypeAdapter.notifyDataSetChanged();
            }
        });
        std_quality_type.setTextsize(AutoSizeUtils.mm2px(mAppContext,28));
        std_quality_type.setTabPadding(AutoSizeUtils.mm2px(mAppContext,30));
        setStatusColor();
        badge = getBadge(getActivity(), fl_shop_car_quality);
    }

    private void scrollTypeVisibility(int position) {
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) rv_quality_product_type.getLayoutManager();
        linearLayoutManager.scrollToPositionWithOffset(position,0);
        linearLayoutManager.setStackFromEnd(true);
    }

    private void setStatusColor() {
        SystemBarHelper.setPadding(getActivity(), rel_quality_header);
        SystemBarHelper.setPadding(getActivity(), ll_drawer_quality);
        SystemBarHelper.immersiveStatusBar(getActivity());
    }

    private void isLoginStatus() {
        SavePersonalInfoBean personalInfo = ConstantMethod.getPersonalInfo(getActivity());
        if (personalInfo.isLogin()) {
            uid = personalInfo.getUid();
        } else {
            uid = 0;
        }
    }

    @Override
    protected void loadData() {
        getQualityHorType();
        getQualitySideType();
        getCarCount();
        //        浮窗广告
        getFloatAd();
    }

    private void getFloatAd() {
        if (NetWorkUtils.checkNet(getActivity())) {
            String url = Url.BASE_URL + Url.H_Q_FLOAT_AD;
            XUtil.Get(url, null, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    HomeQualityFloatAdEntity floatAdEntity = gson.fromJson(result, HomeQualityFloatAdEntity.class);
                    if (floatAdEntity != null) {
                        if (floatAdEntity.getCode().equals("01")) {
                            if (floatAdEntity.getCommunalADActivityBean() != null) {
                                iv_float_ad_icon.setVisibility(View.VISIBLE);
                                GlideImageLoaderUtil.loadFitCenter(getActivity(), iv_float_ad_icon,
                                        getStrings(floatAdEntity.getCommunalADActivityBean().getPicUrl()));
                                iv_float_ad_icon.setTag(R.id.iv_tag, floatAdEntity.getCommunalADActivityBean());
                            }
                        } else {
                            iv_float_ad_icon.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    iv_float_ad_icon.setVisibility(View.GONE);
                    super.onError(ex, isOnCallback);
                }
            });
        }
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
                            showToast(getActivity(), requestStatus.getMsg());
                        }
                    }
                }
            });
        }
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (message.type.equals(updateCarNum)) {
            getCarCount();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        clickNowTime = System.currentTimeMillis();
        try {
            if(clickNowTime-clickOldTime<300&& vp_quality!=null&&vp_quality.getChildCount() > 0
                    && std_quality_type.getTabCount() > 0){
                std_quality_type.setCurrentTab(0);
                vp_quality.setCurrentItem(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        clickOldTime = clickNowTime;
        getCarCount();
    }


    private void getQualityHorType() {
        String url = Url.BASE_URL + Url.QUALITY_SHOP_HOR_TYPE;
        if (NetWorkUtils.isConnectedByState(getActivity())) {
            XUtil.GetCache(url, 0, null, new MyCacheCallBack<String>() {
                private boolean hasError = false;
                private String result = null;
                private boolean hasCache = false;

                @Override
                public boolean onCache(String result) { //得到缓存数据, 缓存过期后不会进入
                    this.result = result;
                    hasCache = true;
                    getTypeHorDataJson(result);
//                判断当前网络是否连接
                    return !NetWorkUtils.isConnectedByState(getActivity()); //true: 信任缓存数据, 不再发起网络请求; false不信任缓存数据
                }

                @Override
                public void onSuccess(String result) {
                    //如果服务返回304或onCache选择了信任缓存,这时result为null
                    if (result != null) {
                        this.result = result;
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    hasError = true;
//                    showToast(x.app(), ex.getMessage());
                    if (ex instanceof HttpException) { //网络错误
                        HttpException httpEx = (HttpException) ex;
                        int responseCode = httpEx.getCode();
                        String responseMsg = httpEx.getMessage();
                        String errorResult = httpEx.getResult();
                        //...
                    } else { //其他错误
                        //...
                    }
                    if (hasCache) {
                        showToast(getActivity(), R.string.unConnectedNetwork);
                    }
                }

                @Override
                public void onCancelled(CancelledException cex) {
                }

                @Override
                public void onFinished() {
                    if (!hasError && result != null) {
                        //成功获取数据
//                    showToast(x.app(), result);
                        getTypeHorDataJson(result);
                    }
                }
            });
        } else {
            XUtil.GetCache(url, 2, null, new MyCacheCallBack<String>() {
                private boolean hasError = false;
                private String result = null;
                private boolean hasCache = false;

                @Override
                public boolean onCache(String result) { //得到缓存数据, 缓存过期后不会进入
                    this.result = result;
                    getTypeHorDataJson(result);
//                判断当前网络是否连接
                    return !NetWorkUtils.isConnectedByState(getActivity()); //true: 信任缓存数据, 不再发起网络请求; false不信任缓存数据
                }

                @Override
                public void onSuccess(String result) {
                    //如果服务返回304或onCache选择了信任缓存,这时result为null
                    if (result != null) {
                        this.result = result;
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    hasError = true;
//                    showToast(x.app(), ex.getMessage());
                    if (ex instanceof HttpException) { //网络错误
                        HttpException httpEx = (HttpException) ex;
                        int responseCode = httpEx.getCode();
                        String responseMsg = httpEx.getMessage();
                        String errorResult = httpEx.getResult();
                        //...
                    } else { //其他错误
                        //...
                    }
                }

                @Override
                public void onCancelled(CancelledException cex) {
                }

                @Override
                public void onFinished() {
                    if (!hasError && result != null) {
                        //成功获取数据
//                    showToast(x.app(), result);
                        getTypeHorDataJson(result);
                    }
                }
            });
        }
    }

    private void getTypeHorDataJson(String result) {
        Gson gson = new Gson();
        QualityTypeEntity typeBean = gson.fromJson(result, QualityTypeEntity.class);
        if (typeBean != null) {
            if (typeBean.getCode().equals("01")) {
                setSlideData(typeBean.getQualityTypeBeanList());
            } else {
                showToast(getActivity(), typeBean.getMsg());
            }
        }
    }

    private void getQualitySideType() {
        String url = Url.BASE_URL + Url.QUALITY_SHOP_TYPE;
        if (NetWorkUtils.isConnectedByState(getActivity())) {
            XUtil.GetCache(url, 0, null, new MyCacheCallBack<String>() {
                private boolean hasError = false;
                private String result = null;
                private boolean hasCache = false;

                @Override
                public boolean onCache(String result) { //得到缓存数据, 缓存过期后不会进入
                    this.result = result;
                    hasCache = true;
                    getTypeDataJson(result);
//                判断当前网络是否连接
                    return !NetWorkUtils.isConnectedByState(getActivity()); //true: 信任缓存数据, 不再发起网络请求; false不信任缓存数据
                }

                @Override
                public void onSuccess(String result) {
                    //如果服务返回304或onCache选择了信任缓存,这时result为null
                    if (result != null) {
                        this.result = result;
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    hasError = true;
//                    showToast(x.app(), ex.getMessage());
                    if (ex instanceof HttpException) { //网络错误
                        HttpException httpEx = (HttpException) ex;
                        int responseCode = httpEx.getCode();
                        String responseMsg = httpEx.getMessage();
                        String errorResult = httpEx.getResult();
                        //...
                    } else { //其他错误
                        //...
                    }
                    if (hasCache) {
                        showToast(getActivity(), R.string.unConnectedNetwork);
                    }
                }

                @Override
                public void onCancelled(CancelledException cex) {
                }

                @Override
                public void onFinished() {
                    if (!hasError && result != null) {
                        //成功获取数据
//                    showToast(x.app(), result);
                        getTypeDataJson(result);
                    }
                }
            });
        } else {
            XUtil.GetCache(url, 2, null, new MyCacheCallBack<String>() {
                private boolean hasError = false;
                private String result = null;
                private boolean hasCache = false;

                @Override
                public boolean onCache(String result) { //得到缓存数据, 缓存过期后不会进入
                    this.result = result;
                    getTypeDataJson(result);
//                判断当前网络是否连接
                    return !NetWorkUtils.isConnectedByState(getActivity()); //true: 信任缓存数据, 不再发起网络请求; false不信任缓存数据
                }

                @Override
                public void onSuccess(String result) {
                    //如果服务返回304或onCache选择了信任缓存,这时result为null
                    if (result != null) {
                        this.result = result;
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    hasError = true;
//                    showToast(x.app(), ex.getMessage());
                    if (ex instanceof HttpException) { //网络错误
                        HttpException httpEx = (HttpException) ex;
                        int responseCode = httpEx.getCode();
                        String responseMsg = httpEx.getMessage();
                        String errorResult = httpEx.getResult();
                        //...
                    } else { //其他错误
                        //...
                    }
                }

                @Override
                public void onCancelled(CancelledException cex) {
                }

                @Override
                public void onFinished() {
                    if (!hasError && result != null) {
                        //成功获取数据
//                    showToast(x.app(), result);
                        getTypeDataJson(result);
                    }
                }
            });
        }
    }

    private void getTypeDataJson(String result) {
        Gson gson = new Gson();
        QualityTypeEntity typeBean = gson.fromJson(result, QualityTypeEntity.class);
        qualityTypeSpecificBeanList.clear();
        qualityTypeBeanList.clear();
        if (typeBean != null) {
            if (typeBean.getCode().equals("01")) {
                qualityTypeBeanList.addAll(typeBean.getQualityTypeBeanList());
                if (qualityTypeBeanList.size() > 0) {
                    QualityTypeBean qualityTypeBean = qualityTypeBeanList.get(0);
                    qualityTypeBean.setSelect(true);
                }
                for (QualityTypeBean qualityTypeBean : qualityTypeBeanList) {
                    qualityTypeBean.setItemType(TYPE_1);
                    qualityTypeSpecificBeanList.add(qualityTypeBean);
                    if (qualityTypeBean.getChildCategoryList() == null) {
                        QualityTypeBean qualityType = new QualityTypeBean();
                        qualityType.setId(qualityTypeBean.getId());
                        qualityType.setName(qualityTypeBean.getName());
                        qualityType.setPicUrl(qualityTypeBean.getPicUrl());
                        qualityType.setType(qualityTypeBean.getType());
                        qualityTypeSpecificBeanList.add(qualityType);
                    } else {
                        for (ChildCategoryListBean childCategoryListBean : qualityTypeBean.getChildCategoryList()) {
                            QualityTypeBean qualityType = new QualityTypeBean();
                            qualityType.setPicUrl(getStrings(childCategoryListBean.getPicUrl()));
                            qualityType.setPid(childCategoryListBean.getPid());
                            qualityType.setId(childCategoryListBean.getId());
                            qualityType.setName(getStrings(childCategoryListBean.getName()));
                            qualityType.setType(childCategoryListBean.getType());
                            qualityType.setpName(getStrings(qualityTypeBean.getName()));
                            qualityTypeSpecificBeanList.add(qualityType);
                        }
                    }
                }
                productTypeSpecificAdapter.notifyDataSetChanged();
                productTypeAdapter.notifyDataSetChanged();
            } else if (typeBean.getCode().equals("02")) {
                showToast(getActivity(), R.string.invalidData);
            } else {
                showToast(getActivity(), typeBean.getMsg());
            }
        }
    }

    private void setSlideData(List<QualityTypeBean> qualityTypeBeanList) {
        if (qualityTypeBeanList.size() > 0) {
            qualityTypeList.clear();
            for (int i = 0; i < qualityTypeBeanList.size(); i++) {
                qualityTypeList.add(qualityTypeBeanList.get(i).getName());
            }
            QualityPageAdapter qualityPageAdapter = new QualityPageAdapter(this.getChildFragmentManager(), qualityTypeBeanList);
            vp_quality.setAdapter(qualityPageAdapter);
            vp_quality.setOffscreenPageLimit(qualityTypeBeanList.size() - 1);
            std_quality_type.setViewPager(vp_quality, qualityTypeList.toArray(new String[qualityTypeList.size()]));
            vp_quality.setCurrentItem(0);
        }
    }


    @OnClick(R.id.iv_quality_type)
    void drawerLayoutMenu(View view) {
        if (dl_quality.isDrawerOpen(ll_drawer_quality)) {
            dl_quality.closeDrawer(ll_drawer_quality);
        } else {
            dl_quality.openDrawer(ll_drawer_quality);
        }
    }

    @OnClick(R.id.tv_quality_search)
    void goSearch(View view) {
        Intent intent = new Intent(getActivity(), HomePageSearchActivity.class);
        intent.putExtra(SEARCH_TYPE, SEARCH_ALL);
        startActivity(intent);
    }

    @OnClick(R.id.iv_quality_shop_car)
    void skipShopCar(View view) {
        Intent intent = new Intent(getActivity(), ShopCarActivity.class);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == ConstantVariable.IS_LOGIN_CODE) {
            Bundle bundle = data.getExtras();
            CommunalUserInfoEntity loginAccount = (CommunalUserInfoEntity) bundle.get("AccountInf");
            uid = loginAccount.getCommunalUserInfoBean().getUid();
            getCarCount();
        }
    }

    @OnClick(R.id.iv_float_ad_icon)
    void floatAdSkip(View view) {
        CommunalADActivityBean communalADActivityBean = (CommunalADActivityBean) view.getTag(R.id.iv_tag);
        if (communalADActivityBean != null) {
            ConstantMethod.adClickTotal(communalADActivityBean.getObjectId());
            ConstantMethod.setSkipPath(getActivity(), getStrings(communalADActivityBean.getAndroidLink()), false);
        }
    }
}
