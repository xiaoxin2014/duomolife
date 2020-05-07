package com.amkj.dmsh.homepage.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.dominant.adapter.GoodProductAdapter;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.user.bean.UserLikedProductEntity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean;
import com.amkj.dmsh.utils.RemoveExistUtils;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.ERROR_CODE;
import static com.amkj.dmsh.constant.Url.QUALITY_SHOP_GOODS_PRO;

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
    private List<LikedProductBean> goodsProList = new ArrayList<>();
    private GoodProductAdapter qualityGoodNewProAdapter;
    private RemoveExistUtils removeExistUtils;
    private UserLikedProductEntity mQualityGoodProductEntity;

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
        qualityGoodNewProAdapter = new GoodProductAdapter(getActivity(), goodsProList);
        communal_recycler.setAdapter(qualityGoodNewProAdapter);
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_dp)
                .create());

        qualityGoodNewProAdapter.setOnLoadMoreListener(() -> {
            page++;
            getGoodsPro();
        }, communal_recycler);
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
                        mQualityGoodProductEntity = GsonUtils.fromJson(result, UserLikedProductEntity.class);
                        List<LikedProductBean> goodList = removeExistUtils.removeExistList(mQualityGoodProductEntity.getGoodsList());
                        if (mQualityGoodProductEntity != null) {
                            if (goodList != null && goodList.size() > 0) {
                                if (page == 1) {
                                    goodsProList.clear();
                                    removeExistUtils.clearData();
                                }
                                goodsProList.addAll(goodList);
                                qualityGoodNewProAdapter.notifyDataSetChanged();
                                qualityGoodNewProAdapter.loadMoreComplete();
                            } else if (ERROR_CODE.equals(mQualityGoodProductEntity.getCode())) {
                                qualityGoodNewProAdapter.loadMoreFail();
                                ConstantMethod.showToast(mQualityGoodProductEntity.getMsg());
                            } else {
                                qualityGoodNewProAdapter.loadMoreEnd();
                            }
                        }

                        NetLoadUtils.getNetInstance().showLoadSir(loadService, goodsProList, mQualityGoodProductEntity);
                    }

                    @Override
                    public void onNotNetOrException() {
                        smart_communal_refresh.finishRefresh();
                        qualityGoodNewProAdapter.loadMoreFail();
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, goodsProList, mQualityGoodProductEntity);
                    }
                });
    }


    @OnClick(R.id.tv_life_back)
    public void onViewClicked() {
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
}
