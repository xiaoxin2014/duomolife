package com.amkj.dmsh.mine.activity;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.mine.adapter.MineContentPageAdapter;
import com.flyco.tablayout.SlidingTabLayout;
import com.zhy.autolayout.utils.AutoUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/8/10
 * class description:我的收藏
 */
public class MineCollectContentActivity extends BaseActivity {
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tv_header_shared)
    TextView tv_header_shared;
    @BindView(R.id.communal_stl_tab)
    SlidingTabLayout communal_stl_tab;
    @BindView(R.id.vp_content_contain)
    ViewPager vp_content_contain;
    @Override
    protected int getContentView() {
        return R.layout.activity_mine_col_content;
    }
    @Override
    protected void initViews() {
        tv_header_titleAll.setText("收藏内容");
        tv_header_shared.setVisibility(View.GONE);
        communal_stl_tab.setTextsize(AutoUtils.getPercentWidth1px() * 28);
        MineContentPageAdapter mineContentPageAdapter = new MineContentPageAdapter(getSupportFragmentManager());
        vp_content_contain.setAdapter(mineContentPageAdapter);
        communal_stl_tab.setViewPager(vp_content_contain);
    }

    @Override
    protected void loadData() {
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }

}
