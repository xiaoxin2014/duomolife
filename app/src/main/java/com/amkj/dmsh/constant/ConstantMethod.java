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
import android.graphics.Matrix;
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
import android.support.v4.app.NotificationManagerCompat;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.amkj.dmsh.MainActivity;
import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.HomeQualityFloatAdEntity;
import com.amkj.dmsh.bean.ImageBean;
import com.amkj.dmsh.bean.MessageBean;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.dominant.activity.QualityNewUserActivity;
import com.amkj.dmsh.dominant.activity.ShopTimeScrollDetailsActivity;
import com.amkj.dmsh.find.activity.ImagePagerActivity;
import com.amkj.dmsh.homepage.activity.DoMoLifeCommunalActivity;
import com.amkj.dmsh.homepage.activity.DoMoLifeLotteryActivity;
import com.amkj.dmsh.mine.activity.MineLoginActivity;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.qyservice.QyServiceUtils;
import com.amkj.dmsh.release.bean.RelevanceProEntity.RelevanceProBean;
import com.amkj.dmsh.rxeasyhttp.utils.DeviceUtils;
import com.amkj.dmsh.shopdetails.activity.DirectMyCouponActivity;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.shopdetails.bean.EditGoodsSkuEntity;
import com.amkj.dmsh.shopdetails.bean.EditGoodsSkuEntity.EditGoodsSkuBean;
import com.amkj.dmsh.shopdetails.bean.ShopCarGoodsSku;
import com.amkj.dmsh.shopdetails.bean.SkuSaleBean;
import com.amkj.dmsh.shopdetails.integration.IntegralScrollDetailsActivity;
import com.amkj.dmsh.utils.MarketUtils;
import com.amkj.dmsh.utils.SharedPreUtils;
import com.amkj.dmsh.utils.alertdialog.AlertDialogHelper;
import com.amkj.dmsh.utils.alertdialog.AlertDialogImage;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.views.bottomdialog.SkuDialog;
import com.google.gson.Gson;
import com.hjq.toast.ToastUtils;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;
import com.tencent.bugly.beta.tinker.TinkerManager;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.stat.StatConfig;
import com.umeng.analytics.MobclickAgent;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.Setting;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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

import me.jessyan.autosize.AutoSize;
import me.jessyan.autosize.utils.AutoSizeUtils;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

