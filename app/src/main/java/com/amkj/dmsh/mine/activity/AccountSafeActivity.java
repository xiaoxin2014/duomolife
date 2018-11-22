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
import com.amkj.dmsh.base.NetLoadUtils;
import com.amkj.dmsh.bean.CommunalUserInfoEntity;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.mine.bean.AuthorizeSuccessOtherData;
import com.amkj.dmsh.mine.bean.OtherAccountBindEntity;
import com.amkj.dmsh.mine.bean.OtherAccountBindEntity.OtherAccountBindInfo;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.alertdialog.AlertDialogHelper;
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

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.OTHER_QQ;
import static com.amkj.dmsh.constant.ConstantVariable.OTHER_SINA;
import static com.amkj.dmsh.constant.ConstantVariable.OTHER_WECHAT;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.umeng.socialize.bean.SHARE_MEDIA.QQ;
import static com.umeng.socialize.bean.SHARE_MEDIA.SINA;

;
;

public class AccountSafeActivity extends BaseActivity {
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
    private UMShareAPI mShareAPI;
    private boolean isBindWeChat = false;
    private CommunalUserInfoEntity.CommunalUserInfoBean minaData;
    private OtherAccountBindEntity otherAccountBindEntity;
    private AlertDialogHelper weChatDialogHelper;
    private AlertDialogHelper sinaDialogHelper;
    private AlertDialogHelper qqDialogHelper;

