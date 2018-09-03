package com.amkj.dmsh.constant;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.page.AlibcBasePage;
import com.alibaba.baichuan.android.trade.page.AlibcPage;
import com.alibaba.baichuan.trade.biz.AlibcConstants;
import com.alibaba.baichuan.trade.biz.context.AlibcTradeResult;
import com.alibaba.baichuan.trade.biz.core.taoke.AlibcTaokeParams;
import com.alibaba.baichuan.trade.biz.login.AlibcLogin;
import com.alibaba.baichuan.trade.biz.login.AlibcLoginCallback;
import com.amkj.dmsh.MainActivity;
import com.amkj.dmsh.R;
import com.amkj.dmsh.ServiceListenerActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.bean.XNServiceDataEntity;
import com.amkj.dmsh.dominant.activity.QualityNewUserActivity;
import com.amkj.dmsh.dominant.activity.ShopTimeScrollDetailsActivity;
import com.amkj.dmsh.homepage.activity.DoMoLifeCommunalActivity;
import com.amkj.dmsh.homepage.activity.DoMoLifeLotteryActivity;
import com.amkj.dmsh.mine.activity.MineLoginActivity;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.release.bean.RelevanceProEntity.RelevanceProBean;
import com.amkj.dmsh.shopdetails.activity.DirectMyCouponActivity;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean;
import com.amkj.dmsh.shopdetails.bean.EditGoodsSkuEntity;
import com.amkj.dmsh.shopdetails.bean.EditGoodsSkuEntity.EditGoodsSkuBean;
import com.amkj.dmsh.shopdetails.bean.ShopCarGoodsSku;
import com.amkj.dmsh.shopdetails.bean.ShopDetailsEntity.ShopPropertyBean.SkuSaleBean;
import com.amkj.dmsh.shopdetails.integration.IntegralScrollDetailsActivity;
import com.amkj.dmsh.utils.Log;
import com.amkj.dmsh.utils.MarketUtils;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.alertdialog.AlertDialogHelper;
import com.amkj.dmsh.utils.alertdialog.AlertDialogImage;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.amkj.dmsh.views.bottomdialog.SkuDialog;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;
import com.tencent.bugly.crashreport.CrashReport;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.Setting;
import com.zhy.autolayout.utils.AutoUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.xiaoneng.coreapi.ChatParamsBody;
import cn.xiaoneng.uiapi.Ntalker;
import cn.xiaoneng.uiapi.OnChatmsgListener;
import cn.xiaoneng.uiapi.OnMsgUrlClickListener;
import cn.xiaoneng.utils.CoreData;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

import static android.content.Context.MODE_PRIVATE;
import static cn.xiaoneng.uiapi.Ntalker.getExtendInstance;
import static com.ali.auth.third.core.context.KernelContext.getApplicationContext;
import static com.amkj.dmsh.base.BaseApplication.mAppContext;
import static com.amkj.dmsh.base.BaseApplication.serviceGroupId;
import static com.amkj.dmsh.base.BaseApplication.webUrlParameterTransform;
import static com.amkj.dmsh.base.BaseApplication.webUrlTransform;
import static com.amkj.dmsh.constant.ConstantVariable.COMMENT_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.IMG_REGEX_TAG;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.MES_ADVISE;
import static com.amkj.dmsh.constant.ConstantVariable.MES_FEEDBACK;
import static com.amkj.dmsh.constant.ConstantVariable.PRO_COMMENT;
import static com.amkj.dmsh.constant.ConstantVariable.PRO_TOPIC;
import static com.amkj.dmsh.constant.ConstantVariable.REGEX_TEXT;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TAOBAO_APPKEY;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_NAME_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_C_WELFARE;
import static com.amkj.dmsh.constant.TagAliasOperatorHelper.ACTION_CLEAN;
import static com.amkj.dmsh.constant.TagAliasOperatorHelper.ACTION_DELETE;
import static com.amkj.dmsh.constant.TagAliasOperatorHelper.ACTION_SET;
import static com.amkj.dmsh.constant.TagAliasOperatorHelper.TagAliasBean;
import static com.amkj.dmsh.constant.TagAliasOperatorHelper.sequence;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/3/11
 * class description:帮助类方法
 */

public class ConstantMethod {
    private OnSendCommentFinish onSendCommentFinish;
    private OnGetPermissionsSuccessListener onGetPermissionsSuccessListener;
    private Context context;
    private KProgressHUD loadHud;
    private OnAddCarListener onAddCarListener;
    private ScheduledExecutorService scheduler;
    private static Toast toast = null;
    //   定时时间更新
    private RefreshTimeListener refreshTimeListener;
    //    线程池
    private static ExecutorService executorService;
    public static int userId = 0;
    public static boolean NEW_USER_DIALOG = true;
    private AlertDialogHelper alertDialogHelper;


    //    判断变量是否为空
    public static String getStrings(String text) {
        return TextUtils.isEmpty(text) ? "" : toDBC(text);
    }

    /**
     * 判断是否为空 去掉特殊\r\n字符
     *
     * @param text
     * @return
     */
    public static String getStringFilter(String text) {
        return TextUtils.isEmpty(text) ? "" : toDBC(filterSpecial(text));
    }

    private static String filterSpecial(String text) {
        text = text.replaceAll("\n", "");
        text = text.replaceAll("\r", "");
        return text;
    }

    /**
     * 数字转String
     *
     * @param number
     * @return
     */
    public static String getStringsInteger(int number) {
        return String.valueOf(number);
    }


    /**
     * @param text
     * @return
     */
    public static int getIntegers(String text) {
        if (TextUtils.isEmpty(text)) {
            return 0;
        } else {
            try {
                return Integer.parseInt(text);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return 0;
            }
        }
    }

    /**
     * string转float
     *
     * @param text
     * @return
     */
    public static float getFloatNumber(String text) {
        if (TextUtils.isEmpty(text)) {
            return 0;
        } else {
            try {
                return Float.parseFloat(text);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return 0;
            }
        }
    }

    /**
     * 中文价格符号转换
     *
     * @param context
     * @param price
     * @return
     */

    public static String getStringsChNPrice(Context context, String price) {
        if (TextUtils.isEmpty(price)) {
            price = "0";
        }
        return String.format(context.getResources().getString(R.string.money_price_chn), price);
    }

    /**
     * 插入一条数据 fragment
     */
    public static TotalPersonalTrajectory insertFragmentNewTotalData(Context context, String typeName) {
        return insertFragmentNewTotalData(context, typeName, null);
    }

    public static TotalPersonalTrajectory insertFragmentNewTotalData(Context context, String typeName, String relateId) {
        TotalPersonalTrajectory totalPersonalTrajectory = new TotalPersonalTrajectory(context);
        Map<String, String> totalMap = new HashMap<>();
        totalMap.put(TOTAL_NAME_TYPE, typeName);
        if (!TextUtils.isEmpty(relateId)) {
            totalMap.put("relate_id", relateId);
            totalPersonalTrajectory.saveTotalDataToFile(totalMap);
        }
        totalPersonalTrajectory.saveTotalDataToFile(totalMap);
        return totalPersonalTrajectory;
    }

    /**
     * 插入一条数据 activity
     */
    public static TotalPersonalTrajectory insertNewTotalData(Context context) {
        return insertNewTotalData(context, null);
    }

    /**
     * 插入一条数据 activity
     */
    public static TotalPersonalTrajectory insertNewTotalData(Context context, String relateId) {
        TotalPersonalTrajectory totalPersonalTrajectory = new TotalPersonalTrajectory(context);
        if (!TextUtils.isEmpty(relateId)) {
            Map<String, String> totalMap = new HashMap<>();
            totalMap.put("relate_id", relateId);
            totalPersonalTrajectory.saveTotalDataToFile(totalMap);
        } else {
            totalPersonalTrajectory.saveTotalDataToFile(null);
        }
        return totalPersonalTrajectory;
    }

    /**
     * 是否从轮播图跳转商品详情
     *
     * @param context         上下文
     * @param link            链接
     * @param isBannerPage    是否是轮播图
     * @param isCloseActivity 跳转是否关闭界面
     */
    public static void setSkipPath(Context context, String link, boolean isBannerPage, boolean isCloseActivity) {
        if (isBannerPage) {
            if (getStrings(link).contains("app://ShopScrollDetailsActivity?productId=")) {
                TotalPersonalTrajectory totalPersonalTrajectory = new TotalPersonalTrajectory(context);
                Map<String, String> totalMap = new HashMap<>();
                totalMap.put("productId", getNumber(link));
                totalMap.put(TOTAL_NAME_TYPE, "BannerProduct");
                totalPersonalTrajectory.saveTotalDataToFile(totalMap);
            }
            setSkipPath(context, link, isCloseActivity);
        } else {
            setSkipPath(context, link, isCloseActivity);
        }
    }

