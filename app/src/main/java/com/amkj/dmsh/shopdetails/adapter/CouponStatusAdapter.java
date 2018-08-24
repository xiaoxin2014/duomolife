package com.amkj.dmsh.shopdetails.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.shopdetails.fragment.DirectMyCouponFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/1/23
 * version 3.7
 * class description:优惠券状态
 */

public class CouponStatusAdapter extends FragmentPagerAdapter {
    private String[] couponStatus = {"未使用", "已使用", "已过期"};
    public CouponStatusAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Map<String,String> params = new HashMap<>();
        params.put("couponStatus",String.valueOf(position));
        return BaseFragment.newInstance(DirectMyCouponFragment.class, params, null);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return couponStatus[position];
    }

    @Override
    public int getCount() {
        return couponStatus.length;
    }
}
