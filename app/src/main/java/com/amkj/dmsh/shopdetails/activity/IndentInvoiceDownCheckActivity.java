package com.amkj.dmsh.shopdetails.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.release.dialogutils.AlertSettingBean;
import com.amkj.dmsh.release.dialogutils.AlertView;
import com.amkj.dmsh.release.dialogutils.OnAlertItemClickListener;
import com.amkj.dmsh.utils.Log;
import com.amkj.dmsh.utils.inteface.MyProgressCallBack;

import java.io.File;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.showToast;

;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/4/25
 * class description:发票下载与查看
 */
public class IndentInvoiceDownCheckActivity extends BaseActivity {
    @BindView(R.id.tv_indent_title)
    TextView tv_indent_invoice_title;
    @BindView(R.id.iv_indent_search)
    ImageView iv_indent_search;
    @BindView(R.id.iv_indent_service)
    ImageView iv_indent_more;
    @BindView(R.id.pb_down_invoice)
    ProgressBar pb_down_invoice;
    @BindView(R.id.web_invoice)
    WebView web_invoice;
    private String invoiceImg;
    private AlertView downDialog;
    private String url = "http://www.domolife.cn/invoice.html?orderId=";
    private String orderId = "";
    @Override
    protected int getContentView() {
        return R.layout.activity_intent_invoice_down_check;
    }
    @Override
    protected void initViews() {
        iv_indent_search.setVisibility(View.GONE);
        tv_indent_invoice_title.setText("电子发票");
        iv_indent_more.setImageResource(R.drawable.invoice_down);
        Intent intent = getIntent();
        invoiceImg = intent.getStringExtra("invoiceImg");
        orderId = intent.getStringExtra("orderId");

        web_invoice.getSettings().setUseWideViewPort(true);
        web_invoice.getSettings().setLoadWithOverviewMode(true);
        web_invoice.getSettings().setUserAgentString(web_invoice.getSettings().getUserAgentString() + " domolifeandroid" + getRandomString(501));
        WebSettings webSettings = web_invoice.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(false);
        web_invoice.setWebChromeClient(new MyWebChromeClient());
        //加载需要显示的网页
        web_invoice.clearCache(true);
        web_invoice.loadUrl(/*url+orderId*/invoiceImg);
        /*web_invoice.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });*/
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

    @Override
    protected void loadData() {
    }

    //    下载发票
    @OnClick(R.id.iv_indent_service)
    void downInvoiceImg(View view) {
        pb_down_invoice.setVisibility(View.VISIBLE);
        iv_indent_more.setVisibility(View.GONE);
        String invoiceSavePath = Environment.getExternalStorageDirectory().getPath() + "/DownInvoice";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            createFilePath(invoiceSavePath);
        }
        if (!TextUtils.isEmpty(invoiceImg)) {
            invoiceSavePath = invoiceSavePath + "/" + invoiceImg.substring(invoiceImg.lastIndexOf("/"));
            if (fileIsExist(invoiceSavePath)) {
                final String finalInvoiceSavePath = invoiceSavePath;
                AlertSettingBean alertSettingBean = new AlertSettingBean();
                AlertSettingBean.AlertData alertData = new AlertSettingBean.AlertData();
                alertData.setCancelStr("取消");
                alertData.setDetermineStr("下载");
                alertData.setFirstDet(false);
                alertData.setTitle("文件已存在是否重新下载");
                alertSettingBean.setStyle(AlertView.Style.Alert);
                alertSettingBean.setAlertData(alertData);
                downDialog = new AlertView(alertSettingBean, this, new OnAlertItemClickListener() {
                    @Override
                    public void onAlertItemClick(Object o, int position) {
                        if (o.equals(downDialog) && position != AlertView.CANCELPOSITION) {
                            downLoadFile(finalInvoiceSavePath);
                        } else {
                            pb_down_invoice.setVisibility(View.GONE);
                            iv_indent_more.setVisibility(View.VISIBLE);
                        }
                    }
                });
                downDialog.setCancelable(false);
                downDialog.show();
            } else {
                downLoadFile(invoiceSavePath);
            }
        }
    }

    private void downLoadFile(String invoiceSavePath) {
        XUtil.DownLoadFile(invoiceImg, invoiceSavePath, new MyProgressCallBack<File>() {
            @Override
            public void onStarted() {
//                    showToast(IndentInvoiceDownCheckActivity.this, "正在下载");
            }

            @Override
            public void onSuccess(File result) {
                pb_down_invoice.setVisibility(View.GONE);
                iv_indent_more.setVisibility(View.VISIBLE);
                showToast(IndentInvoiceDownCheckActivity.this, "发票保存路径为：" + result.getAbsolutePath());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                pb_down_invoice.setVisibility(View.GONE);
                iv_indent_more.setVisibility(View.VISIBLE);
                showToast(IndentInvoiceDownCheckActivity.this, "下载失败");
            }
        });
    }

    //判断文件是否存在
    private boolean fileIsExist(String invoiceSavePath) {
        File file = new File(invoiceSavePath);
        return file.exists();
    }

    //创建文件夹
    private void createFilePath(String savePath) {
        File destDir = new File(savePath);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
    }

    @OnClick(R.id.tv_indent_back)
    void goBack(View view) {
        finish();
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
            TextView titleView = (TextView) LayoutInflater.from(IndentInvoiceDownCheckActivity.this).inflate(R.layout.layout_dialog_title_textview, null);
            titleView.setText("提示");
            builder.setCustomTitle(titleView)
                    .setMessage(message)
                    .setPositiveButton("确定", null);

            // 不需要绑定按键事件
            // 屏蔽keycode等于84之类的按键
            builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    Log.v("onJsAlert", "keyCode==" + keyCode + "event=" + event);
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
            TextView titleView = (TextView) LayoutInflater.from(IndentInvoiceDownCheckActivity.this).inflate(R.layout.layout_dialog_title_textview, null);
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
            TextView titleView = (TextView) LayoutInflater.from(IndentInvoiceDownCheckActivity.this).inflate(R.layout.layout_dialog_title_textview, null);
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
}
