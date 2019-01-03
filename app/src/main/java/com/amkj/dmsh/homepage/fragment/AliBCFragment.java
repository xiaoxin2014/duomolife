package com.amkj.dmsh.homepage.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
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
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.page.AlibcBasePage;
import com.alibaba.baichuan.android.trade.page.AlibcDetailPage;
import com.alibaba.baichuan.android.trade.page.AlibcPage;
import com.alibaba.baichuan.trade.biz.AlibcConstants;
import com.alibaba.baichuan.trade.biz.context.AlibcTradeResult;
import com.alibaba.baichuan.trade.biz.login.AlibcLogin;
import com.alibaba.baichuan.trade.biz.login.AlibcLoginCallback;
import com.alibaba.fastjson.JSON;
import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.constant.AppUpdateUtils;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.homepage.bean.JsInteractiveBean;
import com.amkj.dmsh.qyservice.QyServiceUtils;
import com.amkj.dmsh.utils.ImgUrlHelp;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.alertdialog.AlertDialogHelper;
import com.amkj.dmsh.utils.pictureselector.PictureSelectorUtils;
import com.amkj.dmsh.views.HtmlWebView;
import com.gyf.barlibrary.ImmersionBar;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfigC;
import com.luck.picture.lib.entity.LocalMediaC;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.umeng.socialize.UMShareAPI;
import com.yanzhenjie.permission.Permission;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.AutoSize;

import static android.app.Activity.RESULT_OK;
import static com.amkj.dmsh.constant.ConstantMethod.getDeviceId;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getMapValue;
import static com.amkj.dmsh.constant.ConstantMethod.getOnlyUrlParams;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getVersionName;
import static com.amkj.dmsh.constant.ConstantMethod.setSkipPath;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.WEB_JD_SCHEME;
import static com.amkj.dmsh.constant.ConstantVariable.WEB_TAOBAO_SCHEME;
import static com.amkj.dmsh.constant.ConstantVariable.WEB_TB_SCHEME;
import static com.amkj.dmsh.constant.ConstantVariable.WEB_TMALL_SCHEME;
import static com.luck.picture.lib.config.PictureConfigC.CHOOSE_REQUEST;

