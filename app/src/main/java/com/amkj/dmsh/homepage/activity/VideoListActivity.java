package com.amkj.dmsh.homepage.activity;

import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.homepage.adapter.VideoListPagerAdapter;
import com.amkj.dmsh.views.flycoTablayout.SlidingTabLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;

/**
 * Created by xiaoxin on 2021/2/23
 * Version:v5.0.0
 * ClassDescription :视频商品列表首页
 */
public class VideoListActivity extends BaseActivity {
    @BindView(R.id.tv_life_back)
    TextView mTvLifeBack;
    @BindView(R.id.tv_header_title)
    TextView mTvHeaderTitle;
    @BindView(R.id.tv_header_shared)
    TextView mTvHeaderShared;
    @BindView(R.id.tl_normal_bar)
    Toolbar mTlNormalBar;
    @BindView(R.id.communal_stl_tab)
    SlidingTabLayout mCommunalStlTab;
    @BindView(R.id.vp_content_contain)
    ViewPager mVpContentContain;
    @BindView(R.id.view_divider)
    View mViewDivider;

    @Override
    protected int getContentView() {
        return R.layout.activity_video_list;
    }

    @Override
    protected void initViews() {
        mTvHeaderTitle.setText("发现好货");
        mTvHeaderShared.setVisibility(View.GONE);
        mViewDivider.setVisibility(View.GONE);
        mCommunalStlTab.setTextsize(AutoSizeUtils.mm2px(mAppContext, 28));
        VideoListPagerAdapter videoListPagerAdapter = new VideoListPagerAdapter(getSupportFragmentManager());
        mVpContentContain.setOffscreenPageLimit(videoListPagerAdapter.getCount() - 1);
        mVpContentContain.setAdapter(videoListPagerAdapter);
        mCommunalStlTab.setViewPager(mVpContentContain);
    }

    @Override
    protected void loadData() {

    }

    @OnClick(R.id.tv_life_back)
    public void onViewClicked() {
        finish();
    }
}
