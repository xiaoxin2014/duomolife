package com.amkj.dmsh.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.bean.CommunalUserInfoEntity;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.mine.bean.AuthorizeSuccessOtherData;
import com.amkj.dmsh.mine.bean.OtherAccountBindEntity;
import com.amkj.dmsh.mine.bean.OtherAccountBindEntity.OtherAccountBindInfo;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.release.dialogutils.AlertSettingBean;
import com.amkj.dmsh.release.dialogutils.AlertView;
import com.amkj.dmsh.release.dialogutils.OnAlertItemClickListener;
import com.amkj.dmsh.utils.Log;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.OTHER_QQ;
import static com.amkj.dmsh.constant.ConstantVariable.OTHER_SINA;
import static com.amkj.dmsh.constant.ConstantVariable.OTHER_WECHAT;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.umeng.socialize.bean.SHARE_MEDIA.QQ;
import static com.umeng.socialize.bean.SHARE_MEDIA.SINA;

;

public class AccountSafeActivity extends BaseActivity implements OnAlertItemClickListener {
    @BindView(R.id.tv_header_shared)
    TextView tv_share;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    //    绑定手机
    @BindView(R.id.tv_account_safe_mobile)
    TextView tv_account_safe_mobile;
    //    绑定微信
    @BindView(R.id.tv_account_safe_weChat)
    TextView tv_account_safe_weChat;
    @BindView(R.id.rel_weChat_account)
    RelativeLayout rel_weChat_account;
    //    绑定微博
    @BindView(R.id.tv_account_safe_weiBo)
    TextView tv_account_safe_weiBo;
    @BindView(R.id.rel_weiBo_account)
    RelativeLayout rel_weiBo_account;
    //    绑定QQ
    @BindView(R.id.tv_account_safe_qq)
    TextView tv_account_safe_qq;
    //    实名信息
    @BindView(R.id.tv_account_safe_real_info)
    TextView tv_account_safe_real_info;
    @BindView(R.id.rel_qq_account)
    RelativeLayout rel_qq_account;
    private final String CHANGE_PASSWORD = "changePassword";
    private int uid;
    private AlertView weChatDialog;
    private AlertView qqDialog;
    private AlertView sinaDialog;
    private UMShareAPI mShareAPI;
    private boolean isBindWeChat = false;
    private CommunalUserInfoEntity.CommunalUserInfoBean minaData;

    @Override
    protected int getContentView() {
        return R.layout.activity_account_safe;
    }

    @Override
    protected void initViews() {
        getLoginStatus();
        tv_share.setVisibility(View.INVISIBLE);
        tv_header_titleAll.setText("账户安全");
    }

    //  2016/9/21 获取账号列表
    @Override
    protected void loadData() {
        getCurrentAccountData();
        getOtherAccountData();
    }

    private void getCurrentAccountData() {
        String url = Url.BASE_URL + Url.MINE_PAGE + uid;
        XUtil.Get(url, null, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                CommunalUserInfoEntity minePageData = gson.fromJson(result, CommunalUserInfoEntity.class);
                if (minePageData != null) {
                    if (minePageData.getCode().equals("01")) {
                        minaData = minePageData.getCommunalUserInfoBean();
                        setMobileData(minaData);
                    } else {
                        showToast(AccountSafeActivity.this, minePageData.getMsg());
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.d(this, "onError:", ex);
            }
        });
    }

    private void setMobileData(CommunalUserInfoEntity.CommunalUserInfoBean minaData) {
        if (minaData.isMobile_verification()) {
            tv_account_safe_mobile.setText(!TextUtils.isEmpty(minaData.getMobile()) ? minaData.getMobile() : "已绑定");
        }
        if (!TextUtils.isEmpty(minaData.getIdcard())) {
            tv_account_safe_real_info.setText("已设置");
        } else {
            tv_account_safe_real_info.setText("未设置");
        }
    }

