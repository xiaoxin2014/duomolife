package com.amkj.dmsh.shopdetails.adapter;

import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.shopdetails.bean.LogisticsNewEntity.PackageInfoBean;
import com.amkj.dmsh.shopdetails.fragment.DirectLogisticsFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/5/19
 * class description:请输入类描述
 */

public class LogisticsPagerAdapter extends FragmentPagerAdapter {
    private final List<String> pageTitle;
    private List<PackageInfoBean> packageList;

    public LogisticsPagerAdapter(FragmentManager fm, List<String> pageTitle, List<PackageInfoBean> packageList) {
        super(fm);
        this.pageTitle = pageTitle;
        this.packageList = packageList;
    }

    @Override
    public Fragment getItem(int position) {
        Map<String, String> params = new HashMap<>();
        PackageInfoBean packageInfoBean = packageList.get(position);
        if (packageInfoBean != null) {
            params.put("orderNo", packageInfoBean.getOrderNo());
            params.put("zeroOrderNo", packageInfoBean.getZeroOrderNo());
            params.put("expressNo", packageInfoBean.getExpressNo());
            params.put("refundNo", packageInfoBean.getRefundNo());
        }
        return BaseFragment.newInstance(DirectLogisticsFragment.class, params, null);
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
