package com.amkj.dmsh.mine.activity;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.find.adapter.UserPostPagerAdapter;
import com.amkj.dmsh.find.bean.EventMessageBean;
import com.amkj.dmsh.views.flycoTablayout.SlidingTabLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantVariable.DELETE_POST;
import static com.amkj.dmsh.constant.ConstantVariable.UPDATE_POST_CONTENT;

/**
 * Created by xiaoxin on 2019/9/10
 * Version:v4.2.1
 * ClassDescription :我的帖子改版
 */
public class MyPostActivity extends BaseActivity {
    @BindView(R.id.tv_header_title)
    TextView mTvHeaderTitle;
    @BindView(R.id.tv_header_shared)
    TextView mTvHeaderShared;
    @BindView(R.id.communal_stl_tab)
    SlidingTabLayout mCommunalStlTab;
    @BindView(R.id.vp_user_container)
    ViewPager vp_post;
    @BindView(R.id.smart_refresh_mine)
    SmartRefreshLayout mSmartRefreshMine;
    private String[] titles = {"最新", "最热"};

    @Override
    protected int getContentView() {
        return R.layout.activity_my_post;
    }

    @Override
    protected void initViews() {
        mTvHeaderTitle.setText("我的帖子");
        mTvHeaderShared.setVisibility(View.GONE);
        UserPostPagerAdapter userPostPagerAdapter = new UserPostPagerAdapter(getSupportFragmentManager(), String.valueOf(ConstantMethod.userId), titles);
        vp_post.setAdapter(userPostPagerAdapter);
        mCommunalStlTab.setViewPager(vp_post);
        mSmartRefreshMine.setOnRefreshListener(refreshLayout -> {
            mSmartRefreshMine.finishRefresh(1000);
            updateCurrentPostFragment();
        });
    }

    @Override
    protected void loadData() {

    }

    @OnClick(R.id.tv_life_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (message.type.equals(DELETE_POST)) {
            updateCurrentPostFragment();
        }
    }

    //通知当前选中的帖子类型列表刷新
    private void updateCurrentPostFragment() {
        EventBus.getDefault().post(new EventMessage(UPDATE_POST_CONTENT, new EventMessageBean(getActivity().getClass().getSimpleName(), titles[vp_post.getCurrentItem()])));
    }
}
