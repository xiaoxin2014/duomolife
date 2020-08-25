package com.amkj.dmsh.mine.adapter;

import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.mine.fragment.VipPowerDetailFragment;
import com.amkj.dmsh.mine.bean.PowerEntity.PowerBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * Created by xiaoxin on 2020/7/23
 * Version:v4.7.0
 */
public class PowerPageAdapter extends FragmentPagerAdapter {

    private final List<PowerBean> mDatas;

    public PowerPageAdapter(@NonNull FragmentManager fm, List<PowerBean> datas) {
        super(fm);
        mDatas = datas;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Map<String, String> map = new HashMap<>();
        PowerBean powerBean = mDatas.get(position);
        map.put("name", powerBean.getTitle());
        map.put("picUrl", powerBean.getPicUrl());
        map.put("detail", powerBean.getDetail());
        map.put("androidLink", powerBean.getAndroidLink());
        map.put("btnText", powerBean.getBtnText());
        return BaseFragment.newInstance(VipPowerDetailFragment.class, map, null);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }
}
