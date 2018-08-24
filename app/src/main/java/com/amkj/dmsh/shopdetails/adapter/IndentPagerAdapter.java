package com.amkj.dmsh.shopdetails.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.shopdetails.fragment.DoMoIndentDeliveredFragment;
import com.amkj.dmsh.shopdetails.fragment.DoMoIndentWaitAppraiseFragment;
import com.amkj.dmsh.shopdetails.fragment.DoMoIndentWaitPayFragment;
import com.amkj.dmsh.shopdetails.fragment.DoMoIndentWaitSendFragment;
import com.amkj.dmsh.shopdetails.fragment.DuMoIndentAllFragment;

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
        switch (position) {
            case 0:
                return BaseFragment.newInstance(DuMoIndentAllFragment.class, null, null);
            case 1:
                return BaseFragment.newInstance(DoMoIndentWaitPayFragment.class, null, null);
            case 2:
                return BaseFragment.newInstance(DoMoIndentWaitSendFragment.class, null, null);
            case 3:
                return BaseFragment.newInstance(DoMoIndentDeliveredFragment.class, null, null);
            case 4:
                return BaseFragment.newInstance(DoMoIndentWaitAppraiseFragment.class, null, null);
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }

    @Override
    public int getCount() {
        return title.length;
    }
}
