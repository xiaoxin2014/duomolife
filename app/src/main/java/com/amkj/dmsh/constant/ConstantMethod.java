package com.amkj.dmsh.constant;

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
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.amkj.dmsh.MainActivity;
import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.HomeQualityFloatAdEntity;
import com.amkj.dmsh.bean.ImageBean;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.dao.AddClickDao;
import com.amkj.dmsh.dominant.activity.QualityGroupShopDetailActivity;
import com.amkj.dmsh.dominant.activity.ShopTimeScrollDetailsActivity;
import com.amkj.dmsh.dominant.bean.GroupShopDetailsEntity.GroupShopDetailsBean;
import com.amkj.dmsh.find.activity.ImagePagerActivity;
import com.amkj.dmsh.find.activity.JoinTopicActivity;
import com.amkj.dmsh.find.activity.PostDetailActivity;
import com.amkj.dmsh.find.activity.TopicDetailActivity;
import com.amkj.dmsh.homepage.activity.DoMoLifeCommunalActivity;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity;
import com.amkj.dmsh.homepage.bean.ScoreGoodsEntity;
import com.amkj.dmsh.homepage.bean.ScoreGoodsEntity.ScoreGoodsBean;
import com.amkj.dmsh.message.bean.MessageCenterEntity;
import com.amkj.dmsh.mine.activity.MineLoginActivity;
import com.amkj.dmsh.mine.bean.ShopCarEntity.ShopCartBean.CartBean.CartInfoBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.qyservice.QyServiceUtils;
import com.amkj.dmsh.rxeasyhttp.utils.DeviceUtils;
import com.amkj.dmsh.shopdetails.activity.DirectExchangeDetailsActivity;
import com.amkj.dmsh.shopdetails.activity.DirectIndentWriteActivity;
import com.amkj.dmsh.shopdetails.activity.DoMoRefundDetailActivity;
import com.amkj.dmsh.shopdetails.activity.RefundMoneyActivity;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.shopdetails.bean.IndentProDiscountBean;
import com.amkj.dmsh.shopdetails.integration.IntegralScrollDetailsActivity;
import com.amkj.dmsh.user.activity.UserPagerActivity;
import com.amkj.dmsh.utils.MarketUtils;
import com.amkj.dmsh.utils.alertdialog.AlertDialogHelper;
import com.amkj.dmsh.utils.alertdialog.AlertDialogImage;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.hjq.toast.ToastUtils;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;
import com.tencent.bugly.beta.tinker.TinkerManager;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.Setting;

import java.io.File;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
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

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import me.jessyan.autosize.AutoSize;
import me.jessyan.autosize.utils.AutoSizeUtils;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

import static android.content.Context.MODE_PRIVATE;
import static com.ali.auth.third.core.context.KernelContext.getApplicationContext;
import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.base.WeChatPayConstants.APP_ID;
import static com.amkj.dmsh.constant.ConstantVariable.BUY_AGAIN;
import static com.amkj.dmsh.constant.ConstantVariable.COMMENT_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_GROUP_SHOP;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.MES_ADVISE;
import static com.amkj.dmsh.constant.ConstantVariable.MES_FEEDBACK;
import static com.amkj.dmsh.constant.ConstantVariable.PRO_COMMENT;
import static com.amkj.dmsh.constant.ConstantVariable.PRO_TOPIC;
import static com.amkj.dmsh.constant.ConstantVariable.REGEX_SPACE_CHAR;
import static com.amkj.dmsh.constant.ConstantVariable.REGEX_TEXT;
import static com.amkj.dmsh.constant.ConstantVariable.REGEX_URL;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_C_WELFARE;
import static com.amkj.dmsh.constant.UMShareAction.routineId;
import static com.amkj.dmsh.constant.Url.CHECK_BUY_AGAIN_NEWV2;
import static com.amkj.dmsh.constant.Url.CHECK_ORDER_SETTLE_INFOV2;
import static com.amkj.dmsh.constant.Url.H_Q_FLOAT_AD;
import static com.amkj.dmsh.constant.Url.LOTTERY_URL;
import static com.amkj.dmsh.dao.BaiChuanDao.isTaoBaoUrl;
import static com.amkj.dmsh.dao.BaiChuanDao.skipAliBC;
import static com.yanzhenjie.permission.AndPermission.getFileUri;

/**
 * @author LGuiPeng
 */

public class ConstantMethod {
    private OnSendCommentFinish onSendCommentFinish;
    private OnGetPermissionsSuccessListener onGetPermissionsSuccessListener;
    private Context context;
    private KProgressHUD loadHud;
    private ScheduledExecutorService scheduler;
    private static Toast toast = null;
    //   定时时间更新
    private RefreshTimeListener refreshTimeListener;
    //    线程池
    private static ExecutorService executorService;
    public static int userId = 0;
    public static boolean NEW_USER_DIALOG = true;
    private AlertDialogHelper alertDialogHelper;
    public AlertDialogHelper alertImportDialogHelper;
//    private AlertDialogHelper alertDialogRequestHelper;


    //    判断变量是否为空
    public static String getStrings(String text) {
        return TextUtils.isEmpty(text) ? "" : toDBC(text);
    }

    /**
     * 判断该字符串是否为空
     *
     * @param text
     * @return
     */
    public static boolean isEmptyStrings(String text) {
        return TextUtils.isEmpty(text) || text.equals("null");
    }

    /**
     * 判断是否为空 去掉特殊空格\r\n字符
     *
     * @param text
     * @return
     */
    public static String getStringFilter(String text) {
        return TextUtils.isEmpty(text) ? "" : toDBC(filterSpecial(text));
    }

