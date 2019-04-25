package com.amkj.dmsh.homepage.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.bean.CategoryTypeEntity.CategoryTypeBean;
import com.amkj.dmsh.homepage.fragment.ArticleListFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/7/29
 * class description:请输入类描述
 */

public class HomeArticleTypeAdapter extends FragmentPagerAdapter {
    private final List<CategoryTypeBean> categoryList;

    public HomeArticleTypeAdapter(FragmentManager fragmentManager, List<CategoryTypeBean> categoryList) {
        super(fragmentManager);
        this.categoryList = categoryList;
    }

    @Override
    public Fragment getItem(int position) {
        Map<String, Object> params = new HashMap<>();
        params.put("articleType", categoryList.get(position));
        return BaseFragment.newInstance(ArticleListFragment.class, null, params);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return categoryList.get(position).getTitle();
    }

    @Override
    public int getCount() {
        return categoryList != null ? categoryList.size() : 0;
    }
}
