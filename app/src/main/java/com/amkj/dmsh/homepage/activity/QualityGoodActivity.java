package com.amkj.dmsh.homepage.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.constant.BaseAddCarProInfoBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.dominant.adapter.QualityGoodNewProAdapter;
import com.amkj.dmsh.dominant.bean.QualityGoodProductEntity;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity.CommunalADActivityBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean;
import com.amkj.dmsh.utils.RemoveExistUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.amkj.dmsh.utils.multitypejson.MultiTypeJsonParser;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.adClickTotal;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.insertFragmentNewTotalData;
import static com.amkj.dmsh.constant.ConstantMethod.setSkipPath;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.QUALITY_SHOP_GOODS_PRO;
import static com.amkj.dmsh.dominant.fragment.QualityFragment.updateCarNum;

/**
 * Created by xiaoxin on 2019/4/17 0017
 * Version:v4.0.0
 * ClassDescription :好物列表
 */
public class QualityGoodActivity extends BaseActivity {

    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.tv_life_back)
    TextView mTvLifeBack;
    @BindView(R.id.tv_header_title)
    TextView mTvHeaderTitle;
    @BindView(R.id.iv_img_service)
    ImageView mIvImgService;
    @BindView(R.id.fl_header_service)
    FrameLayout mFlHeaderService;
    @BindView(R.id.iv_img_share)
    ImageView mIvImgShare;
    @BindView(R.id.tl_quality_bar)
    Toolbar mTlQualityBar;
    private int page = 1;
    private List<QualityGoodProductEntity.Attribute> goodsProList = new ArrayList<>();
    private QualityGoodNewProAdapter qualityGoodNewProAdapter;
    private RemoveExistUtils removeExistUtils;

    @Override
    protected int getContentView() {
        return R.layout.activity_quality_good;
    }

    @Override
    protected void initViews() {
        mTvHeaderTitle.setText("优选好物");
        mIvImgService.setVisibility(View.GONE);
        mIvImgShare.setVisibility(View.GONE);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        communal_recycler.setLayoutManager(gridLayoutManager);

        smart_communal_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                page = 1;
                loadData();
            }
        });
//        好物
        qualityGoodNewProAdapter = new QualityGoodNewProAdapter(getActivity(), goodsProList);
        communal_recycler.setAdapter(qualityGoodNewProAdapter);
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_dp)
                .create());

        qualityGoodNewProAdapter.setOnLoadMoreListener(() -> {
            page++;
            getGoodsPro();
        }, communal_recycler);
        qualityGoodNewProAdapter.setOnItemClickListener((adapter, view, position) -> {
            QualityGoodProductEntity.Attribute attribute = (QualityGoodProductEntity.Attribute) view.getTag();
            if (attribute != null) {
                switch (attribute.getObjectType()) {
                    case "product":
                        LikedProductBean likedProductBean = (LikedProductBean) attribute;
                        Intent intent = new Intent(getActivity(), ShopScrollDetailsActivity.class);
                        intent.putExtra("productId", String.valueOf(likedProductBean.getId()));
                        startActivity(intent);
                        break;
                    case "ad":
                        CommunalADActivityBean communalADActivityBean = (CommunalADActivityBean) attribute;
                        /**
                         * 3.1.9 加入好物广告统计
                         */
                        adClickTotal(getActivity(), communalADActivityBean.getId());
                        setSkipPath(getActivity(), getStrings(communalADActivityBean.getAndroidLink()), false);
                        break;
                }

            }
        });
        qualityGoodNewProAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            loadHud.show();
            QualityGoodProductEntity.Attribute attribute = (QualityGoodProductEntity.Attribute) view.getTag();
            if (attribute != null) {
                if (userId > 0) {
                    switch (view.getId()) {
                        case R.id.iv_pro_add_car:
                            LikedProductBean likedProductBean = (LikedProductBean) attribute;
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
                    getLoginStatus(this);
                }
            }
        });
        totalPersonalTrajectory = insertFragmentNewTotalData(getActivity(), this.getClass().getSimpleName());
        removeExistUtils = new RemoveExistUtils();
    }

    @Override
    protected void loadData() {
        getGoodsPro();
    }

    private void getGoodsPro() {
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", page);
        /**
         * version 1 区分是否带入广告页
         */
        params.put("version", 1);
        if (userId > 0) {
            params.put("uid", userId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), QUALITY_SHOP_GOODS_PRO
                , params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();
                        qualityGoodNewProAdapter.loadMoreComplete();
                        if (page == 1) {
                            goodsProList.clear();
                            removeExistUtils.clearData();
                        }
                        MultiTypeJsonParser<QualityGoodProductEntity.Attribute> multiTypeJsonParser = new MultiTypeJsonParser.Builder<QualityGoodProductEntity.Attribute>()
                                .registerTypeElementName("objectType")
                                .registerTargetClass(QualityGoodProductEntity.Attribute.class)
                                .registerTypeElementValueWithClassType("product", LikedProductBean.class)
                                .registerTypeElementValueWithClassType("ad", CommunalADActivityBean.class)
                                .build();
                        QualityGoodProductEntity qualityGoodProductEntity = multiTypeJsonParser.fromJson(result, QualityGoodProductEntity.class);
                        if (qualityGoodProductEntity != null) {
                            if (qualityGoodProductEntity.getCode().equals(SUCCESS_CODE)) {
                                goodsProList.addAll(removeExistUtils.removeExistList(qualityGoodProductEntity.getGoodProductList()));
                            } else if (qualityGoodProductEntity.getCode().equals(EMPTY_CODE)) {
                                qualityGoodNewProAdapter.loadMoreEnd();
                            } else {
                                qualityGoodNewProAdapter.loadMoreEnd();
                                showToast(getActivity(), qualityGoodProductEntity.getMsg());
                            }
                        }
                        NetLoadUtils.getNetInstance().showLoadSirSuccess(loadService);
                        qualityGoodNewProAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onNotNetOrException() {
                        smart_communal_refresh.finishRefresh();
                        qualityGoodNewProAdapter.loadMoreEnd(true);
                        NetLoadUtils.getNetInstance().showLoadSirSuccess(loadService);
                    }
                });
    }


    @OnClick(R.id.tv_life_back)
    public void onViewClicked() {
        finish();
    }
}
