package com.amkj.dmsh.mine.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.mine.adapter.MonthCouponVerticalAdapter;
import com.amkj.dmsh.mine.bean.VipCouponEntity;
import com.amkj.dmsh.mine.bean.VipCouponEntity.VipCouponBean.CouponListBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.webformatdata.CommunalWebDetailUtils;
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

import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.userId;

/**
 * Created by xiaoxin on 2020/8/13
 * Version:v4.7.0
 * ClassDescription :每月专享券列表
 */
public class MonthCouponListActivity extends BaseActivity {
    @BindView(R.id.tv_life_back)
    TextView mTvLifeBack;
    @BindView(R.id.tv_header_title)
    TextView mTvHeaderTitle;
    @BindView(R.id.tv_header_shared)
    TextView mTvHeaderShared;
    @BindView(R.id.tl_normal_bar)
    Toolbar mTlNormalBar;
    @BindView(R.id.communal_recycler)
    RecyclerView mRvCoupon;
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout mSmartCommunalRefresh;
    @BindView(R.id.iv_cover)
    ImageView mIvCover;
    @BindView(R.id.tv_subtitle)
    TextView mTvSubtitle;
    private MonthCouponVerticalAdapter mVipCouponAdapter;
    private List<CouponListBean> mCouponList = new ArrayList<>();
    private VipCouponEntity mVipCouponEntity;
    private String mZoneId;

    @Override
    protected int getContentView() {
        return R.layout.activity_month_coupon_list;
    }

    @Override
    protected void initViews() {
        mZoneId = getIntent().getStringExtra("zoneId");
        mTvHeaderShared.setVisibility(View.GONE);
        //初始化优惠券列表
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mRvCoupon.setLayoutManager(linearLayoutManager);
        mVipCouponAdapter = new MonthCouponVerticalAdapter(this, mCouponList);
        mRvCoupon.setAdapter(mVipCouponAdapter);
        mVipCouponAdapter.setOnItemClickListener((adapter, view, position) -> {
            CouponListBean couponListBean = (CouponListBean) view.getTag();
            if (couponListBean == null || couponListBean.isSoldOut()) return;
            if (userId > 0) {
                CommunalWebDetailUtils.getCommunalWebInstance().getDirectCoupon(getActivity(), couponListBean.getCouponId(), loadHud);
            } else {
                getLoginStatus(getActivity());
            }
        });
        mSmartCommunalRefresh.setOnRefreshListener(refreshLayout -> loadData());
    }

    @Override
    protected void loadData() {
        getCouponList();
    }

    //获取优惠券列表
    private void getCouponList() {
        Map<String, Object> map = new HashMap<>();
        map.put("zoneId", mZoneId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.GET_VIP_COUPON_ZONE, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mSmartCommunalRefresh.finishRefresh();
                mCouponList.clear();
                mVipCouponEntity = GsonUtils.fromJson(result, VipCouponEntity.class);
                if (mVipCouponEntity != null) {
                    VipCouponEntity.VipCouponBean couponBean = mVipCouponEntity.getResult();
                    if (couponBean != null) {
                        mTvHeaderTitle.setText(getStrings(couponBean.getTitle()));
                        mTvSubtitle.setText(getStrings(couponBean.getSubtitle()));
                        if (!TextUtils.isEmpty(couponBean.getCoverImg())) {
                            mIvCover.setVisibility(View.VISIBLE);
                            GlideImageLoaderUtil.loadCenterCrop(getActivity(), mIvCover, couponBean.getCoverImg());
                        }
                        List<CouponListBean> couponList = couponBean.getCouponList();
                        if (couponList != null && couponList.size() > 0) {
                            mCouponList.addAll(couponList);
                        }
                    }
                }
                mVipCouponAdapter.notifyDataSetChanged();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mCouponList, mVipCouponEntity);
            }

            @Override
            public void onNotNetOrException() {
                mSmartCommunalRefresh.finishRefresh();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mCouponList, mVipCouponEntity);
            }
        });
    }

    @OnClick(R.id.tv_life_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    public View getLoadView() {
        return mSmartCommunalRefresh;
    }
}
