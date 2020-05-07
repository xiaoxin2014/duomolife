package com.amkj.dmsh.dominant.activity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.dominant.bean.GroupShopDetailsEntity.GroupShopDetailsBean.ParticipantInfoBean.GroupShopJoinBean;
import com.amkj.dmsh.utils.ProductLabelCreateUtils;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.JustifyContent;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xiaoxin on 2019/12/27
 * Version:v4.4.0
 * ClassDescription :抽奖团全部中奖名单
 */
public class AllLotteryActivity extends BaseActivity {

    @BindView(R.id.flex_lottery_list)
    FlexboxLayout mFlexCommunalTag;
    @BindView(R.id.tv_header_title)
    TextView mTvHeaderTitle;
    @BindView(R.id.tv_header_shared)
    TextView mTvHeaderShared;
    @BindView(R.id.tv_lottery_list)
    TextView mTvLotteryList;
    @BindView(R.id.ll_lottery_list)
    LinearLayout mLlLotteryList;
    private List<GroupShopJoinBean> luckUserList = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.activity_all_lottery;
    }

    @Override
    protected void initViews() {
        mTvHeaderTitle.setText("中奖名单");
        mTvHeaderShared.setVisibility(View.GONE);
        mTvLotteryList.setTextColor(getResources().getColor(R.color.text_login_gray_s));
        if (getIntent() != null) {
            String allLottery = getIntent().getStringExtra("allLotteryJson");
            luckUserList = GsonUtils.fromJson(allLottery, new TypeToken<List<GroupShopJoinBean>>() {
            }.getType());
            mLlLotteryList.setVisibility(luckUserList.size() > 0 ? View.VISIBLE : View.GONE);
            mTvLotteryList.setText(luckUserList.size() + "名幸运儿");
        }
    }

    @Override
    protected void loadData() {
        mFlexCommunalTag.setJustifyContent(JustifyContent.CENTER);
        mFlexCommunalTag.removeAllViews();
        for (int i = 0; i < luckUserList.size(); i++) {
            mFlexCommunalTag.addView(ProductLabelCreateUtils
                    .createOpenGroupUserInfo(this, luckUserList.get(i)));
        }
    }


    @OnClick({R.id.tv_life_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_life_back:
                finish();
                break;
        }
    }
}