    @Override
    protected int getContentView() {
        return R.layout.activity_account_safe;
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
        tv_share.setVisibility(View.INVISIBLE);
        tv_header_titleAll.setText("账户安全");
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
        String url = Url.BASE_URL + Url.MINE_PAGE;
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        NetLoadUtils.getQyInstance().loadNetDataPost(mAppContext, url
                , params, new NetLoadUtils.NetLoadListener() {
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
                    public void netClose() {

                    }

                    @Override
                    public void onError(Throwable throwable) {

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
        params.put("uid", userId);
        NetLoadUtils.getQyInstance().loadNetDataPost(mAppContext, url
                , params, new NetLoadUtils.NetLoadListener() {
                    @Override
                    public void onSuccess(String result) {
                        Gson gson = new Gson();
                        otherAccountBindEntity = gson.fromJson(result, OtherAccountBindEntity.class);
                        if (otherAccountBindEntity != null) {
                            if (otherAccountBindEntity.getCode().equals("01")) {
                                setAccountData(otherAccountBindEntity.getOtherAccountBindInfo());
                            } else if (otherAccountBindEntity.getCode().equals("02")) {
                                setAccountData(null);
                            } else {
                                showToast(AccountSafeActivity.this, otherAccountBindEntity.getMsg());
                            }
                        }
                        NetLoadUtils.getQyInstance().showLoadSir(loadService, otherAccountBindEntity);
                    }

                    @Override
                    public void netClose() {
                        showToast(mAppContext, R.string.unConnectedNetwork);
                        NetLoadUtils.getQyInstance().showLoadSir(loadService, otherAccountBindEntity);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        showToast(mAppContext, R.string.unConnectedNetwork);
                        NetLoadUtils.getQyInstance().showLoadSir(loadService, otherAccountBindEntity);
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
    @OnClick(R.id.rel_weChat_account)
    void bindWeChat() {
        String cancelText;
        String msg;
        String confirmText;
        if (isBindWeChat) {
            cancelText = "取消";
            confirmText = "解绑";
            msg = "解除绑定后该微信订单只能在微信中\n查看，确定要解除该微信账号吗";
        } else {
            //弹窗 打开微信
            cancelText = "取消";
            confirmText = "打开";
            msg = "“多么生活”想要打开“微信”";
        }

        if (weChatDialogHelper == null) {
            weChatDialogHelper = new AlertDialogHelper(AccountSafeActivity.this);
            weChatDialogHelper.setCancelTextColor(getResources().getColor(R.color.text_login_gray_s))
                    .setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                        @Override
                        public void confirm() {
                            doAuthInfo(OTHER_WECHAT);
                        }

                        @Override
                        public void cancel() {
                        }
                    });
        }
        weChatDialogHelper.setTitleVisibility(View.GONE).setMsgTextGravity(Gravity.CENTER)
                .setMsg(msg).setCancelText(cancelText).setConfirmText(confirmText);
        weChatDialogHelper.show();
    }

    //绑定微博
    @OnClick(R.id.rel_weiBo_account)
    void bindWeiBo() {
        if (sinaDialogHelper == null) {
            sinaDialogHelper = new AlertDialogHelper(AccountSafeActivity.this);
            sinaDialogHelper.setCancelTextColor(getResources().getColor(R.color.text_login_gray_s))
                    .setTitleVisibility(View.GONE).setMsgTextGravity(Gravity.CENTER)
                    .setMsg("“多么生活”想要打开“微博”").setCancelText("取消").setConfirmText("打开")
                    .setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                        @Override
                        public void confirm() {
                            doAuthInfo(OTHER_SINA);
                        }

                        @Override
                        public void cancel() {
                        }
                    });
        }
        sinaDialogHelper.show();
    }

    //绑定QQ
    @OnClick(R.id.rel_qq_account)
    void bindQQ() {
        //弹窗 打开QQ
        if (qqDialogHelper == null) {
            qqDialogHelper = new AlertDialogHelper(AccountSafeActivity.this);
            qqDialogHelper.setCancelTextColor(getResources().getColor(R.color.text_login_gray_s))
                    .setTitleVisibility(View.GONE).setMsgTextGravity(Gravity.CENTER)
                    .setMsg("“多么生活”想要打开“QQ”").setCancelText("取消").setConfirmText("打开")
                    .setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                        @Override
                        public void confirm() {
                            doAuthInfo(OTHER_QQ);
                        }

                        @Override
                        public void cancel() {
                        }
                    });
        }
        qqDialogHelper.show();
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

    /**
     * 授权类型
     *
     * @param authType
     */
    public void doAuthInfo(String authType) {
        if (mShareAPI == null) {
            mShareAPI = UMShareAPI.get(this);
        }
        switch (getStrings(authType)) {
            case OTHER_WECHAT:
                if (isBindWeChat) {
                    unBindWeChatAccount();
                } else {
                    // 打开微信授权
                    SHARE_MEDIA platform = SHARE_MEDIA.WEIXIN;
                    mShareAPI.getPlatformInfo(AccountSafeActivity.this, platform, getDataInfoListener);
                }
                break;
            case OTHER_QQ:
                //qq授权
                SHARE_MEDIA platform = QQ;
                mShareAPI.getPlatformInfo(AccountSafeActivity.this, platform, getDataInfoListener);
                break;
            case OTHER_SINA:
                //新浪授权
                platform = SINA;
                mShareAPI.getPlatformInfo(AccountSafeActivity.this, platform, getDataInfoListener);
                break;
            default:
                showToast(AccountSafeActivity.this, "暂不支持该授权！");
                break;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IS_LOGIN_CODE:
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
        params.put("id", userId);
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
            }
        });
    }

    private void unBindWeChatAccount() {
        String url = Url.BASE_URL + Url.ACCOUNT_UNBIND_WECHAT;
        if (NetWorkUtils.checkNet(this)) {
            loadHud.show();
            Map<String, Object> params = new HashMap<>();
            params.put("uid", userId);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (qqDialogHelper != null && qqDialogHelper.getAlertDialog() != null && qqDialogHelper.getAlertDialog().isShowing()) {
            qqDialogHelper.dismiss();
        }
        if (weChatDialogHelper != null && weChatDialogHelper.getAlertDialog() != null && weChatDialogHelper.getAlertDialog().isShowing()) {
            weChatDialogHelper.dismiss();
        }
        if (sinaDialogHelper != null && sinaDialogHelper.getAlertDialog() != null && sinaDialogHelper.getAlertDialog().isShowing()) {
            sinaDialogHelper.dismiss();
        }
    }
}
