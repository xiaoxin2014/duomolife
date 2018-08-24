package com.amkj.dmsh.homepage.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.homepage.fragment.IntegralProductFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/8/4
 * version 3.1.5
 * class description:积分商城分类
 */
public class IntegralProductTypeAdapter extends FragmentPagerAdapter {

    private final String[] integralType;
    private Map<String,String> params  = new HashMap<>();

    public IntegralProductTypeAdapter(FragmentManager fm, String[] integralType) {
        super(fm);
        this.integralType = integralType;
    }

    @Override
    public Fragment getItem(int position) {
        String integralTypeSort;
        switch (integralType[position]) {
            case "全部商品":
                integralTypeSort = "-1";
                break;
            case "积分兑换":
                integralTypeSort = "0";
                break;
            default:
                integralTypeSort = "1";
                break;
        }
        params.put("integralType",integralTypeSort);
        return BaseFragment.newInstance(IntegralProductFragment.class, params, null);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return integralType[position];
    }

    @Override
    public int getCount() {
        return integralType.length;
    }
}