    /**
     * @param context         上下文
     * @param link            跳转链接
     * @param isCloseActivity 是否关闭当前页面跳转
     */
    public static void setSkipPath(Context context, String link, boolean isCloseActivity) {
        String subUrl;
        String prefix = "app://";
        if (context != null) {
            Context applicationContext = context.getApplicationContext();
            if (!TextUtils.isEmpty(link)) {
                Intent intent = new Intent();
//                    app内部网址
                if (link.contains(prefix)) {
                    int prefixLength = link.indexOf(prefix) + prefix.length();
                    int urlIndex = link.indexOf("?", prefixLength);
                    if (urlIndex != -1) {
                        subUrl = link.substring(prefixLength, urlIndex).trim();
                        try {
                            Map<String, String> urlParams = getUrlParams(link);
                            Iterator<String> iterator = urlParams.keySet().iterator();
                            while (iterator.hasNext()) {
                                String keyVariable = iterator.next();
                                String valueVariable = urlParams.get(keyVariable);
                                intent.putExtra(keyVariable.trim(), valueVariable.trim());
                            }
                            intent.setAction(subUrl);
                        } catch (NumberFormatException e) {
                            intent.setAction("MainActivity");
                            e.printStackTrace();
                        }
                    } else {
                        subUrl = link.substring(prefixLength, link.length()).trim();
                        intent.setAction(subUrl);
                    }
                } else {
                    String webUrl;
                    if (link.contains("?")) {
                        webUrl = link.substring(link.lastIndexOf("/") + 1, link.indexOf("?"));
                    } else {
                        webUrl = link.substring(link.lastIndexOf("/") + 1);
                    }
                    Map<String, String> urlParams = getUrlParams(link);
                    if (webUrlTransform != null && webUrlTransform.get(webUrl) != null
                            && webUrlParameterTransform != null && webUrlParameterTransform.get(webUrl) != null) {
//                        获取本地地址
                        String skipUrl = webUrlTransform.get(webUrl);
//                        获取参数
                        Map<String, String> parameterMap = webUrlParameterTransform.get(webUrl);
                        int i = 0;
                        String parameter = "";
                        for (Map.Entry<String, String> entry : urlParams.entrySet()) {
//                            获取本地参数名字
                            if (i > 0) {
                                parameter += "&";
                            }
                            parameter += parameterMap.get(entry.getKey()) + "=" + entry.getValue();
                            i++;
                        }
                        setSkipPath(context, (skipUrl + "?" + parameter), isCloseActivity);
                        return;
                    } else if (webUrlTransform != null && webUrlTransform.get(webUrl) != null) {
                        String skipUrl = webUrlTransform.get(webUrl);
                        setSkipPath(context, skipUrl, isCloseActivity);
                        return;
                    } else {
                        intent.setClass(applicationContext, DoMoLifeCommunalActivity.class);
                        intent.putExtra("loadUrl", link);
                    }
                }
                try {
                    if (link.contains("taobao")) {
                        skipAliBCWebView(link, context);
                    } else {
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        applicationContext.startActivity(intent);
                        if (isCloseActivity) {
                            ((Activity) context).finish();
                            ((Activity) context).overridePendingTransition(0, 0);
                        }
                    }
                } catch (Exception e) {
                    skipMainActivity(context);
                    e.printStackTrace();
                }
            }
        }
    }

    private static void skipAliBCWebView(final String url, Context context) {
        if (!TextUtils.isEmpty(url)) {
            if (userId != 0) {
                skipNewTaoBao(url, context);
            } else {
                getLoginStatus((Activity) context);
            }
        } else {
            showToast(context, "地址缺失");
        }
    }

    /**
     * 登录
     *
     * @param fragment
     */
    public static void getLoginStatus(@NonNull Fragment fragment) {
        Context context = fragment.getContext();
        if (context == null) {
            return;
        }
        SharedPreferences loginStatus = context.getSharedPreferences("LoginStatus", MODE_PRIVATE);
        if (loginStatus.getBoolean("isLogin", false)) {
            userId = loginStatus.getInt("uid", 0);
        } else {
            //未登录跳转登录页
            Intent intent = new Intent(context, MineLoginActivity.class);
            fragment.startActivityForResult(intent, IS_LOGIN_CODE);
        }
    }

    /**
     * 登录
     *
     * @param activity
     */
    public static void getLoginStatus(@NonNull Activity activity) {
        SharedPreferences loginStatus = activity.getSharedPreferences("LoginStatus", MODE_PRIVATE);
        if (loginStatus.getBoolean("isLogin", false)) {
            userId = loginStatus.getInt("uid", 0);
        } else {
            //未登录跳转登录页
            Intent intent = new Intent(activity, MineLoginActivity.class);
            activity.startActivityForResult(intent, IS_LOGIN_CODE);
        }
    }

    private static void skipNewTaoBao(final String url, Context context) {
        AlibcLogin alibcLogin = AlibcLogin.getInstance();
        alibcLogin.showLogin(new AlibcLoginCallback() {
            @Override
            public void onSuccess(int i) {
                skipNewShopDetails(url, context);
            }

            @Override
            public void onFailure(int code, String msg) {
                showToast(context, "登录失败 ");
            }
        });
    }

    private static void skipNewShopDetails(String url, Context context) {
        //提供给三方传递配置参数
        Map<String, String> exParams = new HashMap<>();
        exParams.put(AlibcConstants.ISV_CODE, "appisvcode");
        //设置页面打开方式
        AlibcShowParams showParams = new AlibcShowParams(OpenType.Native, false);
        //实例化商品详情 itemID打开page
        AlibcBasePage ordersPage = new AlibcPage(url);
        AlibcTaokeParams alibcTaokeParams = new AlibcTaokeParams();
//        alibcTaokeParams.setPid(TAOBAO_PID);
//        alibcTaokeParams.setAdzoneid(TAOBAO_ADZONEID);
        alibcTaokeParams.extraParams = new HashMap<>();
        alibcTaokeParams.extraParams.put("taokeAppkey", TAOBAO_APPKEY);
        AlibcTrade.show((Activity) context, ordersPage, showParams, alibcTaokeParams, exParams, new AlibcTradeCallback() {
            @Override
            public void onTradeSuccess(AlibcTradeResult alibcTradeResult) {
                //打开电商组件，用户操作中成功信息回调。tradeResult：成功信息（结果类型：加购，支付；支付结果）
//                showToast(context, "获取详情成功");
                Log.d("商品详情", "onTradeSuccess: ");
            }

            @Override
            public void onFailure(int code, String msg) {
                Log.d("商品详情", "onFailure: " + code + msg);
                //打开电商组件，用户操作中错误信息回调。code：错误码；msg：错误信息
//                showToast(ShopTimeScrollDetailsActivity.this, msg);
            }
        });
    }

    /**
     * 获取链接中的参数
     *
     * @param url
     * @return
     */
    public static Map<String, String> getUrlParams(String url) {
        int index = url.indexOf("?");
        Map<String, String> argMap = new HashMap<>();
        if (index != -1) {
            String argStr = url.substring(index + 1);
            String[] argAry = argStr.split("&");
            argMap = new HashMap<>(argAry.length);
            for (String arg : argAry) {
                String[] argAryT = arg.split("=");
                if (argAryT.length == 2) {
                    argMap.put(argAryT[0], argAryT[1]);
                }
            }
            return argMap;
        }
        return argMap;
    }

    /**
     * 仅仅只获取阿里百川链接
     *
     * @param url
     * @return
     */
    public static Map<String, String> getOnlyUrlParams(String url) {
        int index = url.indexOf("?");
        Map<String, String> argMap = new HashMap<>();
        if (index != -1) {
            String argStr = url.substring(index + 1);
            int indexStr = argStr.indexOf("=");
            if (indexStr > 0) {
                argMap.put(argStr.substring(0, indexStr), argStr.substring(indexStr + 1, argStr.length()));
            }
            return argMap;
        }
        return argMap;
    }

    private static void skipMainActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static Badge getTopBadge(Context context, View view) {
        return getBadge(context, view, 0, 0);
    }

    public static Badge getBadge(Context context, View view) {
        return getBadge(context, view, (int) (AutoUtils.getPercentWidth1px() * 15), (int) (AutoUtils.getPercentWidth1px() * 20));
    }

    public static Badge getBadge(Context context, View view, int offsetX, int offsetY) {
        Badge badge = new QBadgeView(context).bindTarget(view);
        badge.setBadgeGravity(Gravity.END | Gravity.TOP);
        badge.setGravityOffset(offsetX, offsetY, false);
        badge.setBadgePadding(AutoUtils.getPercentWidth1px() * 3, false);
        badge.setBadgeTextSize(AutoUtils.getPercentWidth1px() * 18, false);
        badge.setBadgeBackgroundColor(context.getResources().getColor(R.color.text_normal_red));
        return badge;
    }

    public static String getNumCount(boolean isViewSel, boolean isDataSel, int numCount, String zeroString) {
        if (numCount > 1) {
            return isDataSel ?
                    (isViewSel ? String.valueOf(numCount) : String.valueOf(numCount - 1)) :
                    (isViewSel ? String.valueOf(numCount + 1) : String.valueOf(numCount));
        } else if (numCount == 1) {
            return isDataSel ?
                    (isViewSel ? String.valueOf(numCount) : zeroString) :
                    (isViewSel ? String.valueOf(numCount + 1) : String.valueOf(numCount));
        } else {
            return isViewSel ? String.valueOf(numCount + 1) : zeroString;
        }
    }

    public void setSendComment(final Context context, CommunalComment communalComment) {
        switch (communalComment.getCommType()) {
            case PRO_COMMENT:
                setGoodsComment(context, communalComment);
                break;
            case COMMENT_TYPE:
            case PRO_TOPIC:
            case TYPE_C_WELFARE:
                setDocComment(context, communalComment);
                break;
            case MES_ADVISE:
                setAdviceData(context, communalComment);
                break;
            case MES_FEEDBACK:
                setFeedbackData(context, communalComment);
                break;
            default:
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                if (onSendCommentFinish != null) {
                    onSendCommentFinish.onError();
                }
                showToast(context, R.string.comment_send_failed);
                break;
        }

    }


