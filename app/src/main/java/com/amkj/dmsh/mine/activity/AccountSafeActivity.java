package com.amkj.dmsh.mine.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.bean.CommunalUserInfoEntity;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.dao.UserDao;
import com.amkj.dmsh.mine.bean.OtherAccountBindEntity;
import com.amkj.dmsh.mine.bean.OtherAccountBindEntity.OtherAccountBindInfo;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.SharedPreUtils;
import com.amkj.dmsh.views.alertdialog.AlertDialogHelper;
import com.amkj.dmsh.utils.gson.GsonUtils;
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
import static com.amkj.dmsh.constant.ConstantVariable.TOKEN;
import static com.amkj.dmsh.constant.ConstantVariable.TOKEN_EXPIRE_TIME;
import static com.amkj.dmsh.constant.Url.ACCOUNT_UNBIND_SINA_QQ;
import static com.amkj.dmsh.constant.Url.ACCOUNT_UNBIND_WECHAT;
import static com.amkj.dmsh.constant.Url.MINE_BIND_ACCOUNT;
import static com.amkj.dmsh.constant.Url.MINE_PAGE;
import static com.amkj.dmsh.constant.Url.MINE_SYNC_LOGIN;
import static com.umeng.socialize.bean.SHARE_MEDIA.QQ;
import static com.umeng.socialize.bean.SHARE_MEDIA.SINA;
import static com.umeng.socialize.bean.SHARE_MEDIA.WEIXIN;


public class AccountSafeActivity extends BaseActivity {
    @BindView(R.id.tv_header_shared)
    TextView tv_share;
    @BindView(R.id.tv_header_title)
    TextView tv_header_title;
    //    ????????????
    @BindView(R.id.tv_account_safe_mobile)
    TextView tv_account_safe_mobile;
    //    ????????????
    @BindView(R.id.tv_account_safe_weChat)
    TextView tv_account_safe_weChat;
    @BindView(R.id.rel_weChat_account)
    RelativeLayout rel_weChat_account;
    //    ????????????
    @BindView(R.id.rel_weibo_account)
    RelativeLayout rel_weiBo_account;
    @BindView(R.id.tv_account_safe_weiBo)
    TextView tv_account_safe_weiBo;
    //    ??????QQ
    @BindView(R.id.rel_qq_account)
    RelativeLayout rel_qq_account;
    @BindView(R.id.tv_account_safe_qq)
    TextView tv_account_safe_qq;
    //    ????????????
    @BindView(R.id.tv_account_safe_real_info)
    TextView tv_account_safe_real_info;

