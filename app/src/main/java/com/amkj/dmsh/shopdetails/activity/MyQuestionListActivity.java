package com.amkj.dmsh.shopdetails.activity;

import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.shopdetails.adapter.QuestionPagerAdapter;
import com.amkj.dmsh.views.flycoTablayout.SlidingTabLayout;

import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xiaoxin on 2021/4/9
 * Version:v5.1.0
 * ClassDescription :与我相关的问题列表
 */
public class MyQuestionListActivity extends BaseActivity {
    @BindView(R.id.tv_header_title)
    TextView mTvHeaderTitle;
    @BindView(R.id.tv_header_shared)
    TextView mTvHeaderShared;
    @BindView(R.id.communal_stl_tab)
    SlidingTabLayout mCommunalStlTab;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;

    @Override
    protected int getContentView() {
        return R.layout.activity_my_answer;
    }

    @Override
    protected void initViews() {
        mTvHeaderTitle.setText("我的回答");
        mTvHeaderShared.setVisibility(View.GONE);
        QuestionPagerAdapter answerPagerAdapter = new QuestionPagerAdapter(getSupportFragmentManager());
        mViewpager.setAdapter(answerPagerAdapter);
        mViewpager.setOffscreenPageLimit(3);
        mCommunalStlTab.setViewPager(mViewpager);

    }

    @Override
    protected void loadData() {

    }


    @OnClick(R.id.tv_life_back)
    public void onViewClicked() {
        finish();
    }
}
