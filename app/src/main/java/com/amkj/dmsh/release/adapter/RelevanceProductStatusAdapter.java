package com.amkj.dmsh.release.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.find.fragment.RelevanceProductFragment;
import com.amkj.dmsh.release.bean.RelevanceProEntity.RelevanceProBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.amkj.dmsh.constant.ConstantVariable.BOUGHT_PRODUCT;
import static com.amkj.dmsh.constant.ConstantVariable.CART_PRODUCT;
import static com.amkj.dmsh.constant.ConstantVariable.COLLECT_PRODUCT;
import static com.amkj.dmsh.constant.ConstantVariable.RELEVANCE_TYPE;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/3/8
 * version 1.0
 * class description:请输入类描述
 */

public class RelevanceProductStatusAdapter extends FragmentPagerAdapter {
    private final List<RelevanceProBean> relevanceProBeans;
    private String[] relevanceProductStatus = {"已购买商品", "收藏商品", "购物车商品"};

    public RelevanceProductStatusAdapter(FragmentManager fm, List<RelevanceProBean> relevanceProBeanList) {
        super(fm);
        relevanceProBeans = relevanceProBeanList;
    }

    @Override
    public Fragment getItem(int position) {
        Map<String, String> params = new HashMap<>();
        Map<String,Object> objParams = null;
        if (relevanceProBeans != null&&relevanceProBeans.size()>0) {
            objParams = new HashMap<>();
            objParams.put("relevanceProduct", relevanceProBeans);
        }
        switch (position) {
            case 1:
                params.put(RELEVANCE_TYPE, COLLECT_PRODUCT);
                break;
            case 2:
                params.put(RELEVANCE_TYPE, CART_PRODUCT);
                break;
            default:
                params.put(RELEVANCE_TYPE, BOUGHT_PRODUCT);
                break;
        }
        return BaseFragment.newInstance(RelevanceProductFragment.class, params, objParams);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return relevanceProductStatus[position];
    }

    @Override
    public int getCount() {
        return relevanceProductStatus.length;
    }
}
