package com.amkj.dmsh.homepage.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.bean.BaseAddCarProInfoBean;
import com.amkj.dmsh.bean.H5ShareBean;
import com.amkj.dmsh.constant.AppUpdateUtils;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.homepage.bean.JsInteractiveBean;
import com.amkj.dmsh.qyservice.QyServiceUtils;
import com.amkj.dmsh.utils.CalendarReminderUtils;
import com.amkj.dmsh.utils.ImgUrlHelp;
import com.amkj.dmsh.utils.Log;
import com.amkj.dmsh.utils.MarketUtils;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.SharedPreUtils;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.pictureselector.PictureSelectorUtils;
import com.amkj.dmsh.views.HtmlWebView;
import com.amkj.dmsh.views.alertdialog.AlertDialogHelper;
import com.google.gson.JsonSyntaxException;
import com.gyf.barlibrary.ImmersionBar;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfigC;
import com.luck.picture.lib.entity.LocalMediaC;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.umeng.socialize.UMShareAPI;
import com.yanzhenjie.permission.Permission;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.AutoSize;

import static android.app.Activity.RESULT_OK;
import static com.amkj.dmsh.constant.ConstantMethod.getDeviceAppNotificationStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getMapValue;
import static com.amkj.dmsh.constant.ConstantMethod.getOnlyUrlParams;
import static com.amkj.dmsh.constant.ConstantMethod.getStringChangeIntegers;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsFormat;
import static com.amkj.dmsh.constant.ConstantMethod.getVersionName;
import static com.amkj.dmsh.constant.ConstantMethod.isContextExisted;
import static com.amkj.dmsh.constant.ConstantMethod.setSkipPath;
import static com.amkj.dmsh.constant.ConstantMethod.showImportantToast;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_W_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.REQUEST_NOTIFICATION_STATUS;
import static com.amkj.dmsh.constant.ConstantVariable.TOKEN;
import static com.amkj.dmsh.constant.ConstantVariable.WEB_JD_SCHEME;
import static com.amkj.dmsh.constant.ConstantVariable.WEB_TAOBAO_SCHEME;
import static com.amkj.dmsh.constant.ConstantVariable.WEB_TB_SCHEME;
import static com.amkj.dmsh.constant.ConstantVariable.WEB_TMALL_SCHEME;
import static com.amkj.dmsh.dao.BaiChuanDao.skipAliBC;
import static com.amkj.dmsh.dao.OrderDao.addShopCarGetSku;
import static com.amkj.dmsh.rxeasyhttp.interceptor.MyInterceptor.getCommonApiParameter;
import static com.amkj.dmsh.rxeasyhttp.utils.DeviceUtils.getDeviceId;
import static com.amkj.dmsh.utils.TimeUtils.getDateMilliSecond;
import static com.luck.picture.lib.config.PictureConfigC.CHOOSE_REQUEST;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/10/28
 * class description:????????????
 */
public class AliBCFragment extends BaseFragment {
    @BindView(R.id.ll_web_ali)
    LinearLayout ll_web_ali;
    @BindView(R.id.tv_life_back)
    TextView tv_life_back;
    @BindView(R.id.tv_header_shared)
    TextView tv_header_shared;
    @BindView(R.id.tv_header_title)
    TextView tv_header_title;
    @BindView(R.id.tl_normal_bar)
    Toolbar tl_normal_bar;
    @BindView(R.id.web_fragment_communal)
    HtmlWebView web_fragment_communal;
    @BindView(R.id.rel_communal_net_error)
    RelativeLayout rel_communal_net_error;
    @BindView(R.id.smart_web_fragment_refresh)
    SmartRefreshLayout smart_web_fragment_refresh;
    @BindView(R.id.myProgressBar)
    ProgressBar mPb;
    private String webUrl;
    private String shareData;
    private String paddingStatus;
    private String jsIdentifying;
    private String refreshStatus;
    private AlertDialogHelper alertDialogHelper;
    private boolean isCanEditStatusBar = false;
    private ImmersionBar immersionBar;
    private WeakReference<Activity> activityWeakReference;
    private AlertDialogHelper notificationAlertDialogHelper;
    private AlertDialogHelper marketStoreGradeDialog;
    private H5ShareBean mShareBean;

    @Override
    protected int getContentView() {
        return R.layout.fragment_web_ali_bc_activity;
    }

