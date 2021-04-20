package com.amkj.dmsh.homepage.adapter;

import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.homepage.fragment.VideoListFragment;

import java.util.HashMap;
import java.util.Map;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * Created by xiaoxin on 2021/2/23
 * Version:v5.0.0
 */
public class VideoListPagerAdapter extends FragmentPagerAdapter {
    String[] titlePage = {"发现", "热门"};

    public VideoListPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Map<String, String> map = new HashMap<>();
        map.put("sortType", String.valueOf(position));
        return BaseFragment.newInstance(VideoListFragment.class, map, null);
    }

    @Override
    public int getCount() {
        return titlePage.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titlePage[position];
    }
}
