package com.amkj.dmsh.find.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.mine.fragment.UserPostContentFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiaoxin on 2019/7/16
 * Version:v4.2.2
 * ClassDescription :用户主页-帖子分类 帖子适配器
 */
public class UserPostPagerAdapter extends FragmentPagerAdapter {
    private Map<String, Object> params = new HashMap<>();
    private String[] mTitles;

    public UserPostPagerAdapter(FragmentManager fm, String userId, String[] titles) {
        super(fm);
        mTitles = titles;
        params.put("userId", userId);
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
        return BaseFragment.newInstance(UserPostContentFragment.class, null, params);
    }
}