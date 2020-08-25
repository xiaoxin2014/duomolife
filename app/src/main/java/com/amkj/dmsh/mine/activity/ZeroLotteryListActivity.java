package com.amkj.dmsh.mine.activity;

import android.text.TextUtils;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;

import static com.amkj.dmsh.constant.ConstantMethod.showToast;

/**
 * Created by xiaoxin on 2020/8/14
 * Version:v4.7.0
 * ClassDescription :往期0元试用中奖名单列表
 */
public class ZeroLotteryListActivity extends BaseActivity {
    @Override
    protected int getContentView() {
        return R.layout.activity_zero_lottery_list;
    }

    @Override
    protected void initViews() {
        if (TextUtils.isEmpty(getIntent().getStringExtra("activityId"))) {
            showToast("数据有误");
            finish();
        }


    }

    @Override
    protected void loadData() {

    }
}
