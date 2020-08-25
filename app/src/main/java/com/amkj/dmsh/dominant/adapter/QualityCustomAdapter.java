package com.amkj.dmsh.dominant.adapter;

import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.dominant.fragment.GroupCustomTopicFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * Created by xiaoxin on 2019/12/3
 * Version:v4.4.0
 * ClassDescription :自定义专区viewpager适配器
 */
public class QualityCustomAdapter extends FragmentPagerAdapter {
    private Map<String, Object> params = new HashMap<>();
    private List<String> mProductTypeList;
    private String mSimpleName;
    private int mType;//区分是否是会员专区  0非会员  1会员

    public QualityCustomAdapter(FragmentManager fm, List<String> productTypeList, String simpleName) {
        super(fm);
        mProductTypeList = productTypeList;
        mSimpleName = simpleName;
    }

    public QualityCustomAdapter(FragmentManager fm, List<String> productTypeList, String simpleName, int type) {
        this(fm, productTypeList, simpleName);
        mType = type;
    }

    @Override
    public Fragment getItem(int position) {
        params.put("type", mType);
        params.put("productType", mProductTypeList.get(position));
        params.put("position", String.valueOf(position));
        params.put("simpleName", mSimpleName);
        return BaseFragment.newInstance(GroupCustomTopicFragment.class, null, params);
    }

    @Override
    public int getCount() {
        return mProductTypeList == null ? 0 : mProductTypeList.size();
    }
}