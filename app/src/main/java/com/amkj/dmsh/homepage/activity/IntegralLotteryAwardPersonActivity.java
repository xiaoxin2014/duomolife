package com.amkj.dmsh.homepage.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.homepage.adapter.LotteryAwardPersonAdapter;
import com.amkj.dmsh.homepage.bean.IntegralLotteryEntity.PreviousInfoBean.WinListBean;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/8/1
 * version 3.1.5
 * class description:往期夺宝中奖
 */
public class IntegralLotteryAwardPersonActivity extends BaseActivity {
    @BindView(R.id.tv_header_shared)
    TextView header_shared;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tl_normal_bar)
    Toolbar tl_normal_bar;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    private List<WinListBean> winListBeanList;
    private LotteryAwardPersonAdapter lotteryAwardPersonAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_integral_lottery_award_person;
    }

    @Override
    protected void initViews() {
        header_shared.setVisibility(View.GONE);
        tv_header_titleAll.setText("中奖名单");
        tl_normal_bar.setSelected(true);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            winListBeanList = bundle.getParcelableArrayList("lotteryWinner");
        }
        if (winListBeanList == null) {
            finish();
            return;
        }
        communal_recycler.setLayoutManager(new LinearLayoutManager(this));
        lotteryAwardPersonAdapter = new LotteryAwardPersonAdapter(this,winListBeanList);
        communal_recycler.addItemDecoration(new PinnedHeaderItemDecoration.Builder(-1)
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_gray_f_two_px)
                // 开启绘制分隔线，默认关闭
                .enableDivider(true)
                // 是否关闭标签点击事件，默认开启
                .disableHeaderClick(false)
                // 设置标签和其内部的子控件的监听，若设置点击监听不为null，但是disableHeaderClick(true)的话，还是不会响应点击事件
                .setHeaderClickListener(null)
                .create());
        communal_recycler.setAdapter(lotteryAwardPersonAdapter);
    }

    @Override
    protected void loadData() {}
    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }
}
