package com.amkj.dmsh.mine.activity;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.ConstantMethod;
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

import static com.amkj.dmsh.constant.ConstantMethod.getStringsFormat;
import static com.amkj.dmsh.constant.ConstantMethod.isVip;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;

/**
 * Created by xiaoxin on 2020/7/23
 * Version:v4.7.0
 * ClassDescription :会员权益
 */
public class VipPowerDetailActivity extends BaseActivity {
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
    @BindView(R.id.ll_open)
    LinearLayout mLlOpen;
    private PowerEntity mPowerEntity;
    private List<PowerBean> mPowerList = new ArrayList<>();
    private PowerPageAdapter mPowerPageAdapter;
    private String mPosition;

    @Override
    protected int getContentView() {
        return R.layout.activity_vip_power;
    }

    @Override
    protected void initViews() {
        mTvHeaderTitle.setText("多么会员权益");
        mTvHeaderShared.setVisibility(View.GONE);
        if (getIntent().getExtras() != null) {
            mPosition = getIntent().getStringExtra("position");
        }
    }

    @Override
    protected void loadData() {
        getPowerList();
        getVipInfo();
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
                    mTablayoutPower.setCurrentTab(ConstantMethod.getStringChangeIntegers(mPosition));
                }
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mPowerList, mPowerEntity);
            }

            @Override
            public void onNotNetOrException() {
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mPowerList, mPowerEntity);
            }
        });
    }

    //获取会员信息
    private void getVipInfo() {
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.GET_VIP_USER_INFO, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (SUCCESS_CODE.equals(requestStatus.getCode())) {
                        mTvOpenVip.setText(getStringsFormat(getActivity(), R.string.open_vip_price, requestStatus.getResult().getPriceText()));
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
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
                Intent intent = new Intent(this, OpenVipActivity.class);
                startActivity(intent);
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

    @Override
    protected void onResume() {
        super.onResume();
        mLlOpen.setVisibility(isVip() ? View.GONE : View.VISIBLE);
    }
}
