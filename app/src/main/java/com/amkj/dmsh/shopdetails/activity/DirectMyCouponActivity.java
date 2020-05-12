package com.amkj.dmsh.shopdetails.activity;

import android.content.Intent;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.shopdetails.adapter.CouponStatusAdapter;
import com.amkj.dmsh.views.flycoTablayout.SlidingTabLayout;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;

/**
 * Created by atd48 on 2016/8/23.
 * 我的优惠券
 */
public class DirectMyCouponActivity extends BaseActivity {
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tv_header_shared)
    TextView header_shared;
    @BindView(R.id.tv_life_back)
    TextView tvLifeBack;
    @BindView(R.id.std_mine_coupon)
    SlidingTabLayout stdMineCoupon;
    @BindView(R.id.vp_mine_coupon)
    ViewPager vpMineCoupon;

    @Override
    protected int getContentView() {
        return R.layout.activity_mine_coupon;
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
        tv_header_titleAll.setText("优惠券");
        header_shared.setVisibility(View.INVISIBLE);
        stdMineCoupon.setTextsize(AutoSizeUtils.mm2px(mAppContext, 28));
        setCouponData();
    }

    private void setCouponData() {
        if (userId < 1) {
            return;
        }
        CouponStatusAdapter couponStatusAdapter = new CouponStatusAdapter(getSupportFragmentManager());
        vpMineCoupon.setOffscreenPageLimit(couponStatusAdapter.getCount() - 1);
        vpMineCoupon.setAdapter(couponStatusAdapter);
        stdMineCoupon.setViewPager(vpMineCoupon);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IS_LOGIN_CODE) {
            setCouponData();
        }
    }

    @OnClick(R.id.tv_life_back)
    void finish(View view) {
        finish();
    }

    @Override
    protected void loadData() {
    }
}