    @Override
    protected void initViews() {
        tl_normal_bar.setVisibility(View.GONE);
        rel_communal_net_error.setVisibility(View.GONE);
        ll_web_ali.setBackgroundColor(getResources().getColor(R.color.transparent));
        if (TextUtils.isEmpty(webUrl)) {
            return;
        }
//        ?????????????????????
        web_fragment_communal.getSettings().setUseWideViewPort(true);
        web_fragment_communal.getSettings().setLoadWithOverviewMode(true);
        WebSettings webSettings = web_fragment_communal.getSettings();
        //        ?????????????????????
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        //??????WebView?????????????????????Javascript??????
        webSettings.setJavaScriptEnabled(true);
        //????????????????????????
        webSettings.setAllowFileAccess(true);
        //??????????????????
        webSettings.setBuiltInZoomControls(false);
        //????????????H5 DomStorage
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setUseWideViewPort(true); // ?????????
        webSettings.setSupportZoom(true); // ????????????
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // ?????????????????????

        web_fragment_communal.setWebChromeClient(new MyWebChromeClient());
        //???????????????????????????
        if (NetWorkUtils.checkNet(getActivity())) {
            webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        } else {
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
        web_fragment_communal.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Intent action = new Intent();
                action.setAction("android.intent.action.VIEW");
                action.setData(Uri.parse(url));
                startActivity(action);
            }
        });
        web_fragment_communal.getSettings().setUserAgentString(web_fragment_communal.getSettings().getUserAgentString() + " domolifeandroid" + getRandomString(501));
//        js??????
        web_fragment_communal.addJavascriptInterface(new JsData(getActivity()), "JsToAndroid");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        //??????Web??????
        web_fragment_communal.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                // ??????????????????????????????
                if (errorCode == ERROR_HOST_LOOKUP || errorCode == ERROR_CONNECT || errorCode == ERROR_TIMEOUT) {
                    setErrorException(view);
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                int errorCode = error.getErrorCode();
                if (404 == errorCode || 500 == errorCode || errorCode == -2) {
                    setErrorException(view);
                }
            }

            @Override
            public void onPageFinished(final WebView view, final String url) {
//                    ???????????????????????????
                if (smart_web_fragment_refresh != null && RefreshState.Refreshing.equals(smart_web_fragment_refresh.getState())) {
                    smart_web_fragment_refresh.finishRefresh();
                }
                super.onPageFinished(view, url);
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                try {
                    if (!url.startsWith("http://") && !url.startsWith("https://")) {
                        if (url.contains(WEB_TAOBAO_SCHEME) || url.contains(WEB_TB_SCHEME) || url.contains(WEB_JD_SCHEME)
                                || url.contains(WEB_TMALL_SCHEME)) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            startActivity(intent);
                            return true;
                        }
                    }
                } catch (Exception e) {//??????crash (???????????????????????????????????????scheme?????????url???APP, ?????????crash)
                }
//                ?????????true ??????????????????????????????????????? todo
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                try {
                    if (!url.startsWith("http://") && !url.startsWith("https://")) {
                        if (url.contains(WEB_TAOBAO_SCHEME) || url.contains(WEB_TB_SCHEME) || url.contains(WEB_JD_SCHEME)
                                || url.contains(WEB_TMALL_SCHEME)) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            startActivity(intent);
                            return true;
                        }
                    }
                } catch (Exception e) {//??????crash (???????????????????????????????????????scheme?????????url???APP, ?????????crash)
                }
