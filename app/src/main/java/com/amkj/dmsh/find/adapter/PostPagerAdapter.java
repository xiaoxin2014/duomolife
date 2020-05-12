package com.amkj.dmsh.find.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.find.fragment.PostContentFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiaoxin on 2019/7/16
 * Version:v4.1.0
 * ClassDescription :发现-专题分类/帖子分类 帖子适配器
 */
public class PostPagerAdapter extends FragmentPagerAdapter {
    private Map<String, Object> params = new HashMap<>();
    private String[] mTitles;

    public PostPagerAdapter(FragmentManager fm, String[] titles, String topicId) {
        super(fm);
        mTitles = titles;
        params.put("topicId", topicId);
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    @Override
    public Fragment getItem(int position) {
        params.put("title", mTitles[position]);
        return BaseFragment.newInstance(PostContentFragment.class, null, params);
    }
}