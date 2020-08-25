package com.amkj.dmsh.mine.activity;

import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.dominant.adapter.QualityCustomAdapter;
import com.amkj.dmsh.views.flycoTablayout.SlidingTabLayout;

import java.util.Arrays;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xiaoxin on 2020/8/18
 * Version:v4.7.0
 * ClassDescription :会员专享价
 */
public class VipExclusiveActivity extends BaseActivity {
    @BindView(R.id.tv_life_back)
    TextView mTvLifeBack;
    @BindView(R.id.tv_header_title)
    TextView mTvHeaderTitle;
    @BindView(R.id.tv_header_shared)
    TextView mTvHeaderShared;
    @BindView(R.id.tl_normal_bar)
    Toolbar mTlNormalBar;
    @BindView(R.id.sliding_tablayout)
    SlidingTabLayout mSlidingTablayout;
    @BindView(R.id.vp_vip_exclusive)
    ViewPager mVpVipExclusive;
    private String[] titles = {"专区1", "专区2", "专区3", "专区4"};
    public final String[] CUSTOM_IDS = new String[]{"405", "406", "407", "408"};

    @Override
    protected int getContentView() {
        return R.layout.activity_vip_exclusive;
    }

    @Override
    protected void initViews() {
        mTvHeaderTitle.setText("会员专享价");
        mTvHeaderShared.setVisibility(View.GONE);
        QualityCustomAdapter qualityCustomAdapter = new QualityCustomAdapter(getSupportFragmentManager(), Arrays.asList(CUSTOM_IDS), getSimpleName(), 1);
        mVpVipExclusive.setAdapter(qualityCustomAdapter);
        mVpVipExclusive.setOffscreenPageLimit(titles.length - 1);
        mSlidingTablayout.setViewPager(mVpVipExclusive, titles);
    }

    @Override
    protected void loadData() {

    }

    @OnClick(R.id.tv_life_back)
    public void onViewClicked() {
        finish();
    }
}
