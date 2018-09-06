package com.amkj.dmsh.release.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.release.adapter.RelevanceProductStatusAdapter;
import com.amkj.dmsh.release.bean.RelevanceProEntity.RelevanceProBean;
import com.flyco.tablayout.SlidingTabLayout;
import com.zhy.autolayout.utils.AutoUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.selRelevanceProduct;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/8/29
 * class description:关联商品列表
 */
public class RelevanceProListActivity extends BaseActivity {
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tv_header_shared)
    TextView tv_header_shared;
    @BindView(R.id.std_relevance_product)
    SlidingTabLayout std_relevance_product;
    @BindView(R.id.vp_relevance_product)
    ViewPager vp_relevance_product;

    //    选择条数
    private List<RelevanceProBean> relevanceSelProList = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.activity_relevance_product;
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
        tv_header_titleAll.setText("关联商品");
        tv_header_shared.setCompoundDrawables(null, null, null, null);
        tv_header_shared.setVisibility(View.VISIBLE);
        tv_header_shared.setSelected(true);
        tv_header_shared.setText("完成");
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            List<RelevanceProBean> relevanceList = bundle.getParcelableArrayList("relevanceList");
            if (relevanceList != null) {
                relevanceSelProList.clear();
                relevanceSelProList.addAll(relevanceList);
            } else {
                showToast(RelevanceProListActivity.this, R.string.variable_exception);
                finish();
            }
        } else {
            showToast(RelevanceProListActivity.this, R.string.variable_exception);
            finish();
        }
        std_relevance_product.setTextsize(AutoUtils.getPercentWidthSize(28));
        RelevanceProductStatusAdapter couponStatusAdapter = new RelevanceProductStatusAdapter(getSupportFragmentManager(), relevanceSelProList);
        vp_relevance_product.setAdapter(couponStatusAdapter);
        std_relevance_product.setViewPager(vp_relevance_product);
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        super.postEventResult(message);
        if ("relevanceProduct".equals(message.type)) {
            List<RelevanceProBean> relevanceSelProList = (List<RelevanceProBean>) message.result;
            this.relevanceSelProList.clear();
            if (relevanceSelProList != null && relevanceSelProList.size() > 0) {
                relevanceSelProList = selRelevanceProduct(relevanceSelProList);
                this.relevanceSelProList.addAll(relevanceSelProList);
            }
            EventBus.getDefault().post(new EventMessage("relevanceProductAll", this.relevanceSelProList));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void loadData() {
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }

    //  完成
    @OnClick(R.id.tv_header_shared)
    void completeData(View view) {
        Intent data = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("relevanceList", (ArrayList<? extends Parcelable>) relevanceSelProList);
        data.putExtras(bundle);
        setResult(RESULT_OK, data);
        finish();
    }
}
