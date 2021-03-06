package com.amkj.dmsh.dao;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.bean.LoginDataEntity;
import com.amkj.dmsh.bean.LoginDataEntity.LoginDataBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.TagAliasOperatorHelper;
import com.amkj.dmsh.constant.TagAliasOperatorHelper.TagAliasBean;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.mine.activity.BindingMobileActivity;
import com.amkj.dmsh.mine.bean.OtherAccountBindEntity.OtherAccountBindInfo;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.qyservice.QyServiceUtils;
import com.amkj.dmsh.utils.SharedPreUtils;
import com.tencent.stat.StatConfig;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.util.HashSet;
import java.util.Set;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.ali.auth.third.core.context.KernelContext.getApplicationContext;
import static com.amkj.dmsh.constant.ConstantMethod.dismissLoadhud;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showLoadhud;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.LOGIN_SUCCESS;
import static com.amkj.dmsh.constant.ConstantVariable.TOKEN;
import static com.amkj.dmsh.constant.ConstantVariable.TOKEN_EXPIRE_TIME;
import static com.amkj.dmsh.constant.TagAliasOperatorHelper.ACTION_CLEAN;
import static com.amkj.dmsh.constant.TagAliasOperatorHelper.ACTION_DELETE;
import static com.amkj.dmsh.constant.TagAliasOperatorHelper.ACTION_SET;
import static com.amkj.dmsh.constant.TagAliasOperatorHelper.sequence;
import static com.amkj.dmsh.dao.BaiChuanDao.exitTaoBaoAccount;

/**
 * Created by xiaoxin on 2019/11/25
 * Version:v4.3.7
 * ClassDescription :??????????????????Dao???
 */
public class UserDao {

    /**
     * ????????????
     *
     * @param otherAccountBindInfo ?????????????????????????????????
     */
    public static void loginSuccessSetData(Activity context, LoginDataEntity loginDataEntity, OtherAccountBindInfo otherAccountBindInfo) {
        showToast(R.string.login_success);
        //???????????????????????????
        LoginDataBean loginDataBean = loginDataEntity.getLoginDataBean();
        SavePersonalInfoBean savePersonalInfo = new SavePersonalInfoBean();
        savePersonalInfo.setLogin(true);
        savePersonalInfo.setPhoneNum(loginDataBean.getMobile());
        savePersonalInfo.setNickName(loginDataBean.getNickname());
        savePersonalInfo.setAvatar(loginDataBean.getAvatar());
        savePersonalInfo.setUid(loginDataBean.getUid());
        savePersonalInfo.setVip(loginDataBean.isVip());
        savePersonalInfo.setToken(loginDataBean.getToken());
        savePersonalInfo.setTokenExpireSeconds(System.currentTimeMillis() + loginDataBean.getTokenExpireSeconds());
        if (otherAccountBindInfo != null) {
            savePersonalInfo.setLoginType(otherAccountBindInfo.getType());
            savePersonalInfo.setOpenId(otherAccountBindInfo.getOpenid());
            savePersonalInfo.setUnionId(otherAccountBindInfo.getUnionId());
            savePersonalInfo.setAccessToken(otherAccountBindInfo.getAccessToken());
        }
        savePersonalInfoCache(context, savePersonalInfo);

        //????????????????????????
        Intent data = new Intent();
        context.setResult(RESULT_OK, data);
        context.finish();
        //??????????????????????????????
        EventBus.getDefault().post(new EventMessage(LOGIN_SUCCESS, ""));
    }


    //??????????????????
    public static void savePersonalInfoCache(Context context, SavePersonalInfoBean savePersonalInfo) {
        Context applicationContext = context.getApplicationContext();
        if (savePersonalInfo != null && savePersonalInfo.isLogin()) {
            if (savePersonalInfo.getUid() == 0) return;
            userId = savePersonalInfo.getUid();//????????????????????????????????????uid,??????????????????????????????????????????????????????????????????uid???????????????????????????????????????uid
            ConstantMethod.setIsVip(savePersonalInfo.isVip());
            SharedPreferences loginStatus = applicationContext.getSharedPreferences("loginStatus", MODE_PRIVATE);
            SharedPreferences.Editor edit = loginStatus.edit();
            edit.putBoolean("isLogin", true);
            edit.putBoolean("isVip", savePersonalInfo.isVip());
            edit.putInt("uid", savePersonalInfo.getUid());
            //????????????????????????token?????????
            if (!TextUtils.isEmpty(savePersonalInfo.getToken())) {
                edit.putString(TOKEN, getStrings(savePersonalInfo.getToken()));
                edit.putLong(TOKEN_EXPIRE_TIME, savePersonalInfo.getTokenExpireSeconds());
            }
            if (!TextUtils.isEmpty(savePersonalInfo.getOpenId())) {
                edit.putString("OPEN_ID", getStrings(savePersonalInfo.getOpenId()));
            }
            if (!TextUtils.isEmpty(savePersonalInfo.getLoginType())) {
                edit.putString("LOGIN_TYPE", getStrings(savePersonalInfo.getLoginType()));
            }
            if (!TextUtils.isEmpty(savePersonalInfo.getUnionId())) {
                edit.putString("UNION_ID", getStrings(savePersonalInfo.getUnionId()));
            }

            if (!TextUtils.isEmpty(savePersonalInfo.getAccessToken())) {
                edit.putString("ACCESS_TOKEN", getStrings(savePersonalInfo.getAccessToken()));
            }
            edit.commit();
            //???????????? ??????????????????
            StatConfig.setCustomUserId(applicationContext, String.valueOf(savePersonalInfo.getUid()));
            //????????????
            MobclickAgent.onProfileSignIn(String.valueOf(savePersonalInfo.getUid()));
            //??????JPush
            bindJPush(String.valueOf(savePersonalInfo.getUid()));
            //????????????
            QyServiceUtils.getQyInstance().loginQyUserInfo(applicationContext, savePersonalInfo.getUid()
                    , savePersonalInfo.getNickName(), savePersonalInfo.getPhoneNum(), savePersonalInfo.getAvatar());
        } else {
            userId = 0;
            ConstantMethod.setIsVip(false);
            //????????????
            QyServiceUtils.getQyInstance().logoutQyUser(applicationContext);
            //???????????? ??????????????????
            MobclickAgent.onProfileSignOff();
            //??????JPush
            unBindJPush();
            //??????????????????
            exitTaoBaoAccount();
            //??????????????????
            SharedPreUtils.clearAll();
        }
    }

