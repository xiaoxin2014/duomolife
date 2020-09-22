package com.amkj.dmsh.homepage.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.bean.AllSearchEntity;
import com.amkj.dmsh.bean.AllSearchEntity.AllSearchBean.WatchwordBean;
import com.amkj.dmsh.bean.CouponEntity.CouponListEntity;
import com.amkj.dmsh.bean.HotSearchTagEntity;
import com.amkj.dmsh.bean.HotSearchTagEntity.HotSearchTagBean;
import com.amkj.dmsh.bean.QualityTypeEntity;
import com.amkj.dmsh.dominant.activity.QualityTypeProductActivity;
import com.amkj.dmsh.find.bean.EventMessageBean;
import com.amkj.dmsh.homepage.ListHistoryDataSave;
import com.amkj.dmsh.homepage.activity.AllSearchDetailsNewActivity;
import com.amkj.dmsh.homepage.activity.SearchGoodProMoreActivity;
import com.amkj.dmsh.homepage.adapter.HotSearchAdapter;
import com.amkj.dmsh.views.alertdialog.AlertDialogCoupon;
import com.amkj.dmsh.netloadpage.NetEmptyCallback;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.RemoveExistUtils;
import com.amkj.dmsh.views.alertdialog.AlertDialogImage;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.webformatdata.CommunalWebDetailUtils;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.kingja.loadsir.core.Transport;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

import static android.view.View.VISIBLE;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStringChangeIntegers;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.isContextExisted;
import static com.amkj.dmsh.constant.ConstantMethod.setSkipPath;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.CATEGORY_CHILD;
import static com.amkj.dmsh.constant.ConstantVariable.CATEGORY_ID;
import static com.amkj.dmsh.constant.ConstantVariable.CATEGORY_NAME;
import static com.amkj.dmsh.constant.ConstantVariable.CATEGORY_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.COUPON;
import static com.amkj.dmsh.constant.ConstantVariable.COUPON_PACKAGE;
import static com.amkj.dmsh.constant.ConstantVariable.SEARCH_DATA;
import static com.amkj.dmsh.constant.ConstantVariable.SKIP_LINK;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.UPDATE_SEARCH_NUM;
import static com.amkj.dmsh.constant.Url.H_HOT_NEW_SEARCH_LIST;
import static com.amkj.dmsh.constant.Url.QUALITY_SHOP_TYPE;

/**
 * Created by xiaoxin on 2019/9/16
 * Version:v4.2.2
 * ClassDescription :搜索详情基类
 */
public abstract class BaseSearchDetailFragment extends BaseFragment {
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    @BindView(R.id.nested_scrollview)
    LinearLayout mNestedScrollview;
    //    历史搜索
    @BindView(R.id.ll_search_history)
    LinearLayout ll_search_history;
    @BindView(R.id.rv_history_search)
    RecyclerView rvHistorySearch;
    //    热门搜索
    @BindView(R.id.ll_search_hot)
    LinearLayout ll_search_hot;
    @BindView(R.id.rv_hot_search)
    RecyclerView rvHotSearch;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    FloatingActionButton download_btn_communal;

    protected List<HotSearchTagBean> hotSearchList = new ArrayList<>();
    protected List<HotSearchTagBean> historySearchList = new ArrayList<>();
    protected HotSearchAdapter hotSearchAdapter;
    protected HotSearchAdapter historySearchAdapter;
    protected String SAVE_NAME = "SearchHistory";
    protected boolean isFirst = true;
    protected boolean searchSucess;
    protected int page = 1;

    private String mTitle;
    private QualityTypeEntity.QualityTypeBean qualityTypeBean;

    protected AllSearchEntity allSearchEntity;
    protected AllSearchEntity.AllSearchBean searchBean;
    protected RemoveExistUtils removeExistUtils = new RemoveExistUtils();
    protected RemoveExistUtils removeExistTopicUtils = new RemoveExistUtils();
    private AlertDialogImage mSearchDialogImage;
    private AlertDialogImage mFailDialogImage;
    private AlertDialogCoupon mSucessDialogImage;


    @Override
    protected int getContentView() {
        return R.layout.fragment_commual_search_detail;
    }

