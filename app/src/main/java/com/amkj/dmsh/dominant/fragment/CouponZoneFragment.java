package com.amkj.dmsh.dominant.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dominant.adapter.CouponZoneAdapter;
import com.amkj.dmsh.dominant.bean.CouponZoneEntity;
import com.amkj.dmsh.dominant.bean.CouponZoneEntity.CouponZoneBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static com.amkj.dmsh.constant.ConstantMethod.dismissLoadhud;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.isEndOrStartTime;
import static com.amkj.dmsh.constant.ConstantMethod.showLoadhud;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.showToastRequestMsg;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.COUPON_COVER;
import static com.amkj.dmsh.constant.ConstantVariable.COUPON_ONE_COLUMN;
import static com.amkj.dmsh.constant.ConstantVariable.COUPON_THREE_COLUMN;
import static com.amkj.dmsh.constant.ConstantVariable.ERROR_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.FIND_ARTICLE_COUPON;

/**
 * Created by xiaoxin on 2019/11/10
 * Version:v4.3.6
 * ClassDescription :优惠券专区
 */
public class CouponZoneFragment extends BaseFragment {
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
        if (TextUtils.isEmpty(mZoneId)) {
            return;
        }
        mTlNormalBar.setVisibility(View.GONE);
        mCouponZoneAdapter = new CouponZoneAdapter(getActivity(), mCouponList);
        mGridLayoutManager = new GridLayoutManager(getActivity(), 3);
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
            if (userId > 0) {
                if (couponZoneBean != null) {
                    getCoupon(couponZoneBean, position);
                }
            } else {
                getLoginStatus(getActivity());
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
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Url.Q_COUPON_ZONE, map,
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

    //领取优惠券
    public void getCoupon(CouponZoneBean couponZoneBean, int position) {
        showLoadhud(getActivity());
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("couponId", couponZoneBean.getCouponId());
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), FIND_ARTICLE_COUPON, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                dismissLoadhud(getActivity());
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                showToastRequestMsg(getActivity(), requestStatus);
                if (requestStatus != null && requestStatus.getResult() != null) {
                    String code = requestStatus.getCode();
                    String resultCode = requestStatus.getResult().getResultCode();
                    //领取成功
                    if (SUCCESS_CODE.equals(code) && SUCCESS_CODE.equals(resultCode)) {
                        //修改状态
                        couponZoneBean.setIsAlreadyGet(1);
                        int itemType = couponZoneBean.getItemType();
                        if (itemType == COUPON_ONE_COLUMN) {
                            RelativeLayout rl_coupon = (RelativeLayout) mCouponZoneAdapter.getViewByPosition(mCommunalRecycler, position, R.id.rl_coupon_one);
                            if (rl_coupon != null) {
                                rl_coupon.setBackgroundResource(R.drawable.coupon_already_get_bg);
                            }
                        } else if (itemType == COUPON_THREE_COLUMN) {
                            ImageView ivGet = (ImageView) mCouponZoneAdapter.getViewByPosition(mCommunalRecycler, position, R.id.iv_get);
                            if (ivGet != null) {
                                ivGet.setImageResource(R.drawable.coupon_already_get);
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void getReqParams(Bundle bundle) {
        mZoneId = bundle.getString("zoneId");
    }
}
