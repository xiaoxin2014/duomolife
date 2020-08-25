package com.amkj.dmsh.mine.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.bean.WeekProductEntity;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.mine.adapter.WeekProductVerticalAdapter;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.user.bean.UserLikedProductEntity;
import com.amkj.dmsh.user.bean.LikedProductBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;

/**
 * Created by xiaoxin on 2020/8/13
 * Version:v4.7.0
 * ClassDescription :每周会员特价商品列表
 */
public class VipZoneDetailActivity extends BaseActivity {
    @BindView(R.id.tv_life_back)
    TextView mTvLifeBack;
    @BindView(R.id.tv_header_title)
    TextView mTvHeaderTitle;
    @BindView(R.id.tv_header_shared)
    TextView mTvHeaderShared;
    @BindView(R.id.tl_normal_bar)
    Toolbar mTlNormalBar;
    @BindView(R.id.rv_week_goods)
    RecyclerView mRvWeekGoods;
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout mSmartCommunalRefresh;
    @BindView(R.id.iv_cover)
    ImageView mIvCover;
    private List<LikedProductBean> mGoodsList = new ArrayList<>();
    private WeekProductVerticalAdapter mWeekProductAdapter;
    private WeekProductEntity mVipProductEntity;
    private String mZoneId;

    @Override
    protected int getContentView() {
        return R.layout.activity_week_product_list;
    }


    @Override
    protected void initViews() {
        mZoneId = getIntent().getStringExtra("zoneId");
        if (TextUtils.isEmpty(mZoneId)) {
            showToast("数据有误，请重试");
            finish();
        }
        mTvHeaderShared.setVisibility(View.GONE);
        //初始化每周会员特价商品列表
        mRvWeekGoods.setLayoutManager(new LinearLayoutManager(this));
        mWeekProductAdapter = new WeekProductVerticalAdapter(this, mGoodsList);
        mRvWeekGoods.setAdapter(mWeekProductAdapter);
    }

    @Override
    protected void loadData() {
        getWeekGoods();
    }

    //获取每周会员特价商品
    private void getWeekGoods() {
        Map<String, Object> map = new HashMap<>();
        map.put("zoneId", mZoneId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.GET_VIP_ZONE_DETAIL, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mSmartCommunalRefresh.finishRefresh();
                mGoodsList.clear();
                mVipProductEntity = GsonUtils.fromJson(result, WeekProductEntity.class);
                if (mVipProductEntity != null) {
                    WeekProductEntity.WeekProductBean weekProductBean = mVipProductEntity.getResult();
                    if (weekProductBean != null) {
                        mTvHeaderTitle.setText(getStrings(weekProductBean.getTitle()));
                        if (!TextUtils.isEmpty(weekProductBean.getCoverImg())) {
                            mIvCover.setVisibility(View.VISIBLE);
                            GlideImageLoaderUtil.loadCenterCrop(getActivity(), mIvCover, weekProductBean.getCoverImg());
                        }
                        List<LikedProductBean> goodsList = weekProductBean.getGoodsList();
                        if (goodsList != null && goodsList.size() > 0) {
                            mGoodsList.addAll(goodsList.subList(0, Math.min(goodsList.size(), 3)));
                        }
                    }
                }
                mWeekProductAdapter.notifyDataSetChanged();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mGoodsList, mVipProductEntity);
            }

            @Override
            public void onNotNetOrException() {
                mSmartCommunalRefresh.finishRefresh();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mGoodsList, mVipProductEntity);
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

    @OnClick(R.id.tv_life_back)
    public void onViewClicked() {
        finish();
    }
}
