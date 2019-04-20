package com.amkj.dmsh.homepage.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;

import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.dominant.activity.DoMoLifeWelfareDetailsFragment;
import com.amkj.dmsh.dominant.fragment.DmlOptimizedSelFragment;
import com.amkj.dmsh.dominant.fragment.DoMoLifeWelfareFragment;
import com.amkj.dmsh.dominant.fragment.QualityGroupShopFragment;
import com.amkj.dmsh.dominant.fragment.QualityNewProFragment;
import com.amkj.dmsh.dominant.fragment.QualityNewUserFragment;
import com.amkj.dmsh.dominant.fragment.QualityTypeHotSaleProFragment;
import com.amkj.dmsh.dominant.fragment.WholePointSpikeProductFragment;
import com.amkj.dmsh.homepage.bean.HomeCommonEntity;
import com.amkj.dmsh.homepage.bean.HomeCommonEntity.HomeCommonBean;
import com.amkj.dmsh.homepage.fragment.AliBCFragment;
import com.amkj.dmsh.homepage.fragment.EditorSelectFragment;
import com.amkj.dmsh.homepage.fragment.HomeCouponGetFragment;
import com.amkj.dmsh.homepage.fragment.HomeDefalutFragment;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getUrlParams;
import static com.amkj.dmsh.constant.ConstantMethod.isWebLinkUrl;

/**
 * Created by xiaoxin on 2019/4/12 0012
 * Version:v4.0.0
 * ClassDescription :新版首页 viewPager适配器
 */

public class HomePageNewAdapter extends FragmentPagerAdapter {
    private final List<HomeCommonBean> mHomeCommonBeanList;
    private String[] actionArrays = {"app://HomeDefalutFragment", "app://QualityNewUserActivity", "app://QualityTypeHotSaleProActivity", "app://QualityNewProActivity", "app://HomeCouponGetActivity", "app://DmlOptimizedSelActivity",
            "app://DoMoLifeWelfareActivity", "app://EditorSelectActivity", "app://WholePointSpikeProductActivity", "app://QualityGroupShopActivity", "app://DoMoLifeWelfareDetailsActivity", "app://DuomoLifeActivity"};
    private String prefix = "app://";

    public HomePageNewAdapter(FragmentManager fm, List<HomeCommonBean> homeCommonBeanList) {
        super(fm);
        this.mHomeCommonBeanList = homeCommonBeanList;
        //筛选数据，防止版本api数据不同，会有不支持的原生链接
        List<String> actionList = Arrays.asList(actionArrays);
        Iterator<HomeCommonBean> iterator = mHomeCommonBeanList.iterator();
        while (iterator.hasNext()) {
            HomeCommonEntity.HomeCommonBean bean = iterator.next();
            String link = bean.getLink();
            if (TextUtils.isEmpty(link.trim())) {
                iterator.remove();
            } else {
                if (link.contains(prefix) && !actionList.contains(bean.getLink())) {
                    int prefixLength = link.indexOf(prefix) + prefix.length();
                    int urlIndex = link.indexOf("?", prefixLength);
                    if (urlIndex != -1) {
                        link = link.substring(link.indexOf(prefix), urlIndex).trim();
                        if (!actionList.contains(link)) {
                            iterator.remove();
                        }
                    } else {
                        iterator.remove();
                    }

                }
            }
        }
    }

    @Override
    public Fragment getItem(int position) {
        HomeCommonEntity.HomeCommonBean homeCommonBean = mHomeCommonBeanList.get(position);
        String link = homeCommonBean.getLink().trim();
        //判断是否是H5
        if (isWebLinkUrl(link)) {
            Map<String, String> params = new HashMap<>();
            params.put("loadUrl", getStrings(link));
            return BaseFragment.newInstance(AliBCFragment.class, params, null);
        } else if (link.contains(prefix)) {
            int prefixLength = link.indexOf(prefix) + prefix.length();
            //判断是否有参数
            int urlIndex = link.indexOf("?", prefixLength);
            if (urlIndex != -1) {
                Map<String, String> urlParams = getUrlParams(link);
                switch (link.substring(link.indexOf(prefix), urlIndex).trim()) {
                    case "app://DoMoLifeWelfareDetailsActivity"://福利社专题
                        return BaseFragment.newInstance(DoMoLifeWelfareDetailsFragment.class, urlParams, null);
                    default:
                        return BaseFragment.newInstance(DoMoLifeWelfareDetailsFragment.class, urlParams, null);
                }
            } else {
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
                    case "app://DoMoLifeWelfareActivity"://福利社专区
                    case "app://DuomoLifeActivity"://福利社专区
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
        }
        return BaseFragment.newInstance(HomeDefalutFragment.class, null, null);
    }

    @Override
    public int getCount() {
        return mHomeCommonBeanList.size();
    }
}
