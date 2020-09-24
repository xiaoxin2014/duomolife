package com.amkj.dmsh.mine.activity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.mine.adapter.VipExclusivePagerAdapter;
import com.amkj.dmsh.mine.bean.VipExclusiveInfoEntity;
import com.amkj.dmsh.mine.bean.VipExclusiveInfoEntity.VipExclusiveInfoBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.views.flycoTablayout.SlidingTabLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by xiaoxin on 2020/8/18
 * Version:v4.7.0
 * ClassDescription :会员专享价列表
 */
public class VipExclusiveActivity extends BaseActivity {
    @BindView(R.id.tv_life_back)
    TextView mTvLifeBack;
    @BindView(R.id.tv_header_title)
    TextView mTvHeaderTitle;
    @BindView(R.id.tv_header_shared)
    TextView mTvHeaderShared;
    @BindView(R.id.tl_normal_bar)
    Toolbar mTlNormalBar;
    @BindView(R.id.sliding_tablayout)
    SlidingTabLayout mTablayoutVip;
    @BindView(R.id.vp_vip_exclusive)
    ViewPager mVpVip;
    @BindView(R.id.ll_content)
    LinearLayout mLlContent;
    private List<VipExclusiveInfoBean> mInfos;


    @Override
    protected int getContentView() {
        return R.layout.activity_vip_exclusive;
    }

    @Override
    protected void initViews() {
        mTvHeaderTitle.setText("会员专享价");
        mTvHeaderShared.setVisibility(View.GONE);
    }

    @Override
    protected void loadData() {
        getVipExclusiveInfo();
    }

    //获取会员专享价专区id和标题
    private void getVipExclusiveInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("isHomePage", 0);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.GET_VIP_EXCLUSIVE_TITLE, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                VipExclusiveInfoEntity vipExclusiveInfoEntity = GsonUtils.fromJson(result, VipExclusiveInfoEntity.class);
                if (vipExclusiveInfoEntity != null) {
                    mInfos = vipExclusiveInfoEntity.getResult();
                    if (mInfos != null && mInfos.size() > 0) {
                        //初始化自定义专区
                        VipExclusivePagerAdapter vipExclusivePagerAdapter = new VipExclusivePagerAdapter(getSupportFragmentManager(), mInfos, "0");
                        mVpVip.setAdapter(vipExclusivePagerAdapter);
                        mVpVip.setOffscreenPageLimit(mInfos.size() - 1);
                        mTablayoutVip.setViewPager(mVpVip);
                    }
                }

                NetLoadUtils.getNetInstance().showLoadSir(loadService, mInfos);
            }

            @Override
            public void onNotNetOrException() {
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mInfos);
            }
        });
    }

    @OnClick(R.id.tv_life_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    public View getLoadView() {
        return mLlContent;
    }
}