    //??????????????????
    public static SavePersonalInfoBean getPersonalInfo(Context context) {
        SharedPreferences loginStatus = context.getSharedPreferences("loginStatus", MODE_PRIVATE);
        SavePersonalInfoBean savePersonalInfo = new SavePersonalInfoBean();
        if (loginStatus.getBoolean("isLogin", false)) {
            savePersonalInfo.setUid(loginStatus.getInt("uid", 0));
            savePersonalInfo.setNickName(loginStatus.getString("nickName", ""));
            savePersonalInfo.setAvatar(loginStatus.getString("avatar", ""));
            savePersonalInfo.setPhoneNum(loginStatus.getString("P_NUM", ""));
            savePersonalInfo.setLoginType(loginStatus.getString("LOGIN_TYPE", ""));
            savePersonalInfo.setOpenId(loginStatus.getString("OPEN_ID", ""));
            savePersonalInfo.setUnionId(loginStatus.getString("UNION_ID", ""));
            savePersonalInfo.setLogin(true);
            userId = savePersonalInfo.getUid();
            ConstantMethod.setIsVip(loginStatus.getBoolean("isVip", false));
        } else {
            userId = 0;
        }
        return savePersonalInfo;
    }

    /**
     * ??????????????????,?????????????????????token??????
     *
     * @param isHandOperation ???????????????????????????(????????????????????????????????????????????????)
     */
    public static void logout(Activity activity, boolean isHandOperation) {
        if (isHandOperation) {
            showLoadhud(activity);
        }

        NetLoadUtils.getNetInstance().loadNetDataPost(activity, Url.LOG_OUT, null, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                //????????????????????????
                savePersonalInfoCache(activity, null);
                if (isHandOperation) {
                    showToast("??????????????????");
                    dismissLoadhud(activity);
                    activity.finish();
                }
            }

            @Override
            public void onNotNetOrException() {
                if (isHandOperation) {
                    showToast("?????????????????? ");
                    dismissLoadhud(activity);
                }
            }
        });
    }


    //???????????????????????????????????????
    public static void bindPhoneByWx(Activity context) {
        Intent intent = new Intent();
        String openId = (String) SharedPreUtils.getParam("OPEN_ID", "");
        String unionid = (String) SharedPreUtils.getParam("UNION_ID", "0");
        String accessToken = (String) SharedPreUtils.getParam("ACCESS_TOKEN", "");
        String loginType = (String) SharedPreUtils.getParam("LOGIN_TYPE", "");
        intent.putExtra("uid", String.valueOf(userId));
        intent.putExtra("openId", openId);
        intent.putExtra("unionid", unionid);
        intent.putExtra("accessToken", accessToken);
        intent.putExtra("type", loginType);
        intent.setClass(context, BindingMobileActivity.class);
        context.startActivity(intent);
    }


    /**
     * ??????JPush
     */
    private static void bindJPush(String uid) {
        TagAliasBean tagAliasBean = new TagAliasBean();
        tagAliasBean.action = ACTION_SET;
        tagAliasBean.isAliasAction = true;
        tagAliasBean.alias = uid;
        TagAliasOperatorHelper.getInstance().handleAction(getApplicationContext(), sequence, tagAliasBean);
        if (ConstantVariable.isDebugTag) {
//                ??????????????????tag???alias
            tagAliasBean.action = ACTION_SET;
            tagAliasBean.isAliasAction = false;
            Set<String> setString = new HashSet<>();
            setString.add("test");
            tagAliasBean.tags = setString;
            TagAliasOperatorHelper.getInstance().handleAction(getApplicationContext(), sequence, tagAliasBean);
        }
    }

    /**
     * ??????JPush
     */
    private static void unBindJPush() {
        TagAliasBean tagAliasBean = new TagAliasBean();
        tagAliasBean.action = ACTION_DELETE;
        tagAliasBean.isAliasAction = true;
        TagAliasOperatorHelper.getInstance().handleAction(getApplicationContext(), sequence, tagAliasBean);
        if (ConstantVariable.isDebugTag) {
//                ??????????????????tag???alias
            tagAliasBean.action = ACTION_CLEAN;
            tagAliasBean.isAliasAction = false;
            TagAliasOperatorHelper.getInstance().handleAction(getApplicationContext(), sequence, tagAliasBean);
        }
    }
}