    private static String filterSpecial(String text) {
        Pattern p = Pattern.compile(REGEX_SPACE_CHAR);
        Matcher m = p.matcher(text);
        text = m.replaceAll("");
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
     * 判空并且返回值
     *
     * @param object       基本类型，对象为空
     * @param defaultValue 空值默认返回
     * @return
     */
    public static Object getMapValue(Object object, Object defaultValue) {
        if (object == null) {
            return defaultValue;
        }
        return object;
    }

    /**
     * 强制装换成String类型数据
     *
     * @param object 此方法仅适用非bean对象 为了过滤网络框架不支持value为Object
     * @return
     */
    public static String getStringMapValue(Object object) {
        if (object == null) {
            return "";
        }
        return object + "";
    }

    /**
     * String转换成int
     */
    public static int getStringChangeIntegers(String text) {
        if (TextUtils.isEmpty(text)) {
            return 0;
        } else {
            try {
                return Integer.parseInt(stripTrailingZeros(text));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return 0;
            }
        }
    }


    /**
     * String转换成double(先去掉中文和货币符号)
     */
    public static double getStringChangeDouble(String text) {
        if (TextUtils.isEmpty(text)) {
            return 0;
        } else {
            try {
                Matcher m = Pattern.compile("[\\u4e00-\\u9fa5]").matcher(text);
                return Double.parseDouble(m.replaceAll("").replaceAll("¥", "").trim());
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return 0;
            }
        }
    }

    /**
     * String转换成boolean
     */
    public static boolean getStringChangeBoolean(String text) {
        if (TextUtils.isEmpty(text)) {
            return false;
        } else {
            try {
                return Integer.parseInt(text) == 1;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    /**
     * String转换成boolean
     */
    public static long getStringChangeLong(String text) {
        if (TextUtils.isEmpty(text)) {
            return 0;
        } else {
            try {
                return Long.parseLong(text);
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
     * 带货币单位价格格式化
     */
    public static CharSequence getRmbFormat(Context context, String priceText) {
        return getRmbFormat(context, priceText, true);
    }


    /**
     * 带货币单位价格格式化
     *
     * @param append 是否追加货币符号
     * @return
     */
    public static CharSequence getRmbFormat(Context context, String priceText, boolean append) {
        try {
            String price = getStrings(priceText);
            if (!TextUtils.isEmpty(price)) {
                price = append ? "¥" + price : price;
                Pattern pattern = Pattern.compile("[¥]");
                Link link = new Link(pattern);
                link.setTextColor(Color.parseColor("#ff5a6b"));
                link.setTextSize(AutoSizeUtils.mm2px(mAppContext, 22));
                link.setUnderlined(false);
                link.setHighlightAlpha(0f);
                return LinkBuilder.from(context, price)
                        .addLink(link)
                        .build();
            } else {
                return context.getResources().getString(R.string.defaul);
            }
        } catch (Exception e) {
            return getStrings("¥" + priceText);
        }
    }

    /**
     * 带货币单位价格格式化
     *
     * @param proportion 字体大小缩放比例
     * @return
     */
    public static CharSequence getSpannableString(String text, int start, int end, float proportion, String color) {
        return getSpannableString(text, start, end, proportion, color, false);
    }


    /**
     * @param bold 是否粗体
     * @return
     */
    public static CharSequence getSpannableString(String text, int start, int end, float proportion, String color, boolean bold) {
        if (TextUtils.isEmpty(text)) return "";
        SpannableString spannableString = null;
        try {
            spannableString = new SpannableString(text);
            if (proportion > 0) {
                RelativeSizeSpan sizeSpan = new RelativeSizeSpan(proportion);
                spannableString.setSpan(sizeSpan, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }

            if (!TextUtils.isEmpty(color)) {
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor(color));
                spannableString.setSpan(colorSpan, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }

            if (bold) {
                StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
                spannableString.setSpan(styleSpan, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }

        } catch (Exception e) {
            return text;
        }

        return spannableString;
    }


    /**
     * 去掉多余的0
     *
     * @param priceText
     * @return
     */
    public static String stripTrailingZeros(String priceText) {
        try {
            return new BigDecimal(priceText.replaceAll(" ", "")).stripTrailingZeros().toPlainString();
        } catch (Exception e) {
            return getStrings(priceText);
        }
    }


    /**
     * @param context
     * @param resStringId
     * @param textString
     * @return
     */
    public static String getStringsFormat(Context context, int resStringId, String... textString) {
        if (context == null) {
            return "";
        }
        if (resStringId <= 0) {
            return "";
        }
        if (textString.length == 1) {
            return String.format(context.getResources().getString(resStringId), getStrings(textString[0]));
        } else {
            return String.format(context.getResources().getString(resStringId), getStrings(textString[0]), getStrings(textString[1]));
        }
    }

    public static String getIntegralFormat(Context context, int resStringId, @NonNull int... number) {
        if (context == null) {
            return "";
        }
        if (resStringId <= 0) {
            return "";
        }
        if (number.length == 1) {
            return String.format(context.getResources().getString(resStringId), number[0]);
        } else {
            return String.format(context.getResources().getString(resStringId), number[0], number[1]);
        }
    }

    public static String getDoubleFormat(Context context, int resStringId, @NonNull double number) {
        if (context == null) {
            return "";
        }
        if (resStringId <= 0) {
            return "";
        }
        return String.format(context.getResources().getString(resStringId), number);
    }

    /**
     * 获取去重数组
     *
     * @param strings
     * @return
     */
    public static String[] getDistinctString(String[] strings) {
        Set<String> stringSet = new LinkedHashSet<>(Arrays.asList(strings));
        List<String> strings1 = new ArrayList<>(stringSet);
        return strings1.toArray(new String[strings1.size()]);
    }


    /**
     * @param context         上下文
     * @param link            跳转链接
     * @param isCloseActivity 是否关闭当前页面跳转
     */
    public static void setSkipPath(Context context, String link, boolean isCloseActivity) {
        String subUrl = null;
        String prefix = "app://";
        String smallRoutine = "minip://";
        link = getStringFilter(link);
        TinkerBaseApplicationLike tinkerApplicationLike = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
        Map<String, String> webUrlTransform = tinkerApplicationLike.getWebUrlTransform();
        Map<String, Map<String, String>> webUrlParameterTransform = tinkerApplicationLike.getWebUrlParameterTransform();
        boolean isMiniRoutine = false;
        if (context != null) {
            if (!TextUtils.isEmpty(link)) {
                Intent intent = new Intent();
//                    app内部网址
                if (link.contains(prefix)) {
                    /**
                     * 先识别是否是打开客服 app://ManagerServiceChat
                     */
                    if (link.contains("ManagerServiceChat")) {
                        QyServiceUtils.getQyInstance().openQyServiceChat(context);
                        return;
                    } else {
                        int prefixLength = link.indexOf(prefix) + prefix.length();
                        int urlIndex = link.indexOf("?", prefixLength);
                        //判断是否有参数
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
                    }
                } else if (link.contains(smallRoutine)) {//小程序
                    int smallRoutineStart = link.indexOf(smallRoutine) + smallRoutine.length();
                    // 填应用AppId
                    IWXAPI api = WXAPIFactory.createWXAPI(context, APP_ID);
                    WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
                    req.userName = routineId; // 填小程序原始id
                    String jsonData = link.substring(smallRoutineStart).trim();
                    int versionType = 0;
                    if (!TextUtils.isEmpty(jsonData)) {
                        try {
                            JSONObject jsonObject = JSON.parseObject(jsonData);
                            if (jsonObject != null) {
                                String page = jsonObject.getString("page");
                                if (!TextUtils.isEmpty(page)) {
                                    req.path = page;
                                }
                                versionType = jsonObject.getInteger("type");
                                if (versionType > 2) {
                                    versionType = 0;
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    req.miniprogramType = versionType;// 可选打开 开发版，体验版和正式版
                    api.sendReq(req);
                    isMiniRoutine = true;
                } else if (isWebLinkUrl(link)) {
                    String webUrl = "";
//                    https://www.domolife.cn/ProductDetails.html?id=18137
                    int lastWebUrlIndex = link.lastIndexOf("/");
                    int linkLength = link.length();
                    if (lastWebUrlIndex != -1 && lastWebUrlIndex + 1 < linkLength) {
                        int parameterIndex = link.lastIndexOf("?");
                        if (parameterIndex != -1 && parameterIndex > lastWebUrlIndex) {
                            webUrl = link.substring(lastWebUrlIndex + 1, parameterIndex);
                        } else {
                            webUrl = link.substring(lastWebUrlIndex + 1);
                        }
                    }
                    Map<String, String> urlParams = getUrlParams(link);
//                    不跳转app
                    if (urlParams.get("skipApp") != null) {
                        intent.setClass(context, DoMoLifeCommunalActivity.class);
                        intent.putExtra("loadUrl", link);
                    } else if (webUrlTransform != null && webUrlTransform.get(webUrl) != null
                            && webUrlParameterTransform != null && webUrlParameterTransform.get(webUrl) != null) {
//                        原生地址有参数
//                    https://www.domolife.cn/ProductDetails.html?id=18137 ;最后转换成app://……activity?productId=18137                      获取本地地址
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
//                        原生地址无参数
                        String skipUrl = webUrlTransform.get(webUrl);
                        setSkipPath(context, skipUrl, isCloseActivity);
                        return;
                    } else {
//                        web加载url
                        intent.setClass(context, DoMoLifeCommunalActivity.class);
                        intent.putExtra("loadUrl", link);
                    }
                } else {
                    skipNonsupportEmpty(context);
                    return;
                }
                if (!isMiniRoutine) {
                    try {
                        if (isTaoBaoUrl("taobao")) {
                            skipAliBC(context, link, "");
                        } else {
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            if (intent.resolveActivity(context.getPackageManager()) != null) {
                                context.startActivity(intent);
                            } else {
                                skipNonsupportEmpty(context);
                                return;
                            }
                            if (isCloseActivity) {
                                ((Activity) context).finish();
                                ((Activity) context).overridePendingTransition(0, 0);
                            }
                        }
                    } catch (Exception e) {
                        showToast(R.string.skip_empty_page_hint);
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 方法不支持，弹窗更新版本
     */
    private static void skipNonsupportEmpty(Context context) {
        if (isContextExisted(context)) {
            AlertDialogHelper alertDialogHelper = new AlertDialogHelper(context);
            alertDialogHelper.setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                @Override
                public void confirm() {
                    /***** 检查更新 *****/
                    AppUpdateUtils.getInstance().getAppUpdate(context, true);
                }

                @Override
                public void cancel() {
                    alertDialogHelper.dismiss();
                }
            });
            alertDialogHelper.setTitle("通知提示")
                    .setTitleGravity(Gravity.CENTER)
                    .setMsg(context.getResources().getString(R.string.skip_empty_page_hint))
                    .setSingleButton(true)
                    .setConfirmText("更新");
            if (context instanceof Activity) {
                AutoSize.autoConvertDensityOfGlobal((Activity) context);
            }
            alertDialogHelper.show();
        }
    }

    /**
     * 是否网页地址
     *
     * @param androidLink
     * @return
     */
    public static boolean isWebLinkUrl(String androidLink) {
        if (TextUtils.isEmpty(androidLink)) {
            return false;
        }
        Matcher matcher = Pattern.compile(REGEX_TEXT).matcher(androidLink);
        while (matcher.find()) {
            return true;
        }
        return false;
    }

    /**
     * 获取web地址
     *
     * @param androidLink
     * @return
     */
    public static String getWebLinkUrl(String androidLink) {
        if (TextUtils.isEmpty(androidLink)) {
            return "";
        }
        Matcher matcher = Pattern.compile(REGEX_TEXT).matcher(androidLink);
        while (matcher.find()) {
            return matcher.group();
        }
        return androidLink;
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
        SharedPreferences loginStatus = context.getSharedPreferences("loginStatus", MODE_PRIVATE);
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
    public static void getLoginStatus(Activity activity) {
        if (activity == null || activity.isFinishing()) return;
        SharedPreferences loginStatus = activity.getSharedPreferences("loginStatus", MODE_PRIVATE);
        if (loginStatus.getBoolean("isLogin", false)) {
            userId = loginStatus.getInt("uid", 0);
        } else {
            //未登录跳转登录页
            Intent intent = new Intent(activity, MineLoginActivity.class);
            activity.startActivityForResult(intent, IS_LOGIN_CODE);
        }
    }


    /**
     * 获取链接中的参数
     *
     * @param url
     * @return
     */
    public static Map<String, String> getUrlParams(String url) {
        int parameterIndex = url.lastIndexOf("?");
        Map<String, String> argMap = new HashMap<>();
        if (parameterIndex != -1 && parameterIndex + 1 < url.length()) {
            String argStr = url.substring(parameterIndex + 1);
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


    public static Badge getTopBadge(Context context, View view) {
        return getBadge(context, view, 0, 0);
    }

    public static Badge getBadge(Context context, View view) {
        return getBadge(context, view, AutoSizeUtils.mm2px(mAppContext, 15), AutoSizeUtils.mm2px(mAppContext, 20));
    }

    public static Badge getBadge(Context context, View view, int offsetX, int offsetY) {
        Badge badge = new QBadgeView(context).bindTarget(view);
        badge.setBadgeGravity(Gravity.END | Gravity.TOP);
        badge.setGravityOffset(offsetX, offsetY, false);
        badge.setBadgePadding(AutoSizeUtils.mm2px(mAppContext, 3), false);
        badge.setBadgeTextSize(AutoSizeUtils.mm2px(mAppContext, 18), false);
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

    public void setSendComment(final Activity context, CommunalComment communalComment) {
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
                showToast(R.string.comment_send_failed);
                break;
        }

    }


    private void setGoodsComment(final Activity context, CommunalComment communalComment) {
        String url = Url.GOODS_COMMENT;
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
        NetLoadUtils.getNetInstance().loadNetDataPost(context, url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {

                RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        if (onSendCommentFinish != null) {
                            onSendCommentFinish.onSuccess();
                        }
                        showToast(R.string.comment_send_success);
                    } else {
                        if (onSendCommentFinish != null) {
                            onSendCommentFinish.onError();
                        }
                        showToastRequestMsg(requestStatus);
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                if (onSendCommentFinish != null) {
                    onSendCommentFinish.onError();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(R.string.comment_send_failed);
            }
        });
    }

    private void setDocComment(final Activity context, CommunalComment communalComment) {
        String url = Url.FIND_COMMENT;
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
        NetLoadUtils.getNetInstance().loadNetDataPost(context, url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {

                RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        if (onSendCommentFinish != null) {
                            onSendCommentFinish.onSuccess();
                        }
                    } else {
                        if (onSendCommentFinish != null) {
                            onSendCommentFinish.onError();
                        }
                        showToastRequestMsg(requestStatus);
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                if (onSendCommentFinish != null) {
                    onSendCommentFinish.onError();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(R.string.comment_send_failed);
            }
        });
    }

    /**
     * 发送留言
     *
     * @param context
     * @param communalComment
     */
    private void setAdviceData(final Activity context, CommunalComment communalComment) {
        String url = Url.SEARCH_LEAVE_MES;
        Map<String, Object> params = new HashMap<>();
        params.put("uid", communalComment.getUserId());
        params.put("content", getStrings(communalComment.getContent()));
        NetLoadUtils.getNetInstance().loadNetDataPost(context, url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }

                RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        if (onSendCommentFinish != null) {
                            onSendCommentFinish.onSuccess();
                        }
                        showToast(R.string.Submit_Success);
                    } else {
                        if (onSendCommentFinish != null) {
                            onSendCommentFinish.onError();
                        }
                        showToast(requestStatus.getMsg() + ",请重新提交");
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                if (onSendCommentFinish != null) {
                    onSendCommentFinish.onError();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(R.string.commit_failed);
            }
        });
    }

    /**
     * 发送意见反馈
     *
     * @param context
     * @param communalComment
     */
    private void setFeedbackData(final Activity context, CommunalComment communalComment) {
        String url = Url.MINE_FEEDBACK;
        Map<String, Object> params = new HashMap<>();
        params.put("uid", communalComment.getUserId());
        params.put("remark", getStrings(communalComment.getContent()));
        NetLoadUtils.getNetInstance().loadNetDataPost(context, url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }

                RequestStatus requestInfo = GsonUtils.fromJson(result, RequestStatus.class);
                if (requestInfo != null) {
                    if (requestInfo.getCode().equals(SUCCESS_CODE)) {
                        if (onSendCommentFinish != null) {
                            onSendCommentFinish.onSuccess();
                        }
                        showToast(R.string.Submit_Success);
                    } else {
                        if (onSendCommentFinish != null) {
                            onSendCommentFinish.onError();
                        }
                        showToastRequestMsg(requestInfo);
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                if (onSendCommentFinish != null) {
                    onSendCommentFinish.onError();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(R.string.commit_failed);
            }
        });
    }

    //获取浮动广告
    public static void getFloatAd(Activity activity, ImageView iv_float_ad_icon) {
        NetLoadUtils.getNetInstance().loadNetDataPost(activity, H_Q_FLOAT_AD, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {

                HomeQualityFloatAdEntity floatAdEntity = GsonUtils.fromJson(result, HomeQualityFloatAdEntity.class);
                if (floatAdEntity != null) {
                    if (floatAdEntity.getCode().equals(SUCCESS_CODE)) {
                        CommunalADActivityEntity.CommunalADActivityBean communalADActivityBean = floatAdEntity.getCommunalADActivityBean();
                        if (communalADActivityBean != null) {
                            iv_float_ad_icon.setVisibility(View.VISIBLE);
                            GlideImageLoaderUtil.loadFitCenter(activity, iv_float_ad_icon,
                                    getStrings(communalADActivityBean.getPicUrl()));
                            iv_float_ad_icon.setOnClickListener(v -> {
                                AddClickDao.adClickTotal(activity, communalADActivityBean.getAndroidLink(), communalADActivityBean.getId(), true);
                            });
                        }
                    } else {
                        iv_float_ad_icon.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                iv_float_ad_icon.setVisibility(View.GONE);
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
     * 放大展示图片
     *
     * @param context
     * @param imageType
     * @param firstShowPosition
     * @param imagePathList
     */
    public static void showImageActivity(Context context, String imageType, int firstShowPosition, List<String> imagePathList) {
        if (imagePathList == null || imagePathList.size() < 1) {
            showToast("图片地址错误~");
            return;
        }
        ImageBean imageBean = null;
        List<ImageBean> imageBeanList = new ArrayList<>();
        for (String picUrl : imagePathList) {
            imageBean = new ImageBean();
            imageBean.setPicUrl(picUrl);
            imageBeanList.add(imageBean);
        }
        ImagePagerActivity.startImagePagerActivity(context, imageType, imageBeanList
                , firstShowPosition, new ImagePagerActivity.ImageSize(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }


    //    分享成功 奖励
    public static void shareRewardSuccess(int uid, final Activity context) {
        String url = Url.SHARE_SUCCESS;
        Map<String, Object> params = new HashMap<>();
        params.put("uid", uid);
        params.put("version", 2);
        NetLoadUtils.getNetInstance().loadNetDataPost(context, url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                RequestStatus requestStatus = RequestStatus.objectFromData(result);
                if (requestStatus != null && requestStatus.getCode().equals(SUCCESS_CODE)
                        && !TextUtils.isEmpty(requestStatus.getSrc())) {
                    GlideImageLoaderUtil.loadFinishImgDrawable(context, requestStatus.getSrc(), new GlideImageLoaderUtil.ImageLoaderFinishListener() {
                        @Override
                        public void onSuccess(Bitmap bitmap) {
                            AlertDialogImage alertDialogAdImage = new AlertDialogImage(context);
                            alertDialogAdImage.show();
                            alertDialogAdImage.setAlertClickListener(new AlertDialogImage.AlertImageClickListener() {
                                @Override
                                public void imageClick() {
                                    alertDialogAdImage.dismiss();
                                    String androidLink = requestStatus.getAndroidLink();
                                    AddClickDao.adClickTotal(context, TextUtils.isEmpty(androidLink) ? LOTTERY_URL : androidLink, requestStatus.getId(), false);
                                }
                            });
                            alertDialogAdImage.setImage(bitmap);
                        }

                        @Override
                        public void onError() {

                        }
                    });
                }
            }
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

    public static void skipProductUrl(Context context, int proTypeId, int id, String androidLink) {
        if (!TextUtils.isEmpty(androidLink)) {
            setSkipPath(context, androidLink, false);
        } else {
            skipProductUrl(context, proTypeId, id);
        }
    }


    //跳转结算台(和订单结算接口参数一致)
    public static void skipIndentWrite(Activity context, String type, Bundle bundle) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        try {
            if (BUY_AGAIN.equals(type)) {
                params.put("orderNo", bundle.getString("orderNo"));
                } else {
                    String goodsJson = bundle.getString("goods");
                    String combineGoodsJson = bundle.getString("combineGoods");
                    String gpShopInfo = bundle.getString("gpShopInfo");


                    if (INDENT_GROUP_SHOP.equals(type) && !TextUtils.isEmpty(gpShopInfo)) {
                        GroupShopDetailsBean groupShopDetailsBean = GsonUtils.fromJson(gpShopInfo, GroupShopDetailsBean.class);
                        params.put("gpInfoId", groupShopDetailsBean.getGpInfoId());
                        params.put("gpRecordId", groupShopDetailsBean.getGpRecordId());
                    IndentProDiscountBean indentProBean = new IndentProDiscountBean();
                    indentProBean.setId(groupShopDetailsBean.getProductId());
                    indentProBean.setSaleSkuId(groupShopDetailsBean.getGpSkuId());
                    indentProBean.setCount(1);
                    List<IndentProDiscountBean> discountBeanList = new ArrayList<>();
                    discountBeanList.add(indentProBean);
                    params.put("goods", GsonUtils.toJson(discountBeanList));
                }

                if (!TextUtils.isEmpty(goodsJson)) {
                    List<CartInfoBean> passGoods = GsonUtils.fromJson(goodsJson, new TypeToken<List<CartInfoBean>>() {
                    }.getType());
                    if (passGoods != null) {
                        List<IndentProDiscountBean> discountBeanList = new ArrayList<>();
                        for (int i = 0; i < passGoods.size(); i++) {
                            CartInfoBean cartInfoBean = passGoods.get(i);
                            IndentProDiscountBean indentProBean = new IndentProDiscountBean();
                            indentProBean.setId(cartInfoBean.getProductId());
                            indentProBean.setSaleSkuId(cartInfoBean.getSaleSku().getId());
                            indentProBean.setCount(cartInfoBean.getCount());
                            indentProBean.setCartId(cartInfoBean.getId());
                            discountBeanList.add(indentProBean);
                        }
                        params.put("goods", GsonUtils.toJson(discountBeanList));
                    }
                }

                if (!TextUtils.isEmpty(combineGoodsJson)) {
                    params.put("combineGoods", combineGoodsJson);
                }
            }
        } catch (JsonSyntaxException e) {
            showToast("数据有误，请重试");
            return;
        }
        showLoadhud(context);
        NetLoadUtils.getNetInstance().loadNetDataPost(context, BUY_AGAIN.equals(type) ? CHECK_BUY_AGAIN_NEWV2 : CHECK_ORDER_SETTLE_INFOV2, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                dismissLoadhud(context);
                RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (SUCCESS_CODE.equals(requestStatus.getCode())) {
                        Intent intent = new Intent(context, DirectIndentWriteActivity.class);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    } else {
                        showToast(requestStatus.getMsg());
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                dismissLoadhud(context);
            }
        });
    }

    //跳转拼团详情
    public static void skipGroupDetail(Context context, String gpInfoId) {
        Intent intent = new Intent(context, QualityGroupShopDetailActivity.class);
        intent.putExtra("gpInfoId", gpInfoId);
        context.startActivity(intent);
    }

    //跳转售后详情
    public static void skipRefundDetail(Context context, String refundNo) {
        Intent intent = new Intent(context, DoMoRefundDetailActivity.class);
        intent.putExtra("refundNo", refundNo);
        context.startActivity(intent);
    }

    //跳转钱款去向
    public static void skipRefundAspect(Context context, int refundCount, String orderNo, String refundNo) {
        Intent intent = new Intent();
        if (refundCount > 1) {
            //跳转订单详情
            intent.setClass(context, DirectExchangeDetailsActivity.class);
            intent.putExtra("orderNo", orderNo);
        } else {
            //跳转退款去向页面
            intent.setClass(context, RefundMoneyActivity.class);
            intent.putExtra("refundNo", refundNo);
        }
        context.startActivity(intent);
    }

    //跳转用户中心
    public static void skipUserCenter(Context context, int uid) {
        if (isContextExisted(context)) {
            Intent intent = new Intent();
            intent.setClass(context, UserPagerActivity.class);
            intent.putExtra("userId", String.valueOf(uid));
            context.startActivity(intent);
        }
    }

    /**
     * 跳转帖子详情
     *
     * @param articalType //文章类型 1.后台发布的富文本帖子 2.用户发布的普通帖子
     */
    public static void skipPostDetail(Context context, String ArtId, int articalType) {
        if (isContextExisted(context)) {
            Intent intent = new Intent(context, PostDetailActivity.class);
            intent.putExtra("ArtId", ArtId);
            intent.putExtra("articletype", articalType);
            context.startActivity(intent);
        }
    }

    /**
     * 跳转话题详情
     */
    public static void skipTopicDetail(Context context, String topicId) {
        if (isContextExisted(context)) {
            Intent intent = new Intent(context, TopicDetailActivity.class);
            intent.putExtra("topicId", topicId);
            context.startActivity(intent);
        }
    }


    /**
     * 跳转参与话题
     */
    public static void skipJoinTopic(Activity activity, ScoreGoodsBean scoreGoodsBean, ScoreGoodsEntity mScoreGoodsEntity) {
        if (isContextExisted(activity) && scoreGoodsBean != null) {
            Intent intent = new Intent(activity, JoinTopicActivity.class);
            intent.putExtra("scoreGoods", scoreGoodsBean);
            if (mScoreGoodsEntity != null) {
                intent.putExtra("maxRewardTip", mScoreGoodsEntity.getMaxRewardTip());
                intent.putExtra("rewardtip", mScoreGoodsEntity.getRewardTip());
                intent.putExtra("reminder", mScoreGoodsEntity.getContentReminder());
            }
            activity.startActivity(intent);
        }
    }

    /**
     * 跳转客服
     */
    public static void skipService(Activity activity) {
        if (isContextExisted(activity)) {
            setSkipPath(activity, "app://ManagerServiceChat", false);
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
                                alertDialogHelper = new AlertDialogHelper(context);
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
                            alertDialogHelper.setTitle("权限提示")
                                    .setMsg("还缺少 " + TextUtils.join(" ", Permission.transformText(context, data))
                                            + " 重要权限，为了不影响使用，请到设置-应用管理打开权限")
                                    .setSingleButton(true)
                                    .setConfirmText("去设置");
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
                /*.rationale(new Rationale<List<String>>() { //如果拒绝了权限 弹窗提醒用户是否取消授权
                    @Override
                    public void showRationale(Context context, List<String> data, RequestExecutor executor) {
                        if (data != null) {
                            if (alertDialogRequestHelper == null) {
                                alertDialogRequestHelper = new AlertDialogHelper(context);
                                alertDialogRequestHelper.setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                                    @Override
                                    public void confirm() {
                                        // 如果用户继续：
                                        executor.execute();
                                    }

                                    @Override
                                    public void cancel() {
                                        // 如果用户中断：
                                        executor.cancel();
                                    }
                                });
                            }
                            alertDialogRequestHelper.setTitle("权限提示")
                                    .setMsg("还缺少一些必要的权限，为了不影响使用，是否继续申请权限")
                                    .setConfirmText("继续").setCancelText("取消")
                                    .setCancelTextColor(context.getResources().getColor(R.color.text_gray_hint_n));
                            alertDialogRequestHelper.show();
                        } else {
                            executor.cancel();
                        }
                    }
                })*/
                .start();
    }

    /**
     * 上传设备信息
     *
     * @param context        上下文环境
     * @param oldVersionName 手机系统版本
     * @param oldMobileModel 手机型号
     * @param oldOsVersion   后台数据APP版本
     * @param sysNotice
     */
    public static void setDeviceInfo(Activity context, String oldVersionName, String oldMobileModel, String oldOsVersion, int sysNotice) {
//        系统版本号
        String osVersion = Build.VERSION.RELEASE;
//        手机型号
        String mobileModel = Build.MODEL;
//        app版本
        String versionName = getVersionName(context);
//        获取app通知开关
        int notificationStatus = getDeviceAppNotificationStatus() ? 1 : 0;

        if (!getStrings(oldVersionName).equals(versionName)
                || !getStrings(oldMobileModel).equals(mobileModel)
                || !getStrings(oldOsVersion).equals(osVersion) || notificationStatus != sysNotice) {
            upDeviceInfo(context, osVersion, mobileModel, versionName, notificationStatus);
        }
    }

    //是否打开系统通知
    public static boolean getDeviceAppNotificationStatus() {
        NotificationManagerCompat manager = NotificationManagerCompat.from(mAppContext);
        return manager.areNotificationsEnabled();
    }

    /**
     * 更新设备信息
     *
     * @param osVersion          系统版本
     * @param mobileModel        手机型号
     * @param versionName        版本名称
     * @param notificationStatus app 通知开关
     */
    private static void upDeviceInfo(Activity context, String osVersion, String mobileModel, String versionName, int notificationStatus) {
        String url = Url.DEVICE_INFO;
        Map<String, Object> params = new HashMap<>();
        params.put("device_source", "android");
        params.put("app_version_no", versionName);
        params.put("device_sys_version", osVersion);
        params.put("device_model", mobileModel);
        params.put("sysNotice", notificationStatus);
        params.put("uid", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(context, url, params, null);
    }


    //更新消息数量
    public static void getMessageCount(Activity activity, Badge badgeMsg) {
        if (userId < 1) {
            if (badgeMsg != null) {
                badgeMsg.setBadgeNumber(0);
            }
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(activity, Url.H_MES_STATISTICS_NEW, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {

                MessageCenterEntity messageCenterEntity = GsonUtils.fromJson(result, MessageCenterEntity.class);
                if (messageCenterEntity != null) {
                    if (SUCCESS_CODE.equals(messageCenterEntity.getCode())) {
                        int totalCount = messageCenterEntity.getActivityTotal() + messageCenterEntity.getCommentTotal()
                                + messageCenterEntity.getFocusTotal() + messageCenterEntity.getLikeTotal()
                                + messageCenterEntity.getNoticeTotal() + messageCenterEntity.getOrderTotal();
                        if (badgeMsg != null) {
                            badgeMsg.setBadgeNumber(totalCount);
                        }
                    }
                }
            }
        });
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
                String regexStr = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#¥%……&*（）——+|{}【】‘；：”“’。，、？]";
                Pattern pattern = Pattern.compile(regexStr);
                Matcher matcher = pattern.matcher(source.toString());
                if (matcher.matches()) {
                    return "";
                } else {
                    return null;
                }

            }
        };
        InputFilter strSpaceFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                Pattern p = Pattern.compile(REGEX_SPACE_CHAR);
                Matcher m = p.matcher(source.toString());
                return m.replaceAll("");
            }
        };
        editText.setFilters(new InputFilter[]{strSpaceFilter, emojiFilter, specialCharFilter});
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
     * 处理短信验证码 错误code
     *
     * @param status
     */
    public static void disposeMessageCode(int status) {
        switch (status) {
            case 458:
                showToast("手机号码在黑名单中,请联系该地运营商解除短信限制");
                break;
            case 461:
            case 602:
                showToast("不支持该地区发送短信，请选择国内运营商号码");
                break;
            case 462:
                showToast("每分钟发送短信的数量超过限制，请稍后重试");
                break;
            case 463:
            case 464:
            case 465:
            case 477:
            case 478:
                showToast("手机号码每天发送次数超限，请明天再试");
                break;
            case 467:
                showToast("5分钟内校验错误超过3次，验证码失效，请重新获取验证码");
                break;
            case 468:
                showToast("验证码错误，请重新输入验证码");
                break;
            case 472:
                showToast("发送短信验证过于频繁，请稍后再试");
                break;
            case 601:
                showToast("短信发送受限");
                break;
            default:
                showToast("验证码校验失败，请退出重试");
                break;
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
    }

    private static void allowInstallApps(Context context, File appFile) {
        if (appFile != null && appFile.exists()) {
            Intent intent = new Intent();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(getFileUri(context, appFile),
                    "application/vnd.android.package-archive");
            context.startActivity(intent);
        } else {
            showToast("该文件已删除，请重新下载");
        }
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
    public static void getMarketApp(Context context, String hintText) {
        //        获取已安装应用的包名列表
        try {
            List<PackageInfo> packageInfo = context.getPackageManager().getInstalledPackages(0);
            List<String> marketPackages = MarketUtils.getMarketPackages();
            //本地安装的应用商店列表
            List<String> installList = new ArrayList<>();
            String appMarketStore = "";
            for (int i = 0; i < packageInfo.size(); i++) {
                String packageName = packageInfo.get(i).packageName;
                if (marketPackages.contains(packageName) && !installList.contains(packageName)) {
                    installList.add(packageName);
                }
            }
            //获取本地是否安装官方应用商店(优先跳转厂商官方应用商店)
            String model = DeviceUtils.getManufacturer().toLowerCase();
            switch (model) {
                case "huawei":
                    if (installList.contains("com.huawei.appmarket"))
                        appMarketStore = "com.huawei.appmarket";
                    break;
                case "meizu":
                    if (installList.contains("com.meizu.mstore"))
                        appMarketStore = "com.meizu.mstore";
                    break;
                case "xiaomi":
                    if (installList.contains("com.xiaomi.market"))
                        appMarketStore = "com.xiaomi.market";
                    break;
                case "oppo":
                    if (installList.contains("com.oppo.market"))
                        appMarketStore = "com.oppo.market";
                    break;
                case "vivo":
                    if (installList.contains("com.bbk.appstore"))
                        appMarketStore = "com.bbk.appstore";
                    break;
                case "lenovo":
                    if (installList.contains("com.lenovo.leos.appstore"))
                        appMarketStore = "com.lenovo.leos.appstore";
                    break;
                default:
                    if (installList.size() > 0)
                        appMarketStore = installList.get(0);
                    break;
            }

            if (!TextUtils.isEmpty(appMarketStore)) {
                try {
                    MarketUtils.launchAppDetail(getApplicationContext(), context.getPackageName(), appMarketStore);
                } catch (Exception e) {
                    showToast(hintText);
                }
            } else {
                showToast(hintText);
            }
        } catch (Exception e) {
            showToast(hintText);
        }
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
     */
    public static void showToast(String message) {
        ToastUtils.show(message);
    }

    public static void showToast(int resId) {
        showToast(mAppContext.getResources().getString(resId));
    }

    /**
     * 请求返回状态码提示
     *
     * @param requestStatus
     */
    public static void showToastRequestMsg(RequestStatus requestStatus) {
        showToast(requestStatus == null ? "操作失败！" :
                requestStatus.getResult() != null ? getStrings(requestStatus.getResult().getResultMsg()) : getStrings(requestStatus.getMsg()));
    }

    /**
     * 重要通知提示
     *
     * @param context
     * @param resId
     */
    public static void showImportantToast(Activity context, int resId) {
        showImportantToast(context, context.getResources().getString(resId));
    }

    public static void showImportantToast(Activity context, String hintText) {
        AlertDialogHelper alertImportDialogHelper = new AlertDialogHelper(context)
                .setSingleButton(true)
                .setConfirmText("确认")
                .setConfirmTextColor(context.getResources().getColor(R.color.text_login_gray_s))
                .setTitle("提示")
                .setTitleGravity(Gravity.CENTER)
                .setConfirmTextColor(context.getResources().getColor(R.color.text_login_blue_z))
                .setMsg(hintText)
                .setMsgTextGravity(Gravity.CENTER);
        if (!context.isFinishing()) {
            alertImportDialogHelper.show();
        }
    }


    /**
     * 正则
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\\t|\\r|\\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /**
     * 获取设备唯一码
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
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

    //根据类名获取sourceType
    public static int getSourceType(String className) {
        switch (className) {
            case "ArticleOfficialActivity"://文章
                return ConstantVariable.ARTICLE;
            case "QualityShopBuyListActivity"://必买清单
            case "QualityShopBuyListFragment"://必买清单
            case "QualityShopHistoryListActivity"://历史清单
                return ConstantVariable.MUST_BUY_TOPIC;
            case "DoMoLifeWelfareActivity"://福利社专题列表
            case "DoMoLifeWelfareFragment"://福利社专题列表
            case "DoMoLifeWelfareDetailsActivity"://福利社专题详情
            case "DoMoLifeWelfareDetailsFragment"://福利社专题详情
                return ConstantVariable.WELFARE_TOPIC;
            case "DmlOptimizedSelDetailActivity"://多么定制详情
                return ConstantVariable.SUPER_GOOD;
            case "EditorSelectActivity"://小编精选
            case "EditorSelectFragment"://小编精选
                return ConstantVariable.REDACTOR_PICKED;
            case "QualityWeekOptimizedActivity"://每周优选
            case "QualityWeekOptimizedFragment"://每周优选
                return ConstantVariable.WEEKLY_ZONE;
            case "PostDetailActivity"://帖子详情
                return ConstantVariable.POST;
            default:
                return -1;
        }
    }

    //保存sourceId到全局map
    public static void saveSourceId(String className, String id) {
        if (!MainActivity.class.getSimpleName().equals(className)) {
            ((TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike()).getSourceMap().put(className, id);
        } else {
            TinkerBaseApplicationLike tinkerBaseApplicationLike = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
            String mainCheckedFragment = getMainCheckedFragment(tinkerBaseApplicationLike);
            if (!TextUtils.isEmpty(mainCheckedFragment)) {
                ((TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike()).getSourceMap().put(mainCheckedFragment, id);
            }
        }
    }

    //根据类名获取sourceId
    public static String getSourceId(String className) {
        return (String) ((TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike()).getSourceMap().get(className);
    }

    //添加埋点来源参数
    public static void addSourceParameter(Map<String, Object> params) {
        TinkerBaseApplicationLike tinkerBaseApplicationLike = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
        String[] sourceParameter = tinkerBaseApplicationLike.getPreviousActivity();
        if (!TextUtils.isEmpty(sourceParameter[0])) {
            params.put("sourceType", sourceParameter[0]);
            params.put("sourceId", sourceParameter[1]);
        }
    }


    //如果没有找到对应的埋点Activity,再次判断是否是从首页Tab栏的Fragment进入的
    public static String getMainCheckedFragment(TinkerBaseApplicationLike tinkerBaseApplicationLike) {
        String FragmentName = "";
        LinkedList<Activity> activityLinkedList = tinkerBaseApplicationLike.getActivityLinkedList();
        for (int i = 0; i < activityLinkedList.size(); i++) {
            Activity activity = activityLinkedList.get(i);
            if (activity != null && !activity.isFinishing()) {
                if (activity instanceof MainActivity) {
                    FragmentName = ((MainActivity) activity).getCheckedFragmentName();
                    break;
                }
            }
        }

        return FragmentName;
    }

    //回收bitmap
    public static void recycleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        System.gc();
    }

    //获取帖子类型
    public static int getPostType(String title) {
        if ("精选".equals(title)) {
            return 3;
        } else if ("最新".equals(title)) {
            return 1;
        } else if ("一周最热".equals(title)) {
            return 4;
        } else if ("关注".equals(title)) {
            return 5;
        } else if ("最热".equals(title)) {
            return 2;
        } else {
            return 0;
        }
    }

    //识别并设置超链接
    public static void setTextLink(Activity activity, TextView textView) {
        //识别超链接
        Link discernUrl = new Link(Pattern.compile(REGEX_URL));
        discernUrl.setTextColor(0xff0a88fa);
        discernUrl.setUnderlined(false);
        discernUrl.setHighlightAlpha(0f);
        discernUrl.setUrlReplace(true);
        discernUrl.setOnClickListener(clickedText -> {
            if (!TextUtils.isEmpty(clickedText)) {
                setSkipPath(activity, clickedText, false);
            } else {
                showToast("网址有误，请重试");
            }
        });
        LinkBuilder.on(textView)
                .addLink(discernUrl)
                .build();
    }

    //显示loading
    public static void showLoadhud(Activity context) {
        KProgressHUD loadHud = ((BaseActivity) context).loadHud;
        if (isContextExisted(context) && loadHud != null && !loadHud.isShowing()) {
            loadHud.show();
        }
    }

    //隐藏loading
    public static void dismissLoadhud(Activity context) {
        KProgressHUD loadHud = ((BaseActivity) context).loadHud;
        if (isContextExisted(context) && loadHud != null && loadHud.isShowing()) {
            loadHud.dismiss();
        }
    }
}
