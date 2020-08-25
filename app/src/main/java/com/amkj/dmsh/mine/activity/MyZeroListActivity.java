package com.amkj.dmsh.mine.activity;

import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.mine.adapter.ZeroApplyListAdapter;
import com.amkj.dmsh.views.flycoTablayout.SlidingTabLayout;

import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;

/**
 * Created by xiaoxin on 2020/8/17
 * Version:v4.7.0
 * ClassDescription :我的试用列表
 */
public class MyZeroListActivity extends BaseActivity {
    @BindView(R.id.tv_header_title)
    TextView mTvHeaderTitle;
    @BindView(R.id.tv_header_shared)
    TextView mTvHeaderShared;
    @BindView(R.id.sliding_tablayout)
    SlidingTabLayout mSlidingTablayout;
    @BindView(R.id.vp_vip_exclusive)
    ViewPager mVpVip;
    private String[] titles = {"申请中", "申请成功", "申请失败"};
    private String[] status = {"0", "1", "2"};

    @Override
    protected int getContentView() {
        return R.layout.activity_my_zero_list;
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
        mTvHeaderTitle.setText("我的试用");
        mTvHeaderShared.setVisibility(View.GONE);
        ZeroApplyListAdapter zeroListAdapter = new ZeroApplyListAdapter(getSupportFragmentManager(), status);
        mVpVip.setAdapter(zeroListAdapter);
        mVpVip.setOffscreenPageLimit(titles.length - 1);
        mSlidingTablayout.setViewPager(mVpVip, titles);
    }

    @Override
    protected void loadData() {

    }

    @OnClick(R.id.tv_life_back)
    public void onViewClicked() {
        finish();
    }
}
