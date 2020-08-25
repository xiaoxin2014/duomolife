package com.amkj.dmsh.mine.activity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.mine.adapter.PowerPageAdapter;
import com.amkj.dmsh.mine.bean.PowerEntity;
import com.amkj.dmsh.mine.bean.PowerEntity.PowerBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.views.flycoTablayout.SlidingIconTextTabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xiaoxin on 2020/7/23
 * Version:v4.7.0
 * ClassDescription :会员权益
 */
public class VipPowerActivity extends BaseActivity {
    @BindView(R.id.tv_life_back)
    TextView mTvLifeBack;
    @BindView(R.id.tv_header_title)
    TextView mTvHeaderTitle;
    @BindView(R.id.tv_header_shared)
    TextView mTvHeaderShared;
    @BindView(R.id.tablayout_power)
    SlidingIconTextTabLayout mTablayoutPower;
    @BindView(R.id.vp_power_info)
    ViewPager mVpPowerInfo;
    @BindView(R.id.tv_open_vip)
    TextView mTvOpenVip;
    @BindView(R.id.ll_power)
    LinearLayout mLlPower;
    private PowerEntity mPowerEntity;
    private List<PowerBean> mPowerList = new ArrayList<>();
    private PowerPageAdapter mPowerPageAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_vip_power;
    }

    @Override
    protected void initViews() {
        mTvHeaderTitle.setText("多么会员权益");
    }

    @Override
    protected void loadData() {
        getPowerList();
    }

    //获取权益列表
    private void getPowerList() {
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.GET_VIP_POWER, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mPowerEntity = GsonUtils.fromJson(result, PowerEntity.class);
                if (mPowerEntity != null) {
                    List<PowerBean> powerList = mPowerEntity.getPowerList();
                    mPowerList.clear();
                    if (powerList != null && powerList.size() > 0) {
                        mPowerList.addAll(powerList);
                    }
                    mPowerPageAdapter = new PowerPageAdapter(getSupportFragmentManager(), mPowerList);
                    mVpPowerInfo.setAdapter(mPowerPageAdapter);
                    mVpPowerInfo.setOffscreenPageLimit(mPowerList.size() - 1);
                    mTablayoutPower.setViewPager(mVpPowerInfo, mPowerList);
                }
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mPowerList, mPowerEntity);
            }

            @Override
            public void onNotNetOrException() {
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mPowerList, mPowerEntity);
            }
        });
    }

    @OnClick({R.id.tv_life_back, R.id.tv_open_vip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_life_back:
                finish();
                break;
            //开通vip
            case R.id.tv_open_vip:
                break;
        }
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    public View getLoadView() {
        return mLlPower;
    }
}
