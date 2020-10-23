package com.amkj.dmsh.time.adapter;

import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.time.bean.TimeAxisEntity.TimeAxisBean;
import com.amkj.dmsh.time.fragment.TimeAxisFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * Created by xiaoxin on 2020/9/23
 * Version:v4.8.0
 * ClassDescription :淘好货时间轴适配器
 */
public class TimePagerAdapter extends FragmentPagerAdapter {
    private Map<String, Object> params = new HashMap<>();
    private final List<TimeAxisBean> mTimeAxisBeanList;

    public TimePagerAdapter(@NonNull FragmentManager fm, List<TimeAxisBean> timeAxisBeanList) {
        super(fm);
        mTimeAxisBeanList = timeAxisBeanList;
    }


    @Override
    public int getCount() {
        return mTimeAxisBeanList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        TimeAxisBean timeAxisBean = mTimeAxisBeanList.get(position);
        return timeAxisBean != null ? timeAxisBean.getName() : "";
    }

    @Override
    public Fragment getItem(int position) {
        TimeAxisBean timeAxisBean = mTimeAxisBeanList.get(position);
        if (timeAxisBean != null) {
            params.put("time", timeAxisBean.getDate());
        }
        return BaseFragment.newInstance(TimeAxisFragment.class, null, params);
    }
}
