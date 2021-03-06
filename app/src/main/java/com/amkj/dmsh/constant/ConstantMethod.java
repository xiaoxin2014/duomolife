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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.amkj.dmsh.shopdetails.activity.DirectExchangeDetailsActivity;
import com.amkj.dmsh.shopdetails.activity.DirectIndentWriteActivity;
import com.amkj.dmsh.shopdetails.activity.DoMoRefundDetailActivity;
import com.amkj.dmsh.shopdetails.activity.QuestionDetailActivity;
import com.amkj.dmsh.shopdetails.activity.RefundMoneyActivity;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.shopdetails.bean.IndentProDiscountBean;
import com.amkj.dmsh.shopdetails.integration.IntegralScrollDetailsActivity;
import com.amkj.dmsh.time.activity.ShopTimeScrollDetailsActivity;
import com.amkj.dmsh.time.activity.TimePostDetailActivity;
import com.amkj.dmsh.user.activity.UserPagerActivity;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.views.alertdialog.AlertDialogHelper;
import com.amkj.dmsh.views.alertdialog.AlertDialogImage;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import me.jessyan.autosize.AutoSize;
import me.jessyan.autosize.utils.AutoSizeUtils;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

import static android.content.Context.MODE_PRIVATE;
import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.base.WeChatPayConstants.APP_ID;
import static com.amkj.dmsh.constant.ConstantVariable.BUY_AGAIN;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_GROUP_SHOP;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_W_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.REGEX_SPACE_CHAR;
import static com.amkj.dmsh.constant.ConstantVariable.REGEX_TEXT;
import static com.amkj.dmsh.constant.ConstantVariable.REGEX_URL;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
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

    private OnGetPermissionsSuccessListener onGetPermissionsSuccessListener;
    public static int userId = 0;
    private static boolean isVip = false;
    private AlertDialogHelper alertDialogHelper;

    public static boolean isVip() {
        return userId > 0 && isVip;
//        return false;
    }

    public static void setIsVip(boolean isVip) {
        ConstantMethod.isVip = isVip;
    }


    //    ????????????????????????
    public static String getStrings(String text) {
        return TextUtils.isEmpty(text) ? "" : toDBC(text);
    }

    /**
     * ??????????????????????????????
     *
     * @param text
     * @return
     */
    public static boolean isEmptyStrings(String text) {
        return TextUtils.isEmpty(text) || text.equals("null");
    }

    /**
     * ?????????????????? ??????????????????\r\n??????
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
     * ?????????????????????
     *
     * @param object       ???????????????????????????
     * @param defaultValue ??????????????????
     * @return
     */
    public static Object getMapValue(Object object, Object defaultValue) {
        if (object == null) {
            return defaultValue;
        }
        return object;
    }

    /**
     * ???????????????String????????????
     *
     * @param object ?????????????????????bean?????? ?????????????????????????????????value???Object
     * @return
     */
    public static String getStringMapValue(Object object) {
        if (object == null) {
            return "";
        }
        return object + "";
    }

    /**
     * String?????????int
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
     * String?????????double(??????????????????????????????)
     */
    public static double getStringChangeDouble(String text) {
        if (TextUtils.isEmpty(text)) {
            return 0;
        } else {
            try {
                Matcher m = Pattern.compile("[\\u4e00-\\u9fa5]").matcher(text);
                return Double.parseDouble(m.replaceAll("").replaceAll("??", "").trim());
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return 0;
            }
        }
    }

    /**
     * String?????????boolean
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
     * String?????????boolean
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
     * string???float
     *
     * @param text
     * @return
     */
    public static float getStringChangeFloat(String text) {
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
     * ????????????????????????
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


    public static CharSequence getRmbFormat(Context context, String priceText) {
        return getRmbFormat(context, priceText, true);
    }


    public static CharSequence getRmbFormat(Context context, String priceText, boolean append) {
        return getRmbFormat(context, priceText, append, "#ff5a6b");

    }

    /**
     * ??????????????????????????????
     *
     * @param append ????????????????????????
     * @param color  ?????????????????
     * @return
     */
    public static CharSequence getRmbFormat(Context context, String priceText, boolean append, String color) {
        try {
            String price = getStrings(priceText);
            if (!TextUtils.isEmpty(price)) {
                price = append ? "??" + price : price;
                Pattern pattern = Pattern.compile("[??]");
                Link link = new Link(pattern);
                link.setTextColor(Color.parseColor(color));
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
            return getStrings(append ? "??" + priceText : priceText);
        }
    }


    /**
     * ??????????????????????????????
     *
     * @param proportion ????????????????????????
     * @return
     */
    public static CharSequence getSpannableString(String text, int start, int end, float proportion, String color) {
        return getSpannableString(text, start, end, proportion, color, false);
    }


    /**
     * @param bold ????????????
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
     * ???????????????0
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
     * ??????????????????
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
     * @param context         ?????????
     * @param link            ????????????
     * @param isCloseActivity ??????????????????????????????
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
//                    app????????????
                if (link.contains(prefix)) {
                    /**
                     * ?????????????????????????????? app://ManagerServiceChat
                     */
                    if (link.contains("ManagerServiceChat")) {
                        QyServiceUtils.getQyInstance().openQyServiceChat(context);
                        return;
                    } else {
                        int prefixLength = link.indexOf(prefix) + prefix.length();
                        int urlIndex = link.indexOf("?", prefixLength);
                        //?????????????????????
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
                } else if (link.contains(smallRoutine)) {//?????????
                    // ?????????AppId
                    IWXAPI api = WXAPIFactory.createWXAPI(context, APP_ID);
                    WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
                    req.userName = routineId; // ??????????????????id
                    int versionType = 0;
                    try {
                        String jsonData = link.substring(link.indexOf("{"), link.lastIndexOf("}") + 1);//??????json???
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
                    req.miniprogramType = versionType;// ???????????? ?????????????????????????????????
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
//                    ?????????app
                    if (urlParams.get("skipApp") != null) {
                        intent.setClass(context, DoMoLifeCommunalActivity.class);
                        intent.putExtra("loadUrl", link);
                    } else if (webUrlTransform != null && webUrlTransform.get(webUrl) != null
                            && webUrlParameterTransform != null && webUrlParameterTransform.get(webUrl) != null) {
//                        ?????????????????????
//                    https://www.domolife.cn/ProductDetails.html?id=18137 ;???????????????app://??????activity?productId=18137                      ??????????????????
                        String skipUrl = webUrlTransform.get(webUrl);
//                        ????????????
                        Map<String, String> parameterMap = webUrlParameterTransform.get(webUrl);
                        int i = 0;
                        String parameter = "";
                        for (Map.Entry<String, String> entry : urlParams.entrySet()) {
//                            ????????????????????????
                            if (i > 0) {
                                parameter += "&";
                            }
                            parameter += parameterMap.get(entry.getKey()) + "=" + entry.getValue();
                            i++;
                        }
                        setSkipPath(context, (skipUrl + "?" + parameter), isCloseActivity);
                        return;
                    } else if (webUrlTransform != null && webUrlTransform.get(webUrl) != null) {
//                        ?????????????????????
                        String skipUrl = webUrlTransform.get(webUrl);
                        setSkipPath(context, skipUrl, isCloseActivity);
                        return;
                    } else {
//                        web??????url
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
     * ????????????????????????????????????
     */
    private static void skipNonsupportEmpty(Context context) {
        if (isContextExisted(context)) {
            AlertDialogHelper alertDialogHelper = new AlertDialogHelper(context);
            alertDialogHelper.setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                @Override
                public void confirm() {
                    /***** ???????????? *****/
                    AppUpdateUtils.getInstance().getAppUpdate(context, true);
                }

                @Override
                public void cancel() {
                }
            });
            alertDialogHelper.setTitle("????????????")
                    .setTitleGravity(Gravity.CENTER)
                    .setMsg(context.getResources().getString(R.string.skip_empty_page_hint))
                    .setSingleButton(true)
                    .setConfirmText("??????");
            if (context instanceof Activity) {
                AutoSize.autoConvertDensityOfGlobal((Activity) context);
            }
            alertDialogHelper.show();
        }
    }

    /**
     * ??????????????????
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
     * ??????web??????
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
     * ??????
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
            //????????????????????????
            Intent intent = new Intent(context, MineLoginActivity.class);
            fragment.startActivityForResult(intent, IS_LOGIN_CODE);
        }
    }

    /**
     * ??????
     *
     * @param activity
     */
    public static void getLoginStatus(Activity activity) {
        if (activity == null || activity.isFinishing()) return;
        SharedPreferences loginStatus = activity.getSharedPreferences("loginStatus", MODE_PRIVATE);
        if (loginStatus.getBoolean("isLogin", false)) {
            userId = loginStatus.getInt("uid", 0);
        } else {
            //????????????????????????
            Intent intent = new Intent(activity, MineLoginActivity.class);
            activity.startActivityForResult(intent, IS_LOGIN_CODE);
        }
    }


    /**
     * ????????????????????????
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
     * ?????????????????????????????????
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

    //??????????????????
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
     * ??????TextView??????????????????????????????????????????????????????????????????????????????
     *
     * @param str
     * @return ???????????????????????????????????????
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
     * ????????? ?????? ????????????
     *
     * @param formatString
     * @param args
     * @return
     */
    public static String formatStrings(String formatString, Object... args) {
        return String.format(Locale.getDefault(), formatString, args);
    }

    /**
     * ??????????????????
     *
     * @param context
     * @param imageType
     * @param firstShowPosition
     * @param imagePathList
     */
    public static void showImageActivity(Context context, String imageType, int firstShowPosition, List<String> imagePathList) {
        if (imagePathList == null || imagePathList.size() < 1) {
            showToast("??????????????????~");
            return;
        }
        ImageBean imageBean = null;
        List<ImageBean> imageBeanList = new ArrayList<>();
        for (String picUrl : imagePathList) {
            imageBean = new ImageBean();
            imageBean.setPicUrl(picUrl);
            imageBeanList.add(imageBean);
        }
        ImagePagerActivity.startImagePagerActivity(context, imageType, imageBeanList, firstShowPosition);
    }


    //    ???????????? ??????
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
                    GlideImageLoaderUtil.setLoadImgFinishListener(context, requestStatus.getSrc(), new GlideImageLoaderUtil.ImageLoaderFinishListener() {
                        @Override
                        public void onSuccess(Bitmap bitmap) {
                            AlertDialogImage alertDialogAdImage = new AlertDialogImage(context);
                            alertDialogAdImage.show();
                            alertDialogAdImage.setAlertClickListener(new AlertDialogImage.AlertImageClickListener() {
                                @Override
                                public void imageClick() {
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
     * @param proTypeId ????????????
     * @param id        ??????Id
     */
    public static void skipProductUrl(Context context, int proTypeId, int id) {
        Intent intent = new Intent();
        switch (proTypeId) {
            case 0://????????????
                intent.setClass(context, ShopTimeScrollDetailsActivity.class);
                intent.putExtra("productId", String.valueOf(id));
                context.startActivity(intent);
                break;
            case 1://????????????
                intent.setClass(context, ShopScrollDetailsActivity.class);
                intent.putExtra("productId", String.valueOf(id));
                context.startActivity(intent);
                break;
            case 2://????????????
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


    //???????????????(?????????????????????????????????)
    public static void skipIndentWrite(Activity context, String type, Bundle bundle) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        try {
            if (BUY_AGAIN.equals(type)) {
                params.put("orderNo", bundle.getString("orderNo"));
            } else if (INDENT_W_TYPE.equals(type)) {
                String goodsJson = bundle.getString("goods");
                String combineGoodsJson = bundle.getString("combineGoods");

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
            } else if (INDENT_GROUP_SHOP.equals(type)) {
                String gpShopInfo = bundle.getString("gpShopInfo");
                GroupShopDetailsBean groupShopDetailsBean = GsonUtils.fromJson(gpShopInfo, GroupShopDetailsBean.class);
                if (groupShopDetailsBean != null) {
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
            }
        } catch (Exception e) {
            showToast("????????????????????????");
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

    //??????????????????
    public static void skipGroupDetail(Context context, String gpInfoId) {
        Intent intent = new Intent(context, QualityGroupShopDetailActivity.class);
        intent.putExtra("gpInfoId", gpInfoId);
        context.startActivity(intent);
    }

    //??????????????????
    public static void skipRefundDetail(Context context, String refundNo) {
        Intent intent = new Intent(context, DoMoRefundDetailActivity.class);
        intent.putExtra("refundNo", refundNo);
        context.startActivity(intent);
    }

    //??????????????????
    public static void skipRefundAspect(Context context, int refundCount, String orderNo, String refundNo) {
        Intent intent = new Intent();
        if (refundCount > 1) {
            //??????????????????
            intent.setClass(context, DirectExchangeDetailsActivity.class);
            intent.putExtra("orderNo", orderNo);
        } else {
            //????????????????????????
            intent.setClass(context, RefundMoneyActivity.class);
            intent.putExtra("refundNo", refundNo);
        }
        context.startActivity(intent);
    }

    //??????????????????
    public static void skipUserCenter(Context context, int uid) {
        if (isContextExisted(context)) {
            Intent intent = new Intent();
            intent.setClass(context, UserPagerActivity.class);
            intent.putExtra("userId", String.valueOf(uid));
            context.startActivity(intent);
        }
    }

    /**
     * ??????????????????
     *
     * @param articalType //???????????? 1.?????????????????????????????? 2.???????????????????????????
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
     * ?????????????????????????????????
     */
    public static void skipTimePostDetail(Context context, String postId) {
        if (isContextExisted(context)) {
            Intent intent = new Intent(context, TimePostDetailActivity.class);
            intent.putExtra("id", postId);
            context.startActivity(intent);
        }
    }

    /**
     * ??????????????????
     */
    public static void skipTopicDetail(Context context, String topicId) {
        if (isContextExisted(context)) {
            Intent intent = new Intent(context, TopicDetailActivity.class);
            intent.putExtra("topicId", topicId);
            context.startActivity(intent);
        }
    }


    /**
     * ??????????????????
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
     * ??????????????????
     */
    public static void skipQuestionDetail(Activity context, String questionId, String productId) {
        Intent intent = new Intent(context, QuestionDetailActivity.class);
        intent.putExtra("questionId", questionId);
        intent.putExtra("productId", productId);
        context.startActivity(intent);
    }


    /**
     * ????????????
     */
    public static void skipService(Activity activity) {
        if (isContextExisted(activity)) {
            setSkipPath(activity, "app://ManagerServiceChat", false);
        }
    }


    //    ??????????????????
    public void getPermissions(final Context context, final String... permissions) {
        AndPermission.with(context)
                .runtime()
                .permission(permissions)
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        if (AndPermission.hasAlwaysDeniedPermission(context, permissions)) {
                            // ??????????????????Dialog????????????????????????????????????????????????????????????????????????????????????????????????
                            if (alertDialogHelper == null) {
                                alertDialogHelper = new AlertDialogHelper(context);
                                alertDialogHelper.setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                                    @Override
                                    public void confirm() {
                                        // ?????????????????????
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
                                    }

                                    @Override
                                    public void cancel() {
                                    }
                                });
                            }
                            alertDialogHelper.setTitle("????????????")
                                    .setMsg("????????? " + TextUtils.join(" ", Permission.transformText(context, data))
                                            + " ???????????????????????????????????????????????????-????????????????????????")
                                    .setSingleButton(true)
                                    .setConfirmText("?????????");
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
                .start();
    }

    /**
     * ??????????????????
     *
     * @param context        ???????????????
     * @param oldVersionName ??????????????????
     * @param oldMobileModel ????????????
     * @param oldOsVersion   ????????????APP??????
     * @param sysNotice
     */
    public static void setDeviceInfo(Activity context, String oldVersionName, String oldMobileModel, String oldOsVersion, int sysNotice) {
//        ???????????????
        String osVersion = Build.VERSION.RELEASE;
//        ????????????
        String mobileModel = Build.MODEL;
//        app??????
        String versionName = getVersionName(context);
//        ??????app????????????
        int notificationStatus = getDeviceAppNotificationStatus() ? 1 : 0;

        if (!getStrings(oldVersionName).equals(versionName)
                || !getStrings(oldMobileModel).equals(mobileModel)
                || !getStrings(oldOsVersion).equals(osVersion) || notificationStatus != sysNotice) {
            upDeviceInfo(context, osVersion, mobileModel, versionName, notificationStatus);
        }
    }

    //????????????????????????
    public static boolean getDeviceAppNotificationStatus() {
        NotificationManagerCompat manager = NotificationManagerCompat.from(mAppContext);
        return manager.areNotificationsEnabled();
    }

    /**
     * ??????????????????
     *
     * @param osVersion          ????????????
     * @param mobileModel        ????????????
     * @param versionName        ????????????
     * @param notificationStatus app ????????????
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


    //??????????????????
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
     * ???????????????????????????,???????????????
     *
     * @param editText
     */
    public static void setEtFilter(EditText editText) {
        if (editText == null) {
            return;
        }
        //???????????????
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
        //?????????????????????
        InputFilter specialCharFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String regexStr = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~???@#??%??????&*????????????+|{}????????????????????????????????????]";
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
     * ???????????????
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        try {
            // ??????packagemanager?????????
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()???????????????????????????0???????????????????????????
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * ??????????????????????????????????????????
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
                // getPackageName()???????????????????????????0???????????????????????????
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
     * ????????????????????????
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
     * ????????????????????? ??????code
     *
     * @param status
     */
    public static void disposeMessageCode(int status) {
        switch (status) {
            case 458:
                showToast("???????????????????????????,??????????????????????????????????????????");
                break;
            case 461:
            case 602:
                showToast("???????????????????????????????????????????????????????????????");
                break;
            case 462:
                showToast("????????????????????????????????????????????????????????????");
                break;
            case 463:
            case 464:
            case 465:
            case 477:
            case 478:
                showToast("??????????????????????????????????????????????????????");
                break;
            case 467:
                showToast("5???????????????????????????3????????????????????????????????????????????????");
                break;
            case 468:
                showToast("??????????????????????????????????????????");
                break;
            case 472:
                showToast("????????????????????????????????????????????????");
                break;
            case 601:
                showToast("??????????????????");
                break;
            default:
                showToast("???????????????????????????????????????");
                break;
        }
    }

    /**
     * ??????context ?????????????????????badToken
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
     * ??????app
     */
    public static void installApps(Context context, File appFile) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            android 8.0????????? app????????????App????????????
            AndPermission.with(context)
                    .install()
                    .file(appFile)
                    .onGranted(file -> allowInstallApps(context, appFile))
                    .onDenied(file -> {
                        AlertDialogHelper alertDialogHelper = new AlertDialogHelper(context)
                                .setTitle("????????????")
                                .setMsg("????????? " + "??????????????????"
                                        + " ?????????????????????????????????????????????????????????????????????-????????????????????????")
                                .setSingleButton(true)
                                .setConfirmText("?????????");
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
            showToast("????????????????????????????????????");
        }
    }


    public void setOnGetPermissionsSuccess(OnGetPermissionsSuccessListener onGetPermissionsSuccessListener) {
        this.onGetPermissionsSuccessListener = onGetPermissionsSuccessListener;
    }

    //    ??????????????????
    public interface OnGetPermissionsSuccessListener {
        void getPermissionsSuccess();
    }

    /**
     * ????????????
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
     * ????????????????????????
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
     * ??????????????????
     * ??????1s??????6s
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
     * ??????????????????
     *
     * @param context
     * @param topicString ????????????
     * @param content     ????????????
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


    //??????FragmentManager
    public static void clearFragmentCache(FragmentManager fragmentManager) {
        try {
            FragmentTransaction mCurTransaction = fragmentManager.beginTransaction();
            List<Fragment> fragments = fragmentManager.getFragments();
            Iterator<Fragment> iterator = fragments.iterator();
            while (iterator.hasNext()) {
                mCurTransaction.remove(iterator.next());
            }
            mCurTransaction.commitNowAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ??????Toast???????????? ???????????????????????????
     */
    public static void showToast(String message) {
        ToastUtils.show(message);
    }

    public static void showToast(int resId) {
        showToast(mAppContext.getResources().getString(resId));
    }

    /**
     * ???????????????????????????
     *
     * @param requestStatus
     */
    public static void showToastRequestMsg(RequestStatus requestStatus) {
        showToast(requestStatus == null ? "???????????????" :
                requestStatus.getResult() != null ? getStrings(requestStatus.getResult().getResultMsg()) : getStrings(requestStatus.getMsg()));
    }

    /**
     * ??????????????????
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
                .setConfirmText("??????")
                .setConfirmTextColor(context.getResources().getColor(R.color.text_login_gray_s))
                .setTitle("??????")
                .setTitleGravity(Gravity.CENTER)
                .setConfirmTextColor(context.getResources().getColor(R.color.text_login_blue_z))
                .setMsg(hintText)
                .setMsgTextGravity(Gravity.CENTER);
        if (!context.isFinishing()) {
            alertImportDialogHelper.show();
        }
    }


    /**
     * ??????
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
     * md5??????
     *
     * @param text
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String toMD5(String text) {
        //??????????????? MessageDigest
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            //?????????????????????????????????????????????????????????hash??????
            byte[] digest = messageDigest.digest(text.getBytes());

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < digest.length; i++) {
                //?????????????????? ?????????????????????????????????;
                int digestInt = digest[i] & 0xff;
                //???10????????????????????????16??????
                String hexString = Integer.toHexString(digestInt);
                //???????????????????????????????????????0,??????????????????0
                if (hexString.length() < 2) {
                    sb.append(0);
                }
                //?????????????????????????????????
                sb.append(hexString);
            }
            //??????????????????
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return text;
        }
    }

    //??????????????????sourceType
    public static int getSourceType(String className) {
        switch (className) {
            case "ArticleOfficialActivity"://??????
                return ConstantVariable.ARTICLE;
            case "QualityShopBuyListActivity"://????????????
            case "QualityShopBuyListFragment"://????????????
            case "QualityShopHistoryListActivity"://????????????
                return ConstantVariable.MUST_BUY_TOPIC;
            case "DoMoLifeWelfareActivity"://?????????????????????
            case "DoMoLifeWelfareFragment"://?????????????????????
            case "DoMoLifeWelfareDetailsActivity"://?????????????????????
            case "DoMoLifeWelfareDetailsFragment"://?????????????????????
                return ConstantVariable.WELFARE_TOPIC;
            case "DmlOptimizedSelDetailActivity"://??????????????????
                return ConstantVariable.SUPER_GOOD;
            case "EditorSelectActivity"://????????????
            case "EditorSelectFragment"://????????????
                return ConstantVariable.REDACTOR_PICKED;
            case "QualityWeekOptimizedActivity"://????????????
            case "QualityWeekOptimizedFragment"://????????????
                return ConstantVariable.WEEKLY_ZONE;
            case "PostDetailActivity"://????????????
                return ConstantVariable.POST;
            case "VipZoneDetailActivity"://?????????/????????????????????????
            case "VipZoneDetailFragment"://?????????/????????????????????????
                return ConstantVariable.VIP_ZONE;
            case "VideoDetailActivity"://??????????????????
                return ConstantVariable.VIDEO;
            case "QualityCustomTopicActivity"://???????????????
            case "QualityCustomTopicFragment":
                return ConstantVariable.CUSTOM;
            default:
                return -1;
        }
    }

    //??????sourceId?????????map
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

    //??????????????????sourceId
    public static String getSourceId(String className) {
        return (String) ((TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike()).getSourceMap().get(className);
    }

    //????????????????????????
    public static void addSourceParameter(Map<String, Object> params) {
        TinkerBaseApplicationLike tinkerBaseApplicationLike = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
        String[] sourceParameter = tinkerBaseApplicationLike.getPreviousActivity();
        if (!TextUtils.isEmpty(sourceParameter[0])) {
            params.put("sourceType", sourceParameter[0]);
            params.put("sourceId", sourceParameter[1]);
        }
    }


    //?????????????????????????????????Activity,??????????????????????????????Tab??????Fragment?????????
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

    //??????bitmap
    public static void recycleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        System.gc();
    }

    //??????????????????
    public static int getPostType(String title) {
        if ("??????".equals(title)) {
            return 3;
        } else if ("??????".equals(title)) {
            return 1;
        } else if ("????????????".equals(title)) {
            return 4;
        } else if ("??????".equals(title)) {
            return 5;
        } else if ("??????".equals(title)) {
            return 2;
        } else {
            return 0;
        }
    }

    //????????????????????????
    public static void setTextLink(Activity activity, TextView textView) {
        //???????????????
        Pattern pattern = Pattern.compile(REGEX_URL);
        Matcher matcher = pattern.matcher(textView.getText().toString());
        //?????????????????????
        if (matcher.find()) {
            Link discernUrl = new Link(pattern);
            discernUrl.setTextColor(0xff0a88fa);
            discernUrl.setUnderlined(false);
            discernUrl.setHighlightAlpha(0f);
            discernUrl.setUrlReplace(true);
            discernUrl.setOnClickListener(clickedText -> {
                if (!TextUtils.isEmpty(clickedText)) {
                    setSkipPath(activity, clickedText, false);
                } else {
                    showToast("????????????????????????");
                }
            });
            LinkBuilder.on(textView)
                    .addLink(discernUrl)
                    .build();
        }
    }

    //??????loading
    public static void showLoadhud(Activity context) {
        KProgressHUD loadHud = ((BaseActivity) context).loadHud;
        if (isContextExisted(context) && loadHud != null && !loadHud.isShowing()) {
            loadHud.show();
        }
    }

    //??????loading
    public static void dismissLoadhud(Activity context) {
        KProgressHUD loadHud = ((BaseActivity) context).loadHud;
        if (isContextExisted(context) && loadHud != null && loadHud.isShowing()) {
            loadHud.dismiss();
        }
    }
}
