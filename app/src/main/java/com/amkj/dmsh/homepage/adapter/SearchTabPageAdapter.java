package com.amkj.dmsh.homepage.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.homepage.fragment.SearchInvitationDetailsFragment;
import com.amkj.dmsh.homepage.fragment.SearchDetailsProductFragment;
import com.amkj.dmsh.homepage.fragment.SearchDetailsUserFragment;
import com.amkj.dmsh.homepage.fragment.SpecialDetailsFragment;
import com.amkj.dmsh.homepage.fragment.SearchTopicDetailsFragment;

import java.util.Map;

/**
 * Created by atd48 on 2016/7/4.
 */
public class SearchTabPageAdapter extends FragmentPagerAdapter {
    private String[] title = {"商品", "专题", "帖子", "话题", "用户"};

    private final Map<String, String> params;

    public SearchTabPageAdapter(FragmentManager fm, Map<String, String> data) {
        super(fm);
        this.params = data;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return BaseFragment.newInstance(SearchDetailsProductFragment.class, params, null);
            case 1:
                return BaseFragment.newInstance(SpecialDetailsFragment.class, params, null);
            case 2:
                return BaseFragment.newInstance(SearchInvitationDetailsFragment.class, params, null);
            case 3:
                return BaseFragment.newInstance(SearchTopicDetailsFragment.class, params, null);
            case 4:
                return BaseFragment.newInstance(SearchDetailsUserFragment.class, params, null);
        }
        return null;
    }

    @Override
    public Fragment instantiateItem(ViewGroup container, int position) {
        return (Fragment) super.instantiateItem(container, position);
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
