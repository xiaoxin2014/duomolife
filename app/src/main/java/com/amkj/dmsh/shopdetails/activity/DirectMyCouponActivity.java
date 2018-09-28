package com.amkj.dmsh.shopdetails.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.mine.activity.MineLoginActivity;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.shopdetails.adapter.CouponStatusAdapter;
import com.flyco.tablayout.SlidingTabLayout;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getPersonalInfo;
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
    private int uid;

    @Override
    protected int getContentView() {
        return R.layout.activity_mine_coupon;
    }

    @Override
    protected void initViews() {
        getLoginStatus();
        tv_header_titleAll.setText("优惠券");
        header_shared.setVisibility(View.INVISIBLE);
        stdMineCoupon.setTextsize(AutoSizeUtils.mm2px(mAppContext,28));
        CouponStatusAdapter couponStatusAdapter = new CouponStatusAdapter(getSupportFragmentManager());
        vpMineCoupon.setAdapter(couponStatusAdapter);
        stdMineCoupon.setViewPager(vpMineCoupon);
    }

    private void getLoginStatus() {
        SavePersonalInfoBean personalInfo = getPersonalInfo(this);
        if (personalInfo.isLogin()) {
            uid = personalInfo.getUid();
        } else {
            //未登录跳转登录页
            Intent intent = new Intent(this, MineLoginActivity.class);
            startActivityForResult(intent, IS_LOGIN_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IS_LOGIN_CODE) {
            getLoginStatus();
            EventBus.getDefault().post(new EventMessage("refresh",uid));
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
