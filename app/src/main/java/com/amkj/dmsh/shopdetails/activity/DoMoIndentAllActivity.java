package com.amkj.dmsh.shopdetails.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.homepage.activity.HomePageSearchActivity;
import com.amkj.dmsh.qyservice.QyServiceUtils;
import com.amkj.dmsh.shopdetails.adapter.IndentPagerAdapter;
import com.amkj.dmsh.views.flycoTablayout.SlidingTabLayout;
import com.amkj.dmsh.views.flycoTablayout.listener.OnTabSelectListener;;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SEARCH_INDENT;
import static com.amkj.dmsh.constant.ConstantVariable.SEARCH_TYPE;


public class DoMoIndentAllActivity extends BaseActivity {
    @BindView(R.id.communal_stl_tab)
    SlidingTabLayout communal_stl_tab;
    @BindView(R.id.vp_indent_container)
    ViewPager vp_indent_container;
    private String type = "";
    public static final String INDENT_TYPE = "inquiryOrder";
    private IndentPagerAdapter indentPagerAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_duomo_indent_all;
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
        Intent intent = getIntent();
        type = intent.getStringExtra("tab");
        if (TextUtils.isEmpty(type)) {
            type = "all";
        }
        setIndentTabData();
    }

    private void setIndentTabData() {
        if (userId < 1) {
            return;
        }
        communal_stl_tab.setTextsize(AutoSizeUtils.mm2px(mAppContext, 28));
        communal_stl_tab.setTextUnselectColor(getResources().getColor(R.color.text_login_gray_s));
        indentPagerAdapter = new IndentPagerAdapter(getSupportFragmentManager());
        vp_indent_container.setAdapter(indentPagerAdapter);
        communal_stl_tab.setViewPager(vp_indent_container);
        communal_stl_tab.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                vp_indent_container.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
        if (!TextUtils.isEmpty(type)) {
            int position;
            switch (type) {
                case "waitPay":
                    position = 1;
                    break;
                case "waitSend":
                    position = 2;
                    break;
                case "delivered":
                    position = 3;
                    break;
                case "appraise":
                    position = 4;
                    break;
                default:
                    position = 0;
                    break;
            }
            communal_stl_tab.setCurrentTab(position);
        } else {
            communal_stl_tab.setCurrentTab(0);
        }

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
        if (requestCode == IS_LOGIN_CODE) {
            setIndentTabData();
        }
    }

    @Override
    protected void loadData() {
    }

    @OnClick(R.id.tv_indent_back)
    void goBack(View view) {
        finish();
    }

    @OnClick(R.id.iv_indent_search)
    void skipSearch(View view) {
        Intent intent = new Intent(DoMoIndentAllActivity.this, HomePageSearchActivity.class);
        intent.putExtra(SEARCH_TYPE, SEARCH_INDENT);
        startActivity(intent);
    }

    @OnClick(R.id.iv_indent_service)
    void skipService() {
        QyServiceUtils.getQyInstance()
                .openQyServiceChat(DoMoIndentAllActivity.this
                        , "订单列表", "");
    }
}
