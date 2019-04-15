package com.amkj.dmsh.homepage.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.dominant.fragment.DmlOptimizedSelFragment;
import com.amkj.dmsh.dominant.fragment.DoMoLifeWelfareFragment;
import com.amkj.dmsh.dominant.fragment.QualityGroupShopFragment;
import com.amkj.dmsh.dominant.fragment.QualityNewProFragment;
import com.amkj.dmsh.dominant.fragment.QualityNewUserFragment;
import com.amkj.dmsh.dominant.fragment.QualityTypeHotSaleProFragment;
import com.amkj.dmsh.dominant.fragment.WholePointSpikeProductFragment;
import com.amkj.dmsh.homepage.bean.HomeCommonEntity;
import com.amkj.dmsh.homepage.bean.HomeCommonEntity.HomeCommonBean;
import com.amkj.dmsh.homepage.fragment.EditorSelectFragment;
import com.amkj.dmsh.homepage.fragment.HomeCouponGetFragment;
import com.amkj.dmsh.homepage.fragment.HomeDefalutFragment;

import java.util.List;

/**
 * Created by xiaoxin on 2019/4/12 0012
 * Version:v4.0.0
 * ClassDescription :大改版首页 viewPager适配器
 */

public class HomePageNewAdapter extends FragmentPagerAdapter {
    private final List<HomeCommonBean> mHomeCommonBeanList;


    public HomePageNewAdapter(FragmentManager fm, List<HomeCommonBean> homeCommonBeanList) {
        super(fm);
        this.mHomeCommonBeanList = homeCommonBeanList;
    }

    @Override
    public Fragment getItem(int position) {
        HomeCommonEntity.HomeCommonBean homeCommonBean = mHomeCommonBeanList.get(position);
        String link = homeCommonBean.getLink();
//        String prefix = "app://";
//        String action = "";
//        if (!TextUtils.isEmpty(link)) {
//            int prefixLength = link.indexOf(prefix) + prefix.length();
//            action = link.substring(prefixLength, link.length()).trim();
//        }
        switch (link) {
            case "app://HomeDefalutFragment"://良品优选
                return BaseFragment.newInstance(HomeDefalutFragment.class, null, null);
            case "app://QualityGroupShopActivity"://多么拼团
                return BaseFragment.newInstance(QualityGroupShopFragment.class, null, null);
            case "app://QualityNewUserActivity"://新人专区
                return BaseFragment.newInstance(QualityNewUserFragment.class, null, null);
            case "app://QualityTypeHotSaleProActivity"://热销单品
                return BaseFragment.newInstance(QualityTypeHotSaleProFragment.class, null, null);
            case "app://QualityNewProActivity"://新品发布
                return BaseFragment.newInstance(QualityNewProFragment.class, null, null);
            case "app://HomeCouponGetActivity"://领劵中心
                return BaseFragment.newInstance(HomeCouponGetFragment.class, null, null);
            case "app://DmlOptimizedSelActivity"://多么定制专区
                return BaseFragment.newInstance(DmlOptimizedSelFragment.class, null, null);
            case "app://DoMoLifeWelfareActivity"://福利社所有专题
                return BaseFragment.newInstance(DoMoLifeWelfareFragment.class, null, null);
            case "app://EditorSelectActivity"://小编精选
                return BaseFragment.newInstance(EditorSelectFragment.class, null, null);
            case "app://WholePointSpikeProductActivity"://整点秒杀
                return BaseFragment.newInstance(WholePointSpikeProductFragment.class, null, null);
            //良品优选
            default:
                return BaseFragment.newInstance(HomeDefalutFragment.class, null, null);
        }
    }

    @Override
    public int getCount() {
        return mHomeCommonBeanList.size();
    }
}
