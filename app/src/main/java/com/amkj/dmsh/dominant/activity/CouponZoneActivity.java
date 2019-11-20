package com.amkj.dmsh.dominant.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dominant.adapter.CouponZoneAdapter;
import com.amkj.dmsh.dominant.bean.CouponZoneEntity;
import com.amkj.dmsh.dominant.bean.CouponZoneEntity.CouponZoneBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.webformatdata.CommunalWebDetailUtils;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.isEndOrStartTime;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.COUPON_COVER;
import static com.amkj.dmsh.constant.ConstantVariable.COUPON_ONE_COLUMN;
import static com.amkj.dmsh.constant.ConstantVariable.COUPON_THREE_COLUMN;
import static com.amkj.dmsh.constant.ConstantVariable.ERROR_CODE;

/**
 * Created by xiaoxin on 2019/11/10
 * Version:v4.3.6
 * ClassDescription :优惠券专区
 */
public class CouponZoneActivity extends BaseActivity {
    @BindView(R.id.communal_recycler)
    RecyclerView mCommunalRecycler;
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout mSmartCommunalRefresh;
    @BindView(R.id.tv_header_title)
    TextView mTvHeaderTitle;
    @BindView(R.id.tv_header_shared)
    TextView mTvHeaderShared;
    @BindView(R.id.tl_normal_bar)
    Toolbar mTlNormalBar;
    @BindView(R.id.tv_life_back)
    TextView mTvLifeBack;
    private CouponZoneAdapter mCouponZoneAdapter;
    private GridLayoutManager mGridLayoutManager;
    List<CouponZoneBean> mCouponList = new ArrayList<>();
    private CouponZoneEntity mCouponZoneEntity;
    private String mZoneId;


    @Override
    protected int getContentView() {
        return R.layout.activity_coupon_zone;
    }

    @Override
    protected void initViews() {
        if (getIntent() == null || TextUtils.isEmpty(getIntent().getStringExtra("zoneId"))) {
            showToast(getActivity(), "数据有误，请重试");
            finish();
        }
        mTvHeaderTitle.setText("优惠券专场");
        mTlNormalBar.setSelected(true);
        mZoneId = getIntent().getStringExtra("zoneId");
        mCouponZoneAdapter = new CouponZoneAdapter(this, mCouponList);
        mGridLayoutManager = new GridLayoutManager(this, 3);
        mCommunalRecycler.setLayoutManager(mGridLayoutManager);
        mCommunalRecycler.setAdapter(mCouponZoneAdapter);
        mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                CouponZoneBean couponZoneBean = mCouponList.get(position);
                if (couponZoneBean.getItemType() == COUPON_THREE_COLUMN) {
                    return 1;
                } else {
                    return 3;
                }
            }
        });
        mCouponZoneAdapter.setOnItemClickListener((adapter, view, position) -> {
            CouponZoneBean couponZoneBean = (CouponZoneBean) view.getTag();
            if (couponZoneBean != null && couponZoneBean.getItemType() != ConstantVariable.COUPON_COVER) {
                if (userId > 0) {
                    //已达到领取次数上限
                    if (couponZoneBean.isOverLimit()) {
                        ConstantMethod.showToast(getActivity(), "已达到领取次数上限");
                    } else {
                        CommunalWebDetailUtils.getCommunalWebInstance().getDirectCoupon(getActivity(), couponZoneBean.getCouponId(), loadHud);
                    }
                } else {
                    getLoginStatus(this);
                }
            }
        });
        mSmartCommunalRefresh.setOnRefreshListener(refreshLayout -> {
            loadData();
        });
    }

    @Override
    protected void loadData() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", mZoneId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.Q_COUPON_ZONE, map,
                new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        mSmartCommunalRefresh.finishRefresh();
                        mCouponZoneEntity = new Gson().fromJson(result, CouponZoneEntity.class);
                        if (mCouponZoneEntity != null) {
                            mTvHeaderTitle.setText(getStrings(mCouponZoneEntity.getTitle()));
                            mCouponList.clear();
                            List<CouponZoneBean> couponList = mCouponZoneEntity.getCouponList();
                            String cover = mCouponZoneEntity.getCover();
                            String startTime = mCouponZoneEntity.getStartTime();
                            if (!TextUtils.isEmpty(cover)) {
                                CouponZoneBean couponZoneBean = new CouponZoneBean();
                                couponZoneBean.setCover(cover);
                                couponZoneBean.setItemType(COUPON_COVER);
                                mCouponList.add(couponZoneBean);
                            }
                            if (couponList != null && couponList.size() > 0) {
                                for (CouponZoneBean couponZoneBean : couponList) {
//                                    couponZoneBean.setItemType(COUPON_ONE_COLUMN);
                                    couponZoneBean.setItemType(couponList.size() > 5 ? COUPON_THREE_COLUMN : COUPON_ONE_COLUMN);
                                }
                                mCouponList.addAll(couponList);
                            } else if (ERROR_CODE.equals(mCouponZoneEntity.getCode())) {
                                showToast(getActivity(), mCouponZoneEntity.getMsg());
                            }
                            mGridLayoutManager.setSpanCount(3);
                            mCouponZoneAdapter.setStart(isEndOrStartTime(mCouponZoneEntity.getSystemTime(), startTime));
                            mCouponZoneAdapter.notifyDataSetChanged();
                        }

                        NetLoadUtils.getNetInstance().showLoadSir(loadService, mCouponList, mCouponZoneEntity);
                    }

                    @Override
                    public void onNotNetOrException() {
                        mSmartCommunalRefresh.finishRefresh();
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, mCouponList, mCouponZoneEntity);
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


    @OnClick({R.id.tv_life_back, R.id.tv_header_shared})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_life_back:
                finish();
                break;
            case R.id.tv_header_shared:
                if (mCouponZoneEntity != null) {
                    new UMShareAction(this, mCouponZoneEntity.getCover(),
                            "有钱任性，优惠券免费领",
                            "先领券后下单，部分活动商品可叠加使用，真正折上折。",
                            "http://test.domolife.cn/test/template/20191111/couponZone.html?id=" + mCouponZoneEntity.getId(),
                            mCouponZoneEntity.getId());

                }
                break;
        }
    }
}
