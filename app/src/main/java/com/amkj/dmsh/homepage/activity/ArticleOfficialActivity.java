package com.amkj.dmsh.homepage.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.H5ShareBean;
import com.amkj.dmsh.constant.AppUpdateUtils;
import com.amkj.dmsh.bean.BaseAddCarProInfoBean;
import com.amkj.dmsh.bean.CommunalComment;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dao.CommentDao;
import com.amkj.dmsh.dao.SoftApiDao;
import com.amkj.dmsh.dominant.bean.DmlSearchCommentEntity.DmlSearchCommentBean;
import com.amkj.dmsh.homepage.bean.JsInteractiveBean;
import com.amkj.dmsh.utils.CommonUtils;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.SharedPreUtils;
import com.amkj.dmsh.views.alertdialog.AlertDialogHelper;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.views.HtmlWebView;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.tencent.bugly.beta.tinker.TinkerManager;
import com.umeng.socialize.UMShareAPI;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.emoji.widget.EmojiEditText;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTouch;
import me.jessyan.autosize.AutoSize;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getIntegralFormat;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getMapValue;
import static com.amkj.dmsh.constant.ConstantMethod.getStringChangeIntegers;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsFormat;
import static com.amkj.dmsh.constant.ConstantMethod.isContextExisted;
import static com.amkj.dmsh.constant.ConstantMethod.isWebLinkUrl;
import static com.amkj.dmsh.constant.ConstantMethod.setSkipPath;
import static com.amkj.dmsh.constant.ConstantMethod.showImportantToast;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.COMMENT_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.ERROR_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOKEN;
import static com.amkj.dmsh.constant.ConstantVariable.WEB_JD_SCHEME;
import static com.amkj.dmsh.constant.ConstantVariable.WEB_TAOBAO_SCHEME;
import static com.amkj.dmsh.constant.ConstantVariable.WEB_TB_SCHEME;
import static com.amkj.dmsh.constant.ConstantVariable.WEB_TMALL_SCHEME;
import static com.amkj.dmsh.constant.ConstantVariable.isDebugTag;
import static com.amkj.dmsh.dao.BaiChuanDao.skipAliBC;
import static com.amkj.dmsh.dao.OrderDao.addShopCarGetSku;
import static com.amkj.dmsh.find.activity.ImagePagerActivity.IMAGE_DEF;
import static com.amkj.dmsh.rxeasyhttp.interceptor.MyInterceptor.getCommonApiParameter;

/**
 * Created by atd48 on 2016/6/30.
 */
