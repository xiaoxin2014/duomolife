package com.amkj.dmsh.shopdetails.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.shopdetails.fragment.DoMoIndentNewFragment;
import com.amkj.dmsh.shopdetails.fragment.WaitEvaluateFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/8/14
 * class description:请输入类描述
 */

public class IndentPagerAdapter extends FragmentPagerAdapter {

    private String[] title = {"全 部", "待付款", "待发货", "待收货", "待评价"};

    public IndentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == title.length - 1) {
            return BaseFragment.newInstance(WaitEvaluateFragment.class, null, null);
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("type", position);
            return BaseFragment.newInstance(DoMoIndentNewFragment.class, null, map);
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }

    @Override
    public int getCount() {
        return  title.length;
    }
}