    private final String CHANGE_PASSWORD = "changePassword";
    private UMShareAPI mShareAPI;
    private CommunalUserInfoEntity.CommunalUserInfoBean minaData;
    private OtherAccountBindEntity otherAccountBindEntity;
    private AlertDialogHelper accountDialogHelper;
    private String unbindAccountType;
    //    ??????????????????
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
        tv_header_title.setText("????????????");
        for (String bindType : bindTypeArray) {
            bindAccountType.put(bindType, 0);
        }
    }

    //  2016/9/21 ??????????????????
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

                        CommunalUserInfoEntity minePageData = GsonUtils.fromJson(result, CommunalUserInfoEntity.class);
                        if (minePageData != null) {
                            if (minePageData.getCode().equals(SUCCESS_CODE)) {
                                minaData = minePageData.getCommunalUserInfoBean();
                                setMobileData(minaData);
                            } else {
                                showToast( minePageData.getMsg());
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
            tv_account_safe_mobile.setText(!TextUtils.isEmpty(minaData.getMobile()) ? minaData.getMobile() : "?????????");
            bindAccountType.put(OTHER_MOBILE, 1);
        } else {
            bindAccountType.put(OTHER_MOBILE, 0);
        }
        if (!TextUtils.isEmpty(minaData.getIdcard())) {
            tv_account_safe_real_info.setText("?????????");
        } else {
            tv_account_safe_real_info.setText("?????????");
        }
    }

    /**
     * ???????????????????????????
     */
    private void getOtherAccountData() {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, MINE_SYNC_LOGIN
                , params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {

                        otherAccountBindEntity = GsonUtils.fromJson(result, OtherAccountBindEntity.class);
                        if (otherAccountBindEntity != null) {
                            if (otherAccountBindEntity.getCode().equals(SUCCESS_CODE)) {
                                setAccountData(otherAccountBindEntity.getOtherAccountBindInfo());
                            } else if (otherAccountBindEntity.getCode().equals(EMPTY_CODE)) {
                                setAccountData(null);
                            } else {
                                showToast( otherAccountBindEntity.getMsg());
                            }
                        }
                        NetLoadUtils.getNetInstance().showLoadSirSuccess(loadService);
                    }

                    @Override
                    public void onNotNetOrException() {
                        NetLoadUtils.getNetInstance().showLoadSirSuccess(loadService);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        showToast(R.string.invalidData);
                    }
                });
    }

    /**
     * ??????????????????
     *
     * @param otherAccountBindInfos
     */
    private void setAccountData(List<OtherAccountBindInfo> otherAccountBindInfos) {
        if (otherAccountBindInfos != null) {
            for (int i = 0; i < otherAccountBindInfos.size(); i++) {
                switch (otherAccountBindInfos.get(i).getType()) {
                    case "wechat":
                        tv_account_safe_weChat.setText("?????????");
                        bindAccountType.put(OTHER_WECHAT, 1);
                        break;
                    case "qq":
                        tv_account_safe_qq.setText("?????????");
                        bindAccountType.put(OTHER_QQ, 1);
                        break;
                    case "sina":
                        tv_account_safe_weiBo.setText("?????????");
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

    //????????????
    @OnClick(R.id.rel_chang_password)
    void skipSafe() {
        Intent intent = new Intent(AccountSafeActivity.this, ChangePasswordActivity.class);
        intent.putExtra("type", CHANGE_PASSWORD);
        startActivity(intent);
    }

    //????????????
    @OnClick(R.id.rel_mobile_account)
    void bindMobile() {
        if (minaData != null) {
            //????????????????????????
            if (minaData.isMobile_verification()) {
                Intent intent = new Intent();
                intent.putExtra("uid", String.valueOf(userId));
                intent.putExtra("mobile", minaData.getMobile());
                intent.setClass(AccountSafeActivity.this, ChangeMobileActivity.class);
                startActivity(intent);
            } else {
                //?????????
                UserDao.bindPhoneByWx(this);
            }
        }
    }

    //????????????
    @OnClick({R.id.rel_weChat_account, R.id.rel_weibo_account, R.id.rel_qq_account})
    void bindWeChat(View view) {
        String cancelText;
        String msg;
        String confirmText;
        String accountMsg = "";
        switch (view.getId()) {
            case R.id.rel_weChat_account:
                accountClickType = OTHER_WECHAT;
                accountMsg = "????????????";
                break;
            case R.id.rel_weibo_account:
                accountClickType = OTHER_SINA;
                accountMsg = "????????????";
                break;
            case R.id.rel_qq_account:
                accountClickType = OTHER_QQ;
                accountMsg = "???QQ???";
                break;
            default:
                break;
        }
        if (TextUtils.isEmpty(accountClickType) || bindAccountType.get(accountClickType) == null) {
            showToast("???????????????????????????????????????");
            return;
        }

        if (minaData != null) {
            //?????????????????????
            if (TextUtils.isEmpty(minaData.getMobile())) {
                showToast("?????????????????????");
                return;
            }
        }
        if (bindAccountType.get(accountClickType) == 1) {
            cancelText = "??????";
            confirmText = "??????";
            msg = OTHER_WECHAT.equals(accountClickType) ? "????????????????????????????????????????????????\n??????????????????????????????????????????" :
                    "???????????????" + (OTHER_QQ.equals(accountClickType) ? "QQ" : "??????") + "???????????????";
        } else {
            //?????? ????????????
            cancelText = "??????";
            confirmText = "??????";
            msg = "??????????????????????????????" + getStrings(accountMsg);
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

    //    ????????????
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
     * ????????????
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
                // ????????????
                mShareAPI.getPlatformInfo(AccountSafeActivity.this, OTHER_WECHAT.equals(authType) ?
                        WEIXIN : OTHER_QQ.equals(authType) ? QQ : SINA, getDataInfoListener);
            }
        } else {
            showToast("????????????????????????");
        }
    }

    /**
     * ??????????????????????????????
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
            showToast("????????????");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            showToast("????????????");
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
     * ?????????????????????
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

                RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        //???????????????????????????
                        showToast( requestStatus.getMsg());
                        bindAccountType.put(otherAccountBindInfo.getType(), 1);
                        //????????????Token??????
                        if (!TextUtils.isEmpty(requestStatus.getToken())) {
                            SharedPreUtils.setParam(TOKEN, getStrings(requestStatus.getToken()));
                            SharedPreUtils.setParam(TOKEN_EXPIRE_TIME, System.currentTimeMillis() + requestStatus.getTokenExpireSeconds());
                        }
                        /**
                         * 3.2.0 ???????????????????????????????????? ??????????????????????????????????????????????????????uid????????????
                         */
                        MobclickAgent.onProfileSignIn(getStrings(otherAccountBindInfo.getType()), String.valueOf(userId));
                        loadData();
                    } else {
                        showToast( requestStatus.getMsg());
                    }
                }
            }
        });
    }

    /**
     * ???????????? ?????? QQ ??????
     */
    private void unBindAccount() {
        loadHud.show();
        NetLoadListenerHelper netLoadListener = new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                loadHud.dismiss();

                RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                if (requestStatus != null && SUCCESS_CODE.equals(requestStatus.getCode())) {
                    showToast( String.format(getResources().getString(R.string.doSuccess), "??????"));
//                    ????????????????????????
                    setUnbindAccount();
                } else {
                    showToastRequestMsg( requestStatus);
                }
            }

            @Override
            public void onNotNetOrException() {
                loadHud.dismiss();
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(R.string.invalidData);
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
     * ????????????????????????
     */
    private void setUnbindAccount() {
        if (TextUtils.isEmpty(unbindAccountType)) {
            return;
        }
        switch (unbindAccountType) {
            case OTHER_WECHAT:
                bindAccountType.put(OTHER_WECHAT, 0);
                tv_account_safe_weChat.setText("?????????");
                break;
            case OTHER_QQ:
                bindAccountType.put(OTHER_QQ, 0);
                tv_account_safe_qq.setText("?????????");
                break;
            case OTHER_SINA:
                bindAccountType.put(OTHER_SINA, 0);
                tv_account_safe_weiBo.setText("?????????");
                break;
            default:
                break;
        }
    }

    /**
     * ??????????????????????????????
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
    protected void onResume() {
        super.onResume();
        loadData();
    }
}
