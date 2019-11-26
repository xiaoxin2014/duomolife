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
 * ClassDescription :用户账号相关Dao类
 */
public class UserDao {

    /**
     * 登陆成功
     *
     * @param otherAccountBindInfo 不为空时表示是三方登录
     */
    public static void loginSuccessSetData(Activity context, LoginDataEntity loginDataEntity, OtherAccountBindInfo otherAccountBindInfo) {
        showToast(context, R.string.login_success);
        //保存个人信息到本地
        LoginDataBean loginDataBean = loginDataEntity.getLoginDataBean();
        SavePersonalInfoBean savePersonalInfo = new SavePersonalInfoBean();
        savePersonalInfo.setLogin(true);
        savePersonalInfo.setPhoneNum(loginDataBean.getMobile());
        savePersonalInfo.setNickName(loginDataBean.getNickname());
        savePersonalInfo.setAvatar(loginDataBean.getAvatar());
        savePersonalInfo.setUid(loginDataBean.getUid());
        savePersonalInfo.setToken(loginDataBean.getToken());
        savePersonalInfo.setTokenExpireSeconds(System.currentTimeMillis() + loginDataBean.getTokenExpireSeconds());
        if (otherAccountBindInfo != null) {
            savePersonalInfo.setLoginType(otherAccountBindInfo.getType());
            savePersonalInfo.setOpenId(otherAccountBindInfo.getOpenid());
            savePersonalInfo.setUnionId(otherAccountBindInfo.getUnionId());
            savePersonalInfo.setAccessToken(otherAccountBindInfo.getAccessToken());
        }
        savePersonalInfoCache(context, savePersonalInfo);

        //登录信息回调信息
        Intent data = new Intent();
        context.setResult(RESULT_OK, data);
        context.finish();
        //登录成功关闭登录界面
        EventBus.getDefault().post(new EventMessage("loginSuccess", ""));
    }


    //保存个人信息
    public static void savePersonalInfoCache(Context context, SavePersonalInfoBean savePersonalInfo) {
        Context applicationContext = context.getApplicationContext();
        if (savePersonalInfo != null && savePersonalInfo.isLogin()) {
            if (savePersonalInfo.getUid() == 0) return;
            userId = savePersonalInfo.getUid();//如果用微信登录，会有一个uid,绑定一个已注册手机会发生账号迁移生成一个新的uid，所以这里需要更新静态变量uid
            //登录成功 三方账号登录
            StatConfig.setCustomUserId(applicationContext, String.valueOf(savePersonalInfo.getUid()));
            //友盟统计
            MobclickAgent.onProfileSignIn(String.valueOf(savePersonalInfo.getUid()));
            //绑定JPush
            bindJPush(String.valueOf(savePersonalInfo.getUid()));
            //登录七鱼
            QyServiceUtils.getQyInstance().loginQyUserInfo(applicationContext, savePersonalInfo.getUid()
                    , savePersonalInfo.getNickName(), savePersonalInfo.getPhoneNum(), savePersonalInfo.getAvatar());

            SharedPreferences loginStatus = applicationContext.getSharedPreferences("loginStatus", MODE_PRIVATE);
            SharedPreferences.Editor edit = loginStatus.edit();
            edit.putBoolean("isLogin", true);
            edit.putInt("uid", savePersonalInfo.getUid());
            //先判空再存，避免token被清空
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
        } else {
            userId = 0;
            //七鱼注销
            QyServiceUtils.getQyInstance().logoutQyUser(applicationContext);
            //注销账号 关闭账号统计
            MobclickAgent.onProfileSignOff();
            //解绑JPush
            unBindJPush();
            //退出淘宝账号
            exitTaoBaoAccount();
            //清除登录状态
            SharedPreUtils.clearAll();
        }
    }

    //获取个人信息
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
        } else {
            userId = 0;
        }
        return savePersonalInfo;
    }

    /**
     * 调用登出接口,清除后台记录的token信息
     *
     * @param isHandOperation 是否是手动退出登录
     */
    public static void logout(Activity activity, boolean isHandOperation) {
        if (isHandOperation) {
            showLoadhud(activity);
        }
        NetLoadUtils.token = (String) SharedPreUtils.getParam(TOKEN, "");
        NetLoadUtils.uid = String.valueOf(SharedPreUtils.getParam("uid", 0));

        NetLoadUtils.getNetInstance().loadNetDataPost(activity, Url.LOG_OUT, null, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                //清除本地登录信息
                savePersonalInfoCache(activity, null);
                if (isHandOperation) {
                    showToast(activity, "退出登录成功");
                    dismissLoadhud(activity);
                    activity.finish();
                }
            }

            @Override
            public void onNotNetOrException() {
                if (isHandOperation) {
                    showToast(activity, "退出登录失败 ");
                    dismissLoadhud(activity);
                }
            }
        });
    }


    //微信登录跳转绑定手机号界面
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
     * 绑定JPush
     */
    private static void bindJPush(String uid) {
        TagAliasBean tagAliasBean = new TagAliasBean();
        tagAliasBean.action = ACTION_SET;
        tagAliasBean.isAliasAction = true;
        tagAliasBean.alias = uid;
        TagAliasOperatorHelper.getInstance().handleAction(getApplicationContext(), sequence, tagAliasBean);
        if (ConstantVariable.isDebugTag) {
//                测试版本删除tag跟alias
            tagAliasBean.action = ACTION_SET;
            tagAliasBean.isAliasAction = false;
            Set<String> setString = new HashSet<>();
            setString.add("test");
            tagAliasBean.tags = setString;
            TagAliasOperatorHelper.getInstance().handleAction(getApplicationContext(), sequence, tagAliasBean);
        }
    }

    /**
     * 解绑JPush
     */
    private static void unBindJPush() {
        TagAliasBean tagAliasBean = new TagAliasBean();
        tagAliasBean.action = ACTION_DELETE;
        tagAliasBean.isAliasAction = true;
        TagAliasOperatorHelper.getInstance().handleAction(getApplicationContext(), sequence, tagAliasBean);
        if (ConstantVariable.isDebugTag) {
//                测试版本删除tag跟alias
            tagAliasBean.action = ACTION_CLEAN;
            tagAliasBean.isAliasAction = false;
            TagAliasOperatorHelper.getInstance().handleAction(getApplicationContext(), sequence, tagAliasBean);
        }
    }
}