;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/10/28
 * class description:阿里百川
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
    @BindView(R.id.ll_communal_net_error)
    LinearLayout ll_communal_net_error;
    @BindView(R.id.smart_web_fragment_refresh)
    SmartRefreshLayout smart_web_fragment_refresh;
    private String webUrl;
    private String shareData;
    private String paddingStatus;
    private String jsIdentifying;
    private String refreshStatus;
    private AlertDialogHelper alertDialogHelper;
    private boolean isCanEditStatusBar = false;
    private ImmersionBar immersionBar;

    @Override
    protected int getContentView() {
        return R.layout.fragment_web_ali_bc_activity;
    }

    @Override
    protected void initViews() {
        tv_header_shared.setVisibility(View.GONE);
        tl_normal_bar.setVisibility(View.GONE);
        ll_communal_net_error.setVisibility(View.GONE);
        ll_web_ali.setBackgroundColor(getResources().getColor(R.color.transparent));
        if (TextUtils.isEmpty(webUrl)) {
            return;
        }
//        自适应屏幕大小
        web_fragment_communal.getSettings().setUseWideViewPort(true);
        web_fragment_communal.getSettings().setLoadWithOverviewMode(true);
        WebSettings webSettings = web_fragment_communal.getSettings();
        //        自适应屏幕大小
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(false);
        //设置支持H5 DomStorage
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setUseWideViewPort(true); // 关键点
        webSettings.setSupportZoom(true); // 支持缩放
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 不加载缓存内容

        web_fragment_communal.setWebChromeClient(new MyWebChromeClient());
        //加载需要显示的网页
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
//        js交互
        web_fragment_communal.addJavascriptInterface(new JsData(getActivity()), "JsToAndroid");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        //设置Web视图
        web_fragment_communal.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                // 断网或者网络连接超时
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

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                int errorCode = errorResponse.getStatusCode();
                if (404 == errorCode || 500 == errorCode || errorCode == -2) {
                    setErrorException(view);
                }
            }

            @Override
            public void onPageFinished(final WebView view, final String url) {
//                    是否显示顶部导航栏
                if (RefreshState.Refreshing.equals(smart_web_fragment_refresh.getState())) {
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
                } catch (Exception e) {//防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                }
//                因返回true 会导致重定向而无法返回问题 todo
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
                } catch (Exception e) {//防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                }
//                因返回true 会导致重定向而无法返回问题 todo
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
        smart_web_fragment_refresh.setOnRefreshListener(refreshLayout -> web_fragment_communal.reload());
        if (paddingStatus.contains("true")) {
            isCanEditStatusBar = true;
        }
    }

    private void setErrorException(WebView view) {
        ll_communal_net_error.setVisibility(View.VISIBLE);
    }

    private void setWebRefreshStatus(int refreshStatus) {
        if (!TextUtils.isEmpty(this.refreshStatus)) {
            smart_web_fragment_refresh.setEnableRefresh(this.refreshStatus.contains("1"));
        } else {
            smart_web_fragment_refresh.setEnableRefresh(refreshStatus == 1);
        }
    }

    private String getRandomString(int length) { //length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    //改写物理按键——返回的逻辑

    @Override
    protected void loadData() {
        web_fragment_communal.loadUrl(webUrl);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            if (requestCode == IS_LOGIN_CODE) {
                transmitUid();
                return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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
     * 选择图片上传
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

    //    自定义系统原生弹框
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
         * 覆盖默认的window.alert展示界面，避免title里显示为“：来自file:////”
         */
        @Override
        public boolean onJsAlert(WebView view, String url, String message,
                                 JsResult result) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            TextView titleView = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.layout_dialog_title_textview, null);
            titleView.setText("提示");
            builder.setCustomTitle(titleView)
                    .setMessage(message)
                    .setPositiveButton("确定", null);

            // 不需要绑定按键事件
            // 屏蔽keycode等于84之类的按键
            builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    return true;
                }
            });
            // 禁止响应按back键的事件
            builder.setCancelable(false);
            AlertDialog dialog = builder.create();
            dialog.show();
            result.confirm();// 因为没有绑定事件，需要强行confirm,否则页面会变黑显示不了内容。
            return true;
            // return super.onJsAlert(view, url, message, result);
        }

        @Override
        public boolean onJsBeforeUnload(WebView view, String url,
                                        String message, JsResult result) {
            return super.onJsBeforeUnload(view, url, message, result);
        }

        /**
         * 覆盖默认的window.confirm展示界面，避免title里显示为“：来自file:////”
         */
        @Override
        public boolean onJsConfirm(WebView view, String url, String message,
                                   final JsResult result) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            TextView titleView = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.layout_dialog_title_textview, null);
            titleView.setText("提示");
            builder.setCustomTitle(titleView)
                    .setMessage(message)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            result.confirm();
                        }
                    })
                    .setNeutralButton("取消", new DialogInterface.OnClickListener() {
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

            // 屏蔽keycode等于84之类的按键，避免按键后导致对话框消息而页面无法再弹出对话框的问题
            builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                    Log.v("onJsConfirm", "keyCode==" + keyCode + "event=" + event);
                    return true;
                }
            });
            // 禁止响应按back键的事件
            builder.setCancelable(false);
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
            // return super.onJsConfirm(view, url, message, result);
        }

        /**
         * 覆盖默认的window.prompt展示界面，避免title里显示为“：来自file:////”
         * window.prompt('请输入您的域名地址', '618119.com');
         */
        public boolean onJsPrompt(WebView view, String url, String message,
                                  String defaultValue, final JsPromptResult result) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            TextView titleView = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.layout_dialog_title_textview, null);
            titleView.setText("提示");
            builder.setCustomTitle(titleView).setMessage(message);
            final EditText et = new EditText(view.getContext());
            et.setSingleLine();
            et.setText(defaultValue);
            builder.setView(et)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            result.confirm(et.getText().toString());
                        }
                    })
                    .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            result.cancel();
                        }
                    });

            // 屏蔽keycode等于84之类的按键，避免按键后导致对话框消息而页面无法再弹出对话框的问题
            builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                    Log.v("onJsPrompt", "keyCode==" + keyCode + "event=" + event);
                    return true;
                }
            });

            // 禁止响应按back键的事件
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
            if (newProgress == 100) {
                if (RefreshState.Refreshing.equals(smart_web_fragment_refresh.getState())) {
                    smart_web_fragment_refresh.finishRefresh();
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

        //        跳转页面
        @JavascriptInterface
        public void skipPage(String result) {
            setSkipPath(context, result, false);
        }

        //        获取当前用户uid
        @JavascriptInterface
        public void getUserIdFromAndroid() {
            getUserId();
        }

        //      跳转阿里百川
        @JavascriptInterface
        public void skipAlibc(String urlType) {
            if (!TextUtils.isEmpty(urlType)) {
                Map<String, String> urlParams = getOnlyUrlParams(urlType);
                String url = urlParams.get("url");
                String thirdId = urlParams.get("thirdId");
                if (!TextUtils.isEmpty(url) || !TextUtils.isEmpty(thirdId)) {
                    skipAliBCWebView(url, thirdId);
                } else {
                    showToast(context, R.string.unConnectedNetwork);
                }
            } else {
                showToast(context, R.string.unConnectedNetwork);
            }
        }

        //      跳转客服
        @JavascriptInterface
        public void skipService() {
            QyServiceUtils qyServiceUtils = QyServiceUtils.getQyInstance();
            qyServiceUtils.openQyServiceChat(getActivity(), "web", "");
        }

        @JavascriptInterface
        public void sharePage(String result) {
            if (!TextUtils.isEmpty(result)) {
                Message message = handler.obtainMessage();
                message.obj = result;
                handler.sendMessage(message);
            } else {
                showToast(context, "数据为空");
            }
        }

        /**
         * 打开相册
         *
         * @param jsIdentifying js标识
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
         * v 3.1.8 后提供公用方法交互 避免版本控制
         *
         * @param resultJson json 数据
         */
        /**
         * v 3.1.8 后提供公用方法交互 避免版本控制
         *
         * @param resultJson json 数据
         */
        @JavascriptInterface
        public void androidJsInteractive(String resultJson) {
            try {
                if (TextUtils.isEmpty(resultJson)) {
                    jsInteractiveException("web");
                }
                JsInteractiveBean jsInteractiveBean = JSON.parseObject(resultJson, JsInteractiveBean.class);
                if (jsInteractiveBean != null && !TextUtils.isEmpty(jsInteractiveBean.getType())) {
                    switch (jsInteractiveBean.getType()) {
//                        获取用户Id
                        case "userId":
                            jsGetUserId(jsInteractiveBean);
                            break;
//                            刷新设置
                        case "refresh":
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    jsRefreshStatus(jsInteractiveBean);
                                }
                            });
                            break;
//                            导航栏设置
                        case "navigationBar":
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    jsSetNavBar(jsInteractiveBean);
                                }
                            });
                            break;
