package com.amkj.dmsh.dominant.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.dominant.bean.QualityHotSaleShaftEntity.HotSaleShaftBean;
import com.amkj.dmsh.dominant.fragment.QualityTypeHotSaleProductFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/10/12
 * version 3.1.7
 * class description:热销单品时间轴
 */
public class QualityHotSaleAdapter extends FragmentPagerAdapter {

    private final List<HotSaleShaftBean> hotSaleShaft = new ArrayList<>();

    public QualityHotSaleAdapter(FragmentManager supportFragmentManager, List<HotSaleShaftBean> hotSaleShaft) {
        super(supportFragmentManager);
        this.hotSaleShaft.addAll(hotSaleShaft);
    }

    @Override
    public Fragment getItem(int position) {
        HotSaleShaftBean hotSaleShaftBean = hotSaleShaft.get(position);
        Map<String,String> params = new HashMap<>();
        params.put("hotSaleDay",hotSaleShaftBean.getDay());
        return BaseFragment.newInstance(QualityTypeHotSaleProductFragment.class, params, null);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return hotSaleShaft.get(position).getTitle();
    }

    @Override
    public int getCount() {
        return hotSaleShaft!=null?hotSaleShaft.size():0;
    }
}
