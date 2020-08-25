package com.amkj.dmsh.mine.adapter;

import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.mine.fragment.ZeroApplyListFragment;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * Created by xiaoxin on 2019/12/3
 * Version:v4.7.0
 */
public class ZeroApplyListAdapter extends FragmentPagerAdapter {
    private Map<String, Object> params = new HashMap<>();
    private String[] mStatus;

    public ZeroApplyListAdapter(FragmentManager fm, String[] status) {
        super(fm);
        mStatus = status;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        params.put("status", mStatus[position]);
        return BaseFragment.newInstance(ZeroApplyListFragment.class, null, params);
    }

    @Override
    public int getCount() {
        return mStatus == null ? 0 : mStatus.length;
    }
}