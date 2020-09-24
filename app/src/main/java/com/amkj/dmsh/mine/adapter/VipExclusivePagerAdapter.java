package com.amkj.dmsh.mine.adapter;

import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.mine.bean.VipExclusiveInfoEntity.VipExclusiveInfoBean;
import com.amkj.dmsh.mine.fragment.VipExclusiveGoodsFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * Created by xiaoxin on 2019/12/3
 * Version:v4.7.0
 * ClassDescription :会员专享专区viewpager适配器
 */
public class VipExclusivePagerAdapter extends FragmentPagerAdapter {
    private Map<String, Object> params = new HashMap<>();
    private List<VipExclusiveInfoBean> infos;
    private String isHomePage;

    public VipExclusivePagerAdapter(FragmentManager fm, List<VipExclusiveInfoBean> infos,String isHomePage) {
        super(fm);
        this.infos = infos;
        this.isHomePage = isHomePage;
    }

    @Override
    public Fragment getItem(int position) {
        VipExclusiveInfoBean vipExclusiveInfoBean = infos.get(position);
        if (vipExclusiveInfoBean != null) {
            params.put("categoryId", vipExclusiveInfoBean.getCategoryId());
        }
        params.put("isHomePage", isHomePage);
        return BaseFragment.newInstance(VipExclusiveGoodsFragment.class, null, params);
    }

    @Override
    public int getCount() {
        return infos == null ? 0 : infos.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        VipExclusiveInfoBean vipExclusiveInfoBean = infos.get(position);
        if (vipExclusiveInfoBean != null) {
            return vipExclusiveInfoBean.getTitle();
        }
        return super.getPageTitle(position);
    }
}