    private void setGoodsComment(final Context context, CommunalComment communalComment) {
        String url = Url.BASE_URL + Url.GOODS_COMMENT;
        Map<String, Object> params = new HashMap<>();
        //回复评论
        params.put("uid", communalComment.getUserId());
        params.put("obj_id", communalComment.getObjId());
        params.put("content", communalComment.getContent());
        params.put("is_at", 0);
        params.put("com_type", "goods");
        if (communalComment.getIsReply() == 1) {
            params.put("is_reply", 1);
            params.put("reply_uid", communalComment.getReplyUserId());
            //评论id
            params.put("pid", communalComment.getPid());
        } else {
            //回复文章或帖子
            params.put("is_reply", 0);
        }
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals("01")) {
                        if (onSendCommentFinish != null) {
                            onSendCommentFinish.onSuccess();
                        }
                        showToast(context.getApplicationContext(), R.string.comment_send_success);
                    } else {
                        if (onSendCommentFinish != null) {
                            onSendCommentFinish.onError();
                        }
                        showToast(context.getApplicationContext(), requestStatus.getMsg());
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (onSendCommentFinish != null) {
                    onSendCommentFinish.onError();
                }
                showToast(context.getApplicationContext(), R.string.comment_send_failed);
                super.onError(ex, isOnCallback);
            }
        });
    }

    private void setDocComment(final Context context, CommunalComment communalComment) {
        String url = Url.BASE_URL + Url.FIND_COMMENT;
        Map<String, Object> params = new HashMap<>();
        //回复文章或帖子
        params.put("is_reply", communalComment.getIsReply());
        params.put("uid", communalComment.getUserId());
        params.put("to_uid", communalComment.getToUid());
        params.put("obj_id", communalComment.getObjId());
        params.put("content", communalComment.getContent());
        params.put("com_type", communalComment.getCommType());
        if (communalComment.getIsReply() == 1) {
            params.put("reply_uid", communalComment.getReplyUserId() > 0 ? String.valueOf(communalComment.getReplyUserId()) : "");
            params.put("pid", communalComment.getPid() > 0 ? String.valueOf(communalComment.getPid()) : "");
            params.put("pid_type", communalComment.getCommType());
            params.put("main_comment_id", communalComment.getMainCommentId() > 0 ? String.valueOf(communalComment.getMainCommentId()) : "");
        }
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals("01")) {
                        if (onSendCommentFinish != null) {
                            onSendCommentFinish.onSuccess();
                        }
                    } else {
                        if (onSendCommentFinish != null) {
                            onSendCommentFinish.onError();
                        }
                        showToast(context.getApplicationContext(), requestStatus.getMsg());
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (onSendCommentFinish != null) {
                    onSendCommentFinish.onError();
                }
                showToast(context.getApplicationContext(), R.string.comment_send_failed);
            }
        });
    }

    /**
     * 发送留言
     *
     * @param context
     * @param communalComment
     */
    private void setAdviceData(final Context context, CommunalComment communalComment) {
        String url = Url.BASE_URL + Url.SEARCH_LEAVE_MES;
        Map<String, Object> params = new HashMap<>();
        params.put("uid", communalComment.getUserId());
        params.put("content", getStrings(communalComment.getContent()));
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals("01")) {
                        if (onSendCommentFinish != null) {
                            onSendCommentFinish.onSuccess();
                        }
                        showToast(context, R.string.Submit_Success);
                    } else {
                        if (onSendCommentFinish != null) {
                            onSendCommentFinish.onError();
                        }
                        showToast(context, requestStatus.getMsg() + ",请重新提交");
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                if (onSendCommentFinish != null) {
                    onSendCommentFinish.onError();
                }
                showToast(context, R.string.commit_failed);
                super.onError(ex, isOnCallback);
            }
        });
    }

    /**
     * 发送意见反馈
     *
     * @param context
     * @param communalComment
     */
    private void setFeedbackData(final Context context, CommunalComment communalComment) {
        String url = Url.BASE_URL + Url.MINE_FEEDBACK;
        Map<String, Object> params = new HashMap<>();
        params.put("uid", communalComment.getUserId());
        params.put("remark", getStrings(communalComment.getContent()));
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                Gson gson = new Gson();
                RequestStatus requestInfo = gson.fromJson(result, RequestStatus.class);
                if (requestInfo != null) {
                    if (requestInfo.getCode().equals("01")) {
                        if (onSendCommentFinish != null) {
                            onSendCommentFinish.onSuccess();
                        }
                        showToast(context, R.string.Submit_Success);
                    } else {
                        if (onSendCommentFinish != null) {
                            onSendCommentFinish.onError();
                        }
                        showToast(context, requestInfo.getResult() != null ?
                                requestInfo.getResult().getMsg() : requestInfo.getMsg());
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                if (onSendCommentFinish != null) {
                    onSendCommentFinish.onError();
                }
                showToast(context, R.string.commit_failed);
            }
        });
    }

    /**
     * 针对TextView显示中文中出现的排版错乱问题，通过调用此方法得以解决
     *
     * @param str
     * @return 返回全部为全角字符的字符串
     */
    public static String toDBC(String str) {
        char[] c = str.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375) {
                c[i] = (char) (c[i] - 65248);
            }
        }
        return new String(c);
    }

    /**
     * 格式化 文本 本地语言
     *
     * @param formatString
     * @param args
     * @return
     */
    public static String formatStrings(String formatString, Object... args) {
        return String.format(Locale.getDefault(), formatString, args);
    }

    /**
     * 默认图片 文字描述
     *
     * @param context
     * @param content
     * @return
     */
    public static View getEmptyView(Context context, String content) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_communal_empty, null, false);
        TextView tv_communal_empty = (TextView) view.findViewById(R.id.tv_communal_empty);
        tv_communal_empty.setText(String.format(content, R.string.no_found_content));
        return view;
    }

    //    文章分享统计
    public static void addArticleShareCount(int articleId) {
        String url = Url.BASE_URL + Url.ARTICLE_SHARE_COUNT;
        Map<String, Object> params = new HashMap<>();
        //回复文章或帖子
        params.put("id", articleId);
        XUtil.Post(url, params, new MyCallBack<String>() {
        });
    }

    public static void totalPushMessage(@NonNull String pushId) {
        String url = Url.BASE_URL + Url.TOTAL_PUSH_INFO;
        Map<String, Object> params = new HashMap<>();
        //回复文章或帖子
        params.put("pushId", pushId);
        if (userId > 0) {
            params.put("uid", userId);
        }
        XUtil.Post(url, params, new MyCallBack<String>() {
        });
    }

    //    分享成功 奖励
    public static void shareRewardSuccess(int uid, final Context context) {
        String url = Url.BASE_URL + Url.SHARE_SUCCESS;
        Map<String, Object> params = new HashMap<>();
        params.put("uid", uid);
        params.put("version", 2);
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                RequestStatus requestStatus = RequestStatus.objectFromData(result);
                if (requestStatus != null && requestStatus.getCode().equals("01")
                        && !TextUtils.isEmpty(requestStatus.getSrc())) {
                    GlideImageLoaderUtil.loadFinishImgDrawable(context, requestStatus.getSrc(), new GlideImageLoaderUtil.ImageLoaderFinishListener() {
                        @Override
                        public void onSuccess(Bitmap bitmap) {
                            AlertDialogImage alertDialogAdImage = new AlertDialogImage();
                            AlertDialog alertImageAdDialog = alertDialogAdImage.createAlertDialog(context);
                            alertImageAdDialog.show();
                            alertDialogAdImage.setAlertClickListener(new AlertDialogImage.AlertImageClickListener() {
                                @Override
                                public void imageClick() {
                                    Intent intent = new Intent(context, DoMoLifeLotteryActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(intent);
                                    alertImageAdDialog.dismiss();
                                }
                            });
                            alertDialogAdImage.setImage(bitmap);
                        }

                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onError(Drawable errorDrawable) {

                        }
                    });
                }
            }
        });
    }

    //      统计文章点击商品
    public static void totalProNum(int productId, int artId) {
        String url = Url.BASE_URL + Url.TOTAL_PRO_NUM;
        Map<String, Object> params = new HashMap<>();
        //回复文章或帖子
        params.put("product_id", productId);
        params.put("doc_id", artId);
        XUtil.Post(url, params, new MyCallBack<String>() {
        });
    }

    //      统计福利社点击商品
    public static void totalWelfareProNum(int productId, int topId) {
        String url = Url.BASE_URL + Url.TOTAL_WELFARE_PRO_NUM;
        Map<String, Object> params = new HashMap<>();
        //回复文章或帖子
        params.put("productId", productId);
        params.put("topId", topId);
        XUtil.Post(url, params, new MyCallBack<String>() {
        });
    }

    //      统计官方通知点击商品
    public static void totalOfficialProNum(int productId, String officialId) {
        String url = Url.BASE_URL + Url.TOTAL_OFFICIAL_PRO_NUM;
        Map<String, Object> params = new HashMap<>();
        //回复文章或帖子
        params.put("productId", productId);
        params.put("cId", officialId);
        XUtil.Post(url, params, new MyCallBack<String>() {
        });
    }

    //    统计广告点击
    public static void adClickTotal(int adId) {
        String url = Url.BASE_URL + Url.TOTAL_AD_COUNT;
        Map<String, Object> params = new HashMap<>();
        //回复文章或帖子
        params.put("id", adId);
        XUtil.Post(url, params, new MyCallBack<String>() {
        });
    }

    /**
     * 统计JPush打开数目
     *
     * @param pushType
     */
    public void clickTotalPush(String pushType, String id) {
        String url = Url.BASE_URL + Url.TOTAL_JPUSH_COUNT;
        Map<String, Object> params = new HashMap<>();
        params.put("type", pushType);
        if (!TextUtils.isEmpty(id)) {
            params.put("id", id);
        }
        XUtil.Post(url, params, new MyCallBack<String>() {
        });
    }

    /**
     * @param context
     * @param proTypeId 商品类型
     * @param id        商品Id
     */
    public static void skipProductUrl(Context context, int proTypeId, int id) {
        Intent intent = new Intent();
        switch (proTypeId) {
            case 0://淘宝商品
                intent.setClass(context, ShopTimeScrollDetailsActivity.class);
                intent.putExtra("productId", String.valueOf(id));
                context.startActivity(intent);
                break;
            case 1://自营商品
                intent.setClass(context, ShopScrollDetailsActivity.class);
                intent.putExtra("productId", String.valueOf(id));
                context.startActivity(intent);
                break;
            case 2://积分商品
                intent.setClass(context, IntegralScrollDetailsActivity.class);
                intent.putExtra("productId", String.valueOf(id));
                context.startActivity(intent);
                break;
        }
    }

    //    获取系统权限
    public void getPermissions(final Context context, final String... permissions) {
        this.context = context;
        AndPermission.with(context)
                .runtime()
                .permission(permissions)
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        if (AndPermission.hasAlwaysDeniedPermission(context, permissions)) {
                            // 这里使用一个Dialog展示没有这些权限应用程序无法继续运行，询问用户是否去设置中授权。
                            if (alertDialogHelper == null) {
                                alertDialogHelper = new AlertDialogHelper(context)
                                        .setTitle("权限提示")
                                        .setMsg("还缺少 " + TextUtils.join(" ", Permission.transformText(context, data))
                                                + " 重要权限，为了不影响使用，请到设置-应用管理打开权限")
                                        .setSingleButton(true)
                                        .setConfirmText("去设置");
                                alertDialogHelper.setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                                    @Override
                                    public void confirm() {
                                        // 如果用户继续：
                                        AndPermission.with(context)
                                                .runtime()
                                                .setting()
                                                .onComeback(new Setting.Action() {
                                                    @Override
                                                    public void onAction() {
                                                        if (AndPermission.hasPermissions(context, permissions)) {
                                                            if (onGetPermissionsSuccessListener != null) {
                                                                onGetPermissionsSuccessListener.getPermissionsSuccess();
                                                            }
                                                        }
                                                    }
                                                })
                                                .start();
                                        alertDialogHelper.dismiss();
                                    }

                                    @Override
                                    public void cancel() {
                                        alertDialogHelper.dismiss();
                                    }
                                });
                            }
                            alertDialogHelper.show();

                        }
                    }
                })
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        if (onGetPermissionsSuccessListener != null) {
                            onGetPermissionsSuccessListener.getPermissionsSuccess();
                        }
                    }
                })
                /*.rationale(new Rationale<List<String>>() {
                    @Override
                    public void showRationale(Context context, List<String> data, RequestExecutor executor) {
//                        if(data!=null&&data.size()>1){
//                            executor.execute();
//                            // 这里使用一个Dialog询问用户是否继续授权。
//                            AlertDialogHelper alertDialogTitleConfirmCancel = new AlertDialogHelper();
//                            alertDialogTitleConfirmCancel.setAlertClickListener(new AlertDialogHelper.AlertConfirmCancelListener() {
//                                @Override
//                                public void confirm() {
//                                    // 如果用户继续：
//                                    executor.execute();
//                                    if(permissionAlertView!=null&&permissionAlertView.isShowing()){
//                                        permissionAlertView.dismiss();
//                                    }
//                                }
//
//                                @Override
//                                public void cancel() {
//                                    // 如果用户中断：
//                                    executor.cancel();
//                                }
//                            });
//                            permissionAlertView = alertDialogTitleConfirmCancel.createAlertDialog(context
//                                    , "还缺少一些必要的权限，为了不影响使用，是否继续申请权限", "继续"
//                                    , "", false);
//                            permissionAlertView.show();
//                        }else{
//                            executor.execute();
//                        }
                        executor.execute();
                    }
//                })*/
                .start();
    }

    /**
     * 上传设备信息
     *
     * @param context        上下文环境
     * @param oldVersionName 手机系统版本
     * @param oldOsVersion   后台数据APP版本
     * @param oldMobileModel 手机型号
     */
    public static void setDeviceInfo(Context context, String oldVersionName, String oldMobileModel, String oldOsVersion) {
//        系统版本号
        String osVersion = Build.VERSION.RELEASE;
//        手机型号
        String mobileModel = Build.MODEL;
//        app版本
        String versionName = getVersionName(context);
        if (!getStrings(oldVersionName).equals(versionName)
                || !getStrings(oldMobileModel).equals(mobileModel)
                || !getStrings(oldOsVersion).equals(osVersion)) {
            upDeviceInfo(context, osVersion, mobileModel, versionName);
        }
    }

    /**
     * 上传设备信息
     *
     * @param osVersion
     * @param mobileModel
     * @param versionName
     */
    private static void upDeviceInfo(Context context, String osVersion, String mobileModel, String versionName) {
        if (NetWorkUtils.checkNet(context)) {
            String url = Url.BASE_URL + Url.DEVICE_INFO;
            Map<String, Object> params = new HashMap<>();
            params.put("device_source", "android");
            params.put("app_version_no", versionName);
            params.put("device_sys_version", osVersion);
            params.put("device_model", mobileModel);
            params.put("uid", userId);
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    super.onSuccess(result);
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    super.onError(ex, isOnCallback);
                }
            });
        }
    }

    /**
     * 图片修改尺寸
     *
     * @param bitmap
     * @param screenWidth
     * @return
     */
    public static Bitmap zoomImage(Bitmap bitmap, int screenWidth) {
        // 获取这个图片的宽和高
        float width = bitmap.getWidth();
        float height = bitmap.getHeight();
        Bitmap newBitmap = null;
        try {
            float scaleWidth = ((float) screenWidth) / width;
            // 创建操作图片用的matrix对象
            Matrix matrix = new Matrix();
            // 计算宽高缩放率
            // 缩放图片动作
            matrix.postScale(scaleWidth, scaleWidth);
            newBitmap = Bitmap.createBitmap(bitmap, 0, 0, (int) width,
                    (int) height, matrix, true);
        } catch (Exception e) {
            newBitmap = bitmap;
            e.printStackTrace();
        }
        return newBitmap;
    }

    public static List<CommunalDetailObjectBean> getDetailsDataList(List<CommunalDetailBean> descriptionList) {
        CommunalDetailObjectBean detailObjectBean;
        List<CommunalDetailObjectBean> descriptionDetailList = new ArrayList<>();
        if (descriptionList != null && descriptionList.size() > 0) {
            for (int i = 0; i < descriptionList.size(); i++) {
                detailObjectBean = new CommunalDetailObjectBean();
                CommunalDetailBean descriptionBean = descriptionList.get(i);
                if (descriptionBean.getType().equals("goods")) {
                    try {
                        Map<String, Object> hashMap = (Map<String, Object>) descriptionBean.getContent();
                        detailObjectBean.setItemType(CommunalDetailObjectBean.TYPE_GOODS_WEL);
                        detailObjectBean.setName(hashMap.get("name") + "");
                        detailObjectBean.setId(((Double) hashMap.get("id")).intValue());
                        detailObjectBean.setItemTypeId(((Double) hashMap.get("itemTypeId")).intValue());
                        detailObjectBean.setPicUrl((String) hashMap.get("picUrl"));
                        detailObjectBean.setPrice(hashMap.get("price") + "");
                    } catch (Exception e) {
                        detailObjectBean = null;
                    }
                } else if (descriptionBean.getType().equals("coupon")) {
                    try {
                        detailObjectBean.setItemType(CommunalDetailObjectBean.TYPE_COUPON);
                        Map<String, Object> hashMap = (Map<String, Object>) descriptionBean.getContent();
                        detailObjectBean.setPicUrl((String) hashMap.get("picUrl"));
                        detailObjectBean.setNewPirUrl((String) hashMap.get("newPirUrl"));
                        detailObjectBean.setId(((Double) hashMap.get("id")).intValue());
                    } catch (Exception e) {
                        detailObjectBean = null;
                    }
                } else if (descriptionBean.getType().equals("taobaoLink")) {
                    detailObjectBean.setItemType(CommunalDetailObjectBean.TYPE_LINK_TAOBAO);
                    detailObjectBean.setContent(getStrings(descriptionBean.getText()));
                    detailObjectBean.setPicUrl(getStrings(descriptionBean.getImage()));
                    detailObjectBean.setUrl(getStrings(descriptionBean.getAndroidLink()));
                } else if (descriptionBean.getType().equals("text")) {
                    String content = (String) descriptionBean.getContent();
                    if (!TextUtils.isEmpty(content)) {
//                    判断是否有图片
                        Matcher imgIsFind = Pattern.compile(IMG_REGEX_TAG).matcher(content);
                        boolean isImageTag = imgIsFind.find();
                        if (isImageTag) {
                            detailObjectBean = null;
                            String stringContent = imgIsFind.group();
                            Matcher matcher = Pattern.compile(REGEX_TEXT).matcher(stringContent);
                            boolean hasImgUrl = matcher.find();
                            CommunalDetailObjectBean communalDetailObjectBean;
                            while (hasImgUrl) {
                                String imgUrl = matcher.group();
                                if (imgUrl.contains(".gif")) {
                                    communalDetailObjectBean = new CommunalDetailObjectBean();
                                    communalDetailObjectBean.setPicUrl(imgUrl);
                                    communalDetailObjectBean.setItemType(CommunalDetailObjectBean.TYPE_GIF_IMG);
                                    descriptionDetailList.add(communalDetailObjectBean);
                                } else {
                                    List<String> imageCropList = getImageCrop(imgUrl, 10000);
                                    for (String imageUrl : imageCropList) {
                                        communalDetailObjectBean = new CommunalDetailObjectBean();
                                        String imgUrlContent = ("<span><img src=\"" + imageUrl + "\" /></span>");
                                        communalDetailObjectBean.setContent(imgUrlContent);
                                        communalDetailObjectBean.setItemType(CommunalDetailObjectBean.NORTEXT);
                                        descriptionDetailList.add(communalDetailObjectBean);
                                    }
                                }
                                hasImgUrl = matcher.find();
                            }
                        } else {
//                        正文
                            if (i == 0) {
                                detailObjectBean.setFirstLinePadding(true);
                            }
                            detailObjectBean.setContent(content);
                            detailObjectBean.setItemType(CommunalDetailObjectBean.NORTEXT);
                        }
                    }
                } else if (descriptionBean.getType().equals("pictureGoods")) { //图片地址
                    detailObjectBean.setItemType(CommunalDetailObjectBean.TYPE_GOODS_IMG);
                    detailObjectBean.setNewPirUrl(getStrings(descriptionBean.getPicUrl()));
                    detailObjectBean.setId(descriptionBean.getId());
                } else if (descriptionBean.getType().equals("video")) {
                    detailObjectBean.setItemType(CommunalDetailObjectBean.TYPE_VIDEO);
                    detailObjectBean.setUrl(getStrings((String) descriptionBean.getContent()));
                    detailObjectBean.setPicUrl(getStrings(descriptionBean.getImage()));
                } else if (descriptionBean.getType().equals("audio")) {
                    detailObjectBean.setItemType(CommunalDetailObjectBean.TYPE_AUDIO);
                    detailObjectBean.setUrl(getStrings((String) descriptionBean.getContent()));
                    detailObjectBean.setPicUrl(getStrings(descriptionBean.getImage()));
                    detailObjectBean.setName(getStrings(descriptionBean.getName()));
                    detailObjectBean.setFrom(getStrings(descriptionBean.getFrom()));
                } else if (descriptionBean.getType().equals("share")) {
                    detailObjectBean.setItemType(CommunalDetailObjectBean.TYPE_SHARE);
                } else if (descriptionBean.getType().equals("couponPackage")) {
                    try {
                        Map<String, Object> hashMap = (Map<String, Object>) descriptionBean.getContent();
                        detailObjectBean.setItemType(CommunalDetailObjectBean.TYPE_COUPON_PACKAGE);
                        detailObjectBean.setPicUrl((String) hashMap.get("imgUrl"));
                        detailObjectBean.setCpName((String) hashMap.get("cpName"));
                        detailObjectBean.setId(((Double) hashMap.get("cpId")).intValue());
                    } catch (Exception e) {
                        detailObjectBean = null;
                    }
                } else {
                    detailObjectBean = null;
                }
                if (detailObjectBean != null) {
                    descriptionDetailList.add(detailObjectBean);
                }
            }
        }
        return descriptionDetailList;
    }

    /**
     * 暂时限制每张图片不能超过4096*4096
     * 图片大图截取
     */
    private static List<String> getImageCrop(String imageUrl, int sizeHeight) {
        int maxSize = 4096;
        List<String> imageCropList = new ArrayList<>();
//        oss图片样式
//        根据图片大小 获取展示在屏幕的真正大小
        if (sizeHeight > maxSize) {
            float scale = AutoUtils.getPercentWidth1px();
            int imageNormalSize = 2500;
            int imageCount = (int) (sizeHeight * scale / imageNormalSize);
            if (imageCount > 0) {
                imageCount += (scale % maxSize != 0 ? 1 : 0);
                String ossPrefix = "?x-oss-process=image";
                String imageNewUrl;
                if (!imageUrl.contains(ossPrefix)) {
                    imageNewUrl = imageUrl + ossPrefix;
                } else {
                    imageNewUrl = imageUrl;
                }
                for (int i = 0; i < imageCount; i++) {
                    String cropNewUrl = imageNewUrl + String.format(mAppContext.getString(R.string.image_crop_style), imageNormalSize, i);
                    imageCropList.add(cropNewUrl);
                }
            } else {
                imageCropList.add(imageUrl);
            }
        } else {
            imageCropList.add(imageUrl);
        }
        return imageCropList;
    }

    /**
     * @param context
     * @param baseAddCarProInfoBean 商品基本信息
     * @param loadHud
     */
    public void addShopCarGetSku(final Context context, final BaseAddCarProInfoBean baseAddCarProInfoBean, final KProgressHUD loadHud) {
        //商品详情内容
        this.loadHud = loadHud;
        this.context = context;
        String url = Url.BASE_URL + Url.Q_SHOP_DETAILS_GET_SKU_CAR;
        Map<String, Object> params = new HashMap<>();
        params.put("productId", baseAddCarProInfoBean.getProductId());
        params.put("uid", userId);
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                Gson gson = new Gson();
                EditGoodsSkuEntity editGoodsSkuEntity = gson.fromJson(result, EditGoodsSkuEntity.class);
                if (editGoodsSkuEntity != null) {
                    if (editGoodsSkuEntity.getCode().equals("01")) {
                        EditGoodsSkuBean editGoodsSkuBean = editGoodsSkuEntity.getEditGoodsSkuBean();
                        List<SkuSaleBean> skuSaleBeanList = editGoodsSkuBean.getSkuSale();
                        if (skuSaleBeanList == null) {
                            return;
                        }
                        if (skuSaleBeanList.size() > 1) {
                            setSkuValue(context, editGoodsSkuBean, baseAddCarProInfoBean);
                        } else {
                            SkuSaleBean skuSaleBean = skuSaleBeanList.get(0);
                            if ((skuSaleBean.getIsNotice() == 1 || skuSaleBean.getIsNotice() == 2) && skuSaleBean.getQuantity() == 0) {
                                setSkuValue(context, editGoodsSkuBean, baseAddCarProInfoBean);
                            } else {
                                if (skuSaleBean.getQuantity() > 0) {
                                    ShopCarGoodsSku shopCarGoodsSkuDif = new ShopCarGoodsSku();
                                    shopCarGoodsSkuDif.setCount(1);
                                    shopCarGoodsSkuDif.setSaleSkuId(editGoodsSkuBean.getSkuSale().get(0).getId());
                                    shopCarGoodsSkuDif.setPrice(Double.parseDouble(editGoodsSkuBean.getSkuSale().get(0).getPrice()));
                                    shopCarGoodsSkuDif.setProductId(editGoodsSkuBean.getId());
                                    shopCarGoodsSkuDif.setPicUrl(editGoodsSkuBean.getPicUrl());
                                    shopCarGoodsSkuDif.setActivityCode(getStrings(baseAddCarProInfoBean.getActivityCode()));
                                    shopCarGoodsSkuDif.setValuesName(!TextUtils.isEmpty(editGoodsSkuBean.getPropvalues().get(0).getPropValueName())
                                            ? editGoodsSkuBean.getPropvalues().get(0).getPropValueName() : "默认");
                                    addShopCar(shopCarGoodsSkuDif);
                                } else {
                                    showToast(context, "商品已售罄，正在努力补货中~~~");
                                }
                            }
                        }
                    } else {
                        showToast(context, editGoodsSkuEntity.getMsg());
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                showToast(context, context.getString(R.string.unConnectedNetwork));
                super.onError(ex, isOnCallback);
            }
        });
    }

    private void setSkuValue(Context context, final EditGoodsSkuEntity.EditGoodsSkuBean editGoodsSkuBean, final BaseAddCarProInfoBean baseAddCarProInfoBean) {
        //        sku 展示
        SkuDialog skuDialog = new SkuDialog((Activity) context);
        if (!TextUtils.isEmpty(baseAddCarProInfoBean.getProPic())) {
            editGoodsSkuBean.setPicUrl(baseAddCarProInfoBean.getProPic());
        }
        if (TextUtils.isEmpty(editGoodsSkuBean.getProductName())) {
            editGoodsSkuBean.setProductName(getStrings(baseAddCarProInfoBean.getProName()));
        }
        editGoodsSkuBean.setShowBottom(true);
        skuDialog.refreshView(editGoodsSkuBean);
        skuDialog.show();
        skuDialog.getGoodsSKu(shopCarGoodsSku -> {
            if (shopCarGoodsSku != null) {
//                    加入购物车
                if (loadHud != null) {
                    loadHud.show();
                }
                shopCarGoodsSku.setProductId(baseAddCarProInfoBean.getProductId());
                shopCarGoodsSku.setActivityCode(getStrings(baseAddCarProInfoBean.getActivityCode()));
                addShopCar(shopCarGoodsSku);
            }
        });
    }

    private void addShopCar(final ShopCarGoodsSku shopCarGoodsSku) {
        if (userId != 0) {
//          加入购物车
            String url = Url.BASE_URL + Url.Q_SHOP_DETAILS_ADD_CAR;
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            params.put("productId", shopCarGoodsSku.getProductId());
            params.put("saleSkuId", shopCarGoodsSku.getSaleSkuId());
            params.put("count", shopCarGoodsSku.getCount());
            params.put("price", shopCarGoodsSku.getPrice());
            if (!TextUtils.isEmpty(shopCarGoodsSku.getActivityCode())) {
                params.put("activityCode", shopCarGoodsSku.getActivityCode());
            }
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    if (loadHud != null) {
                        loadHud.dismiss();
                    }
                    RequestStatus status = gson.fromJson(result, RequestStatus.class);
                    if (status != null) {
                        if (status.getCode().equals("01")) {
                            showToast(context, context.getString(R.string.AddCarSuccess));
                            if (onAddCarListener != null) {
                                onAddCarListener.onAddCarSuccess();
                            }
                            TotalPersonalTrajectory totalPersonalTrajectory = new TotalPersonalTrajectory(context);
                            Map<String, String> totalMap = new HashMap<>();
                            totalMap.put("productId", String.valueOf(shopCarGoodsSku.getProductId()));
                            totalMap.put(TOTAL_NAME_TYPE, "addCartSuccess");
                            totalPersonalTrajectory.saveTotalDataToFile(totalMap);
                        } else {
                            showToast(context, status.getMsg());
                        }
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    if (loadHud != null) {
                        loadHud.dismiss();
                    }
                    showToast(context, context.getString(R.string.unConnectedNetwork));
                    super.onError(ex, isOnCallback);
                }
            });
        }
    }

    /**
     * 加入购物车
     *
     * @param onAddCarListener
     */
    public void setAddOnCarListener(OnAddCarListener onAddCarListener) {
        this.onAddCarListener = onAddCarListener;
    }

    /**
     * 访问客服
     *
     * @param context
     * @param chatParamsBody
     */
    public static void skipInitDataXNService(Context context, ChatParamsBody chatParamsBody) {
        requestPermissions(context);
        skipXNService(context, chatParamsBody);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static void requestPermissions(Context context) {
        String[] permissions = {
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        };
        getExtendInstance().ntalkerSystem().requestPermissions((Activity) context, permissions);
    }

    /**
     * 跳转客服
     *
     * @param context
     * @param chatParamsBody
     */
    public static void skipXNService(final Context context, final ChatParamsBody chatParamsBody) {
        if (NetWorkUtils.isConnectedByState(context)) {
            String url = Url.BASE_URL + Url.XN_SERVICE;
            XUtil.Post(url, null, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    XNServiceDataEntity xnServiceDataEntity = XNServiceDataEntity.objectFromData(result);
                    if (xnServiceDataEntity != null && SUCCESS_CODE.equals(xnServiceDataEntity.getCode())
                            && xnServiceDataEntity.getServiceDataList() != null
                            && xnServiceDataEntity.getServiceDataList().size() > 0) {
                        setServiceDate(context, xnServiceDataEntity, chatParamsBody);
                    } else {
                        getDefaultService(context, chatParamsBody);
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    getDefaultService(context, chatParamsBody);
                    super.onError(ex, isOnCallback);
                }
            });
        }
    }

    private static void setServiceDate(Context context, XNServiceDataEntity xnServiceDataEntity, ChatParamsBody chatParamsBody) {
        Calendar calendar = Calendar.getInstance();
        if (!TextUtils.isEmpty(xnServiceDataEntity.getServer_time())) {
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                Date currentDate = simpleDateFormat.parse(xnServiceDataEntity.getServer_time());
                calendar.setTime(currentDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        int i = calendar.get(Calendar.HOUR_OF_DAY);
        boolean isCheckConfirm = false;
        String serviceId = "";
        reach:
        for (XNServiceDataEntity.XNServiceDataBean xnServiceDataBean : xnServiceDataEntity.getServiceDataList()) {
            String weeks = xnServiceDataBean.getWeeks();
//                    比对星期
            if (!TextUtils.isEmpty(weeks) && weeks.contains(String.valueOf(week))) {
//                        比对时间
                if (!TextUtils.isEmpty(xnServiceDataBean.getBegin_time())
                        && !TextUtils.isEmpty(xnServiceDataBean.getEnd_time())) {
                    if (getDataFormatHour(xnServiceDataBean.getBegin_time()) <= i
                            && i <= getDataFormatHour(xnServiceDataBean.getEnd_time())
                            && !TextUtils.isEmpty(xnServiceDataBean.getXn_id())) {
                        isCheckConfirm = true;
                        serviceId = xnServiceDataBean.getXn_id();
                        break reach;
                    }
                }
            }
        }
        if (isCheckConfirm && !TextUtils.isEmpty(serviceId)) {
            try {
                chatParamsBody.clickurltoshow_type = CoreData.CLICK_TO_APP_COMPONENT;
                Ntalker.getExtendInstance().message().setOnMsgUrlClickListener(new OnMsgUrlClickListener() {
                    @Override
                    public void onClickUrlorEmailorNumber(int contentType, String s) {
                        setSkipPath(context, s, false);
                    }
                });
                Ntalker.getBaseInstance().startChat(context.getApplicationContext()
                        , serviceId, "蝗虫团购", chatParamsBody, ServiceListenerActivity.class);

            } catch (Exception e) {
                CrashReport.setUserId(String.valueOf(userId));
                CrashReport.postCatchedException(e);
            }
        } else {
            getDefaultService(context, chatParamsBody);
        }
    }


    private static void getDefaultService(Context context, ChatParamsBody chatParamsBody) {
        try {
            chatParamsBody.clickurltoshow_type = CoreData.CLICK_TO_APP_COMPONENT;
            Ntalker.getExtendInstance().message().setOnMsgUrlClickListener(new OnMsgUrlClickListener() {
                @Override
                public void onClickUrlorEmailorNumber(int contentType, String s) {
                    setSkipPath(context, s, false);
                }
            });
            Ntalker.getBaseInstance().startChat(context.getApplicationContext()
                    , serviceGroupId, "蝗虫团购", chatParamsBody, ServiceListenerActivity.class);
        } catch (Exception e) {
            e.printStackTrace();
            CrashReport.setUserId(String.valueOf(userId));
            CrashReport.postCatchedException(e);
        }
    }

    /**
     * 普通登录
     *
     * @param context
     * @param uid
     * @param nickName
     * @param phone
     */
    public static void loginXNService(Context context, int uid, String nickName, String phone) {
        loginXNService(context, uid, nickName, phone, 0);
    }

    /**
     * 等级登录
     *
     * @param context
     * @param uid
     * @param nickName
     * @param phone
     * @param level
     */
    public static void loginXNService(Context context, int uid, String nickName, String phone, int level) {
        Ntalker.getBaseInstance().login(String.valueOf(uid), getStrings(nickName)
                + " " + phone + " " + getVersionName(context), level);
    }

    public static int getUnReadServiceMessage() {
        final int[] mesCount = {getMesCount()};
        if (userId != 0) {
            //传递用户信息
            Ntalker.getExtendInstance().message().setOnChatmsgListener(new OnChatmsgListener() {
                @Override
                public void onChatMsg(boolean isSelfMsg, String settingId, String username, String msgContent, long msgTime, boolean isUnread, int unReadCount, String uIcon) {
                    if (isUnread) {
                        mesCount[0] = getMesCount();
                    }
                }
            });
        }
        return mesCount[0];
    }

    private static int getMesCount() {
        List<Map<String, Object>> list = Ntalker.getExtendInstance().conversation().getList();
        int mesCount = 0;
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                if ((boolean) list.get(i).get("isunread")) {
                    int messageCount = (int) list.get(i).get("messagecount");
                    mesCount += messageCount;
                }
            }
        }
        return mesCount;
    }

    /**
     * 过滤掉常见特殊字符,常见的表情
     *
     * @param editText
     */
    public static void setEtFilter(EditText editText) {
        if (editText == null) {
            return;
        }
        //表情过滤器
        InputFilter emojiFilter = new InputFilter() {

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest,
                                       int dstart, int dend) {
                Pattern emoji = Pattern.compile(
                        "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                        Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
                Matcher emojiMatcher = emoji.matcher(source);
                if (emojiMatcher.find()) {
                    return "";
                }
                return null;
            }
        };
        //特殊字符过滤器
        InputFilter specialCharFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String regexStr = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
                Pattern pattern = Pattern.compile(regexStr);
                Matcher matcher = pattern.matcher(source.toString());
                if (matcher.matches()) {
                    return "";
                } else {
                    return null;
                }

            }
        };
        editText.setFilters(new InputFilter[]{emojiFilter, specialCharFilter});
    }


    /**
     * 获取版本号
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 文件版本是否高于当前安装版本
     *
     * @param context
     * @param archiveFilePath
     * @return
     */
    public static boolean isHeightVersion(Context context, String archiveFilePath) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo info = pm.getPackageArchiveInfo(archiveFilePath, PackageManager.GET_ACTIVITIES);
            if (info != null) {
                PackageManager packageManager = context.getPackageManager();
                // getPackageName()是你当前类的包名，0代表是获取版本信息
                PackageInfo currentPackageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
                if (currentPackageInfo.packageName.equals(info.packageName) && info.versionCode > currentPackageInfo.versionCode) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 保存个人信息
     *
     * @param context
     * @param savePersonalInfo
     */
    public static void savePersonalInfoCache(Context context, SavePersonalInfoBean savePersonalInfo) {
        if (savePersonalInfo != null && savePersonalInfo.isLogin()) {
            userId = savePersonalInfo.getUid();
            SharedPreferences loginStatus = context.getSharedPreferences("LoginStatus", MODE_PRIVATE);
            SharedPreferences.Editor edit = loginStatus.edit();
            edit.putBoolean("isLogin", true);
            edit.putInt("uid", savePersonalInfo.getUid());
            edit.putString("nickName", getStrings(savePersonalInfo.getNickName()));
            edit.putString("avatar", getStrings(savePersonalInfo.getAvatar()));
            edit.putString("P_NUM", getStrings(savePersonalInfo.getPhoneNum()));
            if (!TextUtils.isEmpty(savePersonalInfo.getOpenId())) {
                edit.putString("OPEN_ID", getStrings(savePersonalInfo.getOpenId()));
            }
            if (!TextUtils.isEmpty(savePersonalInfo.getLoginType())) {
                edit.putString("LOGIN_TYPE", getStrings(savePersonalInfo.getLoginType()));
            }
            if (!TextUtils.isEmpty(savePersonalInfo.getUnionId())) {
                edit.putString("UNION_ID", getStrings(savePersonalInfo.getUnionId()));
            }
            edit.apply();
            EventBus.getDefault().post(new EventMessage("loginShowDialog", ""));
        } else {
            userId = 0;
            SharedPreferences loginStatus = context.getSharedPreferences("LoginStatus", MODE_PRIVATE);
            SharedPreferences.Editor edit = loginStatus.edit();
            edit.putBoolean("isLogin", false);
            edit.clear();
            edit.apply();
        }
    }

    /**
     * 获取个人信息
     *
     * @param context
     * @return
     */
    public static SavePersonalInfoBean getPersonalInfo(Context context) {
        SharedPreferences loginStatus = context.getSharedPreferences("LoginStatus", MODE_PRIVATE);
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
     * 绑定JPush
     *
     * @param uid
     */
    public static void bindJPush(int uid) {
        TagAliasBean tagAliasBean = new TagAliasBean();
        tagAliasBean.action = ACTION_SET;
        tagAliasBean.isAliasAction = true;
        tagAliasBean.alias = String.valueOf(uid);
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
    public static void unBindJPush() {
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

    /**
     * 格式化浮点型数字
     *
     * @param discountPrice
     * @return
     */
    public static String formatFloatNumber(float discountPrice) {
        int intValue = (int) (Math.abs(discountPrice * 100));
        int intNum = intValue / 100;
        int remainder = intValue % 100;
        if (intNum >= 1) {
            if (remainder != 0) {
                return intNum + "." + remainder / 10 + (remainder % 10 != 0 ? remainder % 10 : "");
            } else {
                return String.valueOf(intNum);
            }
        } else {
            return 0 + "." + remainder / 10 + (remainder % 10 != 0 ? remainder % 10 : "");
        }
    }

    /**
     * @param totalRelevanceProBeans 关联商品总数
     * @param relevanceProBeans      已选择关联商品
     * @return
     */
    public static List<RelevanceProBean> changeRelevanceProduct(List<RelevanceProBean> totalRelevanceProBeans,
                                                                List<RelevanceProBean> relevanceProBeans) {
        if (totalRelevanceProBeans != null) {
            if (relevanceProBeans != null && relevanceProBeans.size() > 0) {
                for (RelevanceProBean relevanceProBean : totalRelevanceProBeans) {
                    for (RelevanceProBean relevancePro : relevanceProBeans) {
                        if (relevanceProBean.getId() == relevancePro.getId()) {
                            relevanceProBean.setSelPro(true);
                            break;
                        } else {
                            relevanceProBean.setSelPro(false);
                        }
                    }
                }
            } else {
                for (RelevanceProBean relevanceProBean : totalRelevanceProBeans) {
                    relevanceProBean.setSelPro(false);
                }
            }
        }
        return totalRelevanceProBeans;
    }

    public static List<RelevanceProBean> selRelevanceProduct(List<RelevanceProBean> selectProductBeans) {
        List<RelevanceProBean> relevanceProBeanList = new ArrayList<>();
        for (RelevanceProBean relevanceProBean : selectProductBeans) {
            boolean isAdd = false;
            for (RelevanceProBean relevancePro : relevanceProBeanList) {
                if (relevancePro.getId() == relevanceProBean.getId()) {
                    isAdd = true;
                    break;
                }
            }
            if (!isAdd) {
                relevanceProBeanList.add(relevanceProBean);
            }
        }
        return relevanceProBeanList;
    }

    /**
     * 处理短信验证码 错误code
     *
     * @param status
     */
    public static void disposeMessageCode(Context context, int status) {
        switch (status) {
            case 458:
                showToast(context, "手机号码在黑名单中,请联系该地运营商解除短信限制");
                break;
            case 461:
            case 602:
                showToast(context, "不支持该地区发送短信，请选择国内运营商号码");
                break;
            case 462:
                showToast(context, "每分钟发送短信的数量超过限制，请稍后重试");
                break;
            case 463:
            case 464:
            case 465:
            case 477:
            case 478:
                showToast(context, "手机号码每天发送次数超限，请明天再试");
                break;
            case 467:
                showToast(context, "5分钟内校验错误超过3次，验证码失效，请重新获取验证码");
                break;
            case 468:
                showToast(context, "验证码错误，请重新输入验证码");
                break;
            case 472:
                showToast(context, "发送短信验证过于频繁，请稍后再试");
                break;
            case 601:
                showToast(context, "短信发送受限");
                break;
            default:
                showToast(context, "验证码校验失败，请退出重试");
                break;
        }
    }

    public void getNewUserCouponDialog(Context context) {
        if (NetWorkUtils.checkNet(context) && NEW_USER_DIALOG) {
            NEW_USER_DIALOG = false;
            String url = Url.BASE_URL + Url.H_NEW_USER_COUPON;
            Map<String, Object> params = new HashMap<>();
            params.put("user_id", userId);
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    RequestStatus requestStatus = RequestStatus.objectFromData(result);
                    if (requestStatus != null) {
                        if (requestStatus.getCode().equals(SUCCESS_CODE)
                                && !TextUtils.isEmpty(requestStatus.getImgUrl())
                                && 0 < requestStatus.getUserType() && requestStatus.getUserType() < 4) {
                            //                                    弹窗
                            GlideImageLoaderUtil.loadFinishImgDrawable(context, requestStatus.getImgUrl(), new GlideImageLoaderUtil.ImageLoaderFinishListener() {
                                @Override
                                public void onSuccess(Bitmap bitmap) {
                                    if (isContextExisted(context)) {
                                        AlertDialogImage alertDialogImage = new AlertDialogImage();
                                        AlertDialog alertImageDialog = alertDialogImage.createAlertDialog(context);
                                        alertImageDialog.show();
                                        alertDialogImage.setAlertClickListener(new AlertDialogImage.AlertImageClickListener() {
                                            @Override
                                            public void imageClick() {
                                                Intent intent = new Intent();
                                                switch (requestStatus.getUserType()) {
                                                    case 1: //新人用户
                                                        intent.setClass(context, QualityNewUserActivity.class);
                                                        context.startActivity(intent);
                                                        break;
//                                                    领取优惠券
                                                    case 2:
                                                    case 3:
                                                        getNewUserCoupon(context, requestStatus.getCouponId());
                                                        break;
                                                }
                                                alertImageDialog.dismiss();
                                            }
                                        });
                                        alertDialogImage.setImage(bitmap);
                                    }
                                }

                                @Override
                                public void onStart() {

                                }

                                @Override
                                public void onError(Drawable errorDrawable) {

                                }
                            });
                        }
                    }
                }
            });
        }
    }

    /**
     * 领取新人优惠券
     *
     * @param context
     * @param couponId
     */
    private void getNewUserCoupon(Context context, int couponId) {
        if (couponId > 0) {
            String url = Url.BASE_URL + Url.FIND_ARTICLE_COUPON;
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            params.put("couponId", couponId);
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                    if (requestStatus != null) {
                        if (requestStatus.getCode().equals("01")) {
                            showToast(context, requestStatus.getResult() != null ? requestStatus.getResult().getMsg() : requestStatus.getMsg());
                            Intent intent = new Intent(context, DirectMyCouponActivity.class);
                            context.startActivity(intent);
                        }
                    }
                }
            });

        }
    }

    /**
     * 判断context 是否存活，避免badToken
     *
     * @param context
     * @return
     */
    public static boolean isContextExisted(Context context) {
        return context != null && (context instanceof Activity && !((Activity) context).isFinishing()
                || context instanceof Service && isServiceExisted(context, context.getClass().getName())
                || context instanceof Application);
    }

    private static boolean isServiceExisted(Context context, String className) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager == null) {
            return false;
        }
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager
                .getRunningServices(Integer.MAX_VALUE);
        if (!(serviceList.size() > 0)) {
            return false;
        }
        for (int i = 0; i < serviceList.size(); i++) {
            ActivityManager.RunningServiceInfo serviceInfo = serviceList.get(i);
            ComponentName serviceName = serviceInfo.service;

            if (serviceName.getClassName().equals(className)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 安装app
     */
    public static void installApps(Context context, File appFile) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            android 8.0及以上 app内部安装App权限申请
            AndPermission.with(context)
                    .install()
                    .file(appFile)
                    .onGranted(file -> allowInstallApps(context, appFile))
                    .onDenied(file -> {
                        AlertDialogHelper alertDialogHelper = new AlertDialogHelper(context)
                                .setTitle("权限提示")
                                .setMsg("还缺少 " + "未知来源安装"
                                        + " 重要权限，为了不影响体验更多精彩功能，请到设置-应用管理打开权限")
                                .setSingleButton(true)
                                .setConfirmText("去设置");
                        alertDialogHelper.setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void confirm() {
                                Uri packageURI = Uri.parse("package:" + context.getPackageName());
                                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }

                            @Override
                            public void cancel() {

                            }
                        });
                        alertDialogHelper.show();
                    })
                    .start();
        } else {
            allowInstallApps(context, appFile);
        }
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    private static void allowInstallApps(Context context, File appFile) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(appFile),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    public interface OnAddCarListener {
        void onAddCarSuccess();
    }

    //    发送评论回调
    public void setOnSendCommentFinish(OnSendCommentFinish onSendCommentFinish) {
        this.onSendCommentFinish = onSendCommentFinish;
    }

    public interface OnSendCommentFinish {
        void onSuccess();

        void onError();
    }

    public void setOnGetPermissionsSuccess(OnGetPermissionsSuccessListener onGetPermissionsSuccessListener) {
        this.onGetPermissionsSuccessListener = onGetPermissionsSuccessListener;
    }

    //    获取权限回调
    public interface OnGetPermissionsSuccessListener {
        void getPermissionsSuccess();
    }

    /**
     * 提取数字
     *
     * @param str
     * @return
     */
    public static String getNumber(String str) {
        String regex = "\\d*";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        while (m.find()) {
            if (!TextUtils.isEmpty(m.group())) {
                return m.group();
            }
        }
        return "0";
    }

    /**
     * 提取全部数据拼接
     *
     * @param str
     * @return
     */
    public static String getAppendNumber(String str) {
        String appendNumber = "";
        String regex = "\\d*";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        while (m.find()) {
            appendNumber += m.group();
        }
        return !TextUtils.isEmpty(appendNumber) ? appendNumber : "0";
    }

    /**
     * 广告展示时间
     * 大于1s少于6s
     *
     * @param str
     * @return
     */
    public static int getShowNumber(String str) {
        int defaultSecond = 3;
        if (!TextUtils.isEmpty(str)) {
            String regex = "\\d*";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(str);
            while (m.find()) {
                String textNum = m.group();
                if (!TextUtils.isEmpty(textNum)) {
                    int num = Integer.parseInt(textNum);
                    if (num > 2 && num <= 5) {
                        defaultSecond = num;
                    }
                }
            }
            return defaultSecond;
        } else {
            return defaultSecond;
        }
    }

    /**
     * @param time           时间参数
     * @param timeSwitchover 切换时间格式
     * @return
     */
    public static String getDateFormat(String time, String timeSwitchover) {
        if (!TextUtils.isEmpty(time)) {
            try {
                SimpleDateFormat timeReceiveFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                Date date = timeReceiveFormat.parse(time);
                SimpleDateFormat timeFormat = new SimpleDateFormat(!TextUtils.isEmpty(timeSwitchover)
                        ? timeSwitchover : "yyyy-MM-dd", Locale.CHINA);
                return timeFormat.format(date);
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        } else {
            return "";
        }
    }

    /**
     * 时间转换格式 默认当前时间跟转换时间 为年-月-日
     *
     * @param time
     * @param timeSwitchover
     * @return
     */
    public static String getDateFormat(String time, String currentSwitchover, String timeSwitchover) {
        if (!TextUtils.isEmpty(time)) {
            try {
                SimpleDateFormat timeReceiveFormat = new SimpleDateFormat(TextUtils.isEmpty(currentSwitchover) ? "yyyy-MM-dd HH:mm:ss" : currentSwitchover, Locale.CHINA);
                Date date = timeReceiveFormat.parse(time);
                SimpleDateFormat timeFormat = new SimpleDateFormat(!TextUtils.isEmpty(timeSwitchover)
                        ? timeSwitchover : "yyyy-MM-dd", Locale.CHINA);
                return timeFormat.format(date);
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        } else {
            return "";
        }
    }

    /**
     * 时间获取毫秒
     *
     * @param time
     * @return
     */
    public static long getDateMilliSecond(String time) {
        if (!TextUtils.isEmpty(time)) {
            try {
                SimpleDateFormat timeReceiveFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                Date date = timeReceiveFormat.parse(time);
                return date.getTime();
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        } else {
            return 0;
        }
    }

    /**
     * 判断是否是同一年份
     *
     * @param t1
     * @param t2
     * @return
     */
    public static boolean isTimeSameYear(String t1, String t2) {
        if (!TextUtils.isEmpty(t1) && !TextUtils.isEmpty(t2)) {
            try {
                SimpleDateFormat timeReceiveFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                Date d1 = timeReceiveFormat.parse(t1);
                Date d2 = timeReceiveFormat.parse(t2);
                SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy", Locale.CHINA);
                return timeFormat.format(d1).equals(timeFormat.format(d2));
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 时分秒获取 时
     *
     * @param time
     * @return
     */
    private static int getDataFormatHour(String time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
        try {
            Date date = simpleDateFormat.parse(time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar.get(Calendar.HOUR_OF_DAY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取当前星期几
     *
     * @param time
     * @return
     */
    public static int getDataFormatWeek(String time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            Calendar calendar = Calendar.getInstance();
            if (TextUtils.isEmpty(time)) {
                Date date = calendar.getTime();
                time = simpleDateFormat.format(date);
            }
            Date date = simpleDateFormat.parse(time);
            calendar.setTime(date);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            calendar.set(Calendar.DAY_OF_MONTH, day);  //指定日
            int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            switch (week) {
                case 0:
                    week = 7;
                    break;
            }
            return week;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 时间是否已结束
     *
     * @param t1 当前时间
     * @param t2 结束时间
     * @return
     */
    public static boolean isEndOrStartTime(String t1, String t2) {
        if (!TextUtils.isEmpty(t1) && !TextUtils.isEmpty(t2)) {
            try {
                SimpleDateFormat timeReceiveFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                Date d1 = timeReceiveFormat.parse(t1);
                Date d2 = timeReceiveFormat.parse(t2);
                return d1.getTime() >= d2.getTime();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 获取当前时间 "yyyy-MM-dd HH:mm:ss"
     *
     * @return
     */
    public static String getCurrentTime() {
        SimpleDateFormat timeReceiveFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return timeReceiveFormat.format(Calendar.getInstance().getTime());
    }

    /**
     * 时间是否已结束
     *
     * @param t1        开始时间
     * @param t2        结束时间
     * @param addSecond 时间展示
     * @return
     */
    public static boolean isEndOrEndTimeAddSeconds(String t1, String t2, long addSecond) {
        if (!TextUtils.isEmpty(t1) && !TextUtils.isEmpty(t2)) {
            try {
                SimpleDateFormat timeReceiveFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                Date d1 = timeReceiveFormat.parse(t1);
                Date d2 = timeReceiveFormat.parse(t2);
                return d1.getTime() >= (d2.getTime() + addSecond * 1000);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 时间是否已结束
     *
     * @param t1        开始时间
     * @param t2        结束时间
     * @param addSecond 时间展示
     * @return
     */
    public static boolean isEndOrStartTimeAddSeconds(String t1, String t2, long addSecond) {
        if (!TextUtils.isEmpty(t1) && !TextUtils.isEmpty(t2)) {
            try {
                SimpleDateFormat timeReceiveFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                Date d1 = timeReceiveFormat.parse(t1);
                Date d2 = timeReceiveFormat.parse(t2);
                return (d1.getTime() + addSecond * 1000) >= d2.getTime();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * date 时间对比
     *
     * @param t1
     * @param t2
     * @param addSecond
     * @return
     */
    public static boolean isEndOrStartTimeAddSeconds(Date t1, Date t2, long addSecond) {
        if (t1 != null && t2 != null) {
            try {
                return (t1.getTime() + addSecond * 1000) >= t2.getTime();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    public static Date timeFormatSwitch(String t1) {
        if (!TextUtils.isEmpty(t1)) {
            try {
                SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                Date d1 = timeFormat.parse(t1);
                return d1;
            } catch (Exception e) {
                e.printStackTrace();
                return new Date();
            }
        } else {
            return new Date();
        }
    }

    /**
     * 对比两个时间是否是同一天
     *
     * @param t1
     * @param t2
     * @return
     */
    public static boolean isSameTimeDay(String t1, String t2) {
        if (!TextUtils.isEmpty(t1) && !TextUtils.isEmpty(t2)) {
            try {
                SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                Date d1 = timeFormat.parse(t1);
                Date d2 = timeFormat.parse(t2);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(d1);
                int day1 = calendar.get(Calendar.DAY_OF_MONTH);
                calendar.setTime(d2);
                int day2 = calendar.get(Calendar.DAY_OF_MONTH);
                return day1 == day2;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 话题高粱配置
     *
     * @param context
     * @param topicString 高亮内容
     * @param content     整条内容
     * @return
     */
    public CharSequence topicFormat(Context context, String topicString, String content) {
        Link link = new Link(getStrings(topicString));
        link.setTextColor(0xff0a88fa);
        link.setUnderlined(false);
        link.setHighlightAlpha(0f);
        return LinkBuilder.from(context, getStrings(content))
                .addLink(link)
                .build();
    }

    /**
     * 获取应用市场
     */
    public static void getMarketApp(Context context) {
        //        获取已安装应用商店的包名列表
        try {
            List<PackageInfo> packageInfo = context.getPackageManager().getInstalledPackages(0);
            List<String> marketPackages = MarketUtils.getMarketPackages();
            String appMarketStore = "";
            outLoop:
            for (int i = 0; i < packageInfo.size(); i++) {
                for (int j = 0; j < marketPackages.size(); j++) {
                    if (packageInfo.get(i).packageName.equals(marketPackages.get(j))) {
                        appMarketStore = marketPackages.get(j);
                        break outLoop;
                    }
                }
            }
            if (!TextUtils.isEmpty(appMarketStore)) {
                try {
                    MarketUtils.launchAppDetail(getApplicationContext(), context.getPackageName(), appMarketStore);
                } catch (Exception e) {
                    skipDownStore(context);
                }
            } else {
                skipDownStore(context);
            }
        } catch (Exception e) {
            skipDownStore(context);
        }
    }

    /**
     * 跳转应用宝下载中心
     *
     * @param context
     */
    private static void skipDownStore(Context context) {
        String DownUriAddress = "http://app.qq.com/#id=detail&appid=1101070898";
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(DownUriAddress);
        intent.setData(content_url);
        context.startActivity(intent);
    }

    /**
     * 创建线程定时器
     */
    public ScheduledExecutorService createSchedule() {
        return createSchedule(1000);
    }

    /**
     * 间隔时间定时器
     *
     * @param longMilliseconds 单位毫秒
     * @return
     */
    public ScheduledExecutorService createSchedule(int longMilliseconds) {
        if (getScheduler() == null
                || (getScheduler() != null && getScheduler().isShutdown())) {
            stopSchedule();
            scheduler = Executors.newScheduledThreadPool(1);
            scheduler.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    synchronized (this) {
                        Message message = mHandler.obtainMessage();
                        message.arg1 = 1;
                        mHandler.sendMessage(message);
                    }
                }
            }, longMilliseconds, longMilliseconds, TimeUnit.MILLISECONDS);
        }
        return scheduler;
    }

    /**
     * 获取当前scheduler
     */
    public ScheduledExecutorService getScheduler() {
        return scheduler;
    }

    /**
     * 主线程接收更新
     */
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (message.arg1 == 1) {
                if (refreshTimeListener != null) {
                    refreshTimeListener.refreshTime();
                }
            }
            return false;
        }
    });

    /**
     * 停用定时器
     */
    public void stopSchedule() {
        if (scheduler != null) {
            scheduler.shutdownNow();
        }
    }

    /**
     * 创建线程池
     */
    public static ExecutorService createExecutor() {
        if (executorService == null) {
            int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
            int KEEP_ALIVE_TIME = 1;
            TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
            BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<Runnable>();
            executorService = new ThreadPoolExecutor(NUMBER_OF_CORES,
                    NUMBER_OF_CORES * 2, KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, taskQueue);
        }
        return executorService;
    }

    /**
     * 移除引用的Handler
     */
    public void releaseHandlers() {
        try {
            Class<?> clazz = getClass();
            Field[] fields = clazz.getDeclaredFields();
            if (fields == null || fields.length <= 0) {
                return;
            }
            for (Field field : fields) {
                field.setAccessible(true);
                if (!Handler.class.isAssignableFrom(field.getType())) continue;
                Handler handler = (Handler) field.get(this);
                if (handler != null && handler.getLooper() == Looper.getMainLooper()) {
                    handler.removeCallbacksAndMessages(null);
                }
                field.setAccessible(false);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public interface RefreshTimeListener {
        void refreshTime();
    }

    public void setRefreshTimeListener(RefreshTimeListener refreshTimeListener) {
        this.refreshTimeListener = refreshTimeListener;
    }

    /**
     * 解决Toast重复弹出 长时间不消失的问题
     *
     * @param context
     * @param message
     */
    public static void showToast(Context context, String message) {
        if (toast == null) {
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        } else {
            toast.setText(message);
        }
        //设置新的消息提示
        toast.show();
    }

    /**
     * 资源文件文本
     *
     * @param context
     * @param resId
     */
    public static void showToast(Context context, int resId) {
        showToast(context, context.getString(resId));
    }

    /**
     * 正则
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /**
     * 判断当前存储卡是否可用
     **/
    public static boolean checkSDCardAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取当前存储位置
     **/
    public String takePicRootDir(Context context, @NonNull String filePathName) {
        if (checkSDCardAvailable()) {
            return Environment.getExternalStorageDirectory().getPath() + "/" + filePathName;
        } else {
            return context.getFilesDir().getPath() + "/" + filePathName;
        }
    }

    //判断文件是否存在
    public boolean fileIsExist(String invoiceSavePath) {
        File file = new File(invoiceSavePath);
        return file.exists();
    }

    //创建文件夹
    public void createFilePath(String savePath) {
        File destDir = new File(savePath);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
    }

    /**
     * 获取时分秒
     *
     * @param time
     * @return
     */
    public static String getHMSFormatString(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0) {
            return "00:00";
        } else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = getZeroString(minute) + ":" + getZeroString(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                if (hour > 0) {
                    timeStr = getZeroString(hour) + ":" + getZeroString(minute) + ":" + getZeroString(second);
                } else {
                    timeStr = getZeroString(minute) + ":" + getZeroString(second);
                }
            }
        }
        return timeStr;
    }

    private static String getZeroString(int i) {
        if (i >= 0 && i < 10)
            return "0" + Integer.toString(i);
        else
            return "" + i;
    }

    /**
     * 获取设备唯一码
     *
     * @param context
     * @return
     */
    public static String getUniqueId(Context context) {
        String androidID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        String id = androidID + Build.SERIAL;
        return id;
    }

    /**
     * md5加密
     *
     * @param text
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String toMD5(String text) {
        //获取摘要器 MessageDigest
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            //通过摘要器对字符串的二进制字节数组进行hash计算
            byte[] digest = messageDigest.digest(text.getBytes());

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < digest.length; i++) {
                //循环每个字符 将计算结果转化为正整数;
                int digestInt = digest[i] & 0xff;
                //将10进制转化为较短的16进制
                String hexString = Integer.toHexString(digestInt);
                //转化结果如果是个位数会省略0,因此判断并补0
                if (hexString.length() < 2) {
                    sb.append(0);
                }
                //将循环结果添加到缓冲区
                sb.append(hexString);
            }
            //返回整个结果
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return text;
        }
    }

    /**
     * 高精度相除
     *
     * @param v1
     * @param v2
     * @param scale 保留小数
     * @return
     */
    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
