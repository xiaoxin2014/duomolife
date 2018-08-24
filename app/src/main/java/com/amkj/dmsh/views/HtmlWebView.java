package com.amkj.dmsh.views;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/6/14
 * version 3.1.4
 * class description:webView监听滚动事件
 */
public class HtmlWebView extends WebView {

    private OnScrollChangedCallback mOnScrollChangedCallback;

    public HtmlWebView(Context context) {
        super(context);
    }

    public HtmlWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HtmlWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(final int l, final int t, final int oldl,
                                   final int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mOnScrollChangedCallback != null) {
            mOnScrollChangedCallback.onScroll(l, t);
        }
    }

    public OnScrollChangedCallback getOnScrollChangedCallback() {
        return mOnScrollChangedCallback;
    }

    public void setOnScrollChangedCallback(
            final OnScrollChangedCallback onScrollChangedCallback) {
        mOnScrollChangedCallback = onScrollChangedCallback;
    }

    /**
     * Impliment in the activity/fragment/view that you want to listen to the webview
     */
    public static interface OnScrollChangedCallback {
        public void onScroll(int dx, int dy);
    }
}
