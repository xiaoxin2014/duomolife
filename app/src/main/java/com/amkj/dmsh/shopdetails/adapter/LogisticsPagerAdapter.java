package com.amkj.dmsh.shopdetails.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.shopdetails.bean.DirectLogisticsEntity;
import com.amkj.dmsh.shopdetails.bean.DirectLogisticsEntity.DirectLogisticsBean;
import com.amkj.dmsh.shopdetails.fragment.DirectLogisticsFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/5/19
 * class description:请输入类描述
 */

public class LogisticsPagerAdapter extends FragmentPagerAdapter {

    private Map<String, String> params;
    private Map<String, Object> objectParams;
    private final List<String> pageTitle;
    private DirectLogisticsBean directLogisticsBean;

    public LogisticsPagerAdapter(FragmentManager fm, List<String> pageTitle, DirectLogisticsEntity.DirectLogisticsBean directLogisticsBean) {
        super(fm);
        this.pageTitle = pageTitle;
        this.directLogisticsBean = directLogisticsBean;
    }

    @Override
    public Fragment getItem(int position) {
        params = new HashMap<>();
        objectParams = new HashMap<>();
        params.put("packet", "" + position);
        objectParams.put("directLogisticsBean", directLogisticsBean);
        return BaseFragment.newInstance(DirectLogisticsFragment.class, params, objectParams);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pageTitle.get(position);
    }

    @Override
    public int getCount() {
        return pageTitle == null ? 0 : pageTitle.size();
    }
}
