package com.amkj.dmsh.shopdetails.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.mine.activity.ShopCarActivity;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.adapter.GroupMatchAdapter;
import com.amkj.dmsh.shopdetails.bean.GroupGoodsEntity;
import com.amkj.dmsh.shopdetails.bean.GroupGoodsEntity.GroupGoodsBean.CombineCommonBean;
import com.google.gson.Gson;
import com.luck.picture.lib.decoration.RecycleViewDivider;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;
import q.rorbin.badgeview.Badge;

import static com.amkj.dmsh.constant.ConstantMethod.getBadge;
import static com.amkj.dmsh.constant.ConstantMethod.getCarCount;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;

/**
 * Created by xiaoxin on 2019/5/30
 * Version:v4.1.0
 * ClassDescription :组合搭配
 */
public class GroupMatchActivity extends BaseActivity {
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
    @BindView(R.id.communal_recycler)
    RecyclerView mCommunalRecycler;
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout mSmartCommunalRefresh;
    @BindView(R.id.ll_bottom)
    LinearLayout mLlBottom;
    private Badge mBadge;
    private GroupMatchAdapter mGroupCollocaAdapter;
    private List<CombineCommonBean> groupGoods = new ArrayList<>();
    private GroupGoodsEntity mGroupGoodsEntity;
    private String mProductId;

    @Override
    protected int getContentView() {
        return R.layout.activity_group_collocation;
    }

    @Override
    protected void initViews() {
        if (getIntent() != null && !TextUtils.isEmpty(getIntent().getStringExtra("productId"))) {
            mProductId = getIntent().getStringExtra("productId");
        } else {
            showToast("商品信息有误，请重试");
            finish();
        }
        mTvHeaderTitle.setText("组合搭配");
        mIvImgService.setImageResource(R.drawable.shop_car_gray_icon);
        mBadge = getBadge(this, mFlHeaderService);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mGroupCollocaAdapter = new GroupMatchAdapter(this, groupGoods);
        mCommunalRecycler.addItemDecoration(new RecycleViewDivider(
                this, LinearLayoutManager.HORIZONTAL, AutoSizeUtils.mm2px(this, 1), getResources().getColor(R.color.text_color_e_s)));
        mCommunalRecycler.setLayoutManager(layoutManager);
        mCommunalRecycler.setAdapter(mGroupCollocaAdapter);
        mGroupCollocaAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            CombineCommonBean bean = (CombineCommonBean) view.getTag();
            if (bean == null) return;
            switch (view.getId()) {
                //选择sku
                case R.id.tv_select_sku:

                    break;
                //选中或取消
                case R.id.tv_shop_car_sel:
                    view.setSelected(!view.isSelected());
                    bean.setSelected(view.isSelected());
                    break;
            }
        });
        mSmartCommunalRefresh.setOnRefreshListener(refreshLayout -> getGroupGoods(mProductId));
    }

    @Override
    protected void loadData() {
        getCarCount(this, mBadge);
        getGroupGoods(mProductId);
    }

    //获取组合商品详细信息
    private void getGroupGoods(String id) {
        String url = Url.BASE_URL + Url.Q_GROUP_GOODS_DETAIL;
        Map<String, Object> params = new HashMap<>();
        params.put("productId", id);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mSmartCommunalRefresh.finishRefresh();
                mGroupGoodsEntity = new Gson().fromJson(result, GroupGoodsEntity.class);
                if (mGroupGoodsEntity != null && mGroupGoodsEntity.getResult() != null) {
                    GroupGoodsEntity.GroupGoodsBean groupGoodsBean = mGroupGoodsEntity.getResult();
                    if (mGroupGoodsEntity.getCode().equals(SUCCESS_CODE)) {
                        CombineCommonBean combineMainProduct = groupGoodsBean.getCombineMainProduct();
                        groupGoods.clear();
                        if (combineMainProduct != null) {
                            combineMainProduct.setMainProduct(true);
                            groupGoods.add(combineMainProduct);
                        }
                        List<CombineCommonBean> combineProductList = groupGoodsBean.getCombineMatchProductList();
                        if (combineProductList != null && combineProductList.size() > 0) {
                            groupGoods.addAll(combineProductList.subList(0, combineProductList.size() > 20 ? 20 : combineProductList.size()));
                        }
                        mGroupCollocaAdapter.notifyDataSetChanged();
                    } else if (!mGroupGoodsEntity.getCode().equals(EMPTY_CODE)) {
                        ConstantMethod.showToast(mGroupGoodsEntity.getMsg());
                    }
                }

                mLlBottom.setVisibility(groupGoods.size() > 0 ? View.VISIBLE : View.GONE);
                NetLoadUtils.getNetInstance().showLoadSir(loadService, groupGoods, mGroupGoodsEntity);
            }

            @Override
            public void onNotNetOrException() {
                mSmartCommunalRefresh.finishRefresh();
                mLlBottom.setVisibility(groupGoods.size() > 0 ? View.VISIBLE : View.GONE);
                NetLoadUtils.getNetInstance().showLoadSir(loadService, groupGoods, mGroupGoodsEntity);
            }
        });
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    public View getLoadView() {
        return mSmartCommunalRefresh;
    }

    @OnClick({R.id.tv_life_back, R.id.iv_img_service, R.id.iv_img_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_life_back:
                finish();
                break;
            case R.id.iv_img_service:
                Intent intent = new Intent(getActivity(), ShopCarActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_img_share:
                break;
        }
    }
}
