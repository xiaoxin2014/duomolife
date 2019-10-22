package com.amkj.dmsh.views;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

import me.jessyan.autosize.AutoSize;


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


    //修复fragment嵌入H5，无法正常适配
    @Override
    public void setOverScrollMode(int mode) {
        super.setOverScrollMode(mode);

        AutoSize.autoConvertDensityOfGlobal((Activity) getContext());
    }


    //修复viewpager嵌套webview时，webview嵌套横向滑动的组件和viewpager发生滑动冲突
    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        requestDisallowInterceptTouchEvent(false);//为false时表示父控件viewpager拦截子控件的事件，滑动交给父控件处理
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            requestDisallowInterceptTouchEvent(true);//为true时表示父控件viewpager不拦截子控件的事件，滑动交给子控件处理
            return true;
        }

        return true;
    }
}
