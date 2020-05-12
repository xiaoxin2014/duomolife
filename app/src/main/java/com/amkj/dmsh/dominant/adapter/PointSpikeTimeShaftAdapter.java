package com.amkj.dmsh.dominant.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.dominant.bean.PointSpikeTimeShaftEntity.TimeAxisInfoListBean;
import com.amkj.dmsh.dominant.fragment.PointSpikeProductFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2019/3/2
 * version 3.3.0
 * class description:整点秒时间轴适配器
 */
public class PointSpikeTimeShaftAdapter extends FragmentPagerAdapter {

    private final List<TimeAxisInfoListBean> timeAxisInfoListBeans;

    public PointSpikeTimeShaftAdapter(FragmentManager fm, List<TimeAxisInfoListBean> timeAxisInfoList) {
        super(fm);
        timeAxisInfoListBeans = timeAxisInfoList;
    }

    @Override
    public Fragment getItem(int i) {
        Map<String,String> params = new HashMap<>();
        params.put("pointSpikeId",String.valueOf(timeAxisInfoListBeans.get(i).getId()));
        params.put("pointSpikeStartTime",String.valueOf(timeAxisInfoListBeans.get(i).getStartTime()));
        params.put("pointSpikeEndTime",String.valueOf(timeAxisInfoListBeans.get(i).getEndTime()));
        return BaseFragment.newInstance(PointSpikeProductFragment.class, params, null);
    }


    @Override
    public int getCount() {
        return timeAxisInfoListBeans != null ? timeAxisInfoListBeans.size() : 0;
    }
}
