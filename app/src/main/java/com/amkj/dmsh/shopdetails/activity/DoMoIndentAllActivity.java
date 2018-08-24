package com.amkj.dmsh.shopdetails.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.homepage.activity.HomePageSearchActivity;
import com.amkj.dmsh.mine.activity.MineLoginActivity;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.shopdetails.adapter.IndentPagerAdapter;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.zhy.autolayout.utils.AutoUtils;

import butterknife.BindView;
import butterknife.OnClick;
import cn.xiaoneng.coreapi.ChatParamsBody;

import static com.amkj.dmsh.constant.ConstantMethod.getPersonalInfo;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.skipInitDataXNService;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_SERVICE_PAGE_URL;
import static com.amkj.dmsh.constant.ConstantVariable.SEARCH_INDENT;
import static com.amkj.dmsh.constant.ConstantVariable.SEARCH_TYPE;

;

public class DoMoIndentAllActivity extends BaseActivity {
    @BindView(R.id.communal_stl_tab)
    SlidingTabLayout communal_stl_tab;
    @BindView(R.id.vp_indent_container)
    ViewPager vp_indent_container;
    private String type = "";
    private int uid;
    public static final String INDENT_TYPE = "inquiryOrder";
    private IndentPagerAdapter indentPagerAdapter;
    private String avatar;
    @Override
    protected int getContentView() {
        return R.layout.activity_duomo_indent_all;
    }
    @Override
    protected void initViews() {
        getLoginStatus();
        Intent intent = getIntent();
        type = intent.getStringExtra("tab");
        communal_stl_tab.setTextsize(AutoUtils.getPercentWidth1px() * 28);
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
            int position = 0;
            switch (type) {
                case "all":
                    position = 0;
                    break;
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
            }
            communal_stl_tab.setCurrentTab(position);
        } else {
            communal_stl_tab.setCurrentTab(0);
        }
    }

    private void getLoginStatus() {
        SavePersonalInfoBean personalInfo = getPersonalInfo(this);
        if (personalInfo.isLogin()) {
            uid = personalInfo.getUid();
            avatar = personalInfo.getAvatar();
        } else {
            Intent intent = new Intent(this, MineLoginActivity.class);
            startActivityForResult(intent, ConstantVariable.IS_LOGIN_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ConstantVariable.IS_LOGIN_CODE) {
                getLoginStatus();
            }
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
        intent.putExtra(SEARCH_TYPE,SEARCH_INDENT);
        startActivity(intent);
    }

    @OnClick(R.id.iv_indent_service)
    void skipService() {
        ChatParamsBody chatParamsBody = new ChatParamsBody();
        chatParamsBody.startPageTitle = getStrings("订单列表");
        chatParamsBody.startPageUrl = DEFAULT_SERVICE_PAGE_URL;
        if(uid>0){
            chatParamsBody.headurl = avatar;
        }
        skipInitDataXNService(DoMoIndentAllActivity.this,chatParamsBody);
    }
}
