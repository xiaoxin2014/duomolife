package com.amkj.dmsh.views;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;

/**
 * Created by xiaoxin on 2019/8/12
 * Version:v4.1.0
 * ClassDescription :自定义系统原生弹框
 */
public class MyWebChromeClient extends WebChromeClient {
    private Activity conetxt;
    private SmartRefreshLayout smart_web_refresh;
    private ProgressBar mPb;

    public MyWebChromeClient(Activity activity) {
        conetxt = activity;
    }


    public MyWebChromeClient(Activity activity, SmartRefreshLayout smartRefreshLayout, ProgressBar progressBar) {
        conetxt = activity;
        smart_web_refresh = smartRefreshLayout;
        mPb = progressBar;
    }


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
        TextView titleView = (TextView) LayoutInflater.from(conetxt).inflate(R.layout.layout_dialog_title_textview, null);
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
        TextView titleView = (TextView) LayoutInflater.from(conetxt).inflate(R.layout.layout_dialog_title_textview, null);
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
        TextView titleView = (TextView) LayoutInflater.from(conetxt).inflate(R.layout.layout_dialog_title_textview, null);
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
                return true;
            }
        });
        // 禁止响应按back键的事件
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        return true;
    }


    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        if (newProgress == 100 && mPb != null) {
            if (RefreshState.Refreshing.equals(smart_web_refresh.getState())) {
                smart_web_refresh.finishRefresh();
            }
            mPb.setVisibility(View.INVISIBLE);
        } else if (mPb != null) {
            if (View.INVISIBLE == mPb.getVisibility()) {
                mPb.setVisibility(View.VISIBLE);
            }
            mPb.setProgress(newProgress);
        }
    }

}