    @Override
    protected void initViews() {
        smart_communal_refresh.setOnRefreshListener(refreshLayout -> {
            loadData();
        });
        setFloatingButton(download_btn_communal, communal_recycler);

        hotSearchAdapter = new HotSearchAdapter(hotSearchList);
        historySearchAdapter = new HotSearchAdapter(historySearchList);
        initKeywordRv(rvHotSearch, hotSearchAdapter);
        initKeywordRv(rvHistorySearch, historySearchAdapter);

        initRv();
    }

    private void initKeywordRv(RecyclerView recyclerView, HotSearchAdapter searchAdapter) {
        recyclerView.setNestedScrollingEnabled(false);
        FlexboxLayoutManager manager = new FlexboxLayoutManager(getActivity());
        manager.setFlexWrap(FlexWrap.WRAP);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(searchAdapter);
        searchAdapter.setOnItemClickListener((adapter, view, position) -> {
            HotSearchTagEntity.HotSearchTagBean hotSearchTagBean = (HotSearchTagEntity.HotSearchTagBean) view.getTag();
            if (hotSearchTagBean != null) {
                if (!TextUtils.isEmpty(hotSearchTagBean.getAndroid_link())) {
                    setSkipPath(getActivity(), hotSearchTagBean.getAndroid_link(), false);
                } else if (!TextUtils.isEmpty(hotSearchTagBean.getTag_name())) {
                    updateKeywords(hotSearchTagBean.getTag_name());
                    clickSearch();
                }
            }
        });
    }

    //初始化搜索结果列表
    protected abstract void initRv();

    protected abstract void getSearchDetail();

    @Override
    protected void loadData() {
        communal_recycler.scrollToPosition(0);
        page = 1;
        getHotSearch();
//        if (!searchSucess) {
//            NetLoadUtils.getNetInstance().showLoadSirLoading(loadService);
//        }
        getSearchDetail();
    }

