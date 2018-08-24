package com.amkj.dmsh.shopdetails.integration;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.flyco.tablayout.SlidingTabLayout;
import com.zhy.autolayout.utils.AutoUtils;

import butterknife.BindView;
import butterknife.OnClick;
import cn.xiaoneng.coreapi.ChatParamsBody;
import cn.xiaoneng.coreapi.ItemParamsBody;
import cn.xiaoneng.utils.CoreData;

import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.skipXNService;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/8/8
 * version 3.1.5
 * class description:积分商品订单
 */
public class IntegralProductIndentActivity extends BaseActivity {
    @BindView(R.id.tb_indent_bar)
    Toolbar tb_indent_bar;
    @BindView(R.id.tv_indent_title)
    TextView tv_indent_title;
    @BindView(R.id.iv_indent_search)
    ImageView iv_indent_search;
    @BindView(R.id.stl_integral_indent)
    SlidingTabLayout stl_integral_indent;
    @BindView(R.id.vp_integral_indent_container)
    ViewPager vp_integral_indent_container;

    @Override
    protected int getContentView() {
        return R.layout.activity_integral_product_indent;
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
        tb_indent_bar.setSelected(true);
        tv_indent_title.setText("积分商品订单");
        iv_indent_search.setVisibility(View.GONE);
        setFragmentAdapter();
    }

    private void setFragmentAdapter() {
        if(userId>0){
            stl_integral_indent.setTextsize(AutoUtils.getPercentWidthSize(28));
            vp_integral_indent_container.setAdapter(new IntegralIndentPagerAdapter(getSupportFragmentManager()));
            stl_integral_indent.setViewPager(vp_integral_indent_container);
        }
    }

    @OnClick(R.id.tv_indent_back)
    void goBack() {
        finish();
    }


    @OnClick(R.id.iv_indent_service)
    void foundService() {
        ChatParamsBody chatParamsBody = new ChatParamsBody();
        chatParamsBody.startPageTitle = "积分订单列表";
        chatParamsBody.startPageUrl = "http://";
        ItemParamsBody itemParams = chatParamsBody.itemparams;
        itemParams.clicktoshow_type = CoreData.CLICK_TO_APP_COMPONENT;
        itemParams.appgoodsinfo_type = CoreData.SHOW_GOODS_BY_ID;
        itemParams.clientgoodsinfo_type = CoreData.SHOW_GOODS_BY_ID;
        skipXNService(this, chatParamsBody);
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IS_LOGIN_CODE) {
            setFragmentAdapter();
        }
    }
}
