package com.amkj.dmsh.user.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.mine.activity.MineLoginActivity;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.user.adapter.UserPageAdapter;
import com.amkj.dmsh.user.bean.UserPagerInfoEntity;
import com.amkj.dmsh.user.bean.UserPagerInfoEntity.UserInfoBean;
import com.amkj.dmsh.utils.ImageConverterUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.views.flycoTablayout.SlidingTabLayout;
import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getPersonalInfo;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.showToastRequestMsg;
import static com.amkj.dmsh.constant.ConstantVariable.BASE_RESOURCE_DRAW;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;

;

/**
 * 用户主页
 */
public class UserPagerActivity extends BaseActivity {
    @BindView(R.id.smart_refresh_mine)
    RefreshLayout smart_refresh_mine;
    //高斯模糊背景
    @BindView(R.id.iv_user_header_bg)
    ImageView iv_user_header_bg;
    @BindView(R.id.tl_user_header)
    Toolbar tl_user_header;
    //名字 性别
    @BindView(R.id.tv_user_name)
    TextView tv_user_name;
    @BindView(R.id.cir_user_avatar)
    ImageView cir_user_avatar;
    @BindView(R.id.iv_user_sex)
    ImageView iv_user_sex;
    //    是否关注
    @BindView(R.id.tv_user_att_status)
    TextView tv_user_att_status;
    @BindView(R.id.communal_stl_tab)
    SlidingTabLayout communal_stl_tab;
    @BindView(R.id.vp_user_container)
    ViewPager vp_user_container;
    @BindView(R.id.user_appBarLayout)
    AppBarLayout user_appBarLayout;
    //    举报
    @BindView(R.id.tv_user_report)
    TextView tv_user_report;
    //    返回
    @BindView(R.id.iv_user_back)
    ImageView iv_user_back;
    private Bitmap bitmap;
    private int IS_ATTENTION_REQ_CODE = 10;
    private int mineId = 0;
    private String userId;
    private UserInfoBean userInfo;

    @Override
    protected int getContentView() {
        return R.layout.activity_user_pager_activity;
    }

    @Override
    protected void initViews() {
        isLoginStatus();
        tv_user_report.setVisibility(View.GONE);
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        communal_stl_tab.setTextsize(AutoSizeUtils.mm2px(mAppContext, 30));
        UserPageAdapter userPageAdapter = new UserPageAdapter(getSupportFragmentManager(), userId);
        vp_user_container.setAdapter(userPageAdapter);
        communal_stl_tab.setViewPager(vp_user_container);
        smart_refresh_mine.setOnRefreshListener(refreshLayout -> {
            loadData();
            EventBus.getDefault().post(new EventMessage("refreshMineData", 1));
        });
    }

    @Override
    protected void loadData() {
        getUserData();
    }

    private void getUserData() {
        String url = Url.BASE_URL + Url.USER_PAGE_INFO;
        Map<String, Object> params = new HashMap<>();
        params.put("userid", userId);
        if (mineId > 0) {
            params.put("uid", mineId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(this,url,params,new NetLoadListenerHelper(){
            @Override
            public void onSuccess(String result) {
                smart_refresh_mine.finishRefresh();
                Gson gson = new Gson();
                UserPagerInfoEntity pagerInfoBean = gson.fromJson(result, UserPagerInfoEntity.class);
                if (pagerInfoBean != null) {
                    if (pagerInfoBean.getCode().equals(SUCCESS_CODE)) {
                        userInfo = pagerInfoBean.getUserInfo();
                        setData(userInfo);
                    } else if (pagerInfoBean.getCode().equals(EMPTY_CODE)) {
                        showToast(UserPagerActivity.this, R.string.userDataNull);
                        finish();
                    } else {
                        showToast(UserPagerActivity.this, pagerInfoBean.getMsg());
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                smart_refresh_mine.finishRefresh();
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(UserPagerActivity.this, R.string.userDataNull);
                finish();
            }

            @Override
            public void netClose() {
                showToast(UserPagerActivity.this, R.string.unConnectedNetwork);
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
        tv_user_name.setText(!TextUtils.isEmpty(userInfoBean.getNickname()) ? userInfoBean.getNickname() : "");
        tv_user_name.setSelected(userInfo.getSex() == 0);
        tv_user_att_status.setText(userInfoBean.isFlag() ? "已关注" : "关注");
        tv_user_att_status.setSelected(userInfoBean.isFlag());
        if (0 <= userInfo.getSex() && userInfo.getSex() < 2) {
            iv_user_sex.setVisibility(View.VISIBLE);
            iv_user_sex.setSelected(userInfo.getSex() > 0);
        } else {
            iv_user_sex.setVisibility(View.GONE);
        }
        tv_user_att_status.setTag(userInfoBean);
    }

    public void setAttentionFlag() {
        String url = Url.BASE_URL + Url.UPDATE_ATTENTION;
        Map<String, Object> params = new HashMap<>();
        //用户id
        params.put("fuid", mineId);
        //关注id
        params.put("buid", userId);
        String flag;
        if (tv_user_att_status.isSelected()) {
            flag = "cancel";
        } else {
            flag = "add";
        }
        params.put("ftype", flag);
        NetLoadUtils.getNetInstance().loadNetDataPost(this,url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                loadHud.dismiss();
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        if (!tv_user_att_status.isSelected()) {
                            tv_user_att_status.setSelected(true);
                            tv_user_att_status.setText("已关注");
                            showToast(UserPagerActivity.this, "已关注");
                        } else {
                            tv_user_att_status.setSelected(false);
                            tv_user_att_status.setText("关注");
                            showToast(UserPagerActivity.this, "已取消关注");
                        }
                        loadData();
                    } else {
                        showToastRequestMsg(UserPagerActivity.this, requestStatus);
                    }
                }

            }

            @Override
            public void onNotNetOrException() {
                loadHud.dismiss();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bitmap != null) {
            bitmap.recycle();
            bitmap = null;
        }
    }


    //    返回
    @OnClick(R.id.iv_user_back)
    void goBack(View view) {
        finish();
    }

    //    关注
    @OnClick(R.id.tv_user_att_status)
    void goAttention(View view) {
        if (mineId > 0) {
            loadHud.show();
            setAttentionFlag();
        } else {
            getLoginStatus();
        }
    }

    @Override
    public void setStatusBar() {
        ImmersionBar.with(this).titleBar(tl_user_header).keyboardEnable(true).navigationBarEnable(false)
                .statusBarDarkFont(true).init();
    }
}
