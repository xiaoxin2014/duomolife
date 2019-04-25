package com.amkj.dmsh.homepage.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.catergory.bean.CatergoryOneLevelEntity.CatergoryOneLevelBean.ChildCategoryListBean;
import com.amkj.dmsh.homepage.activity.CatergoryGoodsFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/7/29
 * class description:请输入类描述
 */

public class CatergoryTwoLevelAdapter extends FragmentPagerAdapter {
    private final List<ChildCategoryListBean> categoryList;

    public CatergoryTwoLevelAdapter(FragmentManager fragmentManager, List<ChildCategoryListBean> categoryList) {
        super(fragmentManager);
        this.categoryList = categoryList;
    }

    @Override
    public Fragment getItem(int position) {
        Map<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(categoryList.get(position).getId()));
        params.put("pid", String.valueOf(categoryList.get(position).getPid()));
        return BaseFragment.newInstance(CatergoryGoodsFragment.class, params ,null);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return categoryList.get(position).getName();
    }

    @Override
    public int getCount() {
        return categoryList != null ? categoryList.size() : 0;
    }
}