    private void getOtherAccountData() {
        String url = Url.BASE_URL + Url.MINE_SYNC_LOGIN;
        Map<String, Object> params = new HashMap<>();
        params.put("uid", uid);
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                OtherAccountBindEntity otherAccountBindEntity = gson.fromJson(result, OtherAccountBindEntity.class);
                if (otherAccountBindEntity != null) {
                    if (otherAccountBindEntity.getCode().equals("01")) {
                        setAccountData(otherAccountBindEntity.getOtherAccountBindInfo());
                    } else if (otherAccountBindEntity.getCode().equals("02")) {
                        setAccountData(null);
                    } else {
                        showToast(AccountSafeActivity.this, otherAccountBindEntity.getMsg());
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.d("AppDataActivity", "onError: " + ex);
            }
        });
    }

    private void setAccountData(List<OtherAccountBindInfo> otherAccountBindInfos) {
        if (otherAccountBindInfos != null) {
            for (int i = 0; i < otherAccountBindInfos.size(); i++) {
                switch (otherAccountBindInfos.get(i).getType()) {
                    case "wechat":
                        isBindWeChat = true;
                        tv_account_safe_weChat.setText("已绑定");
//                        tv_account_safe_weChat.setEnabled(false);
//                        rel_weChat_account.setEnabled(false);
                        break;
                    case "qq":
                        tv_account_safe_qq.setText("已绑定");
                        tv_account_safe_qq.setEnabled(false);
                        rel_qq_account.setEnabled(false);
                        break;
                    case "sina":
                        tv_account_safe_weiBo.setText("已绑定");
                        tv_account_safe_weiBo.setEnabled(false);
                        rel_weiBo_account.setEnabled(false);
                        break;
                }
            }
        }
    }

    //修改密码
    @OnClick(R.id.rel_chang_password)
    void skipSafe() {
        Intent intent = new Intent(AccountSafeActivity.this, ChangePasswordActivity.class);
        intent.putExtra("type", CHANGE_PASSWORD);
        intent.putExtra("uid", uid);
        startActivity(intent);
    }

    //绑定手机
    @OnClick(R.id.rel_mobile_account)
    void bindMobile() {
//                            跳转绑定手机页面
        OtherAccountBindInfo accountBind = new OtherAccountBindInfo();
        if (minaData != null) {
            if (minaData.getUid() != 0) {
                accountBind.setUid(minaData.getUid());
            }
            accountBind.setAvatar(getStrings(minaData.getAvatar()));
            accountBind.setSex(minaData.getSex());
            accountBind.setNickname(getStrings(minaData.getNickname()));
            accountBind.setMobile_verification(minaData.isMobile_verification());
            accountBind.setMobileNum(minaData.getMobile());
            accountBind.setMobile(minaData.getMobile());
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putParcelable("info", accountBind);
            intent.putExtras(bundle);
            if (minaData.isMobile_verification()) {
                intent.setClass(AccountSafeActivity.this, ChangeMobileActivity.class);
            } else {
                intent.setClass(AccountSafeActivity.this, BindingMobileActivity.class);
            }
            startActivity(intent);
        } else {
            return;
        }
    }

    //绑定微信
    @OnClick(R.id.rel_weChat_account)
    void bindWeChat() {
        AlertSettingBean alertSettingBean = new AlertSettingBean();
        AlertSettingBean.AlertData alertData = new AlertSettingBean.AlertData();
        if (isBindWeChat) {
            alertData.setCancelStr("取消");
            alertData.setDetermineStr("解绑");
            alertData.setFirstDet(true);
            alertData.setMsg("解除绑定后该微信订单只能在微信中\n查看，确定要解除该微信账号吗");
        } else {
            //弹窗 打开微信
            alertData.setCancelStr("取消");
            alertData.setDetermineStr("打开");
            alertData.setFirstDet(true);
            alertData.setMsg("“多么生活”想要打开“微信”");
        }
        alertSettingBean.setStyle(AlertView.Style.Alert);
        alertSettingBean.setAlertData(alertData);
        weChatDialog = new AlertView(alertSettingBean, this, this);
        weChatDialog.setCancelable(true);
        weChatDialog.show();
    }

    //绑定微博
    @OnClick(R.id.rel_weiBo_account)
    void bindWeiBo() {
        //弹窗 打开新浪
        AlertSettingBean alertSettingBean = new AlertSettingBean();
        AlertSettingBean.AlertData alertData = new AlertSettingBean.AlertData();
        alertData.setCancelStr("取消");
        alertData.setDetermineStr("打开");
        alertData.setFirstDet(true);
        alertData.setMsg("“多么生活”想要打开“微博”");
        alertSettingBean.setStyle(AlertView.Style.Alert);
        alertSettingBean.setAlertData(alertData);
        sinaDialog = new AlertView(alertSettingBean, this, this);
        sinaDialog.setCancelable(true);
        sinaDialog.show();
    }

    //绑定QQ
    @OnClick(R.id.rel_qq_account)
    void bindQQ() {
        //弹窗 打开QQ
        AlertSettingBean alertSettingBean = new AlertSettingBean();
        AlertSettingBean.AlertData alertData = new AlertSettingBean.AlertData();
        alertData.setCancelStr("取消");
        alertData.setDetermineStr("打开");
        alertData.setFirstDet(true);
        alertData.setMsg("“多么生活”想要打开“QQ”");
        alertSettingBean.setStyle(AlertView.Style.Alert);
        alertSettingBean.setAlertData(alertData);
        qqDialog = new AlertView(alertSettingBean, this, this);
        qqDialog.setCancelable(true);
        qqDialog.show();
    }

    @OnClick(R.id.rel_real_info)
    public void setRealName() {
        Intent intent = new Intent();
        intent.setClass(AccountSafeActivity.this, PersonalRealNameActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }

    @Override
    public void onAlertItemClick(Object o, int position) {
        mShareAPI = UMShareAPI.get(this);
        if (o == weChatDialog && position != AlertView.CANCELPOSITION) {
            if (isBindWeChat) {
                unBindWeChatAccount();
            } else {
                // 打开微信授权
                SHARE_MEDIA platform = SHARE_MEDIA.WEIXIN;
                mShareAPI.getPlatformInfo(AccountSafeActivity.this, platform, getDataInfoListener);
            }
        } else if (o == qqDialog && position != AlertView.CANCELPOSITION) {
            //qq授权
            SHARE_MEDIA platform = QQ;
            mShareAPI.getPlatformInfo(AccountSafeActivity.this, platform, getDataInfoListener);
        } else if (o == sinaDialog && position != AlertView.CANCELPOSITION) {
            //新浪授权
            SHARE_MEDIA platform = SINA;
            mShareAPI.getPlatformInfo(AccountSafeActivity.this, platform, getDataInfoListener);
        }
    }

    UMAuthListener getDataInfoListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            if (data != null) {
                final OtherAccountBindInfo info = new OtherAccountBindInfo();
                switch (platform) {
                    case WEIXIN:
                        info.setOpenid(data.get("openid"));
                        info.setUnionId(getStrings(data.get("uid")));
                        info.setType(OTHER_WECHAT);
                        info.setNickname(data.get("name"));
                        info.setAvatar(!TextUtils.isEmpty(data.get("iconurl")) ? data.get("iconurl") : "");
                        bindOtherAccount(info);
                        break;
                    case QQ:
                        info.setOpenid(data.get("uid"));
                        info.setType(OTHER_QQ);
                        info.setNickname(data.get("name"));
                        info.setAvatar(!TextUtils.isEmpty(data.get("iconurl")) ? data.get("iconurl") : "");
                        bindOtherAccount(info);
                        break;
                    case SINA:
                        info.setOpenid(data.get("uid"));
                        info.setType(OTHER_SINA);
                        info.setNickname(data.get("name"));
                        info.setAvatar(!TextUtils.isEmpty(data.get("iconurl ")) ? data.get("iconurl ") : "");
                        bindOtherAccount(info);
                        break;
                }
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            showToast(getApplicationContext(), "授权失败");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            showToast(getApplicationContext(), "授权取消");
        }
    };

    private void getLoginStatus() {
        SavePersonalInfoBean personalInfo = ConstantMethod.getPersonalInfo(this);
        if (personalInfo.isLogin()) {
            uid = personalInfo.getUid();
        } else {
            //未登录跳转登录页
            Intent intent = new Intent(this, MineLoginActivity.class);
            startActivityForResult(intent, IS_LOGIN_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IS_LOGIN_CODE:
                getLoginStatus();
                loadData();
                break;
        }
        mShareAPI.onActivityResult(requestCode, resultCode, data);
    }

    private void bindOtherAccount(OtherAccountBindInfo accountInfo) {
        String url = Url.BASE_URL + Url.MINE_BIND_ACCOUNT;
        Map<String, Object> params = new HashMap<>();
        params.put("openid", accountInfo.getOpenid());
        params.put("type", accountInfo.getType());
        if (OTHER_WECHAT.equals(accountInfo.getType())) {
            params.put("unionid", accountInfo.getUnionId());
        }
        params.put("nickname", accountInfo.getNickname());
        params.put("avatar", accountInfo.getAvatar());
        params.put("id", uid);
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                AuthorizeSuccessOtherData accountInfo = gson.fromJson(result, AuthorizeSuccessOtherData.class);
                if (accountInfo != null) {
                    if (accountInfo.getCode().equals("01")) {
//                            第三方账号登录统计
                        showToast(AccountSafeActivity.this, accountInfo.getMsg());
                        MobclickAgent.onProfileSignIn(accountInfo.getOtherAccountBean().getType(), accountInfo.getOtherAccountBean().getUid() + "");
                        loadData();
                    } else {
                        showToast(AccountSafeActivity.this, accountInfo.getMsg());
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.d("MessageWarmActivity", "onError: " + ex);
            }
        });
    }

    private void unBindWeChatAccount() {
        String url = Url.BASE_URL + Url.ACCOUNT_UNBIND_WECHAT;
        if (NetWorkUtils.checkNet(this)) {
            loadHud.show();
            Map<String, Object> params = new HashMap<>();
            params.put("uid", uid);
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    loadHud.dismiss();
                    Gson gson = new Gson();
                    RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                    if (requestStatus != null && SUCCESS_CODE.equals(requestStatus.getCode())) {
                        loadData();
                        isBindWeChat = false;
                        tv_account_safe_weChat.setText("未绑定");
                        showToast(AccountSafeActivity.this, String.format(getResources().getString(R.string.doSuccess), "解绑"));
                    } else {
                        showToast(AccountSafeActivity.this, getStrings(requestStatus.getMsg()));
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    loadHud.dismiss();
                    showToast(AccountSafeActivity.this, R.string.unConnectedNetwork);
                    super.onError(ex, isOnCallback);
                }
            });
        } else {
            showToast(this, R.string.unConnectedNetwork);
        }
    }
}
