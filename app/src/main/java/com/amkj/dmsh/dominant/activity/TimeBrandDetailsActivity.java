package com.amkj.dmsh.dominant.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.time.bean.AllGroupEntity.BrandBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.time.bean.AllGroupEntity.BrandProductBean;
import com.amkj.dmsh.time.adapter.SingleProductAdapter;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.itemdecoration.NewGridItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;


/**
 * Created by xiaoxin on 2020/9/27
 * Version:v4.8.0
 * ClassDescription :品牌团详情
 */
public class TimeBrandDetailsActivity extends BaseActivity {


    @BindView(R.id.tv_life_back)
    TextView mTvLifeBack;
    @BindView(R.id.tv_header_title)
    TextView mTvHeaderTitle;
    @BindView(R.id.tv_header_shared)
    TextView mTvHeaderShared;
    @BindView(R.id.tl_normal_bar)
    Toolbar mTlNormalBar;
    @BindView(R.id.iv_cover)
    ImageView mIvCover;
    @BindView(R.id.tv_brand_name)
    TextView mTvBrandName;
    @BindView(R.id.tv_brand_desc)
    TextView mTvBrandDesc;
    @BindView(R.id.rv_goods)
    RecyclerView mRvGoods;
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout mSmartCommunalRefresh;
    private String mId;
    private BrandBean mBrandBean;
    private SingleProductAdapter mProductAdapter;
    private List<BrandProductBean> mProductList = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.activity_time_brand_details;
    }

    @Override
    protected void initViews() {
        if (getIntent().getExtras() != null) {
            mId = getIntent().getStringExtra("id");
        } else {
            showToast("数据有误，请重试");
            finish();
        }

        mRvGoods.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mRvGoods.addItemDecoration(new NewGridItemDecoration.Builder()
                .setDividerId(R.drawable.item_divider_five_gray_f)
                .create());
        mProductAdapter = new SingleProductAdapter(getActivity(), mProductList);
        mRvGoods.setAdapter(mProductAdapter);
    }

    @Override
    protected void loadData() {
        getBrandDetails();
    }

    private void getBrandDetails() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", mId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.GET_BRAND_DETAILS, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mProductList.clear();
                mBrandBean = GsonUtils.fromJson(result, BrandBean.class);
                if (mBrandBean != null) {
                    if (SUCCESS_CODE.equals(mBrandBean.getCode())) {
                        GlideImageLoaderUtil.loadCenterCrop(getActivity(), mIvCover, mBrandBean.getPicUrl());
                        mTvHeaderTitle.setText(getStrings(mBrandBean.getTitle()));
                        mTvBrandName.setText(getStrings(mBrandBean.getTitle()));
                        mTvBrandDesc.setText(getStrings(mBrandBean.getSubtitle()));
                        List<BrandProductBean> productList = mBrandBean.getProductList();
                        if (productList != null && productList.size() > 0) {
                            mProductList.addAll(productList);
                        }
                    }
                }

                mProductAdapter.notifyDataSetChanged();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mProductList, mBrandBean);
            }

            @Override
            public void onNotNetOrException() {
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mProductList, mBrandBean);
            }
        });
    }

    @Override
    public View getLoadView() {
        return mSmartCommunalRefresh;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }


    @OnClick({R.id.tv_life_back, R.id.tv_header_shared})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_life_back:
                finish();
                break;
            case R.id.tv_header_shared:
                if (mBrandBean != null) {
                    new UMShareAction(TimeBrandDetailsActivity.this
                            , mBrandBean.getPicUrl()
                            , mBrandBean.getTitle()
                            , mBrandBean.getSubtitle()
                            , Url.BASE_SHARE_PAGE_TWO + "limit_time_template/brand.html?id=" + mBrandBean.getId(), ConstantMethod.getStringChangeIntegers(mBrandBean.getId()));
                }
                break;
        }
    }
}
