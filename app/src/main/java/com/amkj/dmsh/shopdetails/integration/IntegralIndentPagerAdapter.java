package com.amkj.dmsh.shopdetails.integration;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.amkj.dmsh.base.BaseFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * 积分订单分类
 */
public class IntegralIndentPagerAdapter extends FragmentPagerAdapter {
    private Map<String, String> params = new HashMap<>();
    private String[] integralIndentType = {"全部", "未完结", "已完结"};

    public IntegralIndentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return integralIndentType.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return integralIndentType[position];
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 1) {
            params.put("statusCategory", "1");
        } else if (position == 2) {
            params.put("statusCategory", "2");
        }
        return BaseFragment.newInstance(IntegralIndentFragment.class, params, null);
    }
}
