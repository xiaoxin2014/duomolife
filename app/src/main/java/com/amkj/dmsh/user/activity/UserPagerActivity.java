package com.amkj.dmsh.user.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dao.SoftApiDao;
import com.amkj.dmsh.find.adapter.UserPostPagerAdapter;
import com.amkj.dmsh.find.bean.EventMessageBean;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.user.bean.UserPagerInfoEntity.UserInfoBean;
import com.amkj.dmsh.utils.ImageConverterUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.views.flycoTablayout.SlidingTabLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.gyf.barlibrary.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getStringChangeIntegers;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantVariable.BASE_RESOURCE_DRAW;
import static com.amkj.dmsh.constant.ConstantVariable.DELETE_POST;
import static com.amkj.dmsh.constant.ConstantVariable.UPDATE_FOLLOW_STATUS;
import static com.amkj.dmsh.constant.ConstantVariable.UPDATE_POST_CONTENT;
import static com.amkj.dmsh.constant.ConstantVariable.UPDATE_USER_PAGER;


/**
 * 用户主页
 */
public class UserPagerActivity extends BaseActivity {
    @BindView(R.id.smart_refresh_mine)
    SmartRefreshLayout smart_refresh_mine;
    //高斯模糊背景
    @BindView(R.id.iv_user_header_bg)
    ImageView iv_user_header_bg;
    @BindView(R.id.rl_head)
    RelativeLayout tl_user_header;
    //名字 性别
    @BindView(R.id.tv_user_name)
    TextView tv_user_name;
    @BindView(R.id.cir_user_avatar)
    ImageView cir_user_avatar;
    @BindView(R.id.iv_user_sex)
    ImageView iv_user_sex;
    @BindView(R.id.communal_stl_tab)
    SlidingTabLayout communal_stl_tab;
    @BindView(R.id.vp_user_container)
    ViewPager vp_post;
    @BindView(R.id.user_appBarLayout)
    AppBarLayout user_appBarLayout;
    @BindView(R.id.tv_follow_num)
    TextView mTvFollowNum;
    @BindView(R.id.tv_fans_num)
    TextView mTvFansNum;
    @BindView(R.id.tv_follow)
    TextView mTvFollow;
    @BindView(R.id.ll_user_info)
    LinearLayout mLlUserInfo;
    private Bitmap bitmap;
    private String userId;
    private String[] titles = {"最新", "最热"};
    private UserInfoBean userInfoBean;

    @Override
    protected int getContentView() {
        return R.layout.activity_user_pager_activity;
    }

    @Override
    protected void initViews() {
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        tl_user_header.setBackgroundResource(R.color.transparent);
        communal_stl_tab.setTextsize(AutoSizeUtils.mm2px(mAppContext, 30));
        UserPostPagerAdapter postPagerAdapter = new UserPostPagerAdapter(getSupportFragmentManager(), userId, titles);
        vp_post.setAdapter(postPagerAdapter);
        communal_stl_tab.setViewPager(vp_post);
        smart_refresh_mine.setOnRefreshListener(refreshLayout -> {
            smart_refresh_mine.finishRefresh(1000);
            updateCurrentPostFragment();
        });

        //收缩时，0到-390   展开时，-390到0
        user_appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            int abs = Math.abs(verticalOffset);
            int totalScrollRange = appBarLayout.getTotalScrollRange();
            float v = abs * 1.0f / totalScrollRange * 1.0f;
            mLlUserInfo.setAlpha(1 - v);
        });
    }

    @Override
    protected void loadData() {

    }

    private void setData() {
        GlideImageLoaderUtil.loadHeaderImg(UserPagerActivity.this, cir_user_avatar, !TextUtils.isEmpty(userInfoBean.getAvatar())
                ? ImageConverterUtils.getFormatImg(userInfoBean.getAvatar()) : "");
        GlideImageLoaderUtil.loadCenterCrop(UserPagerActivity.this, iv_user_header_bg, !TextUtils.isEmpty(userInfoBean.getBgimg_url())
                ? ImageConverterUtils.getFormatImg(userInfoBean.getBgimg_url()) : BASE_RESOURCE_DRAW + R.drawable.mine_no_login_bg);
        tv_user_name.setText(getStrings(userInfoBean.getNickname()));
        iv_user_sex.setSelected(userInfoBean.getSex() == 0);
        iv_user_sex.setVisibility(0 <= userInfoBean.getSex() && userInfoBean.getSex() < 2 ? View.VISIBLE : View.GONE);
        mTvFollowNum.setText(("关注 " + userInfoBean.getFllow()));
        mTvFansNum.setText(("粉丝 " + userInfoBean.getFans()));
        mTvFollow.setSelected(userInfoBean.isFocus());
        mTvFollow.setText(userInfoBean.isFocus() ? "已关注" : "+ 关注");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bitmap != null) {
            bitmap.recycle();
            bitmap = null;
        }
    }

    @Override
    public void setStatusBar() {
        ImmersionBar.with(this).titleBar(tl_user_header).keyboardEnable(true).navigationBarEnable(false)
                .statusBarDarkFont(true).init();
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    public View getLoadView() {
        return smart_refresh_mine;
    }

    @OnClick({R.id.tv_follow_num, R.id.tv_fans_num, R.id.tv_life_back, R.id.iv_img_share, R.id.tv_follow})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.tv_follow_num:
                intent = new Intent(this, UserFansAttentionActivity.class);
                intent.putExtra("type", "attention");
                intent.putExtra("userId", getStringChangeIntegers(userId));
                startActivity(intent);
                break;
            case R.id.tv_fans_num:
                intent = new Intent(this, UserFansAttentionActivity.class);
                intent.putExtra("type", "fans");
                intent.putExtra("userId", getStringChangeIntegers(userId));
                startActivity(intent);
                break;
            case R.id.tv_life_back:
                finish();
                break;
            case R.id.iv_img_share:
                if (userInfoBean != null) {
                    new UMShareAction(this, userInfoBean.getAvatar(),
                            "快来多么生活看看我的分享吧~",
                            "幸福生活美美哒~",
                            Url.BASE_SHARE_PAGE_TWO + "mine/other_finds.html?fuid=" + userId,
                            getStringChangeIntegers(userId));
                }
                break;
            case R.id.tv_follow:
                SoftApiDao.followUser(this, getStringChangeIntegers(userId), mTvFollow, null);
                break;
        }
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        String type = message.type;
        if (DELETE_POST.equals(type)) {
            updateCurrentPostFragment();
        } else if (UPDATE_FOLLOW_STATUS.equals(type)) {
            userInfoBean.setIsFocus((boolean) message.result);
            setData();
        } else if (UPDATE_USER_PAGER.equals(type)) {
            userInfoBean = (UserInfoBean) message.result;
            setData();
            if (userInfoBean != null) {
                NetLoadUtils.getNetInstance().showLoadSirSuccess(loadService);
            } else {
                NetLoadUtils.getNetInstance().showLoadSirLoadFailed(loadService);
            }
        }
    }

    private void updateCurrentPostFragment() {
        //通知当前选中的帖子类型列表刷新
        EventBus.getDefault().post(new EventMessage(UPDATE_POST_CONTENT, new EventMessageBean(getActivity().getClass().getSimpleName(), titles[vp_post.getCurrentItem()])));
    }
}
