package com.amkj.dmsh.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.bean.CommunalUserInfoEntity;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.mine.bean.AuthorizeSuccessOtherData;
import com.amkj.dmsh.mine.bean.OtherAccountBindEntity;
import com.amkj.dmsh.mine.bean.OtherAccountBindEntity.OtherAccountBindInfo;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.alertdialog.AlertDialogHelper;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.showToastRequestMsg;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.OTHER_MOBILE;
import static com.amkj.dmsh.constant.ConstantVariable.OTHER_QQ;
import static com.amkj.dmsh.constant.ConstantVariable.OTHER_SINA;
import static com.amkj.dmsh.constant.ConstantVariable.OTHER_WECHAT;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.ACCOUNT_UNBIND_SINA_QQ;
import static com.amkj.dmsh.constant.Url.ACCOUNT_UNBIND_WECHAT;
import static com.amkj.dmsh.constant.Url.MINE_BIND_ACCOUNT;
import static com.amkj.dmsh.constant.Url.MINE_PAGE;
import static com.amkj.dmsh.constant.Url.MINE_SYNC_LOGIN;
import static com.umeng.socialize.bean.SHARE_MEDIA.QQ;
import static com.umeng.socialize.bean.SHARE_MEDIA.SINA;
import static com.umeng.socialize.bean.SHARE_MEDIA.WEIXIN;

;
;

public class AccountSafeActivity extends BaseActivity {
    @BindView(R.id.tv_header_shared)
    TextView tv_share;
    @BindView(R.id.tv_header_title)
    TextView tv_header_title;
    //    绑定手机
    @BindView(R.id.tv_account_safe_mobile)
    TextView tv_account_safe_mobile;
    //    绑定微信
    @BindView(R.id.tv_account_safe_weChat)
    TextView tv_account_safe_weChat;
    @BindView(R.id.rel_weChat_account)
    RelativeLayout rel_weChat_account;
    //    绑定微博
    @BindView(R.id.rel_weibo_account)
    RelativeLayout rel_weiBo_account;
    @BindView(R.id.tv_account_safe_weiBo)
    TextView tv_account_safe_weiBo;
    //    绑定QQ
    @BindView(R.id.rel_qq_account)
    RelativeLayout rel_qq_account;
    @BindView(R.id.tv_account_safe_qq)
    TextView tv_account_safe_qq;
    //    实名信息
    @BindView(R.id.tv_account_safe_real_info)
    TextView tv_account_safe_real_info;

    private final String CHANGE_PASSWORD = "changePassword";
    private UMShareAPI mShareAPI;
    private CommunalUserInfoEntity.CommunalUserInfoBean minaData;
    private OtherAccountBindEntity otherAccountBindEntity;
    private AlertDialogHelper accountDialogHelper;
    private String unbindAccountType;
    //    绑定账号数据
    private Map<String, Integer> bindAccountType = new HashMap<>();
    private String[] bindTypeArray = {OTHER_MOBILE, OTHER_WECHAT, OTHER_SINA, OTHER_QQ};
    private String accountClickType;

    @Override
    protected int getContentView() {
        return R.layout.activity_account_safe;
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
        tv_share.setVisibility(View.INVISIBLE);
        tv_header_title.setText("账户安全");
        for (String bindType : bindTypeArray) {
            bindAccountType.put(bindType, 0);
        }
    }