//                ?????????true ??????????????????????????????????????? todo
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        web_fragment_communal.setOnScrollChangedCallback(new HtmlWebView.OnScrollChangedCallback() {
            @Override
            public void onScroll(int dx, int dy) {
                if (dy < 2) {
                    setWebRefreshStatus(1);
                } else {
                    setWebRefreshStatus(0);
                }
            }
        });
        smart_web_fragment_refresh.setEnableAutoLoadMore(false);
        smart_web_fragment_refresh.setEnableNestedScroll(false);
        smart_web_fragment_refresh.setEnableOverScrollBounce(false);
        smart_web_fragment_refresh.setEnableOverScrollDrag(false);
        smart_web_fragment_refresh.setEnableAutoLoadMore(false);
        smart_web_fragment_refresh.setOnRefreshListener(refreshLayout -> web_fragment_communal.reload());
        if (paddingStatus.contains("true")) {
            isCanEditStatusBar = true;
        }
        activityWeakReference = new WeakReference<Activity>(getActivity());
    }

    private void setErrorException(WebView view) {
        rel_communal_net_error.setVisibility(View.VISIBLE);
    }

    private void setWebRefreshStatus(int refreshStatus) {
        if (smart_web_fragment_refresh == null) return;
        if (!TextUtils.isEmpty(this.refreshStatus)) {
            smart_web_fragment_refresh.setEnableRefresh(this.refreshStatus.contains("1"));
        } else {
            smart_web_fragment_refresh.setEnableRefresh(refreshStatus == 1);
        }
    }

    private String getRandomString(int length) { //length??????????????????????????????
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    //???????????????????????????????????????

    @Override
    protected void loadData() {
        web_fragment_communal.loadUrl(webUrl);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            if (requestCode == IS_LOGIN_CODE) {
                transmitUid();
                return;
            }
        }
        if (requestCode == REQUEST_NOTIFICATION_STATUS) {
            notificationStatusCallback();
        }
        if (resultCode == RESULT_OK) {
            if (requestCode == IS_LOGIN_CODE) {
                transmitUid();
            } else if (requestCode == CHOOSE_REQUEST) {
                List<LocalMediaC> localMediaList = PictureSelector.obtainMultipleResult(data);
                if (localMediaList != null && localMediaList.size() > 0) {
                    for (LocalMediaC localMedia : localMediaList) {
                        String imgPath = localMedia.getPath();
                        if (!TextUtils.isEmpty(imgPath)) {
                            selectImageUp(imgPath);
                        }
                    }
                }
            }
        }
        UMShareAPI.get(getActivity()).onActivityResult(requestCode, resultCode, data);
    }

    /**
     * ??????????????????
     *
     * @param imgPath
     */
    private void selectImageUp(String imgPath) {
        if (loadHud != null) {
            loadHud.show();
        }
        List<String> pathList = new ArrayList<>();
        pathList.add(imgPath);
        ImgUrlHelp imgUrlHelp = new ImgUrlHelp();
        imgUrlHelp.setUrl(getActivity(), pathList);
        imgUrlHelp.setOnFinishListener(new ImgUrlHelp.OnFinishDataListener() {
            @Override
            public void finishData(List<String> data, Handler handler) {
                String imageUrl = data.get(0);
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                webViewJs(String.format(getResources().getString(R.string.web_image_up_method), imageUrl, jsIdentifying));
                handler.removeCallbacksAndMessages(null);
            }

            @Override
            public void finishError(String error) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
            }

            @Override
            public void finishSingleImg(String singleImg, Handler handler) {
            }
        });
    }

    //    ???????????????????????????
    class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onCloseWindow(WebView window) {
            super.onCloseWindow(window);
        }

        @Override
        public boolean onCreateWindow(WebView view, boolean dialog,
                                      boolean userGesture, Message resultMsg) {
            return super.onCreateWindow(view, dialog, userGesture, resultMsg);
        }

        /**
         * ???????????????window.alert?????????????????????title????????????????????????file:////???
         */
        @Override
        public boolean onJsAlert(WebView view, String url, String message,
                                 JsResult result) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            TextView titleView = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.layout_dialog_title_textview, null);
            titleView.setText("??????");
            builder.setCustomTitle(titleView)
                    .setMessage(message)
                    .setPositiveButton("??????", null);

            // ???????????????????????????
            // ??????keycode??????84???????????????
            builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    return true;
                }
            });
            // ???????????????back????????????
            builder.setCancelable(false);
            AlertDialog dialog = builder.create();
            dialog.show();
            result.confirm();// ???????????????????????????????????????confirm,??????????????????????????????????????????
            return true;
            // return super.onJsAlert(view, url, message, result);
        }

        @Override
        public boolean onJsBeforeUnload(WebView view, String url,
                                        String message, JsResult result) {
            return super.onJsBeforeUnload(view, url, message, result);
        }

        /**
         * ???????????????window.confirm?????????????????????title????????????????????????file:////???
         */
        @Override
        public boolean onJsConfirm(WebView view, String url, String message,
                                   final JsResult result) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            TextView titleView = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.layout_dialog_title_textview, null);
            titleView.setText("??????");
            builder.setCustomTitle(titleView)
                    .setMessage(message)
                    .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            result.confirm();
                        }
                    })
                    .setNeutralButton("??????", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            result.cancel();
                        }
                    });
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    result.cancel();
                }
            });

            // ??????keycode??????84????????????????????????????????????????????????????????????????????????????????????????????????
            builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                    Log.v("onJsConfirm", "keyCode==" + keyCode + "event=" + event);
                    return true;
                }
            });
            // ???????????????back????????????
            builder.setCancelable(false);
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
            // return super.onJsConfirm(view, url, message, result);
        }

        /**
         * ???????????????window.prompt?????????????????????title????????????????????????file:////???
         * window.prompt('???????????????????????????', '618119.com');
         */
        public boolean onJsPrompt(WebView view, String url, String message,
                                  String defaultValue, final JsPromptResult result) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            TextView titleView = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.layout_dialog_title_textview, null);
            titleView.setText("??????");
            builder.setCustomTitle(titleView).setMessage(message);
            final EditText et = new EditText(view.getContext());
            et.setSingleLine();
            et.setText(defaultValue);
            builder.setView(et)
                    .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            result.confirm(et.getText().toString());
                        }
                    })
                    .setNeutralButton("??????", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            result.cancel();
                        }
                    });

            // ??????keycode??????84????????????????????????????????????????????????????????????????????????????????????????????????
            builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                    Log.v("onJsPrompt", "keyCode==" + keyCode + "event=" + event);
                    return true;
                }
            });

            // ???????????????back????????????
            builder.setCancelable(false);
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
            // return super.onJsPrompt(view, url, message, defaultValue,
            // result);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress == 100 && mPb != null) {
                //??????????????????????????????
                if (RefreshState.Refreshing.equals(smart_web_fragment_refresh.getState())) {
                    smart_web_fragment_refresh.finishRefresh();
                }
                mPb.setVisibility(View.INVISIBLE);
            } else if (mPb != null) {
                //fragment?????????????????????
//                if (View.INVISIBLE == mPb.getVisibility()) {
//                    mPb.setVisibility(View.VISIBLE);
//                }
//                mPb.setProgress(newProgress);

                //?????????????????????????????????
                if (RefreshState.Refreshing.equals(smart_web_fragment_refresh.getState())) {
                    mPb.setVisibility(View.INVISIBLE);
                }
            }
        }

        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }

        @Override
        public void onRequestFocus(WebView view) {
            super.onRequestFocus(view);
        }
    }

    public class JsData {
        private Context context;

        public JsData(Context context) {
            this.context = context;
        }

        //        ????????????
        @JavascriptInterface
        public void skipPage(String result) {
            setSkipPath(context, result, false);
        }

        //        ??????????????????uid
        @JavascriptInterface
        public void getUserIdFromAndroid() {
            getUserId();
        }

        //      ??????????????????
        @JavascriptInterface
        public void skipAlibc(String urlType) {
            if (!TextUtils.isEmpty(urlType)) {
                Map<String, String> urlParams = getOnlyUrlParams(urlType);
                String url = urlParams.get("url");
                String thirdId = urlParams.get("thirdId");
                skipAliBC(getActivity(), url, thirdId);
            } else {
                showToast(R.string.unConnectedNetwork);
            }
        }

        //      ????????????
        @JavascriptInterface
        public void skipService() {
            QyServiceUtils qyServiceUtils = QyServiceUtils.getQyInstance();
            qyServiceUtils.openQyServiceChat(getActivity(), "web", "");
        }

        @JavascriptInterface
        public void sharePage(String result) {
            try {
                getActivity().runOnUiThread(() -> {
                    mShareBean = GsonUtils.fromJson(result, H5ShareBean.class);
                    shareH5();
                });
            } catch (JsonSyntaxException e) {
                jsInteractiveException("");
            }
        }

        /**
         * ????????????
         *
         * @param jsIdentifying js??????
         */
        @JavascriptInterface
        public void openImage(String jsIdentifying) {
            AliBCFragment.this.jsIdentifying = jsIdentifying;
            ConstantMethod constantMethod = new ConstantMethod();
            constantMethod.setOnGetPermissionsSuccess(new ConstantMethod.OnGetPermissionsSuccessListener() {
                @Override
                public void getPermissionsSuccess() {
                    PictureSelectorUtils.getInstance()
                            .resetVariable()
                            .isCrop(false)
                            .imageMode(PictureConfigC.SINGLE)
                            .isShowGif(true)
                            .openGallery(AliBCFragment.this);
                }
            });
            constantMethod.getPermissions(getActivity(), Permission.Group.STORAGE);
        }

        /**
         * v 3.1.8 ??????????????????????????? ??????????????????
         *
         * @param resultJson json ??????
         */
        @JavascriptInterface
        public void androidJsInteractive(String resultJson) {
            try {
                if (TextUtils.isEmpty(resultJson)) {
                    jsInteractiveException("web");
                }
                JsInteractiveBean jsInteractiveBean = JSON.parseObject(resultJson, JsInteractiveBean.class);
                if (jsInteractiveBean != null && !TextUtils.isEmpty(jsInteractiveBean.getType())) {
                    if (!isContextExisted(getActivity())) return;
                    getActivity().runOnUiThread(() -> {
                        try {//?????????????????????????????????????????????????????????????????????????????????
                            switch (jsInteractiveBean.getType()) {
                                case "userId":
                                    jsGetUserId(jsInteractiveBean);
                                    break;
                                case "getHeaderFromApp":
                                    getHeaderFromApp(jsInteractiveBean.getOtherData());
                                    break;
                                case "addGoodsToCart":
                                    addGoodsToCart(jsInteractiveBean.getOtherData());
                                    break;
                                case "showToast":
                                    showImportToast(jsInteractiveBean.getOtherData());
                                    break;
                                case "refresh":
                                    jsRefreshStatus(jsInteractiveBean);
                                    break;
                                case "navigationBar":
                                    jsSetNavBar(jsInteractiveBean);
                                    break;
                                case "appDeviceInfo":
                                    jsGetAppDeviceInfo();
                                    break;
                                case "appUpdate":
                                    jsInteractiveEmpty(jsInteractiveBean);
                                    break;
                                case "finishWebPage":
                                    jsAutoFinishPage(jsInteractiveBean);
                                    break;
                                case "alibcUrl":
                                    jsSkipTaoBao(jsInteractiveBean);
                                    break;
                                case "statusBar":
                                    if (isCanEditStatusBar) {
                                        jsSetStatusBar(jsInteractiveBean);
                                    }
                                    break;
                                case "calendarReminder":
                                    addCalendarReminder(jsInteractiveBean);
                                    break;
                                case "openNotification":
                                    openNotification(jsInteractiveBean);
                                    break;
                                case "appMarketGrade":
                                    skipAppMarketGrade(jsInteractiveBean);
                                    break;
                                case "notificationStatus":
                                    notificationStatusCallback();
                                    break;
                                case "skipIndentWrite":
                                    skipIndentWrite(jsInteractiveBean.getOtherData());
                                    break;
                                case "screenshotToShare":
                                    sharePicUrl(jsInteractiveBean.getOtherData());
                                    break;
                                default:
                                    jsInteractiveEmpty(null);
                                    break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                } else {
                    jsInteractiveException("web");
                }
            } catch (Exception e) {
                jsInteractiveException(null);
                e.printStackTrace();
            }
        }
    }

    //????????????
    private void sharePicUrl(Map<String, Object> map) {
        if (map != null && map.size() > 0) {
            H5ShareBean shareBean = GsonUtils.fromJson(GsonUtils.toJson(map), H5ShareBean.class);
            if (shareBean != null) {
                UMShareAction umShareAction = new UMShareAction((BaseActivity) getActivity()
                        , shareBean.getImageUrl()
                        , shareBean.getTitle()
                        , shareBean.getDescription()
                        , shareBean.getUrl(), shareBean.getRoutineUrl(), shareBean.getObjId(), shareBean.getShareType(), shareBean.getPlatform());
            }
        }
    }

    //???????????????
    private void addGoodsToCart(Map<String, Object> map) {
        if (map != null) {
            int productId = getStringChangeIntegers((String) map.get("productId"));
            String title = (String) map.get("title");
            String picUrl = (String) map.get("picUrl");
            loadHud.show();
            if (userId > 0) {
                BaseAddCarProInfoBean baseAddCarProInfoBean = new BaseAddCarProInfoBean();
                baseAddCarProInfoBean.setProductId(productId);
                baseAddCarProInfoBean.setProName(title);
                baseAddCarProInfoBean.setProPic(picUrl);
                addShopCarGetSku(getActivity(), baseAddCarProInfoBean);
            } else {
                loadHud.dismiss();
                getLoginStatus(getActivity());
            }
        }
    }

    //??????????????????
    private void skipIndentWrite(Map<String, Object> data) {
        if (data != null) {
            String goods = (String) data.get("goods");
            if (!TextUtils.isEmpty(goods)) {
                Bundle bundle = new Bundle();
                bundle.putString("goods", goods);
                ConstantMethod.skipIndentWrite(getActivity(), INDENT_W_TYPE, bundle);
            }
        }
    }

    //????????????H5??????
    private void shareH5() {
        if (mShareBean != null) {
            UMShareAction umShareAction = new UMShareAction((BaseActivity) getActivity()
                    , mShareBean.getImageUrl()
                    , mShareBean.getTitle()
                    , mShareBean.getDescription()
                    , mShareBean.getUrl(), mShareBean.getRoutineUrl(), mShareBean.getObjId(), mShareBean.getShareType(), mShareBean.getPlatform());
        }
    }


    //??????Header???
    public void getHeaderFromApp(Map<String, Object> data) {
        Map<String, Object> map = getCommonApiParameter(getActivity());
        //??????????????????uid???token
        if (userId > 0) {
            map.put("uid", ConstantMethod.userId);
            String token = (String) SharedPreUtils.getParam(TOKEN, "");
            map.put("token", token);
        }

        if (data != null && data.get("mustLogin") != null) {
            int mustLogin = ((int) data.get("mustLogin"));
            //???????????????????????????
            if (mustLogin == 1 && userId <= 0) {
                getLoginStatus(this);
            }
        }
//        String base64 = Base64.encodeToString(new JSONObject(map).toString().getBytes(), Base64.NO_WRAP);
        webViewJs(getStringsFormat(getActivity(), R.string.web_head_method, new JSONObject(map).toString(), userId == 0 ? "0" : String.valueOf(userId)));
    }

    //??????
    public void showImportToast(Map<String, Object> map) {
        if (map != null) {
            String msg = (String) map.get("msg");
            if (!TextUtils.isEmpty(msg)) {
                showImportantToast(getActivity(), msg);
            }
        }
    }

    /**
     * ????????????????????????
     *
     * @param jsInteractiveBean
     */
    private void skipAppMarketGrade(JsInteractiveBean jsInteractiveBean) {
        if (activityWeakReference.get() == null) {
            return;
        }
        Map<String, Object> otherData = jsInteractiveBean.getOtherData();
        if (otherData == null || otherData.get("gradeContent") == null) {
            return;
        }
        //        ??????????????????????????????????????????
        try {
            List<PackageInfo> packageInfo = activityWeakReference.get().getPackageManager().getInstalledPackages(0);
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
                if (marketStoreGradeDialog == null) {
                    marketStoreGradeDialog = new AlertDialogHelper(activityWeakReference.get());
                    String finalAppMarketStore = appMarketStore;
                    marketStoreGradeDialog.setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                        @Override
                        public void confirm() {
                            MarketUtils.launchAppDetail(activityWeakReference.get().getApplicationContext(), activityWeakReference.get().getPackageName(), finalAppMarketStore);
                        }

                        @Override
                        public void cancel() {
                        }
                    });
                }
                String dialogContent = (String) getMapValue(otherData.get("gradeContent"), "");
                String dialogHint = (String) getMapValue(otherData.get("gradeTitle"), "??????");
                String confirmText = (String) getMapValue(otherData.get("btnTitle"), "?????????");
                marketStoreGradeDialog.setTitle(dialogHint)
                        .setMsg(dialogContent)
                        .setSingleButton(true)
                        .setConfirmText(confirmText);
                marketStoreGradeDialog.show();
            }
        } catch (Exception e) {
            Log.d(getClass().getSimpleName(), "????????????????????????");
        }
    }

    /**
     * ??????app??????
     *
     * @param jsInteractiveBean
     */
    private void openNotification(JsInteractiveBean jsInteractiveBean) {
        if (activityWeakReference.get() == null) {
            return;
        }
        if (jsInteractiveBean.getOtherData() != null && !getDeviceAppNotificationStatus()) {
            if (notificationAlertDialogHelper == null) {
                notificationAlertDialogHelper = new AlertDialogHelper(activityWeakReference.get());
                notificationAlertDialogHelper.setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                    @Override
                    public void confirm() {
                        // ??????isOpened?????????????????????????????????????????????AppInfo??????????????????App????????????
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", activityWeakReference.get().getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_NOTIFICATION_STATUS);
                    }

                    @Override
                    public void cancel() {
                    }
                });
            }
            Map<String, Object> otherData = jsInteractiveBean.getOtherData();
            String dialogContent = (String) getMapValue(otherData.get("notificationContent"), "???????????????????????????????????????,???????????????????????????????????????????????????????????????");
            String dialogHint = (String) getMapValue(otherData.get("notificationTitle"), "????????????");
            String confirmText = (String) getMapValue(otherData.get("btnTitle"), "?????????");
            notificationAlertDialogHelper.setTitle(dialogHint)
                    .setMsg(dialogContent)
                    .setSingleButton(true)
                    .setConfirmText(confirmText);
            notificationAlertDialogHelper.show();
        }
    }

    /**
     * ????????????
     */
    private void addCalendarReminder(JsInteractiveBean jsInteractiveBean) {
        Map<String, Object> otherData = jsInteractiveBean.getOtherData();
        if (otherData != null) {
            String goodsId = (String) getMapValue(otherData.get("goodsId"), "");
            String title = (String) getMapValue(otherData.get("title"), "");
            String description = (String) getMapValue(otherData.get("description"), "");
            String startTime = (String) getMapValue(otherData.get("startTime"), "");
            long startTimeMilliSecond = getDateMilliSecond(startTime);
            String endTime = (String) getMapValue(otherData.get("endTime"), "");
            long[] endTimeMilliSecond = {getDateMilliSecond(endTime)};
            if (startTimeMilliSecond == 0) {
                showToast("???????????????????????????????????????");
                return;
            }
            if (endTimeMilliSecond[0] == 0 || endTimeMilliSecond[0] <= startTimeMilliSecond) {
                endTimeMilliSecond[0] = startTimeMilliSecond + 10 * 60 * 1000;
            }
            Number number = (Number) getMapValue(otherData.get("priorMinutes"), 1);
            long priorMinutes = number.longValue();
            ConstantMethod constantMethod = new ConstantMethod();
            constantMethod.setOnGetPermissionsSuccess(new ConstantMethod.OnGetPermissionsSuccessListener() {
                @Override
                public void getPermissionsSuccess() {
                    CalendarReminderUtils calendarReminderUtils = new CalendarReminderUtils();
                    addWebReminderCallback(goodsId, calendarReminderUtils.addCalendarEvent(activityWeakReference.get(), title, description, startTimeMilliSecond, endTimeMilliSecond[0], priorMinutes));
                }
            });
            constantMethod.getPermissions(activityWeakReference.get(), Permission.Group.CALENDAR);
        }
    }

    /**
     * ???????????????
     *
     * @param jsInteractiveBean
     */
    private void jsSetStatusBar(JsInteractiveBean jsInteractiveBean) {
        Map<String, Object> otherData = jsInteractiveBean.getOtherData();
        if (otherData != null) {
            //            ????????????
            String statusBarBgColor = (String) getMapValue(otherData.get("statusBarBgColor"), "ffffff");
//            ??????????????????
            String statusBarBgAlpha = (String) getMapValue(otherData.get("statusBarBgAlpha"), "0");
//            ?????????????????????
            int statusBarTextColorValue = (int) getMapValue(otherData.get("statusBarTextColor"), 1);
            String colorValue = "#ffffff";
            float alpha = 0;
            try {
                colorValue = "#" + statusBarBgColor;
                alpha = Float.parseFloat(statusBarBgAlpha);
            } catch (Exception e) {
                e.printStackTrace();
            }
            setWebFragmentStatusBar(statusBarTextColorValue, colorValue, alpha);
        }
    }

    /**
     * @param statusBarTextColorValue
     * @param colorValue
     * @param alpha
     */
    private void setWebFragmentStatusBar(int statusBarTextColorValue, String colorValue, float alpha) {
        immersionBar = ImmersionBar.with(AliBCFragment.this).statusBarColor(colorValue, alpha).keyboardEnable(true)
                .statusBarDarkFont(statusBarTextColorValue == 1).fitsSystemWindows(true).navigationBarEnable(false);
        immersionBar.init();
    }

    /**
     * js??????????????????
     *
     * @param jsInteractiveBean
     */
    private void jsSkipTaoBao(JsInteractiveBean jsInteractiveBean) {
        Map<String, Object> otherData = jsInteractiveBean.getOtherData();
        if (otherData != null) {
            String thirdId = (String) getMapValue(otherData.get("tbThirdId"), "");
            String tbUrl = (String) getMapValue(otherData.get("tbUrl"), "");
            skipAliBC(getActivity(), tbUrl, thirdId);
        }
    }

    /**
     * js??????????????????
     *
     * @param jsInteractiveBean
     */
    private void jsAutoFinishPage(JsInteractiveBean jsInteractiveBean) {
        Map<String, Object> otherData = jsInteractiveBean.getOtherData();
        if (otherData != null && otherData.get("pageCount") != null) {
            int pageCount = (int) getMapValue(otherData.get("pageCount"), 1);
            finishWebPage(pageCount);
        } else {
            finishWebPage(1);
        }
    }

    /**
     * js??????app??????
     */
    private void jsGetAppDeviceInfo() {
        try {
//        app?????????
            String versionName = getVersionName(getActivity());
//        ???????????????
            String osVersion = Build.VERSION.RELEASE;
//        ????????????
            String mobileModel = Build.MODEL;
//        android?????????
            String deviceId = getDeviceId(getActivity());
            com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
            jsonObject.put("appVersion", versionName);
            jsonObject.put("sysVersion", osVersion);
            jsonObject.put("deviceModel", mobileModel);
            jsonObject.put("deviceId", deviceId);
            webViewJs("javascript:" + "getDeviceInfo(" + jsonObject.toJSONString() + ")");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * js???????????????
     *
     * @param jsInteractiveBean
     */
    private void jsSetNavBar(JsInteractiveBean jsInteractiveBean) {
        Map<String, Object> otherData = jsInteractiveBean.getOtherData();
        if (tl_normal_bar != null && otherData != null && otherData.get("navBarVisibility") != null) {
            try {
                int navBarVisibility = (int) otherData.get("navBarVisibility");
                if (navBarVisibility == 1) {
                    tl_normal_bar.setVisibility(View.VISIBLE);
                    String navBarTitle = (String) getMapValue(otherData.get("navTitle"), "");
                    tv_header_title.setText(getStrings(navBarTitle));
                    tv_header_shared.setVisibility(((int) getMapValue(otherData.get("navShareVisibility"), 0)) == 1 ?
                            View.VISIBLE : View.GONE);
                } else {
                    setDefaultNavBar(View.GONE);
                }
            } catch (Exception e) {
                setDefaultNavBar(View.GONE);
                e.printStackTrace();
            }
        } else {
            setDefaultNavBar(View.GONE);
        }
    }

    /**
     * ???????????????????????????
     */
    private void setDefaultNavBar(int navBarVisibility) {
        tv_header_title.setText("");
        tv_header_shared.setVisibility(View.GONE);
        tl_normal_bar.setVisibility(navBarVisibility);
    }

    /**
     * js????????????id
     *
     * @param jsInteractiveBean
     */
    private void jsGetUserId(JsInteractiveBean jsInteractiveBean) {
        Map<String, Object> otherData = jsInteractiveBean.getOtherData();
        if (otherData != null && otherData.get("mustLogin") != null) {
            try {
                int mustLoginCode = (int) otherData.get("mustLogin");
                if (mustLoginCode == 1) {
                    getUserId();
                } else {
                    transmitUid();
                }
            } catch (Exception e) {
                e.printStackTrace();
                getUserId();
            }
        } else {
            getUserId();
        }
    }

    /**
     * ????????????id ???????????????????????????
     */
    private void getUserId() {
        if (userId > 0) {
            transmitUid();
        } else {
            getLoginStatus(this);
        }
    }

    /**
     * js????????????
     *
     * @param jsInteractiveBean
     */
    private void jsRefreshStatus(JsInteractiveBean jsInteractiveBean) {
        Map<String, Object> otherData = jsInteractiveBean.getOtherData();
        this.refreshStatus = String.valueOf(1);
        if (otherData != null && otherData.get("refreshCode") != null) {
            try {
                int refreshCode = (int) otherData.get("refreshCode");
                this.refreshStatus = String.valueOf(refreshCode);
            } catch (Exception e) {
                this.refreshStatus = String.valueOf(1);
                e.printStackTrace();
            }
        }
        setWebRefreshStatus(Integer.parseInt(refreshStatus));
    }

    /**
     * js??????????????????
     */
    private void jsInteractiveException(String errorType) {
        showToast("??????????????????"
                + (!TextUtils.isEmpty(errorType) && errorType.contains("web") ? "w" : "a") + "??????????????????????????????~");
    }

    /**
     * ????????????????????????????????????
     *
     * @param jsInteractiveBean
     */
    private void jsInteractiveEmpty(JsInteractiveBean jsInteractiveBean) {
        if (alertDialogHelper == null) {
            alertDialogHelper = new AlertDialogHelper(getActivity());
            alertDialogHelper.setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                @Override
                public void confirm() {
                    /***** ???????????? *****/
                    AppUpdateUtils.getInstance().getAppUpdate(getActivity(), true);
                }

                @Override
                public void cancel() {
                }
            });
        }
        String title = "????????????";
        String content = getResources().getString(R.string.skip_empty_page_hint);
        if (jsInteractiveBean != null
                && jsInteractiveBean.getOtherData() != null) {
            Map<String, Object> otherData = jsInteractiveBean.getOtherData();
            title = (String) getMapValue(otherData.get("alertTitle"), "");
            content = (String) getMapValue(otherData.get("alertContent"), content);
        }
        alertDialogHelper.setTitle(title)
                .setTitleVisibility(!TextUtils.isEmpty(title) ? View.VISIBLE : View.GONE)
                .setTitleGravity(Gravity.CENTER)
                .setMsg(content)
                .setSingleButton(true)
                .setConfirmText("??????");
        AutoSize.autoConvertDensityOfGlobal(getActivity());
        alertDialogHelper.show();
    }

    /**
     * ??????uid???web
     */
    private void transmitUid() {
        webViewJs(String.format(getResources().getString(R.string.web_uid_method), userId));
    }

    @Override
    protected void getReqParams(Bundle bundle) {
        webUrl = bundle.getString("loadUrl");
        paddingStatus = bundle.getString("paddingStatus", "false");
    }


    /**
     * ???????????????????????????
     *
     * @param backCode
     */
    private void addWebReminderCallback(String productId, int backCode) {
        webViewJs(String.format(getResources().getString(R.string.web_add_reminder), getStrings(productId), backCode));
    }

    /**
     * ??????????????????
     */
    private void notificationStatusCallback() {
        if (activityWeakReference.get() == null) {
            return;
        }
        webViewJs(String.format(getResources().getString(R.string.web_notification_status), getDeviceAppNotificationStatus() ? 1 : 0));
    }

    /**
     * @param jsUrl
     */
    private void webViewJs(@NonNull String jsUrl) {
        web_fragment_communal.post(new Runnable() {
            @Override
            public void run() {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        web_fragment_communal.evaluateJavascript(jsUrl, null);
                    } else {
                        web_fragment_communal.loadUrl(jsUrl);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (web_fragment_communal != null) {
            web_fragment_communal.removeAllViews();
            web_fragment_communal.destroy();
        }
    }

    /**
     * ????????????????????? ????????????????????????
     *
     * @return
     */
    public boolean goBack() {
        return finishWebPage(1);
    }

    /**
     * ??????web??????
     *
     * @param finishCount
     */
    private boolean finishWebPage(int finishCount) {
        finishCount = Math.abs(finishCount);
        int finalFinishCount = finishCount;
        WebBackForwardList webBackForwardList = web_fragment_communal.copyBackForwardList();
//        ????????????index
        int currentIndex = webBackForwardList.getCurrentIndex();
        boolean isCanGoBack = web_fragment_communal.canGoBack();
        if (isCanGoBack) {
            if (currentIndex - finalFinishCount < 0) {
                web_fragment_communal.goBackOrForward(-currentIndex);
            } else {
                web_fragment_communal.goBackOrForward(-finalFinishCount);
            }
            return true;
        }
        return false;
    }

    @OnClick(R.id.tv_communal_net_refresh)
    void clickError() {
        rel_communal_net_error.setVisibility(View.GONE);
        web_fragment_communal.reload();
    }

    @Override
    public void initImmersionBar() {
        if (immersionBar != null) {
            immersionBar.init();
        } else {
//            ????????????????????????????????????????????????0
            setWebFragmentStatusBar(1, "#ffffff", 0);
        }
    }

    @Override
    protected boolean isLazy() {
        return "false".equals(paddingStatus);
    }
}
