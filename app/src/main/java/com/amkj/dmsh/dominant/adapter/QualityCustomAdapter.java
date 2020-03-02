package com.amkj.dmsh.dominant.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.dominant.fragment.GroupCustomTopicFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xiaoxin on 2019/12/3
 * Version:v4.4.0
 * ClassDescription :自定义专区viewpager适配器
 */
public class QualityCustomAdapter extends FragmentPagerAdapter {
    private Map<String, Object> params = new HashMap<>();
    private List<String> mProductTypeList;

    public QualityCustomAdapter(FragmentManager fm, List<String> productTypeList) {
        super(fm);
        mProductTypeList = productTypeList;
    }

    @Override
    public Fragment getItem(int position) {
        params.put("productType", mProductTypeList.get(position));
        return BaseFragment.newInstance(GroupCustomTopicFragment.class, null, params);
    }

    @Override
    public int getCount() {
        return mProductTypeList == null ? 0 : mProductTypeList.size();
    }
}