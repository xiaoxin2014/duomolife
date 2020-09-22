package com.amkj.dmsh.mine.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.mine.adapter.ZeroApplyListAdapter;
import com.amkj.dmsh.views.flycoTablayout.SlidingTabLayout;

import org.greenrobot.eventbus.EventBus;

import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStringChangeIntegers;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.UPDATE_ZERO_APPLY_LIST;

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
    private String defaultStatus = "0";

    @Override
    protected int getContentView() {
        return R.layout.activity_my_zero_list;
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
        if (!TextUtils.isEmpty(getIntent().getStringExtra("defaultStatus"))) {
            defaultStatus = getIntent().getStringExtra("defaultStatus");
        }
        mTvHeaderTitle.setText("我的试用");
        mTvHeaderShared.setVisibility(View.GONE);
        ZeroApplyListAdapter zeroListAdapter = new ZeroApplyListAdapter(getSupportFragmentManager(), status);
        mVpVip.setAdapter(zeroListAdapter);
        mVpVip.setOffscreenPageLimit(titles.length - 1);
        mSlidingTablayout.setViewPager(mVpVip, titles);
        mSlidingTablayout.setCurrentTab(getStringChangeIntegers(defaultStatus));
    }

    @Override
    protected void loadData() {

    }

    @OnClick(R.id.tv_life_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            if (requestCode == IS_LOGIN_CODE) {
                finish();
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IS_LOGIN_CODE:
                EventBus.getDefault().post(new EventMessage(UPDATE_ZERO_APPLY_LIST));
                break;
        }
    }
}