public class ArticleOfficialActivity extends BaseActivity {
    @BindView(R.id.web_communal)
    HtmlWebView web_communal;
    @BindView(R.id.emoji_edit_comment)
    EmojiEditText mEmojiEditComment;
    @BindView(R.id.tv_send_comment)
    TextView mTvSendComment;
    @BindView(R.id.ll_input_comment)
    LinearLayout mLlInputComment;
    @BindView(R.id.tv_publish_comment)
    TextView mTvPublishComment;
    @BindView(R.id.tv_article_bottom_like)
    TextView mTvArticleBottomLike;
    @BindView(R.id.tv_article_bottom_collect)
    TextView mTvArticleBottomCollect;
    @BindView(R.id.ll_article_comment)
    LinearLayout mLlArticleComment;
    @BindView(R.id.rel_article_bottom)
    RelativeLayout mRelArticleBottom;
    @BindView(R.id.tv_net_load_error_back)
    TextView mTvNetLoadErrorBack;
    @BindView(R.id.iv_communal_pic)
    ImageView mIvCommunalPic;
    @BindView(R.id.tv_communal_net_tint)
    TextView mTvCommunalNetTint;
    @BindView(R.id.tv_communal_net_reason)
    TextView mTvCommunalNetReason;
    @BindView(R.id.tv_communal_net_refresh)
    TextView mTvCommunalNetRefresh;
    @BindView(R.id.rel_communal_net_error)
    RelativeLayout mRelCommunalNetError;
    @BindView(R.id.smart_web_refresh)
    SmartRefreshLayout smart_web_refresh;
    @BindView(R.id.download_btn_communal)
    FloatingActionButton floatingActionButton;
    @BindView(R.id.rl_toolbar2)
    RelativeLayout mRlToolbar2;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.rl_toolbar)
    LinearLayout mRlToolbar;


    private String artId;
    private String webUrl;
    private float locationY;
    private String errorUrl;
    private String refreshStatus;
    private AlertDialogHelper alertDialogHelper;
    private H5ShareBean mShareBean;


    @Override
    protected int getContentView() {
        return R.layout.activity_article_details;
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    @Override
    protected void initViews() {
        Intent intent = getIntent();
        artId = intent.getStringExtra("ArtId");
        webUrl = "https://www.domolife.cn/m/app/pages/study_detail_app.html?id=" + artId;
        if (isDebugTag) {
            SharedPreferences sharedPreferences = mAppContext.getSharedPreferences("selectedServer", MODE_PRIVATE);
            String baseUrl = sharedPreferences.getString("selectServerUrl", Url.getUrl(0));
            if (!"https://app.domolife.cn/".equals(baseUrl)) {
                webUrl = "http://test.domolife.cn/test/app/pages/study_detail_app.html?id=" + artId;
            }
        }

//        webUrl = "http://test.domolife.cn/test/template/activity/mutually.html";

        //记录埋点参数sourceId
        ConstantMethod.saveSourceId(getClass().getSimpleName(), String.valueOf(artId));
        WebSettings webSettings = web_communal.getSettings();
        //自适应屏幕大小
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
        //加载需要显示的网页
        if (NetWorkUtils.checkNet(this)) {
            webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        } else {
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webSettings.setUserAgentString(web_communal.getSettings().getUserAgentString() + " domolifeandroid" + getRandomString(501));
        //        js交互
        web_communal.addJavascriptInterface(new JsData(), "JsToAndroid");
        web_communal.loadUrl(webUrl);
        //设置Web视图
        web_communal.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                // 断网或者网络连接超时
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    errorUrl = failingUrl;
                    setErrorException();
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                //Android6.0新版的onReceiveError能接收到的错误更多,页中的任意一个资源获取不到（比如图片加载错误），
                // 都会回调这里，所以显示错误页之前判断是否是主要的网络请求错误
                if (request.isForMainFrame()) {
                    errorUrl = request.getUrl().toString();
                    setErrorException();
                }
            }

            @Override
            public void onPageFinished(final WebView view, final String url) {
                if (smart_web_refresh != null && RefreshState.Refreshing.equals(smart_web_refresh.getState())) {
                    smart_web_refresh.finishRefresh();
                }
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
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
//                因返回true 会导致重定向而无法返回问题
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

        web_communal.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Intent action = new Intent();
                action.setAction("android.intent.action.VIEW");
                action.setData(Uri.parse(url));
                startActivity(action);
            }
        });

        int screenHeight = ((TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike()).getScreenHeight();
        int bannerHeight = AutoSizeUtils.mm2px(this, 750);
        web_communal.setOnScrollChangedCallback((dx, dy) -> {
            if (dy < 2) {
                setWebRefreshStatus(1);
            } else {
                setWebRefreshStatus(0);
            }

            if (dy > screenHeight * 1.5) {
                if (floatingActionButton.getVisibility() == GONE) {
                    floatingActionButton.setVisibility(VISIBLE);
                    floatingActionButton.hide(false);
                }
                if (!floatingActionButton.isVisible()) {
                    floatingActionButton.show();
                }
            } else {
                if (floatingActionButton.isVisible()) {
                    floatingActionButton.hide();
                }
            }


            //设置标题栏
            float alpha = dy * 1.0f / bannerHeight * 1.0f;
            if (alpha >= 1) {
                mRlToolbar2.setAlpha(0);
                mRlToolbar.setAlpha(1);
            } else if (alpha > 0) {
                mRlToolbar2.setAlpha(alpha > 0.4f ? 0 : 1 - alpha);
                mRlToolbar.setAlpha(alpha < 0.4f ? 0 : alpha);
            } else {
                mRlToolbar2.setAlpha(1);
                mRlToolbar.setAlpha(0);
            }
        });

        floatingActionButton.setOnClickListener(v -> {
            floatingActionButton.hide();
            web_communal.scrollTo(0, 0);
        });
        smart_web_refresh.setOnRefreshListener(refreshLayout -> web_communal.reload());
    }

    private void setErrorException() {
        mRelCommunalNetError.setVisibility(View.VISIBLE);
    }

    /**
     * 设置刷新开启禁用 1 开启 其它禁用
     */
    private void setWebRefreshStatus(int refreshStatus) {
        if (!TextUtils.isEmpty(this.refreshStatus)) {
            smart_web_refresh.setEnableRefresh(this.refreshStatus.contains("1"));
        } else {
            smart_web_refresh.setEnableRefresh(refreshStatus == 1);
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

    //js交互数据异常
    private void jsInteractiveException() {
        showToast("数据异常呦，攻城狮正在加急处理呢~");
    }


    /**
     * 方法不支持，弹窗更新版本
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
        AutoSize.autoConvertDensityOfGlobal(this);
        alertDialogHelper.show();
    }

    //js调用android子线程中执行
    public class JsData {
        //跳转页面
        @JavascriptInterface
        public void skipPage(String result) {
            setSkipPath(getActivity(), result, false);
        }

        //分享
        @JavascriptInterface
        public void sharePage(String result) {
            runOnUiThread(() -> {
                if (!TextUtils.isEmpty(result)) {
                    H5ShareBean shareBean = GsonUtils.fromJson(result, H5ShareBean.class);
                    UMShareAction umShareAction = new UMShareAction(getActivity()
                            , ""
                            , shareBean.getTitle()
                            , shareBean.getDescription()
                            , shareBean.getUrl(), shareBean.getRoutineUrl(), shareBean.getObjId(), shareBean.getShareType(), shareBean.getPlatform());
                } else {
                    showToast("数据为空");
                }
            });
        }

        @JavascriptInterface
        public void androidJsInteractive(String resultJson) {
            try {
                if (TextUtils.isEmpty(resultJson)) {
                    jsInteractiveException();
                    return;
                }
                JsInteractiveBean jsInteractiveBean = JSON.parseObject(resultJson, JsInteractiveBean.class);
                if (jsInteractiveBean != null && !TextUtils.isEmpty(jsInteractiveBean.getType())) {
                    if (!isContextExisted(getActivity())) return;
                    runOnUiThread(() -> {
                        try {//在子线程中无法捕获主线程抛出的异常，所以这里再捕获一次
                            switch (jsInteractiveBean.getType()) {
                                case "showToast":
                                    showImportToast(jsInteractiveBean.getOtherData());
                                    break;
                                case "browseImage":
                                    browseImage(jsInteractiveBean.getOtherData());
                                    break;
                                case "initArticlePage":
                                    initArticlePage(jsInteractiveBean.getOtherData());
                                    break;
                                case "getHeaderFromApp":
                                    getHeaderFromApp(jsInteractiveBean.getOtherData());
                                    break;
                                case "addGoodsToCart":
                                    addGoodsToCart(jsInteractiveBean.getOtherData());
                                    break;
                                case "replyComment":
                                    replyComment(jsInteractiveBean.getOtherData());
                                    break;
                                case "setShareButton":
                                    setShareButton(jsInteractiveBean.getOtherData());
                                    break;
                                case "alibcUrl":
                                    jsSkipTaoBao(jsInteractiveBean.getOtherData());
                                    break;
                                case "navigationBar":
                                    jsSetNavBar(jsInteractiveBean.getOtherData());
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
                    jsInteractiveException();
                }
            } catch (Exception e) {
                jsInteractiveException();
                e.printStackTrace();
            }
        }
    }

    /**
     * js设置导航栏
     */
    private void jsSetNavBar(Map<String, Object> otherData) {
        if (otherData != null && mTvTitle != null) {
            mTvTitle.setText(getStrings((String) otherData.get("navTitle")));
        }
    }


    //图片放大
    public void browseImage(Map<String, Object> map) {
        if (map != null) {
            String imageURL = (String) map.get("imageURL");
            if (!TextUtils.isEmpty(imageURL)) {
                List<String> imgList = new ArrayList<>();
                imgList.add(imageURL);
                ConstantMethod.showImageActivity(this, IMAGE_DEF, 0, imgList);
            }
        }
    }

    //弹窗
    public void showImportToast(Map<String, Object> map) {
        if (map != null) {
            String msg = (String) map.get("msg");
            if (!TextUtils.isEmpty(msg)) {
                showImportantToast(getActivity(), msg);
            }
        }
    }


    //初始化文章数据
    public void initArticlePage(Map<String, Object> map) {
        if (map != null && mTvArticleBottomCollect != null) {
            int isLike = (int) map.get("isLike");
            int isCollect = (int) map.get("isCollect");
            int likeCount = (int) map.get("likeCount");
            mTvArticleBottomCollect.setSelected(isCollect == 1);
            mTvArticleBottomLike.setSelected(isLike == 1);
            mTvArticleBottomLike.setText(likeCount > 0 ? String.valueOf(likeCount) : "赞");
        }
    }

    //获取Header头
    public void getHeaderFromApp(Map<String, Object> data) {
        Map<String, Object> map = getCommonApiParameter(getActivity());
        //登录情况下传uid和token
        if (userId > 0) {
            map.put("uid", ConstantMethod.userId);
            String token = (String) SharedPreUtils.getParam(TOKEN, "");
            map.put("token", token);
        }

        if (data != null && data.get("mustLogin") != null) {
            int mustLogin = ((int) data.get("mustLogin"));
            //强制登录并且未登录
            if (mustLogin == 1 && userId <= 0) {
                getLoginStatus(this);
            }
        }
//        String base64 = Base64.encodeToString(new JSONObject(map).toString().getBytes(), Base64.NO_WRAP);
        webViewJs(getStringsFormat(getActivity(), R.string.web_head_method, new JSONObject(map).toString(), userId == 0 ? "0" : String.valueOf(userId)));
    }

    //加入购物车
    public void addGoodsToCart(Map<String, Object> map) {
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
                addShopCarGetSku(getActivity(), baseAddCarProInfoBean, loadHud);
            } else {
                loadHud.dismiss();
                getLoginStatus(getActivity());
            }
        }
    }

    //回复评论
    public void replyComment(Map<String, Object> map) {
        if (map != null) {
            DmlSearchCommentBean dmlSearchCommentBean = new DmlSearchCommentBean();
            dmlSearchCommentBean.setIs_reply((int) map.get("isReply"));
            dmlSearchCommentBean.setUid(getStringChangeIntegers((String) map.get("replyUid")));
            dmlSearchCommentBean.setId(getStringChangeIntegers((String) map.get("pid")));
            dmlSearchCommentBean.setMainContentId(getStringChangeIntegers((String) map.get("mainId")));
            dmlSearchCommentBean.setObj_id(getStringChangeIntegers((String) map.get("objId")));
            dmlSearchCommentBean.setNickname((String) map.get("replyUserName"));
            if (userId > 0) {
                if (VISIBLE == mLlInputComment.getVisibility()) {
                    commentViewVisible(GONE, dmlSearchCommentBean);
                } else {
                    commentViewVisible(VISIBLE, dmlSearchCommentBean);
                }
            } else {
                getLoginStatus(getActivity());
            }
        }
    }

    //标题栏分享
    public void setShareButton(Map<String, Object> map) {
        String title = (String) map.get("objName");
        String imageUrl = (String) map.get("imageUrl");
        String content = (String) map.get("content");
        String url = (String) map.get("url");
        int objId = (int) map.get("objId");
        String routineUrl = (String) map.get("routineUrl");
        int shareType = (int) map.get("shareType");
        String platform = (String) map.get("platform");
        mShareBean = new H5ShareBean(title, imageUrl, content, url, routineUrl, String.valueOf(objId), shareType, platform);
    }

    /**
     * js跳转阿里百川
     */
    private void jsSkipTaoBao(Map<String, Object> otherData) {
        if (otherData != null) {
            String thirdId = (String) getMapValue(otherData.get("tbThirdId"), "");
            String tbUrl = (String) getMapValue(otherData.get("tbUrl"), "");
            skipAliBC(this, tbUrl, thirdId);
        }
    }


    /**
     * android 原生进行jS交互（调起Js方法）
     */
    private void webViewJs(@NonNull String jsUrl) {
        web_communal.post(new Runnable() {
            @Override
            public void run() {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        web_communal.evaluateJavascript(jsUrl, s -> {
                            Log.d(getSimpleName(), s);//如果调起成功，返回值s等于jsUrl
                        });
                    } else {
                        web_communal.loadUrl(jsUrl);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void loadData() {

    }

    //发送评论
    private void sendComment(CommunalComment communalComment) {
        loadHud.setCancellable(true);
        loadHud.show();
        mTvSendComment.setText("发送中…");
        mTvSendComment.setEnabled(false);
        CommentDao.setSendComment(this, communalComment, new CommentDao.OnSendCommentFinish() {
            @Override
            public void onSuccess() {
                loadHud.dismiss();
                mTvSendComment.setText("发送");
                mTvSendComment.setEnabled(true);
                showToast(R.string.comment_article_send_success);
                commentViewVisible(GONE, null);
                mEmojiEditComment.setText("");
                webViewJs(getStringsFormat(getActivity(), R.string.web_comment_success_method, SUCCESS_CODE, communalComment.getContent()));
            }

            @Override
            public void onError() {
                loadHud.dismiss();
                mTvSendComment.setText("发送");
                mTvSendComment.setEnabled(true);
                webViewJs(getStringsFormat(getActivity(), R.string.web_comment_success_method, ERROR_CODE, communalComment.getContent()));
            }
        });
    }


    private void commentViewVisible(int visibility, final DmlSearchCommentBean dmlSearchCommentBean) {
        mLlInputComment.setVisibility(visibility);
        mLlArticleComment.setVisibility(visibility == VISIBLE ? GONE : VISIBLE);
        if (VISIBLE == visibility) {
            mEmojiEditComment.requestFocus();
            //弹出键盘
            if (dmlSearchCommentBean != null) {
                mEmojiEditComment.setHint("回复" + dmlSearchCommentBean.getNickname() + ":");
            } else {
                mEmojiEditComment.setHint(getString(R.string.comment_article_hint));
            }
            CommonUtils.showSoftInput(mEmojiEditComment.getContext(), mEmojiEditComment);
            mTvSendComment.setOnClickListener((v) -> {
                //判断有内容调用接口
                String comment = mEmojiEditComment.getText().toString();
                if (!TextUtils.isEmpty(comment)) {
                    comment = mEmojiEditComment.getText().toString();
                    CommunalComment communalComment = new CommunalComment();
                    communalComment.setCommType(COMMENT_TYPE);
                    communalComment.setContent(comment);
                    if (dmlSearchCommentBean != null) {
                        communalComment.setIsReply(1);
                        communalComment.setReplyUserId(dmlSearchCommentBean.getUid());
                        communalComment.setPid(dmlSearchCommentBean.getId());
                        communalComment.setMainCommentId(dmlSearchCommentBean.getMainContentId() > 0
                                ? dmlSearchCommentBean.getMainContentId() : dmlSearchCommentBean.getId());
                    }
                    communalComment.setObjId(getStringChangeIntegers(artId));
                    communalComment.setUserId(userId);
//                    communalComment.setToUid(dmlSearchDetailBean.getUid());
                    sendComment(communalComment);
                } else {
                    showToast("请正确输入内容");
                }
            });
        } else if (GONE == visibility) {
            //隐藏键盘
            CommonUtils.hideSoftInput(mEmojiEditComment.getContext(), mEmojiEditComment);
        }
    }

    @OnTouch(R.id.web_communal)
    boolean onTouch(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                locationY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveY = event.getY() - locationY;//y轴距离
                if (Math.abs(moveY) > 250) {
                    commentViewVisible(GONE, null);
                }
                break;
            case MotionEvent.ACTION_UP:
                locationY = 0;
                break;
        }
        return false;
    }

    @OnClick({R.id.iv_life_back, R.id.iv_life_back2, R.id.iv_img_share, R.id.iv_img_share2, R.id.tv_send_comment, R.id.tv_article_bottom_like, R.id.tv_article_bottom_collect, R.id.smart_web_refresh, R.id.tv_publish_comment})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_life_back:
            case R.id.iv_life_back2:
                finish();
                break;
            case R.id.iv_img_share:
            case R.id.iv_img_share2:
                if (mShareBean != null) {
                    UMShareAction umShareAction = new UMShareAction(this
                            , ""
                            , getStrings(mShareBean.getTitle())
                            , getStrings(mShareBean.getDescription())
                            , getStrings(mShareBean.getUrl()), mShareBean.getRoutineUrl(), mShareBean.getObjId(), mShareBean.getShareType(), mShareBean.getPlatform());
                }

                break;
            case R.id.tv_publish_comment:
                if (userId > 0) {
                    if (VISIBLE == mLlInputComment.getVisibility()) {
                        commentViewVisible(GONE, null);
                    } else {
                        commentViewVisible(VISIBLE, null);
                    }
                } else {
                    getLoginStatus(ArticleOfficialActivity.this);
                }
                break;
            case R.id.tv_article_bottom_like:
                SoftApiDao.favorArtical(this, artId, mTvArticleBottomLike);
                break;
            case R.id.tv_article_bottom_collect:
                SoftApiDao.collectArtical(this, artId, mTvArticleBottomCollect);
                break;
            case R.id.smart_web_refresh:
                mRelCommunalNetError.setVisibility(View.GONE);
                if (isWebLinkUrl(errorUrl)) {
                    web_communal.loadUrl(errorUrl);
                } else {
                    web_communal.loadUrl(webUrl);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            if (requestCode == IS_LOGIN_CODE) {
                transmitUid();
                return;
            }
        }

        if (resultCode == RESULT_OK) {
            if (requestCode == IS_LOGIN_CODE) {
                transmitUid();
            }
        }
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    private void transmitUid() {
        webViewJs(getIntegralFormat(this, R.string.web_uid_method, userId));
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                InputMethodManager imm =
                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
        try {
            return super.dispatchTouchEvent(ev);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Return whether touch the view.
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v instanceof EditText) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (web_communal != null) {
            web_communal.removeAllViews();
            web_communal.destroy();
        }
    }

    @Override
    public View getTopView() {
        return web_communal;
    }

    //改写物理按键——返回的逻辑
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finishWebPage();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * 关闭web界面
     */
    private void finishWebPage() {
        runOnUiThread(() -> {
            //当前页面index
            int currentIndex = web_communal.copyBackForwardList().getCurrentIndex();
            if (web_communal.canGoBack()) {
                if (currentIndex - 1 < 0) {
                    finish();
                } else {
                    web_communal.goBackOrForward(-1);
                }
            } else {
                finish();
            }
        });

    }
}
