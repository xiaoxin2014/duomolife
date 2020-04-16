package com.amkj.dmsh.mine.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.shopdetails.fragment.RefundGoodsListFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by atd48 on 2016/8/24.
 */
public class GoodsSalePagerAdapter extends FragmentPagerAdapter {
    private String[] title = {"处理中", "售后记录"};

    public GoodsSalePagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public int getCount() {
        return title.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }

    @Override
    public Fragment getItem(int position) {
        Map<String, Object> map = new HashMap<>();
        map.put("position", position);
        return BaseFragment.newInstance(RefundGoodsListFragment.class, null, map);
    }
}
