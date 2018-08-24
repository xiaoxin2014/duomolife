package com.amkj.dmsh.shopdetails.activity;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.mine.adapter.GoodsSalePagerAdapter;
import com.flyco.tablayout.SlidingTabLayout;
import com.zhy.autolayout.utils.AutoUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by atd48 on 2016/8/24.
 * 退货售后
 */
public class DirectGoodsSaleAfterActivity extends BaseActivity {
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tv_header_shared)
    TextView header_shared;
    @BindView(R.id.search_sliding_bar)
    SlidingTabLayout slidingBar;
    @BindView(R.id.vp_dm_indent_container)
    ViewPager vp_dm_indent_container;

    @Override
    protected int getContentView() {
        return R.layout.activity_quality_indent;
    }

    @Override
    protected void initViews() {
        header_shared.setCompoundDrawables(null, null, null, null);
        header_shared.setTextColor(getResources().getColor(R.color.textColor_blue));
        header_shared.setVisibility(View.GONE);
        tv_header_titleAll.setText("退货/售后");
        GoodsSalePagerAdapter salePagerAdapter = new GoodsSalePagerAdapter(getSupportFragmentManager());
        vp_dm_indent_container.setAdapter(salePagerAdapter);
        slidingBar.setTextsize(AutoUtils.getPercentWidth1px() * 28);
        slidingBar.setViewPager(vp_dm_indent_container);
}

    @Override
    protected void loadData() {}

    @OnClick(R.id.tv_life_back)
    void goBack() {
        finish();
    }

}
