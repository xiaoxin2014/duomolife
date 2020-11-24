package com.amkj.dmsh.time.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.constant.CommunalAdHolderView;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.find.bean.PostEntity.PostBean;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity.CommunalADActivityBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.time.activity.TimePostCatergoryActivity;
import com.amkj.dmsh.time.adapter.BrandAdapter;
import com.amkj.dmsh.time.adapter.SingleProductAdapter;
import com.amkj.dmsh.time.adapter.TimePostAdapter;
import com.amkj.dmsh.time.bean.BrandEntity;
import com.amkj.dmsh.time.bean.BrandEntity.BrandBean;
import com.amkj.dmsh.time.bean.BrandEntity.BrandBean.BrandProductBean;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.getShowNumber;

/**
 * Created by xiaoxin on 2020/9/23
 * Version:v4.8.0
 * ClassDescription :时间轴对应的团购商品列表
 */
public class TimeAxisFragment extends BaseFragment {

    @BindView(R.id.rv_single)
    RecyclerView mRvSingle;
    @BindView(R.id.rv_recommend)
    RecyclerView mRvRecommend;
    @BindView(R.id.tv_more)
    TextView mTvMore;
    @BindView(R.id.rv_brand)
    RecyclerView mRvBrand;
    @BindView(R.id.ad_recommend_banner)
    ConvenientBanner mRecommendBanner;
    private String mTime;

    private CBViewHolderCreator cbViewHolderCreator;
    private List<BrandProductBean> mSingleProductList = new ArrayList<>();
    private List<BrandBean> brandList = new ArrayList<>();
    private List<PostBean> mPostList = new ArrayList<>();
    private SingleProductAdapter mSingleProductAdapter;
    private BrandAdapter mBrandAdapter;
    private TimePostAdapter postAdapter;
    private BrandEntity mBrandEntity;
    private BrandBean mBrandBean;


    @Override
    protected int getContentView() {
        return R.layout.fragment_time_axis;
    }

    @Override
    protected void initViews() {
        ItemDecoration newGridItemDecoration = new ItemDecoration.Builder()
                .setDividerId(R.drawable.item_divider_five_gray_f)
                .create();
        //初始化单品列表
        mRvSingle.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mRvSingle.addItemDecoration(newGridItemDecoration);
        mSingleProductAdapter = new SingleProductAdapter(getActivity(), mSingleProductList);
        mRvSingle.setAdapter(mSingleProductAdapter);
        //初始化品牌团列表
        mRvBrand.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBrandAdapter = new BrandAdapter(getActivity(), brandList);
        mRvBrand.setAdapter(mBrandAdapter);
        //初始化推荐帖子列表
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        mRvRecommend.setLayoutManager(layoutManager);
        mRvRecommend.addItemDecoration(newGridItemDecoration);
        postAdapter = new TimePostAdapter(getActivity(), mPostList, true);
        mRvRecommend.setAdapter(postAdapter);
    }

    @Override
    protected void loadData() {
        getSingleProduct();
    }

    private void getSingleProduct() {
        Map<String, Object> map = new HashMap<>();
        map.put("searchTime", mTime);
        map.put("currentPage", 1);
        map.put("showCount", 80);
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Url.GET_SINGLE_PRODUCT, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mSingleProductList.clear();
                mBrandBean = GsonUtils.fromJson(result, BrandBean.class);
                if (mBrandBean != null) {
                    List<BrandProductBean> singleProducts = mBrandBean.getProductList();
                    if (singleProducts != null) {
                        mSingleProductList.addAll(singleProducts);
                    }
                }
                mSingleProductAdapter.notifyDataSetChanged();
                getBrandProduct();
            }

            @Override
            public void onNotNetOrException() {
                getBrandProduct();
            }
        });
    }

    private void getBrandProduct() {
        Map<String, String> map = new HashMap<>();
        map.put("searchTime", mTime);
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Url.GET_TIME_BANNER, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                brandList.clear();
                mBrandEntity = GsonUtils.fromJson(result, BrandEntity.class);
                if (mBrandEntity != null) {
                    List<BrandBean> brands = mBrandEntity.getResult();
                    if (brands != null) {
                        brandList.addAll(brands);
                    }
                }

                mBrandAdapter.notifyDataSetChanged();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mSingleProductList.size() > 0 || brandList.size() > 0, mBrandEntity);
            }

            @Override
            public void onNotNetOrException() {
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mSingleProductList.size() > 0 || brandList.size() > 0, mBrandEntity);
            }
        });
    }

    @Override
    protected void getReqParams(Bundle bundle) {
        if (bundle != null) {
            mTime = bundle.getString("time");
        }
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        super.postEventResult(message);
        if (ConstantVariable.UPDATE_RECOMMEND_AD.equals(message.type)) {
            List<CommunalADActivityBean> adBeanList = (List<CommunalADActivityBean>) message.result;
            if (cbViewHolderCreator == null) {
                cbViewHolderCreator = new CBViewHolderCreator() {
                    @Override
                    public Holder createHolder(View itemView) {
                        return new CommunalAdHolderView(itemView, getActivity(), mRecommendBanner, true);
                    }

                    @Override
                    public int getLayoutId() {
                        return R.layout.layout_ad_image_video;
                    }
                };
            }
            mRecommendBanner.setPages(TimeAxisFragment.this, cbViewHolderCreator, adBeanList)
                    .startTurning(getShowNumber(adBeanList.get(0).getShowTime()) * 1000);
            mRecommendBanner.setVisibility(adBeanList.size() > 0 ? View.VISIBLE : View.GONE);
        } else if (ConstantVariable.UPDATE_RECOMMEND_LIST.equals(message.type)) {
            List<PostBean> postList = (List<PostBean>) message.result;
            mPostList.clear();
            if (postList != null && postList.size() > 0) {
                mPostList.addAll(postList);
            }

            postAdapter.notifyDataSetChanged();
            mTvMore.setVisibility(mPostList.size() > 0 ? View.VISIBLE : View.GONE);
        }
    }


    @OnClick(R.id.tv_more)
    public void onViewClicked() {
        Intent intent = new Intent(getActivity(), TimePostCatergoryActivity.class);
        startActivity(intent);
    }

    @Override
    protected boolean isLazy() {
        return true;
    }
}
