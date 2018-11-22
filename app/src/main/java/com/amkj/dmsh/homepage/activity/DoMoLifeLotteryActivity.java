package com.amkj.dmsh.homepage.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
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
import android.widget.Toast;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.qyservice.QyServiceUtils;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.amkj.dmsh.views.HtmlWebView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.umeng.socialize.UMShareAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.isWebLinkUrl;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.WEB_BLACK_PAGE;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/3/16
 * class description:抽奖页面
 */
public class DoMoLifeLotteryActivity extends BaseActivity {
    public static final String LOTTERY_URL = "http://www.domolife.cn/m/template/home/lottery.html";
    @BindView(R.id.web_communal)
    HtmlWebView web_communal;
    @BindView(R.id.tv_web_title)
    TextView tv_web_title;
    @BindView(R.id.tv_web_shared)
    TextView tv_web_shared;
    @BindView(R.id.ll_communal_net_error)
    LinearLayout ll_communal_net_error;
    @BindView(R.id.smart_web_refresh)
    SmartRefreshLayout smart_web_refresh;
    private String turnId;
    private String errorUrl;

    @Override
    protected int getContentView() {
        return R.layout.activity_communal_webview;
    }

    @Override
    protected void initViews() {
        getLoginStatus(DoMoLifeLotteryActivity.this);
        if(userId>0){
            web_communal.loadUrl(LOTTERY_URL + "?uid=" + userId);
        }
        tv_web_shared.setVisibility(View.GONE);
        ll_communal_net_error.setVisibility(View.GONE);
        tv_web_title.setText("抽奖");
        WebSettings webSettings = web_communal.getSettings();
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webSettings.setUserAgentString(web_communal.getSettings().getUserAgentString() + " domolifeandroid" + getRandomString(501));
        web_communal.setWebChromeClient(new DoMoLifeLotteryActivity.MyWebChromeClient());
        //加载需要显示的网页
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
//        js交互
        web_communal.addJavascriptInterface(new JsData(DoMoLifeLotteryActivity.this), "JsToAndroid");
        if (userId > 0) {
            web_communal.loadUrl(LOTTERY_URL + "?uid=" + userId);
        }
        //设置Web视图
        web_communal.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                try{
                    if(!url.startsWith("http://") && !url.startsWith("https://")){
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                        return true;
                    }
                }catch (Exception e){//防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                    return true;//没有安装该app时，返回true，表示拦截自定义链接，但不跳转，避免弹出上面的错误页面
                }
                view.loadUrl(url);
                return true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                try{
                    if(!url.startsWith("http://") && !url.startsWith("https://")){
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                        return true;
                    }
                }catch (Exception e){//防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                    return true;//没有安装该app时，返回true，表示拦截自定义链接，但不跳转，避免弹出上面的错误页面
                }
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                // 断网或者网络连接超时
                if (errorCode == ERROR_HOST_LOOKUP || errorCode == ERROR_CONNECT || errorCode == ERROR_TIMEOUT) {
                    errorUrl = failingUrl;
                    setErrorException(view);
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                int errorCode = error.getErrorCode();
                if (404 == errorCode || 500 == errorCode||errorCode == -2) {
                    errorUrl = request.getUrl().toString();
                    setErrorException(view);
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                int errorCode = errorResponse.getStatusCode();
                if (404 == errorCode || 500 == errorCode||errorCode == -2) {
                    errorUrl = request.getUrl().toString();
                    setErrorException(view);
                }
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
        smart_web_refresh.setEnableRefresh(false);
    }

    private void setErrorException(WebView view) {
        view.loadUrl(WEB_BLACK_PAGE);// 避免出现默认的错误界面
        ll_communal_net_error.setVisibility(View.VISIBLE);
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (web_communal.canGoBack()) {
                web_communal.goBack();//返回上一页面
                return true;
            } else {
                finish();
//                System.exit(0);//退出程序
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void loadData() {
    }

    private void getLotteryTime() {
        if(userId<1){
            return;
        }
        final String url = Url.BASE_URL + Url.H_HOT_ACTIVITY_ADD_LOTTERY;
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("turn_id", TextUtils.isEmpty(turnId) ? "3" : turnId);
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                String code = "";
                String msg = "";
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    code = (String) jsonObject.get("code");
                    msg = (String) jsonObject.get("msg");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (code.equals("01")) {
//                    刷新抽奖次数
                    web_communal.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                web_communal.loadUrl("javascript:refreshdml()");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else if (!code.equals("02")) {
                    Toast.makeText(DoMoLifeLotteryActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(DoMoLifeLotteryActivity.this, R.string.unConnectedNetwork, Toast.LENGTH_SHORT).show();
                super.onError(ex, isOnCallback);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            if (requestCode == IS_LOGIN_CODE) {
                finish();
            } else {
                return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IS_LOGIN_CODE) {
                web_communal.loadUrl(LOTTERY_URL + "?uid=" + userId);
            }
        }
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.tv_web_back)
    void finish(View view) {
        if (web_communal.canGoBack()) {
            web_communal.goBack();//返回上一页面
        } else {
            finish();
        }
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
        public boolean onJsAlert(WebView view, String url, String message,
                                 JsResult result) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            TextView titleView = (TextView) LayoutInflater.from(DoMoLifeLotteryActivity.this).inflate(R.layout.layout_dialog_title_textview, null);
            titleView.setText("提示");
            builder.setCustomTitle(titleView)
                    .setMessage(message)
                    .setPositiveButton("确定", null);
            // 不需要绑定按键事件
            // 屏蔽keycode等于84之类的按键
            builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
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

        public boolean onJsBeforeUnload(WebView view, String url,
                                        String message, JsResult result) {
            return super.onJsBeforeUnload(view, url, message, result);
        }

        /**
         * 覆盖默认的window.confirm展示界面，避免title里显示为“：来自file:////”
         */
        public boolean onJsConfirm(WebView view, String url, String message,
                                   final JsResult result) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            TextView titleView = (TextView) LayoutInflater.from(DoMoLifeLotteryActivity.this).inflate(R.layout.layout_dialog_title_textview, null);
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
            TextView titleView = (TextView) LayoutInflater.from(DoMoLifeLotteryActivity.this).inflate(R.layout.layout_dialog_title_textview, null);
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
            ConstantMethod.setSkipPath(context, result, false);
        }

        //分享
        @JavascriptInterface
        public void sharePage(String result) {
            if (!TextUtils.isEmpty(result)) {
                Message message = handler.obtainMessage();
                message.obj = result;
                handler.sendMessage(message);
            } else {
                Toast.makeText(context, "数据为空", Toast.LENGTH_SHORT).show();
            }
        }

        //      跳转客服
        @JavascriptInterface
        public void skipService() {
            QyServiceUtils qyServiceUtils = QyServiceUtils.getQyInstance();
            qyServiceUtils.openQyServiceChat(DoMoLifeLotteryActivity.this, "web：","");
        }
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    UMShareAction.OnShareSuccessListener shareSuccessListener = new UMShareAction.OnShareSuccessListener() {
        @Override
        public void onShareSuccess() {
            getLotteryTime();
        }
    };
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            String shareData = (String) msg.obj;
            try {
                JSONObject jsonObject = new JSONObject(shareData);
                String title = jsonObject.getString("title");
                String imageUrl = jsonObject.getString("imageUrl");
                String content = jsonObject.getString("content");
                String url = jsonObject.getString("url");
                turnId = jsonObject.getString("turnId");
                UMShareAction umShareAction = new UMShareAction(DoMoLifeLotteryActivity.this
                        , imageUrl
                        , TextUtils.isEmpty(title) ? "多么生活" : title
                        , TextUtils.isEmpty(content) ? "抽奖活动" : content
                        , url);
                umShareAction.setOnShareSuccessListener(shareSuccessListener);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }
    });

    @OnClick(R.id.tv_communal_net_refresh)
    void clickError(){
        ll_communal_net_error.setVisibility(View.GONE);
        if(isWebLinkUrl(errorUrl)){
            web_communal.loadUrl(errorUrl);
        }else{
            web_communal.loadUrl(LOTTERY_URL);
        }
    }
}
