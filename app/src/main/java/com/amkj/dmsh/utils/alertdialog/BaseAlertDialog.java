package com.amkj.dmsh.utils.alertdialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import butterknife.ButterKnife;
import me.jessyan.autosize.AutoSize;

import static com.amkj.dmsh.constant.ConstantMethod.isContextExisted;

/**
 * Created by xiaoxin on 2020/4/9
 * Version:v4.5.0
 */
public abstract class BaseAlertDialog extends AlertDialog {

    protected AppCompatActivity context;
    protected View dialogView;
    private boolean isFirstSet = true;

    protected BaseAlertDialog(@NonNull Context context) {
        super(context);

    }

    protected BaseAlertDialog(@NonNull AppCompatActivity context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        setCancelable(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        ButterKnife.bind(this);
        initViews();
    }


    protected abstract int getContentView();

    protected abstract void initViews();

    //调用了show方法才会执行onCreat对控件进行实例化，所以一定要先调用show方法才能对控件进行赋值，否则会报null异常
    public void show(int gravity) {
        if (!isShowing() && isContextExisted(context)) {
            AutoSize.autoConvertDensityOfGlobal(context);
            show();
        }
        if (isFirstSet) {
            Window window = getWindow();
            if (window != null) {
                window.setBackgroundDrawableResource(android.R.color.transparent);
                if (gravity > 0) {
                    // 获取自定义的Dialog所在的窗体的属性参数
                    WindowManager.LayoutParams layoutParams = window.getAttributes();
                    // 设置窗体沉在底部
                    layoutParams.gravity = Gravity.BOTTOM;
                    // 设置窗体宽度全屏
                    layoutParams.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
                    // 获取窗体顶层View对象，并设置内边距为0（保证顶层视图宽度全屏）
                    window.getDecorView().setPadding(0, 0, 0, 0);
                    // 提交窗体属性参数
                    window.setAttributes(layoutParams);
                }
            }
        }
        isFirstSet = false;
    }

    public void dismiss() {
        if (isShowing() && isContextExisted(context)) {
            super.dismiss();
        }
    }
}