import static android.content.Context.MODE_PRIVATE;
import static android.os.Build.VERSION_CODES.KITKAT;
import static com.ali.auth.third.core.context.KernelContext.getApplicationContext;
import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.base.WeChatPayConstants.APP_ID;
import static com.amkj.dmsh.constant.ConstantVariable.COMMENT_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.MES_ADVISE;
import static com.amkj.dmsh.constant.ConstantVariable.MES_FEEDBACK;
import static com.amkj.dmsh.constant.ConstantVariable.PRO_COMMENT;
import static com.amkj.dmsh.constant.ConstantVariable.PRO_TOPIC;
import static com.amkj.dmsh.constant.ConstantVariable.REGEX_SPACE_CHAR;
import static com.amkj.dmsh.constant.ConstantVariable.REGEX_TEXT;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TAOBAO_APPKEY;
import static com.amkj.dmsh.constant.ConstantVariable.TOKEN;
import static com.amkj.dmsh.constant.ConstantVariable.TOKEN_EXPIRE_TIME;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_NAME_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_C_WELFARE;
import static com.amkj.dmsh.constant.TagAliasOperatorHelper.ACTION_CLEAN;
import static com.amkj.dmsh.constant.TagAliasOperatorHelper.ACTION_DELETE;
import static com.amkj.dmsh.constant.TagAliasOperatorHelper.ACTION_SET;
import static com.amkj.dmsh.constant.TagAliasOperatorHelper.TagAliasBean;
import static com.amkj.dmsh.constant.TagAliasOperatorHelper.sequence;
import static com.amkj.dmsh.constant.UMShareAction.routineId;
import static com.amkj.dmsh.constant.Url.H_Q_FLOAT_AD;
import static com.amkj.dmsh.constant.Url.Q_QUERY_CAR_COUNT;
import static com.amkj.dmsh.constant.Url.TOTAL_AD_COUNT;
import static com.amkj.dmsh.constant.Url.TOTAL_AD_DIALOG_COUNT;
import static com.yanzhenjie.permission.AndPermission.getFileUri;

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
     *
     * @param text
     * @return
     */
    public static int getStringChangeIntegers(String text) {
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
     * String转换成int(先去掉中文和货币符号)
     *
     * @param text
     * @return
     */
    public static double getStringChangeDouble(String text) {
        if (TextUtils.isEmpty(text)) {
            return 0;
        } else {
            try {
                Matcher m = Pattern.compile("[\\u4e00-\\u9fa5]").matcher(text);
                return Double.parseDouble(m.replaceAll("").replaceAll("¥","").trim());
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return 0;
            }
        }
    }

    /**
     * String转换成boolean
     *
     * @param text
     * @return
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
     *
     * @return
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
                price = append ? "¥" + stripTrailingZeros(price) : stripTrailingZeros(price);
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
            return new BigDecimal(priceText.trim()).stripTrailingZeros().toPlainString();
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

    public static String getIntegralFormat(Context context, int resStringId, @NonNull int number) {
        if (context == null) {
            return "";
        }
        if (resStringId <= 0) {
            return "";
        }
        return String.format(context.getResources().getString(resStringId), number);
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
                        if (link.contains("taobao")) {
                            skipAliBCWebView(link, context);
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
                        showToast(context, R.string.skip_empty_page_hint);
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
     * 调用登出接口,清除后台记录的token信息
     *
     * @param isHandOperation 是否是手动退出登录
     */
    public static void logout(Activity activity, boolean isHandOperation) {
        if (isHandOperation) {
            ((BaseActivity) activity).loadHud.show();
        }
        NetLoadUtils.token = (String) SharedPreUtils.getParam(TOKEN, "");
        NetLoadUtils.uid = String.valueOf(SharedPreUtils.getParam("uid", 0));
        //Token过期,清除本地登录信息
        savePersonalInfoCache(activity, null);
        NetLoadUtils.getNetInstance().loadNetDataPost(activity, Url.LOG_OUT, null, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                if (isHandOperation) {
                    ((BaseActivity) activity).loadHud.dismiss();
                    activity.finish();
                }
            }

            @Override
            public void onNotNetOrException() {
                if (isHandOperation) {
                    ((BaseActivity) activity).loadHud.dismiss();
                    activity.finish();
                }
            }
        });
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
            }

            @Override
            public void onFailure(int code, String msg) {
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

    private static void skipMainActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
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
                showToast(context, R.string.comment_send_failed);
                break;
        }

    }


    private void setGoodsComment(final Activity context, CommunalComment communalComment) {
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
        NetLoadUtils.getNetInstance().loadNetDataPost(context, url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        if (onSendCommentFinish != null) {
                            onSendCommentFinish.onSuccess();
                        }
                        showToast(context.getApplicationContext(), R.string.comment_send_success);
                    } else {
                        if (onSendCommentFinish != null) {
                            onSendCommentFinish.onError();
                        }
                        showToastRequestMsg(context.getApplicationContext(), requestStatus);
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
                showToast(context.getApplicationContext(), R.string.comment_send_failed);
            }

            @Override
            public void netClose() {
                showToast(context.getApplicationContext(), R.string.unConnectedNetwork);
            }
        });
    }

    private void setDocComment(final Activity context, CommunalComment communalComment) {
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
        NetLoadUtils.getNetInstance().loadNetDataPost(context, url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        if (onSendCommentFinish != null) {
                            onSendCommentFinish.onSuccess();
                        }
                    } else {
                        if (onSendCommentFinish != null) {
                            onSendCommentFinish.onError();
                        }
                        showToastRequestMsg(context.getApplicationContext(), requestStatus);
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
                showToast(context.getApplicationContext(), R.string.comment_send_failed);
            }

            @Override
            public void netClose() {
                showToast(context.getApplicationContext(), R.string.unConnectedNetwork);
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
        String url = Url.BASE_URL + Url.SEARCH_LEAVE_MES;
        Map<String, Object> params = new HashMap<>();
        params.put("uid", communalComment.getUserId());
        params.put("content", getStrings(communalComment.getContent()));
        NetLoadUtils.getNetInstance().loadNetDataPost(context, url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
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
                showToast(context, R.string.commit_failed);
            }

            @Override
            public void netClose() {
                showToast(context, R.string.unConnectedNetwork);
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
        String url = Url.BASE_URL + Url.MINE_FEEDBACK;
        Map<String, Object> params = new HashMap<>();
        params.put("uid", communalComment.getUserId());
        params.put("remark", getStrings(communalComment.getContent()));
        NetLoadUtils.getNetInstance().loadNetDataPost(context, url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                Gson gson = new Gson();
                RequestStatus requestInfo = gson.fromJson(result, RequestStatus.class);
                if (requestInfo != null) {
                    if (requestInfo.getCode().equals(SUCCESS_CODE)) {
                        if (onSendCommentFinish != null) {
                            onSendCommentFinish.onSuccess();
                        }
                        showToast(context, R.string.Submit_Success);
                    } else {
                        if (onSendCommentFinish != null) {
                            onSendCommentFinish.onError();
                        }
                        showToastRequestMsg(context, requestInfo);
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
                showToast(context, R.string.commit_failed);
            }

            @Override
            public void netClose() {
                showToast(context, R.string.unConnectedNetwork);
            }
        });
    }

    //获取浮动广告
    public static void getFloatAd(Activity activity, ImageView iv_float_ad_icon) {
        NetLoadUtils.getNetInstance().loadNetDataPost(activity, H_Q_FLOAT_AD, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                HomeQualityFloatAdEntity floatAdEntity = gson.fromJson(result, HomeQualityFloatAdEntity.class);
                if (floatAdEntity != null) {
                    if (floatAdEntity.getCode().equals(SUCCESS_CODE)) {
                        if (floatAdEntity.getCommunalADActivityBean() != null) {
                            iv_float_ad_icon.setVisibility(View.VISIBLE);
                            GlideImageLoaderUtil.loadFitCenter(activity, iv_float_ad_icon,
                                    getStrings(floatAdEntity.getCommunalADActivityBean().getPicUrl()));
                            iv_float_ad_icon.setTag(R.id.iv_tag, floatAdEntity.getCommunalADActivityBean());
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
            showToast(context, "图片地址错误~");
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
    public static void addArticleShareCount(Activity activity, int articleId) {
        String url = Url.BASE_URL + Url.ARTICLE_SHARE_COUNT;
        Map<String, Object> params = new HashMap<>();
        //回复文章或帖子
        params.put("id", articleId);
        NetLoadUtils.getNetInstance().loadNetDataPost(activity, url, params, null);
    }

    /**
     * 推送点击统计
     */
    public static void totalPushMessage(Context activity, @NonNull String pushId) {
        String url = Url.BASE_URL + Url.TOTAL_PUSH_INFO;
        Map<String, Object> params = new HashMap<>();
        //回复文章或帖子
        params.put("pushId", pushId);
        if (userId > 0) {
            params.put("uid", userId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(activity, url, params, null);
    }

    //    分享成功 奖励
    public static void shareRewardSuccess(int uid, final Activity context) {
        String url = Url.BASE_URL + Url.SHARE_SUCCESS;
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
                                    Intent intent = new Intent(context, DoMoLifeLotteryActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(intent);
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

    //      统计文章点击商品
    public static void totalProNum(Activity activity, int productId, int artId) {
        String url = Url.BASE_URL + Url.TOTAL_PRO_NUM;
        Map<String, Object> params = new HashMap<>();
        //回复文章或帖子
        params.put("product_id", productId);
        params.put("doc_id", artId);
        NetLoadUtils.getNetInstance().loadNetDataPost(activity, url, params, null);
    }

    //      统计福利社点击商品
    public static void totalWelfareProNum(Activity activity, int productId, int topId) {
        String url = Url.BASE_URL + Url.TOTAL_WELFARE_PRO_NUM;
        Map<String, Object> params = new HashMap<>();
        //回复文章或帖子
        params.put("productId", productId);
        params.put("topId", topId);
        NetLoadUtils.getNetInstance().loadNetDataPost(activity, url, params, null);
    }

    //      统计官方通知点击商品
    public static void totalOfficialProNum(Activity activity, int productId, String officialId) {
        String url = Url.BASE_URL + Url.TOTAL_OFFICIAL_PRO_NUM;
        Map<String, Object> params = new HashMap<>();
        //回复文章或帖子
        params.put("productId", productId);
        params.put("cId", officialId);
        NetLoadUtils.getNetInstance().loadNetDataPost(activity, url, params, null);
    }

    //    统计广告点击

    /**
     * 3.1.9 adId （objectId 改为 id）
     *
     * @param adId
     */
    public static void adClickTotal(Activity activity, int adId) {
        Map<String, Object> params = new HashMap<>();
        //回复文章或帖子
        params.put("id", adId);
        NetLoadUtils.getNetInstance().loadNetDataPost(activity, TOTAL_AD_COUNT, params, null);
    }

    /**
     * 启动弹窗统计
     *
     * @param adId
     */
    public static void adDialogClickTotal(Activity activity, int adId) {
        Map<String, Object> params = new HashMap<>();
        //回复文章或帖子
        params.put("id", adId);
        NetLoadUtils.getNetInstance().loadNetDataPost(activity, TOTAL_AD_DIALOG_COUNT, params, null);
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
        NetLoadUtils.getNetInstance().loadNetDataPost(mAppContext, url, params, null);
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
        int notificationStatus = getDeviceAppNotificationStatus(context) ? 1 : 0;

        if (!getStrings(oldVersionName).equals(versionName)
                || !getStrings(oldMobileModel).equals(mobileModel)
                || !getStrings(oldOsVersion).equals(osVersion) || notificationStatus != sysNotice) {
            upDeviceInfo(context, osVersion, mobileModel, versionName, notificationStatus);
        }
    }

    public static boolean getDeviceAppNotificationStatus(Context context) {
        NotificationManagerCompat manager = NotificationManagerCompat.from(context.getApplicationContext());
        return manager.areNotificationsEnabled();
    }

    /**
     * 上传设备信息
     *
     * @param osVersion          系统版本
     * @param mobileModel        手机型号
     * @param versionName        版本名称
     * @param notificationStatus app 通知开关
     */
    private static void upDeviceInfo(Activity context, String osVersion, String mobileModel, String versionName, int notificationStatus) {
        String url = Url.BASE_URL + Url.DEVICE_INFO;
        Map<String, Object> params = new HashMap<>();
        params.put("device_source", "android");
        params.put("app_version_no", versionName);
        params.put("device_sys_version", osVersion);
        params.put("device_model", mobileModel);
        params.put("sysNotice", notificationStatus);
        params.put("uid", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(context, url, params, null);
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

    /**
     * @param context
     * @param baseAddCarProInfoBean 商品基本信息
     * @param loadHud
     */
    public void addShopCarGetSku(final Activity context, final BaseAddCarProInfoBean baseAddCarProInfoBean, final KProgressHUD loadHud) {
        //商品详情内容
        this.loadHud = loadHud;
        this.context = context;
        String url = Url.BASE_URL + Url.Q_SHOP_DETAILS_GET_SKU_CAR;
        Map<String, Object> params = new HashMap<>();
        params.put("productId", baseAddCarProInfoBean.getProductId());
        params.put("uid", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(context, url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                Gson gson = new Gson();
                EditGoodsSkuEntity editGoodsSkuEntity = gson.fromJson(result, EditGoodsSkuEntity.class);
                if (editGoodsSkuEntity != null) {
                    if (editGoodsSkuEntity.getCode().equals(SUCCESS_CODE)) {
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
                                    addShopCar(context, shopCarGoodsSkuDif);
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
            public void onNotNetOrException() {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(context, R.string.do_failed);
            }

            @Override
            public void netClose() {
                showToast(context, R.string.unConnectedNetwork);
            }
        });
    }

    private void setSkuValue(Activity context, final EditGoodsSkuEntity.EditGoodsSkuBean editGoodsSkuBean, final BaseAddCarProInfoBean baseAddCarProInfoBean) {
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
                addShopCar(context, shopCarGoodsSku);
            }
        });
    }

    //加入购物车
    private void addShopCar(Activity activity, final ShopCarGoodsSku shopCarGoodsSku) {
        if (userId != 0) {
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
            addSourceParameter(params);

            NetLoadUtils.getNetInstance().loadNetDataPost(activity, url, params, new NetLoadListenerHelper() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    if (loadHud != null) {
                        loadHud.dismiss();
                    }
                    RequestStatus status = gson.fromJson(result, RequestStatus.class);
                    if (status != null) {
                        if (status.getCode().equals(SUCCESS_CODE)) {
                            showToast(context, context.getString(R.string.AddCarSuccess));
                            TotalPersonalTrajectory totalPersonalTrajectory = new TotalPersonalTrajectory(context);
                            Map<String, String> totalMap = new HashMap<>();
                            totalMap.put("productId", String.valueOf(shopCarGoodsSku.getProductId()));
                            totalMap.put(TOTAL_NAME_TYPE, "addCartSuccess");
                            totalPersonalTrajectory.saveTotalDataToFile(totalMap);
                            //通知刷新购物车数量
                            EventBus.getDefault().post(new EventMessage(ConstantVariable.UPDATE_CAR_NUM, ""));
                        } else {
                            showToastRequestMsg(context, status);
                        }
                    }
                }

                @Override
                public void onNotNetOrException() {
                    if (loadHud != null) {
                        loadHud.dismiss();
                    }
                }

                @Override
                public void netClose() {
                    showToast(context, R.string.unConnectedNetwork);
                }

                @Override
                public void onError(Throwable ex) {
                    showToast(context, R.string.do_failed);
                }
            });
        }
    }

    //更新购物车商品数量
    public static void getCarCount(Activity activity, Badge badge) {
        if (userId > 0) {
            //购物车数量展示
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            NetLoadUtils.getNetInstance().loadNetDataPost(activity, Q_QUERY_CAR_COUNT, params, new NetLoadListenerHelper() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    MessageBean requestStatus = gson.fromJson(result, MessageBean.class);
                    if (requestStatus != null) {
                        if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                            int cartNumber = requestStatus.getResult();
                            badge.setBadgeNumber(cartNumber);
                        }
                    }
                }
            });
        } else {
            if (badge != null) badge.setBadgeNumber(0);
        }
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
     * 保存个人信息
     *
     * @param context
     * @param savePersonalInfo
     */
    public static void savePersonalInfoCache(Context context, SavePersonalInfoBean savePersonalInfo) {
        Context applicationContext = context.getApplicationContext();
        if (savePersonalInfo != null && savePersonalInfo.isLogin()) {
            userId = savePersonalInfo.getUid();
//            登录成功 三方账号登录
            StatConfig.setCustomUserId(applicationContext, String.valueOf(savePersonalInfo.getUid()));
            //        友盟统计
            MobclickAgent.onProfileSignIn(String.valueOf(savePersonalInfo.getUid()));
            //        绑定JPush
            bindJPush(savePersonalInfo.getUid());
            QyServiceUtils.getQyInstance().loginQyUserInfo(applicationContext, savePersonalInfo.getUid()
                    , savePersonalInfo.getNickName(), savePersonalInfo.getPhoneNum(), savePersonalInfo.getAvatar());

            SharedPreferences loginStatus = applicationContext.getSharedPreferences("loginStatus", MODE_PRIVATE);
            SharedPreferences.Editor edit = loginStatus.edit();
            edit.putBoolean("isLogin", true);
            edit.putInt("uid", savePersonalInfo.getUid());
            edit.putString("nickName", getStrings(savePersonalInfo.getNickName()));
            edit.putString("avatar", getStrings(savePersonalInfo.getAvatar()));
            edit.putString("P_NUM", getStrings(savePersonalInfo.getPhoneNum()));
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
            edit.commit();
        } else {
            userId = 0;
//            七鱼注销
            QyServiceUtils.getQyInstance().logoutQyUser(applicationContext);
            //            注销账号 关闭账号统计
            MobclickAgent.onProfileSignOff();
//            解绑JPush
            unBindJPush();
            //清除登录状态
            SharedPreUtils.clearAll();
        }
    }

    /**
     * 获取个人信息
     *
     * @param context
     * @return
     */
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

    public void getNewUserCouponDialog(Activity context) {
        if (NEW_USER_DIALOG && isContextExisted(context)) {
            NEW_USER_DIALOG = false;
            String url = Url.BASE_URL + Url.H_NEW_USER_COUPON;
            Map<String, Object> params = new HashMap<>();
            params.put("user_id", userId);
            NetLoadUtils.getNetInstance().loadNetDataPost(context, url, params, new NetLoadListenerHelper() {
                @Override
                public void onSuccess(String result) {
                    RequestStatus requestStatus = RequestStatus.objectFromData(result);
                    if (requestStatus != null) {
                        if (requestStatus.getCode().equals(SUCCESS_CODE)
                                && !TextUtils.isEmpty(requestStatus.getImgUrl())
                                && 0 < requestStatus.getUserType() && requestStatus.getUserType() < 4) {
                            //                                    弹窗
                            if (isContextExisted(context)) {
                                GlideImageLoaderUtil.loadFinishImgDrawable(context, requestStatus.getImgUrl(), new GlideImageLoaderUtil.ImageLoaderFinishListener() {
                                    @Override
                                    public void onSuccess(Bitmap bitmap) {
                                        AlertDialogImage alertDialogImage = new AlertDialogImage(context);
                                        alertDialogImage.show();
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
                                                alertDialogImage.dismiss();
                                            }
                                        });
                                        alertDialogImage.setImage(bitmap);
                                    }

                                    @Override
                                    public void onError() {

                                    }
                                });
                            }
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
    private void getNewUserCoupon(Activity context, int couponId) {
        if (couponId > 0) {
            String url = Url.BASE_URL + Url.FIND_ARTICLE_COUPON;
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            params.put("couponId", couponId);
            NetLoadUtils.getNetInstance().loadNetDataPost(context, url, params, new NetLoadListenerHelper() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                    if (requestStatus != null) {
                        if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                            showToastRequestMsg(context, requestStatus);
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
            showToast(context, "该文件已删除，请重新下载");
        }
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
     * 获取毫秒 空值为当前默认时间
     *
     * @param time
     * @return
     */
    public static long getDateMilliSecondSystemTime(String time) {
        if (!TextUtils.isEmpty(time)) {
            try {
                SimpleDateFormat timeReceiveFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                Date date = timeReceiveFormat.parse(time);
                return date.getTime();
            } catch (Exception e) {
                e.printStackTrace();
                return new Date().getTime();
            }
        } else {
            return new Date().getTime();
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
        return isSameTimeDay("yyyy-MM-dd HH:mm:ss", t1, t2);
    }

    /**
     * @param formatType 数据格式类型
     * @param t1
     * @param t2
     * @return
     */
    public static boolean isSameTimeDay(String formatType, String t1, String t2) {
        if (!TextUtils.isEmpty(t1) && !TextUtils.isEmpty(t2)) {
            try {
                SimpleDateFormat timeFormat = new SimpleDateFormat(TextUtils.isEmpty(formatType) ? "yyyy-MM-dd HH:mm:ss" : formatType, Locale.CHINA);
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
     * @param t1          起始时间
     * @param t2          当前时间
     * @param intervalDay 间隔天数
     * @return
     */
    public static boolean isTimeDayEligibility(String t1, String t2, int intervalDay) {
        if (!TextUtils.isEmpty(t1) && !TextUtils.isEmpty(t2)) {
            try {
                SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
                Date d1 = timeFormat.parse(t1);
                long milliseconds = d1.getTime() + intervalDay * 24 * 60 * 60 * 1000;
                d1.setTime(milliseconds);
                Date d2 = timeFormat.parse(t2);
                return d1.getTime() <= d2.getTime();
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
                    showToast(context, hintText);
                }
            } else {
                showToast(context, hintText);
            }
        } catch (Exception e) {
            showToast(context, hintText);
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
        if (isContextExisted(context)) {
            Context applicationContext = context.getApplicationContext();
            if (ToastUtils.getToast() == null ||
                    (!NotificationManagerCompat.from(applicationContext).areNotificationsEnabled() &&
                            !"SupportToast".equals(ToastUtils.getToast().getClass().getSimpleName()))) {
                // 因为吐司只有初始化的时候才会判断通知权限有没有开启，根据这个通知开关来显示原生的吐司还是兼容的吐司
                ToastUtils.init(TinkerManager.getApplication());
            }
            ToastUtils.show(message);
        }
    }

    public static void showToast(String message) {
        ToastUtils.show(message);
    }

    /**
     * 资源文件文本
     *
     * @param context
     * @param resId
     */
    public static void showToast(Context context, int resId) {
        if (context == null || !isContextExisted(context)) {
            return;
        }
        Context applicationContext = context.getApplicationContext();
        if (context.getResources() == null) {
            showToast(applicationContext, "数据异常，请稍后再试");
        } else {
            showToast(applicationContext, applicationContext.getResources().getString(resId));
        }
    }

    /**
     * 请求返回状态码提示
     *
     * @param context
     * @param requestStatus
     */
    public static void showToastRequestMsg(Context context, RequestStatus requestStatus) {
        showToast(context, requestStatus == null ? "操作失败！" :
                requestStatus.getResult() != null ? getStrings(requestStatus.getResult().getMsg()) : getStrings(requestStatus.getMsg()));
    }

    /**
     * 重要通知提示
     *
     * @param context
     * @param resId
     */
    public void showImportantToast(Activity context, int resId) {
        showImportantToast(context, context.getResources().getString(resId));
    }

    public void showImportantToast(Activity context, String hintText) {
        int notificationEnabledValue = 1;
        if (Build.VERSION.SDK_INT >= KITKAT) {
            notificationEnabledValue = NotificationManagerCompat.from(context).areNotificationsEnabled() ? 1 : 0;
        }
        // 允许通知权限则尽量用系统toast
        // 没有通知权限或者是可点击的toast则使用自定义toast
        if (notificationEnabledValue > 0) {
            showToast(context, hintText);
        } else {
            if (alertImportDialogHelper == null) {
                alertImportDialogHelper = new AlertDialogHelper(context)
                        .setSingleButton(true)
                        .setConfirmText("确认")
                        .setConfirmTextColor(context.getResources().getColor(R.color.text_login_gray_s))
                        .setTitle("提示")
                        .setTitleGravity(Gravity.CENTER)
                        .setConfirmTextColor(context.getResources().getColor(R.color.text_login_blue_z));
            }
            alertImportDialogHelper.setMsg(hintText);
            if (!context.isFinishing()) {
                alertImportDialogHelper.show();
            }
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


    //根据类名获取sourceType
    public static int getSourceType(String className) {
        switch (className) {
            case "ArticleOfficialActivity"://文章
                return 1;
            case "QualityShopBuyListActivity"://必买清单
            case "QualityShopBuyListFragment"://必买清单
            case "QualityShopHistoryListActivity"://历史清单
                return 2;
            case "DoMoLifeWelfareActivity"://福利社专题列表
            case "DoMoLifeWelfareFragment"://福利社专题列表
            case "DoMoLifeWelfareDetailsActivity"://福利社专题详情
            case "DoMoLifeWelfareDetailsFragment"://福利社专题详情
            case "HomeDefalutFragment"://首页良品商城福利社专区
                return 3;
            case "DmlOptimizedSelDetailActivity"://多么定制详情
                return 4;
            case "EditorSelectActivity"://小编精选
            case "EditorSelectFragment"://小编精选
                return 5;
            case "QualityWeekOptimizedActivity"://每周优选
            case "QualityWeekOptimizedFragment"://每周优选
                return 6;
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
    private static String getSourceId(String className) {
        return (String) ((TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike()).getSourceMap().get(className);
    }

    //添加埋点来源参数
    public static void addSourceParameter(Map<String, Object> params) {
        //添加埋点来源参数
        TinkerBaseApplicationLike tinkerBaseApplicationLike = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
        String simpleName = tinkerBaseApplicationLike.getPreviousActivity();
        int sourceType = getSourceType(simpleName);
        String sourceId = getSourceId(simpleName);
        //如果没有找到对应的埋点Activity,再次判断是否是从首页Tab栏的Fragment进入的
        if (sourceType == -1) {
            String mainCheckedFragment = getMainCheckedFragment(tinkerBaseApplicationLike);
            //如果是，重新获取SourceType和SourceId
            if (!TextUtils.isEmpty(mainCheckedFragment)) {
                sourceType = getSourceType(mainCheckedFragment);
                sourceId = getSourceId(mainCheckedFragment);
            }
        }

        if (sourceType != -1 && !TextUtils.isEmpty(sourceId)) {
            params.put("sourceType", sourceType);
            params.put("sourceId", sourceId);
        }
    }


    //如果没有找到对应的埋点Activity,再次判断是否是从首页Tab栏的Fragment进入的
    private static String getMainCheckedFragment(TinkerBaseApplicationLike tinkerBaseApplicationLike) {
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
            bitmap = null;
        }
        System.gc();
    }
}
