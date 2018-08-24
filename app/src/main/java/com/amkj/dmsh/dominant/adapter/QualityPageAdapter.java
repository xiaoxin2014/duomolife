package com.amkj.dmsh.dominant.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.bean.QualityTypeEntity.QualityTypeBean;
import com.amkj.dmsh.dominant.fragment.QualityCustomTopicFragment;
import com.amkj.dmsh.dominant.fragment.QualityDefaultNewFragment;
import com.amkj.dmsh.dominant.fragment.QualityNormalFragment;
import com.amkj.dmsh.dominant.fragment.QualityOverseasMailFragment;
import com.amkj.dmsh.homepage.fragment.AliBCFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsInteger;
import static com.amkj.dmsh.constant.ConstantVariable.CATEGORY_CHILD;
import static com.amkj.dmsh.constant.ConstantVariable.CATEGORY_ID;
import static com.amkj.dmsh.constant.ConstantVariable.CATEGORY_NAME;
import static com.amkj.dmsh.constant.ConstantVariable.CATEGORY_TYPE;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/6/5
 * class description:请输入类描述
 */

public class QualityPageAdapter extends FragmentPagerAdapter {
    private final List<QualityTypeBean> qualityTypeBeanList = new ArrayList<>();
    private Map<String, String> params = new HashMap<>();

    public QualityPageAdapter(FragmentManager fm, List<QualityTypeBean> qualityTypeBeanList) {
        super(fm);
        this.qualityTypeBeanList.addAll(qualityTypeBeanList);
    }

    @Override
    public Fragment getItem(int position) {
        QualityTypeBean qualityTypeBean = qualityTypeBeanList.get(position);
        params.clear();
        switch (qualityTypeBean.getType()) {
            case 2:
                params.put("loadUrl", getStrings(qualityTypeBean.getWebLink()));
                return BaseFragment.newInstance(AliBCFragment.class, params, null);
            case 3:
                params.put(CATEGORY_ID, getStringsInteger(qualityTypeBean.getId()));
                params.put(CATEGORY_TYPE, getStringsInteger(qualityTypeBean.getType()));
                return BaseFragment.newInstance(QualityOverseasMailFragment.class, params, null);
            case 4:
                params.put("productType", getStringsInteger(qualityTypeBean.getRelateId()));
                return BaseFragment.newInstance(QualityCustomTopicFragment.class, params, null);
            case 5:
                return BaseFragment.newInstance(QualityDefaultNewFragment.class, null, null);
            default:
                params.put(CATEGORY_TYPE, String.valueOf(qualityTypeBean.getCategoryType()));
                params.put(CATEGORY_ID, String.valueOf(qualityTypeBean.getRelateId()));
                params.put(CATEGORY_NAME, getStrings(qualityTypeBean.getRelateName()));
                params.put(CATEGORY_CHILD, getStrings(qualityTypeBean.getChildCategory()));
                return BaseFragment.newInstance(QualityNormalFragment.class, params, null);
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return qualityTypeBeanList.get(position).getName();
    }

    @Override
    public int getCount() {
        return qualityTypeBeanList.size();
    }
}
