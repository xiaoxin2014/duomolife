package com.amkj.dmsh.dominant.initviews;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.amkj.dmsh.R;
import com.amkj.dmsh.dominant.activity.QualityGroupShopActivity;
import com.amkj.dmsh.dominant.activity.QualityGroupShopMineActivity;
import com.amkj.dmsh.views.DrawableCenterTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by xiaoxin on 2019/12/2 0018
 * Version：V4.4.0
 * ClassDescription :拼团相关底部控件
 */
public class GroupBottomView extends LinearLayout {
    @BindView(R.id.tv_quality_all_gp_sp)
    DrawableCenterTextView mTvQualityAllGpSp;

    public GroupBottomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View headView = LayoutInflater.from(context).inflate(R.layout.layout_group_shop_bottom, this, true);
        ButterKnife.bind(this, headView);
    }

    //拼团首页
    public void setHomeColor() {
        mTvQualityAllGpSp.setSelected(true);
        mTvQualityAllGpSp.setTextColor(getContext().getResources().getColor(R.color.text_normal_red));
        mTvQualityAllGpSp.setEnabled(false);
    }

    @OnClick({R.id.tv_quality_all_gp_sp, R.id.tv_quality_join_gp_sp})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_quality_all_gp_sp:
                Intent intent = new Intent(getContext(), QualityGroupShopActivity.class);
                getContext().startActivity(intent);
                break;
            case R.id.tv_quality_join_gp_sp:
                Intent intent2 = new Intent(getContext(), QualityGroupShopMineActivity.class);
                getContext().startActivity(intent2);
                break;
        }
    }
}



