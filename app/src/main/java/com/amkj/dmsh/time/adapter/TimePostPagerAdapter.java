package com.amkj.dmsh.time.adapter;

import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.mine.bean.VipExclusiveInfoEntity.VipExclusiveInfoBean;
import com.amkj.dmsh.time.fragment.TimePostContentFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * Created by xiaoxin on 2020/9/29
 * Version:v4.8.0
 */
public class TimePostPagerAdapter extends FragmentPagerAdapter {

    private final List<VipExclusiveInfoBean> mDatas;

    public TimePostPagerAdapter(@NonNull FragmentManager fm, List<VipExclusiveInfoBean> datas) {
        super(fm);
        mDatas = datas;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Map<String, String> map = new HashMap<>();
        map.put("id", mDatas.get(position).getCategoryId());
        return BaseFragment.newInstance(TimePostContentFragment.class, map, null);
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        VipExclusiveInfoBean vipExclusiveInfoBean = mDatas.get(position);
        if (vipExclusiveInfoBean != null) {
            return vipExclusiveInfoBean.getTitle();
        }
        return super.getPageTitle(position);
    }
}
