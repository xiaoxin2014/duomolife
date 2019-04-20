package com.amkj.dmsh.catergory.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.bean.QualityTypeEntity.QualityTypeBean;
import com.amkj.dmsh.homepage.fragment.CatergoryOneLevelFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xiaoxin on 2019/4/12 0012
 * Version:v4.0.0
 * ClassDescription :新版首页 viewPager适配器
 */

public class CatergoryFragmentAdapter extends FragmentPagerAdapter {
    private final List<QualityTypeBean> mQualityTypeBeanList;

    public CatergoryFragmentAdapter(FragmentManager fm, List<QualityTypeBean> qualityTypeBeanList) {
        super(fm);
        this.mQualityTypeBeanList = qualityTypeBeanList;
    }

    @Override
    public Fragment getItem(int position) {
        QualityTypeBean qualityTypeBean = mQualityTypeBeanList.get(position);
        Map<String, String> map = new HashMap<>();
        map.put("catergoryPid", String.valueOf(qualityTypeBean.getId()));
        return BaseFragment.newInstance(CatergoryOneLevelFragment.class, map, null);

    }

    @Override
    public int getCount() {
        return mQualityTypeBeanList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mQualityTypeBeanList.get(position).getName();
    }
}
