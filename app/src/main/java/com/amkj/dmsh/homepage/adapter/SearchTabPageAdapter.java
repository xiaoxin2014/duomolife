package com.amkj.dmsh.homepage.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.homepage.fragment.SearchDetailsProductFragment;
import com.amkj.dmsh.homepage.fragment.SearchDetailsUserFragment;
import com.amkj.dmsh.homepage.fragment.SearchInvitationDetailsFragment;
import com.amkj.dmsh.homepage.fragment.SearchTopicDetailsFragment;
import com.amkj.dmsh.homepage.fragment.SpecialDetailsFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by atd48 on 2016/7/4.
 */
public class SearchTabPageAdapter extends FragmentPagerAdapter {
    private String[] title = {"商品", "专题", "帖子", "话题", "用户"};
    private Map<Integer, Fragment> fragmentHashMap = new HashMap<>();

    private final Map<String, String> params;

    public SearchTabPageAdapter(FragmentManager fm, Map<String, String> data) {
        super(fm);
        this.params = data;
        fragmentHashMap.clear();
    }

    @Override
    public Fragment getItem(int position) {
        if (fragmentHashMap.get(position) == null) {
            switch (position) {
                case 0:
                    fragmentHashMap.put(position, BaseFragment.newInstance(SearchDetailsProductFragment.class, params, null));
                    break;
                case 1:
                    fragmentHashMap.put(position, BaseFragment.newInstance(SpecialDetailsFragment.class, params, null));
                    break;
                case 2:
                    fragmentHashMap.put(position, BaseFragment.newInstance(SearchInvitationDetailsFragment.class, params, null));
                    break;
                case 3:
                    fragmentHashMap.put(position, BaseFragment.newInstance(SearchTopicDetailsFragment.class, params, null));
                    break;
                case 4:
                    fragmentHashMap.put(position, BaseFragment.newInstance(SearchDetailsUserFragment.class, params, null));
                    break;
            }
        }
        return fragmentHashMap.get(position) != null ? fragmentHashMap.get(position) : BaseFragment.newInstance(SearchDetailsProductFragment.class, params, null);
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
