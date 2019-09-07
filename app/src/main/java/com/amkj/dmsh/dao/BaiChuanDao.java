package com.amkj.dmsh.dao;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.page.AlibcBasePage;
import com.alibaba.baichuan.android.trade.page.AlibcDetailPage;
import com.alibaba.baichuan.android.trade.page.AlibcMyOrdersPage;
import com.alibaba.baichuan.android.trade.page.AlibcPage;
import com.alibaba.baichuan.trade.biz.AlibcConstants;
import com.alibaba.baichuan.trade.biz.context.AlibcTradeResult;
import com.alibaba.baichuan.trade.biz.core.taoke.AlibcTaokeParams;
import com.alibaba.baichuan.trade.biz.login.AlibcLogin;
import com.alibaba.baichuan.trade.biz.login.AlibcLoginCallback;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.homepage.activity.DoMoLifeCommunalActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.amkj.dmsh.constant.ConstantMethod.dismissLoadhud;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.showLoadhud;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.TAOBAO_APPKEY;

/**
 * Created by xiaoxin on 2019/9/6
 * Version:v4.2.1
 * ClassDescription :百川工具类
 */
public class BaiChuanDao {
    public static void skipAliBC(Context activity, String tbUrl, String thirdId) {
        skipAliBC(activity, tbUrl, thirdId, false);
    }

    //跳转淘宝
    public static void skipAliBC(Context activity, String tbUrl, String thirdId, boolean isTbUrl) {
        BaseActivity context = (BaseActivity) activity;
        context.runOnUiThread(() -> {
            showLoadhud(context);
            //验证本地账号
            if (userId == 0) {
                getLoginStatus(context);
                dismissLoadhud(context);
                return;
            }
            //验证淘宝账号
            AlibcLogin alibcLogin = AlibcLogin.getInstance();
            alibcLogin.showLogin(new AlibcLoginCallback() {
                @Override
                public void onSuccess(int i) {
                    dismissLoadhud(context);
                    skipNewShopDetails(context, tbUrl, thirdId, isTbUrl);
                }

                @Override
                public void onFailure(int code, String msg) {
                    dismissLoadhud(context);
                    showToast(context, "登录失败 ");
                }
            });
        });
    }

    //跳转淘宝链接
    private static void skipNewShopDetails(BaseActivity context, String tbUrl, String thirdId, boolean isTbUrl) {
        //提供给三方传递配置参数
        Map<String, String> exParams = new HashMap<>();
        exParams.put(AlibcConstants.ISV_CODE, "appisvcode");
        //设置页面打开方式
        AlibcShowParams showParams = new AlibcShowParams(OpenType.Native, false);
        //淘宝客参数
        AlibcTaokeParams taokeParams = new AlibcTaokeParams();
//        taokeParams.setPid(TAOBAO_PID);
//        taokeParams.setAdzoneid(TAOBAO_ADZONEID);
//        taokeParams.setSubPid();
        taokeParams.extraParams = new HashMap<>();
        taokeParams.extraParams.put("taokeAppkey", TAOBAO_APPKEY);
        //实例化商品详情 itemID打开page
        AlibcBasePage alibcBasePage;
        if (!TextUtils.isEmpty(tbUrl)) {
            if (isTaoBaoUrl(tbUrl) || isTbUrl) {
                alibcBasePage = new AlibcPage(tbUrl.trim());
                AlibcTrade.show(context, alibcBasePage, showParams, taokeParams, exParams, new AlibcTradeCallback() {
                    @Override
                    public void onTradeSuccess(AlibcTradeResult alibcTradeResult) {
                        //打开电商组件，用户操作中成功信息回调。tradeResult：成功信息（结果类型：加购，支付；支付结果）
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        showToast(context, msg);
                    }
                });
            } else {
                //                     网页地址
                Intent intent = new Intent();
                intent.setClass(context, DoMoLifeCommunalActivity.class);
                intent.putExtra("loadUrl", tbUrl);
                context.startActivity(intent);
            }
        } else if (!TextUtils.isEmpty(thirdId)) {
            alibcBasePage = new AlibcDetailPage(thirdId.trim());
            AlibcTrade.show(context, alibcBasePage, showParams, taokeParams, exParams, new AlibcTradeCallback() {
                @Override
                public void onTradeSuccess(AlibcTradeResult alibcTradeResult) {
                    //打开电商组件，用户操作中成功信息回调。tradeResult：成功信息（结果类型：加购，支付；支付结果）
//                showToast(context, "获取详情成功");
                }

                @Override
                public void onFailure(int code, String msg) {
                    showToast(context, msg);
                }
            });
        } else {
            showToast(context, "地址缺失");
        }
    }

    //跳转我的淘宝订单
    public static void skipNewIndent(Activity context) {
        showLoadhud(context);
        //验证本地账号
        if (userId == 0) {
            getLoginStatus(context);
            dismissLoadhud(context);
            return;
        }
        //验证淘宝账号
        AlibcLogin alibcLogin = AlibcLogin.getInstance();
        alibcLogin.showLogin(new AlibcLoginCallback() {
            @Override
            public void onSuccess(int i) {
                dismissLoadhud(context);
                final Map<String, String> exParams = new HashMap<>();
                exParams.put(AlibcConstants.ISV_CODE, "appisvcode");
                final AlibcShowParams showParams = new AlibcShowParams(OpenType.Native, false);
                //实例化我的订单打开page
                final AlibcBasePage ordersPage = new AlibcMyOrdersPage(0, true);
                AlibcTrade.show(context, ordersPage, showParams, null, exParams, new AlibcTradeCallback() {
                    @Override
                    public void onTradeSuccess(AlibcTradeResult alibcTradeResult) {
                        showToast(context, "获取订单成功");
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        showToast(context, msg);
                    }
                });
            }

            @Override
            public void onFailure(int code, String msg) {
                dismissLoadhud(context);
                showToast(context, "登录失败 ");
            }
        });
    }


    //判断是否是淘宝链接
    public static boolean isTaoBaoUrl(String url) {
        Matcher matcher = Pattern.compile(AlibcTrade.ALI_URL).matcher(url);
        return matcher.find();
    }

    //退出淘宝账号
    public static void exitTaoBaoAccount(Activity context) {
        AlibcLogin alibcLogin = AlibcLogin.getInstance();
        alibcLogin.logout(new AlibcLoginCallback() {
            @Override
            public void onSuccess(int i) {
            }

            @Override
            public void onFailure(int code, String msg) {
            }
        });
    }
}
