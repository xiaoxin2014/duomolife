package com.amkj.dmsh.mine.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.shopdetails.fragment.DoMoSalesReturnRecordFragment;
import com.amkj.dmsh.shopdetails.fragment.DoMoSalesReturnReplyFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by atd48 on 2016/8/24.
 */
public class GoodsSalePagerAdapter extends FragmentPagerAdapter {
    List<Fragment> list;
    private String[] title = {"处理中", "售后记录"};


    public GoodsSalePagerAdapter(FragmentManager fm) {
        super(fm);
        list = new ArrayList<>();
        list.add(BaseFragment.newInstance(DoMoSalesReturnReplyFragment.class, null, null));
        list.add(BaseFragment.newInstance(DoMoSalesReturnRecordFragment.class, null, null));
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
        return list.get(position);
    }
}