    /**
     * 获取商品分类
     */
    private void getProductType() {
        if (qualityTypeBean == null) {
            NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), QUALITY_SHOP_TYPE, new NetLoadListenerHelper() {
                @Override
                public void onSuccess(String result) {
                    QualityTypeEntity qualityTypeEntity = GsonUtils.fromJson(result, QualityTypeEntity.class);
                    if (qualityTypeEntity != null && qualityTypeEntity.getCode().equals(SUCCESS_CODE)
                            && qualityTypeEntity.getQualityTypeBeanList() != null && qualityTypeEntity.getQualityTypeBeanList().size() > 0) {
                        qualityTypeBean = qualityTypeEntity.getQualityTypeBeanList().get(0);
                    }
                }
            });
        }
    }

    //商品搜索无结果回调
    protected void setEmptyCallback() {
        getProductType();
        loadService.setCallBack(NetEmptyCallback.class, new Transport() {
            @Override
            public void order(Context context, View view) {
                if ("SearchDetailsProductNewFragment".equals(getSimpleName())) {
                    TextView tvFilter = view.findViewById(R.id.tv_filter);
                    tvFilter.setVisibility(VISIBLE);
                    tvFilter.setOnClickListener(v -> {
                        Intent intent = new Intent();
                        if (qualityTypeBean != null) {
                            intent.setClass(getActivity(), QualityTypeProductActivity.class);
                            intent.putExtra(CATEGORY_ID, String.valueOf(qualityTypeBean.getId()));
                            intent.putExtra(CATEGORY_TYPE, String.valueOf(qualityTypeBean.getType()));
                            intent.putExtra(CATEGORY_NAME, getStrings(qualityTypeBean.getName()));
                            if (qualityTypeBean.getChildCategoryList() != null
                                    && qualityTypeBean.getChildCategoryList().size() > 0) {
                                intent.putExtra(CATEGORY_CHILD, String.valueOf(qualityTypeBean.getChildCategoryList().get(0).getId()));
                            }
                        } else {
                            intent.setClass(getActivity(), SearchGoodProMoreActivity.class);
                        }
                        startActivity(intent);
                    });
                }
            }
        });
    }


    //  清除历史记录
    @OnClick(R.id.tv_search_history_hint)
    void deleteHistoryData(View view) {
        ListHistoryDataSave listHistoryDataSave = new ListHistoryDataSave(getActivity(), SAVE_NAME);
        listHistoryDataSave.delDataList(getSearchKey());
        getHistoryData();//刷新历史搜索记录
    }

    //获取热门搜索
    private void getHotSearch() {
        if (!isFirst || !TextUtils.isEmpty(getKeywords())) return;
        Map<String, Object> map = new HashMap<>();
        map.put("searchType", getSearchType());
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), H_HOT_NEW_SEARCH_LIST, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                isFirst = false;
                hotSearchList.clear();

                HotSearchTagEntity hotSearchTagEntity = GsonUtils.fromJson(result, HotSearchTagEntity.class);
                if (hotSearchTagEntity != null) {
                    if (hotSearchTagEntity.getCode().equals(SUCCESS_CODE)) {
                        hotSearchList.addAll(hotSearchTagEntity.getHotSearchTagList());
                    }
                }
                hotSearchAdapter.notifyDataSetChanged();
                ll_search_hot.setVisibility(hotSearchList.size() > 0 ? View.VISIBLE : View.GONE);
                getHistoryData();
                NetLoadUtils.getNetInstance().showLoadSirSuccess(loadService);
            }

            @Override
            public void onNotNetOrException() {
                getHistoryData();
                NetLoadUtils.getNetInstance().showLoadSirSuccess(loadService);
                if (hotSearchList.size() < 1) {
                    ll_search_hot.setVisibility(View.GONE);
                }
            }
        });
    }


    //获取历史搜索
    private void getHistoryData() {
        historySearchList.clear();
        try {
            ListHistoryDataSave listHistoryDataSave = new ListHistoryDataSave(getActivity(), SAVE_NAME);
            List<String> dataList = listHistoryDataSave.getDataList(getSearchKey());
            if (dataList != null && dataList.size() > 0) {
                for (String keyword : dataList) {
                    historySearchList.add(new HotSearchTagBean(keyword));
                }
            }
            historySearchAdapter.notifyDataSetChanged();
            ll_search_history.setVisibility(historySearchList.size() > 0 ? View.VISIBLE : View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //点击搜索
    private void clickSearch() {
        page = 1;
        NetLoadUtils.getNetInstance().showLoadSirLoading(loadService);
        getSearchDetail();
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (message.type.equals(SEARCH_DATA)) {
            EventMessageBean eventMessageBean = (EventMessageBean) message.result;
            if (getSimpleName().equals(eventMessageBean.getSimpleName())) {
                clickSearch();
            }
        }
    }

    //获取关键字
    protected String getKeywords() {
        if (isContextExisted(getActivity())) {
            return ((AllSearchDetailsNewActivity) getActivity()).getKewords();
        }
        return "";
    }

    //更新关键字
    private void updateKeywords(String keywords) {
        if (isContextExisted(getActivity())) {
            ((AllSearchDetailsNewActivity) getActivity()).updateKewords(keywords);
        }
    }

    //刷新tab数量
    protected void updateSearchNum() {
        if (page == 1 && searchBean != null) {
            List<String> numList = new ArrayList<>();
            numList.add(searchBean.getGoodsCount());
            numList.add(searchBean.getDocumentCount());
            numList.add(searchBean.getTopicCount());
            numList.add(searchBean.getUserCount());
            EventBus.getDefault().post(new EventMessage(UPDATE_SEARCH_NUM, numList));
        }
    }

    //获取搜索类型
    protected int getSearchType() {
        if (getSimpleName().equals(SearchDetailsArticleFragment.class.getSimpleName())) {
            return 2;
        } else if (getSimpleName().equals(SearchDetailsTopicFragment.class.getSimpleName())) {
            return 3;
        } else if (getSimpleName().equals(SearchDetailsUserFragment.class.getSimpleName())) {
            return 4;
        } else {
            return 1;
        }
    }

    protected abstract String getSearchKey();

    //口令红包结果
    protected void setWordData(WatchwordBean watchwordBean) {
        if (watchwordBean != null) {
            String type = watchwordBean.getType();
            String imgUrl = watchwordBean.getImgUrl();
            switch (type) {
                case COUPON:
                case COUPON_PACKAGE:
                    if (mSearchDialogImage == null) {
                        mSearchDialogImage = new AlertDialogImage(getActivity());
                        mSearchDialogImage.hideCloseBtn();
                    }
                    mSearchDialogImage.setAlertClickListener(() -> {
                        openCoupon(getStringChangeIntegers(watchwordBean.getObjId()), type);
                    });

                    //如果有返回图片
                    if (!TextUtils.isEmpty(imgUrl)) {
                        if (isContextExisted(getActivity())) {
                            GlideImageLoaderUtil.setLoadImgFinishListener(getActivity(), imgUrl, new GlideImageLoaderUtil.ImageLoaderFinishListener() {
                                @Override
                                public void onSuccess(Bitmap bitmap) {
                                    mSearchDialogImage.setImage(bitmap);
                                    mSearchDialogImage.show();
                                }

                                @Override
                                public void onError() {
                                    mSearchDialogImage.setImageResource(R.drawable.red_packet);
                                    mSearchDialogImage.show();
                                }
                            });
                        }
                    } else {
                        //没有返回加载本地
                        mSearchDialogImage.setImageResource(R.drawable.red_packet);
                        mSearchDialogImage.show();
                    }

                    break;
                case SKIP_LINK:
                    setSkipPath(getActivity(), watchwordBean.getAndroidLink(), false);
                    break;
            }
        }
    }

    //点击打开优惠券(礼包)
    private void openCoupon(int couponId, String type) {
        if (couponId > 0) {
            if (userId > 0) {
                if (loadHud != null) {
                    loadHud.show();
                }
                if (COUPON.equals(type)) {
                    CommunalWebDetailUtils.getCommunalWebInstance().getDirectCoupon(getActivity(),
                            couponId, loadHud, getCouponListener);
                } else {
                    CommunalWebDetailUtils.getCommunalWebInstance().getDirectCouponPackage(getActivity(),
                            couponId, loadHud, getCouponListener);
                }
            } else {
                getLoginStatus(this);
            }
        }
    }

    //优惠券领取回调
    CommunalWebDetailUtils.GetCouponListener getCouponListener = new CommunalWebDetailUtils.GetCouponListener() {
        @Override
        public void onSuccess(CouponListEntity couponListEntity) {
            getCouponSuccess(couponListEntity);
        }

        @Override
        public void onFailure(CouponListEntity couponListEntity) {
            getCouponFailure(couponListEntity);
        }
    };


    //领取成功弹窗
    private void getCouponSuccess(CouponListEntity couponListEntity) {
        if (mSucessDialogImage == null) {
            mSucessDialogImage = new AlertDialogCoupon(getActivity());
            mSucessDialogImage.hideCloseBtn();
        }
        mSucessDialogImage.setCouponList(couponListEntity.getResult());
        mSucessDialogImage.show();
    }

    //领取失败弹窗
    private void getCouponFailure(CouponListEntity couponListEntity) {
        if (mFailDialogImage == null) {
            mFailDialogImage = new AlertDialogImage(getActivity());
            mFailDialogImage.hideCloseBtn();
            mFailDialogImage.setImageResource(R.drawable.get_coupon_fail);
        }
        mFailDialogImage.setDialogText(couponListEntity.getMsg());
        mFailDialogImage.show();
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    protected String getEmptyText() {
        switch (getClass().getSimpleName()) {
            case "SearchDetailsProductNewFragment":
                return "没有找到相关商品\n建议您换个搜索词试试";
            case "SearchDetailsArticleFragment":
                return "没有找到相关种草\n建议您换个搜索词试试";
            case "SearchDetailsTopicFragment":
                return "没有找到相关话题\n建议您换个搜索词试试";
            case "SearchDetailsUserFragment":
                return "没有找到相关用户\n建议您换个搜索词试试";
            default:
                return "暂无数据，稍后重试";
        }
    }

    @Override
    protected int getEmptyResId() {
        return R.drawable.search_detail;
    }

    @Override
    protected boolean isDataInitiated() {
        return false;
    }

    @Override
    protected void getReqParams(Bundle bundle) {
        super.getReqParams(bundle);
        if (bundle != null) {
            mTitle = (String) bundle.get("title");
        }
    }
}