    //  2016/9/21 获取账号列表
    @Override
    protected void loadData() {
        if (userId < 1) {
            return;
        }
        getCurrentAccountData();
        getOtherAccountData();
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    private void getCurrentAccountData() {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, MINE_PAGE
                , params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        Gson gson = new Gson();
                        CommunalUserInfoEntity minePageData = gson.fromJson(result, CommunalUserInfoEntity.class);
                        if (minePageData != null) {
                            if (minePageData.getCode().equals(SUCCESS_CODE)) {
                                minaData = minePageData.getCommunalUserInfoBean();
                                setMobileData(minaData);
                            } else {
                                showToast(AccountSafeActivity.this, minePageData.getMsg());
                            }
                        }
                    }
                });
    }

    /**
     * @param minaData
     */
    private void setMobileData(CommunalUserInfoEntity.CommunalUserInfoBean minaData) {
        if (minaData.isMobile_verification()) {
            tv_account_safe_mobile.setText(!TextUtils.isEmpty(minaData.getMobile()) ? minaData.getMobile() : "已绑定");
            bindAccountType.put(OTHER_MOBILE, 1);
        } else {
            bindAccountType.put(OTHER_MOBILE, 0);
        }
        if (!TextUtils.isEmpty(minaData.getIdcard())) {
            tv_account_safe_real_info.setText("已设置");
        } else {
            tv_account_safe_real_info.setText("未设置");
        }
    }

    /**
     * 获取第三方账号列表
     */
    private void getOtherAccountData() {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, MINE_SYNC_LOGIN
                , params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        Gson gson = new Gson();
                        otherAccountBindEntity = gson.fromJson(result, OtherAccountBindEntity.class);
                        if (otherAccountBindEntity != null) {
                            if (otherAccountBindEntity.getCode().equals(SUCCESS_CODE)) {
                                setAccountData(otherAccountBindEntity.getOtherAccountBindInfo());
                            } else if (otherAccountBindEntity.getCode().equals(EMPTY_CODE)) {
                                setAccountData(null);
                            } else {
                                showToast(AccountSafeActivity.this, otherAccountBindEntity.getMsg());
                            }
                        }
                        NetLoadUtils.getNetInstance().showLoadSirSuccess(loadService);
                    }

                    @Override
                    public void onNotNetOrException() {
                        NetLoadUtils.getNetInstance().showLoadSirSuccess(loadService);
                    }

                    @Override
                    public void netClose() {
                        showToast(mAppContext, R.string.unConnectedNetwork);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        showToast(mAppContext, R.string.invalidData);
                    }
                });
    }

    /**
     * 设置账号信息
     *
     * @param otherAccountBindInfos
     */
    private void setAccountData(List<OtherAccountBindInfo> otherAccountBindInfos) {
        if (otherAccountBindInfos != null) {
            for (int i = 0; i < otherAccountBindInfos.size(); i++) {
                switch (otherAccountBindInfos.get(i).getType()) {
                    case "wechat":
                        tv_account_safe_weChat.setText("已绑定");
                        bindAccountType.put(OTHER_WECHAT, 1);
                        break;
                    case "qq":
                        tv_account_safe_qq.setText("已绑定");
                        bindAccountType.put(OTHER_QQ, 1);
                        break;
                    case "sina":
                        tv_account_safe_weiBo.setText("已绑定");
                        bindAccountType.put(OTHER_SINA, 1);
                        break;
                }
            }
        } else {
            bindAccountType.put(OTHER_QQ, 0);
            bindAccountType.put(OTHER_WECHAT, 0);
            bindAccountType.put(OTHER_SINA, 0);
        }
    }

    //修改密码
    @OnClick(R.id.rel_chang_password)
    void skipSafe() {
        Intent intent = new Intent(AccountSafeActivity.this, ChangePasswordActivity.class);
        intent.putExtra("type", CHANGE_PASSWORD);
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
        }
    }

    //绑定微信
    @OnClick({R.id.rel_weChat_account, R.id.rel_weibo_account, R.id.rel_qq_account})
    void bindWeChat(View view) {
        String cancelText;
        String msg;
        String confirmText;
        String accountMsg = "";
        switch (view.getId()) {
            case R.id.rel_weChat_account:
                accountClickType = OTHER_WECHAT;
                accountMsg = "“微信”";
                break;
            case R.id.rel_weibo_account:
                accountClickType = OTHER_SINA;
                accountMsg = "“微博”";
                break;
            case R.id.rel_qq_account:
                accountClickType = OTHER_QQ;
                accountMsg = "“QQ”";
                break;
            default:
                break;
        }
        if (TextUtils.isEmpty(accountClickType) || bindAccountType.get(accountClickType) == null) {
            showToast(AccountSafeActivity.this, "账号类型错误，请重新选择！");
            return;
        }
        if (bindAccountType.get(accountClickType) == 1) {
            cancelText = "取消";
            confirmText = "解绑";
            msg = OTHER_WECHAT.equals(accountClickType) ? "解除绑定后该微信订单只能在微信中\n查看，确定要解除该微信账号吗" :
                    "确定要解除" + (OTHER_QQ.equals(accountClickType) ? "QQ" : "微博") + "账号绑定？";
        } else {
            //弹窗 打开微信
            cancelText = "取消";
            confirmText = "打开";
            msg = "“多么生活”想要打开" + getStrings(accountMsg);
        }
        if (accountDialogHelper == null) {
            accountDialogHelper = new AlertDialogHelper(AccountSafeActivity.this);
            accountDialogHelper.setCancelTextColor(getResources().getColor(R.color.text_login_gray_s))
                    .setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                        @Override
                        public void confirm() {
                            setOtherAccountInfo(accountClickType);
                        }

                        @Override
                        public void cancel() {
                        }
                    });
        }
        accountDialogHelper.setTitleVisibility(View.GONE).setMsgTextGravity(Gravity.CENTER_VERTICAL)
                .setMsg(msg).setCancelText(cancelText).setConfirmText(confirmText);
        accountDialogHelper.show();
    }

    @OnClick(R.id.rel_real_info)
    public void setRealName() {
        Intent intent = new Intent();
        intent.setClass(AccountSafeActivity.this, PersonalRealNameActivity.class);
        startActivity(intent);
    }

    //    注销账户
    @OnClick(R.id.tv_account_safe_logout)
    public void logoutAccount() {
        Intent intent = new Intent();
        intent.setClass(AccountSafeActivity.this, AccountLogoutActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }

    /**
     * 授权类型
     *
     * @param authType
     */
    public void setOtherAccountInfo(String authType) {
        if (mShareAPI == null) {
            UMShareConfig config = new UMShareConfig();
            config.isNeedAuthOnGetUserInfo(true);
            UMShareAPI.get(this).setShareConfig(config);
            mShareAPI = UMShareAPI.get(this);
        }
        if (OTHER_WECHAT.equals(authType) || OTHER_QQ.equals(authType) || OTHER_SINA.equals(authType)) {
            if (getOtherAccountBindStatus(authType) == 1) {
                unbindAccountType = authType;
                unBindAccount();
            } else {
                // 打开授权
                mShareAPI.getPlatformInfo(AccountSafeActivity.this, OTHER_WECHAT.equals(authType) ?
                        WEIXIN : OTHER_QQ.equals(authType) ? QQ : SINA, getDataInfoListener);
            }
        } else {
            showToast(AccountSafeActivity.this, "暂不支持该授权！");
        }
    }

    /**
     * 获取三方账号授权信息
     */
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            if (requestCode == IS_LOGIN_CODE) {
                finish();
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IS_LOGIN_CODE:
                loadData();
                break;
        }
        if (mShareAPI != null) {
            mShareAPI.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * 绑定第三方账号
     *
     * @param otherAccountBindInfo
     */
    private void bindOtherAccount(OtherAccountBindInfo otherAccountBindInfo) {
        Map<String, Object> params = new HashMap<>();
        params.put("openid", otherAccountBindInfo.getOpenid());
        params.put("type", otherAccountBindInfo.getType());
        if (OTHER_WECHAT.equals(otherAccountBindInfo.getType())) {
            params.put("unionid", otherAccountBindInfo.getUnionId());
        }
        params.put("nickname", otherAccountBindInfo.getNickname());
        params.put("avatar", otherAccountBindInfo.getAvatar());
        params.put("id", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, MINE_BIND_ACCOUNT, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                AuthorizeSuccessOtherData successOtherData = gson.fromJson(result, AuthorizeSuccessOtherData.class);
                if (successOtherData != null) {
                    if (successOtherData.getCode().equals(SUCCESS_CODE)) {
//                            第三方账号登录统计
                        showToast(AccountSafeActivity.this, successOtherData.getMsg());
                        bindAccountType.put(otherAccountBindInfo.getType(), 1);
                        /**
                         * 3.2.0 修改为统计自己传入的参数 以前版本根据后台返回的数据类型进行跟uid进行统计
                         */
                        MobclickAgent.onProfileSignIn(getStrings(otherAccountBindInfo.getType()), String.valueOf(userId));
                        loadData();
                    } else {
                        showToast(AccountSafeActivity.this, successOtherData.getMsg());
                    }
                }
            }
        });
    }

    /**
     * 解除绑定 微博 QQ 微信
     */
    private void unBindAccount() {
        loadHud.show();
        NetLoadListenerHelper netLoadListener = new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                loadHud.dismiss();
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null && SUCCESS_CODE.equals(requestStatus.getCode())) {
                    showToast(AccountSafeActivity.this, String.format(getResources().getString(R.string.doSuccess), "解绑"));
//                    设置三方账号状态
                    setUnbindAccount();
                } else {
                    showToastRequestMsg(AccountSafeActivity.this, requestStatus);
                }
            }

            @Override
            public void onNotNetOrException() {
                loadHud.dismiss();
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(AccountSafeActivity.this, R.string.invalidData);
            }

            @Override
            public void netClose() {
                showToast(AccountSafeActivity.this, R.string.unConnectedNetwork);
            }
        };
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        if (unbindAccountType.equals(OTHER_QQ) || unbindAccountType.equals(OTHER_SINA)) {
            params.put("type", unbindAccountType.equals(OTHER_QQ) ? OTHER_QQ : OTHER_SINA);
            NetLoadUtils.getNetInstance().loadNetDataPost(this, ACCOUNT_UNBIND_SINA_QQ, params, netLoadListener);
        } else if (unbindAccountType.equals(OTHER_WECHAT)) {
            NetLoadUtils.getNetInstance().loadNetDataPost(this, ACCOUNT_UNBIND_WECHAT, params, netLoadListener);
        }

    }

    /**
     * 设置解绑账号状态
     */
    private void setUnbindAccount() {
        if (TextUtils.isEmpty(unbindAccountType)) {
            return;
        }
        switch (unbindAccountType) {
            case OTHER_WECHAT:
                bindAccountType.put(OTHER_WECHAT, 0);
                tv_account_safe_weChat.setText("未绑定");
                break;
            case OTHER_QQ:
                bindAccountType.put(OTHER_QQ, 0);
                tv_account_safe_qq.setText("未绑定");
                break;
            case OTHER_SINA:
                bindAccountType.put(OTHER_SINA, 0);
                tv_account_safe_weiBo.setText("未绑定");
                break;
            default:
                break;
        }
    }

    /**
     * 获取三方账号绑定状态
     *
     * @param otherAccountType
     * @return
     */
    private int getOtherAccountBindStatus(String otherAccountType) {
        if (TextUtils.isEmpty(otherAccountType) || bindAccountType.get(otherAccountType) == null) {
            return 0;
        }
        return bindAccountType.get(otherAccountType);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (accountDialogHelper != null &&
                accountDialogHelper.getAlertDialog() != null &&
                accountDialogHelper.getAlertDialog().isShowing()) {
            accountDialogHelper.dismiss();
        }
    }
}
