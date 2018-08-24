package com.amkj.dmsh.homepage.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;

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
 * class description:请输入类描述
 */

public class TimeShowPagerAdapter extends FragmentPagerAdapter {
    private final List<TimeShowBean> timeShowBeanList = new ArrayList<>();
    private Map<String, String> params = new HashMap<>();

    public TimeShowPagerAdapter(FragmentManager fm, List<TimeShowBean> timeShowBeanList) {
        super(fm);
        this.timeShowBeanList.addAll(timeShowBeanList);
    }

    @Override
    public Fragment getItem(int position) {
        params.clear();
        if(timeShowBeanList.get(position)!=null&& !TextUtils.isEmpty(timeShowBeanList.get(position).getDate())){
            params.put("show_time", timeShowBeanList.get(position).getDate());
        }
        return BaseFragment.newInstance(SpringSaleFragment.class, params, null);
    }

    @Override
    public int getCount() {
        return timeShowBeanList.size();
    }
}
