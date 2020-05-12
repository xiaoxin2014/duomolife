package com.amkj.dmsh.homepage.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.homepage.adapter.LotteryAwardPersonAdapter;
import com.amkj.dmsh.homepage.bean.IntegralLotteryEntity.PreviousInfoBean.WinListBean;
import com.amkj.dmsh.user.activity.UserPagerActivity;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;

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
        lotteryAwardPersonAdapter = new LotteryAwardPersonAdapter(this, winListBeanList);
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_gray_f_one_px).create());
        communal_recycler.setAdapter(lotteryAwardPersonAdapter);
        lotteryAwardPersonAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                WinListBean winListBean = (WinListBean) view.getTag();
                if (winListBean != null && winListBean.getUid() > 0 ) {
                    if(winListBean.getUid() != userId){
                        Intent intent = new Intent(IntegralLotteryAwardPersonActivity.this, UserPagerActivity.class);
                        intent.putExtra("userId", String.valueOf(winListBean.getUid()));
                        startActivity(intent);
                    }else{
                        showToast("恭喜你，已中奖");
                    }
                }
            }
        });
    }

    @Override
    protected void loadData() {
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }
}
