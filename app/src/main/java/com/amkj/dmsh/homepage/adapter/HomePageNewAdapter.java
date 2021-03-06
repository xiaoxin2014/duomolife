package com.amkj.dmsh.homepage.adapter;

import android.text.TextUtils;

import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.dominant.activity.DoMoLifeWelfareDetailsFragment;
import com.amkj.dmsh.dominant.fragment.CouponZoneFragment;
import com.amkj.dmsh.dominant.fragment.DmlOptimizedSelFragment;
import com.amkj.dmsh.dominant.fragment.DoMoLifeWelfareFragment;
import com.amkj.dmsh.dominant.fragment.QualityCustomTopicFragment;
import com.amkj.dmsh.dominant.fragment.QualityGroupShopFragment;
import com.amkj.dmsh.dominant.fragment.QualityNewProFragment;
import com.amkj.dmsh.dominant.fragment.QualityNewUserFragment;
import com.amkj.dmsh.dominant.fragment.QualityShopBuyListFragment;
import com.amkj.dmsh.dominant.fragment.QualityTypeHotSaleProFragment;
import com.amkj.dmsh.dominant.fragment.QualityWeekOptimizedFragment;
import com.amkj.dmsh.dominant.fragment.WholePointSpikeProductFragment;
import com.amkj.dmsh.find.fragment.TopicDetailFragment;
import com.amkj.dmsh.homepage.bean.HomeCommonEntity;
import com.amkj.dmsh.homepage.bean.HomeCommonEntity.HomeCommonBean;
import com.amkj.dmsh.homepage.fragment.AliBCFragment;
import com.amkj.dmsh.homepage.fragment.ArticleTypeFragment;
import com.amkj.dmsh.homepage.fragment.AttendanceFragment;
import com.amkj.dmsh.homepage.fragment.EditorSelectFragment;
import com.amkj.dmsh.homepage.fragment.HomeCouponGetFragment;
import com.amkj.dmsh.homepage.fragment.HomeDefalutFragment;
import com.amkj.dmsh.mine.fragment.VipZoneDetailFragment;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

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
            "app://DoMoLifeWelfareActivity", "app://EditorSelectActivity", "app://WholePointSpikeProductActivity", "app://QualityGroupShopActivity", "app://DoMoLifeWelfareDetailsActivity", "app://DuomoLifeActivity",
            "app://QualityCustomTopicActivity", "app://QualityWeekOptimizedActivity", "app://QualityShopBuyListActivity", "app://ArticleTypeActivity", "app://CouponZoneActivity", "app://TopicDetailActivity", "app://AttendanceActivity", "app://VipZoneDetailActivity"};
    private String prefix = "app://";

    public HomePageNewAdapter(FragmentManager fm, List<HomeCommonBean> homeCommonBeanList) {
        super(fm);
        this.mHomeCommonBeanList = homeCommonBeanList;
        //筛选数据，防止版本api数据不同，会有不支持的原生链接
        List<String> actionList = Arrays.asList(actionArrays);
        Iterator<HomeCommonBean> iterator = mHomeCommonBeanList.iterator();
        while (iterator.hasNext()) {
            HomeCommonEntity.HomeCommonBean bean = iterator.next();
            String link = bean.getLink().trim();
            if (TextUtils.isEmpty(link)) {
                iterator.remove();
            } else if (link.contains(prefix)) {
                int prefixLength = link.indexOf(prefix) + prefix.length();
                int urlIndex = link.indexOf("?", prefixLength);
                if (urlIndex != -1) {
                    link = link.substring(link.indexOf(prefix), urlIndex).trim();
                }
                if (!actionList.contains(link)) {
                    iterator.remove();
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
                    case "app://QualityCustomTopicActivity"://自定义专区
                        return BaseFragment.newInstance(QualityCustomTopicFragment.class, urlParams, null);
                    case "app://ArticleTypeActivity"://种草特辑(文章分类)
                        return BaseFragment.newInstance(ArticleTypeFragment.class, urlParams, null);
                    case "app://CouponZoneActivity"://优惠券专区
                        return BaseFragment.newInstance(CouponZoneFragment.class, urlParams, null);
                    case "app://TopicDetailActivity"://话题详情
                        return BaseFragment.newInstance(TopicDetailFragment.class, urlParams, null);
                    case "app://VipZoneDetailActivity"://会员专区
                        return BaseFragment.newInstance(VipZoneDetailFragment.class, urlParams, null);
                    default:
                        return BaseFragment.newInstance(HomeDefalutFragment.class, null, null);
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
                    case "app://QualityWeekOptimizedActivity"://每周优选
                        return BaseFragment.newInstance(QualityWeekOptimizedFragment.class, null, null);
                    case "app://QualityShopBuyListActivity"://必买清单
                        return BaseFragment.newInstance(QualityShopBuyListFragment.class, null, null);
                    case "app://ArticleTypeActivity"://种草特辑(文章分类)
                        return BaseFragment.newInstance(ArticleTypeFragment.class, null, null);
                    case "app://AttendanceActivity"://签到
                        return BaseFragment.newInstance(AttendanceFragment.class, null, null);
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
