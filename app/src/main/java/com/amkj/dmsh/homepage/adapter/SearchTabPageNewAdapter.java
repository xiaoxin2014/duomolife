package com.amkj.dmsh.homepage.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.homepage.fragment.SearchDetailsProductNewFragment;
import com.amkj.dmsh.homepage.fragment.SearchDetailsTopicFragment;
import com.amkj.dmsh.homepage.fragment.SearchDetailsUserFragment;
import com.amkj.dmsh.homepage.fragment.SearchDetailsArticleFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by atd48 on 2016/7/4.
 */
public class SearchTabPageNewAdapter extends FragmentPagerAdapter {
    private String[] title = {"商品", "种草", "话题", "用户"};

    private final Map<String, String> params = new HashMap<>();

    public SearchTabPageNewAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        params.put("title", (String) getPageTitle(position));
        switch (position) {
            case 0:
                return BaseFragment.newInstance(SearchDetailsProductNewFragment.class, params, null);
            case 1:
                return BaseFragment.newInstance(SearchDetailsArticleFragment.class, params, null);
            case 2:
                return BaseFragment.newInstance(SearchDetailsTopicFragment.class, params, null);
            case 3:
                return BaseFragment.newInstance(SearchDetailsUserFragment.class, params, null);
            default:
                return BaseFragment.newInstance(SearchDetailsProductNewFragment.class, params, null);
        }
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
