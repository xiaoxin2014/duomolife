package com.amkj.dmsh.time.activity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.mine.bean.VipExclusiveInfoEntity;
import com.amkj.dmsh.mine.bean.VipExclusiveInfoEntity.VipExclusiveInfoBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.time.adapter.TimePostPagerAdapter;
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
 * Created by xiaoxin on 2020/9/29
 * Version:v4.8.0
 * ClassDescription :团购商品种草帖子分类
 */
public class TimePostCatergoryActivity extends BaseActivity {
    @BindView(R.id.tv_life_back)
    TextView mTvLifeBack;
    @BindView(R.id.tv_header_title)
    TextView mTvHeaderTitle;
    @BindView(R.id.tv_header_shared)
    TextView mTvHeaderShared;
    @BindView(R.id.tl_normal_bar)
    Toolbar mTlNormalBar;
    @BindView(R.id.sliding_tablayout)
    SlidingTabLayout mSlidingTablayout;
    @BindView(R.id.vp_group)
    ViewPager mVpGroup;
    @BindView(R.id.ll_content)
    LinearLayout mLlContent;
    private List<VipExclusiveInfoBean> mList;

    @Override
    protected int getContentView() {
        return R.layout.activity_time_post_catergory;
    }

    @Override
    protected void initViews() {
        mTvHeaderShared.setVisibility(View.GONE);
        mTvHeaderTitle.setText("好物推荐");
    }

    @Override
    protected void loadData() {
        getVipExclusiveInfo();
    }

    //获取会员专享价专区id和标题
    private void getVipExclusiveInfo() {
        Map<String, Object> map = new HashMap<>();
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.GET_GROUP_CATEGORY_LIST, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                VipExclusiveInfoEntity vipExclusiveInfoEntity = GsonUtils.fromJson(result, VipExclusiveInfoEntity.class);
                if (vipExclusiveInfoEntity != null) {
                    mList = vipExclusiveInfoEntity.getResult();
                    if (mList != null && mList.size() > 0) {
                        //初始化自定义专区
                        TimePostPagerAdapter timePostPagerAdapter = new TimePostPagerAdapter(getSupportFragmentManager(), mList);
                        mVpGroup.setAdapter(timePostPagerAdapter);
                        mVpGroup.setOffscreenPageLimit(mList.size() - 1);
                        mSlidingTablayout.setViewPager(mVpGroup);
                    }
                }

                NetLoadUtils.getNetInstance().showLoadSir(loadService, mList);
            }

            @Override
            public void onNotNetOrException() {
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mList);
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
