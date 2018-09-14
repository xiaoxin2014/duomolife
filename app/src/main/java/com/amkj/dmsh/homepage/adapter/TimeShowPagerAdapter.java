package com.amkj.dmsh.homepage.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.homepage.bean.TimeShowEntity.TimeShowBean;
import com.amkj.dmsh.homepage.fragment.SpringSaleFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/4/21
 * class description:限时特惠
 */

public class TimeShowPagerAdapter extends FragmentPagerAdapter {
    private List<TimeShowBean> timeShowBeanList;
    private final List<TimeShowBean> timeShowBeanWaitList = new ArrayList<>();
    private final List<TimeShowBean> timeShowBeanOpeningList = new ArrayList<>();
    private Map<String, Object> params = new HashMap<>();

    public TimeShowPagerAdapter(FragmentManager fm, List<TimeShowBean> timeShowBeanList) {
        super(fm);
        for (TimeShowBean timeShowBean : timeShowBeanList) {
            if (timeShowBean.getHourShaft() != null && timeShowBean.getHourShaft().length > 0) {
                if (timeShowBean.getType() > 1) {
                    timeShowBeanWaitList.add(timeShowBean);
                } else {
                    timeShowBeanOpeningList.add(timeShowBean);
                }
            } else {
                if (timeShowBean.getType() > 1) {
                    timeShowBean.setHourShaft(new String[]{"10", "20"});
                    timeShowBeanWaitList.add(timeShowBean);
                } else {
                    timeShowBean.setHourShaft(new String[]{"10", "20"});
                    timeShowBeanOpeningList.add(timeShowBean);
                }
            }
        }
    }

    @Override
    public Fragment getItem(int position) {
        params.clear();
        timeShowBeanList = new ArrayList<>();
        if (position == 0) {
            timeShowBeanList.addAll(timeShowBeanOpeningList);
        } else {
            timeShowBeanList.addAll(timeShowBeanWaitList);
        }
        params.put("showTime", timeShowBeanList);
        return BaseFragment.newInstance(SpringSaleFragment.class, null, params);
    }

    @Override
    public int getCount() {
        return 2;
    }
}
