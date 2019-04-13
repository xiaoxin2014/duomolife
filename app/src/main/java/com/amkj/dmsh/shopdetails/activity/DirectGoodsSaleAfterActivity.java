package com.amkj.dmsh.shopdetails.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.mine.adapter.GoodsSalePagerAdapter;
import com.amkj.dmsh.views.tablayout.SlidingTabLayout;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;

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
        getLoginStatus(this);
        header_shared.setCompoundDrawables(null, null, null, null);
        header_shared.setTextColor(getResources().getColor(R.color.textColor_blue));
        header_shared.setVisibility(View.GONE);
        tv_header_titleAll.setText("退货/售后");
        setGoodsSaleAfter();
}

    private void setGoodsSaleAfter() {
        if(userId<1){
            return;
        }
        GoodsSalePagerAdapter salePagerAdapter = new GoodsSalePagerAdapter(getSupportFragmentManager());
        vp_dm_indent_container.setAdapter(salePagerAdapter);
        slidingBar.setTextsize(AutoSizeUtils.mm2px(mAppContext,28));
        slidingBar.setViewPager(vp_dm_indent_container);
    }

    @Override
    protected void loadData() {}

    @OnClick(R.id.tv_life_back)
    void goBack() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            if(requestCode == IS_LOGIN_CODE){
                finish();
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IS_LOGIN_CODE) {
            setGoodsSaleAfter();
        }
    }
}