//                            获取app设备信息
                        case "appDeviceInfo":
                            jsGetAppDeviceInfo();
                            break;
//                            app更新弹窗
                        case "appUpdate":
                            jsInteractiveEmpty(jsInteractiveBean);
                            break;
                        case "finishWebPage":
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    jsAutoFinishPage(jsInteractiveBean);
                                }
                            });
                            break;
                        case "alibcUrl":
                            jsSkipTaoBao(jsInteractiveBean);
                            break;
                        case "statusBar":
                            if (isCanEditStatusBar) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        jsSetStatusBar(jsInteractiveBean);
                                    }
                                });
                            }
                            break;
                        default:
                            jsInteractiveEmpty(null);
                            break;
                    }
                } else {
                    jsInteractiveException("web");
                }
            } catch (Exception e) {
                jsInteractiveException(null);
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置状态栏
     *
     * @param jsInteractiveBean
     */
    private void jsSetStatusBar(JsInteractiveBean jsInteractiveBean) {
        Map<String, Object> otherData = jsInteractiveBean.getOtherData();
        if (otherData != null) {
            //            背景颜色
            String statusBarBgColor = (String) getMapValue(otherData.get("statusBarBgColor"), "ffffff");
//            背景色透明度
            String statusBarBgAlpha = (String) getMapValue(otherData.get("statusBarBgAlpha"), "0");
//            状态栏字体颜色
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
     * js跳转阿里百川
     *
     * @param jsInteractiveBean
     */
    private void jsSkipTaoBao(JsInteractiveBean jsInteractiveBean) {
        Map<String, Object> otherData = jsInteractiveBean.getOtherData();
        if (otherData != null) {
            String thirdId = (String) getMapValue(otherData.get("tbThirdId"), "");
            if (TextUtils.isEmpty(thirdId)) {
                String tbUrl = (String) getMapValue(otherData.get("tbUrl"), "");
                if (!TextUtils.isEmpty(tbUrl)) {
                    skipAliBCWebView(tbUrl, null);
                }
            } else {
                skipAliBCWebView(null, thirdId);
            }
        }
    }

    /**
     * js关闭自带页面
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
     * js获取app信息
     */
    private void jsGetAppDeviceInfo() {
        try {
//        app版本号
            String versionName = getVersionName(getActivity());
//        系统版本号
            String osVersion = Build.VERSION.RELEASE;
//        手机型号
            String mobileModel = Build.MODEL;
//        android设备号
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
     * js设置导航栏
     *
     * @param jsInteractiveBean
     */
    private void jsSetNavBar(JsInteractiveBean jsInteractiveBean) {
        Map<String, Object> otherData = jsInteractiveBean.getOtherData();
        if (otherData != null && otherData.get("navBarVisibility") != null) {
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
     * 设置导航栏默认设置
     */
    private void setDefaultNavBar(int navBarVisibility) {
        tv_header_title.setText("");
        tv_header_shared.setVisibility(View.GONE);
        tl_normal_bar.setVisibility(navBarVisibility);
    }

    /**
     * js获取用户id
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
     * 获取用户id 用户未登录提示登录
     */
    private void getUserId() {
        if (userId > 0) {
            transmitUid();
        } else {
            getLoginStatus(this);
        }
    }

    /**
     * js刷新状态
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
     * js交互数据异常
     */
    private void jsInteractiveException(String errorType) {
        showToast(getActivity(), "数据异常呦，"
                + (!TextUtils.isEmpty(errorType) && errorType.contains("web") ? "w" : "a") + "攻城狮正在加急处理呢~");
    }

    /**
     * 方法不支持，弹窗更新版本
     *
     * @param jsInteractiveBean
     */
    private void jsInteractiveEmpty(JsInteractiveBean jsInteractiveBean) {
        if (alertDialogHelper == null) {
            alertDialogHelper = new AlertDialogHelper(getActivity());
            alertDialogHelper.setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                @Override
                public void confirm() {
                    /***** 检查更新 *****/
                    AppUpdateUtils.getInstance().getAppUpdate(getActivity(), true);
                }

                @Override
                public void cancel() {
                    alertDialogHelper.dismiss();
                }
            });
        }
        String title = "通知提示";
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
                .setConfirmText("更新");
        AutoSize.autoConvertDensityOfGlobal(getActivity());
        alertDialogHelper.show();
    }

    private void transmitUid() {
        webViewJs(String.format(getResources().getString(R.string.web_uid_method), userId));
    }

    @Override
    protected void getReqParams(Bundle bundle) {
        webUrl = bundle.getString("loadUrl");
        paddingStatus = bundle.getString("paddingStatus", "false");
    }

    public void skipAliBCWebView(final String url, final String thirdId) {
        if (!TextUtils.isEmpty(url) || !TextUtils.isEmpty(thirdId)) {
            if (userId != 0) {
                skipNewTaoBao(url, thirdId);
            } else {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                getLoginStatus(AliBCFragment.this);
            }
        } else {
            showToast(getActivity(), "地址缺失");
            if (loadHud != null) {
                loadHud.dismiss();
            }
        }
    }

    private void skipNewTaoBao(final String url, final String thirdId) {
        AlibcLogin alibcLogin = AlibcLogin.getInstance();
        alibcLogin.showLogin(new AlibcLoginCallback() {
            @Override
            public void onSuccess(int i) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                skipNewShopDetails(url, thirdId);
            }

            @Override
            public void onFailure(int code, String msg) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                showToast(getActivity(), "登录失败 ");
            }
        });
    }

    private void skipNewShopDetails(String url, String thirdId) {
        //提供给三方传递配置参数
        Map<String, String> exParams = new HashMap<>();
        exParams.put(AlibcConstants.ISV_CODE, "appisvcode");
        //设置页面打开方式
        AlibcShowParams showParams = new AlibcShowParams(OpenType.Native, false);
        //实例化商品详情 itemID打开page
        AlibcBasePage ordersPage;
        if (!TextUtils.isEmpty(url)) {
            ordersPage = new AlibcPage(url.trim());
        } else {
            ordersPage = new AlibcDetailPage(thirdId.trim());
        }
        AlibcTrade.show(getActivity(), ordersPage, showParams, null, exParams, new AlibcTradeCallback() {
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

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finishWebPage(1);
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            shareData = (String) msg.obj;
            if (shareData != null && !TextUtils.isEmpty(shareData)) {
                setShareData(shareData);
            }
            return false;
        }
    });

    private void setShareData(String shareData) {
        try {
            JSONObject jsonObject = new JSONObject(shareData);
            String title = jsonObject.getString("title");
            String imageUrl = jsonObject.getString("imageUrl");
            String content = jsonObject.getString("content");
            String url = jsonObject.getString("url");
            String routineUrl = null;
            try {
                routineUrl = jsonObject.getString("routineUrl");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            new UMShareAction(getActivity()
                    , imageUrl
                    , TextUtils.isEmpty(title) ? "多么生活" : title
                    , TextUtils.isEmpty(content) ? "" : content
                    , url, routineUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.tv_header_shared)
    void shareData(View view) {
        webViewJs(getResources().getString(R.string.web_share_getData_method));
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
        if (handler != null) {
            handler.removeCallbacksAndMessages(this);
        }
        if (web_fragment_communal != null) {
            web_fragment_communal.removeAllViews();
            web_fragment_communal.destroy();
        }
        if (alertDialogHelper != null) {
            alertDialogHelper.dismiss();
        }
        super.onDestroy();
    }

    /**
     * 此方法不能删除 主页面返回键调用
     *
     * @return
     */
    public boolean goBack() {
        return finishWebPage(1);
    }

    /**
     * 关闭web界面
     *
     * @param finishCount
     */
    private boolean finishWebPage(int finishCount) {
        finishCount = Math.abs(finishCount);
        int finalFinishCount = finishCount;
        WebBackForwardList webBackForwardList = web_fragment_communal.copyBackForwardList();
//        当前页面index
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
        ll_communal_net_error.setVisibility(View.GONE);
        web_fragment_communal.reload();
    }

    @Override
    public void initImmersionBar() {
        if (immersionBar != null) {
            immersionBar.init();
        } else {
//            默认黑色字体，白色背景，透明度为0
            setWebFragmentStatusBar(1, "#ffffff", 0);
        }
    }
}
