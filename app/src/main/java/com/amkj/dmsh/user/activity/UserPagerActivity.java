package com.amkj.dmsh.user.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.find.adapter.UserPostPagerAdapter;
import com.amkj.dmsh.find.bean.PostTypeBean;
import com.amkj.dmsh.mine.activity.MineLoginActivity;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.user.bean.UserPagerInfoEntity;
import com.amkj.dmsh.user.bean.UserPagerInfoEntity.UserInfoBean;
import com.amkj.dmsh.utils.ImageConverterUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.views.flycoTablayout.SlidingTabLayout;
import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getPersonalInfo;
import static com.amkj.dmsh.constant.ConstantMethod.getStringChangeIntegers;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.BASE_RESOURCE_DRAW;
import static com.amkj.dmsh.constant.ConstantVariable.DELETE_POST;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.UPDATE_POST_CONTENT;


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
    private Bitmap bitmap;
    private int IS_ATTENTION_REQ_CODE = 10;
    private int mineId = 0;
    private String userId;
    private UserInfoBean userInfo;
    private String[] titles = {"最新", "最热"};
    private UserPagerInfoEntity mPagerInfoEntity;

    @Override
    protected int getContentView() {
        return R.layout.activity_user_pager_activity;
    }

    @Override
    protected void initViews() {
        isLoginStatus();
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        tl_user_header.setBackgroundResource(R.color.transparent);
        communal_stl_tab.setTextsize(AutoSizeUtils.mm2px(mAppContext, 30));
        UserPostPagerAdapter postPagerAdapter = new UserPostPagerAdapter(getSupportFragmentManager(), userId, titles);
        vp_post.setAdapter(postPagerAdapter);
        communal_stl_tab.setViewPager(vp_post);
        smart_refresh_mine.setOnRefreshListener(refreshLayout -> {
            loadData();
            updateCurrentPostFragment();
        });
    }

    @Override
    protected void loadData() {
        getUserData();
    }

    private void getUserData() {
        String url = Url.USER_PAGE_INFO;
        Map<String, Object> params = new HashMap<>();
        params.put("userid", userId);
        if (mineId > 0) {
            params.put("uid", mineId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(this, url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                smart_refresh_mine.finishRefresh();
                Gson gson = new Gson();
                mPagerInfoEntity = gson.fromJson(result, UserPagerInfoEntity.class);
                if (mPagerInfoEntity != null) {
                    if (mPagerInfoEntity.getCode().equals(SUCCESS_CODE)) {
                        userInfo = mPagerInfoEntity.getUserInfo();
                        setData(userInfo);
                    } else if (mPagerInfoEntity.getCode().equals(EMPTY_CODE)) {
                        showToast(UserPagerActivity.this, R.string.userDataNull);
                    } else {
                        showToast(UserPagerActivity.this, mPagerInfoEntity.getMsg());
                    }
                }

                NetLoadUtils.getNetInstance().showLoadSir(loadService, mPagerInfoEntity);
            }

            @Override
            public void onNotNetOrException() {
                smart_refresh_mine.finishRefresh();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mPagerInfoEntity);
            }
        });
    }

    private void isLoginStatus() {
        SavePersonalInfoBean personalInfo = getPersonalInfo(this);
        if (personalInfo.isLogin()) {
            //登陆成功，加载信息
            mineId = personalInfo.getUid();
        }
    }

    private void getLoginStatus() {
        SavePersonalInfoBean personalInfo = getPersonalInfo(this);
        if (personalInfo.isLogin()) {
            mineId = personalInfo.getUid();
        } else {
            //未登录跳转登录页
            Intent intent = new Intent(this, MineLoginActivity.class);
            startActivityForResult(intent, ConstantVariable.IS_LOGIN_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IS_ATTENTION_REQ_CODE) {
            getLoginStatus();
            loadData();
        } else if (requestCode == ConstantVariable.IS_LOGIN_CODE) {
            getLoginStatus();
        }

        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    private void setData(final UserInfoBean userInfoBean) {
        GlideImageLoaderUtil.loadHeaderImg(UserPagerActivity.this, cir_user_avatar, !TextUtils.isEmpty(userInfoBean.getAvatar())
                ? ImageConverterUtils.getFormatImg(userInfoBean.getAvatar()) : "");
        GlideImageLoaderUtil.loadCenterCrop(UserPagerActivity.this, iv_user_header_bg, !TextUtils.isEmpty(userInfoBean.getBgimg_url())
                ? ImageConverterUtils.getFormatImg(userInfoBean.getBgimg_url()) : BASE_RESOURCE_DRAW + R.drawable.mine_no_login_bg);
        tv_user_name.setText(getStrings(userInfoBean.getNickname()));
        iv_user_sex.setSelected(userInfo.getSex() == 0);
        iv_user_sex.setVisibility(0 <= userInfo.getSex() && userInfo.getSex() < 2 ? View.VISIBLE : View.GONE);
        mTvFollowNum.setText(("关注 " + userInfoBean.getFllow()));
        mTvFansNum.setText(("粉丝 " + userInfoBean.getFans()));
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

    @OnClick({R.id.tv_follow_num, R.id.tv_fans_num, R.id.tv_life_back, R.id.iv_img_share})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.tv_follow_num:
                intent = new Intent(this, UserFansAttentionActivity.class);
                intent.putExtra("type", "follow");
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
                if (mPagerInfoEntity != null) {

                }
                break;
        }
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (message.type.equals(DELETE_POST)) {
            updateCurrentPostFragment();
        }
    }

    private void updateCurrentPostFragment() {
        //通知当前选中的帖子类型列表刷新
        EventBus.getDefault().post(new EventMessage(UPDATE_POST_CONTENT, new PostTypeBean(getActivity().getClass().getSimpleName(), titles[vp_post.getCurrentItem()])));
    }
}
