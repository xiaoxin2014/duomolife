package com.amkj.dmsh.homepage.activity;

import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.homepage.adapter.IntegralProductTypeAdapter;
import com.amkj.dmsh.views.tablayout.SlidingTabLayout;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/8/3
 * version 3.1.5
 * class description:积分商城商品列表
 */
public class IntegralProductShopActivity extends BaseActivity{
    @BindView(R.id.tv_header_shared)
    TextView header_shared;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tl_normal_bar)
    Toolbar tl_normal_bar;
    @BindView(R.id.std_integral_product)
    SlidingTabLayout std_integral_product;
    @BindView(R.id.vp_integral_product)
    ViewPager vp_integral_product;
    private String[] integralType = {"全部商品","积分抵扣","积分兑换"};

    @Override
    protected int getContentView() {
        return R.layout.activity_integral_product_shop;
    }

    @Override
    protected void initViews() {
        header_shared.setVisibility(View.GONE);
        tv_header_titleAll.setText("积分商城");
        tl_normal_bar.setSelected(true);
        std_integral_product.setTextsize(AutoSizeUtils.mm2px(mAppContext,30));
        std_integral_product.setTabPadding(AutoSizeUtils.mm2px(mAppContext,30));
        IntegralProductTypeAdapter integralProductType = new IntegralProductTypeAdapter(getSupportFragmentManager(), integralType);
        vp_integral_product.setAdapter(integralProductType);
        std_integral_product.setViewPager(vp_integral_product, integralType);
        vp_integral_product.setCurrentItem(0);
    }

    @Override
    protected void loadData() {}

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }
}
